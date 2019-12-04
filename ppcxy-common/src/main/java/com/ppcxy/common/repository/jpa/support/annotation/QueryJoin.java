package com.ppcxy.common.repository.jpa.support.annotation;

import javax.persistence.criteria.JoinType;
import java.lang.annotation.*;

/**
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryJoin {

    /**
     * 连接的名字
     * @return
     */
    String property();

    JoinType joinType();

}
