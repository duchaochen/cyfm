package com.ppcxy.cyfm.dataview.support.excel;

import com.ppcxy.common.utils.excel.support.ExcelImportHandlerAbstract;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;


/**
 * 导入Excel
 */
public class ImportExcelDemo extends ExcelImportHandlerAbstract {
    
    /**
     * 日志
     */
    private static final Logger logger = Logger.getLogger(ImportExcelDemo.class);
    
    
    /**
     * 开始读取文件
     *
     * @param file_name
     */
    public void importExcel(String file_name) {
        
        // OLE添加excel_2007版本，如果excel_2007处理方式
        if (file_name.indexOf(".xlsx") > 1) {
            try {
                int sheetIndex = 0;
                processOneSheet(file_name, 1);
                //process(file_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        
        
    }
    
    /**
     * 读取行后调用次方法
     *
     * @param sheetIndex
     * @param curRow
     * @param rowlist
     * @throws SQLException
     */
    @Override
    public void optRows(int sheetIndex, int curRow, List<String> rowlist) throws SQLException {
        logger.debug(sheetIndex + " " + curRow + "-- " + rowlist);
    }
    
    /**
     * demo
     *
     * @param args
     */
    public static void main(String[] args) {
        ImportExcelDemo importExcelDemo = new ImportExcelDemo();
        importExcelDemo.importExcel("/Volumes/data/xxxx.xlsx");
    }
}
