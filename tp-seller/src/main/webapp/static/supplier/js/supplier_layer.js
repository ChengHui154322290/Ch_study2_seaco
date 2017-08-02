var supplierRegisteMap = {};
var supplierRegisteCallBackMap = {};
var daleiHtml="";
function queryByCategoryAllByParentId(){
	$.ajax({
		url:domain+'/basedata/category/'+'queryByCategoryAllByParentId',
		dataType:'json',
		type:'get',
		async : false,
		success:function(result){
			if(result.success){
				var data = result.data;
				$.each(data,function(i,category){
					daleiHtml+='<option value="'+category.id+'">'+category.name+'</option>';
				});
			}
		}
	})
}
/**
 * 重新加载指定页面
 */
function reloadIframe(iframeId){
	showIframe(iframeId,jQuery("#"+iframeId).attr("src"));
}
/**
 * 关闭当前tab 刷新相应列表
 * 
 * @param iframeId
 * @param nextTabId
 */
function reloadTabAndRefresh(iframeId,nextTabId){
	try{
		reloadIframe(nextTabId);
	    var tabId = iframeId;
	    tabId = tabId.toString().replace("mainIframe_","");
	    closeTab(tabId);
	} catch(e){
	}
	
}

/**
 * 计算unit
 */
var caculator = {
	intAdd:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseInt(val1)+parseInt(val2);
		} catch(e){
		}
		if(isNaN(retVal)){
			retVal = "";
		}
		return retVal;
	},
	intSub:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseInt(val1)-parseInt(val2);
		} catch(e){
		}
		if(isNaN(retVal)){
			retVal = "";
		}
		return retVal;
	},
	floatAdd:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseFloat(val1)+parseFloat(val2);
		} catch(e){
		}
		if(isNaN(retVal)){
			retVal = "";
		} else {
			retVal = parseFloat(retVal).toFixed(2);
		}
		return retVal;
	},
	floatSub:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseFloat(val1)/parseFloat(val2);
		} catch(e){
		}
		if(!retVal){
			retVal = "";
		}
		if(isNaN(retVal)){
			retVal = "";
		} else {
			retVal = parseFloat(retVal).toFixed(2);
		}
		return retVal;
	},
	intDiv:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseInt(val1)/parseInt(val2);
		} catch(e){
		}
		if(!retVal){
			retVal = "";
		}
		if(isNaN(retVal)){
			retVal = "";
		}
		return retVal;
	},
	floatDiv:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseFloat(val1)/parseFloat(val2);
			retVal = parseFloat(retVal).toFixed(2);
		} catch(e){
		}
		if(!retVal){
			retVal = "";
		}
		if(isNaN(retVal)){
			retVal = "";
		}
		return retVal;
	},
	intMul:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseInt(val1)*parseInt(val2);
		} catch(e){
		}
		if(!retVal){
			retVal = "";
		}
		if(isNaN(retVal)){
			retVal = "";
		}
		return retVal;
	},
	floatMul:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseFloat(val1)*parseFloat(val2);
			retVal = parseFloat(retVal).toFixed(2);
		} catch(e){
		}
		if(!retVal){
			retVal = "";
		}
		if(isNaN(retVal)){
			retVal = "";
		}
		return retVal;
	}
}

/**
 * 图片预览
 * 
 * @param file
 * @param tagetImg
 */
function previewImage(file,tagetImg) {
	var img = new Image();
	var url = URL.createObjectURL(file);
	img.src = url;
    var imgPre = jQuery(img)
    imgPre.attr("width","650");
    imgPre.attr("height","650");
    img.onload = function() {
        URL.revokeObjectURL(url)
        tagetImg.html(imgPre);
    }
}

/**
 * 弹出信息
 * 
 * @param msg
 */
function alertMsg(msg){
	layer.alert(msg,9,'信息提示');
}

/**
 * 询问框
 * 
 * @param msg
 * @param fn
 */
function confirmBox(msg,fn,fn2){
	var layerMe = $.layer({
	    shade: [0.4, '#000'],
	    area: ['280px','auto'],
	    dialog: {
	        msg: msg,
	        btns: 2,                    
	        type: 4,
	        btn: ['确定','取消'],
	        yes: function(){
	        	if(fn){
	        		fn();
	        	}
	        	layer.close(layerMe);
	        }, no: function(){
	        	if(fn2){
	        		fn2();
	        	}
	        }
	    }
	});
}

/**
 * 弹出table层
 */
function showTableDiv(popType,dataHtml,iframeId,popOptions) {
	var title = popOptions["title"];
	var defaultWidth = "1000px";
	var defaultHeight = "500px";
	if(!title){
		title = "<b>添加</b>";
	} else {
		title = "<b>"+title+"</b>";
	}
	if(popOptions["width"]){
		defaultWidth = popOptions["width"]+"px";
	}
	if(popOptions["height"]){
		defaultHeight = popOptions["height"]+"px";
	}
	var pageii = $.layer({
	    type: 1,
	    title: title,
	    area: [defaultWidth, defaultHeight],
	    shadeClose: true,
	    border: [0], //去掉默认边框
	    shade: [0.4, '#000'], //去掉遮罩
	    closeBtn: [1, true], //去掉默认关闭按钮
	    shift: 'left', //从左动画弹出
	    page: {
	    	// style="width:420px; height:260px; padding:20px; border:1px solid #ccc; background-color:#eee;" <button id="imgPopShowbtn" class="btns" onclick="">关闭</button>
	        html: '<div><p id="tableShowArea"></p></div>'
	    }
	});
	registerPopEventWithType(popType,popOptions["callBackFn"]);
	generateData(dataHtml,popType,iframeId,pageii);
	
	return false;
}

/**
 * 供应商弹出事件注册
 */
var supplierEventFn_1 = function(genType,iframeId,pageii){
	/**
	 * 设置页面数据
	 */
	function setSuppDataToPage(spId,spName,iframeId,pageii){
		jQuery("#"+iframeId).contents().find("#supplier_add_parent_id").val(spId);
		jQuery("#"+iframeId).contents().find("#supplier_query_text").val(spName);
		layer.close(pageii);
	}
	
	//注册按钮事件
	jQuery("#supplierListQuery").click(function(){
		jQuery.ajax({
			type: 'post',
			url: '/supplier/getSuppliers.htm',
			data:jQuery("#supplier_search_form").serialize(),
			dataType:"text",
			success: function(data){
				try {
					var endIndex = data.toString().lastIndexOf("<!--");
				    var len = data.toString().length;
				    var endTag = data.toString().substring(len-3,len);
				    if('-->'==endTag){
				    	data = data.toString().substring(0,endIndex);
				    }
				} catch(e){
				}
				jQuery("#tableShowArea").html(data);
				refreshPage(genType,iframeId,pageii);
			}
		})
	});
	//注册确定按钮
	jQuery("#supplierListConfirm").click(function(){
		var checkedData = jQuery("input[name='supplierIdSel']:checked");
		var selRadioVal = checkedData.val();
		if(selRadioVal){
			var spName = checkedData.attr("dataname");
			setSuppDataToPage(selRadioVal,spName,iframeId,pageii);
		} else {
			alertMsg('请选择供应商。');
		}
	});
}

/**
 * 品牌弹出事件注册
 */
var brandEventFn_2 = function(genType,iframeId,pageii){
	//注册按钮事件
	jQuery("#brandListQuery").click(function(){
		jQuery.ajax({
			type: 'post',
			url: '/supplier/getSupplierBrands.htm',
			data:jQuery("#brand_search_form").serialize(),
			dataType:"text",
			success: function(data){
				try {
					var endIndex = data.toString().lastIndexOf("<!--");
				    var len = data.toString().length;
				    var endTag = data.toString().substring(len-3,len);
				    if('-->'==endTag){
				    	data = data.toString().substring(0,endIndex);
				    }
				} catch(e){
				}
				jQuery("#tableShowArea").html(data);
				refreshPage(genType,iframeId,pageii);
			}
		})
	});
	//注册确定按钮
	jQuery("#brandListConfirm").click(function(){
		var checkedData = jQuery("input[name='brandIdSel']:checked");
		var selRadioVal = checkedData.length;
		if(selRadioVal>0){
			setBrandToPage(checkedData,iframeId,pageii);
		} else {
			alertMsg('请选择品牌。');
		}
	});
}

/***
 * 物流事件注册
 */
var expressEventFn_3 = function(genType,iframeId,pageii){
	//注册按钮事件
	jQuery("#expressTemplateListQuery").click(function(){
		jQuery.ajax({
			type: 'post',
			url: '/supplier/client/getExpressTemplate.htm',
			data:jQuery("#expressTemplate_search_form").serialize(),
			dataType:"text",
			success: function(data){
				try {
					var endIndex = data.toString().lastIndexOf("<!--");
				    var len = data.toString().length;
				    var endTag = data.toString().substring(len-3,len);
				    if('-->'==endTag){
				    	data = data.toString().substring(0,endIndex);
				    }
				} catch(e){
				}
				jQuery("#tableShowArea").html(data);
				refreshPage(genType,iframeId,pageii);
			}
		})
	});
	//注册确定按钮
	jQuery("#expressTemplateListConfirm").click(function(){
		var checkedData = jQuery("input[name='expressTemplateIdSel']:checked");
		var selRadioVal = checkedData.val();
		if(selRadioVal){
			var expressName = checkedData.attr("dataname");
			setexpressTemplateToPage(selRadioVal,expressName,iframeId,pageii);
		} else {
			alertMsg('请选择物流模板。');
		}
	});
}

var storageEventFn_4 = function(genType,iframeId,pageii){
	/**
	 * 设置仓库信息
	 */
	function setspAddStorageToPage(checkedData,iframeId,pageii){
		var alertStr = new Array();;
		var noUseMap = {};
		var addMap = {};
		var spStorageSel = jQuery("#"+iframeId).contents().find("input[name='supplierAstorageSel']");
		if(spStorageSel.length>0){
			for(var i=0;i<spStorageSel.length;i++){
				var status = jQuery(spStorageSel[i]).attr("dStatus");
				var dName = jQuery(spStorageSel[i]).attr("dataname");
				var dVal = jQuery(spStorageSel[i]).val();
				if(parseInt(status) == 1){
					addMap[dVal] = dName;
				} else {
					noUseMap[dVal] = dName;
				}
			}
		}
		for(var i=0;i<checkedData.length;i++) {
			var status = jQuery(checkedData[i]).attr("dStatus");
			var dName = jQuery(checkedData[i]).attr("dataname");
			var dVal = jQuery(checkedData[i]).val();
			if(noUseMap[dVal]){
				alertStr.push(dName);
			} else if(addMap[dVal]){
				//已经添加
				layer.close(pageii);
			} else {
				var target = jQuery("#"+iframeId).contents().find("#storageSpanTableId");
				addStorageLine(target,dVal,dName,'storage');
				layer.close(pageii);
			}
		}
		if(alertStr.length>0){
			alertMsg("仓库："+alertStr.join(",")+"已经添加，状态是失效状态，请点击【生效】按钮使其有效。");
		}
	}

	//注册按钮事件
	jQuery("#spAddStorageListQuery").click(function(){
		jQuery.ajax({
			type: 'post',
			url: '/supplier/client/getStorages.htm',
			data:jQuery("#spAddStorage_search_form").serialize(),
			dataType:"text",
			success: function(data){
				try {
					var endIndex = data.toString().lastIndexOf("<!--");
				    var len = data.toString().length;
				    var endTag = data.toString().substring(len-3,len);
				    if('-->'==endTag){
				    	data = data.toString().substring(0,endIndex);
				    }
				} catch(e){
				}
				jQuery("#tableShowArea").html(data);
				refreshPage(genType,iframeId,pageii);
			}
		})
	});
	//注册确定按钮
	jQuery("#spAddStorageListConfirm").click(function(){
		var checkedData = jQuery("input[name='spAddStorageIdSel']:checked");
		var selRadioVal = checkedData.length;
		if(selRadioVal>0){
			setspAddStorageToPage(checkedData,iframeId,pageii);
		} else {
			alertMsg('请选择仓库。');
		}
	});
}

/**
 * 刷新No.td
 */
function refreshIndex(iframeId){
	var tdObj = jQuery("#"+iframeId).contents().find("td._indexTd");
	if(tdObj){
		for(var i=0;i<tdObj.length;i++){
			jQuery(tdObj[i]).html(i+1);
		}
	}
	var tdObj2 = jQuery("#"+iframeId).contents().find("td._indexTd2");
	if(tdObj2){
		for(var i=0;i<tdObj2.length;i++){
			jQuery(tdObj2[i]).html(i+1);
		}
	}
}

/**
 * 审核弹出
 */
