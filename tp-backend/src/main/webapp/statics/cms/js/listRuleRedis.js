var PFUNC ={};
var PCACHE  ={};

var pageflag = true;

var layerpopreceive;

/*playformMap = {
		"PC": "PC",
		"APP": "APP"
	};*/

statusMap = {
		"0": "启用",
		"1": "禁用"
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
		$('#advertiseListQuery').unbind().bind('click',function(){
			$('#cust_check').attr("checked", false);   
			if(pageflag){
				$('#currPage').text(1);
			}
			
			$("tr.temp_tr_advertise").remove();   
			var params={}; 
			if($('input.functionName').val()!=null && $('input.functionName').val()!=""){
				params.functionName = $('input.functionName').val();
			}
			if($('input.area').val()!=null && $('input.area').val()!=""){
				params.area = $('input.area').val();
			}
			if($('select.platformType').val()!=null && $('select.platformType').val()!=""){
				params.platformType = $('select.platformType').val();
			}
			if($('select.status').val()!=null && $('select.status').val()!=""){
				params.status = $('select.status').val();
			}
			params.pageId = $('#currPage').text();
			params.pageSize = $('#perCount').find("option:selected").text();
			$.post(domain +"/cmsRuleRedis/queryRuleRedisTempList.htm", {params:$.toJSON(params)}, function(redata){
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
				        		$(tr).find("span.functionName").text(Putil.toNull(val.functionName));  
				 				$(tr).find("span.functionCode").text(val.functionCode);  
				 				$(tr).find("span.platformType").text(Putil.toNull(val.platformType)); 
				 				$(tr).find("span.area").text(Putil.toNull(val.area)); 
				 				$(tr).find("span.areaCode").text(Putil.toNull(val.areaCode)); 
				 				$(tr).find("span.firstKey").text(Putil.toNull(val.firstKey)); 
				 				$(tr).find("span.secondKey").text(Putil.toNull(val.secondKey)); 
				 				$(tr).find("span.page").text(Putil.toNull(val.page)); 
				 				$(tr).find("span.status").text(statusMap[Putil.toNull(val.status)]); 
				 				$(tr).find("span.pop_Id").text(Putil.toNull(val.id)); 
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
			goPage.call(this,"/cmsRuleRedis/editRuleRedisTemp.htm");   
		});
		
		//编辑
		$('a.editAtt').unbind().bind('click',function(){
			var params={}; 
			params.id = $(this).parent().parent().find('td span.pop_Id').text();
			
			//这个是把查询条件带过去
			var advertinfo={}; 
			advertinfo.functionName = $('input.functionName').val();
			advertinfo.area = $('input.area').val();
			advertinfo.platformType = $('select.platformType').val();
			advertinfo.status = $('select.status').val();
			//console.log(advertinfo);
			goPage.call(this,"/cmsRuleRedis/editRuleRedisTemp.htm",{params:$.toJSON(params),ruleRedisinfo:$.toJSON(advertinfo)});   
		});
		
		//上一页
		$('#prePage').unbind().bind('click',function(){
			pageflag = false;
			if(parseInt($('#currPage').text()) != 1){
				$('#currPage').text(parseInt($('#currPage').text())-1);
			}
			$('#advertiseListQuery').trigger('click');  
		});
		
		//下一页
		$('#nextPage').unbind().bind('click',function(){
			pageflag = false;
			if($('#currPage').text() != $("#totalPage").text()){
				$('#currPage').text(parseInt($('#currPage').text())+1);
			}
			$('#advertiseListQuery').trigger('click');  
		});
		
		//全选和反选
		$('#cust_check').unbind().bind('click',function(){
			if(this.checked){    
		        $(".temp_tr_advertise :checkbox").attr("checked", true);   
		    }else{    
		        $(".temp_tr_advertise :checkbox").attr("checked", false); 
		    }    
		});
		
		//启用
		$('#advertise_open').unbind().bind('click',function(){
			var params={}; 
			var subject = [];
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
				alert("请选择需要启用的数据");
				return;
			}
			params = subject; 
			$.post(domain +"/cmsRuleRedis/openRuleRedis.htm", {params:$.toJSON(params)}, function(redata){
				if(redata.isSuccess){
		            alert("启用成功",1);
		            $('#advertiseListQuery').trigger('click');  
		        }else{
		        	alert("启用失败",1);
		        }
		        }, "json");
		});
		
		//禁用
		$('#advertise_no_open').unbind().bind('click',function(){
			var params={}; 
			var subject = [];
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
				alert("请选择需要禁用的数据");
				return;
			}
			params = subject; 
			$.post(domain +"/cmsRuleRedis/noOpenRuleRedis.htm", {params:$.toJSON(params)}, function(redata){
				if(redata.isSuccess){
		            alert("禁用成功",1);
		            $('#advertiseListQuery').trigger('click');  
		        }else{
		        	alert("禁用失败",1);
		        }
		        }, "json");
		});
		
	};
	
});

//页面初始化
$(document).ready(function()
{
	//初始化广告管理事件
	PFUNC.initAdvertiseRegion.call(this); 
	
	PCACHE.temp_tr_advertise=$("tr.temp_tr_advertise").clone(true);
	$('#advertiseListQuery').trigger('click');  
	
});	