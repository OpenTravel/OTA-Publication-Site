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
<tr>
	<td class="required">First Name</td>
	<td>
		<input name="firstName" type="text" class="text" value="${firstName}" />
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.firstName')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.firstName')}</span>
		</c:if>
	</td>
</tr>
<tr>
	<td class="required">Last Name</td>
	<td>
		<input name="lastName" type="text" class="text" value="${lastName}" />
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.lastName')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.lastName')}</span>
		</c:if>
	</td>
</tr>
<tr>
	<td>Title</td>
	<td>
		<input name="title" type="text" class="text" value="${title}" />
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.title')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.title')}</span>
		</c:if>
	</td>
</tr>
<tr>
	<td class="required">Company</td>
	<td>
		<input name="company" type="text" class="text" value="${company}" />
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.company')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.company')}</span>
		</c:if>
	</td>
</tr>
<tr>
	<td>Phone</td>
	<td>
		<input name="phone" type="text" class="text" value="${phone}" />
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.phone')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.phone')}</span>
		</c:if>
	</td>
</tr>
<tr valign="top">
	<td class="required">E-mail</td>
	<td>
		<input name="email" type="text" class="text" value="${email}" />
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
		<table id="MemberRadioButtonList" class="checkList" border="0">
			<tr>
				<td>
					<c:choose>
						<c:when test="${(otaMember != null) && otaMember}">
							<input id="MemberRadioButtonList_Yes" name="otaMember" type="radio" class="text" value="true" checked />
						</c:when>
						<c:otherwise>
							<input id="MemberRadioButtonList_Yes" name="otaMember" type="radio" class="text" value="true" />
						</c:otherwise>
					</c:choose>
					<label for="MemberRadioButtonList_Yes">Yes</label>
				</td>
				<td>
					<c:choose>
						<c:when test="${(otaMember != null) && !otaMember}">
							<input id="MemberRadioButtonList_No" name="otaMember" type="radio" class="text" value="false" checked />
						</c:when>
						<c:otherwise>
							<input id="MemberRadioButtonList_No" name="otaMember" type="radio" class="text" value="false" />
						</c:otherwise>
					</c:choose>
					<label for="MemberRadioButtonList_No">No</label>
				</td>
			</tr>
		</table>
		<c:if test="${(validationErrors != null) && validationErrors.hasViolation('Registrant.otaMember')}">
			<span style="color: Red">${validationErrors.getMessage('Registrant.otaMember')}</span>
		</c:if>
	</td>
</tr>
