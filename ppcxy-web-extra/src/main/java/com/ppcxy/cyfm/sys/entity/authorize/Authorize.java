package com.ppcxy.cyfm.sys.entity.authorize;

import com.ppcxy.common.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by wufab on 2016/8/11.
 */
@Entity
@Table(name = "cy_sys_authorize")
public class Authorize extends IdEntity {

    private Long userId;
    private Long targetId;

    private AuthorizeType authType = AuthorizeType.Role;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    @Enumerated(value = EnumType.STRING)
    public AuthorizeType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthorizeType authType) {
        this.authType = authType;
    }
}
