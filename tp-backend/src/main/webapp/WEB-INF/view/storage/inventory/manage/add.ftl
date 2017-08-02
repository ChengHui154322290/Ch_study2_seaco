<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
    '/statics/backend/js/jquery.form.2.2.7.js',
    '/statics/backend/js/layer/layer.min.js']   
	css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
	] >
    <div "box" style="padding-top: 10px;">
     <div class="box_border">
	   <div class="box_center pt10 pb10">	 
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
		        <tr>
					<td valign="middle" width="100">供应商:</td>
					<td valign="middle" align="left">
						<select id="spId" class="select" style="width:320px;">
							<option value=''>--请选择--</option>
							<#list supplierList as s>
								<option value="${s.id}" <#if s.id=query.spId> selected </#if> >${s.name}</option>
							</#list>
						</select>
                     </td>
				</tr>
				<tr>
					<td valign="middle" width="100">仓库:</td>
					<td valign="middle" align="left">
						<select id="warehouseId" class="select" style="width:320px;">
							<option value=''>--请选择--</option>
						</select>
					</td>
				</tr>
				<tr>
				  <td valign="middle" width="100">商品标题:</td>
				  <td valign="middle" align="left">
				  		<input type="text" data="1" name="spuName" value="" id="spuName" style="width:320px;" class="input-text lh30 promotername ui-autocomplete-input" size="40" autocomplete="off">
						<input type="hidden" name="sku" id="sku" value="">
						<input type="hidden" name="spuText" id="spuText" value="">
						
				  </td>
				</tr>
				<tr>
				  <td valign="middle" width="100">商品初始库存:</td>
				  <td valign="middle" align="left">
					  	<input type="text" value="${inventory.realInventory}" name="inventory"  id="initInventory" class="input-text lh25" size="20"  maxlength="10" />
				  </td>
				</tr>
				<tr>
				  <td valign="middle" width="100">商品预留库存:</td>
				  <td valign="middle" align="left">
					  	<input type="text" value="${inventory.reserveInventory}" name="reserveInventory"  id="reserveInventory" class="input-text lh25" size="20"  maxlength="10" /> 
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
    		// 供应商仓库联动
    		$("#spId").bind("change",function(){
    			var spId = $(this).val();
    			if(spId==null||spId==""){
    				return;
    			}
				$.ajax({ 
					url: domain+"/storage/inventory/manage/getWarehouseBySupplierId.htm",
					type:"post",
					data:{spId:spId},
					success: function(data){
						$("#warehouseId").empty();
						$("#warehouseId").append("<option value=''>--请选择--</option>")
						if(data!=null){
							$(data).each(function(){
								$("#warehouseId").append("<option value="+this.id+"_"+this.districtId+" >"+this.name+"</option>")
							})
						}
			      	}
		      	});
			});
			// 保存
			$("#save").bind("click",function(){
				save();
			})
			// 模糊查询sku
			$( ".promotername" ).autocomplete({
				source: function (request, response) {
					var spId = $("#spId").val();
					if(!spId||spId==""){
						layer.alert("请选择供应商",8)
						return ;
					}
					
		            var term = request.term;
		            request.spuName = term;
		            var param = {
		            	spuName:term,
		            	spId:spId
		            }
		            $.post(domain+'/storage/inventory/manage/querySkuDetail.htm', param, response,'json');
		        },
				max:10,
				minLength: 2,
				select: function( event, ui ) {
					 $("input[name=sku]").val(ui.item.sku);
					 $("input[name=spuText]").val(ui.item.spuName);
					 $("#spuName").val(ui.item.spuName);
					 return false;
				}
			}).on('blur',function(){
				if($(this).val()=='' || $(this).val()==null){
					$(":hidden[name='sku']").val('');
				}
			});
			$(".promotername").data("autocomplete")._renderItem = function (ul, item) {
			    return $("<li></li>")
			        .data("item.autocomplete", item)
			        .append("<a>" + item.spuName + "</a>")
			        .appendTo(ul);
			};
			
			  		
    	})
    	function save(){

    		var initInventory = $("#initInventory").val();
			var reserveInventory = $("#reserveInventory").val();
			var warnInventory = $("#warnInventory").val();
			var spId = $("#spId").val();
			var spName = $("#spId").find("option:selected").text();
			var warehouseId = $("#warehouseId").val();
			var warehouseName = $("#warehouseId").find("option:selected").text();
			var sku = $("#sku").val();
			var spuName = $("#spuText").val();
			var districtId = "";
			
			if(spId==null||spId==""){
				layer.alert("请选择供应商",8)
				return ;
			}
			if(warehouseId==null||warehouseId==""){
				layer.alert("请选择仓库",8)
				return ;
			}else{
				var arr = warehouseId.split("_");
				districtId = arr[1];
				warehouseId = arr[0];
			}
			if(sku==null||sku==""){
				layer.alert("请填写商品",8)
				return ;
			}
			if(initInventory==null||initInventory==""){
				layer.alert("初始库存不能为空",8)
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
			if((reserveInventory*1)>initInventory*1){
				layer.alert("总库存不能小于预留库存",8)
				return ;
			}
			
			var params = {
				inventory:initInventory,
				reserveInventory:reserveInventory,
				warnInventory:warnInventory,
				spId:spId,
				warehouseId:warehouseId,
				sku:sku,
				spuName:spuName,
				warehouseName:warehouseName,
				spName:spName,
				districtId:districtId
			}
			//console.log(params)
			//return;
			 $.ajax({
             	type: "POST",
             	url: domain+"/storage/inventory/manage/add.htm",
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