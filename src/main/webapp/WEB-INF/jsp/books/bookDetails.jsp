<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="books">

    <h2>Book Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Title</th>
            <td><b><c:out value="${book.title}"/></b></td>
        </tr>
        <tr>
            <th>Author</th>
            <td><c:out value="${book.author}"/></td>
        </tr>
        <tr>
            <th>Genre</th>
            <td><c:out value="${book.genre}"/></td>
        </tr>
         <tr>
            <th>ISBN</th>
            <td><c:out value="${book.ISBN}"/></td>
        </tr>
         <tr>
            <th>Pages</th>
            <td><c:out value="${book.pages}"/></td>
        </tr>
         <tr>
            <th>Synopsis</th>
            <td><c:out value="${book.synopsis}"/></td>
        </tr>
        <tr>
            <th>Editorial</th>
            <td><c:out value="${book.editorial}"/></td>
        </tr>
         <tr>
            <th>Publication date</th>
            <td><petclinic:localDate date="${book.publicationDate}" pattern="yyyy-MM-dd"/></td>
        </tr>
    </table>

</petclinic:layout>
