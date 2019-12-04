package com.ppcxy.cyfm.sys.entity.team;

import com.google.common.collect.Lists;
import com.ppcxy.common.entity.IdEntity;
import com.ppcxy.cyfm.sys.entity.user.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 开发团队.
 *
 * @author calvin
 */
@Entity
@Table(name = "cy_sys_team")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Team extends IdEntity {
    
    private String name;
    private User master;
    private List<User> userList = Lists.newArrayList();
    
    private List<User> newUserList = Lists.newArrayList();
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @NotNull
    @OneToOne
    @JoinColumn(name = "master_id")
    public User getMaster() {
        return master;
    }
    
    public void setMaster(User master) {
        this.master = master;
    }
    
    @OneToMany(mappedBy = "team")
    public List<User> getUserList() {
        return userList;
    }
    
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
    
    @Transient
    public List<User> getNewUserList() {
        return newUserList;
    }
    
    public void setNewUserList(List<User> newUserList) {
        this.newUserList = newUserList;
    }
}
