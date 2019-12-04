package com.ppcxy.cyfm.desktop.web;

import com.ppcxy.cyfm.sys.service.user.UserOnlineService;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * <p/>
 * 真正登录的POST请求由Filter完成,
 *
 * @author calvin
 */
@Controller
public class LoginController {
    
    @Resource
    private UserOnlineService userOnlineService;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
        return "login";
    }
    
    @RequestMapping(value = "/exitSystem", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        userOnlineService.offline(session.getId());
        return "redirect:/logout";
    }
}
