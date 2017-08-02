<#include "/common/common.ftl"/> 
 <script src="${domain}/statics/backend/js/jquery.min.js"></script>
<#-- 验证form表单 js-->
<script type="text/javascript" src="${validation}/jquery.validate.min.js" ></script>
<script type="text/javascript" src="${validation}/jquery.validate.method.min.js" ></script>

<#-- 提示框 js-->
<script type="text/javascript" src="${messenger}/js/messenger.min.js"></script>
<#-- 验证form表单 css -->
<link type="text/css" rel="stylesheet" href="${validation}/jquery.validate.min.css"  />

<#-- 提示框 样式-->
<link href="${messenger}/css/messenger.css" rel="stylesheet"  type="text/css"/>
<link href="${messenger}/css/messenger-theme-future.css" rel="stylesheet"  type="text/css"/>
<script type="text/javascript" src="${js}/common.js"></script>
<div class="mt10" id="forms">
        <div class="box">
        	
          <div class="box_border">
            <div class="box_center">
             <form id="updatePasswordForm" class="editRole" autocomplete="off">
               <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                 <tbody>
                 <tr>
                  <td class="td_right">原始密码：</td>
                  <td class="">
                  	<input type="hidden" id="id" name="id" value="${user.id!}"/>
                    <input class="input-text lh30" type="password" placeholder="原始密码" id="password" name="password"/>
                  </td>
                 </tr>
                 <tr>
                  <td class="td_right">新密码：</td>
                  <td class="">
                    <input class="input-text lh30" type="password" placeholder="新密码" id="password1" name="password1"/>
                  </td>
                 </tr>
                 <tr>
                  <td class="td_right">确认密码：</td>
                  <td class="">
                    <input class="input-text lh30" type="password" placeholder="确认密码" id="password2" name="password2"/>
                  </td>
                 </tr>
                 <tr>
                  <td class="td_right" colspan="2" align="center">
                  	<input type="button" value="提交" class="btn btn82 btn_save2 btn_updatePassword_submit" name="button">
                  </td>
                 </tr>
               </tbody>
              </table>
              </form>
            </div>
          </div>
        </div>
     </div>
<script>
$(function(){
	$(".btn_updatePassword_submit").click(function(){
		$("#updatePasswordForm").submit();
	});

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
		               url: domain+"/permission/user/updatePassword.htm",
		               data: $("#updatePasswordForm").serialize(),
		               type:'POST',
		               dataType:'json',
		               success : function(result) {
						   if (result.state) {
								$('.tabclose').click();
						   }else{
						   		$(".updatePass-errorMessage").html(result.message); 
						   		$(".updatePass-errorMessage").show(); 
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
