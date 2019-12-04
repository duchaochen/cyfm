/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ppcxy.cyfm.showcase.demos.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 被Spring各种Scheduler反射调用的Service POJO.
 *
 * @author Calvin
 */
@Component
public class UserCountScanner {
    
    public void executeByJdk() {
        execute("jdk timer job");
    }
    
    public void executeBySpringCronByJava() {
        execute("spring cron job by java");
    }
    
    // 被Spring的Scheduler namespace 反射构造成ScheduledMethodRunnable
    public void executeBySpringCronByXml() {
        execute("spring cron job by xml");
    }
    
    // 被Spring的Scheduler namespace 反射构造成ScheduledMethodRunnable
    public void executeBySpringTimerByXml() {
        execute("spring timer job by xml");
    }
    
    /**
     * 定时打印当前用户数到日志.
     */
    private void execute(String by) {
        Logger logger = LoggerFactory.getLogger(UserCountScanner.class.getName() + "." + by);
       
        logger.info("There are {} user in database.", "test");
    }
}
