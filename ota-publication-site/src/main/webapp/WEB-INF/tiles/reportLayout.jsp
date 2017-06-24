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
<!DOCTYPE html>
<html lang="en-US">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta name="description" content="Enabling the Future">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="pingback" href="${config.mainSiteUrl}/scripts/xmlrpc.php">
		<title><tiles:insertAttribute name="title" ignore="true" /></title>
		<link rel="dns-prefetch" href="http://s.w.org/">
		<link rel="alternate" type="application/rss+xml" title="Open Travel » Feed" href="${config.mainSiteUrl}/feed/">
		<link rel="alternate" type="application/rss+xml" title="Open Travel » Comments Feed" href="${config.mainSiteUrl}/comments/feed/">
		<script src="${config.localSiteUrl}/scripts/analytics.js"></script>
		<script type="text/javascript">
					window._wpemojiSettings = {"baseUrl":"https:\/\/s.w.org\/images\/core\/emoji\/2.2.1\/72x72\/","ext":".png","svgUrl":"https:\/\/s.w.org\/images\/core\/emoji\/2.2.1\/svg\/","svgExt":".svg","source":{"concatemoji":"http:\/\/opentravel.org\/wp-includes\/js\/wp-emoji-release.min.js?ver=4.7.5"}};
					!function(a,b,c){function d(a){var b,c,d,e,f=String.fromCharCode;if(!k||!k.fillText)return!1;switch(k.clearRect(0,0,j.width,j.height),k.textBaseline="top",k.font="600 32px Arial",a){case"flag":return k.fillText(f(55356,56826,55356,56819),0,0),!(j.toDataURL().length<3e3)&&(k.clearRect(0,0,j.width,j.height),k.fillText(f(55356,57331,65039,8205,55356,57096),0,0),b=j.toDataURL(),k.clearRect(0,0,j.width,j.height),k.fillText(f(55356,57331,55356,57096),0,0),c=j.toDataURL(),b!==c);case"emoji4":return k.fillText(f(55357,56425,55356,57341,8205,55357,56507),0,0),d=j.toDataURL(),k.clearRect(0,0,j.width,j.height),k.fillText(f(55357,56425,55356,57341,55357,56507),0,0),e=j.toDataURL(),d!==e}return!1}function e(a){var c=b.createElement("script");c.src=a,c.defer=c.type="text/javascript",b.getElementsByTagName("head")[0].appendChild(c)}var f,g,h,i,j=b.createElement("canvas"),k=j.getContext&&j.getContext("2d");for(i=Array("flag","emoji4"),c.supports={everything:!0,everythingExceptFlag:!0},h=0;h<i.length;h++)c.supports[i[h]]=d(i[h]),c.supports.everything=c.supports.everything&&c.supports[i[h]],"flag"!==i[h]&&(c.supports.everythingExceptFlag=c.supports.everythingExceptFlag&&c.supports[i[h]]);c.supports.everythingExceptFlag=c.supports.everythingExceptFlag&&!c.supports.flag,c.DOMReady=!1,c.readyCallback=function(){c.DOMReady=!0},c.supports.everything||(g=function(){c.readyCallback()},b.addEventListener?(b.addEventListener("DOMContentLoaded",g,!1),a.addEventListener("load",g,!1)):(a.attachEvent("onload",g),b.attachEvent("onreadystatechange",function(){"complete"===b.readyState&&c.readyCallback()})),f=c.source||{},f.concatemoji?e(f.concatemoji):f.wpemoji&&f.twemoji&&(e(f.twemoji),e(f.wpemoji)))}(window,document,window._wpemojiSettings);
		</script>
		<script src="${config.localSiteUrl}/scripts/wp-emoji-release.js" type="text/javascript" defer="defer"></script>
		<style type="text/css">
			img.wp-smiley, img.emoji {
				display: inline !important;
				border: none !important;
				box-shadow: none !important;
				height: 1em !important;
				width: 1em !important;
				margin: 0 .07em !important;
				vertical-align: -0.1em !important;
				background: none !important;
				padding: 0 !important;
			}
		</style>
			<link rel="stylesheet" id="wgs-css" href="${config.localSiteUrl}/styles/wgs.css" type="text/css" media="all">
		<link rel="stylesheet" id="bootstrap-style-css" href="${config.localSiteUrl}/styles/bootstrap_002.css" type="text/css" media="all">
		<link rel="stylesheet" id="bootstraptheme-css" href="${config.localSiteUrl}/styles/bootstrap-theme.css" type="text/css" media="all">
		<link rel="stylesheet" id="fontawesome-style-css" href="${config.localSiteUrl}/styles/font-awesome.css" type="text/css" media="all">
		<link rel="stylesheet" id="style.css-css" href="${config.localSiteUrl}/styles/style.css" type="text/css" media="all">
		<link rel="stylesheet" id="custom.css-css" href="${config.localSiteUrl}/styles/custom.css" type="text/css" media="all">
		<link rel="stylesheet" id="custom-pubs.css-css" href="${config.localSiteUrl}/styles/custom-pubs.css" type="text/css" media="all">
		<link rel="stylesheet" id="bootstrap.css-css" href="${config.localSiteUrl}/styles/bootstrap.css" type="text/css" media="all">
		<link rel="stylesheet" id="stylesheet-css" href="${config.localSiteUrl}/styles/style_002.css" type="text/css" media="all">
		<link rel="stylesheet" id="sccss_style-css" href="${config.localSiteUrl}/styles/a.css" type="text/css" media="all">
		<script type="text/javascript" src="${config.localSiteUrl}/scripts/jquery_002.js"></script>
		<script type="text/javascript" src="${config.localSiteUrl}/scripts/jquery-migrate.js"></script>
		<script type="text/javascript" src="${config.localSiteUrl}/scripts/autoscroll.js"></script>
		<script type="text/javascript" src="${config.localSiteUrl}/scripts/custom.js"></script>
		<link rel="https://api.w.org/" href="${config.mainSiteUrl}/wp-json/">
		<link rel="EditURI" type="application/rsd+xml" title="RSD" href="${config.mainSiteUrl}/xmlrpc.php?rsd">
		<link rel="wlwmanifest" type="application/wlwmanifest+xml" href="${config.mainSiteUrl}/wp-includes/wlwmanifest.xml">
		<link rel="canonical" href="${config.mainSiteUrl}/about-us/frequently-asked-questions-about-opentravel/">
		<link rel="shortlink" href="${config.mainSiteUrl}/?p=31">
		<link rel="alternate" type="application/json+oembed" href="${config.mainSiteUrl}/wp-json/oembed/1.0/embed?url=http%3A%2F%2Fopentravel.org%2Fabout-us%2Ffrequently-asked-questions-about-opentravel%2F">
		<link rel="alternate" type="text/xml+oembed" href="${config.mainSiteUrl}/wp-json/oembed/1.0/embed?url=http%3A%2F%2Fopentravel.org%2Fabout-us%2Ffrequently-asked-questions-about-opentravel%2F&amp;format=xml">
		<style>
			.custom-header-text-color {
				color: #000
			}
		</style>
		<script type="text/javascript" src="${config.localSiteUrl}/scripts/jquery.js"></script>
		<script type="text/javascript">
			$('.dropdown-toggle').click(function() { var location = $(this).attr('href'); 
			window.location.href = location; return false; });
		</script>
