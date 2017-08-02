<!doctype html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>开发专用</title>
    <#include "/common/common.ftl"/>
	<link rel="stylesheet" href="${kaifa}/css/reset.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="${kaifa}/css/style.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="${kaifa}/css/invalid.css" type="text/css" media="screen" />
	<script type="text/javascript" src="${statics}/js/lib.js"></script>
	
	<#-- 验证form表单 js-->
	<script type="text/javascript" src="${validation}/jquery.validate.min.js" ></script>
	<script type="text/javascript" src="${validation}/jquery.validate.method.min.js" ></script>
	
	<#-- 验证form表单 css -->
	<link type="text/css" rel="stylesheet" href="${validation}/jquery.validate.min.css"  />
	<script>
		$(function(){
			$(".linkA").click(function(){
				var url = $(this).attr("data-url");
				$.post(url,null,function(html){
						$("#main-content").html(html);
					},"html");
			
			});
			
			$("#link_menu").click();
		});
	</script>
</head>
<body>
<div id="body-wrapper">
  <div id="sidebar">
    <div id="sidebar-wrapper">
      <h1 id="sidebar-title"><a href="javascript:void(0);">开发专用后台</a></h1>
      <a href="${base}/index" style="margin-left: 15%;"></a>
      <ul id="main-nav">
        <li> 
          <a href="javascript:void(0);" class="nav-top-item sysMenu">菜单管理 </a>
          <ul>
            <li><a class="linkA" id="link_menu" data-url="${base}/kaifa/sysMenuManage" parent="sysMenu" href="javascript:void(0);">新增菜单</a></li>
          </ul>
        </li>
      </ul>
    
    </div>
  </div>
  <div id="main-content">
  		<h2>开发人员专用后台</h2>
    	<p id="page-intro">主要用于添加菜单,添加部门</p>
  </div>
</div>
</body>
</html>

   
 