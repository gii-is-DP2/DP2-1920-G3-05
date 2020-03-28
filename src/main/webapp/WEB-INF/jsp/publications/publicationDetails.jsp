<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<petclinic:layout pageName="publications">

	<h2>Publication Information</h2>

	<table class="table table-striped">
		<tr>
			<th>Title</th>
			<td><b><c:out value="${publication.title}" /></b></td>
			<td rowspan="9">
				<div class="text-center">
					<img src="${publication.image}" width="230" height="350" />
				</div>
			</td>
        </tr>
        <tr>
            <th>Description</th>
            <td><c:out value="${publication.description}"/></td>
        </tr>
         <tr>
            <th>User</th>
            <td><c:out value="${publication.user.username}"/></td>
        </tr>
 
         <tr>
            <th>Publication date</th>
            <td><petclinic:localDate date="${publication.publicationDate}" pattern="yyyy-MM-dd"/></td>
        </tr>
          
         
     
    </table>
     <c:if test="${propiedad2}"> 
    <a class="btn btn-default" href='<spring:url value="/publications/${publication.id}/updateForm" htmlEscape="true"/>'>Edit Publication</a>
	</c:if>
 	<c:if test="${propiedad}">
    <a class="btn btn-default" href='<spring:url value="/books/${publication.book.id}/delete/${publication.id}" htmlEscape="true"/>' onclick="return confirm('Are you sure you want to delete this publication?');">Delete Publication</a>
	</c:if>

</petclinic:layout>
