<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<html>
<head>
    <style>
        .detail { word-break: break-all; }
    </style>
    <title>缓存详情</title>
</head>
<body>
<div data-table="table" class="tabbable-line">
    <%@include file="nav.jspf" %>
    <div class="tab-content">
        <a href="${ctx}/manage/monitor/ehcache" class="btn btn-link pull-right">返回</a>
        <a href="javascript:window.location.reload();" class="btn btn-link pull-right">刷新</a>
        <a class="btn btn-link pull-right btn-clear">清空</a>

        <table class="table table-bordered">
            <tbody>

            <tr class="bold info">
                <td colspan="2">
                    ${cacheName} 键列表
                    <cyform:input path="searchText" class="input-medium pull-right no-margin" placeholder="回车模糊查询"/>
                </td>
            </tr>

            <c:forEach items="${keys}" var="key">
                <tr>
                    <td style="width: 40%">${key}</td>
                    <td data-key="${key}">
                        <a class="btn btn-link no-padding btn-details">查看详细</a>
                        <a class="btn btn-link no-padding btn-delete">删除</a>
                    </td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $("#searchText").keyup(function (event) {
            var currentUrl = window.location.href;
            if (event.keyCode == 13) {
                window.location.href = currentUrl.substring(0,currentUrl.indexOf("?"))+"?searchText=" + this.value + "&BackURL=<cy:BackURL/>";
            }
        });

        $(".btn-details").click(function () {
            var td = $(this).closest("td");
            var key = td.data("key");
            var url = "${ctx}/manage/monitor/ehcache/${cacheName}/" + key + "/details";
            $.getJSON(url, function (data) {
                var detail = td.find(".detail");
                if (detail.length) {
                    detail.remove();
                }
                detail = $("<div class='detail alert alert-block alert-info in fade'><button type='button' class='close' data-dismiss='alert'>&times;</button></div>");

                var detailInfo = "";
                detailInfo += "命中次数:" + data.hitCount;
                detailInfo += " | ";
                detailInfo += "大小:" + data.size;
                detailInfo += " | ";
                detailInfo += "最后创建/更新时间:" + data.latestOfCreationAndUpdateTime;
                detailInfo += " | ";
                detailInfo += ",最后访问时间:" + data.lastAccessTime;
                detailInfo += " | ";
                detailInfo += "过期时间:" + data.expirationTime;
                detailInfo += " | ";
                detailInfo += "timeToIdle(秒):" + data.timeToIdle;
                detailInfo += " | ";
                detailInfo += "timeToLive(秒):" + data.timeToLive;
                detailInfo += " | ";
                detailInfo += "version:" + data.version;

                detailInfo += "值:" + data.objectValue;

                detail.append(detailInfo);
                td.append(detail);

                td.find(".alert").alert();

            });
        });

        $(".btn-delete").click(function () {

            var td = $(this).closest("td");
            var key = td.data("key");
            var url = "${ctx}/manage/monitor/ehcache/${cacheName}/" + key + "/delete";
            $.ajax({
                url: url,
                type: "GET",
                dataType: "text",
                success: function (data) {
                    td.closest("tr").remove();
                    $cy.success(data)
                }
            });
        });

        $(".btn-clear").click(function () {
            $cy.confirm({
                title: "确认清空缓存",
                message: "确认清空整个缓存吗？",
                yes: function () {
                    $cy.waiting("正在执行..");
                    var url = "${ctx}/manage/monitor/ehcache/${cacheName}/clear";
                    $.ajax({
                        url: url,
                        type: "GET",
                        dataType: "text",
                        success: function (data) {
                            $cy.success(data, function () {
                                $cy.refreshPage();
                            })
                        }
                    });
                }
            });
        });
    });
</script>
</body>
</html>

