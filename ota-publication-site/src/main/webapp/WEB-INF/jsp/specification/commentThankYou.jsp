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
<h3>
	Thank You for Your Feedback!<br>
</h3>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<br>
<p>Thank you for taking the time to submit a comment on the
	OpenTravel specification.&nbsp; If we have a question about your
	submission, we will contact you.</p>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<c:if test="${submitCommentsUrl != null}">
	<ul class="classActionButtons"><li>
		<a href="${config.localSiteUrl}${submitCommentsUrl}">Submit Another Comment</a>
	</li></ul>
</c:if>

<p/><br/>
<p/><br/>
<p/><br/>
<p/><br/>
<p/><br/>
