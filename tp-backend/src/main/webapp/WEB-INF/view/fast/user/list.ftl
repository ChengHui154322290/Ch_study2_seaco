<#include "/common/common.ftl"/> 
<@backend title="速递人员管理" 
 js=[ '/statics/backend/js/layer/layer.min.js',
 	  '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.js',
	  '/statics/backend/js/jqgrid/js/jquery.jqGrid.src.js',
	  '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	'/statics/backend/js/fast/user.js'] 
	css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'
] >
<form method="post" action="${domain}/fast/user/list.htm" id="listform">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">速递人员管理</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>用户类型</td>
						<td>
							<select id="userType" name="userType" class="select">
								<option value="">请选择</option> 
	            	           	<#list usertypelist as userType>
	        	               		<option value="${userType.code}" <#if userType.code==fastUserInfo.userType>selected</#if> >${userType.cnName}</option>
	    	                   	</#list>
		                    </select>
						</td>
						<td>店铺类型</td>
						<td>
							<select id="shopType" name="shopType" class="select">
								<option value="">请选择</option> 
	            	           	<#list shoptypelist as shopType>
	        	               		<option value="${shopType.code}" <#if shopType.code==fastUserInfo.shopType>selected</#if> >${shopType.cnName}</option>
	    	                   	</#list>
		                    </select>
						</td>
						<td>关联店铺</td>
						<td>
							<select id="warehouseId" name="warehouseId" class="select">
								<option value="">请选择</option> 
	            	           	<#list warehouselist as warehouse>
	        	               		<option value="${warehouse.id}" <#if warehouse.name==fastUserInfo.warehouseId>selected</#if> >${warehouse.name}</option>
	    	                   	</#list>
    	                    </select>
						</td>
						<td>是否可用</td>
						<td>
							<select id="enabled" name="enabled" class="select">
								<option value="">请选择</option> 
	            	           	<option value="1">可用</option>
	            	           	<option value="0">禁用</option>
		                    </select>
						</td>
						<td>
							<input type="button" value="查询" class="btn btn82 btn_search searchbtn" name="button">
							<input type="button" value="新增" class="btn btn82 btn_save2 savebtn" name="button" fastuserid="">
						</td>
					</tr>
				</table>
			</div>
			
		</div>
	</div>
</div>
</form>
<table id="userlist" class="scroll"></table>
<div id="gridpager" class="scroll"></table>
</@backend>
