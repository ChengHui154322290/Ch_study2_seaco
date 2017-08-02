jQuery(document).ready(function(){
	registeElementEvent();
	jQuery("#supplier_add_save").click(function(){
		jQuery("font._errorMsg").remove();
		if(!validateInfo()){
			return false;
		}
		
		if(!checkSupplierMulInputField()){
			return;
		}
		
		//校验商家平台
		if(!isSupplierEdit && !checkSupplierUser()){
			return;
		}
		//海淘供应商物流校验
		var isHaitao = jQuery("input[name='isHtSupplier']:checked").val();
		if(1 == parseInt(isHaitao)){
			checkHaitaoInfo(function(express){
				jQuery("#expressTemplateId").val(express.id);
				jQuery("#expressTemplateName").val(express.name);
				//校验品类
				$.ajax({
					url:$('#supp_add_form').attr('action'),
					data:$('#supp_add_form').serialize(),
					dataType:'json',
					type:'post',
					success:function(result){
						if(result.success){
							window.parent.popWaitDivMap.showWaitDiv();
							jQuery("#supplier_add_save").attr("disabled",true);
							window.location.href=domain+"/supplier/toSupplierLicenAdd.htm?spId="+result.data;
						}else{
							alert(result.msg.message);
						}
					}
				})
			});
		} else {
			$.ajax({
				url:$('#supp_add_form').attr('action'),
				data:$('#supp_add_form').serialize(),
				dataType:'json',
				type:'post',
				success:function(result){
					if(result.success){
						window.parent.popWaitDivMap.showWaitDiv();
						jQuery("#supplier_add_save").attr("disabled",true);
						window.location.href=domain+"/supplier/toSupplierLicenAdd.htm?spId="+result.data;
					}else{
						alert(result.msg.message);
					}
				}
			})
		}
	});
	
	var categoryTrs = jQuery("select[id^='supplierCategoryBig_']");
	for(var i=0;i<categoryTrs.length;i++){
		var indexId = jQuery(categoryTrs[i]).attr("id");
		var index = indexId.replace("supplierCategoryBig_","");
		registeCategoryEvent(index);
	}
	var userTrs = jQuery("select[id^='mt_link_bm_']");
	for(var i=0;i<userTrs.length;i++){
		var indexId = jQuery(userTrs[i]).attr("id");
		var index = indexId.replace("mt_link_bm_","");
		registeMtlinker(index);
	}
	
	initSupplierTypeExplanation();
});

/**
 * 校验海淘信息
 */
function checkHaitaoInfo(callBackFn){
	//是海淘供应商 校验物流信息
	var expressTemplateId = jQuery("#expressTemplateId").val();
	if(!expressTemplateId){
		alertMsg('海淘供应商物流模板非空。');
		return;
	}
	ajaxRequest({
		method:'post',
		url:'/supplier/client/checkExpressInfo.htm',
		data:{"expressId":expressTemplateId},
		success:function(data){
			data = eval('('+data+')');
			if(null != data && data.length>0 && data[0].id!=null){
				if(callBackFn){
					callBackFn(data[0]);
				}
			} else {
				alertMsg('物流模板找不到。');
				return;
			}
		}
	})
}

/**
 * 校验供应商多选信息
 */
function checkSupplierMulInputField(){
	//校验主供应商信息
	if(!checkMainSupplierInfo()){
		return false;
	}
	//校验品牌信息
	if(!checkBrandIsNotNull()){
		return false;
	}
	//校验银行信息
	if(!checkBankInfo()){
		return false;
	}
	//校验开票信息
	if(!checkKpInfo()){
		return false;
	}
	//校验供应商联系人信息
	if(!checkSupplinkInfo()){
		return false;
	}
	//校验西客联系人信息
	if(!checkMtLinkers()){
		return false;
	}
	return true;
}

/**
 * 校验主供应商信息
 */
function checkMainSupplierInfo(){
	var mainSupplierName = jQuery("#supplier_query_text").val();
	var mainSupplierId = jQuery("#supplier_add_parent_id").val();
	if(mainSupplierName && !mainSupplierId){
		alertMsg('主供应商信息填写完成后，需点击【确认】按钮才可生效。');
		return false;
	} else {
		return true;
	}
}

/**
 * 校验品牌非空
 * 
 * @returns {Boolean}
 */
function checkBrandIsNotNull(){
	var generateBrandSelId = function(brandId,bigId,midId,smallId){
		var retArr = new Array();
		if(brandId){
			retArr.push("b");
			retArr.push(brandId);
		}
		if(bigId){
			retArr.push("big");
			retArr.push(bigId);
		}
		if(midId){
			retArr.push("mid");
			retArr.push(midId);
		}
		if(smallId){
			retArr.push("smallId");
			retArr.push(smallId);
		}
		return retArr.join("");
	};
	var supplierBrands = jQuery("input[name='supplierABrandSel']");
	if(supplierBrands.length==0){
		alertMsg('请选择品牌信息。');
		return false;
	}
	
	var brandSelArr = new Array();
	//校验有效的品牌非空
	var brandIsNull = true;
	for(var i=0;i<supplierBrands.length;i++){
		var spBrand = supplierBrands[i];
		var status = jQuery(spBrand).attr("dstatus");
		if(1 == parseInt(status)){
			brandIsNull = false;
			brandSelArr.push(jQuery(spBrand).attr("id").replace("sBName_",""));
		}
	}
	if(brandIsNull){
		alertMsg('请选择品牌信息。');
		return false;
	}
	//校验有效的品牌不能重复
	var selectMap = {};
	var repeatBrands = new Array();
	for(var i=0; i<brandSelArr.length; i++){
		var index = brandSelArr[i];
		var brandId = jQuery("#sBName_"+index).val();
		var status = jQuery("#sBName_"+index).attr("dstatus");
		var brandName = jQuery("#sBName_"+index).attr("dataname");
		if(brandId && parseInt(status) == 1){
			var idTag = generateBrandSelId(brandId,$("#supplierCategoryBig_"+index).val(),
					$("#supplierCategoryMid_"+index).val(),$("#supplierCategorySmall_"+index).val());
			if(selectMap[idTag]){
				repeatBrands.push(brandName);
			} else {
				selectMap[idTag] = true;
			}
		}
	}
	if(repeatBrands.length>0){
		alertMsg("品牌："+repeatBrands.join("，")+" 存在相同的分类选择！");
		return false;
	}
	return true;
}


