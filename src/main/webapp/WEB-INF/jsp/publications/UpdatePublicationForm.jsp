<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="publications">

    <jsp:body>

                    
              
        <form:form modelAttribute="publication"
                   class="form-horizontal"
                    action="/publications/update/${publication.id}">
            
           
           
            <div class="form-group has-feedback">
     
				
                <fmt:message var="title" key="title"/>
                <fmt:message var="image" key="image"/>
                <fmt:message var ="description" key="description"/>
                <petclinic:inputField label="${title}" name="title"/>
                <petclinic:inputField label="${image}" name="image"/>
                <petclinic:inputField label="${description}" name="description"/>
  
                
                <input type="hidden" name="id" id="id" value="${publication.id}"/>
                            
                <input type="hidden" name="id" value="${id}">
                  
                 
                 
              	
                
    
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    
                            <button class="btn btn-default" type="submit"><fmt:message key="updatePublication"/></button>
                 
                  
                </div>
            </div>
        </form:form>

    </jsp:body>
</petclinic:layout>
