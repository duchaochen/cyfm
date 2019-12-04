<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<html>
<head>
    <title>授权详情</title>
</head>
<body>
<div class="portlet box editBox">
    <div class="portlet-title"><span>权限信息</span></div>
    <div class="portlet-body form">
        <form:form id="inputForm" modelAttribute="entity" action="#" method="post">
        <div class="form-body">
            <input type="hidden" name="id" value="${entity.id}"/>
            <div class="form-group">
                <form:label path="authType" cssClass="control-label">授权类型:</form:label>
                <div class="controls">
                    <form:select path="authType" items="${authorizeTypes}" itemLabel="info" itemValue="value" cssClass="form-control required"/>
                </div>
            </div>
            <div class="form-group">
                <label for="userId" class="control-label">用户id:</label>
                <div class="controls">
                    <input type="text" id="userId" name="userId" value="${entity.userId}" class="form-control required"/>
                </div>
            </div>
            <div class="form-group">
                <label for="targetId" class="control-label">授权对象id:</label>
                <div class="controls">
                    <input type="text" id="targetId" name="targetId" value="${entity.targetId}" class="form-control required"/>
                </div>
            </div>

            <div class="form-actions">
                <input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;
                <p class="help-block">(点击提交保存信息.)</p>
            </div>
        </div>
        </form:form>
    </div>
</div>
<script>
    $(function () {
        $cy.handleUniform();
        $("#inputForm").validate({

        });
    })
</script>
</body>
</html>
