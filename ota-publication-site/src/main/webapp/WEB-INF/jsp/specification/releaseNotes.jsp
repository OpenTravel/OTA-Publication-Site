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
<h2 class="sub-title">${publication.name}-${publication.type.displayId} Publication Release Notes</h2>
<p>
	<b>Download ${publication.name}-${publication.type.displayId} Publication:</b>
	<a href="${config.localSiteUrl}/content/specifications/downloads/${publication.name}/${publication.type}/${publication.archiveFilename}">${publication.archiveFilename}</a>
</p>
<hr/>
<b>OpenTravel ${publication.name}-${publication.type.displayId} Publication Release Notes</b>
<br>
${releaseNotesText}
