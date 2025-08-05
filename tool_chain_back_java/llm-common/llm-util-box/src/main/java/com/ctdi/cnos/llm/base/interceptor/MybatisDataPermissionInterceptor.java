package com.ctdi.cnos.llm.base.interceptor;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.ctdi.cnos.llm.base.constant.DataPermissionRule;
import com.ctdi.cnos.llm.context.ContextHolder;
import com.ctdi.cnos.llm.util.MybatisPlusUtil;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据权限处理器。
 *
 * @author laiqi
 * @since 2024/7/26
 */
public class MybatisDataPermissionInterceptor extends DataPermissionInterceptor {

    /**
     * SQL 解析缓存
     * key 可能是 mappedStatement 的 ID,也可能是 class 的 name
     */
    private static Map<String, InterceptorIgnoreHelper.InterceptorIgnoreCache> INTERCEPTOR_IGNORE_CACHE = new ConcurrentHashMap<>();

    /**
     * 构造器。
     * 反射获取私有数据。
     */
    public MybatisDataPermissionInterceptor() {
        INTERCEPTOR_IGNORE_CACHE = (Map<String, InterceptorIgnoreHelper.InterceptorIgnoreCache>) ReflectUtil.getFieldValue(InterceptorIgnoreHelper.class, "INTERCEPTOR_IGNORE_CACHE");
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        Object obj = ContextHolder.get();
        if (obj instanceof InterceptorIgnore) {
            InterceptorIgnore ignore = (InterceptorIgnore) obj;
            if (MybatisPlusUtil.getBoolean("dataPermission", ms.getId(), ignore.dataPermission())) {
                return;
            }
        }
        else {
            // 自带的InterceptorIgnoreHelper此处有一个坑。正常来说，不添加注解的时候就不生效。而不是默认就生效。
            // 因此重新定义InterceptorIgnoreHelper的该方法。
            if (willIgnoreDataPermission(ms.getId())) {
                return;
            }
        }
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(parserSingle(mpBs.sql(), ms.getId()));
    }

    public static boolean willIgnoreDataPermission(String id) {
        return willIgnore(id, InterceptorIgnoreHelper.InterceptorIgnoreCache::getDataPermission);
    }

    public static boolean willIgnore(String id, java.util.function.Function<InterceptorIgnoreHelper.InterceptorIgnoreCache, Boolean> function) {
        InterceptorIgnoreHelper.InterceptorIgnoreCache cache = INTERCEPTOR_IGNORE_CACHE.get(id);
        if (cache == null) {
            cache = INTERCEPTOR_IGNORE_CACHE.get(id.substring(0, id.lastIndexOf(StringPool.DOT)));
        }
        if (cache != null) {
            Boolean apply = function.apply(cache);
            return apply != null && apply;
        }
        // 改动处：搬运InterceptorIgnoreHelper的方法。默认返回 false：表示默认情况下不应该忽略该操作，即需要进行数据权限。
        return true;
    }

    @Override
    protected void setWhere(PlainSelect plainSelect, String whereSegment) {
        processPlainSelect(plainSelect);
    }

    /**
     * 处理 PlainSelect
     */
    protected void processPlainSelect(PlainSelect plainSelect) {
        //#3087 github
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        if (CollectionUtils.isNotEmpty(selectItems)) {
            selectItems.forEach(this::processSelectItem);
        }

        // 处理 where 中的子查询
        Expression where = plainSelect.getWhere();
        processWhereSubSelect(where);

        // 处理 fromItem
        FromItem fromItem = plainSelect.getFromItem();
        List<Table> list = processFromItem(fromItem);
        List<Table> mainTables = new ArrayList<>(list);

        // 处理 join
        List<Join> joins = plainSelect.getJoins();
        if (CollectionUtils.isNotEmpty(joins)) {
            mainTables = processJoins(mainTables, joins);
        }

        // 当有 mainTable 时，进行 where 条件追加
        if (CollectionUtils.isNotEmpty(mainTables)) {
            plainSelect.setWhere(builderExpression(where, mainTables));
        }
    }

    private List<Table> processFromItem(FromItem fromItem) {
        // 处理括号括起来的表达式
        while (fromItem instanceof ParenthesisFromItem) {
            fromItem = ((ParenthesisFromItem) fromItem).getFromItem();
        }

        List<Table> mainTables = new ArrayList<>();
        // 无 join 时的处理逻辑
        if (fromItem instanceof Table) {
            Table fromTable = (Table) fromItem;
            mainTables.add(fromTable);
        } else if (fromItem instanceof SubJoin) {
            // SubJoin 类型则还需要添加上 where 条件
            List<Table> tables = processSubJoin((SubJoin) fromItem);
            mainTables.addAll(tables);
        } else {
            // 处理下 fromItem
            processOtherFromItem(fromItem);
        }
        return mainTables;
    }

