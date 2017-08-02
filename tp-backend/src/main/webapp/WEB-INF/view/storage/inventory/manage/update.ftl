<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
    '/statics/backend/js/jquery.form.2.2.7.js',
    '/statics/backend/js/layer/layer.min.js']   
	css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
	] >
    <div "box" style="padding-top:10px;">
     <div class="box_border">
	   <div class="box_center pt10 pb10">	 
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
		        <input type="hidden" name="inventoryId" value="${inventory.id}"   id="inventoryId" value=""/> 
		        <tr>
					<td valign="middle" width="100">供应商:</td>
					<td valign="middle" align="left">
						${inventory.spName}
                     </td>
				</tr>
				<tr>
					<td valign="middle" width="100">仓库:</td>
					<td valign="middle" align="left">
						${inventory.warehouseName}
					</td>
				</tr>
				<tr>
				  <td valign="middle" width="100">商品标题:</td>
				  <td valign="middle" align="left">
				  	${inventory.mainTitle}
				  	</td>
				</tr>
				<tr>
				  <td valign="middle" width="100">sku:</td>
				  <td valign="middle" align="left">
				  	${inventory.sku}
				  </td>
				</tr>
				<tr>
				  <td valign="middle" width="100">条形码:</td>
				  <td valign="middle" align="left">
				  	${inventory.barcode}
				  </td>
				</tr>
				<tr>
				  <td valign="middle" width="100">商品总库存:</td>
				  <td valign="middle" align="left">
					  	<input type="text" value="${inventory.realInventory}" name="inventory"  id="inventory" class="input-text lh25" size="20"  maxlength="10" />
				  </td>
				</tr>
				<tr>
				  <td valign="middle" width="100">商品预留库存:</td>
				  <td valign="middle" align="left">
					  	<input type="text" value="${inventory.reserveInventory}" name="reserveInventory"  id="reserveInventory" class="input-text lh25" size="20"  maxlength="10" /> 
				  </td>
				</tr>
				<tr>
				  <td valign="middle" width="100">占用库存:</td>
				  <td valign="middle" align="left">
					  	<input type="text" value="${inventory.occupy}" name="occupy"  id="occupy" class="input-text lh25" size="20"  maxlength="10" disabled/>
					  	<span>说明：商品活动期间占用的库存</span>
				  </td>
				</tr>
				<tr>
				  <td valign="middle" width="100">商品可售库存:</td>
				  <td valign="middle" align="left">
					  	<input type="text" value="${inventory.inventory!}" class="input-text lh25" size="20"  maxlength="10" disabled/>
					  	<span>说明：商品可售库存数=总库存-预留库存-占用库存</span>
				  </td>
				</tr>
				<tr>
				  <td valign="middle" width="100">商品预警库存:</td>
				  <td valign="middle" align="left">
					  	<input type="text" name="warnInventory" value="${inventory.warnInventory}"  id="warnInventory" class="input-text lh25" size="20"  maxlength="10" />
					  	
				  </td>
				</tr>
             </table>
             <div style="text-align:center;">   
			    <input class="btn btn82 btn_nochecked btn_add " type="button" value="保存" id="save"/ >
             </div>    
		 </div>         
	   </div>	
    </div>
    <script>
    	$(function(){
			$("#save").bind("click",function(){
				save();
			})    		
    	})
    	function save(){
    		var inventory = $("#inventory").val();
			var reserveInventory = $("#reserveInventory").val();
			var warnInventory = $("#warnInventory").val();
			var inventoryId = $("#inventoryId").val();
			var occupy = $("#occupy").val();
			
			if(inventoryId==null||inventoryId==""){
				layer.alert("数据有误",8)
				return ;
			}
			if(inventory==null||inventory==""){
				layer.alert("总库存不能为空",8)
				return ;
			}
			if(reserveInventory==null||reserveInventory==""){
				layer.alert("预留库存不能为空",8)
				return ;
			}
			if(warnInventory==null||warnInventory==""){
				layer.alert("预警库存不能为空",8)
				return ;
			}
			if((reserveInventory*1+occupy*1)>inventory*1){
				layer.alert("总库存不能小于（预留库存+冻结库存）",8)
				return ;
			}
			var params = {
				inventory:inventory,
				occupy:occupy,
				reserveInventory:reserveInventory,
				warnInventory:warnInventory,
				id:inventoryId
			}
			 $.ajax({
             	type: "POST",
             	url: domain+ "/storage/inventory/manage/save.htm",
             	data: params,
             	dataType: "json",
             	success: function(data){
             		//console.log(data)
             		if(data.flag=="success"){
         				layer.alert(data.msg,1,"信息",function(){
	             			parent.window.location.reload();
	             		})
         			}else{
         				layer.alert(data.msg,8)
         			}
                }  
         	 });
			
			
    	}
    </script>
</@backend>