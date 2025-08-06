package com.ctdi.cnos.llm.base.constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.ctdi.cnos.llm.base.annotation.RegionFilterColumn;
import com.ctdi.cnos.llm.base.annotation.UserFilterColumn;
import com.ctdi.cnos.llm.base.object.BaseModel;
import com.ctdi.cnos.llm.base.object.DataPermRuleType;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.util.MybatisPlusUtil;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据权限规则定义。
 *
 * @author laiqi
 * @since 2024/7/26
 */
public class DataPermissionRule {

    /**
     * 基于区域的表字段配置
     * 一般情况下，每个表的区域编号字段是 region_code，通过该配置自定义。
     * <p>
     * key：表名
     * value：字段名
     */
    private static final Map<String, String> REGION_COLUMNS = new HashMap<>();
    /**
     * 基于用户的表字段配置
     * 一般情况下，每个表的用户字段是 dept_id，通过该配置自定义。
     * <p>
     * key：表名
     * value：字段名
     */
    private static final Map<String, String> USER_COLUMNS = new HashMap<>();
    /**
     * 所有表名，是 {@link #REGION_COLUMNS} 和 {@link #USER_COLUMNS} 的合集
     */
    private static final Set<String> TABLE_NAMES = new HashSet<>();

    public static Set<String> getTableNames() {
        return TABLE_NAMES;
    }

    public static void loadInfoWithDataPermission() {
        List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
        for (TableInfo tableInfo : tableInfos) {
            List<TableFieldInfo> fieldList = tableInfo.getFieldList();
            // 为了覆盖继承情况. 需要采用倒序遍历(即优先处理父类的, 子类的属性可以覆盖)
            for (int i = fieldList.size() - 1; i >= 0; i--) {
                TableFieldInfo tableFieldInfo = fieldList.get(i);
                // 数据权限过滤的列。
                if (null != tableFieldInfo.getField().getAnnotation(UserFilterColumn.class)) {
                    addUserColumn(tableInfo.getTableName(), tableFieldInfo.getColumn());
                }
                if (null != tableFieldInfo.getField().getAnnotation(RegionFilterColumn.class)) {
                    addRegionColumn(tableInfo.getTableName(), tableFieldInfo.getColumn());
                }
            }
        }
    }

    public static void addUserColumn(Class<?> entityClass) {
        addUserColumn(entityClass, BaseModel.CREATE_USER_ID_NAME);
    }

    public static void addUserColumn(Class<?> entityClass, String columnName) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        addUserColumn(tableName, columnName);
    }

    public static void addUserColumn(String tableName, String columnName) {
        USER_COLUMNS.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }

    public static void addRegionColumn(Class<?> entityClass) {
        addRegionColumn(entityClass, BaseModel.CREATE_USER_ID_NAME);
    }

    public static void addRegionColumn(Class<?> entityClass, String columnName) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        addRegionColumn(tableName, columnName);
    }

    public static void addRegionColumn(String tableName, String columnName) {
        REGION_COLUMNS.put(tableName, columnName);
        TABLE_NAMES.add(tableName);
    }

    public static Expression getExpression(String tableName, Alias tableAlias) {
        UserVO loginUser = UserContextHolder.getUser();
        if (loginUser == null) {
            return null;
        }
        String dataScope = ObjUtil.defaultIfNull(loginUser.getRole(), DataPermRuleType.TYPE_USER_ONLY);
        // 只有非管理员类型的用户，才进行数据权限的处理
        if (DataPermRuleType.TYPE_ALL.equals(dataScope)) {
            return null;
        }
        // 情况三，拼接 Region 和 User 的条件，最后组合
        Expression regionExpression = buildRegionExpression(tableName, tableAlias, loginUser.getRegionCode());
        // 仅查看当前区域 仅查看当前用户 都进行拼接
        boolean buildUser = DataPermRuleType.TYPE_REGION_ONLY.equals(dataScope) || DataPermRuleType.TYPE_USER_ONLY.equals(dataScope);
        Expression userExpression = buildUserExpression(tableName, tableAlias, buildUser, loginUser.getId());
        if (regionExpression == null) {
            return userExpression;
        }
        if (userExpression == null) {
            return regionExpression;
        }
        // 目前，如果有指定部门 + 可查看自己，采用 OR 条件。即，WHERE (region_code = ? OR user_id = ?)
        return new Parenthesis(new OrExpression(regionExpression, userExpression));
    }

    private static Expression buildRegionExpression(String tableName, Alias tableAlias, Set<String> regionCodes) {
        // 如果不存在配置，则无需作为条件
        String columnName = REGION_COLUMNS.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 如果为空，则无条件
        if (CollUtil.isEmpty(regionCodes)) {
            return null;
        }
        // 拼接条件
        return new InExpression(MybatisPlusUtil.buildColumn(tableName, tableAlias, columnName),
                new ExpressionList(regionCodes.stream().map(LongValue::new).collect(Collectors.toList())));
    }

    private static Expression buildRegionExpression(String tableName, Alias tableAlias, String regionCode) {
        // 如果不存在配置，则无需作为条件
        String columnName = REGION_COLUMNS.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 拼接条件
        return new EqualsTo(MybatisPlusUtil.buildColumn(tableName, tableAlias, columnName), new StringValue(regionCode));
    }

    private static Expression buildUserExpression(String tableName, Alias tableAlias, Boolean self, Long userId) {
        // 如果不查看自己，则无需作为条件
        if (Boolean.FALSE.equals(self)) {
            return null;
        }
        String columnName = USER_COLUMNS.get(tableName);
        if (StrUtil.isEmpty(columnName)) {
            return null;
        }
        // 拼接条件
        return new EqualsTo(MybatisPlusUtil.buildColumn(tableName, tableAlias, columnName), new LongValue(userId));
    }
}