    /**
     * 处理where条件内的子查询
     * <p>
     * 支持如下:
     * 1. in
     * 2. =
     * 3. >
     * 4. <
     * 5. >=
     * 6. <=
     * 7. <>
     * 8. EXISTS
     * 9. NOT EXISTS
     * <p>
     * 前提条件:
     * 1. 子查询必须放在小括号中
     * 2. 子查询一般放在比较操作符的右边
     *
     * @param where where 条件
     */
    protected void processWhereSubSelect(Expression where) {
        if (where == null) {
            return;
        }
        if (where instanceof FromItem) {
            processOtherFromItem((FromItem) where);
            return;
        }
        if (where.toString().indexOf("SELECT") > 0) {
            // 有子查询
            if (where instanceof BinaryExpression) {
                // 比较符号 , and , or , 等等
                BinaryExpression expression = (BinaryExpression) where;
                processWhereSubSelect(expression.getLeftExpression());
                processWhereSubSelect(expression.getRightExpression());
            } else if (where instanceof InExpression) {
                // in
                InExpression expression = (InExpression) where;
                Expression inExpression = expression.getRightExpression();
                if (inExpression instanceof SubSelect) {
                    processSelectBody(((SubSelect) inExpression).getSelectBody());
                }
            } else if (where instanceof ExistsExpression) {
                // exists
                ExistsExpression expression = (ExistsExpression) where;
                processWhereSubSelect(expression.getRightExpression());
            } else if (where instanceof NotExpression) {
                // not exists
                NotExpression expression = (NotExpression) where;
                processWhereSubSelect(expression.getExpression());
            } else if (where instanceof Parenthesis) {
                Parenthesis expression = (Parenthesis) where;
                processWhereSubSelect(expression.getExpression());
            }
        }
    }

    protected void processSelectItem(SelectItem selectItem) {
        if (selectItem instanceof SelectExpressionItem) {
            SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
            if (selectExpressionItem.getExpression() instanceof SubSelect) {
                processSelectBody(((SubSelect) selectExpressionItem.getExpression()).getSelectBody());
            } else if (selectExpressionItem.getExpression() instanceof Function) {
                processFunction((Function) selectExpressionItem.getExpression());
            }
        }
    }

    /**
     * 处理函数
     * <p>支持: 1. select fun(args..) 2. select fun1(fun2(args..),args..)<p>
     * <p> fixed gitee pulls/141</p>
     *
     * @param function
     */
    protected void processFunction(Function function) {
        ExpressionList parameters = function.getParameters();
        if (parameters != null) {
            parameters.getExpressions().forEach(expression -> {
                if (expression instanceof SubSelect) {
                    processSelectBody(((SubSelect) expression).getSelectBody());
                } else if (expression instanceof Function) {
                    processFunction((Function) expression);
                }
            });
        }
    }

