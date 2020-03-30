<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="publications">
    <h2>Publications</h2>

    <table id="publicationsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Title</th>
            <th style="width: 200px;">Description</th>
            <th style="width: 150px;">User</th>
            <th style="width: 150px;">Publication Date</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections}" var="publication">
            <tr>
                <td>
                    <spring:url value="/publications/{publicationId}" var="publicationUrl">
                        <spring:param name="publicationId" value="${publication.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(publicationUrl)}"><c:out value="${publication.title}"/></a>
                </td>
                <td>
                    <c:out value="${publication.description}"/>
                </td>
                <td>
                    <c:out value="${publication.user.username}"/>
                </td>
                 <td>
                    <c:out value="${publication.publicationDate}"/>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
