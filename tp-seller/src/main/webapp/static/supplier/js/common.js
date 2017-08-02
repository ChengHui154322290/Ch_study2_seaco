var sp_supplierListTabId = "mainIframe_tabli_22";
var sp_contractListTabId = "mainIframe_tabli_23";
var sp_quatationListTabId = "mainIframe_tabli_24";
var sp_purchaseorderListTabId = "mainIframe_tabli_25";
var sp_purchaseorderbackListTabId = "mainIframe_tabli_26";
var sp_sellorderListTabId = "mainIframe_tabli_27";
var sp_sellorderbackListTabId = "mainIframe_tabli_28";
var sp_warehouseorderListTabId = "mainIframe_tabli_29";

var caculator = {
	intAdd:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseInt(val1)+parseInt(val2);
		} catch(e){
		}
		if(isNaN(retVal)){
			retVal = "";
		}
		return retVal;
	},
	intSub:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseInt(val1)-parseInt(val2);
		} catch(e){
		}
		if(isNaN(retVal)){
			retVal = "";
		}
		return retVal;
	},
	floatAdd:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseFloat(val1)+parseFloat(val2);
		} catch(e){
		}
		if(isNaN(retVal)){
			retVal = "";
		} else {
			retVal = parseFloat(retVal).toFixed(2);
		}
		return retVal;
	},
	floatSub:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseFloat(val1)/parseFloat(val2);
		} catch(e){
		}
		if(!retVal){
			retVal = "";
		}
		if(isNaN(retVal)){
			retVal = "";
		} else {
			retVal = parseFloat(retVal).toFixed(2);
		}
		return retVal;
	},
	intDiv:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseInt(val1)/parseInt(val2);
		} catch(e){
		}
		if(!retVal){
			retVal = "";
		}
		if(isNaN(retVal)){
			retVal = "";
		}
		return retVal;
	},
	floatDiv:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseFloat(val1)/parseFloat(val2);
		} catch(e){
		}
		if(!retVal){
			retVal = "";
		}
		if(isNaN(retVal)){
			retVal = "";
		} else {
			retVal = parseFloat(retVal).toFixed(2);
		}
		return retVal;
	},
	intMul:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseInt(val1)*parseInt(val2);
		} catch(e){
		}
		if(!retVal){
			retVal = "";
		}
		if(isNaN(retVal)){
			retVal = "";
		}
		return retVal;
	},
	floatMul:function(val1,val2){
		var retVal = "";
		try {
			retVal = parseFloat(val1)*parseFloat(val2);
		} catch(e){
		}
		if(!retVal){
			retVal = "";
		}
		if(isNaN(retVal)){
			retVal = "";
		} else {
			retVal = parseFloat(retVal).toFixed(2);
		}
		return retVal;
	}
}

/**
 * id必须为非空  且唯一
 * 
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
 * 关闭tab
 * 
 * @param tabId
 */
function closeTab(tabId){
	if(tabId){
		tabId = tabId.toString().replace("mainIframe_","");
	}
	try{
		window.parent.closeTab(tabId);
	} catch(e){
	}
}

/**
 * 关闭当前tab
 */
function closeCurrentTab() {
	var currentIfame = self.frameElement.getAttribute('id');
	closeTab(currentIfame);
}

/**
 * tab页面重新加载
 * 
 * @param tabId
 */
function reloadTab(tabId){
	window.parent.reloadIframe(tabId);
}

/**
 * 刷新指定页面 关闭当前页面
 */
function refreshAndCloseThis(tabId){
	reloadTab(tabId);
	closeCurrentTab();
}

/**
 * 刷新No.td
 */
function refreshIndex(){
	var tdObj = jQuery("td._indexTd");
	if(tdObj){
		for(var i=0;i<tdObj.length;i++){
			jQuery(tdObj[i]).html(i+1);
		}
	}
	var tdObj2 = jQuery("td._indexTd2");
	if(tdObj2){
		for(var i=0;i<tdObj2.length;i++){
			jQuery(tdObj2[i]).html(i+1);
		}
	}
}

/**
 * 弹出信息
 */
function alertMsg(msg){
	window.parent.alertMsg(msg);
}

/**
 * 消息确认框
 * 
 * @param msg
 * @param fn
 */
function confirmBox(msg,fn,fn2){
	window.parent.confirmBox(msg,fn,fn2);
}

/**
 * 文件下载
 * 
 * @param fileId
 */
function downloadFile(fileId){
	var downloadUrl = "/supplier/upload/download.htm?fileId=";
	window.location.href=downloadUrl+fileId;
	return false;
}

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
 * 页面跳转
 */
