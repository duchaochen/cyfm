<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title>资源管理</title>
</head>
<body>
<div id="treeMain" style="height: 500px">
    <div class="ui-layout-west tree" style="overflow-y: hidden">
        <iframe name="treeFrame" width="100%" height="100%"  frameborder="0" scrolling="auto" src="${ctx}/sys/resource/tree?async=true"></iframe>
    </div>
    <div class="ui-layout-center tree" style="overflow-y: hidden">
        <iframe name="listFrame" width="100%" height="100%"  frameborder="0" scrolling="auto"></iframe>
    </div>
</div>
<script>
    var height = $(".page-content", top.document)[0].clientHeight;
    $("#treeMain").css("height", height-46);
</script>
<script type="text/javascript">
    var layout = $('#treeMain').layout({
        zIndex:10,
        west__size:                    265
        ,    west__spacing_closed:        20
        ,    west__togglerLength_closed:    100
        ,    west__togglerContent_closed:"显示树"
        ,    west__togglerTip_closed:    "显示树"
        ,    west__sliderTip:            "显示树"
        ,    resizerTip:         "调整大小"
        ,    togglerTip_open: "隐藏树"
        ,    togglerTip_closed: "显示树"
        ,    maskContents:        true // IMPORTANT - enable iframe masking
    });
    layout.sizePane("west", 275);
</script>
</body>
</html>
