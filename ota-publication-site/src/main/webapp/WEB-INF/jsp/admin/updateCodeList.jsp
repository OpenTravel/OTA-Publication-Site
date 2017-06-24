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
<h2 class="sub-title">Update Code List</h2>

<br/>
<form:form id="codeListUpdateForm" action="${config.localSiteUrl}/admin/DoUpdateCodeList.html" enctype="multipart/form-data" method="POST" modelAttribute="codeListForm">
	<form:hidden path="processForm" />
	<form:hidden path="codeListId" />
	<table class="formTable">
		<tr>
			<td class="required">* Release Date:<br/><i>(yyyy-mm-dd)</i></td>
			<td>
				<form:input path="releaseDateLabel" maxlength="10" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('CodeList.releaseDate')}">
					<span style="color: Red">${validationErrors.getMessage('CodeList.releaseDate')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">* Archive File: </td>
			<td>
				<input name="archiveFile" type="file" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('CodeList.archiveFilename')}">
					<span style="color: Red">${validationErrors.getMessage('CodeList.archiveFilename')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br/><a id="submitButton" class="buttonRed" href="javascript:document.forms.codeListUpdateForm.submit();">Update Code List</a>
				&nbsp; <a href="${config.localSiteUrl}/admin/ViewCodeList.html?releaseDate=${codeListForm.releaseDateLabel}">Cancel</a>
			</td>
		</tr>
	</table>
</form:form>

<p/><br/>
<p/><br/>
<p/><br/>
<p/><br/>
