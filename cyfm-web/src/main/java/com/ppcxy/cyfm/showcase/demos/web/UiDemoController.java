package com.ppcxy.cyfm.showcase.demos.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by weep on 2016-7-8.
 */
@Controller
@RequestMapping("/demo/ui")
public class UiDemoController {
    
    @RequestMapping(value = "dialog")
    public String dialogDemo() {
        return "/demo/ui/dialog";
    }
    
    @RequestMapping(value = "formDesigner")
    public String formdesignerDemo() {
        return "/demo/ui/formdesigner";
    }
}
