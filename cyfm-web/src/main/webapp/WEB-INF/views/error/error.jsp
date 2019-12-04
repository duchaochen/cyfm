<%@ page contentType="text/html;charset=UTF-8" language="java" session="false"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@ page import="com.ppcxy.common.entity.search.exception.SearchException" %>
<%@ page import="com.ppcxy.common.exception.BaseException" %>
<%@ page import="com.ppcxy.common.utils.LogUtils" %>
<%@ page import="org.apache.shiro.authc.AuthenticationException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<head>
    <!-- ================================= Css 区域 ========================================== -->
    <%@include file="/WEB-INF/views/common/import-css.jspf"%>
    <!-- ================================= JS 区域 ========================================== -->
    <%@include file="/WEB-INF/views/common/import-js.jspf"%>
    <title>系统错误</title>
</head>
<body>
<div class="tabbable-line">
    <%
        Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String reason = (String) request.getAttribute("javax.servlet.error.message");


        LogUtils.logPageError(request);
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        pageContext.setAttribute("statusCode", statusCode);

        String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        String queryString = request.getQueryString();
        String url = uri + (queryString == null || queryString.length() == 0 ? "" : "?" + queryString);
        pageContext.setAttribute("url", url);

        Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
        request.setAttribute("exception", exception);
        request.setAttribute("baseException", exception instanceof BaseException);
        request.setAttribute("authenticationException", exception instanceof AuthenticationException);
        request.setAttribute("searchException", exception instanceof SearchException);

    %>

    <c:if test="${statusCode eq 404}">
        <cy:showMessage
                errorMessage="<h3 style='display:inline;'>页面没有找到！</h3><br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;对不起，暂时没有找到您所访问的页面地址,请联系管理员解决此问题！&nbsp;&nbsp;&nbsp;&nbsp;<refresh><a href='${url}' class='btn btn-danger'>刷新页面</a></refresh>"/>
    </c:if>

    <c:if test="${statusCode ne 404}">
        <c:if test="${baseException}">
            <cy:showMessage
                    errorMessage="<h4 style='display:inline;'>系统异常！</h4><br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;${exception.message}</a>"/>
        </c:if>
        <c:if test="${authenticationException}">
            <cy:showMessage
                    errorMessage="<h4 style='display:inline;'>登录验证错误！</h4><br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;${exception.message}</a>"/>
        </c:if>
        <c:if test="${searchException}">
            <cy:showMessage
                    errorMessage="<h4 style='display:inline;'>查询参数错误！</h4><br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;${exception.message}</a>"/>
        </c:if>
        <c:if test="${not baseException && not authenticationException && not searchException}">
            <cy:showMessage
                    errorMessage="<h3 style='display:inline;'>网络服务出现问题！</h3><br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;请刷新后尝试重新访问。如果仍然无法解决，者联系管理员解决此问题！&nbsp;&nbsp;&nbsp;&nbsp;<refresh><a href='${url}' class='btn btn-danger'>刷新页面</a></refresh>"/>
        </c:if>
        <shiro:hasRole name="dev_user">
            <c:if test="${not empty exception}">
                <%
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    exception.printStackTrace(printWriter);
                    pageContext.setAttribute("stackTrace", stringWriter.toString());
                %>
                <%@include file="exception_details.jsp" %>
            </c:if>
        </shiro:hasRole>
    </c:if>
</div>
<script>
    $(function () {
        $cy.waitingOver();
    })
</script>
</body>
</html>
