<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title>静态资源管理</title>
    <script>
        var ctx="${ctx}"
    </script>
</head>
<body>
<div class="alert alert-info">
    <strong>注意：</strong>你应该把需要版本化的css/js放到webapp/WEB-INF/views/common下，且已jspf为后缀，系统自动扫描这些文件<br/>
    <strong>yui 压缩命令：</strong>java -jar D:\yuicompressor-2.4.2.jar  application.js   --charset utf-8  -o min.js
</div>
<cy:showMessage/>
<div class="row-fluid tool ui-toolbar">
    <div>
        <div class="btn-group">
            <button type="button" class="btn blue btn-custom btn-batch-version">
                <i class="icon-plus"></i>
                版本+1
            </button>
            <button type="button" class="btn yellow btn-custom btn-clear-version">
                <i class="icon-eraser"></i>
                清空版本
            </button>
            <button type="button" class="btn green btn-custom btn-batch-compress">
                <i class="icon-magic"></i>
                压缩js/css
            </button>
            <div class="btn-group last">
                <button type="button" class="btn red btn-custom dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="icon-retweet"></i>
                    切换版本
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li>
                        <a class="btn btn-custom btn-batch-switch min">
                            <i class="icon-retweet"></i>
                            切换到压缩版
                        </a>
                    </li>
                    <li>
                        <a class="btn btn-custom btn-batch-switch">
                            <i class="icon-retweet"></i>
                            切换到开发版
                        </a>
                    </li>
                </ul>
            </div>

        </div>
    </div>
</div>
<div class="listTableWrap">
<table id="contentTable" data-tid="staticResourceVersion" class="table table-list table-sort sort-table dataTable table-bordered table-hover table-striped table-condensed flip-content">
    <thead class="flip-content">
    <tr>
        <th class="check"><input type="checkbox"></th>
        <th>URL</th>
        <th style="width: 80px">当前版本</th>
        <th class="action" style="width: 220px;">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${resources}" var="r">
        <c:set var="fileName" value="${r.key}"/>
        <c:set var="resourceList" value="${r.value}"/>
        <tr class="info bold">
           <td colspan="4">文件：${fileName}</td>
        </tr>
        <c:forEach items="${resourceList}" var="s" varStatus="vs">
            <tr>
                <td class="check">
                    <input type="checkbox" name="ids" value="${vs.index}">
                    <input type="hidden" name="fileName" value="${fn:escapeXml(fileName)}">
                    <input type="hidden" name="content" value="${fn:escapeXml(s.content)}">
                </td>
                <td name="url">${s.url}</td>
                <td name="version">${s.version}</td>
                <td class="action" nowrap="nowrap">
                    <a class="btn btn-xs blue btn-version">版本+1</a>
                    <a class="btn btn-xs green btn-compress">压缩</a>

                    <div class="btn-group">
                        <button type="button" class="btn btn-xs red dropdown-toggle no-disabled" data-toggle="dropdown" aria-expanded="true">
                            <i class="fa fa-ellipsis-horizontal"></i> 切换版本 <i class="fa fa-angle-down"></i>
                        </button>
                        <ul class="dropdown-menu pull-right">
                            <li>
                                <a href="javascript:;" class="btn btn-custom btn-switch min">
                                    切换到压缩版
                                </a>
                            </li>
                            <li>
                                <a href="javascript:;" class="btn btn-custom btn-switch">
                                    切换到开发版
                                </a>
                            </li>
                        </ul>
                    </div>
                </td>

            </tr>
        </c:forEach>
    </c:forEach>
    </tbody>
</table>
</div>
<div class="clear" style="height: 40px;"></div>
<%@include file="/WEB-INF/views/common/manage/import-maintain-js.jspf"%>
<script type="text/javascript">
$(function() {
    $cy.maintain.staticResource.initBtn();
});
</script>
</body>
</html>
