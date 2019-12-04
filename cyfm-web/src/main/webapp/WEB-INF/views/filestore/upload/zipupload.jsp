<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <title>附件上传页面</title>
  <%-- Force latest IE rendering engine or ChromeFrame if installed --%>
  <!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><![endif]-->
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="viewport" content="width=device-width">
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
  <%-- Bootstrap CSS Toolkit styles --%>
  <link rel="stylesheet" href="${ctx}/static/upload/css/bootstrap.min.css">
  <%-- Generic page styles --%>
  <link rel="stylesheet" href="${ctx}/static/upload/css/style.css">
  <%-- Bootstrap styles for responsive website layout, supporting different screen sizes --%>
  <link rel="stylesheet" href="${ctx}/static/upload/css/bootstrap-responsive.min.css">
  <%-- Bootstrap CSS fixes for IE6 --%>
  <!--[if lt IE 7]><link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap-ie6.min.css"><![endif]-->
  <%-- Bootstrap Image Gallery styles --%>
  <link rel="stylesheet" href="${ctx}/static/upload/css/bootstrap-image-gallery.min.css">
  <%-- CSS to style the file input field as button and adjust the Bootstrap progress bars --%>
  <link rel="stylesheet" href="${ctx}/static/upload/css/jquery.fileupload-ui.css">
  <style type="text/css">
    pre{padding-top:20px;}
    div#imgjgbox{
      background:url("${ctx}/static/ding_styles/${map.css_address}/images/imgjg.jpg") left 3px no-repeat;
      padding-left:20px;
      font-size:12px;
      color:#f00;
      margin-bottom:10px;
    }
  </style>
  <script src="${ctx}/static/global/plugins/jquery.min.js" type="text/javascript"></script>
  <script type="text/javascript">
      $.browser = {};
      $.browser.mozilla = /firefox/.test(navigator.userAgent.toLowerCase());
      $.browser.webkit = /webkit/.test(navigator.userAgent.toLowerCase());
      $.browser.opera = /opera/.test(navigator.userAgent.toLowerCase());
      $.browser.msie = /msie/.test(navigator.userAgent.toLowerCase());

      _ctx = "${ctx}";
  </script>
  <script type="text/javascript">
      function preview(fileTypes){
          var suffix="bmp|jpg|jpeg|gif|png";
          var reg="^.*?\.("+suffix+")$";
          if (reg.match(fileTypes.toLowerCase())){
              var pat="${ctx}/upload.html?operate=previewScan&menu_id="+$("#menu_id").val()+"&flow_ins_id="+$("#flow_ins_id").val();
              art.dialog.open(pat,{ title: '图片预览',width: 560,height:300,lock:true,resize:false,drag:false});
          }
      }
      function colsefunc(){

      }
  </script>
