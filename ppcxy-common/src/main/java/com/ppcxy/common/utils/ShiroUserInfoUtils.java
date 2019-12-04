package com.ppcxy.common.utils;

import org.apache.shiro.SecurityUtils;

/**
 * Created by weep on 2016-6-27.
 */
public class ShiroUserInfoUtils {
    public static String getUsername() {
        if (SecurityUtils.getSubject() != null && SecurityUtils.getSubject().getPrincipal() != null) {
            return ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).username;

        }
        return null;
    }
}
