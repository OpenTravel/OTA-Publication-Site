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
<h2 class="sub-title">View Code List Release ${codeList.releaseDateLabel}</h2>

<br/>
<table class="formTable">
	<tr>
		<td width="1%" nowrap><b>Release Date: </b></td>
		<td>${codeList.releaseDateLabel}</td>
	</tr>
	<tr>
		<td colspan="2">
			<br><b>Download the Code List Archive: </b><a href="${config.localSiteUrl}/content/specifications/downloads/cl/${codeList.releaseDateLabel}/${codeList.archiveFilename}">${codeList.archiveFilename}</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<br/><b>Actions:</b>
			<ul>
				<li><a href="${config.localSiteUrl}/admin/CodeListDownloads.html?releaseDate=${codeList.releaseDateLabel}">View Download History</a></li>
				<li><a href="${config.localSiteUrl}/admin/UpdateCodeList.html?codeListId=${codeList.id}">Update this Code List</a></li>
				<li><a href="${config.localSiteUrl}/admin/DeleteCodeList.html?codeListId=${codeList.id}">Delete this Code List</a></li>
			</ul>
		</td>
	</tr>
</table>
