package com.ctdi.cnos.llm.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.ctdi.cnos.llm.base.object.ExcelDataModel;
import com.ctdi.cnos.llm.config.RemoteHostConfig;
import com.ctdi.cnos.llm.entity.HostInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

/**
 * Excel工具类。
 *
 * @author laiqi
 * @since 2024/9/4
 */
public class ExcelUtil {

    /**
     * 导出excel
     *
     * @param excelDataModel excel数据模型
     * @param response 响应
     */
    public static void exportExcel(ExcelDataModel excelDataModel, HttpServletResponse response) throws IOException {
        exportExcel(excelDataModel.getFilename(), excelDataModel.getHeaderAlias(), excelDataModel.getRows(), response);
    }

    /**
     * 导出excel
     *
     * @param filename 文件名
     * @param headerAlias 有序标题。这里推荐使用LinkedHashMap
     * @param rows 行数据
     * @param response 响应
     * @throws IOException 异常
     */
    public static void exportExcel(String filename, Map<String, String> headerAlias, Iterable<?> rows, HttpServletResponse response) throws IOException {
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter();
        // 自定义标题别名
        writer.setHeaderAlias(headerAlias);
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream out = response.getOutputStream();
        // 设置所有列为自动宽度
        // writer.autoSizeColumnAll();
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        // 此处记得关闭输出Servlet流
        IoUtil.close(out);
    }


    /**
     * 导出excel到本地文件
     *
     * @param dirPath 文件保存路径
     * @param remoteHostConfig 远程服务器配置
     * @param headerAlias 有序标题。这里推荐使用LinkedHashMap
     * @param rows 行数据
     * @return 文件路径
     */
    public static String exportExcelToFile(String dirPath, RemoteHostConfig remoteHostConfig,
                                           Map<String, String> headerAlias, Iterable<?> rows) {

        String fileName = UUID.randomUUID() + ".xlsx";
        String filePath = dirPath + fileName;
        HostInfo host41 = remoteHostConfig.getHosts().get("host41").setFilename(fileName);
        HostInfo host112 = remoteHostConfig.getHosts().get("host112").setFilename(fileName);;
        try {
            ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter();
            writer.setHeaderAlias(headerAlias);
            writer.setOnlyAlias(true);
            writer.write(rows, true);
            MultipartFile multipartFile = excelWriterToMultipartFile(writer, fileName);

            // 创建目标文件
            new FileUtils().uploadRemoteFile(host41, multipartFile);
            new FileUtils().uploadRemoteFile(host112, multipartFile);
        } catch (Exception e){
            new FileUtils().deleteRemoteFile(host41);
            new FileUtils().deleteRemoteFile(host112);
            throw new RuntimeException("强化训练数据集构建失败, 请检查");
        }
        return filePath;
    }


    /**
     * excel 转化为MultipartFile
     *
     * @param writer ExcelWriter
     * @param fileName 文件名称
     * @return MultipartFile文件
     */
    public static MultipartFile excelWriterToMultipartFile(ExcelWriter writer, String fileName) {
        // 写入到字节数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer.flush(out);
        writer.close();

        byte[] content = out.toByteArray();

        // 构建 CustomMultipartFile 对象
        MultipartFile multipartFile = new CustomMultipartFileUtil(
                fileName,
                fileName,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                content
        );

        return multipartFile;
    }


    /**
     * 生成Excel并返回字节数组
     *
     * @param headerAlias 有序标题。这里推荐使用LinkedHashMap
     * @param rows 行数据
     */
    public static byte[] exportExcel(Map<String, String> headerAlias, Iterable<?> rows) {
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter();
        // 自定义标题别名
        writer.setHeaderAlias(headerAlias);
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 设置所有列为自动宽度
        writer.autoSizeColumnAll();
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        byte[] bytes = out.toByteArray();
        // 此处记得关闭输出Servlet流
        IoUtil.close(out);
        return bytes;
    }
}