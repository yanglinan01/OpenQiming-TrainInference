package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.model.UserModelDTO;
import com.ctdi.cnos.llm.beans.meta.model.UserModelVO;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 用户模型关系表服务远程数据操作访问接口。
 *
 * @author wangyb
 * @since 2024/11/14
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface UserModelServiceClientFeign {

    @ApiOperation(value = "分页查询符合条件的用户模型关系表数据", notes = "分页查询符合条件的用户模型关系表数据")
    @PostMapping(value = "/userModel/queryPage")
    OperateResult<PageResult<UserModelVO>> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 查询符合条件的用户模型关系表列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @ApiOperation(value = "列表查询符合条件的用户模型关系表数据", notes = "列表查询符合条件的用户模型关系表数据")
    @PostMapping(value = "/userModel/queryList")
    OperateResult<List<UserModelVO>> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查询指定用户模型关系表数据。
     *
     * @param id 指定用户模型关系表主键Id。
     * @return 单条数据。
     */
    @ApiOperation(value = "查询指定ID的用户模型关系表数据", notes = "通过用户模型关系表ID获取具体的用户模型关系表数据")
    @GetMapping(value = "/userModel/queryById")
    OperateResult<UserModelVO> queryById(@RequestParam("id") Long id);

    /**
     * 添加用户模型关系表操作。
     *
     * @param userModelDTO 添加用户模型关系表对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "添加用户模型关系表对象", notes = "添加用户模型关系表对象")
    @PostMapping(value = "/userModel/add")
    OperateResult<String> add(@RequestBody UserModelDTO userModelDTO);


    /**
     * 批量修改用户绑定的模型
     *
     * @param modelIds 模型ID集合
     * @param userId   用户ID
     * @param usage    模型权限用途
     * @return OperateResult<String>
     */
    @ApiOperation(value = "批量修改用户绑定的模型", notes = "批量修改用户绑定的模型")
    @PostMapping(value = "/userModel/updateUserModel/{userId}/{usage}")
    OperateResult<String> updateUserModel(@RequestBody Set<Long> modelIds,
                                          @PathVariable("userId") Long userId,
                                          @PathVariable("usage") String usage);

    /**
     * 批量绑定模型-用户
     *
     * @param userModelDTO 绑定关系
     * @return OperateResult<String>
     */
    @ApiOperation(value = "批量绑定模型-用户", notes = "批量绑定模型-用户")
    @PostMapping(value = "/userModel/batchBinding")
    OperateResult<String> batchBinding(@RequestBody UserModelDTO userModelDTO);

    /**
     * 批量绑定推理和训推模型-用户
     *
     * @param userModelDTOList 绑定对象
     * @return OperateResult<String>
     */
    @ApiOperation(value = "批量绑定推理和训推模型-用户", notes = "批量绑定推理和训推模型-用户")
    @PostMapping(value = "/userModel/batchBindingAll")
    OperateResult<String> batchBindingAll(@RequestBody List<UserModelDTO> userModelDTOList);

    /**
     * 一键绑定模型-用户
     *
     * @param userModelDTO 绑定关系
     * @return OperateResult<String>
     */
    @ApiOperation(value = "一键绑定模型-用户", notes = "一键绑定模型-用户")
    @PostMapping(value = "/userModel/oneClickBinding")
    OperateResult<String> oneClickBinding(@RequestBody UserModelDTO userModelDTO);


    /**
     * 一键取消绑定模型-用户
     *
     * @param userModelDTO 绑定对象
     * @return OperateResult<String>
     */
    @ApiOperation(value = "一键取消绑定模型-用户", notes = "一键取消绑定模型-用户")
    @PostMapping(value = "/userModel/oneClickCancelBinding")
    OperateResult<String> oneClickCancelBinding(@RequestBody UserModelDTO userModelDTO);

    /**
     * 删除用户模型关系表
     *
     * @param userId 用户ID不能为空
     * @param modeId 模型ID不能为空
     * @param usage  模型权限用途
     * @return OperateResult<String>
     */
    @ApiOperation(value = "删除用户模型关系表", notes = "删除用户模型关系表")
    @GetMapping(value = "/userModel/delete/{userId}/{modeId}/{usage}")
    OperateResult<String> delete(@PathVariable("userId") Long userId,
                                 @PathVariable("modeId") Long modeId,
                                 @PathVariable("usage") String usage);


}