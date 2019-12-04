package com.ppcxy.cyfm.manage.service.maintaion.notification;

import com.ppcxy.common.service.BaseService;
import com.ppcxy.cyfm.manage.entity.maintaion.notification.NotificationData;
import com.ppcxy.cyfm.manage.repository.jpa.maintaion.notification.NotificationDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>Date: 13-5-22 下午2:40
 * <p>Version: 1.0
 */
@Service
@Transactional
public class NotificationDataService extends BaseService<NotificationData, Long> {
    
    @Autowired
    private NotificationDataDao notificationDataDao;
    
    public void markReadAll(final Long userId) {
        notificationDataDao.markReadAll(userId);
    }
    
    
    public void markRead(final Long[] notificationId) {
        for (Long id : notificationId) {
            markRead(id);
        }
    }
    
    public void markRead(final Long notificationId) {
        NotificationData data = notificationDataDao.findOne(notificationId);
        if (data == null || data.getRead().equals(Boolean.TRUE)) {
            return;
        }
        data.setRead(Boolean.TRUE);
        update(data);
    }
    
    public List<NotificationData> loadUnread(Long userId) {
        return notificationDataDao.findUnread(userId);
    }
}
