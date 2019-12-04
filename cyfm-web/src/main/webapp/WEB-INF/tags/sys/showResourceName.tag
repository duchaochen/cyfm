<%@ tag import="com.google.common.collect.Lists" %>
<%@ tag import="com.ppcxy.common.spring.SpringContextHolder" %>
<%@ tag import="com.ppcxy.cyfm.sys.entity.resource.Resource" %>
<%@ tag import="com.ppcxy.cyfm.sys.service.resource.ResourceService" %>
<%@ tag import="java.util.List" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.Long" required="true" description="当前要展示的资源的名字" %>
<%@ attribute name="showParents" type="java.lang.Boolean" required="false" description="是否显示父亲" %>
<%@ attribute name="includeRoot" type="java.lang.Boolean" required="false" description="是否包含根" %>
<%!private ResourceService resourceService;%>
<%

    if(showParents == null) {
        showParents = true;
    }
    if(includeRoot == null) {
        includeRoot = true;
    }

    if(resourceService == null) {
        resourceService = SpringContextHolder.getBean(ResourceService.class);
    }

    Resource resource = resourceService.findOne(id);

    if(resource == null ) {
        out.write("<span class='label label-important'>删除的数据，请修改</span>");
        return;
    }

    List<String> names = Lists.newArrayList();

    names.add(resource.getName());

    if(showParents) {
        List<Resource> parents = resourceService.findAncestor(resource.getParentIds());
        for(Resource o : parents) {
            if(!includeRoot && o.isRoot()) {
                continue;
            }
            names.add(o.getName());
        }
    }

    StringBuilder s = new StringBuilder();
    s.append("<a class='btn btn-default btn-link no-padding'>");

    for(int l = names.size() - 1, i = l; i >= 0; i--) {
        if(i != l) {
            s.append(" &gt; ");
        }
        s.append(names.get(i));
    }

    s.append("</a>");
    out.write(s.toString());

%>
