$(function(){
	
	/**
	 * 点击保存
	 */
	$("#quotationAddSave").click(function(){
		$("font._errorMsg").remove();
		if(!validateInfo()) {
			return false;
		}
		if(!validatePageInfo()) {
			return false;
		}
		var form = $("#quotation_add_form");
		$.ajax({
			url:form.attr('action'),
			data:form.serialize(),
			dataType:'json',
			type:'post',
			success:function(resultInfo){
				if(resultInfo.success){
					//disableBtn();
					alertMsg('保存成功');
					//disableBtn();
				}else{
					alertMsg(resultInfo.msg.message);
				}
			}
		
		})
	});
	
	$("#quotationEditSave").click(function(){
		$("font._errorMsg").remove();
		if(!validateInfo()) {
			return false;
		}
		if(!validatePageInfo()) {
			return false;
		}
		
		var form = $("#quotation_add_form");
		$.ajax({
			url:domain + "/supplier/quotationEditSave.htm",
			data:form.serialize(),
			dataType:'json',
			type:'post',
			success:function(resultInfo){
				if(resultInfo.success){
					//disableBtn();
					alertMsg('保存成功');
					//disableBtn();
				}else{
					alertMsg(resultInfo.msg.message);
				}
			}
		
		});
	});
	
	/**
	 * 报价单提交
	 */
	$("#quotationAddSub").click(function(){
		$("font._errorMsg").remove();
		if(!validateInfo()){
			return false;
		}
		if(!validatePageInfo()){
			return false;
		}
		var form =$("#quotation_add_form");
		$.ajax({
			url:domain + "/supplier/quotationSub.htm",
			data:form.serialize(),
			dataType:'json',
			type:'post',
			success:function(resultInfo){
				if(resultInfo.success){
					alertMsg('保存成功');
					disableBtn();
					window.location.href=domain+'/supplier/quotationList.htm'
				}else{
					alertMsg(resultInfo.msg.message);
				}
			}
		
		});
	});
	
	/**
	 * 审核
	 */
	$("#quotationShowAudit").click(function(){
		var quoId = $("#quoId").val();
		ajaxRequest({
			method:'post',
			url:domain + '/supplier/quotationExam/auditPage.htm',
			data:{"quoId":quoId},
			success:function(data){
				showPopDiv(5,data,{"title":"审核"});
			}
		})
	});
	
	/**
	 * 报价单取消
	 */
	$("#quotationShowCancel").click(function(){
		closeCurrentTab();
	});
	$("#downLoadId").click(function(){
		var quoId = $("#quoId").val();
	 	var downloadUrl = domain + "/supplier/quotation/downLoadPdf?quoId="+quoId;
		window.location.href=downloadUrl;
	});
	$("#previewId").click(function(){
		var quoId = $("#quoId").val();
	 	var downloadUrl = domain + "/supplier/quotation/previewPdf?quoId="+quoId;
		window.open(downloadUrl);
	});
	/**
	 * 注册闻事件
	 */
	registePageEvent();
	
	function disableBtn(){
		$("#quotationEditSave").attr("disabled",true);
		$("#quotationAddSub").attr("disabled",true);
	}
});

/**
 * 验证页面信息
 */
function validatePageInfo(){
	var skuInfos = $("#quotation_item_add_body").find("input[name='skuCode']");
	if(!skuInfos || skuInfos.length==0){
		alertMsg('商品列表不能为空。');
		return false;
	} else {
		return true;
	}
}

/**
 * 添加商品
 */
function addItem(){
	var supplierId = $("#all_page_add_supplier_id_hidden").val();
	var supplierType = $("#all_page_add_supplier_type").val();
	if(!supplierId){
		alertMsg('请先填写供应商信息。');
		return;
	}
	// by zhs 修改url。原url:domain + '/supplier/quotation/getItemInfoForm.htm'
	ajaxRequest({
		method:'post',
		data:{"supplierId":supplierId,"supplierType":supplierType},
		url:'/supplier/quotation/getItemInfoForm.htm',
		success:function(data){
			showPopDiv(9,data,{"title":"添加商品"});
		}
	})
}

/**
 * 粘贴输入
 */
function pasteItem(){
	var supplierId = $("#all_page_add_supplier_id_hidden").val();
	var supplierType = $("#all_page_add_supplier_type").val();
	if(!supplierId){
		alertMsg('请先填写供应商信息。');
		return;
	}
	// by zhs 修改url。原		url:domain + '/supplier/quotation/pasteItemInForm.htm'
	ajaxRequest({
		method:'post',
		data:{"supplierId":supplierId,"supplierType":supplierType},
		url:'/supplier/quotation/pasteItemInForm.htm',
		success:function(data){
			showPopDiv(14,data,{"title":"粘贴输入","height":"435"});
		}
	})
}

