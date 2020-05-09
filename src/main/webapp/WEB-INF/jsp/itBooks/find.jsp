<%@page pageEncoding="UTF-8"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<petclinic:layout pageName="findItBooks">

    <h2>Find IT related books</h2>

    <form:form modelAttribute="itBook" action="/itBooks" method="get" class="form-horizontal"
               id="search-it-book-form">
        <div class="form-group">
            <div class="control-group" id="title">
                <label class="col-sm-2 control-label"> </label>
                <div class="col-sm-10">
                    <form:input class="form-control" path="title" size="30" maxlength="80" placeholder="Search by title, author, keyword or ISBN"/>
                    <span class="help-inline"><form:errors path="*"/></span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default" id="findBookButton">Find IT Book</button>
            </div>
        </div>

    </form:form>

</petclinic:layout>
