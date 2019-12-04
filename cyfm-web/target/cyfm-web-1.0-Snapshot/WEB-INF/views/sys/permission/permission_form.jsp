<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
  <title>权限详情</title>
</head>
<body>
<div class="portlet box editBox">
    <div class="portlet-title"><span>权限信息</span></div>
    <div class="portlet-body form">
        <form:form id="inputForm"  modelAttribute="entity" action="#" method="post">
        <div class="form-body">
            <input type="hidden" name="id" value="${entity.id}"/>
            <div class="form-group">
                <label for="name" class="control-label">权限名称:</label>
                <div class="controls">
                    <input type="text" id="name" name="name" value="${entity.name}" class="form-control required"/>
                </div>
            </div>
            <div class="form-group">
                <label for="value" class="control-label">权限值:</label>
                <div class="controls">
                    <input type="text" id="value" name="value" value="${entity.value}" class="form-control required"/>
                </div>
            </div>
            <div class="form-group">
                <label for="description" class="control-label">详细描述:</label>
                <div class="controls">
                    <textarea id="description" name="description" class="form-control required">${entity.description}</textarea>
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
                    ,stringCheck: true
                    , rangelength: [2, 20]
                    , remote: {                               //验证权限名是否存在
                        type: "POST",
                        url: "${ctx}/sys/permission/checkName",
                        data: {
                            'oldName': '${entity.name}'
                            , 'name': function () {
                                return $("#name").val();
                            }
                        }
                    }
                },
                value: {
                    required: true
                    ,variable: true
                    , rangelength: [2, 50]
                    , remote: {                               //验证权限标识是否存在
                        type: "POST",
                        url: "${ctx}/sys/permission/checkValue",
                        data: {
                            'oldValue': '${entity.value}'
                            , 'value': function () {
                                return $("#value").val();
                            }
                        }
                    }
                }
            }
            , messages: {
                name: {
                    remote: "权限名已被其他角色使用."
                },
                value: {
                    remote: "权限标识已被其他角色使用."
                }
            }
        });
    })
</script>
</body>
</html>
