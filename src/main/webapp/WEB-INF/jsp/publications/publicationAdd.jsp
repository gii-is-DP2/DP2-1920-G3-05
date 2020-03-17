<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="publications">
    <jsp:body>
    <h2>
        <c:if test="${publication['new']}">New </c:if> Publication
    </h2>
    <form:form modelAttribute="publication" class="form-horizontal" action="/books/${publication.book.id}/publications/save"> 
        <div class="form-group has-feedback">
            <petclinic:inputField label="Tittle" name="title"/>
            <petclinic:inputField label="Description" name="description"/>
            <petclinic:inputField label="Image" name="image"/>
            
        </div>
         <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${publication['new']}">
                            <button class="btn btn-default" type="submit">Add Publication</button>
                        </c:when>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <c:if test="${!publication['new']}">
        </c:if>
    </jsp:body>
</petclinic:layout>