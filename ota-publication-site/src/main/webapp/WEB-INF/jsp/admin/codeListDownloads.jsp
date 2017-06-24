<%--

    Copyright (C) 2015 OpenTravel Alliance (info@opentravel.org)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h2 class="sub-title">Download History for Code List Release ${codeList.releaseDateLabel}</h2>

<br/>
<form id="reportForm" action="${config.localSiteUrl}/admin/CodeListDownloads.html" method="GET">
	<input name="releaseDate" type="hidden" class="text" value="${codeList.releaseDateLabel}" />
	<table class="formTable">
		<tr>
			<td>
				Date Range: 
				<select name="dateRange">
					<c:forEach var="dr" items="${dateRanges}">
						<c:choose>
							<c:when test="${dateRange == dr}">
								<option value="${dr}" selected>${dr.displayValue}</option>
							</c:when>
							<c:otherwise>
								<option value="${dr}">${dr.displayValue}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</td>
			<td>
				<a id="submitButton" class="buttonRed" href="javascript:document.forms.reportForm.submit();">Refresh</a>
			</td>
			<td>
				<a href="${config.localSiteUrl}/admin/ViewCodeList.html?releaseDate=${codeList.releaseDateLabel}">Back to Overview</a>
			</td>
		</tr>
	</table>
</form>

<table id="reporttable">
	<tr>
		<th width="20%">Name</th>
		<th width="10%">Company</th>
		<th width="10%">Title</th>
		<th width="20%">Email</th>
		<th width="15%">Phone</th>
		<th width="10%" class="centertext">OTA Member</th>
		<th width="15%" class="centertext">Download Date</th>
	</tr>
	<c:choose>
		<c:when test="${!downloadedByList.isEmpty()}">
			<c:forEach var="registrant" items="${downloadedByList}">
				<tr>
					<td>
						<a href="${config.localSiteUrl}/admin/RegistrantDetails.html?rid=${registrant.id}">${registrant.firstName} ${registrant.lastName}</a>
					</td>
					<td>${registrant.company}</td>
					<td>${registrant.title}</td>
					<td><a href="mailto:${registrant.email}">${registrant.email}</a></td>
					<td>${registrant.phone}</td>
					<td class="centertext">
						<c:choose>
							<c:when test="${registrant.otaMember}">Yes</c:when>
							<c:otherwise>No</c:otherwise>
						</c:choose>
					</td>
					<td class="centertext">${formatter.formatDate( registrant.registrationDate )}</td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr><td colspan="7">No downloads occurred within the requested time frame.</td></tr>
		</c:otherwise>
	</c:choose>
</table>
