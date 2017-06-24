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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="publicationCheck.jsp" %>
<c:if test="${publication != null}">
<h2 class="sub-title">Comment on a ${publication.type.displayId} Artifact</h2>

<c:if test="${publication.state.toString() == 'MEMBER_REVIEW'}">
	<h4><span style="color:Red;">This version of the specification is open for Member Review!</span></h4>
</c:if>
<c:if test="${publication.state.toString() == 'PUBLIC_REVIEW'}">
	<h4><span style="color:Red;">This version of the specification is open for Public Review!</span></h4>
</c:if>
<p>OpenTravel is now accepting comments on the ${publication.name}
	version of the ${publication.type.displayId} specification (OpenTravel does not
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

<form:form id="commentForm" action="${config.localSiteUrl}${submitCommentsUrl}" method="POST" modelAttribute="commentForm">
	<form:hidden path="processForm" />
	<table class="formTable">
		<%@ include file="commentContactInfo.jsp" %>
		<tr>
			<td colspan="2">
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
				<br/><br/>
			</td>
		</tr>
		<tr valign="top">
			<td class="required">* Artifact: </td>
			<td>
				<form:select path="itemId">
					<form:option value=""/>
					<form:options items="${publicationItems}" itemValue="id" itemLabel="itemFilename"/>
				</form:select>
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('ArtifactComment.publicationItem')}">
					<span style="color: Red">${validationErrors.getMessage('ArtifactComment.publicationItem')}</span>
				</c:if>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="required">* Page Numbers: </td>
			<td>
				<form:input path="pageNumbers" maxlength="200" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('ArtifactComment.pageNumbers')}">
					<span style="color: Red">${validationErrors.getMessage('ArtifactComment.pageNumbers')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">* Line Numbers: </td>
			<td>
				<form:input path="lineNumbers" maxlength="200" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('ArtifactComment.lineNumbers')}">
					<span style="color: Red">${validationErrors.getMessage('ArtifactComment.lineNumbers')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required" style="vertical-align:middle;padding-bottom:30px;">* Comment Type: </td>
			<td>
				<table id="CommentTypeList" class="checkList">
				<c:forEach var="ct" items="${commentTypes}">
					<tr><td>
					<form:radiobutton id="CommentType_${ct}" path="commentType" cssClass="text" value="${ct}" />
					<label for="CommentType_${ct}">${ct.displayName}</label>
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
				* Comment and Reason:
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				<form:textarea path="commentText" rows="5" cols="50" cssStyle="width:350px;" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('ArtifactComment.commentText')}">
					<span style="color: Red">${validationErrors.getMessage('ArtifactComment.commentText')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				* Suggested Change:
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				<form:textarea path="suggestedChange" rows="5" cols="50" cssStyle="width:350px;" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('ArtifactComment.suggestedChange')}">
					<span style="color: Red">${validationErrors.getMessage('ArtifactComment.suggestedChange')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<a id="saveButton" class="buttonRed" href="javascript:document.forms.commentForm.submit();">Submit Comment</a>
			</td>
		</tr>
	</table>
</form:form>
</c:if>
