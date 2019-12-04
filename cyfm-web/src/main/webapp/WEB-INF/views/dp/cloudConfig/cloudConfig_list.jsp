<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title>采集实例配置</title>
    <style>
        div.items div.content-items{
            padding: 5px 0px 5px 10px;
        }
        div.items div.content-items ul {
            border: 1px solid #aaaaaa;
            padding: 5px;
        }
        div.items div.content-items ul li{
            border-bottom: 1px solid #aaaaaa;
            padding: 4px 0 4px 0;
        }
        div.items div.content-items ul li:last-child{
            border-bottom: 0;
        }
    </style>
</head>
<body>
<a class="btn btn-primary create" href="${ctx}/dp/cloudConfig/create"><i class="fa fa-plus"></i> 添加</a>
<div class="items">
    <c:forEach items="${configs}" var="config">
        <c:if test="${config.application eq 'datatask-executor'}">
        <div class="item">
            <h3>APPLICATION: ${config.application}-${config.profile}-${config.label} <small><button class="btn btn-xs red delete" data-application="${config.application}" data-profile="${config.profile}">删除</button></small></h3>
            <div class="content-items">
                <ul>
                    <c:forEach items="${fn:split(config.content,',')}" var="item">
                        <li>${item}</li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        </c:if>
    </c:forEach>
</div>

<script>
    $(function () {
        $("div.items button.delete").click(function () {
            var $btn = $(this);
            var postUrl = "${ctx}/dp/cloudConfig/delete";
            var application = $btn.data("application")
            var profile = $btn.data("profile")

            $cy.confirm({
                yes: function () {
                    $.post(postUrl, {"application": application, "profile": profile}, function (data) {
                        $btn.parents("div.item").remove();
                    })

                }
            })

        })

    });
</script>
</body>
</html>
