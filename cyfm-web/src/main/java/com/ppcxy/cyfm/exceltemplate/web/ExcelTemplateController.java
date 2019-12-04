package com.ppcxy.cyfm.exceltemplate.web;

import com.ppcxy.cyfm.exceltemplate.entity.ExcelTemplate;
import com.ppcxy.cyfm.exceltemplate.service.ExcelTemplateService;
import com.ppcxy.cyfm.filestore.upload.entity.StoreFiles;
import com.ppcxy.cyfm.filestore.upload.repository.jpa.StoreFilesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/excel/excelTemplate")
public class ExcelTemplateController {
    @Autowired
    private ExcelTemplateService excelTemplateService;
    @Autowired
    private StoreFilesDao storeFilesDao;
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(ExcelTemplate excelTemplate) {
        
        excelTemplateService.save(excelTemplate);
        return "yes";
    }
    
    @RequestMapping(value = "/findresource", method = RequestMethod.POST)
    @ResponseBody
    public Object findresource(String resource) {
        ExcelTemplate excelTemplate = excelTemplateService.findByResourceIdentity(resource);
        List<StoreFiles> list = new ArrayList<>();
        if (excelTemplate != null) {
            String[] files = excelTemplate.getTemplateId().split(",");
            for (String file : files) {
                list.add(storeFilesDao.findOne(file));
            }
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("files", list);
        map.put("excelTemplate", excelTemplate);
        return map;
    }
    
    
}
