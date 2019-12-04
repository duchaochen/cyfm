<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<html>
<head>
    <title>表单设计器</title>
    <%@include file="/WEB-INF/views/common/import-editor-js.jspf"%>
</head>
<body>
<o id="myFormDesign" type="text/plain" style="width:1024px;"></o>
<textarea id="template"></textarea>
<!-- script start-->
<script type="text/javascript">

    var formEditor = UE.getEditor('myFormDesign', {
        designer: true,//是否显示，设计器的 toolbars
        textarea: 'design_content',
        //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
        toolbars: [
            ['fullscreen', 'source', '|', 'undo', 'redo', '|', 'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'removeformat', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', '|', 'fontfamily', 'fontsize', 'customstyle', '|', 'indent', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'link', 'unlink', '|', 'horizontal', 'spechars', 'wordimage', '|', 'searchreplace',],
            ['|', 'date', 'time', '|', 'drafts', 'simpleupload', 'insertimage', 'attachment', '|', 'inserttable', 'deletetable', 'insertrow', 'insertcol', 'mergeright', 'mergedown', 'deleterow', 'deletecol', 'splittorows', 'splittocols', 'splittocells', 'deletecaption', 'inserttitle', 'mergecells',
                'insertparagraphbeforetable', //"表格前插入行"
                '|',
                'edittd', //单元格属性
                'edittable', //表格属性
                '|',
                'background',
                'scrawl',
                '|',
                'autotypeset',
            ]
        ],
        //focus时自动清空初始化时的内容
        //autoClearinitialContent:true,
        //关闭字数统计
        wordCount: false,
        //关闭elementPath
        elementPathEnabled: true,
        //默认的编辑区域高度
        initialFrameHeight: 300,
        allowDivTransToP: false,
        xssFilterRules: false
        //input xss过滤
        , inputXssFilter: false
        //output xss过滤
        , outputXssFilter: false
        , designer: {
            saveCallback: function (content) {
                $("#template").val(content)
            }
        }
        ///,iframeCssUrl:"css/bootstrap/css/bootstrap.css" //引入自身 css使编辑器兼容你网站css
        //更多其他参数，请参考ueditor.config.js中的配置项
    });

    formEditor.ready(function () {
        var template = $("#template").html();
        if (template) {
            formEditor.setContent(template);
        }
    });

</script>
</body>
</html>
