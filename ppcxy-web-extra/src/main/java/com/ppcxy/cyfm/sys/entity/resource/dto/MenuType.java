package com.ppcxy.cyfm.sys.entity.resource.dto;

/**
 * Created by weep on 2015-12-24.
 */
public enum MenuType {
    rightFrame("默认"), ajax("异步"), blank("新窗口"), self("本页"), dialog("模态窗口"), fullDialog("全屏模态窗口");
    
    private final String info;
    
    MenuType(String info) {
        this.info = info;
    }
    
    public String getInfo() {
        return info;
    }
    
    public String getValue() {
        return name();
    }
}
