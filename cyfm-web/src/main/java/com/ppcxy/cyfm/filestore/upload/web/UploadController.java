package com.ppcxy.cyfm.filestore.upload.web;

import com.google.common.collect.Lists;
import com.ppcxy.common.exception.BaseException;
import com.ppcxy.common.extra.conf.SystemConfigs;
import com.ppcxy.common.utils.ImagesUtils;
import com.ppcxy.common.utils.LogUtils;
import com.ppcxy.common.utils.MessageUtils;
import com.ppcxy.common.web.bind.annotation.CurrentUser;
import com.ppcxy.cyfm.filestore.upload.entity.StoreFiles;
import com.ppcxy.cyfm.filestore.upload.service.StoreFilesService;
import com.ppcxy.cyfm.filestore.upload.support.exception.FileNameLengthLimitExceededException;
import com.ppcxy.cyfm.filestore.upload.support.exception.InvalidExtensionException;
import com.ppcxy.cyfm.filestore.upload.support.model.AjaxUploadResponse;
import com.ppcxy.cyfm.filestore.upload.support.model.FileUploadModel;
import com.ppcxy.cyfm.filestore.upload.support.utils.DownloadUtils;
import com.ppcxy.cyfm.filestore.upload.support.utils.FileUploadUtils;
import com.ppcxy.cyfm.filestore.upload.support.utils.PackageZipUtils;
import com.ppcxy.cyfm.sys.entity.user.User;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "filestore/upload")
public class UploadController {
    
    //单个文件大小限制100M
    private static final Integer SINGLE_MAX_SIZE = 100 * 1024 * 1024;
    private static final String UPLOAD_BASE_DESTPATH = SystemConfigs.UPLOAD_BASE_DESTPATH;
    
    @Autowired
    private StoreFilesService storeFilesService;
    
