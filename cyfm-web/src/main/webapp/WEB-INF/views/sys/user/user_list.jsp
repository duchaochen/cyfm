<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
	<style>

	</style>
    <title>用户管理</title>
	<style>

	</style>
</head>
<body>
<div class="tools search-toolbar">
	<div class="toolbar-right">
		<form class="form-search form-inline text-right" action="#">
			<div class="form-group">
				<label>登录名：<input type="text" name="search.username_like" class="form-control input-small"
								  value="${param['search.username_like']}"></label>
				<label>电子邮箱：<input type="text" name="search.email_like" class="form-control input-small"
								   value="${param['search.email_like']}"></label>
				<label>手机号：<input type="text" name="search.tel_like" class="form-control input-small"
								  value="${param['search.tel_like']}"></label>
            </div>
			<div class="form-group">
				<label><button type="submit" class="btn btn-default" id="search_btn">查询</button></label>
			</div>
		</form>
	</div>
</div>
<%--用来控制table横向滚动条的div--%>
<div class="listTableWrap">
	<table id="contentTable" data-tid="${modelName}" class="table table-list table-sort table-striped table-bordered table-hover table-condensed table-advance">
		<thead>
		<tr>
			<th class="check"><input type="checkbox"></th>
			<th data-sort="username">登录名</th>
			<th data-sort="name">姓名</th>
			<th data-sort="email">电邮</th>
			<th>角色</th>
			<th>状态</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.content}" var="user">
			<tr>
				<td class="check"><input name="ids" type="checkbox" value="${user.id}"></td>
				<td>${user.username}&nbsp;</td>
				<td>${user.name}&nbsp;</td>
				<td>${user.email}&nbsp;</td>
				<td>${user.roleNames}&nbsp;</td>
				<td>${user.status.info}&nbsp;</td>
					<%-- 根据权限显示按钮 --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
<cy:pagination page="${page}" paginationSize="5"/>
<script>
    $(document).ready(function () {

    });
</script>
</body>
</html>
