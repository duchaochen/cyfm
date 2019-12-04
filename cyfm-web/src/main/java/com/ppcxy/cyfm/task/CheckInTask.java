package com.ppcxy.cyfm.task;

import com.google.common.collect.Maps;
import com.ppcxy.common.spring.SpringContextHolder;
import com.ppcxy.common.utils.LinkedProperties;
import com.ppcxy.cyfm.bus.common.HttpClientUtils;
import com.ppcxy.manage.maintain.notification.support.NotificationApi;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springside.modules.mapper.JsonMapper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("checkInTask")
public class CheckInTask {
    Timer timer = new Timer();//实例化Timer类
    
    
    private String lastSessionToken = null;
    private String lastJSESSIONID = null;
    
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
    
    
    HttpClientUtils clientUtils;
    
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
        
        clientUtils = HttpClientUtils.newInstance();
        
        clientUtils.addCookie("sessionToken", prop.getProperty("CHECKIN.SESSIONTOKEN"), "qy.do1.com.cn", "/wxqyh");
        clientUtils.addCookie("JSESSIONID", prop.getProperty("CHECKIN.JSESSIONID"), "qy.do1.com.cn", "/wxqyh");
        //clientUtils.get("https://qy.do1.com.cn/wxqyh/portal/weixin/weixinclientAction!handleOauthCode.action?pre=https%253A%252F%252Fqy.do1.com.cn%252Fwxqyh%252Fjsp%252Fwap%252Fcheckwork%252Fadd.jsp%253Fid%253D5763CC61-9939-43A7-9485-B1B0CC0A8AD6%2526corp_id%253Dwx77ea70808725d904%2526agentCode%253Dcheckwork&sessionId=&corpId=wx77ea70808725d904&ip=&agentCode=checkwork&code=TfIaRvXrsjPDe1_TPsIwjrSHkiatGKae6nFt1DLgdcw&state=ok");
        
    }
    
    
    public void execute() throws Exception {
        Date today = new Date();
        
        Date nowTime = new SimpleDateFormat(TIME_FORMAT).parse(new SimpleDateFormat(TIME_FORMAT).format(today));
        
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        
        if (weekday == 1 || weekday == 7) {
            heartBeat();
            return;
        }
        
        if (!isSignIn && isEffectiveDate(nowTime, SIGN_IN_STARTTIME, SIGN_IN_ENDTIME)) {
            //签到从8:25分开始扫描 延迟0-5分钟内执行
            timer.schedule(new TimerTask() {
                public void run() {
                    sign(SIGN_IN_ID);
                    this.cancel();
                }
            }, new Random().nextInt(300000));
            
            isSignIn = true;
        } else if (isEffectiveDate(nowTime, SIGN_OUT_STARTTIME, SIGN_OUT_ENDTIME)) {
            timer.schedule(new TimerTask() {
                public void run() {
                    sign(SIGN_OUT_ID);
                    this.cancel();
                }
            }, new Random().nextInt(120000));
            isSignIn = false;
        } else {
            heartBeat();
        }
        
    }
    
    private void tokenRefresh() {
        String s = clientUtils.get("https://qy.do1.com.cn/wxqyh/jsp/wap/checkwork/calendarDetail.jsp?agentCode=checkwork&corp_id=wx77ea70808725d904&stoken=9735daf425ef450da86691efbb4c545c");
        System.out.println("============");
        try {
            System.out.println("token 刷新-" + lastSessionToken);
            //System.out.println(s);
        } catch (Exception e) {
            System.err.println("token 刷新失败...");
        } finally {
            lastSessionToken = this.clientUtils.checkCookie("sessionToken");
            lastJSESSIONID = this.clientUtils.checkCookie("JSESSIONID");
            
            prop.setProperty("CHECKIN.SESSIONTOKEN", lastSessionToken);
            prop.setProperty("CHECKIN.JSESSIONID", lastJSESSIONID);
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
        System.out.println("============");
    }
    
    private void heartBeat() {
        tokenRefresh();
        
        String s = clientUtils.get("https://qy.do1.com.cn/wxqyh/portal/vipPortalAction!wxqyhConfig.action?dqdp_csrf_token=d76f42f99e3b3403");
        System.out.println("============");
        try {
            System.out.print("心跳检测-");
            System.out.println((String.format("心跳回馈,当前用户 [%s].", ((Map<String, Object>) JsonMapper.nonDefaultMapper().fromJson(s, HashMap.class).get("data")).get("personName"))));
        } catch (Exception e) {
            NotificationApi bean = SpringContextHolder.getBean(NotificationApi.class);
            Map<String, Object> map = new HashMap<>();
            
            String type = null;
            
            
            map.put("title", "心跳检测失败,请补充打卡凭证");
            map.put("message", String.format("心跳检测失败,当前sessionId[%s],当前sessionToken[%s]", lastJSESSIONID, lastSessionToken));
            bean.notify(1l, "commonTemplate", map);
            
            System.err.println("检测失败,请补充打卡凭证");
        }
        System.out.println("============");
    }
    
    private static final String[] addresss = {"黑龙江省哈尔滨市道里区太行路", "黑龙江省哈尔滨市道里区太湖北街15号", "黑龙江省哈尔滨市道里区洪湖街"};
    private static final String[] longitudes = {"126.53176983486148", "126.53177743309622", "126.52714475364802"};
    private static final String[] latitude = {"45.70509498678491", "45.705018642255844", "45.711111531652335"};
    
    private void sign(String signId) {
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
        map.put("title", String.format("打卡%s通知[]", type));
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
        //CheckInTask checkInTask = new CheckInTask();
        //while (true) {
        //
        //    Thread.sleep(6000);
        //    checkInTask.heartBeat();
        //}
        
        
        //Thread.sleep(10000);
        //checkInTask.heartBeat();
        //Thread.sleep(10000);
        //checkInTask.heartBeat();
        //Thread.sleep(10000);
        //checkInTask.heartBeat();
        //Thread.sleep(10000);
        //checkInTask.heartBeat();
        //Thread.sleep(10000);
        //checkInTask.heartBeat();
        //Thread.sleep(10000);
        //checkInTask.heartBeat();
        //Thread.sleep(10000);
        //checkInTask.sign(SIGN_OUT_ID);
        //Date nowTime = new SimpleDateFormat(TIME_FORMAT).parse("08:25:00");
        //System.out.println(nowTime.getTime());
        //
        //Timer timer = new Timer();
        //
        //while (true) {
        //    Thread.sleep(1000);
        //    int i = new Random().nextInt(3000);
        //    System.out.println(i+"毫秒后执行输出");
        //    timer.schedule(new TimerTask() {
        //        public void run() {
        //            System.out.println("输出"+i);
        //            this.cancel();
        //        }
        //    }, new Random().nextInt(3000));
        //}
    }
}
