package org.apache.shiro.web.filter.jcaptcha;

import com.ppcxy.common.web.jcaptcha.JCaptcha;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 验证码过滤器
 */
public class JCaptchaValidateFilter extends AccessControlFilter {

    private boolean jcaptchaEbabled = true;

    private String jcaptchaParam = "jcaptchaCode";

    private String jcapatchaErrorUrl;

    /**
     * 是否开启jcaptcha
     *
     * @param jcaptchaEbabled
     */
    public void setJcaptchaEbabled(boolean jcaptchaEbabled) {
        this.jcaptchaEbabled = jcaptchaEbabled;
    }

    /**
     * 前台传入的验证码
     *
     * @param jcaptchaParam
     */
    public void setJcaptchaParam(String jcaptchaParam) {
        this.jcaptchaParam = jcaptchaParam;
    }

    public String getJcapatchaErrorUrl() {
        return jcapatchaErrorUrl;
    }

    public void setJcapatchaErrorUrl(String jcapatchaErrorUrl) {
        this.jcapatchaErrorUrl = jcapatchaErrorUrl;
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        request.setAttribute("jcaptchaEbabled", jcaptchaEbabled);
        return super.onPreHandle(request, response, mappedValue);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //验证码禁用 或不是表单提交 允许访问
        if (!jcaptchaEbabled || !"post".equals(httpServletRequest.getMethod().toLowerCase())) {
            return true;
        }
        return JCaptcha.validateResponse(httpServletRequest, httpServletRequest.getParameter(jcaptchaParam));
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        redirectToLogin(request, response);
        return false;
    }


    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        WebUtils.issueRedirect(request, response, getJcapatchaErrorUrl());
    }

}
