package com.ppcxy.common.utils;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Date: 13-2-7 下午8:46
 * <p>Version: 1.0
 */
public class ServletUtils {
    
    /**
     * 判断指定请求url是否以method请求的 firstPrefix+lastPrefixes开头
     * 如当前请求url是/sample/create 则匹配firstPrefix:/sample lastPrefixed /create
     *
     * @param request
     * @param method       请求的方法
     * @param firstPrefix
     * @param lastPrefixes
     * @return
     */
    public static boolean startWith(HttpServletRequest request, RequestMethod method, String firstPrefix, String... lastPrefixes) {
        String requestMethod = request.getMethod();
        if (!requestMethod.equalsIgnoreCase(method.name())) {
            return false;
        }
        String url = request.getServletPath();
        if (!url.startsWith(firstPrefix)) {
            return false;
        }
        
        if (lastPrefixes.length == 0) {
            return true;
        }
        
        url = url.substring(firstPrefix.length());
        for (String lastPrefix : lastPrefixes) {
            if (url.startsWith(lastPrefix)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static String loadRealPath() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        return request.getServletContext().getRealPath("/");
    }
    
    /**
     * FIXME 一个暂时性的，可能会出现问题的获取contentPath方法，不合理。
     *
     * @return
     */
    public static String loadContentPath() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            
            //如果当先线程中存在 request
            if (request != null) {
                return request.getContextPath();
            }
        } catch (IllegalStateException e) {
            //如果当前线程非request线程
            String result = "";
            URL resource = ServletUtils.class.getClassLoader().getResource("");
            
            //FIXME 开发环境在工作区内情况直接使用root
            if (resource.toString().contains("orkspaces") || resource.toString().contains("project")) {
                return "";
            }
            
            Pattern pattern = Pattern.compile("(/[\\w\\-]+)/WEB-INF/classes/");///([\w\-]+)/WEB-INF/classes/
            Matcher matcher = pattern.matcher(resource.toString());
            
            //当条件满足时，将返回true，否则返回false
            if (matcher.find()) {
                result = matcher.group(1);
            }
            
            if ("/ROOT".equals(result)) {
                return "";
            }
            return result;
        }
        
        return "";
    }
}
