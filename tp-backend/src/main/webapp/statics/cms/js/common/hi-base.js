//var ctx = '/backend-front';
var ctx = '';
var PAjax = {
    version : '1.0'
};
PAjax.jsonp = {};

if (!window.console)
{
    var console = {};
    console.log = function(n)
    {
        return null;
    };
    console.trace = function(n)
    {
        return null;
    };
}
/**
 * 
 * @param url,请求url
 * @param param，请求参数
 * @param method，回调函数
 * @param reqType，请求方式，post或get
 * @param obj
 * @returns
 */
// 通信块
(function()
{
    /**
     * 默认回调函数
     */
    ajaxDefaultCallback = function(redata, cbparams)
    {
        console.log("hibase,默认回调函数：" + redata + "回调函数参数:" + cbparams);
        if (redata.code != 'GL_S000' && (redata.message != undefined && redata.message != "" && redata.message.indexOf("成功") < 0))
        {// 如果返回代码不为成功，则给出提示
            alert(redata.message);
        }
    }
    /**
     * 回调函数统一处理
     * create by:jackyshang
     * create date:2014-09-29
     *  参数说明：
     *      callback：自定义回调函数
     *      redata：异步返回值，格式：code，message，value
     *      cbparams：回调函数参数
     */
    ajaxCallbackRun = function(callback, redata, cbparams)
    {
       // console.log("hibase,ajax统一回调函数返回数据：" + JSON.stringify(redata));
        if (redata == undefined)
        {
            console.log("统一回调函数返回空数据");
        } else
        {
            // 如果回调函数不为空，则走默认回调函数，否则走自定义回调函数
            if ((!callback || typeof callback == 'undefined' || callback == undefined))
            {
                ajaxDefaultCallback(redata, cbparams);
            } else
            {
                callback(redata, cbparams);
            }
            // 如果返回代码不为成功，则给出提示
            if (redata.code != 'GL_S000' && (redata.message != null && redata.message != undefined && redata.message != "" && redata.message.indexOf("成功") < 0))
            {
                alert(redata.message);
            }
        }
    }
    /**
     * 异步跨域方式访问，jsonp方式 url 发送请求的地址,如：/homeaction/getuser.do data
     * 发送到服务器的数据，数组存储，如：{"date": new Date().getTime(), "state": 1} async 默认值:
     * true。默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false。
     * 注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。 successfn 成功回调函数 errorfn 失败回调函数
     */
    ajaxJsonp = function(url, params, callback, cbparams)
    {
        if( url != null && url!=undefined && (url.substr(0,1)=="/" || url.substr(0,1)=="\\" )){
            url = url.substr(1);
        }
        var url = (ctx == null ? url : (ctx + "/" + url));
        $.ajax({
            url : url,
            type : "get", // jsonp默认为get
            async : false, // 同步方法ajsop失效
            data :  {params:$.toJSON(params)},
            crossDomain : true,
            dataType : "jsonp",
            jsonp : "jsonpCallback",
            beforeSend:function(XMLHttpRequest){ 
                //createLoadDiv("loading",99,null,null);
           }, 
           success : function(redata,textStatus)
            {
                if (redata)
                {
                    ajaxCallbackRun(callback, redata, cbparams);
                }
                delDiv("loading");
            },
            error : function(XHR, textStatus, errorThrown)
            {
                delDiv("loading");
                return true;
            }
        });
    };
    /**
     * ajax封装 url 发送请求的地址,如：/homeaction/getuser.do data
     * 发送到服务器的数据，数组存储，如：{"date": new Date().getTime(), "state": 1} async 默认值:
     * true。默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false。
     * 注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。 type 请求方式("POST" 或 "GET")， 默认为 "GET"
     * dataType 预期服务器返回的数据类型，常用的如：xml、html、json、text successfn 成功回调函数 errorfn
     * 失败回调函数
     */
    ajaxJson = function(url, params, async, callback, cbparams)
    {
        if( url != null && url!=undefined && (url.substr(0,1)=="/" || url.substr(0,1)=="\\" )){
            url = url.substr(1);
        }
        var url = (ctx == null ? url : (ctx + "/" + url));
        var obj = "";
        async = ((typeof (async) == undefined) ? true : async);//设置是否同步，默认为异步
        params = (params == null || params == "" || typeof (params) == undefined) ? {
            "date" : new Date().getTime()
        } : params;
        
        $.ajax({
            url : url,
            contentType : 'text/plain;charset=utf-8',
            type : "get",
            async : async,
            data :  {params:$.toJSON(params)},
            dataType : "json",
            beforeSend:function(XMLHttpRequest){ 
                //createLoadDiv("loading",99,null,null);
           }, 
            success : function(redata)
            {
                ajaxCallbackRun(callback, redata, cbparams);
                delDiv("loading");
            },
            error : function(XHR, textStatus, errorThrown)
            {
                delDiv("loading");
                return true;
            }
        });
    };
    
    /**
     * 普通ajax封装
     * 参数：
     *      pdata对象格式{url:url,params:params,callback:callback}
     *默认值:
     *dataType:json,
     *reqType:post
     *async:false,
     *contentType：text/plain;charset=utf-8
     */
    
    ajax = function(pdata)
    {
       var url = pdata.url;
       var params = pdata.params;
       var async = pdata.async;
       var callback = pdata.callback;
       var  cbparams = pdata.cbparams;
       var  dataType=pdata.dataType;
       var reqType =pdata.reqType;
       var contentType = pdata.contentType;
        if( url != null && url!=undefined && (url.substr(0,1)=="/" || url.substr(0,1)=="\\" )){
            url = url.substr(1);
        }
        var url = (ctx == null ? url : (ctx + "/" + url));
        async = ((typeof (async) == undefined) ? true : async);//设置是否同步，默认为异步
        reqType =((typeof (reqType) == undefined) ? "post" : reqType);//设置请求类型，默认为post
        dataType =((typeof (dataType) == undefined) ? "json" : dataType);//数据类型，默认为json
        contentType =((typeof (contentType) == undefined) ? "text/plain;charset=utf-8" : contentType);//text/plain;charset=utf-8
        $.ajax({
            url : url,
            contentType :  contentType,
            type : reqType,
            async : async,
            data :  {params:$.toJSON(params)},
            dataType : dataType,
            beforeSend:function(XMLHttpRequest){ 
                //createLoadDiv("loading",99,null,null);
           }, 
            success : function(redata)
            {
                ajaxCallbackRun(callback, redata, cbparams);
                delDiv("loading");
            },
            error : function(XHR, textStatus, errorThrown)
            {
                delDiv("loading");
                return true;
            }
        });
    };

    /**
     * ajax封装POST url 发送请求的地址,如：/homeaction/getuser.do data
     * params 请求参数
     * toUrl  成功后跳转URL
     */
    ajaxPost = function(url, params,toUrl)
    {
    	var obj=$(this);
        if( url != null && url!=undefined && (url.substr(0,1)=="/" || url.substr(0,1)=="\\" )){
            url = url.substr(1);
        }
        var url = (ctx == null ? url : (ctx + "/" + url));
        $.post(url, {params:$.toJSON(params)}, function(redata){
           if(redata.code=='GL_S000'){//添加成功
   	          goPage.call(this,toUrl,{});
		   }else{
			   alert(redata.message);
		   }
           $(obj).data("req",true);
        }, "json");
    };
    
    aJsonp = function(controller,action,params,callback,cbparams){
        $.ajax({
            url: controller + '/' + action,
            data:{params:$.toJSON(params)},
            crossDomain: true,
            dataType: "jsonp",
            jsonp: "jsonpCallback",
            success: function(redata) {
                callbackRun(callback,redata,cbparams);
            }
        });
    }; 

    /**
     * 异步获取json数据 create by:jackyshang create date :2014-09-25
     */
    $.getAjaxData = function(url)
    {
        if( url != null && url!=undefined && (url.indexOf(ctx)< 0 )){
            if( url.substr(0,1)=="/" || url.substr(0,1)=="\\" ){
                url = url.substr(1);
            }
            url = (ctx == null ? url : (ctx + "/" + url));
        }
        var jsonList = {};
        $.ajax({
            type : "POST",
            url : url,
            async : false,
            data : arguments[1] || {},
            beforeSend:function(XMLHttpRequest){ 
                //createLoadDiv("loading",99,null,null);
           }, 
            success : function(redata, textStatus, jqXHR)
            {
                var result ={};
                if ('timeout' == jqXHR.getResponseHeader('sessionstatus') && '0' == jqXHR.getResponseHeader('Content-Length'))
                {
                    return;
                }
                // 如果返回代码
                if (redata.code == undefined || redata.code == 'GL_S000')
                {
                    if (redata.value == undefined   )
                    {// 如果有结果值封装i
                        result =redata
                    } else
                    {
                        result =redata.value;
                    }
                } 
                jsonList = eval("(" + result + ")");
                delDiv("loading");
            },
            error : function(XHR, textStatus, errorThrown)
            {
                delDiv("loading");
                return true;
            }
        });
        return jsonList;
    };
    /**
     * 页面跳转 path：目标url data：参数 isnew：是否开启一个新窗口
     */
    window.goPage = function(url, data, isnew)
    {
        if( url != null && url!=undefined && (url.substr(0,1)=="/" || url.substr(0,1)=="\\" )){
            url = url.substr(1);
        }
        var url = (ctx == null ? url : (ctx + "/" + url));
        var form = $("<form>");
        if (isnew)
        {
            form.attr("target", "_blank")
        };
        if (data != undefined)
        {
            for ( var sub in data)
            {
                $(form).append($("<input>").attr("type", "hidden").attr("name", sub).val(data[sub]));
            }
        }
        $(form).attr("action", url).attr("method", "post").appendTo($("body")).submit();
    }

    window.goPageGet = function(url, data, isnew)
    {
        if( url != null && url!=undefined && (url.substr(0,1)=="/" || url.substr(0,1)=="\\" )){
            url = url.substr(1);
        }
        var url = (ctx == null ? url : (ctx + "/" + url));
        var form = $("<form>");
        if (isnew)
        {
            form.attr("target", "_blank")
        };
        if (data != undefined)
        {
            for ( var sub in data)
            {
                $(form).append($("<input>").attr("type", "hidden").attr("name", sub).val(data[sub]));
            }
        }
        $(form).attr("action", url).attr("method", "get").appendTo($("body")).submit();
    }
    
    
    window.goPageForOld = function(url, data, isnew)
    {
        var form = $("<form>");
        if (isnew)
        {
            form.attr("target", "_blank")
        };
        if (data != undefined)
        {
            for ( var sub in data)
            {
                $(form).append($("<input>").attr("type", "hidden").attr("name", sub).val(data[sub]));
            }
        }
        $(form).attr("action", url).attr("method", "post").appendTo($("body")).submit();
    }
    /**
     * 弹出layer窗口 id:弹出的目标 //title:标题 //callback:回调 //whc:宽高设置 //showCB:是否显示关闭按钮
     */
    window.opennw = window.open;
    window.open = function(id, title, callback, whc, showCB)
    {
        var windex = $.layer({
            type : 1,
            title : title == undefined ? false : title,
            fix : false,
            moveOut : true,
            closeBtn : (showCB != undefined && showCB) ? [ 0, true ] : false,
            /*offset : [ '80%', '50%' ],*/
            move : [ '.xubox_title', true ],
            border : [ 5, 0.5, '#666', true ],
            area : whc == undefined ? [ '600px', 'auto' ] : whc,
            page : {
                dom : '#' + id
            },
            success : function(div)
            {
                if (callback != undefined)
                {
                    callback.call(this, div);
                }
            },
            close : function(index)
            {
                layer.close(index);
            }
        });
        $("#" + id).data("pop", windex);
        return windex;
    };
}).call(this);

