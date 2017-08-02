var ADD_TOPIC_ITEM = domain + "/topicItem/{topicId}/addItem/{spu}/{supplierId}/{sortIndex}";
var EDIT_TOPIC_ITEM = domain + "/topicItem/{topicId}/editItem/{topicItemId}";
var REMOVE_TOPIC_ITEM = domain + "/topicItem/{topicId}/removeItem";
var UPLOAD_TOPIC_ITEM_PIC = domain +"/topicItem/{topicItemIndex}/upload/Image";
var UPLOAD_TOPIC_PIC = domain +"/topic/upload/Image/{topicControlName}";
var PASTE_TOPIC_ITEMS = domain + "/topicItem/{topicId}/pasteItems/{sortIndex}";
var SEARCH_TOPIC_BRAND = domain + "/topic/brand/query";
var SEARCH_TOPIC_SUPPLIER = domain + "/topic/supplier/query";
var CONFIRM_TOPIC_BRAND = domain + "/topic/brand/confirm?";
var CONFIRM_TOPIC_SUPPLIER = domain + "/topic/supplier/confirm?";
var UPLD_IMG = domain + "/statics/backend/images/wait_upload.png";
var LOCK_TOPIC_ITEM = domain + "/topicItem/lockItem";
var RELEASE_TOPIC_ITEM = domain + "/topicItem/releaseItem";
var QUERY_SAME_SKU_PRICE = domain + "/topicItem/querySameSkuPrice";
var UPLOAD_COUPON = domain + "/topicCoupon/{couponIndex}/uploadImg";
var TOPIC_TAB = "tabli_56";
var TOPIC_CHANGE_TAB = "tabli_56";

var swfu;
var deleteItems = new Array();

//var editorM;
//var editorPC;

$(document).ready(function() {

	formInit();
	
	querySameSkuPrice();
});



