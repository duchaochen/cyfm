package com.ppcxy.common.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser implements Serializable {
    
    public Long id;
    public String username;
    public String name;
    
    public ShiroUser(Long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getName() {
        return name;
    }
    
    
    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return username;
    }
    
    /**
     * 重载hashCode,只计算username;
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
    
    /**
     * 重载equals,只计算username;
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ShiroUser other = (ShiroUser) obj;
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }
}
