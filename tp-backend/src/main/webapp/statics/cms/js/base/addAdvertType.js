var PFUNC ={};
var PCACHE  ={};
/**
 * 页面事件注册
 */
$(function(){
	
	PFUNC.inputFormSaveBtn = function() {
		$('#inputFormSaveBtn').live('click',function(){
			if(Putil.isEmpty($("input.name").val())){
				alert("请填写类型名称");
				return;
			}
			if(Putil.isEmpty($("input.ident").val())){
				alert("请填写接口标识");
				return;
			}
			var params={}; 
			params.name = $('input.name').val();
			params.ident = $('input.ident').val();
			params.status = $('select.status').val();
			params.id = $('input.idd').val();
			$.post(domain +"/cms/saveAdvertType.htm", {params:$.toJSON(params)}, function(redata){
				pageflag=true;
	           if(redata == "000"){
	        	   alert("提交成功");
					goPageGet.call(this,"/cms/listAdvertType.htm");   
			   }else if(redata == "001"){
				   alert("提交失败，请联系管理员");
			   }else if(redata == "002"){
				   alert("提交失败，已经存在接口标识");
			   }
	        }, "json");
			
		});
	};
	
	/**
	 * 屏蔽输入框只能输入数字
	 */
	PFUNC.inputFormValidate = function() {
			$("input.ident").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");    
			
	};
	
});

//页面初始化
$(document).ready(function()
{
	// 提交事件
	PFUNC.inputFormSaveBtn.call(this);
	
	// 控制界面的输入框只能输入数字问题
	PFUNC.inputFormValidate.call(this);
	
});	