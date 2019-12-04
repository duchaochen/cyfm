<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/plugins/simditor/simditor.css" />
    <link rel="stylesheet" href="${ctx}/static/plugins/simditor/extensions/html/simditor-html.css" media="screen" charset="utf-8" />
    <link rel="stylesheet" href="${ctx}/static/plugins/simditor/extensions/markdown/simditor-markdown.css" media="screen" charset="utf-8" />
    <title>角色详情</title>
    <script type="text/javascript" src="${ctx}/static/plugins/simditor/module.js"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/simditor/hotkeys.js"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/simditor/uploader.js"></script>

    <script type="text/javascript" src="${ctx}/static/plugins/simditor/simditor.js"></script>

    <script type="text/javascript" src="${ctx}/static/plugins/simditor/extensions/html/beautify-html.js"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/simditor/extensions/html/simditor-html.js"></script>


    <script type="text/javascript" src="${ctx}/static/plugins/simditor/extensions/markdown/marked.js"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/simditor/extensions/markdown/simditor-marked.js"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/simditor/extensions/markdown/to-markdown.js"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/simditor/extensions/markdown/simditor-markdown.js"></script>

    <script src="${ctx}/static/plugins/simditor/extensions/fullscreen/simditor-fullscreen.js"></script>


</head>
<body>
<div>
    <button type="button" class="btn btn-xs btn-default" onclick="$cy.waiting();setTimeout(function() {
        $cy.waitingOver();
    },10000);">加载10秒
    </button>


    <button type="button" class="btn btn-xs btn-default" onclick="$cy.waiting('加载中.');setTimeout(function() {
        $cy.waitingOver()
    },5000);">等待加载
    </button>


    <button type="button" class="btn btn-xs btn-default" onclick="$cy.info('这是一段提示信息.')">提示消息</button>
    <button type="button" class="btn btn-xs btn-default" onclick="$cy.success('这是一段成功消息.')">成功消息</button>
    <button type="button" class="btn btn-xs btn-default" onclick="$cy.warn('这是一段警告消息.')">警告消息</button>
    <button type="button" class="btn btn-xs btn-default" onclick="$cy.error('这是一段错误消息.')">错误消息</button>
    <button type="button" class="btn btn-xs btn-default" onclick="$cy.confirm({message:'这是一段确认信息.',yes:function() {
        alert('点击了确定');
    },no:function() {
        alert('点击了取消');
    }})">需要确认
    </button>


    <button type="button" class="btn btn-xs btn-default"
            onclick="$cy.tools.chooseUser({callback:function(show,results){alert(show)}})">用户选择控件
    </button>


    <button type="button" class="btn btn-xs btn-default" onclick="upload()">文件上传</button>

</div>
<div>
    <input type="text">
    <input type="text" class="datepicker" data-format="both">
</div>

<div>
    <textarea id="editor" placeholder="演示富文本编辑器..." autofocus></textarea>
</div>

<div>
    <dd>
        <div class="title">
            <span>演示说明</span>
        </div>
        <ul class="menuson">
            <li><cite></cite><a id="persistence-tab" href="${ctx}/story/persistence"
                                target="rightFrame">持久化高级演示</a><i></i></li>
            <li><cite></cite><a id="jms-tab" href="${ctx}/story/jms" target="rightFrame">JMS演示</a><i></i></li>
            <li><cite></cite><a id="cache-tab" href="${ctx}/story/cache" target="rightFrame">Cache演示</a><i></i></li>
            <li><cite></cite><a id="security-tab" href="${ctx}/story/security" target="rightFrame">安全高级演示</a><i></i>
            </li>
            <li><cite></cite><a id="utilizes-tab" href="${ctx}/story/utilizes" target="rightFrame">工具类演示</a><i></i></li>
            <li><cite></cite><a id="executablewar-tab" href="${ctx}/story/executablewar"
                                target="rightFrame">可运行war包演示</a><i></i></li>
        </ul>
    </dd>
</div>
<script>
    $cy.handleUniform();

    function upload() {
        //调用文件上传窗口,identity为使用模块,tag为tag,可以用来区分是哪里上传的
        $cy.tools.uploadFile({
            identity: "test", secondIdentity: "1", tag: "all", callback: function (oper, data) {
                /**
                 * 上传文件回调函数
                 * @param oper 操作类型
                 * @param data 数据
                 */
                switch (oper) {
                    case 'add':
                        console.debug('add file', data)
                        break;
                    case 'delete':
                        console.log('delete file', data)
                        break;
                }
            }
        })
    }


    $(function(){
        var editor = new Simditor({
            textarea: $('#editor'),
            toolbar: ['bold', 'italic', 'underline', 'color', '|', 'ol', 'ul', '|', 'link' , 'html' , 'markdown', 'marked', '|' , 'fullscreen']
        });
    })
</script>
</body>
</html>