/**
 * 校验分类非空
 */
//function checkCategoryIsNotNull(){
//	var spCategory = jQuery("select[name='supplierCategorySmall']");
//	if(spCategory.length==0){
//		alertMsg('请选择分类信息。');
//		return false;
//	}
//	for(var i=0;i<spCategory.length;i++){
//		var spCategory = spCategory[i];
//		var status = jQuery(spCategory).val();
//		if(status){
//			return true;
//		}
//	}
//	return false;
//}

/**
 * 校验银行信息
 */
function checkBankInfo(){
	var banks = jQuery("input[name='bankName']");
	if(!banks || banks.length==0){
		alertMsg('请填写银行信息。');
		return false;
	} else {
		return true;
	}
}

/**
 * 校验开票信息
 */
function checkKpInfo(){
	var kpInfos = jQuery("input[name='kpName']");
	if(!kpInfos || kpInfos.length==0){
		alertMsg('请填写开票信息。');
		return false;
	} else {
	    var kpaccountname = jQuery("#kpaccountname").val();
	    var kpname = jQuery("#kpname").val();
	    if(kpname != kpaccountname){
	    	alertMsg('开户名称和开票名称要相同。');
	    	return false;
	    }else{
	    	return true;
	    }
		return true;
	}
}

/**
 * 校验供应商联系人信息
 * @returns {Boolean}
 */
function checkSupplinkInfo(){
	var suppLinkInfos = jQuery("input[name='supplierLinkName']");
	if(!suppLinkInfos || suppLinkInfos.length==0){
		alertMsg('请填写供应商联系人信息。');
		return false;
	} else {
		return true;
	}
}

/**
 * 校验仓库信息
 */
function checkStorageInfo(){
	var supplierStorages = jQuery("input[name='supplierAstorageSel']");
	if(supplierStorages.length==0){
		alertMsg('请选择仓库信息。');
		return false;
	}
	for(var i=0;i<supplierStorages.length;i++){
		var spBrand = supplierStorages[i];
		var status = jQuery(spBrand).attr("dstatus");
		if(1 == parseInt(status)){
			return true;
		}
	}
	return false;
}

/**
 * 校验西客对接人信息
 */
function checkMtLinkers(){
	var mtLinks = jQuery("select[name='xgLinkerUserId']");
	if(mtLinks.length==0){
		alertMsg('请选择西客联系人信息。');
		return false;
	}
	for(var i=0;i<mtLinks.length;i++){
		var spMtLink = mtLinks[i];
		var status = jQuery(spMtLink).val();
		if(status){
			return true;
		}
	}
	alertMsg('请选择西客联系人信息。');
	return false;
}

/**
 * 校验商家平台
 */
function checkSupplierUser(){
	var supplierUserpwd = jQuery("#sp_user_password").val();
	if(!supplierUserpwd || '商家平台密码' == supplierUserpwd){
		alertMsg("请输入商家平台密码。");
		return false;
	}
//	var pa=/^(\w){6,17}$/;
	var pa=/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,.\/]).{8,16}$/;
	if( !pa.test(supplierUserpwd)){
		alertMsg("密码必须由 8-16位字母、数字、特殊符号线组成。");
		return false;
	}
	return true;
}
/**
 * 添加分类
 */
function addCategory(){
	var index = jQuery("#fieldIdDefined").val();
	var categoryArr = new Array();
	categoryArr.push('<tr>');
	categoryArr.push('<td>品类：</td>');
	categoryArr.push('<td>');
	categoryArr.push('<div class="select_border">');
	categoryArr.push('<div class="select_containers">');
	categoryArr.push('<span class="fl">');
	categoryArr.push('<select class="select" style="width:140px;" id="supplierCategoryBig_'+index+'" name="categoryDalei">');
	categoryArr.push('<option value="0">大类</option>');
	categoryArr.push('</select>');
	categoryArr.push('</span>');
	categoryArr.push('</div>');
	categoryArr.push('</div>');
	categoryArr.push('</td>');
	categoryArr.push('<td>');
	categoryArr.push('<div class="select_border">');
	categoryArr.push('<div class="select_containers">');
	categoryArr.push('<span class="fl">');
	categoryArr.push('<select class="select" style="width:140px;" id="supplierCategoryMid_'+index+'" name="supplierCategoryMid">');
	categoryArr.push('<option value="-1">中类</option>');
	categoryArr.push('</select>');
	categoryArr.push('</span>');
	categoryArr.push('</div>');
	categoryArr.push('</div>');
	categoryArr.push('</td>');
	categoryArr.push('<td>');
	categoryArr.push('<div class="select_border">');
	categoryArr.push('<div class="select_containers">');
	categoryArr.push('<span class="fl">');
	categoryArr.push('<select class="select" style="width:140px;" id="supplierCategorySmall_'+index+'" name="supplierCategorySmall">');
	categoryArr.push('<option value="">小类</option>');
	categoryArr.push('</select>');
	categoryArr.push('</span>');
	categoryArr.push('</div>');
	categoryArr.push('</div>');
	categoryArr.push('</td>');
    categoryArr.push('<td id="categoryStatusVal_'+index+'">有效</td>');
    categoryArr.push('<td id="categoryStatusBtn_'+index+'"><input type="button" value="失效" onclick="categoryStatusChange_1('+index+')" class="ext_btn ext_btn_error"></td>');
    categoryArr.push('</tr>');
    var categoryHtml = categoryArr.join('');
    jQuery(categoryHtml).appendTo(jQuery("#categoryAddTable"));
    jQuery("#fieldIdDefined").val(parseInt(index)+1);
    registeCategoryEvent(index,true);
	return false;
}

