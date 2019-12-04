package com.ppcxy.manage.maintain.notification.exception;

/**
 * <p>Date: 13-7-8 下午5:34
 * <p>Version: 1.0
 */
public class TemplateNotFoundException extends TemplateException {
    public TemplateNotFoundException(String templateName) {
        super("notification.template.not.found", new Object[]{templateName});
    }
}
