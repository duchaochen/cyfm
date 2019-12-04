package com.ppcxy.cyfm.task;

import com.ppcxy.common.spring.SpringContextHolder;
import com.ppcxy.manage.maintain.notification.support.NotificationApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Date: 14-1-18
 * <p>Version: 1.0
 */
@Service("testTask1")
public class TestTask1 {

    @Autowired
    private ApplicationContext ctx;

    public void run() {
        System.out.println("====hello test task1::" + ctx);
    }

    public void run1() {
    
        NotificationApi bean = SpringContextHolder.getBean(NotificationApi.class);
        Map<String, Object> map = new HashMap<>();
        map.put("title", "测试消息推送");
        map.put("message", "测试消息推送实时刷新.");
        bean.notify(1l, "commonTemplate", map);
    
    }
}
