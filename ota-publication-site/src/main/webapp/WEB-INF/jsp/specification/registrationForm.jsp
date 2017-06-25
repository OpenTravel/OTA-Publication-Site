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
<c:if test="${registrant == null}">
<div>
	<p>
		<span style="font-weight: bold;">All fields are required.</span>
	</p>
</div>
<form:form id="registrationForm" action="${config.localSiteUrl}/specifications/${registrationPage}" method="POST" modelAttribute="specificationForm">
	<form:hidden path="processForm" />
	<table class="formTable">
		<%@ include file="registrantInfoForm.jsp" %>
		<tr>
			<td colspan="2">
				<h5>Terms of Service</h5>
				<textarea class="licenseTextarea" style="width: 500px" rows="9" cols="40" readonly="true"><%@ include file="licenseAgreement.txt" %></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding-left: 465px;">
				<a id="saveButton" class="buttonRed" href="javascript:document.forms.registrationForm.submit();">Accept</a>
			</td>
		</tr>
	</table>
</form:form>
</c:if>

<c:if test="${registrant != null}">
	<span style="font-weight: bold;">Thank you for accepting the license agreement, ${registrant.firstName}.</span>
	<br/>
	<p class="small"><i>Not ${registrant.firstName}? Click <a href="${config.localSiteUrl}/specifications/${registrationPage}?newSession=true"/>here</a> to re-register.</i></p>
</c:if>
