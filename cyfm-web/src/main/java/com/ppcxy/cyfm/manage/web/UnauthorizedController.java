package com.ppcxy.cyfm.manage.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/unauthorized")
public class UnauthorizedController {
    
    @RequestMapping
    public String unauthorized(HttpServletRequest request, Model model) {
        
        return "";
    }
}