    /**
     * 进入上传页面
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping
    public String upload(HttpServletRequest request, Model model) {
        
        model.addAttribute("uploadParam", FileUploadModel.newInstance(FileUploadUtils.DEFAULT_ALLOWED_EXTENSION));
        
        return "filestore/upload/fileupload";
    }
    
    /**
     * 文件上传
     *
     * @param request
     * @param files
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AjaxUploadResponse ajaxUpload(
            HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "files[]", required = false) MultipartFile[] files, String identity, String secondIdentity, String tag, @CurrentUser User user) {
        response.setContentType("text/plain");
        
        AjaxUploadResponse ajaxUploadResponse = new AjaxUploadResponse();
        
        if (ArrayUtils.isEmpty(files)) {
            return ajaxUploadResponse;
        }
        
        FileUploadModel fileUploadModel = FileUploadModel.newInstance(FileUploadUtils.DEFAULT_ALLOWED_EXTENSION);
        
        for (MultipartFile file : files) {
            String filename = FileUploadUtils.cleanFileNameSpecialChar(file.getOriginalFilename());
            String contentType = file.getContentType();
            long fileSize = file.getSize();
            
            if (fileSize > fileUploadModel.getSingleSize()) {
                ajaxUploadResponse.add(filename, fileSize, MessageUtils.message("upload.exceed.singleSize"));
                continue;
            }
            
            if (!fileUploadModel.allowType(filename, contentType)) {
                ajaxUploadResponse.add(filename, fileSize, MessageUtils.message("upload.not.allow.extension"));
                continue;
            }
            
            try {
                
                String filePath = FileUploadUtils.upload(request, UPLOAD_BASE_DESTPATH, file, FileUploadUtils.DEFAULT_ALLOWED_EXTENSION, SINGLE_MAX_SIZE, true);
                
                
                StoreFiles storeFiles = storeFilesService.addFile(filename, fileSize, contentType, user, filePath, identity, secondIdentity, tag);
                
                // TODO 保存文件信息到本地文件上传记录
                
                String deleteURL = "filestore/upload/ajax/batch/delete?fileIds=" + storeFiles.getId();// + fileManage.getId();
                String downloadurl = "filestore/upload/ajaxDownload?fileIds=" + storeFiles.getId();// + fileManage.getId();
                
                if (ImagesUtils.isImage(filename)) {
                    ajaxUploadResponse.add(storeFiles.getId(), filename, fileSize, downloadurl, downloadurl, deleteURL);
                } else {
                    ajaxUploadResponse.add(storeFiles.getId(), filename, fileSize, downloadurl, deleteURL);
                }
                
            } catch (IOException e) {
                LogUtils.logError("upload.server.error", e);
                ajaxUploadResponse.add(filename, fileSize, MessageUtils.message("upload.server.error"));
            } catch (InvalidExtensionException e) {
                LogUtils.logError("upload.not.allow.extension", e);
                ajaxUploadResponse.add(filename, fileSize, MessageUtils.message("upload.not.allow.extension"));
            } catch (FileUploadBase.FileSizeLimitExceededException e) {
                LogUtils.logError("upload.exceed.maxSize", e);
                ajaxUploadResponse.add(filename, fileSize, MessageUtils.message("upload.exceed.maxSize"));
            } catch (FileNameLengthLimitExceededException e) {
                LogUtils.logError("upload.filename.exceed.length", e);
                ajaxUploadResponse.add(filename, fileSize, MessageUtils.message("upload.filename.exceed.length"));
            } catch (Exception e) {
                LogUtils.logError("upload.server.error", e);
                ajaxUploadResponse.add(filename, fileSize, MessageUtils.message("upload.server.error"));
            }
            
        }
        
        return ajaxUploadResponse;
    }
    
    /**
     * 自解压上传
     *
     * @param files
     * @return
     */
    @RequestMapping(value = "zip", method = RequestMethod.POST)
    @ResponseBody
    public AjaxUploadResponse zipUpload(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "files[]", required = false) MultipartFile[] files) {
        response.setContentType("text/plain");
        AjaxUploadResponse ajaxUploadResponse = new AjaxUploadResponse();
        
        if (ArrayUtils.isEmpty(files)) {
            return ajaxUploadResponse;
        }
        
        String destPath = "/";
        
        for (MultipartFile file : files) {
            File destFile = null;
            
            try {
                String filename = FileUploadUtils.extractFilename(file, destPath, true);
                destFile = FileUploadUtils.getAbsoluteFile(FileUploadUtils.extractUploadDir(request, false), filename);
                
                file.transferTo(destFile);
                
                //TODO 上传成功,开启线程进行解压操作.
                ajaxUploadResponse.add(filename.substring(filename.lastIndexOf("_"), filename.length()), file.getSize(), "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return ajaxUploadResponse;
    }
    
    @RequestMapping(value = "ajaxDownload", method = RequestMethod.GET)
    @ResponseBody
    public void downloadLocal(@RequestParam(value = "fileIds", required = false) String[] fileIds, HttpServletRequest request, HttpServletResponse response) {
        //解决中文乱码问题
        
        String downloadFileName = null;
        File downloadFile = null;
        
        if (fileIds.length == 1) {
            StoreFiles sf = storeFilesService.findOne(fileIds[0]);
            
            downloadFile = new File(sf.getLocation());
            downloadFileName = sf.getRealName();
        } else {
            
            String basedir = UUID.randomUUID().toString();
            String destPath = System.getProperty("java.io.tmpdir") + basedir;
            
            List<File> srcFiles = Lists.newArrayList();
            
            
            for (int i = 0; i < fileIds.length; i++) {
                StoreFiles sf = storeFilesService.findOne(fileIds[i]);
                srcFiles.add(new File(sf.getLocation()));
            }
            
            if (!PackageZipUtils.zip(srcFiles, destPath, basedir)) {
                throw new BaseException("文件打包失败,未能找到所选定的所有文件.");
            }
            
            downloadFile = new File(destPath);
            downloadFileName = String.format("打包下载[%s 等 " + fileIds.length + " 个文件]" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip", srcFiles.get(0).getName());
        }
        
        try {
            DownloadUtils.download(request, response, downloadFile, downloadFileName);
        } catch (IOException e) {
            throw new BaseException("文件下载失败,未找到要下载的文件.", e);
        }
        
    }
    
    
    /**
     * 批量删除
     */
    @RequestMapping(value = "ajax/batch/delete", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String deleteInBatch(@RequestParam(value = "fileIds", required = false) String[] fileIds) {
        if (fileIds != null && fileIds.length != 0) {
            storeFilesService.delete(fileIds);
        }
        return "success";
    }
    
    
}