function initControl(){
	var today = new Date();
	var model = $("#viewModel").val();
	$("input.dateInput").focus(function(){
		var fmt = jQuery(this).attr("datafmt");
		//var defaultFormat = 'yyyy-MM-dd HH:00:00';
		var defaultFormat = 'yyyy-MM-dd';
		if(!fmt){
			fmt = defaultFormat;
		}
		WdatePicker({skin:'default',dateFmt:fmt});
	});
	$("#saveTopic").on("click", function() {
		var isSubmit = false;
		if(!isSubmit){
			if(!checkSameItemSku()){
				return;
			}
			var index = layer.load("请稍等...");
			prepareData();
			var ajax_url = SAVE_TOPIC; //表单目标 
		    var ajax_type = "POST"; //提交方法 
		    var ajax_data = $("#submitTopic").serialize(); //表单数据 
		    isSubmit = true;
		    $.ajax({ 
			     type:ajax_type, //表单提交类型 
			     url:ajax_url, //表单提交目标 
			     data:ajax_data, //表单数据
			     dataType:'html',
			     success:function(msg){
			    	 var data = eval("("+msg+")");
			    	 if(data.success){
			    		 layer.alert("保存成功", 1, function(){
			    			 formReload();	 
			    		 });
			    		 //layer.alert("保存成功!", 1);
			    		 //parent.window.closeTab("editTopic");
			    		 //window.location.href = domain + data.data;
			    	 } else {
			    		 layer.close(index);
			    		 layer.alert(data.msg.message);
			    	 }
			     },
				error : function(data) {
					layer.close(index);
					layer.alert("保存失败!");
				}
		    });
		}
	});
	$("#submTopic").off("click");
	$("#submTopic").on("click", function() {
		var isSubmit = false;
		if(!isSubmit){
			var index = layer.load("请稍等...");
			prepareData();
			if (!validateFormInfo()) {
				layer.close(index);
				return;
			}
			var ajax_url = SUBMIT_TOPIC; //表单目标 
		    var ajax_type = "POST"; //提交方法 
		    var ajax_data = $("#submitTopic").serialize(); //表单数据 

		    isSubmit = true;
		    $.ajax({ 
			     type:ajax_type, //表单提交类型 
			     url:ajax_url, //表单提交目标 
			     data:ajax_data, //表单数据
			     dataType:'json',
			     success:function(result){
			    	 if(result.success){
						 closeTab();
			    		//window.location.href = domain + data.data; 
			    	 } else {
				    	 layer.close(index);
				    	 if(result.msg.message=="system"){
				    		 layer.alert(result.msg.detailMessage, 8);
				    	 }else{
				    		 layer.alert(result.msg.message, 8);
				    	 }
				    	 
			    	 }
			     },
				error : function(data) {
					layer.close(index);
					layer.alert("提交失败!");
				}
		    });
		}
	});
	$("#cancel").on("click", function() {
		closeTab();
		//if(model == "view"){
		//	parent.window.closeTab(currentIfame);
		//} else {
		//	parent.window.closeTab(currentIfame);
		//}
		//window.location.href = TOPIC_SEARCH_PAGE;
	});

	function closeTab(){
		var currentIfame = self.frameElement.getAttribute('id');
		if(currentIfame){
			currentIfame = currentIfame.toString().replace("mainIframe_","");
		}
		var type = $("#page-type-info").val();
		if(type && type =='topic'){
			var tab ={};
			tab.url = "/topic/load.htm";
			tab.tabId="topic-load";
			tab.text = "专场活动列表";
			tab.linkId = tab.id+"_link";
			window.parent.showTab(tab);
			window.parent.closeTab(currentIfame);
		}else {
			var tab ={};
			tab.url = "/topicChange/load.htm";
			tab.tabId="topic-change-load";
			tab.text = "专场活动列变更单";
			tab.linkId = tab.id+"_link";
			window.parent.showTab(tab);
			window.parent.closeTab(currentIfame);
		}
	}


	$("#addTopicItem").on("click", function() {
		var topicId = $("#topicId").val();
		var type = $("#typeValue").val();
		var bid = $("#brandId").val();
		var bName = $("#brandName").val();
		var suppId = $("#supplierId").val();
		var suppName = $("#supplierName").val();
		var sortIndex = "0";
		if(null != type){
			if(2 == parseInt(type) && (null == bid || "" == $.trim(bid) || 0 == parseInt(bid) || 
					null == bName || "" == $.trim(bName))){
				layer.alert("品牌团必须先选择商品品牌, 才能添加商品");
				return;
			}

			if(5 == parseInt(type) && (null == suppId || "" == $.trim(suppId) || 0 == parseInt(suppId) ||
				null == suppName || "" == $.trim(suppName))){
				layer.alert("商家店铺必须先选择商家, 才能添加商品");
				return;
			}

		}
		//TODO:获取最大的排序序号
		var sortIntexMaxRow = $("#topicItemsList tr:last").find("input[name='sortIndex']");
		if(null != sortIntexMaxRow && sortIntexMaxRow.length > 0){
			sortIndex = sortIntexMaxRow.val();
		}
		var formatUrl = ADD_TOPIC_ITEM.replace("{topicId}", topicId);
		formatUrl = formatUrl.replace("{sortIndex}", sortIndex);
		if(null == type){
			layer.alert("专场类型不正确");
			return;
		}
		if(1 == parseInt(type)){
			formatUrl = genTopicItemAddUrl(formatUrl);
		}else if( 5== parseInt(type)){
			formatUrl = formatUrl.replace("{supplierId}", suppId);
			formatUrl = formatUrl.replace("{spu}", "0");
		}
		else{
			formatUrl = formatUrl.replace("{spu}", "0");
			formatUrl = formatUrl.replace("{supplierId}", "-1");
		}
		$.layer({
			type : 2,
			title : "新增专场商品",
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '800px', 750 ],
			iframe : {
				src : formatUrl
			},
			end : function(){
				addTopicItemOperationScript();
			}
		});
	});
	$("#pasteTopicItem").on("click", function() {
		var topicId = $("#topicId").val();
		var type = $("#typeValue").val();
		var bid = $("#brandId").val();
		var bName = $("#brandName").val();
		var suppId = $("#supplierId").val();
		var suppName = $("#supplierName").val();
		var sortIndex = "0";
		if(null != type){
			if(2 == parseInt(type) && (null == bid || "" == $.trim(bid) || 0 == parseInt(bid) || 
					null == bName || "" == $.trim(bName))){
				layer.alert("品牌团必须先选择商品品牌, 才能添加商品");
				return;
			}
			if(5 == parseInt(type) && (null == suppId || "" == $.trim(suppId) || 0 == parseInt(suppId) ||
				null == suppName || "" == $.trim(suppName))){
				layer.alert("商家店铺必须先选择商家, 才能添加商品");
				return;
			}
		}
		var sortIntexMaxRow = $("#topicItemsList tr:last").find("input[name='sortIndex']");
		if(null != sortIntexMaxRow && sortIntexMaxRow.length > 0){
			sortIndex = sortIntexMaxRow.val();
		}
		var url = PASTE_TOPIC_ITEMS.replaceAll("{topicId}", topicId);
		url = url.replaceAll("{sortIndex}", sortIndex);
		$.layer({
			type : 2,
			scorll : false,
			title : "粘贴输入",
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '600px', 500 ],
			iframe : {
				src : url
			},
			end : function(){
				addTopicItemOperationScript();
			}
		});
	});
	addTopicItemOperationScript();
	addCouponClickEventOperation();
	$("#registerTime").on("click", function() {
		$("#userRegisterStartTime").attr("disabled", !$(this)[0].checked);
		$("#userRegisterEndTime").attr("disabled", !$(this)[0].checked);
	});
	$("#startTime").on("change", function() {
		$("#endTime").val("");
	});

	$("#areaAll").on("click", function() {
		$("#areaEastChina")[0].checked = $(this)[0].checked;
		$("#areaNorthChina")[0].checked = $(this)[0].checked;
		$("#areaCentChina")[0].checked = $(this)[0].checked;
		$("#areaSoutChina")[0].checked = $(this)[0].checked;
		$("#areaNortheastChina")[0].checked = $(this)[0].checked;
		$("#areaNorthwestChina")[0].checked = $(this)[0].checked;
		$("#areaSouthwestChina")[0].checked = $(this)[0].checked;
	});
	if(null != $("#areaAll")[0] && $("#areaAll")[0].checked){
		$("#areaEastChina")[0].checked = true;
		$("#areaNorthChina")[0].checked = true;
		$("#areaCentChina")[0].checked = true;
		$("#areaSoutChina")[0].checked = true;
		$("#areaNortheastChina")[0].checked = true;
		$("#areaNorthwestChina")[0].checked = true;
		$("#areaSouthwestChina")[0].checked = true;
	}
	$("#areaEastChina").on("click", function() {
		if (!$(this)[0].checked) {
			$("#areaAll")[0].checked = false;
		}
	});
	$("#areaNorthChina").on("click", function() {
		if (!$(this)[0].checked) {
			$("#areaAll")[0].checked = false;
		}
	});
	$("#areaCentChina").on("click", function() {
		if (!$(this)[0].checked) {
			$("#areaAll")[0].checked = false;
		}
	});
	$("#areaSoutChina").on("click", function() {
		if (!$(this)[0].checked) {
			$("#areaAll")[0].checked = false;
		}
	});
	$("#areaNortheastChina").on("click", function() {
		if (!$(this)[0].checked) {
			$("#areaAll")[0].checked = false;
		}
	});
	$("#areaNorthwestChina").on("click", function() {
		if (!$(this)[0].checked) {
			$("#areaAll")[0].checked = false;
		}
	});
	$("#areaSouthwestChina").on("click", function() {
		if (!$(this)[0].checked) {
			$("#areaAll")[0].checked = false;
		}
	});

	$("#platAll").on("click", function() {
		$("#platPc")[0].checked = $(this)[0].checked;
		$("#platApp")[0].checked = $(this)[0].checked;
		$("#platWap")[0].checked = $(this)[0].checked;
		$("#platHapPreg")[0].checked = $(this)[0].checked;
	});
	if(null != $("#platAll")[0] && $("#platAll")[0].checked){
		$("#platPc")[0].checked = true;
		$("#platApp")[0].checked = true;
		$("#platWap")[0].checked = true;
		$("#platHapPreg")[0].checked = true;
	}
	$("#platPc").on("click", function() {
		if (!$(this)[0].checked) {
			$("#platAll")[0].checked = false;
		}
	});
	$("#platApp").on("click", function() {
		if (!$(this)[0].checked) {
			$("#platAll")[0].checked = false;
		}
	});
	$("#platWap").on("click", function() {
		if (!$(this)[0].checked) {
			$("#platAll")[0].checked = false;
		}
	});
	$("#platHapPreg").on("click", function() {
		if (!$(this)[0].checked) {
			$("#platAll")[0].checked = false;
		}
	});
	$("#updPriceBt").on("click", function() {
		var updPrice = $(this).closest("tr").find("#updPrice").val();
		var expMoney = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
		if (null == updPrice || $.trim(updPrice).length == 0 || !expMoney.test(updPrice) || 0 > parseFloat(updPrice)) {
			layer.alert("请输入有效金额!");
			return;
		}
		$("#topicItemsList").find("tr input[name='topicPrice']").val(updPrice);
	});
	$("#updAmountBt").on("click", function() {
		var updAmount = $(this).closest("tr").find("#updAmount").val();
		if (null == updAmount || $.trim(updAmount).length == 0 || 0 > parseInt(updAmount)) {
			layer.alert("请输入有效数量!");
			return;
		}
		if (99 < parseInt(updAmount)) {
			layer.alert("限购数量必须小于99!");
			return;
		}
		$("#topicItemsList").find("tr input[name='limitAmount']").val(updAmount);
	});
	$("#updTotalAmountBt").on("click", function() {
		var errorTotal = false;
		var errorAmount = false;
		var updTotalAmount = $(this).closest("tr").find("#updTotalAmount").val();
		if (null == updTotalAmount || $.trim(updTotalAmount).length == 0 || 0 > parseInt(updTotalAmount)) {
			layer.alert("请输入有效数量!");
			return;
		}
		$(".topicItemData").each(function(){
			if(false){
			//if($(this).find("input[name='limitTotal']").not("[readonly]").length > 0){
				var supplierId = $(this).find("input[name='topicItemSupplierId']").val();
				var amount = $(this).find("span")[6].innerHTML;
				//var limitTotal = $(this).find("input[name='limitTotal']").not("[readonly]").val();
				var limitAmount = $(this).find("input[name='limitAmount']").val();
//				if(null == limitTotal || "" == $.trim(limitTotal) || 0 > parseInt(limitTotal)){
//					layer.alert("请输入有效的限购总量!");
//					return;
//				}
				if(null == limitAmount || "" == $.trim(limitAmount) || 0 > parseInt(limitAmount)){
					layer.alert("请输入有效的限购数量!");
					return;
				}
//				// 新增行校验
//				if("0" == supplierId){
//					if(null != limitTotal && "" != limitTotal && parseInt(amount) < parseInt(limitTotal)){
//						errorTotal = true;
//						$(this).find("input[name='limitTotal']").val(amount);
//						limitTotal = amount;
//					}
//				}
				
//				if(parseInt(limitTotal) < parseInt(limitAmount)){
//					errorAmount = true;
//					$(this).find("input[name='limitAmount']").val(limitTotal);
//				} else {
//					$(this).find("input[name='limitTotal']").val(updTotalAmount);
//				}
			}
		});
		if(errorTotal && errorAmount){
			layer.alert("限制总库存不能大于现有库存，并且限制数量不能大于限制总库存!");
			return;
		}
		if(errorTotal && !errorAmount){
			layer.alert("限制总库存不能大于现有库存!");
			return;
		}
		if(!errorTotal && errorAmount){
			layer.alert("限制数量不能大于限制总库存!");
			return;
		}
	});
	//查看pc专场内容
	$("#previewPcContent").on("click",function(){
		var pcContent = pcEditor.html();
		if(null == pcContent || "" == $.trim(pcContent)){
			layer.alert("请输入电脑专场内容!");
			return;
		}
		$.layer({
			type : 1,
			title : "查看PC端活动专场内容",
			shadeClose : true,
			maxmin : true,
			fix : false,
			area: ['800px', 600],
			page: {
				html : pcContent
			}
		});
	});
	//查看phone专场内容
	$("#previewPhoneContent").on("click",function(){
		var phoneContent = phoneEditor.html();
		if(null == phoneContent || "" == $.trim(phoneContent)){
			layer.alert("请输入手机专场内容!");
			return;
		}
		$.layer({
			type : 1,
			title : "查看移动端活动专场内容",
			shadeClose : true,
			maxmin : true,
			fix : false,
			area: ['800px', 600],
			page: {
				html : phoneContent
			}
		});
	});
	//查看pc专场内容
	$("#previewPcContentView").on("click",function(){
		var pcContent = $("#pcContentEditor").val();
		if(null == pcContent || "" == $.trim(pcContent)){
			return;
		}
		$.layer({
			type : 1,
			title : "查看活动专场内容",
			shadeClose : true,
			maxmin : true,
			fix : false,
			area: ['800px', 600],
			page: {
				html : pcContent
			}
		});
	});
	//查看phone专场内容
	$("#previewPhoneContentView").on("click",function(){
		var phoneContent = $("#phoneContentEditor").val();
		if(null == phoneContent || "" == $.trim(phoneContent)){
			return;
		}
		$.layer({
			type : 1,
			title : "查看活动专场内容",
			shadeClose : true,
			maxmin : true,
			fix : false,
			area: ['800px', 600],
			page: {
				html : phoneContent
			}
		});
	});
	//格式化图片,合并服务器ip和图片后缀地址
	$("img[name='selectImage']").each(function(){
		var srcUrl = $(this).attr("key");
		if(null == srcUrl || "" == $.trim(srcUrl)){
			$(this).attr("src",UPLD_IMG);
		} else {
			$(this).attr("src",srcUrl);
		}
	});
	//格式化优惠券图片,合并服务器ip和图片后缀地址
	$("img[name='selectCouponImage']").each(function(){
		var srcUrl = $(this).attr("key");
		if(null == srcUrl || "" == $.trim(srcUrl)){
			$(this).attr("src",UPLD_IMG);
		} else {
			$(this).attr("src",srcUrl);
		}
	});
	//PC图片
	if(null != $("#pcImageImg")[0]){
		var src = $("#pcImageImg").attr("src");
		if(null == src || "" == $.trim(src)){
			$("#pcImageImg")[0].src = UPLD_IMG;
		}
	}
	//New图片
	if(null != $("#newImageImg")[0]){
		var src = $("#newImageImg").attr("src");
		if(null == src || "" == $.trim(src)){
			$("#newImageImg")[0].src = UPLD_IMG;
		}
	}
	//PHONE图片
	if(null != $("#phoneImageImg")[0]){
		var src = $("#phoneImageImg").attr("src");
		if(null == src || "" == $.trim(src)){
			$("#phoneImageImg")[0].src = UPLD_IMG;
		}
	}
	//可能感兴趣图片
	if(null != $("#interestedImage")[0]){
		var src = $("#interestedImageImg").attr("src");
		if(null == src || "" == $.trim(src)){
			$("#interestedImageImg")[0].src = UPLD_IMG;
		}
	}
	//海淘首页图片
	if(null != $("#hitaoImage")[0]){
		var src = $("#hitaoImageImg").attr("src");
		if(null == src || "" == $.trim(src)){
			$("#hitaoImageImg")[0].src = UPLD_IMG;
		}
	}
	
	//新图片
	//PC图片
	if(null != $("#pcImageNImg")[0]){
		var src = $("#pcImageNImg").attr("src");
		if(null == src || "" == $.trim(src)){
			$("#pcImageNImg")[0].src = UPLD_IMG;
		}
	}
	//Mall图片
	if(null != $("#mallImageNImg")[0]){
		var src = $("#mallImageNImg").attr("src");
		if(null == src || "" == $.trim(src)){
			$("#mallImageNImg")[0].src = UPLD_IMG;
		}
	}
	//PHONE图片
	if(null != $("#mobileImageNImg")[0]){
		var src = $("#mobileImageNImg").attr("src");
		if(null == src || "" == $.trim(src)){
			$("#mobileImageNImg")[0].src = UPLD_IMG;
		}
	}
	//可能感兴趣图片
	if(null != $("#interestedImageN")[0]){
		var src = $("#interestedImageNImg").attr("src");
		if(null == src || "" == $.trim(src)){
			$("#interestedImageNImg")[0].src = UPLD_IMG;
		}
	}
	//海淘首页图片
	if(null != $("#hitaoImageN")[0]){
		var src = $("#hitaoImageNImg").attr("src");
		if(null == src || "" == $.trim(src)){
			$("#hitaoImageNImg")[0].src = UPLD_IMG;
		}
	}
	$("#salesPartten").on("change",function(){
		if(null != $(this)){
			$("#spValue").val($("#salesPartten").val());
			spChangeEvent($(this).val());
		}
	});
	$("#type").on("change",function(){
		if(null != $(this)){
			$("#typeValue").val($("#type").val());
			typeChangeEvent($(this).val());
		}
	});
	
	//确定品牌
	$("#confirmBrand").on("click",function(){
		confirmBrandInfo();
	});
	//查询品牌
	$("#searchBrand").on("click",function(){

		var type = $("#type").val();
		if("5"==type){
			//load supplier info
			$.layer({
				type : 2,
				title : "查询商家信息",
				shadeClose : true,
				maxmin : true,
				fix : false,
				area : [ '600px', 450 ],
				iframe : {
					src : SEARCH_TOPIC_SUPPLIER
				},
				end : function(){
					//confirmBrandInfo();
				}
			});
		}else {
			$.layer({
				type : 2,
				title : "查询品牌信息",
				shadeClose : true,
				maxmin : true,
				fix : false,
				area : [ '600px', 450 ],
				iframe : {
					src : SEARCH_TOPIC_BRAND
				},
				end : function(){
					confirmBrandInfo();
				}
			});
		}


	});
	// pc端图片上传
	$("#pcImageImg").on("click",function(){
		topicClickImgBind("pcImage","查看PC端专场图片");
	});
	$("#removePcImage").on("click",function(){
		$("#pcImage").val("");
		$("#pcImageImg").attr("src", UPLD_IMG);
	});
	// pc端明日预告图片上传
	$("#newImageImg").on("click",function(){
		topicClickImgBind("newImage","查看明日预告图片");
	});
	$("#removeNewImage").on("click",function(){
		$("#newImage").val("");
		$("#newImageImg").attr("src", UPLD_IMG);
	});
	// app端图片上传
	$("#phoneImageImg").on("click",function(){
		topicClickImgBind("phoneImage","查看移动端专场图片");
	});
	$("#removePhoneImage").on("click",function(){
		$("#phoneImage").val("");
		$("#phoneImageImg").attr("src", UPLD_IMG);
	});
	// PC端感兴趣图片上传
	$("#interestedImageImg").on("click",function(){
		topicClickImgBind("interestedImage","查看可能感兴趣图片");
	});
	$("#removeInterestedImage").on("click",function(){
		$("#interestedImage").val("");
		$("#interestedImageImg").attr("src", UPLD_IMG);
	});
	// PC端海淘专场图片上传
	$("#hitaoImageImg").on("click",function(){
		topicClickImgBind("hitaoImage","查看海淘专场图片");
	});
	$("#removeHitaoImage").on("click",function(){
		$("#hitaoImage").val("");
		$("#hitaoImageImg").attr("src", UPLD_IMG);
	});
	
	// 新版图片
	// pc端图片上传
	$("#pcImageNImg").on("click",function(){
		topicClickImgBind("pcImageN","查看PC端专场图片");
	});
	$("#removePcImageN").on("click",function(){
		$("#pcImageN").val("");
		$("#pcImageNImg").attr("src", UPLD_IMG);
	});
	// pc端MALL图片上传
	$("#mallImageNImg").on("click",function(){
		topicClickImgBind("mallImageN","查看明日预告图片");
	});
	$("#removeMallImageN").on("click",function(){
		$("#mallImageN").val("");
		$("#mallImageNImg").attr("src", UPLD_IMG);
	});
	// app端图片上传
	$("#mobileImageNImg").on("click",function(){
		topicClickImgBind("mobileImageN","查看移动端专场图片");
	});
	$("#removeMobileImageN").on("click",function(){
		$("#mobileImageN").val("");
		$("#mobileImageNImg").attr("src", UPLD_IMG);
	});
	// PC端感兴趣图片上传
	$("#interestedImageNImg").on("click",function(){
		topicClickImgBind("interestedImageN","查看可能感兴趣图片");
	});
	$("#removeInterestedImageN").on("click",function(){
		$("#interestedImageN").val("");
		$("#interestedImageNImg").attr("src", UPLD_IMG);
	});
	// PC端海淘专场图片上传
	$("#hitaoImageNImg").on("click",function(){
		topicClickImgBind("hitaoImageN","查看海淘专场图片");
	});
	$("#removeHitaoImageN").on("click",function(){
		$("#hitaoImageN").val("");
		$("#hitaoImageNImg").attr("src", UPLD_IMG);
	});
	
	if("view" != model){
		if($("#topicItemsList tr").length > 2){
			$("#updTotalAmountDiv")[0].style.display="none";
		}
	}
	
	$(".topicInteger").integer();
	$(".topicNumber").numeral();
	spLoadEvent();
	typeLoadEvent();
}

