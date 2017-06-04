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
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title><tiles:insertAttribute name="title" ignore="true" /></title>
	<link rel="shortcut icon" href="${config.localSiteUrl}/images/icons/favicon.ico" />
	<link id="linkRSS" rel="alternate" type="application/rss+xml" title="RSS 2.0" href="${config.mainSiteUrl}/News/evNewsRSS.aspx" />
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js"></script>
	<script src="${config.mainSiteUrl}/Javascript/Main.js" type="text/javascript"></script>
	<script src="${config.mainSiteUrl}/Javascript/jquery.easing.min.js" type="text/javascript"></script>
	<script src="${config.localSiteUrl}/scripts/jquery.lavalamp.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function() {
			$("#mainNav").lavaLamp({
				fx : "backout",
				speed : 1200
			});
		});
	</script>
	<link href="${config.mainSiteUrl}/App_Themes/Main/Main.css" type="text/css" rel="stylesheet" />
	<link href="${config.localSiteUrl}/styles/SpecStyles.css" type="text/css" rel="stylesheet" />
	<meta name="keywords" />
	<meta name="description" />
</head>
<body>
	<!--container div-->
	<div id="container">
		<!--header div-->
		<tiles:insertAttribute name="header" />
		<!--end header div-->
		<div id="contentWrapper" class="clear">
			<div class="mainContentWrap clear">
				<div id="contentright">
					<c:if test="${errorMessage != null}">
						<div><span style="color: Red">${errorMessage}</span></div>
					</c:if>
					<c:if test="${statusMessage != null}">
						<div><span style="color: Green">${statusMessage}</span></div>
					</c:if>
					<!--content2-->
					<div id="content2" class="twocolumns">
						<tiles:insertAttribute name="body" />
					</div>
					<!--end content2-->
				</div>
			</div>
			
			<!--main2-->
			<div id="main2" class="subNav clear">
				<!--subnav-->
				<tiles:insertAttribute name="sidebar" />
				<!--end subnav-->
			</div>
			<!--end main2-->
			
			<!--imagecontainer2-->
			<div id="imagecontainer2" class="sideImage">
				&nbsp;
				<!--img id="ctl00_ctl00_ContentPlaceHolder1_RotatingSideImage1_SideImage" src="${sidebarImageUrl}" style="border-width: 0px;" /-->
			</div>
			<!--end imagecontainer2-->
		</div>
		
		<!--footer-->
		<tiles:insertAttribute name="footer" />
		<!--end footer-->
		
	</div>
	<script type="text/javascript">
		var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
		document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
	</script>
	<script type="text/javascript">
		try {
			var pageTracker = _gat._getTracker("UA-11179988-1");
			pageTracker._trackPageview();
		} catch (err) {
		}
	</script>
</body>
</html>
