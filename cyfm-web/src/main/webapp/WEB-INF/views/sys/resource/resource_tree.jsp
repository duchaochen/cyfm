<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<head>
    <!-- ================================= Css 区域 ========================================== -->
    <%@include file="/WEB-INF/views/common/import-css.jspf"%>
    <%@include file="/WEB-INF/views/common/import-zTree-css.jspf"%>
    <!-- ================================= JS 区域 ========================================== -->
    <%@include file="/WEB-INF/views/common/import-js.jspf"%>
    <title>系统资源数</title>
</head>
<body>
<div data-table="table" class="tabbable-line">
    <ul class="nav nav-tabs">
        <li ${empty param['search.show_eq'] ? 'class="active"' : ''}>
            <a href="${ctx}/sys/resource/tree?async=${not empty param.async and param.async eq true}">
                <i class="fa fa-table"></i>
                所有
                <i class="icon-refresh" title="点击刷新"></i>
            </a>
        </li>
        <li ${not empty param['search.show_eq'] ? 'class="active"' : ''}>
            <a href="${ctx}/sys/resource/tree?search.show_eq=true&async=${not empty param.async and param.async eq true}">
                <i class="fa fa-table"></i>
                显示的
            </a>
        </li>
    </ul>
    <div class="tab-content">
        <div id="treeContainer"></div>
    </div>
</div>
<%@include file="/WEB-INF/views/common/import-zTree-js.jspf"%>
<script type="text/javascript">
    var async = ${not empty param.async and param.async eq true};
    $(function() {
        var zNodes =[
            <c:forEach items="${trees}" var="m">
            { id:${m.id}, pId:${m.pId}, name:"${m.name}", iconSkin:"${m.iconSkin}", open: true, root : ${m.root},isParent:${m.isParent}},
            </c:forEach>
        ];


        $.zTree.initMovableTree({
            zNodes : zNodes,
            urlPrefix : "${ctx}/sys/resource",
            async : async,
            onlyDisplayShow:${param['search.show_eq'] eq true},
            permission: <cy:treePermission resourceIdentity="resource"/>,
            autocomplete : {
                enable : true
            },
            setting : {
                callback : {
                    onClick: function(event, treeId, treeNode, clickFlag) {
                        parent.frames['listFrame'].location.href='${ctx}/sys/resource/' + treeNode.id + "/update?async=" + async ;
                    }
                }
            }
        });

    });
</script>
</body>
</html>
