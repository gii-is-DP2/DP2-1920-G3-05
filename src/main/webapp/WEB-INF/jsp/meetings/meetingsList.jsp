<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="meetings list">
    <h2 id="page title">Meetings</h2>

    <c:if test="${empty meetings}">
        <h2>No meetings found</h2>
    </c:if>
    <c:if test="${not empty meetings}">
    <table id="meetingsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Name</th>
            <th style="width: 200px;">Place</th>
            <th style="width: 200px;">Start</th>
            <th style="width: 150px;">End</th>
            <th style="width: 200px;">Capacity</th>
            <th style="width: 100px;">Book</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${meetings}" var="meeting">
            <tr>
                <td>
                    <spring:url value="/meetings/{meetingId}" var="meetingUrl">
                        <spring:param name="meetingId" value="${meeting.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(meetingUrl)}"><c:out value="${meeting.name}"/></a>
                </td>
                <td>
                    <c:out value="${meeting.place}"/>
                </td>
                <td>
                    <c:out value="${meeting.start}"/>
                </td>
                 <td>
                    <c:out value="${meeting.end}"/>
                </td>
                <td>
                    <c:out value="${meeting.capacity}"/>
                </td>
                <td>
                    <c:out value="${meeting.book.title}"/>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

    
</petclinic:layout>
