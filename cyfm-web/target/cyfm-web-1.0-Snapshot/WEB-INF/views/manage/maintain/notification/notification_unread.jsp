<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head>
  <title>通知管理</title>
    <style>
        body{
            min-width: auto;
        }

        li.item{
            border-bottom: 1px solid #cccccc !important;
            height: 80px;
            padding: 15px 5px 5px 5px;

        }
        li.item a{
            line-height: 22px;
            line-height: 22px;
            color: #888888;
        }
        .label.label-icon {
            padding: 4px 4px 4px 4px;
        }
        .content span.details {
            border-top: 1px solid #EFF2F6 !important;
            margin-top: 3px;
            padding-top: 3px;
        }
        li.item .details{
            overflow: hidden;
        }
        li.item .time{
            float: right;
            max-width: 75px;
            font-size: 11px;
            font-weight: 400;
            opacity: 0.7;
            /* text-align: right; */
            padding: 1px 5px;
            background: #f1f1f1;
        }

        li.external{
            background: #eaedf2;
            display: block;
            overflow: hidden;
            padding: 15px 15px;
            letter-spacing: 0.5px;
            -webkit-border-radius: 4px 4px 0 0;
            -moz-border-radius: 4px 4px 0 0;
            -ms-border-radius: 4px 4px 0 0;
            -o-border-radius: 4px 4px 0 0;
            border-radius: 4px 4px 0 0;
        }
        li.external h3{
            color: #62878f;
            margin: 0;
            padding: 0;
            float: left;
            font-size: 13px;
            display: inline-block;
        }

        li.external h3 span{
            display: inline-block;
        }
        li.external a{
            display: inline-block;
            padding: 0;
            background: none;
            clear: inherit;
            font-size: 13px;
            font-weight: 300;
            position: absolute;
            right: 20px;
            border: 0;
            margin-top: -1px;
        }
    </style>
</head>
<body>
<div>
    <ul class="notice_list">
        <li class="external">
            <h3><span class="bold">${fn:length(unreads)}</span> 条未读通知</h3>
            <a href="${ctx}/manage/maintain/notification" target="rightFrame" onclick="$cy.closeme();">查看全部 </a>
        </li>
        <c:forEach items="${unreads}" var="item">
            <%--        <li style="cursor: pointer;">
                    <div class="col1">
                        <div class="cont">
                            <div class="cont-col1">
                                <div class="label label-sm label-icon label-info">
                                <i class="fa fa-plus"></i>
                            </div>
                        </div>
                        <div class="cont-col2">
                            <div class="desc">
                                ${item.title}
                            </div>
                        </div>
                    </div>
                    <div class="col2" style='float: right;width: 85px;'>
                        <div class="date">
                            {date}
                        </div>
                    </div>
                    </li>--%>


            <li class="item" data-id="${item.id}">
                <div id="notificaiton-${item.id}" class="notification-detail">
                    <div class="subject">
                        <a href="javascript:;">
                            <span class="time"><pretty:prettyTime date="${item.date}"/></span>
                            <span class="details">
                                    <span class="label label-sm label-icon label-success"><i class="fa fa-plus"></i></span>
                                    ${item.title}
                            </span>
                        </a>
                    </div>
                    <div class="clearfix"></div>
                    <div class="actions text-right">
                        <button class="btn btn-xs yellow markRead">标记已读</button>
                        <button class="btn btn-xs blue showDetails">通知详情</button>
                    </div>
                    <div class="content hidden">
                        <span class="details"> ${item.content}</span>
                    </div>
                </div>
            </li>
        </c:forEach>
    </ul>
</div>
<script>
      $("button.showDetails").click(function () {
          var $item = $(this).parents("li.item")

          var id = $item.data("id");
          var title = $item.find(".subject .details").text();
          var content = $item.find(".content .details").html();

          var message = top.layer.alert(content, {
              skin: 'layui-layer-lan'
              , title: title
              , closeBtn: 0
              , shift: 5 //动画类型
          }, function(){
              $.get("${ctx}/manage/maintain/notification/markRead/" + id,function (result) {
                  top.$cy.sysNotice.flush()
              });
              top.layer.close(message)
          });

          $item.remove();
          $("li.external .bold").text($(".notice_list li").size()-1);

      })

      $("button.markRead").click(function () {
          var $item = $(this).parents("li.item")
          var id = $item.data("id");
          
          $.get("${ctx}/manage/maintain/notification/markRead/" + id,function (result) {
              top.$cy.sysNotice.flush()
          });
          $item.remove();
      })
</script>
</body>
</html>
