package com.ppcxy.manage.maintain.notification.exception;

import com.ppcxy.common.exception.BaseException;

/**
 * <p>Date: 13-7-8 下午5:32
 * <p>Version: 1.0
 */
public class TemplateException extends BaseException {
    
    public TemplateException(final String code, final Object[] args) {
        super("notification", code, args);
    }
}
