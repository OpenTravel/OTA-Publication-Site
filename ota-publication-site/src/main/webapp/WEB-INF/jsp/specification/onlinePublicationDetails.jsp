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
<c:set var="urlPubType" value="${publication.type.displayId.replaceAll('\\\\.', '_')}" />
<h2 class="sub-title">Browse the ${publication.name}-${publication.type.displayId} Publication</h2>

<div class="tblwrap">
<table id="grouptable" align="left">
	<tr>
		<c:forEach var="group" items="${groupList}">
			<c:if test="${!group.removed}">
			<c:choose>
				<c:when test="${group.id == selectedGroup}"><th class="sel">${group.name}</th></c:when>
				<c:otherwise>
					<th class="nsel"><a href="${config.localSiteUrl}/specifications/OnlinePublicationDetails.html?spec=${publication.name}&specType=${urlPubType}&group=${group.id}">${group.name}</a></th>
				</c:otherwise>
			</c:choose>
			</c:if>
		</c:forEach>
	</tr>
</table>
</div>
<p><br/><br/>

<div class="tblwrap">
<table id="browsetable" align="left">
	<tr>
		<th>Filename</th>
		<th>Created</th>
		<th colspan="2">File Size</th>
	</tr>
	<c:forEach var="item" items="${itemList}">
	<c:if test="${!item.removed}">
	<tr>
		<td><a href="${config.localSiteUrl}/content/specifications/downloads/${publication.name}/${urlPubType}/${item.itemFilename}">${item.itemFilename}</a></td>
		<td>${formatter.formatDate( item.createDate )}</td>
		<td class="fileSize">${formatter.formatFileSize( item.fileSize )}</td>
		<td><a href="${config.localSiteUrl}/content/specifications/downloads/noregister/${publication.name}/${urlPubType}/${item.itemFilename}">
			<img src="${config.localSiteUrl}/images/link.png" title="Automated download link (no registration required)"/>
		</a></td>
	</tr>
	</c:if>
	</c:forEach>
</table>
</div>

<div class="tblwrap">
<c:if test="${itemList.size() < 20}">
	<c:forEach var="count" begin="0" end="${20-itemList.size()}">
		<br/>
	</c:forEach>
</c:if>
</div>