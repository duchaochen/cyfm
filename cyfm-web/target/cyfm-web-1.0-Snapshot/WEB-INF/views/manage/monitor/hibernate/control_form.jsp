<%@ page import="org.hibernate.stat.Statistics" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<html>
<head>
    <title>缓存控制</title>
</head>
<body>
<div data-table="table" class="tabbable-line">
    <c:set var="type" value="invalidate"/>
    <%@include file="nav.jspf" %>
    <div class="tab-content">
        <%
            Statistics statistics = (Statistics) request.getAttribute("statistics");
            String[] entityNames = statistics.getEntityNames();
            String[] collectionRoleNames = statistics.getCollectionRoleNames();
            String[] queries = statistics.getQueries();
            pageContext.setAttribute("entityNames", entityNames);
            pageContext.setAttribute("collectionRoleNames", collectionRoleNames);
            pageContext.setAttribute("queries", queries);

        %>
        <div class="portlet box editBox">
            <div class="portlet-title"><span>全局操作</span></div>
            <div class="portlet-body form">
                    <div class="form-body">
                    <div>
                        <a class="btn btn-evict-all btn-danger">失效整个二级缓存</a>
                        <a class="btn btn-clear-all btn-danger">清空二级缓存，重新计算</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="portlet box editBox">
            <div class="portlet-title"><span>失效实体缓存</span></div>
            <div class="portlet-body form">
                <form>
                    <div class="form-body">
                        <div class="form-group">
                            <cyform:label path="entityNames">实体缓存：</cyform:label>
                            <div class="controls">
                                <select id="entityNames" name="entityNames" multiple="true" class="form-control">
                                    <c:forEach items="${entityNames}" var="e">
                                        <option value="${e}" title="${e}">${e}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <cyform:label path="entityIds">实体编号：</cyform:label>
                            <div class="controls">
                                <cyform:input path="entityIds" cssClass="form-control" placeholder="多个之间，逗号分隔"/>
                            </div>
                        </div>
                        <div class="form-actions">
                            <a class="btn btn-evict-entity btn-primary">确定</a>
                            &nbsp;&nbsp;
                            <a class="btn btn-evict-entity-all btn-danger">失效所有实体缓存</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="portlet box editBox">
            <div class="portlet-title"><span>失效集合缓存</span></div>
            <div class="portlet-body form">
                <form>
                    <div class="form-body">
                        <div class="form-group">
                            <cyform:label path="entityNames">集合缓存：</cyform:label>
                            <div class="controls">
                                <select id="collectionRoleNames" name="collectionRoleNames"  cssClass="form-control" multiple="true">
                                    <c:forEach items="${collectionRoleNames}" var="e">
                                        <option value="${e}" title="${e}">${e}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div>
                            <div class="form-group">
                                <cyform:label path="collectionEntityIds">集合所属实体编号：</cyform:label>
                                <div class="controls">
                                    <cyform:input path="collectionEntityIds" cssClass="form-control" placeholder="多个之间，逗号分隔"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
                <div class="form-actions">
                    <a class="btn btn-evict-collection btn-primary">确定</a>
                    &nbsp;
                    <a class="btn btn-evict-collection-all btn-danger">失效所有集合缓存</a>
                </div>
            </div>
        </div>
        <div class="portlet box editBox">
            <div class="portlet-title"><span>失效查询缓存</span></div>
            <div class="portlet-body form">
                <form>
                    <div class="form-body">
                        <div class="form-group">
                            <cyform:label path="entityNames">查询缓存：</cyform:label>
                            <div class="controls">
                                <select id="queries" name="queries"  cssClass="form-control" multiple="true">
                                    <c:forEach items="${queries}" var="e">
                                        <option value="${e}" title="${e}">${e}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </form>
                <div class="form-actions">
                    <a class="btn btn-evict-query btn-primary">确定</a>
                    &nbsp;
                    <a class="btn btn-evict-query-all btn-danger">失效所有查询缓存</a>
                </div>

            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function() {
        $(".btn-evict-all").click(function() {
            $cy.confirm({
                title: "失效整个二级缓存",
                message: "确认失效整个二级缓存吗？",
                yes: function () {
                    var url = "${ctx}/manage/monitor/hibernate/evictAll";
                    $cy.waiting("正在执行..");
                    $.ajax({
                        url: url,
                        type: "GET",
                        dataType: "text",
                        success: function (data) {
                            $cy.waitingOver();
                            $cy.success(data)
                        }
                    });
                }
            });
        });
        $(".btn-clear-all").click(function() {
            $cy.confirm({
                title: "清空整个二级缓存",
                message: "确认清空整个二级缓存,重新计算吗？",
                yes: function () {
                    var url = "${ctx}/manage/monitor/hibernate/clearAll";
                    $cy.waiting("正在执行..");
                    $.ajax({
                        url: url,
                        type: "GET",
                        dataType: "text",
                        success: function (data) {
                            $cy.waitingOver();
                            $cy.success(data)
                        }
                    });
                }
            });
        });

        $(".btn-evict-entity-all").click(function() {
            $cy.confirm({
                title: "失效所有实体缓存",
                message: "确认失效所有实体缓存吗？",
                yes: function () {
                    var url = "${ctx}/manage/monitor/hibernate/evictEntity";
                    $cy.waiting("正在执行..");
                    $.ajax({
                        url: url,
                        type: "GET",
                        dataType: "text",
                        success: function (data) {
                           $cy.waitingOver();
                            $cy.success(data)
                        }
                    });
                }
            });
        });
        $(".btn-evict-entity").click(function() {
            if(!$("#entityNames").val()) {
                $cy.warn("未选择失效项,未进行任何失效操作.")
                return;
            }
            var form = $("#entityNames").closest("form");
            var url = "${ctx}/manage/monitor/hibernate/evictEntity?" + form.serialize();
            $cy.waiting("正在执行..");
            $.ajax({
                url: url,
                type: "GET",
                dataType: "text",
                success: function (data) {
                    $cy.success(data, function () {
                        $cy.waitingOver();
                    })
                }
            });
        });

        $(".btn-evict-collection-all").click(function() {
            $cy.confirm({
                title: "失效所有实体的集合缓存",
                message: "确认失效所有实体的集合缓存吗？",
                yes: function () {
                    var url = "${ctx}/manage/monitor/hibernate/evictCollection";
                    $cy.waiting("正在执行..");
                    $.ajax({
                        url: url,
                        type: "GET",
                        dataType: "text",
                        success: function (data) {
                            $cy.success(data, function () {
                                $cy.waitingOver();
                            })
                        }
                    });
                }
            });
        });
        $(".btn-evict-collection").click(function() {
            if(!$("#collectionRoleNames").val()) {
                $cy.warn("未选择失效项,未进行任何失效操作.")
                return;
            }
            var form = $("#collectionRoleNames").closest("form");
            var url = "${ctx}/manage/monitor/hibernate/evictCollection?" + form.serialize();
            $cy.waiting("正在执行..");
            $.ajax({
                url: url,
                type: "GET",
                dataType: "text",
                success: function (data) {
                    $cy.success(data, function () {
                        $cy.waitingOver();
                    })
                }
            });
        });

        $(".btn-evict-query-all").click(function() {
            $cy.confirm({
                title: "失效所有查询缓存",
                message: "确认失效所有查询缓存吗？",
                yes: function () {
                    var url = "${ctx}/manage/monitor/hibernate/evictQuery";
                    $cy.waiting("正在执行..");
                    $.ajax({
                        url: url,
                        type: "GET",
                        dataType: "text",
                        success: function (data) {
                            $cy.success(data, function () {
                                $cy.waitingOver();
                            })
                        }
                    });
                }
            });
        });
        $(".btn-evict-query").click(function() {
            if(!$("#queries").val()) {
                $cy.warn("未选择失效项,未进行任何失效操作.")
                return;
            }
            var form = $("#quries").closest("form");
            var url = "${ctx}/manage/monitor/hibernate/evictQuery?" + form.serialize();
            $cy.waiting("正在执行..");
            $.ajax({
                url: url,
                type: "GET",
                dataType: "text",
                success: function (data) {
                    $cy.success(data, function () {
                        $cy.waitingOver();
                    })
                }
            });
        });
    });
</script>
</body>
</html>
