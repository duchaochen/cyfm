package com.ppcxy.cyfm.nbpm.calcengine.funs.exception;

import com.ppcxy.common.exception.BaseException;

public class CalcExecuteException extends BaseException {
    public CalcExecuteException(String message) {
        super(message);
    }
    
    public CalcExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
