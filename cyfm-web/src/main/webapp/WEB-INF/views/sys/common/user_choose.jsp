<%--
  Created by IntelliJ IDEA.
  User: weep
  Date: 2017/9/10
  Time: 下午1:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<html>
<head>
    <title>用户选择</title>
    <style>
        body{
            min-width: auto;
        }

    </style>
</head>
<body>
<div class="container">
    <div class="tools search-toolbar">
        <div class="toolbar-right">
            <form class="form-search form-inline text-right" action="#">
                <div class="form-group">
                    <label>关键字(用户名,用户姓名, Email)：<input type="text" name="search.keyword_custom"
                                                       class="form-control input-small"
                                                       value="${param['search.keyword_custom']}">
                        <button type="submit" class="btn btn-default" id="search_btn">查询</button>
                    </label>
                </div>
            </form>
        </div>
    </div>
    <div class="listTableWrap">
        <table id="contentTable" data-tid="user_choose"
               class="table table-list table-sort table-striped table-bordered table-hover table-condensed table-advance">
            <thead>
            <tr>
                <th class="check"><input type="checkbox"></th>
                <th>用户名</th>
                <th>用户姓名</th>
                <th>Email</th>
                <th>团队</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${choosePage.content}" var="user">
                <tr>
                    <td class="check"><input name="ids" type="checkbox" value="${user.id}" data-id="${user.id}" data-username="${user.username}" data-name="${user.name}" data-email="${user.email}"  data-tel="${user.tel}" data-show="${user.name}[${user.username}]"></td>
                    <td>${user.username}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${empty user.team ? '无' : user.team.name}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <cy:pagination page="${choosePage}" paginationSize="5"></cy:pagination>
</div>
</body>
</html>
