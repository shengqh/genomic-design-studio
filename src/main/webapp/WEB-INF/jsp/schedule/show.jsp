<%@ include file="../include.jsp"%>
<html>
<head>
<%@ include file="../include_head.jsp"%>
<script type="text/javascript">
	function showComments(taskid) {
		//alert(taskid);
		$
				.ajax({
					type : "POST",
					url : "getStatusList.html",
					data : "taskid=" + taskid.toString(),
					success : function(response) {
						//alert(response);
						var tbody = "";
						$
								.each(
										response,
										function(n, value) {
											//alert(n+' '+value);  
											var trs = "<tr class=\"comment\">";
											trs += "<td align=\"right\">log:</td>";
											trs += "<td colspan=\"3\" class=\"comment\" background=\"red\"><pre>"
													+ value.comment
													+ "</pre></td>";
											trs += "<td>" + value.statusString
													+ "</td>";
											trs += "<td>" + value.updateUser
													+ "</td>";
											trs += "<td>"
													+ value.updateDateString
													+ "</td>";
											trs += "<td></td>";
											trs += "<td></td>";
											trs += "<td></td>";
											trs += "</tr>";
											tbody += trs;
										});
						$('#task' + taskid.toString()).html(tbody);
					},
					error : function(e) {
						alert('Error: ' + e);
					}
				});
	}
</script>
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
							<sec:authorize access="hasRole('ROLE_MANAGER')">
								<th scope="col">IP address</th>
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
								<sec:authorize access="hasRole('ROLE_MANAGER')">
									<td>${user.ipaddress}</td>
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
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>
	<p class="message">${message}</p>
</body>
</html>
