package org.apache.shiro.web.filter.user;

import com.ppcxy.common.Constants;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.entity.user.UserStatus;
import com.ppcxy.cyfm.sys.service.user.UserService;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springside.modules.utils.Reflections;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 验证用户过滤器
 * 1、用户是否删除
 * 2、用户是否锁定
 */
public class SysUserFilter extends AccessControlFilter {
    
    @Resource
    private UserService userService;
    
    /**
     * 用户删除了后重定向的地址
     */
    private String userNotfoundUrl;
    /**
     * 用户锁定后重定向的地址
     */
    private String userBlockedUrl;
    /**
     * 未知错误
     */
    private String userUnknownErrorUrl;
    
    public String getUserNotfoundUrl() {
        return userNotfoundUrl;
    }
    
    public void setUserNotfoundUrl(String userNotfoundUrl) {
        this.userNotfoundUrl = userNotfoundUrl;
    }
    
    public String getUserBlockedUrl() {
        return userBlockedUrl;
    }
    
    public void setUserBlockedUrl(String userBlockedUrl) {
        this.userBlockedUrl = userBlockedUrl;
    }
    
    public String getUserUnknownErrorUrl() {
        return userUnknownErrorUrl;
    }
    
    public void setUserUnknownErrorUrl(String userUnknownErrorUrl) {
        this.userUnknownErrorUrl = userUnknownErrorUrl;
    }
    
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (subject == null) {
            return true;
        }
        
        String username = (String) Reflections.getFieldValue(subject.getPrincipal(), "username");
        //此处注意缓存 防止大量的查询db
        User user = userService.findByUsername(username);
        //把当前用户放到session中
        request.setAttribute(Constants.CURRENT_USER, user);
        //druid监控需要
        ((HttpServletRequest) request).getSession().setAttribute(Constants.CURRENT_USERNAME, username);
        
        return true;
    }
    
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        User user = (User) request.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return true;
        }
        
        //TODO 逻辑删除用户 if (Boolean.TRUE.equals(user.getDeleted()) || user.getStatus() == UserStatus.blocked) {
        if (user.getStatus() != UserStatus.normal) {
            getSubject(request, response).logout();
            saveRequestAndRedirectToLogin(request, response);
            return false;
        }
        return true;
    }
    
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        getSubject(request, response).logout();
        saveRequestAndRedirectToLogin(request, response);
        return true;
    }
    
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        User user = (User) request.getAttribute(Constants.CURRENT_USER);
        String url = null;
        //if (Boolean.TRUE.equals(user.getDeleted())) {
        //    url = getUserNotfoundUrl();
        //} else
        if (user.getStatus() != UserStatus.normal) {
            url = getUserBlockedUrl();
        } else {
            url = getUserUnknownErrorUrl();
        }
        
        WebUtils.issueRedirect(request, response, url);
    }
    
}
