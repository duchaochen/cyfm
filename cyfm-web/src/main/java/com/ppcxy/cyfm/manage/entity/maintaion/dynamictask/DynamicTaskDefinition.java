package com.ppcxy.cyfm.manage.entity.maintaion.dynamictask;

import com.ppcxy.common.entity.IdEntity;
import com.ppcxy.common.task.TaskDefinition;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 定时任务 beanName.beanMethod 和 beanClass.beanMethod 二选一
 * <p>Date: 13-4-19 上午7:13
 * <p>Version: 1.0
 */
@Entity
@Table(name = "cy_maintain_task_definition")
public class DynamicTaskDefinition extends IdEntity implements TaskDefinition {
    
    
    private String name;
    
    /**
     * cron表达式
     */
    
    private String cron;
    
    
    /**
     * 要执行的class类名
     */
    
    private String beanClass;
    
    /**
     * 要执行的bean名字
     */
    
    private String beanName;
    
    /**
     * 要执行的bean的方法名
     */
    
    private String methodName;
    
    /**
     * 是否已启动
     */
    
    private Boolean start = Boolean.FALSE;
    
    /**
     * 描述
     */
    
    private String description;
    
    @Column(name = "name")
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name = "cron")
    public String getCron() {
        return cron;
    }
    
    public void setCron(String cron) {
        this.cron = cron;
    }
    
    @Column(name = "bean_class")
    public String getBeanClass() {
        return beanClass;
    }
    
    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }
    
    @Column(name = "bean_name")
    public String getBeanName() {
        return beanName;
    }
    
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    
    @Column(name = "method_name")
    public String getMethodName() {
        return methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    @Column(name = "is_start")
    public Boolean getStart() {
        return start;
    }
    
    public void setStart(Boolean start) {
        this.start = start;
    }
    
    @Column(name = "description")
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Transient
    public String getRunInfo() {
        if (this.start) {
            return "运行中";
        }
        
        if (StringUtils.isBlank(this.description)) {
            return "停止运行";
        }
        
        return String.format("运行失败,发生错误: %s", this.description);
    }
}