</head>
<body class="page-template-default page page-id-31 page-parent page-child parent-pageid-9">
	<tiles:insertAttribute name="header" />

	<div class="sub-header">
		<img src="" alt="">
	</div>
	<div class="sub-page">
		<div class="container">
			<div class="row">
				<c:if test="${errorMessage != null}">
					<div><span style="color: Red">${errorMessage}</span></div>
				</c:if>
				<div class="col-sm-9">
					<tiles:insertAttribute name="body" />
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
/* <![CDATA[ */
var scriptParams = {"google_search_engine_id":""};
/* ]]> */
</script>
	<script type="text/javascript" src="${config.localSiteUrl}/scripts/google_cse_v2.js"></script>
	<script type="text/javascript" src="${config.localSiteUrl}/scripts/bootstrap_002.js"></script>
	<script type="text/javascript" src="${config.localSiteUrl}/scripts/scripts.js"></script>
	<script type="text/javascript" src="${config.localSiteUrl}/scripts/bootstrap.js"></script>
	<script type="text/javascript" src="${config.localSiteUrl}/scripts/wp-embed.js"></script>
	<script>

$(function(){

	$('.single-departments .panel-group').on('show.bs.collapse', function(e) {

    		$(e.target).prev('.panel-heading').addClass('actives');

  	}).on('hide.bs.collapse', function(e) {

    		$(e.target).prev('.panel-heading').removeClass('actives');

  	});

});

</script>
	<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-100134995-1', 'auto');
  ga('send', 'pageview');

</script>
</body>
</html>