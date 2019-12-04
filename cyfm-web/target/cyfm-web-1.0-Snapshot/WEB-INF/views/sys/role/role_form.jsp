<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<html>
<head>
    <%@include file="/WEB-INF/views/common/import-zTree-css.jspf"%>
    <title>角色详情</title>
</head>
<body>
<div class="portlet box editBox">
    <div class="portlet-title"><span>角色信息</span></div>
    <div class="portlet-body form">
        <form:form id="inputForm" modelAttribute="entity" action="#" method="post">
            <div class="form-body">
                <input type="hidden" name="id" value="${entity.id}"/>
                <div class="form-group">
                    <label for="name" class="control-label">角色名称:</label>
                    <div class="controls">
                        <input type="text" id="name" name="name" value="${entity.name}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="value" class="control-label">角色标识:</label>
                    <div class="controls">
                        <input type="text" id="value" name="value" value="${entity.value}" class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="description" class="control-label">角色描述:</label>
                    <div class="controls">
                        <textarea id="description" name="description" class="form-control">${entity.description}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label for="permissions" class="control-label">非资源权限设定:</label>
                    <div class="controls">
                        <textarea id="permissions" name="permissions" class="form-control required">${entity.permissions}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <div id="selectedResourcePermssion">
                        <label for="selectedResourcePermssion" class="control-label">资源权限设定:</label>
                        <table class="table table-bordered table-hover">
                            <thead>
                            <tr>
                                <th>资源</th>
                                <th>权限</th>
                                <th style="width: 50px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${entity.roleResourcePermissions}" var="o">
                                <c:if test="${cyfn:existsResource(o.resourceId, onlyDisplayShow)}">
                                    <tr>
                                        <td>
                                            <input type='hidden' id='resourceId_${o.resourceId}' name='resourceId' value='${o.resourceId}'>
                                            <sys:showResourceName id="${o.resourceId}"/>
                                        </td>
                                        <td>
                                    <span style="line-height: 30px">
                                    <c:set var="permissionIds" value=""/>
                                    <c:set var="count" value="0"/>
                                    <c:forEach items="${o.permissionIds}" var="permissionId">
                                        <c:if test="${cyfn:existsPermission(permissionId, onlyDisplayShow)}">
                                            <c:set var="count" value="${count+1}"/>
                                            <c:set var="permissionIds" value="${permissionIds}${count == 1 ? '' : ','}${permissionId}"/>
                                            <c:if test="${status.count > 1}">|</c:if>
                                            <sys:showPermissionName id="${permissionId}"/>
                                        </c:if>
                                    </c:forEach>
                                    </span>
                                            <input type='hidden' name='permissionIds' value='${permissionIds}'>
                                        </td>
                                        <td>
                                            <a class='btn btn-link btn-edit btn-delete-resource-permission' href='javascript:;'
                                               onclick='removeResourcePermission(this);'>
                                                <i class='icon-trash'></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="clearfix"></div>
                <div class="form-group">
                    <div class="border">
                        <div id="resourceInfo" class="col-xs-9">
                            <label>资源</label>&nbsp;

                            <div class="input-group" title="选择资源选择资源">
                                <input type="hidden" id="resourceId" value="">
                                <input type="text" id="resourceName" class="form-control" value="" readonly="readonly">
                                <div class="input-group-addon" >

                                    <a id="selectResourceTree" href="javascript:;">
                                        <span class="add-on"><i class="fa fa-chevron-down"></i></span>
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div class="clearfix"></div>
                        <div id="permissionInfo" class="col-xs-9">
                            <div class="col-xs-3">
                                <label>权限</label>&nbsp;
                                <select id="permissionList" multiple="multiple" class="form-control" size="8">
                                    <c:forEach items="${permissionList}" var="p">
                                        <option value="${p.id}" title="${p.description}">${p.name}&nbsp;&nbsp;(${p.value})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div id="appendResourcePermissionBtn" class="col-xs-2">
                                <br><br>
                                <a class="btn btn-warning btn-add-resource-permission">
                                    <i class="fa fa-file-o"></i>
                                    添加
                                </a>
                            </div>
                            <div class="muted font-12" style="margin: 10px auto;float: left;width: 100%;">
                                注意：添加的数据是临时的，还需要点击页面下方的[提交]保存该数据
                            </div>
                        </div>
                        <div class="clearfix"></div>
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
<%@include file="/WEB-INF/views/common/import-zTree-js.jspf"%>
<script>
    $(function () {
        $cy.handleUniform();
        $("#inputForm").validate({
            rules: {
                name: {
                    required: true
                    ,stringCheck: true
                    , rangelength: [2, 20]
                    , remote: {                               //验证角色名是否存在
                        type: "POST",
                        url: "${ctx}/sys/role/checkName",
                        data: {
                            'oldName': '${entity.name}'
                            , 'name': function () {
                                return $("#name").val();
                            }
                        }
                    }
                },
                value: {
                    required: true
                    ,variable: true
                    , rangelength: [2, 50]
                    , remote: {                               //验证角色标识是否存在
                        type: "POST",
                        url: "${ctx}/sys/role/checkValue",
                        data: {
                            'oldValue': '${entity.value}'
                            , 'value': function () {
                                return $("#value").val();
                            }
                        }
                    }
                }
            }
            , messages: {
                name: {
                    remote: "角色名已被其他角色使用."
                },
                value: {
                    remote: "角色标识已被其他角色使用."
                }
            }
        });

        var resourceTreeId = $.zTree.initSelectTree({
            zNodes : [],
            nodeType : "default",
            fullName:true,
            urlPrefix : "${ctx}/sys/resource",
            async : true,
            asyncLoadAll : true,
            onlyDisplayShow: true,
            lazy : true,
            select : {
                btn : $("#selectResourceTree,#resourceName"),
                id : "resourceId",
                name : "resourceName",
                includeRoot: false
            },
            autocomplete : {
                enable : true
            },
            setting :{
                check : {
                    enable:true,
                    chkboxType: {"Y":"","N":""},
                    chkStyle:"checkbox",
                    onlyCheckLeaf : false,
                    onlySelectLeaf : false
                }
            }
        });

        $(".btn-add-resource-permission").click(function() {

            var resourceIds = $("#resourceId").val().split(",");
            var resourceNames = $("#resourceName").val().split(",");
            var $selectedPermissions = $("#permissionList option:selected");
            var permissionIds = $selectedPermissions.map(function() {return this.value;}).get().join(",");
            var permissionNames = $selectedPermissions.map(function() {return this.innerText;}).get().join(",");

            if(!resourceIds || !resourceIds.length || !resourceIds[0]) {
                $cy.warn("请选择资源");
                return;
            }
            for(var index=0;index<resourceIds.length;index++){
                var resourceId = resourceIds[index];
                if(!resourceId) {
                    continue;
                }
                if($("#resourceId_" + resourceId).size() > 0) {
                    $cy.warn("您已经选择了此资源，不能重复选择！");
                    return;
                }
            }

            if(!permissionIds) {
                $cy.warn("请选择权限");
                return;
            }

            var template =
                "<tr>" +
                "<td>" +
                "<input type='hidden' id='resourceId_{resourceId}' name='resourceId' value='{resourceId}'>" +
                "<a class='btn btn-default btn-link no-padding'>{resourceName}</a>" +
                "</td>" +
                "<td>" +
                "<input type='hidden' name='permissionIds' value='{permissionIds}'>" +
                "{permissionNames}" +
                "</td>" +
                "<td>" +
                "<a class='btn btn-link btn-edit btn-delete-resource-permission' href='javascript:;' onclick='removeResourcePermission(this);'><i class='icon-trash'></i></a>" +
                "</td>" +
                "</tr>";

            for(var index=0;index<resourceIds.length;index++){
                var resourceId = resourceIds[index];
                var resourceName = resourceNames[index];
                if(!resourceId) {
                    continue;
                }

                $("#selectedResourcePermssion .table tbody").append(
                    template.replace(/{resourceId}/g, resourceId)
                        .replace("{resourceName}", resourceName.replace(",", "<br/>").replace(">", "&gt;"))
                        .replace("{permissionIds}", permissionIds)
                        .replace("{permissionNames}", permissionNames)
                );
                // TODO 更新表格的全选反选
                // $.table.initCheckbox($("#selectedResourcePermssion table"));


            };

            $.fn.zTree.getZTreeObj(resourceTreeId).checkAllNodes(false);
            $("#resourceId,#resourceName").val("");
            $selectedPermissions.attr("selected", false);
        });
    })

    //删除资源权限授权表格行
    function removeResourcePermission(a) {
        $cy.confirm({
            message : "确认删除吗？",
            yes : function() {
                $(a).closest('tr').remove();
            }
        });

    }
</script>
</body>
</html>
