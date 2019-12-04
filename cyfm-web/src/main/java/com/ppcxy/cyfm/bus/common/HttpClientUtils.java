package com.ppcxy.cyfm.bus.common;

import com.google.common.collect.Maps;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springside.modules.mapper.JsonMapper;

import java.io.IOException;
import java.util.*;

public class HttpClientUtils {
    
    private static final Map<String, HttpClientUtils> CLIENT_CACHE = Maps.newHashMap();
    
    private CloseableHttpClient client = null;
    public HttpClientContext context = null;
    public CookieStore cookieStore = null;
    public RequestConfig requestConfig = null;
    public Long beginTimestamp;
    public Long timeout = 1000 * 60 * 10l;
    
    public HttpClientUtils() {
        context = HttpClientContext.create();
        
        client = HttpClients.createDefault();
        cookieStore = new BasicCookieStore();
        // 配置超时时间（连接服务端超时120秒，请求数据返回超时60秒）
        requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
                .setConnectionRequestTimeout(60000).build();
        // 设置默认跳转以及存储cookie
        client = HttpClientBuilder.create().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore).build();
    }
    
    /**
     * 返回登录后的client
     *
     * @param loginUrl    登录地址
     * @param loginParams json loginParams
     * @return not null 即初始化成功,反之失败
     */
    public static HttpClientUtils newInstance(String loginUrl, Map<String, Object> loginParams) {
        String clientCacheKey = String.format("%s%s", loginUrl, JsonMapper.nonDefaultMapper().toJson(loginParams));
        
        //如果存在缓存的client , 且未关闭(关闭或过期)状态,则直接返回
        HttpClientUtils cacheClient = CLIENT_CACHE.get(clientCacheKey);
        if (cacheClient != null && !cacheClient.isClose()) {
            //return cacheClient;
        }
        //没有满足的缓存client,则创建新的client,并进行授权
        HttpClientUtils clientUtils = new HttpClientUtils();
        //记录时间戳
        clientUtils.beginTimestamp = System.currentTimeMillis();
        
        CLIENT_CACHE.put(clientCacheKey, clientUtils);
        return clientUtils.login(loginUrl, loginParams);
    }
    
    /**
     * 登录系统
     *
     * @param loginUrl
     * @return
     */
    private HttpClientUtils login(String loginUrl, Map<String, Object> loginParmas) {
        
        try {
            post(loginUrl, loginParmas);
            
        } catch (Exception e) {
            return null;
        }
        return this;
    }
    
    
    /**
     * 手动增加cookie
     *
     * @param name
     * @param value
     * @param domain
     * @param path
     */
    public void addCookie(String name, String value, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookieStore.addCookie(cookie);
    }
    
    /**
     * 把当前cookie从控制台输出出来
     */
    public void getCookies() {
        cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
        }
    }
    
    private boolean isClose() {
        boolean isTimeout = System.currentTimeMillis() - this.beginTimestamp > timeout;
        if (isTimeout) {
            try {
                client.close();
            } catch (IOException e) {
            
            }
        }
        return isTimeout;
    }
    
    /**
     * 检查cookie的键值是否包含传参
     *
     * @param key
     * @return
     */
    public String checkCookie(String key) {
        cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }
    
    /**
     * 自己处理请求
     *
     * @return
     */
    public static HttpClientUtils newInstance() {
        return new HttpClientUtils();
    }
    
    private static void printResponse(HttpResponse httpResponse)
            throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        System.out.println("status:" + httpResponse.getStatusLine());
        System.out.println("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            System.out.println("\t" + iterator.next());
        }
        // 判断响应实体是否为空
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            System.out.println("response length:" + responseString.length());
            System.out.println("response content:"
                    + responseString.replace("\r\n", ""));
        }
    }
    
    private static List<NameValuePair> genParam(Map<String, Object> parameterMap) {
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        if (parameterMap != null) {
            Iterator it = parameterMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> parmEntry = (Map.Entry<String, Object>) it.next();
                param.add(new BasicNameValuePair(parmEntry.getKey(),
                        parmEntry.getValue().toString()));
            }
        }
        return param;
    }
    
    
    /**
     * get请求,并返回请求结果
     *
     * @param getUrl
     * @return
     * @throws Exception
     */
    public String get(String getUrl) {
        return get(getUrl, null);
    }
    
    /**
     * get请求,并返回请求结果
     *
     * @param getUrl
     * @return
     * @throws Exception
     */
    public String get(String getUrl, String contentType) {
        
        // 执行get请求
        HttpGet httpGet = new HttpGet(getUrl);
        initHeader(httpGet, contentType);
        try {
            HttpResponse httpResponse = client.execute(httpGet, context);
            //Header[] headers = httpResponse.getHeaders("Set-Cookie");
            //System.out.println(Arrays.asList(headers));
            
            return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    
    public String post(String postUrl, Map<String, Object> parmas) {
        return post(postUrl, null, parmas);
    }
    
    public String post(String postUrl, String contentType, Map<String, Object> parmas) {
        HttpPost httpPost = new HttpPost(postUrl);
        initHeader(httpPost, contentType);
        
        HttpResponse httpResponse = null;
        try {
            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(
                    genParam(parmas), "UTF-8");
            httpPost.setEntity(postEntity);
            
            // 执行post请求登录
            httpResponse = client.execute(httpPost, context);
            //System.out.println(httpResponse.getFirstHeader("Content-Type"));
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    /**
     * 初始化请求头信息
     *
     * @param httpRequestBase
     * @param contentType
     */
    public void initHeader(HttpRequestBase httpRequestBase, String contentType) {
        if (contentType != null) {
            httpRequestBase.setHeader("Content-Type", contentType);
        }
        httpRequestBase.setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E302 MicroMessenger/6.6.7 NetType/4G Language/zh_CN");
        //httpRequestBase.setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E302 MicroMessenger/6.6.7 NetType/4G Language/zh_CN");
        httpRequestBase.setHeader("Accept", "application/json, text/html, text/javascript, application/xhtml+xml, application/xml, image/webp, image/apng, */*;q=0.01");
    }
    
    private void close() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
        }
    }
    
    public static void main(String[] args) {
        HttpClientUtils clientUtils = new HttpClientUtils();
        Map<String, Object> loginParam = Maps.newHashMap();
        loginParam.put("username", "weep_x");
        loginParam.put("password", "123456");
        
        //clientUtils.login("http://192.168.1.19:8888/login", loginParam);
        //Cookie: sessionToken=bea67089d7584c9db631ee8a01eb7ed6; JSESSIONID=505A59BB0103239E9D7966970D0964BB.2297; tgw_l7_route=d040be7ea1ecba2e0ebd5581c86e0052
        clientUtils.addCookie("sessionToken", "bea67089d7584c9db631ee8a01eb7ed6", "qy.do1.com.cn", "/");
        clientUtils.addCookie("JSESSIONID", "505A59BB0103239E9D7966970D0964BB.2297", "qy.do1.com.cn", "/");
    
    
        //        address=黑龙江省哈尔滨市道里区太行路
//                &
//                isWorkDate=0
//                &
//                longitude=126.53176983486148
//                &
//                id=F9BB709F-CEBE-40A5-88C6-16ABBAF080C7
//                &
//                isgps=0
//                &
//                ruleId=5763CC61-9939-43A7-9485-B1B0CC0A8AD6
//                &
//                signDate=
//&
//        againsignin=1
//                &
//                latitude=45.70509498678491
        
        Map<String, Object> params = Maps.newHashMap();
        
        params.put("address","黑龙江省哈尔滨市道里区太行路");
        params.put("isWorkDate",0);
        params.put("longitude","126.53176983486148");
        params.put("id","458FEBFD-ECFD-419D-B875-4402F757AF6C");
        params.put("isgps",0);
        params.put("ruleId","5763CC61-9939-43A7-9485-B1B0CC0A8AD6");
        params.put("signDate","");
        params.put("againsignin",1);
        params.put("latitude","45.70509498678491");
        
        String s = clientUtils.post("https://qy.do1.com.cn/wxqyh/portal/checkworkAction!addsignin.action?dqdp_csrf_token=4073407eb0e938ab", params);
        //System.out.println(s);
        
        clientUtils.close();
    }
}
