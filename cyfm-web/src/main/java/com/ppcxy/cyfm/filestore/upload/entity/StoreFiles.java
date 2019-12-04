package com.ppcxy.cyfm.filestore.upload.entity;

import com.ppcxy.common.entity.UUIDEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 文件管理实体,管理上传文件相关信息
 */
@Entity
@Table(name = "fs_files")
public class StoreFiles extends UUIDEntity {
    
    /**
     * 文件真实存储名
     */
    private String realName;
    /**
     * 文件逻辑名称(上传文件名)
     */
    private String logicName;
    /**
     * 文件存储路径
     */
    private String location;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 文件类型
     */
    private String typeContent;
    /**
     * 文件拓展名
     */
    private String fileSuffix;
    
    /**
     * 上传模块
     */
    private String identity;
    /**
     * 模块内识别id
     */
    private String secondIdentity;
    /**
     * 模块内识别tag
     */
    private String tag;
    
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 上传人
     */
    private String uploader;
    
    /**
     * 使用状态
     */
    private String state;
    
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getLogicName() {
        return logicName;
    }
    
    public void setLogicName(String logicName) {
        this.logicName = logicName;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getTypeContent() {
        return typeContent;
    }
    
    public void setTypeContent(String typeContent) {
        this.typeContent = typeContent;
    }
    
    public String getFileSuffix() {
        return fileSuffix;
    }
    
    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }
    
    public String getIdentity() {
        return identity;
    }
    
    public void setIdentity(String identity) {
        this.identity = identity;
    }
    
    public String getSecondIdentity() {
        return secondIdentity;
    }
    
    public void setSecondIdentity(String secondIdentity) {
        this.secondIdentity = secondIdentity;
    }
    
    public String getTag() {
        return tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public Date getUploadTime() {
        return uploadTime;
    }
    
    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
    
    public String getUploader() {
        return uploader;
    }
    
    public void setUploader(String uploader) {
        this.uploader = uploader;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
}
