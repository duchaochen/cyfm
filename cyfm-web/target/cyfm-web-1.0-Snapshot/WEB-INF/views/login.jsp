<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<script>
    if (top != window) {
        top.location.href = "${ctx}/";
    }
</script>
<%
    if (SecurityUtils.getSubject().isAuthenticated()) {
        if ("".equals(request.getContextPath())) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect(request.getContextPath());
        }
    }
%>
<%
    String error = null;

    Object message = request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
    if(message instanceof Exception){
        error = ((Exception) message).getMessage();
        request.setAttribute("error", error);
    } else {
        request.setAttribute("message", message);
    }

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>       <meta charset="utf-8"/>
    <title>登录页面</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <%--<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css"/>--%>
    <link href="${ctx}/static/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN PAGE LEVEL STYLES -->
    <link href="${ctx}/static/manage/css/login-soft.css?1" rel="stylesheet" type="text/css"/>
    <!-- END PAGE LEVEL SCRIPTS -->
    <!-- BEGIN THEME STYLES -->
    <link href="${ctx}/static/common/styles/components.css" id="style_components" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/common/styles/plugins.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/manage/css/layout.css?2" rel="stylesheet" type="text/css"/>
    <link id="style_color" href="${ctx}/static/manage/css/darkblue.css?2" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/common/styles/custom.css?6" rel="stylesheet" type="text/css"/>
    <!-- END THEME STYLES -->
    <link rel="shortcut icon" href="${ctx}/static/images/favicon.ico"/>

    <style>
        .loginbox-body {
            width: 320px;
            margin-left: 285px;
            margin-top: 95px;
        }
        .login-form .form-actions .checkbox {
            margin-top: 8px;
            display: inline-block;
        }
        .login-form .form-actions .checkbox {
            margin-left: 0;
            padding-left: 0;
        }
        .login-form .alert.alert-danger{
            margin-bottom: 5px;
        }

        .login-form .input-icon i {
            padding-top: 6px;

        }
    </style>
    <script>
        ctx = _ctx = "${ctx}";
    </script>
    <script src="${ctx}/static/plugins/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
</head>
<body class="login-page">
<div id="mainBody">
    <div id="cloud1" class="cloud"></div>
    <div id="cloud2" class="cloud"></div>
</div>
<div class="logintop">
    <span>欢迎登陆${cy_systemName}</span>
    <ul>
        <li><a href="${ctx}/">回首页</a></li>
        <li><a href="#">帮助</a></li>
        <li><a href="#">关于</a></li>
    </ul>
</div>
<div class="loginbody">
    <span class="systemlogo display-hide"></span>
    <div class="loginbox display-hide">
        <div class="loginbox-body">
            <form class="login-form" action="${ctx}/login" method="post" autocomplete="off">
                <cy:showMessage/>
                <c:if test="${not empty error || not empty message}">
                    <script>
                        $(function () {
                            $(".loginbox-body").css({'margin-top':'78px'});
                        })
                    </script>
                </c:if>
                <c:if test="${empty error || empty message}">
                    <div class="alert alert-error alert-danger valid-alert display-hide">
                        <button type="button" class="close">&times;</button>
                        <span class="icon-remove-sign icon-large"></span>&nbsp;请输入正确的用户名和密码
                    </div>
                </c:if>
                <div class="form-group">
                    <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
                    <label class="control-label visible-ie8 visible-ie9">用户名</label>
                    <div class="input-icon">
                        <i class="fa fa-user"></i>
                        <input class="form-control placeholder-no-fix loginuser required" type="text" autocomplete="off" placeholder="账号/Email/手机号" id="username" name="username" disabled/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label visible-ie8 visible-ie9">密码</label>
                    <div class="input-icon">
                        <i class="fa fa-lock"></i>
                        <input class="form-control placeholder-no-fix loginpwd required" type="password" autocomplete="off" placeholder="登录密码" id="password" name="password" disabled/>
                    </div>
                </div>
                <div class="form-actions">
                    <label class="checkbox">
                        <input type="checkbox" name="rememberMe" value="1"/> 记住登录 </label>
                    <button type="submit" class="btn blue pull-right">
                        登录 <i class="m-icon-swapright m-icon-white"></i>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="loginbm">版权所有 2018 <a href="http://www.ppcxy.com">www.ppcxy.com</a> 欢迎反馈使用中遇到的问题.</div>
<!-- END COPYRIGHT -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="${ctx}/static/plugins/respond.min.js"></script>
<script src="${ctx}/static/plugins/excanvas.min.js"></script>
<![endif]-->

<script src="${ctx}/static/plugins/jquery/jquery-migrate.min.js" type="text/javascript"></script>
<script src="${ctx}/static/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctx}/static/plugins/jquery.blockUI.js" type="text/javascript"></script>
<script src="${ctx}/static/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<script src="${ctx}/static/plugins/jquery.cokie.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${ctx}/static/plugins/jquery-validation/1.15.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctx}/static/plugins/backstretch/jquery.backstretch.min.js" type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${ctx}/static/manage/js/login-soft.js?1" type="text/javascript"></script>

<script src="${ctx}/static/manage/js/cloud.js?1" type="text/javascript"></script>
<script language="javascript">
    $(function () {
        $(".valid-alert .close").click(function () {
            $(this).parent(".valid-alert").hide();
        });
        $(".alert .close").click(function () {
            $(".loginbox-body").css({'margin-top': '95px'});
        });
        $('.loginbox').css({'position': 'absolute', 'left': ($(window).width() - 692) / 2});
        $(window).resize(function () {
            $('.loginbox').css({'position': 'absolute', 'left': ($(window).width() - 692) / 2});
        });
        $("[name=rememberMe]").uniform();
        setTimeout(function(){
            $('.loginbox,.systemlogo').fadeIn({duration: 1000});
            $(":input").attr("disabled", false).eq(0).focus();
            Login.init();
            $("#username").focus();
        },300)

    });
</script>
</body>

</html>

