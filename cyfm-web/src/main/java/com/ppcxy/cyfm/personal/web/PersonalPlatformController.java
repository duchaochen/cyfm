package com.ppcxy.cyfm.personal.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("personal/center")
public class PersonalPlatformController {
    
    @RequestMapping
    public String platform() {
        return "personal/personal_center";
    }
    
}