function jumpToPage(objUrl){
    window.location.href=objUrl;
}

/**
 * 图片预览
 * 
 * @param file
 * @param tagetImg
 */
function previewImage(file,tagetImg) {
	var img = new Image();
	var url = img.src = URL.createObjectURL(file);
    var imgPre = jQuery(img)
    imgPre.attr("width","180");
    imgPre.attr("height","180");
    img.onload = function() {
        URL.revokeObjectURL(url)
        tagetImg.html(imgPre)
    }
}

/**
 * 弹出div
 */
function showPopDiv(showType,htmlData,popOptions){
	var parentFrameID = self.frameElement.getAttribute('id');
	window.parent.showTableDiv(showType,htmlData,parentFrameID,popOptions);
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
	if(domain && !option['url'].startWith("http")){
		option['url'] = domain + option['url'];
	}
	var dataParams = {};
	var requestMethod = "post";
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
	jQuery.ajax({
		type: requestMethod,
		url: option['url'],
		data:dataParams,
		dataType:"text",
		beforeSend: function(XMLHttpRequest){
		},
		success:function(data){
			try {
				var endIndex = data.toString().lastIndexOf("<!--");
			    var len = data.toString().length;
			    var endTag = data.toString().substring(len-3,len);
			    if('-->'==endTag){
			    	data = data.toString().substring(0,endIndex);
			    }
			} catch(e){
			}
			succFun(data);
		}, 
	    complete: function(XMLHttpRequest, textStatus){
	    }, 
	    error: function(){ //请求出错处理 
	    } 
	});
}

/**
 * 获取json时间格式
 */
function getJsonDateStr(obj,format){
	var retVal = "";
	try {
		var date = new Date(parseInt(obj));
		retVal = date.format(format);
	} catch(e){
	}
	return retVal;
}

/** 
 * 时间对象的格式化 
 */  
Date.prototype.format = function(format) {
	/*
	 * format="yyyy-MM-dd hh:mm:ss";
	 */
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	}
	
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}  

/**
 * 初始化文件上传
 */
function initFileUploadField() {
	var fileUploadFields = jQuery("input._reUpload");
	for(var i=0;i<fileUploadFields.length;i++){
		var fileField = fileUploadFields[i];
		var outerSpan = jQuery('<span class="a-upload"></span>');
		jQuery(fileField).wrap(outerSpan);
		jQuery("<span>点击重新上传</span>").insertAfter(jQuery(fileField));
	}
}

jQuery(document).ready(function(){
	/**
	 * 设置默认日期
	 */
	function setDefaultDate(targetObj,defaultVal){
		var dateFieldList = jQuery("input._dateField");
		for(var i=0;i<dateFieldList.length;i++){
			if('nowDay'==jQuery(dateFieldList[i]).attr("defaultDate")) {
				jQuery(dateFieldList[i]).val((new Date()).format('yyyy-MM-dd'));
			}
		}
	}
	/**
	 * 设置时间控件
	 */
	/*if(jQuery("input._dateField").datepicker) {
		jQuery("input._dateField").datepicker({
			dateFormat:'yy-mm-dd',
			onClose: function( selectedDate ) {
				jQuery( "#quotation_list_start" ).datepicker();
		    }
		});
		jQuery("input._dateField").attr("readOnly","readOnly");
	}*/
	//jQuery("#ui-datepicker-div").css("z-index",19891199);
	setDefaultDate();
	jQuery("input._dateField").focus(function(){
		var fmt = jQuery(this).attr("datafmt");
		//var defaultFormat = 'yyyy-MM-dd HH:00:00';
		var defaultFormat = 'yyyy-MM-dd';
		if(!fmt){
			fmt = defaultFormat;
		}
		WdatePicker({skin:'blue',dateFmt:fmt});
	});
	
	initFileUploadField();
	 /**
	  * 注册文件的change事件
	  */
	jQuery("input._reUpload").change(function(){
		var msgSpan = jQuery(this).siblings("span");
		var fileName = jQuery(this).val();
		msgSpan.html(fileName);
	});
	
	var compressedTds = jQuery("td.line-compress");
	for(var i=0;i<compressedTds.length;i++) {
		var cTd = compressedTds[i];
		var titleTxt = jQuery(cTd).html();
		jQuery(cTd).html("<p class='line-compress-p' title='"+titleTxt+"'>"+titleTxt+"</p>");
	}
	
	String.prototype.startWith=function(str){    
	  var reg=new RegExp("^"+str);    
	  return reg.test(this);       
	} 

	String.prototype.endWith=function(str){    
	  var reg=new RegExp(str+"$");    
	  return reg.test(this);       
	}
	
});
