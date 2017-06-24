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
<h2 class="sub-title">Delete Specification ${publication.name}-${publication.type.displayId}</h2>

<form id="specDeleteForm" action="${config.localSiteUrl}/admin/DeleteSpecification.html" method="POST">
	<input name="confirmDelete" type="hidden" class="text" value="true" />
	<input name="publicationId" type="hidden" class="text" value="${publication.id}" />
	<table class="formTable"><tr><td>
		<h2><span style="color: #CC0000;">Are you sure you want to delete Specification ${publication.name}-${publication.type.displayId}?</span></h2>
		
		<p>The following information will also be deleted from the OpenTravel web site:
		<ul>
			<li>The specification archive, release notes, and all related publication items.</li>
			<li>All associated comments.</li>
			<li>All associated download history.</li>
		</ul>
	</td></tr>
	<tr><td align="right">
		<br/><a id="submitButton" class="buttonRed" href="javascript:document.forms.specDeleteForm.submit();">Delete Specification</a>
		&nbsp; <a href="${config.localSiteUrl}/admin/ViewSpecification.html?publication=${publication.name}&specType=${publication.type}">Cancel</a>
	</td></tr></table>
</form>
