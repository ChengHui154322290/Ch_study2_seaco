jQuery(function(){
	
	/**
	 * 合同取消
	 *//*
	jQuery("#contract_cancel").click(function(){
		closeCurrentTab();
	});*/
	
	/**
	 * 注册事件
	 */
	registePageEvent();
	
	if(actionType == "add"){//添加页面
		addcontractorDept();
	}else if(actionType == "edit"){//编辑页面
		registeContractor();
	} 
	
	var contract_is_submit=true;
	jQuery("#contract_add_save").click(function(){
		if(contract_is_submit==false){
			return alertMsg('正在处理，请不要重复提交!');
		}
		contract_is_submit==false;
		jQuery("font._errorMsg").remove();
		if(!validateInfo()){
			return false;
		}
		if(!checkLicen()){
			return false;
		}
		if(!checkPageInfo()){
			return false;
		}
		var form = jQuery("#contract_add_form");
		$.ajax({
			url:form.attr('action'),
			data:form.serialize(),
			dataType:'json',
			type:'post',
			success:function(result){
				if(result.success){
					alertMsg('保存成功');
					window.location.href=domain+"/supplier/contract/list.htm";
				}else{
					alertMsg(result.msg.message);
					contract_is_submit=true;
				}
			},
			error : function() { 
				alertMsg('异常，请等待...');
				contract_is_submit=true;
			} 
		})
	});
	
	/**
	 * 供应商联系人
	 * 
	 */
	jQuery("#supplier_link_select").change(function(e){
		e.preventDefault();
		var supplierId = jQuery("#all_page_add_supplier_id_hidden").val();
		var linkerType = jQuery(this).val();
		if(!supplierId){
			jQuery(this).find("option:eq(0)").attr("selected","selected");
			alertMsg("请选择供应商。");
			return;
		}
		if(!linkerType){
			clearLinkInfo();
			return;
		}
		ajaxRequest({
			method:'post',
			data:{"supplierId":supplierId,"linkerType":linkerType},
			url:'/supplier/getSupplierLinkers.htm',
			success:function(resultInfo){
				resultInfo = eval('('+resultInfo+')');
				if(resultInfo.success){
					clearLinkInfo();
					var optionArr = new Array()
					var dataUse = resultInfo.data;
					for(var i=0;i<dataUse.length;i++){
						var userInfo = dataUse[i];
						optionArr.push("<option value='"+userInfo.linkName+"'>");
						optionArr.push(userInfo.linkName);
						optionArr.push("</option>");
					}
					jQuery("#supplier_link_name").html(optionArr.join(""));
					if(dataUse.length >0){
						jQuery("#supplier_link_table").find("input[name='supplierLinkMobile']").val(dataUse[0].mobilePhone);
						jQuery("#supplier_link_table").find("input[name='supplierLinkEmail']").val(dataUse[0].email);
						jQuery("#supplier_link_table").find("input[name='supplierLinkTel']").val(dataUse[0].telephone);
						jQuery("#supplier_link_table").find("input[name='supplierLinkFaq']").val(dataUse[0].fax);
						jQuery("#supplier_link_table").find("input[name='supplierLinkAddr']").val(dataUse[0].linkAddress);
						jQuery("#supplier_link_table").find("input[name='supplierLinkQQ']").val(dataUse[0].qq);
					}
				}
			}
		})
		
	});
	
	jQuery("#supplier_link_name").change(function(){
	    var supplierId = jQuery("#all_page_add_supplier_id_hidden").val();
	    var linkType = jQuery("#supplier_link_select").val();
	    var linkName = jQuery(this).val();
	    if(!supplierId){
	    	alertMsg("请先选择供应商。");
	    	return;
	    }
	    ajaxRequest({
			method:'post',
			data:{"supplierId":supplierId,"linkerType":linkType,"linkName":linkName},
			url:'/supplier/getSupplierLinkers.htm',
			success:function(resultInfo){
				resultInfo = eval('('+resultInfo+')');
				if(data.success){
					clearLinkInput();
					var dataUse = resultInfo.data;
					if(dataUse.length > 0){
						jQuery("#supplier_link_table").find("input[name='supplierLinkMobile']").val(dataUse[0].mobilePhone);
						jQuery("#supplier_link_table").find("input[name='supplierLinkEmail']").val(dataUse[0].email);
						jQuery("#supplier_link_table").find("input[name='supplierLinkTel']").val(dataUse[0].telephone);
						jQuery("#supplier_link_table").find("input[name='supplierLinkFaq']").val(dataUse[0].fax);
						jQuery("#supplier_link_table").find("input[name='supplierLinkAddr']").val(dataUse[0].linkAddress);
						jQuery("#supplier_link_table").find("input[name='supplierLinkQQ']").val(dataUse[0].qq);
					}
				}
			}
		});
	});
	
	/**
	 * 西客商城联系人
	 */
	jQuery("#mt_linktype_select").change(function(){
		clearMtLinkInfo();
		jQuery("#mt_linktype_select_value").val(jQuery(this).find("option:selected").text());
		departmentUserLoad(jQuery(this).val(),jQuery("#mt_linkname_select"));
	});
	
	jQuery("#mt_linkname_select").change(function(){
		clearMtLinkInput();
		jQuery("#mt_linkname_select_value").val(jQuery(this).find("option:selected").text());
		var userId = jQuery(this).val();
		if(!userId){
			return;
		}
		ajaxRequest({
			method:'post',
			data:{"userId":userId},
			url:'/supplier/client/getUserDetail.htm',
			success:function(resultInfo){
				resultInfo = eval('('+resultInfo+')');
				if(resultInfo.success){
					var dataUse = resultInfo.data;
					if(dataUse) {
						jQuery("#mt_user_link_table").find("input[name='xgLinkMobile']").val(dataUse.mobile);
						jQuery("#mt_user_link_table").find("input[name='xgLinkEmail']").val(dataUse.email);
						jQuery("#mt_user_link_table").find("input[name='xgLinkTel']").val(dataUse.fixedPhone);
						jQuery("#mt_user_link_table").find("input[name='xgLinkFaq']").val(dataUse.fax);
						jQuery("#mt_user_link_table").find("input[name='xgLinkAddr']").val(dataUse.linkAddress);
						jQuery("#mt_user_link_table").find("input[name='xgLinkQQ']").val(dataUse.qq);
					}
				}
			}
		});
	});
	
	/**
	 * 银行账户选择
	 */
	jQuery("#contractBankAccountSel").change(function(){
		var supplierId = jQuery("#all_page_add_supplier_id_hidden").val();
		var bankInfo = jQuery(this).val();
		if(!supplierId){
			alertMsg("请选择供应商信息。");
			return;
		}
		if(!bankInfo){
			return;
		}
		
		clearSpBankInfo();
		
		ajaxRequest({
			method:'post',
			data:{"supplierId":supplierId,"bankAccount":bankInfo},
			url:'/supplier/getSupplierBank.htm',
			success:function(data){
				data = eval('('+data+')');
				if(data.success && data.success != 'false' && data.data_key){
					var useDatas = data.data_key;
					if(useDatas && useDatas.length>0){
						jQuery("#supplierBankTrArea").find("input[name='contractBankName']").val(useDatas[0].bankName);
						jQuery("#supplierBankTrArea").find("input[name='contractBankAccount']").val(useDatas[0].bankAccount);
						jQuery("#supplierBankTrArea").find("input[name='contractAccountName']").val(useDatas[0].bankAccName);
						jQuery("#supplierBankTrArea").find("select option[value='"+useDatas[0].bankCurrency+"']").attr("selected","selected");
					}
				}
			}
		});
	});
	
	jQuery("#invoiceNameSelect").change(function(){
		var invoiceInfo = jQuery(this).val();
		var supplierId = jQuery("#all_page_add_supplier_id_hidden").val();
		clearInvoiceInfo();
		ajaxRequest({
			method:'post',
			data:{"supplierId":supplierId,"invoiceInfo":invoiceInfo},
			url:'/supplier/getSupplierInvoices.htm',
			success:function(data){
				data = eval('('+data+')');
				if(data.success && data.success != 'false' && data.data_key){
					var useDatas = data.data_key;
					if(useDatas && useDatas.length>0){
						jQuery("#invoiceNameTrArea").find("input[name='taxpayerCode']").val(useDatas[0].taxpayerCode);
						jQuery("#invoiceNameTrArea").find("input[name='kpBank']").val(useDatas[0].bankName);
						jQuery("#invoiceNameTrArea").find("input[name='kpAccountName']").val(useDatas[0].bankAccName);
						jQuery("#invoiceNameTrArea").find("input[name='kpAccount']").val(useDatas[0].bankAccount);
						jQuery("#invoiceNameTrArea").find("input[name='kpTel']").val(useDatas[0].linkPhone);
						jQuery("#invoiceNameTrArea").find("input[name='kpAddress']").val(useDatas[0].linkAddr);
					}
				}
			}
		});
	});
	
	jQuery("#isAggermentContractChecked").click(function(){
		if(jQuery(this).attr("checked")){
			jQuery("#isAgreementContractSet").val("1");
			jQuery("#uploadAggermentContractSpan").show();
		} else {
			jQuery("#isAgreementContractSet").val("");
			jQuery("#uploadAggermentContractSpan").hide();
		}
	});
	
	jQuery("#contract_pre_view").click(function(){
		var contractId = jQuery("#contractId").val();
		if(!contractId){
			return;
		}
		window.open("/supplier/contract/preview?cid="+contractId);
		//window.parent.showPdf("/supplier/contract/contractPreview?cid="+contractId);
	});
	
	registeValidateEvent();
});

