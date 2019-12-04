package com.ppcxy.cyfm.dp.cloudconfig.repository.jpa;

import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.cyfm.dp.cloudconfig.entity.ConfigProperties;

public interface ConfigPropertiesDao extends BaseRepository<ConfigProperties, Long> {
    void deleteByApplicationAndProfile(String application, String profile);
}
