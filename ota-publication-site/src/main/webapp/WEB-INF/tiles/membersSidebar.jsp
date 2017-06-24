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
					<c:when test="${(currentPage == 'specification10MainMembers') || ((currentPage == 'specificationReleaseNotesMembers') && (publication.type == 'OTA_1_0'))}">
						<a id="SideNavControl1_MenuHyperLink" class="current" href="${config.localSiteUrl}/specifications/members/Specifications.html">1.0 Specification</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl1_MenuHyperLink" href="${config.localSiteUrl}/specifications/members/Specifications.html">1.0 Specification</a>
					</c:otherwise>
				</c:choose></li>

			<li><c:choose>
					<c:when test="${(currentPage == 'specification20MainMembers') || ((currentPage == 'specificationReleaseNotesMembers') && (publication.type == 'OTA_2_0'))}">
						<a id="SideNavControl1a_MenuHyperLink" class="current" href="${config.localSiteUrl}/specifications/members/Specifications20.html">2.0 Specification</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl1a_MenuHyperLink" href="${config.localSiteUrl}/specifications/members/Specifications20.html">2.0 Specification</a>
					</c:otherwise>
				</c:choose></li>

			<li><c:choose>
					<c:when test="${currentPage == 'specificationComment10Members'}">
						<a id="SideNavControl2_MenuHyperLink" class="current" href="${config.localSiteUrl}/specifications/members/Comment10Spec.html">Comment on 1.0 Specification</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl2_MenuHyperLink" href="${config.localSiteUrl}/specifications/members/Comment10Spec.html">Comment on 1.0 Specification</a>
					</c:otherwise>
				</c:choose></li>

			<li><c:choose>
					<c:when test="${currentPage == 'artifactComment10Members'}">
						<a id="SideNavControl3_MenuHyperLink" class="current" href="${config.localSiteUrl}/specifications/members/Comment10Artifact.html">Comment on 1.0 Artifact</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl3_MenuHyperLink" href="${config.localSiteUrl}/specifications/members/Comment10Artifact.html">Comment on 1.0 Artifact</a>
					</c:otherwise>
				</c:choose></li>

			<li><c:choose>
					<c:when test="${currentPage == 'specificationComment20Members'}">
						<a id="SideNavControl4_MenuHyperLink" class="current" href="${config.localSiteUrl}/specifications/members/Comment20Spec.html">Comment on 2.0 Specification</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl4_MenuHyperLink" href="${config.localSiteUrl}/specifications/members/Comment20Spec.html">Comment on 2.0 Specification</a>
					</c:otherwise>
				</c:choose></li>

			<li><c:choose>
					<c:when test="${currentPage == 'artifactComment20Members'}">
						<a id="SideNavControl5_MenuHyperLink" class="current" href="${config.localSiteUrl}/specifications/members/Comment20Artifact.html">Comment on 2.0 Artifact</a>
					</c:when>
					<c:otherwise>
						<a id="SideNavControl5_MenuHyperLink" href="${config.localSiteUrl}/specifications/members/Comment20Artifact.html">Comment on 2.0 Artifact</a>
					</c:otherwise>
				</c:choose></li>

		</ul>
	</aside>
</div>
