<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<spring:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js" var="bootstrapJs"/>
<script src="${bootstrapJs}"></script>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<petclinic:layout pageName="news">
<div align="right">
    <a class="btn btn-default" href='<spring:url value="/admin/news/books/save/${newId}" htmlEscape="true"/>'>Save</a>
    <br><br>
    </div>
<table id="booksTable" class="table table-striped">
<tbody>
<tr>
<td width="50%">
<h2 align="center">Books related to the new</h2>
<table id="booksTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 65%;">Title</th>
            <th style="width: 30%;">Author</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${booksIncludes}" var="bookInclude">
            <tr>
                <td><c:out value="${bookInclude.title}"/>
                </td>
                <td>
                    <c:out value="${bookInclude.author}"/>
                </td>
                <td>
                    <spring:url value="/admin/news/books/delete/{newId}/{bookId}" var="bookUrl">
                    <spring:param name="newId" value="${newId}"/>
                        <spring:param name="bookId" value="${bookInclude.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(bookUrl)}">
                    <img width="10px" height="10px" src="<spring:url value="/resources/images/delete.png" htmlEscape="true" />"/>
                    
                </a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${booksNotEmpty}">
            <tr>
                <td colspan="2">
            <div style="color: #FF0000;">
            <c:out value="Must include at least one book"/>
            </div>
            </td>
            </tr>
            </c:if>
        </tbody>
    </table>
    </td>
    <td width="50%">
 <h2 align="center">Other books</h2>
<table id="booksTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 65%;">Title</th>
            <th style="width: 30%;">Author</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${booksNotIncludes}" var="booksNotInclude">
            <tr>
                <td><c:out value="${booksNotInclude.title}"/>
                </td>
                <td>
                    <c:out value="${booksNotInclude.author}"/>
                </td>
                <td>
                    <spring:url value="/admin/news/books/add/{newId}/{bookId}" var="bookUrl">
                    <spring:param name="newId" value="${newId}"/>
                        <spring:param name="bookId" value="${booksNotInclude.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(bookUrl)}">
                    <img width="10px" height="10px" src="<spring:url value="/resources/images/add.png" htmlEscape="true" />"/>
                    
                </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </td>
    </tr>
    </tbody>
    </table>
</petclinic:layout>
