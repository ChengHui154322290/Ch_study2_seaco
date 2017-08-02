var PFUNC ={};
var PCACHE  ={};

var layerpopreceive;

playformMap = {
		"0": "PC",
		"1": "APP"
	};

statusMap = {
		"0": "启用",
		"1": "禁用",
		"2": "过期"
	};

/**
 * 页面事件注册
 */
$(function(){
	
	/**
	 * 出一个tab
	 */
	function supplierShowTab(id,text,tabUrl){
		var tv = {};
		tv.linkId = id+"_link";
		tv.tabId =  id;
		tv.url = tabUrl;
		tv.text = text;
		try{
			window.parent.showTab(tv);
		} catch(e){
		}
	}
	
	PFUNC.initAdvertiseRegion =function(){
		//查询
		$("#advertiseListQuery").on("click", function() {
			$("#pageNo").val(1);
			$("#contract_list_form").submit();
		});
		$("#cancelAct").on("click", function() {
			$("#activityName_id").attr("value","");
			$("#startdate_id").attr("value","");
			$("#enddate_id").attr("value","");
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
		
		//创建
		$('#advertise_list_add').unbind().bind('click',function(){
			var params={}; 
			//这个是把查询条件带过去
			params.actNameBak = $('#actNameBak').val();
			goPage.call(this,"/topicCoupon/editActExchangeCode.htm");   
		});
		
		//编辑
		$('a.editAtt').unbind().bind('click',function(){
			var params={}; 
			params.id = $(this).parent().parent().find('td span.pop_Id').text();
			
			//这个是把查询条件带过去
			params.actNameBak = $('#actNameBak').val();
			
			goPage.call(this,"/topicCoupon/editActExchangeCode.htm",{params:$.toJSON(params)});    
		});
		
		//终止
		$('a.stopAtt').unbind().bind('click',function(){
			var params={}; 
			params.id = $(this).parent().parent().find('td span.pop_Id').text();//ID
			var index = layer.confirm(
	                 "您确定需要终止该活动吗?",
	                  function(index){
	         			$.post(domain +"/topicCoupon/stopActExchangeCode.htm", {params:$.toJSON(params)}, function(redata){
	    		           if(redata.success){//添加成功
	    		        	   alert("终止成功");
	    		        	   $('#advertiseListQuery').trigger('click');  
	    				   }else{
	    					   alert("终止失败");
	    				   }
	    		        }, "json");
	                	 
	                     layer.close(index);                        
	                  },
	                  "提示"
	               );
		});
		
		//全选和反选
		$('#cust_check').unbind().bind('click',function(){
			if(this.checked){    
		        $(".temp_tr_advertise :checkbox").attr("checked", true);   
		    }else{    
		        $(".temp_tr_advertise :checkbox").attr("checked", false); 
		    }    
		});
		
	};
	
});

//页面初始化
$(document).ready(function()
{
	PFUNC.initAdvertiseRegion.call(this); 
	
});	