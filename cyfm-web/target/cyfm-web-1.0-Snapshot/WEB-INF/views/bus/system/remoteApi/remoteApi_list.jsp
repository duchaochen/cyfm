<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title>接入API配置</title>
</head>
<body>
    <div class="tools search-toolbar">
        <div class="toolbar-right">
            <form class="form-search form-inline text-right" action="#">
                <div class="form-group">
                    <label>接入系统：</label> <input type="text" name="search.joinSystem.sysName_like" class="form-control input-small" value="${param['search.joinSystem.sysName_like']}">
                    <label>访问方法：</label> <input type="text" name="search.requestMethod_eq" class="form-control input-small" value="${param['search.requestMethod_eq']}">
                    <label>接入API名称：</label> <input type="text" name="search.remoteApiPath_eq" class="form-control input-small" value="${param['search.remoteApiName_like']}">
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
                <th data-sort="joinSystem.sysName">接入系统</th>
                <th data-sort="remoteApiName">API名称</th>
                <th data-sort="remoteApiPath">API路径</th>
                <th data-sort="requestMethod">访问方法</th>
                <th>访问参数模型</th>
                <th>备注信息</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.content}" var="remoteApi">
                <tr>
                    <td class="check"><input name="ids" type="checkbox" value="${remoteApi.id}"></td>
                    <td>${remoteApi.joinSystem.sysName}</td>
                    <td>${remoteApi.remoteApiName}</td>
                    <td>${remoteApi.remoteApiPath}</td>
                    <td>${remoteApi.requestMethod}</td>
                    <td>${remoteApi.paramModel}</td>
                    <td>${remoteApi.remark}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
     <cy:pagination page="${page}" paginationSize="5"/>
</body>
</html>
