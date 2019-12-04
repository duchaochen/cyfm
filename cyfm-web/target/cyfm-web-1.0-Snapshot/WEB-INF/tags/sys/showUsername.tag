<%@ tag import="com.ppcxy.common.spring.SpringContextHolder" %>
<%@ tag import="com.ppcxy.cyfm.sys.entity.user.User" %>
<%@ tag import="com.ppcxy.cyfm.sys.service.user.UserService" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.Long" required="true" description="当前要展示的用户的id" %>
<%@ attribute name="needLink" type="java.lang.Boolean" required="false" description="是否需要链接" %>
<%!private UserService userService;%>
<%

    if(userService == null) {
        userService = SpringContextHolder.getBean(UserService.class);
    }

    User user = userService.findOne(id);

    if(user == null) {
        out.write("<span class='label label-important'>删除的数据，请修改</span>");
        return;
    }
    String username = user.getUsername();

    if (Boolean.FALSE.equals(needLink)) {
        out.write(username);
        return;
    }
    
    out.write(
            String.format(
                    "<a class='btn btn-default btn-link no-padding'>%s</a>",
                    username));
%>
