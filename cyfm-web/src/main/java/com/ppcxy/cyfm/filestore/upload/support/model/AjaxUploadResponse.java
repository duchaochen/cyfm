package com.ppcxy.cyfm.filestore.upload.support.model;

import com.google.common.collect.Lists;
import com.ppcxy.cyfm.filestore.upload.support.utils.FileUploadUtils;

import java.util.Date;
import java.util.List;

/**
 * jquery File Upload 文件上传响应
 * <p>Date: 13-2-13 下午4:37
 * <p>Version: 1.0
 */
public class AjaxUploadResponse {
    
    private List<FileMeta> files = Lists.newArrayList();
    
    public void add(String name, long size, String error) {
        files.add(new FileMeta(name, size, error));
    }
    
    /**
     * 添加上传成功的文件信息
     *
     * @param name
     * @param size
     * @param url
     * @param delete_url
     */
    public void add(String name, long size, String url, String delete_url) {
        files.add(new FileMeta(name, size, url, delete_url));
    }
    
    /**
     * 添加上传成功的文件信息，且带key
     *
     * @param key
     * @param name
     * @param size
     * @param url
     * @param delete_url
     */
    public void add(String key, String name, long size, String url, String delete_url) {
        FileMeta fileMeta = new FileMeta(name, size, url, delete_url);
        //设置上传文件的key
        fileMeta.setKey(key);
        files.add(fileMeta);
    }
    
    
    /**
     * 添加上传成功的信息，带缩略图
     *
     * @param name
     * @param size
     * @param url
     * @param thumbnail_url
     * @param delete_url
     */
    public void add(String name, long size, String url, String thumbnail_url, String delete_url) {
        files.add(new FileMeta(name, size, url, thumbnail_url, delete_url));
    }
    
    /**
     * 添加上传成功的信息，且带key，带缩略图
     *
     * @param key
     * @param name
     * @param size
     * @param url
     * @param thumbnail_url
     * @param delete_url
     */
    public void add(String key, String name, long size, String url, String thumbnail_url, String delete_url) {
        FileMeta fileMeta = new FileMeta(name, size, url, thumbnail_url, delete_url);
        //设置上传文件的key
        fileMeta.setKey(key);
        files.add(fileMeta);
    }
    
    public List<FileMeta> getFiles() {
        return files;
    }
    
    public void setFiles(List<FileMeta> files) {
        this.files = files;
    }
    
    /**
     * 文件信息
     */
    public static class FileMeta {
        
        private String key;
        /**
         * 名称
         */
        private String name;
        /**
         * 大小
         */
        private Long size = -1l;
        /**
         * 类型
         */
        private String file_type;
        /**
         * 错误信息
         */
        private String error = "";
        /**
         * 上传文件后的地址
         */
        private String url = "";
        /**
         * 缩略图
         */
        private String thumbnail_url = "";
        /**
         * 删除时的URL
         */
        private String delete_url = "";
        
        private String delete_type = "POST";
        
        private Date createDate = new Date();
        
        
        public FileMeta(String name, long size, String url, String thumbnail_url, String delete_url) {
            this.name = name;
            this.size = size;
            this.url = url;
            this.thumbnail_url = thumbnail_url;
            this.delete_url = delete_url;
            this.file_type = FileUploadUtils.fileSuffix(name);
        }
        
        public FileMeta(String name, long size, String url, String delete_url) {
            this.name = name;
            this.size = size;
            this.url = url;
            this.delete_url = delete_url;
        }
        
        public FileMeta(String name, long size, String error) {
            this.name = name;
            this.error = error;
            this.size = size;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public long getSize() {
            return size;
        }
        
        public void setSize(long size) {
            this.size = size;
        }
        
        public String getFile_type() {
            return file_type;
        }
        
        public void setFile_type(String file_type) {
            this.file_type = file_type;
        }
        
        public String getError() {
            return error;
        }
        
        public void setError(String error) {
            this.error = error;
        }
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getThumbnail_url() {
            return thumbnail_url;
        }
        
        public void setThumbnail_url(String thumbnail_url) {
            this.thumbnail_url = thumbnail_url;
        }
        
        public String getDelete_url() {
            return delete_url;
        }
        
        public void setDelete_url(String delete_url) {
            this.delete_url = delete_url;
        }
        
        public String getDelete_type() {
            return delete_type;
        }
        
        public void setDelete_type(String delete_type) {
            this.delete_type = delete_type;
        }
        
        public Date getCreateDate() {
            return createDate;
        }
        
        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }
        
        public String getKey() {
            return key;
        }
        
        public void setKey(String key) {
            this.key = key;
        }
    }
    
}
