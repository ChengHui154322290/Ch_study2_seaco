var PFUNC ={};
var PCACHE  ={};

var pageflag = true;


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
			
			$("tr.temp_tr_singleActivitytemp").remove();   
			var params={}; 
			if($('input.userId').val()!=null && $('input.userId').val()!=""){
				params.userId = $('input.userId').val();
			}
			if($('input.userName').val()!=null && $('input.userName').val()!=""){
				params.userName = $('input.userName').val();
			}
			params.pageId = $('#currPage').text();
			params.pageSize = $('#perCount').find("option:selected").text();
			$.post(domain +"/cms/queryFeedbackList.htm", {params:$.toJSON(params)}, function(redata){
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
				        		var tr = PCACHE.temp_tr_singleActivitytemp.clone(true);
				        		currPage=val.pageNo;
								totalPage=val.pageSize;
								topicCount=val.totalCount;
								topicCountNum=val.totalCountNum;
								if(val.userName == null
										|| val.userName == ""){
									$(tr).find("span.userName").text("匿名");  
								}else{
									$(tr).find("span.userName").text(val.userName);  
								}
				 				$(tr).find("span.feedbackDate").text(Putil.toNull(val.feedbackDateStr));  
				 				$(tr).find("span.feedbackInfo").text(Putil.toNull(val.feedbackInfo)); 
				 				$(tr).find("span.mobile").text(Putil.toNull(val.mobile)); 
				 				$(tr).find("span.email").text(Putil.toNull(val.email)); 
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
		        $(".temp_tr_singleActivitytemp :checkbox").attr("checked", true);   
		    }else{    
		        $(".temp_tr_singleActivitytemp :checkbox").attr("checked", false); 
		    }    
		});
		
		//查看
		$('a.viewAtt').unbind().bind('click',function(){
			var params={}; 
			params.id = $(this).parent().parent().find('td span.pop_Id').text();
			
			//这个是把查询条件带过去
			var feedbackInfo={}; 
			feedbackInfo.userIdBak = $('input.userId').val();
			feedbackInfo.userNameBak = $('input.userName').val();
			
			goPage.call(this,"/cms/viewFeedback.htm",{params:$.toJSON(params),feedbackInfo:$.toJSON(feedbackInfo)});   
		});
		
		//删除
		$('a.delAtt').unbind().bind('click',function(){
			var params={}; 
	       	 var subject = [];
	       	 subject.push({
	        		id:$(this).parent().parent().find('td span.pop_Id').text()});
	       	 params = subject; 
			var index = layer.confirm(
	                 "您确定需要删除该条反馈信息吗?",
	                  function(index){
	         			$.post(domain +"/cms/delFeedback.htm", {params:$.toJSON(params)}, function(redata){
	    					pageflag=true;
	    		           if(redata.isSuccess){//添加成功
	    		        	   alert("删除成功");
	    		        	   $('#advertiseListQuery').trigger('click');  
	    				   }else{
	    					   alert("删除失败");
	    				   }
	    		        }, "json");
	                	 
	                     layer.close(index);                        
	                  },
	                  "提示"
	               );
		});
		
	};
	
});

//页面初始化
$(document).ready(function()
{
	//初始化广告管理事件
	PFUNC.initAdvertiseRegion.call(this); 
	
	PCACHE.temp_tr_singleActivitytemp=$("tr.temp_tr_singleActivitytemp").clone(true);
	
	$('#advertiseListQuery').trigger('click');  
	
});	