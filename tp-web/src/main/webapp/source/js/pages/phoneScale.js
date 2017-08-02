/*
* @Author: dongqian
* @Date:   2016-12-03 10:51:55
* @Last Modified by:   dongqian
* @Last Modified time: 2016-12-05 09:51:35
*/

//页面适配

if(/Android (\d+\.\d+)/.test(navigator.userAgent)){
    var version = parseFloat(RegExp.$1);
    if(version>2.3){
        var phoneScale = parseInt(window.screen.width)/375;
        $("head").append('<meta name="viewport" content=" width=375, minimum-scale = '+ phoneScale +', maximum-scale = '+ phoneScale +', target-densitydpi=device-dpi">');
    }else{
    	$("head").append('<meta name="viewport" content=" width=375, target-densitydpi=device-dpi">');
    }
}else{
	$("head").append('<meta name="viewport" content=" width=375, user-scalable=no, target-densitydpi=device-dpi">');
}
