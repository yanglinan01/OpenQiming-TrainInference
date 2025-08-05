package com.ctdi.cnos.llm.controller.announcement;

import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @Author hanfulei
 * @Date 2024/8/14 15:08
 * @Version 1.0
 **/
@Slf4j
@Api(tags = {"公告接口"})
@RequestMapping(value = "/web/announcement")
@RestController
@RefreshScope
public class AnnouncementController {

    @Value("${announcement.content}")
    private String content = "";

    @GetMapping("/getAnnouncementContent")
    public OperateResult getAnnouncement() {
        try {
            //将announcementConfigImpl中的content按照英文;分割然后放在一个list里
            List listRes = Arrays.asList(content.split(";"));
            return new OperateResult(true, "查询成功", listRes);
        } catch (Exception e) {
            log.error("查询异常", e);
            return new OperateResult(false, e.getMessage(), null);
        }
    }
}
