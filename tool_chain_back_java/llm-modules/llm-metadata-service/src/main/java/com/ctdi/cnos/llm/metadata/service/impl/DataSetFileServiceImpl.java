package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetAndPrInfoVO;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetFile;
import com.ctdi.cnos.llm.config.RemoteHostConfig;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.DataSetDao;
import com.ctdi.cnos.llm.metadata.dao.DataSetFileDao;
import com.ctdi.cnos.llm.metadata.service.DataSetFileService;
import com.ctdi.cnos.llm.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


/**
 * 数据集上传文件(DataSetFile)表服务实现类
 *
 * @author wangyb
 * @since 2024-05-24 11:22:12
 */
@Service("dataSetFileService")
@Slf4j
@RequiredArgsConstructor
public class DataSetFileServiceImpl implements DataSetFileService {

    private final DataSetFileDao dataSetFileDao;

    private final DataSetDao dataSetDao;

    private final RemoteHostConfig remoteHostConfig;


    @Override
    public void add(DataSetFile dataSetFile) {
        dataSetFileDao.insert(dataSetFile);
    }

    @Override
    public List<DataSetFile> queryByRequestId(BigDecimal requestId) {
        LambdaQueryWrapper<DataSetFile> qw = new LambdaQueryWrapper<>();
        qw.eq(DataSetFile::getRequestId, requestId);
        return dataSetFileDao.selectList(qw);

    }


    @Override
    public DataSetFile queryBySavePath(String savePath) {
        Assert.isTrue(CharSequenceUtil.isNotBlank(savePath), "文件路径不能为空");
        LambdaQueryWrapper<DataSetFile> qw = new LambdaQueryWrapper<>();
        qw.eq(DataSetFile::getSavePath, savePath);
        return dataSetFileDao.selectOne(qw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAddDataSet(BigDecimal requestId) {
        List<DataSetFile> list = queryByRequestId(requestId);
        //voList为空, 没绑定数据集
        DataSetAndPrInfoVO vo = new DataSetAndPrInfoVO();
        vo.setRequestId(requestId);

        //自定义模板只能看自己创建的
        if (Objects.equals(vo.getBelong(), MetaDataConstants.PROMPT_BELONG_DICT_SELF)) {
            vo.setCreatorId(UserContextHolder.getUser().getId());
        }

        List<DataSet> voList = dataSetDao.queryList(vo);
        if (CollectionUtils.isEmpty(voList) && CollectionUtils.isNotEmpty(list)) {
            list.forEach(value -> {
                dataSetFileDao.deleteById(value.getId());
                this.deleteRemoteHostFileByPath(value.getSavePath());
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(BigDecimal id) {
        DataSetFile dataSetFile = dataSetFileDao.selectById(id);
        if (ObjUtil.isNotNull(dataSetFile)) {
            this.deleteRemoteHostFileByPath(dataSetFile.getSavePath());
            dataSetFileDao.deleteById(id);
        }
    }


    @Override
    public void deleteBySavePath(String savePath) {
        LambdaQueryWrapper<DataSetFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(savePath), DataSetFile::getSavePath, savePath);
        dataSetFileDao.delete(wrapper);
    }


    @Override
    public void deleteByRequestId(BigDecimal requestId) {
        LambdaQueryWrapper<DataSetFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(requestId), DataSetFile::getRequestId, requestId);
        dataSetFileDao.delete(wrapper);
    }


    @Override
    public DataSetFile queryByDataSetId(Long dataSetId) {
        return dataSetFileDao.selectOne(Wrappers.lambdaQuery(DataSetFile.class).eq(DataSetFile::getRequestId, dataSetId));
    }

    private void deleteRemoteHostFileByPath(String filePath) {
        new FileUtils().deleteRemoteFile(remoteHostConfig.getHosts().get("host41").setFilename(FileUtils.getFileName(filePath)));
        new FileUtils().deleteRemoteFile(remoteHostConfig.getHosts().get("host112").setFilename(FileUtils.getFileName(filePath)));
    }


}