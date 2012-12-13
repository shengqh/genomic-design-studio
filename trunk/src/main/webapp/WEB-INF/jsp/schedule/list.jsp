<%@ include file="../include.jsp"%>
<html>
<head>
<%@ include file="../include_head.jsp"%>
</head>

<body>
	<jsp:include page="../menu.jsp" />

	<p>
	<h1 align="center">
		Studio Dates
		<sec:authorize access="hasRole('ROLE_ADMIN')">
								| <a href="export">Export All</a>
		</sec:authorize>
	</h1>

	<p>
	<p class="message">${message}</p>
	<c:if test="${!empty days}">
		<c:set var="haspassed" value="0" />
		<table id="box-table-a" summary="Schedule list">
			<thead>
				<tr>
					<th scope="col">Date</th>
					<th scope="col">Registered Users</th>
					<th scope="col">&nbsp;</th>
					<sec:authorize access="hasRole('ROLE_ADMIN')">
						<th scope="col">&nbsp;</th>
						<th scope="col">&nbsp;</th>
					</sec:authorize>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${days}" var="day">
					<c:if test="${day.passed}">
						<c:set var="haspassed" value="1" />
					</c:if>

					<c:if test="${!day.passed}">
						<tr>
							<td><a href="showday?dayid=${day.id}">${day.date}</a></td>
							<td>${day.registeredNumber}</td>
							<c:choose>
								<c:when test="${day.canRegister}">
									<td>
										<form action="addscheduleuser?dayid=${day.id}" method="post">
											<input type="submit" value="register" />
										</form>
									</td>
								</c:when>
								<c:otherwise>
									<td>full</td>
								</c:otherwise>
							</c:choose>
							<sec:authorize access="hasRole('ROLE_ADMIN')">
								<td>
									<form action="deleteday?dayid=${day.id}" method="post">
										<input type="submit"
											value="<spring:message	code="label.delete" />"
											onclick="return confirm('Are you sure you want to delete?')" />
									</form>
								</td>
								<td>
									<form action="export?dayid=${day.id}" method="post">
										<input type="submit" value="Export" />
									</form>
								</td>
							</sec:authorize>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

	<sec:authorize access="hasRole('ROLE_VANGARD')">
		<c:if test='${haspassed == "1"}'>
			<h1 align="center">
				Finished Dates | <a href="export?dayid=-1">Export Record </a>
			</h1>
			<p>
			<table id="box-table-a" summary="Schedule list">
				<thead>
					<tr>
						<th scope="col">Schedule Date</th>
						<th scope="col">Registered User</th>
						<th scope="col">Attendee</th>
						<sec:authorize access="hasRole('ROLE_ADMIN')">
							<th scope="col">&nbsp;</th>
							<th scope="col">&nbsp;</th>
						</sec:authorize>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${days}" var="day">
						<c:if test="${day.passed}">
							<tr>
								<td><a href="showday?dayid=${day.id}">${day.date}</a></td>
								<td>${day.registeredNumber}</td>
								<td>${day.attendeeNumber}</td>
								<sec:authorize access="hasRole('ROLE_ADMIN')">
									<td>
										<form action="addscheduleuser?dayid=${day.id}" method="post">
											<input type="submit" value="register" />
										</form>
									</td>
									<td>
										<form action="export?dayid=${day.id}" method="post">
											<input type="submit" value="Export" />
										</form>
									</td>
								</sec:authorize>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</sec:authorize>
</body>
</html>
