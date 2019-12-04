<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title>用户详情</title>
</head>

<body>

<div class="portlet box editBox">
	<div class="portlet-title"><span>用户信息</span></div>
	<div class="portlet-body form">
		<form:form id="inputForm"  modelAttribute="entity" action="#" method="post">
			<div class="form-body">
				<input type="hidden" name="id" value="${entity.id}"/>
				<div class="form-group">
					<label for="username" class="control-label">登录名:</label>
					<div class="controls">
						<input type="text" id="username" name="username" value="${entity.username}" class="form-control required" ${not empty entity.id ? 'readonly=true' : ''} />
					</div>
				</div>
				<div class="form-group">
					<label for="name" class="control-label">用户名:</label>
					<div class="controls">
						<input type="text" id="name" name="name" value="${entity.name}" class="form-control required" autocomplete="off" />
					</div>
				</div>
				<div class="form-group">
					<label for="plainPassword" class="control-label">密码:</label>
					<div class="controls">
						<input type="password" id="plainPassword" name="plainPassword" class="form-control" placeholder="...Leave it blank if no change" autocomplete="off"/>
					</div>
				</div>
				<div class="form-group">
					<label for="email" class="control-label">邮箱:</label>
					<div class="controls">
						<input type="text" id="email" name="email" value="${entity.email}" class="form-control required email"/>
					</div>
				</div>
				<div class="form-group">
					<label for="tel" class="control-label">手机:</label>
					<div class="controls">
						<input type="text" id="tel" name="tel" value="${entity.tel}" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label for="roleList" class="control-label">角色:</label>
					<div class="controls">
						<form:bscheckboxes path="roleList" items="${allRoles}" itemLabel="name" itemValue="id" cssClass="required"/>
						<script>
							<c:forEach items="${entity.roleList}" var="item">
								$("[name=roleList][value=${item.id}]").prop("checked", true);
							</c:forEach>
						</script>
					</div>
				</div>
				<div class="form-group">
					<label for="status" class="control-label">状态:</label>
					<div class="controls">
						<form:radiobuttons path="status" items="${allStatus}" itemLabel="info" labelCssClass="inline" cssClass="required"/>
					</div>
				</div>
				<div class="form-actions">
					<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;
					<p class="help-block">(点击提交保存信息.)</p>
					<%--<p class="help-block">(保存后将发送JMS消息通知改动，而消息接收者将发送提醒邮件)</p>--%>
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
                username: {
                    required: true
                    , stringCheck: true
                    , rangelength: [2, 20]
                    , remote: {//后台验证唯一性
                        type: "POST",
                        url: "${ctx}/sys/user/checkUsername",
                        data: {
                            'oldUsername': '${entity.username}'
                            , 'username': function () {
                                return $("#username").val();
                            }
                        }
                    }
                },
                name: {
                    required: true
                    , stringCheck: true
                    , rangelength: [2, 20]
                }
                , tel: {
                    mobile: true,
                    remote: {//后台验证唯一性
                        type: "POST",
                        url: "${ctx}/sys/user/checkTel",
                        data: {
                            'oldTel': '${entity.tel}'
                            , 'tel': function () {
                                return $("#tel").val();
                            }
                        }
                    }
                }
                , email: {
                    remote: {//后台验证唯一性
                        type: "POST",
                        url: "${ctx}/sys/user/checkEmail",
                        data: {
                            'oldEmail': '${entity.email}'
                            , 'email': function () {
                                return $("#email").val();
                            }
                        }
                    }
                }
            }
            , messages: {
                username: {
                    remote: "用户名已被其他用户使用."
                }
                , email: {
                    remote: "邮箱已被其他用户使用."
                }
                , tel: {
                    remote: "手机号码已经被其他用户使用."
                }
            }
        });
    })
</script>
</body>
</html>
