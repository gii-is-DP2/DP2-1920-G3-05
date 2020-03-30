<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="news">
 <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#fecha").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
    <form:form modelAttribute="new" class="form-horizontal">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Head" name="head"/>
            <petclinic:inputField label="Name" name="name"/>
            <petclinic:inputField label="Body" name="body"/>
            <petclinic:inputField label="Redactor" name="redactor"/>
            <petclinic:inputField label="Tags" name="tags"/>
            <petclinic:inputField label="Image" name="img"/>
            <petclinic:inputField label="Publication date" name="fecha"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${addNew}">
                       <button class="btn btn-default" type="submit">Create New</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update New</button>
                        <c:if test="${hasErrors==null||!hasErrors}">      
                        	<a class="btn btn-default" href='<spring:url value="/admin/news/delete/${newId}" htmlEscape="true"/>' onclick="return confirm('Are you sure you want to delete this new?');">Delete New</a>
                        	<a class="btn btn-default" href='<spring:url value="/admin/news/books/${newId}" htmlEscape="true"/>'>Books related to the new</a>
                    	</c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
    </jsp:body>
</petclinic:layout>
