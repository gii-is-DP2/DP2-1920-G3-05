<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">
			
				<petclinic:menuItem active="${name eq 'home'}" url="/"
					title="Home page">
					<span class="glyphicon glyphicon-home navbar-left" aria-hidden="true"></span>
					<span>Home</span>
				</petclinic:menuItem>
				
				<petclinic:menuItem active="${name eq 'books'}" url="/books/find"
					title="Find books">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span id="findBook">Search</span>
				</petclinic:menuItem>
				
				<petclinic:menuItem active="${name eq 'topRaited'}" url="/books/topRaited"
					title="Top raited books">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span id="topRaited">Top raited books</span>
				</petclinic:menuItem>
				
				<petclinic:menuItem active="${name eq 'readBooks'}" url="/books/readBooks"
					title="Read books">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Read books</span>
				</petclinic:menuItem>
				
				<petclinic:menuItem active="${name eq 'wishList'}" url="/books/wishList"
					title="To read list">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>To read list</span>
				</petclinic:menuItem>
				<petclinic:menuItem active="${name eq 'recomendations'}" url="/books/recomendations"
					title="Recomendations">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Recomendations</span>
				</petclinic:menuItem>
				
				<petclinic:menuItem active="${name eq 'topReadBooks'}" url="/books/topRead"
					title="Top read books">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Top read books</span>
				</petclinic:menuItem>

				<petclinic:menuItem active="${name eq 'meetings'}" url="/meetings/find"
					title="Meetings">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Meetings</span>
				</petclinic:menuItem>

				<petclinic:menuItem active="${name eq 'IT Section'}" url="/itBooks/find"
					title="IT Section">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>IT Section</span>
				</petclinic:menuItem>

			</ul>




			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
						<span class="glyphicon glyphicon-chevron-down"></span></a>
						<ul class="dropdown-menu" >
						<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-8">
											<p class="text-left">
												<strong>Options</strong>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
 							
							<li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-8">
											<p class="text-left">
												<a href="<c:url value="/login" />" class="btn btn-primary btn-block btn-sm">Login</a>
											</p>
										</div>
									</div>
								</div>
							</li>

							<li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-8">
											<p class="text-left">
												<a href="<c:url value="/users/new" />" class="btn btn-primary btn-block btn-sm">Register</a>
											</p>
										</div>
									</div>
								</div>
							</li>
					</ul>
					</li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
							 <span class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="/users/update" class="btn btn-primary btn-block">My Profile</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<sec:authorize access="hasAuthority('admin')">
							<li> 
								<div class="navbar-login navbar-login-admin">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="/admin/listUsers" class="btn btn-primary btn-block">List users</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li> 
								<div class="navbar-login navbar-login-admin">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="/admin/meetingDashboard" class="btn btn-primary btn-block">Meeting dashboard</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							</sec:authorize>
						</ul></li>
				</sec:authorize>
			</ul>
		</div>

</nav>
