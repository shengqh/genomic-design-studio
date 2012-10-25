<%@ include file="../include.jsp"%>
<html>
<head>
<%@ include file="../include_head.jsp"%>
</head>

<body>
	<jsp:include page="../menu.jsp" />

	<p>
	<h1 align="center">
		<spring:message code="label.schedulelist" />
	</h1>
	<p>

		<c:if test="${!empty days}">
			<table id="box-table-a" summary="Schedule list">
				<thead>
					<tr>
						<th scope="col">Schedule Date</th>
						<th scope="col">Registered User</th>
						<th scope="col">Attendee</th>
						<th scope="col">&nbsp;</th>
						<sec:authorize access="hasRole('ROLE_ADMIN')">
							<th scope="col">&nbsp;</th>
						</sec:authorize>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${days}" var="day">
						<tr>
							<td><a href="showday?dayid=${day.id}">${day.date}</a></td>
							<td>${day.registeredNumber}</td>
							<td>${day.attendeeNumber}</td>
							<c:choose>
								<c:when test="${day.passed}">
									<td></td>
								</c:when>
								<c:otherwise>
									<td>
										<form action="addscheduleuser/dayid=${day.id}">
											<input type="submit" value="register" />
										</form>
									</td>
								</c:otherwise>
							</c:choose>
							<sec:authorize access="hasRole('ROLE_ADMIN')">
								<td>
									<form action="deleteday/${day.id}">
										<input type="submit"
											value="<spring:message	code="label.delete" />"
											onclick="return confirm('Are you sure you want to delete?')" />
									</form>
								</td>
							</sec:authorize>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	<p class="message">${message}</p>
</body>
</html>
