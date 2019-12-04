package com.ppcxy.cyfm.bus.system.entity;

import com.ppcxy.common.entity.IdEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Table(name = "BUS_REMOTE_API")
public class RemoteApiModel extends IdEntity {
    //对应接入系统
    private JoinSystem joinSystem;
    //接入API名称
    private String remoteApiName;
    //api路径
    private String remoteApiPath;
    //访问方法
    private String requestMethod;
    //访问参数
    private String paramModel;
    //备注信息
    private String remark;
    
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "join_system_id")
    @Fetch(FetchMode.SELECT)
    public JoinSystem getJoinSystem() {
        return joinSystem;
    }
    
    public void setJoinSystem(JoinSystem joinSystem) {
        this.joinSystem = joinSystem;
    }
    
    public String getRemoteApiName() {
        return remoteApiName;
    }
    
    public void setRemoteApiName(String remoteApiName) {
        this.remoteApiName = remoteApiName;
    }
    
    public String getRemoteApiPath() {
        return remoteApiPath;
    }
    
    public void setRemoteApiPath(String remoteApiPath) {
        this.remoteApiPath = remoteApiPath;
    }
    
    public String getRequestMethod() {
        return requestMethod;
    }
    
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
    
    public String getParamModel() {
        return paramModel;
    }
    
    public void setParamModel(String paramModel) {
        this.paramModel = paramModel;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Transient
    public String getRemoteApi() {
        return String.format("%s%s%s", joinSystem.getSysAddress(), joinSystem.getSysBasePath(), this.remoteApiPath);
    }
}
