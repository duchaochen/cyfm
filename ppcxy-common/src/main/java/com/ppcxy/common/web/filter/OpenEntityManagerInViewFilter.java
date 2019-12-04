package com.ppcxy.common.web.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 委托给实际的OpenEntityManagerInViewFilter；
 * 但加入了过滤功能；这样有些地址不需要EntityManager就不需要拦截了
 */
public class OpenEntityManagerInViewFilter extends BaseFilter {
    private org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter delegate =
            new org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter();


    @Override
    public void init() throws ServletException {
        super.init();
        delegate.init(getConfig());
    }

    @Override
    public void destroy() {
        super.destroy();
        delegate.destroy();
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        delegate.doFilter(request, response, chain);
    }
}
