package com.ppcxy.cyfm.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * <p>Date: 14-1-18
 * <p>Version: 1.0
 */
public class TestTask2 {

    @Autowired
    private ApplicationContext ctx;

    public void run() {
        System.out.println("====hello test task2::" + ctx.getDisplayName());
    }
}