var auditEventFn_5 = function(genType,iframeId,pageii){
	jQuery("#auditPopConfirm").click(function(){
		confirmBox('确定提交审核？',function(){
			jQuery("#audit_pop_form").ajaxSubmit({
				dataType:'text',
				success:function(data){
					try {
						var endIndex = data.toString().lastIndexOf("<!--");
					    var len = data.toString().length;
					    var endTag = data.toString().substring(len-3,len);
					    if('-->'==endTag){
					    	data = data.toString().substring(0,endIndex);
					    }
					} catch(e){
					}
					var data = jQuery.parseJSON(data);
					alertMsg(data.message);
					layer.close(pageii);
				    jQuery("#layAllDiv").hide();
				    //showIframe(iframeId,data.nextUrl);
				    reloadTabAndRefresh(iframeId,data.nextTab);
				}
			});
		});
	});
	jQuery("#auditPopCancel").click(function(){
		layer.close(pageii);
	    jQuery("#layAllDiv").hide();
	});
}

var quotationSupplierFn_6 = function(genType,iframeId,pageii){
	/**
	 * 设置供应商信息到报价单添加页面
	 */
	function setSupplierInfoToQuotationPage(spId,spName,supplierType,iframeId,pageii){
		jQuery("#"+iframeId).contents().find("#all_page_add_supplier_id").val(spId);
		jQuery("#"+iframeId).contents().find("#all_page_add_supplier_id_hidden").val(spId);
		jQuery("#"+iframeId).contents().find("#all_page_add_supplier_name").val(spName);
		jQuery("#"+iframeId).contents().find("#all_page_add_supplier_type").val(supplierType);
		jQuery("#"+iframeId).contents().find("#all_page_add_contract_type").val(jQuery("#supplierType"+spId).val());
		jQuery("#"+iframeId).contents().find("#all_page_add_contract_t_show").val(jQuery("#supplierTypeName"+spId).val());
		layer.close(pageii);
	}
	
	/**
	 * 清空页面信息
	 */
	function clearPageInfo(iframeId){
		jQuery("#"+iframeId).contents().find("#quotation_add_contract_id").val("");
		jQuery("#"+iframeId).contents().find("#quotation_add_contract_name").val("");
		jQuery("#"+iframeId).contents().find("#quotation_add_contract_id_hidden").val("");
		jQuery("#"+iframeId).contents().find("#quotation_add_contract_type").val("");
		jQuery("#"+iframeId).contents().find("#quotation_item_add_body").html("");
		
		jQuery("#"+iframeId).contents().find("#sp_order_table_body_1").html("");
		jQuery("#"+iframeId).contents().find("#pur_order_add_warehouse_id").val("");
		jQuery("#"+iframeId).contents().find("#pur_order_add_warehouse_name").html("");
		jQuery("#"+iframeId).contents().find("#pur_order_add_warehouse_id_hidden").html("");
	}
	
	//注册按钮事件
	// by zhs 01131624 修改url。原 url: domain+'/supplier/getSuppliers.htm',
	jQuery("#supplierListQuery").click(function(){
		jQuery.ajax({
			type: 'post',
			url: domain+'/supplier/getSuppliers.htm',
			data:jQuery("#supplier_search_form").serialize(),
			dataType:"text",
			success: function(data){
				try {
					var endIndex = data.toString().lastIndexOf("<!--");
				    var len = data.toString().length;
				    var endTag = data.toString().substring(len-3,len);
				    if('-->'==endTag){
				    	data = data.toString().substring(0,endIndex);
				    }
				} catch(e){
				}
				jQuery("#tableShowArea").html(data);
				refreshPage(genType,iframeId,pageii);
			}
		})
	});

	//注册确定按钮
	jQuery("#supplierListConfirm").click(function(){
		var checkedData = jQuery("input[name='supplierIdSel']:checked");
		var selRadioVal = checkedData.val();
		if(selRadioVal){
			var spName = checkedData.attr("dataname");
			var supplierType = checkedData.attr("dataname2");
			if(supplierRegisteCallBackMap[6]){
				supplierRegisteCallBackMap[6]({"id":selRadioVal});
			}
			setSupplierInfoToQuotationPage(selRadioVal,spName,supplierType,iframeId,pageii);
			clearPageInfo(iframeId);
		} else {
			alertMsg('请选择供应商。');
		}
	});
}

/**
 * 供应商列表弹出
 */
var contractEventFn_7 = function(genType,iframeId,pageii){
	
	/**
	 * 将合同设置到页面
	 */
	function setContractToPage(selRadioVal,dataName,dataName2,contractCode,iframeId,pageii){
		jQuery("#"+iframeId).contents().find("#quotation_add_contract_code").val(contractCode);
		jQuery("#"+iframeId).contents().find("#quotation_add_contract_name").val(dataName);
		jQuery("#"+iframeId).contents().find("#quotation_add_contract_type").val(dataName2);
		jQuery("#"+iframeId).contents().find("#quotation_add_contract_id_hidden").val(selRadioVal);
		jQuery("#"+iframeId).contents().find("#quotation_add_contract_code_hidden").val(contractCode);
	}
	
	//注册按钮事件
	jQuery("#contractListQuery").click(function(){
		// by zhs 修改url。原url:  '/supplier/contract/getContracts.htm'
		jQuery.ajax({
			type: 'post',
			url: domain + '/supplier/contract/getContracts.htm',
			data:jQuery("#contract_search_form").serialize(),
			dataType:"text",
			success: function(data){
				try {
					var endIndex = data.toString().lastIndexOf("<!--");
				    var len = data.toString().length;
				    var endTag = data.toString().substring(len-3,len);
				    if('-->'==endTag){
				    	data = data.toString().substring(0,endIndex);
				    }
				} catch(e){
				}
				jQuery("#tableShowArea").html(data);
				refreshPage(genType,iframeId,pageii);
			}
		})
	});
	//注册确定按钮
	jQuery("#contractListConfirm").click(function(){
		var checkedData = jQuery("input[name='contractIdSel']:checked");
		var selRadioVal = checkedData.val();
		if(selRadioVal){
			var dataName = checkedData.attr("dataName");
			var dataName2 = checkedData.attr("dataName2");
			var contractCode = checkedData.attr("contractcode");
			setContractToPage(selRadioVal,dataName,dataName2,contractCode,iframeId,pageii);
			layer.close(pageii);
		    jQuery("#layAllDiv").hide();
		} else {
			alertMsg('请选择合同。');
		}
	});
}

/**
 * 计算押金变更的值
 */
function sumValue(){
	var basevalue = jQuery("#value").val();
	var changevalue = jQuery("#changeValue").val();
	var sumval = 0;
	/**
	 * 校验输入的是否为数字
	 */
	var checkvalue = /^-?\d+(\.\d+)?$/;
	if(checkvalue.test(changevalue)){
		sumval = parseInt(basevalue) + parseInt(changevalue);
		var newvalue = jQuery("#newvalue").val(sumval);
	}
	else{
		return false;
	}
	
}

