<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title>个人中心</title>
</head>
<body>
<div data-table="table" class="tabbable-line">
<%@include file="nav.jspf"%>
<div class="tab-content">
    <div class="portlet box editBox">
        <div class="portlet-title"><span>个人资料</span></div>
        <div class="portlet-body form">
    <form id="inputForm" method="post" action="#">
        <div class="form-body">
            <div class="col-md-6 col-xs-12">
                <div class="control-group">
                    <label for="username">用户名</label>
                    <div id="username" class="controls">
                        ${user.username}
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-xs-12">
                <div class="control-group">
                    <label for="createDate">创建时间</label>
                    <div id="createDate" class="controls input-append">
                        <fmt:formatDate value="${user.createDate}" pattern="yyyy-MM-dd hh:mm:ss" type="both"/>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-xs-12">
                <div class="control-group">
                    <label for="name">用户姓名</label>
                    <div class="controls">
                        <input type="input" id="name" name="name" value="${user.name}" class="form-control" readonly placeholder="5到20个汉字、字母、数字或下划线"/>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-xs-12">
                <div class="control-group">
                    <label for="email">邮箱</label>
                    <div class="controls">
                        <input type="email" id="email" name="email" value="${user.email}" class="form-control" readonly placeholder="zzzzz@163.com"/>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-xs-12">
                <div class="control-group">
                    <label for="tel">手机号</label>
                    <div class="controls">
                        <input type="input" id="tel" name="tel" value="${user.tel}" class="form-control required" readonly placeholder="如15666666520"/>
                    </div>
                </div>
            </div>
            <div class="clearfix"></div>
        </div>
    </form>
    </div>
    </div>
</div>
</div>
<script>
    $(function () {
        $cy.handleUniform();
    })
</script>
</body>
</html>
