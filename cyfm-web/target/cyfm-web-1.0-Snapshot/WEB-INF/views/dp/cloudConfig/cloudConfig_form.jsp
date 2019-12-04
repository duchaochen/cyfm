<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title>cloud实例详情</title>
</head>
<body>
    <div class="portlet box editBox">
        <div class="portlet-title"><span>cloud实例</span></div>
        <div class="portlet-body form">
            <form:form id="inputForm" modelAttribute="entity" action="#" method="post">
            <div class="form-body">
                <input type="hidden" name="id" value="${entity.id}"/>
                <div class="form-group">
                    <label for="application" class="control-label">APPLICATION:</label>
                    <div class="controls">
                        <input type="text" id="application" name="application" value="datatask" class="form-control required" readonly="readonly"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="profile" class="control-label">profile:</label>
                    <div class="controls">
                        <input type="text" id="profile" name="profile" value="" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="profile" class="control-label">可用数据源:</label>
                    <div class="controls">
                        <select class="form-control required" name="sourceManageId">
                            <option value="">请选择</option>
                            <c:forEach items="${datasources}" var="item">
                                <option value="${item.id}">${item.dsName}</option>
                            </c:forEach>
                        </select>

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
