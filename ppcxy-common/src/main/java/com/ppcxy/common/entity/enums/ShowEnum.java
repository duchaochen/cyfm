package com.ppcxy.common.entity.enums;

public enum ShowEnum {
    TRUE(Boolean.TRUE, "显示"), FALSE(Boolean.FALSE, "不显示");

    private final Boolean value;
    private final String info;

    ShowEnum(Boolean value, String info) {
        this.value = value;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public Boolean getValue() {
        return value;
    }
}
