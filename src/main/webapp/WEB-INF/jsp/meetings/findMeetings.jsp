<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<petclinic:layout pageName="meetings">

    <h2>Find Meetings</h2>

    
    <form:form modelAttribute="meeting" action="/meetings" method="get" class="form-horizontal"
               id="search-meeting-form">
        <div class="form-group">
            <div class="control-group" id="name">
                <label class="col-sm-2 control-label"> </label>
                <div class="col-sm-10">
                    <form:input class="form-control" path="name" size="30" maxlength="80" placeholder="Search by name, place or book title"/>
                    <span class="help-inline"><form:errors path="*"/></span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Find Meeting</button>
            </div>
        </div>
    </form:form>
    <br><br>
    <div class="card" align="center">
  <div class="card-body">
    <blockquote class="blockquote mb-0">
      <p><c:out value="${quote.content}"></c:out></p>
      <footer class="blockquote-footer">
        <cite><c:out value="${quote.author}"></c:out></cite>
      </footer>
    </blockquote>
  </div>
  </div>	
</petclinic:layout>
