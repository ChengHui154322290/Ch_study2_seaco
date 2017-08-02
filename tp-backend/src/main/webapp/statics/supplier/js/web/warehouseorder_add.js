jQuery(document).ready(function(){
	
	$("#orderCode").focus(function(){
		this._cache = this.value;
		if(this._cache == '单号'){
			$("#orderCode").attr("value","");
		}
    }).blur(function(){
        if(this.value=="")
            this.value='单号';
    });
	
//	添加仓库预约单-保存
	jQuery("#order_add_save").click(function(){
		var orderCode = jQuery("#orderCode").val();
        if(orderCode == "" || orderCode == "单号"){
        	alertMsg('请先填写单号。');
        	return;
        }
        
        var bookingDate = jQuery("#bookingDate").val();
        if(bookingDate == ""){
        	alertMsg('请先填写预约日期。');
        	return;
        }
        
        var date = new Date(Date.parse(bookingDate.replace(/-/g,   "/")));
        if(date <= new Date()){
        	alertMsg('预约日期必须大于当前时间。');
        	return;
        }
        var form = jQuery("#warehouse_add_form");
        $.ajax({
			url:form.attr('action'),
			data:form.serialize(),
			dataType:'json',
			type:'post',
			success:function(resultInfo){
				if(resultInfo.success){
					jQuery("#order_add_save").attr("disabled",true);
					alertMsg('保存成功');
					window.location.href=domain+'/supplier/warehouseorderList';
				}else{
					alertMsg(resultInfo.msg.message);
				}
			}
		});
	});
	
//	添加仓库预约单-单据类型-确定
	jQuery("#orderTypeConfirm").click(function(){
		var purchaseCode = jQuery("#purchaseCode").val();
		var purchaseType = jQuery("#purchaseTypeId").val();
        if(purchaseCode == "" || purchaseCode == "单号"){
        	alertMsg('请先填写单号。');
        	return;
        }
        if(!purchaseType){
        	alertMsg("请选择单据类型。");
        	return;
        }
        ajaxRequest({
			method:'post',
			data:{"orderCode":purchaseCode,"orderType":purchaseType},
			url:'/supplier/isWarehouseOrderExist.htm',
			success:function(data){
				data = eval('('+data+')');
				if(data.success && data.success != 'false'){
					var warehouseVO = data.data;
					if(warehouseVO){
						jQuery("#warehouseId").val(warehouseVO.warehouseId);
						jQuery("#supplierName").val(warehouseVO.supplierName);
						jQuery("#supplierId").val(warehouseVO.supplierId);
						jQuery("#purchaseDate").val(getJsonDateStr(warehouseVO.purchaseDate,'yyyy-MM-dd hh:mm:ss'));
						jQuery("#warehouseName").val(warehouseVO.warehouseName);
						jQuery("#warehouseAddr").val(warehouseVO.warehouseAddr);
						jQuery("#warehouseLinkmanName").val(warehouseVO.warehouseLinkmanName);
						jQuery("#warehouseLinkmanTel").val(warehouseVO.warehouseLinkmanTel);
						jQuery("#orderExpectDate").val(getJsonDateStr(warehouseVO.orderExpectDate,'yyyy-MM-dd hh:mm:ss'));
					} else {
						jQuery("#warehouseId").val("");
						jQuery("#supplierName").val("");
						jQuery("#orderDate").val("");
						jQuery("#warehouseName").val("");
						jQuery("#warehouseAddr").val("");
						jQuery("#warehouseLinkmanName").val("");
						jQuery("#warehouseLinkmanTel").val("");
						jQuery("#orderExpectDate").val("");
						alertMsg('无此单据信息！');
					}
				} else {
					alertMsg('无此单据信息。');
				}
			}
		})
		
	});
	
//	添加仓库预约单-单据类型-查询
	jQuery("#orderTypeId").change(function(){
		var orderCode = jQuery("#orderCode").val();
		var orderType = jQuery("#orderTypeId").val();
		if(orderCode && orderType){
			jQuery("#orderTypeConfirm").click();
		}
	});
	
	/**
	 * 预约单提交到仓库按钮
	 */
	jQuery("#warehoueSubmit").click(function(){
		var form = jQuery("#warehouse_show_form");
		$.ajax({
			url:form.attr('action'),
			data:form.serialize(),
			dataType:'json',
			type:'post',
			success:function(resultInfo){
				if(resultInfo.success){
					jQuery("#warehoueSubmit").attr("disabled",true);
					alertMsg('保存成功');
					window.location.href=domain+'/supplier/warehouseorderList';
				}else{
					alertMsg(resultInfo.msg.message);
				}
			}
		});
	});
//	添加仓库预约单-提交
	jQuery("#order_add_saveandsubmit").click(function(){
		var orderCode = jQuery("#orderCode").val();
        if(orderCode == "" || orderCode == "单号"){
        	alertMsg('请先填写单号。');
        	return;
        }
        
        var bookingDate = jQuery("#bookingDate").val();
        if(bookingDate == ""){
        	alertMsg('请先填写预约日期。');
        	return;
        }
        
		var date = new Date(Date.parse(bookingDate.replace(/-/g,   "/")));
	    if(date <= new Date()){
	  	  alertMsg('预约日期必须大于当前时间。');
	  	  return;
	    }
	    var form = jQuery("#warehouse_add_form");
		$.ajax({
			url:domain+'/supplier/warehouseOrderSaveandSub.htm',
			data:form.serialize(),
			dataType:'json',
			type:'post',
			success:function(resultInfo){
				if(resultInfo.success){
					alertMsg('保存成功');
					window.location.href=domain+'/supplier/warehouseorderList';
				}else{
					//推送失败会传回仓库预约单Id防止重复生成预约单信息
					if(resultInfo.data !==null){
						$("#id").val(resultInfo.data);
					}
					alertMsg(resultInfo.msg.message);
				}
			}
		});
	});
	
});


