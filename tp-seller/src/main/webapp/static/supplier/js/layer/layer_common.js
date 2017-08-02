/**
 * 获取json时间格式
 */
function getJsonDateStr(obj,format){
	var date = new Date(parseInt(obj.time));
	return date.format(format);
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

jQuery(document).ready(function(){
	/**
	 * 设置默认日期
	 */
	function setDefaultDate(){
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
		setDefaultDate();
	}*/
	
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
});
