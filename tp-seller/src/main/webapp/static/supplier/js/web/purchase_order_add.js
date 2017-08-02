jQuery(function(){
	
	/**
	 * 订单保存按钮
	 */
	jQuery("#orderAddSave").click(function(){
		jQuery("font._errorMsg").remove();
		if(!validateInfo()){
			return false;
		}
		var form = jQuery("#order_form");
		$.ajax({
			url:form.attr('action'),
			data:form.serialize(),
			dataType:'json',
			type:'post',
			success:function(resultInfo){
				// by zhs 01161640 修改 window.location.href=domain+'/supplier/quotationList'
				if(resultInfo.success){
					alertMsg('保存成功');
					jQuery("#orderAddSave").attr("disabled",true);
					jQuery("#orderAddSub").attr("disabled",true);
					window.location.href=domain+'/supplier/purchaseorderList';
				}else{
					alertMsg(resultInfo.msg.message);
				}
			}
		});
	});
	
	/**
	 * 订单提交按钮
	 */
	jQuery("#orderAddSub").click(function(){
		jQuery("font._errorMsg").remove();
		if(!validateInfo()){
			return false;
		}
		var form = jQuery("#order_form");
		$.ajax({
			url:domain+"/supplier/purchaseOrderSub.htm",
			data:form.serialize(),
			dataType:'json',
			type:'post',
			success:function(resultInfo){
				// by zhs 01161640 修改 window.location.href=domain+'/supplier/quotationList'
				if(resultInfo.success){
					alertMsg('保存成功');
					jQuery("#orderAddSave").attr("disabled",true);
					jQuery("#orderAddSub").attr("disabled",true);
					window.location.href=domain+'/supplier/purchaseorderList';
				}else{
					alertMsg(resultInfo.msg.message);
				}
			}
		});
	});
	
	/**
	 * 审核
	 */
	jQuery("#quotationShowAudit").click(function(){
		var quoId = jQuery("#pruOrderId").val();
		ajaxRequest({
			method:'post',
			url:'/supplier/purchaseOrderExam/auditPage.htm',
			data:{"quoId":quoId},
			success:function(data){
				showPopDiv(5,data,{"title":"审核"});
			}
		})
	});
	
	/**
	 * 
	 */
	jQuery("#quotationShowCancel").click(function(){
		confirmBox('确定取消？',function(){
			jQuery("#examineStatusId").val("cancel");
			jQuery("#quotationShowCancel").attr("action","/supplier/purchaseOrderCancel.htm");
			jQuery("#order_form").submit();
			jQuery("#orderAddSave").attr("disabled",true);
			jQuery("#orderAddSub").attr("disabled",true);
		});
	});
	
	registerPageEvent();
	

	/**
	 * 采购订单取消
	 */
	/*jQuery("#quotationAddCancel").click(function(){
		closeCurrentTab();
	});*/
	
	
});

/**
 * 添加商品
 */
function addOrderItem() {
	var supplierId = jQuery("#all_page_add_supplier_id_hidden").val();
	if(!supplierId){
		alertMsg('请先填写供应商信息。');
		return;
	}
	var rateSel = jQuery("#rateSelId").val();
	ajaxRequest({
		method:'post',
		data:{"supplierId":supplierId,'rateSel':rateSel},
		url:'/supplier/purchaseorder/getItemInfoForm.htm',
		success:function(data) {
			showPopDiv(11,data,{"title":"添加商品"});
		}
	})
}

/**
 * 删除一行
 * 
 * @param index
 */
function deleteThisProductTr(index){
	jQuery("#addQuotaItemTr_"+index).remove();
	jQuery("#addQuotaItemTr2_"+index).remove();
	refreshIndex();
	caculatePageInfo();
}

/**
 * 计算页面部分信息
 */
