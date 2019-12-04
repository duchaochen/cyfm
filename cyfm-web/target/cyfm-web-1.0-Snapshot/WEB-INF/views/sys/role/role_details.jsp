<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<html>
<head>
    <title>角色授权实体管理</title>
    <style>
        .editBox {
            height: 530px;
        }

        div.action {
            position: fixed;
            top: 495px;
            right: 15px;
        }

        div.form-body {
            height: 440px;
        }

        ul.list {
            height: 420px;
            overflow: auto;
        }

        ul.list li {
            width: 50%;
            overflow: auto;
            float: left;
            padding: 5px;
            border-right: 1px solid #ccc;
            border-bottom: 1px solid #ccc;
        }

        ul.list li:nth-of-type(odd) {
            border-left: 1px solid #ccc;
        }

        ul.list li:first-child, li:nth-child(2) {
            border-top: 1px solid #ccc;
        }
    </style>
</head>
<body>
<div class="portlet box tabbable-line editBox">
    <ul class="nav nav-tabs">
        <li class="active">
            <a href="javascript:void(0);">
                <i class="icon-user"></i>
                用户授权
            </a>
        </li>
        <%--
        <li>
            <a href="javascript:void(0);">
                <i class="icon-users"></i>
                部门授权
            </a>
        </li>
        --%>
    </ul>
    <div class="portlet-body form">
        <div class="form-body">
            <div class="list-wrap">
                <ul class="list">
                    <c:forEach items="${users}" var="u">
                        <li data-id="${u.id}"><h4>${u.name}[${u.username}] <small class="pull-right"><a
                                href="javascript:void(0);" onclick="removeUser('${u.id}',this)">点击移除</a></small></h4>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="action pull-right">
                <button class="btn blue" onclick="addUsers()">添加</button>
                <button class="btn btn-default" onclick="parent.layer.close(parent.layer.getFrameIndex(window.name))">
                    关闭
                </button>
            </div>
        </div>
    </div>
</div>
<script>
    function removeUser(userId, t) {
        $cy.confirm({
            message: "是否确认移除该用户授权？",
            yes: function () {
                $.post("${ctx}/sys/role/roleAllot/remove/user", {"roleId": ${role.id}, "targetId": userId}, function (result) {
                    if (result == 'success') {
                        $(t).parents('li').remove();
                    }
                }, "text");
            },
            no: $.noop
        })

    }

    function addUsers() {
        $cy.tools.chooseUser({
            multi: true,
            callback: function (show, results) {
                var $tul = $("<ul></ul>");
                var existsUser = "";
                var addInfo = "";
                var userIds = [];
                $(results).each(function (i, o) {
                    $o = $(o);
                    if ($('[data-id="' + o.value + '"]').size() > 0) {
                        existsUser += $o.data('name') + "[" + $o.data('username') + "]，"
                        return true;
                    }
                    addInfo += $o.data('name') + "[" + $o.data('username') + "]，"
                    userIds.push(o.value);
                    $tul.append(
                        "<li data-id=\"" + o.value + "\"><h4>"
                        + $o.data('name') + "[" + $o.data('username') + "]"
                        + "<small class=\"pull-right\"><a href=\"javascript:void(0);\" onclick=\"removeUser('" + o.value + "',this)\">点击移除</a></small></h4></li>"
                    );
                });

                $.post("${ctx}/sys/role/roleAllot/add/user", {"roleId": ${role.id}, "targetIds": userIds.join(",")}, function (result) {
                    if (result == 'success') {
                        $("ul.list").append($tul.html());
                    }
                }, "text");
                var message = ""
                if (existsUser != "") {
                    message += existsUser + "等用户已存在该角色中，所以被忽略。<br>"
                }
                if (addInfo != "") {
                    message += addInfo + "等用户被添加到该角色中。"
                }
                $cy.info(message);
            }
        })
    }
</script>
</body>
</html>
