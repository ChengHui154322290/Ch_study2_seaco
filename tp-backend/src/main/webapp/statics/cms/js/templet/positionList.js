var PFUNC ={};
var PCACHE  ={};

var pageflag = true;
var parentId = 0;

var layerpopreceive;

statusMap = {
		"0": "启用",
		"1": "停用"
	};

/**
 * 页面事件注册
 */
$(function(){
	
	PFUNC.initAdvertiseRegion =function(){
		//查询
		$("#positionListQuery").on("click", function() {
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
		
		
		//创建
		$('#position_add').unbind().bind('click',function(){
			goPage.call(this,"/cms/addPosition.htm");   
		});
		
		//修改
		$('#position_upd').unbind().bind('click',function(){
			var params={}; 
			var subject = [];
			params.announceinfo ={};
			var announceflag = 0;
			$("table.customertable").find('tr').each(function(){
	            var chk=$(this).find('td').find('input.dev_ck');
	            if(chk.attr("checked")=="checked"){
	            	subject.push({
	            		id:$(this).find("td").find("span.pop_Id").text()
	            	});
	            	announceflag++;
	            }
	        });  
			if(announceflag != 1){
				alert("请选择一条记录进行修改");
				return;
			}
			params = subject; 
			
			//需要把查询条件也带到修改页面去，返回或提交成功后可以把查询条件带回来
			var positionInfo = {};
			positionInfo.pageName = $('input.pageName').val();
			positionInfo.templeName = $('input.templeName').val();
			positionInfo.positionName = $('input.positionName').val();
			
			goPage.call(this,"/cms/addPosition.htm",{params:$.toJSON(params),positionInfo:$.toJSON(positionInfo)});   
		});
		
		//删除
		$('#position_del').unbind().bind('click',function(){
				var params={}; 
				var subject = [];
				params.announceinfo ={};//公告资讯实体
				var announceflag = 0;
				$("table.customertable").find('tr').each(function(){
		            var chk=$(this).find('td').find('input.dev_ck');
		            if(chk.attr("checked")=="checked"){
		            	subject.push({
		            		id:$(this).find("td").find("span.pop_Id").text()});
		            	announceflag++;
		            }
		        });  
				if(announceflag == 0){
					alert("请选择需要删除的数据");
					return;
				}
				params = subject; 
				
				var index = layer.confirm(
		                 "您确定需要删除该条反馈信息吗?",
		                  function(index){
		                	 $.post(domain +"/cms/delPosition.htm", {params:$.toJSON(params)}, function(redata){
								if(redata.isSuccess){
						            alert("删除成功",1);
						            $('#positionListQuery').trigger('click');  
						        }else{
						        	alert("删除失败",1);
						        }
						      }, "json");
		                	 
		                     layer.close(index);                        
		                  },
		                  "提示"
		          );
		});
		
		
		
		//编辑
		$('a.addInfo').unbind().bind('click',function(){
			var params={}; 
			params.pageId = $(this).parent().parent().find('td span.pop_pageId').text();//页面ID
			params.templetId = $(this).parent().parent().find('td span.pop_templetId').text();//模块ID
			params.positionId = $(this).parent().parent().find('td span.pop_Id').text();//位置ID
			params.pageName = $(this).parent().parent().find('td span.pop_pageName').text();//页面名称
			params.templeName = $(this).parent().parent().find('td span.pop_templeName').text();//模块名称
			params.positionName = $(this).parent().parent().find('td span.pop_positionName').text();//位置名称
			params.elementType = $(this).parent().parent().find('td span.elementType_code').text();//页面元素
			
			//这个是把查询条件带过去
			params.pageNameBak = $('input.pageName').val();
			params.templeNameBak = $('input.templeName').val();
			params.positionNameBak = $('input.positionName').val(); 
			
			goPage.call(this,"/cms/addSingleActivity.htm",{params:$.toJSON(params)/*,singleTempInfo:$.toJSON(singleTempInfo)*/});    
		});
		
		//全选和反选
		$('#cust_check').unbind().bind('click',function(){
			if(this.checked){    
		        $(".temp_tr_master :checkbox").attr("checked", true);   
		    }else{    
		        $(".temp_tr_master :checkbox").attr("checked", false); 
		    }    
		});
		
	};
	
});

//页面初始化
$(document).ready(function()
{
	PFUNC.initAdvertiseRegion.call(this); 
	
});	