package org.apache.shiro.exception;


import com.ppcxy.common.exception.BaseException;

/**
 */
public class UserException extends BaseException {

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }

}
