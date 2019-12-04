<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
  <title>通知模板管理</title>
</head>

<body>
 <div class="tools search-toolbar">
   <div class="toolbar-right">
       <form class="form-search form-inline text-right" action="#">
           <div class="form-group">
               <label>模板名称：<input type="text" name="search.name_like" class="form-control input-small"
                                  value="${param['search.name_like']}"></label>
               <label>模板内容：<input type="text" name="search.template_like" class="form-control input-small"
                                  value="${param['search.template_like']}"></label>
           </div>
           <div class="form-group">
               <label>
                   <button type="submit" class="btn btn-default" id="search_btn">查询</button>
               </label>
           </div>
       </form>
   </div>
 </div>
<div class="listTableWrap">
    <table id="contentTable" data-tid="${modelName}" class="table table-list table-sort table-striped table-bordered table-hover table-condensed table-advance">
        <thead>
        <tr>
            <th class="check"><input type="checkbox"></th>
            <th style="width: 200px" sort="name">模板名称</th>
            <th style="width: 100px">所属系统</th>
            <th>模板内容</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.content}" var="template" varStatus="s">
            <tr>
                <td class="check"><input name="ids" type="checkbox" value="${template.id}"></td>
                <td>${template.name}</td>
                <td>${template.system.info}</td>
                <td>${template.template}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
 <cy:pagination page="${page}" paginationSize="5"/>
</body>
</html>
