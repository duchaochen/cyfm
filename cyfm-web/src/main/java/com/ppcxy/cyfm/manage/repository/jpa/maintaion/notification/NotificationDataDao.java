package com.ppcxy.cyfm.manage.repository.jpa.maintaion.notification;

import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.cyfm.manage.entity.maintaion.notification.NotificationData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>Date: 13-5-22 下午2:39
 * <p>Version: 1.0
 */
public interface NotificationDataDao extends BaseRepository<NotificationData, Long> {
    
    @Modifying
    @Query("update NotificationData o set o.read=true where userId=?1")
    void markReadAll(Long userId);
    
    @Query("select o from NotificationData o where o.read=false and userId=?1 order by o.date desc, id desc")
    List<NotificationData> findUnread(Long userId);
}
