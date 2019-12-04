<%@tag pageEncoding="UTF-8" %>
<%@ attribute name="page" type="org.springframework.data.domain.PageImpl" required="true" %>
<%@ attribute name="paginationSize" type="java.lang.Integer" required="true" %>
<%@ attribute name="showMeg" type="java.lang.Boolean" required="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    if (page == null) {
        return;
    }
    if (showMeg == null) {
        showMeg = true;
    }
    int current = page.getNumber() + 1;
    int begin = Math.max(1, current - paginationSize / 2);
    int end = Math.min(begin + (paginationSize - 1), page.getTotalPages());

    //当已经到尾页,开始到结束不足 paginationSize 时,重新计算开始页.

    if (page.getTotalPages() < paginationSize) {
        begin = 1;
    } else if (end - begin < paginationSize) {
        begin = end - paginationSize + 1;
    }

    request.setAttribute("current", current);
    request.setAttribute("begin", begin);
    request.setAttribute("end", end);
    request.setAttribute("pageSize", page.getSize());

%>
<style>
    .page-bar .page-size {
        padding: 0px 2px;
        width: 48px !important;
    }
</style>
<div class="page-bar" style="padding:5px 0px">
    <div class="pagin col-md-5 col-sm-10" style="margin-top: 0px;padding:0;">
        <% if (showMeg) {%>
        <div class="message">
            <c:choose>
                <c:when test="${page.first}">
                    <a href="javascript:;" class="btn btn-sm default prev disabled" style="height: 28px;" title="上一页"><i
                            class="fa fa-angle-left"></i></a>
                </c:when>
                <c:otherwise>
                    <a href="javascript:;" class="btn btn-sm default prev" title="上一页" style="height: 28px;"
                       onclick="jumpPage('${pageSize}', ${current - 1});"><i
                            class="fa fa-angle-left"></i></a>
                </c:otherwise>
            </c:choose>
            <input type="text" class="pagination-panel-input form-control input-mini input-inline input-sm"
                   disabled="disabled"
                   maxlenght="5" style="text-align:center; margin: 0 5px;" value="${current}"
                   onchange="jumpPage('${pageSize}', $(this).val());"/>
            <c:choose>
                <c:when test="${page.last}">
                    <a href="javascript:;" class="btn btn-sm default next disabled" title="下一页" style="height: 28px;"><i
                            class="fa fa-angle-right"></i></a>
                </c:when>
                <c:otherwise>
                    <a href="javascript:;" class="btn btn-sm default next" title="下一页" style="height: 28px;"
                       onclick="jumpPage('${pageSize}', ${current + 1});"><i
                            class="fa fa-angle-right"></i></a>
                </c:otherwise>
            </c:choose>
            共<i class="blue">${page.totalPages}</i>页，<i class="blue">${page.totalElements}</i> 条数据,
            <label><span class="seperator"></span>每页
                <select class="form-control input-xsmall input-sm input-inline page-size"
                        onchange="jumpPage($(this).val(), 1);">
                    <option value="5" <c:if test="${pageSize == 5}">selected="selected" </c:if>>5</option>
                    <option value="10" <c:if test="${pageSize == 10}">selected="selected" </c:if>>10</option>
                    <option value="15" <c:if test="${pageSize == 15}">selected="selected" </c:if>>15</option>
                    <option value="20" <c:if test="${pageSize == 20}">selected="selected" </c:if>>20</option>
                    <option value="50" <c:if test="${pageSize == 50}">selected="selected" </c:if>>50</option>
                    <option value="100" <c:if test="${pageSize == 100}">selected="selected" </c:if>>100</option>
                </select>
                条
            </label>
        </div>
        <% } %>
    </div>
    <div class="paginList visible-lg-block visible-md-block dataTables_paginate paging_bootstrap_full_number col-md-7 col-sm-10"
         style="padding:0;">
        <ul class="pagination pull-right" style="margin: 0px;">
            <% if (page.hasPrevious()) {%>
            <li class="paginItem"><a href="javascript:;" style="width: 54px" onclick="jumpPage('${pageSize}', 1);"
                                     title="首页"><span>首页</span></a></li>
            <%--<li class="paginItem"><a href="?page.pn=${current-1}"><i class="fa fa-angle-left"></i></a></li>--%>
            <%--<li><a href="?page=1&sortType=${sortType}&${searchParams}">&lt;&lt;</a></li>--%>
            <%--<li><a href="?page=${current-1}&sortType=${sortType}&${searchParams}">&lt;</a></li>--%>
            <%} else {%>
            <li class="paginItem disabled"><a href="javascript:;" style="width: 54px" title="首页"><span>首页</span></a>
            </li>
            <%--<li class="paginItem"><a href="javascript:;" class="disabled"><i class="fa fa-angle-left"></i></a></li>--%>
            <%} %>

            <c:forEach var="i" begin="${begin}" end="${end}">
                <c:choose>
                    <c:when test="${i == current}">
                        <li class="paginItem active"><a href="javascript:;">${i}</a></li>
                        <%--<li class="active"><a href="?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li>--%>
                    </c:when>
                    <c:otherwise>
                        <li class="paginItem"><a href="javascript:;" onclick="jumpPage('${pageSize}', ${i});">${i}</a>
                        </li>
                        <%--<li><a href="?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li>--%>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <% if (page.hasNext()) {%>
            <%--<li class="paginItem"><a href="?page.pn=${current+1}"><i class="fa fa-angle-right"></i></a></li>--%>
            <li class="paginItem"><a href="javascript:;" style="width: 54px"
                                     onclick="jumpPage('${pageSize}', ${page.totalPages});"
                                     title="尾页"><span>尾页</span></a></li>
            <%--<li><a href="?page=${current+1}&sortType=${sortType}&${searchParams}">&gt;</a></li>--%>
            <%--<li><a href="?page=${page.totalPages}&sortType=${sortType}&${searchParams}">&gt;&gt;</a></li>--%>
            <%} else {%>
            <%--<li class="paginItem"><a href="javascript:;" class="disabled"><i class="fa fa-angle-right"></i></a></li>--%>
            <li class="paginItem disabled"><a href="javascript:;" title="尾页" style="width: 54px"><span>尾页</span></a>
            </li>
            <%} %>
        </ul>
    </div>
    <a id="hideJumpA" class="display:none;"><span></span></a>
    <div class="clearfix"></div>
