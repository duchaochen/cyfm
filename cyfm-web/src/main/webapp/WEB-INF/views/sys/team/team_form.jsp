<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<html>
<head>
    <title>团队详情</title>
    <style>
        .userList li{
            border-bottom: 1px solid #ccc;
            margin-top: 3px;
            height: 24px;
        }
    </style>
</head>
<body>

<div class="portlet box editBox">
    <div class="portlet-title"><span>团队信息</span></div>
    <div class="portlet-body form">
        <form:form id="inputForm" modelAttribute="entity" action="#" method="post">
            <div class="form-body">
                <input type="hidden" name="id" value="${entity.id}"/>
                <div id="messageBox" class="alert alert-error form-control controls" style="display:none">输入有误，请先更正。
                </div>
                <div class="form-group">
                    <label for="name" class="control-label">团队名称:</label>
                    <div class="controls">
                        <input type="text" id="name" name="name" value="${entity.name}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="master" class="control-label">管理人员:</label>
                    <div class="controls">
                        <input type="text" id="masterInfo" value="${entity.master.showName}"
                               class="form-control required" readonly/>
                        <input type="hidden" id="master" name="master" value="${entity.master.id}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">团队成员(注: 点击成员可移除):</label>
                    <div class="controls">
                        <div class="col-xs-4">
                            <ul class="userList">
                                <c:forEach items="${entity.userList}" var="item">
                                    <li><input type="hidden" name="userList.id" value="${item.id}">${item.showName}</li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="col-xs-2">
                            <button type="button" id="addMember" class="btn btn-sm blue"><i class="fa fa-plus"></i> 新增成员
                            </button>
                        </div>
                    </div>
                    <div class="clearfix"></div>
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
        $("#inputForm").validate({});

        $("#masterInfo").focus(function () {
            $cy.tools.chooseUser({
                callback: function (show, results) {
                    $("#masterInfo").val(show);
                    $("#master").val(results.data("id"));
                }
            })
        });

        $("#addMember").click(function () {
            $cy.tools.chooseUser({
                multi: true,
                callback: function (show, results) {
                    $(results).each(function (i, o) {
                        var dataId = $(o).data("id");
                        $("[name='userList.id'][value='" + dataId + "']").parent("li").remove();
                        $(".userList").append('<li><input type="hidden" name="userList.id" value="' + dataId + '">' + $(o).data("show") + '</li>');
                    });
                }
            })
        })

        $(".userList").on("click", "li", function () {
            let li = $(this);
            $cy.confirm({
                message: "是否移成员 "+li.text()+" ?",
                yes: function () {
                    li.remove();
                }
            })
        })
    })
</script>
</body>
</html>
