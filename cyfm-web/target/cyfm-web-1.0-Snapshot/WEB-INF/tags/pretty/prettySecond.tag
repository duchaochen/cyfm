<%@ tag import="com.ppcxy.common.utils.PrettyTimeUtils" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="seconds" type="java.lang.Long" required="true" description="秒" %>
<%=PrettyTimeUtils.prettySeconds(seconds == null ? 0 : seconds)%>
