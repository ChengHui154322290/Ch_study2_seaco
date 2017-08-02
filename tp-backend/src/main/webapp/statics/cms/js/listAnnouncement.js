var PFUNC ={};
var PCACHE  ={};
var pageflag = true;

announceTypeDictMap = {
		"1": "西客商城首页-资讯",
		"2": "西客商城首页-市场资讯",
		"3": "西客商城最后疯抢-资讯",
		"4": "西客商城最后疯抢-市场资讯",
		"5": "海淘首页-自定义区",
		"6": "西客商城首页-自定义区"
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
	
	/**
	 * 公告资讯管理
	 */
	PFUNC.initAnnounceRegion =function(){
		//查询
		$('#contractListQuery').unbind().bind('click',function(){
			$('#cust_check').attr("checked", false);   
			if(pageflag){
				$('#currPage').text(1);
			}
			$("tr.temp_tr_announce").remove();   
			var params={}; 
			if($('input.titleName').val()!=null && $('input.titleName').val()!=""){
				params.titleName = $('input.titleName').val();
			}
			if($('select.status').val()!=null && $('select.status').val()!=""){
				params.status = $('select.status').val();
			}
			if($('select.type').val()!=null && $('select.type').val()!=""){
				params.type = $('select.type').val();
			}
			params.pageId = $('#currPage').text();
			params.pageSize = $('#perCount').find("option:selected").text();
			$.post(domain +"/cmsIndex/queryAnnounce.htm", {params:$.toJSON(params)}, function(redata){
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
				        		var tr = PCACHE.temp_tr_announce.clone(true);
				        		currPage=val.pageNo;
								totalPage=val.pageSize;
								topicCount=val.totalCount;
								topicCountNum=val.totalCountNum;
				        		$(tr).find("span.pop_Id").text(val.id);  
				 				$(tr).find("span.pop_title").text(Putil.toNull(val.title));  
				 				$(tr).find("span.pop_type").text(announceTypeDictMap[val.type]); 
				 				$(tr).find("span.pop_link").text(Putil.toNull(val.link)); 
				 				$(tr).find("span.pop_contacts").text(Putil.toNull(val.content)); 
				 				if(val.status == 0){
				 					$(tr).find("span.pop_status").text("启用"); 
				 				}else{
				 					$(tr).find("span.pop_status").text("草稿"); 
				 				}
				 				$(tr).find("span.pop_sort").text(Putil.toNull(val.sort)); 
				 				$("table.customertable tbody").append(tr); 
				        	});
				        	$("#currPage").text(currPage);
				        	$("#totalPage").text(topicCount);
				        	$("#topicCount").text(topicCountNum);
				        	//$("#topicCount").text(totalCountNum);
				   }else{
					   alert(redata.message);
				   }
		        }, "json");
		});
		
		//创建
		$('#announcement_list_add').unbind().bind('click',function(){
			//supplierShowTab("announcement_list_add_btn","新增公告资讯","/cmsIndex/addAnnouncement.htm");
			goPage.call(this,"/cmsIndex/addAnnouncement.htm");   
		});
		
		//修改
		$('#announcement_list_upd').unbind().bind('click',function(){
			var params={}; 
			var subject = [];
			params.announceinfo ={};
			var announceflag = 0;
			$("table.customertable").find('tr').each(function(){
	            var chk=$(this).find('td').find('input.dev_ck');
	            if(chk.attr("checked")=="checked"){
	            	var tempStatus = $(this).find("td").find("span.pop_status").text();
	            	if(tempStatus=="启用"){
	            		tempStatus ="0" ;
	            	}else{
	            		tempStatus ="1" ;
	            	}
	            	subject.push({
	            		id:$(this).find("td").find("span.pop_Id").text(),
	            		title:$(this).find("td").find("span.pop_title").text(),
	            		content:$(this).find("td").find("span.pop_contacts").text(),
	            		status:tempStatus,
	            		sort:$(this).find("td").find("span.pop_sort").text(),
	            		type:$(this).find("td").find("span.pop_type").text(),
	            		link:$(this).find("td").find("span.pop_link").text()
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
			var announceInfo = {};
			announceInfo.titleNameBak = $('input.titleName').val();
			announceInfo.statusBak = $('select.status').val();
			announceInfo.typeBak = $('select.type').val();
			
			//syncPost("addAnnouncement.htm", params);
			goPage.call(this,"/cmsIndex/addAnnouncement.htm",{params:$.toJSON(params),announceInfo:$.toJSON(announceInfo)});   
		});
		
		//删除
		$('#announcement_list_del').unbind().bind('click',function(){
				var params={}; 
				var subject = [];
				params.announceinfo ={};//公告资讯实体
				var announceflag = 0;
				$("table.customertable").find('tr').each(function(){
		            var chk=$(this).find('td').find('input.dev_ck');
		            if(chk.attr("checked")=="checked"){
		            	//$(this).find('td').remove();
		            	subject.push({
		            		id:$(this).find("td").find("span.pop_Id").text()});
		            	announceflag++;
		            }
		        });  
				if(announceflag == 0){
					alert("请选择需要删除的数据");
					return;
				}
				//params.announceinfo = subject; 
				params = subject; 
				
				
				$.post(domain +"/cmsIndex/delAnnounce.htm", {params:$.toJSON(params)}, function(redata){
					if(redata.isSuccess){
			            alert("删除成功",1);
			            $('#contractListQuery').trigger('click');  
			        }else{
			        	alert("删除失败",1);
			        }
			        }, "json");
		});
		
		//禁用和启用
		$('#announcement_list_sav').unbind().bind('click',function(){
				var params={}; 
				var subject = [];
				params.announceinfo ={};//公告资讯实体
				var announceflag = 0;
				alert("123");
		});
		
		//全选和反选
		$('#cust_check').unbind().bind('click',function(){
			if(this.checked){    
		        $(".temp_tr_announce :checkbox").attr("checked", true);   
		    }else{    
		        $(".temp_tr_announce :checkbox").attr("checked", false); 
		    }    
		});
		
		//上一页
		$('#prePage').unbind().bind('click',function(){
			pageflag = false;
			if(parseInt($('#currPage').text()) != 1){
				$('#currPage').text(parseInt($('#currPage').text())-1);
			}
			$('#contractListQuery').trigger('click');  
		});
		
		//下一页
		$('#nextPage').unbind().bind('click',function(){
			pageflag = false;
			if($('#currPage').text() != $("#totalPage").text()){
				$('#currPage').text(parseInt($('#currPage').text())+1);
			}
			$('#contractListQuery').trigger('click');  
		});
		
	};
	
});

//页面初始化
$(document).ready(function()
{
	//初始化公告资讯管理事件
	PFUNC.initAnnounceRegion.call(this); 
	
	PCACHE.temp_tr_announce=$("tr.temp_tr_announce").clone(true);
	$('#contractListQuery').trigger('click');  
});	