<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
	<!-- ================================= Css 区域 ========================================== -->
	<%@include file="/WEB-INF/views/common/import-css.jspf"%>
	<%@include file="/WEB-INF/views/common/import-zTree-css.jspf"%>
	<!-- ================================= JS 区域 ========================================== -->
	<%@include file="/WEB-INF/views/common/import-js.jspf"%>
	<title>资源管理-新增</title>
	<script>
		$(document).ready(function() {
			$("#entity-tab").addClass("active");
			$cy.handleUniform();
		});
	</script>
</head>
<body>
<div class="tabbable-line">
	<!-- form -->
	<!-- BEGIN SAMPLE FORM PORTLET-->
	<div class="portlet-body form">
		<form:form id="editForm" method="post" commandName="m"  role="form" enctype="multipart/form-data">
			<cy:showGlobalError commandName="m"/>

			<form:hidden path="id"/>
			<form:hidden path="parentId"/>
			<form:hidden path="parentIds"/>
			<form:hidden path="weight"/>
			<div class="form-body">
				<div class="form-group">
					<form:label path="name">名称</form:label>
					<div class="controls">
						<form:input path="name" cssClass="form-control required" placeholder="小于50个字符"/>
					</div>
				</div>
				<div class="form-group">
					<form:label path="identity">资源标识</form:label>
					<div class="controls">
						<form:input path="identity" cssClass="form-control" placeholder="用于权限验证"/>
					</div>
				</div>
				<div class="form-group">
					<form:label path="url">URL地址</form:label>
					<div class="controls">
						<form:input path="url" cssClass="form-control" placeholder="菜单跳转地址"/>
					</div>
				</div>
				<div class="form-group">
					<form:label path="resourceType">打开方式</form:label>
					<div>
					<div class="radio-list">
						<cyform:radiobuttons
								path="resourceType" items="${resourceTypes}" itemLabel="info" itemValue="value" cssClass="required"/>
					</div>
					</div>
				</div>
				<div class="form-group">
					<form:label path="icon">图标</form:label>
					<div class="controls">
						<form:input path="icon" cssClass="form-control"/><maintain:showIcon iconIdentity="${m.icon}"/>
					</div>
				</div>
				<div class="form-group">
					<form:label path="show">是否显示</form:label>
					<div>
						<div class="radio-list">
							<cyform:radiobuttons
									path="show" items="${booleanList}" itemLabel="info" itemValue="value" cssClass="required"/>
						</div>
					</div>
				</div>
				<div class="form-actions">
					<c:if test="${action eq '新增'}">
						<c:set var="icon" value="fa fa-file-o"/>
					</c:if>
					<c:if test="${action eq '修改'}">
						<c:set var="icon" value="icon-edit"/>
					</c:if>
					<c:if test="${action eq '删除'}">
						<c:set var="icon" value="icon-trash"/>
					</c:if>
					<button type="submit" class="btn btn-primary">
						<i class="${icon}"></i>
							${action}
					</button>
				</div>
			</div>
		</form:form>
	</div>
	<!-- END SAMPLE FORM PORTLET-->
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
