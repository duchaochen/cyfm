package com.ppcxy.cyfm.manage.entity.maintaion.notification;

/**
 * 触发的子系统
 * <p>Date: 13-7-8 下午2:19
 * <p>Version: 1.0
 */
public enum NotificationSystem {

    system("系统"), excel("excel");

    private final String info;

    private NotificationSystem(final String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

}
