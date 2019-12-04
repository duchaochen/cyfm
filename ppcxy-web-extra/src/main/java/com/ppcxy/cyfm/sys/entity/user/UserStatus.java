package com.ppcxy.cyfm.sys.entity.user;

/**
 * <p>Date: 13-3-11 下午3:19
 * <p>Version: 1.0
 */
public enum UserStatus {

    normal("正常"), blocked("异常锁定"),disabled("禁用");

    private final String info;

    private UserStatus(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