function confirmBrandInfo(){


	var type = $("#type").val();
	if(type=="2"){
		var bid = $("#brandId").val();
		if(null == bid || "" == $.trim(bid)){
			return;
		}
		var data=syncGet(CONFIRM_TOPIC_BRAND + new Date().getTime(), {
			brandId : bid
		})
		if(null != data && data.success){
			if(null == data.data){
				layer.alert("没有品牌信息",2);
			}else{
				$("#brandName").val(data.data.name);
			}
		}else{
			layer.alert("品牌信息查询失败",2);
		}
	}else if(type=="5"){
		var suppId = $("#supplierId").val();
		var data=syncGet(CONFIRM_TOPIC_SUPPLIER + new Date().getTime(), {
			supplierId : suppId
		})
		if(null != data && data.success){
			if(null == data.data){
				layer.alert("没有商家信息",2);
			}else{
				$("#supplierName").val(data.data.name);
				querySupplierShop(suppId)
			}
		}else{
			layer.alert("商家信息查询失败",2);
		}
	}


	
}

function prepareData() {
	var parentBrandId = $("#brandId").val();
	if(null == parentBrandId || "" == $.trim(parentBrandId)){
		$("#brandName").val("");
	}
	topicItemsInfo();
	topicRelates();
	topicCoupons();
}

