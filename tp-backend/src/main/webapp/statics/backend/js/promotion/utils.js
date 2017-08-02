/**
 * ajax异步调用
 * 
 * @param url
 * @param data
 * @param callback
 *            回调函数
 * @param type
 */
function asyncXhr(url, data, callback, type) {
	var _data, options = {
		url : url,
		data : data,
		type : type,
		async : true,
		success : callback
	};
	$.ajax(options);
}

/**
 * ajax异步get调用
 */
function asyncXhrGet(url, data, callback) {
	if (arguments.length == 2) {
		callback = data;
		data=null;
	}
	asyncXhr(url, data, callback, "GET");
}

/**
 * ajax异步post调用
 */
function asyncXhrPost(url, data, callback) {
	if (arguments.length == 2) {
		callback = data;
		data=null;
	}
	asyncXhr(url, data, callback, "POST");
}

/**
 * ajax同步调用
 */
function sync(url, data, type) {
	var _data, options = {
		url : url,
		data : data,
		type : type,
		async : false,
		success : function(rs) {
			_data = rs;
		}
	};
	$.ajax(options);
	return _data;
}

/**
 * ajax同步get调用
 */
function syncGet(url, data) {
	return sync(url, data, "GET");
}

/**
 * ajax同步post调用
 */
function syncPost(url, data) {
	return sync(url, data, "POST");
}

/**
 * 比较日期大小
 */
function compareDate(data1, data2) {
	return Date.parse(data1.replace(/-/g, "/")) > Date.parse(data2.replace(/-/g, "/"));
}

/**
 * 是否空字符串
 */
function isBlank(str) {
	return !(str && $.trim(str).length > 0);
}

function changeTwoDecimal(x){
	changeDecimal(x, 2);
}

/**
 * 转换为固定位数的小数
 * @param x
 * @returns
 */
function changeDecimal(x,num) {
	var f_x = parseFloat(x);
	if (isNaN(f_x)) {
		alert('function:changeTwoDecimal->parameter error');
		return false;
	}
	f_x = Math.round(f_x * 100) / 100;
	var s_x = f_x.toString();
	var pos_decimal = s_x.indexOf('.');
	if (pos_decimal < 0) {
		pos_decimal = s_x.length;
		s_x += '.';
	}
	while (s_x.length <= pos_decimal + num) {
		s_x += '0';
	}
	return s_x;
}

String.prototype.replaceAll = function(s1,s2) { 
    return this.replace(new RegExp(s1,"gm"),s2); 
}

//文本框只能输入数字，并屏蔽输入法和粘贴  
$.fn.integer = function() {     
   $(this).css("ime-mode", "disabled");     
   this.bind("keypress",function(e) {     
	   var code = (e.keyCode ? e.keyCode : e.which);  //兼容火狐 IE      
	   if(!$.browser.msie&&(e.keyCode==0x8))  //火狐下不能使用退格键     
	   {     
	    return ;     
	   }     
	   return code >= 48 && code<= 57;     
   });     
   this.bind("blur", function() {     
       if (this.value.lastIndexOf(".") == (this.value.length - 1)) {     
           this.value = this.value.substr(0, this.value.length - 1);     
       } else if (isNaN(this.value)) {     
           this.value = "";     
       }     
   });     
   this.bind("paste", function() {     
       var s = clipboardData.getData('text');     
       if (!/\D/.test(s));     
       value = s.replace(/^0*/, '');     
       return false;     
   });     
   this.bind("dragenter", function() {     
       return false;     
   });     
//   this.bind("keyup", function() {     
//   if (/(^0+)/.test(this.value)) {     
//       this.value = this.value.replace(/^0*/, '');     
//       }     
//   });     
 }; 
 
 $.fn.numeral = function() {     
   $(this).css("ime-mode", "disabled");     
   this.bind("keypress",function(e) {     
	   var code = (e.keyCode ? e.keyCode : e.which);  //兼容火狐 IE      
	   if(!$.browser.msie&&(e.keyCode==0x8))  //火狐下不能使用退格键     
	   {     
	    return ;     
	   }     
	   return (code >= 48 && code<= 57) || code == 45 || code == 46;     
   });     
   this.bind("blur", function() {     
       if (this.value.lastIndexOf("-") > 0) {     
    	   this.value = ""; 
       } else if (isNaN(this.value)) {     
           this.value = "";     
       }     
   });     
   this.bind("paste", function() {     
       var s = clipboardData.getData('text');     
       if (!/\D/.test(s));     
       value = s.replace(/^0*/, '');     
       return false;     
   });     
   this.bind("dragenter", function() {     
       return false;     
   });     
//   this.bind("keyup", function() {     
//   if (/(^0+)/.test(this.value)) {     
//       this.value = this.value.replace(/^0*/, '');     
//       }     
//   });     
 }; 

 $.ajaxSetup({
  	    url: "/user/toLogin" , // 默认URL
     type: "POST" , // 默认使用POST方式
 	error:function(XMLHttpRequest, textStatus, errorThrown){
 		var errorCode = XMLHttpRequest.getResponseHeader("errorCode");
 		var returnUrl = XMLHttpRequest.getResponseHeader("returnUrl");
 		if(!isNull(errorCode) && "-999" == errorCode) window.location.href=returnUrl;
 	}
 });
