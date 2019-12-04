<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<script type="text/javascript">
    function refreshTree() {
        parent.frames['treeFrame'].location.reload();
        $('#refreshInfo').text('刷新完成。');
    }
</script>

<c:set var="refreshInfo" value="<br/><br/><p id='refreshInfo'>修改树后不会自动刷新，如需要刷新，请点击 <a class='btn btn-success' onclick='refreshTree();'><i class
='icon-refresh'></i> 刷新树</a></p>"/>

<c:if test="${not empty message}">
    <c:set var="message" value="${message}${refreshInfo}" scope="request"/>
</c:if>
<c:if test="${not empty error}">
    <c:set var="error" value="${error}${refreshInfo}" scope="request"/>
</c:if>
