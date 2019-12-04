<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title>修改密码</title>
</head>
<body>
<div data-table="table" class="tabbable-line">
<%@include file="nav.jspf"%>
<div class="tab-content">
    <div class="portlet box editBox">
    <div class="portlet-title"><span>团队信息</span></div>
    <div class="portlet-body form">
        <form id="inputForm" method="post" action="#">
            <div class="form-body">
                <div class="control-group">
                    <label for="oldPassword" class="control-label">旧密码</label>
                    <div class="controls">
                        <input type="password" id="oldPassword" name="oldPassword" class="form-control validate[required]"/>
                    </div>
                </div>

                <div class="control-group">
                    <label for="newPassword1" class="control-label">新密码</label>
                    <div class="controls">
                        <input type="password" id="newPassword1" name="newPassword1" class="form-control validate[required,custom[includeSpace],minSize[5],maxSize[25]]" maxlength="25" placeholder="请输入5-25位的新密码"/>
                    </div>
                </div>
                <div class="control-group">
                    <label for="newPassword2" class="control-label">确认新密码</label>
                    <div class="controls">
                        <input type="password" id="newPassword2" name="newPassword2" class="form-control validate[required,equals[newPassword1]]" placeholder="重复输入新密码确认"/>
                    </div>
                </div>

                <div class="control-group">
                    <div class="controls">
                        <button type="submit" class="btn btn-primary">
                            <i class="icon-key"></i>
                            ${action}
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</div>
</div>
</body>
<script>
    $(function () {
        $cy.handleUniform();
        $("#inputForm").validate({

        });
    })
</script>
</body>
</html>
