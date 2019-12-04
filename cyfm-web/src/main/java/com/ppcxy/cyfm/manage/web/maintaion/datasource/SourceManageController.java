package com.ppcxy.cyfm.manage.web.maintaion.datasource;

import com.ppcxy.common.web.controller.BaseCRUDController;
import com.ppcxy.cyfm.manage.entity.maintaion.datasource.SourceManage;
import com.ppcxy.cyfm.manage.service.maintaion.datasource.SourceManageService;
import com.ppcxy.manage.maintain.datasource.conf.DataBaseType;
import com.ppcxy.manage.maintain.datasource.conf.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;

import java.util.UUID;

/**
 * Created by weep on 2016-5-23.
 */
@Controller
@RequestMapping("/manage/maintain/datasource")
public class SourceManageController extends BaseCRUDController<SourceManage, Long> {
    
    @Autowired
    private SourceManageService sourceManageService;
    
    public SourceManageController() {
        setResourceIdentity("maintain:datasource");
        //和资源一致可以不处理
        setModelName("datasource");
    }
    
    @Override
    protected void preResponse(Model model) {
        super.preResponse(model);
        model.addAttribute("dbTypes", DataBaseType.values());
        model.addAttribute("dsTypes", DataSourceType.values());
    }
    
    
    @RequestMapping(value = "validate")
    @ResponseBody
    public String validateDsm(SourceManage sourceManage) {
        sourceManage.setDsName(UUID.randomUUID().toString());
        boolean connectionSuccess = sourceManageService.validateDatasource(sourceManage);
        
        return JsonMapper.nonDefaultMapper().toJson(connectionSuccess);
    }
    
    
}
