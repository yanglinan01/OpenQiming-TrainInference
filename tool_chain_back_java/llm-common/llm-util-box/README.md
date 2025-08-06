### 必知要点
---
在该文档中，我们将主要介绍开发和调试阶段，系统所依赖的服务组件的启动与控制台访问方式。

1. 代码生成器。
2. 基础核心类及代码生成器封装。数据字典转换等。

### 代码生成器模块
---
- [llm-code-generator-ui](..%2Fllm-code-generator-ui)
    - 启动com.ctdi.cnos.llm.code.CodeGeneratorUIServer。
    - 打开浏览器访问：http://localhost:8068
    - 生成代码：适配整个系统单表的CRUD代码。包括DTO、VO、FeignClient。
- 其它说明：
    - 模板定义：llm-common/llm-code-generator-ui/src/main/resources/com.ctdi.cnos.llm/template

### 系统公共服务模块
---
#### 数据权限使用
1. 首先在实体类中（即添加了@TableName("xxx")注解）的类。为字段添加@UserFilterColumn、@RegionFilterColumn。
```java
    /**
     * 创建者Id。
     */
    @UserFilterColumn
    @ApiModelProperty(value = "创建者Id", example = "0")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "creator_id")
    private Long creatorId;

    /**
     * 区域编码
     */
    @RegionFilterColumn
    @ApiModelProperty(value = "区域编码", example = "5000000000000000000")
    private String regionCode;
```
特别说明：当继承了BaseModel类。可不需要标注。或者在实体类中覆盖父类定义的@UserFilterColumn。
2. 其次，在Mapper(Dao)接口中。可在类上或方法上添加@InterceptorIgnore(dataPermission = "false")注解。表示启用数据权限过滤。
3. 启用数据权限生成规则, 见：DataPermRuleType(数据权限规则类型常量类)：
    - 当用户表的dataScope是TYPE_ALL(查看全部)。不添加过滤条件。
    - 当用户表的dataScope是TYPE_REGION_ONLY(仅查看当前区域)。添加regionCode过滤条件。a.region_code = ? OR a.creator_id = ? 
    - 当用户表的dataScope是TYPE_USER_ONLY(仅查看当前用户)。添加creatorId过滤条件。 a.creator_id = ?
#### 数据字典转换
1. 同样的，在实体类中添加：
```java
@RelationGlobalDict(masterIdField = "modelChatType", dictCode = "APPLICATION_TYPE")
@ApiModelProperty(value = "模型对话类型字典")
@TableField(exist = false)
private Map<String, Object> modelChatTypeDictMap;
```
2. VO视图层也需要包装字典数据。
```java
@ApiModelProperty(value = "模型对话类型字典")
private Map<String, Object> modelChatTypeDictMap;
```
3. 查询效果：
```json
{
  "id": "1816029693296902145",
  "modelChatType": 1,
  "modelChatTypeDictMap": {
    "name": "大模型",
    "id": 1
  }
}
```