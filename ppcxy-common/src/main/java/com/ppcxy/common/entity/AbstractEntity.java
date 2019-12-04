package com.ppcxy.common.entity;


import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by weep on 2016-5-17.
 */
public abstract class AbstractEntity<ID extends Serializable> {
    protected ID id;
    
    public ID getId() {
        return id;
    }
    
    public void setId(ID id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
