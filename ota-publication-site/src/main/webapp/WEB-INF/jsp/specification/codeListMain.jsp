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
<h2 class="sub-title">Download Release ${codeList.releaseDateLabel} of the OpenTravel Code List</h2>
<p>
<c:if test="${registrant == null}">
<p>
	Please complete the form below and accept the license agreement to download
	Release ${codeList.releaseDateLabel} of the OpenTravel Code List.<br/>
</p>
</c:if>

<%@ include file="registrationForm.jsp" %>

<c:if test="${registrant != null}">
	<c:if test="${codeList != null}">
		<p>
			<b>Download Release ${codeList.releaseDateLabel}:</b> <a href="${config.localSiteUrl}/content/specifications/downloads/cl/${codeList.releaseDateLabel}/${codeList.archiveFilename}">${codeList.archiveFilename}</a>
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
