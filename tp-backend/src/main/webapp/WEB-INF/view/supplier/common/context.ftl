<#macro backend title="西客商城电商后台管理系统"
	js=[] 
	css=[] 
	>
<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
   <link rel="stylesheet" href="${domain}/statics/backend/css/common.css">
   <link rel="stylesheet" href="${domain}/statics/backend/css/main.css">
   <script type="text/javascript" src="${domain}/statics/backend/js/jquery.min.js"></script>
   <script type="text/javascript" src="${domain}/statics/backend/js/colResizable-1.3.min.js"></script>
   <script type="text/javascript" src="${domain}/statics/backend/js/common.js"></script>	
   <script>var domain = "${domain}";</script>
		<#list css as file>   
			<#if file?lower_case?starts_with('http://')>
				<link rel="stylesheet" href="${file}?v=${version}" />
			<#else>
				<link rel="stylesheet" href="${domain}${file}?v=${version}" />	
			</#if>		
		</#list>
		<#list js as file>
			<#if file?lower_case?starts_with('http://')>   		
				<script type="text/javascript" src="${file}?v=${version}"></script>
			<#else>
				<script type="text/javascript" src="${domain}${file}?v=${version}"></script>
			</#if>
		</#list>
	</head>
	<body>
		<div  class="container">
		<#nested/>
		</div>
	</body>
</html>
</#macro>