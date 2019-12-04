package com.ppcxy.cyfm.manage.service.maintaion.dynamictask;

import com.ppcxy.common.service.BaseService;
import com.ppcxy.cyfm.manage.entity.maintaion.dynamictask.DynamicTaskDefinition;
import com.ppcxy.common.task.TaskDefinition;
import com.ppcxy.cyfm.manage.repository.jpa.maintaion.dynamictask.DynamicTaskDefinitionDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Date: 13-2-4 下午3:01
 * <p>Version: 1.0
 */
@Service
@Transactional
public class DynamicTaskDefinitionService extends BaseService<DynamicTaskDefinition, Long> {
    
    public DynamicTaskDefinition findByName(String name) {
        return ((DynamicTaskDefinitionDao) baseRepository).findByName(name);
    }
    
    public void save(TaskDefinition taskDefinition) {
        save((DynamicTaskDefinition) taskDefinition);
    }
    
    public void update(TaskDefinition taskDefinition) {
        update((DynamicTaskDefinition) taskDefinition);
    }
}
