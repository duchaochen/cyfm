package com.ppcxy.manage.maintain.datasource.support;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.google.common.collect.Maps;
import com.ppcxy.common.exception.BaseException;
import com.ppcxy.common.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * DataSource生成工具类.
 *
 * @author weep
 */
public class DataSourceGenerates {
    /**
     * slf日志类
     **/
    private static final Logger logger = LoggerFactory.getLogger(DataSourceGenerates.class);
    
    private static final Map<String, String> DS_URL_MAP = Maps.newHashMap();
    private static final Map<String, DruidDataSource> DS_MAP = Maps.newHashMap();
    
    private static final Map<String, Integer> REPEAT_COUNT = Maps.newHashMap();
    
    static {
        REPEAT_COUNT.put("count", 0);
    }
    
    private DataSourceGenerates() {
    
    }
    
    /**
     * 实例化一个对象.
     *
     * @return
     */
    public static DataSourceGenerates newInstance() {
        return new DataSourceGenerates();
    }
    
    /**
     * 创建并获取一个DataSource.
     *
     * @param connName 唯一标识名称 ，相同名会取得上次名字所代表的DataSource
     * @param driver
     * @param url
     * @param userName
     * @param password
     * @return
     */
    public DruidDataSource genDataSource(String connName, String driver, String url, String userName, String password) {
        DruidDataSource ds = null;
        
        if (DS_MAP.get(connName) == null) {
            String emptyConnName = DS_URL_MAP.get(url + userName);
            // 如果名字不存在,但url连接存在则返回相同的数据源
            if (emptyConnName != null) {
                
                ds = DS_MAP.get(emptyConnName);
                DS_MAP.put(connName, ds);
                
                REPEAT_COUNT.put("count", REPEAT_COUNT.get("count") + 1);
                
                logger.info("Generate DataSource [{}],use DataSource [{}],current Gen DataSource count [{}].", connName,
                        emptyConnName, DS_MAP.size() - REPEAT_COUNT.get("count"));
                return ds;
            }
            
            ds = new DruidDataSource();
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(userName);
            ds.setPassword(password);
            ds.setDefaultAutoCommit(true);
            ds.setLoginTimeout(5);
            ds.setQueryTimeout(5);
            ds.setNotFullTimeoutRetryCount(3);
            
            if (ds.getUrl().indexOf("oracle") > -1) {
                ds.setValidationQuery("select 1 from dual");
            } else {
                ds.setValidationQuery("select 1");
            }
            
            logger.info("Generate DataSource [{}],current Gen DataSource count [{}].", connName, DS_MAP.size());
            
            try {
                ds.setMaxWait(3000);
                ds.setTimeBetweenEvictionRunsMillis(60000);
                
                DruidPooledConnection connection = ds.getConnection();
                if (!connection.isClosed()) {
                    DS_MAP.put(connName, ds);
                    DS_URL_MAP.put(url + userName + password, connName);
                }
            } catch (Exception e) {
                LogUtils.logError("连接创建工具创建数据库连接失败.", e);
                DS_MAP.remove(connName);
                DS_URL_MAP.remove(url);
                ds.close();
            }
            
            return ds;
        }
        
        logger.info("Get DataSource [{}],current Gen DataSource count [{}].", connName, DS_MAP.size());
        return DS_MAP.get(connName);
        
    }
    
    /**
     * 传入连接名称得到DataSource.
     *
     * @param connName
     * @return
     * @throws BaseException
     */
    public static DruidDataSource loadDataSource(String connName) throws BaseException {
        if (DS_MAP.get(connName) == null) {
            logger.error("Datasource [{}] is not exists,please Generate it.", connName);
            throw new BaseException(String.format("Datasource [%s] is not exists,please Generates DataSource [%s].", connName, connName));
        }
        
        logger.info("Load DataSource [{}]", connName, DS_MAP.size());
        return DS_MAP.get(connName);
        
    }
    
    /**
     * 神权为测试连接写的
     */
    public DruidDataSource genTestDataSource(String connName, String driver, String url, String userName, String password) {
        DruidDataSource ds = null;
        
        ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        ds.setDefaultAutoCommit(false);
        
        return ds;
    }
    
    public static Boolean removeDataSource(String connName, String url, String userName) {
        DruidDataSource ds = DS_MAP.get(connName);
        if (ds != null) {
            ds.close();
        }
        DS_MAP.remove(connName);
        DS_URL_MAP.remove(url+userName);
        
        return true;
    }
}
