package com.ppcxy.cyfm.sys.web.authorize;

import com.ppcxy.common.web.bind.annotation.CurrentUser;
import com.ppcxy.cyfm.sys.entity.user.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.JoinAuthenticationToken;
import org.apache.shiro.authc.TotpUtil;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping(value = "/auth/join")
public class AuthJoinController {
    
    private static final String JOIN_SYSTEM_URL_TEMPLATE = "%s/auth/join/accept?code=%s&loginName=%s&redirectUrl=%s";
    
    @RequestMapping
    public String join(@CurrentUser User user, String joinSystemName, String redirectUrl) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(joinSystemName)) {
            return "redirect:/";
        }
        
        if (StringUtils.isBlank(redirectUrl)) {
            redirectUrl = "/";
        }
        
        return "redirect:" + String.format(JOIN_SYSTEM_URL_TEMPLATE, joinSystemName, TotpUtil.generate(user.getTotpSecret()), URLEncoder.encode(user.getUsername(), "UTF-8"), redirectUrl);
    }
    
    @RequestMapping("accept")
    public String join(String loginName, String code, String redirectUrl) {
        Subject subject = SecurityUtils.getSubject();
        
        //未登录直接登录,已登录不是同一用户则退出当前用户登录新用户，已登录同一用户则直接使用。
        if (!subject.isAuthenticated()) {
            loginByTotp(loginName, code, subject);
        } else {
            if (!subject.getPrincipal().toString().equals(loginName)) {
                subject.logout();
                loginByTotp(loginName, code, subject);
            }
        }
        
        if (StringUtils.isBlank(redirectUrl)) {
            redirectUrl = "/";
        }
        
        return "redirect:" + redirectUrl;
    }
    
    private void loginByTotp(String loginName, String code, Subject subject) {
        JoinAuthenticationToken token = new JoinAuthenticationToken();
        token.setUsername(loginName);
        token.setToken(code);
        subject.login(token);
    }
    
    @RequestMapping("ajax")
    @ResponseBody
    public String join(String loginName, String code) {
        Subject subject = SecurityUtils.getSubject();
        loginByTotp(loginName, code, subject);
        
        return Boolean.TRUE.toString();
    }
}
