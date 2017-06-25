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
<h2 class="sub-title">Email Notification Settings</h2>

<br/>
<form:form id="credentialsForm" action="${config.localSiteUrl}/admin/EmailSettings.html" method="POST" modelAttribute="emailSettings" autocomplete="off">
	<form:hidden path="processForm" />
	<table class="formTable">
		<tr>
			<td class="required aligntop">Enable Email Notifications: </td>
			<td>
				<form:checkbox id="enableNotification" style="width:10px; display:inline; text-align:left;" path="enableNotification" onchange="updateEnabledState( this );" />
			</td>
		</tr>
		<tr><td colspan="2"><hr align="center" width="80%" /></td></tr>
		<tr>
			<td class="required aligntop">* SMTP Host: </td>
			<td>
				<form:input id="smtpHost" path="smtpHost" maxlength="50" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('EmailSettingsForm.smtpHost')}">
					<span style="color: Red">${validationErrors.getMessage('EmailSettingsForm.smtpHost')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required aligntop">* SMTP Port: </td>
			<td>
				<form:input id="smtpPort" path="smtpPort" maxlength="50" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('EmailSettingsForm.smtpPort')}">
					<span style="color: Red">${validationErrors.getMessage('EmailSettingsForm.smtpPort')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required aligntop">Email User ID: </td>
			<td>
				<form:input id="smtpUser" path="smtpUser" maxlength="50" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('EmailSettingsForm.smtpUser')}">
					<span style="color: Red">${validationErrors.getMessage('EmailSettingsForm.smtpUser')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required aligntop">Email Password: </td>
			<td>
				<form:password id="smtpPassword" path="smtpPassword" showPassword="true" maxlength="50" autocomplete="off" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('EmailSettingsForm.smtpPassword')}">
					<span style="color: Red">${validationErrors.getMessage('EmailSettingsForm.smtpPassword')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required aligntop">* Sender Full Name: </td>
			<td>
				<form:input id="senderName" path="senderName" maxlength="50" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('EmailSettingsForm.senderName')}">
					<span style="color: Red">${validationErrors.getMessage('EmailSettingsForm.senderName')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required aligntop">* Sender Email Address: </td>
			<td>
				<form:input id="senderAddress" path="senderAddress" maxlength="50" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('EmailSettingsForm.senderAddress')}">
					<span style="color: Red">${validationErrors.getMessage('EmailSettingsForm.senderAddress')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required aligntop">CC Recipients: </td>
			<td>
				<form:input id="ccRecipients" path="ccRecipients" maxlength="200" />
				<p class="small">Use a commas to separate multiple addresses.</p>
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('EmailSettingsForm.ccRecipients')}">
					<span style="color: Red">${validationErrors.getMessage('EmailSettingsForm.ccRecipients')}</span>
				</c:if>
			</td>
		</tr>
		<tr><td colspan="2"><hr align="center" width="80%" /></td></tr>
		<tr>
			<td class="required aligntop">Timeout (ms): </td>
			<td>
				<form:input id="timeout" path="timeout" maxlength="10" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('EmailSettingsForm.timeout')}">
					<span style="color: Red">${validationErrors.getMessage('EmailSettingsForm.timeout')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required aligntop">Enable SSL: </td>
			<td>
				<form:checkbox id="sslEnable" style="width:10px; display:inline; text-align:left;" path="sslEnable" maxlength="50" />
			</td>
		</tr>
		<tr>
			<td class="required aligntop">Enable Authorization: </td>
			<td>
				<form:checkbox id="authEnable" style="width:10px; display:inline; text-align:left;" path="authEnable" maxlength="50" />
			</td>
		</tr>
		<tr>
			<td class="required aligntop">Enable TLS: </td>
			<td>
				<form:checkbox id="startTlsEnable" style="width:10px; display:inline; text-align:left;" path="startTlsEnable" maxlength="50" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br/><a id="submitButton" class="buttonRed" href="javascript:document.forms.credentialsForm.submit();">Update Settings</a>
				&nbsp; <a href="${config.localSiteUrl}/admin/index.html">Cancel</a>
			</td>
		</tr>
	</table>
</form:form>

<script type="text/javascript">

formFields = [
		"smtpHost", "smtpPort", "smtpUser", "smtpPassword", "senderName", "senderAddress",
		"ccRecipients", "timeout", "sslEnable", "authEnable", "startTlsEnable" ];

function updateEnabledState( formField ) {
	var isDisabled = !formField.checked;
	
	for (i = 0; i < formFields.length; i++) {
		document.getElementById( formFields[i] ).disabled = isDisabled;
	}
}
updateEnabledState( document.getElementById( "enableNotification" ) );

</script>