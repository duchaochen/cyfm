package com.ppcxy.manage.maintain.datasource.conf;

/**
 * Created by weep on 2016-5-23.
 */
public enum DataSourceType {
    nocache("按需创建链接"), cache("按需创建缓存链接");
    
    private final String info;
    
    private DataSourceType(String info) {
        this.info = info;
    }
    
    public String getInfo() {
        return info;
    }
}
