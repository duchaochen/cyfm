package com.ppcxy.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.StringUtils;

/**
 * 基础cache切面
 */
public class BaseCacheAspect implements InitializingBean {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected String cacheName;
    @Autowired
    private CacheManager cacheManager;
    private Cache cache;

    /**
     * 缓存池名称
     *
     * @param cacheName
     */
    public void setCacheName(String cacheName) {

        this.cacheName = cacheName;
    }

    /**
     * 缓存管理器
     *
     * @return
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        cache = cacheManager.getCache(cacheName);
    }

    public void clear() {
        log.debug("cacheName:{}, cache clear", cacheName);
        this.cache.clear();
    }

    public void evict(String key) {
        log.debug("cacheName:{}, evict key:{}", cacheName, key);
        this.cache.evict(key);
    }

    public <T> T get(Object key) {
        log.debug("cacheName:{}, get key:{}", cacheName, key);
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        Cache.ValueWrapper value = cache.get(key);
        if (value == null) {
            return null;
        }
        return (T) value.get();
    }

    public void put(String key, Object value) {
        log.debug("cacheName:{}, put key:{}", cacheName, key);
        this.cache.put(key, value);
    }

}
