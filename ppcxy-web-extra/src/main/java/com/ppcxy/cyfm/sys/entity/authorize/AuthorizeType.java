package com.ppcxy.cyfm.sys.entity.authorize;

/**
 * Created by wufab on 2016/8/11.
 */
public enum AuthorizeType {

    Role("Role","角色授权"), User("User","用户授权"), Dept("Dept","组织机构授权");

    private String info;
    private String value;


    AuthorizeType(String value,String info) {
        this.value = value;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public String getValue() {
        return value;
    }
}
