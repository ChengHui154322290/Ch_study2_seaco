<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jquery.tools.js',    
	'/statics/backend/js/form.js',
	'/statics/backend/js/wms/waybill.js']
	css=[] >

  <form action="waybillOper.htm" method="post" class="jqtransform"   id='waybillOperationForm'  > 
  	<div>
     <table class="form_table" border="0" cellpadding="0" cellspacing="0">
      <tr>
     	 <td class="td_right requiredField">*运单号:
		</td>
		 <td><input type="text" class="input-text lh30" size="25"	name="waybillNo"  id="waybillNo" maxlength=60 value='${waybillNo}'/></td>
      </tr>  
      <tr>
      	<td class="td_right requiredField">订单号:</td>
		<td ><input type="text" class="input-text lh30" size="25" name="orderCode"  id="orderCode" maxlength=60 value='${orderCode}'/></td>
      </tr>
     </table>
   </div>
   <div>
   		<input type="button" name="button" value="绑定" otype="1" class="btn btn82 btn_search waybillNoOperation" />
      	<input type="button" name="button" value="解绑" otype="2" class="btn btn82 btn_search waybillNoOperation" />
      	<input type="button" name="button" value="作废" otype="3" class="btn btn82 btn_search waybillNoOperation" />
   </div>
</form>
</@backend>