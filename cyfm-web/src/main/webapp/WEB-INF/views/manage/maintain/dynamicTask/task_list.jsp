<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
  <title>任务调度管理</title>
</head>

<body>
 <div class="tools search-toolbar">
   <div class="toolbar-right">
       <form class="form-search form-inline text-right" action="#">
           <div class="form-group">
               <label>任务名称：<input type="text" name="search.name_like" class="form-control input-small"
                                  value="${param['search.name_like']}"></label>
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
            <th data-sort="name">任务名称</th>
            <th data-sort="cron">Cron表达式</th>
            <th>任务Bean名称/任务全限定类名</th>
            <th data-sort="methodName">执行方法名</th>
            <th data-sort="run">运行状态</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.content}" var="task">
            <tr>
                <td class="check"><input name="ids" type="checkbox" value="${task.id}"></td>
                <td>${task.name}&nbsp;</td>
                <td>${task.cron}&nbsp;</td>
                <td>${task.beanClass}${task.beanName}&nbsp;</td>
                <td>${task.methodName}&nbsp;</td>
                <td <c:if test="${not empty task.description}">style="color: red" </c:if>>${task.runInfo}&nbsp;</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
 <cy:pagination page="${page}" paginationSize="5"/>
<script>
    $(function () {

        $cy.toolbar.addMore({
            classText: "btn-start",
            text: "启动任务",
            icon: ""
        });
        $cy.toolbar.addMore({
            classText: "btn-stop",
            text: "停止任务",
            icon: ""
        });

        $(".btn.delete").addClass("custom").click(function() {
            var checkbox = $cy.table.getAllSelectedCheckbox($("#contentTable"));
            if(!checkbox.length) {
                return;
            }

            $cy.confirm({
                title : "确认删除",
                message: "<div class='form-inline'>是否强制终止正在运行的任务：<label class='checkbox inline'><input type='radio' name='forceTermination' checked='true' value='true'>是</label>&nbsp;&nbsp;<label class='checkbox inline'><input type='radio' name='forceTermination' value='false'>否</label></div>",
                yes : function() {
                    var forceTermination = $("[name=forceTermination]:checked").val();
                    window.location.href = ctx + '/manage/maintain/dynamicTask/batch/delete?' + checkbox.serialize() + "&forceTermination=" + forceTermination;
                }
            });
        });

        $(".btn-start").click(function() {
            var checkbox = $cy.table.getAllSelectedCheckbox($("#contentTable"));
            if(!checkbox.length) {
                return;
            }

            $cy.confirm({
                title : "启动任务",
                message: "确认启动选中任务吗？",
                yes : function() {
                    var forceTermination = $("[name=forceTermination]:checked").val();
                    window.location.href = ctx + '/manage/maintain/dynamicTask/start?' + checkbox.serialize();
                }
            });
        });

        $(".btn-stop").click(function() {
            var checkbox = $cy.table.getAllSelectedCheckbox($("#contentTable"));
            if(!checkbox.length) {
                return;
            }

            $cy.confirm({
                title : "停止任务",
                message: "<div class='form-inline'>是否强制终止正在运行的任务：<label class='checkbox inline'><input type='radio' name='forceTermination' checked='true' value='true'> 是</label>&nbsp;&nbsp;<label class='checkbox inline'><input type='radio' name='forceTermination' value='false'> 否</label></div>",
                yes : function() {
                    var forceTermination = $("[name=forceTermination]:checked").val();
                    window.location.href = ctx + '/manage/maintain/dynamicTask/stop?' + checkbox.serialize() + "&forceTermination=" + forceTermination;
                }
            });
        });
    });
</script>
</body>
</html>
