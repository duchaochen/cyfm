package com.ppcxy.cyfm.manage.entity.maintaion.datasource;

import com.ppcxy.common.entity.IdEntity;
import com.ppcxy.manage.maintain.datasource.conf.DataBaseType;
import com.ppcxy.manage.maintain.datasource.conf.DataSourceType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 数据源管理,配置数据源连接信息等.
 * <p/>
 * Created by weep on 2016-5-23.
 */
@Entity
@Table(name = "cy_datasource_manage")
public class SourceManage extends IdEntity {
    private String dsName;
    @Enumerated(EnumType.STRING)
    private DataSourceType dsType;
    
    /**
     * 数据库名称
     **/
    private String dbName;
    /**
     * 数据库类型
     **/
    @Enumerated(EnumType.STRING)
    private DataBaseType dbType;
    /**
     * 数据库IP
     **/
    private String dbHost;
    /**
     * 数据库端口
     **/
    private String dbPort;
    /**
     * 数据库用户名
     **/
    private String dbUsername;
    /**
     * 数据库密码
     **/
    private String dbPassword;
    
    public String getDsName() {
        return dsName;
    }
    
    public void setDsName(String dsName) {
        this.dsName = dsName;
    }
    
    public DataSourceType getDsType() {
        return dsType;
    }
    
    public void setDsType(DataSourceType dsType) {
        this.dsType = dsType;
    }
    
    public String getDbName() {
        return dbName;
    }
    
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    
    public DataBaseType getDbType() {
        return dbType;
    }
    
    public void setDbType(DataBaseType dbType) {
        this.dbType = dbType;
    }
    
    public String getDbHost() {
        return dbHost;
    }
    
    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }
    
    public String getDbPort() {
        return dbPort;
    }
    
    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }
    
    public String getDbUsername() {
        return dbUsername;
    }
    
    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }
    
    public String getDbPassword() {
        return dbPassword;
    }
    
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
    
    /**
     * @return 方法功能:返回当前数据库连接信息
     */
    public String loadConDriver() {
        return dbType.getDriver();
    }
    
    /**
     * @return 方法功能:根据当前数据库信息返回连接字符串
     */
    public String loadConUrl() {
        String url = "";
        
        if (DataBaseType.MSSQLSERVER.equals(this.dbType)) {
            url = "jdbc:sqlserver://" + this.dbHost + ":" + this.dbPort + ";databaseName=" + this.dbName;
        } else if (DataBaseType.MYSQL.equals(this.dbType)) {
            url = "jdbc:mysql://" + this.dbHost + ":" + this.dbPort + "/" + this.dbName + "?useUnicode=true&characterEncoding=utf-8";
        } else if (DataBaseType.ORACLE.equals(this.dbType)) {
            url = "jdbc:oracle:thin:@" + this.dbHost + ":" + this.dbPort + ":" + this.dbName;
        }
        
        return url;
    }
    
}
