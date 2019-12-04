package com.ppcxy.cyfm.sys.web.permission;

import com.ppcxy.common.web.controller.BaseCRUDController;
import com.ppcxy.cyfm.sys.entity.permission.Permission;
import com.ppcxy.cyfm.sys.service.permission.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by weep on 2016-7-7.
 */
@Controller
@RequestMapping("/sys/permission")
public class PermissionController extends BaseCRUDController<Permission, Long> {
    
    @Resource
    private PermissionService permissionService;
    
    public PermissionController() {
        setResourceIdentity("sys:permission");
        setModelName("permission");
    }
    
    @RequestMapping(value = "checkName")
    @ResponseBody
    public String checkName(@RequestParam("oldName") String oldName, @RequestParam("name") String name) {
        if (name.equals(oldName)) {
            return "true";
        } else if (permissionService.findByName(name) == null) {
            return "true";
        }
        
        return "false";
    }
    
    @RequestMapping(value = "checkValue")
    @ResponseBody
    public String checkValue(@RequestParam("oldValue") String oldValue, @RequestParam("value") String value) {
        if (value.equals(oldValue)) {
            return "true";
        } else if (permissionService.findByValue(value) == null) {
            return "true";
        }
        
        return "false";
    }
}
