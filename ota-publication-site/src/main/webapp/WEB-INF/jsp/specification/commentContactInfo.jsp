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
		<tr>
			<td colspan="2">
				<h3>Contact Information</h3>
			</td>
		</tr>
		<c:if test="${registrant == null}">
			<%@ include file="registrantInfoForm.jsp" %>
		</c:if>
		<c:if test="${registrant != null}">
			<tr>
				<td class="required">First Name: </td>
				<td>${registrant.firstName}</td>
			</tr>
			<tr>
				<td class="required">Last Name: </td>
				<td>${registrant.lastName}</td>
			</tr>
			<tr>
				<td>Title</td>
				<td>${registrant.title}</td>
			</tr>
			<tr>
				<td class="required">Company: </td>
				<td>${registrant.company}</td>
			</tr>
			<tr>
				<td>Phone</td>
				<td>${registrant.phone}</td>
			</tr>
			<tr valign="top">
				<td class="required">E-mail: </td>
				<td>${registrant.email}</td>
			</tr>
			<tr>
				<td class="required">OpenTravel Member? </td>
				<td>${registrant.otaMember ? "Yes" : "No"}</td>
			</tr>
			<c:if test="${registrant != null}">
				<tr>
					<td colspan="2">
						<p class="small"><i>Not ${registrant.firstName}? Click <a href="${config.localSiteUrl}${submitCommentsUrl}?newSession=true"/>here</a> to re-register.</i></p>
					</td>
				</tr>
			</c:if>
		</c:if>
