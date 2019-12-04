package com.ppcxy.cyfm.filestore.upload.service;

import com.ppcxy.common.service.BaseService;
import com.ppcxy.cyfm.filestore.upload.entity.StoreFiles;
import com.ppcxy.cyfm.filestore.upload.support.utils.FileUploadUtils;
import com.ppcxy.cyfm.sys.entity.user.User;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class StoreFilesService extends BaseService<StoreFiles, String> {
    
    public StoreFiles addFile(String filename, long fileSize, String contentType, User user,  String filePath, String identity, String secondIdentity, String tag) {
        
        StoreFiles storeFiles = new StoreFiles();
        
        storeFiles.setRealName(filename);
        storeFiles.setLogicName(filename);
        storeFiles.setFileSize(fileSize);
        storeFiles.setTypeContent(contentType);
        storeFiles.setFileSuffix(FileUploadUtils.fileSuffix(filename));
        
        storeFiles.setLocation(filePath);
        
        storeFiles.setUploader(user.getUsername());
        storeFiles.setUploadTime(Calendar.getInstance().getTime());
        
        storeFiles.setIdentity(identity);
        storeFiles.setSecondIdentity(secondIdentity);
        storeFiles.setTag(tag);
        
        storeFiles.setState(tag);
        
        return save(storeFiles);
    }
}
