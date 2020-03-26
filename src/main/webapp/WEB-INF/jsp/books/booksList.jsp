<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="books">
    <h2>Books</h2>

    <table id="booksTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Title</th>
            <th style="width: 200px;">Author</th>
            <th style="width: 200px;">Genre</th>
            <th style="width: 150px;">ISBN</th>
            <th style="width: 200px;">Pages</th>
            <th style="width: 100px;">Verified</th>
            <th style="width: 200px;">Synopsis</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections}" var="book">
            <tr>
                <td>
                    <spring:url value="/books/{bookId}" var="bookUrl">
                        <spring:param name="bookId" value="${book.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(bookUrl)}"><c:out value="${book.title}"/></a>
                </td>
                <td>
                    <c:out value="${book.author}"/>
                </td>
                <td>
                    <c:out value="${book.genre}"/>
                </td>
                 <td>
                    <c:out value="${book.ISBN}"/>
                </td>
                <td>
                    <c:out value="${book.pages}"/>
                </td>
                <td>
                <c:if test="${book.verified}">
                <c:out value="Yes"/>
                </c:if>
				<c:if test="${!book.verified}">
                <c:out value="No"/>
                </c:if>
                </td>
                <td>
                    <c:out value="${book.synopsis}"/>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
        <table id="booksTable" style="width: 20%;" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 100%;">Recomendations</th>

        </tr>
        </thead>
        <tbody>
        <c:forEach var = "i" begin ="0" end="2">
        <c:if test="${recomendations[i]!=null}">
            <tr>
                <td>
                    <spring:url value="/books/{bookId}" var="bookUrl">
                        <spring:param name="bookId" value="${recomendations[i].id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(bookUrl)}"><c:out value="${recomendations[i].title}"/></a>
                </td>
             
                
            </tr>
            </c:if>
        </c:forEach>
        </tbody>
    </table>
    
</petclinic:layout>
