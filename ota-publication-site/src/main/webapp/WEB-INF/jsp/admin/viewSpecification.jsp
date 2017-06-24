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
<h2 class="sub-title">View Specification ${publication.name}-${publication.type.displayId}</h2>

<br/>
<table class="formTable">
	<tr>
		<td width="1%" nowrap><b>Publication Status: </b></td>
		<td>${publication.state.displayValue}</td>
	</tr>
	<tr>
		<td width="1%" nowrap><b>Publication Date: </b></td>
		<td>${formatter.formatDate( publication.publicationDate )}</td>
	</tr>
	<tr>
		<td colspan="2">
			<br><b>Download the Specification Archive: </b><a href="${config.localSiteUrl}/content/specifications/downloads/${publication.name}/${publication.type}/${publication.archiveFilename}">${publication.archiveFilename}</a> | 
			<a href="${config.localSiteUrl}/specifications/ReleaseNotes.html?spec=${publication.name}&specType=${publication.type}">View Release Notes</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<br/><b>Actions:</b>
			<ul>
				<li><a href="${config.localSiteUrl}/admin/SpecificationComments.html?publication=${publication.name}&specType=${publication.type}">View Specification Comments</a></li>
				<li><a href="${config.localSiteUrl}/admin/SpecificationDownloads.html?publication=${publication.name}&specType=${publication.type}">View Download History</a></li>
				<li><a href="${config.localSiteUrl}/admin/UpdateSpecification.html?publicationId=${publication.id}">Update this Specification</a></li>
				<li><a href="${config.localSiteUrl}/admin/DeleteSpecification.html?publicationId=${publication.id}">Delete this Specification</a></li>
			</ul>
		</td>
	</tr>
</table>
