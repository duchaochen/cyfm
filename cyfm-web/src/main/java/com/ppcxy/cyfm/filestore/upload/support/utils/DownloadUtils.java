package com.ppcxy.cyfm.filestore.upload.support.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


public class DownloadUtils {
    
    public static void download(HttpServletRequest request, HttpServletResponse response, String filePath, String displayName) throws IOException {
        File file = new File(filePath);
        download(request, response, file, displayName);
    }
    
    
    public static void download(HttpServletRequest request, HttpServletResponse response, String filePath) throws IOException {
        
        download(request, response, filePath, "");
    }
    
    
    public static void download(HttpServletRequest request, HttpServletResponse response, File file, String displayName) throws IOException {
        if (StringUtils.isEmpty(displayName)) {
            displayName = file.getName();
        }
        
        if (!file.exists() || !file.canRead()) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("您下载的文件不存在！");
            return;
        }
        
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
        download(request, response, displayName, is);
    }
    
    
    public static void download(HttpServletRequest request, HttpServletResponse response, String displayName, byte[] bytes) throws IOException {
        if (ArrayUtils.isEmpty(bytes)) {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write("您下载的文件不存在！");
            return;
        }
        
        BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(bytes));
        
        download(request, response, displayName, is);
    }
    
    
    public static void download(HttpServletRequest request, HttpServletResponse response, String displayName, BufferedInputStream is) throws IOException {
        downloadResponse(request, response, displayName);
        
        response.setContentType("application/x-download");
        response.setContentLength(is.available());
        
        OutputStream os = null;
        
        try {
            os = response.getOutputStream();
            IOUtils.copy(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
    
    public static void downloadResponse(HttpServletRequest request, HttpServletResponse response, String displayName) throws IOException {
        String userAgent = request.getHeader("User-Agent");
        boolean isIE = (userAgent != null) && (userAgent.toLowerCase().indexOf("msie") != -1);
        
        response.reset();
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "must-revalidate, no-transform");
        response.setDateHeader("Expires", 0L);
        
        String displayFilename = displayName.substring(displayName.lastIndexOf("_") + 1);
        displayFilename = displayFilename.replace(" ", "_");
        
        if (isIE) {
            displayFilename = URLEncoder.encode(displayFilename, "UTF-8");
        } else {
            displayFilename = new String(displayFilename.getBytes("UTF-8"), "ISO8859-1");
        }
        response.setHeader("Content-Disposition", "attachment;filename=\"" + displayFilename + "\"");
    }
    
    public static void downloadFile(HttpServletResponse response, File file) throws IOException {
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            IOUtils.copy(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
        
        os.flush();
    }
}
