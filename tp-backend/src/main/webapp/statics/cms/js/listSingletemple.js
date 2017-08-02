var PFUNC ={};
var PCACHE  ={};
var pageflag = true;

temptype = {
		"10": "单品团",
		"15": "海淘首页特卖",
		"16": "西客商城首页-热销榜单",
		"17": "西客商城首页-品牌精选",
		"102": "APP单品团",
		"206": "APP秒杀单品团",
		"207": "WAP今日精选-单品(APP)",
		"208": "WAP今日精选-专场(APP)"
	};

platform = {
		"0": "PC",
		"1": "APP"
	};
/**
 * 页面事件注册
 */
$(function(){
	
	function getSingleQueryInfo(){
		var singleTempInfo={}; 
		singleTempInfo.templeName = $('input.templeName').val();
		singleTempInfo.positionName = $('input.positionName').val();
		singleTempInfo.status = $('select.status').val();
		singleTempInfo.platformType = $('select.platformType').val();
		singleTempInfo.type = $('select.type').val();
		return singleTempInfo;
	}
	
	/**
	 * 单品团模板管理
	 */
	PFUNC.initAdvertiseRegion =function(){
		//查询
		$('#singleTempleListQuery').unbind().bind('click',function(){
			$('#cust_check').attr("checked", false);   
			if(pageflag){
				$('#currPage').text(1);
			}
			$("tr.temp_tr_singletemp").remove();   
			var params={}; 
			if($('input.templeName').val()!=null && $('input.templeName').val()!=""){
				params.templeName = $('input.templeName').val();
			}
			if($('input.positionName').val()!=null && $('input.positionName').val()!=""){
				params.positionName = $('input.positionName').val();
			}
			if($('select.status').val()!=null && $('select.status').val()!=""){
				params.status = $('select.status').val();
			}
			if($('select.platformType').val()!=null && $('select.platformType').val()!=""){
				params.platformType = $('select.platformType').val();
			}
			if($('select.type').val()!=null && $('select.type').val()!=""){
				params.type = $('select.type').val();
			}
			params.pageId = $('#currPage').text();
			params.pageSize = $('#perCount').find("option:selected").text();
			$.post(domain +"/cmsSingleTemple/querySingleTempleList.htm", {params:$.toJSON(params)}, function(redata){
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
				        		var tr = PCACHE.temp_tr_singletemp.clone(true);
				        		currPage=val.startPage;
								totalPage=val.pageSize;
								topicCount=val.totalCount;
								topicCountNum=val.totalCountNum;
				        		$(tr).find("span.pop_templeName").text(Putil.toNull(val.templeName));  
				 				$(tr).find("span.pop_positionName").text(Putil.toNull(val.positionName));  
				 				$(tr).find("span.pop_positionSize").text(Putil.toNull(val.positionSize)); 
				 				$(tr).find("span.pop_positionSort").text(Putil.toNull(val.positionSort)); 
				 				$(tr).find("span.pop_buriedCode").text(Putil.toNull(val.buriedCode)); 
				 				$(tr).find("span.pop_status").text(val.status); 
				 				
				 				$(tr).find("span.pop_platformType").text(platform[val.platformType]); 
				 				$(tr).find("span.pop_type").text(temptype[val.type]); 
				 				
				 				$(tr).find("span.pop_singleTempleId").text(val.singleTempleId); 
				 				$(tr).find("span.pop_singleTempleNodeId").text(val.singleTempleNodeId); 
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
		$('#singleTemple_list_add').unbind().bind('click',function(){
			 $(".temp_tr_singletemp :checkbox").attr("checked", false); 
			
			var singleTempInfo=getSingleQueryInfo(); 
			goPage.call(this,"/cmsSingleTemple/addSingletemple.htm",{singleTempInfo:$.toJSON(singleTempInfo)}); 
		});
		
		//修改
		$('#singleTemple_list_upd').unbind().bind('click',function(){
			var params={}; 
			var subject = [];
			var announceflag = 0;
			$("table.customertable").find('tr').each(function(){
	            var chk=$(this).find('td').find('input.dev_ck');
	            if(chk.attr("checked")=="checked"){
	            	subject.push({
	            		id:$(this).find("td").find("span.pop_singleTempleId").text()
	            	});
	            	announceflag++;
	            }
	        });  
			if(announceflag != 1){
				alert("请选择一条记录进行修改");
				return;
			}
			params = subject; 
			 $(".temp_tr_singletemp :checkbox").attr("checked", false); 
			 
			//这个是把查询条件带过去
			var singleTempInfo=getSingleQueryInfo(); 
				
			goPage.call(this,"/cmsSingleTemple/updateSingletemple.htm",{params:$.toJSON(params),singleTempInfo:$.toJSON(singleTempInfo)});   
		});
		
		//编辑
		$('a.addActivity').unbind().bind('click',function(){
			var params={}; 
			params.id = $(this).parent().parent().find('td span.pop_singleTempleNodeId').text();
			params.templeName = $(this).parent().parent().find('td span.pop_templeName').text();
			params.positionName = $(this).parent().parent().find('td span.pop_positionName').text();
			params.positionSize = $(this).parent().parent().find('td span.pop_positionSize').text();
			params.positionSort = $(this).parent().parent().find('td span.pop_positionSort').text();
			
			//$(".temp_tr_singletemp :checkbox").attr("checked", false); 
			//这个是把查询条件带过去
			var singleTempInfo=getSingleQueryInfo(); 
			goPage.call(this,"/cmsSingleTemple/addSingleActivity.htm",{params:$.toJSON(params),singleTempInfo:$.toJSON(singleTempInfo)});    
		});
		
		//删除
		$('#singleTemple_list_del').unbind().bind('click',function(){
				var params={}; 
				var subject = [];
				params.announceinfo ={};
				var announceflag = 0;
				$("table.customertable").find('tr').each(function(){
		            var chk=$(this).find('td').find('input.dev_ck');
		            if(chk.attr("checked")=="checked"){
		            	subject.push({
		            		//注意：只需要删除字表即可，查询是用inner管理的，没有字表就不会有数据
		            		id:$(this).find("td").find("span.pop_singleTempleId").text(),
		            		subId:$(this).find("td").find("span.pop_singleTempleNodeId").text()});
		            	announceflag++;
		            }
		        });  
				if(announceflag == 0){
					alert("请选择需要删除的数据");
					return;
				}
				params = subject; 
				$.post(domain +"/cmsSingleTemple/delSingleTemple.htm", {params:$.toJSON(params)}, function(redata){
					if(redata.isSuccess){
			            alert("删除成功",1);
			            $('#singleTempleListQuery').trigger('click');  
			        }else{
			        	alert("删除失败",1);
			        }
			        }, "json");
		});
		
		$('#cust_check').unbind().bind('click',function(){
			if(this.checked){    
		        $(".temp_tr_singletemp :checkbox").attr("checked", true);   
		    }else{    
		        $(".temp_tr_singletemp :checkbox").attr("checked", false); 
		    }    
		});
		
		//上一页
		$('#prePage').unbind().bind('click',function(){
			pageflag = false;
			if(parseInt($('#currPage').text()) != 1){
				$('#currPage').text(parseInt($('#currPage').text())-1);
			}
			$('#singleTempleListQuery').trigger('click');  
		});
		
		//下一页
		$('#nextPage').unbind().bind('click',function(){
			pageflag = false;
			if($('#currPage').text() != $("#totalPage").text()){
				$('#currPage').text(parseInt($('#currPage').text())+1);
			}
			$('#singleTempleListQuery').trigger('click');  
		});
		
	};
	
});

//页面初始化
$(document).ready(function()
{
	//初始化广告管理事件
	PFUNC.initAdvertiseRegion.call(this); 
	
	PCACHE.temp_tr_singletemp=$("tr.temp_tr_singletemp").clone(true);
	$('#singleTempleListQuery').trigger('click');  
});	