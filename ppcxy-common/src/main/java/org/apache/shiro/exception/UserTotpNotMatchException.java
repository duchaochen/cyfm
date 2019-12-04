package org.apache.shiro.exception;

/**
 */
public class UserTotpNotMatchException extends UserException {

    public UserTotpNotMatchException() {
        super("user.totpcode.not.match", null);
    }
}
