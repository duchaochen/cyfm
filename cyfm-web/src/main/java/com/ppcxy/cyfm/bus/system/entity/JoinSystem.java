package com.ppcxy.cyfm.bus.system.entity;

import com.google.common.collect.Maps;
import com.ppcxy.common.entity.IdEntity;
import com.ppcxy.common.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springside.modules.mapper.JsonMapper;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "BUS_JOIN_SYSTEM")
public class JoinSystem extends IdEntity {
    //系统名称
    private String sysName;
    //系统地址,含端口
    private String sysAddress;
    //系统访问名
    private String sysBasePath;
    //系统授权api地址
    private String loginApiPath;
    //授权用户名
    private String authUsername;
    //授权密码
    private String authPassword;
    //授权接口访问方式
    private String requstMethod;
    //接入api
    private Set<RemoteApiModel> remoteApiModels;
    private String authUsernameKey;
    private String authPasswordKey;
    
    private String authOtherParams;
    
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "joinSystem")
    @Fetch(FetchMode.SELECT)
    //@NotFound(action = NotFoundAction.IGNORE)
    //@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)//集合缓存
    @OrderBy(value = "ID ASC")
    public Set<RemoteApiModel> getRemoteApiModels() {
        return remoteApiModels;
    }
    
    public void setRemoteApiModels(Set<RemoteApiModel> remoteApiModels) {
        this.remoteApiModels = remoteApiModels;
    }
    
    public String getSysName() {
        return sysName;
    }
    
    public void setSysName(String sysName) {
        this.sysName = sysName;
    }
    
    public String getSysAddress() {
        return sysAddress;
    }
    
    public void setSysAddress(String sysAddress) {
        this.sysAddress = sysAddress;
    }
    
    public String getSysBasePath() {
        return sysBasePath;
    }
    
    public void setSysBasePath(String sysBasePath) {
        this.sysBasePath = sysBasePath;
    }
    
    public String getLoginApiPath() {
        return loginApiPath;
    }
    
    public void setLoginApiPath(String loginApiPath) {
        this.loginApiPath = loginApiPath;
    }
    
    public String getAuthUsername() {
        return authUsername;
    }
    
    public void setAuthUsername(String authUsername) {
        this.authUsername = authUsername;
    }
    
    public String getAuthPassword() {
        return authPassword;
    }
    
    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }
    
    public String getRequstMethod() {
        return requstMethod;
    }
    
    public void setRequstMethod(String requstMethod) {
        this.requstMethod = requstMethod;
    }
    
    public String getAuthUsernameKey() {
        return authUsernameKey;
    }
    
    public void setAuthUsernameKey(String authUsernameKey) {
        this.authUsernameKey = authUsernameKey;
    }
    
    public String getAuthPasswordKey() {
        return authPasswordKey;
    }
    
    public void setAuthPasswordKey(String authPasswordKey) {
        this.authPasswordKey = authPasswordKey;
    }
    
    public String getAuthOtherParams() {
        return authOtherParams;
    }
    
    public void setAuthOtherParams(String authOtherParams) {
        this.authOtherParams = authOtherParams;
    }
    
    @Transient
    public String getLoginApi() {
        return String.format("%s%s%s", this.sysAddress, this.getSysBasePath(), this.loginApiPath);
    }
    
    @Transient
    public Map<String, Object> getLoginParams() {
        Map<String, Object> params = null;
        JsonMapper jsonMapper = new JsonMapper();
        
        if (StringUtils.isNotBlank(authOtherParams)) {
            try {
                params = jsonMapper.fromJson(authOtherParams, HashMap.class);
            } catch (Exception e) {
                params = Maps.newHashMap();
                LogUtils.logError(String.format("在通过总线登录%s[%s]系统时,追加参数[]读取错误.", this.getSysName(), this.getId(), this.authOtherParams), e);
            }
        } else {
            params = Maps.newHashMap();
        }
        params.put(this.authUsernameKey, this.authUsername);
        params.put(this.authPasswordKey, this.authPassword);
        return params;
    }
}
