var pageii;

$(function() {
	//1-普通(自营) 2-平台  3-国内直发  4-保税区 5-境外直发 6-海淘自营 7-速递
	//是否普通,国内直发
	var hiddenType = $("#hiddenTypeId").val();
	if(hiddenType=="4"||hiddenType=="5"||hiddenType=="6"){
		$(".bondedAreaTr").show();
	}else{
		$("#bondedArea").val("0");
		$(".bondedAreaTr").hide();
	}
    $("#storageType").change(function(){
		var type = $(this).val();
		if(type=="1"||type=="2"||type=="3" || type=="7"){
			$("#bondedArea").val("0");
			$(".bondedAreaTr").hide();
		}else{
			queryBondarea(type);
			$(".bondedAreaTr").show();
		}
    });
	
    $("#mainType").change(function(){
    	var mainType = $("#mainType").val();
    	if(mainType == "0"){
    		$(".mainWarehouseTr").show();
    	}else{
    		$(".mainWarehouseTr").hide();
    		$("#mainWarehouseId").val("0");
    		$("#mainWarehouseName").val("");
        	$("#mainSpId").val('0');
        	$("#mainSpName").val("");
    	}
    });
    
    //主仓库
    $("#mainWarehouseId").change(function(){
    	var mainId = $(this).find("option:selected").text();
    	if(mainId ==null && mainId == "" && mainId == "0") return;
    	var mainWhName = $(this).find("option:selected").attr("whname");
    	var mainSpId = $(this).find("option:selected").attr("spid");
    	var mainSpName = $(this).find("option:selected").attr("spname");
    	$("#mainWarehouseName").val(mainWhName);
    	$("#mainSpId").val(mainSpId);
    	$("#mainSpName").val(mainSpName);
    });
    
	/**
	 * warehouse添加
	 */
	function warehousSaveSubmit(button) {
		var flag = validateBonded();
		if(!flag){
			return;
		}
		var validateResult = vaditeBondedParams();
		if(!validateResult){
			return;
		}
		getSelectAreaIds();
		$.ajax({
			url : 'save.htm',
			data : $('#warehouseSaveForm').serialize(),
			type : "post",
			cache : false,
			success : function(data) {
				if (data.success) {// 成功
					layer.alert('操作成功', 4, function() {
						location.href = 'list.htm';
					});
				} else {// 失败
					layer.alert(data.msg.message, 8);
				}
			}
		});
	}

	/**
	 * warehouse编辑
	 */
	function warehousEditSubmit(button) {
		var flag = validateBonded();
		if(!flag){
			return;
		}
		var validateResult = vaditeBondedParams();
		if(!validateResult){
			return;
		}
		getSelectAreaIds();
		$.ajax({
			url : 'update.htm',
			data : $('#warehouseditForm').serialize(),
			type : "post",
			cache : false,
			success : function(data) {
				if (data.success) {// 成功
					layer.confirm('操作成功,是否返回列表页?点击确认按钮返回列表,否则,继续修改仓库',
							function() {
								location.href = 'list.htm';
							}, function() {
								location.href = 'edit.htm?id='
										+ obj.getMessage();
							});
				} else {// 失败
					layer.alert(data.msg.message, 8);
				}
			}
		});
	}

	// 商品对于供应商
	$('#selectSupplierbtn').live('click', function() {
		pageii = $.layer({
			type : 2,
			title : '查询供应商列表',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '800px', 500 ],
			iframe : {
				src : domain + '/storage/warehouse/selectSupplier.htm'
			}
		});
	});

	// 取消按钮
	$('.closebtn').on('click', function() {
		parent.layer.close(parent.pageii);
	});
	
	/**
	 * 仓库增加页面的数据校验
	 */
	if ($("#warehouseSaveBtn").is("input")) {
		var butt = $("#warehouseSaveBtn");
		$("#spname").trigger('focus');
		$.formValidator.initConfig({
			submitButtonID : "warehouseSaveBtn",
			debug : false,
			onSuccess : function() {
				warehousSaveSubmit(butt);
			},
			onError : function() {
				alert("数据输入有误，请看网页上的提示");
			}
		});
		validatorInput();
	}

	/**
	 * 仓库编辑页面的数据校验
	 */
	if ($("#warehouseditBtn").is("input")) {
		var butt = $("#warehouseditBtn");
		$("#spname").trigger('focus');
		$.formValidator.initConfig({
			submitButtonID : "warehouseditBtn",
			debug : false,
			onSuccess : function() {
				warehousEditSubmit(butt);
			},
			onError : function() {
				alert("数据输入有误，请看网页上的提示");
			}
		});
		validatorInput();
	}

	/**
	 * 数据校验方法
	 */
	function validatorInput() {
		
		$("#spname").formValidator({
			// onShow : "请选择供应商",
			onShow : "",
			onFocus : "请选择供应商！",
			onCorrect : "正确"
		}).inputValidator({
			min : 1,
			max : 100,
			onError : "商家名称不能为空"
		});
		$("#spid").formValidator({
			onShow : "请输入商家编号",
			onFocus : "商家编号不能为空",
			onCorrect : "正确"
		}).regexValidator({
			regExp : "num1",
			dataType : "enum",
			onError : "商家编号由数字组成"
		});
		$("#storagename").formValidator({
			onShow : "请输入仓库名称",
			onFocus : "仓库名称不能为空",
			onCorrect : "正确"
		}).inputValidator({
			min : 2,
			max : 50,
			onError : "仓库名称为2~50个字符"
		});
		$("#address").formValidator({
			onShow : "请输入仓库地址",
			onFocus : "仓库地址不能为空",
			onCorrect : "正确"
		}).inputValidator({
			min : 2,
			max : 200,
			onError : "仓库地址为2~200个字符"
		});
		$("#zipcode").formValidator({
			onShow : "请输入邮编,由6位数字组成",
			onfocus : "邮编不能为空",
			onCorrect : "正确"
		}).regexValidator({
			regExp : "zipcode",
			dataType : "enum",
			onError : "邮编由6位数字组成"
		});
		$("#linkman").formValidator({
			onShow : "请输入联系人",
			onFocus : "联系人不能为空",
			OnCorrect : "正确"
		}).inputValidator({
			min : 2,
			max : 20,
			onError : "联系人为2~20个字符"
		});
		$("#phone").formValidator({
			onShow : "请输入联系电话",
			onFocus : "联系电话不能为空",
			OnCorrect : "正确"
		}).inputValidator({
			min : 2,
			max : 15,
			onError : "联系电话为2~15个字符"
		});
	}

	function vaditeBondedParams(){
		var mainType = $.trim($("#mainType").val());
		if (mainType == null || mainType == ""){
			alert("请选择是否是主仓库");
			return false;
		}
		
		var putClear = $("#putCleanOrder").is(":checked");
		if(!putClear){
			return true;
		}
		var importType = $.trim($("#importType").val());
		if(importType == null || importType == "" || importType == "-1"){
			alert("进口类型不能为空");
			return false;
		}
		
		//保税备货模式
		if(importType == "1" || importType == 1){
			var wmsName = $.trim($("#wmsName").val());
			if(wmsName == null || wmsName == ""){
				alert("WMS仓库名称不能为空");
				return false;
			}
			var wmsCode = $.trim($("#wmsCode").val());
			if(wmsCode == null || wmsCode == ""){
				alert("WMS仓库编码不能为空");
				return false;
			}	
			var accountBookNo = $.trim($("#accountBookNo").val());
			if(accountBookNo == null || accountBookNo == ""){
				alert("账册编号不能为空");
				return false;
			}
			var goodsOwner = $.trim($("#goodsOwner").val());
			if(goodsOwner == null || goodsOwner == ""){
				alert("货主不能为空");
				return false;
			}
			var storageName = $.trim($("#storageName").val());
			if(storageName == null || storageName == ""){
				alert("仓储企业名称不能为空");
				return false;
			}
			var storageCode = $.trim($("#storageCode").val());
			if(storageCode == null || storageCode == ""){
				alert("仓储企业编码不能为空");
				return false;
			}
			var applicationFormNo = $.trim($("#applicationFormNo").val());
			if(applicationFormNo == null || applicationFormNo == ""){
				alert("仓库申请单编号不能为空");
				return false;
			}
		}
				
		var ioSeaport = $.trim($("#ioSeaport").val());
		if(ioSeaport == null || ioSeaport == "" || ioSeaport == "0"){
			alert("请选择进出关区");
			return false;
		}
		
		var declSeaport = $.trim($("#declSeaport").val());
		if(declSeaport == null || declSeaport == "" || declSeaport == "0"){
			alert("请选择申报关区");
			return false;
		}
		
		var customsField = $.trim($("#customsField").val());
		if(customsField == null || customsField == "" || customsField == "0"){
			alert("请选择码头或者货场");
			return false;
		}
		
		var logistics = $.trim($("#logistics").val());
		if(logistics == null || logistics == "" || logistics == "0"){
			alert("请选择快递公司");
			return;
		}
		
		var logisticsCode = $.trim($("#logisticsCode").val());
		if(logisticsCode == null || logisticsCode == ""){
			alert("快递公司企业编码不能为空");
			return false;
		}
		
		var logisticsName = $.trim($("#logisticsName").val());
		if(logisticsName == null || logisticsName == ""){
			alert("快递公司企业名称不能为空");
			return false;
		}
				
		var declareType = $.trim($("#declareType").val());
		if(declareType == null || declareType == "" || declareType == "0"){
			alert("申报类型不能为空");
			return false;
		}
		
		var declareCompanyCode = $.trim($("#declareCompanyCode").val());
		if(declareCompanyCode == null || declareCompanyCode == ""){
			alert("申报企业编码不能为空");
			return false;
		}
		
		var declareCompanyName = $.trim($("#declareCompanyName").val());
		if(declareCompanyName == null || declareCompanyName == ""){
			alert("申报企业名称不能为空");
			return false;
		}
		
		var senderName = $.trim($("#senderName").val());
		if(senderName == null | senderName == ""){
			alert("发件人不能为空");
			return false;
		}
		
		var senderCountryCode = $.trim($("#senderCountryCode").val());
		if(senderCountryCode == null || senderCountryCode == ""){
			alert("发件人国别不能为空");
			return false;
		}
		return true;
	}
	
	function validateBonded(){
		var flag =true;
		var storageType = $("#storageType").val();
		var bondedArea = $("#bondedArea").val();
		if(storageType==4||storageType==5){
			if(bondedArea==""||bondedArea==0){
				alert("必须选择通关渠道");
				flag = false;
			}
		}
		return flag;
	}
	
	$(document.body).on('click',':checkbox[name=putCleanOrder]',function(){
		$('.cleanorder').attr('checked',$(this).attr('checked'));
	});
	$(document.body).on('click',':checkbox[name=putWaybill]',function(){
		$('.waybill').attr('checked',$(this).attr('checked'));
	});
	$(document.body).on('click',':checkbox[name=putOrder]',function(){
		$('.order').attr('checked',$(this).attr('checked'));
	});
	
	$("#putCleanOrder").bind("click", function (){
		if($(this).is(":checked")){
			$("#bondedAreaParamTable").show();
		}else{
			$("#bondedAreaParamTable").hide();
		}
	});
});

/**
 * 查询通关渠道
 **/
function queryBondarea(type){
	$.ajax({
		url : 'bondedarea-list?typeId=' + type,
		type : "get",
		cache : false,
		success : function(data) {
			if (data.success) {// 成功
				var list = data.data;
				var html = [];
				html.push('<option value=0 selected=selected>请选择</option>');
				var len = data.data.length;
				for(var i = 0; i< len; i++){
					var option = "<option " + " value=" + data.data[i].id + ">" + data.data[i].name + "</option>";
					html.push(option);
				}
				$("#bondedArea").html(html);
			} else {// 失败
				layer.alert(data.msg.message, 8);
			}
		}
	});
}
