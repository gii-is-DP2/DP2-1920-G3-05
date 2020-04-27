<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="ItBookDetails">

	<h2>IT Book details</h2>

	<table class="table table-striped">
		<tr>
			<th>Title</th>
            <td><c:out value="${itBook.title}" /></td>
            <td rowspan="9">
				<div class="text-center">
					<img src="${itBook.image}" width="230" height="350" />
				</div>
			</td>
		</tr>
		<tr>
			<th>Subtitle</th>
			<td><c:out value="${itBook.subtitle}" /></td>
		</tr>
		<tr>
			<th>Writed by</th>
			<td><c:out value="${itBook.authors}" /></td>
		</tr>
		<tr>
			<th>Published by</th>
			<td><c:out value="${itBook.publisher}" /></td>
		</tr>
		<tr>
			<th>Language</th>
			<td><c:out value="${itBook.language}" /></td>
        </tr>
        <tr>
			<th>Isbn 10</th>
			<td><c:out value="${itBook.isbn10}" /></td>
        </tr>
        <tr>
			<th>Isbn 13</th>
			<td><c:out value="${itBook.isbn13}" /></td>
        </tr>
        <tr>
			<th>Pages</th>
			<td><c:out value="${itBook.pages}" /></td>
        </tr>
        <tr>
			<th>Year</th>
			<td><c:out value="${itBook.year}" /></td>
        </tr>
        <tr>
			<th>Rating</th>
			<td><c:out value="${itBook.rating}" /></td>
        </tr>
        <tr>
			<th>Description</th>
			<td><c:out value="${itBook.desc}" /></td>
        </tr>
        <tr>
			<th>Source</th>
			<td><c:out value="${itBook.url}" /></td>
        </tr>
	</table>

	<a class="btn btn-default"
		href='<spring:url value="/itBooks/find" htmlEscape="true"/>'>Return</a>

</petclinic:layout>
