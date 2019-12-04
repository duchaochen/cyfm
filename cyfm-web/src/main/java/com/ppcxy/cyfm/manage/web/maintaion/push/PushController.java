package com.ppcxy.cyfm.manage.web.maintaion.push;

import com.google.common.collect.Maps;
import com.ppcxy.common.web.bind.annotation.CurrentUser;
import com.ppcxy.cyfm.manage.service.maintaion.push.PushService;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.manage.maintain.notification.support.NotificationApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 1、实时推送用户：消息和通知
 * <p>Date: 13-7-16 下午2:08
 * <p>Version: 1.0
 */
@Controller
public class PushController {
    
    
    @Autowired
    private NotificationApi notificationApi;
    
    @Autowired
    private PushService pushService;
    
    /**
     * 获取页面的提示信息
     *
     * @return
     */
    @RequestMapping(value = "/polling")
    @ResponseBody
    public Object polling(@RequestParam(defaultValue = "false") Boolean flush, HttpServletResponse resp, @CurrentUser User user) {
        try {
            
            Long userId = user.getId();
            if (userId == null) {
                return null;
            }
            
            //如果刷新消息
            if (flush) {
                pushService.offline(userId);
            } else {
                resp.setHeader("Connection", "Keep-Alive");
                resp.addHeader("Cache-Control", "private");
                resp.addHeader("Pragma", "no-cache");
            }
            
            //如果用户第一次来 立即返回
            if (!pushService.isOnline(userId)) {
                pushService.online(userId);
                Map<String, Object> data = Maps.newHashMap();
             
                //List<Map<String, Object>> notifications = notificationApi.topFiveNotification(user.getId());
                //data.put("unreadNotifications", notifications);
                
                Long unreadNotificationsCount = notificationApi.countUnread(userId);
                
                data.put("unreadNotificationsCount", unreadNotificationsCount);
                data.put("unreadNotifications", notificationApi.topFiveNotification(user.getId()));

                return data;
            } else {
                //长轮询
                return pushService.newDeferredResult(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
}
