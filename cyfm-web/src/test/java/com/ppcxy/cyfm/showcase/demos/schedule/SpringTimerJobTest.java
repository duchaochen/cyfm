/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ppcxy.cyfm.showcase.demos.schedule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.category.UnStable;
import org.springside.modules.test.log.LogbackListAppender;
import org.springside.modules.test.spring.SpringContextTestCase;
import org.springside.modules.utils.Threads;

import static org.assertj.core.api.Assertions.assertThat;

@Category(UnStable.class)
@DirtiesContext
@ContextConfiguration(locations = { "/applicationContext-test.xml","/schedule/applicationContext-spring-scheduler.xml" })
public class SpringTimerJobTest extends SpringContextTestCase {

	private static LogbackListAppender appender;

	@BeforeClass
	public static void initLogger() {
		// 加载测试用logger appender
		appender = new LogbackListAppender();
		appender.addToLogger(UserCountScanner.class.getName() + ".spring timer job by xml");
	}

	@AfterClass
	public static void removeLogger() {
		appender.removeFromLogger(UserCountScanner.class);
	}

	@Test
	public void scheduleJob() throws Exception {
		// 等待任务执行完毕
		Threads.sleep(2000);

		// 验证任务已执行   UserCountScanner
		assertThat(appender.getLogsCount()).isEqualTo(1);
		assertThat(appender.getFirstMessage()).isEqualTo("There are test user in database.");
	}
}
