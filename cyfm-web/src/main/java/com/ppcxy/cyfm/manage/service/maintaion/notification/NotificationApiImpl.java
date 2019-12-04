package com.ppcxy.cyfm.manage.service.maintaion.notification;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ppcxy.common.entity.search.SearchOperator;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.utils.PrettyTimeUtils;
import com.ppcxy.cyfm.manage.entity.maintaion.notification.NotificationData;
import com.ppcxy.cyfm.manage.entity.maintaion.notification.NotificationTemplate;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.service.user.UserService;
import com.ppcxy.manage.maintain.notification.exception.TemplateNotFoundException;
import com.ppcxy.manage.maintain.notification.support.NotificationApi;
import com.ppcxy.manage.maintain.notification.support.PushApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>Date: 13-7-8 下午5:25
 * <p>Version: 1.0
 */
@Service
@Transactional
public class NotificationApiImpl implements NotificationApi {
    
    @Autowired
    private NotificationTemplateService notificationTemplateService;
    
    @Autowired
    private NotificationDataService notificationDataService;
    
    @Autowired
    private PushApi pushApi;
    
    @Autowired
    private UserService userService;
    
    /**
     * 异步发送
     *
     * @param userId       接收人用户编号
     * @param templateName 模板名称
     * @param context      模板需要的数据
     */
    @Async
    @Override
    public void notify(final Long userId, final String templateName, final Map<String, Object> context) {
        NotificationTemplate template = notificationTemplateService.findByName(templateName);
        
        if (template == null) {
            throw new TemplateNotFoundException(templateName);
        }
        
        NotificationData data = new NotificationData();
        
        data.setUserId(userId);
        data.setSystem(template.getSystem());
        data.setDate(new Date());
        
        String content = template.getTemplate();
        String title = template.getTitle();
        //TODO 暂时使用前端替换ctx方案
        //context.put("ctx", ServletUtils.loadContentPath());
        if (context != null) {
            for (String key : context.keySet()) {
                //TODO 如果量大可能有性能问题 需要调优
                title = title.replace("{" + key + "}", String.valueOf(context.get(key)));
                content = content.replace("{" + key + "}", String.valueOf(context.get(key)));
            }
        }
        
        data.setTitle(title);
        data.setContent(content);
        
        notificationDataService.save(data);
        
        
        pushApi.pushNewNotification(userId, topFiveNotification(userId), countUnread(userId));
        
    }
    
    @Async
    @Override
    public void notify(String username, String templateName, Map<String, Object> context) throws TemplateNotFoundException {
        User user = userService.findByUsername(username);
        this.notify(user.getId(), templateName, context);
    }
    
    @Override
    public List<Map<String, Object>> topFiveNotification(final Long userId) {
        
        List<Map<String, Object>> dataList = Lists.newArrayList();
        
        Searchable searchable = Searchable.newSearchable();
        searchable.addSearchFilter("userId", SearchOperator.eq, userId);
        searchable.addSearchFilter("read", SearchOperator.eq, Boolean.FALSE);
        searchable.addSort(Sort.Direction.DESC, "id");
        searchable.setPage(0, 5);
        
        Page<NotificationData> page = notificationDataService.findAll(searchable);
        
        for (NotificationData data : page.getContent()) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", data.getId());
            map.put("title", data.getTitle());
            map.put("content", data.getContent());
            map.put("read", data.getRead());
            map.put("date", PrettyTimeUtils.prettyTime(data.getDate()));
            dataList.add(map);
        }
        
        return dataList;
    }
    
    @Override
    public Long countUnread(Long userId) {
        Searchable searchable = Searchable.newSearchable();
        searchable.addSearchFilter("userId", SearchOperator.eq, userId);
        searchable.addSearchFilter("read", SearchOperator.eq, Boolean.FALSE);
        searchable.addSort(Sort.Direction.DESC, "id");
        
        return notificationDataService.count(searchable);
    }
    
    
}
