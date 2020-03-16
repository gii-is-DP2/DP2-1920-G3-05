<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="books">

    <jsp:body>

                    
              
        <form:form modelAttribute="book"
                   class="form-horizontal"
                    action="/books/update/${book.id}">
            
           
           
            <div class="form-group has-feedback">
     
				
                <fmt:message var="title" key="title"/>
                <fmt:message var="image" key="image"/>
                <fmt:message var = "author" key="author"/>
                 <fmt:message var="book.genre.name" key="book.genre.name"/>
                <fmt:message var="ISBN" key="ISBN"/>
                <fmt:message var = "pages" key="pages"/>
                  <fmt:message var="synopsis" key="synopsis"/>
                <fmt:message var="editorial" key="editorial"/>
                <fmt:message var = "publicationDate" key="publicationDate"/>
                <petclinic:inputField label="${title}" name="title"/>
                <petclinic:inputField label="${image}" name="image"/>
                 <petclinic:inputField label="${author}" name="author"/>
                 <div class="control-group">
                    <petclinic:selectField name="genre.name" label="genre" names="${genres}" size="5"/>
                </div>
                
                <input type="hidden" name="id" id="id" value="${book.id}"/>
                
                <petclinic:inputField label="${ISBN}" name="ISBN"/>
                 <petclinic:inputField label="${pages}" name="pages"/>
                 <petclinic:inputField label="${synopsis}" name="synopsis"/>
                <petclinic:inputField label="${editorial}" name="editorial"/>
                 <petclinic:inputField label="${publicationDate}" name="publicationDate"/>
                
                <input type="hidden" name="id" value="${id}">
                  
                 
                 
              	
                
    
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    
                            <button class="btn btn-default" type="submit"><fmt:message key="updateBook"/></button>
                 
                  
                </div>
            </div>
        </form:form>

    </jsp:body>
</petclinic:layout>