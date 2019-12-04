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
    <%@include file="/WEB-INF/views/common/import-upload-css.jspf"%>

</head>
<body>
<form enctype="multipart/form-data"  method="post" action="${ctx}/filestore/upload" id="fileupload" >
    <div class="container">
        <div>
            <%--<input type="hidden" id="ctx" value="">--%>
            <%--<input type="hidden" id="singleSize" name="size" value="500">--%>
            <%--<input type="hidden" id="num" name="num" value="">--%>
            <%--<input type="hidden" id="onceNum" name="onceNum" value="10">--%>
            <%--<input type="hidden" id="type" name="type" value="txt,zip,xlsx,ppt,docx,doc,xls,exe,rar,png,jpg">--%>
            <input type="hidden" id="ctx" value="${ctx}" />
            <input type="hidden" id="singleSize" name="size" value="${uploadParam.singleSize}"/>
            <input type="hidden" id="num" name="num" value="${requestScope.num}"/>
            <input type="hidden" id="onceAmount" name="onceAmount" value="${uploadParam.onceAmount}"/>
            <input type="hidden" id="type" name="type" value="${uploadParam.fileTypes}"/>
            <input type="hidden" id="identity" name="identity" value="${param['identity']}"/>
            <input type="hidden" id="secondIdentity" name="secondIdentity" value="${param['secondIdentity']}"/>
            <input type="hidden" id="tag" name="tag" value="${param['tag']}"/>

            <div class="fileupload-buttonbar">
                <div style="border: 1px solid #0a0b0c;background: #eee;margin-bottom: 5px;padding: 5px;display:block;word-break: break-all;word-wrap: break-word;">

                    <c:choose>
                        <c:when test="${uploadParam.totalSize > 0}">  文件总数：
                        ${uploadParam.totalSize}（个）

                            <c:choose>
                                <c:when test="${!empty uploadParam.currentFileCount}"> 已经上传 ${uploadParam.currentFileCount}（个） ，只能继续上传${uploadParam.totalSize-uploadParam.currentFileCount > 0 ? uploadParam.totalSize-uploadParam.currentFileCount : 0}（个）</c:when>
                            </c:choose>
                            &nbsp;|&nbsp;
                        </c:when>
                    </c:choose>

                    单个文件大小限制：
                    <c:choose>
                        <c:when test="${uploadParam.singleSize == 0}"> 无限制</c:when>
                        <c:otherwise>
                            <pretty:prettyMemory byteSize="${uploadParam.singleSize}"/>
                        </c:otherwise>
                    </c:choose>

                    &nbsp;|&nbsp;单次限制上传文件个数：${uploadParam.onceAmount} 个

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
            <div class="filelist">
                <%-- The table listing the files available for upload/download --%>
                <table role="presentation" class="table table-striped"><tbody class="files" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody></table>
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
               <a title="{%=file.name%}" href="javascript:preview('{%=file.file_type%}','${ctx}/{%=file.thumbnail_url%}','{%=file.name%}')">  {%=file.name.substring(0,15)%}{% if ((file.name).length>15) { %}...{% } %}</a>
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
<%@include file="/WEB-INF/views/common/import-upload-js.jspf"%>
</body>
</html>
