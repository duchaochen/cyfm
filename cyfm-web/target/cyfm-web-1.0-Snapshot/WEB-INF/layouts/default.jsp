<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<meta charset="utf-8"/>
	<title>${cy_systemName}:<sitemesh:title/></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1" name="viewport"/>
	<meta content="" name="description"/>
	<meta content="" name="author"/>
	<meta HTTP-EQUIV="pragma" CONTENT="no-cache">
	<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
	<meta HTTP-EQUIV="expires" CONTENT="0">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
<!-- ================================= Css 区域 ========================================== -->
<%@include file="/WEB-INF/views/common/import-css.jspf"%>
<link href="${ctx}/static/plugins/dialog/css/ui-dialog.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/static/pub/styles/default.css" rel="stylesheet" type="text/css"/>
	<style>
		.page-sidebar-closed .page-logo{
			display: none !important
		}

		.page-logo a:hover{
			text-decoration: none;
		}
		.page-logo a{
			FONT-SIZE: 16PX;
			MARGIN-TOP: 12PX;
			COLOR: #b4bcc8;
			font-weight: 500;
		}
	</style>
<!-- ================================= JS 区域 ========================================== -->
<script>
	var $cy = {};
</script>
<%@include file="/WEB-INF/views/common/import-js.jspf"%>
<sitemesh:head />
</head>
<body>
<%@ include file="/WEB-INF/layouts/default/header.jsp"%>
<sitemesh:body/>
<%@ include file="/WEB-INF/layouts/default/footer.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		var d=undefined;
		$("li.xdh a").click(function(){
			if(d){
				d.close();
			}
			d = dialog({
				title: $(this).text()+"-敬请期待",
				width:500,
				content: $(this).attr("data-info"),
			});
			d.show($(this)[0]);
			return false;
		});
	});
</script>
</body>
</html>
