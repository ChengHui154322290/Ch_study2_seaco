<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户登录</title>
		<#include "/common/common.ftl">
		<script type="text/javascript" src="${domain}/statics/js/lib.js"></script>
		<script type="text/javascript" src="${js}/jquery.form.min.js"></script>
		<#-- 自定义js-->
		
		<#-- 验证form表单 js-->
		<script type="text/javascript" src="${validation}/jquery.validate.min.js" ></script>
		<script type="text/javascript" src="${validation}/jquery.validate.method.min.js" ></script>
		
		<#-- 提示框 js-->
		<script type="text/javascript" src="${messenger}/js/messenger.min.js"></script>
		
		<script type="text/javascript" src="${js}/common.js"></script>
		
		<#-- 验证form表单 css -->
		<link type="text/css" rel="stylesheet" href="${validation}/jquery.validate.min.css"  />
		
		<#-- 提示框 样式-->
		<link href="${messenger}/css/messenger.css" rel="stylesheet"  type="text/css"/>
		<link href="${messenger}/css/messenger-theme-future.css" rel="stylesheet"  type="text/css"/>
	  	<link rel="stylesheet" href="${backend_folder}/css/login.css">
	  	<style>
	  		
		label.error {
		    color: #ea5200;
		    display: block;
		    margin-left: 10px;
		    padding-bottom: 2px;
		    position: absolute;
		}
		
		#btn_area label.error {
		    color: #ea5200;
		    display: block;
		    margin-left: 10px;
		    padding-bottom: 2px;
		    position: absolute;
		    top: 39px;
		}
	  	</style>
	  	<script>
	  	if(window.frameElement!=null){
			window.parent.location.reload();
		}
		</script>
	</head>
	<body>
	<div id="login_top">
		<div id="welcome">
			${logo}
		</div>
		<div id="back">
			<a href="${base}/index">返回首页</a>&nbsp;&nbsp; | &nbsp;&nbsp;
			<a href="javascript:void(0);">帮助</a>
		</div>
	</div>
	<div id="login_center">
		<div id="login_area">
			<div id="login_form">
				<form id="login" autocomplete="off">
					<div id="login_tip" style="position: relative;margin-left:10px;">
						用户登录
					</div>
					<div style="position: relative;"><input type="text" placeholder="登录名" name="loginName" class="username"></div>
					<div style="position: relative;"><input type="password" placeholder="密码" name="password" class="pwd"></div>
					<div style="position: relative;" id="btn_area">
						<input type="submit" class="sub_btn" id="sub_btn" value="">
				<!--		<input type="text" name="securityCode"  class="verify"> -->
				<!--		<img id="securityCode" src="${base}/securityCode/0" alt="验证码" width="80" height="40"> -->
					</div>
				</form>
				<@shiro.user>
				<form action="${base}/index"  autocomplete="off">
					<div id="login_tip" style="position: relative;">
						欢迎您,<@shiro.principal property="userName" />!
					</div>
					<div style="position: relative;" id="btn_area">
						<input type="submit" class="sub_btn" value="进入首页">
						<a href="${base}/logout">登出</a>
					</div>
					
				</form>
				</@shiro.user>
			</div>
		</div>
	</div>
	<div id="login_bottom">
		版权所有
	</div>
</body>
</html>

<script type="text/javascript">
	$(document).ready(function(){
		
		$("#securityCode").click(function(){
   			$("#securityCode").attr("src","${base}/securityCode/0?timestamp="+new Date().getTime());
   		});
   		
   		$("#login").validate({
	            rules: {
	                loginName:{required: true},
	                password:{required: true}
	            },
	            messages: {
	                loginName: {required:"请输入用户名"},
	                password: {required:"请输入密码"},
	                securityCode: {required:"请输入验证码", rangelength:jQuery.format("最多{0}个字符")}
	            },
	            submitHandler: function (form) { 
	            	$("#sub_btn").attr("disable","disabled");   
	                $.ajax({
	                    url: "${base}/doLogin",
	                    data: $("#login").serialize(),
	                    type:'POST',
	                    dataType:'json',
	                    success : function(result) {
							if (result.success) {
								window.location.href="${base}/index";
							} else {
								
								$("#sub_btn").removeAttr("disabled");
								$("#securityCode").click();
								error(result.msg.message);
							}
						}
	                });
	            },
	            errorPlacement: function (error, element) {
            		 error.appendTo(element.parent()); 
     	 
	            },
	            success: function (label, element) {
	                $(label).remove();
	            }
			});	
   		
   	});
</script>