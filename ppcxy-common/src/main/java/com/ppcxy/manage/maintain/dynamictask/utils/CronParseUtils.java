package com.ppcxy.manage.maintain.dynamictask.utils;

import com.google.common.collect.Lists;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.spi.OperableTrigger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by weep on 2016-7-22.
 */
public class CronParseUtils {
    
    public static List<String> parse(String cronExpression) {
        return parse(cronExpression, 5);
    }
    
    public static List<String> parse(String cronExpression, int size) {
        
        String[] split = cronExpression.split(" ");
        
        
        CronTriggerImpl cronTrigger = new CronTriggerImpl();
        
        try {
            cronTrigger.setCronExpression(cronExpression);
        } catch (ParseException e) {
            return null;
        }
        
       
        Date now = Calendar.getInstance().getTime();
        
        List<Date> dates = computeFireTimesBetween(cronTrigger, now, 20);//这个是重点，一行代码搞定~~
        
        List<String> result = Lists.newArrayList();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < dates.size(); i++) {
            if (i == size) {//这个是提示的日期个数
                break;
            }
            result.add(dateFormat.format(dates.get(i)));
        }
        
        return result;
    }
    
    
    private static List<Date> computeFireTimesBetween(OperableTrigger trigg, Date from, Integer max) {
        org.quartz.Calendar cal = null;
        
        LinkedList<Date> lst = new LinkedList();
        OperableTrigger t = (OperableTrigger) trigg.clone();
        if (t.getNextFireTime() == null) {
            t.setStartTime(from);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 5);
            t.setEndTime(calendar.getTime());
            t.computeFirstFireTime(cal);
        }
        
        while (true) {
            Date d = t.getNextFireTime();
            if (d == null) {
                break;
            }
            
            if (d.before(from)) {
                t.triggered(cal);
            } else {
                lst.add(d);
                if (max != null && max > 0) {
                    if (lst.size() > max) {
                        break;
                    }
                }
                t.triggered(cal);
            }
        }
        
        return Collections.unmodifiableList(lst);
    }
}
