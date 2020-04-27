<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<spring:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js" var="bootstrapJs"/>
<script src="${bootstrapJs}"></script>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<petclinic:layout pageName="itBooks">
<h2>Results:</h2>
    <c:if test="${empty itBooks}">
      <c:out value="No results..."/>
    </c:if>
    <div class="row" style="margin: 0 -5px;">
    <c:forEach items="${itBooks}" var="itBook">
      <div class="column" style="float: left; width: 25%; padding: 10px 10px;">
        <div class="card" style=" box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);padding: 16px;text-align: center;background-color: #f1f1f1;height: 550;">
          <div class="card-header">
            <h2>
            	<c:out value="${itBook.title}"/>
            </h2>
          </div>
          <div class="card-body">
            <img src="${itBook.image}" class="card-img-top" width="100" height="150">
            <h5 class="card-title"><em><c:out value="${itBook.url}" /></em></h5>
            <spring:url value="/itBooks/details/{isbn}" var="detailsUrl">
							<spring:param name="isbn" value="${itBook.isbn13}" />
						</spring:url>
						<a href="${fn:escapeXml(detailsUrl)}" class="btn btn-default">See more</a>
      </div>
       <div class="card-footer text-muted">
  </div>
    </div>
  </div>
        </c:forEach>
        </div>
   
</petclinic:layout>
