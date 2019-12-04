package com.ppcxy.common.student.entity;

/**
 * <p>性别</p>
 */
public enum Sex {

    male("男"), female("女");
    private final String info;

    Sex(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
