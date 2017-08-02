/**
 * 主要实现商品维护供应商,商品属性的页面...
 */
var pageii;
$(function(){
	//复制Pc模板到手机模板
	$('#copyPc2Mobile').bind('click',function(){
	  mobileEditor.sync(); 
	  var mobileEditorVal = $.trim($("#mobileEditor").val());
	  if(mobileEditorVal!=""){
	  	//pc详情同步到手机模板
	 	 pageii = layer.confirm('手机模板里面内容已经存在，是否继续复制模板', function(){
	   		 editor.sync(); 
   			 var editorVal = $("#editor").val();
			 mobileEditor.html(editorVal);
		     layer.close(pageii);
		     alert('复制成功!');
		     $('#mobileEditorTabBtn').click();
	  	 },function(){
		 
	     });
	  
	  }else{
	  	 editor.sync(); 
   		 var editorVal = $("#editor").val();
		 mobileEditor.html(editorVal);
		 alert('复制成功!');
		  $('#mobileEditorTabBtn').click();
	  }
	});

	//是否海淘
	var wavesSignVal = $("#wavesSignId").val();
	if(wavesSignVal=="1"){
		$(".tarriRateClass").hide();
		$(".triggerCountryClass").hide();
		$(".customsListClass").hide();
		$(".exciseRateClass").hide();
		$(".addedValueRateClass").hide();
	}
	//有效期
    $("#wavesSignId").change(function(){
		var wavesSignVal = $(this).val();
		if(wavesSignVal=="1"){
			$(".tarriRateClass").hide();
			$(".triggerCountryClass").hide();
			
			$(".customsListClass").hide();
			$(".exciseRateClass").hide();
			$(".addedValueRateClass").hide();
		}else{
			$(".tarriRateClass").show();
			$(".triggerCountryClass").show();
			
			$(".customsListClass").show();
			$(".exciseRateClass").show();
			$(".addedValueRateClass").show();
		}
    });
	 
	var selectExpSignVal = $("#selectExpSign").val();
	
	if(selectExpSignVal=="2"){
		$(".expSignClass").hide();
	}
	
    $("#selectExpSign").change(function(){
		var selectExpSignVal = $(this).val();
		if(selectExpSignVal=="2"){
			$(".expSignClass").hide();
		}else{
			$(".expSignClass").show();
		}
    });
	
	//商品对于供应商
	$('#selectSupplierbtn').live('click',function(){
		  //var barcode = 
		  var hasXgSeller = $(this).attr("status");
		  if(hasXgSeller&&hasXgSeller==1){
				alert("同一个商品中，只能有一个西客商城自营的sku,可以有多个商家平台的sku。");
		  }
		  pageii=$.layer({
            type : 2,
            title: '查询供应商列表',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['800px', 500],                     
            iframe: {
                src : domain+'/item/selectSupplier.htm?hasXgSeller='+hasXgSeller
            } 
        });
	}); 
	
	/**
	 * 父选子
	 * 全选/取消全选
	 */
	$('#chooseItemStatus').live('click',function() {
		if ($(this).attr('checked')) {
			$("input[name='status']").attr("checked", true);
		} else {
			$("input[name='status']").attr("checked", false);
		}
	});
	
	
	
	$("#querySupplierBtn").live('click',function(){
		var supplierId = $('#supplierIdQuery').val();
		if(!checkIntNum(supplierId)){
			alert('编码只能为整数');
			return;
		}
		$('#selectSupplierForm').submit();
	});
	
	
	
	//作废sku
	$('.cancelSkuBtn').live('click',function(){
		  var skuId = $(this).attr("id");
		  $.ajax({ 
				url: 'cancelSku.htm?skuId='+skuId, 
				dataType: 'json',
				type: "post", 
				cache : false, 
				success: function(data) {
					if(data.success){//成功
						layer.alert('操作成功', 4,function(){
							location.reload();
						});
				    }else{//失败
				    	 layer.alert(data.msg.message, 8);
				    }			
				}	
		  });
	}); 
	//商品属性
	$('#selectAttributesbtn').live('click',function(){
		  pageii=$.layer({
            type : 2,
            title: '查询供应商列表',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['800px', 500],                     
            iframe: {
                src : domain+'/item/selectAttribute.htm'
            } 
        });
	}); 
	
	//查询自销商品的sku对应的供应商
	$('.querySkuSupplier').live('click',function(){
		var skuId = $(this).attr('id');
		pageii=$.layer({
            type : 2,
            title: '查询Sku对应的供应商列表',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['800px', 500],                     
            iframe: {
                src : domain+'/item/getSkuSupplier.htm?skuId='+skuId
            } 
        });
	}); 
	
	
	//查询自销商品的sku对应的供应商
	$('.querySkuArtNumber').live('click',function(){
		var skuId = $(this).attr('id');
		pageii=$.layer({
            type : 2,
            title: '设置sku备案信息', 
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['1500px', 600],                     
            iframe: { 
                src : domain+'/item/getSkuArtNumber.htm?skuId='+skuId
            } 
        });
	}); 
	
	
	$('#addAttributeBtn').live('click',function(){
		//添加自定义属性到商品中
		$('#detailAttrList').append(appendToAttrList());
		
	});

	$('#addDummyAttributeBtn').live('click',function(){
		//添加自定义属性到商品中
		$('#dummyAttrList-custom').append(appendToDummyAttrList());

	});

	
	$('.deleteAttrBtn').live('click',function(){
		var $this = $(this);
		$this.closest("tr").detach();
	});
	/**
	 * 保存
	 */
	$('#inputFormSaveBtn').live('click',function(){
		//绑定商品属性
		bindSkuListData();
		if(validateDetailForm()){
				$.ajax({ 
					url: 'saveDetail.htm', 
					dataType: 'json',
					data: $('#inputForm').serialize(), 
					type: "post", 
					cache : false, 
					success: function(resultInfo) {
						if(resultInfo.success){//成功
							layer.alert('操作成功', 4,function(){
								location.href='detail.htm?detailId='+resultInfo.data;
							});
					    }else{//失败
					    	 layer.alert(resultInfo.msg.message, 8);
					    }			
					}
				});
		}
	});
	
	$('.deleteSkuListItem').live('click',function(){
		var $this = $(this);
		$this.closest("tr").detach();
	});
	
	$("#detailPicPreview").on("click",function(){
		var phoneContent = $("#mobileEditor").val();
		if(null == phoneContent || "" == $.trim(phoneContent)){
			return;
		}
		var content = phoneContent.replace(/.jpg/g, ".jpg?imageView2/2/w/750");
		$.layer({
			type : 1,
			title : "查看图片详情",
			shadeClose : true,
			maxmin : true,
			fix : false,
			area: ['800px', 35],
			page: {
				html : content
			}
		});
	});
	
	
	$('#imguplod').find('.item-picture').each(function(i,n){
		$(n).children('.remove-btn').click(function(){
			$(n).remove();
			fileCountlimit++;
			swfu.setFileUploadLimit(fileCountlimit);
		});
		$(n).children('.pre-btn').click(function(){
			$(n).insertBefore($(n).prev());
		});
		$(n).children('.next-btn').click(function(){
			$(n).insertAfter($(n).next());
		});
		$(n).hover(function(){
			$(this).find('.remove-btn').fadeIn();
			$(this).find('.pre-btn').fadeIn();
			$(this).find('.next-btn').fadeIn();
		},function(){
			$(this).find('.remove-btn').fadeOut();
			$(this).find('.pre-btn').fadeOut();
			$(this).find('.next-btn').fadeOut();
		});
		hasUploadCount++;
	});

	
	/**
	 * 全选/取消全选
	 */
	$('#chkall').click(function() {
		if ($(this).attr('checked')) {
			$("input[name='spId']").attr("checked", true);
		} else {
			$("input[name='spId']").attr("checked", false);
		}
	});
	
	/**
	 * 确定按钮
	 * 列表table supplierListTr
	 */
	$('#confirmSuppliersBtn').click(function(){
		var supplierIdArray = new Array();
		supplierIdArray = parent.getSkuSupplier();
		var selectSeller = $('#selectSeller').val();
		var puchaseArry = new Array();
		var flag = true;
		$('.supplierListTr').each(function(){
			if($(this).find('[name=spId]').attr('checked')){
				//动态添加行到父页面
				var supplierId = $(this).find('[name=spId]').val();
				var supplierName = $(this).find('[name=supplierName]').val();
				var supplierType = $(this).find('[name="supplierType"]').val();
			
				var supplierTypeName = $(this).find('[name="supplierTypeName"]').val();
				
				if(supplierIdArray.indexOf(supplierId)>-1){
					flag = false;
					alert("供应商["+ supplierName+"]已经存在sku中,不能再被选择");
					return ;
				}
				//西客商城自营
				if(supplierType  != "Associate"){
					if(puchaseArry.length===0){
						parent.$('#suppListTable').append(appendToSuppList(selectSeller,supplierId,supplierName,supplierType,supplierTypeName));//生成 
					}					
					var skuAndSupplierJson ={selectSeller:selectSeller,supplierId:supplierId,supplierName:supplierName,supplierType:supplierType};
					puchaseArry.push(skuAndSupplierJson);
					var purchaseJson=JSON.stringify(puchaseArry);
					parent.$('#purchaseJson').val(purchaseJson);
				}else{
					parent.$('#suppListTable').append(appendToSuppList(selectSeller,supplierId,supplierName,supplierType,supplierTypeName));//生成 
				}
			} 
		});
		if(flag){
			var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			parent.layer.close(index); //执行关闭
		}
	});

	$("select[name='itemDetail.itemType']").on("change",function () {
		var itemType = $("select[name='itemDetail.itemType']").val();
		if(itemType==2){
			initDUMMYItemDefaultAttribute();
		}else {
			delDUMMYItemDefaultAttribute();
		}
	});

});

