package com.ppcxy.common.extra.aop;

import com.google.common.collect.Maps;
import com.ppcxy.common.cache.BaseCacheAspect;
import com.ppcxy.cyfm.sys.entity.resource.dto.Menu;
import com.ppcxy.cyfm.sys.entity.user.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 缓存及清理菜单缓存
 */
@Component
@Aspect
public class ResourceMenuCacheAspect extends BaseCacheAspect implements Ordered {
    
    private static final String MENUS_KEY_PREFIX = "menus-";
    
    public ResourceMenuCacheAspect() {
        setCacheName("sys-menuCache");
    }
    
    @Pointcut(value = "target(com.ppcxy.cyfm.sys.service.resource.ResourceService)")
    private void resourceServicePointcut() {
    }
    
    @Pointcut(value = "execution(* save(..)) || execution(* update*(..))|| execution(* move(..)) || execution(* appendChild(..)) || execution(* delete*(..))")
    private void resourceCacheEvictAllPointcut() {
    }
    
    @Pointcut(value = "execution(* findMenus(*,*)) && args(user,menuRoot)", argNames = "user,menuRoot")
    private void resourceCacheablePointcut(User user, Long menuRoot) {
    }
    
    @Before(value = "resourceServicePointcut() && resourceCacheEvictAllPointcut()")
    public void findRolesCacheableAdvice() throws Throwable {
        clear();
    }
    
    @Around(value = "resourceServicePointcut() && resourceCacheablePointcut(user,menuRoot)", argNames = "pjp,user,menuRoot")
    public Object findRolesCacheableAdvice(ProceedingJoinPoint pjp, User user, Long menuRoot) throws Throwable {
        
        String key = menusKey(user.getId());
        Map<Long, List<Menu>> retVal = (Map<Long, List<Menu>>) get(key);
        
        
        if (retVal != null && retVal.get(menuRoot) != null) {
            log.debug("cacheName:{}, method:findRolesCacheableAdvice, hit key:{}", cacheName, key);
            return retVal.get(menuRoot);
        }
        log.debug("cacheName:{}, method:findRolesCacheableAdvice, miss key:{}", cacheName, key);
        
        List<Menu> result = (List<Menu>) pjp.proceed();
        
        //如果无缓存，则初始化缓存对象
        if (retVal == null) {
            retVal = Maps.newHashMap();
            put(key, retVal);
        }
        
        //将aop方法结果put到
        retVal.put(menuRoot, result);
        
        return result;
    }
    
    
    public void evict(Long userId) {
        evict(menusKey(userId));
    }
    
    
    private String menusKey(Long userId) {
        return this.MENUS_KEY_PREFIX + userId;
    }
    
    
    @Override
    public int getOrder() {
        return 3;
    }
}
