<%@ tag import="com.ppcxy.common.entity.search.SearchOperator" %>
<%@ tag import="org.apache.commons.lang3.StringUtils" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ attribute name="searchType" type="java.lang.String" required="true" description="筛选类型" %>
<%=StringUtils.isNotBlank(searchType) ? SearchOperator.valueOf(searchType).getInfo() : "" %>
