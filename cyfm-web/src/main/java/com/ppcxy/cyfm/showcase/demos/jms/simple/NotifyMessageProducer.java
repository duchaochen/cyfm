/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ppcxy.cyfm.showcase.demos.jms.simple;

import com.ppcxy.cyfm.sys.entity.user.User;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;

/**
 * JMS用户变更消息生产者.
 * <p>
 * 使用jmsTemplate将用户变更消息分别发送到queue与topic.
 *
 * @author calvin
 */

public class NotifyMessageProducer {
    
    private JmsTemplate jmsTemplate;
    private Destination notifyQueue;
    private Destination notifyTopic;
    
    public void sendQueue(final User user) {
        sendMessage(user, notifyQueue);
    }
    
    public void sendTopic(final User user) {
        sendMessage(user, notifyTopic);
    }
    
    /**
     * 使用jmsTemplate最简便的封装convertAndSend()发送Map类型的消息.
     */
    private void sendMessage(User user, Destination destination) {
        Map map = new HashMap();
        map.put("userName", user.getName());
        map.put("email", user.getEmail());
        
        jmsTemplate.convertAndSend(destination, map);
    }
    
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    
    public void setNotifyQueue(Destination notifyQueue) {
        this.notifyQueue = notifyQueue;
    }
    
    public void setNotifyTopic(Destination nodifyTopic) {
        this.notifyTopic = nodifyTopic;
    }
}
