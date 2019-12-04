package com.ppcxy.cyfm.filestore.editor.support;

import com.ppcxy.cyfm.filestore.editor.support.define.ActionMap;
import com.google.common.collect.Maps;
import org.springside.modules.mapper.JsonMapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理器
 *
 * @author hancong03@baidu.com
 */
public final class ConfigManager {
    
    private final String rootPath;
    private final String originalPath;
    private final String contextPath;
    private static final String configFileName = "config.json";
    private String parentPath = null;
    private Map<String, Object> jsonConfig = null;
    // 涂鸦上传filename定义
    private final static String SCRAWL_FILE_NAME = "scrawl";
    // 远程图片抓取filename定义
    private final static String REMOTE_FILE_NAME = "remote";
    
    /*
     * 通过一个给定的路径构建一个配置管理器， 该管理器要求地址路径所在目录下必须存在config.properties文件
     */
    private ConfigManager(String rootPath, String contextPath, String uri) throws FileNotFoundException, IOException {
        
        rootPath = rootPath.replace("\\", "/");
        
        this.rootPath = rootPath;
        this.contextPath = contextPath;
        
        if (contextPath.length() > 0) {
            this.originalPath = this.rootPath + uri.substring(contextPath.length());
        } else {
            this.originalPath = this.rootPath + uri;
        }
        
        this.initEnv();
        
    }
    
    /**
     * 配置管理器构造工厂
     *
     * @param rootPath    服务器根路径
     * @param contextPath 服务器所在项目路径
     * @param uri         当前访问的uri
     * @return 配置管理器实例或者null
     */
    public static ConfigManager getInstance(String rootPath, String contextPath, String uri) {
        
        try {
            return new ConfigManager(rootPath, contextPath, uri);
        } catch (Exception e) {
            return null;
        }
        
    }
    
    // 验证配置文件加载是否正确
    public boolean valid() {
        return this.jsonConfig != null;
    }
    
    public String getAllConfig() {
        
        return JsonMapper.nonDefaultMapper().toJson(this.jsonConfig);
        
    }
    
    public Map<String, Object> getConfig(int type) {
        
        Map<String, Object> conf = Maps.newHashMap();
        
        String savePath = null;
        
        switch (type) {
            
            case ActionMap.UPLOAD_FILE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.jsonConfig.get("fileMaxSize"));
                conf.put("allowFiles", this.jsonConfig.get("fileAllowFiles"));
                conf.put("fieldName", this.jsonConfig.get("fileFieldName"));
                savePath = (String) this.jsonConfig.get("filePathFormat");
                break;
            
            case ActionMap.UPLOAD_IMAGE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.jsonConfig.get("imageMaxSize"));
                conf.put("allowFiles", this.jsonConfig.get("imageAllowFiles"));
                conf.put("fieldName", this.jsonConfig.get("imageFieldName"));
                savePath = (String) this.jsonConfig.get("imagePathFormat");
                break;
            
            case ActionMap.UPLOAD_VIDEO:
                conf.put("maxSize", this.jsonConfig.get("videoMaxSize"));
                conf.put("allowFiles", this.jsonConfig.get("videoAllowFiles"));
                conf.put("fieldName", this.jsonConfig.get("videoFieldName"));
                savePath = (String) this.jsonConfig.get("videoPathFormat");
                break;
            
            case ActionMap.UPLOAD_SCRAWL:
                conf.put("filename", ConfigManager.SCRAWL_FILE_NAME);
                conf.put("maxSize", this.jsonConfig.get("scrawlMaxSize"));
                conf.put("fieldName", this.jsonConfig.get("scrawlFieldName"));
                conf.put("isBase64", "true");
                savePath = (String) this.jsonConfig.get("scrawlPathFormat");
                break;
            
            case ActionMap.CATCH_IMAGE:
                conf.put("filename", ConfigManager.REMOTE_FILE_NAME);
                conf.put("filter", this.jsonConfig.get("catcherLocalDomain"));
                conf.put("maxSize", this.jsonConfig.get("catcherMaxSize"));
                conf.put("allowFiles", this.jsonConfig.get("catcherAllowFiles"));
                conf.put("fieldName", this.jsonConfig.get("catcherFieldName") + "[]");
                savePath = (String) this.jsonConfig.get("catcherPathFormat");
                break;
            
            case ActionMap.LIST_IMAGE:
                conf.put("allowFiles", this.jsonConfig.get("imageManagerAllowFiles"));
                conf.put("dir", this.jsonConfig.get("imageManagerListPath"));
                conf.put("count", this.jsonConfig.get("imageManagerListSize"));
                break;
            
            case ActionMap.LIST_FILE:
                conf.put("allowFiles", this.jsonConfig.get("fileManagerAllowFiles"));
                conf.put("dir", this.jsonConfig.get("fileManagerListPath"));
                conf.put("count", this.jsonConfig.get("fileManagerListSize"));
                break;
            
        }
        
        conf.put("savePath", savePath);
        conf.put("rootPath", this.rootPath);
        
        return conf;
        
    }
    
    private void initEnv() throws FileNotFoundException, IOException {
        
        File file = new File(this.originalPath);
        
        if (!file.isAbsolute()) {
            file = new File(file.getAbsolutePath());
        }
        
        this.parentPath = file.getParent();
        
        String configContent = this.readFile(this.getConfigPath());
        
        try {
            
            this.jsonConfig = JsonMapper.nonDefaultMapper().fromJson(configContent, HashMap.class);
        } catch (Exception e) {
            this.jsonConfig = null;
        }
        
    }
    
    private String getConfigPath() {
        return this.getClass().getClassLoader().getResource("/editor/" + configFileName).getPath();
    }
    
    
    private String readFile(String path) throws IOException {
        
        StringBuilder builder = new StringBuilder();
        
        try {
            
            InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
            BufferedReader bfReader = new BufferedReader(reader);
            
            String tmpContent = null;
            
            while ((tmpContent = bfReader.readLine()) != null) {
                builder.append(tmpContent);
            }
            
            bfReader.close();
            
        } catch (UnsupportedEncodingException e) {
            // 忽略
        }
        
        return this.filter(builder.toString());
        
    }
    
    // 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
    private String filter(String input) {
        
        return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
        
    }
    
}
