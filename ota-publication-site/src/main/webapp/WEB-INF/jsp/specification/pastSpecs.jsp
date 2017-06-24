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
<h2 class="sub-title">Past Specification</h2>
<h3>OpenTravel Members - Don't Forget about the Implementation Guide!</h3>
<p>
	<span>The OpenTravel Implementation Guide provides invaluable
		information to both analysts and implementers of the OpenTravel
		specification on how to more easily understand and build software
		systems that are interoperable with other travel systems using the
		OpenTravel schema.&nbsp; Non-members,</span>
		<a href="${config.mainSiteUrl}/Resources/Uploads/PDF/OpenTravel_ImplementationGuide_v1.5_ExecSum.pdf">
			click here for the executive summary
		</a>. To download the document, please visit the
		<a href="http://wiki.opentravel.org/index.php/Public:Resources">OpenTravel Wiki</a>
		(you must have member rights to access the document).
</p>
<p>All previous versions of the OpenTravel specification are
	available below.&nbsp; Please note that changes are made only to the
	most recent version.</p>

<div id="editBox">
	<div id="formWpr">

<c:set var="itemsPerColumn" value="6"/>

<c:if test="${!publications10.isEmpty()}">
	<br/><p><b>OpenTravel 1.0 Specifications</b></p>
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
			<li><a href="${config.localSiteUrl}/specifications/Specifications.html?spec=${publication.name}">${publication.name}</a></li>
			<c:set var="firstRow" value="false"/>
			<c:set var="currentRow" value="${currentRow + 1}"/>
		</c:forEach>
		</ul></td></tr>
	</table>
	<p/>
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
			<li><a href="${config.localSiteUrl}/specifications/Specifications20.html?spec=${publication.name}">${publication.name}</a></li>
			<c:set var="firstRow" value="false"/>
			<c:set var="currentRow" value="${currentRow + 1}"/>
		</c:forEach>
		</ul></td></tr>
	</table>
	<p/>
</c:if>

<c:if test="${!codeLists.isEmpty()}">
	<p><b>OpenTravel Code Lists</b></p>
	<table id="MemberRadioButtonList" class="checkList" border="0">
		<c:set var="rowCount" value="${codeLists.size() / itemsPerColumn}"/>
		<c:set var="currentRow" value="0"/>
		<c:set var="firstRow" value="true"/>
		<tr>
		<c:forEach var="codeList" items="${codeLists}">
			<c:if test="${(currentRow % itemsPerColumn) == 0}">
				<c:if test="${!firstRow}"></ul></td></c:if>
				<td><ul>
			</c:if>
			<li><a href="${config.localSiteUrl}/specifications/CodeLists.html?releaseDate=${codeList.releaseDateLabel}">${codeList.releaseDateLabel}</a></li>
			<c:set var="firstRow" value="false"/>
			<c:set var="currentRow" value="${currentRow + 1}"/>
		</c:forEach>
		</ul></td></tr>
	</table>
</c:if>

	</div>
</div>