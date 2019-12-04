<%@tag pageEncoding="UTF-8" description="构建子菜单" %>
<%@ attribute name="menu" type="com.ppcxy.cyfm.sys.entity.resource.dto.Menu" required="true" description="当前菜单" %>
<%@ attribute name="parentName" type="java.lang.String" required="true" description="父菜单名" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="cy" tagdir="/WEB-INF/tags" %>
<c:choose>
    <c:when test="${!menu.hasChildren}">
        <li>
            <a href="<%=menuUrl(request, menu.getUrl())%>" target="_self" nav-n="${parentName},${menu.name},<%=menuUrl(request, menu.getUrl())%>,${menu.menuType}">
                <div class="root-icon"><i class="${menu.icon} icon-large"></i></div>
                <h2>${menu.name}</h2>
            </a>
        </li>
    </c:when>
    <c:otherwise>
        <li>
            <a href="javascript:;">
                <i class="${menu.icon}"></i>
                <span class="title">${menu.name}</span><span class="arrow"></span>
            </a>
            <ul class="sub-menu">
                <c:forEach items="${menu.children}" var="menu2">
                    <cy:mobileSubmenu menu="${menu2}" parentName="${parentName},${menu.name}"/>
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
