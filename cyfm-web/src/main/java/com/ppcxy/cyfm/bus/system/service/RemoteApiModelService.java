package com.ppcxy.cyfm.bus.system.service;

import com.ppcxy.common.service.BaseService;
import com.ppcxy.cyfm.bus.common.HttpClientUtils;
import com.ppcxy.cyfm.bus.system.entity.JoinSystem;
import com.ppcxy.cyfm.bus.system.entity.RemoteApiModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class RemoteApiModelService extends BaseService<RemoteApiModel, Long> {
    
    public String requestApi(RemoteApiModel remoteApiModel, Map<String, Object> parmas) {
        JoinSystem joinSystem = remoteApiModel.getJoinSystem();
        
        HttpClientUtils client = HttpClientUtils.newInstance(joinSystem.getLoginApi(), joinSystem.getLoginParams());
        
        if ("post".equals(remoteApiModel.getRequestMethod())) {
            return client.post(remoteApiModel.getRemoteApi(), parmas);
        }
        
        return client.get(genGetUrl(remoteApiModel.getRemoteApi(), parmas));
    }
    
    
    public String requestApi(Long remoteApiId, Map<String, Object> parmas) {
        return requestApi(findOne(remoteApiId), parmas);
    }
    
    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    private String genGetUrl(String url, Map<String, Object> map) {
        if (map == null) {
            return url;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        
        
        return String.format("%s?%s", url, s);
    }
    
}
