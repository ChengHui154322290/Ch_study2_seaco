var PFUNC ={};
var PCACHE  ={};

var pageflag = true;

var layerpopreceive;

/*transSettleDictMap = {
		"3": "首页-轮播图",
		"5": "首页-最优惠图片",
		"6": "最后疯抢-最优惠图片",
		"7": "首页-弹出层",
		"8": "用户登录-图片",
		"101": "(APP)首页-广告位",
		"102": "(APP)秒杀-广告位信息",
		"103": "(APP)首页-功能标签信息",
		"104": "(APP)海淘-广告位信息",
		"105": "(APP)广告-启动页面",
		"106": "(APP)广告-支付成功",
		"107": "(APP)wap-首页弹框",
		"108": "(APP)Wap-今日精选-首图"
	};*/

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
	
	/**
	 * 广告管理
	 */
	PFUNC.initAdvertiseRegion =function(){
		//查询
		$('#advertiseListQuery').unbind().bind('click',function(){
			$('#cust_check').attr("checked", false);   
			if(pageflag){
				$('#currPage').text(1);
			}
			
			$("tr.temp_tr_advertise").remove();   
			var params={}; 
			if($('input.titleName').val()!=null && $('input.titleName').val()!=""){
				params.titleName = $('input.titleName').val();
			}
			if($('input.position').val()!=null && $('input.position').val()!=""){
				params.position = $('input.position').val();
			}
			if($('input.startdate').val()!=null && $('input.startdate').val()!=""){
				params.startdate = $('input.startdate').val();
			}
			if($('input.enddate').val()!=null && $('input.enddate').val()!=""){
				params.enddate = $('input.enddate').val();
			}
			if($('input.type').val()!=null && $('input.type').val()!=""){
				params.type = $('input.type').val();
			}
			if($('select.status').val()!=null && $('select.status').val()!=""){
				params.status = $('select.status').val();
			}
			params.pageId = $('#currPage').text();
			params.pageSize = $('#perCount').find("option:selected").text();
			$.post(domain +"/cmsAdvertIndex/queryAdvertiseTempList.htm", {params:$.toJSON(params)}, function(redata){
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
				        		$(tr).find("span.pop_advertname").text(Putil.toNull(val.advertname));  
				 				$(tr).find("span.pop_type").text(val.type);  
				 				//$(tr).find("span.pop_position").text(val.position); 
				 				$(tr).find("span.pop_sort").text(Putil.toNull(val.sort)); 
				 				$(tr).find("span.pop_link").text(Putil.toNull(val.link)); 
				 				$(tr).find("span.pop_startdate").text(Putil.toNull(val.startdateStr)); 
				 				$(tr).find("span.pop_enddate").text(Putil.toNull(val.enddateStr)); 
				 				$(tr).find("span.pop_activityid").text(Putil.toNull(val.activityid)); 
				 				$(tr).find("span.pop_sku").text(Putil.toNull(val.sku)); 
				 				$(tr).find("span.pop_actType").text(Putil.toNull(val.actType)); 
				 				$(tr).find("span.pop_time").text(Putil.toNull(val.time)); 
				 				$(tr).find("span.pop_platformType").text(playformMap[val.platformType]); 
				 				$(tr).find("span.pop_status").text(statusMap[val.status]); 
				 				$(tr).find("span.pop_Id").text(Putil.toNull(val.id)); 
				 				$(tr).find("span.pop_path").text(Putil.toNull(val.path)); 
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
			goPage.call(this,"/cmsAdvertIndex/editAdvertiseTemp.htm"); 
		});
		
		$('#picSizeRemark').unbind().bind('click',function(){
			
			layer.tips('首页-轮播图：1920*325<br>首页-最优惠（右下方）：宽度395，高度不限<br>最后疯抢-最优惠（右下方）：宽度395，高度不限<br>登陆页面-左侧：550*350<br>'+
					'App首页轮播图：640*380<br>App商城广告图：640*120', this, {
			    style: ['background-color:#78BA32; color:#fff', '#78BA32'],
			    maxWidth:300,
			    time: 5,
			    closeBtn:[0, true]
			});
			
			/*$.layer({
				type : 2,
				title : "图片尺寸说明",
				shadeClose : true,
				maxmin : true,
				fix : false,
				area : [ '600px', 400 ],
				iframe : {
					src : imgSrc
				}
			});*/
			
		});
		
		//修改
		/*$('#announcement_list_upd').unbind().bind('click',function(){
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
			goPage.call(this,domain +"/cmsIndex/addAnnouncement.htm",{params:$.toJSON(params)});   
		});*/
		
		
		//编辑
		$('a.editAtt').unbind().bind('click',function(){
			var params={}; 
			params.id = $(this).parent().parent().find('td span.pop_Id').text();
			
			//这个是把查询条件带过去
			var advertinfo={}; 
			advertinfo.titleName = $('input.titleName').val();
			advertinfo.position = $('input.position').val();
			advertinfo.startdate = $('input.startdate').val();
			advertinfo.enddate = $('input.enddate').val();
			advertinfo.type = $('input.type').val();
			advertinfo.status = $('select.status').val();
			
			goPage.call(this,"/cmsAdvertIndex/editAdvertiseTemp.htm",{params:$.toJSON(params),advertinfo:$.toJSON(advertinfo)});   
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
		
		//点击table，会显示图片信息
		$('.customertable tr td').unbind().bind('click',function(){
			var imgSrc = $(this).parent().find("td span.pop_path").text();
			$("#imgPrivew").src = imgSrc;
			$("#imgPrivew").attr("src",imgSrc);
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
			$.post(domain +"/cmsAdvertIndex/openAdertise.htm", {params:$.toJSON(params)}, function(redata){
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
			$.post(domain +"/cmsAdvertIndex/noOpenAdertise.htm", {params:$.toJSON(params)}, function(redata){
				if(redata.isSuccess){
		            alert("禁用成功",1);
		            $('#advertiseListQuery').trigger('click');  
		        }else{
		        	alert("禁用失败",1);
		        }
		        }, "json");
		});
		
		$('#imgPrivew').unbind().bind('click',function(){
			var imgSrc = $("#imgPrivew").attr("src");
			$.layer({
				type : 2,
				title : "图片信息",
				shadeClose : true,
				maxmin : true,
				fix : false,
				area : [ '600px', 400 ],
				iframe : {
					src : imgSrc
				}
			});
		});
		
		//点击查询，弹出图片类型查询框
		$('#advert_type_query').unbind().bind('click',function(){
			$("tr.temp_dev_tr_advertise").remove();   
			layerpopreceive=window.open("advert_type_pop","图片类型",function(){      
				},['550px', '450px'],true);
		});
		
		/**********************************  一下是弹出框事件    *******************************************************/
		//关闭弹出框
		$('#dev_advert_type_close').unbind().bind('click',function(){
			layer.close($("#advert_type_pop").data("pop"));
		});
		
		//点击弹出框里面的查询
		$('#dev_advert_type_query').unbind().bind('click',function(){
			$("tr.temp_dev_tr_advertise").remove();   
			var params={}; 
			if($('input.dev_name').val()!=null && $('input.dev_name').val()!=""){
				params.name = $('input.dev_name').val();
			}
			params.status = "0";
			$.post(domain +"/cms/queryAdvertType.htm", {params:$.toJSON(params)}, function(redata){
					pageflag=true;
		           if(redata.isSuccess){//添加成功
		        	   var value = redata.data;
				        	$.each(value,function(i,val){  
				        		var tr = PCACHE.temp_dev_tr_advertise.clone(true);
				        		$(tr).find("span.pop_dev_name").text(Putil.toNull(val.name));  
				 				$(tr).find("span.pop_dev_ident").text(Putil.toNull(val.ident));  
				 				$(tr).find("span.pop_dev_id").text(val.id); 
				 				
				 				//添加单击和双击事件
					        	$(tr).find("span").unbind().bind("click",function(){ 	
					        		$("#type_query").val(Putil.toNull(val.name));
					        		$("#ident_query").val(Putil.toNull(val.ident));
					        		layer.close($("#advert_type_pop").data("pop"));
					        	});
					        	
				 				$("table.dev_customertable tbody").append(tr); 
				        	});
				   }else{
					   alert(redata.message);
				   }
		        }, "json");
		});
		/*****************************************************************************************/
		
	};
	
});

//页面初始化
$(document).ready(function()
{
	//初始化广告管理事件
	PFUNC.initAdvertiseRegion.call(this); 
	
	PCACHE.temp_tr_advertise=$("tr.temp_tr_advertise").clone(true);
	PCACHE.temp_dev_tr_advertise=$("tr.temp_dev_tr_advertise").clone(true);
	$('#advertiseListQuery').trigger('click');  
	
	$(".startdate").datetimepicker({
		dayOfWeekStart : 1,
		lang : 'zh',
		dateFormat:'yyyy-MM-dd HH:mm:ss'
	});

	$(".enddate").datetimepicker({
		dayOfWeekStart : 1,
		lang : 'zh',
		dateFormat:'yyyy-MM-dd HH:mm:ss'
	});
	/*$(".startdate").datetimepicker({
        showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
    });
	
	$(".enddate").datetimepicker({
        showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
    });*/
	
});	