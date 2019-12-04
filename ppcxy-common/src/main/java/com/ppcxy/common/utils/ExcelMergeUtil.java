package com.ppcxy.common.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelMergeUtil {


    /**
     * 获取合并单元格的值
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }

        return null;
    }

    /**
     * 如果excel是wps格式，获取合并单元格的cell时，cell会是null，此时不能用该方法，请用getMergedRegionValue(Sheet sheet, int row, int column)
     *
     * @param sheet
     * @param cell
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet, Cell cell) {
        return getMergedRegionValue(sheet, cell.getRowIndex(),
                cell.getColumnIndex());
    }

    /**
     * 判断合并了行
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static boolean isMergedRow(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row == firstRow && row == lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet  工作表
     * @param row    行下标
     * @param column 列下标
     * @return
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 如果excel是wps格式，获取合并单元格的cell时，cell会是null，此时不能用该方法，请用isMergedRegion(Sheet sheet, int row, int column)
     *
     * @param sheet
     * @param cell
     * @return
     * @description
     * @author yjy
     */
    public static boolean isMergedRegion(Sheet sheet, Cell cell) {
        int row = cell.getRowIndex();
        int column = cell.getColumnIndex();
        return isMergedRegion(sheet, row, column);
    }

    public static boolean isCellInRegion(int rowIndex, int colIndex,
                                         Region region) {
        if (rowIndex >= region.getFirstRow() && rowIndex <= region.getLastRow()) {
            if (colIndex >= region.getFirstColumn()
                    && colIndex <= region.getLastColumn()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCellInRegion(Cell cell, Region region) {
        return isCellInRegion(cell.getRowIndex(), cell.getColumnIndex(), region);
    }

    
    public static Region getMergedRegion(Sheet sheet, int rowIndex, int colIndex) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (rowIndex >= firstRow && rowIndex <= lastRow) {
                if (colIndex >= firstColumn && colIndex <= lastColumn) {
                    Region region = new Region();
                    region.setFirstRow(firstRow);
                    region.setLastRow(lastRow);
                    region.setFirstColumn(firstColumn);
                    region.setLastColumn(lastColumn);
                    return region;
                }
            }
        }
        return null;
    }

    public static Region getMergedRegion(Sheet sheet, Cell cell) {
        return getMergedRegion(sheet, cell.getRowIndex(), cell.getColumnIndex());
    }

    /**
     * 判断sheet页中是否含有合并单元格
     *
     * @param sheet
     * @return
     */
    public static boolean hasMerged(Sheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }

    /**
     * 合并单元格
     *
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow  结束行
     * @param firstCol 开始列
     * @param lastCol  结束列
     */
    public static void mergeRegion(Sheet sheet, int firstRow, int lastRow,
                                   int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol,
                lastCol));
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {

        if (cell == null)
            return "";

        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

            return cell.getStringCellValue();

        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {

            return String.valueOf(cell.getBooleanCellValue());

        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

            return cell.getCellFormula();

        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return "";
    }


    /**
     * 判断Row(行)是否为空行(行本身为null或行中的单元格全部为null)
     *
     * @param row
     * @return
     */
    public static boolean isEmptyRow(Row row) {
        if (row != null) {
            short lastCellNum = row.getLastCellNum();
            if (lastCellNum == 0) {// 如果不存在单元格则返回true
                return true;
            } else {
                // 空单元格的个数
                int emptyCellNum = 0;
                for (int i = 0; i < lastCellNum; i++) {
                    Cell cell = row.getCell(i);
                    if (isEmptyCell(cell)) {
                        emptyCellNum++;
                    }
                }
                if (emptyCellNum == lastCellNum) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 判断Row(行)是否存在空的单元格或者这行是否存在单元格
     *
     * @param row
     * @return
     */
    public static boolean rowContianEmptyCell(Row row) {
        if (row != null) {
            short lastCellNum = row.getLastCellNum();
            if (lastCellNum == 0) {// 如果不存在单元格则返回true
                return true;
            } else {
                for (int i = 0; i < lastCellNum; i++) {
                    Cell cell = row.getCell(i);
                    if (isEmptyCell(cell)) {
                        return true;
                    }
                }
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 判断Sheet是否存在空的行或存在空数据的行
     *
     * @param sheet
     * @return
     */
    public static boolean sheetContainEmptyRow(Sheet sheet) {
        if (sheet != null) {
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum == 0) {// 如果不存在sheet则返回true
                return true;
            } else {
                for (int i = 0; i < lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (isEmptyRow(row)) {
                        return true;
                    }
                }
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 基于指定列数判断Sheet是否存在空的行或存在空数据的行
     *
     * @param sheet
     * @param columnNum
     * @return
     */
    public static boolean sheetContainEmptyRow(Sheet sheet, int columnNum) {
        if (sheet != null) {
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum == 0) {// 如果不存在sheet则返回true
                return true;
            } else {
                if (lastRowNum >= columnNum) {
                    for (int i = 0; i < columnNum; i++) {
                        Row row = sheet.getRow(i);
                        if (isEmptyRow(row)) {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 获取表格中空行的行号
     *
     * @param sheet
     * @return
     */
    public static List<Integer> getEmptyRowNos(Sheet sheet) {
        List<Integer> list = new ArrayList<>();
        if (sheet != null) {
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum != 0) {// 如果不存在sheet则返回true
                for (int i = 0; i < lastRowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (isEmptyRow(row)) {
                        list.add(i);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 判断Cell(单元格)是否为空
     *
     * @param cell
     * @return
     */
    public static boolean isEmptyCell(Cell cell) {
        String cellContent = getCellValue(cell);
        if (StringUtils.hasText(cellContent)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 关闭workbook
     *
     * @param workbook
     */
    public static void closeWorkbook(Workbook workbook) {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addDataToRow(Row row, List<String> values) {
        if (values != null && !values.isEmpty()) {
            for (int i = 0; i < values.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(values.get(i));
            }
        }
    }


}
