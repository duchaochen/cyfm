package com.ppcxy.cyfm.sys.web.resource;

import com.ppcxy.common.extend.web.controller.BaseTreeableController;
import com.ppcxy.cyfm.sys.entity.resource.Resource;
import com.ppcxy.cyfm.sys.entity.resource.dto.MenuType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by weep on 2016-5-16.
 */
@Controller
@RequestMapping(value = "/sys/resource")
public class ResourceController extends BaseTreeableController<Resource, Long> {
    
    public ResourceController() {
        setResourceIdentity("sys:resource");
        setModelName("resource");
    }
    
    @Override
    protected void preResponse(Model model) {
        super.preResponse(model);
        model.addAttribute("resourceTypes", MenuType.values());
    }
    
}
