<%@ tag import="com.ppcxy.common.utils.PrettyTimeUtils" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="beginDate" type="java.util.Date" required="true" description="开始时间" %>
<%@ attribute name="endDate" type="java.util.Date" required="true" description="结束时间" %>
<%=PrettyTimeUtils.prettyMillis(beginDate, endDate)%>
