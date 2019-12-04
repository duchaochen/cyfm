<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
  <title>授权管理</title>
</head>

<body>
 <div class="tools search-toolbar">
   <div class="toolbar-right">
       <form class="form-search form-inline text-right" action="#">
           <div class="form-group">
               <label>授权用户：<input type="text" name="search.userId_like" class="form-control input-small"
                                  value="${param['search.userId_like']}"></label>
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
            <th>用户id</th>
            <th>目标id</th>
            <th>授权类型</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.content}" var="authorize">
            <tr>
                <td class="check"><input name="ids" type="checkbox" value="${authorize.id}"></td>
                <td>${authorize.authType.info}&nbsp;</td>
                <td><sys:showUsername id="${authorize.userId}"/>&nbsp;</td>
                <td><sys:showRoleName id="${authorize.targetId}"/>&nbsp;</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
 <cy:pagination page="${page}" paginationSize="5"></cy:pagination>
</body>
</html>
