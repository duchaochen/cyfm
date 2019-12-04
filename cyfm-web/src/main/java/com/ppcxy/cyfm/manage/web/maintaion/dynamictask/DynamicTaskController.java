package com.ppcxy.cyfm.manage.web.maintaion.dynamictask;

import com.ppcxy.common.Constants;
import com.ppcxy.common.task.TaskApi;
import com.ppcxy.common.web.controller.BaseCRUDController;
import com.ppcxy.cyfm.manage.entity.maintaion.dynamictask.DynamicTaskDefinition;
import com.ppcxy.cyfm.manage.service.maintaion.dynamictask.DynamicTaskDefinitionService;
import com.ppcxy.manage.maintain.dynamictask.utils.CronParseUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.MediaTypes;

import javax.annotation.Resource;

/**
 * <p>Date: 13-1-28 下午4:29
 * <p>Version: 1.0
 */
@Controller
@RequestMapping(value = "/manage/maintain/dynamicTask")
public class DynamicTaskController extends BaseCRUDController<DynamicTaskDefinition, Long> {
    
    public DynamicTaskController() {
        setResourceIdentity("maintain:dynamicTask");
        //和资源一致可以不处理
        setModelName("task");
    }
    
    @Resource(name = "dynamicTaskApiImpl")
    private TaskApi dynamicTaskApi;
    
    @Override
    public void afterCreate(Model model, DynamicTaskDefinition entity, BindingResult result, RedirectAttributes redirectAttributes) {
        super.afterCreate(model, entity, result, redirectAttributes);
        dynamicTaskApi.addTaskDefinition(entity);
    }
    
    
    @Override
    public void afterUpdate(Model model, DynamicTaskDefinition entity, BindingResult result, String backURL, RedirectAttributes redirectAttributes) {
        super.afterUpdate(model, entity, result, backURL, redirectAttributes);
        dynamicTaskApi.updateTaskDefinition(entity);
    }
    

    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    public String delete(
            @RequestParam(value = "forceTermination") boolean forceTermination,
            @PathVariable("id") DynamicTaskDefinition entity,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }

        dynamicTaskApi.deleteTaskDefinition(forceTermination, entity.getId());
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        return redirectToUrl(backURL);
    }
    
    @RequestMapping(value = "batch/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteInBatch(
            @RequestParam(value = "forceTermination") boolean forceTermination,
            @RequestParam(value = "ids", required = false) Long[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }
        
        dynamicTaskApi.deleteTaskDefinition(forceTermination, ids);
        
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        return redirectToUrl(backURL);
    }
    
    
    @RequestMapping("/start")
    public String startTask(
            @RequestParam(value = "ids", required = false) Long[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }
        
        try {
            dynamicTaskApi.startTask(ids);
            redirectAttributes.addFlashAttribute(Constants.MESSAGE, "启动任务成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE, "启动任务失败");
        }
        
       
        return redirectToUrl(backURL);
    }
    
    @RequestMapping("/stop")
    public String stopTask(
            @RequestParam(value = "forceTermination") boolean forceTermination,
            @RequestParam(value = "ids", required = false) Long[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }
        
        dynamicTaskApi.stopTask(forceTermination, ids);
        
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "停止任务成功");
        return redirectToUrl(backURL);
    }
    
    
    @RequestMapping(value = "{id}/delete/discard", method = RequestMethod.POST)
    @Override
    public String delete(@PathVariable("id") Long id, String backURL, RedirectAttributes redirectAttributes) {
        return super.delete(id, backURL, redirectAttributes);
    }
    
    
    @RequestMapping(value = "batch/delete/discard", method = {RequestMethod.GET, RequestMethod.POST})
    @Override
    public String deleteInBatch(@RequestParam(value = "ids", required = false) Long[] longs, @RequestParam(value = Constants.BACK_URL, required = false) String backURL, RedirectAttributes redirectAttributes) {
        throw new RuntimeException("discard method");
    }
    
    @RequestMapping(value = "cronParse", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Object cronParse(String cronExpression) {
        return CronParseUtils.parse(cronExpression);
    }
    
    
    @RequestMapping(value = "validate", method = RequestMethod.POST)
    @ResponseBody
    public Object validate(
            @RequestParam("fieldName") String fieldName, @RequestParam("fieldValue") String fieldValue,
            @RequestParam(value = "id", required = false) Long id) {
        
        if ("name".equals(fieldName)) {
            DynamicTaskDefinition task = ((DynamicTaskDefinitionService) baseService).findByName(fieldValue);
            if (task == null || (task.getId().equals(id))) {
                //success
            } else {
                return false;
            }
        }
        
        return true;
    }
}