/**
 * 获取选中的供应商列表 supplierId,supplierName,supplierType ,supplierTypeName
 * supplierType：自营 ，第三方
 * 追加到父页面的table： suppListTable
 */
var __index=0;
var appendToSuppList = function(selectSeller,supplierId,supplierName,supplierType,supplierTypeName){
	/*
	 自营(西客商城) ： 代销 sell ,联营 Associate
         商家：            自营  Purchase
	*/
	var __supplierName = supplierName;
	var __sellerName="商家";
	var __supplierId = supplierId;
	var saleType = 0;
	if(supplierType=="Associate"){
		saleType =1 ;
//		__supplierName = supplierName;
//		__supplierId = supplierId;
		__sellerName="商家";
	}
	__index++;
	var __basicPrice = parent.$("#basicPrice").val(); 
	var trHtml = 
		'<tr class="skuList" >'
		
			+'<td width="60">'
			+	'<input type="text" name="sort" class="input-text lh30" size="5" maxlength=4 value='+__index+' />'
			+	'<input type="hidden" name="saleType" class="input-text lh30" size="20" value= '+saleType+' />'
			+	'<input type="hidden" name="spId" class="input-text lh30" size="20" value= '+__supplierId+' />'
			+	'<input type="hidden" name="supplierType" class="input-text lh30" size="20" value= '+supplierType+' />'
			+'</td>'
			+'<td width="60">'
			+	'<input type="text" name="prdid" class="input-text lh30" size="20" value="系统自动生成"  readonly=readonly/>'
			+'</td>'
			
			+'<td width="60">'
			+	'<input type="text" name="supplierName" class="input-text lh50" value="'+__supplierName+'"  readonly= readonly />'
			+'</td>'
			
			+'<td width="60">'
			+	'<input type="text" name="" class="input-text lh30" size="20" value="系统自动生成"  readonly=readonly />'
			+'</td>'
			
			
			+'<td width="60">'
			+	'<input type="text" name="skuBasicPrice" class="input-text lh30" size="5" maxlength=6 value="'+__basicPrice+'"  />'
			+'</td>'
			
			+'<td width="60">未上架</td>'
			
			+'<td width="60">'+__sellerName+'</td>'
			
			+'<td width="60">'
			+'	<input name="status" type="checkbox" />上架'
			+'	<a href="javascript:;" class="deleteSkuListItem" >删除</a>';
	
			if(supplierType!=='Main'){
				trHtml +='<input type="hidden" name="purchaseJson" id="purchaseJson"  />' ;
			}
			trHtml += '</td></tr>';
//	$('#'+skuListId).append(trHtml);
	return trHtml;
};

