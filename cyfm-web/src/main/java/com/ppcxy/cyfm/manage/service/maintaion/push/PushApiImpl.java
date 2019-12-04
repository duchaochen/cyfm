package com.ppcxy.cyfm.manage.service.maintaion.push;

import com.google.common.collect.Maps;
import com.ppcxy.manage.maintain.notification.support.PushApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>Date: 13-7-17 下午2:28
 * <p>Version: 1.0
 */
@Service
@Transactional
public class PushApiImpl implements PushApi {

    @Autowired
    private PushService pushService;

    @Override
    public void pushUnreadMessage(final Long userId, List<Map<String, Object>> unreadMessages,Long unreadMessagesContent) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("unreadMessages", unreadMessages);
        data.put("unreadMessagesCount", unreadMessagesContent);
        pushService.push(userId, data);
    }

    @Override
    public void pushNewNotification(final Long userId, List<Map<String, Object>> unreadNotifiations,Long notificationsCount) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("unreadNotifications", unreadNotifiations);
        data.put("unreadNotificationsCount", notificationsCount);
        pushService.push(userId, data);
    }

    @Override
    public void offline(final Long userId) {
        pushService.offline(userId);
    }
}
