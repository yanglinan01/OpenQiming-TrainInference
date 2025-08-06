package com.ctdi.cnos.llm.feign.metadata;


import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.group.GroupUser;
import com.ctdi.cnos.llm.beans.meta.group.GroupUserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author wangyb
 * @version 1.0.0
 * @ClassName GroupUserServiceClientFeign.java
 * @Description 用户组用户访问接口
 * @createTime 2024-5-17-10:55:00
 */

@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface GroupUserServiceClientFeign {


    /**
     * 查询用户组中的数据列表
     *
     * @return 用户组中的数据列表
     */
    @GetMapping(value = "/groupUser/queryGroupUserIds")
    List<GroupUserVO> queryGroupUserIds();


    /**
     * 根据用户组ID查找用户列表
     *
     * @param groupId 户组id
     * @return 用户列表
     */
    @GetMapping(value = "/groupUser/queryUserIdsByGroupId")
    List<GroupUser> queryUserIdsByGroupId(@RequestParam("groupId") Long groupId);



}
