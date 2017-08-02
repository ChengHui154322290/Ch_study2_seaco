var pageii;
var PCACHE={};
var SEARCH_TOPIC_BRAND = domain + "/topic/brand/query";
var CONFIRM_TOPIC_BRAND = domain + "/topic/brand/confirm?";

var SEARCH_COUPON_SUPPLIER = domain + "/coupon/supplier/query";
var CONFIRM_COUPON_SUPPLIER = domain + "/coupon/supplier/confirm?";


var  categoryChangeEdit = function(id,target){
	var url = domain+"/item/category-cld.htm";
	$.get(url,{catId:id},function(data){
		if(data){
			target.html('');
			target.append("<option value=''>--请选择分类--</option>");
			$.each(data,function(i,n){
				var id = n.id;
				var name = n.name;
				var opt = $("<option />");
				opt.val(id);
				opt.html(name);
				target.append(opt);
			});
		}
	});
}



$(function(){
	
	//确定供应商
	$(".confirmSource").live("click",function(){
		confirmSupplierInfo($(this));
	});
	//查询供应商
	$(".searchSource").live("click",function(){
		var _this = $(this);
//		$("span[name=brand_span_2][current=true]").removeAttr("current");
//		_this.parent().attr("current","true");
		$.layer({
			type : 2,
			title : "查询商户信息",
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '600px', 450 ],
			iframe : {
				src : SEARCH_COUPON_SUPPLIER
			},
			end : function(){
				confirmSupplierInfo(_this);
			}
		});
	});
	function confirmSupplierInfo(obj){
		var bid = obj.parent().find(".source_id").val();
		if(null == bid || "" == $.trim(bid)){
			return;
		}
		var data=syncGet(CONFIRM_COUPON_SUPPLIER + new Date().getTime(), {
			supplierId : bid
		})
		if(null != data && data.success){
			if(null == data.data){
				layer.alert("没有商户信息",2);
			}else{
				obj.parent().find(".source_name").val(data.data.name);
			}
		}else{
			layer.alert("商户信息查询失败",2);
			obj.parent().find(".source_id").val("");
		}
		
	}
	
	//绑定专场和商品事件
	$('#offerType').live("change",function(){
	         var useScopeValue=$(this).val();
	         if(useScopeValue!="1"){
	        	 $("#showReceiveSpan").show();
	         }else{
	        	 $("#showReceiveSpan").hide();
	        	 $("input[name='isShowReceive'][value='1']").attr("checked",true); //不展示
	         }
	});
	
//	//绑定专场和商品事件
//	$('input[name="useScope"]').live("click",function(){
//	         var useScopeValue=$(this).val();
//	         if(useScopeValue=="2"){//显示专场ID
//	        	 $("#topicSelect").show();
//	         }else{//隐藏专场ID
//	        	 $("#topicSelect").hide();
//	         }
//	});
	//确定品牌
	$(".confirmBrand").live("click",function(){
		confirmBrandInfo($(this));
	});
	//查询品牌
	$(".searchBrand").live("click",function(){
		var _this = $(this);
		$("span[name=brand_span_2][current=true]").removeAttr("current");
		_this.parent().attr("current","true");
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
				confirmBrandInfo(_this);
			}
		});
	});
	function confirmBrandInfo(obj){
		var bid = obj.parent().find(".brandId").val();
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
				obj.parent().find(".brandName").val(data.data.name);
			}
		}else{
			layer.alert("品牌信息查询失败",2);
		}
		
	}
	
	
	
	$('select.largeIdSel').live("change",function(){ 
		var _this = $(this);
		var val = $(_this).val();
		if(val==null||val.length==0){
			return;
		}
		categoryChange(val, $(_this).parent().find('select.mediumIdSel'));
	});
	
	$('select.mediumIdSel').live("change",function(){  
		var _this=this;
		var val = $(_this).val();
		if(val==null||val.length==0){
			return;
		}
		categoryChange(val, $(_this).parent().find('select.smallIdSel'));
	});
	
	$("input.brandName").autocomplete({
		source: domain+'/coupon/queryBrand.htm',
		minLength: 1,    
		select: function( event, ui ) {
				  $(this).attr("brandId",ui.item.id);  
		}
	});
	
	$("#all_items_checkbox").on("click", function() {
		if($("#all_items_checkbox").is(":checked")){
			$("#coupon_range_span").hide();
		}else
			$("#coupon_range_span").show();
	});
	
	PCACHE.clone_tr=$("#clone_tr").clone(true);
	$("#clone_tr").remove();
	$("#type_choose").find("select").hide();
	$("#type_choose").find("input[type='text']").hide(); 
	$("span.over_type_span").hide();
	$("span.cash_type_span").hide();
	$("span.over_type_remark").hide();
	$("span.cash_type_remark").hide();
	$("span.exchangeXgMoney").hide();
	$("#exchangeXgMoney").hide();
	
	$("input.remark1").hide();
	$("input.remark2").hide();
	$("#coupon_use_stime").hide();
	$("#coupon_use_etime").hide();
	$("#use_date_interval").hide();
	$("span.coupon_use_time").hide();
	$("span.use_date_interval").hide();
	
	$("#all_platform").on("click", function() {
		$("#platform_self")[0].checked = $(this)[0].checked;
		$("#platform_union")[0].checked = $(this)[0].checked;
		
	});
	$("#platAll").on("click", function() {
		$("#platPc")[0].checked = $(this)[0].checked;
		$("#platApp")[0].checked = $(this)[0].checked;
		$("#platWap")[0].checked = $(this)[0].checked;
		$("#platHapPreg")[0].checked = $(this)[0].checked;
	});
	
	$("#both_hitao").on("click", function() {
		$("#justhitao")[0].checked = $(this)[0].checked;
		$("#nohitao")[0].checked = $(this)[0].checked;
	});
	
	$(".source_type").live('click', function(){
		var _this=$(this);
		var val = $(_this).val();
		if(val ==1 ){
			$("#source_info").hide();
		}else{
			$("#source_info").show();
		}
	});
	$("#source_id").live("click",function(){
		if($("#source_id").val() == "商户ID"){
			$("#source_id").val("") ;
		}
	});
	
	$("#source_name").live("click",function(){
		if($("#source_name").val() == "商户名称"){
			$("#source_name").val("") ;
		}
	});
	
	$("[name=add_category]").live('click', function(){
		var newC = $("#category_span_copy").html();
		$("#category_span_1").append(newC);
	});
	
	$("#add_brand").live('click', function(){
		var newC = $("#brand_span_copy").html();
		$("#brand_span_1").append(newC);
		
	});
	$("#add_sku").live('click', function(){
		var newC = $("#sku_span_copy").html();
		$("#sku_span_1").append(newC);
		
	});
	
	$("[name=remove_category]").live('click', function(){
		if($("[name=remove_category]").length > 2){
			var _this =  this;
			$(_this).parent().remove();
		}else
			alert("不能继续删除，至少保留一行");
	});
	$("[name=remove_sku]").live('click', function(){
		if($("[name=remove_sku]").length > 2){
			var _this =  this;
			$(_this).parent().remove();
		}else
			alert("不能继续删除，至少保留一行");
	});
	$("[name=remove_brand]").live('click', function(){
		if($("[name=remove_brand]").length > 2){
			var _this =  this;
			$(_this).parent().remove();
		}else
			alert("不能继续删除，至少保留一行");
	});
	
	//不包含
	$("[name=add_category_no]").live('click', function(){
		var newC = $("#category_span_copy_no").html();
		$("#category_span_1_no").append(newC);
		
	});
	
	$("#add_brand_no").live('click', function(){
		var newC = $("#brand_span_copy_no").html();
		$("#brand_span_1_no").append(newC);
		
	});
	$("#add_sku_no").live('click', function(){
		var newC = $("#sku_span_copy_no").html();
		$("#sku_span_1_no").append(newC);
		
	});
	
	$("[name=remove_category_no]").live('click', function(){
		if($("[name=remove_category_no]").length > 2){
			var _this =  this;
			$(_this).parent().remove();
		}else
			alert("不能继续删除，至少保留一行");
	});
	$("[name=remove_sku_no]").live('click', function(){
		if($("[name=remove_sku_no]").length > 2){
			var _this =  this;
			$(_this).parent().remove();
		}else
			alert("不能继续删除，至少保留一行");
	});
	$("[name=remove_brand_no]").live('click', function(){
		if($("[name=remove_brand_no]").length > 2){
			var _this =  this;
			$(_this).parent().remove();
		}else
			alert("不能继续删除，至少保留一行");
	});
	
	
	
	
	$("#add_tr_range").live('click',function(){ 
		var to_clone = PCACHE.clone_tr.clone(true);
		$(to_clone).find("a.to_del").live('click',function(){
			var _this = this;
			$(_this).parent().parent().remove();
		});
		
		
		$(to_clone).find("input.brandauto").autocomplete({
			source: domain+'/coupon/queryBrand.htm',
			minLength: 1,    
			select: function( event, ui ) {
					  $(this).attr("brandId",ui.item.id);  
			}
		});
		
		$(to_clone).show();
		to_clone.insertBefore($("#clone_tr_before"));    
	});
	
	/**优惠券类型绑定**/
	$("input[name='couponType']").live('click',function(){
		var _this = this;
		if($(_this).val() == '0'){
				$("#over_type").show();
				$("#over_type_select_over").show();
				$("#over_type_select_face").show();
				$("span.over_type_span").show();
				
				$("input.remark1").show();
				$("input.remark2").hide();
				$("span.over_type_remark").show();
				$("span.cash_type_remark").hide();
				
				
				$("#cash_type").hide();
				$("#cash_type_select_face").hide();
				$("span.cash_type_span").hide();
				$("span.exchangeXgMoney").hide();
				$("#exchangeXgMoney").hide();
				
				$("#show_choose_over_value").show();
				$("#over_type_input_over").show();
				$("#over_type_input_face").show();
				$("#cash_type_input_face").hide();
				
				$("#promotertr").hide();
			}else{
				$("#over_type").hide();
				$("#over_type_select_over").hide();
				$("#over_type_select_face").hide();
				$("span.over_type_span").hide();
				
				$("input.remark1").hide();
				$("input.remark2").show();
				
				$("span.over_type_remark").hide();
				$("span.cash_type_remark").show(); 
				
				$("#cash_type").show();
				$("#cash_type_select_face").show();
				$("span.cash_type_span").show(); 
				$("span.exchangeXgMoney").show();
				$("#exchangeXgMoney").show();
				
				$("#show_choose_over_value").hide();
				$("#over_type_input_over").hide();
				$("#over_type_input_face").hide();
				$("#cash_type_input_face").show();
				
				$("#promotertr").show();
			}
	});
	
	$("input[name='coupon_use_type']").live('click',function(){
		var _this = this;
		if($(_this).val() == '0'){
				$("#coupon_use_stime").show();
				$("#coupon_use_etime").show();
				$("span.coupon_use_time").show();
				$("#use_date_interval").hide();
				$("span.use_date_interval").hide();
			}else{
				$("#coupon_use_stime").hide();
				$("#coupon_use_etime").hide();
				$("span.coupon_use_time").hide();
				$("#use_date_interval").show();
				$("span.use_date_interval").show();
			}
	});
	
	
	
	$('#over_type_select_over').change(function(){
			var _this = this;
			$("#show_over").find("option[value='"+$(_this).val()+"']").attr("selected",true);
	}); 
	$('#over_type_select_face').change(function(){
		var _this = this;
		$("#show_face").find("option[value='"+$(_this).val()+"']").attr("selected",true);
	}); 
	$('#cash_type_select_face').change(function(){
		var _this = this;
		$("#show_face").find("option[value='"+$(_this).val()+"']").attr("selected",true);
	}); 
	
	//输入框输入面额start
	$('#over_type_input_over').change(function(){
		var _this = this;
		var _over = $('#over_type_input_over').val();
		var pattern=/^[0-9]\d*$/;
		if(!pattern.test(_over)){
			alert("满额请输入正确的数字。");   
			return false;
		}
		$("#show_over_input").val(_over);
	}); 
	$('#over_type_input_face').change(function(){
		var _this = this;
		var _face = $('#over_type_input_face').val();
		var pattern=/^[0-9]\d*$/;
		if(!pattern.test(_face)){
			alert("面值请输入正确的数字。");   
			return false;
		}
		$("#show_face_input").val(_face);
	}); 
	
	$('#cash_type_input_face').change(function(){
		var _this = this;
		var cash_face = $('#cash_type_input_face').val();
		var pattern=/^[0-9]\d*$/;
		if(!pattern.test(cash_face)){
			alert("面值请输入正确的数字。");   
			return false;
		}
		$("#show_face_input").val(cash_face);
	});
	
	//输入框输入面额end
	
	/***时间空间绑定**/ 
	$( "#coupon_release_stime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});
	$( "#coupon_release_etime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});
	$( "#coupon_use_stime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});
	$( "#coupon_use_etime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	    }
	});
	
	
	
	
	
	function categoryChange(id,target){
		var url = domain+"/item/category-cld.htm";
		$.get(url,{catId:id},function(data){
			if(data){
				target.html('');
				target.append("<option value=''>--请选择分类--</option>");
				$.each(data,function(i,n){
					var id = n.id;
					var name = n.name;
					var opt = $("<option />");
					opt.val(id);
					opt.html(name);
					target.append(opt);
				});
			}
		});
	}
	
	/**类包装**/
	function getSaveInfo(){
		/**常规数据封装***/
		var saveInfo= {};
		var couponType = $("input[name='couponType']:checked").val();
		var couponCount = $("input.couponCount").val();
		var activeStatus = $(':radio[name=activeStatus]:checked').val();
		saveInfo.couponCount = couponCount;
		saveInfo.activeStatus = activeStatus;
		saveInfo.isShowReceive=$("input[name='isShowReceive']:checked").val();
		saveInfo.couponImagePath=$("#couponImagePath").val();
		var topicId=$("#topicId").val();
		/**使用范围 1:商品   2： 专场  3:品牌   4：类别*/
		var useScope = $("input[name='useScope']:checked").val();
		if(useScope=="2"){
			saveInfo.topicId=topicId;
			if(topicId==""){
				alert("请输入专场ID");
				return false;
			}
		}
		
		if(couponType == 0){
//			var needOverMon  = $("#over_type_select_over").find("option:selected").text();
//			var  faceValue = $("#over_type_select_face").find("option:selected").text();
			var needOverMon  = $("#over_type_input_over").val();
			var faceValue  = $("#over_type_input_face").val();
			var couponName = $("#over_type").val();
			saveInfo.faceValue = faceValue;
			saveInfo.needOverMon = needOverMon;
			saveInfo.couponName = couponName;
			saveInfo.remark=$("input.remark1").val();
		}else if(couponType == 1){
//			var  faceValue = $("#cash_type_select_face").find("option:selected").text(); 
			var  faceValue = $("#cash_type_input_face").val(); 
			var couponName = $("#cash_type").val();
			var exchangeXgMoney = $("#exchangeXgMoney").val();
			saveInfo.faceValue = faceValue;
			saveInfo.couponName = couponName;
			saveInfo.exchangeXgMoney=exchangeXgMoney
			saveInfo.remark=$("input.remark2").val();
		}
		var  couponReleaseStime = $("#coupon_release_stime").val();
		saveInfo.couponReleaseStime = couponReleaseStime;
		var promoterId = $("input[name='promoterId']").val();
		saveInfo.promoterId = promoterId;
		var  couponReleaseEtime = $("#coupon_release_etime").val();
		saveInfo.couponReleaseEtime = couponReleaseEtime;
		saveInfo.couponType = couponType;
		var couponUseType = $("input[name='coupon_use_type']:checked").val();
		saveInfo.couponUseType = couponUseType;
		if(couponUseType == 0){
			var  couponUseStime  = $("#coupon_use_stime").val();
			var  couponUseEtime =  $("#coupon_use_etime").val();
			saveInfo.couponUseStime= couponUseStime;
			saveInfo.couponUseEtime= couponUseEtime;
		}else if(couponUseType == 1){
			var useReceiveDay = $("#use_date_interval").val();
			saveInfo.useReceiveDay= useReceiveDay;
		}
		
		var usePlantform = [];
		$("input[name='platformCodes']:checked").each(function(){
			usePlantform.push($(this).val());
		});
		saveInfo.usePlantform = usePlantform.join(",");
		
		var hitaoSign = [];
		$("input[name='hitao_sign']:checked").each(function(){
			hitaoSign.push($(this).val());
		});
		saveInfo.hitaoSign = hitaoSign.join(",");
		
		//发券主体
		var sourceType = [];
		$("input[name='source_type']:checked").each(function(){
			sourceType.push($(this).val());
		});
		saveInfo.sourceType = sourceType.join(",");
		if(saveInfo.sourceType  == 2 ){
			var sourceId  = $("#source_id").val();
			var sourceName  = $("#source_name").val();
			saveInfo.sourceId = sourceId;
			saveInfo.sourceName = sourceName;
		}
		
		var useRange = [];
		$("input[name='use_range']:checked").each(function(){
			useRange.push($(this).val());
		});
		saveInfo.useRange = useRange.join(",");
		//saveInfo.status = $("input[name='status']:checked").val();  
		
		/***优惠品范围数据封装**/
		if($("#all_items_checkbox").is(':checked')){
		}else{
			var couponRangeGroup=[]; 
			 if(useScope=="4"){//类别
			$("[name=category_span_2]").each(function(){
				var _this = this;
				var group=[];
				$(_this).find("select").each(function(i,val){
					if($(val).val() != null && $(val).val() !=""){
							group.push($(val).val());
						}
				});
				
				var attributeName=""; 
				var largeId = $(_this).find("select.largeIdSel").val();
				
				if(largeId == null || largeId =="undefined"){
					largeId="";
				}
				var middleId = $(_this).find("select.mediumIdSel").val();
				if(middleId == null || middleId =="undefined"){
					middleId="";
				}
				var smallId = $(_this).find("select.smallIdSel").val();
				if(smallId == null || smallId =="undefined"){
					smallId="";
				}
				var  largeName = $(_this).find("option:selected").eq(0).text();
				var middleName = $(_this).find("option:selected").eq(1).text();
				var smallName = $(_this).find("option:selected").eq(2).text();
				//attributeName = largeName +">" + middleName +">" + smallName;
				if(largeId!= ""){
					attributeName =  largeName;
				}
				if(middleId!=""){
					attributeName = attributeName+">" + middleName
				}
				if(smallId!=""){
					attributeName=attributeName+">" + smallName;
				}


				couponRangeGroup.push({
					categoryId:largeId,
					categoryMiddleId:middleId,
					categorySmallId:smallId,
					attributeName:attributeName
					});
				
			});
			 }
				 
		 if(useScope=="3"){//品牌
			$("[name=brand_span_2]").each(function(){
				var _this = this;
				var group=[];
				$(_this).find("select").each(function(i,val){
					if($(val).val() != null && $(val).val() !=""){
							group.push($(val).val());
						}
				});
				
				var attributeName=""; 
				var brandId=$(_this).find("input.brandId").val();
				var brandName;
				brandName =$(_this).find("input.brandName").val();
				if(brandName != "" &&  brandId != ""){
					couponRangeGroup.push({
						brandId:brandId,
						brandName:brandName,
						});
				}
			});
			 }
		  if(useScope=="1"){//商品
				$("[name=sku_span_2]").each(function(){
					var _this = this;
					var sku = $(_this).find("input[name='typeCode']").val();
					couponRangeGroup.push({
						code:sku,
						type:$(_this).find("select.itemLevelSelect").val()
						});
				});
			  }
		  if(useScope=="2"){//专场
				$("[name=topic_span_2]").each(function(){
					var _this = this;
					var topicId = $(_this).find("input[name='topicId']").val();
					couponRangeGroup.push({
						code:topicId,
						type:'2'
						});
				});
			  }
		}
		saveInfo.couponRangeGroup = JSON.stringify(couponRangeGroup);
		
		//不包含
		var couponRangeGroup_no=[]; 
		$("[name=category_span_2_no]").each(function(){
			var _this = this;
			var group=[];
			$(_this).find("select").each(function(i,val){
				if($(val).val() != null && $(val).val() !=""){
					group.push($(val).val());
				}
			});
			
			var attributeName=""; 
			var largeId = $(_this).find("select.largeIdSel").val();
			
			if(largeId == null || largeId =="undefined"){
				largeId="";
			}
			var middleId = $(_this).find("select.mediumIdSel").val();
			if(middleId == null || middleId =="undefined"){
				middleId="";
			}
			var smallId = $(_this).find("select.smallIdSel").val();
			if(smallId == null || smallId =="undefined"){
				smallId="";
			}
			var  largeName = $(_this).find("option:selected").eq(0).text();
			var middleName = $(_this).find("option:selected").eq(1).text();
			var smallName = $(_this).find("option:selected").eq(2).text();
			//attributeName = largeName +">" + middleName +">" + smallName;
			if(largeId!= ""){
				attributeName =  largeName;
			}
			if(middleId!=""){
				attributeName = attributeName+">" + middleName
			}
			if(smallId!=""){
				attributeName=attributeName+">" + smallName;
			}

			
			couponRangeGroup_no.push({
				categoryId:largeId,
				categoryMiddleId:middleId,
				categorySmallId:smallId,
				attributeName:attributeName
			});
			
		});
		$("[name=brand_span_2_no]").each(function(){
			var _this = this;
			var group=[];
			$(_this).find("select").each(function(i,val){
				if($(val).val() != null && $(val).val() !=""){
					group.push($(val).val());
				}
			});
			
			var attributeName=""; 
			var brandId=$(_this).find("input.brandId").val();
			var brandName;
			brandName =$(_this).find("input.brandName").val();
			if(brandName != "" && brandId != ""){
				couponRangeGroup_no.push({
					brandId:brandId,
					brandName:brandName,
				});
			}
			
		});
		
		$("[name=sku_span_2_no]").each(function(){
			var _this = this;
			var sku = $(_this).find("input[name='typeCode_no']").val();
			couponRangeGroup_no.push({
				code:sku,
				type:$(_this).find("select.itemLevelSelect").val()
			});
		});
		saveInfo.couponRangeGroupNoInclude = JSON.stringify(couponRangeGroup_no);
		
		saveInfo.id = $("#conponId").val();

		var offerType = $(".offerType :selected").val();
		saveInfo.offerType=offerType;

		var justScan = $(".just_scan :selected").val();
		saveInfo.justScan= justScan;
		var useScope = $("input[name='useScope']:checked").val();
		saveInfo.useScope=useScope;
		var topicId=$("#topicId").val();
		saveInfo.topicId=topicId;
		
		
		
		return saveInfo;
	}	

	$(".datasubmit").click(function(){  
		if(validationData()){
			var url = $(this).attr("param");
			var status = $(this).attr("status");
			url = domain+url;
			var couponInfo = getSaveInfo();
			couponInfo.status = status; //编辑中 or  提交审核
			$.post(url,{coupon:JSON.stringify(couponInfo)},function(data){

				if(data.success ){
					alert(data.data);
					window.location.href = domain+'/coupon/list.htm';
				}else {
					alert(data.msg.message);
				}
			});
		}
	});
	
	
	/***数据验证**/
	function validationData(){
		if($("#couponImagePath").val() == null  || $("#couponImagePath").val()==""){
			alert("请上传优惠券图片。");
			return false;
		}  
		if($("input.couponCount").val() == null  || $("input.couponCount").val()==""){
			alert("请输入优惠券数量。");
			return false;
		}  
		var pattern=/^[-1-9]\d*$/;
		if(!pattern.test($("input.couponCount").val())){
			alert("发行量请输入正确的数字。");   
			return false;
		}
		if( !$("input[name='couponType']:checked").length > 0){
			alert("请选择优惠券名称。");
			return false;
		}
		if($("input[name='couponType']:checked").val()==0){
			if($("#over_type").val() == null || $("#over_type").val()==""){
				alert("请输入活动名称。");
				return false;
			}
			
			if($("#over_type_input_over").val() == null || $("#over_type_input_over").val()==""){
				alert("请填写满减金额。");
				return false;
			}
			if($("#over_type_input_face").val() == null || $("#over_type_input_face").val()==""){
				alert("请输入优惠券面值。");
				return false;
			}
			if(parseInt($("#over_type_input_over").val()) < parseInt($("#over_type_input_face").val())){   
				alert("优惠金额不能超过满额。");
				return false;
			}
		}else{
			if($("#cash_type").val() == null || $("#cash_type").val()==""){
				alert("请输入活动名称。");
				return false;
			}
			
			if($("#cash_type_input_face").val() == null || $("#cash_type_input_face").val()==""){
				alert("请输入现金券面值。");
				return false;
			}
		}
		
		if($("#coupon_release_stime").val() == null || $("#coupon_release_stime").val()==""){
			alert("请选择优惠券发放时间。");
			return false;
		}
		if($("#coupon_release_etime").val() == null || $("#coupon_release_etime").val()==""){
			alert("请选择优惠券发放时间。");
			return false;
		}
		if(!$("input[name='coupon_use_type']:checked").length > 0){
			alert("请选择优惠券使用时间。");
			return false;
		}
		
		if($("input[name='coupon_use_type']:checked").val() == 0){
			if($("#coupon_use_stime").val() == null || $("#coupon_use_stime").val()==""){
				alert("请选时间段。");
				return false;
			}
			if($("#coupon_use_etime").val() == null || $("#coupon_use_etime").val()==""){
				alert("请选时间段。");
				return false;
			}
		}else{
			if($("#use_date_interval").val() == null || $("#use_date_interval").val()==""){
				alert("请输入有效日。");
				return false;
			}
			if(!pattern.test($("#use_date_interval").val())){
				alert("有效日请输入正确的数字。");   
				return false;
			}
		}
		
		var usePlantform = [];
		$("input[name='platformCodes']:checked").each(function(){
			usePlantform.push($(this).val());
		});
		if(!usePlantform.length > 0){
			alert("请选择适用平台。");
			return false;
		}
		
		var hitaoSign = [];
		$("input[name='hitao_sign']:checked").each(function(){
			hitaoSign.push($(this).val());
		});
		if(!hitaoSign.length > 0){
			alert("请选择是否适用海淘。");
			return false;
		}
		//发券主体
		var sourceType = [];
		$("input[name='source_type']:checked").each(function(){
			sourceType.push($(this).val());
		});
		if(sourceType  == 2 ){
			var sourceId  = $("#source_id").val();
			var sourceName  = $("#source_name").val();
			if(sourceId ==''|| sourceId=='商户ID'){
				alert("商户ID不能为空");
				return  false;
			}else{
			}
		}
		
		var useRange = [];
		$("input[name='use_range']:checked").each(function(){
			useRange.push($(this).val());
		});
		if(!useRange.length > 0){
			alert("请选择适用范围。");
			return false;
		}
	
		return true;
	}
	
	$("#cancel_page").click(function(){ 
		window.location.href = domain+'/coupon/list.htm';
	});
	
	$("#reset").click(function(){ 
	});
	
}); 

