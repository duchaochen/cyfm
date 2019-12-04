<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <%@include file="/WEB-INF/views/common/import-css.jspf" %>
<title>默认工作台</title>
<%@include file="/WEB-INF/views/common/import-js.jspf"%>
</head>
<body>
<div class="mainindex">
    <div class="pull-right"><span class="clock"></span></div>
    <div class="welinfo">
        <span><img src="${ctx}/static/manage/img/sun.png" alt="天气"/></span>
        <b style="color:blue;font-weight: 900">[<shiro:principal property="name"/>](<shiro:principal property="username"/>)</b> <b>早上好，欢迎使用${cy_systemName}</b>
        <a href="#">帐号设置</a>
    </div>

</div>
<script>
    if (top == parent){
        $cy.place.appendUrl(document.title.substring("${cy_systemName}".length+1), urlPrefix, urlSuffix);
    }

    function formatDate(date) {
        var d = date,
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear(),
            hours = d.getHours(),
            minutes = d.getMinutes(),
            seconds = d.getSeconds();

        //这就是判断当前如果是1位的话前面补个0 比如 01 02
        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;
        if (hours < 10) hours = '0' + hours;
        if (minutes < 10) minutes = '0' + minutes;
        if (seconds < 10) seconds = '0' + seconds;

        //返回拼的字符串 [xx,xx,xx].join('-') 就是用-连接 前面[]里的东西
        return [year, month, day].join('-') + " " + [hours, minutes, seconds].join(':');
    }

    function showTime() {
        //先显示当前时间 new Date()  是获取当前时间对象

        $(".clock").text(formatDate(new Date()))
        //隔一秒在次调用自己,达到每秒刷新一次显示的效果
        setTimeout(function () {
            //再次调用自己
            showTime();
        }, 1000);
    }

    //页面加载完成后执行上面的自定义函数
    $(function () {
        showTime()
    })
</script>
</body>
</html>
