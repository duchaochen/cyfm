package com.ppcxy.cyfm.bus.common;

import com.google.common.collect.Maps;
import org.springside.modules.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用http api返回数据适配器,根据返回数据类型自动甄别转换类型....
 */
public class HttpApiAdapter {
    
    public static Map<String, Object> adapter(String content) {
        Map<String, Object> results = Maps.newHashMap();
        
        try {
            if (content.charAt(0) == '{') {
                Map<String, Object> jsonData = new JsonMapper().fromJson(content, HashMap.class);
                
                if (jsonData != null) {
                    results.put("data", jsonData);
                    results.put("dataType", "json");
                }
                
            } else if (content.charAt(0) == '[') {
                
                List jsonArrayData = new JsonMapper().fromJson(content, ArrayList.class);
                if (jsonArrayData != null) {
                    results.put("data", jsonArrayData);
                    results.put("dataType", "jsonArray");
                }
                
            } else if (content.lastIndexOf("</html>") > 0) {
                results.put("data", content);
                results.put("dataType", "html");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (results.get("dataType") == null) {
            results.put("data", content);
            results.put("dataType", "text");
        }
        
        return results;
    }
    
    
    public static void main(String[] args) {
        
        Map<String, Object> loginParam = Maps.newHashMap();
        loginParam.put("username", "weep_x");
        loginParam.put("password", "123456");
        HttpClientUtils client = HttpClientUtils.newInstance("http://192.168.1.11:8888/login", loginParam);
        
        String s = client.get("http://192.168.1.11:8888/admin/polling?flush=true");
        
        Map<String, Object> adapter = HttpApiAdapter.adapter("{\"111\":\"11\"}");
        System.out.println(adapter.get("dataType"));
        System.out.println(adapter.get("data"));
    }
}
