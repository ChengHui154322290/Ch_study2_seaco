var DATA_URL = domain + "/order/data.htm";
var EXPORT_URL = domain + "/order/export.htm";
var VIEW_URL = "/order/view.htm"; 
var selectOption = "<option value=''>请选择</option>";
$(document).ready(function() {

	$(".select2").select2();
	$(".select2").css("margin-left","1px");
	
    $(".list_table").colResizable({
      liveDrag:true,
      gripInnerHtml:"<div class='grip'></div>", 
      draggingClass:"dragging", 
      minWidth:10,
      minHeight:10
    }); 
	$(":text[name='startTime']").datetimepicker();
	$(":text[name='endTime']").datetimepicker();
	
	var oneDay = 31 * 24 * 60 * 60;	// 31天的秒数
	
	// 导出
	$(".btn_export").click(function() {
		var sTime = $(":text[name='startTime']").val();
		var eTime = $(":text[name='endTime']").val();
		if (sTime.length > 0 && eTime.length > 0) {
			var sms = parseDate(sTime).getTime();
			var ems = parseDate(eTime).getTime();
			if ((ems - sms) / 1000 > oneDay) {
				alert("只能导出31天的数据");
				return false;
			} else if (sms >= ems) {
				alert("结束时间必须大于开始时间");
				return false;
			}
		}
		 $("#orderSearchForm").attr('action',EXPORT_URL);
		 $("#orderSearchForm").submit();
		//window.open(EXPORT_URL + "?" + $("#orderSearchForm").serialize());
	});	
	$('.btn_search').click(function(){
		$("#orderSearchForm").attr('action',domain+'/order/list.htm');
	});
	$('.btn_search').mousedown(function(){
		$("#orderSearchForm").attr('action',domain+'/order/list.htm');
	});
	jQuery(".pager").mouseover(function(e){
		$("#orderSearchForm").attr('action',domain+'/order/list.htm');
	});
	function parseDate(text) {
		var dateArr = text.split(" ")[0].split("-");
		var timeArr = text.split(" ")[1].split(":");
		var date = new Date();
		date.setFullYear(dateArr[0]);
		date.setMonth(dateArr[1] - 1)
		date.setDate(dateArr[2]);
		date.setHours(timeArr[0]);
		date.setMinutes(timeArr[1]);
		date.setSeconds(0);
		date.setMilliseconds(0);
		return date;
	}

	if($(".promotername").is('input')){
		$( ".promotername" ).autocomplete({
			source: function (request, response) {
	            var term = request.term;
	            request.promoterName = term;
	            $.post(domain+'/dss/promoterinfo/querypromoterlistbylikepromotername.htm', {
	            	promoterName:request.promoterName,promoterType:0
	              }, response,'json');
	        },
			max:10,
			minLength: 2,
			select: function( event, ui ) {
				 $("input[name='promoterId']").val(ui.item.promoterId);
				 $("input.promotername").val(ui.item.promoterName);
				 return false;
			}
		}).on('blur',function(){
			if($(this).val()=='' || $(this).val()==null){
				$(":hidden[name='promoterId']").val('');
			}
		});
		$(".promotername").data("autocomplete")._renderItem = function (ul, item) {
		    return $("<li></li>")
		        .data("item.autocomplete", item)
		        .append("<a>" + item.promoterName + ",类型："+item.promoterTypeCn+",手机："+item.mobile+"</a>")
		        .appendTo(ul);
		};
	}
	if($(".shoppromotername").is('input')){
		$( ".shoppromotername" ).autocomplete({
			source: function (request, response) {
	            var term = request.term;
	            request.promoterName = term;
	            $.post(domain+'/dss/promoterinfo/querypromoterlistbylikepromotername.htm', {
	            	promoterName:request.promoterName,promoterType:1
	              }, response,'json');
	        },
			max:10,
			minLength: 2,
			select: function( event, ui ) {
				 $("input[name='shopPromoterId']").val(ui.item.promoterId);
				 $("input.shoppromotername").val(ui.item.promoterName);
				 return false;
			}
		}).on('blur',function(){
			if($(this).val()=='' || $(this).val()==null){
				$(":hidden[name='shopPromoterId']").val('');
			}
		});
		$(".shoppromotername").data("autocomplete")._renderItem = function (ul, item) {
		    return $("<li></li>")
		        .data("item.autocomplete", item)
		        .append("<a>" + item.promoterName + ",类型："+item.promoterTypeCn+",手机："+item.mobile+"</a>")
		        .appendTo(ul);
		};
	}
	if($(".scanpromotername").is('input')){
		$( ".scanpromotername" ).autocomplete({
			source: function (request, response) {
	            var term = request.term;
	            request.promoterName = term;
	            $.post(domain+'/dss/promoterinfo/querypromoterlistbylikepromotername.htm', {
	            	promoterName:request.promoterName,promoterType:2
	              }, response,'json');
	        },
			max:10,
			minLength: 2,
			select: function( event, ui ) {
				 $("input[name='scanPromoterId']").val(ui.item.promoterId);
				 $("input.scanpromotername").val(ui.item.promoterName);
				 return false;
			}
		}).on('blur',function(){
			if($(this).val()=='' || $(this).val()==null){
				$(":hidden[name='scanPromoterId']").val('');
			}
		});
		$(".scanpromotername").data("autocomplete")._renderItem = function (ul, item) {
		    return $("<li></li>")
		        .data("item.autocomplete", item)
		        .append("<a>" + item.promoterName + ",类型："+item.promoterTypeCn+",手机："+item.mobile+"</a>")
		        .appendTo(ul);
		};
	}
	$("#supplierId").change(function(){
		var spId = $(this).val();
		if ( spId == null || spId == '' || spId.length ==0) {	
			//清空仓库
			$("#warehouseId").empty();
			$("#warehouseId").append(selectOption);
			$("#warehouseId").select2('val', '');
			return;
		}		
		updateWarehouseSelectBySpId(spId);
	});
});

// 查看
function view(code) {
	showTab("order_list_show_detail_btn_" + code, "订单查看", VIEW_URL + "?code=" + code);
	return false;
}

function showTab(id, text, url) {
	var tv = {};
	tv.linkId = id+"_link";
	tv.tabId =  id;
	tv.url = url;
	tv.text = text;
	try{
		window.parent.showTab(tv);
	} catch(e){
	}
}

function updateWarehouseSelectBySpId(spId){
	var url = domain + '/storage/warehouse/warehouselist.htm?spId=' + spId;
	$.ajax({
		type:"get",
		url: url,
		async: false,
		success : function(data){
			if(data && data.success){
				$("#warehouseId").html('');
				$("#warehouseId").append(selectOption);
				$.each(data.data,function(i,n){
					var id = n.id;
					var name = n.name;
					var opt = $("<option />");
					opt.val(id);
					opt.html(name);
					$("#warehouseId").append(opt);
				});
				$("#warehouseId").select2('val', '');
			}
		}
	});
	
}