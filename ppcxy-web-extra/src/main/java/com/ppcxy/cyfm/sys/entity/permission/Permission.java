package com.ppcxy.cyfm.sys.entity.permission;

import com.ppcxy.common.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wufab on 2016/8/11.
 */
@Entity
@Table(name = "cy_sys_permission")
public class Permission extends IdEntity {

    private String name;
    private String value;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
