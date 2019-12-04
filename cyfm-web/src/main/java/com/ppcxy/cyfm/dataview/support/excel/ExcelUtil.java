package com.ppcxy.cyfm.dataview.support.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.*;

public class ExcelUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    
    
    /**
     * 把excel里的列名A、B...换成序列1、2...
     *
     * @param letters
     * @return
     */
    public int letter2Number(String letters) {
        if (!letters.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Format ERROR!");
        }
        char[] chs = letters.toLowerCase().toCharArray();// a,b
        int result = 0;
        for (int i = chs.length - 1, p = 1; i >= 0; i--) {
            result += getNum(chs[i]) * p;
            p *= 26;
        }
        return result;
    }
    
    /**
     * 把excel里的列名1、2...换成序列A、B...
     *
     * @return
     * @param-letters
     */
    public static String getExcelColumnLabel(int num) {
        String temp = "";
        
        //make sure how many letters are there
        double i = Math.floor(Math.log(25.0 * (num) / 26.0 + 1) / Math.log(26)) + 1;
        if (i > 1) {
            double sub = num - 26 * (Math.pow(26, i - 1) - 1) / 25;
            for (double j = i; j > 0; j--) {
                temp = temp + (char) (sub / Math.pow(26, j - 1) + 65);
                sub = sub % Math.pow(26, j - 1);
            }
        } else {
            temp = temp + (char) (num + 65);
        }
        return temp;
    }
    
    private int getNum(char c) {
        return c - 'a' + 1;
    }
    
    /**
     * 打印一个sheet中的每个合并单元格区的位置
     *
     * @param sheet
     */
    public Map<String, List<Integer>> sysRange(Sheet sheet) {
        Map<String, List<Integer>> cellMap = new HashMap<String, List<Integer>>();
        
        int count = sheet.getNumMergedRegions();//找到当前sheet单元格中共有多少个合并区域  
        for (int i = 0; i < count; i++) {
            List<Integer> valueList = new ArrayList<Integer>();
            CellRangeAddress range = sheet.getMergedRegion(i);//一个合并单元格代表 CellRangeAddress  
//            System.out.println(range.formatAsString());//打印合并区域的字符串表示方法 如： B2:C4  
            
            valueList.add(range.getFirstRow());    //开始行
            valueList.add(range.getLastRow());     //结束行
            valueList.add(range.getFirstColumn()); //开始列
            valueList.add(range.getLastColumn());  //结束列
            cellMap.put(range.formatAsString().split(":")[0], valueList);
//            System.out.println(range.getFirstRow() + "." + range.getLastRow() + ":"   
//                    + range.getFirstColumn() + "." + range.getLastColumn() );//打印起始行、结束行、起始列、结束列   
        }
        return cellMap;
    }
    
    //获取标准单元格的值
    public String getCellValues(Cell aCell) {
        String info = "";
        try {
            int cellType = aCell.getCellType();
            switch (cellType) {//格式判断
                case Cell.CELL_TYPE_NUMERIC:
                    
                    if (DateUtil.isCellDateFormatted(aCell)) {// 读取日期格式
                        info = aCell.getNumericCellValue() + "";
                    } else {// 读取数字
                        info = aCell.getNumericCellValue() + "";
                        // info=NumberFormat.getInstance().format(aCell.getNumericCellValue())+"";//防止数字过大，读取时自动以科学计数法读取。
                        //info=info.replaceAll(",", "");
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    info = aCell.getRichStringCellValue().toString();
                    
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    try {
                        info = NumberFormat.getInstance().format(aCell.getNumericCellValue()) + "";
                        info = info.replaceAll(",", "");
                    } catch (Exception e) {
                        try {
                            info = aCell.getRichStringCellValue().toString();
                            logger.error("", e);
                        } catch (Exception e2) {
                            logger.error("", e2);
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("", e);
            logger.info("EXCEL字段格式转换有误！"); // 日志写入文件
        }
        return info;
    }
    
    //行合并单元格取值
    public String getMergedValues(Map<String, List<Integer>> cellMap,
                                  Map<String, String> cellValue, int rowNumOfSheet, int cellNumOfRow) {
        String info = "";
        Iterator it = cellMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = entry.getKey().toString();
            List<Integer> value = (List<Integer>) entry.getValue();
            if (rowNumOfSheet >= Integer.parseInt(value.get(0).toString()) &&
                    rowNumOfSheet <= Integer.parseInt(value.get(1).toString())) {
                if (cellNumOfRow >= Integer.parseInt(value.get(2).toString()) &&
                        cellNumOfRow <= Integer.parseInt(value.get(3).toString())) {
                    info = cellValue.get(key).toString();
                    break;
                }
            }
        }
        return info;
    }
    
    public String getCellValue(Cell aCell) {
        String info = "";
        try {
            int cellType = aCell.getCellType();
            switch (cellType) {//格式判断
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(aCell)) {// 读取日期格式
                        info = aCell.getNumericCellValue() + "";
                    } else {// 读取数字
                        info = aCell.getNumericCellValue() + "";
                        //info=NumberFormat.getInstance().format(aCell.getNumericCellValue())+"";//防止数字过大，读取时自动以科学计数法读取。
                        //info=info.replaceAll(",", "");
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    info = aCell.getRichStringCellValue().toString();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    try {
                        info = NumberFormat.getInstance().format(aCell.getNumericCellValue()) + "";
                        info = info.replaceAll(",", "");
                    } catch (Exception e) {
                        try {
                            info = aCell.getRichStringCellValue().toString();
                            logger.error("", e);
                        } catch (Exception e2) {
                            logger.error("", e2);
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("", e);
            logger.info("EXCEL字段格式转换有误！"); // 日志写入文件
        }
        return info;
    }
    
    
    public boolean allowNull(List<String> line, List<Integer> notNull) {
        for (Integer i : notNull) {
            if ("".equals(line.get(i).trim())) return false;
        }
        return true;
    }
    
    
}
