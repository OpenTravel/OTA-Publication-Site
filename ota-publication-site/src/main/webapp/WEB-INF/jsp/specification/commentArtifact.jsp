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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1>Comment on a ${publicationType} Artifact</h1>

<p>OpenTravel is now accepting comments on the ${publication.name}
	version of the ${publicationType} specification (OpenTravel does not
	accept comments on past versions).&nbsp; Please submit a comment on the
	specification using the following form.&nbsp; You may be contacted by
	an OpenTravel staff member regarding your submission.</p>
<p>
	Please note that comments are reviewed by OpenTravel work groups as
	quickly as time permits.<br />
</p>
<p>
	Thank you for taking the time to help improve the OpenTravel
	specification. <br />
</p>
<div id="editBox">
<div id="formWpr">
<form id="commentForm" action="${config.localSiteUrl}${submitCommentsUrl}" method="POST">
	<input name="processComment" type="hidden" class="text" value="true" />
	<table border="0" cellpadding="0" cellspacing="0">
		<%@ include file="commentContactInfo.jsp" %>
		<tr>
			<td colspan="2">
				<br/>
				<h3 style="float:left;">Specification Comments</h3>
				<script type="text/javascript" language="javascript">
				<!--
				function Open_Window1() {
					var newwin= window.open('${config.mainSiteUrl}/Specifications/CommentOnArtifactSampleStudy.aspx','page1','toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes,copyhistory=no,border=no,width=600,height=600')
				}
				
				function Open_Window2() {
					var newwin= window.open('${config.mainSiteUrl}/Specifications/CommentOnArtifactSampleGuideline.aspx','page2','toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes,copyhistory=no,border=no,width=600,height=600')
				}
				//-->
				</script>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<a href="javascript: Open_Window1()">See a sample OpenTravel Study comment.</a>
				<br/><a href="javascript: Open_Window2()">See a sample OpenTravel Guidelines Paper comment.</a>
			</td>
		</tr>
		<tr valign="top">
			<td class="required">* Artifact: </td>
			<td>
				<select name="itemId">
					<option value=""></option>
					<c:forEach var="item" items="${publicationItems}">
						<c:choose>
							<c:when test="${itemId == item.id}">
								<option value="${item.id}" selected>${item.itemFilename}</option>
							</c:when>
							<c:otherwise>
								<option value="${item.id}">${item.itemFilename}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('ArtifactComment.publicationItem')}">
					<span style="color: Red">${validationErrors.getMessage('ArtifactComment.publicationItem')}</span>
				</c:if>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="required">* Page Numbers: </td>
			<td>
				<input name="pageNumbers" type="text" maxlength="200" value="${pageNumbers}" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('ArtifactComment.pageNumbers')}">
					<span style="color: Red">${validationErrors.getMessage('ArtifactComment.pageNumbers')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">* Line Numbers: </td>
			<td>
				<input name="lineNumbers" type="text" maxlength="200" value="${lineNumbers}" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('ArtifactComment.lineNumbers')}">
					<span style="color: Red">${validationErrors.getMessage('ArtifactComment.lineNumbers')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">* Comment Type: </td>
			<td>
				<table id="MemberRadioButtonList" class="checkList" cellspacing="0" cellpadding="0" border="0">
				<c:forEach var="ct" items="${commentTypes}">
					<tr><td>
					<c:choose>
						<c:when test="${commentType == ct}">
							<input id="CommentType_${ct}" name="commentType" type="radio" class="text" value="${ct.toString()}" checked />
						</c:when>
						<c:otherwise>
							<input id="CommentType_${ct}" name="commentType" type="radio" class="text" value="${ct.toString()}" />
					</c:otherwise>
					</c:choose>
					<label for="CommentType_${ct}">${ct.displayName}</label><br/>
					</td></tr>
				</c:forEach>
				</table>
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('ArtifactComment.commentType')}">
					<span style="color: Red">${validationErrors.getMessage('ArtifactComment.commentType')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				<br/>* Comment and Reason:
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				<textarea name="commentText" rows="5" cols="50" style="width:350px">${commentText}</textarea>
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('ArtifactComment.commentText')}">
					<span style="color: Red">${validationErrors.getMessage('ArtifactComment.commentText')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				<br/>* Suggested Change:
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				<textarea name="suggestedChange" rows="5" cols="50" style="width:350px">${suggestedChange}</textarea>
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('ArtifactComment.suggestedChange')}">
					<span style="color: Red">${validationErrors.getMessage('ArtifactComment.suggestedChange')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<a id="saveButton" class="buttonBlue marginRight10" href="javascript:document.forms.commentForm.submit();"><span>Submit Comment</span></a>
			</td>
		</tr>
	</table>
</form>
</div></div>
