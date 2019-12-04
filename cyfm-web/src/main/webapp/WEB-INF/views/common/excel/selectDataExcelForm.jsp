<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
    <title></title>
</head>
<body>
<table id="contentTable" data-tid="${modelName}" class="table table-list table-striped table-bordered table-hover table-condensed table-advance no-disable">
    <thead>
    <tr>
        <th>字段名称</th>
        <th>标题</th>
        <th>类型</th>
        <th>名字</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${datacolumn}" var="datacolumn">
        <tr>
            <td>${datacolumn.columnName} </td>
            <td>${datacolumn.title}</td>
            <td>${datacolumn.columnType}</td>
            <td>${line.refColumnName}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div>
    <div class="col-xs-3">
        <div class="form-group">
            <label class="control-label">导出模版:</label>
            <div class="controls inline">

                <input id="file" onclick="upload(this,'template','template','file')" class="btn btn-primary" type="button" value="上传文件"/>
                <ul class="template">

                </ul>

                <input id="resource" type="hidden" value="${modelName}" />
            </div>
        </div>
    </div>
</div>
<script>
    var excelid = "";
    var fileData = [];
    $(function () {
        var resource = $("#resource").val();
        $.ajax({
            type: 'post',
            url: "${ctx}/excel/excelTemplate/findresource",
            cache: false,
            dataType: "json",
            data:{"resource":resource},
            success: function (data) {
                console.log(data)
                excelid = data.excelTemplate.id;
                var files = data.files;
                for (var i=0;i<files.length;i++) {
                    $(".template").append("<li class='file' data-id='"+files[i].id+"' style='width: 250px; height: 30px;'><div style='width: 250px; height: 30px;'><a > " + files[i].realName + "</a><a onclick='removeFile(this)' class='btn btn-xs red pull-right'>移除</a></div></li>")
                    fileData.push(files[i].id)
                }
            }
        })
    })


    function removeFile(a) {
        $cy.confirm({
            message: '是否移除此文件?', yes: function () {
                $(a).parents("li").remove();
            }
        })
    }
    function upload(obj,tag,ys,type) {
        //调用文件上传窗口,identity为使用模块,tag为tag,可以用来区分是哪里上传的
        $cy.tools.uploadFile({
            identity: "agent", secondIdentity: "${entity.id}", tag: tag, callback: function (oper, data) {
                /**
                 * 上传文件回调函数
                 * @param oper 操作类型
                 * @param data 数据
                 */
                switch (oper) {
                    case 'add':
                        console.log('add file', data)
                        var file = data.files[0];

                        $("." + ys).append("<li class='file' data-id='"+file.key+"' style='width: 250px; height: 30px;'><div style='width: 250px; height: 30px;'><a href='" + _ctx + "/" +file.url+"'> "+file.name+"</a><a onclick='removeFile(this)' class='btn btn-xs red pull-right'>移除</a></div></li>")
                        fileData.push(file.key)
                        break;
                    case 'delete':
                        console.log('delete file', data)
                        break;
                }
            }
        })
    }

    function sub(index) {
        var   resourceIdentity = $("#resource").val();
        var  files = [];
        $(".file").each(function (i, o) {
            files.push($(o).data("id"));
        });
        var   templateId = files.join(",");
        $.ajax({
            type: 'post',
            url: "${ctx}/excel/excelTemplate/add",
            cache: false,
            dataType: "text",
            data:{"id":excelid,"templateId":templateId,"resourceIdentity":resourceIdentity},
            success: function (data) {
                $cy.info("上传成功!");
                top.layer.close(index);

            }
        })

    }
</script>
</body>
</html>
