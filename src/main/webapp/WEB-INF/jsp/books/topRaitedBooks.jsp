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
<c:if test="${!empty selections}">
<h2>Top best raited books</h2>

		<table id="booksTable" class="table table-striped">

			<thead>
				<tr>
					<th style="width: 150px;">Title</th>
					<th style="width: 200px;">Author</th>
					<th style="width: 150px;">ISBN</th>
					<th style="width: 100px;">Raiting</th>
				</tr>
			</thead>
			<tbody>
			<c:set var="index" value="0"></c:set>
				<c:forEach items="${selections}" var="book">
					<tr>
						<td><spring:url value="/books/{bookId}" var="bookUrl">
								<spring:param name="bookId" value="${book.id}" />
							</spring:url> <a href="${fn:escapeXml(bookUrl)}"><c:out
									value="${book.title}" /></a></td>
						<td><c:out value="${book.author}" /></td>
						<td><c:out value="${book.ISBN}" /></td>
						<td>
						<div>
						<img style="position: absolute; z-index: 1" width="${raiting[index]}px" height="18px" src="<spring:url value="/resources/images/fondo.png" htmlEscape="true" />"/>
						<img title="${raiting[index]/20}/5.0" style="position: absolute; z-index: 2" width="100px" height="18px" src="<spring:url value="/resources/images/stars.png" htmlEscape="true" />"/>
						</div>
						</td>

					</tr>
					<c:set var="index" value="${index+1}"></c:set>
				</c:forEach>
			</tbody>
		</table>
		</c:if>
		<c:if test="${empty selections}">
	<h1><font size="15">Not enough data in the app yet</font></h1>
	</c:if>
</petclinic:layout>


			