/**
 * 添加银行信息
 */
function addBank(){
	var bankTypeHtml = $('#supplierBankTypes').html();
	var index = jQuery("#fieldIdDefined").val();
	var bankArr = new Array();
	bankArr.push('<tr id="bankTr_'+index+'">');
	bankArr.push('<td>');
	bankArr.push('<div class="select_border">');
	bankArr.push('<div class="select_containers">');
	bankArr.push('<span class="fl">');
	bankArr.push('<select class="_req select" style="width:100px;" name="bankType">');
	bankArr.push(bankTypeHtml);
	bankArr.push('</select>');
	bankArr.push('</span>');
	bankArr.push('</div>');
	bankArr.push('</div>');
	bankArr.push('</td>');
	bankArr.push('<td>银行名称：');
	bankArr.push('<input type="text" name="bankName" maxlength="60" class="_req input-text lh30" size="15">');
	bankArr.push('</td>');
	bankArr.push('<td>银行账号：');
	bankArr.push('<input type="text" name="bankAccount" maxlength="60" class="_req input-text lh30" size="15">');
	bankArr.push('</td>');
	bankArr.push('<td>开户名称：');
	bankArr.push('<input type="text" name="bankAccName" maxlength="60" class="_req input-text lh30" size="15">');
	bankArr.push('</td>');
	bankArr.push('<td>');
	bankArr.push('<div class="select_border">');
	bankArr.push('<div class="select_containers">');
	bankArr.push('<span class="fl">币种：');
	bankArr.push('<select class="_req select" style="width:100px;" name="bankCurrency">');
	bankArr.push('<option value="CNY">人民币</option>');
	bankArr.push('<option value="USD">美元</option>');
	bankArr.push('<option value="EUR">欧元</option>');
	bankArr.push('<option value="GBP">英镑</option>');
	bankArr.push('<option value="HKD">港币</option>');
	bankArr.push('<option value="JPY">日币</option>');
	bankArr.push('<option value="KER">韩币</option>');
	bankArr.push('</select>');
	bankArr.push('</span>');
	bankArr.push('</div>');
	bankArr.push('</div>');
	bankArr.push('</td>');
	bankArr.push('<td><input type="button" class="ext_btn" onclick="deleteBrand('+index+')" value="删除"></td>');
	bankArr.push('</tr>')
	var bankHtml = bankArr.join('');
    jQuery(bankHtml).appendTo(jQuery("#bankChooseTable"));
    jQuery("#fieldIdDefined").val(parseInt(index)+1);
}

/**
 * 删除银行信息
 */
function deleteBrand(index){
	jQuery("#bankTr_"+index).remove();
}

/**
 * 分类状态改变
 * 
 * @param index
 */
function categoryStatusChange_1(index){
	var thisVal = jQuery("#supplierCategorySmall_"+index).val();
	if(thisVal){
		jQuery("#categoryStatusVal_"+index).html("失效");
		jQuery("#categorySmallConfirm_"+index).remove();
		jQuery("#categoryStatusBtn_"+index).html('<input type="button" value="生效" onclick="categoryStatusChange_0('+index+')" class="ext_btn ext_btn_success">');
	}
}

function addKaipiaoInfo(){
	var index = jQuery("#fieldIdDefined").val();
	var kaipiaoArr = new Array();
	kaipiaoArr.push('<tr id="kaipiaoTr1_'+index+'">');
	kaipiaoArr.push('<td class="td_right"><font color="red">*</font>开票名称： </td>');
	kaipiaoArr.push('<td class=""  width="100">');
	kaipiaoArr.push('<input type="text" name="kpName" id="kpname" class="_req input-text lh30" size="40" onblur="changeKpAccountName(this,'+index+');">');
	kaipiaoArr.push('</td>');
	kaipiaoArr.push('<td class="td_right"><font color="red">*</font>纳税人识别码：</td>');
	kaipiaoArr.push('<td>');
	kaipiaoArr.push('<input type="text" name="taxpayerCode" class="_req input-text lh30" size="40">&nbsp;');
	kaipiaoArr.push('<input type="button" class="ext_btn" onclick="deleteKaipiao('+index+')" value="删除">');
	kaipiaoArr.push('</td>');
	kaipiaoArr.push('</tr>');
	kaipiaoArr.push('<tr id="kaipiaoTr2_'+index+'">');
	kaipiaoArr.push('<td class="td_right"><font color="red">*</font>开户银行： </td>');
	kaipiaoArr.push('<td class=""  width="100">');
	kaipiaoArr.push('<input type="text" name="kpBank" class="_req input-text lh30" size="40">');
	kaipiaoArr.push('</td>');
	kaipiaoArr.push('<td class="td_right"><font color="red">*</font>开户名称：</td>');
	kaipiaoArr.push('<td><input type="text" name="kpAccountName" id="kpaccountname" class="_req input-text lh30" size="40" readonly="readonly" ></td>');
	kaipiaoArr.push('</tr>');
	kaipiaoArr.push('<tr id="kaipiaoTr3_'+index+'">');
	kaipiaoArr.push('<td class="td_right"><font color="red">*</font>开户银行账号： </td>');
	kaipiaoArr.push('<td class=""  width="100">');
	kaipiaoArr.push('<input type="text" name="kpAccount" class="_req input-text lh30" size="40">');
	kaipiaoArr.push('</td>');
	kaipiaoArr.push('<td class="td_right"><font color="red">*</font>联系电话：</td>');
	kaipiaoArr.push('<td><input type="text" name="kpTel" class="_req input-text lh30" size="40" maxlength="20"></td>');
	kaipiaoArr.push('</tr>');
	kaipiaoArr.push('<tr id="kaipiaoTr4_'+index+'">');
	kaipiaoArr.push('<td class="td_right"><font color="red">*</font>联系地址： </td>');
	kaipiaoArr.push('<td class=""  width="100">');
	kaipiaoArr.push('<input type="text" name="kpAddress" class="_req input-text lh30" size="40">');
	kaipiaoArr.push('</td>');
	kaipiaoArr.push('<td class="td_right"></td>');
	kaipiaoArr.push('<td></td>');
	kaipiaoArr.push('</tr>');
	var kaipiaoHtml = kaipiaoArr.join('');
    jQuery(kaipiaoHtml).appendTo(jQuery("#kaipiao_table"));
	jQuery("#fieldIdDefined").val(parseInt(index)+1);
}
/**
 * 改写开户名称
 * 
 * @param index
 */