function showImg(img){
	var picUrl = $(img).attr("src");
	if(null == picUrl || "" == $.trim(picUrl)){
		return;
	}
	$.layer({
		type : 2,
		title : "查看图片",
		shadeClose : true,
		maxmin : true,
		fix : false,
		area: ['600px', 500],
		iframe : {
			src : picUrl
		}
	});
};

//序列化sku列表
function bindSkuListData(){
	var skuArry = new Array();
	$('.skuList').each(function(){
		var skuObj = new Object();
		var skuId = $.trim($(this).find('[name=skuId]').val());
		if(!skuId.isEmpty()){
			// SKU修改状态  只能修改sku的价格，商品号 ，上下架
			var basicPrice = $(this).find('[name=skuBasicPrice]').val().trim();
			var status = $(this).find('[name=status]').val().trim();
			var statusCheckbox = $(this).find('[name=statusCheckbox]');
			if(status != 2){
				if (statusCheckbox.attr('checked')){
					status = 1 ;
				}else{
					status = 0;
				}	
			}
			var sort = $(this).find('[name=sort]').val().trim();
			skuObj.basicPrice=basicPrice;
			skuObj.status=status;
			skuObj.sort=sort;
			skuObj.id=skuId;
		}else{
			if($(this).find('[id=purchaseJson]')!=undefined){
				if($(this).find('[id=purchaseJson]').val()!=""){
					skuObj.skuSupplierList = JSON.stringify($(this).find('[id=purchaseJson]').val());
				}
			}
			var saleType = $(this).find('[name=saleType]').val().trim();
			var supplierId = $(this).find('[name=spId]').val().trim();
			var supplierName = $(this).find('[name=supplierName]').val().trim();
			//目前供应商code就是Id
			var supplierCode = $(this).find('[name=spId]').val().trim();
			var basicPrice = $(this).find('[name=skuBasicPrice]').val().trim();
			var status = $(this).find('[name=status]').val().trim();;
			var statusCheckbox = $(this).find('[name=statusCheckbox]');
			if(status != 2){
				if (statusCheckbox.attr('checked')){
					status = 1 ;
				}else{
					status = 0;
				}	
			}			
			var supplierType = $(this).find('[name=supplierType]').val().trim();
			var sort = $(this).find('[name=sort]').val().trim();
			skuObj.saleType=saleType;
			skuObj.spId=supplierId;
			skuObj.spName=supplierName;
			skuObj.spCode=supplierCode;
			skuObj.basicPrice=basicPrice;
			skuObj.status=status;
			skuObj.supplierType=supplierType;
			skuObj.sort=sort;
		}
		skuArry.push(skuObj);
	});
	
	var attrItemArry = new Array();
	//绑定属性组 attribute
	$('.attrItem').each(function(){
		var attrItem = new Object();
		var attrId = $(this).find('[name=attributeId]').val().trim();
		var attributeValueArry = new Array();
		
		if($(this).has('select').length){ 
			var selectObj = $(this).find('select').val();
			if(null!==selectObj){
				var attrValueId = $(this).find('select').val().trim();
	        	attributeValueArry.push(attrValueId);
			}
        }else{
        	$(this).find('[name=attributeValueId]:checkbox').each(function(){ 
                if($(this).attr("checked")){
                	attributeValueArry.push($(this).val());
                }
            });
        }
		attrItem.attrId=attrId;
		attrItem.attrValueIds= attributeValueArry.join(',');;
		attrItemArry.push(attrItem);
	});
	var attrListArry = new Array();
	$('.attrList').each(function(){
		var attrObj = new Object();
		var attrKey = $(this).find('[name=attrKey]').val().trim();
		var attrValue = $(this).find('[name=attrValue]').val().trim();
		attrObj.attrKey=attrKey;
		attrObj.attrValue=attrValue;
		attrListArry.push(attrObj);
	});

	var dummyAttrListArray = new Array();
	if($("select[name='itemDetail.itemType']").val()=="2"){
		var effectTimeStart  = $("input[name='effectTimeStart']").val();
		var effectTimeEnd  = $("input[name='effectTimeEnd']").val();
		// if(effectTimeStart==null || effectTimeStart=="" || effectTimeEnd==null || effectTimeEnd==""){
        //
		// }
		var ets = new Object();
		ets.attrKey = "effectTimeStart";
		ets.attrValue= effectTimeStart;
		var ete = new Object();
		ete.attrKey = "effectTimeEnd";
		ete.attrValue = effectTimeEnd;
		var ie = new Object();
		ie.attrKey = "includeFestival";
		ie.attrValue = $("input[name='includeFestival']:checked").length;
		dummyAttrListArray.push(ets);
		dummyAttrListArray.push(ete);
		dummyAttrListArray.push(ie);

		$('.dummyAttrList').each(function(){
			var attrObj = new Object();
			var attrKey = $(this).find('[name=dummyAttrKey]').val().trim();
			var attrValue = $(this).find('[name=dummyAttrValue]').val().trim();
			attrObj.attrKey=attrKey;
			attrObj.attrValue=attrValue;
			dummyAttrListArray.push(attrObj);
		});
	}


	
	var skuListJson=JSON.stringify(skuArry);
	var attrItemJson=JSON.stringify(attrItemArry);
	var attrListJson=JSON.stringify(attrListArry);
	var dummyAttrListJson=JSON.stringify(dummyAttrListArray);
	$('#skuListJson').val(skuListJson);
	$('#attrItemJson').val(attrItemJson);
	$('#attrListJson').val(attrListJson);
	$('#dummyAttrListJson').val(dummyAttrListJson);
}


