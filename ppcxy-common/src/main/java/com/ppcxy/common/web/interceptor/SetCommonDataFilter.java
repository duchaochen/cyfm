package com.ppcxy.common.web.interceptor;

import com.ppcxy.common.web.filter.BaseFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 设置通用数据的Filter
 * <p/>
 * 使用Filter时 文件上传时 getParameter时为null 所以改成Interceptor
 * <p/>
 * 1、ctx---->request.contextPath
 * 2、currentURL---->当前地址
 */
public class SetCommonDataFilter extends BaseFilter {

    private SetCommonDataInterceptor setCommonDataInterceptor = new SetCommonDataInterceptor();

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            //使用Filter时 文件上传时 getParameter时为null 所以改成Interceptor
            setCommonDataInterceptor.preHandle(request, response, null);
        } catch (Exception e) {
            throw new ServletException(e);
        }
        chain.doFilter(request, response);
    }
}
