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
<tr>
	<td class="required">First Name</td>
	<td>
		<form:input path="registrantForm.firstName" cssClass="text" />
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.firstName')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.firstName')}</span>
		</c:if>
	</td>
</tr>
<tr>
	<td class="required">Last Name</td>
	<td>
		<form:input path="registrantForm.lastName" cssClass="text" />
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.lastName')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.lastName')}</span>
		</c:if>
	</td>
</tr>
<tr>
	<td>Title</td>
	<td>
		<form:input path="registrantForm.title" cssClass="text" />
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.title')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.title')}</span>
		</c:if>
	</td>
</tr>
<tr>
	<td class="required">Company</td>
	<td>
		<form:input path="registrantForm.company" cssClass="text" />
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.company')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.company')}</span>
		</c:if>
	</td>
</tr>
<tr>
	<td>Phone</td>
	<td>
		<form:input path="registrantForm.phone" cssClass="text" />
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.phone')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.phone')}</span>
		</c:if>
	</td>
</tr>
<tr valign="top">
	<td class="required">E-mail</td>
	<td>
		<form:input path="registrantForm.email" cssClass="text" />
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.email')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.email')}</span>
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
					<form:radiobutton id="MemberRadioButtonList_Yes" path="registrantForm.otaMember" value="true"/>
					<label for="MemberRadioButtonList_Yes">Yes</label>
				</td>
				<td>
					<form:radiobutton id="MemberRadioButtonList_No" path="registrantForm.otaMember" value="false"/>
					<label for="MemberRadioButtonList_No">No</label>
				</td>
			</tr>
		</table>
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.otaMember')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.otaMember')}</span>
		</c:if>
	</td>
</tr>
