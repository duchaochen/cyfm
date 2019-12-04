package com.ppcxy.cyfm.manage.repository.jpa.maintaion.dynamictask;

import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.cyfm.manage.entity.maintaion.dynamictask.DynamicTaskDefinition;

/**
 * <p>Date: 13-2-4 下午3:00
 * <p>Version: 1.0
 */
public interface DynamicTaskDefinitionDao extends BaseRepository<DynamicTaskDefinition, Long> {

    DynamicTaskDefinition findByName(String name);
}
