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
<h2>
       News <c:if test="${AllNews}">from books I've reviewed</c:if>
    </h2>
    <div align="right">
    <c:if test="${AllNews}"><a class="btn btn-default" href='<spring:url value="/news" htmlEscape="true"/>'>All news</a></c:if>
     <c:if test="${NewsRec}"><a class="btn btn-default" href='<spring:url value="/" htmlEscape="true"/>'>Recommended news</a></c:if>
<sec:authorize access="hasAuthority('admin')">
     <a class="btn btn-default" style="margin: 10px" href='<spring:url value="/admin/news/create" htmlEscape="true"/>'>Add New</a>
     <br><br>
	</sec:authorize>
	 </div>
    <div class="row" style="margin: 0 -5px;">
    <c:forEach items="${news}" var="neew">
  <div class="column" style="float: left; width: 25%; padding: 10px 10px;">
    <div class="card" style=" box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);padding: 16px;text-align: center;background-color: #f1f1f1;height: 550;">
    <div class="card-header">
   <h2>
   <sec:authorize access="hasAuthority('admin')">
  <spring:url value="/admin/news/{newId}" var="newUrl">
     <spring:param name="newId" value="${neew.id}"/>
  </spring:url>
  <a href="${fn:escapeXml(newUrl)}"><c:out value="${neew.head}"/></a>
  </sec:authorize>
  <sec:authorize access="!hasAuthority('admin')">
  	<c:out value="${neew.head}"/>
  </sec:authorize>
  </h2>
    </div>
      <div class="card-body">
      <img src="${neew.img}" class="card-img-top" width="100" height="150">
        <h5 class="card-title"><em><c:out value="${neew.tags}" /></em></h5>
        <p class="card-text"><c:out value="${neew.body}" /></p>
      </div>
       <div class="card-footer text-muted">
    <c:out value="${neew.redactor}" /><br>
    <petclinic:localDate date="${neew.fecha}" pattern="yyyy-MM-dd"/>
  </div>
    </div>
  </div>
        </c:forEach>
        </div>
   
</petclinic:layout>
