<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<head>
    <title>${cy_systemName}</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <meta http-equiv="Cache-Control" content="no-store" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta name="meta.decorator" content="content" />
    <meta name="decorator" content="content" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <%@include file="/WEB-INF/views/common/import-css.jspf" %>
    <style>
        body{
            /*overflow: hidden;*/
        }


        div.mobile-main a {
            text-decoration: none;
        }

        div.mobile-main .nav li {
            float: left;
            width: 25%;
            height: 120px;
            text-align: center;
        }

        div.mobile-main .nav li a {
            padding-top: 10px;
            display: block;
            width: 87px;
            height: 88px;
            -moz-transition: none;
            transition: background-color 0.3s linear;
            -moz-transition: background-color 0.3s linear;
            -webkit-transition: background-color 0.3s linear;
            -o-transition: background-color 0.3s linear;
        }

        div.mobile-main .nav li a:hover {
            display: block;
            background: #000;
            color: #fff;
            background: none repeat scroll 0% 0% rgb(43, 127, 181);
        }

        div.mobile-main .nav li a {
            display: block;
        }

        div.mobile-main .nav a h2 {
            font-size: 14px;
            color: #0c04f1;
        }

        div.mobile-main .nav a:hover h2 {
            color: #ff0000;
        }

        div.mobile-main div.top-title {
            height: 40px;
            padding: 15px 0 0 40px;
            font-size: 28px;
            color: #fff9ec;
        }

        div.mobile-main .nav a div.root-icon {
            padding-bottom: 5px;
            font-size: 25px;
            width: 55px;
            height: 55px;
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            color: #f3f3f3;
        }

        div.mobile-main .nav a div.root-icon {
            padding: 8px;
            background: rgba(3, 1, 255, 0.81); /* 一些不支持背景渐变的浏览器 */
            background: -moz-linear-gradient(top, rgba(118, 172, 238, 1), rgba(109, 177, 179, 0.8));
            background: -o-linear-gradient(top, rgba(118, 172, 238, 1), rgba(118, 172, 238, 0.8));
            background: -webkit-gradient(linear, 0 0, 0 bottom, from(rgba(118, 172, 238, 1)), to(rgba(118, 172, 238, 0.8)));
            border-radius: 9px !important;
        }

        div.mobile-header div.title{
            width: 100%;
            height: 50px;
            border-left: 0;
            border-right: 0;
            background-color: #7dc2fb;
            line-height: 50px;
            font-size: 25px;
            padding-left: 8px;
            color: #EEEEEE;
        }

        div.mobile-main{
            margin-bottom: 65px;

        }
        div.mobile-main .menu-group{
            padding-bottom: 15px;
        }
        div.mobile-main .title {
            width: 100%;
            height: 50px;
            border-left: 0;
            border-right: 0;
            background-color: #bff0fb;
            margin-bottom: 5px;
            line-height: 50px;
            font-size: 25px;
            padding-left: 8px;
        }
        div.mobile-main .title i{
            font-size: 28px;
        }
        div.mobile-main ul.nav li{
            padding-top: 15px;
        }
        div.footer-menu-bar{
            width: 100%;
            height: 60px;
            position: fixed;
            left: 0;
            bottom: 0px;
            background: #f8f8f8;
            border-top: 1px solid #ccc;
        }
        div.footer-menu-bar li {
            float: left;
            padding: 25px;
        }

        div.footer-menu-bar li .menu-icon{
            font-size: 35px;
        }

        ul.nav li a{
            background: transparent !important;
            broder-radius:10px;
        }

        ul.nav li a:link {
            text-decoration: none;
            color: transparent
        }

        ul.nav li a:active {
            text-decoration: blink
        }

        ul.nav li a:hover {
            text-decoration: underline;
            color: transparent;
        }

        ul.nav li a:hover .root-icon{
            background: rgba(8, 10, 255, 0.5);
        }

        ul.nav li a:visited {
            text-decoration: none;
            color: transparent
        }

    </style>
    <%@include file="/WEB-INF/views/common/import-js.jspf"%>
    <script>
        $cy.waiting();
        window.paceOptions = {
            ajax: {
                trackMethods: ['GET', 'POST']
            },
            eventLag: false,
            elements: false
        }


        function layoutManageMain(){
            var width = $(window).width();
            var height = $(window).height();

            $("div.content").css({"width":width-5,"height": height - 88 + "px"});
        }
        $(function () {
            $(".manage-main").css("display", "block");
            $cy.waitingOver();
            layoutManageMain();
            $(window).resize(function () {
                layoutManageMain();
            });
        })
    </script>
</head>
<body>
<div class="mobile-header">
    <div class="title">
        <img src="${ctx}/static/images/logo.png" height="45">
        ${cy_systemName}
    </div>
</div>
<div class="mobile-main">
    <%--${root}--%>
    <%--${roots}--%>
    <%--${menus}--%>

    <c:forEach items="${menus}" var="m">
        <dd class="menu-group">
            <div class="title">
                <i class="${m.icon}"></i> ${m.name}
            </div>
            <ul class="nav">
                <c:forEach items="${m.children}" var="c">
                    <cy:mobileSubmenu menu="${c}" parentName="${m.name}"/>
                </c:forEach>
            </ul>
            <div class="clearfix"></div>
        </dd>
        <div class="clearfix"></div>
    </c:forEach>
</div>
<div class="footer-menu-bar">
    <ul>
        <c:forEach items="${roots}" var="root" varStatus="s">
            <li><a href="${ctx}/desktop/${root.id}" class="bottom-menu"><i class="${root.icon} icon-large menu-icon"></i></a></li>
        </c:forEach>
    </ul>
</div>

<script>
    document.body.onselectstart = function () {
        return false;
    };
    document.body.onmousedown = function () {
        return false;
    };
</script>
</body>
</html>
