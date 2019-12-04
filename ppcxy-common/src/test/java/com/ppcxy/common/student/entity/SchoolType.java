package com.ppcxy.common.student.entity;

/**
 * <p>学校类型</p>
 */
public enum SchoolType {
    primary_school("小学"), secondary_school("中学"), high_school("高中"), university("大学");

    private final String info;

    SchoolType(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
