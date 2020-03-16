<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<petclinic:layout pageName="review">

	<h2>Review Information</h2>

	<table class="table table-striped">
		<tr>
			<th>Title</th>
			<td><c:out value="${review.title}" /></td>
        </tr>
        <tr>
			<th>Book</th>
			<td><c:out value="${review.book.title}" /></td>
        </tr>
        <tr>
            <th>Writed by</th>
            <td><c:out value="${review.user.username}"/></td>
        </tr>
        <tr>
            <th>Rating</th>
            <td><c:out value="${review.raiting}"/></td>
        </tr>
         <tr>
            <th>Opinion</th>
            <td><c:out value="${review.opinion}"/></td>
        </tr>
    </table>
    
    
    <a class="btn btn-default" href='<spring:url value="/books/${review.book.id}/reviews/${review.id}/edit" htmlEscape="true"/>'>Edit review</a>
    
    <a class="btn btn-default" href='<spring:url value="/books/${review.book.id}/reviews/${review.id}/delete" htmlEscape="true"/>'>Delete review</a>
    
    <a class="btn btn-default" href='<spring:url value="/books/${review.book.id}" htmlEscape="true"/>'>Return to book</a>
    
    
    <!--<sec:authorize access="hasAuthority('admin')">
     <a class="btn btn-default" href='<spring:url value="/admin/books/delete/${book.id}" htmlEscape="true"/>' onclick="return confirm('Are you sure you want to delete this book?');">Delete Book</a>
	</sec:authorize>-->
	

</petclinic:layout>
