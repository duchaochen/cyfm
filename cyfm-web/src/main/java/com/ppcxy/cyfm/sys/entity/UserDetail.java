package com.ppcxy.cyfm.sys.entity;

import com.ppcxy.common.entity.IdEntity;
import com.ppcxy.cyfm.sys.entity.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cy_sys_user_detail")
public class UserDetail extends IdEntity {
    
    private String realName;
    private String gender;
    private Date birthDate;
    private String province;
    private String city;
    private String address;
    private String zipCode;
    private User user;
    
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getProvince() {
        return province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public Date getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    @OneToOne()
    @JoinColumn(name = "user_id", unique = true, nullable = false, updatable = false)
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
