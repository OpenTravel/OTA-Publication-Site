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
<h1>Download Release ${codeList.releaseDateLabel} of the OpenTravel Code List</h1>

<p><b>Downloading File: ${filename}</b><br/></p>
<p>
	<span style="font-weight: bold;">Please complete the form below
		and accept the license agreement to download the selected file.<br/>
	</span>
</p>
<div>
	<p>
		<span style="font-weight: bold;">All fields are required.</span>
	</p>
</div>
<div id="editBox">
	<div id="formWpr">
		<form:form id="registrationForm" action="${config.localSiteUrl}/specifications/cl/DownloadRegister.html" method="POST" modelAttribute="specificationForm">
		<form:hidden path="processForm" />
		<input name="releaseDate" type="hidden" class="text" value="${releaseDate}" />
		<input name="filename" type="hidden" class="text" value="${filename}" />
		<table border="0" cellpadding="0" cellspacing="0">
			<%@ include file="registrantInfoForm.jsp" %>
			<tr>
				<td colspan="2"><br />
					<h5>Terms of Service</h5> <textarea style="width: 500px" rows="9"
						cols="40" readonly="true"><%@ include file="licenseAgreement.txt" %></textarea></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><a id="saveButton" class="buttonBlue marginRight10" href="javascript:document.forms.registrationForm.submit();"><span>Accept</span></a>
				</td>
			</tr>
		</table>
		</form:form>
	</div>
</div>
