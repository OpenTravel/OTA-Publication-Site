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
<h2 class="sub-title">Comments on Specification ${publication.name}-${publication.type.displayId}</h2>

<br/>
<form id="reportForm" action="${config.localSiteUrl}/admin/SpecificationComments.html" method="GET">
	<input name="publication" type="hidden" class="text" value="${publication.name}" />
	<input name="specType" type="hidden" class="text" value="${publication.type}" />
	<table class="formTable">
		<tr>
			<td nowrap>
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
			<td nowrap>
				<a id="submitButton" class="buttonRed" href="javascript:document.forms.reportForm.submit();">Refresh</a>
			</td>
			<td nowrap>
				<a href="${config.localSiteUrl}/admin/ViewSpecification.html?publication=${publication.name}&specType=${publication.type}">Back to Overview</a>
			</td>
		</tr>
	</table>
</form>

<table id="reporttable" align="left">
	<tr>
		<th width="5%">Comment Number</th>
		<th width="15%">Schema/Artifact</th>
		<th width="5%">Type</th>
		<th width="15%">Submitted By</th>
		<th width="60%">Comment</th>
	</tr>
	<c:choose>
		<c:when test="${!commentList.isEmpty()}">
			<c:forEach var="comment" items="${commentList}">
			<tr>
				<td>${comment.commentNumber}</td>
				<td>
					${comment.publicationItem.itemFilename}
					<p class="small"><i>${comment.publicationState.displayValue}</i></p>
				</td>
				<td>${comment.commentType.displayName}</td>
				<td>
					<a href="${config.localSiteUrl}/admin/RegistrantDetails.html?rid=${comment.submittedBy.id}">${comment.submittedBy.firstName} ${comment.submittedBy.lastName}</a>
					<br><i>${comment.submittedBy.company}</i>
				</td>
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
			<tr><td colspan="5">No comments received that meet your criteria.</td></tr>
		</c:otherwise>
	</c:choose>
</table>