/**
 * 清空发票信息
 */
function clearInvoiceInfo(){
	jQuery("#invoiceNameTrArea").find("input[name='taxpayerCode']").val("");
	jQuery("#invoiceNameTrArea").find("input[name='kpBank']").val("");
	jQuery("#invoiceNameTrArea").find("input[name='kpAccountName']").val("");
	jQuery("#invoiceNameTrArea").find("input[name='kpAccount']").val("");
	jQuery("#invoiceNameTrArea").find("input[name='kpTel']").val("");
	jQuery("#invoiceNameTrArea").find("input[name='kpAddress']").val("");
}

/**
 * 清空供应商银行信息
 */
function clearSpBankInfo(){
	jQuery("#supplierBankTrArea").find("input[name='contractBankName']").val("");
	jQuery("#supplierBankTrArea").find("input[name='contractBankAccount']").val("");
	jQuery("#supplierBankTrArea").find("input[name='contractAccountName']").val("");
	jQuery("#supplierBankTrArea").find("select option[value='CNY']").attr("selected","selected");
}

/**
 * 清空西客商城联系人信息
 */
function clearMtLinkInfo(){
	jQuery("#mt_linkname_select").html("<option value=''>请选择</option>");
	clearMtLinkInput();
}

/**
 * 清空西客商城联系人输入框
 */
