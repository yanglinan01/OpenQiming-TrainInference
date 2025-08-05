package com.ctdi.cnos.llm.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.base.constant.CommonConstant;
import com.ctdi.cnos.llm.base.constant.SheetHeaderConstant;
import com.ctdi.cnos.llm.config.RemoteHostConfig;
import com.ctdi.cnos.llm.entity.HostInfo;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FileUtils {

    private Session session = null;

    private ChannelSftp channelSftp = null;


    /**
     * 根据文件名获取文件类型
     *
     * @param fileName 文件名
     * @return 文件类型
     */
    private static MediaType determineMediaType(String fileName) {
        String extension = FilenameUtils.getExtension(fileName.toLowerCase());
        switch (extension) {
            case "jsonl":
                return MediaType.parseMediaType("application/json");
            case "xlsx":
                return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            case "doc":
                return MediaType.parseMediaType("application/msword");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }


    /**
     * 文件上传
     *
     * @param file      文件
     * @param uploadDir 文件目录
     * @return 文件上传返回结果
     */
    @Async("fileUploadExecutor")
    public CompletableFuture<Map<String, String>> uploadFile(MultipartFile file, String uploadDir, String dataType,
                                                             String setType, RemoteHostConfig remoteHostConfig) {
        log.info("当前线程名为：" + Thread.currentThread().getName());
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件或内容不能为空!");
        }

        Map<String, String> map = new ConcurrentHashMap<>();
        this.validateFile(file, dataType, setType); //校验文件格式

        String originalFileName = file.getOriginalFilename();
        String fileExtension = FileUtil.extName(originalFileName);
        String templateType = getValidExtension(fileExtension);
        map.put("templateType", templateType);

        String newFileName = IdUtil.randomUUID() + "." + fileExtension;

        String savePath = uploadDir + newFileName;

        map.put("savePath", savePath);
        long fileSize = file.getSize();
        map.put("fileSize", String.valueOf(fileSize));

        Long prCount = 0L;

        Map<String, Object> checkMap = new HashMap<>();
        if (templateType.toLowerCase().contains("xlsx")) {
            //校验数据
            checkMap = FileUtils.getXlsxCheck(file, dataType, setType);
            if ((boolean) checkMap.get("judge")) {
                prCount = FileUtils.getXlsxPrCount(file, dataType, setType);
            }
        }
        if (fileExtension.toLowerCase().contains("json")) {
            prCount = FileUtils.getJsonPrCount(file);
        }

        if (checkMap.containsKey("judge") && !(boolean) checkMap.get("judge")) {
            throw new RuntimeException("数据集校验失败：" + checkMap.get("msg"));
        }

        if (!CommonConstant.PROMPT_SEQUENTIAL.equals(dataType) && CommonConstant.TEST_DATA_SET.equals(setType) && (prCount > CommonConstant.MAX_PR_DATA_POINTS)) {
            throw new RuntimeException("测试数据集不能超过" + CommonConstant.MAX_PR_DATA_POINTS + "条");
        }

        if (CommonConstant.TRAIN_DATA_SET.equals(setType) && (prCount < CommonConstant.MIN_PR_DATA_POINTS)) {
            throw new RuntimeException("问答对训练数据集最低不能少于" + CommonConstant.MIN_PR_DATA_POINTS + "条");
        }

        if (CommonConstant.PROMPT_SEQUENTIAL.equals(dataType) && CommonConstant.TEST_DATA_SET.equals(setType) && (prCount < CommonConstant.MIN_DATA_POINTS)) {
            throw new RuntimeException("时序测试数据集最低不能少于" + CommonConstant.MIN_DATA_POINTS + "条");
        }

        map.put("prCount", String.valueOf(prCount));

        //文件上传112, 41
        HostInfo host41 = remoteHostConfig.getHosts().get("host41").setFilename(newFileName);
        HostInfo host112 = remoteHostConfig.getHosts().get("host112").setFilename(newFileName);
        this.uploadRemoteFile(host41, file);  //41
//        this.uploadRemoteFile(host112, file);  //112
        return CompletableFuture.completedFuture(map);
    }

    /**
     * 校验文件上传格式
     *
     * @param file     文件
     * @param dataType 数据类型（问答对, 意图识别...）
     * @param setType  数据集类型 (测试集, 训练集)
     */

    public void validateFile(MultipartFile file, String dataType, String setType) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件为空");
        }

        //Workbook workbook = WorkbookFactory.create(file.getInputStream()); 要改成这样才行
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                throw new RuntimeException("文件第一行为空");
            }

            String one = headerRow.getCell(0) != null ? headerRow.getCell(0).getStringCellValue().trim() : "";
            String two = headerRow.getCell(1) != null ? headerRow.getCell(1).getStringCellValue().trim() : "";
            String three = headerRow.getCell(2) != null ? headerRow.getCell(2).getStringCellValue().trim() : "";
            String four = Optional.ofNullable(headerRow.getCell(3))
                    .map(Cell::getStringCellValue).map(String::trim)
                    .orElse("");
            log.info("=======setType【{}】, dataType【{}】, excel表格SheetHeader【{}, {}, {}, {}】=======", setType, dataType, one, two, three, four);
            switch (setType) {
                case CommonConstant.TEST_DATA_SET:  //测试集
                    switch (dataType) {
                        case CommonConstant.PROMPT_RESPONSE:
                            log.info("======校验-问答对-测试集=======");
                            if (!one.equalsIgnoreCase(SheetHeaderConstant.TEST_PROMPT_RESPONSE_ONE) ||
                                    !two.equalsIgnoreCase(SheetHeaderConstant.TEST_PROMPT_RESPONSE_TWO) ||
                                    !three.equalsIgnoreCase(SheetHeaderConstant.TEST_PROMPT_RESPONSE_THREE))
                                throw new RuntimeException("请上传正确的问答对测试集模板");
                            break;

                        case CommonConstant.PROMPT_CATEGORY:
                            log.info("======校验-意图识别-测试集=======");
                            if (!one.equalsIgnoreCase(SheetHeaderConstant.TEST_PROMPT_CATEGORY_ONE) ||
                                    !two.equalsIgnoreCase(SheetHeaderConstant.TEST_PROMPT_CATEGORY_TWO) ||
                                    !three.equalsIgnoreCase(SheetHeaderConstant.TEST_PROMPT_CATEGORY_THREE))
                                throw new RuntimeException("请上传正确的意图识别测试集模板");
                            break;
                    }
                    break;

                case CommonConstant.TRAIN_DATA_SET:  //训练集
                    switch (dataType) {
                        case CommonConstant.PROMPT_RESPONSE:
                            log.info("======校验-问答对-训练集=======");
                            if (!one.equalsIgnoreCase(SheetHeaderConstant.TRAIN_PROMPT_RESPONSE_ONE) ||
                                    !two.equalsIgnoreCase(SheetHeaderConstant.TRAIN_PROMPT_RESPONSE_TWO) ||
                                    !three.equalsIgnoreCase(SheetHeaderConstant.TRAIN_PROMPT_RESPONSE_THREE) ||
                                    !four.equalsIgnoreCase(SheetHeaderConstant.TRAIN_PROMPT_RESPONSE_FOUR))
                                throw new RuntimeException("请上传正确的问答对训练集模板");
                            break;

                        case CommonConstant.PROMPT_CATEGORY:
                            log.info("======校验-意图识别-训练集=======");
                            if (!one.equalsIgnoreCase(SheetHeaderConstant.TRAIN_PROMPT_CATEGORY_ONE) ||
                                    !two.equalsIgnoreCase(SheetHeaderConstant.TRAIN_PROMPT_CATEGORY_TWO) ||
                                    !three.equalsIgnoreCase(SheetHeaderConstant.TRAIN_PROMPT_CATEGORY_THREE))
                                throw new RuntimeException("请上传正确的意图识别训练集模板");
                            break;
                    }
                    break;
            }

        } catch (IOException e) {
            throw new RuntimeException("读取文件时发生错误", e);
        }
    }


    /**
     * 检查文件后缀是否为 xlsx 或 jsonl
     *
     * @param fileExtension 文件后缀
     * @return 文件后缀校验
     */
    public static String getValidExtension(String fileExtension) {
        if ((fileExtension != null) && (fileExtension.equals("xlsx") || fileExtension.equals("jsonl"))) {
            return fileExtension;
        }
        throw new RuntimeException("请上传xlsx或json文件");
    }


    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return 文件后缀
     */
    public static String getFileExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        }
        return null;
    }


    /**
     * excel前10条问答转化为Json context
     *
     * @param file 文件
     * @return context内容json格式
     * @throws IOException
     */
    public static String convertXlsxToJson(File file, String setType, String dataType, String savePath) throws IOException {
        List<JSONObject> list = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(file);
//        Workbook workbook = new XSSFWorkbook(inputStream);
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0); // 假设只有一个工作表
        int rowCount = 0;
        if (CommonConstant.TRAIN_DATA_SET.equals(setType)) {
            rowCount = Math.min(getNonEmptyFirstCellRowCount(sheet), 10); // 取前11行或全部行
        } else {
            rowCount = getNonEmptyFirstCellRowCount(sheet);
        }

        for (int i = 1; i <= rowCount; i++) { // 从第2行开始
            Row row = sheet.getRow(i);
            if (row != null) {
                String one = getValueFromCell(row.getCell(0)).trim();
                if (one.contains(".")) {
                    one = one.substring(0, one.indexOf(".")); // 第1列作为对话索引
                }

                String two = getValueFromCell(row.getCell(1)).trim();
                if (two.contains(".")) {
                    two = two.substring(0, two.indexOf(".")); // 第1列作为对话索引
                }

                String three = getValueFromCell(row.getCell(2)); // 第3列作为键
                String four = getValueFromCell(row.getCell(3)); // 第4列作为值
                String five = getValueFromCell(row.getCell(4)); // 第5列作为值
                String six = getValueFromCell(row.getCell(5)); // 第6列作为值
                String seven = getValueFromCell(row.getCell(6)); // 第7列作为值
                String eight = getValueFromCell(row.getCell(7)); // 第8列作为值
                String nine = getValueFromCell(row.getCell(8)); // 第9列作为值
                String ten = getValueFromCell(row.getCell(9)); // 第10列作为值
                String eleven = getValueFromCell(row.getCell(10)); // 第11列作为值
                String twelve = getValueFromCell(row.getCell(11)); // 第12列作为值
                String thirteen = getValueFromCell(row.getCell(12)); // 第13列作为值
                String fourteen = getValueFromCell(row.getCell(13)); // 第14列作为值

                JSONObject object = new JSONObject();
                if (CommonConstant.PROMPT_RESPONSE.equals(dataType) && CommonConstant.TRAIN_DATA_SET.equals(setType)) {
                    object.put("questionId", Convert.toInt(one));
                    object.put("rank", Convert.toInt(two));
                    object.put("prompt", three);
                    object.put("response", four);
                } else if (CommonConstant.PROMPT_RESPONSE.equals(dataType) && CommonConstant.TEST_DATA_SET.equals(setType)) {
                    object.put("questionId", Convert.toInt(one));
                    object.put("prompt", two);
                    object.put("response", three);
                } else if (CommonConstant.PROMPT_CATEGORY.equals(dataType)) {
                    object.put("id", i);
                    object.put("questionRole", one);
                    object.put("prompt", two);
                    object.put("category", three);
                } else if (CommonConstant.PROMPT_SEQUENTIAL.equals(dataType)) {
                    object.put("circuitId", one);
                    object.put("cirName", two);
                    object.put("kDevId", three);
                    object.put("kIntfId", four);
                    object.put("devidIp", five);
                    object.put("bDevId", six);
                    object.put("bIntfDescr", seven);
                    object.put("bDevidIp", eight);
                    object.put("dCirbw", nine);
                    object.put("dInFlux", ten);
                    object.put("dOutFlux", eleven);
                    object.put("dInFluxRatio", twelve);
                    object.put("dOutFluxRatio", thirteen);
                    object.put("tCtime", fourteen);
                }

                list.add(object);
            }
        }

        //TODO 远程jsonl
