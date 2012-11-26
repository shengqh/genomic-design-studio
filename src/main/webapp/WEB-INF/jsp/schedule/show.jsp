<%@ include file="../include.jsp"%>
<html>
<head>
<%@ include file="../include_head.jsp"%>
</head>

<body>
	<jsp:include page="../menu.jsp" />
	<p>
	<h1 align="center">Genomic Design Studio : ${day.date}</h1>
	<p>
		<c:if test="${!empty day.users}">
			<div>
				<table id="box-table-a" summary="Registered Users">
					<thead>
						<tr>
							<th scope="col">First name</th>
							<th scope="col">Last name</th>
							<th scope="col">Email</th>
							<th scope="col">Department/Division</th>
							<th scope="col">Study PI</th>
							<sec:authorize access="hasRole('ROLE_VANGARD')">
								<th scope="col">IP address</th>
								<th scope="col">&nbsp;</th>
								<th scope="col">&nbsp;</th>
							</sec:authorize>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${day.users}" var="user">
							<tr>
								<td>${user.firstname}</td>
								<td>${user.lastname}</td>
								<td>${user.email}</td>
								<td>${user.department}</td>
								<td>${user.studyPI}</td>
								<sec:authorize access="hasRole('ROLE_VANGARD')">
									<td>${user.ipaddress}</td>
									<td><c:choose>
											<c:when test="${user.checkIn}">
												<form
													action="uncheckinscheduleuser?dayid=${day.id}&&userid=${user.id}"
													method="post">
													<input type="submit" value="uncheck in" />
												</form>
											</c:when>
											<c:otherwise>
												<form
													action="checkinscheduleuser?dayid=${day.id}&&userid=${user.id}"
													method="post">
													<input type="submit" value="check in" />
												</form>
											</c:otherwise>
										</c:choose></td>
									<td>
										<form
											action="deletescheduleuser?dayid=${day.id}&&userid=${user.id}"
											method="post">
											<input type="submit"
												value="<spring:message	code="label.delete" />"
												onclick="return confirm('Are you sure you want to delete?')" />
										</form>
									</td>
								</sec:authorize>
							</tr>
							<sec:authorize access="hasRole('ROLE_VANGARD')">
								<tr class="comment">
									<td>&nbsp;</td>
									<td colspan="7">${user.purpose}</td>
								</tr>
							</sec:authorize>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>
	<p class="message">${message}</p>
</body>
</html>
