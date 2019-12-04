<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title>接入系统配置</title>
</head>
<body>
    <div class="tools search-toolbar">
        <div class="toolbar-right">
           <form class="form-search form-inline text-right" action="#">
             <div class="form-group">
                 <label>接入系统名称：</label> <input type="text" name="search.sysName_like" class="form-control input-small" value="${param['search.sysName_like']}">
                 <button type="submit" class="btn btn-default" id="search_btn">查询</button>
             </div>
           </form>
        </div>
    </div>
    <div class="listTableWrap">
        <table id="contentTable" data-tid="${modelName}" class="table table-list table-sort table-striped table-bordered table-hover table-condensed table-advance">
            <thead>
            <tr>
                <th class="check"><input type="checkbox"></th>
                <th data-sort="sysName">系统名称</th>
                <th data-sort="sysAddress">系统地址</th>
                <th data-sort="sysBasePath">系统根路径</th>
                <th>接入系统授权api</th>
                <th>授权用户名</th>
                <th>授权密码</th>
                <th>授权接口访问方式</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.content}" var="joinSystem">
                <tr>
                    <td class="check"><input name="ids" type="checkbox" value="${joinSystem.id}"></td>
                    <td>${joinSystem.sysName}</td>
                    <td>${joinSystem.sysAddress}</td>
                    <td>${joinSystem.sysBasePath}</td>
                    <td>/login/</td>
                    <td>${joinSystem.authUsername}</td>
                    <td>********</td>
                    <td>${joinSystem.requstMethod}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <cy:pagination page="${page}" paginationSize="5"/>
</body>
</html>