/**
 * 删除一行
 * 
 * @param index
 */
function deleteThisProductTr(index){
	$("#addQuotaItemTr_"+index).remove();
	refreshIndex();
}

/**
 *
 *修改
 */
function modifyThisProductTr(index){
var tar =$("#addQuotaItemTr_"+index);
	var obj = new Object();
	obj.index = index;
	obj.basePrice = tar.find("input[name='basePrice']").val();
	//obj.supplyPrice = tar.find("input[name='supplyPrice']").val();
	obj.freight = tar.find("input[name='freight']").val();
	obj.mulTaxRate = tar.find("input[name='mulTaxRate']").val();
	obj.tarrifTaxRate = tar.find("input[name='tarrifTaxRate']").val();
	obj.sumPrice = tar.find("input[name='sumPrice']").val();
	obj.commissionPercent = tar.find("input[name='commissionPercent']").val();
	obj.standardPrice = tar.find("input[name='marketPrice']").val();
	obj.productName = tar.find("input[name='productName']").val();
	obj.sku = tar.find("input[name='skuCode']").val();

	ajaxRequest({
		method:'post',
		data:{"index":obj.index,"product":JSON.stringify(obj)},
		url:'/supplier/quotation/modifyItem.htm',
		success:function(data){
			showPopDiv(9,data,{"title":"修改商品"});
		}
	});
}

function  his(index){
	var tar =$("#addQuotaItemTr_"+index);
	var quotationProductId  = tar.find("input[name='productId']").val();
	ajaxRequest({
		method:'post',
		data:{quotationProductId:quotationProductId},
		url:'/supplier/quotation/productPriceHis.htm',
		success:function(data){
			showPopDiv(9,data,{"title":"商品历史价格"});
		}
	});
}

/**
 * 清空页面信息
 */
function cliearPageInfo(){
	$("#quotation_add_contract_code").val("");
	$("#quotation_add_contract_name").val("");
	$("#quotation_add_contract_id_hidden").val("");
	$("#quotation_add_contract_code_hidden").val("");
	$("#quotation_add_contract_type").val("");
	$("#quotation_item_add_body").html("");
}

function registePageEvent(){
	if(typeof(registerSupplierPop) != 'undefined'){
		registerSupplierPop(cliearPageInfo);
	}
	//合同确认按钮
	$("#quotation_add_contract_confirm").click(function(){
		var supplierId = $("#all_page_add_supplier_id_hidden").val();
		var supplierName = $("#all_page_add_supplier_name").val();
		var spName = $("#quotation_add_contract_name").val();
		var contractCode = $("#quotation_add_contract_code").val();
		
		if(contractCode !="" ||spName!=""){
			supplierId="";
			supplierName="";
		}
		if(!contractCode && !spName){
			alertMsg('请填写合同编号或者名称。');
			return;
		}
		// by zhs  修改url。原url:domain + '/supplier/contract/getContractInfo.htm'
		ajaxRequest({
			method:'post',
			data:{"supplierId":supplierId,"contractCode":contractCode,"contractName":spName,"supplierName":supplierName},
			url:'/supplier/contract/getContractInfo.htm',
			success:function(data){
				data = eval('('+data+')');
				if(data.success && 'false' != data.success){
					// by zhs 修改contract赋值 
					var contract = data.data;
					$("#quotation_add_contract_code_hidden").val(contract.contractCode);
					$("#quotation_add_contract_code").val(contract.contractCode);
					$("#quotation_add_contract_id_hidden").val(contract.id);
					$("#quotation_add_contract_name").val(contract.contractName);
					$("#quotation_add_contract_type").val(contract.contractTypeName);
					$("#quotation_add_supplier_id").val(contract.supplierId);
					$("#all_page_add_supplier_id").val(contract.supplierId);
					$("#all_page_add_supplier_id_hidden").val(contract.supplierId);
					$("#all_page_add_supplier_name").val(contract.supplierName);
				} else {
					alertMsg('供应商合同不存在。');
					return;
				}
			}
		})
	});
	
	//合同查询按钮
	$("#quotation_add_contract_query").click(function(){
		var supplierId = $("#all_page_add_supplier_id_hidden").val();
		if(!supplierId){
			alertMsg('请先填写供应商信息');
			return;
		}
		//  zhs 修改url， 原 url:domain + '/supplier/contract/getContracts.htm'
		ajaxRequest({
			method:'post',
			data:{"supplierId":supplierId},
			url:'/supplier/contract/getContracts.htm',
			success:function(data){
				showPopDiv(7,data,{"title":"选择合同"});
			}
		})
	});
	
}
