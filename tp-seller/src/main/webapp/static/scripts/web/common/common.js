$(function(){
	$('.datetimepicker').datetimepicker({
	    'autoclose' : true,
	    'todayBtn' : true
	});
});


$.fn.serializeObject = function() {
   var o = {};  
   var a = this.serializeArray();  
   $.each(a, function() {  
       if (o[this.name]) {  
           if (!o[this.name].push) {  
               o[this.name] = [o[this.name]];  
           }  
           o[this.name].push(this.value || '');  
       } else {  
           o[this.name] = this.value || '';  
       }  
   });  
   return o;  
};  

/**
 * 获取文件格式
 */
function getFileFormat(obj){
	if(!obj){
		return "";
	}
	var objStr = obj.toString();
	if(objStr.indexOf(".")>-1){
		var lIndex = objStr.lastIndexOf(".");
		return objStr.substring(lIndex+1,objStr.length);
	} else {
		return "";
	}
}

/**
 * ajax页面请求
 */
function ajaxRequestPage(option,startPage){
	var myOption = {};
	var dataParams = {};
	myOption = jQuery.extend({},option)
	myOption["dataType"]="html";
	if(option['data']){
		dataParams = option['data'];
	}
	if(startPage){
		dataParams = jQuery.extend(dataParams,{"start":startPage});
	}
	myOption['data'] = dataParams; 
	ajaxRequest(myOption);
}

/**
 * 初始化查询页面
 */
function initQueryPage(formId,queryUrl,contentId){
	var defaultPageStart = 1;
	var pageFn = {
		loadPage:function(startPage){
			if(!startPage){
				startPage = defaultPageStart;
			}
			ajaxRequestPage({
				method:'post',
				data:jQuery("#"+formId).serializeObject(),
				url:queryUrl,
				success:function(data){
					//判断是否是登陆页面
					if(data){
						var aStr = "<!--isLoginPage-->";
						if(data.toString().length>18){
							if(aStr == data.toString().substring(0,18)){
								window.location.href="/tologin?fm=re";
							}
						}
						//console.log(aStr.substring(0,18));
						//else
						jQuery("#"+contentId).html(data);
					}
				}
			},startPage)
		},
		gotoPage:function(index){
			pageFn.loadPage(index);
		}
	};
	return pageFn;
}

/**
 * 弹出信息
 * 
 * @param msg
 */
function alertMsg(msg){
	//layer.alert(msg,9,'信息提示');
	jQuery("#alertPopMsg").find("div.modal-body").html(msg);
	$('#alertPopMsg').modal('show');
}

/**
 * 隐藏弹出框
 */
function hideAlertMsg(){
	jQuery("#alertPopMsg").modal("hide");
}

/**
 * 关闭confirm
 */
function hideCofirmBox(){
	$('#confirmPopDiv').modal('hide');
}

/**
 * 关闭弹出页面
 */
function hidePopPage(){
	$('#pagePopDiv').modal('hide');
}

function hideOldPopPage(){
	$('#myModal').modal('hide');
}

/**
 * 询问框
 * 
 * @param msg
 * @param fn
 */
function confirmBox(msg,fn){
	$('#confirmPopDiv').find("div.modal-body").html(msg);
	$('#confirmPopDiv').find("button.btn-primary").unbind("click");
	$('#confirmPopDiv').find("button.btn-primary").bind("click",function(){
		if(fn){
			fn();
		}
	});
	$('#confirmPopDiv').modal('show');
	/*var layerMe = $.layer({
	    shade: [0.4, '#000'],
	    area: ['280px','auto'],
	    dialog: {
	        msg: msg,
	        btns: 2,                    
	        type: 4,
	        btn: ['确定','取消'],
	        yes: function(){
	        	if(fn){
	        		fn();
	        	}
	        	layer.close(layerMe);
	        }, no: function(){
	        }
	    }
	});*/
}

