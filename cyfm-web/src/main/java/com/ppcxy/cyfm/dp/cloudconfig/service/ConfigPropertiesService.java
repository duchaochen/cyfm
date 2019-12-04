package com.ppcxy.cyfm.dp.cloudconfig.service;

import com.ppcxy.common.service.BaseService;
import com.ppcxy.cyfm.dp.cloudconfig.entity.ConfigProperties;
import com.ppcxy.cyfm.dp.cloudconfig.repository.jpa.ConfigPropertiesDao;
import com.ppcxy.cyfm.manage.entity.maintaion.datasource.SourceManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ConfigPropertiesService extends BaseService<ConfigProperties, Long> {
    
    private static final String SOURCE_URL_KEY = "spring.datasource.url";
    private static final String SOURCE_USERNAME_KEY = "spring.datasource.username";
    private static final String SOURCE_PASSWORD_KEY = "spring.datasource.password";
    private static final String SOURCE_DRIVER_KEY = "spring.datasource.driver-class-name";
    
    private static final String SOURCE_APPLICATION_KEY = "datatask";
    
    private static final String SOURCE_LABLE_KEY = "master";
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private ConfigPropertiesDao configPropertiesDao;
    
    public String genDataTaskConfig(String application, String profile, SourceManage sourceManage) {
        
        String url = sourceManage.loadConUrl();
        
        ConfigProperties urlConfig = new ConfigProperties();
        urlConfig.setProfile(profile);
        urlConfig.setApplication(application);
        urlConfig.setLabel(SOURCE_LABLE_KEY);
        urlConfig.setKey(SOURCE_URL_KEY);
        urlConfig.setValue(url);
        
        baseRepository.save(urlConfig);
        
        
        ConfigProperties usernameConfig = new ConfigProperties();
        usernameConfig.setProfile(profile);
        usernameConfig.setApplication(application);
        usernameConfig.setLabel(SOURCE_LABLE_KEY);
        usernameConfig.setKey(SOURCE_USERNAME_KEY);
        usernameConfig.setValue(sourceManage.getDbUsername());
        
        baseRepository.save(usernameConfig);
        
        
        ConfigProperties passwordConfig = new ConfigProperties();
        passwordConfig.setProfile(profile);
        passwordConfig.setApplication(application);
        passwordConfig.setLabel(SOURCE_LABLE_KEY);
        passwordConfig.setKey(SOURCE_PASSWORD_KEY);
        passwordConfig.setValue(sourceManage.getDbPassword());
        
        baseRepository.save(passwordConfig);
        
        
        ConfigProperties driverConfig = new ConfigProperties();
        driverConfig.setProfile(profile);
        driverConfig.setApplication(application);
        driverConfig.setLabel(SOURCE_LABLE_KEY);
        driverConfig.setKey(SOURCE_DRIVER_KEY);
        driverConfig.setValue(sourceManage.loadConDriver());
        
        baseRepository.save(driverConfig);
        
        
        return profile;
    }
    
    public void removeByApplicationAndProfile(String application, String profile) {
        configPropertiesDao.deleteByApplicationAndProfile(application, profile);
    }
    
    
    public List<Map<String, Object>> queryAll() {
        return jdbcTemplate.queryForList("select APPLICATION,PROFILE,LABEL,group_concat(CONCAT(`KEY`,'=',`VALUE`)) as content FROM  properties group by APPLICATION,PROFILE,LABEL");
    }
    
    public Map<String, Object> queryProfile(SourceManage sourceManage) {
        String hash = genhash(sourceManage);
        return jdbcTemplate.queryForMap("SELECT c.`profile` FROM (SELECT group_concat(p.`VALUE`) AS oo, PROFILE FROM properties p GROUP BY PROFILE ) c WHERE c.oo =?", hash);
    }
    
    public Boolean existsProfile(SourceManage sourceManage) {
        String hash = genhash(sourceManage);
        return jdbcTemplate.queryForObject("SELECT count(c.`profile`) FROM (SELECT group_concat(p.`VALUE`) AS oo, PROFILE FROM properties p GROUP BY PROFILE ) c WHERE c.oo =?", Integer.class, hash) > 0;
    }
    
    
    public String genhash(SourceManage sourceManage) {
        return sourceManage.loadConUrl() + "," + sourceManage.getDbUsername() + "," + sourceManage.getDbPassword() + "," + sourceManage.loadConDriver();
    }
    
}
