package com.ppcxy.cyfm.task;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ppcxy.common.spring.SpringContextHolder;
import com.ppcxy.common.utils.LinkedProperties;
import com.ppcxy.cyfm.bus.common.HttpClientUtils;
import com.ppcxy.manage.maintain.notification.support.NotificationApi;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.ResourceUtils;
import org.springside.modules.mapper.JsonMapper;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("checkInTaskMult")
public class CheckInTaskMult {
    private static Logger logger = LoggerFactory.getLogger(CheckInTaskMult.class);
    
    Timer timer = new Timer();//实例化Timer类
    
    public static final String TIME_FORMAT = "HH:mm:ss";
    private Boolean isSignIn = false;
    
    public static final String SIGN_IN_ID = "F9BB709F-CEBE-40A5-88C6-16ABBAF080C7";
    public static final String SIGN_OUT_ID = "458FEBFD-ECFD-419D-B875-4402F757AF6C";
    
    //08:15 900000
    //08:20 1200000
    //08:25 1500000
    //08:30 1800000
    public static final Date SIGN_IN_STARTTIME = new Date(1500000);
    public static final Date SIGN_IN_ENDTIME = new Date(1800000);
    
    //17:00 32400000
    public static final Date SIGN_OUT_STARTTIME = new Date(32400000);
    //17:06 32700000
    public static final Date SIGN_OUT_ENDTIME = new Date(32700000);
    
    
    List<HttpClientUtils> clientUtilsList = Lists.newArrayList();
    
    LinkedProperties prop;
    File propFile;
    
    {
        
        prop = new LinkedProperties();//属性集合对象
        
        FileInputStream fis = null;//属性文件流
        
        try {
            propFile = ResourceUtils.getFile("classpath:dp/taskConfig.properties");
            
            fis = new FileInputStream(propFile);
            prop.load(fis);//将属性文件流装载到Properties对象中
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for (int i = 0; i < prop.size() / 2; i++) {
            HttpClientUtils client = HttpClientUtils.newInstance();
            
            client.addCookie("sessionToken", prop.getProperty("CHECKIN.SESSIONTOKEN." + i), "qy.do1.com.cn", "/wxqyh");
            client.addCookie("JSESSIONID", prop.getProperty("CHECKIN.JSESSIONID." + i), "qy.do1.com.cn", "/wxqyh");
            clientUtilsList.add(client);
        }
        
        //clientUtils.get("https://qy.do1.com.cn/wxqyh/portal/weixin/weixinclientAction!handleOauthCode.action?pre=https%253A%252F%252Fqy.do1.com.cn%252Fwxqyh%252Fjsp%252Fwap%252Fcheckwork%252Fadd.jsp%253Fid%253D5763CC61-9939-43A7-9485-B1B0CC0A8AD6%2526corp_id%253Dwx77ea70808725d904%2526agentCode%253Dcheckwork&sessionId=&corpId=wx77ea70808725d904&ip=&agentCode=checkwork&code=TfIaRvXrsjPDe1_TPsIwjrSHkiatGKae6nFt1DLgdcw&state=ok");
        
    }
    
    
    public void execute() throws Exception {
        Date today = new Date();
        
        Date nowTime = new SimpleDateFormat(TIME_FORMAT).parse(new SimpleDateFormat(TIME_FORMAT).format(today));
        
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        
        if (weekday == 1 || weekday == 7) {
            for (HttpClientUtils clientUtils : clientUtilsList) {
                heartBeat(clientUtils);
            }
            return;
        }
        
        
        if (!isSignIn && isEffectiveDate(nowTime, SIGN_IN_STARTTIME, SIGN_IN_ENDTIME)) {
            for (HttpClientUtils clientUtils : clientUtilsList) {
                //签到从8:25分开始扫描 延迟0-2分钟内执行
                timer.schedule(new TimerTask() {
                    public void run() {
                        sign(SIGN_IN_ID, clientUtils);
                        this.cancel();
                    }
                }, new Random().nextInt(120000));
            }
            
            isSignIn = true;
        } else if (isEffectiveDate(nowTime, SIGN_OUT_STARTTIME, SIGN_OUT_ENDTIME)) {
            for (HttpClientUtils clientUtils : clientUtilsList) {
                //签到从8:25分开始扫描 延迟0-2分钟内执行
                timer.schedule(new TimerTask() {
                    public void run() {
                        sign(SIGN_OUT_ID, clientUtils);
                        this.cancel();
                    }
                }, new Random().nextInt(120000));
            }
            
            isSignIn = false;
        } else {
            for (HttpClientUtils clientUtils : clientUtilsList) {
                heartBeat(clientUtils);
            }
        }
    }
    
    private void tokenRefresh(HttpClientUtils clientUtils, int index) {
        
        
        String s = clientUtils.get("https://qy.do1.com.cn/wxqyh/jsp/wap/checkwork/calendarDetail.jsp?agentCode=checkwork&corp_id=wx77ea70808725d904&stoken=9735daf425ef450da86691efbb4c545c");
        
        if (s.indexOf("抱歉，出错了") > 0) {
            FileInputStream fis = null;//属性文件流
            
            try {
                propFile = ResourceUtils.getFile("classpath:dp/taskConfig.properties");
                
                fis = new FileInputStream(propFile);
                prop.load(fis);//将属性文件流装载到Properties对象中
                
            } catch (Exception e) {
                ///加载失败无所谓
            }
            
            logger.warn("刷新信息凭证无效,重新加载凭证...");
            clientUtils.addCookie("sessionToken", prop.getProperty("CHECKIN.SESSIONTOKEN." + index), "qy.do1.com.cn", "/wxqyh");
            clientUtils.addCookie("JSESSIONID", prop.getProperty("CHECKIN.JSESSIONID." + index), "qy.do1.com.cn", "/wxqyh");
            logger.debug("从配置文件加载凭证信息: sessionToken [{}] ,JSESSIONID [{}]", prop.getProperty("CHECKIN.SESSIONTOKEN." + index), prop.getProperty("CHECKIN.JSESSIONID." + index));
        }
        
        try {
            logger.debug(String.format("用户[%d] token 信息刷新-%s", index, clientUtils.checkCookie("sessionToken")));
            logger.debug(String.format("用户[%d] sessionid 信息刷新-%s", index, clientUtils.checkCookie("JSESSIONID")));
            //System.out.println(s);
        } catch (Exception e) {
            logger.warn("token 刷新失败...");
        } finally {
            
            prop.setProperty("CHECKIN.SESSIONTOKEN." + index, clientUtils.checkCookie("sessionToken"));
            prop.setProperty("CHECKIN.JSESSIONID." + index, clientUtils.checkCookie("JSESSIONID"));
            FileOutputStream fos = null;
            
            try {
                fos = new FileOutputStream(propFile);
                // 将Properties集合保存到流中
                prop.store(fos, new Date().toString() + " update checkIn Task sessionToken.");
            } catch (Exception e) {
            
            } finally {
                try {
                    fos.close();// 关闭流
                } catch (IOException e) {
                }
            }
            
        }
    }
    
    private void heartBeat(HttpClientUtils clientUtils) {
        Boolean isFail = false;
        int index = clientUtilsList.indexOf(clientUtils);
        
        tokenRefresh(clientUtils, index);
        
        String s = clientUtils.get("https://qy.do1.com.cn/wxqyh/portal/vipPortalAction!wxqyhConfig.action?dqdp_csrf_token=d76f42f99e3b3403");
        
        
        try {
            
            if (s.indexOf("<html>") < 0) {
                logger.debug(String.format("心跳检测-回馈,当前用户 [%s].", ((Map<String, Object>) JsonMapper.nonDefaultMapper().fromJson(s, HashMap.class).get("data")).get("personName")));
                
            } else {
                isFail = true;
                logger.error(String.format("用户[%d]心跳检测-失败,请补充打卡凭证", index));
            }
            
        } catch (Exception e) {
            isFail = true;
            logger.error("检测失败,请查看异常日志", e);
            e.printStackTrace();
        } finally {
            try {
                if (isFail) {
                    NotificationApi bean = SpringContextHolder.getBean(NotificationApi.class);
                    Map<String, Object> map = new HashMap<>();
                    
                    map.put("title", String.format("用户[%d]心跳失败,请补充凭证[%s]", index, DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date())));
                    map.put("message", String.format("心跳检测失败,当前sessionId[%s],当前sessionToken[%s]", clientUtils.checkCookie("JSESSIONID"), clientUtils.checkCookie("sessionToken")));
                    bean.notify(1l, "commonTemplate", map);
                    
                    
                }
                
            } catch (Exception e1) {
            
            }
        }
        
    }
    