/**
 * 弹出页面
 * 
 * @param url
 * @param params
 */
function showPopPage(url,params,title){
	ajaxRequestPage({
		method:'post',
		data:params,
		url:url,
		success:function(data){
			popDiv(data,title);
		}
	});
}

/**
 * 弹出
 * 
 * @param htmlInfo
 */
function popDiv(htmlInfo,title){
	if(title){
		jQuery("#pagePopDivLabel").html(title);
	}
	jQuery("#pagePopDiv").find("div.modal-body").html(htmlInfo);
	jQuery('#pagePopDiv').modal('show');
	/*var pageii = $.layer({
	    type: 1,
	    title: title,
	    area: ['auto', ($(window).height() - 50) +'px'],
	    border: [0], //去掉默认边框
	    shade: [0.4, '#000'], //去掉遮罩
	    closeBtn: [0, true], //去掉默认关闭按钮
	    shift: 'left', //从左动画弹出
	    page: {
	        html: '<div>'+htmlInfo+'</div>'
	    }
	});
	//自设关闭
	$('#pagebtn').on('click', function(){
	    layer.close(pageii);
	});*/
}

/**
 * 设置时间
 */
function setDate(fmt){
	var defaultFormat = 'yyyy-MM-dd HH:00:00';
	if(!fmt){
		fmt = defaultFormat;
	}
	WdatePicker({dateFmt:fmt});
}

/**
 * 显示一个弹出层
 * 
 * @param params
 */
function showPopDiv(url,params){
	var contentId = "popContentHtml";
	ajaxRequestPage({
		method:'post',
		data:params,
		url:url,
		success:function(data){
			jQuery("#"+contentId).html(data);
		}
	});
}

/**
 * ajax请求
 */
function ajaxRequest(option){
	if(!option){
		return;
	}
	if(!option['url']){
		return;
	}
	var dataParams = {};
	var requestMethod = "post";
	var dataType = "json";
	var succFun = function(data, textStatus){};
	if(option['method']){
		requestMethod = option['method'];
	}
	if(option['success']){
		succFun = option['success'];
	}
	if(option['data']){
		dataParams = option['data'];
	}
	if(option["dataType"]){
		dataType = option["dataType"];
	}
	jQuery.ajax({
		type: requestMethod,
		url: option['url'],
		data:dataParams,
		dataType:dataType,
		beforeSend: function(XMLHttpRequest){
		},
		success: succFun, 
	    complete: function(XMLHttpRequest, textStatus){
	    }, 
	    error: function(){ //请求出错处理 
	    } 
	});
}

function downloadFile(fileId){
	var form=$("<form>");//定义一个form表单
    form.attr("style","display:none");
    //form.attr("target","");
    form.attr("method","post");
    form.attr("action","/seller/upload/download");
    $("body").append(form);//将表单放置在web中
    var input1=$("<input>");
    input1.attr("type","hidden");
    input1.attr("name","fileId");
    input1.attr("value",fileId);
    form.append(input1);
    form.submit();//表单提交 
}

var popWaitDivMap = {
	showWaitDiv:function(msg){
		if(!msg){
			msg = '请稍候';
		}
		var lay = layer.load(msg);
		popWaitDivMap["waitDiv"] = lay;
	},
	hideWaitDiv:function(){
		if(popWaitDivMap["waitDiv"]){
			layer.close(popWaitDivMap["waitDiv"]);
		}
	}
};
///**
// * 关闭tab
// * 
// * @param tabId
// */
//function closeTab(tabId){
//	if(tabId){
//		tabId = tabId.toString().replace("mainIframe_","");
//	}
//	try{
//		window.parent.closeTab(tabId);
//	} catch(e){
//	}
//}
//
///**
// * 关闭当前tab
// */
//function closeCurrentTab() {
//	var currentIfame = self.frameElement.getAttribute('id');
//	closeTab(currentIfame);
//}

