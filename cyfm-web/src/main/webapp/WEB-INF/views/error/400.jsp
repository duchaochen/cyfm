<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>400 - 用户权限不足</title>
</head>
<body>
    <h2>400 - 什么轨迹.${msg}</h2>
    <p><a href="<c:url value="/"/>">返回首页</a></p>
</body>
</html>
