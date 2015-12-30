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
<h1>Upload a New Specification</h1>

<div id="editBox">
<div id="formWpr">
<form id="specUploadForm" action="${config.localSiteUrl}/admin/DoUploadSpecification.html" enctype="multipart/form-data" method="POST">
	<input name="processUpload" type="hidden" class="text" value="true" />
	<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="required">* Publication Name: </td>
			<td>
				<input name="name" type="text" maxlength="10" value="${name}" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Publication.name')}">
					<span style="color: Red">${validationErrors.getMessage('Publication.name')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">* Type: </td>
			<td>
				<table class="checkList">
					<tr>
						<td>
							<c:set var="checkedState" value="" />
							<c:if test="${(pubType != null) && (pubType == 'OTA_1_0')}">
								<c:set var="checkedState" value="checked" />
							</c:if>
							<input id="PubType_10" name="pubType" type="radio" class="text" value="OTA_1_0" ${checkedState} />
							<label for="PubType_10">1.0</label>
						</td>
						<td>
							<c:set var="checkedState" value="" />
							<c:if test="${(pubType != null) && (pubType == 'OTA_2_0')}">
								<c:set var="checkedState" value="checked" />
							</c:if>
							<input id="PubType_20" name="pubType" type="radio" class="text" value="OTA_2_0" ${checkedState} />
							<label for="PubType_20">2.0</label>
						</td>
					</tr>
				</table>
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Publication.type')}">
					<span style="color: Red">${validationErrors.getMessage('Publication.type')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">* State: </td>
			<td>
				<select name="pubState">
					<c:forEach var="ps" items="${publicationStates}">
						<c:choose>
							<c:when test="${pubState == ps}">
								<option value="${ps}" selected>${ps.displayValue}</option>
							</c:when>
							<c:otherwise>
								<option value="${ps}">${ps.displayValue}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Publication.state')}">
					<span style="color: Red">${validationErrors.getMessage('Publication.state')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">* Archive File: </td>
			<td>
				<input name="archiveFile" type="file" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Publication.archiveFilename')}">
					<span style="color: Red">${validationErrors.getMessage('Publication.archiveFilename')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<a id="submitButton" class="buttonBlue marginRight10" href="javascript:document.forms.specUploadForm.submit();"><span>Upload Specification</span></a>
			</td>
		</tr>
	</table>
</form>
</div>
</div>