var appendToAttrList =function(){
	var trHtml = 
		'<tr class="attrList" >'
		+'<td width="60" class="td_left">'
		+	'<input type="text" class="input-text lh30" size="25" name="attrKey" maxlength="20" value="" />'
		+'</td>'
		+'<td width="200" class="td_left">'
		+	'<input type="text" class="input-text lh30" size="25" name="attrValue" maxlength="200"  value="" />'
		+'</td>'
		
		+'<td width="60" class="td_left">'
		+	'<input type="button"   value="-" class="ext_btn ext_btn_submit m10 deleteAttrBtn">'
		+'</td>'
		+'</tr>';
	return trHtml;
	
};



var appendToDummyAttrList =function(){
	var trHtml =
		'<tr class="dummyAttrList" >'
		+'<td width="60" class="td_left">'
		+	'<input type="text" class="input-text lh30" size="25" name="dummyAttrKey" maxlength="20" value="" />'
		+'</td>'
		+'<td width="200" class="td_left">'
		+	'<input type="text" class="input-text lh30" size="25" name="dummyAttrValue" maxlength="200"  value="" />'
		+'</td>'

		+'<td width="60" class="td_left">'
		+	'<input type="button"   value="-" class="ext_btn ext_btn_submit m10 deleteAttrBtn">'
		+'</td>'
		+'</tr>';
	return trHtml;

};


