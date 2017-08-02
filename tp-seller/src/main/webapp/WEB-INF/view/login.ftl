<!--isLoginPage-->
<!doctype html>
<html>
    
    <head>
        <meta charset="utf-8">
        <meta content="IE=edge, chrome=1" http-equiv="X-UA-Compatible" />
        <meta http-equiv="X-UA-Compatible" content="IE=Edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>商家登录 </title>
        <!--[if lt IE 9]>
            <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js">
            </script>
            <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js">
            </script>
        <![endif]-->
        <#include "/commons/common.ftl" />
        <#include "/commons/js_and_css.ftl" />
        <link rel="stylesheet" href="${staticPath}/static/css/login.css">
        <script type="text/javascript" charset="utf-8" src="${staticPath}/static/scripts/web/login.js"> </script>
        <script type="text/javascript" language=JavaScript charset="UTF-8">
		       document.onkeydown=function(event){
		            var e = event || window.event || arguments.callee.caller.arguments[0];
		            if(e && e.keyCode==13){ // enter 键
		                 jQuery("#loginBtn").click();
		            }
		       }; 
		</script>
		
    </head>
    
    <body>
	    <div id="login_top">
			<div id="welcome">				
				西客商城商家后台管理系统		
			</div>
		</div>
    	<div id="login_center">
	    	<div id="login_area">
	    	  <div id="login_form">     
			     <form id="loginForm" role="form" action="/loginSubmit" method="post">
			     	<div id="login_tip" style="position: relative;margin-left:10px;">
						用户登录
					</div>
					<div style="position: relative;"><input type="text" placeholder="登录名" name="userName" class="username"></div>
			     	<div style="position: relative;">
			     		<input type="password" placeholder="密码" name="password" class="pwd">
			     		<#if pwdError?exists>
                            <font color="red" size="2" id="pwdMsg">密码错误</font>
                        <#elseif passwordIsNull?exists>
                            <font color="red" size="2" id="pwdMsg">密码不能为空</font>
                        <#elseif useStatusError?exists>
                            <font color="red" size="2" id="pwdMsg">账户已失效</font>
                        <#elseif auditStatusError?exists>
                            <font color="red" size="2" id="pwdMsg">账户未审核通过</font>
                        <#else>
                            <font style="display:none;" color="red" size="2" id="pwdMsg">密码错误</font>
                        </#if>
			     	</div>
			     	<div style="position: relative;" id="btn_area">
			     		<input type="text" class="form-control checkCode" name="code" id="checkCodeTxt" placeholder="请输入验证码">
			     		<#if hasError?exists>
                        	<font color="red" size="2" id="errorMsg">验证码错误</font>
                        <#else>
                        	<font style="display:none;" color="red" size="2" id="errorMsg">验证码错误</font>
                        </#if>
						<img src="/securityCode" id="securiteCode" alt="image" height="34">
						<a href="javascript:void(0)" id="changeSecurite">换一张</a>
					</div>
					<div><input type="submit" class="sub_btn" id="sub_btn" value=""></div>                   
			     </form>
			   </div>                      
	        </div>
	      </div>
        <script type="text/javascript">
            $('.row').css({
                'position': 'relative',
                'top': $(window).height() / 2,
                'margin-top': -$('.row').height() / 2
            });
        </script>
    </body>

</html>