function clearMtLinkInput(){
	jQuery("#mt_user_link_table").find("input[name='xgLinkMobile']").val("");
	jQuery("#mt_user_link_table").find("input[name='xgLinkEmail']").val("");
	jQuery("#mt_user_link_table").find("input[name='xgLinkTel']").val("");
	jQuery("#mt_user_link_table").find("input[name='xgLinkFaq']").val("");
	jQuery("#mt_user_link_table").find("input[name='xgLinkAddr']").val("");
	jQuery("#mt_user_link_table").find("input[name='xgLinkQQ']").val("");
}

/**
 * 清空联系人信息
 * 
 */
function clearLinkInfo(){
	jQuery("#supplier_link_name").html('');
	clearLinkInput();
}

function clearLinkInput(){
	jQuery("#supplier_link_table").find("input[name='supplierLinkMobile']").val('');
	jQuery("#supplier_link_table").find("input[name='supplierLinkEmail']").val('');
	jQuery("#supplier_link_table").find("input[name='supplierLinkTel']").val('');
	jQuery("#supplier_link_table").find("input[name='supplierLinkFaq']").val('');
	jQuery("#supplier_link_table").find("input[name='supplierLinkAddr']").val('');
	jQuery("#supplier_link_table").find("input[name='supplierLinkQQ']").val('');
}

/**
 * 校验协议合同
 */
function checkIsAggermentContract(){
	if(jQuery("#isAggermentContractChecked").attr("checked")) {
		if(!jQuery("#agreementContractId").val()){
			alertMsg("协议合同必须上传合同附件。");
			return false;
		} else {
			return true;
		}
	} else {
		return true;
	}
}

/**
 * 校验销售渠道
 */
function checkSalesWay(){
	var checkboxSel = jQuery("input[name='salesWay']:checked");
	if(checkboxSel.length){
		return true;
	} else {
		alertMsg("销售渠道至少选择一个。");
		return false;
	}
}

/**
 * 校验供应商联系人信息
 * 
 */
function checkSupplierLink(){
	var contractType = jQuery("#all_page_add_contract_type").val();
	var supplierLinker = jQuery("#supplier_link_name").val();
	var mtLinkName = jQuery("#mt_linkname_select").val();
	if("Purchase" == contractType || "sell" == contractType){
		if(supplierLinker && mtLinkName){
			return true;
		} else {
			alertMsg("自营供应商必须填写经办、采购及其联系方式。");
			return false;
		}
	} else {
		return true;
	}
}

/**
 * 校验供应商的开票信息
 */
function checkKpInfo() {
	var contractType = jQuery("#all_page_add_contract_type").val();
	var invoiceName = jQuery("#invoiceNameSelect").val();
	if("Associate" == contractType){
		if(invoiceName){
			return true;
		} else {
			alertMsg("代发供应商必须填开票信息。");
			return false;
		}
	} else {
		return true;
	}
}


/**
 * 校验商品范围
 */
function checkProduct(){
	var checkproduct = jQuery("#productAddInfo tr").size();
	if(!checkproduct || checkproduct.length == 0){
		alertMsg('请填写商品范围的信息。');
		return false;
	}else{
		return true;
	}
}

/**
 * 校验资质文件
 */
function checkLicen(){
	//纸质证明验证
	var quaCheckBox = jQuery("input[name^='quaitionAttach']:checked");
	for(var i=0;i<quaCheckBox.length;i++){
		var ckSel = quaCheckBox[i];
		var cbId = jQuery(ckSel).attr("name").toString().replace("quaitionAttach_","");
		var quaId = jQuery(ckSel).val();
		var inputFileId = "qualityFile"+cbId+"_"+quaId;
		if(!jQuery("#"+inputFileId).val() && !jQuery("#quaitionAttach_sel_"+cbId).val()){
			alertMsg("选中的资质文件必须上传。");
			return false;
		}
	}
	return true;
}

/**
 * 添加商品
 */
function addproduct(){
	var supplierId = jQuery("#all_page_add_supplier_id").val();
	if(!supplierId){
		alertMsg('请先填写供应商信息。');
		return;
	}
	ajaxRequest({
		method:'post',
		data:{"supplierId":supplierId},
		url:'/supplier/contract/addproduct.htm',
		success:function(data){
			showPopDiv(8,data,{"title":"添加商品"});
		}
	});
}

/**
 * 删除添加的商品
 */
