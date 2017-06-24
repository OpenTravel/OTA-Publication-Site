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
<%@ include file="publicationCheck.jsp" %>
<c:if test="${publication != null}">
<h2 class="sub-title">Download the ${publication.name}-${publication.type.displayId}
	<c:choose>
		<c:when test="${publication.state == 'PUBLIC_REVIEW'}">Public Review</c:when>
		<c:when test="${publication.state == 'MEMBER_REVIEW'}">Member Review</c:when>
	</c:choose>
	Version of the OpenTravel Schema </h2>
<p2>
<c:if test="${registrant == null}">
<p>
	Please complete the form below and accept the license agreement to download the
	${publication.name}-${publication.type.displayId} Version of the OpenTravel
	specification.<br/>
</p>
</c:if>

<%@ include file="registrationForm.jsp" %>

<c:if test="${registrant != null}">
	<c:if test="${publication != null}">
		<p>
			<b>Download ${publication.name}-${publication.type.displayId} Publication:</b> <a href="${config.localSiteUrl}/content/specifications/downloads/${publication.name}/${publication.type}/${publication.archiveFilename}">${publication.archiveFilename}</a> | 
			<a href="${config.localSiteUrl}${releaseNotesUrl}?spec=${publication.name}&specType=${publication.type}">View Release Notes</a>
		</p>
		<p/><br/>
		<p/><br/>
		<p/><br/>
		<p/><br/>
		<p/><br/>
		<p/><br/>
		<p/><br/>
	</c:if>
</c:if>
</c:if>
