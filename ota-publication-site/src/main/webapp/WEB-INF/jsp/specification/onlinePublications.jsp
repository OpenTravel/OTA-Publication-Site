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
<h2 class="sub-title">Browse Publications</h2>
<p>
	These pages provide a central location by which each OpenTravel schema
	can be accessed via its own, unique URL. For instance, entering
	"<span class="techtext">${config.localSiteUrl}/content/specifications/downloads/2004A/1_0/OTA_CommonTypes.xsd</span>"
	into a brower's 'Address' field will display the contents of the
	<span class="techtext">OTA_CommonTypes</span> schema file from the
	2004A-1.0 release. <br />
</p>
<p>These online schema are not meant to replace the downloadable
	specifications available for download from this site, and are not
	intended to be accessed at run-time by production systems.&nbsp; They
	are meant simply to provide a set of convenient reference pages to
	implementers who need to quickly view a particular schema using a
	utility such as a web browser.</p>
<p>
	OpenTravel retains the right to change these URL addresses at any time
	with no public notice.<br />
</p>
<c:set var="itemsPerColumn" value="6"/>

<br/>
<c:if test="${!publications10.isEmpty()}">
	<p><b>OpenTravel 1.0 Specifications</b></p>
	<table id="MemberRadioButtonList" class="checkList" border="0">
		<c:set var="rowCount" value="${publications10.size() / itemsPerColumn}"/>
		<c:set var="currentRow" value="0"/>
		<c:set var="firstRow" value="true"/>
		<tr>
		<c:forEach var="publication" items="${publications10}">
			<c:if test="${(currentRow % itemsPerColumn) == 0}">
				<c:if test="${!firstRow}"></ul></td></c:if>
				<td><ul>
			</c:if>
			<li><a href="${config.localSiteUrl}/specifications/OnlinePublicationDetails.html?spec=${publication.name}&specType=OTA_1_0">${publication.name}</a></li>
			<c:set var="firstRow" value="false"/>
			<c:set var="currentRow" value="${currentRow + 1}"/>
		</c:forEach>
		</ul></td></tr>
	</table>
</c:if>

<c:if test="${!publications20.isEmpty()}">
	<p><b>OpenTravel 2.0 Specifications</b></p>
	<table id="MemberRadioButtonList" class="checkList" border="0">
		<c:set var="rowCount" value="${publications20.size() / itemsPerColumn}"/>
		<c:set var="currentRow" value="0"/>
		<c:set var="firstRow" value="true"/>
		<tr>
		<c:forEach var="publication" items="${publications20}">
			<c:if test="${(currentRow % itemsPerColumn) == 0}">
				<c:if test="${!firstRow}"></ul></td></c:if>
				<td><ul>
			</c:if>
			<li><a href="${config.localSiteUrl}/specifications/OnlinePublicationDetails.html?spec=${publication.name}&specType=OTA_2_0">${publication.name}</a></li>
			<c:set var="firstRow" value="false"/>
			<c:set var="currentRow" value="${currentRow + 1}"/>
		</c:forEach>
		</ul></td></tr>
	</table>
</c:if>
