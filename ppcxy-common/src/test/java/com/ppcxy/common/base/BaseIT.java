package com.ppcxy.common.base;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 */
@ContextConfiguration({"classpath:spring-common-test.xml"})
@Transactional(transactionManager = "transactionManager")
@Rollback
public abstract class BaseIT extends AbstractTransactionalJUnit4SpringContextTests {
    
    @PersistenceContext
    protected EntityManager entityManager;
    
    
    public void clear() {
        flush();
        entityManager.clear();
    }
    
    public void flush() {
        entityManager.flush();
    }
    
    
    protected String nextRandom() {
        return System.currentTimeMillis() + RandomStringUtils.randomNumeric(5);
    }
    
}