function topicRelates() {
	var relateList = new Array();
	$("#topicRelationsList").find("input[name='secondTopicId']").each(
			function() {
				relateList.push($(this).val());
			});
	var relates = JSON.stringify(relateList);
	var removeRelates = JSON.stringify(relateRemoveList);
	$("#relateIds").val(relates);
	$("#removeRelateIds").val(removeRelates);
}

function topicCoupons(){
	if(!isCouponTopic()){
		clearCouponList();
		var removeCouponInfos = JSON.stringify(couponRemoveList);
		$("#removeCouponIds").val(removeCouponInfos);
		return;
	}
	var coupons = new Array();
	$("#topicCouponsList .topicCouponList").each(
		function() {
			var topicCouponObj = new Object();
			topicCouponObj.id = $(this).find("input[name='topicCouponId']")
								.val();
			topicCouponObj.sortIndex = $(this).find("input[name='couponSortIndex']")
										.val();
			topicCouponObj.couponId = $(this).find("input[name='couponId']")
									  .val();
			topicCouponObj.couponImage = $(this).find("input[name='couponImage']")
					.val();
			topicCouponObj.couponSize = $(this).find("select[name='couponSize']")
					.val();
			coupons.push(topicCouponObj);
		});
	var couponInfos = JSON.stringify(coupons);
	$("#topicCoupons").val(couponInfos);
	var removeCouponInfos = JSON.stringify(couponRemoveList);
	$("#removeCouponIds").val(removeCouponInfos);
}