    private static final String[] addresss = {"黑龙江省哈尔滨市道里区太行路", "黑龙江省哈尔滨市道里区太湖北街15号", "黑龙江省哈尔滨市道里区洪湖街"};
    private static final String[] longitudes = {"126.53176983486148", "126.53177743309622", "126.52714475364802"};
    private static final String[] latitude = {"45.70509498678491", "45.705018642255844", "45.711111531652335"};
    
    private void sign(String signId, HttpClientUtils clientUtils) {
        Map<String, Object> params = Maps.newHashMap();
        int l = (int) (new Date().getTime() % 3);
        
        params.put("address", addresss[l]);
        params.put("isWorkDate", 0);
        params.put("longitude", longitudes[l]);
        params.put("latitude", latitude[l]);
        params.put("id", signId);
        params.put("isgps", 0);
        params.put("ruleId", "5763CC61-9939-43A7-9485-B1B0CC0A8AD6");
        params.put("signDate", "");
        params.put("againsignin", 1);
        
        String s = clientUtils.post("https://qy.do1.com.cn/wxqyh/portal/checkworkAction!addsignin.action?dqdp_csrf_token=", params);
        
        NotificationApi bean = SpringContextHolder.getBean(NotificationApi.class);
        Map<String, Object> map = new HashMap<>();
        
        String type = null;
        
        switch (signId) {
            case SIGN_IN_ID:
                type = "签到";
                break;
            case SIGN_OUT_ID:
                type = "签退";
                break;
        }
        
        map.put("title", String.format("打卡%s通知[%s]", type, DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date())));
        map.put("message", String.format("打卡返回信息: %s", s));
        bean.notify(1l, "commonTemplate", map);
    }
    
    
    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author jqlin
     */
    private static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        
        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);
        
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void main(String[] args) throws Exception {
        CheckInTaskMult checkInTask = new CheckInTaskMult();
        
        Thread.sleep(1000);
        checkInTask.execute();
        checkInTask.timer.cancel();
        
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecretKey deskey = keygen.generateKey();
        System.out.println(Base64Utils.encodeToString(deskey.getEncoded()));
    }
}
