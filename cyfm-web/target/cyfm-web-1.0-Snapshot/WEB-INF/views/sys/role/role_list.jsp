<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
  <title>角色管理</title>
</head>
<body>
<div class="tools search-toolbar">
    <div class="toolbar-right">
        <form class="form-search form-inline text-right" action="#">
            <div class="form-group">
                <label>角色名称：<input type="text" name="search.name_like" class="form-control input-small" value="${param['search.name_like']}"></label>
            </div>
            <div class="form-group">
                <label><button type="submit" class="btn btn-default" id="search_btn">查询</button></label>
            </div>
        </form>
    </div>
</div>
<div class="listTableWrap">
    <table id="contentTable" data-tid="${modelName}" class="table table-list table-sort table-striped table-bordered table-hover table-condensed table-advance">
        <thead>
            <tr>
                <th class="check"><input type="checkbox"></th>
                <th data-sort="name">角色名称</th>
                <th>非资源权限</th>
                <th class="action">操作</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.content}" var="role">
            <tr>
                <td class="check"><input name="ids" type="checkbox" value="${role.id}"></td>
                <td>${role.name}&nbsp;</td>
                <td>${role.permissions}&nbsp;</td>
                <td class="action">
                    <button class="btn btn-xs blue" onclick="showRoleDetails('${role.id}','${role.name}')">实体授权</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<cy:pagination page="${page}" paginationSize="5"></cy:pagination>
<script>
    function showRoleDetails(roleId,roleName) {
        top.layer.open({
            type:2,
            area:['660px','600px'],
            title:'角色授权实体管理-' + roleName,
            content: "${ctx}/sys/role/roleAllot/"+roleId
        })
    }
</script>
</body>
</html>