//        if (CommonConstant.PROMPT_RESPONSE.equals(dataType)) {
//            convertPromptResXlsxToJsonl(sheet, savePath);
//        }

        workbook.close();
        inputStream.close();
        return list.toString();
    }


    /**
     * 获取首格非空行
     *
     * @param sheet 表格
     * @return 非空行数
     */
    public static int getNonEmptyFirstCellRowCount(Sheet sheet) {
        int nonEmptyRowCount = 0;

        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell firstCell = row.getCell(0); // 获取该行的第一个单元格
                if (firstCell != null && firstCell.getCellType() != CellType.BLANK) {
                    nonEmptyRowCount++; // 如果第一个单元格非空，则计数加一
                }
            }
        }

        return nonEmptyRowCount;
    }

    /**
     * 问答对excel转化为jsonl文件
     *
     * @param sheet    xlsx文件
     * @param filePath 文件绝对路径
     */
    private static void convertPromptResXlsxToJsonl(Sheet sheet, String filePath) {
        if (null == sheet || null == filePath) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                String three = getValueFromCell(row.getCell(2)).trim(); // 第3列作为键
                String four = getValueFromCell(row.getCell(3)).trim(); // 第4列作为值
//                String item = "{\"input\":\"\",\"instruction\":[{\"from\":\"user\",\"value\":\"" + three + "\"},{\"from\":\"assistant\",\"value\":\"" + four + "\"}]}";
                String item = "{\"instruction\":\"" + three + "\",\"input\":\"\",\"output\":\"" + four + "\"}";

                stringBuilder.append(item);
            }
        }
        stringBuilder.append("]");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.replace(".xlsx", ".json")))) {
            writer.write(stringBuilder.toString()
                    .replaceAll("\\r\\n|\\r|\\n", "")
                    .replaceAll("\\s+", ""));
        } catch (IOException e) {
            log.error("xlsx文件转json失败", e);
        }
    }


    /**
     * xlsx从单元格中获取值
     *
     * @param cell excel单元格
     * @return 单元格值
     */
    private static String getValueFromCell(Cell cell) {
        if (null == cell) {
            return "";
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            return "";
        }
    }


    /**
     * 获取问答对数量(单轮)
     *
     * @param file 文件
     * @return conversation 数量
     */
    public static Long getJsonPrCount(MultipartFile file) {
        Long size = 0L;
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                throw new IllegalArgumentException("文件不能为空");
            }

            byte[] bytes = file.getBytes();
            String content = new String(bytes, StandardCharsets.UTF_8);

            content = content.replaceAll("\\s+", "");
            JSONArray array = JSONArray.parseArray(content);

            // 获取数组大小
            size = (long) array.size();

        } catch (IOException e) {
            throw new RuntimeException("读取文件内容时发生错误", e);
        } catch (Exception e) {
            throw new RuntimeException("处理 JSON 数据时发生错误", e);
        }

        return size;
    }


    /**
     * 获取conversation数
     *
     * @param file     上传文件
     * @param dataType 数据类型（1prompt_response数据集; 2时序数据集; 3意图识别）
     * @return conversations数量
     */
    public static Long getXlsxPrCount(MultipartFile file, String dataType, String setType) {
        int rowCount = 1;
        long start = System.currentTimeMillis();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 第1个sheet
            int lastRowNum = sheet.getLastRowNum();  // 最后一行

            if (CommonConstant.PROMPT_RESPONSE.equals(dataType) && CommonConstant.TRAIN_DATA_SET.equals(setType)) {
                for (int i = 1; i < lastRowNum; i++) {
                    String trim = getValueFromCell(sheet.getRow(i).getCell(1)).trim();  // 第2列， "1" 的个数
                    String value = trim;
                    if (trim.contains(".")) {
                        value = trim.substring(0, trim.indexOf("."));
                    }

                    if ("1".equals(value)) {
                        rowCount++;
                    }
                }
            } else if (CommonConstant.PROMPT_RESPONSE.equals(dataType) && CommonConstant.TEST_DATA_SET.equals(setType)) {
                rowCount = lastRowNum;
            } else if (CommonConstant.PROMPT_SEQUENTIAL.equals(dataType)) {
                rowCount = lastRowNum;
            } else if (CommonConstant.PROMPT_CATEGORY.equals(dataType)) {
                rowCount = lastRowNum;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            long end = System.currentTimeMillis();
            log.info("统计对话数执行时间： " + (end - start) + "ms");
        }

        return (long) rowCount;
    }

    public static Map<String, Object> getXlsxCheck(MultipartFile file, String dataType, String setType) {
        Map<String, Object> map = new HashMap<>();
        boolean judge = true;
        StringBuilder msg = new StringBuilder();
        long start = System.currentTimeMillis();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 第1个sheet
            int lastRowNum = sheet.getLastRowNum();  // 最后一行
            Set<String> set = new HashSet<>();
            int errorCount = 0;
            if (lastRowNum == 0) {
                judge = false;
                errorCount++;
                msg.append("上传内容不能为空\n");
            }
            if (CommonConstant.PROMPT_RESPONSE.equals(dataType) && CommonConstant.TRAIN_DATA_SET.equals(setType)) {
                for (int i = 1; i <= lastRowNum && errorCount < 10; i++) {
                    if (sheet.getRow(i) == null) {
                        judge = false;
                        errorCount++;
                        msg.append("第").append(i + 1).append("行，内容不能为空。\n");
                        continue;
                    }
                    //首尾空格
                    for (int j = 0; j < 4 && errorCount < 10; j++) {
                        String value = getValueFromCell(sheet.getRow(i).getCell(j));
                        if (!value.equals(value.trim())) {
                            judge = false;
                            errorCount++;
                            msg.append("第").append(i + 1).append("行，第").append(j + 1).append("列，首尾有空格。\n");
                        }
                        if (StringUtils.isEmpty(value)) {
                            judge = false;
                            errorCount++;
                            msg.append("第").append(i + 1).append("行，第").append(j + 1).append("列，内容不能为空。\n");
                        }
                    }
                    //是否整数字
                    for (int j = 0; j < 2 && errorCount < 10; j++) {
                        String value = getValueFromCell(sheet.getRow(i).getCell(j));
                        try {
                            Double d = Double.valueOf(value);
                            String integer = value.substring(0, value.indexOf("."));
                            if (!Double.valueOf(integer).equals(d)) {
                                judge = false;
                                errorCount++;
                                msg.append("第").append(i + 1).append("行，第").append(j + 1).append("列，必须为整型。\n");
                            }
                        } catch (NumberFormatException e) {
                            judge = false;
                            errorCount++;
                            msg.append("第").append(i + 1).append("行，第").append(j + 1).append("列，必须为数字。\n");
                        }
                    }
                    //多轮问答序号和问答轮数重复
                    String seq = getValueFromCell(sheet.getRow(i).getCell(0)) + getValueFromCell(sheet.getRow(i).getCell(1));
                    if (!set.add(seq)) {
                        judge = false;
                        errorCount++;
                        msg.append("第").append(i + 1).append("行，序号和问答轮数重复。\n");
                    }
                    //判断问题和答案不能为数字
                    for (int j = 2; j < 4 && errorCount < 10; j++) {
                        String value = getValueFromCell(sheet.getRow(i).getCell(j));
                        if (value.matches("-?\\d+(\\.\\d+)?")) {
                            judge = false;
                            errorCount++;
                            msg.append("第").append(i + 1).append("行，第").append(j + 1).append("列，不能为纯数字。\n");
                        }
                    }
                }
            } else if (CommonConstant.PROMPT_RESPONSE.equals(dataType) && CommonConstant.TEST_DATA_SET.equals(setType)) {
                for (int i = 1; i <= lastRowNum && errorCount < 10; i++) {
                    if (sheet.getRow(i) == null) {
                        judge = false;
                        errorCount++;
                        msg.append("第").append(i + 1).append("行，内容不能为空。\n");
                        continue;
                    }
                    //首尾空格
                    for (int j = 0; j < 3; j++) {
                        String value = getValueFromCell(sheet.getRow(i).getCell(j));
                        if (!value.equals(value.trim())) {
                            judge = false;
                            errorCount++;
                            msg.append("第").append(i + 1).append("行，第").append(j + 1).append("列，首尾有空格。\n");
                        }
                        if (StringUtils.isEmpty(value)) {
                            judge = false;
                            errorCount++;
                            msg.append("第").append(i + 1).append("行，第").append(j + 1).append("列，内容不能为空。\n");
                        }
                    }
                    //是否整数字
                    for (int j = 0; j < 1 && errorCount < 10; j++) {
                        String value = getValueFromCell(sheet.getRow(i).getCell(j));
                        try {
                            Double d = Double.valueOf(value);
                            String integer = value.substring(0, value.indexOf("."));
                            if (!Double.valueOf(integer).equals(d)) {
                                judge = false;
                                errorCount++;
                                msg.append("第").append(i + 1).append("行，第").append(j + 1).append("列，必须为整型。\n");
                            }
                        } catch (NumberFormatException e) {
                            judge = false;
                            errorCount++;
                            msg.append("第").append(i + 1).append("行，第").append(j + 1).append("列，必须为数字。\n");
                        }
                    }
                    //判断问题和答案不能为数字
                    for (int j = 1; j < 3 && errorCount < 10; j++) {
                        String value = getValueFromCell(sheet.getRow(i).getCell(j));
                        if (value.matches("-?\\d+(\\.\\d+)?")) {
                            judge = false;
                            errorCount++;
                            msg.append("第").append(i + 1).append("行，第").append(j + 1).append("列，不能为纯数字。\n");
                        }
                    }
                    //问题ID一致
                    String seq = getValueFromCell(sheet.getRow(i).getCell(0));
                    if (!set.add(seq)) {
                        judge = false;
                        errorCount++;
                        msg.append("第").append(i + 1).append("行，问题ID重复。\n");
                    }
                }
            }
            map.put("judge", judge);
            map.put("msg", msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            long end = System.currentTimeMillis();
            log.info("对话校验合规项执行时间： " + (end - start) + "ms");
        }
        return map;
    }

    /**
     * 合并多个JSON文件获取结果
     *
     * @param directoryPath 文件保存目录
     * @param filePaths     文件地址
     * @return 合并后的JSON
     */
    public static Map<String, Object> mergeJSONFilesResult(String directoryPath, List<String> filePaths) {
        String mergeFilePath = directoryPath + UUID.randomUUID() + ".jsonl";
        Map<String, Object> map = MapUtil.newHashMap(true);
        JSONArray jsonArray = new JSONArray();
        String content = null;
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(mergeFilePath)), StandardCharsets.UTF_8))) {
            for (String filePath : filePaths) {
                try {
                    byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                    content = new String(bytes, StandardCharsets.UTF_8);
                    if (CharSequenceUtil.isNotBlank(content)) {
                        JSONArray array = JSONArray.parseArray(content.trim());
                        for (int i = 0; i < array.size(); i++) {
                            jsonArray.add(array.getJSONObject(i));
                        }
                    }
                } catch (IOException e) {
                    log.error("无法读取文件:" + filePath + ";" + e.getMessage());
                    new File(filePath).delete();
                    throw new RuntimeException("无法读取文件:" + filePath);
                } finally {
                    // 确保在结束时关闭文件流
                    new File(filePath).delete();
                    if (content != null) {
                        content = null; // 释放content对象的引用，以便垃圾回收器回收内存
                    }
                }
            }
            int min = Math.min(jsonArray.size(), 10);
            map.put("prCount", jsonArray.size());
            map.put("mergeFilePath", mergeFilePath);
            map.put("context", convertPrContext(jsonArray, min));
            writer.write(jsonArray.toString());
        } catch (IOException e) {
            log.error("json文件转化异常");
            throw new RuntimeException("无法写入文件:" + mergeFilePath);
        }
        log.info("合并后的JSON文件已生成, 地址：{}", mergeFilePath);
        return map;
    }


    /**
     * 获取PrContext字符串
     *
     * @param jsonArray jsonArray
     * @param size      数组取值大小
     * @return PrContext字符串
     */
    private static String convertPrContext(JSONArray jsonArray, int size) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < size; i++) {
            JSONArray conversationArray = JSONArray.parseArray(jsonArray.getJSONObject(i).getString("conversations"));
            JSONObject resultObj = new JSONObject();
            String prompt = "";
            String response = "";
            for (int j = 0; j < conversationArray.size(); j++) {
                if ("user".equals(conversationArray.getJSONObject(j).getString("role"))) {
                    prompt = conversationArray.getJSONObject(j).getString("content");
                }
                if ("assistant".equals(conversationArray.getJSONObject(j).getString("role"))) {
                    response = conversationArray.getJSONObject(j).getString("content");
                }
            }

            resultObj.put("questionId", Convert.toStr(i + 1));
            resultObj.put("rank", Convert.toStr(1));
            resultObj.put("prompt", prompt);
            resultObj.put("response", response);

            array.add(resultObj);
        }
        return array.toString();
    }


    public void uploadRemoteFile(HostInfo hostInfo, MultipartFile file) {
        log.info(hostInfo.getIp() + "远程数据集文件是否启用：" + hostInfo.isFlag());
        if (hostInfo.isFlag()) {
            try (InputStream inputStream = file.getInputStream()) {
                session = connectToRemoteHost(hostInfo);

                if (channelSftp == null || channelSftp.isClosed()) {
                    channelSftp = (ChannelSftp) session.openChannel("sftp");
                    channelSftp.connect();
                }
                if (StringUtils.isNotBlank(hostInfo.getDirectory())) {
                    channelSftp.cd(hostInfo.getDirectory());
                }

                channelSftp.put(inputStream, hostInfo.getFilename());

                log.info("SFTP文件上传远程机器成功: {} ;文件: {} -> {}", hostInfo.getIp(), file.getOriginalFilename(), hostInfo.getDirectory());
            } catch (JSchException | SftpException | IOException e) {
                log.error("SFTP文件上传远程机器失败: {};文件: {}; {}", hostInfo.getIp(), file.getOriginalFilename(), e);
                throw new RuntimeException("SFTP文件上传远程机器失败: " + hostInfo.getIp() + ":" + hostInfo.getDirectory(), e);
            } finally {
                disconnectFromRemoteHost(hostInfo);
            }
        }
    }

    public void deleteRemoteFile(HostInfo hostInfo) {
        log.info(hostInfo.getIp() + "远程数据集文件是否启用：" + hostInfo.isFlag());
        if (hostInfo.isFlag()) {
            try {
                session = connectToRemoteHost(hostInfo);

                if (channelSftp == null || channelSftp.isClosed()) {
                    channelSftp = (ChannelSftp) session.openChannel("sftp");
                    channelSftp.connect();
                }

                ChannelSftp sftpChannel = (ChannelSftp) channelSftp;

                sftpChannel.rm(hostInfo.getDirectory() + File.separator + hostInfo.getFilename()); // 删除远程文件

                log.info("SFTP远程机器{} 删除文件成功: {}{}", hostInfo.getIp(), hostInfo.getDirectory(), hostInfo.getFilename());
            } catch (JSchException | SftpException e) {
                log.error("SFTP远程机器{} 删除文件失败: {}{}", hostInfo.getIp(), hostInfo.getDirectory(), hostInfo.getFilename());
//                throw new RuntimeException("远程机器删除文件失败");
            } finally {
                disconnectFromRemoteHost(hostInfo);
            }
        }
    }


    public byte[] downloadFileFromRemote(HostInfo hostInfo) throws Exception {
        try {
            session = connectToRemoteHost(hostInfo);

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            InputStream inputStream = channelSftp.get(hostInfo.getFilePath());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } finally {
            disconnectFromRemoteHost(hostInfo);
        }
    }

    public List<String> downloadFileToLines(HostInfo hostInfo) throws Exception {
        try {
            session = connectToRemoteHost(hostInfo);

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            log.info("-----下载路径：{}，文件名：{}---------",hostInfo.getDirectory(),hostInfo.getFilename());
            InputStream inputStream = channelSftp.get(hostInfo.getDirectory()+"/"+hostInfo.getFilename());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> lines=new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } finally {
            disconnectFromRemoteHost(hostInfo);
        }
    }


    public static File convertByteArrayToXlsxFile(byte[] data) throws IOException {
        // 创建一个临时文件，扩展名为 .xlsx
        File tempFile = File.createTempFile("tmp_", ".xlsx");

        // 将字节数组写入临时文件
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(data);
        }

        return tempFile;
    }


    private Session connectToRemoteHost(HostInfo hostInfo) {
        if (session == null || !session.isConnected()) {
            log.info("远程机器连接成功: {}", hostInfo.getIp());
            try {
                JSch jsch = new JSch();
                session = jsch.getSession(hostInfo.getUsername(), hostInfo.getIp(), hostInfo.getPort());
                session.setPassword(hostInfo.getPassword());
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
            } catch (JSchException e) {
                log.error("远程机器连接失败: {}", hostInfo.getIp());
                throw new RuntimeException("远程机器连接失败: " + hostInfo.getIp());
            }
        }
        return session;
    }


    public void disconnectFromRemoteHost(HostInfo hostInfo) {
        if (channelSftp != null && !channelSftp.isClosed()) {
            channelSftp.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
            session = null; // Release session
            channelSftp = null; // Release channelSftp
            log.info("远程机器已断开: {}", hostInfo.getIp());
        }
    }


    public static MultipartFile convertFileToMultipartFile(String filePath) throws IOException {
        File newFile = new File(filePath);

        // 创建 FileItemFactory
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(newFile.getName(), "text/plain", true, newFile.getName());

        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try (FileInputStream fis = new FileInputStream(newFile);
             OutputStream os = item.getOutputStream()) {
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException("文件转换异常！");
        }

        // 删除原始文件
        if (newFile.delete()) {
            log.info("临时文件已删除: " + filePath);
        } else {
            log.info("临时文件删除失败: " + filePath);
        }

        // 返回 CommonsMultipartFile
        return new CommonsMultipartFile(item);
    }


    public static String getFileName(String filePath) {
        int lastSeparatorIndex = filePath.lastIndexOf('/');

        if (lastSeparatorIndex == -1) {
            lastSeparatorIndex = filePath.lastIndexOf('\\');
        }

        return filePath.substring(lastSeparatorIndex + 1);
    }


}