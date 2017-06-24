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
<h2 class="sub-title">Change Admin Login Credentials</h2>

<br/>
<form id="credentialsForm" action="${config.localSiteUrl}/admin/ChangeAdminCredentials.html" method="POST">
	<input name="processUpdate" type="hidden" class="text" value="true" />
	<table class="formTable">
		<tr>
			<td class="required">User ID: </td>
			<td>
				<input name="userId" type="text" maxlength="10" value="${userId}" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('AdminCredentials.userId')}">
					<span style="color: Red">${validationErrors.getMessage('AdminCredentials.userId')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">Password: </td>
			<td>
				<input name="password" type="password" maxlength="10" value="${password}" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('AdminCredentials.password')}">
					<span style="color: Red">${validationErrors.getMessage('AdminCredentials.password')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br/><a id="submitButton" class="buttonRed" href="javascript:document.forms.credentialsForm.submit();">Change Credentials</a>
				&nbsp; <a href="${config.localSiteUrl}/admin/index.html">Cancel</a>
			</td>
		</tr>
	</table>
</form>
