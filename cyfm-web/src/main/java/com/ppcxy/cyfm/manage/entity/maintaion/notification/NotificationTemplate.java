package com.ppcxy.cyfm.manage.entity.maintaion.notification;

import com.ppcxy.common.entity.IdEntity;
import com.ppcxy.common.entity.base.LogicDeleteable;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 消息通知模板
 * <p>Date: 13-7-8 下午2:15
 * <p>Version: 1.0
 */
@Entity
@Table(name = "cy_maintain_notification_template")
public class NotificationTemplate extends IdEntity implements LogicDeleteable {
    
    /**
     * 模板名称 必须唯一 发送时使用
     */
    
    private String name;
    
    /**
     * 所属系统
     */
    
    private NotificationSystem system;
    
    
    /**
     * 模板标题
     */
    
    private String title;
    
    
    /**
     * 模板内容
     */
    private String template;
    
    /**
     * 是否已逻辑删除
     */
    private Boolean deleted = Boolean.FALSE;
    
    @NotNull(message = "{not.null}")
    @Length(min = 1, max = 100, message = "{length.not.valid}")
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @NotNull(message = "{not.null}")
    @Enumerated(EnumType.STRING)
    public NotificationSystem getSystem() {
        return system;
    }
    
    public void setSystem(final NotificationSystem system) {
        this.system = system;
    }
    
    @Length(min = 1, max = 200, message = "{length.not.valid}")
    public String getTitle() {
        return title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getTemplate() {
        return template;
    }
    
    public void setTemplate(final String template) {
        this.template = template;
    }
    
    public Boolean getDeleted() {
        return deleted;
    }
    
    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }
    
    public void markDeleted() {
        setDeleted(Boolean.TRUE);
    }
    
}
