package com.ppcxy.common.extra.aop;

import com.ppcxy.common.cache.BaseCacheAspect;
import com.ppcxy.cyfm.sys.entity.user.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 用户缓存切面,补充jpa缓存
 * 缓存实现
 * 1、username/email/tel------>id
 * 2、id------->Model
 */
@Component
@Aspect
public class UserCacheAspect extends BaseCacheAspect implements Ordered {

    private static final String ID_KEY_PREFIX = "id-";
    private static final String USERNAME_KEY_PREFIX = "username-";
    private static final String EMAIL_KEY_PREFIX = "email-";
    private static final String TEL_KEY_PREFIX = "tel-";
    public UserCacheAspect() {
        setCacheName("sys-userCache");
    }

    ////////////////////////////////////////////////////////////////////////////////
    ////切入点
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 匹配用户Service
     */
    @Pointcut(value = "target(com.ppcxy.cyfm.sys.service.user.UserService)")
    private void userServicePointcut() {
    }

    /**
     * only put
     * 如 新增 修改 登录 改密 改状态  只有涉及修改即可
     */
    @Pointcut(value =
            "execution(* save(..)) " +
                    "|| execution(* saveAndFlush(..)) " +
                    "|| execution(* update(..)) " +
                    "|| execution(* login(..)) " +
                    "|| execution(* changePassword(..)) " +
                    "|| execution(* changeStatus(..))")
    private void cachePutPointcut() {
    }


    /**
     * evict 比如删除
     */
    @Pointcut(value = "(execution(* delete(*))) && args(arg)", argNames = "arg")
    private void cacheEvictPointcut(Object arg) {
    }

    /**
     * put 或 get
     * 比如查询
     */
    @Pointcut(value =
            "(execution(* findByUsername(*)) " +
                    "|| execution(* findByEmail(*)) " +
                    "|| execution(* findByTel(*)) " +
                    "|| execution(* findOne(*)))")
    private void cacheablePointcut() {
    }


    ////////////////////////////////////////////////////////////////////////////////
    ////增强实现
    ////////////////////////////////////////////////////////////////////////////////
    @AfterReturning(value = "userServicePointcut() && cachePutPointcut()", returning = "user")
    public void cachePutAdvice(Object user) {
        //put cache
        put((User) user);
    }

    @After(value = "userServicePointcut() && cacheEvictPointcut(arg)", argNames = "arg")
    public void cacheEvictAdvice(Object arg) {
        if (arg == null) {
            return;
        }
        if (arg instanceof Long) {
            //only evict id
            evictId(String.valueOf(arg));
        }
        if (arg instanceof Long[]) {
            for (Long id : (Long[]) arg) {
                //only evict id
                evictId(String.valueOf(id));
            }
        }

        if (arg instanceof String) {
            //only evict id
            evictId((String) arg);
        }
        if (arg instanceof String[]) {
            for (String id : (String[]) arg) {
                //only evict id
                evictId(String.valueOf(id));
            }
        }
        if (arg instanceof User) {
            //evict user
            evict((User) arg);
        }
    }

    @Around(value = "userServicePointcut() && cacheablePointcut()")
    public Object cacheableAdvice(ProceedingJoinPoint pjp) throws Throwable {

        String methodName = pjp.getSignature().getName();
        Object arg = pjp.getArgs().length >= 1 ? pjp.getArgs()[0] : null;

        String key = "";
        boolean isIdKey = false;
        if ("findOne".equals(methodName)) {
            key = idKey(String.valueOf(arg));
            isIdKey = true;
        } else if ("findByUsername".equals(methodName)) {
            key = usernameKey((String) arg);
        } else if ("findByEmail".equals(methodName)) {
            key = emailKey((String) arg);
        } else if ("findByTel".equals(methodName)) {
            key = telKey((String) arg);
        }

        User user = null;
        if (isIdKey == true) {
            user = get(key);
        } else {
            Long id = get(key);
            if (id != null) {
                key = idKey(String.valueOf(id));
                user = get(key);
            }
        }
        //cache hit
        if (user != null) {
            log.debug("cacheName:{}, hit key:{}", cacheName, key);
            return user;
        }
        log.debug("cacheName:{}, miss key:{}", cacheName, key);

        //cache miss
        user = (User) pjp.proceed();

        //put cache
        put(user);
        return user;

    }


    private String idKey(String id) {
        return ID_KEY_PREFIX + id;
    }

    private String usernameKey(String username) {
        return USERNAME_KEY_PREFIX + username;
    }

    private String emailKey(String email) {
        return EMAIL_KEY_PREFIX + email;
    }

    private String telKey(String tel) {
        return TEL_KEY_PREFIX + tel;
    }


    ////////////////////////////////////////////////////////////////////////////////
    ////cache 抽象实现
    ////////////////////////////////////////////////////////////////////////////////
    public void put(User user) {
        if (user == null) {
            return;
        }
        Long id = user.getId();
        //username email tel ---> id
        put(usernameKey(user.getUsername()), id);
        put(emailKey(user.getEmail()), id);
        put(telKey(user.getTel()), id);
        // id ---> user
        put(idKey(String.valueOf(id)), user);
    }


    public void evictId(String id) {
        //取出cache的user,根据cache的user弹出缓存。
        User cacheUer = get(idKey(String.valueOf(id)));
        evict(idKey(String.valueOf(id)));
        evict(usernameKey(cacheUer.getUsername()));
        evict(emailKey(cacheUer.getEmail()));
        evict(telKey(cacheUer.getTel()));
    }

    public void evict(User user) {
        if (user == null) {
            return;
        }
        Long id = user.getId();
        //取出cache的user,根据cache的user弹出缓存。
        User cacheUer = get(idKey(String.valueOf(id)));
        evict(idKey(String.valueOf(id)));
        evict(usernameKey(cacheUer.getUsername()));
        evict(emailKey(cacheUer.getEmail()));
        evict(telKey(cacheUer.getTel()));
    }
    
    
    @Override
    public int getOrder() {
        return 3;
    }
}
