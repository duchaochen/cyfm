package com.ppcxy.cyfm.filestore.editor.support.hunter;

import com.ppcxy.cyfm.filestore.editor.support.PathFormat;
import com.ppcxy.cyfm.filestore.editor.support.define.AppInfo;
import com.ppcxy.cyfm.filestore.editor.support.define.BaseState;
import com.ppcxy.cyfm.filestore.editor.support.define.MultiState;
import com.ppcxy.cyfm.filestore.editor.support.define.State;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;

public class FileManager {
    
    private String dir = null;
    private String rootPath = null;
    private String[] allowFiles = null;
    private int count = 0;
    
    public FileManager(Map<String, Object> conf) {
        
        this.rootPath = (String) conf.get("rootPath");
        this.dir = this.rootPath + (String) conf.get("dir");
        this.allowFiles = this.getAllowFiles((ArrayList) conf.get("allowFiles"));
        this.count = (Integer) conf.get("count");
        
    }
    
    public State listFile(int index) {
        
        File dir = new File(this.dir);
        State state = null;
        
        if (!dir.exists()) {
            return new BaseState(false, AppInfo.NOT_EXIST);
        }
        
        if (!dir.isDirectory()) {
            return new BaseState(false, AppInfo.NOT_DIRECTORY);
        }
        
        Collection<File> list = FileUtils.listFiles(dir, this.allowFiles, true);
        
        if (index < 0 || index > list.size()) {
            state = new MultiState(true);
        } else {
            Object[] fileList = Arrays.copyOfRange(list.toArray(), index, index + this.count);
            state = this.getState(fileList);
        }
        
        state.putInfo("start", index);
        state.putInfo("total", list.size());
        
        return state;
        
    }
    
    private State getState(Object[] files) {
        
        MultiState state = new MultiState(true);
        BaseState fileState = null;
        
        File file = null;
        
        for (Object obj : files) {
            if (obj == null) {
                break;
            }
            file = (File) obj;
            fileState = new BaseState(true);
            fileState.putInfo("url", PathFormat.format(this.getPath(file)));
            state.addState(fileState);
        }
        
        return state;
        
    }
    
    private String getPath(File file) {
        
        String path = file.getPath().replace("\\", "/");
        
        return path.replace(this.rootPath, "/");
        
    }
    
    private String[] getAllowFiles(List<String> fileExt) {
        List<String> result = Lists.newArrayList();
        fileExt.stream().forEach(s -> result.add(s.replace(".", "")));
    
        String[] ss = new String[0];
        return result.toArray(ss);
    }
    
}