</head>
<body>
<form enctype="multipart/form-data"  method="POST" action="${ctx}/filelibs/filemanage/zipUpload?filePathId=${filePathId}&fileLibsId=${fileLibsId}" id="fileupload" >
  <div class="container">

    <div>
      <input type="hidden" id="ctx" value="${ctx}" />
      <input type="hidden" id="singleSize" name="size" value="${uploadParam.singleSize}"/>
      <input type="hidden" id="num" name="num" value="${requestScope.num}"/>
      <input type="hidden" id="onceNum" name="onceNum" value="10"/>
      <input type="hidden" id="type" name="type" value="zip,ZIP"/>
      <div class="row fileupload-buttonbar">
        <div style="border: 1px solid #0a0b0c;background: #eee;margin-bottom: 5px;padding: 5px;">
          文件总数：<c:choose><c:when test="${uploadParam.totalNum==''}"> 无限制</c:when><c:otherwise> ${uploadParam.totalNum}（个）<c:choose>
          <c:when test="${!empty requestScope.oknum}"> 已经上传${requestScope.oknum}（个） ，只能继续上传${requestScope.num}（个）</c:when></c:choose></c:otherwise></c:choose>单个文件大小：<c:choose><c:when test="${uploadParam.singleSize==''}"> 无限制</c:when><c:otherwise><c:choose><c:when test="${uploadParam.singleSize>1000}">${uploadParam.singleSize/1024.0} 1（GB）</c:when><c:otherwise> ${uploadParam.singleSize}（MB）</c:otherwise></c:choose></c:otherwise></c:choose>
          <br>支持的文件格式：${uploadParam.fileTypes}
          <%--<c:if test="${fn:contains(uploadParam.fileTypes,'txt')}">文本文档 </c:if>--%>
          <%--<c:if test="${fn:contains(uploadParam.fileTypes,'doc')}">word </c:if>--%>
          <%--<c:if test="${fn:contains(uploadParam.fileTypes,'xls')}">excel </c:if>--%>
          <%--<c:if test="${fn:contains(uploadParam.fileTypes,'jpg')}">图片(jpg) </c:if>--%>
          <%--<c:if test="${fn:contains(uploadParam.fileTypes,'avi')}">影音(avi) </c:if>--%>
          <%--<c:if test="${fn:contains(uploadParam.fileTypes,'zip')}">压缩文件(zip) </c:if>--%>
          <%--<c:if test="${fn:contains(uploadParam.fileTypes,'rar')}">压缩文件(rar) </c:if>--%>
        </div>
        <div>
                <span class="btn btn-success fileinput-button">
                    <i class="icon-plus icon-white"></i>
                    添加文件
                    <input type="file" name="files[]" multiple>
                </span>
          <button type="submit" class="btn btn-primary start">
            <i class="icon-upload icon-white"></i>
            开始上传
          </button>
        </div>

        <%-- The global progress information --%>
        <div class="span5 fileupload-progress fade" >
          <div class="all" style="float:right;"></div>
          <!-- The global progress bar -->
          <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" style="width:605px;">
            <div class="bar" style="width:0%;"></div>
          </div>
          <%-- The extended global progress information --%>
          <div class="progress-extended" style="width:605px;">&nbsp;</div>
        </div>
      </div>

      <div class="fileupload-loading"></div>
      <br>
      <%-- The table listing the files available for upload/download --%>
      <table role="presentation" class="table table-striped"><tbody class="files" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody></table>

    </div>

    <%-- modal-gallery is the modal dialog used for the image gallery --%>
    <div id="modal-gallery" class="modal modal-gallery hide fade" data-filter=":odd">
      <div class="modal-header">
        <a class="close" data-dismiss="modal">&times;</a>
        <h3 class="modal-title"></h3>
      </div>
      <div class="modal-body"><div class="modal-image"></div></div>
      <div class="modal-footer">
        <a class="btn modal-download" target="_blank">
          <i class="icon-download"></i>
          <span>下载</span>
        </a>
        <a class="btn btn-success modal-play modal-slideshow" data-slideshow="5000">
          <i class="icon-play icon-white"></i>
          <span>幻灯片</span>
        </a>
        <a class="btn btn-info modal-prev">
          <i class="icon-arrow-left icon-white"></i>
          <span>上一个</span>
        </a>
        <a class="btn btn-primary modal-next">
          <span>下一个</span>
          <i class="icon-arrow-right icon-white"></i>
        </a>
      </div>
    </div>

  </div>
  <%-- The template to display files available for upload --%>
  <script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td class="preview"><span class="fade"></span></td>
        <td class="name" title="{%=file.name%}"><span >{%=file.name.substring(0,10)%}{% if ((file.name).length>10) { %}...{% } %}</span></td>
        <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
        {% if (file.error) { %}
            <td class="error" colspan="2"><span class="label label-important">{%=locale.fileupload.error%}</span> {%=locale.fileupload.errors[file.error] || file.error%}</td>
        {% } else if (o.files.valid && !i) { %}
            <td>
                <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="bar" style="width:0%;"></div></div>
            </td>
            <td class="start">{% if (!o.options.autoUpload) { %}
                <button type="button" class="btn btn-primary">
                    <span>{%=locale.fileupload.start%}</span>
                </button>
            {% } %}</td>
        {% } else { %}
            <td colspan="2"></td>
        {% } %}
        <td class="cancel">{% if (!i) { %}
            <button type="button" class="btn btn-warning">
                <span>{%=locale.fileupload.cancel%}</span>
            </button>
        {% } %}</td>
    </tr>
{% } %}
</script>

  <%-- The template to display files available for download --%>
  <script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        {% if (file.error) { %}
            <td></td>
            <td class="name" title="{%=file.name%}"><span>{%=file.name.substring(0,15)%}{% if ((file.name).length>15) { %}...{% } %}</span></td>
            <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
            <td class="error" colspan="2"><span class="label label-important">{%=locale.fileupload.error%}</span> {%=locale.fileupload.errors[file.error] || file.error%}</td>
        {% } else { %}
            <td class="preview" style="width:57px;">{% if (file.thumbnail_url) { %}
               <img src="${ctx}/{%=file.thumbnail_url%}" width="57px" height="61px">
            {% } %}</td>
            <td class="name" colspan="3">
               <a title="{%=file.name%}" href="javascript:preview('{%=file.file_type%}')">  {%=file.name.substring(0,15)%}{% if ((file.name).length>15) { %}...{% } %}</a>
            </td>
            <td class="size" ><span>上传成功</span></td>
        {% } %}
        <td class="delete" >
            <button type="button" class="btn btn-danger" data-type="{%=file.delete_type%}" data-url="{%=file.delete_url%}">
                <span>{%=locale.fileupload.destroy%}</span>
            </button>
        </td>
    </tr>
{% } %}
</script>
  <%-- The jQuery UI widget factory, can be omitted if jQuery UI is already included --%>
  <script type="text/javascript" src="${ctx}/static/upload/js/vendor/jquery.ui.widget.js"></script>
  <%-- The Templates plugin is included to render the upload/download listings --%>
  <script type="text/javascript" src="${ctx}/static/upload/js/tmpl.min.js"></script>
  <%-- The Load Image plugin is included for the preview images and image resizing functionality --%>
  <script type="text/javascript" src="${ctx}/static/upload/js/load-image.min.js"></script>
  <%-- The Canvas to Blob plugin is included for image resizing functionality --%>
  <script type="text/javascript" src="${ctx}/static/upload/js/canvas-to-blob.min.js"></script>
  <%-- Bootstrap JS and Bootstrap Image Gallery are not required, but included for the demo --%>
  <script type="text/javascript" src="${ctx}/static/upload/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="${ctx}/static/upload/js/bootstrap-image-gallery.min.js"></script>
  <%-- The Iframe Transport is required for browsers without support for XHR file uploads --%>
  <script type="text/javascript" src="${ctx}/static/upload/js/jquery.iframe-transport.js"></script>
  <%-- The basic File Upload plugin --%>
  <script type="text/javascript" src="${ctx}/static/upload/js/jquery.fileupload.js"></script>
  <%-- The File Upload file processing plugin --%>
  <script type="text/javascript" src="${ctx}/static/upload/js/jquery.fileupload-fp.js"></script>
  <%-- The File Upload user interface plugin --%>
  <script type="text/javascript" src="${ctx}/static/upload/js/jquery.fileupload-ui.js"></script>
  <%-- The localization script --%>
  <script type="text/javascript" src="${ctx}/static/upload/js/locale.js"></script>
  <%-- The main application script --%>
  <script type="text/javascript" src="${ctx}/static/upload/js/main.js"></script>
  <script type="text/javascript" src="${ctx}/static/upload/js/cors/jquery.xdr-transport.js"></script>

</body>
</html>
