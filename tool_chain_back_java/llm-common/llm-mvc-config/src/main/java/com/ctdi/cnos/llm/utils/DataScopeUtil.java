/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.utils;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.system.user.service.UserService;
import com.ctdi.cnos.llm.system.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;

import java.util.Objects;

/**
 * 数据权限工具类
 *
 * @author huangjinhua
 * @since 2024/7/24
 */
@Slf4j
public class DataScopeUtil {
    /**
     * 获取当前用户权限sql
     *
     * @param tableAlias 表别名
     * @return String
     */
    public static String dataScopeSql(String tableAlias) {
        String userColumns = SystemConstant.DEFAULT_USER_COLUMNS;
        String regionColumns = SystemConstant.DEFAULT_REGION_COLUMNS;
        return buildDataScope(tableAlias, userColumns, regionColumns);
    }

    /**
     * 获取当前用户权限sql （表支持region_code,且默认为region_code）
     *
     * @param tableAlias  表别名
     * @param userColumns 限制用户字段范围的字段名称 默认为creator_id
     * @return String
     */
    public static String dataScopeSql(String tableAlias, String userColumns) {
        if (Objects.isNull(userColumns)) {
            userColumns = SystemConstant.DEFAULT_USER_COLUMNS;
        }
        String regionColumns = SystemConstant.DEFAULT_REGION_COLUMNS;
        return dataScopeSql(tableAlias, userColumns, regionColumns);
    }


    /**
     * 获取当前用户权限sql
     *
     * @param tableAlias    表别名
     * @param regionColumns 限制区域字段范围的字段名称 默认为region_code
     * @param userColumns   限制用户字段范围的字段名称 默认为creator_id
     * @return String
     */
    public static String dataScopeSql(String tableAlias, String userColumns, String regionColumns) {
        if (Objects.isNull(userColumns)) {
            userColumns = SystemConstant.DEFAULT_USER_COLUMNS;
        }
        if (Objects.isNull(regionColumns)) {
            regionColumns = SystemConstant.DEFAULT_REGION_COLUMNS;
        }
        return buildDataScope(tableAlias, userColumns, regionColumns);
    }

    /**
     * 获取当前用户权限sql
     *
     * @param tableAlias     表别名
     * @param regionColumns  限制区域字段范围的字段名称 默认为region_code
     * @param projectColumns 限制项目字段范围的字段名称 默认为project_id
     * @param projectId      projectId值
     * @return String
     */
    public static String dataProjectScopeSql(String tableAlias, String projectColumns, String regionColumns, Object projectId) {
        if (Objects.isNull(regionColumns)) {
            regionColumns = SystemConstant.DEFAULT_REGION_COLUMNS;
        }
        if (Objects.isNull(projectColumns)) {
            projectColumns = SystemConstant.DEFAULT_PROJECT_COLUMNS;
        }
        return buildProjectDataScope(tableAlias, projectColumns, regionColumns, projectId);
    }

    /**
     * 获取当前用户权限sql（表不支持region_code）
     *
     * @param tableAlias  表别名
     * @param userColumns 过滤字段 默认为creator_id
     * @return String
     */
    public static String dataScopeSqlWithoutRegion(String tableAlias, String userColumns) {
        if (Objects.isNull(userColumns)) {
            userColumns = SystemConstant.DEFAULT_USER_COLUMNS;
        }
        return buildDataScope(tableAlias, userColumns, null);
    }

    /**
     * 获取当前用户权限sql
     *
     * @param tableAlias    表别名
     * @param regionColumns 限制区域字段范围的字段名称 默认为region_code
     * @param userColumns   限制用户字段范围的字段名称 默认为creator_id
     * @return String
     */
    private static String buildDataScope(String tableAlias, String userColumns, String regionColumns) {
        // 获取 UserDetails用户信息
        UserVO userVO = getCurrentUser();
        if (userVO == null) {
            return null;
        }
        //用户角色，1管理员，2区域管理员，3普通用户
        String scope = userVO.getRole();
        if (CharSequenceUtil.isBlank(scope)) {
            scope = SystemConstant.USER_ORDINARY_CODE;
        }
        Expression expression = buildDataScopeExpression(tableAlias, scope, regionColumns, userVO.getRegionCode(), userColumns, userVO.getId());
        return Objects.nonNull(expression) ? " and (" + expression + ")" : null;
    }

    /**
     * 获取当前项目权限sql
     *
     * @param tableAlias     表别名
     * @param regionColumns  限制区域字段范围的字段名称 默认为region_code
     * @param projectColumns 限制项目字段范围的字段名称 默认为creator_id
     * @param projectId
     * @return String
     */
    private static String buildProjectDataScope(String tableAlias, String projectColumns, String regionColumns, Object projectId) {
        // 获取 UserDetails用户信息
        UserVO userVO = getCurrentUser();
        if (userVO == null) {
            return null;
        }
        //用户角色，1管理员，2区域管理员，3普通用户
        String scope = userVO.getRole();
        if (CharSequenceUtil.isBlank(scope)) {
            scope = SystemConstant.USER_ORDINARY_CODE;
        }
        Expression expression = buildProjectDataScopeExpression(tableAlias, scope, regionColumns, userVO.getRegionCode(), projectColumns, projectId);
        return Objects.nonNull(expression) ? " and (" + expression + ")" : null;
    }

