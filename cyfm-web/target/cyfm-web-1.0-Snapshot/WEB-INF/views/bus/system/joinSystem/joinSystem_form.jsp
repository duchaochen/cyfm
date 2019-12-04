<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title>接入系统详情</title>
</head>
<body>
    <div class="portlet box editBox">
        <div class="portlet-title"><span>接入系统信息</span></div>
        <div class="portlet-body form">
            <form:form id="inputForm"  modelAttribute="entity" action="#" method="post">
            <div class="form-body">
                <input type="hidden" name="id" value="${entity.id}"/>
                <div class="form-group">
                    <label for="sysName" class="control-label">系统名称:</label>
                    <div class="controls">
                        <input type="text" id="sysName" name="sysName" value="${entity.sysName}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="sysAddress" class="control-label">系统地址:</label>
                    <div class="controls">
                        <input type="text" id="sysAddress" name="sysAddress" value="${entity.sysAddress}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="sysBasePath" class="control-label">系统根路径:</label>
                    <div class="controls">
                        <input type="text" id="sysBasePath" name="sysBasePath" value="${entity.sysBasePath}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="loginApiPath" class="control-label">接入系统授权api:</label>
                    <div class="controls">
                        <input type="text" id="loginApiPath" name="loginApiPath" value="${entity.loginApiPath}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="authUsernameKey" class="control-label">授权用户名Key:</label>
                    <div class="controls">
                        <input type="text" id="authUsernameKey" name="authUsernameKey" value="${empty entity.authUsernameKey ? 'username' : entity.authUsernameKey}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="authUsername" class="control-label">授权用户名:</label>
                    <div class="controls">
                        <input type="text" id="authUsername" name="authUsername" value="${entity.authUsername}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="authPasswordKey" class="control-label">授权密码Key:</label>
                    <div class="controls">
                        <input type="text" id="authPasswordKey" name="authPasswordKey" value="${empty entity.authPasswordKey ? 'password' : entity.authPasswordKey}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="authPassword" class="control-label">授权密码:</label>
                    <div class="controls">
                        <input type="password" id="authPassword" name="authPassword" value="${entity.authPassword}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="authOtherParams" class="control-label">其他授权补充参数:</label>
                    <div class="controls">
                        <textarea id="authOtherParams" name="authOtherParams" class="form-control">${entity.authOtherParams}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label for="requstMethod" class="control-label">授权接口访问方式:</label>
                    <div class="controls">
                        <input type="text" id="requstMethod" name="requstMethod" value="${entity.requstMethod}" class="form-control required"/>
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
