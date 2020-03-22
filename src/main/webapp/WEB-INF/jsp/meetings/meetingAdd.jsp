<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<head>
    <link href="jquery.datetimepicker.min.css" rel="stylesheet">
</head>
<petclinic:layout pageName="newMeeting">


	<jsp:body>
    <h2>
        <b>New Meeting about ${book.title}</b>
    </h2>
    <form:form modelAttribute="meeting" class="form-horizontal"
			action="/admin/books/${bookId}/meetings/new"> 
        <div class="form-group has-feedback">
            <input type="hidden" name="bookId" value="${bookId}"/>
            <petclinic:inputField label="Name" name="name" />
            <petclinic:inputField label="Place" name="place" />

            <spring:bind path="start">
                <c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }"/>
                <c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
                <div class="${cssGroup}">
                    <label class="col-sm-2 control-label">Start date</label>
            
                    <div class="col-sm-10">
                        <form:input class="form-control" path="start" type="datetime-local"/>
                        <c:if test="${valid}">
                            <span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
                        </c:if>
                        <c:if test="${status.error}">
                            <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                            <span class="help-inline">${status.errorMessage}</span>
                        </c:if>
                    </div>
                </div>
            </spring:bind>
            <script>
                start: format(new Date(), 'yyyy-MM-dd\'T\'HH:mm', { awareOfUnicodeTokens: true })
            </script>

            <spring:bind path="end">
                <c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }"/>
                 <c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
                    <div class="${cssGroup}">
                        <label class="col-sm-2 control-label">End date</label>

                        <div class="col-sm-10">
                            <form:input class="form-control" path="end" type="datetime-local"/>
                            <c:if test="${valid}">
                                <span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
                            </c:if>
                            <c:if test="${status.error}">
                                <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                                <span class="help-inline">${status.errorMessage}</span>
                            </c:if>
                        </div>
                    </div>
            </spring:bind>

            <petclinic:inputField label="Capacity" name="capacity" />
        </div>
         <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                            <button class="btn btn-default"
								type="submit">Create meeting</button>
                </div>
            </div>
        </form:form>
           </jsp:body>
</petclinic:layout>