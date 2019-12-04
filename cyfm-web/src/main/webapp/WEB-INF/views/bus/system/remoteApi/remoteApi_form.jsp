<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title>接入API详情</title>
</head>
<body>
    <div class="portlet box editBox">
        <div class="portlet-title"><span>接入API信息</span></div>
        <div class="portlet-body form">
            <form:form id="inputForm"  modelAttribute="entity" action="#" method="post">
            <div class="form-body">
                <input type="hidden" name="id" value="${entity.id}"/>
                <div class="form-group">
                    <form:label path="joinSystem" cssClass="control-label">接入系统:</form:label>
                    <div class="controls">
                        <form:select path="joinSystem" cssClass="form-control required" >
                            <form:option label="请选择" value=""/>
                            <form:options items="${joinSystems}" itemLabel="sysName" itemValue="id"></form:options>
                        </form:select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="remoteApiName" class="control-label">API名称:</label>
                    <div class="controls">
                        <input type="text" id="remoteApiName" name="remoteApiName" value="${entity.remoteApiName}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="remoteApiPath" class="control-label">API路径:</label>
                    <div class="controls">
                        <input type="text" id="remoteApiPath" name="remoteApiPath" value="${entity.remoteApiPath}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="requestMethod" class="control-label">访问方法:</label>
                    <div class="controls">
                        <input type="text" id="requestMethod" name="requestMethod" value="${entity.requestMethod}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="paramModel" class="control-label">访问参数模型:</label>
                    <div class="controls">
                        <textarea id="paramModel" name="paramModel" class="form-control">${entity.paramModel}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label for="remark" class="control-label">备注信息:</label>
                    <div class="controls">
                        <textarea id="remark" name="remark" class="form-control">${entity.remark}</textarea>
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
