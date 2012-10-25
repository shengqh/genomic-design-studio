<%@ include file="../include.jsp"%>
<html>
<head>
<%@ include file="../include_head.jsp"%>
</head>

<body>
	<jsp:include page="../menu.jsp" />

	<p>
	<h1 align="center">Registration for ${scheduleUserForm.day}</h1>
	<p>
		<form:form method="post" action="savescheduleuser.html"
			commandName="scheduleUserForm">
			<form:hidden path="id" />
			<form:hidden path="dayId" />
			<form:hidden path="regType" />
			<form:errors path="*" cssClass="errorblock" element="div" />
			<table id="box-table-a">
				<tr>
					<td><form:label path="email">
							<spring:message code="label.email" />
						</form:label></td>
					<td><form:input path="email" cssClass="txt" /></td>
					<td><form:errors path="email" cssClass="error" /></td>
				</tr>
				<tr>
					<td><form:label path="firstname">
							<spring:message code="label.firstname" />
						</form:label></td>
					<td><form:input path="firstname" cssClass="txt" size="300"/></td>
					<td><form:errors path="firstname" cssClass="error" /></td>
				</tr>
				<tr>
					<td><form:label path="lastname">
							<spring:message code="label.lastname" />
						</form:label></td>
					<td><form:input path="lastname" cssClass="txt" /></td>
					<td><form:errors path="lastname" cssClass="error" /></td>
				</tr>
				<tr>
					<td><form:label path="department">
							Department/Division
						</form:label></td>
					<td><form:input path="department" cssClass="txt" /></td>
					<td><form:errors path="department" cssClass="error" /></td>
				</tr>
				<tr>
					<td colspan="3" align="center"><input type="submit"
						value="<spring:message code="label.add"/>" />
						<form>
							<input type="button" value="<spring:message code="label.back" />"
								onClick="parent.location='showlist.html'" />
						</form></td>
				</tr>
			</table>
		</form:form>
	<p class="message">${message}</body>

</html>
