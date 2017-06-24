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
<h2 class="sub-title">Download History</h2>

<table><tr>
	<td>
		<h3>Registrant Information:</h3>
		<table class="formTable">
			<tr>
				<td class="required">Name: </td>
				<td>${registrant.firstName} ${registrant.lastName}</td>
			</tr>
			<tr>
				<td>Title</td>
				<td>${registrant.title}</td>
			</tr>
			<tr>
				<td class="required">Company: </td>
				<td>${registrant.company}</td>
			</tr>
			<tr>
				<td>Phone</td>
				<td>${registrant.phone}</td>
			</tr>
			<tr valign="top">
				<td class="required">E-mail: </td>
				<td>${registrant.email}</td>
			</tr>
			<tr>
				<td class="required">OpenTravel Member? </td>
				<td>${registrant.otaMember ? "Yes" : "No"}</td>
			</tr>
			<tr valign="top">
				<td class="required">Registration Date: </td>
				<td>${formatter.formatDate( registrant.registrationDate )}</td>
			</tr>
		</table>
	</td>
	<td style="width:100px;"></td>
	<td>
		<h3>Actions:</h3>
		<ul>
			<li>
				<a href="${config.localSiteUrl}/admin/RegistrantDetails.html?rid=${registrant.id}">View Submitted Comments</a>
			</li>
			<li>
				<a href="${config.localSiteUrl}/admin/ViewRegistrants.html">Back to All Web Site Registrants</a>
			</li>
		</ul>
	</td>
</tr><tr><td colspan="3">
	<p/>
	<h3>Download History:</h3>
</td></tr></table>

<table id="reporttable" width="80%" align="left" style="margin-left:20px;">
	<tr>
		<th width="20%">Specification</th>
		<th width="50%">Schema / Artifact</th>
		<th width="30%">Category</th>
	</tr>
	<c:choose>
		<c:when test="${!registrant.downloadedPublications.isEmpty() || !registrant.downloadedPublicationItems.isEmpty() || !registrant.downloadedCodeLists.isEmpty()}">
			<c:forEach var="publication" items="${registrant.downloadedPublications}">
				<tr>
					<td>${publication.name}-${publication.type.displayId}</td>
					<td>${publication.archiveFilename}</td>
					<td>Specification Archive</td>
				</tr>
			</c:forEach>
			<c:forEach var="item" items="${registrant.downloadedPublicationItems}">
				<c:set var="publication" value="${item.owner.owner}"/>
				<tr>
					<td>${publication.name}-${publication.type.displayId}</td>
					<td>${item.itemFilename}</td>
					<td>${item.owner.name}</td>
				</tr>
			</c:forEach>
			<c:forEach var="item" items="${registrant.downloadedCodeLists}">
				<tr>
					<td>Code List Release ${item.releaseDateLabel}</td>
					<td>${item.archiveFilename}</td>
					<td>Code List Archive</td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr><td colspan="5">No downloads by the registrant on this date.</td></tr>
		</c:otherwise>
	</c:choose>
</table>
