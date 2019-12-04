/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ppcxy.cyfm.sys.repository.jpa;

import com.google.common.collect.Lists;
import com.ppcxy.common.entity.search.SearchOperator;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.entity.search.filter.SearchFilter;
import com.ppcxy.common.entity.search.filter.SearchFilterHelper;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.repository.jpa.user.UserDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@ContextConfiguration(locations = {"/applicationContext.xml", "/applicationContext-cyfm.xml"})
public class SearchableTest extends SpringTransactionalTestCase {
    
    @Autowired
    private UserDao userDao;
    
    @Test
    public void fineUserByFilter() {
        //EQ
        SearchFilter filter = SearchFilterHelper.newCondition("name", SearchOperator.eq, "管理员");
        List<User> users = userDao
                .findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        assertThat(users).hasSize(1);
        
        // LIKE
        filter = SearchFilterHelper.newCondition("username", SearchOperator.like, "min");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        assertThat(users).hasSize(1);
        
        // GT
        filter = SearchFilterHelper.newCondition("id", SearchOperator.gt, "1");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        assertThat(users).hasSize(5);
        
        filter = SearchFilterHelper.newCondition("id", SearchOperator.gt, "6");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        assertThat(users).isEmpty();
        
        // GTE
        filter = SearchFilterHelper.newCondition("id", SearchOperator.gte, "1");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        assertThat(users).hasSize(6);
        
        filter = SearchFilterHelper.newCondition("id", SearchOperator.gte, "6");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        assertThat(users).hasSize(1);
        
        // LT
        filter = SearchFilterHelper.newCondition("id", SearchOperator.lt, "6");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        assertThat(users).hasSize(5);
        
        filter = SearchFilterHelper.newCondition("id", SearchOperator.lt, "1");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        assertThat(users).isEmpty();
        
        // LTE
        filter = SearchFilterHelper.newCondition("id", SearchOperator.lte, "6");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        assertThat(users).hasSize(6);
        
        filter = SearchFilterHelper.newCondition("id", SearchOperator.lte, "1");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        assertThat(users).hasSize(1);
        
        // Empty filters, select all
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(new ArrayList<SearchFilter>())).getContent();
        assertThat(users).hasSize(6);
        
        users = userDao.findAll(Searchable.newSearchable(null)).getContent();
        assertThat(users).hasSize(6);
        
        // Nest Attribute
        filter = SearchFilterHelper.newCondition("team.id", SearchOperator.eq, "1");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        assertThat(users).hasSize(6);
        
        // AND 2 Conditions
        filter = SearchFilterHelper.newCondition("team.id", SearchOperator.eq, "10");
        SearchFilter filter1 = SearchFilterHelper.newCondition("name", SearchOperator.eq, "管理员");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter))).getContent();
        SearchFilter filter2 = SearchFilterHelper.newCondition("username", SearchOperator.like, "min");
        assertThat(users).isEmpty();
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter1, filter2))).getContent();
        assertThat(users).hasSize(1);
        
        filter1 = SearchFilterHelper.newCondition("name", SearchOperator.eq, "管理员");
        filter2 = SearchFilterHelper.newCondition("username", SearchOperator.like, "user");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter1, filter2))).getContent();
        assertThat(users).isEmpty();
        
        // 2 conditions on same field
        filter1 = SearchFilterHelper.newCondition("id", SearchOperator.gte, "1");
        filter2 = SearchFilterHelper.newCondition("id", SearchOperator.lte, "6");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilters(Lists.newArrayList(filter1, filter2))).getContent();
        assertThat(users).hasSize(6);
        
        
        // OR 2 Conditions
        filter = SearchFilterHelper.newCondition("team.id", SearchOperator.eq, "1");
        filter1 = SearchFilterHelper.newCondition("id", SearchOperator.eq, 1);
        filter2 = SearchFilterHelper.newCondition("id", SearchOperator.eq, 2);
        users = userDao.findAll(Searchable.newSearchable().addSearchFilter(filter).or(filter1, filter2)).getContent();
        assertThat(users).hasSize(2);
        
        filter = SearchFilterHelper.newCondition("team.id", SearchOperator.eq, "99");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilter(filter).or(filter1, filter2)).getContent();
        assertThat(users).isEmpty();
        
        // OR 2 Conditions
        filter = SearchFilterHelper.newCondition("team.id", SearchOperator.eq, "1");
        filter1 = SearchFilterHelper.newCondition("username", SearchOperator.like, "min");
        filter2 = SearchFilterHelper.newCondition("id", SearchOperator.eq, 2);
        users = userDao.findAll(Searchable.newSearchable().addSearchFilter(filter).or(filter1, filter2)).getContent();
        assertThat(users).hasSize(2);
        
        filter = SearchFilterHelper.newCondition("team.id", SearchOperator.eq, "99");
        users = userDao.findAll(Searchable.newSearchable().addSearchFilter(filter).or(filter1, filter2)).getContent();
        assertThat(users).isEmpty();
    }
}
