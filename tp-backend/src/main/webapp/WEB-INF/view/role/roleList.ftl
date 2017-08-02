<#include "/common/common.ftl"/> 
	<input type="hidden" id="pageNo" value="${page.page}"/>
	<input type="hidden" id="pageSize" value="${page.size}"/>
	<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="CRZ0">
	    <thead>
	    	<tr>
		       <th>角色名</th>
		       <th>角色描述</th>
		       <th>创建人</th>
		       <th>创建时间</th>
		       <th>修改人</th>
		       <th>修改时间</th>
		       <th>操作</th>
	        </tr>
	    </thead>
        <tbody>
        	<#list page.rows as role>
        		 <tr class="tr" style="background-color: rgb(255, 255, 255);">
			       <td align="center">${role.name}</td>
			       <td align="center">${role.roleDesc}</td>
			       <td align="center">${role.createUser}</td>
			       <td align="center">${role.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
			       <td align="center">${role.updateUser}</td>
			       <td align="center">${role.updateTime?string("yyyy-MM-dd HH:mm:ss")}</td>
			       <td align="center"><input data-id="${role.id}"  type="button" value="修改" class="editRole_btn btn btn82 btn_config" name="button"></td>
			     </tr>
        	</#list>
        	
  		</tbody>
 </table>
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



<script>
$(function(){
	$(".toPage").bind("click",function(){
		var pageNo = $(this).attr("data-page");
		$("#pageNo").val(pageNo);
		loadRoleList();
	});
});

</script>
