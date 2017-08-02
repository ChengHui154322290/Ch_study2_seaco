var PFUNC = {};
var PCACHE = {};
/**
 * 页面事件注册
 */
$(function() {

	/**
	 * 出一个tab
	 */
	function supplierShowTab(id, text, tabUrl) {
		var tv = {};
		tv.linkId = id + "_link";
		tv.tabId = id;
		tv.url = tabUrl;
		tv.text = text;
		try {
			window.parent.showTab(tv);
		} catch (e) {
		}
	}

	PFUNC.retpar = function() {
		$('.btn_nochecked').unbind().bind('click',function(){
			//history.go(-1);
			var announceInfo = {};
			announceInfo.titleNameBak = $('#titleNameBak').val();
			announceInfo.statusBak = $('#statusBak').val();
			announceInfo.typeBak = $('#typeBak').val();
			//goPageGet.call(this,domain +"/cmsIndex/listAnnouncement.htm?announceInfo="+$.toJSON(announceInfo));  
			window.location.href=domain +"/cmsIndex/listAnnouncement.htm?announceInfo="+$.toJSON(announceInfo);
		});
	};
	
	PFUNC.submitForm = function() {
		$('.btn_sab').unbind().bind('click',function(){
			var params = {};
			params.id = $("input.announce_id").val();
			params.title = $("input.title").val();
			params.status = $("select.status").val();
			params.sort = $("input.sort").val();
			params.link = $("input.link").val();
			params.type = $("select.type").val();
			//params.content = $("textarea.content ").val();
			params.content = $("#pcContentEditor").val();
			if(Putil.isEmpty(params.title)){
				alert("请填写标题");
				return;
			}
			if(Putil.isEmpty(params.status)){
				alert("请选择状态");
				return;
			}
			if(Putil.isEmpty(params.sort)){
				alert("请填写排序");
				return;
			}
			if(Putil.isEmpty(params.content)){
				alert("请填写内容");
				return;
			}
			if(Putil.isEmpty(params.type)){
				alert("请填写页面位置");
				return;
			}
			
			$.post(domain +"/cmsIndex/addAnnounce.htm", {params:$.toJSON(params)}, function(redata){
		           if(redata.isSuccess){//添加成功
		        	   alert("提交成功");
		        	   //goPage.call(this,"/cmsIndex/listPostAnnouncement.htm");   
		        	   //goPageGet.call(this,domain +"/cmsIndex/listAnnouncement.htm");   
		        	   
		        	   var announceInfo = {};
			   		   announceInfo.titleNameBak = $('#titleNameBak').val();
			   		   announceInfo.statusBak = $('#statusBak').val();
			   	       announceInfo.typeBak = $('#typeBak').val();
			   	       window.location.href=domain+"/cmsIndex/listAnnouncement.htm?announceInfo="+$.toJSON(announceInfo);  
				   }else{
					   alert(redata.data);
				   }
		        }, "json");
		});
	};
	
	/**
	 * 屏蔽输入框只能输入数字
	 */
	PFUNC.inputFormValidate = function() {
		/**
		 * 屏蔽顺序的输入框只能输入数字
		 */
		$("input.sort").keyup(function(){      
	        var tmptxt=$(this).val();      
	        $(this).val(tmptxt.replace(/\D|^0/g,''));      
	   }).bind("paste",function(){      
	        var tmptxt=$(this).val();      
	        $(this).val(tmptxt.replace(/\D|^0/g,''));      
	   }).css("ime-mode", "disabled");     
	};
	
});

// 页面初始化
$(document).ready(function() {
	// 返回事件
	PFUNC.retpar.call(this);
	// 提交事件
	PFUNC.submitForm.call(this);
	// 控制界面的输入框只能输入数字问题
	PFUNC.inputFormValidate.call(this);
	
	$(".status").val($(".announce_status").val());
	

});