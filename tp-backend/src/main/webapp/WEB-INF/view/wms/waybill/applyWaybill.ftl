<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jquery.tools.js',    
	'/statics/backend/js/form.js',
	'/statics/select2/js/select2.js',
	'/statics/select2/js/select2Util.js',
	'/statics/select2/js/select2_locale_zh-CN.js',
	'/statics/backend/js/item/item-select2.js',
	'/statics/backend/js/wms/waybill.js']
	css=['/statics/select2/css/select2.css'] >

  <form action="applyWaybill.htm" method="post" class="jqtransform"   id='applyWaybillForm'  > 
     <table class="form_table" border="0" cellpadding="0" cellspacing="0">
      <tr>
     	 <td class="td_right requiredField">*物流公司:
		</td>
		 <td>
			<select name="logisticsCode"  id="logisticsCode" class="select2" style="width:150px; margin-left: 1px">
						<option value="" >请选择</option>
						<#list logisticsList as t>
							<option value="${t.code}">${t.name}</option>  
						</#list>
			 </select> 
		</td>
      </tr>  
      <tr>
      	<td class="td_right requiredField">*申请数量:</td>
		<td ><input type="text" class="input-text lh30" size="25"	name="amount"  id="amount" maxlength=60 onMouseOver="this.title=this.value" /></td>
      </tr>
      <tr>
      	<td class="td_right"> <a href="#"><input type="button" name="button" value="申请" id="applyWaybill" class="btn btn82 btn_search"></a></td>
      </tr>
     </table>
	  </form>
</@backend>