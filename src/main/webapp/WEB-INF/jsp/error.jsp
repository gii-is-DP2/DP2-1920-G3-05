<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">

	<blockquote class="blockquote text-center">
	  <p class="mb-0">UPS!</p>
  	<h2>Something happened...</h2>
    <p>${exception.message}</p>
	 <a class="nav-link" href="/">Return to home page</a>
	</blockquote>
	</petclinic:layout>
