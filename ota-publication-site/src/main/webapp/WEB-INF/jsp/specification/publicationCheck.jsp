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
<c:if test="${publication == null}">
	<c:if test="${isMembersOnly}">
		<h2 class="sub-title">No version of the OpenTravel
			<c:if test="${expectedPubType != 'OTA_2_0'}">1.0</c:if><c:if test="${expectedPubType == 'OTA_2_0'}">2.0</c:if>
			specification is available for member review at this time.
		</h2>
	</c:if>
	<c:if test="${!isMembersOnly}">
		<h2 class="sub-title">The requested specification is not available.</h2>
	</c:if>
	<p/><br/>
	<p/><br/>
	<p/><br/>
	<p/><br/>
	<p/><br/>
	<p/><br/>
	<p/><br/>
	<p/><br/>
	<p/><br/>
	<p/><br/>
	<p/><br/>
</c:if>