function validateFormInfo() {
	var today = new Date();
	var type = $("#typeValue").val();
	var parentBrandId = $("#brandId").val();
	var parentSupplierId = $("#supplierId").val();
	var lastingType = $("input[name='topic.lastingType']:checked").val();
	var name = $("#name").val();
	var number = $("#number").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var byRegisterTime = $("#registerTime").val();
	var lateTime = $("#userRegisterStartTime").val();
	var earlyTime = $("#userRegisterEndTime").val();

	var pcImage = $("#pcImage").val();
	var newImage = $("#newImage").val();
	var phoneImage = $("#phoneImage").val();
	var interestedImage = $("#interestedImage").val();
	var pcImageN = $("#pcImageN").val();
	var mallImageN = $("#mallImageN").val();
	var phoneImageN = $("#mobileImageN").val();
	var interestedImageN = $("#interestedImageN").val();
	var pcContent = pcEditor.html();
	var phoneContent = phoneEditor.html();

	var pts = new Array();
	var pfs = $("input[name='platformCodes']:checked")

	for(var i = 0;i<pfs.length;i++){
		pts.push(pfs[i].value);
	}


	if(null == type || "" == $.trim(type) || 1 > parseInt(type) || 5 < parseInt(type)){
		layer.alert("【专题信息】专场类型不在有效范围内!");
		return false;
	}
	endTime = endTime.replaceAll("-", "/");
    var endTimeDate = new Date(endTime);
	if(endTimeDate < today){
		layer.alert("【专题信息】结束时间必须大于当前时间");
		return false;
	}
    
    startTime = startTime.replaceAll("-", "/");
    var startTimeDate = new Date(startTime);
	if(endTimeDate < startTimeDate){
		layer.alert("【专题信息】结束时间必须大于开始时间");
		return false;
	}
	
	if(null == name || "" == $.trim(name)){
		layer.alert("【专题信息】专场名称必填");
		return false;
	}
	
	if(null == number || "" == $.trim(number)){
		layer.alert("【专题信息】专场编号必填");
		return false;
	}

	if(null == lastingType){
		layer.alert("【专题信息】请选择专场时间");
	}
	if(null == startTime || "" == $.trim(startTime)){
		layer.alert("【专题信息】开始时间必填");
		return false;
	}

	if(null == endTime || "" == $.trim(endTime)){
		layer.alert("【专题信息】结束时间必填");
		return false;
	}

	if(null != byRegisterTime && byRegisterTime.checked){
		if(null == lateTime || "" == $.trim(lateTime)){
			layer.alert("【专题信息】限购注册开始时间必填!");
			return false;
		}
		if(null == earlyTime || "" == $.trim(earlyTime)){
			layer.alert("【专题信息】限购注册结束时间必填!");
			return false;
		}
		lateTime = lateTime.replaceAll("-", "/");
	    var lateTimeDate = new Date(lateTime);
	    earlyTime = earlyTime.replaceAll("-", "/");
	    var earlyTimeDate = new Date(earlyTime);
		if(lateTimeDate > earlyTimeDate){
			layesr.alert("【专题信息】限购注册结束时间必须大于起始时间!");
			return false;
		}
	}
	if(pts.length<1){
		layer.alert("【专题信息】平台至少选一项!");
		return false;
	}
	

	//if(null == pcImage || "" == $.trim(pcImage) ||
	//		null == phoneImage || "" == $.trim(phoneImage) ||
	//		null == interestedImage || "" == $.trim(interestedImage) ||
	//		null == newImage || "" == $.trim(newImage) ){
	//	layer.alert("【专题信息】PC端、明日预告、可能感兴趣和手机端图片必须上传!");
	//	return false;
	//}

	if(null == phoneImage || "" == $.trim(phoneImage) ){
		layer.alert("【专题信息】手机端图片必须上传!");
		return false;
	}
	if(null == phoneImageN || "" == $.trim(phoneImageN) ){
		layer.alert("移动端封面图片必须上传!");
		return false;
	}

	
	//if("1" != type && (null == pcContent || "" == $.trim(pcContent) ||
	//		null == phoneContent || "" == $.trim(phoneContent))){
	//	layer.alert("【专题信息】PC端和手机端专场介绍必填!");
	//	return false;
	//}

	if("1" != type && (null == phoneContent || "" == $.trim(phoneContent))){
		layer.alert("【专题信息】手机端专场介绍必填!");
		return false;
	}
	
	if(1 == type && !validateSingleType()){
		return false;
	}

	if(!topicSpecialValidate()){
		return false;
	}
	
	var skuArray = new Array();
	skuArray = initialSkuList(skuArray);
	var valid = true;
	var topicType = $("#type").val();
	var reserveInventoryFlag = $("input[name='reserveInventoryFlag']:checked").val()||$("input[name='reserveInventoryFlag']").val();
	//alert(reserveInventoryFlag)
	$(".topicItemData").each(function(){
		var topicItemId = $(this).find("input[name='topicItemId']").val();
		var supplierId = $(this).find("input[name='topicItemSupplierId']").val();
		var stockLocationId = $(this).find("input[name='topicItemLocationId']").val();
		//取仓库库存校验
		var amount = $(this).find("span")[6].innerHTML;
		var sku =  $(this).find("input[name='topicItemSku']").val();
		var brandId =  $(this).find("input[name='brandId']").val();
		var sortIndex =  $(this).find("input[name='sortIndex']").val();
		var limitAmount = $(this).find("input[name='limitAmount']").val();
		var limitTotal = $(this).find("input[name='limitTotal']").val();
		var topicPrice = $(this).find("input[name='topicPrice']").val();
		var salePrice = $(this).find("input[name='salePrice']").val();
		var expMoney = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
		
		var sameKey = sku;
		// + "-" + stockLocationId
		if(skuArray.length > 0){
			for(var count=0;count < skuArray.length; count++){
				if(skuArray[count] == sameKey){
					layer.alert("【专题商品】商品清单中存在相同sku的商品:"+skuArray[count]);
					valid = false;
					return;
				}
			}
		}
		skuArray.push(sameKey);
		if(topicType=="2"){
			if(null != parentBrandId && "" != $.trim(parentBrandId) && parentBrandId != brandId){
				layer.alert("【专题商品】添加的商品必须是指定品牌商品");
				valid = false;
				return;
			}
		}else if( topicType=="5"){
			if(parentSupplierId!=supplierId){
			layer.alert("【专题商品】添加的商品必须是指定商家商品" );
			valid = false;
			return;
			}
		}
		// 预留标志：1预留 0不预留
		if(reserveInventoryFlag&&reserveInventoryFlag=="1"){
			$(this).find("input[name='limitTotal']").css("background-color","white");
			if(null == limitTotal || "" == $.trim(limitTotal) || 0 > parseInt(limitTotal)){
				$(this).find("input[name='limitTotal']").css("background-color","#FFE5FC");
				layer.alert("【专题商品】序号["+sortIndex+"]请输入有效的限购总量!");
				valid = false;
				return;
			}
		}
		
		$(this).find("input[name='limitAmount']").css("background-color","white");
		if(null == limitAmount || "" == $.trim(limitAmount) || 0 > parseInt(limitAmount)){
			$(this).find("input[name='limitAmount']").css("background-color","#FFE5FC");
			layer.alert("【专题商品】序号["+sortIndex+"]请输入有效的限购数量!");
			valid = false;
			return;
		}
		$(this).find("input[name='limitAmount']").css("background-color","white");
		if(99 < parseInt(limitAmount)){
			$(this).find("input[name='limitAmount']").css("background-color","#FFE5FC");
			layer.alert("【专题商品】序号["+sortIndex+"]限购数量必须小于99!");
			valid = false;
			return;
		}
		$(this).find("input[name='topicPrice']").css("background-color","white");
		if(null == topicPrice || !expMoney.test(topicPrice) || 0 > parseFloat(topicPrice)){
			$(this).find("input[name='topicPrice']").css("background-color","#FFE5FC");
			layer.alert("【专题商品】序号["+sortIndex+"]请输入有效的限购价格!");
			valid = false;
			return;
		}
		$(this).find("input[name='salePrice']").css("background-color","white");
		if(null == salePrice || !expMoney.test(salePrice) || 0 > parseFloat(salePrice)){
			$(this).find("input[name='salePrice']").css("background-color","#FFE5FC");
			layer.alert("【专题商品】序号["+sortIndex+"]商品没有有效的销售价!");
			valid = false;
			return;
		}
		$(this).find("input[name='topicPrice']").css("background-color","white");
		if(parseFloat(salePrice) < parseFloat(topicPrice)){
			$(this).find("input[name='topicPrice']").css("background-color","#FFE5FC");
			layer.alert("【专题商品】序号["+sortIndex+"]商品的促销价不能大于销售价!");
			valid = false;
			return;
		}
		// 预留标志：1预留 0不预留
		if(reserveInventoryFlag&&reserveInventoryFlag=="1"){
			$(this).find("input[name='limitTotal']").css("background-color","white");
			// 新增行校验
			if("0" == supplierId && (topicItemId == null || $.trim(topicItemId).length == 0 || "0" == topicItemId)){
				if(null != limitTotal && "" != limitTotal && parseInt(amount) < parseInt(limitTotal) && 0 < parseInt(limitTotal)){
					$(this).find("input[name='limitTotal']").css("background-color","#FFE5FC");
					layer.alert("【专题商品】序号["+sortIndex+"]限制总库存不能大于现有库存!");
					valid =  false;
					return;
				}
			}
			$(this).find("input[name='limitAmount']").css("background-color","white");
			if(parseInt(limitTotal) < parseInt(limitAmount)){
				$(this).find("input[name='limitAmount']").css("background-color","#FFE5FC");
				layer.alert("【专题商品】序号["+sortIndex+"]限制总库存不能大于现有库存，并且限制数量不能大于限制总库存!");
				valid =  false;
				return;
			}
		}
		
		if(!topicItemSpecialValidate()){
			return valid =  false;
		}
	});
	if(!valid) {
		return valid;
	}
	var couponArray = new Array();
	$(".topicCouponList").each(function(){
		var couponId = $(this).find("input[name='couponId']").val();
		var couponIdValue = $(this).find("input[name='couponIdValue']").val();
		var couponImage = $(this).find("input[name='couponImage']").val();
		var couponSortIndex = $(this).find("input[name='couponSortIndex']").val();
		var couponSize = $(this).find("select[name='couponSize'] option:selected").val();
		
		$(this).find("input[name='couponIdValue']").css("background-color","white");
		if(couponIdValue == null || $.trim(couponIdValue).length == 0){
			$(this).find("input[name='couponIdValue']").css("background-color","#FFE5FC");
			layer.alert("【专题优惠券】优惠券序号必须填写");
			valid = false;
			return;
		}
		if(couponId == null || $.trim(couponId).length == 0 || couponIdValue != couponId){
			$(this).find("input[name='couponIdValue']").css("background-color","#FFE5FC");
			layer.alert("【专题优惠券】请点击确定,获取优惠券信息");
			valid = false;
			return;
		}
		if(couponArray.length > 0){
			for(var count=0;count < couponArray.length; count++){
				if(couponArray[count] == couponId){
					$(this).find("input[name='couponIdValue']").css("background-color","#FFE5FC");
					layer.alert("【专题优惠券】活动下存在相同的优惠券批次,优惠券序号:"+couponId);
					valid = false;
					return;
				}
			}
		}
		couponArray.push(couponId);
		$(this).find("input[name='couponSortIndex']").css("background-color","white");
		if(couponSortIndex == null || $.trim(couponSortIndex).length == 0){
			$(this).find("input[name='couponSortIndex']").css("background-color","#FFE5FC");
			layer.alert("【专题优惠券】排序必填,优惠券序号:"+couponId);
			valid = false;
			return;
		}
		if(parseInt(couponSortIndex) < 1){
			$(this).find("input[name='couponSortIndex']").css("background-color","#FFE5FC");
			layer.alert("【专题优惠券】排序必须大于1,优惠券序号:"+couponId);
			valid = false;
			return;
		}
		if(couponImage == null || $.trim(couponImage).length == 0){
			layer.alert("【专题优惠券】必须上传图片,优惠券序号:"+couponId);
			valid = false;
			return;
		}
	});
	return valid;
}

