package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetailVO;
import com.ctdi.cnos.llm.util.ModelUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yuyong
 * @date 2024/8/19 10:18
 */
public class ExcelValueCounter {
    private static final String FILE_PATH = "C:\\Users\\lenovo\\Desktop\\工作簿1.xlsx"; // Excel 文件路径
    private static final int COLUMN_INDEX = 1;            // 要统计的列索引（从0开始）
    private static final String VALUE_TO_COUNT = "2"; // 要统计的值
    // 时间节点维度
    private static final int EXPECTED_INTERVAL_MINUTES = 5;
    private static final long EXPECTED_INTERVAL_MILLIS = EXPECTED_INTERVAL_MINUTES * 60 * 1000;
    // 最大缺失数量
    private static final int MAX_MISSING_NODES = 10;
    // 时序测试数据集最小数据量
    private static final int MIN_DATA_POINTS = 1024;

    public static void main(String[] args) {
//        try {
//            Map<String, Integer> valueCounts = countValueOccurrences(FILE_PATH, COLUMN_INDEX);
//            System.out.println("值 '" + VALUE_TO_COUNT + "' 出现的次数: " + valueCounts.getOrDefault(VALUE_TO_COUNT, 0));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 解析excel数据
        List<PromptSequentialDetailVO> dataPoints = readDataFromExcel(FILE_PATH);
        // 组装excel数据
//        List<PromptSequentialDetail> processedData = processDataPoints(dataPoints);
        List<PromptSequentialDetail> processedData = processData(dataPoints);
//        System.out.println("abc:" + processedData.size());

        int numIntervalsToFill = (int) (20 / 5) + 1;
        System.out.println(numIntervalsToFill);

        List<String> circuitIdList = new ArrayList<>();
        circuitIdList.add("1");
        circuitIdList.add("1");
        circuitIdList.add("1");
        System.out.println(circuitIdList.size());
//        String timeStr = "Tue Aug 27 21:08:00 CST 2024";
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
//        Date nowTime = null;
//        try {
//            nowTime = inputFormat.parse(timeStr);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        try {
//            System.out.println(dateFormat.parse(dateFormat.format(nowTime)));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    private static Map<String, Integer> countValueOccurrences(String filePath, int columnIndex) throws IOException {
        Map<String, Integer> counts = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell cell = row.getCell(columnIndex);
                if (cell != null) {
                    String cellValue = getCellValue(cell);
                    counts.put(cellValue, counts.getOrDefault(cellValue, 0) + 1);
                }
            }
        }

        if(counts.getOrDefault(VALUE_TO_COUNT, 0) < 20){
            System.out.println("问答对少于10，请重新输入!");
        }



        return counts;
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

    private static List<PromptSequentialDetailVO> readDataFromExcel(String filePath) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PromptSequentialDetailVO> dataPoints = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if(row.getRowNum() == 0) continue;
                boolean isEmptyRow = true;
                for (Cell cell : row) {
                    if (cell.getCellType() != CellType.BLANK) {
                        isEmptyRow = false;
                        break;
                    }
                }
                if(!isEmptyRow){
                    if (row != null) {
                        PromptSequentialDetailVO vo = new PromptSequentialDetailVO();

                        Cell circuitId = row.getCell(0);
                        Cell cirName = row.getCell(1);
                        Cell kDevId = row.getCell(2);
                        Cell kIntfId = row.getCell(3);
                        Cell devidIp = row.getCell(4);
                        Cell bDevId = row.getCell(5);
                        Cell bIntfDescr = row.getCell(6);
                        Cell bDevidIp = row.getCell(7);
                        DataFormatter dataFormatter = new DataFormatter();
                        System.out.println(dataFormatter.formatCellValue(row.getCell(8)));
//                        Cell dCirbw = row.getCell(8);
//                        Cell dInFlux = row.getCell(9);
                        System.out.println(dataFormatter.formatCellValue(row.getCell(9)));
//                        Cell dOutFlux = row.getCell(10);
                        System.out.println(dataFormatter.formatCellValue(row.getCell(10)));
//                        Cell dInFluxRatio = row.getCell(11);
                        System.out.println(dataFormatter.formatCellValue(row.getCell(11)));
//                        Cell dOutFluxRatio = row.getCell(12);
                        System.out.println(dataFormatter.formatCellValue(row.getCell(12)));
                        Cell tCtime = row.getCell(13);

                        vo.setCircuitId(circuitId.getStringCellValue());
                        vo.setCirName(cirName.getStringCellValue());
                        vo.setKDevId(kDevId.getStringCellValue());
                        vo.setKIntfId(kIntfId.getStringCellValue());
                        vo.setDevidIp(devidIp.getStringCellValue());
                        vo.setBDevId(bDevId.getStringCellValue());
                        vo.setBIntfDescr(bIntfDescr.getStringCellValue());
                        vo.setBDevidIp(bDevidIp.getStringCellValue());
//                        double dCirbws = dCirbw.getNumericCellValue();
//                        double dInFluxs = dInFlux.getNumericCellValue();
//                        double dOutFluxs = dOutFlux.getNumericCellValue();
//                        double dInFluxRatios = dInFluxRatio.getNumericCellValue();
//                        double dOutFluxRatios = dOutFluxRatio.getNumericCellValue();
                        vo.setDCirbw(dataFormatter.formatCellValue(row.getCell(8)));
                        vo.setDInFlux(dataFormatter.formatCellValue(row.getCell(9)));
                        vo.setDOutFlux(dataFormatter.formatCellValue(row.getCell(10)));
                        vo.setDInFluxRatio(dataFormatter.formatCellValue(row.getCell(11)));
                        vo.setDOutFluxRatio(dataFormatter.formatCellValue(row.getCell(12)));
                        vo.setTCtime(tCtime.getDateCellValue());
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

//    private static List<PromptSequentialDetail> processDataPoints(List<PromptSequentialDetailVO> dataPoints) {
//        List<PromptSequentialDetail> result = new ArrayList<>();
//        if (dataPoints.isEmpty()) return result;
//
//        dataPoints.sort(Comparator.comparing(PromptSequentialDetail::getTCtime));
//        PromptSequentialDetail previous = dataPoints.get(0);
//        result.add(previous);
//
//        for (int i = 1; i < dataPoints.size(); i++) {
//            PromptSequentialDetailVO current = dataPoints.get(i);
//            long interval = (current.getTCtime().getTime() - previous.getTCtime().getTime()) / (1000 * 60);
//
//            if (interval % EXPECTED_INTERVAL_MINUTES != 0) {
////                int missingCount = (int) (Math.ceil(interval * 1.0 / EXPECTED_INTERVAL_MINUTES));
//                int missingCount = (int) (interval / EXPECTED_INTERVAL_MINUTES) + 1;
//                if (missingCount > MAX_MISSING_NODES) {
//                    System.err.println("File content error: More than " + MAX_MISSING_NODES + " missing nodes.");
//                    return Collections.emptyList(); // Return an empty list to indicate error
//                }
//                for (int j = 0; j < missingCount; j++) {
//                    Date missingTime = new Date(previous.getTCtime().getTime() + j + 1 * EXPECTED_INTERVAL_MINUTES * 60 * 1000);
//                    PromptSequentialDetail vo = previous;
//                    vo.setTCtime(missingTime);
//                    result.add(vo);
//                }
//            }
//
//            result.add(current);
//            previous = current;
//        }
//
//        return result;
//    }

//    public static List<PromptSequentialDetail> processData(List<PromptSequentialDetailVO> dataEntries) {
//        List<PromptSequentialDetail> processedEntries = new ArrayList<>();
//
//        // Sort entries by time
//        dataEntries.sort(Comparator.comparing(PromptSequentialDetailVO::getTCtime));
//
//        for (int i = 0; i < dataEntries.size(); i++) {
//            PromptSequentialDetailVO currentEntry = dataEntries.get(i);
//            Date currentTime = currentEntry.getTCtime();
//
//            if (i == 0) {
//                processedEntries.add(convertToDetail(currentEntry));
//                continue;
//            }
//
//            PromptSequentialDetail previousEntry = processedEntries.get(processedEntries.size() - 1);
//            Date previousTime = previousEntry.getTCtime();
//
//            long timeGap = currentTime.getTime() - previousTime.getTime();
//            long numIntervals = timeGap / EXPECTED_INTERVAL_MILLIS;
//
//            if (numIntervals > 1) {
//                // Check if missing intervals are acceptable
//                if (numIntervals - 1 > 10) {
//                    System.out.println("连续缺失超过 10 个节点！");
//                    break;
//                }
//
//                // Fill missing intervals
//                for (int j = 1; j < numIntervals; j++) {
//                    Date newTime = new Date(previousTime.getTime() + j * EXPECTED_INTERVAL_MILLIS);
//                    PromptSequentialDetail newEntry = ModelUtil.copyTo(previousEntry, PromptSequentialDetail.class);
//                    newEntry.setTCtime(newTime);
//                    processedEntries.add(newEntry);
//                }
//            }
//
//            // Add the current entry
//            processedEntries.add(convertToDetail(currentEntry));
//        }
//
//        return processedEntries;
//    }
//
//    private static PromptSequentialDetail convertToDetail(PromptSequentialDetailVO entry) {
//        return ModelUtil.copyTo(entry, PromptSequentialDetail.class);
//    }

//    public static List<PromptSequentialDetail> processData(List<PromptSequentialDetailVO> dataEntries) {
//        List<PromptSequentialDetail> processedEntries = new ArrayList<>();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        dataEntries.sort(Comparator.comparing(PromptSequentialDetail::getTCtime));
//
//        for (int i = 0; i < dataEntries.size(); i++) {
//            PromptSequentialDetailVO currentEntry = dataEntries.get(i);
//            Date currentTime = currentEntry.getTCtime();
//
//            if (i == 0) {
//                processedEntries.add(currentEntry);
//                continue;
//            }
//            PromptSequentialDetail previousEntry = processedEntries.get(processedEntries.size() - 1);
//
//            Date previousTime = previousEntry.getTCtime();
//
//            long timeDifference = (currentTime.getTime() - previousTime.getTime()) / (1000 * 60);
//
//            long timeGap = currentTime.getTime() - previousTime.getTime();
//
//            int numIntervals = (int) (timeGap / (EXPECTED_INTERVAL_MINUTES * 60 * 1000));
//
//            if(numIntervals >= 1){
//                int missingCount = numIntervals - 1;
//                if(missingCount > 10){
//                    System.out.println("连续缺失超过 10 个节点！");
//                    break;
//                }{
//                    if (timeDifference % 5!= 0) {
//                        int numIntervalsToFill = (int) (timeDifference / 5) + 1;
//                        for (int j = 0; j < numIntervalsToFill; j++) {
//                            Date newTime = new Date(previousTime.getTime() + (j + 1) * 5 * 60 * 1000);
//                            PromptSequentialDetail newEntry = ModelUtil.copyTo(previousEntry, PromptSequentialDetail.class);
//                            if(j == numIntervalsToFill - 1){
//                                newEntry = ModelUtil.copyTo(currentEntry, PromptSequentialDetail.class);
//                            }
//                            newEntry.setTCtime(newTime);
//                            processedEntries.add(newEntry);
//                        }
//                    }
//                    if(timeDifference % 5 == 0){
//                        int numIntervalsToFill = (int) (timeDifference / 5);
//                        for (int j = 0; j < numIntervalsToFill; j++) {
//                            Date newTime = new Date(previousTime.getTime() + (j + 1) * 5 * 60 * 1000);
//                            PromptSequentialDetail newEntry = ModelUtil.copyTo(previousEntry, PromptSequentialDetail.class);
//                            if(newTime.getTime() == currentEntry.getTCtime().getTime()){
//                                newEntry = ModelUtil.copyTo(currentEntry, PromptSequentialDetail.class);
//                            }else{
//                                newEntry.setTCtime(newTime);
//                            }
//                            processedEntries.add(newEntry);
//                        }
//                    }
//                }
//            }
//        }
//        return processedEntries;
//    }

//    public static List<PromptSequentialDetail> processData(List<PromptSequentialDetailVO> dataEntries) {
//        List<PromptSequentialDetail> processedEntries = new ArrayList<>();
//        dataEntries.sort(Comparator.comparing(PromptSequentialDetailVO::getTCtime));
//
//        for (int i = 0; i < dataEntries.size(); i++) {
//            PromptSequentialDetailVO currentEntry = dataEntries.get(i);
//            Date currentTime = currentEntry.getTCtime();
//
//            if (i == 0) {
//                processedEntries.add(ModelUtil.copyTo(currentEntry, PromptSequentialDetail.class));
//                continue;
//            }
//
//            PromptSequentialDetail previousEntry = processedEntries.get(processedEntries.size() - 1);
//            Date previousTime = previousEntry.getTCtime();
//
//            long timeGapMillis = currentTime.getTime() - previousTime.getTime();
//            int numIntervals = (int) (timeGapMillis / EXPECTED_INTERVAL_MILLIS);
//
//            if (numIntervals > 1) {
//                if (numIntervals - 1 > 10) {
//                    System.out.println("连续缺失超过 10 个节点！");
//                    break;
//                }
//
//                for (int j = 1; j < numIntervals; j++) {
//                    Date newTime = new Date(previousTime.getTime() + j * EXPECTED_INTERVAL_MILLIS);
//                    PromptSequentialDetail newEntry = ModelUtil.copyTo(previousEntry, PromptSequentialDetail.class);
//                    newEntry.setTCtime(newTime);
//                    processedEntries.add(newEntry);
//                }
//            }
//
//            processedEntries.add(ModelUtil.copyTo(currentEntry, PromptSequentialDetail.class));
//        }
//
//        return processedEntries;
//    }

    public static List<PromptSequentialDetail> processData(List<PromptSequentialDetailVO> dataEntries) {
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

            int numIntervals = (int) (timeGap / EXPECTED_INTERVAL_MILLIS) + 1;

            if (numIntervals >= 1) {
                int missingCount = numIntervals - 1;
                if (missingCount > 10) {
                    System.out.println("连续缺失超过 10 个节点！");
                    break;
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
}
