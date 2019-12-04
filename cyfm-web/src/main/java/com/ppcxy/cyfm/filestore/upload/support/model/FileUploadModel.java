package com.ppcxy.cyfm.filestore.upload.support.model;

import com.google.common.collect.Lists;
import com.ppcxy.cyfm.filestore.upload.support.utils.FileUploadUtils;

import java.util.Arrays;
import java.util.List;

public class FileUploadModel {
    private Long totalSize;
    private Long singleSize;
    private Long onceAmount;
    private List<String> fileTypes = Lists.newArrayList();
    private List<String> contentTypes = Lists.newArrayList();
    private Long currentTotalSize;
    private Long currentFileCount;
    
    
    public static FileUploadModel newInstance(String[] allows) {
        FileUploadModel fileUploadModel = new FileUploadModel();
        
        fileUploadModel.setTotalSize(0l);
        fileUploadModel.setSingleSize(200 * 1024 * 1024l);
        fileUploadModel.setOnceAmount(20l);
        
        fileUploadModel.getFileTypes().addAll(Arrays.asList(allows));
        
        fileUploadModel.setCurrentTotalSize(-1l);
        fileUploadModel.setCurrentFileCount(-1l);
        return fileUploadModel;
    }
    
    public Long getTotalSize() {
        return totalSize;
    }
    
    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }
    
    public Long getSingleSize() {
        return singleSize;
    }
    
    public void setSingleSize(Long singleSize) {
        this.singleSize = singleSize;
    }
    
    public Long getOnceAmount() {
        return onceAmount;
    }
    
    public void setOnceAmount(Long onceAmount) {
        this.onceAmount = onceAmount;
    }
    
    public List<String> getFileTypes() {
        return fileTypes;
    }
    
    public void setFileTypes(List<String> fileTypes) {
        this.fileTypes = fileTypes;
    }
    
    public List<String> getContentTypes() {
        return contentTypes;
    }
    
    public void setContentTypes(List<String> contentTypes) {
        this.contentTypes = contentTypes;
    }
    
    public Long getCurrentTotalSize() {
        return currentTotalSize;
    }
    
    public void setCurrentTotalSize(Long currentTotalSize) {
        this.currentTotalSize = currentTotalSize;
    }
    
    public Long getCurrentFileCount() {
        return currentFileCount;
    }
    
    public void setCurrentFileCount(Long currentFileCount) {
        this.currentFileCount = currentFileCount;
    }
    
    public boolean allowType(String filename, String contentType) {
        
        String suffix = FileUploadUtils.fileSuffix(filename);
        
        if (!this.fileTypes.contains(suffix)) {
            return this.contentTypes.contains(contentType);
        }
        return true;
    }
}