/**
 * alert方法重写
 *  icon类型：
 *      1：对号，系统成功提示
 *      4：问号，系统确认框
 *      12：感叹号，默认系统出错等
 */
window.xalert = window.alert;
window.alert = function(msg, icon, callback, wh)
{
    wh = (wh == undefined ? [ "360px", "auto" ] : wh);
    icon = (icon == undefined ? 12 : icon);
    layer.xalert(msg, icon, wh, callback);
}
/**
 * 前后台调用
 */
$(function()
{
    if ($('html').hasClass('ieOld'))
    {
        $('a,button').attr('hidefocus', true);
    }
});

$(window).load(function()
{
    // 兼容placeholder
    if ($.fn.placeholder)
    {
        $('input[placeholder],textarea[placeholder]').placeholder();
    }
});
/**
 * eselect初始化方法
 * create by:jackyshang
 * create date:2014-09-25
 */
$.fn.selectInit = function(options)
{
    var that = this;
    $(this).empty();
    if (options.defaults)
    {
        $(this).append("<option value='" + options.defaults.key + "'>" + options.defaults.value + "</option>");
    }
    var key = "code", value = "value";
    if (options.mapping)
    {
        key = options.mapping.key;
        value = options.mapping.value;
    }
    $(options.data).each(function(num, item)
    {
        var $d1 = $("<option value='" + item[key] + "'>" + item[value] + "</option>");
        $d1.data("optionData", item);
        $(that).append($d1);
    });
    $(this).bind("change", options.onChange);
    return this;
};

