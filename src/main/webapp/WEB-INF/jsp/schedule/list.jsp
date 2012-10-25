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
						<th scope="col">&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${days}" var="day">
						<tr>
							<td><a href="showday?dayid=${day.id}">${day.date}</a></td>
							<td>${day.registeredNumber}</td>
							<c:choose>
								<c:when test="${day.passed}">
									<td>finished</td>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${day.registeredNumber >= 15}">
											<td>full</td>
										</c:when>
										<c:otherwise>
											<td><a href="addscheduleuser?dayid=${day.id}">register</a>
											</td>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	<p class="message">${message}</p>
</body>
</html>
