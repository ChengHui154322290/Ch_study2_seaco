<#include "/common/common.ftl"/> 
<div class="mt10" id="forms">
        <div class="box">
          <div class="box_border">
            <div class="box_center">
             <form id="saveUserForm" class="userInfo" autocomplete="off">
               <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                 <tbody>
                 <tr>
                  <td class="td_right">用户名：</td>
                  <td class=""> 
                   <input type="hidden" id="id" name="id" value="${userInfo.id}"/>
                    <input type="text" size="40" class="input-text lh30" id="userName" name="userName" value="${userInfo.userName}">
                  </td>
                </tr>
                 <tr>
                  <td class="td_right">登录名：</td>
                  <td class=""> 
                  	<#if userInfo.id != null>
						  <input type="text" size="40" readOnly = "true" class="input-text lh30" id="loginName" name="loginName" value="${userInfo.loginName}">            		
                  	<#else>
                  		  <input type="text" size="40" class="input-text lh30" id="loginName" name="loginName" value="${userInfo.loginName}">       
                  	</#if>
                  </td>
                </tr>
                 <tr>
                  <td class="td_right">手机：</td>
                  <td class=""> 
                  		  <input type="text" size="40" class="input-text lh30" id="mobile" name="mobile" value="${userInfo.mobile}">       
                  </td>
                </tr>
                 <tr>
                  <td class="td_right">邮箱：</td>
                  <td class=""> 
                  		  <input type="text" size="40" class="input-text lh30" id="email" name="email" value="${userInfo.email}">       
                  </td>
                </tr>
                 <tr>
                  <td class="td_right">密码：</td>
                  <td class=""> 
                    <input type="password" size="40" class="input-text lh30" id="password" name="password" value="">
                  </td>
                </tr>
                 <tr>
                  <td class="td_right">确认密码：</td>
                  <td class=""> 
                    <input type="password" size="40" class="input-text lh30" id="ckPass" name="ckPass" value="">
                  </td>
                </tr>
                 <tr>
                  <td class="td_right">角色：</td>
                  <td class="">
                     <select class="select" id="roleId" name="roleId"> 
                        <option value="">请选择</option> 
                        <#list roleList as role>
                        	<option value="${role.id}" <#if userInfo.roleId==role.id>selected</#if> >${role.name}</option> 
                        </#list>
                     </select>
                  </td>
                </tr>
                <tr>
                  <td class="td_right">部门：</td>
                  <td class=""> 
                  	<select class="select" id="departmentId" name="departmentId"> 
                        <option value="">请选择</option> 
                        <#list departmentList as department>
                        	<option value="${department.id}" <#if userInfo.departmentId==department.id>selected</#if> >${department.name}</option> 
                        </#list>
                     </select>
                  </td>
                </tr>
                <tr align="right">
                  <td colspan="2"> 
                  	 <label style="display:none" class="error"></label>
                  	 <input type="submit" value="保存" class="btn btn82 btn_save2 btn_submit" name="button">
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
     		$("#saveUserForm").validate({
	            rules: {
	                userName:{required: true},
	                loginName:{required: true,username:true},
	                mobile:{mobile:true},
	                email:{email:true},
	                roleId:{required: true},
	                departmentId:{required: true},
	                ckPass:{equalTo:"#password"}
	            },
	            messages: {
	               	userName:{required: "请输入用户名"},
	                loginName:{required: "请输入登录名"},
	                roleId:{required: "请选择角色"},
	                departmentId:{required: "请选择部门"},
	                ckPass:{equalTo:"两次密码不一致"}
	            },
	            submitHandler: function (form) {   
	            	$(".btn_submit").attr("disabled","disabled");
	            	$(".btn_submit").val("保存中...");
	                $.ajax({
	                    url: "${base}/permission/user/save",
	                    data: $("#saveUserForm").serialize(),
	                    type:'POST',
	                    dataType:'json',
	                    success : function(result) {
	                    	if (result.success) {
								$('.tabclose').click();
								loadUserList();
							} else {
								$(".btn_submit").removeAttr("disabled");
	            				$(".btn_submit").val("保存");
								$(".error").html(result.msg.message);
								$(".error").show();
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

