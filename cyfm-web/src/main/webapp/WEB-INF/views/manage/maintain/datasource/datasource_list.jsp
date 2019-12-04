<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
  <title>数据源管理</title>
</head>

<body>
 <div class="tools search-toolbar">
   <div class="toolbar-right">
       <form class="form-search form-inline text-right" action="#">
           <div class="form-group">
               <label>源名称：<input type="text" name="search.dsName_like" class="form-control input-small"
                                 value="${param['search.dsName_like']}"></label>
               <label>库名称：<input type="text" name="search.dbName_like" class="form-control input-small"
                                 value="${param['search.dbName_like']}"></label>
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
            <th data-sort="dsName">数据源名称</th>
            <th data-sort="dsType">数据源类型</th>
            <th data-sort="dbName">库名称</th>
            <th data-sort="dbType">库类型</th>
            <th data-sort="dbHost">库IP</th>
            <th>库端口</th>
            <th>库用户名</th>
            <th>库密码</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.content}" var="datasource">
            <tr>
                <td class="check"><input name="ids" type="checkbox" value="${datasource.id}"></td>
                <td>${datasource.dsName}</td>
                <td>${datasource.dsType.info}</td>
                <td>${datasource.dbName}</td>
                <td>${datasource.dbType.name}</td>
                <td>${datasource.dbHost}</td>
                <td>${datasource.dbPort}</td>
                <td>${datasource.dbUsername}</td>
                <td>${datasource.dbPassword}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
 <cy:pagination page="${page}" paginationSize="5"/>
</body>
</html>
