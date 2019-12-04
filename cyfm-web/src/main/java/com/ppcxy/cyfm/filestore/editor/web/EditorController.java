package com.ppcxy.cyfm.filestore.editor.web;

import com.ppcxy.cyfm.filestore.editor.support.ActionEnter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.MediaTypes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/filestore/editor")
public class EditorController {
    
    @RequestMapping(value = "init",produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public String init(HttpServletRequest request) {
    
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        
        return new ActionEnter(request, rootPath).exec();
    }
}
