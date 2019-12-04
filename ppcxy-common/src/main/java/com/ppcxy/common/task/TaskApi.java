package com.ppcxy.common.task;


/**
 * <p>Date: 14-1-17
 * <p>Version: 1.0
 */
public interface TaskApi {
    
    public void addTaskDefinition(TaskDefinition taskDefinition);
    
    public void updateTaskDefinition(TaskDefinition taskDefinition);
    
    public void deleteTaskDefinition(boolean forceTermination, Long... taskDefinitionIds);
    
    
    public void startTask(Long... taskDefinitionIds);
    
    public void stopTask(boolean forceTermination, Long... taskDefinitionId);
    
}
