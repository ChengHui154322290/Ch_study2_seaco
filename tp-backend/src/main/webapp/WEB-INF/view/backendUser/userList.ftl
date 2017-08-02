<#include "/common/common.ftl"/> 
	<input type="hidden" name="pageNo" id="pageNo" value="${page.pageNo}"/>
	<input type="hidden" name="pageSize" id="pageSize" value="${page.pageSize}"/>
	
	
	
             
	<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="CRZ0">
	    <thead>
	    	<tr>
		       <td colspan="11">
		       	用户名：<input type="text" size="40" class="input-text lh30" style="margin-right:40px;"  name="userName">
		       	登录名：<input type="text" size="40" class="input-text lh30" style="margin-right:40px;" name="loginName">
		       	角色：<select class="select" style="min-width:150px;margin-right:40px;" name="roleId"> 
				        <option value="">请选择</option> 
				        <#list roList as role>
				        	<option value="${role.id}">${role.name}</option> 
				        </#list>
				     </select>
				 部门：<select class="select" name="departmentId" style="min-width:150px;margin-right:40px;"> 
                    <option value="">请选择</option> 
                    <#list deList as department>
                    	<option value="${department.id}">${department.name}</option> 
                    </#list>
                 </select>
                 <input type="button" class="btn btn82 btn_search" value="筛选"/>
		       	</td>
	        </tr>
	    	<tr>
		       <th width="8%">用户名</th>
		       <th width="8%">登录名</th>
		       <th width="9%">手机号</th>
		       <th width="9%">邮箱</th>
		       <th width="8%">部门</th>
		       <th width="8%">角色</th>
		       <th width="10%">创建时间</th>
		       <th width="10%">登录时间</th>
		       <th width="10%">登录IP</th>
		       <th width="5%">状态</th>
		       <th width="15%">操作</th>
	        </tr>
	    </thead>
        <tbody>
        <#if page??&&page.rows??&&page.rows?size gt 0>
        	<#list page.rows as u>
        		 <tr class="tr" style="background-color: rgb(255, 255, 255);">
			       <td align="center">${u.userName!}</td>
			       <td align="center">${u.loginName!}</td>
			       <td align="center">${u.mobile!}</td>
			       <td align="center">${u.email!}</td>
			       <td align="center">${u.departmentName!}</td>
			       <td align="center">${u.roleName!}</td>
			       <td align="center">${(u.createTime?string("yyyy-MM-dd HH:mm:ss"))!'无数据 '}</td>
			       <td align="center">${(u.lastLoginTime?string("yyyy-MM-dd HH:mm:ss"))!'未登陆 '}</td>
			       <td align="center">${(u.lastLoginIp)!'未登陆 '}</td>
			       <td align="center">${(u.status==1)?string("正常","冻结")}</td>
			       <td align="center">
			       		<input data-id="${u.id}"  type="button" value="修改" class="editUser_btn btn btn82 btn_config" name="button">
			       		<#if u.status==1>
				       		<input data-id="${u.id}"  type="button" data-state="0" value="冻结" class="clockUser_btn btn btn82 btn_recycle" name="button">
			       		<#else>
			       			<input data-id="${u.id}"  type="button" data-state="1" value="恢复" class="clockUser_btn btn btn82 btn_nochecked" name="button">
			       		</#if>
			       </td>
			     </tr>
        	</#list>
        	
        	<#else>
        		 <tr class="tr" style="background-color: rgb(255, 255, 255);"><td colspan="11" align="center">无数据</td></tr>
        	
        	</#if>
  		</tbody>
 </table>
 	<#if page??&&page.records gt 10>
 <div class="page mt10" style="float: right;">
    <div class="pagination">
      <ul>
      	 <#if page.page  == 1>
	           	<li class="disabled first-child"><span>首页</span></li>
	          	<li class="disabled"><span>上一页</span></li>
          	<#else> 
          	  <li class="first-child"><a class="toPage" data-page="1" href="javascript:void(0);">首页</a></li>
          	  <li><a class="toPage" data-page="${page.page-1}"  href="javascript:void(0);">上一页</a></li>
          </#if> 
          <#list 1..page.total as index>
          
          		<#if index == page.page>
					<li class="active"><span>${index}</span></li>
				<#else> 
					<li><a class="toPage" data-page="${index}"  href="javascript:void(0);">${index}</a></li>
				</#if> 
			</#list>
			
			 <#if page.page  == page.total>
          		 <li class="disabled"><span>下一页</span></li>
          		 <li class="disabled"><span>末页</span></li>
          	<#else> 
          		 <li><a class="toPage" data-page="${page.page+1}" href="javascript:void(0);">下一页</a></li>
          		<li class="last-child"><a class="toPage" data-page="${page.total}" href="javascript:void(0);">末页</a></li>
          </#if> 
      </ul>
    </div>
  </div>
</#if>

<script>
$(function(){
	$(".btn_search").click(function(){
		$("#pageNo").val(1);
		loadUserList();
	});

	$(".toPage").on("click",function(){
		var pageNo = $(this).attr("data-page");
		$("#pageNo").val(pageNo);
		loadUserList();
	});
	
	$(".clockUser_btn").click(function(){
		var status = $(this).attr("data-state");
		var userId = $(this).attr("data-id");
		$.post("${base}/permission/user/clockUser",{userId:userId,status:status},function(result){
			if(result.success){
				loadUserList();
			}else{
				error(result.msg.message);
			}
			
		},"json");
		
	
	});
});

</script>