/** 
 * 序列化表单为json对象 
 */  
$.fn.serializeObject = function()  
 {  
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
 * 合并两个json对象属性为一个对象 
 * @param jsonbject1 
 * @param jsonbject2 
 * @returns resultJsonObject 
 */  
$.mergeJsonObject = function(jsonbject1, jsonbject2)  
{  
    var resultJsonObject={};  
    for(var attr in jsonbject1){  
        resultJsonObject[attr]=jsonbject1[attr];  
    }  
    for(var attr in jsonbject2){  
        resultJsonObject[attr]=jsonbject2[attr];  
    }  
    
    return resultJsonObject;  
};  
        
/**
*bodyId:最大容器的ID；
*objId：加载进度条DIV的ID；
*index：加载进度条的DIV的z-index属性
*width:加载进度条的DIV的宽，可缺省
*height:加载进度条的DIV的高，可缺省
*/
/*function createLoadDiv(objId,index,width,height){
    var divWidth = 32;
    var divHeight = 32;
    if(!isNaN(width)){
        divWidth = width;
    }
    if(!isNaN(height)){
        divWidth = height;
    }
    if(objId!=""&&objId!=null)
    {
        var bodyObj = document.body;
        var divObj = document.createElement("div");
        divObj.innerHTML = "<img  src='"+resPath+"/images/hibit/loading_medium.gif' width='32' height='32' align='middle' />";
        divObj.id = objId;
        divObj.style.zIndex = index;
        divObj.style.width = divWidth;
        divObj.style.height = divHeight;
        divObj.style.top= (document.documentElement.scrollTop+(document.documentElement.clientHeight-divHeight)/2)+"px"; 
        divObj.style.left= (document.documentElement.scrollLeft+(document.documentElement.clientWidth-divWidth)/2)+"px"; 
        divObj.style.position = "absolute";
        bodyObj.appendChild(divObj);
        return divObj;
    }else
    {
        return null;
    }
}*/

/**
*objId：加载进度条DIV的ID；
*/
function delDiv(objId){
    if(objId!=null&&objId!=undefined)
    {
        $("#"+objId+"").remove();
    }
}

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
function syncPost(url, data) {
	return sync(url, data, "POST");
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
