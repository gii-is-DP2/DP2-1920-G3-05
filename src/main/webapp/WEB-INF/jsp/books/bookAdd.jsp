<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="books">
	 <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#publicationDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
    <h2>
        <c:if test="${book['new']}">New </c:if> Book
    </h2>
    <form:form modelAttribute="book" class="form-horizontal" action="/books/save"> 
        <div class="form-group has-feedback">
            <petclinic:inputField label="Tittle" name="title"/>
            <petclinic:inputField label="Author" name="author"/>
            <petclinic:inputField label="Editorial" name="editorial"/>
             <div class="control-group">
                    <petclinic:selectField name="genre" label="Genre " names="${genres}" size="5"/> 
            </div>
            <petclinic:inputField label="Pages" name="pages"/>
            <petclinic:inputField label="Synopsis" name="synopsis"/>
            <petclinic:inputField label="ISBN" name="ISBN"/>
            <petclinic:inputField label="Publication Date" name="publicationDate"/>
            <petclinic:inputField label="Image" name="image"/>
            
        </div>
         <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${book['new']}">
                            <button class="btn btn-default" type="submit">Add Book</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Update Book</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <c:if test="${!book['new']}">
        </c:if>
    </jsp:body>
</petclinic:layout>