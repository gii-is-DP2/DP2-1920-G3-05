<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<petclinic:layout pageName="books">

	<h2 id="bookDetail">Book Information</h2>

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
			<td id="authorBook"><c:out value="${book.author}" /></td>
		</tr>
		<tr>
			<th>Genre</th>
			<td><c:out value="${book.genre}" /></td>
		</tr>
		<tr>
			<th>ISBN</th>
			<td><c:out value="${book.ISBN}" /></td>
		</tr>
		<tr>
			<th>Pages</th>
			<td><c:out value="${book.pages}" /></td>
		</tr>
		<tr>
			<th>Synopsis</th>
			<td><c:out value="${book.synopsis}" /></td>
		</tr>
		<tr>
			<th>Editorial</th>
			<td><c:out value="${book.editorial}" /></td>
		</tr>
		<tr>
			<th>Publication date</th>
			<td><petclinic:localDate date="${book.publicationDate}"
					pattern="yyyy-MM-dd" /></td>
		</tr>
		<tr>
			<th>Verified</th>
			<td><c:if test="${book.verified}">
					<c:out value="Yes" />
					<sec:authorize access="hasAuthority('admin')">
						<a class="btn btn-default"
							onclick="alert('This book is already verified')">Verify book</a>
					</sec:authorize>
				</c:if> <c:if test="${!book.verified}">
					<c:out value="No" />
					<sec:authorize access="hasAuthority('admin')">
						<spring:url value="/admin/books/{bookId}/verify" var="editUrl">
							<spring:param name="bookId" value="${book.id}" />
						</spring:url>
						<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Verify
							book</a>
					</sec:authorize>
				</c:if></td>
		</tr>



	</table>

	<c:if test="${propiedad}">
		<a class="btn btn-default"
			href='<spring:url value="/books/${book.id}/updateForm" htmlEscape="true"/>'>Edit
			Book</a>
	</c:if>

	<sec:authorize access="hasAuthority('admin')">
		<a class="btn btn-default"
			href='<spring:url value="/admin/books/delete/${book.id}" htmlEscape="true"/>'
			onclick="return confirm('Are you sure you want to delete this book?');" id="deleteBook">Delete Book</a>
		<c:if test="${book.verified}">
		<a class="btn btn-default"
			href='<spring:url value="/admin/books/${book.id}/meetings/new" htmlEscape="true"/>'>
			Create meeting</a>
		</c:if>
		<c:if test="${!book.verified}">
			<a class="btn btn-default"
			onclick="alert('In order to create a meeting the book must be verified')">Create meeting</a>
		</c:if>
	</sec:authorize>

	<c:if test="${noEsReadBook}">
		<form:form modelAttribute="book" class="form-horizontal"
			action="/books/readBooks/${book.id}">
			<button class="btn btn-default" type="submit">
				<fmt:message key="addReadBook" />
			</button>
		</form:form>
	</c:if>

	<c:choose>
		<c:when test="${canWriteReview}">
			<a class="btn btn-default"
				href='<spring:url value="/books/${book.id}/reviews/new" htmlEscape="true"/>'>Write
				review</a>
		</c:when>
		<c:otherwise>
			<a class="btn btn-default"
				onclick="alert('In order to write a review you must have read the book and you can only review the same book once')">Write
				review</a>
		</c:otherwise>
	</c:choose>


	<c:if test="${hasAnyReview}">
		<a class="btn btn-default" id="Go to the reviews"
			href='<spring:url value="/books/${book.id}/reviews" htmlEscape="true"/>'>Go
			to the reviews</a>
	</c:if>

	<c:choose>
		<c:when test="${notWishedBook and noEsReadBook}">
			<form:form modelAttribute="book" class="form-horizontal"
				action="/books/wishList/${book.id}">
				<button class="btn btn-default" type="submit">
					<fmt:message key="addWishedBook" />
				</button>
			</form:form>
		</c:when>
		<c:otherwise>
			<a class="btn btn-default"
				onclick="alert('You have already read this book!')">Add to wish
				list</a>
		</c:otherwise>
	</c:choose>
	<br>
	<h1>Fan Zone</h1>
	<c:if test="${!noEsReadBook}">
		<a class="btn btn-default"
			href='<spring:url value="/books/${book.id}/publications/publicationAdd" htmlEscape="true"/>'>New
			Fan Publications</a>
	</c:if>
	<a class="btn btn-default"
		href='<spring:url value="/books/${book.id}/publications" htmlEscape="true"/>'>Fans
		Publications</a>








</petclinic:layout>