    /**
     * 处理子查询等
     */
    protected void processOtherFromItem(FromItem fromItem) {
        // 去除括号
        while (fromItem instanceof ParenthesisFromItem) {
            fromItem = ((ParenthesisFromItem) fromItem).getFromItem();
        }

        if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {
            logger.debug("Perform a subQuery, if you do not give us feedback");
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
    }

    /**
     * 处理 sub join
     *
     * @param subJoin subJoin
     * @return Table subJoin 中的主表
     */
    private List<Table> processSubJoin(SubJoin subJoin) {
        List<Table> mainTables = new ArrayList<>();
        if (subJoin.getJoinList() != null) {
            List<Table> list = processFromItem(subJoin.getLeft());
            mainTables.addAll(list);
            mainTables = processJoins(mainTables, subJoin.getJoinList());
        }
        return mainTables;
    }

    /**
     * 处理 joins
     *
     * @param mainTables 可以为 null
     * @param joins      join 集合
     * @return List<Table> 右连接查询的 Table 列表
     */
    private List<Table> processJoins(List<Table> mainTables, List<Join> joins) {
        // join 表达式中最终的主表
        Table mainTable = null;
        // 当前 join 的左表
        Table leftTable = null;

        if (mainTables == null) {
            mainTables = new ArrayList<>();
        } else if (mainTables.size() == 1) {
            mainTable = mainTables.get(0);
            leftTable = mainTable;
        }

        // 对于 on 表达式写在最后的 join，需要记录下前面多个 on 的表名
        Deque<List<Table>> onTableDeque = new LinkedList<>();
        for (Join join : joins) {
            // 处理 on 表达式
            FromItem joinItem = join.getRightItem();

            // 获取当前 join 的表，subJoint 可以看作是一张表
            List<Table> joinTables = null;
            if (joinItem instanceof Table) {
                joinTables = new ArrayList<>();
                joinTables.add((Table) joinItem);
            } else if (joinItem instanceof SubJoin) {
                joinTables = processSubJoin((SubJoin) joinItem);
            }

            if (joinTables != null) {

                // 如果是隐式内连接
                if (join.isSimple()) {
                    mainTables.addAll(joinTables);
                    continue;
                }

                // 当前表是否忽略
                Table joinTable = joinTables.get(0);

                List<Table> onTables = null;
                // 如果不要忽略，且是右连接，则记录下当前表
                if (join.isRight()) {
                    mainTable = joinTable;
                    if (leftTable != null) {
                        onTables = Collections.singletonList(leftTable);
                    }
                } else if (join.isLeft()) {
                    onTables = Collections.singletonList(joinTable);
                } else if (join.isInner()) {
                    if (mainTable == null) {
                        onTables = Collections.singletonList(joinTable);
                    } else {
                        onTables = Arrays.asList(mainTable, joinTable);
                    }
                    mainTable = null;
                }

                mainTables = new ArrayList<>();
                if (mainTable != null) {
                    mainTables.add(mainTable);
                }

                // 获取 join 尾缀的 on 表达式列表
                Collection<Expression> originOnExpressions = join.getOnExpressions();
                // 正常 join on 表达式只有一个，立刻处理
                if (originOnExpressions.size() == 1 && onTables != null) {
                    List<Expression> onExpressions = new LinkedList<>();
                    onExpressions.add(builderExpression(originOnExpressions.iterator().next(), onTables));
                    join.setOnExpressions(onExpressions);
                    leftTable = joinTable;
                    continue;
                }
                // 表名压栈，忽略的表压入 null，以便后续不处理
                onTableDeque.push(onTables);
                // 尾缀多个 on 表达式的时候统一处理
                if (originOnExpressions.size() > 1) {
                    Collection<Expression> onExpressions = new LinkedList<>();
                    for (Expression originOnExpression : originOnExpressions) {
                        List<Table> currentTableList = onTableDeque.poll();
                        if (CollectionUtils.isEmpty(currentTableList)) {
                            onExpressions.add(originOnExpression);
                        } else {
                            onExpressions.add(builderExpression(originOnExpression, currentTableList));
                        }
                    }
                    join.setOnExpressions(onExpressions);
                }
                leftTable = joinTable;
            } else {
                processOtherFromItem(joinItem);
                leftTable = null;
            }
        }

        return mainTables;
    }


    /**
     * 处理条件
     *
     * @param currentExpression 当前 where 条件
     * @param tables            多个表
     */
    protected Expression builderExpression(Expression currentExpression, List<Table> tables) {
        // 没有表需要处理直接返回
        if (CollectionUtils.isEmpty(tables)) {
            return currentExpression;
        }

        // 第一步，获得 Table 对应的数据权限条件
        Expression dataPermissionExpression = null;
        for (Table table : tables) {
            // 构建每个表的权限 Expression 条件
            Expression expression = buildDataPermissionExpression(table);
            if (expression == null) {
                continue;
            }
            // 合并到 dataPermissionExpression 中
            dataPermissionExpression = dataPermissionExpression == null ? expression
                    : new AndExpression(dataPermissionExpression, expression);
        }

        // 第二步，合并多个 Expression 条件
        if (dataPermissionExpression == null) {
            return currentExpression;
        }
        if (currentExpression == null) {
            return dataPermissionExpression;
        }
        // ① 如果表达式为 Or，则需要 (currentExpression) AND dataPermissionExpression
        if (currentExpression instanceof OrExpression) {
            return new AndExpression(new Parenthesis(currentExpression), dataPermissionExpression);
        }
        // ② 如果表达式为 And，则直接返回 where AND dataPermissionExpression
        return new AndExpression(currentExpression, dataPermissionExpression);
    }

    /**
     * 构建指定表的数据权限的 Expression 过滤条件
     *
     * @param table 表
     * @return Expression 过滤条件
     */
    private Expression buildDataPermissionExpression(Table table) {
        // 判断表名是否匹配
        String tableName = MybatisPlusUtil.getTableName(table);
        if (!DataPermissionRule.getTableNames().contains(tableName)) {
            return null;
        }
        // 单条规则的条件
        return DataPermissionRule.getExpression(tableName, table.getAlias());
    }

    protected void processSelectBody(SelectBody selectBody) {
        if (selectBody == null) {
            return;
        }
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            processSelectBody(withItem.getSubSelect().getSelectBody());
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            List<SelectBody> selectBodyList = operationList.getSelects();
            if (CollectionUtils.isNotEmpty(selectBodyList)) {
                selectBodyList.forEach(this::processSelectBody);
            }
        }
    }

}