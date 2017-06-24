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
<div class="col-sm-3 sidebar">
	<aside id="text-3" class="widget widget_text">
		<ul>

			<li><c:choose>
					<c:when test="${(currentPage == 'adminMain')}">
						<a id="SideNavControl1_MenuHyperLink" class="current" href="${config.localSiteUrl}/admin/index.html">Administration Home</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl1_MenuHyperLink" href="${config.localSiteUrl}/admin/index.html">Administration Home</a>
					</c:otherwise>
				</c:choose></li>

			<li><c:choose>
					<c:when test="${(currentPage == 'uploadSpecification')}">
						<a id="SideNavControl2_MenuHyperLink" class="current" href="${config.localSiteUrl}/admin/UploadSpecification.html">Upload a New Specification</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl2_MenuHyperLink" href="${config.localSiteUrl}/admin/UploadSpecification.html">Upload a New Specification</a>
					</c:otherwise>
				</c:choose></li>

			<li><c:choose>
					<c:when test="${(currentPage == 'uploadCodeList')}">
						<a id="SideNavControl6_MenuHyperLink" class="current" href="${config.localSiteUrl}/admin/UploadCodeList.html">Upload a New Code List</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl6_MenuHyperLink" href="${config.localSiteUrl}/admin/UploadCodeList.html">Upload a New Code List</a>
					</c:otherwise>
				</c:choose></li>

			<li><c:choose>
					<c:when test="${(currentPage == 'viewRegistrants')}">
						<a id="SideNavControl3_MenuHyperLink" class="current" href="${config.localSiteUrl}/admin/ViewRegistrants.html">View Web Site Registrants</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl3_MenuHyperLink" href="${config.localSiteUrl}/admin/ViewRegistrants.html">View Web Site Registrants</a>
					</c:otherwise>
				</c:choose></li>

			<li><c:choose>
					<c:when test="${(currentPage == 'emailSettings')}">
						<a id="SideNavControl4_MenuHyperLink" class="current" href="${config.localSiteUrl}/admin/EmailSettings.html">Email Notification Settings</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl4_MenuHyperLink" href="${config.localSiteUrl}/admin/EmailSettings.html">Email Notification Settings</a>
					</c:otherwise>
				</c:choose></li>

			<li><c:choose>
					<c:when test="${(currentPage == 'changeCredentials')}">
						<a id="SideNavControl5_MenuHyperLink" class="current" href="${config.localSiteUrl}/admin/ChangeAdminCredentials.html">Change Admin Login Credentials</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl5_MenuHyperLink" href="${config.localSiteUrl}/admin/ChangeAdminCredentials.html">Change Admin Login Credentials</a>
					</c:otherwise>
				</c:choose></li>

		</ul>
	</aside>
</div>
