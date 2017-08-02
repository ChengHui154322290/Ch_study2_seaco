<#macro sellContent title="西客商城电商后台管理系统" 
	js=[] 
	css=[] 
	>
	<#include "/commons/common.ftl" />
    <#list css as file>   
		<#if file?lower_case?starts_with('http://')>
			<link rel="stylesheet" href="${file}?v=${version}" />
		<#else>
			<link rel="stylesheet" href="${staticPath}${file}?v=${version}" />	
		</#if>		
	</#list>
	<#list js as file>
		<#if file?lower_case?starts_with('http://')>   		
			<script type="text/javascript" src="${file}?v=${version}"></script>
		<#else>
			<script type="text/javascript" src="${staticPath}${file}?v=${version}"></script>
		</#if>
	</#list>
	<#nested/>
</#macro>