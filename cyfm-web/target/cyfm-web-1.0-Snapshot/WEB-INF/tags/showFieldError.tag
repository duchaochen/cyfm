<%@ tag pageEncoding="UTF-8" description="显示字段错误消息" %>
<%@ attribute name="commandName" type="java.lang.String" required="true" description="命令对象名称" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:hasBindErrors name="${commandName}">
    <c:if test="${errors.fieldErrorCount > 0}">
    <c:forEach items="${errors.fieldErrors}" var="error">
        <spring:message var="message" code="${error.code}" arguments="${error.arguments}" text="${error.defaultMessage}"/>

        var message = '<label id=${error.field}-error class=error>${not empty message ? message : error.code}</label>';

        if($cy.validate.validFiledTips[fieldName]){
            $("#error-tips-" + fieldName).find(".error").after(message);
        }else{
            var point = 1;
            var target =  $("[name='${error.field}']");
            target.addClass("has-error");

            var fieldName = "${error.field}";

            //如果存在label,则显示在label右侧.
            var fieldLabel = $("label[for='" + fieldName + "']");
            if (fieldLabel.size() > 0) {
            point = 2;
            target = fieldLabel;
            }
            var index = layer.tips(message, target, {
            id: "error-tips-" + fieldName
            , tipsMore: true
            , tips: [point, '#F24100']
            , time: 0
            });
            console.log("hT:"+" "+ fieldName +" "+index)
            $cy.validate.validFiledTips[fieldName] = index;
        }

    </c:forEach>
    </c:if>
</spring:hasBindErrors>
