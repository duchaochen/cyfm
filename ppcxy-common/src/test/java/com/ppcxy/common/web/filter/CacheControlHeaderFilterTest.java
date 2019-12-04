/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ppcxy.common.web.filter;

import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheControlHeaderFilterTest {
	@Test
	public void test() throws IOException, ServletException {
		MockFilterConfig config = new MockFilterConfig();
		MockFilterChain chain = new MockFilterChain();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		config.addInitParameter("expiresSeconds", "123");

		CacheControlHeaderFilter filter = new CacheControlHeaderFilter();
		filter.init(config);
		filter.doFilter(request, response, chain);

		assertThat(response.getHeader("Cache-Control")).isEqualTo("private, max-age=123");
	}
}
