var PFUNC ={};
var PCACHE  ={};

var pageflag = true;

statusMap = {
		"0": "启用",
		"1": "禁用"
	};

/**
 * 页面事件注册
 */
$(function(){
	
	/**
	 * 图片类型管理
	 */
	PFUNC.initAdvertiseRegion =function(){
		//查询
		$('#advertiseListQuery').unbind().bind('click',function(){
			$('#cust_check').attr("checked", false);   
			
			$("tr.temp_tr_advertise").remove();   
			var params={}; 
			if($('input.name').val()!=null && $('input.name').val()!=""){
				params.name = $('input.name').val();
			}
			if($('input.ident').val()!=null && $('input.ident').val()!=""){
				params.ident = $('input.ident').val();
			}
			if($('select.status').val()!=null && $('select.status').val()!=""){
				params.status = $('select.status').val();
			}
			$.post(domain +"/cms/queryAdvertType.htm", {params:$.toJSON(params)}, function(redata){
					pageflag=true;
		           if(redata.isSuccess){//添加成功
		        	   var value = redata.data;
				        	$.each(value,function(i,val){  
				        		var tr = PCACHE.temp_tr_advertise.clone(true);
				        		$(tr).find("span.pop_name").text(Putil.toNull(val.name));  
				 				$(tr).find("span.pop_ident").text(Putil.toNull(val.ident));  
				 				$(tr).find("span.pop_status").text(statusMap[val.status]); 
				 				$(tr).find("span.pop_Id").text(val.id); 
				 				$("table.customertable tbody").append(tr); 
				        	});
				   }else{
					   alert(redata.message);
				   }
		        }, "json");
		});
		
		//创建
		$('#advertise_list_add').unbind().bind('click',function(){
			goPage.call(this,"/cms/editAdvertType.htm");   
		});
		
		//编辑
		$('a.editAtt').unbind().bind('click',function(){
			var params={}; 
			params.id = $(this).parent().parent().find('td span.pop_Id').text();
			goPage.call(this,"/cms/editAdvertType.htm",{params:$.toJSON(params)});   
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
	//初始化广告管理事件
	PFUNC.initAdvertiseRegion.call(this); 
	
	PCACHE.temp_tr_advertise=$("tr.temp_tr_advertise").clone(true);
	$('#advertiseListQuery').trigger('click');  
	
});	