function deleteContractProduct(index,endTag){
	var tagArr = endTag.toString().split("_");
	var bid = "";
	var cid = "";
	var bigNameTdId = "bigCname";
	var qualiTdId = "qualiTdId";
	if(tagArr.length>1){
		bid = tagArr[0];
		cid = tagArr[1].replace("b","");
	}
	if(cid){
		bigNameTdId = bigNameTdId+"_"+bid+"_"+cid;
		qualiTdId = qualiTdId+"_"+bid+"_"+cid;
	} else {
		bigNameTdId = bigNameTdId+"_"+bid;
		qualiTdId = qualiTdId+"_"+bid;
	}
	var cRowSpanNum = jQuery("#"+bigNameTdId).attr("rowSpan");
	if(!cRowSpanNum){
		cRowSpanNum = jQuery("#"+bigNameTdId).attr("rowspan");
	}
	var qRowSpanNum = jQuery("#"+qualiTdId).attr("rowSpan");
	if(!qRowSpanNum){
		qRowSpanNum = jQuery("#"+qualiTdId).attr("rowspan");
	}
	if(cRowSpanNum){
		jQuery("#"+bigNameTdId).attr("rowSpan",parseInt(cRowSpanNum)-1+"");
		jQuery("#"+qualiTdId).attr("rowSpan",parseInt(qRowSpanNum)-1+"");
	}
	//判断删除的当前行是不是该大类下面的第一个行
	var currentTr = jQuery("#contractProduct_"+index);
	var nextTr = currentTr.next("tr");
	var nextBrandId = nextTr.find("input[name='brandId']").val();
	var nextBigCid = nextTr.find("input[name='bigId']").val();
	if(currentTr.find("#"+bigNameTdId).html()
			&& nextBrandId && nextBigCid && cid
			&& parseInt(nextBrandId) == parseInt(bid)
			&& parseInt(nextBigCid) == parseInt(cid)){
		var currentQuantityTd = jQuery("#contractProduct_"+index).find("#"+qualiTdId);
		var brandTd = jQuery("#contractProduct_"+index).next("tr").find("td:eq(1)");
		var commissionTd = jQuery("#contractProduct_"+index).next("tr").find("td:eq(4)");
		jQuery("#contractProduct_"+index).find("#"+bigNameTdId).insertAfter(brandTd);
		currentQuantityTd.insertAfter(commissionTd);
		jQuery("#contractProduct_"+index).remove();
	} else {
		jQuery("#contractProduct_"+index).remove();
	}
	
	//如果该品牌和大类已经不存在时  资质文件干掉
	if(!jQuery("#"+bigNameTdId).html()){
		jQuery("#zzzm_"+cid+"_"+bid).remove();
	}
	
}


/**
 * 押金变更
 */
//function adddepositchange(){
//	var supplierId = jQuery("#all_page_add_supplier_id_hidden").val();
//	var basevalue = jQuery("#basevalue").val();
//	if(!supplierId){
//		alertMsg('请先填写合同的信息。');
//		return;
//	}
//	var type=jQuery("#costType").val();
//	if(type == "Deposit")
//	{
//		ajaxRequest({
//			method:'post',
//			data:{"basevalue":basevalue},
//			url:'/supplier/addDepositchange.htm',
//			success:function(data){
//				data = eval('('+data+')');
//				showPopDiv(13,data.html);
//			}
//		})
//	}
//	else{
//		alertMsg("此类型不能变更。");
//		return false;
//	}
//}

/**
 * 添加资质证明的一行
 */
function addCategoryQuationLine(cid,bid,brandName,smallName,quaObj,isShow){
	var zzArr = new Array();
	/*var index = jQuery("#fieldIdDefined").val();
	jQuery("#fieldIdDefined").val(parseInt(index)+1);*/
	zzArr.push('<table width="100%" cellspacing="0" cellpadding="0" class="form_table pt15 pb15" id="zzzm_'+cid+'_'+bid+'" name="zzInfoTable" style="width:495px;">');
	zzArr.push('<tr>');
	zzArr.push('<td colspan="4">资质证明&nbsp;&nbsp;&nbsp;'+brandName+'&nbsp;&nbsp;'+smallName+'</td>');
	zzArr.push('</tr>');
	if(null != quaObj && quaObj.length>0){
		for(var i=0;i<quaObj.length;i++){
			var oneQuaObj = quaObj[i];
			var quaId = oneQuaObj.id;
			var quaName =oneQuaObj.name;
			var endTag = cid+'_'+bid+'_'+quaId;
			var definedFieldName = 'qualityFile'+endTag;
			var defindFieldName2 = "quaName"+endTag;
			zzArr.push('<tr>');
			zzArr.push('<td align="center" width="20%">');
			zzArr.push('<input type="checkbox" value="'+quaId+'" name="quaitionAttach_'+cid+'_'+bid+'">');
			zzArr.push('</td>');
			zzArr.push('<td  width="40%">'+quaName+'<input type="hidden" value="'+quaName+'" name="'+defindFieldName2+'"></td>');
			zzArr.push('<td class="" width="120" align="right">');
			if(isShow && 1 == isShow){
				//zzArr.push('<input type="file" name="'+definedFieldName+'" id="'+definedFieldName+'" class="_imgPre input-text lh30" size="15">');
			} else {
				zzArr.push('<input type="file" name="'+definedFieldName+'" id="'+definedFieldName+'" class="_imgPre input-text lh30" size="15">');
			}
			zzArr.push('</td>');
			zzArr.push('<td align="left">');
			zzArr.push('<input type="button" class="ext_btn" onclick="showImage(\''+definedFieldName+'\');" value="预览">');
			zzArr.push('</td>');
			zzArr.push('</tr>');
		}
	}
	zzArr.push('</table>');
    var categoryHtml = zzArr.join('');
    jQuery(categoryHtml).appendTo(jQuery("#zzInfoTr"));
    registeImgPreEvent(jQuery("#zzzm_"+cid+"_"+bid).find("input._imgPre"));
}