/**页面赋值***/
$(function(){
	var couponInfos = $.parseJSON($("#dto").val());
	if(couponInfos.couponType == 0){
		$("input[name='couponType'][value=0]").trigger('click');
		$("#over_type").val(couponInfos.couponName);
		$("input.remark1").val(couponInfos.remark);   
	}else if(couponInfos.couponType == 1){
		$("input[name='couponType'][value=1]").trigger('click');
		$("#cash_type").val(couponInfos.couponName);
		$("input.remark2").val(couponInfos.remark);
		$("#show_choose_over_value").hide();
	}
	if(couponInfos.couponUseType == 0){
		$("input[name='coupon_use_type'][value=0]").trigger('click');
		//$("#coupon_use_stime").val(couponInfos.couponUseStime);
		//$("#coupon_use_etime").val(couponInfos.couponUseEtime);
	}else if(couponInfos.couponUseType == 1){
		$("input[name='coupon_use_type'][value=1]").trigger('click');
		$("#use_date_interval").val(couponInfos.useReceiveDay); 
	}
	/**适用平台**/
	var plant =[];
	plant = couponInfos.usePlantform.split(",");
	$.each(plant,function(i,n){
		$("input[name='platformCodes'][value='"+n+"']").prop("checked",true);
	});
	/**适用海淘**/
	var hitaoSign =[];
	hitaoSign = couponInfos.hitaoSign.split(",");
	$.each(hitaoSign,function(i,n){
		$("input[name='hitao_sign'][value='"+n+"']").prop("checked",true); 
	});
	/**适用范围**/
	var  useRange=[];
	useRange = couponInfos.useRange.split(",");
	$.each(useRange,function(i,n){
		$("input[name='use_range'][value='"+n+"']").prop("checked",true);
	});
	
	
	/**发券主体**/
	var sourceType =[];
	sourceType = couponInfos.sourceType;
	$("#source_type_" + sourceType).attr("checked",true);
	if(sourceType == 2){
		$("#source_info").show();
		$('#source_id').val(couponInfos.sourceId);
		$('#source_name').val(couponInfos.sourceName);
	}
	
	/**是否是查看详细**/
	if($("#only_view").val()){
		$("#datasubmit").hide();
		$("#reset").hide();
		$("#can_edit_info").hide();
	}

	$(".offerType").val(couponInfos.offerType);
	 if(couponInfos.offerType!="1"){
    	 $("#showReceiveSpan").show();
     }else{
    	 $("#showReceiveSpan").hide();
    	 $("input[name='isShowReceive'][value='1']").attr("checked",true); //不展示
     }
	$(".just_scan").val(couponInfos.justScan);
	$(':radio[name=activeStatus]').each(function(){
		if($(this).val()==couponInfos.activeStatus){
			$(this).attr('checked',true);
		}
	})
	
	/**优惠商品范围**/
	var rangeinfos = $.parseJSON(couponInfos.couponRangeGroup);
	var topicId=$("#topicId").val();
	if(rangeinfos != null ){
		$.each(rangeinfos,function(i,val){
			if(i==0){
				if(val.categoryId != null && val.categoryId != ""){
					
					$("#category_span_0").find("select.largeIdSel").val(val.categoryId);
					var url = domain+"/item/category-cld.htm";
					$.ajax({
						  url: url,
						  type:'get',
						  data: {catId:val.categoryId},
						  success: function(data){
								if(data){
									$("#category_span_0").find('select.mediumIdSel').html('');
									$("#category_span_0").find('select.mediumIdSel').append("<option value=''>--请选择分类--</option>");
									$.each(data,function(i,n){
										var id = n.id;
										var name = n.name;
										var opt = $("<option />");
										opt.val(id);
										opt.html(name);
										$("#category_span_0").find('select.mediumIdSel').append(opt);
									});
									if(val.categoryMiddleId != null && val.categoryMiddleId != ""){
										$("#category_span_0").find("select.mediumIdSel").val(val.categoryMiddleId);
										$.ajax({
											  url: url,
											  data: {catId:val.categoryMiddleId},
											  success: function(data){
													if(data){
														$("#category_span_0").find('select.smallIdSel').html('');
														$("#category_span_0").find('select.smallIdSel').append("<option value=''>--请选择分类--</option>");
														$.each(data,function(i,n){
															var id = n.id;
															var name = n.name;
															var opt = $("<option />");
															opt.val(id);
															opt.html(name);
															$("#category_span_0").find('select.smallIdSel').append(opt);
														});
														if(val.categorySmallId != null && val.categorySmallId != ""){
															$("#category_span_0").find("select.smallIdSel").val(val.categorySmallId);
														}
													}
												}
										});
									}
								}
						  	}
						});
				}
			
				if(val.brandName != null && val.brandName != ""){
					if($("#default_brand").val()==""){
						$("#default_brand").val(val.brandName);
						$("#default_brand_id").val(val.brandId);
					}else{
						var newC = $("#brand_span_copy").html();
						var idName = "brand_span_edit_" + i;
						newC = newC.replace("brand_span_2", idName);
						$("#brand_span_1").append(newC);
						$("#" + idName).find("input.brandName").val(val.brandName);
					}
				}
				if(val.code != null && val.code != "" && val.type=="0"){
					if($("#default_sku").val()==""){
						$("#default_sku").val(val.code);
					}else{
						var newC = $("#sku_span_copy").html();
						var idName = "sku_span_edit_" + i;
						newC = newC.replace("sku_span_2", idName);
						$("#sku_span_1").append(newC);
						$("#" + idName).find("input[name='typeCode']").val(val.code);
					}
				}
				if(val.code != null && val.code != "" && val.type=="2"){
					$("#topicId").val(val.code);
				}
			}else{
				if(val.categoryId != null && val.categoryId != ""){
					var newC = $("#category_span_copy").html();
					var idName = "category_span_edit_" + i;
					newC = newC.replace("category_span_0", idName);
					$("#category_span_1").append(newC);
					$("#" + idName).find("select.largeIdSel").val(val.categoryId);
					var url = domain+"/item/category-cld.htm";
					$.ajax({
						url: url,
						 type:'get',
						data: {catId:val.categoryId},
						success: function(data){
							if(data){
								$("#" + idName).find('select.mediumIdSel').html('');
								$("#" + idName).find('select.mediumIdSel').append("<option value=''>--请选择分类--</option>");
								$.each(data,function(i,n){
									var id = n.id;
									var name = n.name;
									var opt = $("<option />");
									opt.val(id);
									opt.html(name);
									$("#" + idName).find('select.mediumIdSel').append(opt);
								});
								if(val.categoryMiddleId != null && val.categoryMiddleId != ""){
									$("#" + idName).find("select.mediumIdSel").val(val.categoryMiddleId);
									$.ajax({
										url: url,
										type:'get',
										data: {catId:val.categoryMiddleId},
										success: function(data){
											if(data){
												$("#" + idName).find('select.smallIdSel').html('');
												$("#" + idName).find('select.smallIdSel').append("<option value=''>--请选择分类--</option>");
												$.each(data,function(i,n){
													var id = n.id;
													var name = n.name;
													var opt = $("<option />");
													opt.val(id);
													opt.html(name);
													$("#" + idName).find('select.smallIdSel').append(opt);
												});
												if(val.categorySmallId != null && val.categorySmallId != ""){
													$("#" + idName).find("select.smallIdSel").val(val.categorySmallId);
												}
											}
										}
									});
								}
							}
						}
					});
				}
				
				if(val.brandName != null && val.brandName != ""){
					if($("#default_brand").val()==""){
						$("#default_brand").val(val.brandName);
						$("#default_brand_id").val(val.brandId);
					}else{
						var newC = $("#brand_span_copy").html();
						var idName = "brand_span_edit_" + i;
						newC = newC.replace("brand_span_2", idName);
						$("#brand_span_1").append(newC);
						$("#" + idName).find("input.brandName").val(val.brandName);
						$("#" + idName).find("input.brandId").val(val.brandId);
					}
				}
				
				if(val.code != null && val.code != ""){
					if($("#default_sku").val()==""){
						$("#default_sku").val(val.code);
					}else{
						var newC = $("#sku_span_copy").html();
						var idName = "sku_span_edit_" + i;
						newC = newC.replace("sku_span_2", idName);
						$("#sku_span_1").append(newC);
						$("#" + idName).find("input[name='typeCode']").val(val.code);
					}
				}
				}
			});
		}else{
			//全品类
			$("#all_items_checkbox").attr("checked",true);
			$("#coupon_range_span").hide();
		}
	
	//不包含
	var rangeinfos = $.parseJSON(couponInfos.couponRangeGroupNoInclude);
	if(rangeinfos != null){
		$.each(rangeinfos,function(i,val){
			if(i==0){
				if(val.categoryId != null && val.categoryId != ""){
					
					$("#category_span_0_no").find("select.largeIdSel").val(val.categoryId);
					var url = domain+"/item/category-cld.htm";
					$.ajax({
						  url: url,
						  type:'get',
						  data: {catId:val.categoryId},
						  success: function(data){
								if(data){
									$("#category_span_0_no").find('select.mediumIdSel').html('');
									$("#category_span_0_no").find('select.mediumIdSel').append("<option value=''>--请选择分类--</option>");
									$.each(data,function(i,n){
										var id = n.id;
										var name = n.name;
										var opt = $("<option />");
										opt.val(id);
										opt.html(name);
										$("#category_span_0_no").find('select.mediumIdSel').append(opt);
									});
									if(val.categoryMiddleId != null && val.categoryMiddleId != ""){
										$("#category_span_0_no").find("select.mediumIdSel").val(val.categoryMiddleId);
										$.ajax({
											  url: url,
											  type:'get',
											  data: {catId:val.categoryMiddleId},
											  success: function(data){
													if(data){
														$("#category_span_0_no").find('select.smallIdSel').html('');
														$("#category_span_0_no").find('select.smallIdSel').append("<option value=''>--请选择分类--</option>");
														$.each(data,function(i,n){
															var id = n.id;
															var name = n.name;
															var opt = $("<option />");
															opt.val(id);
															opt.html(name);
															$("#category_span_0_no").find('select.smallIdSel').append(opt);
														});
														if(val.categorySmallId != null && val.categorySmallId != ""){
															$("#category_span_0_no").find("select.smallIdSel").val(val.categorySmallId);
														}
													}
												}
										});
									}
								}
						  	}
						});
				}
			
				if(val.brandName != null && val.brandName != ""){
					if($("#default_brand_no").val()==""){
						$("#default_brand_no").val(val.brandName);
						$("#default_brand_id_no").val(val.brandId);
					}else{
						var newC = $("#brand_span_copy_no").html();
						var idName = "brand_span_edit_" + i;
						newC = newC.replace("brand_span_2_no", idName);
						$("#brand_span_1_no").append(newC);
						$("#" + idName).find("input.brandName").val(val.brandName);
					}
				}
				
				if(val.code != null && val.code != ""){
					if($("#default_sku_no").val()==""){
						$("#default_sku_no").val(val.code);
					}else{
						var newC = $("#sku_span_copy_no").html();
						var idName = "sku_span_edit_" + i;
						newC = newC.replace("sku_span_2_no", idName);
						$("#sku_span_1_no").append(newC);
						$("#" + idName).find("input[name='typeCode_no']").val(val.code);
					}
				}
			}else{
				if(val.categoryId != null && val.categoryId != ""){
					var newC = $("#category_span_copy_no").html();
					var idName = "category_span_edit_no" + i;
					newC = newC.replace("category_span_0_no", idName);
					$("#category_span_1_no").append(newC);
					$("#" + idName).find("select.largeIdSel").val(val.categoryId);
					var url = domain+"/item/category-cld.htm";
					$.ajax({
						url: url,
						type:'get',
						data: {catId:val.categoryId},
						success: function(data){
							if(data){
								$("#" + idName).find('select.mediumIdSel').html('');
								$("#" + idName).find('select.mediumIdSel').append("<option value=''>--请选择分类--</option>");
								$.each(data,function(i,n){
									var id = n.id;
									var name = n.name;
									var opt = $("<option />");
									opt.val(id);
									opt.html(name);
									$("#" + idName).find('select.mediumIdSel').append(opt);
								});
								if(val.categoryMiddleId != null && val.categoryMiddleId != ""){
									$("#" + idName).find("select.mediumIdSel").val(val.categoryMiddleId);
									$.ajax({
										url: url,
										type:'get',
										data: {catId:val.categoryMiddleId},
										success: function(data){
											if(data){
												$("#" + idName).find('select.smallIdSel').html('');
												$("#" + idName).find('select.smallIdSel').append("<option value=''>--请选择分类--</option>");
												$.each(data,function(i,n){
													var id = n.id;
													var name = n.name;
													var opt = $("<option />");
													opt.val(id);
													opt.html(name);
													$("#" + idName).find('select.smallIdSel').append(opt);
												});
												if(val.categorySmallId != null && val.categorySmallId != ""){
													$("#" + idName).find("select.smallIdSel").val(val.categorySmallId);
												}
											}
										}
									});
								}
							}
						}
					});
				}
				
				if(val.brandName != null && val.brandName != ""){
					if($("#default_brand_no").val()==""){
						$("#default_brand_no").val(val.brandName);
						$("#default_brand_id_no").val(val.brandId);
					}else{
						var newC = $("#brand_span_copy_no").html();
						var idName = "brand_span_edit_no" + i;
						newC = newC.replace("brand_span_2_no", idName);
						$("#brand_span_1_no").append(newC);
						$("#" + idName).find("input.brandName").val(val.brandName);
						$("#" + idName).find("input.brandId").val(val.brandId);
					}
				}
				
				if(val.code != null && val.code != ""){
					if($("#default_sku_no").val()==""){
						$("#default_sku_no").val(val.code);
					}else{
						var newC = $("#sku_span_copy_no").html();
						var idName = "sku_span_edit_no" + i;
						newC = newC.replace("sku_span_2_no", idName);
						$("#sku_span_1_no").append(newC);
						$("#" + idName).find("input[name='typeCode_no']").val(val.code);
					}
				}
				}
			});
		}
	
}); 
