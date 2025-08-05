package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctdi.cnos.llm.base.constant.CommonConstant;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetFile;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptCategoryDetail;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptResp;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptResponseDetail;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import com.ctdi.cnos.llm.config.RemoteHostConfig;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.PromptRespDao;
import com.ctdi.cnos.llm.metadata.service.*;
import com.ctdi.cnos.llm.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 问答详情(PromptResp)表服务实现类
 *
 * @author wangyb
 * @since 2024-05-15 14:06:52
 */
@Service("promptRespService")
@Slf4j
@RequiredArgsConstructor
public class PromptRespServiceImpl implements PromptRespService {

    private final PromptRespDao promptRespDao;

    private final PromptResponseDetailService promptResponseDetailService;

    private final PromptSequentialDetailService promptSequentialDetailService;

    private final PromptCategoryDetailService promptCategoryDetailService;

    private final DataSetFileService dataSetFileService;

    private final RemoteHostConfig remoteHostConfig;





    @Override
    public PromptResp queryByDataSetId(BigDecimal dataSetId) {
        return promptRespDao.queryByDataSetId(dataSetId);
    }

    @Override
    public void deleteByDataSetId(BigDecimal dataSetId) {
        Long creatorId = UserContextHolder.getUser().getId();
        promptRespDao.deleteByDataSetId(dataSetId, creatorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPromptResp(String savePath, BigDecimal dataSetId, String templateType,
                              BigDecimal currentId, String setType, String dataType) {

        //获取数据集
        PromptResp promptResp = new PromptResp();
        promptResp.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
        promptResp.setDataSetId(dataSetId);
        promptResp.setCreatorId(currentId);
        promptResp.setModifierId(currentId);

        //获取测试数据集详情列表
        File file;
        try {
            String filePath = remoteHostConfig.getHosts().get("host41").getDirectory() + File.separator + FileUtils.getFileName(savePath);
            byte[] host41 = new FileUtils().downloadFileFromRemote(remoteHostConfig.getHosts().get("host41").setFilePath(filePath));
            file = FileUtils.convertByteArrayToXlsxFile(host41);
                if ("xlsx".equals(templateType)) {
                    String context = FileUtils.convertXlsxToJson(file, setType, dataType, savePath);
                    if (CommonConstant.TRAIN_DATA_SET.equals(setType)) { //训练数据集
                        promptResp.setContext(context);
                    } else {  //测试数据集
                        DataSetFile dataSetFile = dataSetFileService.queryBySavePath(savePath);
                        if (null != dataSetFile) {
                            switch (dataType) {
                                case CommonConstant.PROMPT_RESPONSE: {
                                    List<PromptResponseDetail> list = JSON.parseObject(context, new TypeReference<List<PromptResponseDetail>>(){});
                                    promptResponseDetailService.addBatch(list, dataSetFile.getId());
                                    break;
                                }

                                case CommonConstant.PROMPT_SEQUENTIAL: {
                                    //TODO 时序数据库
                                    List<PromptSequentialDetail> list = JSON.parseObject(context, new TypeReference<List<PromptSequentialDetail>>(){});
                                    promptSequentialDetailService.insertBatch(list,  dataSetFile.getId());
                                    break;
                                }

                                case CommonConstant.PROMPT_CATEGORY: {
                                    List<PromptCategoryDetail> list = JSON.parseObject(context, new TypeReference<List<PromptCategoryDetail>>(){});
                                    promptCategoryDetailService.addBatch(list, dataSetFile.getId());
                                    break;
                                }

                            }
                        }
                    }
                }


            } catch (IOException e) {
                throw new RuntimeException("网络异常,请联系管理员");
            } catch (Exception e) {
            throw new RuntimeException("xlsx文件转换异常");
        }

        promptRespDao.add(promptResp);
        }


    }