var quotationItemEvent_9 = function(genType,iframeId,pageii){
	/**
	 * 设置商品信息
	 */
	function setItemInfo(itemObj){
		jQuery("#brandName").val(itemObj.brandName);
		jQuery("#daleiName").val(itemObj.bigCatName);
		jQuery("#midCategoryName").val(itemObj.midCatName);
		jQuery("#smaillCName").val(itemObj.smallCatName);
		jQuery("#unitName").val(itemObj.unitName);
		jQuery("#boxProp").val(itemObj.cartonSpec);
		jQuery("#productProp").val(itemObj.specifications);
		jQuery("#productName").val(itemObj.skuName);
		jQuery("#productNameTd").html(itemObj.skuName);
		jQuery("#skuCodeSel").val(itemObj.sku);
		jQuery("#barCodeSel").val(itemObj.barcode);
		jQuery("#marketPrice").val(itemObj.basicPrice);
		
		
		
	}
	
	/**
	 * 重置页面商品信息
	 */
	function resetItemInfo(){
		var supplierId = jQuery("#supplierId").val();
		jQuery("#product_pop_form")[0].reset();
		jQuery("#productNameTd").html("");
		
		jQuery("#supplierId").val(supplierId);
	}
	
	/**
	 * 校验sku是否存在
	 */
	function checkSkuExists(skuCodeSel){
		var inputSkus = jQuery("#"+iframeId).contents().find("input[name='skuCode']");
		for(var i=0;i<inputSkus.length;i++){
			if(skuCodeSel == jQuery(inputSkus[i]).val()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 添加报价单商品行
	 */
	function addQuotationProductLine(target,index){
		var skuCode = jQuery("#skuCodeSel").val();
		var barCode = jQuery("#barCodeSel").val();
		if(checkSkuExists(skuCode)){
			alertMsg('sku:'+skuCode+'已经存在！无法重复添加。');
			return;
		}
		var marketPrice = jQuery("#marketPrice").val();
		//var salePrice = jQuery("#salePrice").val();
		var supplyPrice = jQuery("#supplyPrice").val();

		var basePrice = jQuery("#basePrice").val();
		var freight = jQuery("#freight").val();
		var mulTaxRate = jQuery("#mulTaxRate").val();
		var tarrifTaxRate = jQuery("#tarrifTaxRate").val();
		var sumPrice = jQuery("#sumPrice").val();
		var commissionPercent = jQuery("#commissionPercent").val();
		var itemArr = new Array();
		itemArr.push('<tr align="center" id="addQuotaItemTr_'+index+'">');
		itemArr.push('<td class="_indexTd"></td>');
		itemArr.push('<td><input type="hidden" value="'+barCode+'" name="barCode" />'+jQuery("#barCodeSel").val()+'</td>');
		itemArr.push('<td><input type="hidden" value="'+skuCode+'" name="skuCode" />'+skuCode+'</td>');
		itemArr.push('<td><input type="hidden" value="'+jQuery("#productName").val()+'" name="productName" />'+jQuery("#productName").val()+'</td>');
		itemArr.push('<td>'+jQuery("#brandName").val()+'</td>');
		itemArr.push('<td>'+jQuery("#daleiName").val()+'</td>');
		itemArr.push('<td>'+jQuery("#midCategoryName").val()+'</td>');
		itemArr.push('<td>'+jQuery("#smaillCName").val()+'</td>');
		itemArr.push('<td><input type="hidden" name="unitName" value="'+jQuery("#unitName").val()+'">'+jQuery("#unitName").val()+'</td>');
		itemArr.push('<td><input type="hidden" name="marketPrice" value="'+marketPrice+'"><label>'+marketPrice+'</label></td>');
		//itemArr.push('<td><input type="hidden" name="salePrice" value="'+salePrice+'">'+salePrice+'</td>');
		//itemArr.push('<td><input type="hidden" name="supplyPrice" value="'+supplyPrice+'"><label>'+supplyPrice+'</label></td>');

		itemArr.push('<td><input type="hidden" name="basePrice" value="'+basePrice+'"><label>'+basePrice+'</label></td>');
		itemArr.push('<td><input type="hidden" name="freight" value="'+freight+'"><label>'+freight+'</label></td>');
		itemArr.push('<td><input type="hidden" name="mulTaxRate" value="'+mulTaxRate+'"><label>'+mulTaxRate+'</label></td>');
		itemArr.push('<td><input type="hidden" name="tarrifTaxRate" value="'+tarrifTaxRate+'"><label>'+tarrifTaxRate+'</label></td>');
		itemArr.push('<td><input type="hidden" name="sumPrice" value="'+sumPrice+'"><label>'+sumPrice+'</label></td>');
		if(commissionPercent!=""){
			itemArr.push('<td><input type="hidden" name="commissionPercent" value="'+commissionPercent+'"><label>'+commissionPercent+'</label>%</td>');
		}else{
			itemArr.push('<td><input type="hidden" name="commissionPercent" value=""><label>---</label></td>');
		}
		
		itemArr.push('<td>'+jQuery("#boxProp").val()+'</td>');
		itemArr.push('<td>'+jQuery("#productProp").val()+'</td>');
		itemArr.push('<td><input type="button" class="ext_btn" onclick="deleteThisProductTr('+index+')" value="删除"><input type="button" class="ext_btn" onclick="modifyThisProductTr('+index+')" value="修改"></td>');
		itemArr.push('</tr>');
		var lineHtml = itemArr.join("");
		jQuery(lineHtml).prependTo(target);
		alertMsg('新增成功，请输入下一条。');
	}
	
	/**
	 * 商品barcode
	 */
	jQuery("#quotation_add_barcode_confirm").click(function(){
		var supplierId = jQuery("#supplierId").val();
		var barCode = jQuery("#barCodeSel").val();
		if(!barCode){
			alertMsg('请填写条码信息。');
			return;
		}
		// by zhs 修改url。原url: '/supplier/client/getProductInfo.htm',
		jQuery.ajax({
			type: 'post',
			url: domain + '/supplier/client/getProductInfo.htm',
			data:{"supplierId":supplierId,"barCode":barCode},
			dataType:"json",
			success: function(data){
				if(data!=null && data.barcode!=null){
					setItemInfo(data);
				} else {
					alertMsg("该供应商下无此条码信息。");
				}
			}
		});
	});
	
	jQuery("#quotation_add_sku_confirm").click(function(){
		var supplierId = jQuery("#supplierId").val();
		var skuCode = jQuery("#skuCodeSel").val();
		if(!skuCode){
			alertMsg('请填写SKU信息。');
			return;
		}
		// by zhs 修改url。原url: '/supplier/client/getProductInfo.htm'
		jQuery.ajax({
			type: 'post',
			url: domain+'/supplier/client/getProductInfo.htm',
			data:{"supplierId":supplierId,"skuCode":skuCode},
			dataType:"json",
			success: function(data){
				if(data!=null && data.barcode!=null){
					setItemInfo(data);
				} else {
					alertMsg("该供应商下无此SKU信息。");
				}
			}
		});
	});
	
	jQuery("#productPopConfirm").click(function(){
		jQuery("font._errorMsg").remove();
		var marketPrice = jQuery("#marketPrice").val();
		var salePrice = jQuery("#salePrice").val();
		//var supplyPrice = jQuery("#supplyPrice").val();
		var commissionPercent = jQuery("#commissionPercent").val();
		var supplierType = jQuery("#supplierType").val();//供应商类型{sell：代发 ，Purchase：自营}

		var basePrice = jQuery("#basePrice").val();//裸价
		var freight = jQuery("#freight").val();//运费
		var mulTaxRate = jQuery("#mulTaxRate").val();//综合税率
		var tarrifTaxRate = jQuery("#tarrifTaxRate").val();//行邮税
		var sumPrice = jQuery("#sumPrice").val();//包邮包税代发价

		var errorMsg = "";
		if(marketPrice==""){
			errorMsg = "<font class='_errorMsg' color='red'>市场价不可为空</font>";
			jQuery("#marketPrice").after(jQuery(errorMsg));
			return;
		}else if(!checkPrice(marketPrice)){
			errorMsg = "<font class='_errorMsg' color='red'>市场价必须为数字，如：12.3</font>";
			jQuery("#marketPrice").after(jQuery(errorMsg));
			return;
		}

		if(basePrice==""){
			errorMsg = "<font class='_errorMsg' color='red'>裸价不可为空</font>";
			jQuery("#basePrice").after(jQuery(errorMsg));
			return;
		}else if(!checkPrice(basePrice)){
			errorMsg = "<font class='_errorMsg' color='red'>裸价必须为数字，如：12.3</font>";
			jQuery("#basePrice").after(jQuery(errorMsg));
			return;
		}

		if(freight==""){
			errorMsg = "<font class='_errorMsg' color='red'>运费不可为空</font>";
			jQuery("#freight").after(jQuery(errorMsg));
			return;
		}else if(!checkPrice(freight)){
			errorMsg = "<font class='_errorMsg' color='red'>运费必须为数字，如：12.3</font>";
			jQuery("#freight").after(jQuery(errorMsg));
			return;
		}

		if(mulTaxRate==""){
			errorMsg = "<font class='_errorMsg' color='red'>跨境综合税率不可为空</font>";
			jQuery("#mulTaxRate").after(jQuery(errorMsg));
			return;
		}else if(!checkTexRate(mulTaxRate)){
			errorMsg = "<font class='_errorMsg' color='red'>跨境综合税率必须为数字，如：12.3</font>";
			jQuery("#mulTaxRate").after(jQuery(errorMsg));
			return;
		}

		if(tarrifTaxRate==""){
			errorMsg = "<font class='_errorMsg' color='red'>行邮税税率不可为空</font>";
			jQuery("#tarrifTaxRate").after(jQuery(errorMsg));
			return;
		}else if(!checkTexRate(tarrifTaxRate)){
			errorMsg = "<font class='_errorMsg' color='red'>行邮税税率必须为数字，如：12.3</font>";
			jQuery("#tarrifTaxRate").after(jQuery(errorMsg));
			return;
		}

		if(sumPrice==""){
			errorMsg = "<font class='_errorMsg' color='red'>包邮包税代发价不可为空</font>";
			jQuery("#sumPrice").after(jQuery(errorMsg));
			return;
		}else if(!checkPrice(sumPrice)){
			errorMsg = "<font class='_errorMsg' color='red'>包邮包税代发价必须为数字，如：12.3</font>";
			jQuery("#sumPrice").after(jQuery(errorMsg));
			return;
		}

		
		//if(salePrice==""){
		//	if(supplierType=="Associate"){
		//		errorMsg = "<font class='_errorMsg' color='red'>代发建议最低售价不可为空</font>";
		//		jQuery("#salePrice").after(jQuery(errorMsg));
		//		return;
		//	}
		//}else{
		//	if(!checkPrice(salePrice)){
		//		errorMsg = "<font class='_errorMsg' color='red'>建议最低售价必须为数字，如：12.3</font>";
		//		jQuery("#salePrice").after(jQuery(errorMsg));
		//		return;
		//	}else if(parseFloat(salePrice)>parseFloat(marketPrice)){
		//		errorMsg = "<font class='_errorMsg' color='red'>不可大于市场价</font>";
		//		jQuery("#salePrice").after(jQuery(errorMsg));
		//		return;
		//	}
		//}
		
		
		//if(supplyPrice==""){
		//	if(supplierType=="Purchase"){
		//		errorMsg = "<font class='_errorMsg' color='red'>自营供货价不可为空</font>";
		//		jQuery("#supplyPrice").after(jQuery(errorMsg));
		//		return;
		//	}
		//}else{
		//	if(!checkPrice(supplyPrice)){
		//		errorMsg = "<font class='_errorMsg' color='red'>自营供货价为数字，如：12.3</font>";
		//		jQuery("#supplyPrice").after(jQuery(errorMsg));
		//		return;
		//	}else if(parseFloat(supplyPrice)>parseFloat(marketPrice)){
		//		errorMsg = "<font class='_errorMsg' color='red'>不可大于市场价</font>";
		//		jQuery("#supplyPrice").after(jQuery(errorMsg));
		//		return;
		//	}
		//
		//}
		
		if(supplierType!="Purchase"){
			if(!checkPrice(commissionPercent)){
				errorMsg = "<font class='_errorMsg' color='red'>平台使用费必须为数字，如：12.3</font>";
				jQuery("#commissionPercentHidden").after(jQuery(errorMsg));
				return;
			}else if(parseFloat(commissionPercent)>100){
				errorMsg = "<font class='_errorMsg' color='red'>平台使用费不能大于100</font>";
				jQuery("#commissionPercentHidden").after(jQuery(errorMsg));
				return;
			}
		}
		var target = jQuery("#"+iframeId).contents().find("#quotation_item_add_body");
		var index = jQuery("#"+iframeId).contents().find("#indexNumHiddenId").val();
		var bName = jQuery("#brandName").val();
		if(!bName){
			alertMsg('请填写skucode或者条形码，并点击右侧【确认】按钮');
			return;
		}
		addQuotationProductLine(target,index);
		jQuery("#"+iframeId).contents().find("#indexNumHiddenId").val(parseInt(index)+1);
		resetItemInfo();
		refreshIndex(iframeId);
	});

	/**
	 * 编辑
	 */
	jQuery("#productEditConfirm").click(function(){
		jQuery("font._errorMsg").remove();
		var index = $("#index").val();
		var basePrice = $("#basePrice").val();
		//var supplyPrice = $("#supplyPrice").val();
		var freight = $("#freight").val();
		var mulTaxRate = $("#mulTaxRate").val();
		var tarrifTaxRate = $("#tarrifTaxRate").val();
		var sumPrice = $("#sumPrice").val();
		var commissionPercent = $("#commissionPercent").val();
		var marketPrice = $("#marketPrice").val();
		var tar =  jQuery("#"+iframeId).contents().find("#addQuotaItemTr_"+index);

		if(marketPrice==""){
			errorMsg = "<font class='_errorMsg' color='red'>市场价不可为空</font>";
			jQuery("#marketPrice").after(jQuery(errorMsg));
			return;
		}else if(!checkPrice(marketPrice)){
			errorMsg = "<font class='_errorMsg' color='red'>市场价必须为数字，如：12.3</font>";
			jQuery("#marketPrice").after(jQuery(errorMsg));
			return;
		}

		if(basePrice==""){
			errorMsg = "<font class='_errorMsg' color='red'>裸价不可为空</font>";
			jQuery("#basePrice").after(jQuery(errorMsg));
			return;
		}else if(!checkPrice(basePrice)){
			errorMsg = "<font class='_errorMsg' color='red'>裸价必须为数字，如：12.3</font>";
			jQuery("#basePrice").after(jQuery(errorMsg));
			return;
		}

		if(freight==""){
			errorMsg = "<font class='_errorMsg' color='red'>运费不可为空</font>";
			jQuery("#freight").after(jQuery(errorMsg));
			return;
		}else if(!checkPrice(freight)){
			errorMsg = "<font class='_errorMsg' color='red'>运费必须为数字，如：12.3</font>";
			jQuery("#freight").after(jQuery(errorMsg));
			return;
		}

		if(mulTaxRate==""){
			errorMsg = "<font class='_errorMsg' color='red'>跨境综合税率不可为空</font>";
			jQuery("#mulTaxRate").after(jQuery(errorMsg));
			return;
		}else if(!checkTexRate(mulTaxRate)){
			errorMsg = "<font class='_errorMsg' color='red'>跨境综合税率必须为数字，如：12.3</font>";
			jQuery("#mulTaxRate").after(jQuery(errorMsg));
			return;
		}

		if(tarrifTaxRate==""){
			errorMsg = "<font class='_errorMsg' color='red'>行邮税税率不可为空</font>";
			jQuery("#tarrifTaxRate").after(jQuery(errorMsg));
			return;
		}else if(!checkTexRate(tarrifTaxRate)){
			errorMsg = "<font class='_errorMsg' color='red'>行邮税税率必须为数字，如：12.3</font>";
			jQuery("#tarrifTaxRate").after(jQuery(errorMsg));
			return;
		}

		if(sumPrice==""){
			errorMsg = "<font class='_errorMsg' color='red'>包邮包税代发价不可为空</font>";
			jQuery("#sumPrice").after(jQuery(errorMsg));
			return;
		}else if(!checkPrice(sumPrice)){
			errorMsg = "<font class='_errorMsg' color='red'>包邮包税代发价必须为数字，如：12.3</font>";
			jQuery("#sumPrice").after(jQuery(errorMsg));
			return;
		}

		//if(supplyPrice==""){
		//	if(supplierType=="Purchase"){
		//		errorMsg = "<font class='_errorMsg' color='red'>自营供货价不可为空</font>";
		//		jQuery("#supplyPrice").after(jQuery(errorMsg));
		//		return;
		//	}
		//}else{
		//	if(!checkPrice(supplyPrice)){
		//		errorMsg = "<font class='_errorMsg' color='red'>自营供货价为数字，如：12.3</font>";
		//		jQuery("#supplyPrice").after(jQuery(errorMsg));
		//		return;
		//	}else if(parseFloat(supplyPrice)>parseFloat(marketPrice)){
		//		errorMsg = "<font class='_errorMsg' color='red'>不可大于市场价</font>";
		//		jQuery("#supplyPrice").after(jQuery(errorMsg));
		//		return;
		//	}
        //
		//}



		if(supplierType!="Purchase"){
			if(!checkPrice(commissionPercent)){
				errorMsg = "<font class='_errorMsg' color='red'>平台使用费必须为数字，如：12.3</font>";
				jQuery("#commissionPercentHidden").after(jQuery(errorMsg));
				return;
			}else if(parseFloat(commissionPercent)>100){
				errorMsg = "<font class='_errorMsg' color='red'>平台使用费不能大于100</font>";
				jQuery("#commissionPercentHidden").after(jQuery(errorMsg));
				return;
			}
		}


		var bp = tar.find("input[name='basePrice']");
		bp.val(basePrice);
		bp.parent().find("label").text(basePrice);

		//var bp = tar.find("input[name='supplyPrice']");
		//bp.val(supplyPrice);
		//bp.parent().find("label").text(supplyPrice);

		var bp = tar.find("input[name='freight']");
		bp.val(freight);
		bp.parent().find("label").text(freight);

		var bp = tar.find("input[name='mulTaxRate']");
		bp.val(mulTaxRate);
		bp.parent().find("label").text(mulTaxRate);

		var bp = tar.find("input[name='tarrifTaxRate']");
		bp.val(tarrifTaxRate);
		bp.parent().find("label").text(tarrifTaxRate);


		var bp = tar.find("input[name='sumPrice']");
		bp.val(sumPrice);
		bp.parent().find("label").text(sumPrice);

		var bp = tar.find("input[name='commissionPercent']");
		bp.val(commissionPercent);
		bp.parent().find("label").text(commissionPercent);


		var bp = tar.find("input[name='marketPrice']");
		bp.val(marketPrice);
		bp.parent().find("label").text(marketPrice);
		layer.close(pageii);

	});

	
	jQuery("#productPopCancel").click(function(){
		layer.close(pageii);
	    jQuery("#layAllDiv").hide();
	});
}

/**
 * 注册供应商分类级联
 */
function registeSupplierCategoryCascade(index,supplierId){
	jQuery("#brandName").change(function(){
		jQuery("#supplierCategoryBig_"+index).html('<option value="">请选择</option>');
		jQuery("#supplierCategoryMid_"+index).html('<option value="">请选择</option>');
		jQuery("#supplierCategorySmall_"+index).html('<option value="">请选择</option>');
		//大类id
		var brandId = jQuery(this).val();
		if(brandId){
			loadSupplierCategoryOptions(supplierId,brandId,brandId,0,index);
		}
	});
	//大类事件
	jQuery("#supplierCategoryBig_"+index).change(function(){
		jQuery("#supplierCategoryMid_"+index).html('<option value="">请选择</option>');
		jQuery("#supplierCategorySmall_"+index).html('<option value="">请选择</option>');
		//大类id
		var bigCid = jQuery(this).val();
		if(bigCid){
			loadSupplierCategoryOptions(supplierId,jQuery("#brandName").val(),bigCid,1,index);
		}
	});
	//中类事件
	jQuery("#supplierCategoryMid_"+index).change(function(){
		jQuery("#supplierCategorySmall_"+index).html('<option value="">请选择</option>');
		//大类id
		var midCid = jQuery(this).val();
		if(midCid){
			loadSupplierCategoryOptions(supplierId,jQuery("#brandName").val(),midCid,2,index);
		}
	});
}

/**
 *  注册分类的级联事件
 */
function registeCategoryCascade(target,index){
	queryByCategoryAllByParentId();
	jQuery(target).find("#supplierCategoryBig_"+index).html(daleiHtml);
	//大类事件
	jQuery(target).find("#supplierCategoryBig_"+index).change(function(){
		jQuery(target).find("#supplierCategoryMid_"+index).html('<option value="">中类</option>');
		jQuery(target).find("#supplierCategorySmall_"+index).html('<option value="">小类</option>');
		//大类id
		var bigCid = jQuery(this).val();
		if(bigCid){
			loadMidCategoryOptions(target,bigCid,1,index);
		}
	});
	//中类事件
	jQuery(target).find("#supplierCategoryMid_"+index).change(function(){
		jQuery(target).find("#supplierCategorySmall_"+index).html('<option value="">小类</option>');
		//大类id
		var midCid = jQuery(this).val();
		if(midCid){
			loadMidCategoryOptions(target,midCid,2,index);
		}
	});
}

/**
 * 加载供应商分类option
 */
function loadSupplierCategoryOptions(supplierId,brandIdSel,bCid,opType,index){
	jQuery.ajax({
		method:'post',
		url:'/supplier/getSpCategorys.htm',
		data:{"spId":supplierId,"brandId":brandIdSel,"cid":bCid,"cType":opType},
		dataType:"json",
		success:function(resultInfo){
			if(resultInfo.success){
				var data = resultInfo.data;
				var cList = data;
				var opArr = new Array();
				opArr.push('<option value="">请选择</option>');
				if(!cList || cList.length == 0){
					return;
				}
				for(var i=0;i<cList.length;i++){
					var cInfo = cList[i];
					opArr.push("<option value='"+cInfo.id+"'>"+cInfo.cnName+"</option>");
				}
				if(0 == opType){
					jQuery("#supplierCategoryBig_"+index).html(opArr.join(""));
				} else if(1 == opType){
					jQuery("#supplierCategoryMid_"+index).html(opArr.join(""));
				} else {
					jQuery("#supplierCategorySmall_"+index).html(opArr.join(""));
				}
				
			}
		}
	});
}

/**
 * 加载中类option
 */
function loadMidCategoryOptions(target,bCid,opType,index){
	jQuery.ajax({
		method:'post',
		url:'/supplier/client/getCategorys.htm',
		data:{"cid":bCid},
		dataType:"text",
		success:function(data){
			try {
				var endIndex = data.toString().lastIndexOf("<!--");
			    var len = data.toString().length;
			    var endTag = data.toString().substring(len-3,len);
			    if('-->'==endTag){
			    	data = data.toString().substring(0,endIndex);
			    }
			} catch(e){
			}
			data = eval('('+data+')');
			if(data.success && 'false' != data.success){
				var cList = data.data;
				var opArr = new Array();
				opArr.push('<option value="">全部</option>');
				if(!cList || cList.length == 0){
					return;
				}
				for(var i=0;i<cList.length;i++){
					var cInfo = cList[i];
					opArr.push("<option value='"+cInfo.id+"'>"+cInfo.name+"</option>");
				}
				if(1 == opType){
					jQuery(target).find("#supplierCategoryMid_"+index).html(opArr.join(""));
				} else {
					jQuery(target).find("#supplierCategorySmall_"+index).html(opArr.join(""));
				}
				
			}
		}
	});
}

var contractproductEventFn_8 = function(genType,iframeId,pageii){
	registeSupplierCategoryCascade(1,jQuery("#supplierId").val());
	registeValidateEvent(iframeId,jQuery("#product_add_form"));
	
	function generateEndTag(bid,bigId,minId,smallId){
		var endTag = bid;
		if(bigId){
			endTag = endTag+"_b"+bigId;
		}
		if(minId){
			endTag = endTag+"_m"+minId;
		}
		if(smallId){
			endTag = endTag+"_s"+smallId;
		}
		return endTag;
	}
	
	/**
	 * 校验合同的商品是否存在
	 */
	function checkContractProduct(bid,bigId,minId,smallId){
		var endTag = generateEndTag(bid,bigId,minId,smallId);
		var brandIdSel = jQuery("#"+iframeId).contents().find("#brandId"+endTag).val();
		var cidSel = jQuery("#"+iframeId).contents().find("#smallId"+endTag).val();
		if(brandIdSel || cidSel){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 添加合同商品行
	 */
	function addContractProductLine(target,index){
		var brandName = jQuery("#brandName").find("option:selected")[0].text;
		var brandId = jQuery("#brandName").val();
		var bigName = jQuery("#supplierCategoryBig_1").find("option:selected")[0].text;
		var bigId = jQuery("#supplierCategoryBig_1").val();
		var bigNameHidden = bigName;
		if(!bigId){
			bigName = "——";
			bigNameHidden = "";
		}
		var midName = jQuery("#supplierCategoryMid_1").find("option:selected")[0].text;
		var midNameHidden = midName;
		var minId = jQuery("#supplierCategoryMid_1").val();
		if(!minId){
			midName = "——";
			midNameHidden = "";
		}
		var smallName = jQuery("#supplierCategorySmall_1").find("option:selected")[0].text;
		var smallNameHidden = smallName;
		var smallId = jQuery("#supplierCategorySmall_1").val();
		if(!smallId){
			smallName = "——";
			smallNameHidden = "";
		}
		var commission = jQuery("#commission").val();
		var productId = parseInt(index)+parseInt(1);
		
		if(commission==""){
			alertMsg("请填写佣金。");
			return;
		}
		
		if(checkContractProduct(brandId,bigId,minId,smallId)){
			//已经存在
			alertMsg("此品牌的分类已经存在，不能重复添加。");
			return;
		}
		var bigNameTdId = "bigCname";
		var qualiTdId = "qualiTdId";
		if(bigId){
			bigNameTdId = bigNameTdId+"_"+brandId+"_"+bigId;
			qualiTdId = qualiTdId+"_"+brandId+"_"+bigId;
		} else {
			bigNameTdId = bigNameTdId+"_"+brandId;
			qualiTdId = qualiTdId+"_"+brandId;
		}
		var bigCategoryTd = jQuery("#"+iframeId).contents().find("#"+bigNameTdId);
		var qualiTd = jQuery("#"+iframeId).contents().find("#"+qualiTdId);
		var endTag = generateEndTag(brandId,bigId,minId,smallId);
		var itemArr = new Array();
		if(bigCategoryTd.html()){
			//大类已经存在
			itemArr.push('<tr align="center" id="contractProduct_'+index+'">');
			itemArr.push('<td class="_indexTd"></td>');
			itemArr.push('<td>'+brandName+'</td>');
			//itemArr.push('<td id="'+bigNameTdId+'">'+bigName+'</td>');
			itemArr.push('<td>'+midName+'</td>');
			itemArr.push('<td>'+smallName+'</td>');
			itemArr.push('<td>'+commission+'</td>');
			//itemArr.push('<td  id="'+qualiTdId+'"><a href="javascript:void(0)" onclick="addZzzmInfo('+smallId+','+brandId+');">上传/查看</a></td>');
			itemArr.push('<td>启用</td>');
			itemArr.push('<td><input type="button" class="ext_btn" onclick="deleteContractProduct('+index+',\''+endTag+'\')" value="删除"></td>');
			itemArr.push('<input type="hidden" name="productId" value="'+parseInt(index)+parseInt(1)+'">');
			itemArr.push('<input type="hidden" name="brandId" value="'+brandId+'" id="brandId'+endTag+'">');
			itemArr.push('<input type="hidden" name="brandName" value="'+brandName+'" id="brandName'+endTag+'"/>');
			itemArr.push('<input type="hidden" name="bigId" value="'+bigId+'">');
			itemArr.push('<input type="hidden" name="midId" value="'+minId+'">');
			itemArr.push('<input type="hidden" name="smallId" value="'+smallId+'" id="smallId'+endTag+'">');
			itemArr.push('<input type="hidden" name="bigName" value="'+bigNameHidden+'">');
			itemArr.push('<input type="hidden" name="midName" value="'+midNameHidden+'">');
			itemArr.push('<input type="hidden" name="smallName" value="'+smallNameHidden+'" id="smallName'+endTag+'"> ');
			itemArr.push('<input type="hidden" name="commission" value="'+commission+'">');
			itemArr.push('</tr>');
			var lineHtml = itemArr.join("");
			var colspanNum = bigCategoryTd.attr("rowSpan");
			if(!colspanNum){
				bigCategoryTd.attr("rowSpan","2");
				qualiTd.attr("rowSpan","2");
			} else {
				bigCategoryTd.attr("rowSpan",parseInt(colspanNum)+1+"");
				qualiTd.attr("rowSpan",parseInt(colspanNum)+1+"");
			}
			jQuery(lineHtml).insertAfter(bigCategoryTd.parent("tr"));
		} else {
			itemArr.push('<tr align="center" id="contractProduct_'+index+'">');
			itemArr.push('<td class="_indexTd"></td>');
			itemArr.push('<td>'+brandName+'</td>');
			itemArr.push('<td id="'+bigNameTdId+'">'+bigName+'</td>');
			itemArr.push('<td>'+midName+'</td>');
			itemArr.push('<td>'+smallName+'</td>');
			itemArr.push('<td>'+commission+'</td>');
			if(bigId){
				itemArr.push('<td  id="'+qualiTdId+'"><a href="javascript:void(0)" onclick="addZzzmInfo(this,\''+endTag+'\');">上传/查看</a></td>');
			} else {
				itemArr.push('<td  id="'+qualiTdId+'">——</td>');
			}
			itemArr.push('<td>启用</td>');
			itemArr.push('<td><input type="button" class="ext_btn" onclick="deleteContractProduct('+index+',\''+endTag+'\')" value="删除"></td>');
			itemArr.push('<input type="hidden" name="productId" value="'+parseInt(index)+parseInt(1)+'">');
			itemArr.push('<input type="hidden" name="brandId" value="'+brandId+'" id="brandId'+endTag+'">');
			itemArr.push('<input type="hidden" name="brandName" value="'+brandName+'" id="brandName'+endTag+'"/>');
			itemArr.push('<input type="hidden" name="bigId" value="'+bigId+'" id="bigCategoryId'+endTag+'">');
			itemArr.push('<input type="hidden" name="midId" value="'+minId+'">');
			itemArr.push('<input type="hidden" name="smallId" value="'+smallId+'">');
			itemArr.push('<input type="hidden" name="bigName" value="'+bigNameHidden+'" id="bigCategoryName'+endTag+'">');
			itemArr.push('<input type="hidden" name="midName" value="'+midNameHidden+'">');
			itemArr.push('<input type="hidden" name="smallName" value="'+smallNameHidden+'" id="smallName'+endTag+'"> ');
			itemArr.push('<input type="hidden" name="commission" value="'+commission+'">');
			itemArr.push('</tr>');
			var lineHtml = itemArr.join("");
			jQuery(lineHtml).appendTo(target);
		}
		alertMsg('新增成功，请输入下一条。');
	}
	
	jQuery("#contract_productpop_Confirm").click(function(){
		var retVal= true;
		var target = jQuery("#"+iframeId).contents().find("#productAddInfo");
		var index = jQuery("#"+iframeId).contents().find("#indexNumHiddenId").val();
		var bName = jQuery("#brandName").val();
		if(!bName){
			alertMsg('请选择品牌');
			return;
		}
		addContractProductLine(target,index);
		jQuery("#"+iframeId).contents().find("#indexNumHiddenId").val(parseInt(index)+1);
		refreshIndex(iframeId);
	});
	
	jQuery("#contract_productpop_Cancel").click(function(){
		layer.close(pageii);
	    jQuery("#layAllDiv").hide();
	});
}

function storageEventRadio_10(genType,iframeId,pageii){
	/**
	 * 设置页面数据
	 */
	function setDataToPage(checkedData,iframeId,pageii){
		var selRadioVal = jQuery(checkedData).val();
		var dataName = jQuery(checkedData).attr("dataName");
		jQuery("#"+iframeId).contents().find("#pur_order_add_warehouse_id").val(selRadioVal);
		jQuery("#"+iframeId).contents().find("#pur_order_add_warehouse_name").val(dataName);
		jQuery("#"+iframeId).contents().find("#pur_order_add_warehouse_id_hidden").val(selRadioVal);
	}
	//注册按钮事件
	// by zhs 01131714  修改url。原url: '/supplier/client/getStoragesRadio.htm'
	jQuery("#spAddStorageListQuery").click(function(){
		jQuery.ajax({
			type: 'post',
			url: domain+'/supplier/client/getStoragesRadio.htm',
			data:jQuery("#spAddStorage_search_form").serialize(),
			dataType:"text",
			success: function(data){
				try {
					var endIndex = data.toString().lastIndexOf("<!--");
				    var len = data.toString().length;
				    var endTag = data.toString().substring(len-3,len);
				    if('-->'==endTag){
				    	data = data.toString().substring(0,endIndex);
				    }
				} catch(e){
				}
				jQuery("#tableShowArea").html(data);
				refreshPage(genType,iframeId,pageii);
			}
		})
	});
	//注册确定按钮
	jQuery("#spAddStorageListConfirm").click(function(){
		var checkedData = jQuery("input[name='spAddStorageIdSel']:checked");
		var selRadioVal = checkedData.val();
		if(selRadioVal){
			setDataToPage(checkedData,iframeId,pageii);
			layer.close(pageii);
		    jQuery("#layAllDiv").hide();
		} else {
			alertMsg('请选择仓库。');
		}
	});
}

/**
 * 注册验证事件
 */
function registeValidateEvent(iframeId,targetObj){
	//先通过die()方法解除，再通过live()绑定
	var intNumObjs = jQuery("#"+iframeId).contents().find("input._intnum");
	var priceObjs = jQuery("#"+iframeId).contents().find("input._price");
	if(targetObj){
		intNumObjs = jQuery(targetObj).find("input._intnum");
		priceObjs = jQuery(targetObj).find("input._price");
	}
	for(var i=0;i<intNumObjs.length;i++){
		jQuery(intNumObjs[i]).unbind("keydown");
		jQuery(intNumObjs[i]).keydown(function(e){
			return isNumber(e.keyCode);
		});
		jQuery(intNumObjs[i]).unbind("keyup");
		jQuery(intNumObjs[i]).keyup(function(){
			var intVal = jQuery(this).val();
			if(!checkIntNum(intVal)){
				jQuery(this).val("");
			}
		});
	}
	
	for(var i=0;i<priceObjs.length;i++){
		jQuery(priceObjs[i]).unbind("keydown");
		jQuery(priceObjs[i]).keydown(function(e){
			return isNumber(e.keyCode);
		});
		jQuery(priceObjs[i]).unbind("keyup");
		jQuery(priceObjs[i]).keyup(function(e){
			var priceVal = jQuery(this).val();
			if(!checkPrice(priceVal)){
				jQuery(this).val("");
			}
		});
	}
}

//仅能输入数字
function isNumber(keyCode) {
    // 数字
    if (keyCode >= 48 && keyCode <= 57 ) return true
    // 小数字键盘
    if (keyCode >= 96 && keyCode <= 105) return true
    // Backspace键
    if (keyCode == 8) return true
    //点
    if(keyCode == 190 || keyCode == 110) return true;
    return false
}

var purchaseOrderFun = {
	/**
	 * 注册单行的事件
	 */
	registeOneLineEvent: function(iframeId,index) {
    	var trObj = jQuery("#"+iframeId).contents().find("tr[id='addQuotaItemTr_"+index+"']");
    	var trObj2 = jQuery("#"+iframeId).contents().find("tr[id='addQuotaItemTr2_"+index+"']");
    	var countFields = jQuery(trObj).find("input[name='count']");
    	var stdPriceFields = jQuery(trObj).find("input[name='standardPrice']");
    	var sppPriceFields = jQuery(trObj).find("input[name='orderPrice']");
    	var discountField = jQuery(trObj).find("input[name='discount']");
    	var subtotalField = jQuery(trObj).find("input[name='subtotal']");
    	
    	var noTaxRateTd = jQuery(trObj2).find("td[name='noTaxRateTd']");
    	var noTaxRateHidden = jQuery(trObj2).find("input[name='noTaxRate']");
    	var jsRate = jQuery(trObj2).find("input[name='jsRate']").val();
    	
    	countFields.change(function(){
    		var countVal = countFields.val();
        	var stdPriceVal = stdPriceFields.val();
        	var sppPriceVal = sppPriceFields.val();
        	//设置折扣
			discountField.val(caculator.floatDiv(sppPriceVal,stdPriceVal));
			//订单总额
			var subTotalSet = caculator.floatMul(sppPriceVal,countVal);
			subtotalField.val(subTotalSet);
			//未税金额 = 小计/(1+税率)
			var jsRatePer = caculator.floatDiv(jsRate,100);
			var noTaxRateMoney = caculator.floatDiv(subTotalSet,caculator.floatAdd(1,jsRatePer));
			noTaxRateTd.html(noTaxRateMoney);
			noTaxRateHidden.val(noTaxRateMoney);
			//计算订单信息
			purchaseOrderFun.caculatePageInfo(iframeId);
    	});
    	stdPriceFields.change(function(){
    		var countVal = countFields.val();
        	var stdPriceVal = stdPriceFields.val();
        	var sppPriceVal = sppPriceFields.val();
        	//设置折扣
        	discountField.val(caculator.floatDiv(sppPriceVal,stdPriceVal));
        	//设置订单总额
			var subTotalSet = caculator.floatMul(sppPriceVal,countVal);
    		subtotalField.val(subTotalSet);
    		//未税金额 = 小计/(1+税率)
    		var jsRatePer = caculator.floatDiv(jsRate,100);
			var noTaxRateMoney = caculator.floatDiv(subTotalSet,caculator.floatAdd(1,jsRatePer));
			noTaxRateTd.html(noTaxRateMoney);
			noTaxRateHidden.val(noTaxRateMoney);
    		//计算订单信息
			purchaseOrderFun.caculatePageInfo(iframeId);
    	});
    	sppPriceFields.change(function(){
    		var countVal = countFields.val();
        	var stdPriceVal = stdPriceFields.val();
        	var sppPriceVal = sppPriceFields.val();
        	//设置折扣
			discountField.val(caculator.floatDiv(sppPriceVal,stdPriceVal));
			//设置订单总额
			var subTotalSet = caculator.floatMul(sppPriceVal,countVal);
			subtotalField.val(subTotalSet);
			//未税金额 = 小计/(1+税率)
			var jsRatePer = caculator.floatDiv(jsRate,100);
			var noTaxRateMoney = caculator.floatDiv(subTotalSet,caculator.floatAdd(1,jsRatePer));
			noTaxRateTd.html(noTaxRateMoney);
			noTaxRateHidden.val(noTaxRateMoney);
			//计算订单信息
			purchaseOrderFun.caculatePageInfo(iframeId);
    	});
    	
    	discountField.change(function(){
    		var countVal = countFields.val();
        	var stdPriceVal = stdPriceFields.val();
        	var sppPriceVal = sppPriceFields.val();
        	var discountVal = discountField.val();
        	//设置供货价
        	sppPriceVal = caculator.floatMul(discountVal,stdPriceVal);
        	sppPriceFields.val(sppPriceVal);
        	//设置订单总额
        	var subTotalSet = caculator.floatMul(sppPriceVal,countVal);
			subtotalField.val(subTotalSet);
			//未税金额 = 小计/(1+税率)
			var jsRatePer = caculator.floatDiv(jsRate,100);
			var noTaxRateMoney = caculator.floatDiv(subTotalSet,caculator.floatAdd(1,jsRatePer));
			noTaxRateTd.html(noTaxRateMoney);
			noTaxRateHidden.val(noTaxRateMoney);
			//计算订单信息
			purchaseOrderFun.caculatePageInfo(iframeId);
    	});
    },
	/**
	 * 计算页面部分信息
	 */
    caculatePageInfo:function(iframeId) {
		var countObjs = jQuery("#"+iframeId).contents().find("input[name='count']");
		var subTotalObjs = jQuery("#"+iframeId).contents().find("input[name='subtotal']");
		var totalCount = 0;
		var subTotal = 0;
		for(var i=0;i<countObjs.length;i++) {
			var cVal = jQuery(countObjs[i]).val();
			totalCount = caculator.intAdd(totalCount,cVal);
		}
		for(var i=0;i<subTotalObjs.length;i++){
			var cVal = jQuery(subTotalObjs[i]).val();
			subTotal = caculator.floatAdd(subTotal,cVal);
		}
		jQuery("#"+iframeId).contents().find("#itemCountSum").val(totalCount);
		jQuery("#"+iframeId).contents().find("#itemAmountSum").val(subTotal);
	}
};

/**
 * 采购订单event
 * 
 * @param genType
 * @param iframeId
 * @param pageii
 */
function purchaseOrderEvent_11(genType,iframeId,pageii) {
	
	/**
	 * 设置商品信息
	 */
	function setItemInfo(itemObj) {
		var standardPrice = itemObj.starndardPrice;
	    var supplierPrice = itemObj.supplyPrice;
		//var rateDefault = jQuery("#rateSel").val();
		jQuery("#brandName").val(itemObj.brandName);
		jQuery("#daleiName").val(itemObj.bigCatName);
		jQuery("#midCategoryName").val(itemObj.midCatName);
		jQuery("#smaillCName").val(itemObj.smallCatName);
		jQuery("#unitName").val(itemObj.unitName);
		jQuery("#boxProp").val(itemObj.cartonSpec);
		jQuery("#productProp").val(itemObj.g);
		jQuery("#productName").val(itemObj.skuName);
		jQuery("#productNameTd").html(itemObj.skuName);
		jQuery("#noTaxAmount").val("");
		jQuery("#prop1").val(itemObj.propValue1);
		jQuery("#prop2").val(itemObj.propValue2);
		jQuery("#prop3").val(itemObj.propValue3);
		jQuery("#standardPrice").val(standardPrice);
		jQuery("#orderPrice").val(supplierPrice);
		jQuery("#barCodeSel").val(itemObj.barcode);
		jQuery("#skuCodeSel").val(itemObj.sku);
		jQuery("#batchNumber").val(itemObj.batchNumber);
		jQuery("#wavesSign").val(itemObj.wavesSign);
	}
	
	/**
	 * 重置页面商品信息
	 */
	function resetItemInfo(){
		var supplierId = jQuery("#supplierId").val();
		jQuery("#product_pop_form")[0].reset();
		jQuery("#supplierId").val(supplierId);
	}
	
	/**
	 * 校验sku是否存在
	 */
	function checkSkuExists(skuCodeSel){
		var inputSkus = jQuery("#"+iframeId).contents().find("input[name='skuCode']");
		for(var i=0;i<inputSkus.length;i++){
			if(skuCodeSel == jQuery(inputSkus[i]).val()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 添加采购单商品行
	 */
	function addProductLine(target1,target2,index) {
		var skuCode = jQuery("#skuCodeSel").val();
		if(checkSkuExists(skuCode)){
			alertMsg('sku:'+skuCode+'已经存在！无法重复添加。');
			return;
		}
		var standardPrice = jQuery("#standardPrice").val();
		var supplierPrice = jQuery("#orderPrice").val();
		//小计
		var count = jQuery("#count").val(); //默认为1
		if(!count){
			count = 1;
		}
		var subTotal = caculator.floatMul(count,supplierPrice);
		var discountValInput = caculator.floatDiv(supplierPrice,standardPrice);
		//未税金额 = 小计/(1+税率)
		var jsRate = jQuery("#jsRateSel").val();
		var jsRatePer = caculator.floatDiv(jsRate,100);
		var noTaxRate = caculator.floatDiv(subTotal,caculator.floatAdd(1,jsRatePer));
		
		var orderItemArr = new Array();
		orderItemArr.push('<tr align="center" id="addQuotaItemTr_'+index+'">');
		orderItemArr.push('<td class="_indexTd"></td>');
		orderItemArr.push('<td><input type="hidden" value="'+skuCode+'" name="skuCode" />'+skuCode+'</td>');
		orderItemArr.push('<td>'+jQuery("#barCodeSel").val()+'</td>');
		orderItemArr.push('<td>'+jQuery("#productName").val()+'</td>');
		orderItemArr.push('<td><input type="hidden" value="'+jQuery("#unitName").val()+'" name="unitName">'+jQuery("#unitName").val()+'</td>');
		orderItemArr.push('<td><input type="hidden" value="'+jQuery("#prop1").val()+'" name="prop1">'+jQuery("#prop1").val()+'</td>');
		orderItemArr.push('<td><input type="hidden" value="'+jQuery("#prop2").val()+'" name="prop2">'+jQuery("#prop2").val()+'</td>');
		orderItemArr.push('<td><input type="hidden" value="'+jQuery("#prop3").val()+'" name="prop3">'+jQuery("#prop3").val()+'</td>');
		orderItemArr.push('<td><input type="text" class="_req _intnum input-text lh30" name="count" value="'+jQuery("#count").val()+'" size="5" /></td>');
		orderItemArr.push('<td><input type="text" class="_req _price input-text lh30" name="standardPrice" value="'+standardPrice+'" size="5" /> </td>');
		orderItemArr.push('<td><input type="text" class="_req _price input-text lh30" name="discount" value="'+discountValInput+'" size="5" /></td>');
		orderItemArr.push('<td><input type="text" class="_req _price input-text lh30" name="orderPrice" value="'+supplierPrice+'" size="5" /></td>');
		orderItemArr.push('<td><input type="text" class="_req _price input-text lh30" name="subtotal" readOnly="readOnly" value="'+subTotal+'" size="5" /></td>');
		orderItemArr.push('<td></td>');
		orderItemArr.push('<td><input type="button" class="ext_btn" onclick="deleteThisProductTr('+index+')" value="删除"></td>');
		orderItemArr.push('</tr>');
		var lineHtml = orderItemArr.join("");
		jQuery(lineHtml).prependTo(target1);
		
		var orderItem2 = new Array();
		orderItem2.push('<tr align="center" id="addQuotaItemTr2_'+index+'">');
		orderItem2.push('<td  class="_indexTd2"></td>');
		orderItem2.push('<td>'+skuCode+'</td>');
		orderItem2.push('<td>'+jQuery("#productName").val()+'</td>');
		orderItem2.push('<td>'+jQuery("#brandName").val()+'</td>');
		orderItem2.push('<td>'+jQuery("#daleiName").val()+'</td>');
		orderItem2.push('<td>'+jQuery("#midCategoryName").val()+'</td>');
		orderItem2.push('<td>'+jQuery("#smaillCName").val()+'</td>');
		orderItem2.push('<td>'+jQuery("#batchNumber").val()+'</td>');
		orderItem2.push('<td>'+purchaseRateSel+'</td>');
		if(jQuery("#wavesSign").val()=="2"){//海淘商品
			orderItem2.push('<td>'+tariffRateSel+'</td>');
		}else{
			orderItem2.push('<td><input type="hidden" name="tariffRate" value=""/></td>');
		}
		orderItem2.push('<td name="noTaxRateTd">'+noTaxRate+'</td>');
		orderItem2.push('<td></td>');
		orderItem2.push('<td><input type="text" size="10" class="input-text lh30" name="productDesc"/></td>');		
		orderItem2.push('<input type="hidden" value="'+noTaxRate+'" name="noTaxRate" />');
		orderItem2.push('<input type="hidden" value="'+jsRate+'" name="jsRate" />');
		orderItem2.push('</tr>');
		var lineHtml2 = orderItem2.join("");
		jQuery(lineHtml2).prependTo(target2);
		alertMsg('新增成功，请输入下一条。');
	}
	
	/**
	 * 商品barcode
	 */
	jQuery("#order_item_dd_barcode_confirm").click(function(){
		var supplierId = jQuery("#supplierId").val();
		var barCode = jQuery("#barCodeSel").val();
		if(!barCode){
			alertMsg('请填写条码信息。');
			return;
		}
		// by zhs 01131719 修改url。原url: '/supplier/client/getProductInfo.htm'
		jQuery.ajax({
			type: 'post',
			url: domain+'/supplier/client/getProductInfo.htm',
			data:{"supplierId":supplierId,"barCode":barCode},
			dataType:"json",
			success: function(data){
				if(data!=null && data.barcode!=null){
					setItemInfo(data);
				} else {
					alertMsg("该供应商下无此条码信息。");
				}
			}
		})
	});
	
	jQuery("#order_item_dd_sku_confirm").click(function(){
		var supplierId = jQuery("#supplierId").val();
		var skuCode = jQuery("#skuCodeSel").val();
		if(!skuCode){
			alertMsg('请填写SKU信息。');
			return;
		}
		// by zhs 01131720 修改url。原url: '/supplier/client/getProductInfo.htm'
		jQuery.ajax({
			type: 'post',
			url: domain+'/supplier/client/getProductInfo.htm',
			data:{"supplierId":supplierId,"skuCode":skuCode},
			dataType:"json",
			success: function(data){
				if(data!=null && data.barcode!=null){
					setItemInfo(data);
				} else {
					alertMsg("该供应商下无此SKU信息。");
				}
			}
		})
	});
	
	jQuery("#productPopConfirm").click(function(){
		var retVal= true;
		var priceObj = jQuery("input._price");
		jQuery("font._errorMsg").remove();
		//校验价格
		priceObj.each(function(key,obj){
			if(!checkPrice(jQuery(obj).val())){
				var errorMsg = "<font class='_errorMsg' color='red'>此项内容必须为数字，如：12.3</font>";
				//错误信息展示
				jQuery(obj).after(jQuery(errorMsg));
				retVal = false;
			}
		});
		if(!retVal){
			return;
		}
		var target1 = jQuery("#"+iframeId).contents().find("#sp_order_table_body_1");
		var target2 = jQuery("#"+iframeId).contents().find("#sp_order_table_body_2");
		var index = jQuery("#"+iframeId).contents().find("#indexNumHiddenId").val();
		var bName = jQuery("#brandName").val();
		if(!bName) {
			alertMsg('请填写skucode或者条形码，并点击右侧【确认】按钮');
			return;
		}
		addProductLine(target1,target2,index);
		resetItemInfo();
		refreshIndex(iframeId);
		purchaseOrderFun.registeOneLineEvent(iframeId,index);
		purchaseOrderFun.caculatePageInfo(iframeId);
		registeValidateEvent(iframeId);
		jQuery("#"+iframeId).contents().find("#indexNumHiddenId").val(parseInt(index)+1);
	});
	
	jQuery("#productPopCancel").click(function(){
		layer.close(pageii);
	    jQuery("#layAllDiv").hide();
	});
}

/**
 * 采购退货单退货相关事件函数
 */
var purchaseBackFnMap = {
	registeOneLineEvent:function(index,iframeId){
    	var trObj = jQuery("#"+iframeId).contents().find("tr[id='addQuotaItemTr_"+index+"']");
    	var trObj2 = jQuery("#"+iframeId).contents().find("tr[id='addQuotaItemTr2_"+index+"']");
    	var stdPriceFields = jQuery(trObj).find("input[name='standardPrice']");
    	var sppPriceFields = jQuery(trObj).find("input[name='orderPrice']");
    	var discountField = jQuery(trObj).find("input[name='discount']");
    	var subtotalField = jQuery(trObj).find("input[name='subtotal']");
    	var countFields = jQuery(trObj).find("input[name='count']");
    	
    	var noTaxRateTd = jQuery(trObj2).find("td[name='noTaxRateTd']");
    	var noTaxRateHidden = jQuery(trObj2).find("input[name='noTaxRate']");
    	var jsRate = jQuery(trObj2).find("input[name='jsRate']").val();
		// by zhs 0122 
		var storageCountField = jQuery(trObj).find("input[name='storageCount']");
    	var numberReturnsField = jQuery(trObj).find("input[name='numberReturns']");
		////////////////////////////////
    	countFields.change(function() {
    		var countVal = countFields.val();
        	var stdPriceVal = stdPriceFields.val();
        	var sppPriceVal = sppPriceFields.val();
			// by zhs 0122 确认退货数量是否大于可退货数据			
			var storageCountVal = storageCountField.val();
			var numberReturnsVal = numberReturnsField.val();
			if(countVal > (storageCountVal-numberReturnsVal)){
				alertMsg('最大可退数量为' + (storageCountVal-numberReturnsVal) );
				countVal = storageCountVal-numberReturnsVal;
				countFields.val(countVal);
			}
			//////////////////////////////////////
			
        	//设置折扣
        	discountField.val(caculator.floatDiv(sppPriceVal,stdPriceVal));
        	//设置小计
        	var subTotalSet = caculator.floatMul(sppPriceVal,countVal);
        	subtotalField.val(subTotalSet);
        	//计算订单总额信息
        	//未税金额 = 小计/(1+税率)
			var jsRatePer = caculator.floatDiv(jsRate,100);
			var noTaxRateMoney = caculator.floatDiv(subTotalSet,caculator.floatAdd(1,jsRatePer));
			noTaxRateTd.html(noTaxRateMoney);
			noTaxRateHidden.val(noTaxRateMoney);
			//计算总额
        	purchaseBackFnMap.caculatePageInfo(iframeId);
    	});
	},
	caculatePageInfo:function(iframeId){
		var countObjs = jQuery("#"+iframeId).contents().find("input[name='count']");
		var subTotalObjs = jQuery("#"+iframeId).contents().find("input[name='subtotal']");
//		var subTotalBeforeObjs = jQuery("#"+iframeId).contents().find("input[name='subtotalBefore']");
		var totalCount = 0;
		var subTotal = 0;
		var subTotalBefore = 0;
		for(var i=0;i<countObjs.length;i++){
			var cVal = jQuery(countObjs[i]).val();
			totalCount = caculator.intAdd(totalCount,cVal);
		}
		for(var i=0;i<subTotalObjs.length;i++){
			var cVal = jQuery(subTotalObjs[i]).val();
			subTotal = caculator.floatAdd(subTotal,cVal);
		}
//		for(var i=0;i<subTotalBeforeObjs.length;i++){
//			var cVal = jQuery(subTotalBeforeObjs[i]).val();
//			subTotalBefore = caculator.floatAdd(subTotalBefore,cVal);
//		}
		jQuery("#"+iframeId).contents().find("#totalReturn").val(totalCount);
		jQuery("#"+iframeId).contents().find("#totalMoneyReturn").val(subTotal);
		jQuery("#"+iframeId).contents().find("#totalMoney").val("0");
	}
};

var orderItemSelEvent_12 = function(genType,iframeId,pageii) {
	
	/**
	 * 添加商品行
	 */
	function addProductLine(target1,target2,id,index){
		var skuCode = jQuery("#skuCodeSel"+id).val();
		if(checkSkuExists(iframeId,id)){
			return;
		}
		var standardPrice = jQuery("#standardPrice"+id).val();
		var supplierPrice = jQuery("#orderPrice"+id).val();
		//小计  默认退一件  
		var subTotal = caculator.floatAdd(0,supplierPrice);
		
		//未税金额 = 小计/(1+税率)
		var jsRate = jQuery("#jsRateSel").val();
		var jsRatePer = caculator.floatDiv(jsRate,100);
		var noTaxRate = caculator.floatDiv(subTotal,caculator.floatAdd(1,jsRatePer));
		var tariffRate = jQuery("#tariffRate"+id).val();
		if(tariffRate){
			tariffRate = tariffRate+"%";
		}
		
		var orderItemArr = new Array();
		orderItemArr.push('<tr align="center" id="addQuotaItemTr_'+index+'">');
		orderItemArr.push('<td class="_indexTd"></td>');
		orderItemArr.push('<td><input type="hidden" id="skuCode'+id+'" value="'+skuCode+'" name="skuCode" />'+skuCode+'</td>');
		orderItemArr.push('<td>'+jQuery("#barcodeSel"+id).val()+'</td>');
		orderItemArr.push('<td>'+jQuery("#productName"+id).val()+'</td>');
		orderItemArr.push('<td><input type="hidden" value="'+jQuery("#unitName"+id).val()+'" name="unitName">'+jQuery("#unitName"+id).val()+'</td>');
		orderItemArr.push('<td><input type="hidden" value="'+jQuery("#prop1"+id).val()+'" name="prop1">'+jQuery("#prop1"+id).val()+'</td>');
		orderItemArr.push('<td><input type="hidden" value="'+jQuery("#prop2"+id).val()+'" name="prop2">'+jQuery("#prop2"+id).val()+'</td>');
		orderItemArr.push('<td><input type="hidden" value="'+jQuery("#prop3"+id).val()+'" name="prop3">'+jQuery("#prop3"+id).val()+'</td>');
		orderItemArr.push('<td><input type="text" class="_req _intnum input-text lh30" name="count" value="1" size="5" /></td>');
		orderItemArr.push('<td><input type="text" class="_req _price input-text lh30" readOnly="readOnly" name="standardPrice" value="'+standardPrice+'" size="5" /> </td>');
		orderItemArr.push('<td><input type="text" class="_req _price input-text lh30" readOnly="readOnly" name="discount" value="'+jQuery("#discount"+id).val()+'" size="5" /> </td>');
		orderItemArr.push('<td><input type="text" class="_req _price input-text lh30" readOnly="readOnly" name="orderPrice" value="'+supplierPrice+'" size="5" /></td>');
		orderItemArr.push('<td><input type="text" class="_req _price input-text lh30" readOnly="readOnly" name="subtotal" value="'+subTotal+'" size="5" /><input type="hidden" name="subtotalBefore" value="'+jQuery("#subtotal"+id).val()+'" /></td>');
		orderItemArr.push('<td>'+jQuery("#numberReturns"+id).val()+'</td>');
		orderItemArr.push('<td><input type="button" class="ext_btn" onclick="deleteThisProductTr('+index+')" value="删除">');
		orderItemArr.push('<input type="hidden" name="originId" value="'+jQuery("#orderIdSel"+id).val()+'"><input type="hidden" name="originProductId" value="'+id+'"></td>');
		orderItemArr.push('<input type="hidden" name="batchNumber" value="'+jQuery("#batchNumber"+id).val()+'">');
		// by zhs 0122 保存 采购数据storageCount, 已退货数据numberReturns
		orderItemArr.push('<input type="hidden" name="storageCount" value="'+jQuery("#storageCount"+id).val()+'">');
		orderItemArr.push('<input type="hidden" name="numberReturns" value="'+jQuery("#numberReturns"+id).val()+'">');		
		//////////////////
		orderItemArr.push('</tr>');
		var lineHtml = orderItemArr.join("");
		jQuery(lineHtml).prependTo(target1);
		
		var orderItem2 = new Array();
		orderItem2.push('<tr align="center" id="addQuotaItemTr2_'+index+'">');
		orderItem2.push('<td  class="_indexTd2"></td>');
		orderItem2.push('<td>'+skuCode+'</td>');
		orderItem2.push('<td>'+jQuery("#productName"+id).val()+'</td>');
		orderItem2.push('<td>'+jQuery("#brandName"+id).val()+'</td>');
		orderItem2.push('<td>'+jQuery("#daleiName"+id).val()+'</td>');
		orderItem2.push('<td>'+jQuery("#midCategoryName"+id).val()+'</td>');
		orderItem2.push('<td>'+jQuery("#smaillCName"+id).val()+'</td>');
		orderItem2.push('<td>'+jQuery("#batchNumber"+id).val()+'</td>');
		orderItem2.push('<td><input type="hidden" value="'+jQuery("#purchaseRate"+id).val()+'" name="purchaseRate" />'+jQuery("#purchaseRate"+id).val()+'%</td>');
		orderItem2.push('<td><input type="hidden" value="'+jQuery("#tariffRate"+id).val()+'" name="tariffRate" />'+tariffRate+'</td>');
		orderItem2.push('<td name="noTaxRateTd">'+noTaxRate+'</td>');
		orderItem2.push('<td>'+jQuery("#orderIdSel"+id).val()+'</td>');
		orderItem2.push('<td><input type="text" size="10" class="input-text lh30" name="productDesc"/></td>');		
		orderItem2.push('<input type="hidden" value="'+noTaxRate+'" name="noTaxRate" />');
		orderItem2.push('<input type="hidden" value="'+jsRate+'" name="jsRate" />');
		orderItem2.push('</tr>');
		var lineHtml2 = orderItem2.join("");
		jQuery(lineHtml2).prependTo(target2);
	}
	
	/**
	 * 校验sku是否存在
	 */
	function checkSkuExists(iframeId,id){
		var skuSelVal = jQuery("#"+iframeId).contents().find("input[id='skuCode"+id+"']").val();
		if(skuSelVal){
			return true;
		}
		return false;
	}
	
	//注册按钮事件
	// by zhs 01131759 修改url。原url: actionUrl
	jQuery("#orderListQuery").click(function(){
		var actionUrl = jQuery("#order_search_form").attr("action");
		jQuery.ajax({
			type: 'post',
			url: domain + actionUrl,
			data:jQuery("#order_search_form").serialize(),
			dataType:"text",
			success: function(data){
				try {
					var endIndex = data.toString().lastIndexOf("<!--");
				    var len = data.toString().length;
				    var endTag = data.toString().substring(len-3,len);
				    if('-->'==endTag){
				    	data = data.toString().substring(0,endIndex);
				    }
				} catch(e){
				}
				jQuery("#tableShowArea").html(data);
				refreshPage(genType,iframeId,pageii);
			}
		})
	});
	
	//注册确定按钮
	jQuery("#orderListConfirm").click(function() {
		//拿到所有的checkbox  获取name add
		var target1 = jQuery("#"+iframeId).contents().find("#sp_order_table_body_1");
		var target2 = jQuery("#"+iframeId).contents().find("#sp_order_table_body_2");
		var checkBoxSel = jQuery("input[name='orderIdSel']:checked");
		for(var i=0;i<checkBoxSel.length;i++){
			var idIndex = jQuery(checkBoxSel[i]).val();
			var index = jQuery("#"+iframeId).contents().find("#indexNumHiddenId").val();
			addProductLine(target1,target2,idIndex,index);
			purchaseBackFnMap.registeOneLineEvent(index,iframeId);
			jQuery("#"+iframeId).contents().find("#indexNumHiddenId").val(parseInt(index)+1);
		}
		registeValidateEvent(iframeId);
		refreshIndex(iframeId);
		purchaseBackFnMap.caculatePageInfo(iframeId);
		layer.close(pageii);
	    jQuery("#layAllDiv").hide();
	});
}


var depositchangeEvent_13=function(genType,iframeId,pageii){

	/**计算押金变更的值*/
	sumValue();
	
	jQuery("#costconfirm").click(function(){
		var retVal= true;
		var newvalue = jQuery("#newvalue").val();
		var changevalue = jQuery("#changeValue").val();
		if(!newvalue){
			alertMsg('押金值不合法。');
			return;
		}
		jQuery("#"+iframeId).contents().find("#basevalue").val(newvalue);
		jQuery("#"+iframeId).contents().find("#changeValue").val(changevalue);
		layer.close(pageii);
	    jQuery("#layAllDiv").hide();
	})
	
	jQuery("#costCancel").click(function(){
		layer.close(pageii);
	    jQuery("#layAllDiv").hide();
	});
	
}


var pasteEvent_14=function(genType,iframeId,pageii){
	
	function checkPasteInputInfo(pasteInfo,supplierType){
		if(!pasteInfo){
			alertMsg("请输入商品信息。");
			return false;
		}
		pasteInfo = pasteInfo.toString().replace(new RegExp("\n{2,}", "g"), "\n");
		if(pasteInfo.length>1 && '\n' == pasteInfo.substring(pasteInfo.length-1,pasteInfo.length)){
			pasteInfo = pasteInfo.substring(0,pasteInfo.length-1);
		}
		jQuery("#pasteInfos").val(pasteInfo);
		var pasteLine = pasteInfo.split("\n");
		
		var skuArr = [];
		var barCodeArr =[];
		for(var i=0 ;i<pasteLine.length;i++){
			var skuInfoArr = pasteLine[i].split("\t");
			var skuInfo = "";
			if(supplierType=="Purchase"){
				if(typeof(skuInfoArr[5]) == 'undefined'){
					skuInfoArr.push("");
				}
			}
			if(skuInfoArr.length != 6){
				alertMsg("输入格式不对。");
				return false;
			}
			
			if(skuInfoArr[0]){
				skuInfo = skuInfoArr[0];
			} else {
				if(skuInfoArr[1]){
					skuInfo = skuInfoArr[1];
				} else {
					alertMsg("第"+(i+1)+"行SKU和条码全部为空。");
					return false;
				}
			}
			
			if(skuInfoArr[0]){
				barCodeArr.push(skuInfoArr[0]);
			}
			if(skuInfoArr[1]){
				skuArr.push(skuInfoArr[1]);
			}
			
			if(!checkPrice(skuInfoArr[2])){
				alertMsg("商品："+skuInfo+" 市场价格式不正确。");
				return false;
			}

			if(skuInfoArr[3]==""){
				if(supplierType=="Associate"){
					alertMsg("代发商品："+skuInfo+" 建议最低售价不可为空。");
					return;
				}
			}else{
				if(!checkPrice(skuInfoArr[3])){
					alertMsg("商品："+skuInfo+" 销售价格式不正确。");
					return false;
				}
				if(parseFloat(skuInfoArr[3])>parseFloat(skuInfoArr[2])){
					alertMsg("商品："+skuInfo+" 建议最低售价不能大于市场价。");
					return false;
				}
			}
			
			if(skuInfoArr[4].replace(/(^\s*)|(\s*$)/g, "")==""){
				if(supplierType=="Purchase"){
					alertMsg("自营商品："+skuInfo+" 货价不可为空。");
					return;
				}
			}else{
				if(!checkPrice(skuInfoArr[4])){
					alertMsg("商品："+skuInfo+" 供货价格式不正确。");
					return false;
				}
				if(parseFloat(skuInfoArr[4])>parseFloat(skuInfoArr[2])){
					alertMsg("商品："+skuInfo+" 供货价不能大于市场价。");
					return false;
				}
			}

			if(supplierType=="Purchase"){
				if(skuInfoArr[5].replace(/(^\s*)|(\s*$)/g, "")!=""){
					alertMsg("自营商品："+skuInfo+" 无需填写平台使用费。");
					return false;
				}
			}else{
				if(!checkPrice(skuInfoArr[5])){
					alertMsg("商品："+skuInfo+" 平台使用费填写不正确。");
					return false;
				}
				if(parseFloat(skuInfoArr[5])>100){
					alertMsg("商品："+skuInfo+" 平台使用费不能大于100。");
					return false;
				}
			}

		}
		for(var i =0;i<barCodeArr.length-1;i++){
			if (barCodeArr[i] == barCodeArr[i+1] && barCodeArr[i].replace(/(^\s*)|(\s*$)/g, "")!="")
		    {
				alertMsg("行条码不能出现重复");
				return false;
		    }
		}
		for(var i =0;i<skuArr.length-1;i++){
			if (skuArr[i] == skuArr[i+1] && skuArr[i].replace(/(^\s*)|(\s*$)/g, "")!="")
		    {
				alertMsg("行SKU不能出现重复");
				return false;
		    }
		}

		return true;
	}
	
	/**
	 * 校验sku是否存在
	 */
	function checkSkuExists(skuCodeSel){
		var inputSkus = jQuery("#"+iframeId).contents().find("input[name='skuCode']");
		for(var i=0;i<inputSkus.length;i++){
			if(skuCodeSel == jQuery(inputSkus[i]).val()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 添加报价单商品行
	 */
	function addQuotationProductLine(skuInfo,target,index){
		var itemArr = new Array();
		itemArr.push('<tr align="center" id="addQuotaItemTr_'+index+'">');
		itemArr.push('<td class="_indexTd"></td>');
		itemArr.push('<td><input type="hidden" value="'+skuInfo.barcode+'" name="barCode" />'+skuInfo.barcode+'</td>');
		itemArr.push('<td><input type="hidden" value="'+skuInfo.sku+'" name="skuCode" />'+skuInfo.sku+'</td>');
		itemArr.push('<td>'+skuInfo.skuName+'</td>');
		itemArr.push('<td>'+skuInfo.brandName+'</td>');
		itemArr.push('<td>'+skuInfo.bigCatName+'</td>');
		itemArr.push('<td>'+skuInfo.midCatName+'</td>');
		itemArr.push('<td>'+skuInfo.smallCatName+'</td>');
		itemArr.push('<td><input type="hidden" name="unitName" value="'+skuInfo.unitName+'">'+skuInfo.unitName+'</td>');
		itemArr.push('<td><input type="hidden" name="marketPrice" value="'+skuInfo.marketPrice+'">'+skuInfo.marketPrice+'</td>');
		itemArr.push('<td><input type="hidden" name="salePrice" value="'+skuInfo.starndardPrice+'">'+skuInfo.starndardPrice+'</td>');
		itemArr.push('<td><input type="hidden" name="supplyPrice" value="'+skuInfo.supplyPrice+'">'+skuInfo.supplyPrice+'</td>');
		if(skuInfo.commissionPer!=""){
			itemArr.push('<td><input type="hidden" name="commissionPercent" value="'+skuInfo.commissionPer+'">'+skuInfo.commissionPer+'%</td>');
		}else{
			itemArr.push('<td><input type="hidden" name="commissionPercent" value="">---</td>');
		}
		itemArr.push('<td>'+skuInfo.cartonSpec+'</td>');
		itemArr.push('<td>'+skuInfo.specifications+'</td>');
		itemArr.push('<td><input type="button" class="ext_btn" onclick="deleteThisProductTr('+index+')" value="删除"></td>');
		itemArr.push('</tr>');
		var lineHtml = itemArr.join("");
		jQuery(lineHtml).prependTo(target);
//		alertMsg('新增成功');
	}
	
	function addItems(skuVOList){
		var target = jQuery("#"+iframeId).contents().find("#quotation_item_add_body");
		var indexs = [];
		for(var i=0;i<skuVOList.length;i++){
			var skuInfo = skuVOList[i];
			if(skuInfo.pasteSuccessOrFail == 0){
				indexs.push(parseInt(skuInfo.pasteIndex));
				continue;
			}
			if(checkSkuExists(skuInfo.sku)){
				continue;
			}
			var index = jQuery("#"+iframeId).contents().find("#indexNumHiddenId").val();
			addQuotationProductLine(skuInfo,target,index);
			jQuery("#"+iframeId).contents().find("#indexNumHiddenId").val(parseInt(index)+1);
		}
		if(indexs.length>0){
			alertMsg("第"+indexs.sort(function(a,b){return a-b;})+""+"行粘贴失败，请核对");
		}else{
			alertMsg("新增成功");
		}
		
	}
	
	jQuery("#quotation_paste_confirm").click(function(){
		var supplierId = jQuery("#supplierId").val();
		var pasteinfos = jQuery("#pasteInfos").val();
		var supplierType = jQuery("#supplierType").val();
		if(!checkPasteInputInfo(pasteinfos,supplierType)){
			return;
		}
		jQuery.ajax({
			type: 'post',
			url: '/supplier/quotation/pasteInfo.htm',
			data:{"pasteinfos":pasteinfos,"supplierId":supplierId},
			dataType:"text",
			success: function(data){
				try {
					var endIndex = data.toString().lastIndexOf("<!--");
				    var len = data.toString().length;
				    var endTag = data.toString().substring(len-3,len);
				    if('-->'==endTag){
				    	data = data.toString().substring(0,endIndex);
				    }
				} catch(e){
				}
				data = eval('('+data+')');
				if(data.success && data.success != 'false'){
					addItems(data.skuList);
					layer.close(pageii);
					refreshIndex(iframeId);
				} else {
					alertMsg("error！");
				}
			}
		});
	});
	
	jQuery("#quotation_paste_cancel").click(function(){
		layer.close(pageii);
	    jQuery("#layAllDiv").hide();
	});
}

/**
 * 注册弹出的各种方法
 * 
 * @param callBackFn
 */
function registerPopEventWithType(popType,callBackFn){
	supplierRegisteMap[1] = supplierEventFn_1;
	supplierRegisteMap[2] = brandEventFn_2;
	supplierRegisteMap[3] = expressEventFn_3;
	supplierRegisteMap[4] = storageEventFn_4;
	supplierRegisteMap[5] = auditEventFn_5;
	supplierRegisteMap[6] = quotationSupplierFn_6;
	supplierRegisteMap[7] = contractEventFn_7;
	supplierRegisteMap[8] = contractproductEventFn_8;
	supplierRegisteMap[9] = quotationItemEvent_9;
	supplierRegisteMap[10] = storageEventRadio_10;
	supplierRegisteMap[11] = purchaseOrderEvent_11;
	supplierRegisteMap[12] = orderItemSelEvent_12;
	supplierRegisteMap[13] = depositchangeEvent_13;
	supplierRegisteMap[14] = pasteEvent_14;
	supplierRegisteCallBackMap[popType] = callBackFn;
}

/**
 * 页面刷新
 * 
 * @param genType
 * @param iframeId
 * @param pageii
 */
function refreshPage(genType,iframeId,pageii){
	//自设关闭
	$('#popClosebtn').on('click', function(){
	    layer.close(pageii);
	    jQuery("#layAllDiv").hide();
	});
	if(supplierRegisteMap[genType]) {
		supplierRegisteMap[genType](genType,iframeId,pageii);
	}
}

/**
 * 添加品牌行
 */
function addStorageLine(target,sid,sName,bTag) {
	var lineArr = new Array();
	lineArr.push('<tr>');
	lineArr.push('<td><input type="hidden" id="'+bTag+'NameSel_'+sid+'" name="supplier'+bTag+'" value="'+sid+'"/><input type="hidden" id="'+bTag+'Name_'+sid+'" name="supplierA'+bTag+'Sel" value='+sid+' dStatus="1" dataname="'+sName+'" />'+sid+'</td>');
	lineArr.push('<td>'+sName+'</td>');
	lineArr.push('<td id="'+bTag+'spp_add_status_'+sid+'">有效</td>');
	lineArr.push('<td id="sp_'+bTag+'_btn_'+sid+'"><input type="button" onclick="changeStatus_1('+sid+',\''+bTag+'\')" value="失效" class="ext_btn ext_btn_error"></td>');
	lineArr.push('</tr>');
	var lineHtml = lineArr.join("");
	jQuery(lineHtml).prependTo(target);
}

/**
 * 设置物流页面信息
 * 
 * @param checkedData
 * @param iframeId
 * @param pageii
 */
function setexpressTemplateToPage(selRadioVal,expressName,iframeId,pageii){
	jQuery("#"+iframeId).contents().find("#expressTemplateId").val(selRadioVal);
	jQuery("#"+iframeId).contents().find("#expressTemplateName").val(expressName);
	layer.close(pageii);
}

/**
 * 设置页面品牌参数
 * 
 * @param bId
 * @param bName
 * @param iframeId
 * @param pageii
 */
function setBrandToPage(checkedData,iframeId,pageii){
	var alertStr = new Array();;
	var noUseMap = {};
	var addMap = {};
	var spBrandSel = jQuery("#"+iframeId).contents().find("input[name='supplierABrandSel']");
	
	if(spBrandSel.length>0){
		for(var i=0;i<spBrandSel.length;i++){
			var status = jQuery(spBrandSel[i]).attr("dStatus");
			var dName = jQuery(spBrandSel[i]).attr("dataname");
			var dVal = jQuery(spBrandSel[i]).val();
			if(parseInt(status) == 1){
				addMap[dVal] = dName;
			} else {
				noUseMap[dVal] = dName;
			}
		}
	}
	for(var i=0;i<checkedData.length;i++) {
		var status = jQuery(checkedData[i]).attr("dStatus");
		var dName = jQuery(checkedData[i]).attr("dataname");
		var dVal = jQuery(checkedData[i]).val();
		var target = jQuery("#"+iframeId).contents().find("#brandSpanTableId");
		var index = jQuery("#"+iframeId).contents().find("#fieldIdDefined").val();
		addBrandLine(index,target,dVal,dName);
		jQuery("#"+iframeId).contents().find("#fieldIdDefined").val(parseInt(index)+1);
		layer.close(pageii);
	}
}

/**
 * 添加品牌行
 */
function addBrandLine(index,target,bid,bName){
	var lineArr = new Array();
	lineArr.push('<tr>');
	lineArr.push('<td><input type="hidden" id="sBNameSel_'+index+'" name="supplierBrand" value="'+bid+'"/><input type="hidden" id="sBName_'+index+'" name="supplierABrandSel" value='+bid+' dStatus="1" dataname="'+bName+'" />'+bid+'</td>');
	lineArr.push('<td>'+bName+'</td>');
	//category start
	lineArr.push('<td>');
	lineArr.push('<div class="select_border">');
	lineArr.push('<div class="select_containers">');
	lineArr.push('<span class="fl">');
	lineArr.push('<select class="select" style="width:140px;" id="supplierCategoryBig_'+index+'" name="categoryDalei">');
	lineArr.push('<option value="">大类</option>');
	lineArr.push('</select>');
	lineArr.push('</span>');
	lineArr.push('</div>');
	lineArr.push('</div>');
	lineArr.push('</td>');
	lineArr.push('<td>');
	lineArr.push('<div class="select_border">');
	lineArr.push('<div class="select_containers">');
	lineArr.push('<span class="fl">');
	lineArr.push('<select class="select" style="width:140px;" id="supplierCategoryMid_'+index+'" name="supplierCategoryMid">');
	lineArr.push('<option value="">中类</option>');
	lineArr.push('</select>');
	lineArr.push('</span>');
	lineArr.push('</div>');
	lineArr.push('</div>');
	lineArr.push('</td>');
	lineArr.push('<td>');
	lineArr.push('<div class="select_border">');
	lineArr.push('<div class="select_containers">');
	lineArr.push('<span class="fl">');
	lineArr.push('<select class="select" style="width:140px;" id="supplierCategorySmall_'+index+'" name="supplierCategorySmall">');
	lineArr.push('<option value="">小类</option>');
	lineArr.push('</select>');
	lineArr.push('</span>');
	lineArr.push('</div>');
	lineArr.push('</div>');
	lineArr.push('</td>');
	//category end
	lineArr.push('<td id="spp_add_status_'+index+'">有效</td>');
	lineArr.push('<td id="sp_brand_btn_'+index+'"><input type="button" onclick="changeStatus_1('+index+','+bid+')" value="失效" class="ext_btn ext_btn_error"></td>');
	lineArr.push('</tr>');
	var lineHtml = lineArr.join("");
	jQuery(lineHtml).prependTo(target);
	registeCategoryCascade(target,index);
}

/**
 * 生成页面数据
 */
function generateData(htmlObj,genType,iframeId,pageii){
	if(htmlObj){
		jQuery("#tableShowArea").html(htmlObj);
	}
	refreshPage(genType,iframeId,pageii);
}

/**
 * 校验价格
 * 
 * @param txt
 * @returns
 */
function checkPrice(txt){
	var reg = /^(\d{1,10})+(\.\d{0,2})?$/;
	return reg.test(txt);
}
/**
 * 校验税率
 *
 * @param txt
 * @returns
 */
function checkTexRate(txt){
	var reg = /^(\d{1,10})+(\.\d{0,4})?$/;
	return reg.test(txt);
}

/**
 * 校验价格
 * 
 * @param txt
 * @returns
 */
function checkIntNum(txt){
	var reg = /^\d{0,10}?$/;
	return reg.test(txt);
}

/**
 * 显示等待遮罩
 */
function supplierWaitDiv(){
	//jQuery("#layAllDiv").show();
	/*var pageii = $.layer({
	    type: 1,
	    title: false,
	    area: ['auto', 'auto'],
	    border: [0], //去掉默认边框
	    shade: [0], //去掉遮罩
	    closeBtn: [0, false], //去掉默认关闭按钮
	    shift: 'left', //从左动画弹出
	    page: {
	        html: '<div style="margin-left:-50%;margin-top:-15%;"><img src="/statics/supplier/images/xubox_loading2.gif"></div>'
	    }
	});*/
}

var popWaitDivMap = {
	showWaitDiv:function(){
		var lay = layer.load('请稍候');
		popWaitDivMap["waitDiv"] = lay;
	},
	hideWaitDiv:function(){
		if(popWaitDivMap["waitDiv"]){
			layer.close(popWaitDivMap["waitDiv"]);
		}
	}
};

/**
 * 图片div展示
 * 
 * @param file
 */
function showImageDiv(file,imgUrl){
	//var parentFrameID = self.frameElement.getAttribute('id');
	//jQuery("#layAllDiv").show();
	//var imageHtml = "<img src='"+imgUrl+"'>";
	var pageii = $.layer({
	    type: 1,
	    title: false,
	    area: ['650px', 'auto'],
	    border: [0], //去掉默认边框
	    shadeClose: true,
	    closeBtn:[0],
	    shift: 'left', //从左动画弹出
	    page: {
	    	// style="width:420px; height:260px; padding:20px; border:1px solid #ccc; background-color:#eee;"
	        html: '<div style="margin-top:-23.5%;"><p id="imgShowArea"></p></div>'
	    	//html: '<div class="xubox_setwin" style="margin-left:-50%;margin-top:-23.5%;"><a style="" href="javascript:;" class="xubox_close xulayer_png32 xubox_close0 xubox_close1"></a><p id="imgShowArea"></p></div>'
	    }
	});
	if(file){
		previewImage(file,jQuery("#imgShowArea"));
	} else if(imgUrl){
		jQuery("#imgShowArea").html("<img width='650' height='650' src='"+imgUrl+"'/>");
	} else {
		previewImage(file,jQuery("#imgShowArea"));
	}
	//自设关闭
	$('#imgPopShowbtn').on('click', function(){
	    layer.close(pageii);
	    jQuery("#layAllDiv").hide();
	});
	return false;
}

/**
 * pdf预览
 */
function showPdf(url){
	var pageii = $.layer({
	    type: 1,
	    title: "预览",
	    area: ['1000px', 'auto'],
	    border: [0], //去掉默认边框
	    shadeClose: true,
	    closeBtn:[1,true],
	    shift: 'left', //从左动画弹出
	    page: {
	    	// style="width:420px; height:260px; padding:20px; border:1px solid #ccc; background-color:#eee;"
	        html: '<div style="width:1000px; height:700px;" ><p id="pdfShowArea" style="margin-top:-36px;margin-bottom:0px;height:700px;"></p></div>'
	    	//html: '<div class="xubox_setwin" style="margin-left:-50%;margin-top:-23.5%;"><a style="" href="javascript:;" class="xubox_close xulayer_png32 xubox_close0 xubox_close1"></a><p id="imgShowArea"></p></div>'
	    }
	});
	var pdfObj = new PDFObject({ url:url ,pdfOpenParams: {},id:"contract_prev_show"}).embed("pdfShowArea");
}
	