function changeKpAccountName(kpname,index){
	var trObj = jQuery("#kaipiaoTr2_"+index);
	trObj.find("input")[1].value = kpname.value;
}
/**
 * 删除开票信息
 * 
 * @param index
 */
function deleteKaipiao(index){
	jQuery("#kaipiaoTr1_"+index).remove();
	jQuery("#kaipiaoTr2_"+index).remove();
	jQuery("#kaipiaoTr3_"+index).remove();
	jQuery("#kaipiaoTr4_"+index).remove();
}

/**
 * 修改供应商状态
 * 
 * @param index
 */
function categoryStatusChange_0(index){
	var thisVal = jQuery("#supplierCategorySmall_"+index).val();
	if(thisVal){
		jQuery("#categoryStatusVal_"+index).html("有效");
		jQuery("#categoryStatusVal_"+index).append("<input type='hidden' name='categorySmaillSel' id='categorySmallConfirm_"+index+"' value='"+thisVal+"' />");
		jQuery("#categoryStatusBtn_"+index).html('<input type="button" value="失效" onclick="categoryStatusChange_1('+index+')" class="ext_btn ext_btn_error">');
	}
}

/**
 * 注册分类的事件
 */
function registeCategoryEvent(index,needClean){
	if(needClean){
		jQuery("#supplierCategoryBig_"+index).html(daleiHtml);
	}
	//大类事件
	jQuery("#supplierCategoryBig_"+index).change(function(){
		jQuery("#supplierCategoryMid_"+index).html('<option value="">中类</option>');
		jQuery("#supplierCategorySmall_"+index).html('<option value="">小类</option>');
		//大类id
		var bigCid = jQuery(this).val();
		if(bigCid){
			loadMidCategoryOptions(bigCid,1,index);
		}
	});
	//中类事件
	jQuery("#supplierCategoryMid_"+index).change(function(){
		jQuery("#supplierCategorySmall_"+index).html('<option value="">小类</option>');
		
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
				var bigtype = jQuery("#supplierCategoryBig_"+index+" option:selected").val();
				var mintype = jQuery("#supplierCategoryMid_"+index+" option:selected").val();
				
				if(mintype >0 && bigtype >0){
					opArr.push('<option value="">小类</option>');
				}
				else{
					opArr.push('<option value="">中类</option>');
				}
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
 * 刪除供應商聯繫人信息
 */
function deleteSupplierLink(index){
	jQuery("#supplier_link_tr1_"+index).remove();
	jQuery("#supplier_link_tr2_"+index).remove();
	jQuery("#supplier_link_tr3_"+index).remove();
	jQuery("#supplier_link_tr4_"+index).remove();
	
}

/**
 * 添加供应商联系人
 */
function addSuppLink(){
	var index = jQuery("#fieldIdDefined").val();
	var suplierLinkArr = new Array();
	suplierLinkArr.push('<tr id="supplier_link_tr1_'+index+'">');
	suplierLinkArr.push('<td class="td_right">联系人类型：</td>');
	suplierLinkArr.push('<td width="100">');
	suplierLinkArr.push('<div class="select_border">');
	suplierLinkArr.push('<div class="select_containers">');
	suplierLinkArr.push('<span class="fl">');
	suplierLinkArr.push('<select class="select" name="suppLinkType" style="width:140px;">');
	suplierLinkArr.push('<option value="Business">业务联系人</option>');
	suplierLinkArr.push('<option value="Financial">财务联系人</option>');
	suplierLinkArr.push('<option value="Logistics">物流联系人</option>');
	suplierLinkArr.push('<option value="Aftersale">售后联系人</option>');
	suplierLinkArr.push('</select>');
	suplierLinkArr.push('</span>');
	suplierLinkArr.push('</div>');
	suplierLinkArr.push('</div>');
	suplierLinkArr.push('</td>');
	suplierLinkArr.push('<td class="td_right"><font color="red">*</font>姓名：</td>');
	suplierLinkArr.push('<td> ');
	suplierLinkArr.push('<input type="text" name="supplierLinkName" class="_req input-text lh30" size="40">&nbsp;');
	suplierLinkArr.push('<input type="button" class="ext_btn" onclick="deleteSupplierLink('+index+')" value="删除">');
	suplierLinkArr.push('</td>');
	suplierLinkArr.push('</tr>');
	suplierLinkArr.push('<tr id="supplier_link_tr2_'+index+'">');
	suplierLinkArr.push('<td class="td_right"><font color="red">*</font>手机号码：</td>');
	suplierLinkArr.push('<td width="100"> ');
	suplierLinkArr.push('<input type="text" name="supplierLinkMobile" class="_req _mobile input-text lh30" size="40">');
	suplierLinkArr.push('</td>');
	suplierLinkArr.push('<td class="td_right"><font color="red">*</font>电子邮箱：</td>');
	suplierLinkArr.push('<td> ');
	suplierLinkArr.push('<input type="text" name="supplierLinkEmail" class="_req_email input-text lh30" size="40">');
	suplierLinkArr.push('</td>');
	suplierLinkArr.push('</tr>');
	suplierLinkArr.push('<tr id="supplier_link_tr3_'+index+'">');
	suplierLinkArr.push('<td class="td_right"><font color="red">*</font>固定电话：</td>');
	suplierLinkArr.push('<td width="100"> ');
	suplierLinkArr.push('<input type="text" name="supplierLinkTel" class="_req _tel input-text lh30" size="40">');
	suplierLinkArr.push('</td>');
	suplierLinkArr.push('<td class="td_right">传真号码：</td>');
	suplierLinkArr.push('<td> ');
	suplierLinkArr.push('<input type="text" name="supplierLinkFaq" class="input-text lh30" size="40">');
	suplierLinkArr.push('</td>');
	suplierLinkArr.push('</tr>');
	suplierLinkArr.push('<tr id="supplier_link_tr4_'+index+'">');
	suplierLinkArr.push('<td class="td_right"><font color="red">*</font>地址：</td>');
	suplierLinkArr.push('<td width="100"> ');
	suplierLinkArr.push('<input type="text" name="supplierLinkAddr" class="input-text lh30" size="40">');
	suplierLinkArr.push('</td>');
	suplierLinkArr.push('<td class="td_right">QQ：</td>');
	suplierLinkArr.push('<td> ');
	suplierLinkArr.push('<input type="text" name="supplierLinkQQ" class="input-text lh30" size="40">');
	suplierLinkArr.push('</td>');
	suplierLinkArr.push('</tr>');
	var SuppLinkHtml = suplierLinkArr.join('');
    jQuery(SuppLinkHtml).appendTo(jQuery("#supplier_link_table"));
    jQuery("#fieldIdDefined").val(parseInt(index)+1);
	return false;
}

/***
 * 添加西客联系人信息
 */
function addMtLink(){
	var departOptHtml = $('#departmentList').html();
	var index = jQuery("#fieldIdDefined").val();
	var mtLinkArr = new Array();
	mtLinkArr.push('<tr id="mt_linker_tr_'+index+'">')
	mtLinkArr.push('<td>')
	mtLinkArr.push('<div class="select_border">')
	mtLinkArr.push('<div class="select_containers">')
	mtLinkArr.push('<span class="fl">类型：')
	mtLinkArr.push('<select class="select" style="width:100px;" name="xgLinkType">')
	mtLinkArr.push('<option value="Business">业务联系人</option>');
	mtLinkArr.push('<option value="Financial">财务联系人</option>');
	mtLinkArr.push('<option value="Logistics">物流联系人</option>');
	mtLinkArr.push('<option value="Aftersale">售后联系人</option>');
	mtLinkArr.push('</select>')
	mtLinkArr.push('</span>')
	mtLinkArr.push('</div>')
	mtLinkArr.push('</div>')
	mtLinkArr.push('</td>')
	mtLinkArr.push('<td>')
	mtLinkArr.push('<div class="select_border">')
	mtLinkArr.push('<div class="select_containers">')
	mtLinkArr.push('<span class="fl">部门：')
	mtLinkArr.push('<select class="_req select" style="width:100px;" id="mt_link_bm_'+index+'" name="xgLinkerDeptId">')
	mtLinkArr.push('<option value="">选择部门</option>')
	mtLinkArr.push(departOptHtml)
//	mtLinkArr.push('<option value="1">部门1</option>')
//	mtLinkArr.push('<option value="2">部门2</option>')
	mtLinkArr.push('</select>')
	mtLinkArr.push('<input type="hidden" id="mt_link_bms_'+index+'" name="xgLinkerDeptName">')//部门名
	mtLinkArr.push('</span>')
	mtLinkArr.push('</div>')
	mtLinkArr.push('</div>')
	mtLinkArr.push('</td>')
	mtLinkArr.push('<td>')
	mtLinkArr.push('<div class="select_border">')
	mtLinkArr.push('<div class="select_containers">')
	mtLinkArr.push('<span class="fl">姓名：')
	mtLinkArr.push('<select class="_req select" style="width:100px;" id="mt_link_name_'+index+'" name="xgLinkerUserId">')
	mtLinkArr.push('<option value="">请选择</option>')
	mtLinkArr.push('</select>')
	mtLinkArr.push('<input type="hidden" id="mt_link_names_'+index+'" name="xgLinkerUserName">')//人员名
	mtLinkArr.push('</span>')
	mtLinkArr.push('</div>')
	mtLinkArr.push('</div>')
	mtLinkArr.push('</td>')
	mtLinkArr.push('<td><input type="button" class="ext_btn" onclick="deleteMtLink('+index+')" value="删除"></td>');
	mtLinkArr.push('</tr>')
	var mtLinkHtml = mtLinkArr.join('');
    jQuery(mtLinkHtml).appendTo(jQuery("#mt_link_table"));
    jQuery("#fieldIdDefined").val(parseInt(index)+1);
    registeMtlinker(index);
	return false;
}

/**
 * 注册西客联系人事件
 */
function registeMtlinker(index){
	jQuery("#mt_link_bm_"+index).change(function(){
		jQuery("#mt_link_name_"+index).html("<option value=''></option>");
		var bmId = jQuery(this).val();
		if(!bmId){
			return;
		}
		jQuery("#mt_link_bms_"+index).val(jQuery(this).find("option:selected").text());//部门名
		ajaxRequest({
			method:'post',
			url:'/supplier/client/getXgUsers.htm',
			data:{"bmId":bmId},
			success:function(resultInfo){
				resultInfo = eval('('+resultInfo+')');
				if(resultInfo.success){
					var mtUsers = resultInfo.data;
					var optArr = new Array();
					optArr.push("<option value=''>请选择</option>");
					if(mtUsers && mtUsers.length>0){
						for(var i=0;i<mtUsers.length;i++){
							var mtUser = mtUsers[i];
							optArr.push("<option value="+mtUser.id+">"+mtUser.userName+"</option>");
						}
					}
						jQuery("#mt_link_name_"+index).html(optArr.join(""));
					
				}
			}
		})
	});
	
	jQuery("#mt_link_name_"+index).change(function(){
		jQuery("#mt_link_names_"+index).val(jQuery(this).find("option:selected").text());//人员名
		
	});
}

/**
 * 删除西客对接人信息
 * 
 * @param index
 */
function deleteMtLink(index){
	jQuery("#mt_linker_tr_"+index).remove();
}

/**
 * 添加品牌行
 */
function addBrandLine(){
	var lineArr = new Array();
	lineArr.push('<tr>');
	lineArr.push('<td>01</td>');
	lineArr.push('<td>帮宝适</td>');
	lineArr.push('<td>有效</td>');
	lineArr.push('<td><input type="button" value="失效" class="ext_btn ext_btn_error"></td>');
	lineArr.push('</tr>');
	var lineHtml = lineArr.join("");
	jQuery(lineHtml).appendTo(jQuery("#brandSpanTableId"));
	///jQuery("#brandSpanId").insert;
}

function changeStatus_0(index,bId,bTag){
	if(bTag){
		jQuery("#"+bTag+"spp_add_status_"+index).html("有效");
		jQuery("#"+bTag+"Name_"+index).attr("dStatus","1");
		jQuery('<input type="hidden" id="'+bTag+'NameSel_'+index+'" name="supplier'+bTag+'" value="'+bId+'"/>').insertBefore(jQuery("#"+bTag+"Name_"+index));
		jQuery("#sp_"+bTag+"_btn_"+bId).html('<input type="button" onclick="changeStatus_1('+index+','+bId+',\''+bTag+'\')" value="失效" class="ext_btn ext_btn_error">');
	} else {
		jQuery("#spp_add_status_"+index).html("有效");
		jQuery("#sBName_"+index).attr("dStatus","1");
		jQuery('<input type="hidden" id="sBNameSel_'+index+'" name="supplierBrand" value="'+bId+'"/>').insertBefore(jQuery("#sBName_"+index));
		jQuery("#sp_brand_btn_"+index).html('<input type="button" onclick="changeStatus_1('+index+','+bId+')" value="失效" class="ext_btn ext_btn_error">');
	}
}

function changeStatus_1(index,bId,bTag){
	if(bTag){
		jQuery("#"+bTag+"spp_add_status_"+index).html("无效");
		jQuery("#"+bTag+"Name_"+index).attr("dStatus","0");
		jQuery("#"+bTag+"NameSel_"+index).remove();
		jQuery("#sp_"+bTag+"_btn_"+index).html('<input type="button" onclick="changeStatus_0('+index+','+bId+',\''+bTag+'\')" value="生效" class="ext_btn ext_btn_success">');
	} else {
		jQuery("#spp_add_status_"+index).html("无效");
		jQuery("#sBName_"+index).attr("dStatus","0");
		jQuery("#sBNameSel_"+index).remove();
		jQuery("#sp_brand_btn_"+index).html('<input type="button" onclick="changeStatus_0('+index+','+bId+')" value="生效" class="ext_btn ext_btn_success">');
	}
}

/**
 * 改变用户的状态 当前的设置状态为1
 */
function changeUserStatus_1(){
	jQuery("#supp_user_status_show").html("无效<input type='hidden' value='0' name='status'>");
	jQuery("#supp_user_status_btn").html('<input type="button" onclick="changeUserStatus_0()" value="生效" class="ext_btn ext_btn_success">');
}

/**
 * 改变用户状态  当前的状态为0
 */
function changeUserStatus_0(){
	jQuery("#supp_user_status_show").html("有效<input type='hidden' value='1' name='status'>");
	jQuery("#supp_user_status_btn").html('<input type="button" onclick="changeUserStatus_1()" value="失效" class="ext_btn ext_btn_error">');
}

/**
 * 注册元素上的时间
 * 
 * @returns
 */
function registeElementEvent(){
	var parentFrameID = self.frameElement.getAttribute('id');
	jQuery("#supp_add_name_id").change(function(){
		var spName = jQuery(this).val();
		var obj = jQuery(this);
		jQuery(this).siblings("font._errorMsg").remove();
		if(!spName){
			return;
		}
		ajaxRequest({
			method:'post',
			url:'/supplier/checkSupplierInfo.htm',
			data:{"name":spName},
			success:function(data){
				data = eval('('+data+')');
				if(data.success){
					confirmBox('供应商名称已存在,确定继续？',null,function(){
						jQuery("#supp_add_name_id").val("");
					});
				} else {
					var errorMsg = "<font class='_errorMsg' color='green'>名称有效</font>";
					jQuery(obj).after(jQuery(errorMsg));
					jQuery("#supplier_add_save").attr("disabled",false);
				}
			}
		})
	});
	
	jQuery("#supp_add_simple_name_id").change(function(){
		var simpleName = jQuery(this).val();
		var obj = jQuery(this);
		jQuery(this).siblings("font._errorMsg").remove();
		if(!simpleName){
			return;
		}
		ajaxRequest({
			method:'post',
			url:'/supplier/checkSupplierInfo.htm',
			data:{"alias":simpleName},
			success:function(data){
				data = eval('('+data+')');
				if(data.success){
					confirmBox('供应商简称已存在,确定继续？',null,function(){
						jQuery("#supp_add_simple_name_id").val("");
					});
				} else {
					var errorMsg = "<font class='_errorMsg' color='green'>简称有效</font>";
					jQuery(obj).after(jQuery(errorMsg));
					jQuery("#supplier_add_save").attr("disabled",false);
				}
			}
		})
	});
	
	/**
	 * 主供应商 确认按钮 
	 */
	jQuery("#main_supp_cofirm").click(function(){
		var supplierName = jQuery("#supplier_query_text").val();
		if(!supplierName){
			alertMsg('请填写供应商名称。');
			return;
		}
		jQuery("#supplier_add_parent_id").val("");
		ajaxRequest({
			method:'post',
			url:'/supplier/checkSupplierInfo.htm',
			data:{"name":supplierName},
			success:function(data){
				data = eval('('+data+')');
				if(data.success){
					var suppInfo = data.supplier;
					jQuery("#supplier_add_parent_id").val(suppInfo.id);
					alertMsg('供应商有效。');
				} else {
					alertMsg('供应商找不到。');
				}
			}
		})
	});
	
	/***
	 * 主供应商查询按钮
	 */
	jQuery("#main_supp_query").click(function(){
		var supplierName = "";
		ajaxRequest({
			method:'post',
			url:'/supplier/getSuppliers.htm',
			success:function(data){
				window.parent.showTableDiv(1,data,parentFrameID,{"title":"供应商"});
			}
		})
	});
	
	/**
	 * 设置页面品牌参数
	 * 
	 * @param bId
	 * @param bName
	 */
	function setBrandToPage(InBId,InbName){
		var target = jQuery("#brandSpanTableId");
		var index = jQuery("#fieldIdDefined").val();
		window.parent.addBrandLine(index,target,InBId,InbName);
		jQuery("#fieldIdDefined").val(parseInt(index)+1);
	}
	
	/**
	 * 设置选择参数
	 * 
	 * @param bId
	 * @param bName
	 */
	function setStorageInfoToPage(InBId,InbName){
		var idenfity = "storage";
		var identifyName = "仓库";
		var alertStr = new Array();;
		var noUseMap = {};
		var addMap = {};
		var spBrandSel = jQuery("input[name='supplierA"+idenfity+"Sel']");
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
		if(noUseMap[InBId]){
			alertStr.push(InbName);
		} else if(addMap[InBId]){
			//已经添加
		} else {
			var target = jQuery("#"+idenfity+"SpanTableId");
			window.parent.addStorageLine(target,InBId,InbName,idenfity);
		}
		if(alertStr.length>0){
			alertMsg(identifyName+"："+alertStr.join(",")+"已经添加，状态是失效状态，请点击【生效】按钮使其有效。");
		}
	}
	
	/**
	 * 品牌确认按钮
	 */
	jQuery("#brand_btn_confirm").click(function(){
		var parentFrameID = self.frameElement.getAttribute('id');
		/*//TODO  品牌接口
		addBrandLine();*/
		var brandName = jQuery("#supper_brand_search_text").val();
		if(!brandName){
			alertMsg('请填写品牌名称。');
			return;
		}
		ajaxRequest({
			method:'post',
			url:'/supplier/checkBrandInfo.htm',
			data:{"brandName":brandName},
			success:function(data){
				data = eval('('+data+')');
				if(data.success && data.data!=null){
					var brand = data.data;
					setBrandToPage(brand.id,brand.name);
				} else {
					alertMsg('找不到相应品牌。');
				}
			}
		})
	});
	
	/**
	 * 品牌查询按钮
	 */
	jQuery("#brand_btn_query").click(function(){
		ajaxRequest({
			method:'post',
			url:domain + '/supplier/getSupplierBrands.htm',
			success:function(data){
				window.parent.showTableDiv(2,data,parentFrameID,{"title":"添加品牌"});
			}
		})
	});
	
	/**
	 * 运费模板确认按钮
	 */
    jQuery("#sp_add_express_confirm").click(function() {
    	var isHaitao = jQuery("input[name='isHtSupplier']:checked").val();
		if(1 != parseInt(isHaitao)){
			alertMsg('非海淘供应商不能选择运费模板。');
			return;
		}
		
		var expressTemplateId = jQuery("#expressTemplateId").val();
		var expressTemplateName = jQuery("#expressTemplateName").val();
		if(!expressTemplateId && !expressTemplateName){
			alertMsg('请填写物流模板信息。');
			return;
		}
		ajaxRequest({
			method:'post',
			url:'/supplier/client/checkExpressInfo.htm',
			data:{"expressName":expressTemplateName,"expressId":expressTemplateId},
			success:function(data){
				data = eval('('+data+')');
				if(null != data && data.length>0 && data[0].id!=null){
					jQuery("#expressTemplateId").val(data[0].id);
					jQuery("#expressTemplateName").val(data[0].name);
					alertMsg('物流模板有效。');
				} else {
					alertMsg('物流模板找不到。');
				}
			}
		})
    });
    
    jQuery("#sp_add_express_query").click(function(){
    	var isHaitao = jQuery("input[name='isHtSupplier']:checked").val();
		if(1 != parseInt(isHaitao)){
			alertMsg('非海淘供应商不能选择运费模板。');
			return;
		}
		
    	ajaxRequest({
			method:'post',
			url:'/supplier/client/getExpressTemplate.htm',
			success:function(html){
				window.parent.showTableDiv(3,html,parentFrameID,{"title":"物流模板"});
			}
		})
    });
    jQuery("#sp_edit_express_query").click(function(){
    	var isHaitao = jQuery("input[name='isHtSupplier']").val();
		if(1 != parseInt(isHaitao)){
			alertMsg('非海淘供应商不能选择运费模板。');
			return;
		}
		
    	ajaxRequest({
			method:'post',
			url:'/supplier/client/getExpressTemplate.htm',
			success:function(html){
				window.parent.showTableDiv(3,html,parentFrameID,{"title":"物流模板"});
			}
		})
    });
    
    //仓库确认按钮
    jQuery("#sp_add_storage_confirm").click(function(){
    	var storageId = jQuery("#sp_add_storageId").val();
    	var storageName = jQuery("#sp_add_storageName").val();
		if(!storageName && !storageId){
			alertMsg('请填写仓库信息。');
			return;
		}
    	
    	ajaxRequest({
			method:'post',
			url:'/supplier/client/getStorageInfo.htm',
			data:{"storageId":storageId,"storageName":storageName},
			success:function(data){
				data = eval('('+data+')');
				if(data.success && 'false' != data.success){
					var storage = data.storage;
					setStorageInfoToPage(storage.id,storage.name);
				} else {
					alertMsg('找不到相应仓库。');
				}
			}
		})
    });
    
    jQuery("#sp_add_storage_query").click(function(){
    	ajaxRequest({
			method:'post',
			url:'/supplier/client/getStorages.htm',
			success:function(data){
				window.parent.showTableDiv(4,data,parentFrameID,{"title":"添加仓库"});
			}
		})
    });
    
    /**
     * 密码输入框的功能
     */
    jQuery("#sp_user_password").focus(function(){
    	var val = jQuery(this).val();
    	if("商家平台密码" == val){
    		jQuery(this).val("");
    	}
    });
    jQuery("#sp_user_password").attr("oncopy","return false;");
    jQuery("#sp_user_password").attr("oncut","return false;");
    jQuery("#sp_user_password").attr("onpaste","return false;");
    /*jQuery("#sp_user_password").copy(function(e){
    	e.preventDefault();
    	return false;
    });*/
    /*jQuery("#sp_user_password").cut(function(e){
    	e.preventDefault();
    	return false;
    });*/
}
function show(obj){
	var radioValue = obj.value;
	if(radioValue==0){
		jQuery("#recordationsDiv").hide();
	}else if(radioValue==1){
		jQuery("#recordationsDiv").show();
	}
}
/**
 * 添加海关备案信息
 */
function addRecordation(){
	var customsChannelHtml = $('#customsChannelListid').html();
	var index = jQuery("#fieldIdDefined").val();
	var recordationArr = new Array();
	recordationArr.push('<tr id="recordation_'+index+'">');
	recordationArr.push('<td>');
	recordationArr.push('<div class="select_border">');
	recordationArr.push('<div class="select_containers">');
	recordationArr.push('<span class="fl">');
	recordationArr.push('<select class="_req select" style="width:100px;" name="customsChannel"  onchange="judgeCustomsChannel(this,'+index+')">');
	recordationArr.push(customsChannelHtml);
	recordationArr.push('</select>');
	recordationArr.push('<input type="hidden" id="recordation_customsChannelName_'+index+'" name="customsChannelName">');
	recordationArr.push('</span>');
	recordationArr.push('</div>');
	recordationArr.push('</div>');
	recordationArr.push('</td>');
	recordationArr.push('<td>备案名称：');
	recordationArr.push('<input type="text" name="recordationName" maxlength="60" class="_req input-text lh30" size="15">');
	recordationArr.push('</td>');
	recordationArr.push('<td>备案编号：');
	recordationArr.push('<input type="text" name="recordationNum" maxlength="60" class="_req input-text lh30" size="15">');
	recordationArr.push('</td>');
	recordationArr.push('<td><input type="button" class="ext_btn" onclick="deleteRecordation('+index+')" value="删除"></td>');
	recordationArr.push('</tr>');
	var recordationHtml = recordationArr.join('');
    jQuery(recordationHtml).appendTo(jQuery("#recordationChooseTable"));
    jQuery("#fieldIdDefined").val(parseInt(index)+1);
}

/**
 * 判断通关渠道不能重复,以及customsChannelName赋值
 */
function judgeCustomsChannel(thisSelect,index){
	
	var val=$(thisSelect).val();
	jQuery("select[name='customsChannel']").each(function(){
		var value = this.value;
		if( value != "" && value == val && this != thisSelect){
			alertMsg("通关渠道不能重复");
			$(thisSelect).val("");
			return;
		}
	});
	jQuery("#recordation_customsChannelName_"+index).val(jQuery(thisSelect).find("option:selected").text());
}
/**
 * 删除海关备案信息
 */
function deleteRecordation(index){
	jQuery("#recordation_"+index).remove();
}
/**
 * 说明供应商类型
 */
function explainSupplierType(thisSelect){
	var supplierTypeExplanation = jQuery("#supplierTypeExplanation_"+thisSelect.value).val();
	jQuery("#supplierTypeExplanation").html(supplierTypeExplanation);
}
/**
 * 初始化供应商类型说明
 */
function initSupplierTypeExplanation(){
	var key = jQuery("select[name='supplierType']").val();
	var supplierTypeExplanation = jQuery("#supplierTypeExplanation_"+key).val();
	jQuery("#supplierTypeExplanation").html(supplierTypeExplanation);
}
