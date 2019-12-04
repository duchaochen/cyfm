<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
  <title>模板详情</title>
</head>
<body>

<div class="portlet box editBox">
    <div class="portlet-title"><span>通知模板信息</span></div>
    <div class="portlet-body form">
        <form:form id="inputForm"  modelAttribute="entity" action="#" method="post">
        <div class="form-body">
            <input type="hidden" name="id" value="${entity.id}"/>
            <div class="form-group">
                <form:label path="system" cssClass="control-label">所属系统</form:label>
                <div class="controls">
                    <form:select path="system" cssClass="form-control " items="${notificationSystems}" itemLabel="info"></form:select>
                </div>
            </div>
            <div class="form-group">
                <form:label path="name">模板名称</form:label>
                <div class="controls">
                    <form:input path="name" cssClass="form-control required stringCheck" placeholder="不超过100个字符"/>
                </div>
            </div>
            <div class="form-group">
                <form:label path="title">模板标题</form:label>
                <div class="controls">
                    <form:input path="title" cssClass="form-control required" placeholder="不超过100个字符"/>
                </div>
            </div>
            <div class="form-group">
                <form:label path="template">模板内容</form:label>
                <div class="controls">
                    <form:textarea path="template" cssClass="form-control required"/>
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
            rules: {
                name: {
                    required: true
                    , variable: true
                    , rangelength: [4, 20]
                    , remote: {//后台验证唯一性
                        type: "POST",
                        url: "${ctx}/manage/maintain/notificationTemplate/validate",
                        data: {
                            'fieldId': 'name'
                            , 'fieldValue': function () {
                                return $("#name").val();
                            }
                            , 'id': '${entity.id}'
                        }
                    }
                }

            }
            , messages: {
                name: {
                    remote: "模板名称已经被其他记录使用"
                }
            }
        });
    })
</script>
</body>
</html>
