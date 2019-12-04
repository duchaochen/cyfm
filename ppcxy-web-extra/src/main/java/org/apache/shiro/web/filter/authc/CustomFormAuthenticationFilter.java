
package org.apache.shiro.web.filter.authc;

import com.ppcxy.common.utils.ShiroUserInfoUtils;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.service.user.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 基于几点修改：
 * 1、onLoginFailure 时 把异常添加到request attribute中 而不是异常类名
 * 2、登录成功时：成功页面重定向：
 * 2.1、如果前一个页面是登录页面，-->2.3
 * 2.2、如果有SavedRequest 则返回到SavedRequest
 * 2.3、否则根据当前登录的用户决定返回到管理员首页/前台首页
 * <p/>
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

    @Autowired
    UserService userService;
    /**
     * 默认的成功地址
     */
    private String defaultSuccessUrl;
    /**
     * 管理员默认的成功地址
     */
    private String adminDefaultSuccessUrl;

    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        request.setAttribute(getFailureKeyAttribute(), ae);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getDefaultSuccessUrl() {
        return defaultSuccessUrl;
    }

    public void setDefaultSuccessUrl(String defaultSuccessUrl) {
        this.defaultSuccessUrl = defaultSuccessUrl;
    }

    public String getAdminDefaultSuccessUrl() {
        return adminDefaultSuccessUrl;
    }

    public void setAdminDefaultSuccessUrl(String adminDefaultSuccessUrl) {
        this.adminDefaultSuccessUrl = adminDefaultSuccessUrl;
    }

    /**
     * 根据用户选择成功地址
     *
     * @return
     */
    @Override
    public String getSuccessUrl() {
        //TODO 区分登录成功页面
        if (haveAdminRole()) {
            return getAdminDefaultSuccessUrl();
        }
        return getDefaultSuccessUrl();
    }

    /**
     * 覆盖父类避免登录后跳转到记录的前一个访问链接
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        //TODO 区分登录成功页面,管理员登录到成功页
        if (haveAdminRole()) {
            WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
            return;
        }
        
        super.issueSuccessRedirect(request, response);
    }
    
    private boolean haveAdminRole() {
        return haveRole("Admin");
    }
    
    private boolean haveRole(String roleName) {
        String username = ShiroUserInfoUtils.getUsername();
        
        User user = userService.findByUsername(username);
        if (user != null && Boolean.TRUE.equals(user.getRoleValues().indexOf(roleName) != -1)) {
            return true;
            
        }
        return false;
    }
}
