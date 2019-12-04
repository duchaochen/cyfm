package com.ppcxy.cyfm.manage.repository.jpa.maintaion.notification;

import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.cyfm.manage.entity.maintaion.notification.NotificationTemplate;
import org.springframework.data.jpa.repository.Query;

/**
 * <p>Date: 13-5-22 下午2:39
 * <p>Version: 1.0
 */
public interface NotificationTemplateDao extends BaseRepository<NotificationTemplate, Long> {

    @Query("from NotificationTemplate o where name=?1")
    NotificationTemplate findByName(String name);
}
