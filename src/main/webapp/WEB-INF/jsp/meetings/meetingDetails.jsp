<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<petclinic:layout pageName="meeting">
<div style="color:red"><c:out value="${mensaje}"></c:out></div>
	<h2>Meeting Information</h2>

	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><b><c:out value="${meeting.name}" /></b></td>
		</tr>
		<tr>
			<th>Place</th>
			<td><c:out value="${meeting.place}" /></td>
		</tr>
		<tr>
			<th>Start</th>
			<td><c:out value="${meeting.start}" /></td>
		</tr>
		<tr>
			<th>End</th>
			<td><c:out value="${meeting.end}" /></td>
		</tr>
		<tr>
			<th>Capacity</th>
			<td><c:out value="${meeting.capacity}" /></td>
		</tr>
		<tr>
			<th>Book</th>
			<td><c:out value="${meeting.book.title}" /></td>
		</tr>
	</table>
	<c:choose>
		<c:when test="${canInscribe}">
			<a class="btn btn-default"
				href='<spring:url value="/meetings/${meeting.id}/inscribe" htmlEscape="true"/>'>Inscribe</a>
		</c:when>
		<c:otherwise>
			<a class="btn btn-default"
				onclick="alert('In order to inscribe a meeting you must have read the book and you can only attend one meeeting at the same time, it cant be in the past and there must be capacity')">Inscribe</a>
		</c:otherwise>
	</c:choose>
				
	<c:if test="${suscribed}">
	<a class="btn btn-default"
			href='<spring:url value="/meetings/${meeting.id}/unsuscribe" htmlEscape="true"/>'
			>Unsubscribe</a>
		
	</c:if>
	
	
</petclinic:layout>