</div>
<script type="text/javascript">
    function jumpPage(pageSize, jumpNumber) {
        if (jumpNumber >${page.totalPages}) {
            jumpNumber = ${page.totalPages};
        }
        if (jumpNumber < 1) {
            jumpNumber = 1;
        }

        $("#hideJumpA").attr("href", "?page.total=${page.totalElements}&page.size=" + pageSize + "&page.pn=" + jumpNumber + "&" + $('form.form-search').serialize() + "&" + $cy.urlTools.findSortParam(currentUrl)).find("span").click();
    }

    $(function () {
        // 你肯定不信，这是处理osx下 safari bug用的。。。
        setTimeout(function () {
            $(".pagination-panel-input:disabled").removeAttr("disabled")
        }, 300);
    })
</script>

<%--

<div class="pagination">
    <ul>
        <% if (page.hasPreviousPage()){%>
        <li><a href="?page=1&sortType=${sortType}&${searchParams}">&lt;&lt;</a></li>
        <li><a href="?page=${current-1}&sortType=${sortType}&${searchParams}">&lt;</a></li>
        <%}else{%>
        <li class="disabled"><a href="#">&lt;&lt;</a></li>
        <li class="disabled"><a href="#">&lt;</a></li>
        <%} %>

        <c:forEach var="i" begin="${begin}" end="${end}">
            <c:choose>
                <c:when test="${i == current}">
                    <li class="active"><a href="?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <% if (page.hasNextPage()){%>
        <li><a href="?page=${current+1}&sortType=${sortType}&${searchParams}">&gt;</a></li>
        <li><a href="?page=${page.totalPages}&sortType=${sortType}&${searchParams}">&gt;&gt;</a></li>
        <%}else{%>
        <li class="disabled"><a href="#">&gt;</a></li>
        <li class="disabled"><a href="#">&gt;&gt;</a></li>
        <%} %>

    </ul>
</div>--%>