function addZzzmInfoShow(currentTag,endTag){
	addZzzmInfo(currentTag,endTag,1);
}

/**
 * 添加资质证明信息
 */
function addZzzmInfo(currentTag,endTag,isShow){
	var currentLine = jQuery(currentTag).parent().parent();
    //1. 判断是否已经存在
	//2. 如果已经存在  直接展示
	//3. 如果不存在  异步请求
	if(!checkLicen()){
		return false;
	}
	var cid = endTag.toString().split("_")[1].replace("b","");
	var bid = endTag.toString().split("_")[0];
	var brandName = jQuery(currentLine).find("input[name='brandName']").val();
    var bigCName = jQuery(currentLine).find("input[name='bigName']").val();
    var brandId = jQuery(currentLine).find("input[name='brandId']").val();
    var tableObj = jQuery("#zzzm_"+cid+"_"+bid);
    jQuery("table[id^='zzzm_']").hide();
    if(tableObj.attr("name")){ // table已经存在
    	tableObj.show();
    } else {
    	ajaxRequest({
    		method:'post',
    		data:{"cid":cid},
    		url:'/supplier/client/getQuatation.htm',
    		success:function(data){
    			data = eval('('+data+')');
    			if(data.success && data.success != 'false'){
    				var quaObj = data.quaList;
    				addCategoryQuationLine(cid,bid,brandName,bigCName,quaObj,isShow);
    			} else {
    				alertMsg('请求资质信息异常.'+data.message);
    			}
    			
    		}
    	});
    }
	return false;
}

/**
 * 添加结算规则
 * 
 */
function addContractSettlementRule(){
	var index = jQuery("#fieldIdDefined").val();
	var categoryArr = new Array();
	categoryArr.push('<tr id="contractsettlementrule_'+index+'">');
	categoryArr.push('<td class="td_right">');
	categoryArr.push('活动结束后 ：');
	categoryArr.push('</td>');
	categoryArr.push('<td style="width: 90px;">');
	categoryArr.push('<input type="text" name="day" id="day_'+index+'" class="_intnum _req input-text lh30" size="10" onchange="ruleDay();" />');
	categoryArr.push('</td>');
	categoryArr.push('<td align="left" style="width: 80px;">');
	categoryArr.push('<span class="fl">');
	categoryArr.push('<select class="select" name="dayType" style="width: 80px;">');
	categoryArr.push('<option value="naturnlday">自然日</option>');
	categoryArr.push('<option value="workday">工作日</option>');
	categoryArr.push('</select>');
	categoryArr.push('</span>');
	categoryArr.push('</td>');
	categoryArr.push('<td align="right" style="width: 140px;">');
	categoryArr.push('工作日结算当期(%) ：');
	categoryArr.push('</td>');
	
	categoryArr.push('<td  style="width: 90px;">');
	categoryArr.push('<input type="text" name="percent" id="percent_'+index+'" class="_intnum _req input-text lh30" size="10" />');
	categoryArr.push('</td>');
	
	categoryArr.push('<td align="left">');
	categoryArr.push('<a class="ext_btn" href="javascript:void(0)" onclick="addContractSettlementRule();"><span class="add"></span>添加</a>&nbsp;');
	categoryArr.push('<a class="ext_btn" href="javascript:void(0)" onclick="deletesettlementRule('+index+')"><span class="sub"></span>删除</a>');
	categoryArr.push('</td>');
	categoryArr.push('</tr>');
    var categoryHtml = categoryArr.join('');
    jQuery(categoryHtml).appendTo(jQuery("#settlementruleInfo"));
    
    //添加校验
    registeValidateEvent(jQuery("#contractsettlementrule_"+index));
    
    jQuery("#fieldIdDefined").val(parseInt(index)+1);
	return false;
}

/**
 * 删除结算规则
 */
function deletesettlementRule(index){
	jQuery("#contractsettlementrule_"+index).remove();
}

/**
 * 计算页面部分信息
 */
function ruleDay(){
	var dayFields = jQuery("input[id^='day_']");
	var totaldays = 0 ;
	for(var i = 0 ; i < dayFields.length ; i++){
		var value = jQuery(dayFields[i]).val();
		var intVal = parseInt(value);
		totaldays = totaldays + intVal;
	}
	/*if(totaldays <= 31){
		jQuery("#sumDay").val(totaldays);
	} else{
		alertMsg("总天数不能超过31天。");
	}*/
}

//function selectday(){
//	var days = jQuery("#dayType").val();
//	jQuery("#daysType").val(days);
//}

