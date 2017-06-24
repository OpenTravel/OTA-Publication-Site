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
<h2 class="sub-title">Administration Home</h2>

<p>Click on one the specification links to view or edit an existing specification, or select from one of the following actions.</p>

<ul>
	<li>
		<a href="${config.localSiteUrl}/admin/UploadSpecification.html">Upload a New Specification</a>
	</li>
	<li>
		<a href="${config.localSiteUrl}/admin/UploadCodeList.html">Upload a New Code List</a>
	</li>
	<li>
		<a href="${config.localSiteUrl}/admin/ViewRegistrants.html">View Web Site Registrants</a>
	</li>
	<li>
		<a href="${config.localSiteUrl}/admin/ChangeAdminCredentials.html">Change Admin Login Credentials</a>
	</li>
</ul>
<br/>

<c:set var="itemsPerColumn" value="6"/>

<c:if test="${!publications10.isEmpty()}">
	<p><b>OpenTravel 1.0 Specifications</b></p>
	<table id="MemberRadioButtonList" class="checkList" border="0">
		<c:set var="rowCount" value="${publications10.size() / itemsPerColumn}"/>
		<c:set var="currentRow" value="0"/>
		<c:set var="firstRow" value="true"/>
		<tr>
		<c:forEach var="publication" items="${publications10}">
			<c:if test="${publication.state == 'PUBLIC_REVIEW'}"><c:set var="pubStatus" value="&nbsp;(PR)"/></c:if>
			<c:if test="${publication.state == 'MEMBER_REVIEW'}"><c:set var="pubStatus" value="&nbsp;(MR)"/></c:if>
			<c:if test="${publication.state == 'RELEASED'}"><c:set var="pubStatus" value=""/></c:if>
			<c:if test="${(currentRow % itemsPerColumn) == 0}">
				<c:if test="${!firstRow}"></ul></td></c:if>
				<td><ul>
			</c:if>
			<li>
				<a href="${config.localSiteUrl}/admin/ViewSpecification.html?publication=${publication.name}&specType=${publication.type}">${publication.name}${pubStatus}</a>
			</li>
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
			<c:if test="${publication.state == 'PUBLIC_REVIEW'}"><c:set var="pubStatus" value="&nbsp;(PR)"/></c:if>
			<c:if test="${publication.state == 'MEMBER_REVIEW'}"><c:set var="pubStatus" value="&nbsp;(MR)"/></c:if>
			<c:if test="${publication.state == 'RELEASED'}"><c:set var="pubStatus" value=""/></c:if>
			<c:if test="${(currentRow % itemsPerColumn) == 0}">
				<c:if test="${!firstRow}"></ul></td></c:if>
				<td nowrap><ul>
			</c:if>
			<li>
				<a href="${config.localSiteUrl}/admin/ViewSpecification.html?publication=${publication.name}&specType=${publication.type}">${publication.name}${pubStatus}</a>
			</li>
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
				<td nowrap><ul>
			</c:if>
			<li>
				<a href="${config.localSiteUrl}/admin/ViewCodeList.html?releaseDate=${codeList.releaseDateLabel}">${codeList.releaseDateLabel}</a>
			</li>
			<c:set var="firstRow" value="false"/>
			<c:set var="currentRow" value="${currentRow + 1}"/>
		</c:forEach>
		</ul></td></tr>
	</table>
</c:if>
