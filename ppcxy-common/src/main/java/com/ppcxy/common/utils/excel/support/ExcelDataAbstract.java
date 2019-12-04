package com.ppcxy.common.utils.excel.support;

import com.google.common.collect.Lists;
import com.ppcxy.common.entity.AbstractEntity;
import com.ppcxy.common.utils.CamelCaseUtils;
import com.ppcxy.common.utils.CompressUtils;
import com.ppcxy.common.utils.excel.model.DataColumn;
import com.ppcxy.common.utils.excel.model.FieldMeta;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.springside.modules.utils.Collections3;
import org.springside.modules.utils.Reflections;

import javax.persistence.JoinColumn;
import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by weep on 2016-8-9.
 */
public abstract class ExcelDataAbstract {
    private final String storePath = "upload/excel";
    private final String EXPORT_FILENAME_PREFIX = "excel";
    
    /**
     * 导出文件的最大大小 超过这个大小会压缩
     */
    private final int MAX_EXPORT_FILE_SIZE = 10 * 1024 * 1024; //10MB
    
    protected String compressAndDeleteOriginal(final String filename) {
        String newFileName = FilenameUtils.removeExtension(filename) + ".zip";
        compressAndDeleteOriginal(newFileName, filename);
        return newFileName;
    }
    
    protected void compressAndDeleteOriginal(final String newFileName, final String... needCompressFilenames) {
        CompressUtils.zip(newFileName, needCompressFilenames);
        for (String needCompressFilename : needCompressFilenames) {
            FileUtils.deleteQuietly(new File(needCompressFilename));
        }
    }
    
    protected boolean needCompress(final File file) {
        return file.length() > MAX_EXPORT_FILE_SIZE;
    }
    
    /**
     * 生成要导出的文件名
     *
     * @param username
     * @param contextRootPath
     * @param extension
     * @return
     */
    protected String generateFilename(final String username, final String contextRootPath, final String extension) {
        return generateFilename(username, contextRootPath, null, extension);
    }
    
    /**
     * 生成要导出的文件名
     *
     * @param username
     * @param contextRootPath
     * @param index
     * @param extension
     * @return
     */
    private String generateFilename(final String username, final String contextRootPath, Integer index, final String extension) {
        String path = FilenameUtils.concat(contextRootPath, storePath);
        path = FilenameUtils.concat(path, username);
        path = FilenameUtils.concat(
                path,
                EXPORT_FILENAME_PREFIX + "_" +
                        DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") +
                        (index != null ? ("_" + index) : "") +
                        "." + extension);
        
        File file = new File(path);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            return path;
        }
        return generateFilename(username, contextRootPath, extension);
    }
    
    
    protected void setterCellValue(Cell cell, Object cellValue) {
        if (cellValue != null) {
            if (cellValue instanceof Collection) {
                String ids = Collections3.extractToString((Collection) cellValue, "id", ",");
                cell.setCellValue(ids);
            } else if (cellValue instanceof AbstractEntity) {
                String id = ((AbstractEntity) cellValue).getId().toString();
                cell.setCellValue(id);
            } else {
                switch (cellValue.getClass().toString()) {
                    case "class java.lang.String":
                        cell.setCellValue((String) cellValue);
                        break;
                    case "class java.util.Date":
                    case "class java.sql.Timestamp":
                        cell.setCellValue(DateFormatUtils.ISO_DATETIME_FORMAT.format((Date) cellValue));
                        break;
                    case "class java.math.BigDecimal":
                        cell.setCellValue(((BigDecimal) cellValue).doubleValue());
                        break;
                    case "class java.lang.Integer":
                        cell.setCellValue((Integer) cellValue);
                        break;
                    default:
                        if (cellValue.getClass().isEnum()) {
                            cell.setCellValue(Reflections.getFieldValue(cellValue, "info").toString());
                        } else {
                            cell.setCellValue(cellValue.toString());
                        }
                    
                }
            }
        }
    }
    
    /**
     * 根据类去初始化列信息
     *
     * @param dataClass
     */
    public List<DataColumn> initColumns(Class dataClass) {
        List<DataColumn> columns = Lists.newArrayList();
        
        if (columns.size() == 0) {
            Field[] fs = dataClass.getDeclaredFields();
            
            columns.add(new DataColumn("数据库主键", "id"));
            
            for (Field field : fs) {
                DataColumn dataColumn = new DataColumn();
                dataColumn.setColumnName(field.getName());
                //获取字段中包含fieldMeta的注解
                FieldMeta meta = field.getAnnotation(FieldMeta.class);
                if (meta != null) {
                    dataColumn.setTitle(meta.description());
                    if (meta.isRef()) {
                        dataColumn.setRefColumnName(meta.refColumn());
                    }
                }
                
                JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                if (joinColumn != null) {
                    String refColumnName = joinColumn.referencedColumnName();
                    if (StringUtils.isNotBlank(refColumnName)) {
                        String refColumn = CamelCaseUtils.toCamelCase(refColumnName);
                        dataColumn.setRefColumnName(refColumn);
                    }
                }
                columns.add(dataColumn);
            }
            
            Collections.sort(columns, new Comparator<DataColumn>() {
                @Override
                public int compare(DataColumn o1, DataColumn o2) {
                    return o1.getOrder() - o2.getOrder();
                }
            });
        }
        
        return columns;
    }
}
