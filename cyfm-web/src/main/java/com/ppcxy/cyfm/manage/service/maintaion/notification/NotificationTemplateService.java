package com.ppcxy.cyfm.manage.service.maintaion.notification;

import com.ppcxy.common.service.BaseService;
import com.ppcxy.cyfm.manage.entity.maintaion.notification.NotificationTemplate;
import com.ppcxy.cyfm.manage.repository.jpa.maintaion.notification.NotificationTemplateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Date: 13-5-22 下午2:40
 * <p>Version: 1.0
 */
@Service
@Transactional
public class NotificationTemplateService extends BaseService<NotificationTemplate, Long> {
    
    @Autowired
    private NotificationTemplateDao notificationTemplateDao;

    public NotificationTemplate findByName(final String name) {
        return notificationTemplateDao.findByName(name);
    }
}