var attrTemplate = 	'<tr class="attrList" >'
	+'<td width="60" class="td_left">'
	+	'<input type="text" class="input-text lh30" size="25" name="attrKey" maxlength="20" value="DEF-VALUE-" />'
	+'</td>'
	+'<td width="200" class="td_left">'
	+	'<input type="text" class="input-text lh30" size="25" name="attrValue" maxlength="200"  value="" />'
	+'</td>'

	+'<td width="60" class="td_left">'
	+	'<input type="button"   value="-" class="ext_btn ext_btn_submit m10 deleteAttrBtn">'
	+'</td>'
	+'</tr>';

var attrTemplateA = 	'<tr class="dummyAttrList" >'
	+'<td width="60" class="td_left">'
	+	'<input type="text" class="input-text lh30" size="25" name="dummyAttrKey" maxlength="20" value="'
	;

var attrTemplateB = '" />'
	+'</td>'
	+'<td width="200" class="td_left">'
	+	'<input type="text" class="input-text lh30" size="25" name="dummyAttrValue" maxlength="200"  value="" />'
	+'</td>'

	+'<td width="60" class="td_left">'
	+	'<input type="button"   value="-" class="ext_btn ext_btn_submit m10 deleteAttrBtn">'
	+'</td>'
	+'</tr>';

