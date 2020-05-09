<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="books">
	<c:if test="${!empty selections}">
	<h2>Readers</h2>
	
		<table id="readersTable" class="table table-striped">

			<thead>
				<tr>
					<th style="width: 150px;">First Name</th>
					<th style="width: 200px;">Last Name</th>
					<th style="width: 100px;">Username</th>
					<th style="width: 100px;">Verified</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${selections}" var="reader">
					<tr>
						<td><c:out value="${reader.firstName}" /></td>
						<td><c:out value="${reader.lastName}" /></td>
						<td><c:out value="${reader.user.username}" /></td>
						<td><c:if test="${reader.verified}">
								<c:out value="Yes	" />
							</c:if> <c:if test="${!reader.verified}">
								<c:out value="No	" />
							</c:if>
						<c:if test="${reader.verified}">
						<a class="btn btn-default"
							onclick="alert('This user is already verified')">Verify user</a>
				</c:if> <c:if test="${!reader.verified}">
						<spring:url value="/admin/users/{userId}/verify" var="editUrl">
							<spring:param name="userId" value="${reader.id}" />
						</spring:url>
						<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Verify
							user</a>
				</c:if></td>


					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

	<c:if test="${empty selections}">
	<h1><font size="15">There are no readers yet!</font></h1>
	</c:if>

</petclinic:layout>