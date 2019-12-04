package com.ppcxy.cyfm.sys.web.permission;

import com.google.common.collect.Sets;
import com.ppcxy.common.Constants;
import com.ppcxy.common.web.controller.BaseCRUDController;
import com.ppcxy.cyfm.sys.entity.permission.Role;
import com.ppcxy.cyfm.sys.entity.permission.RoleResourcePermission;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.service.permission.PermissionService;
import com.ppcxy.cyfm.sys.service.permission.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Created by weep on 2016-7-7.
 */
@Controller
@RequestMapping("/sys/role")
public class RoleController extends BaseCRUDController<Role, Long> {
    
    @Resource
    private RoleService roleService;
    
    @Resource
    private PermissionService permissionService;
    
    public RoleController() {
        setResourceIdentity("sys:role");
        setModelName("role");
    }
    
    @Override
    protected void preResponse(Model model) {
        super.preResponse(model);
        model.addAttribute("permissionList", permissionService.findAll());
    }
    
    
    @RequestMapping(value = "create/discard", method = RequestMethod.POST)
    @Override
    public String create(
            Model model, @Valid @ModelAttribute("entity") Role entity, BindingResult result,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL, RedirectAttributes redirectAttributes) {
        throw new RuntimeException("discarded method");
    }
    
    @RequestMapping(value = "/update/{id}/discard", method = RequestMethod.POST)
    @Override
    public String update(
            Model model, @Valid @ModelAttribute("entity") Role entity, BindingResult result,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        
        throw new RuntimeException("discarded method");
    }
    
    
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createWithResourcePermission(
            Model model,
            @Valid @ModelAttribute("entity") Role role, BindingResult result,
            @RequestParam(value = "resourceId", defaultValue = "") Long[] resourceIds,
            @RequestParam(value = "permissionIds", defaultValue = "") Long[][] permissionIds,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        
        fillResourcePermission(role, resourceIds, permissionIds);
        
        return super.create(model, role, result, backURL, redirectAttributes);
    }
    
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String updateWithResourcePermission(
            Model model,
            @Valid @ModelAttribute("entity") Role role, BindingResult result,
            @RequestParam(value = "resourceId", defaultValue = "") Long[] resourceIds,
            @RequestParam(value = "permissionIds", defaultValue = "") Long[][] permissionIds,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        
        fillResourcePermission(role, resourceIds, permissionIds);
        
        return super.update(model, role, result, backURL, redirectAttributes);
    }
    
    @RequestMapping(value = "/roleAllot/{id}", method = RequestMethod.GET)
    public String roleDetails(@PathVariable(value = "id") Role role, Model model) {
        super.beforView(model, role);
        
        List<User> users = roleService.findUsers(role);
        model.addAttribute("role", role);
        model.addAttribute("users", users);
        return viewName("details");
    }
    
    @RequestMapping(value = "/roleAllot/add/{type}", method = RequestMethod.POST)
    @ResponseBody
    public String allotRoleDetails(@PathVariable(value = "type") String type, Long roleId, @RequestParam(name = "targetIds") Long[] targetIds, Model model) {
        super.beforUpdateForm(null, model);
        roleService.addRoleAllot(type, roleId, targetIds);
        return "success";
    }
    
    @RequestMapping(value = "/roleAllot/remove/{type}", method = RequestMethod.POST)
    @ResponseBody
    public String removeRoleDetails(@PathVariable(value = "type") String type, Long roleId, Long targetId, Model model) {
        super.beforUpdateForm(null, model);
        roleService.removeRoleAllot(type, roleId, targetId);
        return "success";
    }
    
    private void fillResourcePermission(Role role, Long[] resourceIds, Long[][] permissionIds) {
        role.getRoleResourcePermissions();
        int resourceLength = resourceIds.length;
        if (resourceIds.length == 0) {
            role.clearResourcePermission(resourceIds);
            return;
        }
        
        //向角色中新增或追加资源权限
        if (resourceLength == 1) { //如果长度为1  那么permissionIds就变成如[[0],[1],[2]]这种
            Set<Long> permissionIdSet = Sets.newHashSet();
            for (Long[] permissionId : permissionIds) {
                permissionIdSet.add(permissionId[0]);
            }
            role.addResourcePermission(
                    new RoleResourcePermission(resourceIds[0], permissionIdSet)
            );
            
        } else {
            for (int i = 0; i < resourceLength; i++) {
                role.addResourcePermission(
                        new RoleResourcePermission(resourceIds[i], Sets.newHashSet(permissionIds[i]))
                );
            }
        }
        
        //清理被删除的资源权限
        role.clearResourcePermission(resourceIds);
    }
    
    @RequestMapping(value = "checkName")
    @ResponseBody
    public String checkName(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
        if (name.equals(oldName)) {
            return "true";
        } else if (roleService.findByName(name) == null) {
            return "true";
        }
        
        return "false";
    }
    
    @RequestMapping(value = "checkValue")
    @ResponseBody
    public String checkValue(@RequestParam("oldValue") String oldValue, @RequestParam("value") String value) {
        if (value.equals(oldValue)) {
            return "true";
        } else if (roleService.findByValue(value) == null) {
            return "true";
        }
        
        return "false";
    }
}
