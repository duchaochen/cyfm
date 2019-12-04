package com.ppcxy.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取访问的BaseUrl
 */
public class WebUtils {
    
    private static final String re = "((http|ftp|https)://(.*?)(:(\\d*))?)/";
    
    private static Pattern p = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
    
    public static String getBasePath(String url) {
        //使用正则表达式过滤，
        String result = null;
        
        Matcher m = p.matcher(url);
        while (m.find()) {
            result = m.group(1);
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        System.out.println(getBasePath("http://192.168.1.11:8080/dataplatform"));
    }
}
