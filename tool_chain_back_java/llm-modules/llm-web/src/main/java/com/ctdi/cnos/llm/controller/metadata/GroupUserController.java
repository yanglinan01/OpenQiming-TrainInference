package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.beans.meta.group.GroupUser;
import com.ctdi.cnos.llm.beans.meta.group.GroupUserVO;
import com.ctdi.cnos.llm.feign.metadata.GroupUserServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户组_用户关系 控制器类。
 *
 * @author wangyb
 * @since 2024/09/23
 */
@Api(tags = "用户组_用户关系接口", value = "GroupUserController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/groupUser")
@Slf4j
public class GroupUserController {
	
    private final GroupUserServiceClientFeign groupUserServiceClientFeign;
	
    @ApiOperation(value = "查询用户组中的用户列表", notes = "查询用户组中的用户列表")
    @GetMapping(value = "/queryGroupUserIds")
    public OperateResult<List<GroupUserVO>> queryGroupUserIds() {

        try {
            List<GroupUserVO> vos = groupUserServiceClientFeign.queryGroupUserIds();
            return new OperateResult<>(true, null, vos);
        } catch (Exception e) {
            log.error("查询部署模型列表异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @ApiOperation(value = "根据用户组ID查找用户列表", notes = "根据用户组ID查找用户列表")
    @GetMapping(value = "/queryUserIdsByGroupId")
    public OperateResult<List<GroupUser>> queryUserIdsByGroupId(@ApiParam(value = "用户组ID", required = true, example = "1")
                     @NotNull(message = "用户组ID不能为空")@RequestParam("groupId") Long groupId) {
        try {
            List<GroupUser> groupUsers = groupUserServiceClientFeign.queryUserIdsByGroupId(groupId);
            return new OperateResult<>(true, null, groupUsers);
        } catch (Exception e) {
            log.error("查询部署模型列表异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


}
