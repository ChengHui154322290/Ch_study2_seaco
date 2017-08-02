var PFUNC ={};
var PCACHE  ={};

var pageflag = true;
var parentId = 0;

var layerpopreceive;

statusMap = {
		"1": "编辑中",
		"2": "审核中",
		"3": "已取消",
		"4": "审核通过",
		"5": "已驳回",
		"6": "已终止"
	};

/**
 * 页面事件注册
 */
$(function(){
	
	PFUNC.initAdvertiseRegion =function(){
		//查询
		$("#mastListQuery").on("click", function() {
			$("#pageNo").val(1);
			$("#contract_list_form").submit();
		});
		$("#perCount").on("change", function() {
			$("#pageNo").val(1);
			$("#contract_list_form").submit();
		});
		$("#nextPage").on(
				"click",
				function() {
					var currentPage = $("#currPage")[0].innerHTML;
					if (null == currentPage
							|| $.trim(currentPage).length == 0
							|| 0 > parseInt(currentPage)) {
						return;
					}
					var totalPage = $("#totalPage")[0].innerHTML;
					var nextPage = parseInt(currentPage) + 1;
					if (nextPage > parseInt(totalPage)) {
						layer.alert("已经是最后一页");
						return;
					}
					$("#pageNo").val(nextPage);
					$("#contract_list_form").submit();
				});
		$("#prePage").on(
				"click",
				function() {
					var currentPage = $("#currPage")[0].innerHTML;
					if (null == currentPage
							|| $.trim(currentPage).length == 0
							|| 0 > parseInt(currentPage)) {
						return;
					}
					var prePage = parseInt(currentPage) - 1;
					if (prePage < 1) {
						layer.alert("已经是第一页");
						return;
					}
					$("#pageNo").val(prePage);
					$("#contract_list_form").submit();
				});
		
		//点击操作事件
		$('tr .temp_tr_master a').unbind().bind('click',function(){
				var params={}; 
				//达人账号
				params.cmoMasterAccount = $(this).parent().parent().find('td span.pop_cmoMasterAccount').text();
				//活动编号
				params.cmoMastactCode = $(this).parent().parent().find('td span.pop_cmoMastactCode').text();
				//收益类型
				params.cmoRewardType = $(this).parent().parent().find('td span.pop_cmoRewardType').text();
				//params.id =$(this).attr("param");
				goPage.call(this,domain +"/cms/listEdMasterPlanOrder.htm",{params:$.toJSON(params)});   
		});
		
		
	};
	
});

//页面初始化
$(document).ready(function()
{
	PFUNC.initAdvertiseRegion.call(this); 
	
	PCACHE.temp_tr_master=$("tr.temp_tr_master").clone(true);
	//$('#mastListQuery').trigger('click');  
	
	$(".orderStartDate").datetimepicker({
		showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
	});

	$(".orderEndDate").datetimepicker({
		showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
	});
	
});	