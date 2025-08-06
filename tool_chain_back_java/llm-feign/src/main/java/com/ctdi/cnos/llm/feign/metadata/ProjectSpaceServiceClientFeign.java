package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectSpaceDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectSpaceVO;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 项目空间信息表服务远程数据操作访问接口。
 *
 * @author 
 * @since 2025/06/05
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface ProjectSpaceServiceClientFeign {

    /**
     * 分页查询符合条件的项目空间信息表列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @PostMapping(value = "/projectSpace/queryPage")
    OperateResult<PageResult<ProjectSpaceVO>> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 查询符合条件的项目空间信息表列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @PostMapping(value = "/projectSpace/queryList")
    OperateResult<List<ProjectSpaceVO>> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查询指定项目空间信息表数据。
     *
     * @param id 指定项目空间信息表主键Id。
     * @return 单条数据。
     */
	@GetMapping(value = "/projectSpace/queryById")
    OperateResult<ProjectSpaceVO> queryById(@RequestParam("id") Long id);

    /**
     * 添加项目空间信息表操作。
     *
     * @param projectSpaceDTO 添加项目空间信息表对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/projectSpace/add")
    OperateResult<String> add(@RequestBody ProjectSpaceDTO projectSpaceDTO);
	
    /**
     * 更新项目空间信息表操作。
     *
     * @param projectSpaceDTO 更新项目空间信息表对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/projectSpace/updateById")
    OperateResult<String> updateById(@RequestBody ProjectSpaceDTO projectSpaceDTO);

    /**
     * 删除指定的项目空间信息表。
     *
     * @param id 指定项目空间信息表主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/projectSpace/deleteById")
    OperateResult<String> deleteById(@RequestParam("id") Long id);

}