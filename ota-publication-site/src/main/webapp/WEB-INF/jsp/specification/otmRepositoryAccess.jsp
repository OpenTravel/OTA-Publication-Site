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
<h2 class="sub-title">Request Access to the Open Travel OTM Repository</h2>

<p>The Open Travel Model (OTM) is the language used to define our next-generation of XML and JSON schemas.
These models are managed and shared using the OTM repository.  Members and non-members may request access
to view these models online or via the OTM Development Environment.

<h4>Please provide the following information to request access:</h4>
<form:form id="repositoryForm" action="${config.localSiteUrl}/specifications/OTMRepositoryAccess.html" method="POST" modelAttribute="repositoryForm">
	<form:hidden path="processForm" />
	<table class="formTable">
		<tr>
			<td class="required">* First Name</td>
			<td>
				<form:input path="firstName" cssClass="text" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('RepositoryAccessForm.firstName')}">
					<span style="color: Red">${validationErrors.getMessage('RepositoryAccessForm.firstName')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">* Last Name</td>
			<td>
				<form:input path="lastName" cssClass="text" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('RepositoryAccessForm.lastName')}">
					<span style="color: Red">${validationErrors.getMessage('RepositoryAccessForm.lastName')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">Company</td>
			<td>
				<form:input path="company" cssClass="text" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('RepositoryAccessForm.company')}">
					<span style="color: Red">${validationErrors.getMessage('RepositoryAccessForm.company')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">Primary Company Contact</td>
			<td>
				<form:input path="companyContact" cssClass="text" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('RepositoryAccessForm.companyContact')}">
					<span style="color: Red">${validationErrors.getMessage('RepositoryAccessForm.companyContact')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td>Phone</td>
			<td>
				<form:input path="phone" cssClass="text" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('RepositoryAccessForm.phone')}">
					<span style="color: Red">${validationErrors.getMessage('RepositoryAccessForm.phone')}</span>
				</c:if>
			</td>
		</tr>
		<tr valign="top">
			<td class="required">* E-mail</td>
			<td>
				<form:input path="email" cssClass="text" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('RepositoryAccessForm.email')}">
					<span style="color: Red">${validationErrors.getMessage('RepositoryAccessForm.email')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">Address</td>
			<td class="required">
				<form:textarea path="address" rows="3" cols="50" cssStyle="width:250px; height:75px" />
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('RepositoryAccessForm.address')}">
					<span style="color: Red">${validationErrors.getMessage('RepositoryAccessForm.address')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required">
				Is your company a member<br />of the OpenTravel Alliance?
			</td>
			<td>
				<table class="radioList" border="0">
					<tr>
						<td>
							<form:radiobutton id="MemberRadioButtonList_Yes" path="otaMember" value="true"/>
							<label for="MemberRadioButtonList_Yes">Yes</label>
						</td>
						<td>
							<form:radiobutton id="MemberRadioButtonList_No" path="otaMember" value="false"/>
							<label for="MemberRadioButtonList_No">No</label>
						</td>
					</tr>
				</table>
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('RepositoryAccessForm.otaMember')}">
					<span style="color: Red">${validationErrors.getMessage('RepositoryAccessForm.otaMember')}</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="required" colspan="2">
				<form:checkbox style="width:10px; text-align:left; display:inline;" id="wantModelDevelopmentInfo_CB" path="wantModelDevelopmentInfo" value="true"/>
				<label style="display:inline; padding-left:5px;" for="wantModelDevelopmentInfo_CB">Please send me information about participating in OTM model development.</label>
			</td>
		</tr>
		<tr>
			<td class="required" colspan="2">
				<form:checkbox style="width:10px; text-align:left; display:inline;" id="wantWorkgroupInfo_CB" path="wantWorkgroupInfo" value="true"/>
				<label style="display:inline; padding-left:5px;" for="wantWorkgroupInfo_CB">Please send me information about participating in OpenTravel working groups.</label>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br/><h5>Terms of Service</h5>
				<textarea class="licenseTextarea" style="width: 500px;" rows="9" cols="40" readonly="true"><%@ include file="licenseAgreement.txt" %></textarea>
				* <form:checkbox style="width:10px; text-align:left; display:inline;" id="acceptTerms_CB" path="acceptTerms" value="true"/>
				<label style="display:inline; padding-left:5px;" for="acceptTerms_CB">I accept the OpenTravel terms of service.</label>
				<c:if test="${(validationErrors != null) && validationErrors.hasViolation('RepositoryAccessForm.acceptTerms')}">
					<span style="color: Red">You must accept the terms of service.</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br/><a id="submitButton" class="buttonRed" href="javascript:document.forms.repositoryForm.submit();">Request OTM Repository Access</a>
			</td>
		</tr>
	</table>
</form:form>
