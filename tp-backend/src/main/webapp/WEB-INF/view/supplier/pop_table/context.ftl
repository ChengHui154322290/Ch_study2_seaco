<#macro backend title="" 
	js=[] 
	css=[] 
	>
<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
    <head>
    <#assign popDomain="${domain}" />
    <script>var popDomain = "${popDomain}";</script>
    <script>
	    jQuery(function(){
			var compressedTds = jQuery("td.line-compress");
			for(var i=0;i<compressedTds.length;i++) {
				var cTd = compressedTds[i];
				var w = jQuery(cTd).attr("t-width");
				if(w){
				    w = w+"px;";
				} else {
				    w = "200px;"
				}
				var titleTxt = jQuery(cTd).html();
				jQuery(cTd).html("<p class='line-compress-p' style='width:"+w+"' title='"+titleTxt+"'>"+titleTxt+"</p>");
			}
		});
	</script>
	<#list css as file>   
		<#if file?lower_case?starts_with('http://')>
			<link rel="stylesheet" href="${file}" />
		<#else>
			<link rel="stylesheet" href="${popDomain}${file}" />	
		</#if>		
	</#list>
	<#list js as file>
		<#if file?lower_case?starts_with('http://')>   		
			<script type="text/javascript" src="${file}"></script>
		<#else>
			<script type="text/javascript" src="${popDomain}${file}"></script>
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