var defAttrList = new Array("使用时间","预约信息","适用人数","使用人群","规则提醒","商家服务","温馨提示","温馨提示","温馨提示");

Array.prototype.contains = function(item){
	for(i=0;i<this.length;i++){
		if(this[i]==item){return true;}
	}
	return false;
};

var initDUMMYItemDefaultAttribute = function () {

	var info_div=$("#dummyItemAttrInfo-div");
	info_div.show();

	var curAttrList = $("input[name='dummyAttrKey']");
	var content = "";
	if(curAttrList==null || curAttrList.size()==0){
		for(var i = 0; i< defAttrList.length;i++){
			var temp =  attrTemplateA+ defAttrList[i]+attrTemplateB;

			content += temp;
		}
	}else {
		var curAttrArray = new Array();
		$.each(curAttrList,function (index, curAttr) {
			curAttrArray.push(	$(curAttr).val());
		});
		for(var i = 0 ;i < defAttrList.length;i++){
			if(curAttrArray.contains(defAttrList[i])){
				continue;
			}
			var temp =  attrTemplate;
			var rep = defAttrList[i];
			console.log("temp:"+temp);
			console.log("defAttrList[i]",defAttrList[i]);
			var temp =  attrTemplateA+ defAttrList[i]+attrTemplateB;

			content += temp;
		}

	}
	$('#dummyAttrList-custom').append(content);
}


var delDUMMYItemDefaultAttribute = function () {
	var info_div=$("#dummyItemAttrInfo-div");
	info_div.hide();
    //
	// var curAttrList = $("input[name='dummyAttrKey']");
	// if(curAttrList==null || curAttrList.size()==0){
	// return;
	// }else {
	// 	$.each(curAttrList,function (index, curAttr) {
	// 		if(defAttrList.contains($(curAttr).val())){
	// 			$(curAttr).parent().parent().remove();
	// 		}
	// 	});
    //
	// }

}



/**
 * 检查是否为整数
 */
function checkIntNum(txt){
	var numReg = /^[-+]?\d*$/;
	return numReg.test(txt);
}

function getSkuSupplier(){
	var supplierIdArray = new Array();
	$('.skuList').each(function(){
		var supplierId = $(this).find('[name=spId]').val().trim();
		supplierIdArray.push(supplierId);
	});
	return supplierIdArray;
}


