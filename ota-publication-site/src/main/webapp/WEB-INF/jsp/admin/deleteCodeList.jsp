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
<h2 class="sub-title">Delete Code List ${codeList.releaseDateLabel}</h2>

<form id="codeListDeleteForm" action="${config.localSiteUrl}/admin/DeleteCodeList.html" method="POST">
	<input name="confirmDelete" type="hidden" class="text" value="true" />
	<input name="codeListId" type="hidden" class="text" value="${codeList.id}" />
	<table class="formTable"><tr><td>
		<h2><span style="color: #CC0000;">Are you sure you want to delete Code List Release ${codeList.releaseDateLabel}?</span></h2>
		
		<p>The following information will also be deleted from the OpenTravel web site:
		<ul>
			<li>The code list archive.</li>
			<li>All associated download history.</li>
		</ul>
	</td></tr>
	<tr><td>
		<br/><a id="submitButton" class="buttonRed" href="javascript:document.forms.codeListDeleteForm.submit();">Delete Code List</a>
		&nbsp; <a href="${config.localSiteUrl}/admin/ViewCodeList.html?releaseDate=${codeList.releaseDateLabel}">Cancel</a>
	</td></tr></table>
</form>
