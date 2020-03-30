<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="newOrUpdateReview">
    <h2>
        <c:if test="${review['new']}">New </c:if> Review for <c:out value="${review.book.title}"/>
    </h2>
    <form:form modelAttribute="review" class="form-horizontal" id="add-review-form" action="/books/${bookId}/reviews/${reviewId}/edit" >
        <div class="form-group has-feedback">
        
        		<input type="hidden" name="reviewId" value="${review.id}"/>
        
        		<input type="hidden" name="bookId" value="${bookId}"/>
              		    
                <petclinic:inputField label="Title" name="title"/>
        
                <spring:bind path="raiting">
                    <c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }"/>
                    <c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
                    <div class="${cssGroup}">
                        <label class="col-sm-2 control-label">Raiting</label>
                
                        <div class="col-sm-10">
                            <form:input class="form-control" path="raiting" type="number"/>
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
                        
                <petclinic:inputField label="Opinion" name="opinion"/>
        
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                   <button class="btn btn-default" type="submit">Update Review</button>
            </div>
        </div>
    </form:form>
    
    
</petclinic:layout>
