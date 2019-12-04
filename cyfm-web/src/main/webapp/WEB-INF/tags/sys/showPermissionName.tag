<%@ tag import="com.ppcxy.common.spring.SpringContextHolder" %>
<%@ tag import="com.ppcxy.cyfm.sys.entity.permission.Permission" %>
<%@ tag import="com.ppcxy.cyfm.sys.service.permission.PermissionService" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.Long" required="true" description="当前要展示的组织机构的名字" %>
<%!private PermissionService permissionService;%>
<%

    if(permissionService == null) {
        permissionService = SpringContextHolder.getBean(PermissionService.class);
    }

    Permission permission = permissionService.findOne(id);

    if(permission == null) {
        out.write("<span class='label label-important'>删除的数据，请修改</span>");
        return;
    }

    out.write(String.format("<label class='btn-xs no-padding purple' title='%s'>%s[%s]</label>", permission.getDescription(), permission.getName(), permission.getValue()));

%>
