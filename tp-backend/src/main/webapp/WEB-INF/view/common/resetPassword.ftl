<!doctype html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>重置密码</title>
    <#include "/common/common.ftl"/>
	<link rel="stylesheet" href="${kaifa}/css/reset.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="${kaifa}/css/style.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="${kaifa}/css/invalid.css" type="text/css" media="screen" />
	<script type="text/javascript" src="${static_domain}/js/lib.js"></script>
	<#-- 验证form表单 js-->
	<script type="text/javascript" src="${validation}/jquery.validate.min.js" ></script>
	<script type="text/javascript" src="${validation}/jquery.validate.method.min.js" ></script>
	
	<#-- 验证form表单 css -->
	<link type="text/css" rel="stylesheet" href="${validation}/jquery.validate.min.css"  />
	<script>
		$(function(){
		
		});
	</script>
</head>
<body>
<div id="body-wrapper">
  <div id="sidebar">
    <div id="sidebar-wrapper">
      <h1 id="sidebar-title"><a href="javascript:void(0);">开发专用后台</a></h1>
      <a href="${base}/index" style="margin-left: 15%;"><img id="log" title="西客商城开发专用" /></a>
    </div>
  </div>
  <div id="main-content">
  		<h2>密码过于简单,请重置密码</h2>
    	<div class="notification attention png_bg"> 
	      <div> 新的密码规则为 必须包含 字母数字 以及特殊符号 特殊符号包含&nbsp;&nbsp;~!@#$%^&*()_+-=&nbsp;&nbsp;10-20位</div>
	    </div>
    	<div class="errorMessage notification error png_bg" style="display:none"> 
	      	<div></div>
	    </div>
	    <div class="successMessage notification success png_bg" style="display:none"> 
	      <div>修改密码成功！ </div>
	    </div>
    	<form id="updatePasswordForm" method="post">
	        <p>
	          <label>原始密码</label>
	          <input type="hidden" id="id" name="id" value="${user.id!}"/>
	          <input type="password" name="password" id="password" class="text-input small-input">
	        </p>
	         <p>
	          <label>新密码</label>
	          <input type="password" id="password1" name="password1" class="text-input small-input">
	        </p>
	        <p>
	          <label>确认信密码</label>
	          <input type="password" id="password2" name="password2" class="text-input small-input">
	        </p>
	        <p>
	          <input type="submit" value="保    存" class="button addSysMenuBtn">
	        </p>
	        <div class="clear"></div>
	      </form>
    	
  </div>
</div>
</body>
</html>
<script>
$(function(){
	$("#updatePasswordForm").validate({
			rules: {
				 password: {required:true},
				 password1: {required:true,nPassword:true},
	             password2:{required:true,equalTo:"#password1"}
			},
			messages: {
	                password: {required:"请输入原密码"},
	                password1: {required:"请输入新密码"},
	                password2: {required:"请再次输入密码",
	                		   equalTo:"两次密码不一致" 				   
	                		}
	        },
	        submitHandler: function (form) {    
	        	 $.ajax({
		               url: "/permission/user/updatePassword.htm",
		               data: $("#updatePasswordForm").serialize(),
		               type:'POST',
		               dataType:'json',
		               success : function(result) {
						   if (result.state) {
								$(".errorMessage").slideUp(400,function(){
							   		$(".successMessage").slideDown(400);
							   	});
						   }else{
							   	$(".errorMessage").slideDown(400,function(){
							   		$(".errorMessage div").html(result.message);
							   	});
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
   
 