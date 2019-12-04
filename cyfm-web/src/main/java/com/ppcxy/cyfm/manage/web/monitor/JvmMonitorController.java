package com.ppcxy.cyfm.manage.web.monitor;

import com.ppcxy.common.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>Date: 13-5-27 下午6:50
 * <p>Version: 1.0
 */
@Controller
@RequestMapping("/manage/monitor/jvm")
@RequiresPermissions("monitor:jvm:*")
public class JvmMonitorController extends BaseController {
    
    @RequestMapping
    public String index() {
        return viewName("index");
    }
    
    
}
