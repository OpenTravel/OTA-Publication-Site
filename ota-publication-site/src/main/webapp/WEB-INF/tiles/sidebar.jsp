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
<div id="subnav">
	<ul>

		<li>
			<c:choose>
				<c:when test="${(currentPage == 'specificationMain') || (currentPage == 'specificationReleaseNotes')}">
					<a id="SideNavControl1_MenuHyperLink" class="current"
						href="${config.localSiteUrl}/specifications/Specifications.html">Specification</a>
				</c:when>
				<c:otherwise>
					<a id="SideNavControl1_MenuHyperLink"
						href="${config.localSiteUrl}/specifications/Specifications.html">Specification</a>
				</c:otherwise>
			</c:choose>
		</li>

		<li>
			<c:choose>
				<c:when test="${currentPage == 'specificationComment10'}">
					<a id="SideNavControl2_MenuHyperLink" class="current"
						href="${config.localSiteUrl}/specifications/Comment10Spec.html">Comment on 1.0 Specification</a>
				</c:when>
				<c:otherwise>
					<a id="SideNavControl2_MenuHyperLink"
						href="${config.localSiteUrl}/specifications/Comment10Spec.html">Comment on 1.0 Specification</a>
				</c:otherwise>
			</c:choose>
		</li>

		<li>
			<c:choose>
				<c:when test="${currentPage == 'artifactComment10'}">
					<a id="SideNavControl3_MenuHyperLink" class="current"
						href="${config.localSiteUrl}/specifications/Comment10Artifact.html">Comment on 1.0 Artifact</a>
				</c:when>
				<c:otherwise>
					<a id="SideNavControl3_MenuHyperLink"
						href="${config.localSiteUrl}/specifications/Comment10Artifact.html">Comment on 1.0 Artifact</a>
				</c:otherwise>
			</c:choose>
		</li>

		<li>
			<c:choose>
				<c:when test="${currentPage == 'specificationComment20'}">
					<a id="SideNavControl4_MenuHyperLink" class="current"
						href="${config.localSiteUrl}/specifications/Comment20Spec.html">Comment on 2.0 Specification</a>
				</c:when>
				<c:otherwise>
					<a id="SideNavControl4_MenuHyperLink"
						href="${config.localSiteUrl}/specifications/Comment20Spec.html">Comment on 2.0 Specification</a>
				</c:otherwise>
			</c:choose>
		</li>

		<li>
			<c:choose>
				<c:when test="${currentPage == 'artifactComment20'}">
					<a id="SideNavControl5_MenuHyperLink" class="current"
						href="${config.localSiteUrl}/specifications/Comment20Artifact.html">Comment on 2.0 Artifact</a>
				</c:when>
				<c:otherwise>
					<a id="SideNavControl5_MenuHyperLink"
						href="${config.localSiteUrl}/specifications/Comment20Artifact.html">Comment on 2.0 Artifact</a>
				</c:otherwise>
			</c:choose>
		</li>

		<li>
			<c:choose>
				<c:when test="${currentPage == 'modelViewer'}">
					<a id="SideNavControl6_MenuHyperLink" class="current"
						href="${config.localSiteUrl}/specifications/ModelViewer.html">OpenTravel Model Viewer</a>
				</c:when>
				<c:otherwise>
					<a id="SideNavControl6_MenuHyperLink"
						href="${config.localSiteUrl}/specifications/ModelViewer.html">OpenTravel Model Viewer</a>
				</c:otherwise>
			</c:choose>
		</li>

		<li>
			<c:choose>
				<c:when test="${(currentPage == 'onlinePublications') || (currentPage == 'onlinePublicationDetails') || (currentPage == 'downloadRegister')}">
					<a id="SideNavControl7_MenuHyperLink" class="current"
						href="${config.localSiteUrl}/specifications/OnlinePublications.html">Browse Publications</a>
				</c:when>
				<c:otherwise>
					<a id="SideNavControl7_MenuHyperLink"
						href="${config.localSiteUrl}/specifications/OnlinePublications.html">Browse Publications</a>
				</c:otherwise>
			</c:choose>
		</li>

		<li>
			<c:choose>
				<c:when test="${currentPage == 'pastSpecifications'}">
					<a id="SideNavControl8_MenuHyperLink" class="current"
						href="${config.localSiteUrl}/specifications/PastSpecs.html">Past Specifications</a>
				</c:when>
				<c:otherwise>
					<a id="SideNavControl8_MenuHyperLink"
						href="${config.localSiteUrl}/specifications/PastSpecs.html">Past Specifications</a>
				</c:otherwise>
			</c:choose>
		</li>

		<li>
			<c:choose>
				<c:when test="${currentPage == 'specificationFAQ'}">
					<a id="SideNavControl9_MenuHyperLink" class="current"
						href="${config.localSiteUrl}/specifications/FAQ.html">FAQ</a>
				</c:when>
				<c:otherwise>
					<a id="SideNavControl9_MenuHyperLink"
						href="${config.localSiteUrl}/specifications/FAQ.html">FAQ</a>
				</c:otherwise>
			</c:choose>
		</li>

	</ul>
</div>