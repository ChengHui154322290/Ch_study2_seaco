<#include "/common/common.ftl"/> 
<@backend title="促销人员管理" 
 js=[ 
    '/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/jqgrid/js/jquery.jqGrid.src.js',
	'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
    '/statics/backend/js/layer/layer.min.js',
 	'/statics/js/validation/jquery.validate.min.js',
    '/statics/js/validation/jquery.validate.method.min.js',
	'/statics/backend/js/fast/user.js'] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >
<form method="post" action="${domain}/fast/user/save.htm" id="saveuserform">
<input type="hidden" name="fastUserId" value="${fastUserInfo.fastUserId}"/>
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>姓名</td>
						<td><input id="userName" type="text" name="userName" value="${fastUserInfo.userName}" class="input-text lh25" size="20"></td>
					</tr>
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
					</tr>
					<tr>
						<td>店铺类型</td>
						<td>
						<#if fastUserInfo.fastUserId!=null>
							<input type="hidden" name="shopType" value="${fastUserInfo.shopType}"/>
							${fastUserInfo.shopTypeCn}
						<#else>
							<select id="shopType" name="shopType" class="select">
								<option value="">请选择</option> 
	            	           	<#list shoptypelist as shopType>
	        	               		<option value="${shopType.code}" <#if shopType.code==fastUserInfo.shopType>selected</#if> >${shopType.cnName}</option>
	    	                   	</#list>
    	                    </select>
    	                </#if>
						</td>
					</tr>
					<tr>
						<td>店铺名称</td>
						<td><#if fastUserInfo.fastUserId!=null>
							<input type="hidden" name="warehouseId" value="${fastUserInfo.warehouseId}"/>
							<#list warehouselist as warehouse>
								<#if warehouse.id==fastUserInfo.warehouseId>${warehouse.name}</#if>
							</#list>
						<#else>
							<select id="warehouseId" name="warehouseId" class="select">
								<option value="">请选择</option> 
	            	           	<#list warehouselist as warehouse>
	        	               		<option value="${warehouse.id}" <#if warehouse.name==fastUserInfo.warehouseId>selected</#if> >${warehouse.name}</option>
	    	                   	</#list>
    	                    </select>
						</#if>
						</td>
					</tr>
					<tr>
						<td>手机号</td>
						<td><input id="mobile" type="text" name="mobile" value="${fastUserInfo.mobile}" class="input-text lh25" size="20"></td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: center;">
					<input type="button" value="编辑" class="btn btn82 btn_add editbtn" name="button" id="savebtn">
				</div>
			</div>
		</div>
	</div>
</div>
</form>
</@backend>
