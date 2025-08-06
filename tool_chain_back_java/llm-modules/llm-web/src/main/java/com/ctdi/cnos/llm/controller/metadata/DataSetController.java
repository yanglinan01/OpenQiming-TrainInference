package com.ctdi.cnos.llm.controller.metadata;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.ctdi.cnos.llm.base.constant.CommonConstant;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetAndPrInfoVO;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetFile;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptResponseDetail;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetailVO;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.Info3cTreeResp;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusReq;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusResp;
import com.ctdi.cnos.llm.config.RemoteHostConfig;
import com.ctdi.cnos.llm.config.WebConfig;
import com.ctdi.cnos.llm.constant.Constants;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.entity.HostInfo;
import com.ctdi.cnos.llm.feign.metadata.DataSetFileServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.DataSetServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.PrTestSetEvaluationServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.FileUtils;
import com.ctdi.cnos.llm.util.MessageUtils;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.ctdi.cnos.llm.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * 数据集(DataSet)表控制层
 *
 * @author wangyb
 * @since 2024-05-15 14:06:51
 */
@Api(tags = {"DataSetController接口"})
@RestController
@RequestMapping(value = "/dataSet")
@Slf4j
@RequiredArgsConstructor
public class DataSetController {

    private final WebConfig config;

    private final DataSetServiceClientFeign dataSetServiceClientFeign;

    private final TrainTaskServiceClientFeign trainTaskServiceClientFeign;

    private final DataSetFileServiceClientFeign dataSetFileServiceClientFeign;

    private final PrTestSetEvaluationServiceClientFeign prTestSetEvaluationServiceClientFeign;


    @Autowired
    public RemoteHostConfig remoteHostConfig;

    // 时间节点维度
    private static final int EXPECTED_INTERVAL_MINUTES = 5;
    private static final long EXPECTED_INTERVAL_MILLIS = EXPECTED_INTERVAL_MINUTES * 60 * 1000;


