package com.ppcxy.cyfm.dp.cloudconfig.web;

import com.ppcxy.common.Constants;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.web.controller.BaseCRUDController;
import com.ppcxy.cyfm.dp.cloudconfig.entity.ConfigProperties;
import com.ppcxy.cyfm.dp.cloudconfig.service.ConfigPropertiesService;
import com.ppcxy.cyfm.manage.entity.maintaion.datasource.SourceManage;
import com.ppcxy.cyfm.manage.service.maintaion.datasource.SourceManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.mapper.JsonMapper;

@Controller
@RequestMapping(value = "/dp/cloudConfig")
public class ConfigPropertiesController extends BaseCRUDController<ConfigProperties, Long> {
    
    @Autowired
    private ConfigPropertiesService configPropertiesService;
    
    @Autowired
    private SourceManageService sourceManageService;
    
    public ConfigPropertiesController() {
        setResourceIdentity("dp:cloudConfig");
        //和资源一致可以不处理
        setModelName("cloudConfig");
    }
    
    @Override
    protected void preResponse(Model model) {
        super.preResponse(model);
        model.addAttribute("datasources", sourceManageService.findAll());
    }
    
    @Override
    public String list(Searchable searchable, Model model) {
        beforList(searchable, model);
        
        model.addAttribute("configs", configPropertiesService.queryAll());
        
        return viewName("list");
    }
    
    
    /**
     * 根据application 和 profile 删除一组配置信息
     *
     * @param application
     * @param profile
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String delete(String application, String profile, RedirectAttributes redirectAttributes) {
        beforDelete(0l, null, redirectAttributes);
        
        configPropertiesService.removeByApplicationAndProfile(application, profile);
        return JsonMapper.nonEmptyMapper().toJson(true);
    }
    
    
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(String application, String profile, @RequestParam("sourceManageId") SourceManage sourceManage, @RequestParam(value = Constants.BACK_URL, required = false) String backURL, RedirectAttributes redirectAttributes) {
        
        if (configPropertiesService.existsProfile(sourceManage)) {
            redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE, String.format("已存在此数据源的配置项 [profile=%s].", configPropertiesService.queryProfile(sourceManage).get("profile")));
            return redirectToUrl(backURL);
        }
        configPropertiesService.genDataTaskConfig(application, profile, sourceManage);
        
        return redirectToUrl(backURL);
    }
    
    @RequestMapping(value = "/create/discard", method = RequestMethod.POST)
    public String create(Model model, ConfigProperties entity, BindingResult result, @RequestParam(value = Constants.BACK_URL, required = false) String backURL, RedirectAttributes redirectAttributes) {
        beforCreate(model, entity, result, redirectAttributes);
        
        throw new RuntimeException("discarded method");
    }
    
}