function topicClickImgBind(controlName,title){
	var picUrl = $("#" + controlName).val();
	var model=$("#viewModel").val();
	var width="100px";
	var height = 100;
	var typeValue = "0";
	if("view" == model){
		typeValue=$("#typeValue").val();
	} else {
		typeValue=$("#type").val();
	}
	if("pcImage" == controlName){
		if("1" == typeValue){
			width = "303px";
			height = 380;
		} else {
			width = "790px";
			height = 245;
		}
	} else if("newImage" == controlName){
		width = "264px";
		height = 330;
	} else if("interestedImage" == controlName){
		width = "375px";
		height = 180;
	} else if("hitaoImage" == controlName){
		width = "564px";
		height = 345;
	} else if("phoneImage" == controlName){
		if("1" == typeValue){
			width = "608px";
			height = 470;
		} else {
			width = "608px";
			height = 420;
		}
	} else if("mobileImageN" == controlName){
		if("3" == typeValue){
			width = "610px";
			height = 265;
		} else if("2" == typeValue){
			width = "715px";
			height = 312;
		} else if("1" == typeValue){
			width = "715px";
			height = 528;
		}
	} else if("interestedImageN" == controlName){
		width = "378px";
		height = 164;
	} else if("hitaoImageN" == controlName){
		width = "715px";
		height = 403;
	} else if("mallImageN" == controlName){
		width = "715px";
		height = 403;
	} else if("pcImageN" == controlName){
		if("3" == typeValue){
			width = "755px";
			height = 226;
		} else if("2" == typeValue){
			width = "715px";
			height = 312;
		} else if("1" == typeValue){
			width = "715px";
			height = 528;
		}
	}
	if(null != picUrl && "" != $.trim(picUrl)){
		picUrl = $("#" + controlName + "Img").attr("src");
		$.layer({
			type : 2,
			title : title,
			shadeClose : true,
			maxmin : true,
			fix : false,
			area: [width, height],
			iframe : {
				src : picUrl
			}
		});
	} else if("view" != model){
		var url = UPLOAD_TOPIC_PIC.replace("{topicControlName}",controlName);
		$.layer({
			type : 2,
			title : "上传活动图片",
			maxmin : true,
			fix : false,
			area: ['400px', 300],
			iframe : {
				src : url
			}
		});
	}
}