    /**
     * 拼装数据权限
     *
     * @param tableAlias    表别名
     * @param scope         限制范围，1管理员，2区域管理员，3普通用户
     * @param regionColumns 限制区域字段范围的字段名称
     * @param regionCode    部门编码
     * @param userColumns   限制用户字段范围的字段名称
     * @param userId        本人ID
     * @return Expression
     */
    private static Expression buildDataScopeExpression(String tableAlias, String scope, String regionColumns, String regionCode,
                                                       String userColumns, Long userId) {
        if (CharSequenceUtil.equals(scope, SystemConstant.USER_ADMIN_CODE)) {
            //1管理员
            return null;
        } else if (CharSequenceUtil.equals(scope, SystemConstant.USER_REGION_LEADER_CODE)) {
            //2区域管理员  区域相同或当前用户
            EqualsTo userExpression = null;
            EqualsTo regionExpression = null;
            if (userId != null) {
                userExpression = new EqualsTo();
                userExpression.withLeftExpression(buildColumn(tableAlias, userColumns));
                userExpression.setRightExpression(new LongValue(userId));
            }
            if (CharSequenceUtil.isNotBlank(regionCode)) {
                regionExpression = new EqualsTo();
                regionExpression.withLeftExpression(buildColumn(tableAlias, regionColumns));
                regionExpression.setRightExpression(new StringValue(regionCode));
            }
            return orExpression(userExpression, regionExpression);
        } else {
            //3普通用户
            EqualsTo expression = null;
            if (userId != null) {
                expression = new EqualsTo();
                expression.withLeftExpression(buildColumn(tableAlias, userColumns));
                expression.setRightExpression(new LongValue(userId));
            }
            return expression;
        }
    }


    private static Expression buildProjectDataScopeExpression(String tableAlias, String scope, String regionColumns, String regionCode,
                                                              String projectColumns, Object projectId) {
        Expression id;
        if (projectId instanceof Long) {
            id = new LongValue(projectId.toString());
        } else if (projectId instanceof Double) {
            id = new DoubleValue(projectId.toString());
        } else if (projectId instanceof String) {
            id = new StringValue(projectId.toString());
        } else {
            id = new StringValue(projectId.toString());
        }
        if (CharSequenceUtil.equals(scope, SystemConstant.USER_ADMIN_CODE)) {
            //1 管理员
            EqualsTo expression = null;
            if (projectId != null) {
                expression = new EqualsTo();
                expression.withLeftExpression(buildColumn(tableAlias, projectColumns));
                expression.setRightExpression(id);
            }
            return expression;
        } else if (CharSequenceUtil.equals(scope, SystemConstant.USER_REGION_LEADER_CODE)) {
            //2 区域管理员  区域相同或当前用户
            EqualsTo expression = null;
            if (projectId != null) {
                expression = new EqualsTo();
                expression.withLeftExpression(buildColumn(tableAlias, projectColumns));
                expression.setRightExpression(id);
            }
            return expression;
        } else {
            //3 普通用户
            EqualsTo expression = null;
            if (projectId != null) {
                expression = new EqualsTo();
                expression.withLeftExpression(buildColumn(tableAlias, projectColumns));
                expression.setRightExpression(id);
            }
            return expression;
        }
    }


    private static UserVO getCurrentUser() {
        // 获取 UserDetails用户信息
        UserVO userVO = UserContextHolder.getUser();
        if (userVO == null) {
            return null;
        }
        //用户角色，1管理员，2区域管理员，3普通用户
        if (CharSequenceUtil.isBlank(userVO.getRole())) {
            UserService userService = SpringUtil.getBean(UserServiceImpl.class);
            UserVO user = userService.queryById(userVO.getId(), false);
            if (user != null) {
                userVO.setRole(user.getRole());
            }
        }
        return userVO;
    }


    /**
     * 构建Column
     *
     * @param tableAlias 表别名
     * @param columnName 字段名称
     * @return 带表别名字段
     */
    public static Column buildColumn(String tableAlias, String columnName) {
        if (CharSequenceUtil.isNotEmpty(tableAlias)) {
            columnName = tableAlias + "." + columnName;
        }
        return new Column(columnName);
    }

    public static Expression andExpression(Expression orginExpression, Expression newExpression) {
        if (Objects.nonNull(orginExpression) && Objects.nonNull(newExpression)) {
            return new AndExpression(orginExpression, newExpression);
        } else if (Objects.nonNull(orginExpression)) {
            return new Parenthesis(orginExpression);
        } else if (Objects.nonNull(newExpression)) {
            return new Parenthesis(newExpression);
        }
        return null;

    }

    public static Expression orExpression(Expression orginExpression, Expression newExpression) {
        if (Objects.nonNull(orginExpression) && Objects.nonNull(newExpression)) {
            return new OrExpression(orginExpression, newExpression);
        } else if (Objects.nonNull(orginExpression)) {
            return new Parenthesis(orginExpression);
        } else if (Objects.nonNull(newExpression)) {
            return new Parenthesis(newExpression);
        }
        return null;
    }


}