package com.ppcxy.cyfm.filestore.editor.support.upload;

import com.ppcxy.cyfm.filestore.editor.support.PathFormat;
import com.ppcxy.cyfm.filestore.editor.support.define.AppInfo;
import com.ppcxy.cyfm.filestore.editor.support.define.BaseState;
import com.ppcxy.cyfm.filestore.editor.support.define.FileType;
import com.ppcxy.cyfm.filestore.editor.support.define.State;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class BinaryUploader {
    
    public static final State save(HttpServletRequest request,
                                   Map<String, Object> conf) {
        boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;
        
        if (!ServletFileUpload.isMultipartContent(request)) {
            return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
        }
        
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        multipartResolver.setMaxUploadSize(524288000);
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            try {
                //转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    
                    //取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    
                    if (file == null) {
                        return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
                    }
                    
                    
                    String savePath = (String) conf.get("savePath");
                    String originFileName = file.getOriginalFilename();
                    String suffix = FileType.getSuffixByFilename(originFileName);
                    
                    originFileName = originFileName.substring(0,
                            originFileName.length() - suffix.length());
                    savePath = savePath + suffix;
                    
                    int maxSize = ((Integer) conf.get("maxSize")).intValue();
                    
                    if (!validType(suffix, (ArrayList<String>) conf.get("allowFiles"))) {
                        return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
                    }
                    
                    savePath = PathFormat.parse(savePath, originFileName);
                    
                    String physicalPath = conf.get("rootPath") + savePath;
                    InputStream is = file.getInputStream();
                    State storageState = StorageManager.saveFileByInputStream(is,
                            physicalPath, maxSize);
                    is.close();
                    
                    if (storageState.isSuccess()) {
                        storageState.putInfo("url", PathFormat.format(savePath));
                        storageState.putInfo("type", suffix);
                        storageState.putInfo("original", originFileName + suffix);
                    }
                    
                    return storageState;
                    
                }
            } catch (IOException e) {
                return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
            }
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }
    
    private static boolean validType(String type, List<String> allowTypes) {
        
        return allowTypes.contains(type);
    }
}
