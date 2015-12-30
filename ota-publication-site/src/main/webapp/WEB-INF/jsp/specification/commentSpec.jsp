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
<h1>Comment on the Latest ${publication.type.displayId} Specification</h1>

<c:if test="${publication.state.toString() == 'MEMBER_REVIEW'}">
	<h3><span style="color:Red;">This version of the specification is open for Member Review!</span></h3>
</c:if>
<c:if test="${publication.state.toString() == 'PUBLIC_REVIEW'}">
	<h3><span style="color:Red;">This version of the specification is open for Public Review!</span></h3>
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
					var newwin= window.open('${config.mainSiteUrl}/Specifications/CommentOnSpecSample.aspx','page1','toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes,copyhistory=no,border=no,width=600,height=600')
				}
				
				function Open_Window2() {
					var newwin= window.open('${config.mainSiteUrl}/Specifications/CommentOnSpecSamplesRentalCar.aspx','page2','toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes,copyhistory=no,border=no,width=600,height=600')
				}
				//-->
				</script>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<a href="javascript: Open_Window1()">See a Sample Hotel Message Comment</a>
				<br/><a href="javascript: Open_Window2()">See a Sample Rental Car Message Comment</a>
			</td>
		</tr>
		<tr valign="top">
			<td class="required">* XML Schema: </td>
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
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('SchemaComment.publicationItem')}">
					<span style="color: Red">${validationErrors.getMessage('SchemaComment.publicationItem')}</span>
				</c:if>
				&nbsp;
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
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('SchemaComment.commentType')}">
					<span style="color: Red">${validationErrors.getMessage('SchemaComment.commentType')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				<br/>* Full XML Schema paths for the message(s) this Comment refers to
				<span style="font-style: italic;">(for example, OTA_ReadRQ/UniqueID/CompanyName/@CompanyShortName)</span>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				<input name="commentXPath" type="text" maxlength="4000" style="width:350px;" value="${commentXPath}" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('SchemaComment.commentXPath')}">
					<span style="color: Red">${validationErrors.getMessage('SchemaComment.commentXPath')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				* If this Comment applies to a complexType, simpleType or attributeGroup, provide
				the XML Schema (XSD) and full element or attribute path that needs to be modified
				<span style="font-style: italic;">(for example, OTA_CommonTypes/UniqueID_Type/CompanyName/@CompanyShortName)</span>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				<input name="modifyXPath" type="text" maxlength="4000" style="width:350px;" value="${modifyXPath}" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('SchemaComment.modifyXPath')}">
					<span style="color: Red">${validationErrors.getMessage('SchemaComment.modifyXPath')}</span>
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
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('SchemaComment.commentText')}">
					<span style="color: Red">${validationErrors.getMessage('SchemaComment.commentText')}</span>
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
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('SchemaComment.suggestedChange')}">
					<span style="color: Red">${validationErrors.getMessage('SchemaComment.suggestedChange')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				<br/>Provide an annotation for each new element, attribute, complexType, simpleType, attributeGroup, or enumeration.
			</td>
		</tr>
		<tr>
			<td colspan="2" class="required">
				<textarea name="newAnnotations" rows="5" cols="50" style="width:350px">${newAnnotations}</textarea>
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('SchemaComment.newAnnotations')}">
					<span style="color: Red">${validationErrors.getMessage('SchemaComment.newAnnotations')}</span>
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
