<%@ include file="include.jsp"%>

<c:url value="home" var="homeUrl" />
<c:url value="showlist" var="scheduleUrl" />
<c:url value="showlog" var="logUrl" />
<c:url value="user" var="userUrl" />
<c:url value="alluser" var="userAllUrl" />
<c:url value="changeownpassword" var="passwordUrl" />
<c:url value="logout" var="logoutUrl" />
<c:url value="login" var="loginUrl" />

<div class="menu">
	<ul>
		<li><a href="${homeUrl}">Home</a></li>

		<li><a href="${scheduleUrl}">Schedule</a></li>

		<sec:authorize access="hasRole('ROLE_VANGARD')">
			<sec:authorize ifAnyGranted="ROLE_ADMIN">
				<li><a href="${userAllUrl}">User</a></li>
				<li><a href="${logUrl}">Log</a></li>
			</sec:authorize>
			<sec:authorize ifNotGranted="ROLE_ADMIN">
				<li><a href="${userUrl}">User</a></li>
			</sec:authorize>
		</sec:authorize>

		<sec:authorize access="hasRole('ROLE_USER')">
			<li><a href="${passwordUrl}">Change Password</a></li>
			<li><a href="${logoutUrl}">Logout</a></li>
		</sec:authorize>

		<sec:authorize ifNotGranted="ROLE_USER">
			<li><a href="${loginUrl}">Admin</a></li>
		</sec:authorize>
	</ul>
	<sec:authorize access="hasRole('ROLE_USER')">
		<span id="menu-username"><%=SecurityContextHolder.getContext()
						.getAuthentication().getName()%></span>
		<br style="clear: left" />
	</sec:authorize>
	<sec:authorize ifNotGranted="ROLE_USER">
		<span id="menu-username">Guest</span>
		<br style="clear: left" />
	</sec:authorize>
</div>