/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ppcxy.cyfm.sys.repository.mybatis;

import com.ppcxy.cyfm.sys.entity.team.Team;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@ContextConfiguration(locations = {"/applicationContext.xml", "/applicationContext-cyfm.xml"})
public class TeamMybatisDaoTest extends SpringTransactionalTestCase {
    
    @Autowired
    private TeamMybatisDao teamDao;
    
    @Test
    public void getTeamWithDetail() throws Exception {
        Team team = teamDao.getWithDetail(1L);
        assertThat(team.getName()).isEqualTo("Dolphin");
        assertThat(team.getMaster().getName()).isEqualTo("管理员");
    }
}
