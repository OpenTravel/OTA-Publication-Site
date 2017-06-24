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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h2 class="sub-title">Upload a New Specification</h2>

<br/>
<form:form id="specUploadForm" action="${config.localSiteUrl}/admin/DoUploadSpecification.html"  enctype="multipart/form-data" method="POST" modelAttribute="specificationForm">
	<form:hidden path="processForm" />
	<table  class="formTable">
		<tr>
			<td class="required">* Publication Name: </td>
			<td>
				<form:input path="name" maxlength="10" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Publication.name')}">
					<span style="color: Red">${validationErrors.getMessage('Publication.name')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">* Type: </td>
			<td>
				<table class="radioList">
					<tr>
						<td>
							<form:radiobutton id="SpecType_10" path="specType" value="OTA_1_0"/>
							<label for="SpecType_10">1.0</label>
						</td>
						<td>
							<form:radiobutton id="SpecType_20" path="specType" value="OTA_2_0"/>
							<label for="SpecType_20">2.0</label>
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
				<form:select path="pubState">
					<form:options items="${specificationForm.publicationStates}" itemLabel="displayValue" />
				</form:select>
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
				<br/><a id="submitButton" class="buttonRed" href="javascript:document.forms.specUploadForm.submit();">Upload Specification</a>
			</td>
		</tr>
	</table>
</form:form>