    @ApiOperation(value = "分页查询数据集列表", notes = "分页查询数据集列表")
    @GetMapping("/queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param"),
            @ApiImplicitParam(name = "dataSetName", value = "dataSet名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "dataSet归属(1系统;2用户)", required = true, paramType = "param"),
            @ApiImplicitParam(name = "types", value = "dataSet类别,多个以逗号（,）分隔", paramType = "param")
    })
    public OperateResult<Map<String, Object>> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                                        @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                                        @RequestParam(name = "dataSetName", required = false) String dataSetName,
                                                        @RequestParam(name = "belong") String belong,
                                                        @RequestParam(name = "setType", required = false) String setType,
                                                        @RequestParam(name = "types", required = false) String types,
                                                        @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId) {

        try {
            List<String> dataTypes = new ArrayList<>();
            dataTypes.add(CommonConstant.PROMPT_RESPONSE);
            dataTypes.add(CommonConstant.PROMPT_SEQUENTIAL);
            Map<String, Object> result = dataSetServiceClientFeign.queryPage(pageSize, currentPage, dataSetName, belong, setType, types, enhancedTrainTaskId, dataTypes);
            return new OperateResult<>(true, "分页查询数据集列表成功", result);
        } catch (Exception e) {
            log.error("分页查询数据集列表异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    @ApiOperation(value = "分页查询意图识别数据集列表", notes = "分页查询数据集列表")
    @GetMapping("/queryPageByCategory")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param"),
            @ApiImplicitParam(name = "dataSetName", value = "dataSet名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "dataSet归属(1系统;2用户)", required = true, paramType = "param"),
            @ApiImplicitParam(name = "types", value = "dataSet类别,多个以逗号（,）分隔", paramType = "param"),
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "param")
    })
    public OperateResult<Map<String, Object>> queryPageByCategory(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                                                  @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                                                  @RequestParam(name = "dataSetName", required = false) String dataSetName,
                                                                  @RequestParam(name = "belong") String belong,
                                                                  @RequestParam(name = "setType", required = false) String setType,
                                                                  @RequestParam(name = "types", required = false) String types,
                                                                  @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId,
                                                                  @RequestParam(name = "projectId", required = false) Long projectId) {

        try {
            List<String> dataTypes = new ArrayList<>();
            dataTypes.add(CommonConstant.PROMPT_CATEGORY);
            dataTypes.add(CommonConstant.PROMPT_RESPONSE);
            dataTypes.add(CommonConstant.PROMPT_SEQUENTIAL);
            Map<String, Object> result = dataSetServiceClientFeign.queryPageByCategory(pageSize, currentPage, dataSetName, belong, setType, types, enhancedTrainTaskId, dataTypes,projectId);
            return new OperateResult<>(true, "分页查询数据集列表成功", result);
        } catch (Exception e) {
            log.error("分页查询数据集列表异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    @ApiOperation(value = "查询数据集列表", notes = "查询数据集列表")
    @GetMapping("/queryList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSetName", value = "dataSet名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "dataSet归属(1系统;2用户)", paramType = "param"),
            @ApiImplicitParam(name = "types", value = "dataSet类别,多个以逗号（,）分隔", paramType = "param")
    })


    public OperateResult<List<DataSet>> queryList(@RequestParam(name = "dataSetName", required = false) String dataSetName,
                                                  @RequestParam(name = "belong", required = false) String belong,
                                                  @RequestParam(name = "types", required = false) String types,
                                                  @RequestParam(name = "dataType", required = false) String dataType,
                                                  @RequestParam(name = "setType", required = false) String setType,
                                                  @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId) {
        try {
            List<String> dataTypes = new ArrayList<>();
            if (StringUtils.isEmpty(dataType)) {
                dataTypes.add(CommonConstant.PROMPT_RESPONSE);
                dataTypes.add(CommonConstant.PROMPT_SEQUENTIAL);
            }
            List<DataSet> list = dataSetServiceClientFeign.queryList(dataSetName, belong, types, dataType, setType, enhancedTrainTaskId, dataTypes);
            return new OperateResult<>(true, "查询数据集列表成功", list);
        } catch (Exception e) {
            log.error("查询数据集列表成功异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @ApiOperation(value = "查询意图识别数据集列表", notes = "查询数据集列表")
    @GetMapping("/queryListByCategory")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSetName", value = "dataSet名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "dataSet归属(1系统;2用户)", paramType = "param"),
            @ApiImplicitParam(name = "types", value = "dataSet类别,多个以逗号（,）分隔", paramType = "param")
    })
    public OperateResult<List<DataSet>> queryListByCategory(@RequestParam(name = "dataSetName", required = false) String dataSetName,
                                                            @RequestParam(name = "belong", required = false) String belong,
                                                            @RequestParam(name = "types", required = false) String types,
                                                            @RequestParam(name = "dataType", required = false) String dataType,
                                                            @RequestParam(name = "setType", required = false) String setType,
                                                            @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId,
                                                            @RequestParam(name = "projectId", required = false) Long projectId,
                                                            @RequestParam(name = "dataTypes", required = false) String dataTypes) {
        try {
            List<String> dataTypeList=new ArrayList<>();
            if(StringUtils.isNotEmpty(dataTypes)){
                dataTypeList=Arrays.asList(dataTypes.split(","));
            }
            List<DataSet> list = dataSetServiceClientFeign.queryListByCategory(dataSetName, belong, types, dataType, setType, enhancedTrainTaskId, dataTypeList,projectId);
            return new OperateResult<>(true, "查询意图识别数据集列表成功", list);
        } catch (Exception e) {
            log.error("查询意图识别数据集列表成功异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @GetMapping("/queryById")
    @ApiOperation(value = "查询DataSet详情")
    public OperateResult<DataSetAndPrInfoVO> queryById(@RequestParam(name = "id") String id,
                                                       @RequestParam(name = "dataType") String dataType,
                                                       @RequestParam(name = "setType") String setType,
                                                       @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                                       @RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize) {
        try {
            DataSetAndPrInfoVO vo = dataSetServiceClientFeign.queryVOById(id, dataType, setType, currentPage, pageSize);
            if (null != vo) {
                vo.setSavePath("");
            }
            return new OperateResult<>(true, "查询DataSet详情成功", vo);
        } catch (Exception e) {
            log.error("查询DataSet详情异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @GetMapping("/deleteById")
    @ApiOperation(value = "删除数据集")
    public OperateResult<Map<String, Object>> deleteById(@RequestParam(name = "dataSetId") String dataSetId) {
        try {
            Long count = trainTaskServiceClientFeign.countByDataSetId(Long.valueOf(dataSetId), "");
            if (count == 0L) {
                dataSetServiceClientFeign.deleteById(dataSetId);
                return new OperateResult<>(true, "删除数据集成功", null);
            } else {
                return new OperateResult<>(true, "当前数据集已绑定训练任务, 无法被删除", null);
            }
        } catch (Exception e) {
            log.error("删除数据集异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @PostMapping(value = "/add")
    @ApiOperation(value = "新增数据集")
    public OperateResult<Void> add(@RequestBody DataSet dataSet) {
        try {
            dataSetServiceClientFeign.add(dataSet);
            return new OperateResult<>(true, "新增数据集成功", null);
        } catch (Exception e) {
            log.error("新增数据集异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    @PostMapping(value = "/projSpace/add")
    @ApiOperation(value = "新增项目空间数据集")
    public OperateResult<Void> addProjSpace(@RequestBody DataSet dataSet) {
        try {
            // 项目空间的上传数据集和个人空间的一致,复用即可
            dataSetServiceClientFeign.add(dataSet);
            return new OperateResult<>(true, "新增数据集成功", null);
        } catch (Exception e) {
            log.error("新增数据集异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改数据集")
    public OperateResult<Void> update(@RequestBody DataSet dataSet) {
        try {
            dataSetServiceClientFeign.update(dataSet);
            return new OperateResult<>(true, "修改数据集成功", null);
        } catch (Exception e) {
            log.error("修改数据集异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @PostMapping("/uploadTemp")
    @ApiOperation(value = "上传问答对文件")
    public OperateResult<Void> uploadTemp(@RequestParam("requestId") String requestId,
                                          @RequestParam("dataSetFileId") String dataSetFileId,
                                          @RequestParam("dataType") String dataType,
                                          @RequestParam("setType") String setType,
                                          @RequestParam("file") MultipartFile file) {
        DataSetFile dataSetFile = new DataSetFile();
        // 文件存储路径
        String savePath = "";
        // 文件大小
        Long fileSize = 0L;
        // 对话数
        Long prCount = 0L;
        // 文件类型
        String templateType = "";
        // 判断数据集标签是否为测试数据集
        if (CommonConstant.TEST_DATA_SET.equals(setType)) {
            // 判断数据集类型是否为问答
            if (CommonConstant.PROMPT_RESPONSE.equals(dataType) || CommonConstant.PROMPT_CATEGORY.equals(dataType)) {
                try {
                    Map<String, String> map = new FileUtils().uploadFile(file, config.getDatasetUploadDir(), dataType,
                            setType, remoteHostConfig).get();
                    savePath = map.get("savePath");
                    fileSize = Long.parseLong(map.get("fileSize"));
                    prCount = Long.parseLong(map.get("prCount"));
                    templateType = map.get("templateType");
                } catch (Exception e) {
                    log.error("文件上传异常", e);
                    return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
                }
            }

            // 判断数据集类型是否为时序
            if (CommonConstant.PROMPT_SEQUENTIAL.equals(dataType)) {
                // 解析excel数据
                List<PromptSequentialDetailVO> dataPoints = null;
                try {
                    dataPoints = readDataFromExcel(file, dataSetFileId);
                } catch (Exception e) {
                    return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
                }

                // 组装excel数据
                List<PromptSequentialDetail> processedData = null;
                try {
                    processedData = processDataPoints(dataPoints);
                } catch (Exception e) {
                    return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
                }

                // 重新生成excel并上传到服务器
                try {
                    Map<String, String> result = createExcel(file, processedData, dataType, setType);

                    savePath = result.get("savePath");
                    fileSize = Long.parseLong(result.get("fileSize"));
                    prCount = Long.parseLong(result.get("prCount"));
                    templateType = result.get("templateType");

                    log.info("时序数据集文件路径为:" + savePath);

                } catch (Exception e) {
                    if (StrUtil.isNotBlank(dataSetFile.getSavePath())) {
                        this.deleteRemoteHostFileByPath(dataSetFile.getSavePath());
                    }
                    log.error("文件上传异常", e);
                    return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
                }
            }
        } else {
            try {
                Map<String, String> map = new FileUtils().uploadFile(file, config.getDatasetUploadDir(), dataType, setType, remoteHostConfig).get();
                savePath = map.get("savePath");
                fileSize = Long.parseLong(map.get("fileSize"));
                prCount = Long.parseLong(map.get("prCount"));
                templateType = map.get("templateType");
            } catch (Exception e) {
                if (StrUtil.isNotBlank(dataSetFile.getSavePath())) {
                    this.deleteRemoteHostFileByPath(dataSetFile.getSavePath());
                }
                log.error("文件上传异常", e);
                return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
            }
        }


        dataSetFile.setId(new BigDecimal(dataSetFileId));
        dataSetFile.setCreatorId(new BigDecimal(UserContextHolder.getUser().getId()));
        dataSetFile.setModifierId(new BigDecimal(UserContextHolder.getUser().getId()));
        dataSetFile.setRequestId(new BigDecimal(requestId));
        dataSetFile.setSavePath(savePath);
        dataSetFile.setFileSize(fileSize);
        dataSetFile.setPrCount(prCount);
        dataSetFile.setTemplateType(templateType);
        dataSetFile.setBelong(Constants.SELF_UP);
        dataSetFileServiceClientFeign.add(dataSetFile);
        return new OperateResult<>(true, "文件上传成功", null);
    }


    @GetMapping("/downloadTemp")
    @ApiOperation(value = "下载问答对模板")
    public ResponseEntity<Resource> downloadTemp(@RequestParam(value = "param", required = false, defaultValue = "1") String param) throws Exception {
        String fileName = getFileNameByParam(param);
        String remoteFilePath = "template/" + fileName;
        HostInfo hostInfo = remoteHostConfig.getHosts().get("host112").setFilePath(remoteFilePath);
        byte[] fileContent = new FileUtils().downloadFileFromRemote(hostInfo);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileContent));
        // 返回文件下载响应
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(fileContent.length)
                .body(resource);
    }


    /**
     * 根据参数返回对应的模板名
     *
     * @param param 模板参数
     * @return 文件名
     */
    private String getFileNameByParam(String param) {
        switch (param) {
            case "1":
                return "问答对.zip";
            case "2":
                return "意图识别.zip";
            case "3":
                return "流量预测.zip";
            case "4":
                return "测试数据集模版.zip";
        }
        return "";
    }


    @GetMapping("/cancelAddDataSet")
    @ApiOperation(value = "取消新增数据集")
    public OperateResult<Map<String, Object>> cancelAddDataSet(@RequestParam("requestId") String requestId) {
        try {
            dataSetFileServiceClientFeign.cancelAddDataSet(new BigDecimal(requestId));
            return new OperateResult<>(true, "取消新增数据集成功", null);
        } catch (Exception e) {
            log.error("取消新增数据集异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @GetMapping("/deleteByDataSetFileId")
    @ApiOperation(value = "取消单个数据集文件")
    public OperateResult<Map<String, Object>> deleteByDataSetFileId(@RequestParam("dataSetFileId") String dataSetFileId) {
        try {
            dataSetFileServiceClientFeign.deleteById(new BigDecimal(dataSetFileId));
            return new OperateResult<>(true, "取消文件成功", null);
        } catch (Exception e) {
            log.error("取消文件成功异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @GetMapping("/getDocList")
    @ApiOperation(value = "获取当前用户远程数据集列表")
    public OperateResult<JSONArray> getDocList() {
        try {
            JSONArray jsonArray = JSON.parseArray(dataSetServiceClientFeign.getDocList());
            return new OperateResult<>(true, "获取当前用户远程数据集列表成功", jsonArray);
        } catch (Exception e) {
            log.error("获取当前用户远程数据集列表异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @PostMapping("/addFromKnowledgeBase")
    @ApiOperation(value = "新增知识库数据集")
    public OperateResult<Void> addFromKnowledgeBase(@RequestBody DataSetAndPrInfoVO vo) {
        try {
            vo.setUploadDir(config.getDatasetMergeDir());
            dataSetServiceClientFeign.addFromKnowledgeBase(vo);
            return new OperateResult<>(true, "新增知识库数据集成功", null);
        } catch (Exception e) {
            log.error("新增知识库数据集异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    @PostMapping("/projSpace/addFromKnowledgeBase")
    @ApiOperation(value = "新增项目空间知识库数据集")
    public OperateResult<Void> addProjSpaceFromKB(@RequestBody DataSetAndPrInfoVO vo) {
        try {
            vo.setUploadDir(config.getDatasetMergeDir());
            dataSetServiceClientFeign.addProjSpaceFromKB(vo);
            return new OperateResult<>(true, "新增知识库数据集成功", null);
        } catch (Exception e) {
            log.error("新增知识库数据集异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    @PostMapping("/addIntentionRecognition")
    @ApiOperation(value = "新增意图识别数据集从知识库")
    public OperateResult<Void> addIntentionRecognition(@RequestBody DataSetAndPrInfoVO vo){
        try {
            vo.setUploadDir(config.getDatasetUploadDir());
            dataSetServiceClientFeign.addIntentionRecognition(vo);
            return new OperateResult<>(true, "新增意图识别数据集从知识库", null);
        } catch (Exception e) {
            log.error("新增意图识别数据集从知识库", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    @PostMapping("/intentionRecognitionCorpusList")
    @ApiOperation(value = "意图识别语料查询")
    public IntentRecognitionCorpusResp intentionRecognitionCorpusList(@RequestBody IntentRecognitionCorpusReq req) {
        IntentRecognitionCorpusResp intentRecognitionCorpusResp = new IntentRecognitionCorpusResp();
        try {
            intentRecognitionCorpusResp = dataSetServiceClientFeign.intentionRecognitionCorpusList(req);
        } catch (Exception e) {
            log.error("意图识别语料查询异常", e);
            intentRecognitionCorpusResp.setMsg("意图识别语料查询异常");
            intentRecognitionCorpusResp.setCode(500);
        }
        return intentRecognitionCorpusResp;
    }

    @PostMapping("/info3cTreeList")
    @ApiOperation(value = "3c信息查询")
    public Info3cTreeResp info3cTreeList(){
        Info3cTreeResp resp = new Info3cTreeResp();
        try {
            resp = dataSetServiceClientFeign.info3cTreeList();
        } catch (Exception e) {
            log.error("3c信息查询异常", e);
            resp.setMsg("3c信息查询异常");
            resp.setCode(500);
        }
        return resp;
    }

    /**
     * 从excel文件读取内容
     *
     * @param file
     * @param dataSetFileId
     * @return
     */
    private static List<PromptSequentialDetailVO> readDataFromExcel(MultipartFile file, String dataSetFileId) throws Exception {
        List<PromptSequentialDetailVO> dataPoints = new ArrayList<>();
        Set<String> circuitIdSet = new HashSet<>();
        try (InputStream fis = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(fis)) {
//             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                boolean isEmptyRow = true;
                for (Cell cell : row) {
                    if (cell.getCellType() != CellType.BLANK) {
                        isEmptyRow = false;
                        break;
                    }
                }

                if (isEmptyRow) continue;

                PromptSequentialDetailVO vo = new PromptSequentialDetailVO();
                DataFormatter dataFormatter = new DataFormatter();

                Cell circuitId = row.getCell(0);
                Cell cirName = row.getCell(1);
                Cell kDevId = row.getCell(2);
                Cell kIntfId = row.getCell(3);
                Cell devidIp = row.getCell(4);
                Cell bDevId = row.getCell(5);
                Cell bIntfDescr = row.getCell(6);
                Cell bDevidIp = row.getCell(7);
                Cell dCirbw = row.getCell(8);
                Cell dInFlux = row.getCell(9);
                Cell dOutFlux = row.getCell(10);
                Cell dInFluxRatio = row.getCell(11);
                Cell dOutFluxRatio = row.getCell(12);
                Cell tCtime = row.getCell(13);

                vo.setCircuitId(circuitId.getStringCellValue());
                vo.setCirName(cirName.getStringCellValue());
                vo.setKDevId(kDevId.getStringCellValue());
                vo.setKIntfId(kIntfId.getStringCellValue());
                vo.setDevidIp(devidIp.getStringCellValue());
                vo.setBDevId(bDevId.getStringCellValue());
                vo.setBIntfDescr(bIntfDescr.getStringCellValue());
                vo.setBDevidIp(bDevidIp.getStringCellValue());
//                vo.setDCirbw(dataFormatter.formatCellValue(row.getCell(8)));
//                vo.setDInFlux(dataFormatter.formatCellValue(row.getCell(9)));
//                vo.setDOutFlux(dataFormatter.formatCellValue(row.getCell(10)));
//                vo.setDInFluxRatio(dataFormatter.formatCellValue(row.getCell(11)));
//                vo.setDOutFluxRatio(dataFormatter.formatCellValue(row.getCell(12)));
                // 校验宽带（dCirbw）是否为数字
                if (dCirbw.getCellType() == CellType.NUMERIC) {
                    vo.setDCirbw(dataFormatter.formatCellValue(dCirbw));
                } else {
                    throw new Exception("第" + row.getRowNum() + "行宽带数据不是数字格式。");
                }

                // 校验流入流速（dInFlux）是否为数字
                if (dInFlux.getCellType() == CellType.NUMERIC) {
                    vo.setDInFlux(dataFormatter.formatCellValue(dInFlux));
                } else {
                    throw new Exception("第" + row.getRowNum() + "行流入流速数据不是数字格式。");
                }

                // 校验流出流速（dOutFlux）是否为数字
                if (dOutFlux.getCellType() == CellType.NUMERIC) {
                    vo.setDOutFlux(dataFormatter.formatCellValue(dOutFlux));
                } else {
                    throw new Exception("第" + row.getRowNum() + "行流出流速数据不是数字格式。");
                }

                // 校验流入带宽利用率（dInFluxRatio）是否为数字
                if (dInFluxRatio.getCellType() == CellType.NUMERIC) {
                    vo.setDInFluxRatio(dataFormatter.formatCellValue(dInFluxRatio));
                } else {
                    throw new Exception("第" + row.getRowNum() + "行入带宽利用率数据不是数字格式。");
                }

                // 校验流出带宽利用率（dOutFluxRatio）是否为数字
                if (dOutFluxRatio.getCellType() == CellType.NUMERIC) {
                    vo.setDOutFluxRatio(dataFormatter.formatCellValue(dOutFluxRatio));
                } else {
                    throw new Exception("第" + row.getRowNum() + "行流出带宽利用率数据不是数字格式。");
                }
                vo.setTCtime(tCtime.getDateCellValue());
                vo.setDataSetFileId(new BigDecimal(dataSetFileId));
                vo.setStatus("0");

                circuitIdSet.add(vo.getCircuitId());
                if (circuitIdSet.size() > 1) {
                    throw new Exception("只能上传单条链路！");
                }
                dataPoints.add(vo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataPoints;
    }

    /**
     * 对测试数据集进行处理
     *
     * @param dataEntries
     * @return
     */
    public static List<PromptSequentialDetail> processDataPoints(List<PromptSequentialDetailVO> dataEntries) throws Exception {
        List<PromptSequentialDetail> processedEntries = new ArrayList<>();
        dataEntries.sort(Comparator.comparing(PromptSequentialDetail::getTCtime));
        int size = dataEntries.size();
        for (int i = 0; i < size; i++) {
            PromptSequentialDetailVO currentEntry = dataEntries.get(i);
            Date currentTime = currentEntry.getTCtime();

            if (i == 0) {
                processedEntries.add(currentEntry);
                continue;
            }

            PromptSequentialDetail previousEntry = processedEntries.get(processedEntries.size() - 1);
            Date previousTime = previousEntry.getTCtime();

            long timeDifference = (currentTime.getTime() - previousTime.getTime()) / (1000 * 60);
            long timeGap = currentTime.getTime() - previousTime.getTime();

            int numIntervals = (int) (timeGap / EXPECTED_INTERVAL_MILLIS);

            if (numIntervals >= 1) {
                int missingCount = numIntervals - 1;
                if (missingCount > 10) {
                    log.error("连续缺失超过 10 个节点！");
                    throw new Exception("连续缺失超过 10 个节点！");
//                    break;
                }

                int numIntervalsToFill;
                if (timeDifference % 5 != 0) {
                    numIntervalsToFill = (int) (timeDifference / 5) + 1;
                } else {
                    numIntervalsToFill = (int) (timeDifference / 5);
                }

                for (int j = 0; j < numIntervalsToFill; j++) {
                    Date newTime = new Date(previousTime.getTime() + (j + 1) * EXPECTED_INTERVAL_MILLIS);
                    PromptSequentialDetail newEntry;
                    if (j == numIntervalsToFill - 1 || newTime.getTime() == currentTime.getTime()) {
                        newEntry = ModelUtil.copyTo(currentEntry, PromptSequentialDetail.class);
                    } else {
                        newEntry = ModelUtil.copyTo(previousEntry, PromptSequentialDetail.class);
                    }
                    newEntry.setTCtime(newTime);
                    processedEntries.add(newEntry);
                }
            }
        }
        return processedEntries;
    }

    /**
     * 创建新的测试数据集
     *
     * @param file
     * @param dataPoints
     * @param datasetType
     * @param datasetLabel
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Map<String, String> createExcel(MultipartFile file, List<PromptSequentialDetail> dataPoints, String datasetType, String datasetLabel) throws Exception {
        // 创建工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();// 这里也可以设置sheet的Name
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 创建工作表对象
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "时序测试数据集");

        // 创建工作表的行
        HSSFRow row = sheet.createRow(0);// 设置第一行，从零开始
        row.createCell(0).setCellValue("链路编号");
        row.createCell(1).setCellValue("电路名称");
        row.createCell(2).setCellValue("A端设备编码");
        row.createCell(3).setCellValue("A端端口");
        row.createCell(4).setCellValue("A端设备IP");
        row.createCell(5).setCellValue("B端设备编码");
        row.createCell(6).setCellValue("B端端口号");
        row.createCell(7).setCellValue("B端设备IP");
        row.createCell(8).setCellValue("带宽");
        row.createCell(9).setCellValue("流入流速");
        row.createCell(10).setCellValue("流出流速");
        row.createCell(11).setCellValue("流入带宽利用率");
        row.createCell(12).setCellValue("流出带宽利用率");
        row.createCell(13).setCellValue("采集时间");

        if (dataPoints.size() > 0) {
            for (int i = 0; i < dataPoints.size(); i++) {
                HSSFRow rows = sheet.createRow(i + 1);// 设置第一行，从零开始
                rows.createCell(0).setCellValue(dataPoints.get(i).getCircuitId());
                rows.createCell(1).setCellValue(dataPoints.get(i).getCirName());
                rows.createCell(2).setCellValue(dataPoints.get(i).getKDevId());
                rows.createCell(3).setCellValue(dataPoints.get(i).getKIntfId());
                rows.createCell(4).setCellValue(dataPoints.get(i).getDevidIp());
                rows.createCell(5).setCellValue(dataPoints.get(i).getBDevId());
                rows.createCell(6).setCellValue(dataPoints.get(i).getBIntfDescr());
                rows.createCell(7).setCellValue(dataPoints.get(i).getBDevidIp());
                rows.createCell(8).setCellValue(dataPoints.get(i).getDCirbw());
                rows.createCell(9).setCellValue(dataPoints.get(i).getDInFlux());
                rows.createCell(10).setCellValue(dataPoints.get(i).getDOutFlux());
                rows.createCell(11).setCellValue(dataPoints.get(i).getDInFluxRatio());
                rows.createCell(12).setCellValue(dataPoints.get(i).getDOutFluxRatio());
                rows.createCell(13).setCellValue(dateFormat.format(dataPoints.get(i).getTCtime()));
            }
        }

        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();

        // 文档输出
        FileOutputStream out;
        try {
            out = new FileOutputStream(config.getDatasetUploadDir() + fileName);
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File newFile = new File(config.getDatasetUploadDir() + fileName);

        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fileName, "text/plain", true, fileName);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(newFile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MultipartFile mfile = new CommonsMultipartFile(item);

        FileUtils fileUtils = new FileUtils();

        if (newFile.exists()) {
            newFile.delete();
        }

        Map<String, String> map = fileUtils.uploadFile(mfile, config.getDatasetUploadDir(), datasetType, datasetLabel, remoteHostConfig).get();

        return map;
    }


    /**
     * 问答对数据集解析
     *
     * @param file
     * @param dataSetFileId
     * @return
     */
    private static List<PromptResponseDetail> countValueOccurrences(MultipartFile file, String dataSetFileId) {
        List<PromptResponseDetail> dataPoints = new ArrayList<>();
        Map<String, Integer> counts = new HashMap<>();
        try {
            try (InputStream fis = file.getInputStream();
                 Workbook workbook = WorkbookFactory.create(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Skip header row
                    if (null != row) {
                        PromptResponseDetail vo = new PromptResponseDetail();

                        Cell questionId = row.getCell(0);
                        Cell rank = row.getCell(1);
                        Cell prompt = row.getCell(2);
                        Cell response = row.getCell(3);

                        vo.setQuestionId(Convert.toInt(getCellValue(questionId)));
                        vo.setRank(Convert.toInt(getCellValue(rank)));
                        vo.setPrompt(getCellValue(prompt));
                        vo.setResponse(getCellValue(response));
                        vo.setDataSetFileId(new BigDecimal(dataSetFileId));
                        vo.setStatus("0");
                        dataPoints.add(vo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataPoints;
    }

    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }


    private void deleteRemoteHostFileByPath(String filePath) {
        new FileUtils().deleteRemoteFile(remoteHostConfig.getHosts().get("host41").setFilename(FileUtils.getFileName(filePath)));
        new FileUtils().deleteRemoteFile(remoteHostConfig.getHosts().get("host112").setFilename(FileUtils.getFileName(filePath)));
    }
}
