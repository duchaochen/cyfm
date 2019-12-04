<%@tag pageEncoding="UTF-8" description="构建子菜单" %>
<%@ attribute name="menu" type="com.ppcxy.cyfm.sys.entity.resource.dto.Menu" required="true" description="当前菜单" %>
<%@ attribute name="parentName" type="java.lang.String" required="true" description="父菜单名" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="cy" tagdir="/WEB-INF/tags" %>
<c:choose>
    <c:when test="${!menu.hasChildren}">
        <li class="nav-item">
            <a href="<%=menuUrl(request, menu.getUrl())%>" class="nav-link " target="rightFrame" nav-n="${parentName},${menu.name},<%=menuUrl(request, menu.getUrl())%>,${menu.menuType}" data-target-type="${menu.menuType}">
                <i class="${menu.icon}"></i>
                <span class="title tooltips" data-placement="bottom" data-original-title="${menu.name}"><cy:digest content="${menu.name}" length="9"></cy:digest></span>
                <span class="selected"></span>
                    <%--标记新功能支持--%>
                    <%--<span class="badge badge-roundless badge-danger">New</span>--%>
            </a>
        </li>
    </c:when>
    <c:otherwise>
        <li class="nav-item">
            <a href="javascript:;" class="nav-link nav-toggle">
                <i class="${menu.icon}"></i>
                <span class="title"><cy:digest content="${menu.name}" length="9"></cy:digest></span>
                <span class="arrow"></span>
            </a>
            <ul class="sub-menu" style="display: none;">
                <c:forEach items="${menu.children}" var="menu2">
                    <cy:submenu menu="${menu2}" parentName="${parentName},${menu.name}"/>
                </c:forEach>
            </ul>
        </li>
    </c:otherwise>
</c:choose>

<%!
    private static String menuUrl(HttpServletRequest request, String url) {
        if(url.startsWith("http")) {
            return url;
        }
        String ctx = request.getContextPath();

        if(url.startsWith(ctx) || url.startsWith("/" + ctx  )) {
            return url;
        }

        if(!url.startsWith("/")) {
            url = url + "/";
        }
        return ctx + url;

    }
%>

