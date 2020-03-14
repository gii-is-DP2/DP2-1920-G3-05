<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<petclinic:layout pageName="books">

	<h2>Book Information</h2>

	<table class="table table-striped">
		<tr>
			<th>Title</th>
			<td><b><c:out value="${book.title}" /></b></td>
			<td rowspan="9">
				<div class="text-center">
					<img src="${book.image}" width="230" height="350" />
				</div>
			</td>
        </tr>
        <tr>
            <th>Author</th>
            <td><c:out value="${book.author}"/></td>
        </tr>
        <tr>
            <th>Genre</th>
            <td><c:out value="${book.genre}"/></td>
        </tr>
         <tr>
            <th>ISBN</th>
            <td><c:out value="${book.ISBN}"/></td>
        </tr>
         <tr>
            <th>Pages</th>
            <td><c:out value="${book.pages}"/></td>
        </tr>
         <tr>
            <th>Synopsis</th>
            <td><c:out value="${book.synopsis}"/></td>
        </tr>
        <tr>
            <th>Editorial</th>
            <td><c:out value="${book.editorial}"/></td>
        </tr>
         <tr>
            <th>Publication date</th>
            <td><petclinic:localDate date="${book.publicationDate}" pattern="yyyy-MM-dd"/></td>
        </tr>
        <tr>
           			<th>Verified</th>
			<td><c:if test="${book.verified}">
					<c:out value="Yes" />
				</c:if> <c:if test="${!book.verified}">
					<c:out value="No" />
					<sec:authorize access="hasAuthority('admin')">
						<spring:url value="/admin/books/{bookId}/verify" var="editUrl">
							<spring:param name="bookId" value="${book.id}" />
						</spring:url>
						<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Verify book</a>
					</sec:authorize>
				</c:if></td>
		</tr>
          <c:if test="${propiedad}">  <tr>
            <th>Edit</th>
            
            <td><form action="/books/${book.id}/updateForm">
    			<input type="submit" value="Edit Book" />
				</form>	</td>
        </tr></c:if>
    </table>
    
    <sec:authorize access="hasAuthority('admin')">
     <a class="btn btn-default" href='<spring:url value="/admin/books/delete/${book.id}" htmlEscape="true"/>' onclick="return confirm('Are you sure you want to delete this book?');">Delete Book</a>
	</sec:authorize>
	

</petclinic:layout>
