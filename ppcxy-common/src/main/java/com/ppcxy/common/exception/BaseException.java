package com.ppcxy.common.exception;

import com.ppcxy.common.utils.MessageUtils;
import org.springframework.util.StringUtils;

/**
 * 基础异常
 */
public class BaseException extends RuntimeException {
    
    //所属模块
    private String module;
    
    /**
     * 错误码
     */
    private String code;
    
    /**
     * 错误码对应的参数
     */
    private Object[] args;
    
    /**
     * 错误消息
     */
    private String defaultMessage;
    
    
    public BaseException(String module, String code, Object[] args, String message) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = message;
    }
    
    public BaseException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }
    
    public BaseException(String module, String message) {
        this(module, null, null, message);
    }
    
    public BaseException(String code, Object[] args) {
        this(null, code, args, null);
    }
    
    public BaseException(String message) {
        this(null, null, null, message);
    }
    
    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.defaultMessage = message;
    }
    
    public BaseException(Throwable cause) {
        super(cause);
    }
    
    public BaseException() {
        super();
    }
    
    @Override
    public String getMessage() {
        String message = null;
        if (!StringUtils.isEmpty(code)) {
            message = MessageUtils.message(code, args);
        }
        if (message == null) {
            message = defaultMessage;
        }
        return message;
    }
    
    
    public String getModule() {
        return module;
    }
    
    public String getCode() {
        return code;
    }
    
    public Object[] getArgs() {
        return args;
    }
    
    public String getmessage() {
        return defaultMessage;
    }
    
    @Override
    public String toString() {
        return this.getClass() + "{" +
                "module='" + module + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
