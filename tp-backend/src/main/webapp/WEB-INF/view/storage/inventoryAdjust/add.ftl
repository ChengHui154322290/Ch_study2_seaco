<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
    '/statics/backend/js/jquery.form.2.2.7.js',
    '/statics/backend/js/layer/layer.min.js',
    '/statics/storage/js/inventory_adjust_list.js']   
	css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
	] >
    <div "box">
     <div class="box_border">
	   <div class="box_center pt10 pb10">	 
			     <form action="${domain}/storage/inventoryAdjust/add" class="jqtransform"  method="post"  id='adjustLog' > 
			       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
				        <input type="hidden" name="action"   id="action" value=""/> 
				        <tr>
							<td valign="middle" width="80">SKU:</td>
							<td valign="middle" align="left">
								<input type="text" name="sku"  id='sku' class="input-text lh25" size="20">
								<input type="button" id="selectWarehouseBtn" class="btn btn82 btn_search" value="仓库" />
		                     </td>
						</tr>
						<tr>
							<td valign="middle" width="80">仓库:</td>
							<td valign="middle" align="left">
								<div id="warehouseList">请选择仓库</div>
							</td>
						</tr>
						<tr>
						  <td valign="middle" width="80">盘点数量:</td>
						  <td valign="middle" align="left">
						  		<input type="text" name="quantity"  id="quantity" class="input-text lh25" size="20"  maxlength="10" />
						  	</td>
						</tr>
						<tr>
						  <td valign="middle" width="80">备注:</td>
						  <td valign="middle" align="left">
							  	<textarea  maxlength="50" name="remark" id="remark" value=""></textarea> 
						  </td>
						</tr>
		             </table>
		             <div style="text-align:center;">   
					    <input class="btn btn82 btn_nochecked btn_add " type="button" value="盘盈" id="adjustIncrease"/ >
		                <input class="btn btn82 btn_nochecked btn_add " type="button" value="盘亏" id="adjustDecrease"/ >  
		             </div>    
		       </form>   
		 </div>         
	   </div>	
    </div>
</@backend>