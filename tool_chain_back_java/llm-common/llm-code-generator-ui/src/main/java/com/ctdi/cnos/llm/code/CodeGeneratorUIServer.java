package com.ctdi.cnos.llm.code;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.ctdi.cnos.llm.code.config.GenCustomerConfig;
import com.ctdi.cnos.llm.code.config.SchemaEnum;
import com.github.davidfantasy.mybatisplus.generatorui.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.MybatisPlusToolsApplication;
import com.github.davidfantasy.mybatisplus.generatorui.mbp.NameConverter;
import com.github.davidfantasy.mybatisplus.generatorui.service.SqlGeneratorService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.github.davidfantasy.mybatisplus.generatorui.dto.Constant.DOT_JAVA;

/**
 * 代码生成器启动入口。
 *
 * @author laiqi
 * @see <a href="https://github.com/davidfantasy/mybatis-plus-generator-ui">提供交互式的Web UI用于生成兼容mybatis-plus框架的相关功能代码</a>
 * @see AbstractTemplateEngine#getObjectMap(ConfigBuilder, TableInfo)
 * @see SqlGeneratorService#genParamDtoFromSql(String, String, boolean)
 * @see AbstractTemplateEngine#getObjectMap(ConfigBuilder, TableInfo)
 * @since 2024/7/19
 */
public class CodeGeneratorUIServer {

    // TODO 1、代码生成的源码目录(可为空)
    public static final String SOURCE_PATH = "D:\\codes";

    public static void main(String[] args) {
        // TODO 2、数据库连接信息
//        String jdbcUrl = "jdbc:postgresql://116.205.191.112:5432/llm";
//        String userName = "postgres";
        System.out.println("在控制台输入数据库密码：");
//        String password = new Scanner(System.in).next();

        String jdbcUrl = "jdbc:postgresql://192.168.220.32:18921/llm";
        String userName = "llm_app";
        String password = new Scanner(System.in).next();
        // TODO 3、数据库模式
        SchemaEnum schema = SchemaEnum.META;

        // 接下来就是直接启动即可。
        String basePackage = "com.ctdi.cnos.llm";
        GeneratorConfig config = GeneratorConfig.builder().jdbcUrl(jdbcUrl)
                .userName(userName)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                // 数据库schema，MSSQL,PGSQL,ORACLE,DB2类型的数据库需要指定
                .schemaName(schema.getDbScheme())
                // 数据库表前缀，生成entity名称时会去掉(v2.0.3新增)
                .tablePrefix("mm_")
                // 如果需要修改entity及其属性的命名规则，以及自定义各类生成文件的命名规则，可自定义一个NameConverter实例，覆盖相应的名称转换方法，详细可查看该接口的说明：
                .nameConverter(new NameConverter() {
                    @Override
                    public String mapperNameConvert(String entityName) {
                        return entityName + "Dao";
                    }

                    @Override
                    public String mapperXmlNameConvert(String entityName) {
                        return mapperNameConvert(entityName);
                    }

                    @Override
                    public String serviceNameConvert(String entityName) {
                        return entityName + "Service";
                    }

                    /**
                     * 用户自定义生成文件的文件名
                     * @param fileType   用户自定义的文件类型
                     * @param entityName 数据表关联的entity的名称
                     * @return
                     */
                    @Override
                    public String customFileNameConvert(String fileType, String entityName) {
                        if ("FeignClient".equalsIgnoreCase(fileType)) {
                            return entityName + "ServiceClientFeign" + DOT_JAVA;
                        } else if ("FeignController".equalsIgnoreCase(fileType)) {
                            return entityName + "Controller" + DOT_JAVA;
                        }
                        return NameConverter.super.customFileNameConvert(fileType, entityName) + DOT_JAVA;
                    }
                })
                // 注入自定义模板参数
                .templateVaribleInjecter(tableInfo -> {

                    Map<String, Object> params = new HashMap<>();
                    // 主键生成的策略
                    params.put("idType", "ASSIGN_ID");
                    params.put("entityInstanceName", StrUtil.lowerFirst(tableInfo.getEntityName()));

                    GenCustomerConfig dtoConfig = new GenCustomerConfig();
                    dtoConfig.setClassName(tableInfo.getEntityName() + "DTO");
                    dtoConfig.setSerialVersionUID(false);
                    // dtoConfig.setPackageName(dtoPackage);
                    params.put("dtoConfig", dtoConfig);

                    GenCustomerConfig voConfig = ObjUtil.cloneByStream(dtoConfig);
                    voConfig.setClassName(tableInfo.getEntityName() + "VO");
                    voConfig.setSuperClass("BaseVo");
                    params.put("voConfig", voConfig);

                    GenCustomerConfig feignClientConfig = new GenCustomerConfig();
                    feignClientConfig.setPackageName(basePackage + ".feign." + schema.getModulePackage());
                    feignClientConfig.setClassName(tableInfo.getEntityName() + "ServiceClientFeign");
                    feignClientConfig.setSuperClass("BaseClient");
                    feignClientConfig.setFeignClientRef(schema.getFeignClientRef());
                    params.put("feignClientConfig", feignClientConfig);

                    GenCustomerConfig feignControllerConfig = new GenCustomerConfig();
                    feignControllerConfig.setPackageName(basePackage + ".controller." + schema.getModulePackage());
                    feignControllerConfig.setClassName(tableInfo.getEntityName() + "Controller");
                    params.put("feignControllerConfig", feignControllerConfig);
                    return params;
                })
                // 所有生成的java文件的父包名，后续也可单独在界面上设置
                .basePackage(basePackage)
                .port(8068)
                .build();
        MybatisPlusToolsApplication.run(config);
        System.out.println("http://localhost:8068");
    }
}