<table class="form_table" border="0" cellpadding="0" cellspacing="0">
	<tr>
         <td>PRD:</td>
         <td><input type="text" name="prd" class="input-text lh25" size="20" data-v="${prd}"/></td>
         
         <td width="10"></td>
         
         <td>条形码:</td>
         <td><input type="text" name="barcode" class="input-text lh25" size="20" data-v="${barcode}"/></td>
         
         <td width="10"></td>
         
         <td>商品名称:</td>
         <td><input type="text" name="itemName" class="input-text lh25" size="20" data-v="${itemName}"/></td>
         
         <td width="10"></td>
         
         <td>评论日期:</td>
         <td colspan="3">
         	<input type="text" name="createBeginTime" id="createBeginTime" data-v="${createBeginTime}" value="<#if createBeginTime??>${createBeginTime?string("yyyy-MM-dd")}</#if>" class="input-text lh25" size="20">
            <span>到</span>
            <input type="text" name="createEndTime" id="createEndTime" data-v="${createEndTime}" value="<#if createEndTime??>${createEndTime?string("yyyy-MM-dd")}</#if>" class="input-text lh25" size="20">
         </td>
	</tr>
	<tr>
         <td>会员账号:</td>
         <td><input type="text" name="username" placeholder="手机号" class="input-text lh25" size="20" data-v="${username}"/></td>
	</tr>
</table>

          