function rulePercent(){
	var dayFields = jQuery("input[id^='percent_']");
	var totalpercents = 0 ;
	for(var i = 0 ; i < dayFields.length ; i++){
		var value = jQuery(dayFields[i]).val();
		var intVal = parseInt(value);
		totalpercents = totalpercents + intVal;
	}
	if(totalpercents == 100 ){
		jQuery("#sumPercent").val(totalpercents);
		return true;
	} else {
		alertMsg("总工作日结算必须等于100%。");
		return false;
	}
}


/**
 * 添加费用明细
 */
function addContractCost() {
	var index=jQuery("#fieldIdDefined").val();
	var costArr = new Array();
	costArr.push('<tr id="contractCostInfo_'+index+'" name="costinfo">');
	costArr.push('<td  align="center" width="550">');
	costArr.push('<select class="select" name="costType" style="width: 120px;" id="costType">');
	costArr.push('<option value="rebate">返利</option>');
	costArr.push('</select>&nbsp;');
	costArr.push('<input type="text" name="value" class="_price input-text lh30" size="20">');
	costArr.push('&nbsp;币种：');
	costArr.push('<select class="_req select" style="width: 100px;" name="currency" id="costCurrency" >');
	costArr.push('<option value="CNY">人民币</option>');
	costArr.push('<option value="USD">美元</option>');
	costArr.push('<option value="EUR">欧元</option>');
	costArr.push('<option value="GBP">英镑</option>');
	costArr.push('<option value="HKD">港币</option>');
	costArr.push('<option value="JPY">日币</option>');
	costArr.push('<option value="KER">韩币</option>');
	costArr.push('</select>');
	costArr.push('</td>');
	costArr.push('<td align="left"><input type="button" value="删除" onclick="deleteContractCost('+index+');" class="ext_btn"></td>');
	costArr.push('</td>');
	costArr.push('</tr>');
    var categoryHtml = costArr.join('');
    jQuery(categoryHtml).appendTo(jQuery("#contractcostinfos"));
    
    /**
     * 添加校验
     */
    registeValidateEvent(jQuery("#contractCostInfo_"+index));
    jQuery("#fieldIdDefined").val(parseInt(index)+1);
	return false;
}

/**
 * 删除添加费用明细
 */
function deleteContractCost(index){
	jQuery("#contractCostInfo_"+index).remove();
}

/**
 * 
 * 查询供应商
 */
