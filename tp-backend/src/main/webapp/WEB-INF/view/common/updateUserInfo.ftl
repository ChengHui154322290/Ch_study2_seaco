<#include "/common/common.ftl"/> 
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
             <form id="updateUserInfoForm" class="editRole" autocomplete="off">
               <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                 <tbody>
                 <tr>
	                  <td class="td_right">登录名：</td>
	                  <td class=""> 
	                   <input type="hidden" id="id" name="id" value="${user.userDetail.id!}"/>
	                   <@shiro.principal property="loginName" />
	                  </td>
                </tr>
                 <tr>
	                  <td class="td_right">用户名：</td>
	                  <td class=""> 
	                   <@shiro.principal property="userName" />
	                  </td>
                </tr>
                 <tr>
                  <td class="td_right">手机号：</td>
                  <td class="">
                    <input class="input-text lh30" style="width:200px" type="text" placeholder="手机号码" id="mobile" name="mobile" value='${user.userDetail.mobile!}'/>
                  </td>
                 </tr>
                 <tr>
                  <td class="td_right">固话：</td>
                  <td class="">
                    <input class="input-text lh30" style="width:200px" type="text" placeholder="固定电话" id="fixedPhone" name="fixedPhone" value='${user.userDetail.fixedPhone!}'/>
                  </td>
                 </tr>
                 <tr>
                  <td class="td_right">邮箱：</td>
                  <td class="">
                    <input class="input-text lh30" style="width:200px" type="text" placeholder="邮箱地址" id="email" name="email" value='${user.userDetail.email!}'/>
                  </td>
                 </tr>
                 <tr>
                  <td class="td_right">地址：</td>
                  <td class="">
                    <input class="input-text lh30" style="width:300px" type="text" placeholder="地址" id="address" name="address" value='${user.userDetail.address!}'/>
                  </td>
                 </tr>
                 <tr>
                  <td class="td_right">邮编：</td>
                  <td class="">
                    <input class="input-text lh30" style="width:200px" type="text" placeholder="邮编" id="zipCode" name="zipCode" value='${user.userDetail.zipCode!}'/>
                  </td>
                 </tr>
                 <tr>
                 <tr>
                  <td class="td_right">传真：</td>
                  <td class="">
                    <input class="input-text lh30" style="width:200px" type="text" placeholder="传真号" id="fax" name="fax" value='${user.userDetail.fax!}'/>
                  </td>
                 </tr>
                 <tr>
                  <td class="td_right">QQ：</td>
                  <td class="">
                    <input class="input-text lh30" style="width:200px" type="text" placeholder="QQ号" id="qq" name="qq" value='${user.userDetail.qq!}'/>
                  </td>
                 </tr>
                 <tr>
                  <td class="td_right">性别：</td>
                  <td class="">
                  	<select name="sex" id="sex" value="${user.userDetail.sex!}">
                  		<option value="0">女</option>
                  		<option value="1">男</option>
                  	</select>
                  </td>
                 </tr>
                 <tr>
                  <td class="td_right" colspan="2" align="center">
                  	<input type="button" value="提交" class="btn btn82 btn_save2 btn_updateUserInfo_submit" name="button">
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
	$("#sex").val('${user.userDetail.sex!}');

	$(".btn_updateUserInfo_submit").click(function(){
		$("#updateUserInfoForm").submit();
	});

	$("#updateUserInfoForm").validate({
			rules: {
				 email:{email: true},
				 mobile:{mobile: true},
				 fixedPhone:{simplePhone: true},
				 zipCode:{zipCode:true},
				 fax:{fax: true},
				 qq:{fax: true}
			},
			messages: {
	        },
	        submitHandler: function (form) {    
	         $.ajax({
		               url: domain+"/permission/user/updateUserInfo.htm",
		               data: $("#updateUserInfoForm").serialize(),
		               type:'POST',
		               dataType:'json',
		               success : function(result) {
						   if (result.state) {
								$('.tabclose').click();
						   }else{
						   		$(".updateUserInfo-errorMessage").html(result.message); 
						   		$(".updateUserInfo-errorMessage").show(); 
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
