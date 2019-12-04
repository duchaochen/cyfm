<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="icon" type="com.ppcxy.cyfm.manage.entity.maintaion.Icon" required="false" description="当前图标" %>
<%@ attribute name="iconIdentity" type="java.lang.String" required="false" description="当前图标" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cy" tagdir="/WEB-INF/tags" %>
<c:if test="${not empty icon}">
<c:choose>
    <c:when test="${not empty icon.cssClass}">
        <i class="${icon.cssClass}"></i>
    </c:when>
    <c:when test="${not empty icon.imgSrc}">
        <i class="${icon.identity}" title="生成后的图标"></i>
    </c:when>
    <c:when test="${not empty icon.spriteSrc}">
        <i class="${icon.identity}" title="生成后的图标"></i>
    </c:when>
</c:choose>
</c:if>
<c:if test="${not empty iconIdentity}">
    <i class="${iconIdentity}"></i>
</c:if>