function validateDetailForm(){
	
	//品类校验
	var largelCVal = $("#largeIdSel ").val()
	if(largelCVal == ""){
		alert("商品大类不能为空");	
		return false;
	}
	var midCVal = $("#mediumIdSel ").val()
	if(midCVal == ""){
		alert("商品中类不能为空");	
		return false;
	}
	var smlCVal = $("#smallIdSel ").val()
	if(smlCVal == ""){
		alert("商品小类不能为空");	
		return false;
	}
	
	var brand = $("#brandIdSel").val();
	if(brand == ""){
		alert("品牌不能为空！");
		return false;
	}
	var unit = $("#unitId").val();
	if(unit == ""){
		alert("单位不能为空");
		return false;
	}
	
	if($('#wavesSignId').val() == "2"){
		var countryId = $("#countryId").val();
		  if(countryId==null||countryId==""){
			  alert("海淘商品，请选择产地");
			  return false;
		  }
		  
		var  tarrifRate =  $("#tarrifRate").val();
		  if(tarrifRate==null||tarrifRate==""){
			  alert("海淘商品，请选择行邮税税率"); 
			  return false;
		  }
	  var  customsRate =  $("#customsRate").val();
	  if(customsRate==null||customsRate==""){
		  layer.alert("海淘商品,请选择关税税率"); 
		  return false;
	  }
	  var  exciseRate =  $("#exciseRate").val();
	  if(exciseRate==null||exciseRate==""){
		  layer.alert("海淘商品,请选择消费税税率"); 
		  return false;
	  }
	  var  addedValueRate =  $("#addedValueRate").val();
	  if(addedValueRate==null||addedValueRate==""){
		  layer.alert("海淘商品,请选择增值税税率"); 
		  return false;
	  }
	}
	
	
	//form校验 
	//1. 商品如果存在“有效期“管理，必须输入有效期的月数
	var t = $('#selectExpSign').val();
	if(t==1){
		if($.trim($('#inputExpDays').val()).isEmpty()){
			alert("商品如果存在有效期管理，必须输入有效期的月数");
			return false;
		}
		if(!/^[0-9]+(.[0-9]{2})?$/.test($.trim($('#inputExpDays').val()))){
			alert("有效期的月数只能为正整数或者浮点数");
			return false;
		}
	}
	
	
	if($.trim($('#itemTitle').val()).isEmpty()){
		alert("商品名称不能为空");
		return false;
	}
	if($.trim($('#mainTitle').val()).isEmpty()){
		alert("网站显示名称不能为空");
		return false;
	}
	if($.trim($('#storageTitle').val()).isEmpty()){
		alert("仓库名称不能为空");
		return false;
	}
	if($.trim($('#subTitle').val()).isEmpty()){
		alert("商品卖点不能为空");
		$('#subTitle').focus();
		//$('#subTitle').css('border','1px solid red');
		return false;
	}
	if($.trim($('#basicPrice').val()).isEmpty()){
		alert("市场价不能为空");
		return false;
	}
	
	if($.trim($('#returnDays').val()).isEmpty()){
		alert("无理由退货天数不能为空");
		return false;
	}
	
	if(!/^[+]?\d*$/.test($.trim($("#returnDays").val()))){
		alert("无理由退货天数为整数");
		return false;
	}
	if($.trim($('#weight').val()).isEmpty()){
		alert("毛重不能为空");
		return false;
	}
	
	if($.trim($('#applyAgeId').val()).isEmpty()){
		alert("适用年龄不能为空");
		return false;
	}
	
	if($.trim($('#weightNet').val()).isEmpty()){
		alert("净重不能为空");
		return false;
	}
	if($.trim($('#unitQuantity').val()).isEmpty()){
		alert("商品净数量不能为空");
		return false;
	}
	if($.trim($('#wrapQuantity').val()).isEmpty()){
		alert("商品独立包装数量不能为空");
		return false;
	}
	/*
	
	if($.trim($('#weightNet').val()).isEmpty()){
		alert("净重不能为空");
		return false;
	}
	if($.trim($('#volumeLength').val()).isEmpty()){
		alert("体积长不能为空");
		return false;
	}
	if($.trim($('#volumeWidth').val()).isEmpty()){
		alert("体积宽不能为空");
		return false;
	}
	if($.trim($('#volumeHigh').val()).isEmpty()){
		alert("体积高不能为空");
		return false;
	}
	if($.trim($('#specifications').val()).isEmpty()){
		alert("规格不能为空");
		$('#specifications').focus();
		//$('#subTitle').css('border','1px solid red');
		return false;
	}
	if($.trim($('#cartonSpec').val()).isEmpty()){
		alert("箱规不能为空");
		$('#cartonSpec').focus();
		//$('#subTitle').css('border','1px solid red');
		return false;
	}*/
	if($.trim($('#barcode').val()).isEmpty()){
		alert("条码不能为空");
		$('#barcode').focus();
		return false;
	}
	if(!$.trim($('#basicPrice').val()).isEmpty()&&!/^(([0-9]+.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($.trim($('#basicPrice').val()))){
		alert("市场价只能为正整数或者浮点数");
		return false;
	}
	
	if(!$.trim($('#weight').val()).isEmpty()&&!/^(([0-9]+.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($.trim($('#weight').val()))){
		alert("毛重只能为正整数或者浮点数");
		return false;
	}
	
	if(!$.trim($('#weightNet').val()).isEmpty()&&!/^(([0-9]+.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($.trim($('#weightNet').val()))){
		alert("净重只能为正整数或者浮点数");
		return false;
	}
	if(!$.trim($('#volumeLength').val()).isEmpty()&&!/^(([0-9]+.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($.trim($('#volumeLength').val()))){
		alert("体积长只能为正整数或者浮点数");
		return false;
	}
	if(!$.trim($('#volumeWidth').val()).isEmpty()&&!/^(([0-9]+.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($.trim($('#volumeWidth').val()))){
		alert("体积宽只能为正整数或者浮点数");
		return false;
	}
	if(!$.trim($('#volumeHigh').val()).isEmpty()&&!/^(([0-9]+.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($.trim($('#volumeHigh').val()))){
		alert("体积高只能为正整数或者浮点数");
		return false;
	}
	var checkSku = true;
	$("input[name='sort']").each(function(){
		if($.trim($(this).val()).isEmpty()){
			alert("序号不能为空");
			checkSku = false;
			return false;
		}
		if(!/^[-+]?\d*$/.test($.trim($(this).val()))){
			alert("序号只能为正整数");
			checkSku = false;
			return false;
		}
	});
	
	$("input[name='skuBasicPrice']").each(function(){
		if($.trim($(this).val()).isEmpty()){
			alert("市场价不能为空");
			checkSku = false;
			return false;
		}
		if(!/^(([0-9]+.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($.trim($(this).val()))){
			alert("市场价只能为正整数或者浮点数");
			checkSku = false;
			return false;
		}
	});
	if(!checkSku){
		return false;
	}
	
	var checkAttrFlag = true;
	//绑定属性组 attribute
	$('.attrItem').each(function(){
		if($(this).find("select").length==0){
			var $this = $(this);
			var flag = 0 ; 
			$this.find(".attributeValueClass").each(function(){
				if($(this).attr('isRequired')&&!$(this).prop('checked')){
					flag++;
				}
			});
			
			if(flag===$this.find(".attributeValueClass").length){
				checkAttrFlag = false;
				return false;
			}
			
		}   
	});
	
	if(!checkAttrFlag){
		alert("（必选的）属性不能为空");
		return false;
	}
	//
	editor.sync(); 
	var editorVal = $.trim($("#editor").val());
	if(editorVal==""){
	    alert("请填写PC模板的内容,PC模板不能为空");
	    return ;
	}
	
	mobileEditor.sync(); 
	var mobileEditorVal = $.trim($("#mobileEditor").val());
	if(mobileEditorVal==""){
		alert("请填写手机模板的内容,手机模板不能为空");
		return false ;
	}
	//服务商品校验有效期
	if($("select[name='itemDetail.itemType']").val()=="2") {
		var effectTimeStart = $("input[name='effectTimeStart']").val();
		var effectTimeEnd = $("input[name='effectTimeEnd']").val();
		if (effectTimeStart == null || effectTimeStart == "" || effectTimeEnd == null || effectTimeEnd == "") {
			layer.alert("请输入服务商品的有效期");
			return false;
		}
	}
	
	//attribute提示
	//图片提示
	return true;
}
