package com.ctdi.llmtc.xtp.traininfer.controller;

import org.springframework.web.bind.annotation.*;


/**
 * 数据集对外接口控制器类。
 *
 */
@RestController
@RequestMapping("/dataset")
@Deprecated
public class DataSetController {

    @PostMapping("/upload")
    public String uploadDataSet() {
        return "success";
    }

    @GetMapping("/delete/{filename}")
    public String deleteDataSet(@PathVariable("filename") String fileName) {
        // 文件名合法性校验（示例：防止路径穿越）

        // TODO: 添加实际删除逻辑

        return "success";
    }

}
