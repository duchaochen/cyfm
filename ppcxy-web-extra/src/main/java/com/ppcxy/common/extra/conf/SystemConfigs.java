package com.ppcxy.common.extra.conf;

import org.springside.modules.utils.PropertiesLoader;

public class SystemConfigs {
    
    
    public static final String UPLOAD_BASE_DESTPATH;
    
    static {
        PropertiesLoader propertiesLoader = new PropertiesLoader("classpath:/application.properties");
        UPLOAD_BASE_DESTPATH = propertiesLoader.getProperty("sysconfig.update.savepath");
    }
    
    
    private SystemConfigs() {
    
    }
    
    
}
