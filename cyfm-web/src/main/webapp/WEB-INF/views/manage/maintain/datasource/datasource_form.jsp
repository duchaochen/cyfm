<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
  <title>数据源详情</title>
</head>
<body>

<div class="portlet box editBox">
    <div class="portlet-title"><span>数据源信息</span></div>
    <div class="portlet-body form">
        <form:form id="inputForm"  modelAttribute="entity" method="post">
        <div class="form-body">
            <input type="hidden" name="id" value="${entity.id}"/>
            <div class="form-group">
                <label for="dsName" class="control-label">数据源名称:</label>
                <div class="controls">
                    <input type="text" id="dsName" name="dsName" value="${entity.dsName}" class="form-control required" placeholder="数据源名称"/>
                </div>
            </div>
            <div class="form-group">
                <form:label path="dsType" cssClass="control-label">数据源类型</form:label>
                <div class="controls">
                    <form:select path="dsType" cssClass="form-control required">
                        <form:option label="请选择" value=""/>
                        <form:options items="${dsTypes}"  itemLabel="info"></form:options>
                    </form:select>
                </div>
            </div>
            <div class="form-group">
                <label name="dbName">库名</label>
                <div class="controls">
                    <input id="dbName" name="dbName" class="form-control required ajaxCallName" value="${entity.dbName}" placeholder="数据库名称"/>
                </div>
            </div>
            <div class="form-group">
                <form:label path="dbType" cssClass="control-label">库类型</form:label>
                <div class="controls">
                    <form:select path="dbType" cssClass="form-control required">
                        <form:option label="请选择" value=""/>
                        <form:options items="${dbTypes}" itemLabel="name"></form:options>
                    </form:select>
                </div>
            </div>
            <div class="form-group">
                <label name="dbHost">库IP</label>
                <div class="controls">
                    <input id="dbHost" name="dbHost" class="form-control required" value="${entity.dbHost}" placeholder="数据库IP"/>
                </div>
            </div>
            <div class="form-group">
                <label name="dbPort">库端口</label>
                <div class="controls">
                    <input id="dbPort" name="dbPort" class="form-control required" value="${entity.dbPort}" placeholder="数据库端口"/>
                </div>
            </div>
            <div class="form-group">
                <label name="dbUsername">库用户名</label>
                <div class="controls">
                    <input id="dbUsername" name="dbUsername" class="form-control required" value="${entity.dbUsername}" placeholder="数据库用户名"/>
                </div>
            </div>
            <div class="form-group">
                <label name="dbPassword">库密码</label>
                <div class="controls">
                    <input id="dbPassword" name="dbPassword" class="form-control required" value="${entity.dbPassword}" placeholder="数据库密码"/>
                </div>
            </div>

            <div class="form-actions">
                <a id="validate_datasource_btn" class="btn green">校验</a>
                <input id="submit_btn" class="btn btn-primary disabled" type="submit" value="提交"/>&nbsp;
                <p class="help-block">(点击提交保存信息.)</p>
            </div>
        </div>
        </form:form>
    </div>
</div>
<script>
    $(function () {
        $cy.handleUniform();
        $("#inputForm").validate({});

        $("#validate_datasource_btn").click(function () {
            $cy.waiting("",60000);

            var dbName = $("#dbName").val();
            var dbType = $("#dbType").val();
            var dbHost = $("#dbHost").val();
            var dbPort = $("#dbPort").val();
            var dbUsername = $("#dbUsername").val();
            var dbPassword = $("#dbPassword").val();

            var ajaxParam = {};
            ajaxParam.dbName = dbName;
            ajaxParam.dbType = dbType;
            ajaxParam.dbHost = dbHost;
            ajaxParam.dbPort = dbPort;
            ajaxParam.dbUsername = dbUsername;
            ajaxParam.dbPassword = dbPassword;

            $.ajax({
                type: "post",
                dataType: "json",
                url: "${ctx}/manage/maintain/datasource/validate",
                data: ajaxParam,
                cache: false,
                success: function (data) {
                    
                    if (data) {
                        $cy.confirm({
                            title: "校验结果",
                            message: "校验成功,是否保存配置？",
                            cancelTitle: '取消',
                            okTitle: '确定',
                            yes: function () {
                                $cy.waitingOver();
                                $("#inputForm").submit();
                            },
                            no: function () {
                                $("#inputForm").find(":input").attr("readonly", true);
                                $("#submit_btn").removeClass("disabled");
                                $cy.waitingOver();
                            }
                            ,
                            alert: false
                        })
                    } else {
                        $cy.error("校验失败,未能连接到数据库,请检查配置.",function (index, layero) {
                            $cy.waitingOver();
                            top.layer.close(index);
                        })
                    }

                },
                error:function () {
                    $cy.error("校验过程发生错误,请重新尝试.",function (index, layero) {
                        $cy.waitingOver();
                        top.layer.close(index);
                    })
                }
            });

            return false;
        });
    });
</script>
</body>
</html>