function registePageEvent(){
	
	/*
	 * 清空之前的信息
	 */
	function clearBeforeInfo(){
		jQuery("#zzInfoTr").html("");
		jQuery("#productAddInfo").html("");
	}
	/*
	 * 设置页面信息
	 */
	function setPageInfo(supplierObj){
		jQuery("#all_page_add_contract_type").val(supplierObj.supplierType);
		jQuery("#all_page_add_contract_t_show").val(supplierObj.supplierTypeName);
		ajaxRequest({
			method:'post',
			data:{"supplierId":supplierObj.id},
			url:'/supplier/contract/getSupplierContractInfo.htm',
			success:function(data){
				data = eval('('+data+')');
				if(data.success && data.success != 'false'){
					data = data.data;
					//1. 合同模板
					if(data.contractTemplateName){
						jQuery("#contractTemplateShow").html(data.contractTemplateName);
					}
					jQuery("#contractCodeShowTd").html(data.contractCode);
					jQuery("#contractCodeHidden").val(data.contractCode);
					//合同标题
					jQuery("#contractNameText").val(data.contractTitle);
					//2. 供应商联系人
					//3. 西客商城联系人
					//4. 银行账户信息
					var banksInfo = data.data_key.supplierBankAccountList;
					if(banksInfo){
						var bankSelArr = new Array();
						for(var i=0;i<banksInfo.length;i++){
							var bankInfo = banksInfo[i];
							bankSelArr.push("<option value='"+bankInfo.bankAccount+"'>");
							bankSelArr.push(bankInfo.bankAccount);
							bankSelArr.push("</option>");
						}
						jQuery("#contractBankAccountSel").html(bankSelArr.join(''));
						if(banksInfo.length>0){
							jQuery("#supplierBankTrArea").find("input[name='contractBankName']").val(banksInfo[0].bankName);
							jQuery("#supplierBankTrArea").find("input[name='contractBankAccount']").val(banksInfo[0].bankAccount);
							jQuery("#supplierBankTrArea").find("input[name='contractAccountName']").val(banksInfo[0].bankAccName);
							jQuery("#supplierBankTrArea").find("select option[value='"+banksInfo[0].bankCurrency+"']").attr("selected","selected");
						}
					}
					//5. 开票信息
					var supplierInvoices = data.data_key.supplierInvoiceList;
					if(supplierInvoices){
						var bankSelArr = new Array();
						for(var i=0;i<supplierInvoices.length;i++){
							var sInvoice = supplierInvoices[i];
							bankSelArr.push("<option value='"+sInvoice.name+"'>");
							bankSelArr.push(sInvoice.name);
							bankSelArr.push("</option>");
						}
						jQuery("#invoiceNameSelect").html(bankSelArr.join(''));
						if(supplierInvoices.length>0){
							jQuery("#invoiceNameTrArea").find("input[name='taxpayerCode']").val(supplierInvoices[0].taxpayerCode);
							jQuery("#invoiceNameTrArea").find("input[name='kpBank']").val(supplierInvoices[0].bankName);
							jQuery("#invoiceNameTrArea").find("input[name='kpAccountName']").val(supplierInvoices[0].bankAccName);
							jQuery("#invoiceNameTrArea").find("input[name='kpAccount']").val(supplierInvoices[0].bankAccount);
							jQuery("#invoiceNameTrArea").find("input[name='kpTel']").val(supplierInvoices[0].linkPhone);
							jQuery("#invoiceNameTrArea").find("input[name='kpAddress']").val(supplierInvoices[0].linkAddr);
						}
					}
					
				}
			}
		});
		
	}
	/*
	 *查询供应商信息
	 */
	function fn(supplierObj){
		clearBeforeInfo(supplierObj);
		setPageInfo(supplierObj);
	}
	
	registerSupplierPop(fn,{"supplierType":"contract"});

	/**
	 * 弹出添加商品层
	 */
	jQuery("#main_product_add").click(function(){
		var supplierName = "";
		var supplierId = jQuery("#all_page_add_supplier_id").val();
		ajaxRequest({
			method:'post',
			data:{"supplierId":supplierId},
			url:'/supplier/contract/addproduct.htm',
			success:function(data){
				window.parent.showTableDiv(8,data,parentFrameID,{"title":"添加商品"});
			}
		})
	});
	
	
	/**
	 * 下载文件
	 */
	/*jQuery("#Filedownload").click(function(){
		ajaxRequest({
			method:'post',
			url:'/supplier/upload/download.htm',
			success:function(data){
				window.parent.showTableDiv(8,data,parentFrameID);
			}
		})
	});*/
	
	/**
	 * 编辑保存
	 */
	jQuery("#contract_edit_save").click(function(){
		jQuery("font._errorMsg").remove();
		if(!validateInfo()){
			return false;
		}
		if(!checkPageInfo()){
			return false;
		}
		jQuery("#contract_edit_form").attr("action","/supplier/contract/editSave.htm");
		jQuery("#contract_edit_form").submit();
	});
	
	/**
	 * 合同页面提交
	 */
	jQuery("#contract_edit_submit").click(function(){
		if(!validateInfo()){
			return false;
		}
		if(!checkPageInfo()){
			return false;
		}
		confirmBox('确认提交?',function(){
			jQuery("#statusSet").val("submit");
			jQuery("#contract_edit_form").attr("action","/supplier/contract/editSub.htm");
			jQuery("#contract_edit_form").submit();
			jQuery("#contract_edit_submit").attr("disabled",true);
			jQuery("#contract_add_save").attr("disabled",true);
			jQuery("#contract_edit_submit").attr("disabled",true);
		});
	});
	
	/**
	 * 合同页面提交
	 */
	jQuery("#contract_add_submit").click(function(){
		if(!validateInfo()){
			return false;
		}
		if(!checkPageInfo()){
			return false;
		}
		confirmBox('确认提交?',function(){
			jQuery("#statusSet").val("submit");
			jQuery("#contract_add_form").attr("action","/supplier/contract/editSub.htm");
			jQuery("#contract_add_form").submit();
			jQuery("#contract_add_submit").attr("disabled",true);
		});
	});
	
	/**
	 * 审核
	 */
	jQuery("#contractShowAudit").click(function(){
		var quoId = jQuery("#contractId").val();
		ajaxRequest({
			method:'post',
			url:'/supplier/contractExam/auditPage.htm',
			data:{"quoId":quoId},
			success:function(data){
				showPopDiv(5,data,{"title":"审核"});
			}
		})
	});
	
	/**
	 * 合同终止
	 */
	jQuery("#contractShowStop").click(function(){
		var quoId = jQuery("#contractId").val();
		confirmBox('确认终止?',function(){
			jQuery("#contract_edit_form").attr("action","/supplier/contractExam/contractAuditExec.htm");
			jQuery("#statusSet").val("stop");
			jQuery("#contract_edit_form").submit();
			jQuery("#contract_edit_form").attr("disabled",true);
		});
	});
	
	jQuery("#contract_download").click(function(){
		var contractId = jQuery("#contractId").val();
		if(!contractId){
			return;
		}
		window.open("/supplier/contract/contractDownload?cid="+contractId);
	});
	
}

function checkPageInfo(){
	if(!checkProduct()){
		return false;
	}
	if(!checkLicen()){
		return false;
	}
	if(!rulePercent()){
		return false;
	}
	if(!checkIsAggermentContract()){
		return false;
	}
	if(!checkSalesWay()){
		return false;
	}
	if(!checkSupplierLink()){
		return false;
	}
	if(!checkKpInfo()){
		return false;
	}
	return true;
}

/**
 * 注册分类的事件
 */
