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