function addTopicItemOperationScript(){
	$("a[name='editItem']").off("click");
	$("a[name='editItem']").on("click", topicItemEditBind);
	
	$("a[name='removeItem']").off("click");
	$("a[name='removeItem']").on("click", topicItemRemoveBind);

	$("img[name='selectImage']").off("click");
	$("img[name='selectImage']").on("click", topicItemClickImgBind);
	
	$("a[name='removeImage']").off("click");
	$("a[name='removeImage']").on("click", topicItemRemoveImgBind);
	
	$("a[name='lockItem']").off("click");
	$("a[name='lockItem']").on("click", topicItemLockBind);
	
	$(".topicInteger").integer();
	$(".topicNumber").numeral();
}

function topicItemClickImgBind(){
	var picUrl = $(this).closest("tr").find("input[name='topicImage']").val();
	var model=$("#viewModel").val();
	if(null != picUrl && "" != $.trim(picUrl)){
		picUrl = $(this).closest("tr").find("img[name='selectImage']").attr("src");
		$.layer({
			type : 2,
			title : "查看商品图片",
			shadeClose : true,
			maxmin : true,
			fix : false,
			area: ['600px', 400],
			iframe : {
				src : picUrl
			}
		});
	}else if("view" != model){
		var selectIndex = $(this).closest("tr").index(); 
		var url = UPLOAD_TOPIC_ITEM_PIC.replace("{topicItemIndex}",selectIndex);
		$.layer({
			type : 2,
			title : "上传活动商品图片",
			maxmin : true,
			fix : false,
			area: ['400px', 300],
			iframe : {
				src : url
			},
			end : function(){
				addTopicItemOperationScript();
			}
		});
	}
}

function topicItemRemoveImgBind(){
	$(this).closest("tr").find("input[name='topicImage']").val("");
	$(this).closest("tr").find("img[name='selectImage']").attr("src", UPLD_IMG);
}

function validateInfo(txt, control, time) {
	layer.tips(txt, control, {
		guide : 1,
		time : time,
		more : true
	});
}

function spLoadEvent(){
	var sp = $("#spValue").val()
	if(null != sp){
		spChangeEvent(sp);
	}
}

function typeLoadEvent(){
	var type = $("#typeValue").val()
	if(null != type){
		typeChangeEvent(type);
	}
}

