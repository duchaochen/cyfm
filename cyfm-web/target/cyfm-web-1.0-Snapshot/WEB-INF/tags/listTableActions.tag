<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="name" type="java.lang.String" required="true"%>
<%@ attribute name="id" type="java.lang.Object" required="true"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<shiro:hasPermission name="user:delete or user:update">
    <td class="action">
        <shiro:hasPermission name="user:update">
            <a href="${ctx}/${viewPrefix}/update/${id}" id="editLink-${name}" class="btn btn-xs purple"><i class="fa fa-edit"></i> 修改</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="user:delete">
            <a href="${ctx}/${viewPrefix}/delete/${id}" id="deleteLink-${name}" class="btn btn-xs red deleteRow"><i class="fa fa-trash"></i> 删除</a>
        </shiro:hasPermission>
    </td>
</shiro:hasPermission>