function caculatePageInfo() {
	var countObjs = jQuery("input[name='count']");
	var subTotalObjs = jQuery("input[name='subtotal']");
	var totalCount = 0;
	var subTotal = 0;
	for(var i=0;i<countObjs.length;i++){
		var cVal = jQuery(countObjs[i]).val();
		try{
			totalCount = totalCount + parseInt(cVal);
		}catch(e){
		}
	}
	for(var i=0;i<subTotalObjs.length;i++){
		var cVal = jQuery(subTotalObjs[i]).val();
		try{
			subTotal = subTotal + parseFloat(cVal);
		}catch(e){
		}
	}
	jQuery("#itemCountSum").val(totalCount);
	jQuery("#itemAmountSum").val(subTotal.toFixed(2));
}

/**
 * 清空页面部分信息
 */
function cliearPageInfo(){
	jQuery("#sp_order_table_body_1").html("");
	jQuery("#pur_order_add_warehouse_id").val("");
	jQuery("#pur_order_add_warehouse_name").html("");
	jQuery("#pur_order_add_warehouse_id_hidden").html("");
}

/**
 * 注册页面事件
 */
function registerPageEvent(){
	
	/**
	 * 注册keyup弹出事件
	 */
	registeValidateEvent();
	
	//供应商弹出
	var paramsMap = {"supplierType":"Purchase"};
	registerSupplierPop(cliearPageInfo,paramsMap);
	
	//仓库确认按钮
	jQuery("#pur_order_add_warehouse_confirm").click(function(){
		var supplierId = jQuery("#all_page_add_supplier_id_hidden").val();
		if(!supplierId){
			alertMsg('请先填写供应商信息');
			return;
		}
		var spName = jQuery("#pur_order_add_warehouse_name").val();
		var spId = jQuery("#pur_order_add_warehouse_id").val();
		if(!spId && !spName){
			alertMsg('请填写仓库id或者名称。');
			return;
		}
		ajaxRequest({
			method:'post',
			data:{"supplierId":supplierId,"warehouseId":spId,"warehouseName":spName},
			url:'/supplier/client/getStorageInfo.htm',
			success:function(data){
				data = eval('('+data+')');
				if(data.success && 'false' != data.success){
					// by zhs 修改赋值 原来var warehouse = data.warehouse
					var warehouse = data.data;
					if(warehouse){
						jQuery("#pur_order_add_warehouse_id").val(warehouse.id);
						jQuery("#pur_order_add_warehouse_id_hidden").val(warehouse.id);
						jQuery("#pur_order_add_warehouse_name").val(warehouse.name);
						jQuery("#pur_order_add_warehouse_type").val(warehouse.warehouseType);
					}
				} else {
					alertMsg('供应商仓库不存在。');
					return;
				}
			}
		})
	});
	
	//仓库查询按钮
	jQuery("#pur_order_add_warehouse_query").click(function(){
		var supplierId = jQuery("#all_page_add_supplier_id_hidden").val();
		if(!supplierId){
			alertMsg('请先填写供应商信息');
			return;
		}
		ajaxRequest({
			method:'post',
			data:{"supplierId":supplierId},
			url:'/supplier/client/getStoragesRadio.htm',
			success:function(data){
				showPopDiv(10,data,{"title":"选择仓库"});
			}
		})
	});
	
	/**
	 * 币别更换
	 */
	jQuery("#currencySelId").change(function(){
		var currencyVal = jQuery(this).val();
		if('CNY'==currencyVal){
			jQuery("#exchangeRateId").val("1");
		} else {
			jQuery("#exchangeRateId").val("");
		}
	});
	
	registeMoneyChangeEvent();
	
}

/**
 * 注册页面金额变动或者数量变动的事件
 * 
 */
function registeMoneyChangeEvent(){
	var trObj = jQuery("tr[id^='addQuotaItemTr_']");
	var iframeId = self.frameElement.getAttribute('id');
    for(var i=0;i<trObj.length;i++){
    	var id = jQuery(trObj[i]).attr("id");
    	var index = id.toString().replace('addQuotaItemTr_','');
    	window.parent.purchaseOrderFun.registeOneLineEvent(iframeId,index);
    }
}