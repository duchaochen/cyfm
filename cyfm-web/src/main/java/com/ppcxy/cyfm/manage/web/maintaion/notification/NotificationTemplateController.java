package com.ppcxy.cyfm.manage.web.maintaion.notification;

import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.web.bind.annotation.PageableDefaults;
import com.ppcxy.common.web.bind.annotation.SearchableDefaults;
import com.ppcxy.common.web.controller.BaseCRUDController;
import com.ppcxy.cyfm.manage.entity.maintaion.notification.NotificationSystem;
import com.ppcxy.cyfm.manage.entity.maintaion.notification.NotificationTemplate;
import com.ppcxy.cyfm.manage.service.maintaion.notification.NotificationTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>Date: 13-1-28 下午4:29
 * <p>Version: 1.0
 */
@Controller
@RequestMapping(value = "/manage/maintain/notificationTemplate")
public class NotificationTemplateController extends BaseCRUDController<NotificationTemplate, Long> {
    
    @Autowired
    private NotificationTemplateService notificationTemplateService;
    
    public NotificationTemplateController() {
        setResourceIdentity("maintain:notificationTemplate");
        setModelName("notification_template");
    }
    
    @Override
    protected void preResponse(Model model) {
        super.preResponse(model);
        model.addAttribute("notificationSystems", NotificationSystem.values());
    }
    
    
    @Override
    @RequestMapping(method = RequestMethod.GET)
    @PageableDefaults(sort = "id=desc")
    @SearchableDefaults(value = "deleted_eq=false", merge = true)
    public String list(Searchable searchable, Model model) {
        return super.list(searchable, model);
    }
    
    @Override
    @SearchableDefaults(value = "deleted_eq=false", merge = true)
    public String listTable(Searchable searchable, Model model) {
        return super.listTable(searchable, model);
    }
    
    /**
     * 验证失败返回true
     *
     * @param m
     * @param result
     * @return
     */
    @Override
    protected boolean hasError(NotificationTemplate m, BindingResult result) {
        Assert.notNull(m);
        
        NotificationTemplate template = notificationTemplateService.findByName(m.getName());
        if (template == null || (template.getId().equals(m.getId()) && template.getName().equals(m.getName()))) {
            //success
        } else {
            result.rejectValue("name", "field.exist.valid", new Object[]{"模板名称"}, "");
        }
        
        return result.hasErrors();
    }
    
    /**
     * 验证返回格式
     * 单个：[fieldId, 1|0, msg]
     * 多个：[[fieldId, 1|0, msg],[fieldId, 1|0, msg]]
     *
     * @param fieldId
     * @param fieldValue
     * @return
     */
    @RequestMapping(value = "validate", method = RequestMethod.POST)
    @ResponseBody
    public Object validate(
            @RequestParam("fieldId") String fieldId, @RequestParam("fieldValue") String fieldValue,
            @RequestParam(value = "id", required = false) Long id) {
        
        if ("name".equals(fieldId)) {
            NotificationTemplate template = notificationTemplateService.findByName(fieldValue);
            //如果存在记录,并且id与当前id不相同则不允许
            if (template == null || (template.getId().equals(id) && template.getName().equals(fieldValue))) {
                //success
            } else {
                return false;
            }
        }
        return true;
    }
    
    
}
