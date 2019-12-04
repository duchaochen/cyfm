package com.ppcxy.cyfm.manage.web.maintaion.notification;

import com.ppcxy.common.Constants;
import com.ppcxy.common.entity.enums.ReadEnum;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.utils.MessageUtils;
import com.ppcxy.common.web.bind.annotation.CurrentUser;
import com.ppcxy.common.web.bind.annotation.PageableDefaults;
import com.ppcxy.common.web.controller.BaseCRUDController;
import com.ppcxy.cyfm.manage.entity.maintaion.notification.NotificationData;
import com.ppcxy.cyfm.manage.entity.maintaion.notification.NotificationSystem;
import com.ppcxy.cyfm.manage.service.maintaion.notification.NotificationDataService;
import com.ppcxy.cyfm.sys.entity.user.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * <p>Date: 13-7-9 下午2:01
 * <p>Version: 1.0
 */
@Controller
@RequestMapping("/manage/maintain/notification")
public class NotificationController extends BaseCRUDController<NotificationData, Long> {
    public NotificationController() {
        //和资源一致可以不处理
        setResourceIdentity("maintain:notification");
        setModelName("notification");
        
    }
    
    @Autowired
    private NotificationDataService notificationDataService;
    
    @ModelAttribute
    protected void preResponse(Model model) {
        //覆盖请务必调用super.preResponse()
        super.preResponse(model);
        model.addAttribute("customToolbar", true);
        model.addAttribute("systemList", NotificationSystem.values());
        model.addAttribute("readList", ReadEnum.values());
    }
    
    @RequestMapping(value = "no/no/no1", method = RequestMethod.GET)
    public String list(Searchable searchable, Model model) {
        return null;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @PageableDefaults(sort = {"date=desc", "id=desc"})
    public String list(Searchable searchable, Model model, @CurrentUser User loginUser) {
        
        searchable.addSearchParam("userId_eq", loginUser.getId());
        return super.list(searchable, model);
    }
    
    @RequestMapping(value = "no/no/no2", method = RequestMethod.GET, headers = "table=true")
    @Override
    public String listTable(Searchable searchable, Model model) {
        return null;
    }
    
    
    @RequestMapping(method = RequestMethod.GET, headers = "table=true")
    @PageableDefaults(sort = {"date=desc", "id=desc"})
    public String listTable(Searchable searchable, @CurrentUser User loginUser, Model model) {
        searchable.addSearchParam("userId_eq", loginUser.getId());
        super.list(searchable, model);
        return viewName("listTable");
    }
    
    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(Model model, @PathVariable("id") NotificationData m) {
        
        notificationDataService.markRead(m.getId());
        return super.view(model, m);
    }
    
    @RequestMapping(value = "/markRead/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String markReadOne(@PathVariable("id") Long id) {
        notificationDataService.markRead(id);
        return "true";
    }
    
    @RequestMapping(value = "/markReadAll", method = RequestMethod.GET)
    @ResponseBody
    public String markReadAll(@CurrentUser User user) {
        if (!SecurityUtils.getSubject().hasRole("Admin") && SecurityUtils.getSubject().isPermitted("maintain:notification:markReadAll") ) {
            throw new UnauthorizedException(MessageUtils.message(null, "maintain:notification:markReadAll"));
        }
        notificationDataService.markReadAll(user.getId());
        return "true";
    }
    
    @RequestMapping(value = "markRead", method = RequestMethod.GET)
    public String markRead(@RequestParam("ids") Long[] ids, @RequestParam(value = Constants.BACK_URL, required = false) String backURL, RedirectAttributes redirectAttributes) {
        notificationDataService.markRead(ids);
        
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "标记已读成功");
        return redirectToUrl(backURL);
    }
    
    @RequestMapping(value = "/list/unread", method = RequestMethod.GET)
    public String unreadList(Model model, @CurrentUser User loginUser) {
        
        model.addAttribute("unreads", notificationDataService.loadUnread(loginUser.getId()));
        return viewName("unread");
    }
    
    
}
