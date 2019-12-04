package com.ppcxy.cyfm.manage.service.maintaion.dynamictask;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ppcxy.common.task.TaskApi;
import com.ppcxy.cyfm.manage.entity.maintaion.dynamictask.DynamicTaskDefinition;
import com.ppcxy.common.task.TaskDefinition;
import com.ppcxy.manage.maintain.dynamictask.exception.DynamicTaskException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MethodInvoker;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * <p>Date: 14-1-17
 * <p>Version: 1.0
 */
@Service
@Transactional
public class DynamicTaskApiImpl implements TaskApi {
    
    private final Logger logger = LoggerFactory.getLogger(DynamicTaskApiImpl.class);
    
    private Map<Long, ScheduledFuture<?>> taskMap = Maps.newConcurrentMap();
    
    
    @Autowired
    private DynamicTaskDefinitionService taskDefinitionService;
    
    @Autowired
    private TaskScheduler taskScheduler;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @PostConstruct
    public void initTask() { //系统启动后，自动加载任务
        List<Long> ids = Lists.newArrayList();
        for (DynamicTaskDefinition td : taskDefinitionService.findAll()) {
            if (Boolean.TRUE.equals(td.getStart())) {
                ids.add(td.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(ids)) {
            startTask(true, ids.toArray(new Long[0]));
        }
    }
    
    @Override
    public void addTaskDefinition(TaskDefinition taskDefinition) {
        taskDefinitionService.save(taskDefinition);
    }
    
    @Override
    public void updateTaskDefinition(TaskDefinition taskDefinition) {
        taskDefinitionService.update(taskDefinition);
        stopTask(false, taskDefinition.getId());
        startTask(taskDefinition.getId());
    }
    
    @Override
    public void deleteTaskDefinition(boolean forceTermination, Long... taskDefinitionIds) {
        stopTask(forceTermination, taskDefinitionIds);
        taskDefinitionService.delete(taskDefinitionIds);
    }
    
    private synchronized void startTask(boolean forceStart, Long... taskDefinitionIds) {
        for (Long taskDefinitionId : taskDefinitionIds) {
            DynamicTaskDefinition td = taskDefinitionService.findOne(taskDefinitionId);
            if (td == null || (!forceStart && Boolean.TRUE.equals(td.getStart()))) {
                return;
            }
            
            try {
                ScheduledFuture<?> future = taskScheduler.schedule(createTask(td), new CronTrigger(td.getCron()));
                taskMap.put(taskDefinitionId, future);
                td.setStart(Boolean.TRUE);
                td.setDescription("");
            } catch (Exception e) {
                logger.error("start task error, task id:" + taskDefinitionId, e);
                td.setDescription(e.getMessage());
            }
            taskDefinitionService.update(td);
        }
    }
    
    @Override
    public synchronized void startTask(Long... taskDefinitionIds) {
        startTask(false, taskDefinitionIds);
    }
    
    
    @Override
    public synchronized void stopTask(boolean forceTermination, Long... taskDefinitionIds) {
        for (Long taskDefinitionId : taskDefinitionIds) {
            DynamicTaskDefinition td = taskDefinitionService.findOne(taskDefinitionId);
            
            if (td == null || Boolean.FALSE.equals(td.getStart())) {
                return;
            }
            
            try {
                ScheduledFuture<?> future = taskMap.get(taskDefinitionId);
                if (future != null) {
                    future.cancel(forceTermination);
                }
                td.setStart(Boolean.FALSE);
            } catch (Exception e) {
                logger.error("stop task error, task id:" + taskDefinitionId, e);
                td.setDescription(e.getMessage());
            }
            taskDefinitionService.update(td);
        }
        
    }
    
    
    private Runnable createTask(TaskDefinition td) {
        DynamicTaskDefinition dynamicTask = (DynamicTaskDefinition) td;
        final MethodInvoker methodInvoker = new MethodInvoker();
        final Long taskId = dynamicTask.getId();
        try {
            methodInvoker.setTargetMethod(dynamicTask.getMethodName());
            Object bean = null;
            if (StringUtils.isNotEmpty(dynamicTask.getBeanName())) {
                bean = applicationContext.getBean(dynamicTask.getBeanName());
            } else {
                bean = applicationContext.getAutowireCapableBeanFactory().createBean(Class.forName(dynamicTask.getBeanClass()));
            }
            methodInvoker.setTargetObject(bean);
            methodInvoker.prepare();
        } catch (Exception e) {
            throw new DynamicTaskException("create task runnable error, task id is : " + taskId, e);
        }
        return new Runnable() {
            @Override
            public void run() {
                try {
                    methodInvoker.invoke();
                } catch (Exception e) {
                    logger.error("run dynamic task error, task id is:" + taskId, e);
                    throw new DynamicTaskException("run dynamic task error, task id is:" + taskId, e);
                }
            }
        };
    }
    
    
}
