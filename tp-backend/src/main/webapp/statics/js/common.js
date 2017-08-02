//警告框
$._messengerDefaults;

function isNull(value){
	if(typeof(value) == "undefined" || null == value || "" == value ) return true;
	else return false;
}

function p(position){
	if( "left-top" == position)$._messengerDefaults = {extraClasses: 'messenger-fixed messenger-theme-future messenger-on-top messenger-on-left'} ;
	else if( "top"== position)$._messengerDefaults = {extraClasses: 'messenger-fixed messenger-theme-future messenger-on-top'} ;
	else if( "right-top"== position)$._messengerDefaults = {extraClasses: 'messenger-fixed messenger-theme-future messenger-on-top messenger-on-right'} ;
	else if( "left-bottom"== position)$._messengerDefaults = {extraClasses: 'messenger-fixed messenger-theme-future messenger-on-bottom messenger-on-left'} ;
	else if( "bottom"== position)$._messengerDefaults = {extraClasses: 'messenger-fixed messenger-theme-future messenger-on-bottom'} ;
	else if( "right-bottom"== position)$._messengerDefaults = {extraClasses: 'messenger-fixed messenger-theme-future messenger-on-bottom messenger-on-right'} ;
	else $._messengerDefaults = {extraClasses: 'messenger-fixed messenger-theme-future messenger-on-bottom messenger-on-right'} ;
}

function error(message,hideTime,showCloseButton){
	if(isNull(hideTime) || hideTime < 0) hideTime = 0;
	if(typeof(showCloseButton) == "undefined") showCloseButton = true;
	Messenger().post({
        message: message,
        hideAfter: hideTime,
        showCloseButton: showCloseButton,
        type:"error"
    });
}

function info(message,hideTime,showCloseButton){
	if(isNull(hideTime) || hideTime < 0) hideTime = 0;
	if(typeof(showCloseButton) == "undefined") showCloseButton = true;
	Messenger().post({
        message: message,
        hideAfter: hideTime,
        showCloseButton: showCloseButton,
        type:"info"
    });
}

function success(message,hideTime,showCloseButton){
	if(isNull(hideTime) || hideTime < 0) hideTime = 0;
	if(typeof(showCloseButton) == "undefined") showCloseButton = true;
	Messenger().post({
        message: message,
        hideAfter: hideTime,
        showCloseButton: showCloseButton,
        type:"success"
    });
}

$(function() {
	p("right-bottom");
});

String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}

Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}
