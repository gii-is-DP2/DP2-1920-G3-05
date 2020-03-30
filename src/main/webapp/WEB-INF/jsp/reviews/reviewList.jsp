<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="reviews">
	<h2>Reviews of ${reviews[0].book.title}</h2>

	<table id="reviewsTable" class="table table-striped">
		<thead>
			<tr>
				<th style="width: 150px;">Title</th>
				<th style="width: 200px;">Writed by</th>
				<th style="width: 150px;">Rating</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${reviews}" var="review">
				<tr>
					<td><spring:url value="/reviews/{reviewId}" var="reviewUrl">
							<spring:param name="reviewId" value="${review.id}" />
						</spring:url> <a href="${fn:escapeXml(reviewUrl)}"><c:out
								value="${review.title}" /></a></td>
					<td><c:out value="${review.user.username}" /></td>
					<td><c:out value="${review.raiting}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<a class="btn btn-default"
		href='<spring:url value="/books/${reviews[0].book.id}" htmlEscape="true"/>'>Return to book</a>


</petclinic:layout>
