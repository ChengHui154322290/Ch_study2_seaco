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
		$('#mastListQuery').unbind().bind('click',function(){
			$('#cust_check').attr("checked", false);   
			if(pageflag){
				$('#currPage').text(1);
			}
			
			$("tr.temp_tr_advertise").remove();   
			var params={}; 
			if($('input.cmaNum').val()!=null && $('input.cmaNum').val()!=""){
				params.cmaNum = $('input.cmaNum').val();
			}
			if($('input.cmaActivityName').val()!=null && $('input.cmaActivityName').val()!=""){
				params.cmaActivityName = $('input.cmaActivityName').val();
			}
			if($('select.cmaActivityStatus').val()!=null && $('select.cmaActivityStatus').val()!=""){
				params.cmaActivityStatus = $('select.cmaActivityStatus').val();
			}
			params.pageId = $('#currPage').text();
			params.pageSize = $('#perCount').find("option:selected").text();
			$.post(domain +"/cms/queryMasterPlanList.htm", {params:$.toJSON(params)}, function(redata){
					pageflag=true;
		           if(redata.isSuccess){//添加成功
		        	   
		        	 //首先赋予初始值
		        	   $("#currPage").text(1);
			        	$("#totalPage").text(1);
			        	$("#topicCount").text(0);
			        	
		        	   var value = redata.data;
						var currPage;
						var totalPage;
						var topicCount;
						var topicCountNum;
				        	$.each(value,function(i,val){  
				        		var tr = PCACHE.temp_tr_advertise.clone(true);
				        		currPage=val.pageNo;
								totalPage=val.pageSize;
								topicCount=val.totalCount;
								topicCountNum=val.totalCountNum;
				        		$(tr).find("span.pop_cmaNum").text(Putil.toNull(val.id));  
				 				$(tr).find("span.pop_cmaActivityName").text(val.cmaActivityName);  
				 				$(tr).find("span.pop_cmaPlanRecruit").text(Putil.toNull(val.cmaPlanRecruit)); 
				 				$(tr).find("span.pop_cmaActualRecruit").text(val.cmaActualRecruit==null?0:val.cmaActualRecruit); 
				 				$(tr).find("span.pop_validityTime").text(Putil.toNull(val.validityTime)); 
				 				$(tr).find("span.pop_cecruitTime").text(Putil.toNull(val.cmaRecruitEnddateStr)); 
				 				$(tr).find("span.pop_cmaActivityStatus").text(Putil.toNull(statusMap[val.cmaActivityStatus])); 
				 				$(tr).find("span.pop_progress").text(Putil.toNull(val.progress)); 
				 				$(tr).find("span.pop_id").text(Putil.toNull(val.id)); 
				 				
								if(val.cmaActivityStatus == "1"){
									//编辑中:单据保存后状态为编辑，且显示（编辑，取消）
									$(tr).find(".editAtt").removeAttr("style"); 
									$(tr).find(".cancelAtt").removeAttr("style"); 
								}else if(val.cmaActivityStatus == "2"){
									//审核中：单据提交后状态为审核中，且显示（审核，驳回）
									$(tr).find(".examineAtt").removeAttr("style"); 
									$(tr).find(".rejectAtt").removeAttr("style"); 
								}else if(val.cmaActivityStatus == "4"){
									//审核通过：单据审核通过后，显示（终止）
									//$(tr).find(".rejectAtt").removeAttr("style"); 
									$(tr).find(".stopAtt").removeAttr("style"); 
								}else {
									//已取消和已终止的，不需要再继续操作了
								}
								
								/*$(tr).find(".examineAtt").removeAttr("style"); 
								$(tr).find(".stopAtt").removeAttr("style"); 
								$(tr).find(".rejectAtt").removeAttr("style"); 
								$(tr).find(".cancelAtt").removeAttr("style"); */
				 				
				 				$("table.customertable tbody").append(tr); 
				        	});
				        	$("#currPage").text(currPage);
				        	$("#totalPage").text(topicCount);
				        	$("#topicCount").text(topicCountNum);
				   }else{
					   alert(redata.message);
				   }
		        }, "json");
		});
		
		//创建
		$('#advertise_list_add').unbind().bind('click',function(){
			goPage.call(this,domain +"/cms/editMasterPlan.htm");   
		});
		
		/*//编辑
		$('a.editAtt').unbind().bind('click',function(){
			var params={}; 
			params.id = $(this).parent().parent().find('td span.pop_Id').text();
			goPage.call(this,domain +"/cms/editMasterPlan.htm",{params:$.toJSON(params)});   
		});*/
		
		//上一页
		$('#prePage').unbind().bind('click',function(){
			pageflag = false;
			if(parseInt($('#currPage').text()) != 1){
				$('#currPage').text(parseInt($('#currPage').text())-1);
			}
			$('#mastListQuery').trigger('click');  
		});
		
		//下一页
		$('#nextPage').unbind().bind('click',function(){
			pageflag = false;
			if($('#currPage').text() != $("#totalPage").text()){
				$('#currPage').text(parseInt($('#currPage').text())+1);
			}
			$('#mastListQuery').trigger('click');  
		});
		
		//点击操作事件
		$('tr .temp_tr_advertise a').unbind().bind('click',function(){
			if($(this).text() != "【编辑】" && $(this).text() != "【详情】"){
				$("select.cmeResultTmp").find("option").remove();
				/*$("#examineAttId").attr("style","display:none"); 
				$("#stopAttId").attr("style","display:none");
				$("#rejectAttId").attr("style","display:none");
				$("#cancelAttId").attr("style","display:none");*/
				if($(this).text() == "【审核】"){
					$("#examineAttId").removeAttr("style"); 
					/*$("select.cmaActivityStatus").attr("value","4");*/
					$("select.cmeResultTmp").append("<option id='examineAttId'  value='4'>审核</option>");
				}else if($(this).text() == "【终止】"){
					$("#stopAttId").removeAttr("style"); 
					/*$("select.cmaActivityStatus").attr("value","6");*/
					$("select.cmeResultTmp").append("<option id='rejectAttId'  value='6'>终止</option>");
				}else if($(this).text() == "【驳回】"){
					$("#rejectAttId").removeAttr("style"); 
					/*$("select.cmaActivityStatus").attr("value","5");*/
					$("select.cmeResultTmp").append("<option id='rejectAttId'  value='5'>驳回</option>");
				}else if($(this).text() == "【取消】"){
					$("#cancelAttId").removeAttr("style"); 
					/*$("select.cmaActivityStatus").attr("value","3");*/
					$("select.cmeResultTmp").append("<option id='rejectAttId'  value='3'>取消</option>");
				}
				//设置id
				parentId = $(this).parent().parent().find('td span.pop_id').text();
				layerpopreceive=window.open("applay_pop","审核",function(){      
				},['380px', '280px'],true);
			}else if($(this).text() == "【编辑】"){
				var params={}; 
				params.id = $(this).parent().parent().find('td span.pop_id').text();
				params.status = "edit";
				goPage.call(this,domain +"/cms/editMasterPlan.htm",{params:$.toJSON(params)});   
			}else{
				var params={}; 
				params.id = $(this).parent().parent().find('td span.pop_id').text();
				params.status = "view";
				goPage.call(this,domain +"/cms/editMasterPlan.htm",{params:$.toJSON(params)});   
			}
		});
		
		/**********************************  一下是弹出框事件    *******************************************************/
		//关闭弹出框
		$('#dev_cancel').unbind().bind('click',function(){
			layer.close($("#applay_pop").data("pop"));
		});
		
		//提交
		$('#dev_submit').unbind().bind('click',function(){
			var params = {};
			params.cmeSuggestionTmp = $("textarea.cmeSuggestionTmp").val();
			params.cmeResultTmp = $("select.cmeResultTmp").val();
			params.id = parentId;
			if(Putil.isEmpty(params.cmeResultTmp)){
				alert("请选择审核结果");
				return;
			}
			if(Putil.isEmpty(params.cmeSuggestionTmp)){
				alert("请选择审核意见");
				return;
			}
			console.log(params);
			$.post(domain +"/cms/examineSumit.htm", {params:$.toJSON(params)}, function(redata){
		           if(redata.isSuccess){//添加成功
		        	   alert("提交成功");
		        	   goPageGet.call(this,domain +"/cms/listMasterPlan.htm");   
				   }else{
					   alert("提交失败，请联系管理员");
				   }
		        }, "json");
		});
		/*****************************************************************************************/
		
	};
	
});

//页面初始化
$(document).ready(function()
{
	PFUNC.initAdvertiseRegion.call(this); 
	
	PCACHE.temp_tr_advertise=$("tr.temp_tr_advertise").clone(true);
	$('#mastListQuery').trigger('click');  
	
	/*$(".startdate").datetimepicker({
		dayOfWeekStart : 1,
		lang : 'zh',
		dateFormat:'yyyy-MM-dd HH:mm:ss'
	});

	$(".enddate").datetimepicker({
		dayOfWeekStart : 1,
		lang : 'zh',
		dateFormat:'yyyy-MM-dd HH:mm:ss'
	});*/
	
});	