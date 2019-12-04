package com.ppcxy.common.utils.excel.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 修改数据库实际表结构
 * Created by weep on 2015-11-3.
 */
public class DataColumn implements Serializable {
    private String title;
    private String columnName;
    private Class columnType;
    private String refColumnName;
    private Integer order;
    
    public DataColumn() {
        this.order = 0;
    }
    
    public DataColumn(String title, String columnName) {
        this.title = title;
        this.columnName = columnName;
        this.order = 0;
    }
    
    public DataColumn(String title, String columnName, Class columnType) {
        this.title = title;
        this.columnName = columnName;
        this.columnType = columnType;
        this.order = 0;
    }
    
    public String getTitle() {
        return title == null ? "" : title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getColumnName() {
        return columnName;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public Class getColumnType() {
        return columnType;
    }
    
    public void setColumnType(Class columnType) {
        this.columnType = columnType;
    }
    
    public String getRefColumnName() {
        return refColumnName;
    }
    
    public void setRefColumnName(String refColumnName) {
        this.refColumnName = refColumnName;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
