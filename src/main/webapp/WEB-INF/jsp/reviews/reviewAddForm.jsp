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
    <form:form modelAttribute="review" class="form-horizontal" id="add-review-form" action="/books/${bookId}/reviews/new" >
        <div class="form-group has-feedback">
        
        		<input type="hidden" name="bookId" value="${bookId}"/>
              		    
                <petclinic:inputField label="Title" name="title"/>
        
                <petclinic:inputField label="Rating" name="raiting"/>
        
                <petclinic:inputField label="Opinion" name="opinion"/>
        
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
				<button class="btn btn-default" type="submit">Add Review</button>
            </div>
        </div>
    </form:form>
    
    
</petclinic:layout>
