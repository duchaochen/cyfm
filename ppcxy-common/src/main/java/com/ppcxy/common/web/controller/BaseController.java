package com.ppcxy.common.web.controller;

import com.ppcxy.common.entity.AbstractEntity;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.modules.utils.Reflections;

import java.io.Serializable;

/**
 * 基础控制器
 */
public abstract class BaseController<T extends AbstractEntity, ID extends Serializable> {
    
    /**
     * 实体类型
     */
    protected final Class<T> entityClass;
    
    private String viewPrefix;
    
    private String modelName;
    
    protected BaseController() {
        
        this.entityClass = Reflections.getClassGenricType(getClass());
        setViewPrefix(defaultViewPrefix());
    }
    
    
    /**
     * 模块名称：如user
     * 则使用页面为 user_list.jsp user_form.jsp
     */
    protected void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    /**
     * 模块名称：如user
     * 则使用页面为 user_list.jsp user_form.jsp
     */
    protected String getModelName() {
        return this.modelName;
    }
    
    /**
     * 设置通用数据
     *
     * @param model
     */
    protected void preResponse(Model model) {
        model.addAttribute("viewPrefix", viewPrefix);
        model.addAttribute("modelName", modelName);
    }
    
    public String getViewPrefix() {
        return viewPrefix;
    }
    
    /**
     * 当前模块 视图的前缀
     * 默认
     * 1、获取当前类头上的@RequestMapping中的value作为前缀
     * 2、如果没有就使用当前模型小写的简单类名
     */
    private void setViewPrefix(String viewPrefix) {
        if (viewPrefix.startsWith("/")) {
            viewPrefix = viewPrefix.substring(1);
        }
        this.viewPrefix = viewPrefix;
    }
    
    protected T newEntity() {
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("can not instantiated model : " + this.entityClass, e);
        }
    }
    
    /**
     * 获取视图名称：即prefixViewName + "/" + suffixName
     *
     * @return
     */
    protected String viewName(String suffixName) {
        if (!suffixName.startsWith("/")) {
            if (modelName != null) {
                suffixName = modelName + "_" + suffixName;
            }
            suffixName = "/" + suffixName;
        }
        return getViewPrefix() + suffixName;
    }
    
    /**
     * 共享的验证规则
     * 验证失败返回true
     *
     * @param entity
     * @param result
     * @return
     */
    protected boolean hasError(T entity, BindingResult result) {
        Assert.notNull(entity);
        return result.hasErrors();
    }
    
    /**
     * @param backURL null 将重定向到默认getViewPrefix()
     * @return
     */
    protected String redirectToUrl(String backURL) {
        if (StringUtils.isEmpty(backURL)) {
            backURL = getViewPrefix();
        }
        if (!backURL.startsWith("/") && !backURL.startsWith("http")) {
            backURL = "/" + backURL;
        }
        return "redirect:" + backURL;
    }
    
    private String defaultViewPrefix() {
        String currentViewPrefix = "";
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(getClass(), RequestMapping.class);
        if (requestMapping != null && requestMapping.value().length > 0) {
            currentViewPrefix = requestMapping.value()[0];
        }
        
        if (StringUtils.isEmpty(currentViewPrefix)) {
            currentViewPrefix = this.entityClass.getSimpleName();
        }
        
        return currentViewPrefix;
    }
}
