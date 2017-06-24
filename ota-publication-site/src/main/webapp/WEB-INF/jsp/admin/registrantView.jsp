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
<h2 class="sub-title">Registrant Details</h2>

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
				<a href="${config.localSiteUrl}/admin/RegistrantDownloads.html?rid=${registrant.id}">View Download History</a>
			</li>
			<li>
				<a href="${config.localSiteUrl}/admin/ViewRegistrants.html">Back to All Web Site Registrants</a>
			</li>
		</ul>
	</td>
</tr><tr><td colspan="3">
	<p/>
	<h3>Submitted Comments:</h3>
</td></tr></table>

<table id="reporttable" align="left" style="margin-left:20px;">
	<tr>
		<th width="5%">Comment Number</th>
		<th width="20%">Specification / Artifact</th>
		<th width="15%">Type</th>
		<th width="60%">Comment</th>
	</tr>
	<c:choose>
		<c:when test="${!registrant.submittedComments.isEmpty()}">
			<c:forEach var="comment" items="${registrant.submittedComments}">
			<tr>
				<c:set var="publication" value="${comment.publicationItem.owner.owner}"/>
				<td>${comment.commentNumber}</td>
				<td>${publication.name}-${publication.type.displayId} / ${comment.publicationItem.itemFilename}</td>
				<td>${comment.commentType.displayName}</td>
				<td>
					<c:choose>
						<c:when test="${typeChecker.instanceOf( comment, 'org.opentravel.pubs.model.SchemaComment' )}">
							<c:if test="${comment.commentXPath != null}"><p class="commentspacing">
								<span class="commentheader">Schema XPath:</span>
								<br><span class="techtext">${comment.commentXPath}</span>
							</p></c:if>
							<c:if test="${comment.modifyXPath != null}"><p class="commentspacing">
								<span class="commentheader">XPath to be Modified:</span>
								<br><span class="techtext">${comment.modifyXPath}</span>
							</p></c:if>
							<c:if test="${comment.commentText != null}"><p class="commentspacing">
								<span class="commentheader">Comments:</span>
								<br><span>${comment.commentText}</span>
							</p></c:if>
							<c:if test="${comment.suggestedChange != null}"><p class="commentspacing">
								<span class="commentheader">Suggested Change:</span>
								<br><span>${comment.suggestedChange}</span>
							</p></c:if>
							<c:if test="${comment.newAnnotations != null}"><p class="commentspacing">
								<span class="commentheader">New Annotations:</span>
								<br><span>${comment.newAnnotations}</span>
							</p></c:if>
						</c:when>
						<c:otherwise>
							<c:if test="${comment.pageNumbers != null}"><p class="commentspacing">
								<span class="commentheader">Page Numbers:</span>
								<span class="techtext">${comment.pageNumbers}</span>
							</p></c:if>
							<c:if test="${comment.lineNumbers != null}"><p class="commentspacing">
								<span class="commentheader">Line Numbers:</span>
								<span class="techtext">${comment.lineNumbers}</span>
							</p></c:if>
							<c:if test="${comment.commentText != null}"><p class="commentspacing">
								<span class="commentheader">Comments:</span>
								<br><span>${comment.commentText}</span>
							</p></c:if>
							<c:if test="${comment.suggestedChange != null}"><p class="commentspacing">
								<span class="commentheader">Suggested Change:</span>
								<br><span>${comment.suggestedChange}</span>
							</p></c:if>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr><td colspan="5">No comments submitted by this web site registrant.</td></tr>
		</c:otherwise>
	</c:choose>
</table>
