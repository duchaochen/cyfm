<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<head>
    <title>${cy_systemName}:<sitemesh:title/></title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <!-- ================================= Css 区域 ========================================== -->
    <%@include file="/WEB-INF/views/common/import-css.jspf" %>
    <style>
        .mobile-content .search-toolbar {
            display: none;
        }

        .btn-back {
            float: left;
            padding: 9px 10px;
            margin-top: 8px;
            margin-left: 8px;
            margin-bottom: 8px;
            background-color: transparent;
            background-image: none;
            border: 1px solid #ddd;
        }

        .btn-menu {
            margin-right: 8px;
        }

        .nav.navbar-nav {
            margin-right: 8px;
        }
        input,textarea,select {
            border: 0; /* 方法1 */
            -webkit-appearance: none; /* 方法2 */
        }

        .mobile-content {
            padding: 65px 5px 50px 5px;
        }
    </style>
    <!-- ================================= JS 区域 ========================================== -->
    <script>
        var $cy = {};
    </script>
    <%@include file="/WEB-INF/views/common/import-js.jspf" %>
    <script>
        $(function () {
            $(".btn-back").click(function () {
                window.location.href = "${param['BackURL']}" == "" ? "${ctx}/" : "${param['BackURL']}";
            })
        })
    </script>
    <sitemesh:head/>
</head>
<body>
<!--导航-->
<div class="mobile-page-header navbar navbar-default navbar-fixed-top hidden">
    <div class="container-full">
        <div class="navbar-header">
            <button type="button" class="btn btn-default btn-back " style="left:8px;top:10px">
                <i class="fa fa-angle-left icon-large"></i> 返回
            </button>

            <button type="button" class="btn-menu navbar-toggle" data-toggle="collapse" data-target="#navBar">
                <i class="fa fa-reorder icon-large"></i>
            </button>
        </div>
        <div class="collapse navbar-collapse navbar-right" id="navBar">
            <ul class="nav navbar-nav">
                <li><a href="${ctx}/">主页</a></li>
                <%--<li class="dropdown">--%>
                <%--<a href="#" class="dropdown-toggle" data-toggle="dropdown">--%>
                <%--产品中心<span class="caret"></span>--%>
                <%--</a>--%>
                <%--<ul class="dropdown-menu pull-right" role="menu">--%>
                <%--<li><a href="#">SmartCall智能呼</a></li>--%>
                <%--<li><a href="#">运营管理平台ＯＭＳ</a></li>--%>
                <%--<li><a href="#">客户关系管理系统</a></li>--%>
                <%--</ul>--%>

                <%--</li>--%>
            </ul>
        </div>
    </div>
</div>
<div class="clearfix"></div>
<div class="mobile-content">
    <c:if test="${not empty message or not empty errorMessage}">
        <cy:showMessage/>
    </c:if>
    <c:if test="${not empty page && not customToolbar}">
        <div class="tools">
            <cy:listToolBarActions/>
        </div>
    </c:if>
    <sitemesh:body/>
</div>
<!-- BEGIN FOOTER -->
<div class="page-footer">
    <div class="scroll-box">
        <div class="scroll">&nbsp;</div>
    </div>
    <div class="scroll-to-top">
        <i class="icon-arrow-up"></i>
    </div>
</div>
<!-- END FOOTER -->
<script>
    $cy.handleUniform();
    var index = parent.layer.getFrameIndex(window.name);
    if (top == parent && !index) {
        $cy.place.appendUrl(document.title.substring("${cy_systemName}".length + 1), urlPrefix, urlSuffix);
    }
    <cy:showFieldError commandName="entity"/>
</script>
<script>
    if (window === top) {
        $(".mobile-page-header").removeClass("hidden");
    }else{
        $(".mobile-content").css({"padding-top": "5px"});
    }
    var theadTop = 0;

    function updateTheadTop() {
        if ($("#contentTable thead").position()){
            theadTop = $("#contentTable thead").position().top-55
        }
    }

    $(function () {
        setTimeout(function(){
            var $thead = $("#contentTable thead");
            if ($thead.size() > 0) {
                theadTop = $thead.position().top-55;
                /**
                 * scroll handle
                 * @param {event} e -- scroll event
                 */
                function scrollHandle(e) {
                    var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
                    if (scrollTop > theadTop) {
                        $thead.find("th").css({
                            "transform": "translateY(" + (scrollTop - theadTop) + "px)"
                        });
                    } else if (scrollTop == 0) {
                        $thead.find("th").css({
                            "transform": "",
                        });
                    }
                }

                window.addEventListener('scroll', scrollHandle)
            }
        },100)
    });
</script>
</body>
</html>
