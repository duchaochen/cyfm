<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="tabbable-line">
    <br/>
    <cy:showMessage errorMessage="${error.message}"/>
    <c:set var="stackTrace" value="${error.stackTrace}"/>
    <%@include file="exception_details.jsp" %>
</div>
