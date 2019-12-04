package com.ppcxy.cyfm.personal.web;

import com.ppcxy.common.Constants;
import com.ppcxy.common.entity.enums.BooleanEnum;
import com.ppcxy.common.web.bind.annotation.CurrentUser;
import com.ppcxy.common.web.controller.BaseController;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.service.user.PasswordService;
import com.ppcxy.cyfm.sys.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 当前用户的个人信息
 * <p>Date: 13-3-30 下午2:00
 * <p>Version: 1.0
 */
@Controller
@RequestMapping("/personal/profile")
public class PersonalProfileController extends BaseController<User, Long> {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordService passwordService;
    
    public PersonalProfileController() {
        setModelName("personalProfile");
    }
    
    @Override
    public void preResponse(Model model) {
        model.addAttribute("booleanList", BooleanEnum.values());
    }
    
    
    @RequestMapping(value = {"", "/ viewInfo"})
    public String viewInfo(@CurrentUser User user, Model model) {
        preResponse(model);
        
        model.addAttribute(Constants.OPTION_NAME, "个人资料");
        user = userService.findOne(user.getId());
        model.addAttribute("user", user);
        return viewName("form");
    }
    
    @RequestMapping(value = "/updateInfo", method = RequestMethod.GET)
    public String updateInfoForm(@CurrentUser User user, Model model) {
        preResponse(model);
        
        model.addAttribute(Constants.OPTION_NAME, "修改个人资料");
        model.addAttribute("user", user);
        return viewName("form");
    }
    
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    public String updateInfo(
            @CurrentUser User user,
            @RequestParam("email") String email,
            @RequestParam("tel") String tel,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (email == null || !email.matches(User.EMAIL_PATTERN)) {
            model.addAttribute(Constants.ERROR_MESSAGE, "请输入正确的邮箱地址");
            return updateInfoForm(user, model);
        }
        
        if (tel == null || !tel.matches(User.MOBILE_PHONE_NUMBER_PATTERN)) {
            model.addAttribute(Constants.ERROR_MESSAGE, "请输入正确的手机号");
            return updateInfoForm(user, model);
        }
        
        User emailDbUser = userService.findByEmail(email);
        if (emailDbUser != null && !emailDbUser.equals(user)) {
            model.addAttribute(Constants.ERROR_MESSAGE, "邮箱地址已经被其他人使用，请换一个");
            return updateInfoForm(user, model);
        }
        
        User telNumberDbUser = userService.findByTel(tel);
        if (telNumberDbUser != null && !telNumberDbUser.equals(user)) {
            model.addAttribute(Constants.ERROR_MESSAGE, "手机号已经被其他人使用，请换一个");
            return updateInfoForm(user, model);
        }
        
        user.setEmail(email);
        user.setTel(tel);
        userService.update(user);
        
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改个人资料成功");
        
        return redirectToUrl(viewName("updateInfo"));
        
    }
    
    
    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String changePasswordForm(@CurrentUser User user, Model model) {
        preResponse(model);
        
        model.addAttribute(Constants.OPTION_NAME, "修改密码");
        model.addAttribute("user", user);
        return getViewPrefix() + "/changePassword";
    }
    
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(
            @CurrentUser User user,
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "newPassword1") String newPassword1,
            @RequestParam(value = "newPassword2") String newPassword2,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        
        if (!passwordService.matches(user, oldPassword)) {
            model.addAttribute(Constants.ERROR_MESSAGE, "旧密码不正确");
            return changePasswordForm(user, model);
        }
        
        if (StringUtils.isEmpty(newPassword1) || StringUtils.isEmpty(newPassword2)) {
            model.addAttribute(Constants.ERROR_MESSAGE, "必须输入新密码");
            return changePasswordForm(user, model);
        }
        
        if (!newPassword1.equals(newPassword2)) {
            model.addAttribute(Constants.ERROR_MESSAGE, "两次输入的密码不一致");
            return changePasswordForm(user, model);
        }
        
        userService.changePassword(user, newPassword1);
        
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改密码成功");
        return redirectToUrl(null);
    }
    
    
}
