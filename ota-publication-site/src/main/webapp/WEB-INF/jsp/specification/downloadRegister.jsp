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
<h1>Download the ${publication.name}-${publicationType} Publication</h1>
<p>
	<c:choose>
		<c:when test="${item == null}">
			<b>Downloading File: ${publication.archiveFilename}</b><br/>
		</c:when>
		<c:otherwise>
			<b>Downloading File: ${item.itemFilename}</b><br/>
		</c:otherwise>
	</c:choose>
</p>
<p>
	<span style="font-weight: bold;">Please complete the form below
		and accept the license agreement to browse the selected file.<br/>
	</span>
</p>
<div>
	<p>
		<span style="font-weight: bold;">All fields are required.</span>
	</p>
</div>
<div id="editBox">
	<div id="formWpr">
		<form id="registrationForm" action="${config.localSiteUrl}/specifications/DownloadRegister.html" method="POST">
		<input name="processRegistrant" type="hidden" class="text" value="true" />
		<input name="pubName" type="hidden" class="text" value="${pubName}" />
		<input name="pubType" type="hidden" class="text" value="${pubType}" />
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
	</div>
</div>
