package com.ppcxy.cyfm.manage.entity.maintaion.notification;

import com.ppcxy.common.entity.IdEntity;
import com.ppcxy.common.repository.jpa.support.annotation.EnableQueryCache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 通知数据
 * <p>Date: 13-7-8 下午2:15
 * <p>Version: 1.0
 */
@Entity
@Table(name = "cy_maintain_notification_data")
@EnableQueryCache
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NotificationData extends IdEntity {
    
    /**
     * 接收通知的用户
     */
    private Long userId;
    
    /**
     * 通知所属系统
     */
    private NotificationSystem system;
    
    /**
     * 标题
     */
    private String title;
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 通知时间
     */
    private Date date;
    
    /**
     * 是否已读
     */
    
    private Boolean read = Boolean.FALSE;
    
    
    @NotNull(message = "{not.null}")
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(final Long userId) {
        this.userId = userId;
    }
    
    
    @NotNull(message = "{not.null}")
    @Enumerated(EnumType.STRING)
    public NotificationSystem getSystem() {
        return system;
    }
    
    public void setSystem(final NotificationSystem system) {
        this.system = system;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "publish_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }
    
    public void setDate(final Date date) {
        this.date = date;
    }
    
    @Column(name = "is_read")
    public Boolean getRead() {
        return read;
    }
    
    public void setRead(final Boolean read) {
        this.read = read;
    }
}
