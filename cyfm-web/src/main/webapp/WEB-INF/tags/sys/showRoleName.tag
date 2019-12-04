<%@ tag import="com.ppcxy.common.spring.SpringContextHolder" %>
<%@ tag import="com.ppcxy.cyfm.sys.entity.permission.Role" %>
<%@ tag import="com.ppcxy.cyfm.sys.service.permission.RoleService" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.Long" required="true" description="当前要展示的组织机构的名字" %>
<%!private RoleService roleService;%>
<%

    if(roleService == null) {
        roleService = SpringContextHolder.getBean(RoleService.class);
    }

    Role role = roleService.findOne(id);
    if(role == null) {
        out.write("<span class='label label-important'>删除的数据，请修改</span>");
        return;
    }

    out.write(String.format("<a class='btn btn-default btn-link no-padding'><span title='%s'>%s[%s]</span></a>", role.getDescription(), role.getName(), role.getValue()));

%>
