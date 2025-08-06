//package com.ctdi.cnos.llm.metadata.service.impl;
//
//import cn.hutool.core.util.ObjUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
//
//import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetAndPrInfoVO;
//import com.ctdi.cnos.llm.context.UserContextHolder;
//import com.ctdi.cnos.llm.metadata.config.RemoteDateSetFileConfig;
//import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
//import com.ctdi.cnos.llm.metadata.dao.DataSetDao;
//import com.ctdi.cnos.llm.metadata.dao.DataSetFileDao;
//import com.ctdi.cnos.llm.metadata.service.DataSetFileService;
//import com.jcraft.jsch.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPReply;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.net.SocketException;
//import java.util.List;
//import java.util.Objects;
//
//
///**
// * 数据集上传文件(DataSetFile)表服务实现类
// *
// * @author wangyb
// * @since 2024-05-24 11:22:12
// */
//@Service("dataSetFileService")
//@Slf4j
//@RequiredArgsConstructor
//public class DataSetFileServiceImpl implements DataSetFileService {
//
//    private final DataSetFileDao dataSetFileDao;
//
//    private final DataSetDao dataSetDao;
//
//    private final RemoteDateSetFileConfig config;
//
//    private FTPClient ftpClient;
//
//    @Override
//    public void add(DataSetFile dataSetFile) {
//        dataSetFileDao.insert(dataSetFile);
//    }
//
//    @Override
//    public List<DataSetFile> queryByRequestId(BigDecimal requestId) {
//        LambdaQueryWrapper<DataSetFile> qw = new LambdaQueryWrapper<>();
//        qw.eq(DataSetFile::getRequestId, requestId);
//        return dataSetFileDao.selectList(qw);
//
//    }
//
//
//    @Override
//    public DataSetFile queryBySavePath(String savePath) {
//        LambdaQueryWrapper<DataSetFile> qw = new LambdaQueryWrapper<>();
//        qw.eq(DataSetFile::getSavePath, savePath);
//        return dataSetFileDao.selectOne(qw);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void cancelAddDataSet(BigDecimal requestId) {
//        List<DataSetFile> list = queryByRequestId(requestId);
//        //voList为空, 没绑定数据集
//        DataSetAndPrInfoVO vo = new DataSetAndPrInfoVO();
//        vo.setRequestId(requestId);
//
//        //自定义模板只能看自己创建的
//        if (Objects.equals(vo.getBelong(), MetaDataConstants.PROMPT_BELONG_DICT_SELF)) {
//            vo.setCreatorId(new BigDecimal(UserContextHolder.getUser().getId()));
//        }
//
//        List<DataSetAndPrInfoVO> voList = dataSetDao.queryList(vo);
//        if (CollectionUtils.isEmpty(voList) && CollectionUtils.isNotEmpty(list)) {
//            list.forEach(value -> {
//                dataSetFileDao.deleteById(value.getId());
//                new File(value.getSavePath()).delete();
//                deleteRemoteFile(value.getSavePath());
//            });
//        }
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteById(BigDecimal id) {
//        DataSetFile dataSetFile = dataSetFileDao.selectById(id);
//        if (ObjUtil.isNotNull(dataSetFile)) {
//            new File(dataSetFile.getSavePath()).delete();
//            deleteRemoteFile(dataSetFile.getSavePath());
//            dataSetFileDao.deleteById(id);
//        }
//    }
//
//
//    @Override
//    public void deleteBySavePath(String savePath) {
//        LambdaQueryWrapper<DataSetFile> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Objects.nonNull(savePath), DataSetFile::getSavePath, savePath);
//        dataSetFileDao.delete(wrapper);
//    }
//
//
//    /**
//     * 上传文件
//     * @param localFileName 本地文件绝对路径
//     */
//    public void uploadRemoteFile(String localFileName) {
//        log.info("远程数据集文件是否启用：" + config.isFlag());
//        if (config.isFlag()) {
//            FileInputStream fis = null;
//            try {
//                connectToRemoteHost();
//                File localFile = new File(localFileName);
//                fis = new FileInputStream(localFile);
//
//                ftpClient.changeWorkingDirectory(config.getDestDir());
//                ftpClient.storeFile(localFile.getName(), fis);
//
//                log.info("文件上传远程机器成功: {} ;文件: {} -> {}", config.getIp(), localFileName, config.getDestDir());
//            } catch (IOException e) {
//                log.error("文件上传远程机器失败: {};文件: {}; {}", config.getIp(), localFileName, e);
//                new File(localFileName).delete();
//                throw new RuntimeException("文件上传远程机器失败: " + config.getIp() + ":" + config.getDestDir(), e);
//            } finally {
//                if (fis != null) {
//                    try {
//                        fis.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                disconnectFromRemoteHost();
//            }
//        }
//    }
//
//
//    /**
//     * 删除远程文件
//     * @param filePath 文件绝对路径
//     */
//    public void deleteRemoteFile(String filePath) {
//        log.info("远程数据集文件是否启用：" + config.isFlag());
//        if (config.isFlag()) {
//            try {
//                connectToRemoteHost();
//
//                String fileName = getFileName(filePath);
//                boolean deleted = ftpClient.deleteFile(config.getDestDir() + "/" + fileName);
//
//                if (deleted) {
//                    log.info("远程机器{} 删除文件成功: {}{}", config.getIp(), config.getDestDir(), fileName);
//                } else {
//                    log.warn("远程机器{} 删除文件失败: {}{}", config.getIp(), config.getDestDir(), fileName);
//                }
//            } catch (IOException e) {
//                log.error("远程机器{} 删除文件失败: {}{}", config.getIp(), config.getDestDir(), getFileName(filePath), e);
//                throw new RuntimeException("远程机器删除文件失败", e);
//            } finally {
//                disconnectFromRemoteHost();
//            }
//        }
//    }
//
//
//    /**
//     * 连接远程数据集机器
//     * @return FTPClient
//     */
//    private FTPClient connectToRemoteHost() {
//        if (ftpClient == null || !ftpClient.isConnected()) {
//            log.info("远程机器连接成功: {}", config.getIp());
//            try {
//                ftpClient = new FTPClient();
//                ftpClient.connect(config.getIp(), config.getPort());
//                ftpClient.login(config.getUsername(), config.getPassword());
//
//                int replyCode = ftpClient.getReplyCode();
//                if (!FTPReply.isPositiveCompletion(replyCode)) {
//                    ftpClient.disconnect();
//                    throw new RuntimeException("FTP 连接失败: " + config.getIp());
//                }
//
//                ftpClient.enterLocalPassiveMode(); // 使用被动模式
//                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); // 设置为二进制文件传输
//
//            } catch (IOException e) {
//                log.error("FTP 连接失败: {}", config.getIp(), e);
//                throw new RuntimeException("FTP 连接失败: " + config.getIp());
//            }
//        }
//        return ftpClient;
//    }
//
//
//    /**
//     * 断开远程数据集机器连接
//     */
//    public void disconnectFromRemoteHost() {
//        if (ftpClient != null && ftpClient.isConnected()) {
//            try {
//                ftpClient.logout();
//                ftpClient.disconnect();
//                ftpClient = null;
//                log.info("远程机器已断开: {}", config.getIp());
//            } catch (IOException e) {
//                log.error("断开远程机器连接失败: {}", config.getIp(), e);
//            }
//        }
//    }
//
//
//    /**
//     * 获取文件名
//     * @param filePath 文件绝对路径
//     * @return 文件名
//     */
//    private String getFileName(String filePath) {
//        int index = filePath.lastIndexOf("/");
//        return filePath.substring(index + 1);
//    }
//
//
//}
//
