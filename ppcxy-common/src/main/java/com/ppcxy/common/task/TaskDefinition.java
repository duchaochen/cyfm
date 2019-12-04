package com.ppcxy.common.task;

public interface TaskDefinition {
    
    Long getId();
    
    Boolean getStart();
    
    void setStart(Boolean state);

    String getCron();
}
