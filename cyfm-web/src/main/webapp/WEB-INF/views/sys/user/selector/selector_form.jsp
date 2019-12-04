<%--
  Created by IntelliJ IDEA.
  User: weep
  Date: 2017/10/25
  Time: 下午11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<html>
<head>
    <title>系统用户选择器</title>
</head>
<body>
^^ 选择系统用户
<table id="content">
    <tbody>
    </tbody>
</table>
<script>

    var config = {key: "id", columns: ["name", "username", "email"]};
    $(function () {
        $.post("${ctx}/sys/user/selector/loadData", function (data) {
            console.log(data);
            $("#content tbody").append($("<tr><td><td></tr>").text(JSON.stringify(data)));
        })
    });
</script>
</body>
</html>
