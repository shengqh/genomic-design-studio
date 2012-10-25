<%@ include file="include.jsp"%>

<c:url value="home" var="homeUrl" />
<c:url value="showlist" var="scheduleUrl" />
<c:url value="user" var="userUrl" />
<c:url value="alluser" var="userAllUrl" />
<c:url value="changeownpassword" var="passwordUrl" />
<c:url value="logout" var="logoutUrl" />

<div class="menu">
	<ul>
		<li><a href="${homeUrl}">Home</a></li>

		<li><a href="${scheduleUrl}">Schedule</a></li>

		<%-- 
		<sec:authorize access="hasRole('ROLE_USER')">
			<sec:authorize ifAnyGranted="ROLE_ADMIN">
				<li><a href="${userAllUrl}">User</a></li>
			</sec:authorize>
			<sec:authorize ifNotGranted="ROLE_ADMIN">
				<li><a href="${userUrl}">User</a></li>
			</sec:authorize>
		</sec:authorize>

		<li><a href="${passwordUrl}">Change Password</a></li>

		<li><a href="${logoutUrl}">Logout</a></li>
		--%>
	</ul>
	<span id="menu-username"><%=SecurityContextHolder.getContext().getAuthentication()
					.getName()%></span> <br style="clear: left" />
</div>