function spChangeEvent(spValue){
	var type = $("#typeValue").val();
	if(null != spValue){
		if("1" == spValue || "0" == spValue){
			$(".imageRow").css("display","");
			$(".pcTag").css("display","");
			if(1 != type){
				$(".interestTag").css("display","none");
			} else {
			//	$(".interestTag").css("display","");
			}
			$(".htTag").css("display","none");
			$(".mallTag").css("display","none");
			if(null != type && "3" == type){
				$(".mobileTag").css("display","");
				$(".imageTable").css("width","300");
			} else {
			//	$(".mobileTag").css("display","none");
				if(1 != type){
					$(".imageTable").css("width","150");
				} else {
					$(".imageTable").css("width","300");
				}
			}
		} else if("2" == spValue) {
		//	$(".imageRow").css("display","none");
		} else if("3" == spValue) {
			$(".imageRow").css("display","");
			$(".pcTag").css("display","none");
			//$(".mobileTag").css("display","none");
			$(".interestTag").css("display","none");
			$(".mallTag").css("display","");
			$(".htTag").css("display","none");
			$(".imageTable").css("width","150");
		} else if("4" == spValue) {
			$(".imageRow").css("display","");
			$(".pcTag").css("display","none");
			//$(".mobileTag").css("display","none");
			$(".interestTag").css("display","none");
			$(".mallTag").css("display","none");
			$(".htTag").css("display","");
			$(".imageTable").css("width","150");
		} else {
			$(".imageRow").css("display","");
			$(".pcTag").css("display","");
			$(".mobileTag").css("display","");
			$(".interestTag").css("display","");
			$(".mallTag").css("display","");
			$(".htTag").css("display","");
			$(".imageTable").css("width","750");
		}

	}
	$(".mobileTag").css("display","");
}

function typeChangeEvent(typeValue){

	var sp = $("#spValue").val()
	if(null != typeValue){

		if("3" == typeValue){
			$("#brandId").val("");
			$("#brandId").attr("disabled","true");
			$("#brandName").val("");
			$("#brandName").attr("disabled","true");
			$("#confirmBrand").attr("disabled","true");
			$("#searchBrand").attr("disabled","true");
			$(".interestTag").css("display","none");
			if(null != sp && ("1" == sp || "0" == sp)){
				$(".mobileTag").css("display","");
				$(".imageTable").css("width","300");
			} else {
				//$(".mobileTag").css("display","none");
				$(".imageTable").css("width","150");
			}
		}else{
			$("#brandId").removeAttr("disabled");
			$("#brandName").removeAttr("disabled");
			$("#confirmBrand").removeAttr("disabled");
			$("#searchBrand").removeAttr("disabled");
		//	$(".mobileTag").css("display","none");
			if(1 != typeValue){
				$(".interestTag").css("display","none");
				if(null != sp && ("1" == sp || "0" == sp)){
					$(".imageTable").css("width","150");
				} else {
					$(".imageTable").css("width","150");
				}
			} else {
				if(null != sp && ("1" == sp || "0" == sp)){
					$(".interestTag").css("display","");
					$(".imageTable").css("width","300");
				} else {
					$(".interestTag").css("display","none");
					$(".imageTable").css("width","150");
				}
			}
		}
		resizeImgByType();

		if("5"==typeValue){
			$("#b_or_s").text("商家:");
			$("#brand-info").hide();
			$("#supplier-info").show();
		}else {
			$("#brand-info").show();
			$("#supplier-info").hide();
			$("#b_or_s").text("品牌:");
		}
		if(!isCouponTopic()){
			$("#topicCouponDetails").css("display","none");
		} else {
			$("#topicCouponDetails").css("display","block");
		}
	}
}

function resizeImgByType(){
	var typeValue = $("#typeValue").val()
	if(null != typeValue){
		 if("1" == typeValue){
			 $("#pcImageSize").text("尺寸: 278*325");
			 $("#phoneImageSize").text("尺寸: 608*450");
			 $("#pcImageSizeN").text("尺寸: 715*528");
			 $("#mobileImageSizeN").text("尺寸: 715*528");
		 } else {
			 $("#pcImageSize").text("尺寸: 755*190");
			 $("#phoneImageSize").text("尺寸: 608*400");
			 if("2" == typeValue){
				 $("#pcImageSizeN").text("尺寸: 715*312");
				 $("#mobileImageSizeN").text("尺寸: 715*312");
			 } else {
				 $("#pcImageSizeN").text("尺寸: 755*226");
				 $("#mobileImageSizeN").text("尺寸: 750*350");
			 }
		 }
	}
}

function isSingleType(){
	var type = $("#typeValue").val();
	return type == 1;
}

function isBrandType(){
	var type = $("#typeValue").val();
	return type == 2;
}

function isThemeType(){
	var type = $("#typeValue").val();
	return type == 3;
}

function isCouponTopic(){
	return isBrandType() || isThemeType();
}

function checkSameItemSku(){
	var skuArray = new Array();
	var itemsValid = true;
	$(".topicItemData").each(function(){
		var sku =  $(this).find("input[name='topicItemSku']").val();
		var sameKey = sku;
		if(skuArray.length > 0){
			for(var count=0;count < skuArray.length; count++){
				if(skuArray[count] == sameKey){
					layer.alert("商品清单中存在相同sku的商品:"+skuArray[count]);
					itemsValid = false;
					return;
				}
			}
		}
		skuArray.push(sameKey);
	});
	return itemsValid;
}

function querySameSkuPrice(){
	bindQuerySameSkuPrice($("input[name='topicPrice']"));
}
function bindQuerySameSkuPrice(selector) {
	selector.unbind("focus");
	selector.bind("focus",function(){
		var sku = $(this).closest("tr").find("input[name='topicItemSku']").val();
		var _this = this;
		$.ajax({ 
		     type:'post', //表单提交类型 
		     url:QUERY_SAME_SKU_PRICE, //表单提交目标 
		     data:{sku: sku}, //表单数据
		     dataType:'json',
		     success:function(msg){
		    	 //console.log(msg);
		    	 var text = "";
		    	 if(msg.success){
		    		 if(msg.data != null && msg.data.length>0){
		    			 for(var i=0; i<msg.data.length; i++) {
		    				text += "专场ID:"+msg.data[i].topicId + ",价格:" + msg.data[i].topicPrice + ";<br/>"; 
		    			 }
		    		 }
		    		 else {
		    			 text = "无其他专场参考价";
		    		 }
		    		 layer.tips(text, $(_this), {closeBtn:[0,true]});
		    	 }
		    	 else {
		    		 layer.tips("获取失败", $(_this), {closeBtn:[0,true]});
		    	 }
		     },
			error : function(data) {
				layer.tips("获取失败", $(_this), {closeBtn:[0,true]});
			}
	    });
	});
}