function registeCategoryEvent(index){
	jQuery("#supplierCategoryBig_"+index).html(daleiHtml);
	//大类事件
	jQuery("#supplierCategoryBig_"+index).change(function(){
		jQuery("#supplierCategoryMid_"+index).html('<option value="">全部</option>');
		jQuery("#supplierCategorySmall_"+index).html('<option value="">全部</option>');
		//大类id
		var bigCid = jQuery(this).val();
		if(bigCid){
			loadMidCategoryOptions(bigCid,1,index);
		}
	});
	//中类事件
	jQuery("#supplierCategoryMid_"+index).change(function(){
		jQuery("#supplierCategorySmall_"+index).html('<option value="">全部</option>');
		//大类id
		var midCid = jQuery(this).val();
		if(midCid){
			loadMidCategoryOptions(midCid,2,index);
		}
	});
	//小类事件
	jQuery("#supplierCategorySmall_"+index).change(function(){
		//大类id
		var midCid = jQuery(this).val();
		var thisId = jQuery(this).attr("id");
		var thisVal = jQuery(this).val();
		if(midCid){
			var selects = jQuery("select[id^='supplierCategorySmall_']");
			for(var i=0;i<selects.length;i++){
				var tId = jQuery(selects[i]).attr("id");
				if(tId != thisId){
					var valSel = jQuery("#"+tId).val();
					if(thisVal == valSel){
						alertMsg('此分类已被选择过。');
						jQuery("#"+thisId).find("option[value='']").attr("selected","selected");
						return;
					}
				}
			}
			jQuery("#categoryStatusVal_"+index).append("<input type='hidden' name='categorySmaillSel' id='categorySmallConfirm_"+index+"' value='"+thisVal+"' />");
		}
	});
}

/**
 * 加载中类option
 */
function loadMidCategoryOptions(bCid,opType,index){
	ajaxRequest({
		method:'post',
		url:'/supplier/client/getCategorys.htm',
		data:{"cid":bCid},
		success:function(data){
			data = eval('('+data+')');
			if(data.success && 'false' != data.success){
				var cList = data.data;
				var opArr = new Array();
				opArr.push('<option value=""></option>');
				if(!cList || cList.length == 0){
					return;
				}
				for(var i=0;i<cList.length;i++){
					var cInfo = cList[i];
					opArr.push("<option value='"+cInfo.id+"'>"+cInfo.name+"</option>");
				}
				if(1 == opType){
					jQuery("#supplierCategoryMid_"+index).html(opArr.join(""));
				} else {
					jQuery("#supplierCategorySmall_"+index).html(opArr.join(""));
				}
				
			}
		}
	})
}

/**
 * 新增报价单信息
 */
function addQuotation() {
	var contractId = jQuery("#contractId").val();
	var supplierId = jQuery("#all_page_add_supplier_id").val();
	if(contractId && supplierId){
		//到新增报价单页面
		supplierShowTab("quotation_list_add_btn","新增报价单","/supplier/quotationAdd.htm?spId="+supplierId+"&cId="+contractId);
	} else {
		 alertMsg('请先保存此合同。');
	}
	return false;
}

/**
 * 显示报价单详情页面
 */
function showQuotationDetailPage(quoId) {
	supplierShowTab("contract_list_show_quotation_btn","报价单详情","/supplier/quotationShow.htm?quoId="+quoId);
	return false;
}
/***
 * 添加签约人部门
 */
function addcontractorDept(){
	var departOptHtml = $('#departmentList').html();
	var contractorDeptArr = new Array();
	contractorDeptArr.push('<select class="_req select" style="width:100px;" id="contractorDeptId" name="contractorDeptId">');
	contractorDeptArr.push('<option value="">请选择</option>');
	contractorDeptArr.push(departOptHtml);
	contractorDeptArr.push('</select>');
	contractorDeptArr.push('<input type="hidden" id="contractorDeptName" name="contractorDeptName">')//部门名
	var contractorDeptHtml = contractorDeptArr.join('');
	jQuery(contractorDeptHtml).appendTo(jQuery("#contractorDeptSpan"));
    registeContractor();
	return false;
}
/**
 * 注册签约人件
 */
function registeContractor(){
	jQuery("#contractorDeptId").change(function(){
		jQuery("#contractorId").html("<option value=''>请选择</option>");
		var bmId = jQuery(this).val();
		if(!bmId){
			return;
		}
		jQuery("#contractorDeptName").val(jQuery(this).find("option:selected").text());//部门名
		departmentUserLoad(bmId,jQuery("#contractorId"));
	});
	jQuery("#contractorId").change(function(){
		jQuery("#contractorName").val(jQuery(this).find("option:selected").text());//人员名
	});
}

/**
 * 根据部门加载用户信息
 * 
 * @param departId
 * @param target
 */
function departmentUserLoad(departId,target){
	ajaxRequest({
		method:'post',
		url:'/supplier/client/getXgUsers.htm',
		data:{"bmId":departId},
		success:function(resultInfo){
			resultInfo = eval('('+resultInfo+')');
			if(resultInfo.success){
				var mtUsers = resultInfo.data;
				if(mtUsers && mtUsers.length>0){
					var optArr = new Array();
					optArr.push("<option value=''>请选择</option>");
					for(var i=0;i<mtUsers.length;i++){
						var mtUser = mtUsers[i];
						optArr.push("<option value="+mtUser.id+">"+mtUser.userName+"</option>");
					}
					target.html(optArr.join(""));
				}
			}
		}
	});
}

