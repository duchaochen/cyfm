<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ attribute name="content" type="java.lang.String" required="true" description="内容" %>
<%@ attribute name="placeholder" type="java.lang.String" required="false" description="占位符" %>
<%=content.replaceAll((placeholder == null ? "\\{ctx}" : placeholder), request.getContextPath())%>
