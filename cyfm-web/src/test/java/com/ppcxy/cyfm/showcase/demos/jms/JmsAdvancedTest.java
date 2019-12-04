/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ppcxy.cyfm.showcase.demos.jms;

import com.ppcxy.cyfm.showcase.demos.jms.advanced.AdvancedNotifyMessageListener;
import com.ppcxy.cyfm.showcase.demos.jms.advanced.AdvancedNotifyMessageProducer;
import com.ppcxy.cyfm.sys.entity.user.User;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.category.UnStable;
import org.springside.modules.test.log.LogbackListAppender;
import org.springside.modules.test.spring.SpringContextTestCase;
import org.springside.modules.utils.Threads;

import javax.annotation.Resource;
import javax.jms.*;

import static org.assertj.core.api.Assertions.assertThat;

@Category(UnStable.class)
@DirtiesContext
@ContextConfiguration(locations = { "/applicationContext-test.xml", "/jms/applicationContext-jms-advanced.xml" })
public class JmsAdvancedTest extends SpringContextTestCase {

	@Autowired
	private AdvancedNotifyMessageProducer notifyMessageProducer;

	@Resource
	private JmsTemplate advancedJmsTemplate;

	@Resource
	private Destination advancedNotifyTopic;

	@Test
	public void queueMessage() {
		Threads.sleep(1000);
		LogbackListAppender appender = new LogbackListAppender();
		appender.addToLogger(AdvancedNotifyMessageListener.class);

		User user = new User();
		user.setName("calvin");
		user.setEmail("calvin@sringside.org.cn");

		notifyMessageProducer.sendQueue(user);
		Threads.sleep(1000);
		assertThat(appender.getFirstMessage()).isEqualTo(
				"UserName:calvin, Email:calvin@sringside.org.cn, ObjectType:user");
	}

	@Test
	public void topicMessage() {
		Threads.sleep(1000);
		LogbackListAppender appender = new LogbackListAppender();
		appender.addToLogger(AdvancedNotifyMessageListener.class);

		User user = new User();
		user.setName("calvin");
		user.setEmail("calvin@sringside.org.cn");

		notifyMessageProducer.sendTopic(user);
		Threads.sleep(1000);
		assertThat(appender.getFirstMessage()).isEqualTo(
				"UserName:calvin, Email:calvin@sringside.org.cn, ObjectType:user");
	}

	@Test
	public void topicMessageWithWrongType() {
		Threads.sleep(1000);
		LogbackListAppender appender = new LogbackListAppender();
		appender.addToLogger(AdvancedNotifyMessageListener.class);

		advancedJmsTemplate.send(advancedNotifyTopic, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {

				MapMessage message = session.createMapMessage();
				message.setStringProperty("objectType", "role");
				return message;
			}
		});

		Threads.sleep(1000);
		assertThat(appender.isEmpty()).isTrue();
	}
}
