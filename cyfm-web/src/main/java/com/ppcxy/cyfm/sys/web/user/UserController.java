
package com.ppcxy.cyfm.sys.web.user;

import com.ppcxy.common.Constants;
import com.ppcxy.common.web.controller.BaseCRUDController;
import com.ppcxy.cyfm.sys.entity.permission.Role;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.entity.user.UserStatus;
import com.ppcxy.cyfm.sys.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/sys/user")
public class UserController extends BaseCRUDController<User, Long> {
    
    @Autowired
    private UserService userService;
    
    public UserController() {
        setResourceIdentity("sys:user");
        setModelName("user");
    }
    
    @Override
    protected void preResponse(Model model) {
        super.preResponse(model);
        model.addAttribute("allStatus", UserStatus.values());
        model.addAttribute("allRoles", userService.getAllRole());
    }
    
    
    // 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
    //@RequiresRoles(value = {"Admin", "User"}, logical = Logical.OR)
    
    @Override
    @RequestMapping(value = "create/disabled", method = RequestMethod.POST)
    public String create(Model model, @Valid @ModelAttribute("entity") User entity, BindingResult result, @RequestParam(value = Constants.BACK_URL, required = false) String backURL, RedirectAttributes redirectAttributes) {
        throw new RuntimeException("discarded method");
    }
    
    @Override
    @RequestMapping(value = "update/disabled", method = RequestMethod.POST)
    public String update(
            Model model, @Valid @ModelAttribute("entity") User entity, BindingResult result,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        throw new RuntimeException("discarded method");
    }
    
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(Model model, @Valid @ModelAttribute("entity") User user, BindingResult result, @RequestParam(value = "roleList", required = false, defaultValue = "") List<Long> checkedRoleList, @RequestParam(value = Constants.BACK_URL, required = false) String backURL, RedirectAttributes redirectAttributes) {
        user.getRoleList().clear();
        for (Long roleId : checkedRoleList) {
            Role role = new Role(roleId);
            user.getRoleList().add(role);
        }
        return super.create(model, user, result, backURL, redirectAttributes);
    }
    
    
    /**
     * 演示自行绑定表单中的checkBox roleList到对象中.
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    public String update(Model model, @Valid @ModelAttribute("entity") User user, BindingResult result,
                         @RequestParam(value = "roleList", required = false, defaultValue = "") List<Long> checkedRoleList, RedirectAttributes redirectAttributes) {
        
        user.getRoleList().clear();
        for (Long roleId : checkedRoleList) {
            Role role = new Role(roleId);
            user.getRoleList().add(role);
        }
        
        return super.update(model, user, result, null, redirectAttributes);
    }
    
    @RequestMapping(value = "checkUsername")
    @ResponseBody
    public String checkUsername(@RequestParam("oldUsername") String oldUsername,
                                @RequestParam("username") String username) {
        if (username.equals(oldUsername)) {
            return "true";
        } else if (userService.findByUsername(username) == null) {
            return "true";
        }
        
        return "false";
    }
    
    @RequestMapping(value = "checkEmail")
    @ResponseBody
    public String checkEmail(@RequestParam("oldEmail") String oldEmail,
                             @RequestParam("email") String email) {
        if (email.equals(oldEmail)) {
            return "true";
        } else if (userService.findByEmail(email) == null) {
            return "true";
        }
        
        return "false";
    }
    
    @RequestMapping(value = "checkTel")
    @ResponseBody
    public String checkTel(@RequestParam("oldTel") String oldTel,
                           @RequestParam("tel") String tel) {
        if (tel.equals(oldTel)) {
            return "true";
        } else if (userService.findByTel(tel) == null) {
            return "true";
        }
        
        return "false";
    }
    
    /**
     * 不自动绑定对象中的roleList属性，另行处理。
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("roleList");
    }
}
