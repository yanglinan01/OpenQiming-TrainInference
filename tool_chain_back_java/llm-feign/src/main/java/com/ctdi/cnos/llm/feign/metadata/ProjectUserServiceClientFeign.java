package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserVO;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目空间用户关联信息表服务远程数据操作访问接口。
 *
 * @author 
 * @since 2025/06/05
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface ProjectUserServiceClientFeign {

    /**
     * 分页查询符合条件的项目空间用户关联信息表列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @PostMapping(value = "/projectUser/queryPage")
    OperateResult<PageResult<ProjectUserVO>> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 查询符合条件的项目空间用户关联信息表列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @PostMapping(value = "/projectUser/queryList")
    OperateResult<List<ProjectUserVO>> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查询指定项目空间用户关联信息表数据。
     *
     * @param id 指定项目空间用户关联信息表主键Id。
     * @return 单条数据。
     */
	@GetMapping(value = "/projectUser/queryById")
    OperateResult<ProjectUserVO> queryById(@RequestParam("id") Long id);

    /**
     * 添加项目空间用户关联信息表操作。
     *
     * @param projectUserDTO 添加项目空间用户关联信息表对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/projectUser/add")
    OperateResult<String> add(@RequestBody ProjectUserDTO projectUserDTO);
	
    /**
     * 更新项目空间用户关联信息表操作。
     *
     * @param projectUserDTO 更新项目空间用户关联信息表对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/projectUser/updateById")
    OperateResult<String> updateById(@RequestBody ProjectUserDTO projectUserDTO);

    /**
     * 删除指定的项目空间用户关联信息表。
     *
     * @param id 指定项目空间用户关联信息表主键Id。
     * @return 应答结果对象。
     */
    @DeleteMapping(value = "/projectUser/deleteById")
    OperateResult<String> deleteById(@RequestParam("id") Long id);

}