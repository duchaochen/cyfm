package com.ppcxy.cyfm.exceltemplate.service;

import com.ppcxy.common.service.BaseService;
import com.ppcxy.cyfm.exceltemplate.entity.ExcelTemplate;
import com.ppcxy.cyfm.exceltemplate.repository.jpa.ExcelTemplateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExcelTemplateService extends BaseService<ExcelTemplate, Long> {
    @Autowired
    private ExcelTemplateDao excelTemplateDao;
 
    public ExcelTemplate findByResourceIdentity(String resource) {
        return excelTemplateDao.findByResourceIdentity(resource);
    }
   /*

    public ExcelTemplate findByResourceIdentity(String modelName) {
        return excelTemplateDao.findByResourceIdentity(modelName);
    }*/
}
