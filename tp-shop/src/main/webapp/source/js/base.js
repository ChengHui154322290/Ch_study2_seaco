/**
 * base
 */
$(function() {
	gotop();

	//给购物车 添加事件
	$("footer .footer_shopcar")[xigou.events.click](function() {
		gotoCart();
	});
	$("footer .footer_shopcar_active")[xigou.events.click](function() {
		gotoCart();
	});

	//点击客服
	$('.customer_service').attr({
		href: xigou.customerService,
		target: '_blank'
	});

	if ((!xigou) || (!xigou.getLoginUserInfo)) {
		alert("请不要在无痕浏览模式下访问本站");
	}

	//点击footer 导航清缓存数据
	$("footer")[xigou.events.click](function() {
		xigou.setSessionStorage("speciallist","");
		xigou.setSessionStorage("speciallist_ht","");
		xigou.setSessionStorage("productlist","");
		xigou.setSessionStorage("spikelist","");
	})

	//给洋淘加提示红圆点
	var ht_tab = $('a[class*=footer_ht]');
	var _timestamp = xigou.getLocalStorage("timestamp"),
		_htclick = xigou.getLocalStorage("htclick") || 0;
	var _nowTime = new Date().getTime();
	var moreTime = _nowTime - _timestamp;
	var staticTime = 24*60*60*1000;	//24小时时间值
	if(!_timestamp || moreTime > staticTime || _htclick == 0) {
		ht_tab.append('<span class="ht-round"></span>');
		var _date = new Date(),
			_year = _date.getFullYear(),
			_month = _date.getMonth() + 1,
			_day = _date.getDate();
		var _time = new Date(_year+"-"+_month+"-"+_day).getTime();
		xigou.setLocalStorage("timestamp",_time);
		xigou.setLocalStorage("htclick",0);
	}/*else if(_htclick == 1) {
		ht_tab.find('span').remove();
	}*/

});

//判断参数如果是null或者空字符串或者非数字，返回0
function isNaNDefaultInt(data){
	if(data == null || data == '' || isNaN(data))
		return 0;
	return data;
}

//检查是否登录，如果未登录跳转到登录页面
function checkGotoLogin(backurl) {
	backurl=backurl||"";
	if(backurl){
		backurl="?backurl="+backurl;
	}
	if (xigou.getSessionStorage("userinfo") == "") {
		window.location.href = "logon.html"+backurl;
		return false;
	}
	return true;
}

//判断客户端来源是否是微信浏览器
function isWeiXin() {
	var ua = window.navigator.userAgent.toLowerCase(); 
	if(ua.match(/MicroMessenger/i) == 'micromessenger')
	{ 
		return true; 
	}else{ 
		return false; 
	}
}

//加到顶部
var goto_top_type = -1;
var goto_top_itv = 0;
function goto_top_timer() {
    var y = goto_top_type == 1 ? document.documentElement.scrollTop: document.body.scrollTop;
    var moveby = 15;
    y -= Math.ceil(y * moveby / 100);
    if (y < 0) {
        y = 0;
    }
    if (goto_top_type == 1) {
        document.documentElement.scrollTop = y;
    } else {
        document.body.scrollTop = y;
    }
    if (y == 0) {
        clearInterval(goto_top_itv);
        goto_top_itv = 0;
    }
}
function goto_top() {
    if (goto_top_itv == 0) {
        if (document.documentElement && document.documentElement.scrollTop) {
            goto_top_type = 1;
        } else if (document.body && document.body.scrollTop) {
            goto_top_type = 2;
        } else {
            goto_top_type = 0;
        }
        if (goto_top_type > 0) {
            goto_top_itv = setInterval('goto_top_timer()', 10);
        }
    }
}
function gotop() {
	var g = g || {
		options: {
			el: $('#gotop')
		},
		init: function() {
			/*if(this.options.el.size()<1){
				$("body").append('<a href="javascript:window.scrollTo(0, 0);" id="gotop" title="回顶部"></a>');
				this.options.el=$('#gotop');
			}*/
			if(this.options.el.size()>-1){
				this.options.el.remove();
			}
			$("body").append('<a href="javascript:void(0)" onclick="goto_top()" id="gotop" title="回顶部"></a>');
			this.options.el=$('#gotop');
			g.options.el.addClass('ui_gotop hide');
		},
		check: function(position) {
			(position !== undefined ? position : window.pageYOffset) > 200 ? g.show() : g.hide();
		},
		show: function() {
			g.options.el.removeClass('hide');
			
		},
		hide: function() {
			g.options.el.addClass('hide');
		},
		click: function() {
			g.options.el[xigou.events.click](function() {
				g.hide();
				window.scrollTo(0, 0);
				return false;
			});
		}
	};

	g.init();
	//g.click();

	$(window).scroll(function() {
		g.check(window.pageYOffset);
	});

};


function formatDate(date, format) {
	var cfg = {
		yyyy: date.getFullYear(),
		yy: date.getFullYear().toString().substring(2),
		MM: paddNum(date.getMonth() + 1),
		dd: paddNum(date.getDate()),
		d: date.getDate(),
		hh: paddNum(date.getHours()),
		mm: paddNum(date.getMinutes()),
		ss: paddNum(date.getSeconds())
	}
	format || (format = "yyyy-MM-dd hh:mm:ss");
	return format.replace(/([a-z])(\1)*/ig, function(m) {
		return cfg[m];
	});
};

function paddNum(num) {
	num += "";
	return num.replace(/^(\d)$/, "0$1");
};

//毫秒值转换
function formatDateTime(dateStr, format) {
	if (!/^\d+$/g.test(dateStr)) {
		return;
	}
	var newTime = new Date(parseInt(dateStr));
	return formatDate(newTime, format);
};

//倒计时分秒
function countDown(startTime, endTime, t) {

	/*var _time = endTime - startTime,
		_countTime,h,m,s;
	_countTime = parseInt( _time / 1000);//精确到秒
	h = Math.floor(_countTime / 60 / 60);
    m = Math.floor((_countTime - h * 60 * 60) / 60);
    s = Math.floor((_countTime - h * 60 * 60 - m * 60));
    document.getElementById("DD").innerHTML = parseInt(h/24);
    document.getElementById("HH").innerHTML = h;
    document.getElementById("MM").innerHTML = m;
    document.getElementById("SS").innerHTML = s;
    _countTime--;
    if (_countTime < 0)
    {
        document.getElementById("DD").innerHTML = "0";
        document.getElementById("HH").innerHTML = "00";
        document.getElementById("MM").innerHTML = "00";
        document.getElementById("SS").innerHTML = "00";;
    }*/
}
/**
 *时间器
 *startTime 开始时间
 *endTime 结束时间
 */
function restTime(startTime, endTime, t,countDown) {
	var result = "",
		l_now = Date.parse(new Date()), //当时时间
		l_p_time = endTime - startTime, //入参结束时间减去入参开始时间
		l_s_time = startTime - l_now, //入参开始时间减去当时时间
		l_e_time = endTime - l_now; //入参结束时间减去当前时间

	if (l_p_time <= 0) {
		result = '已过期';
		if (window[t]) {
			window.clearInterval(window[t]);
		}
	}
	if (l_s_time > 0) {
		result = '未开始';
		if (window[t]) {
			window.clearInterval(window[t]);
		}
	}
	if (l_e_time < 0) {
		result = '已过期';
		if (window[t]) {
			window.clearInterval(window[t]);
		}
	} else {

		var second = 1000;
		var minute = 1000 * 60;
		var hour = minute * 60;
		var day = hour * 24;
		var l_diffValue;

		var wording; //= countDown == "tmnotice" ? "距开团":"剩余";
		if (l_s_time > 0) {
			wording = "距开团";
			l_diffValue = l_s_time;
		}else{
			wording = "剩余";
			l_diffValue = l_e_time;
		}

		var l_day = parseInt(l_diffValue / day);
		l_diffValue = l_diffValue - l_day * day;
		var l_hour = parseInt(l_diffValue / hour);
		l_diffValue = l_diffValue - l_hour * hour;
		var l_minute = parseInt(l_diffValue / minute);
		l_diffValue = l_diffValue - l_minute * minute;
		var l_second = parseInt(l_diffValue / second);
		l_day = l_day == 0 ? "" : l_day;
		l_hour = l_hour == 0 ? "" : l_hour;
		l_minute = l_minute == 0 ? "" : paddNum(l_minute);
		l_second =  paddNum(l_second);
		
		
		
		if(l_day!=0){
			result = wording + l_day + "天";
		}
		if(l_day==0&&l_hour != 0){
			result = wording + l_hour + "时";
		}
		if(l_day==0&&l_hour == 0){
			result = wording + l_minute + "分";
		}
		if(l_day==0&&l_hour == 0&&l_minute == 0){
			result = wording+'1分';  
		}

		if(countDown && countDown !="tmnotice") {
			
			l_day = l_day == ""? "00":paddNum(l_day);
			l_hour = l_hour == ""? "00":paddNum(l_hour);			
			l_minute = l_minute == ""? "00":l_minute;
			l_second = l_second == ""? "00":l_second;
			result = "<span class='first'>"+wording+"</span><span class='time'>"+l_day+"</span>天"+
					"<span class='time'>"+l_hour+"</span>时"+
					"<span class='time'>"+l_minute+"</span>分"+
					"<span class='time'>"+l_second+"</span>秒";		
		}
	}
	return result;
};

//倒计时
var timeRestIndex = 0;

function getTimeRest(startTime, endTime, el,countDown) {
	var s = startTime,
		e = endTime;

	var t = "Times" + timeRestIndex++;
	el.html(restTime(s, e, t,countDown));
	if(countDown) {
		window[t] = window.setInterval(function() {
			el.html(restTime(s, e, t,countDown));
		}, 1000);
	}
	
};

function getCountDown(startTime, endTime, el) {
	var s = startTime,
		e = endTime;

	var t = "Times" + timeRestIndex++;
	el.html(countDown(s, e, t));
}

//购物车跳转
function gotoCart() {
	if (checkGotoLogin() == true) {
		var params = {
			'token': xigou.getToken()
		};

		xigou.activeGotoCart.getshopcount({
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					var json = response || null;
					if (null == json || json.length == 0) return false;
					if (json.rescode.code == 0) {
								window.location.href = "cart.html";
					}
				}
			}
		});
	}
}

/**
 *  @file 基于Zepto的图片延迟加载插件
 *  @name Imglazyload
 *  @desc 图片延迟加载
 *  @import zepto.js, extend/event.scrollStop.js, extend/event.ortchange.js
 */
(function($) {
	/**
	 * @name imglazyload
	 * @grammar  imglazyload(opts) => self
	 * @desc 图片延迟加载
	 * **Options**
	 * - ''placeHolder''     {String}:              (可选, 默认值:\'\')图片显示前的占位符
	 * - ''container''       {Array|Selector}:      (可选, 默认值:window)图片延迟加载容器，若innerScroll为true，则传外层wrapper容器即可
	 * - ''threshold''       {Array|Selector}:      (可选, 默认值:0)阀值，为正值则提前加载
	 * - ''urlName''         {String}:              (可选, 默认值:data-url)图片url名称
	 * - ''eventName''       {String}:              (可选, 默认值:scrollStop)绑定事件方式
	 * - --''refresh''--     {Boolean}              --(可选, 默认值:false)是否是更新操作，若是页面追加图片，可以将该参数设为true--（该参数已经删除，无需使用该参数，可以同样为追加的图片增加延迟加载）
	 * - ''innerScroll''     {Boolean}              (可选, 默认值:false)是否是内滚，若内滚，则不绑定eventName事件，用户需在外部绑定相应的事件，可调$.fn.imglazyload.detect去检测图片是否出现在container中
	 * - ''isVertical''      {Boolean}              (可选, 默认值:true)是否竖滚
	 *
	 * **events**
	 * - ''startload'' 开始加载图片
	 * - ''loadcomplete'' 加载完成
	 * - ''error'' 加载失败
	 *
	 * 使用img标签作为初始标签时，placeHolder无效，可考虑在img上添加class来完成placeHolder效果，加载完成后移除。使用其他元素作为初始标签时，placeHolder将添加到标签内部，并在图片加载完成后替换。
	 * 原始标签中以\"data-\"开头的属性会自动添加到加载后的图片中，故有自定义属性需要放在图片中的可以考虑以data-开头
	 * @example $('.lazy-load').imglazyload();
	 * $('.lazy-load').imglazyload().on('error', function (e) {
	 *     e.preventDefault();      //该图片不再加载
	 * });
	 */
	var pedding = [];
	$.fn.imglazyload = function(opts) {

		//2015-3-14,暂时不用。 动态渲染html时第一屏图片如果不滚动页面永远出不来
		$(this).each(function() {
			var _this = $(this);
			if (_this.is('img')) {
				_this.attr("src", _this.attr("data-url"));
			}
		});
		return;
		//2015-3-14,暂时不用。 动态渲染html时第一屏图片如果不滚动页面永远出不来

		var splice = Array.prototype.splice,
			opts = $.extend({
				threshold: 0,
				container: window,
				urlName: 'data-url',
				placeHolder: '',
				eventName: 'scrollStop',
				innerScroll: false,
				isVertical: true
			}, opts),
			$viewPort = $(opts.container),
			isVertical = opts.isVertical,
			isWindow = $.isWindow($viewPort.get(0)),
			OFFSET = {
				win: [isVertical ? 'scrollY' : 'scrollX', isVertical ? 'innerHeight' : 'innerWidth'],
				img: [isVertical ? 'top' : 'left', isVertical ? 'height' : 'width']
			},
			$plsHolder = $(opts.placeHolder).length ? $(opts.placeHolder) : null,
			isImg = $(this).is('img');

		!isWindow && (OFFSET['win'] = OFFSET['img']); //若container不是window，则OFFSET中取值同img

		function isInViewport(offset) { //图片出现在可视区的条件
			var viewOffset = isWindow ? window : $viewPort.offset(),
				viewTop = viewOffset[OFFSET.win[0]],
				viewHeight = viewOffset[OFFSET.win[1]];
			return viewTop >= offset[OFFSET.img[0]] - opts.threshold - viewHeight && viewTop <= offset[OFFSET.img[0]] + offset[OFFSET.img[1]];
		}

		pedding = Array.prototype.slice.call($(pedding.reverse()).add(this), 0).reverse(); //更新pedding值，用于在页面追加图片
		if ($.isFunction($.fn.imglazyload.detect)) { //若是增加图片，则处理placeHolder
			_addPlsHolder();
			return this;
		};

		function _load(div) { //加载图片，并派生事件
			var $div = $(div),
				attrObj = {},
				$img = $div;

			if (!isImg) {
				$.each($div.get(0).attributes, function() { //若不是img作为容器，则将属性名中含有data-的均增加到图片上
					~this.name.indexOf('data-') && (attrObj[this.name] = this.value);
				});
				$img = $('<img />').attr(attrObj);
			}
			$div.trigger('startload');
			$img.on('load', function() {
				!isImg && $div.replaceWith($img); //若不是img，则将原来的容器替换，若是img，则直接将src替换
				$div.trigger('loadcomplete');
				$img.off('load');
			}).on('error', function() { //图片加载失败处理
				var errorEvent = $.Event('error'); //派生错误处理的事件

				//$div.trigger(errorEvent);//2015.3.8 此代码引起回调函数无限循环调用

				errorEvent.defaultPrevented || pedding.push(div);
				$img.off('error').remove();
			}).attr('src', $div.attr(opts.urlName));
		}

		function _detect() { //检测图片是否出现在可视区，并对满足条件的开始加载
			var i, $image, offset, div;
			for (i = pedding.length; i--;) {
				$image = $(div = pedding[i]);
				offset = $image.offset();
				isInViewport(offset) && (splice.call(pedding, i, 1), _load(div));
			}
		}

		function _addPlsHolder() {
			!isImg && $plsHolder && $(pedding).append($plsHolder); //若是不是img，则直接append
		}

		$(document).ready(function() { //页面加载时条件检测
			_addPlsHolder(); //初化时将placeHolder存入
			_detect();
		});

		!opts.innerScroll && $(window).on(opts.eventName + ' ortchange', function() { //不是内滚时，在window上绑定事件
			_detect();
		});

		$.fn.imglazyload.detect = _detect; //暴露检测方法，供外部调用

		return this;
	};

})(Zepto);


/**
 * 购物车跳转接口相关
 */
;
(function($) {
	var me = this;
	me.activeGotoCart = me.activeGotoCart || {};
	//购物车-美图和海囤数量
	me.activeGotoCart.getshopcount = function(params) {
		if (me.locationData) {
			$.isFunction(params.callback) && params.callback(static_data.cart.supcount, me.dictionary.success);
		} else {
			var path = me.activeHost + me.activeUrl.cart.supcount;
			ajaxPost(params, path);
		}
	};
}).call(xigou, Zepto);



var ajaxGet = function(params, path) {
	xigou.ajaxGet(params, path);
}

var ajaxPost = function(params, path) {
	xigou.ajaxPost(params, path);
}



;
(function($) {
	this.ajaxGet = function(params, path) {
		//默认请求参数
		var dp = {
			url: path,
			callback: function(response, status) { //回调函数
				if (status != me.dictionary.success) {
					me.alert('请求失败，详见' + response);
				}
			}
		};
		$.extend(dp, params);
		this.get(dp);
	};

	this.ajaxPost = function(params, path) {
		//默认请求参数
		var dp = {
			url: path,
			requestBody: '',
			callback: function(response, status) { //回调函数
				if (status != me.dictionary.success) {
					me.alert('请求失败，详见' + response);
				}
			}
		};
		$.extend(dp, params);
		this.post(dp);
	};

}).call(xigou, Zepto);



var ky_doPost = function(params, path) {
	$.ajax({
		type: 'get',
		//dataType: "json",
		// contentType: 'application/json',
		// accepts: "application/json",
		url: "http://qcnuo8.duapp.com/send_doPost?callback=?&url=" + path + "&json=" + JSON.stringify(params.requestBody),

		timeout: 1000 * 60, //60秒超时
		beforeSend: function(XHR) {

		},
		success: function(data) {

			params.callback(data, "success");

		}
	});

};

var ky_doGet = function(params, path) {
	$.ajax({
		type: 'get',
		url: "http://qcnuo8.duapp.com/send_doGet?callback=?&url=" + path + "&json=" + params.p,

		timeout: 1000 * 60, //60秒超时
		beforeSend: function(XHR) {

		},
		success: function(data) {

			params.callback(data, "success");

		}
	});
};

/**
 * 1.获取付款表单数据
 * 2.根据编号获取中文付款方式名称
 */
;
(function($) {
	var me = this;

	me.getPaymentForm=function(params){

		//////////“0”支付宝
		 //“1”微信支付
		 //“2”银联支付
		 //“3”招行支付
		 //“4”快钱支付

		// 已修改  0微信
		// 已修改  1 支付宝
		var forms=[];

		if(params.payway=="1" || params.payway=="alipayDirect"){//支付宝
		    forms.push('<input type="hidden" name="service" value="'+params.datas.service+'"/>');
		    forms.push('<input type="hidden" name="partner" value="'+params.datas.partner+'"/>');
		    forms.push('<input type="hidden" name="seller_id" value="'+params.datas.sellerid+'"/>');
		    forms.push('<input type="hidden" name="_input_charset" value="'+params.datas.inputcharset+'"/>');
		    forms.push('<input type="hidden" name="payment_type" value="'+params.datas.paymenttype+'"/>');
		    forms.push('<input type="hidden" name="notify_url" value="'+params.datas.notifyurl+'"/>');
		    forms.push('<input type="hidden" name="return_url" value="'+params.datas.returnurl+'"/>');
			forms.push('<input type="hidden" name="out_trade_no" value="'+params.datas.outtradeno+'"/>');
			forms.push('<input type="hidden" name="subject" value="'+params.datas.subject+'"/>');
			forms.push('<input type="hidden" name="total_fee" value="'+params.datas.totalfee+'"/>');
			forms.push('<input type="hidden" name="show_url" value="'+params.datas.showurl+'"/>');
			forms.push('<input type="hidden" name="sign" value="'+params.datas.sign+'"/>');
			forms.push('<input type="hidden" name="sign_type" value="'+params.datas.signtype+'"/>');
			params.el.attr("method","get");
		}else if(params.payway=="4" || params.payway=="kq"){//块钱
			forms.push('<input type="hidden" name="inputCharset" value="'+params.datas.inputcharset+'" />');
			forms.push('<input type="hidden" name="pageUrl" value="'+params.datas.pageurl+'" />');
			forms.push('<input type="hidden" name="bgUrl" value="'+params.datas.bgurl+'" />');
			forms.push('<input type="hidden" name="version" value="'+params.datas.version+'" />');
			forms.push('<input type="hidden" name="language" value="'+params.datas.language+'" />');
			forms.push('<input type="hidden" name="signType" value="'+params.datas.signtype+'" />');
			forms.push('<input type="hidden" name="signMsg" value="'+params.datas.signmsg+'" />');
			forms.push('<input type="hidden" name="merchantAcctId" value="'+params.datas.merchantacctid+'" />');
			forms.push('<input type="hidden" name="payerName" value="'+params.datas.payername+'" />');
			forms.push('<input type="hidden" name="payerIdType" value="'+params.datas.payeridtype+'" />');
			forms.push('<input type="hidden" name="payerId" value="'+params.datas.payerid+'" />');
			forms.push('<input type="hidden" name="orderId" value="'+params.datas.orderid+'" />');
			forms.push('<input type="hidden" name="mobileGateway" value="'+params.datas.mobilegateway+'" />');
			forms.push('<input type="hidden" name="orderAmount" value="'+params.datas.orderamount+'" />');
			forms.push('<input type="hidden" name="orderTime" value="'+params.datas.ordertime+'" />');
			forms.push('<input type="hidden" name="productName" value="'+params.datas.productname+'" />');
			forms.push('<input type="hidden" name="productNum" value="'+params.datas.productnum+'" />');
			forms.push('<input type="hidden" name="productId" value="'+params.datas.productid+'" />');
			forms.push('<input type="hidden" name="productDesc" value="'+params.datas.productdesc+'" />');
			forms.push('<input type="hidden" name="ext1" value="'+params.datas.ext1+'" />');
			forms.push('<input type="hidden" name="ext2" value="'+params.datas.ext2+'" />');
			forms.push('<input type="hidden" name="payType" value="'+params.datas.paytype+'" />');
			forms.push('<input type="hidden" name="bankId" value="'+params.datas.bankid+'" />');
			forms.push('<input type="hidden" name="redoFlag" value="'+params.datas.redoflag+'" />');
			forms.push('<input type="hidden" name="pid" value="'+params.datas.pid+'" />');
		}else if(params.payway=="2"||params.payway=="union"){//银联
			   forms.push('<input type="hidden" name="txnType" id="txnType" value="'+params.datas.txntype+'" />');
			   forms.push('<input type="hidden" name="frontUrl" id="frontUrl" value="'+params.datas.fronturl+'" />');
			   forms.push('<input type="hidden" name="currencyCode" id="currencyCode" value="'+params.datas.currencycode+'" />');
			   forms.push('<input type="hidden" name="channelType" id="channelType" value="'+params.datas.channeltype+'" />');
			   forms.push('<input type="hidden" name="merId" id="merId" value="'+params.datas.merid+'" />');
			   forms.push('<input type="hidden" name="txnSubType" id="txnSubType" value="'+params.datas.txnsubtype+'" />');
			   forms.push('<input type="hidden" name="txnAmt" id="txnAmt" value="'+params.datas.txnamt+'" />');
			   forms.push('<input type="hidden" name="version" id="version" value="'+params.datas.version+'" />');
			   forms.push('<input type="hidden" name="signMethod" id="signMethod" value="'+params.datas.signmethod+'" />');
			   forms.push('<input type="hidden" name="backUrl" id="backUrl" value="'+params.datas.backurl+'" />');
			   forms.push('<input type="hidden" name="certId" id="certId" value="'+params.datas.certid+'" />');
			   forms.push('<input type="hidden" name="encoding" id="encoding" value="'+params.datas.encoding+'" />');
			   forms.push('<input type="hidden" name="bizType" id="bizType" value="'+params.datas.biztype+'" />');
			   forms.push('<input type="hidden" name="signature" id="signature" value="'+params.datas.signature+'" />');
			   forms.push('<input type="hidden" name="orderId" id="orderId" value="'+params.datas.orderid+'" />');
			   forms.push('<input type="hidden" name="accessType" id="accessType" value="'+params.datas.accesstype+'" />');
			   forms.push('<input type="hidden" name="txnTime" id="txnTime" value="'+params.datas.txntime+'" />');
		}else if(params.payway=="mergeAlipay"){//国际支付宝
			forms.push('<input type="hidden" name="return_url" value="'+params.datas.returnurl+'"/>');
			forms.push('<input type="hidden" name="notify_url" value="'+params.datas.notifyurl+'"/>');
			forms.push('<input type="hidden" name="subject" value="'+params.datas.subject+'"/>');
			forms.push('<input type="hidden" name="out_trade_no" value="'+params.datas.outtradeno+'"/>');
			forms.push('<input type="hidden" name="currency" value="'+params.datas.currency+'"/>');
			forms.push('<input type="hidden" name="partner" value="'+params.datas.partner+'"/>');
			forms.push('<input type="hidden" name="sign" value="'+params.datas.sign+'"/>');
			forms.push('<input type="hidden" name="_input_charset" value="'+params.datas.inputcharset+'"/>');
			forms.push('<input type="hidden" name="rmb_fee" value="'+params.datas.rmbfee+'"/>');
			forms.push('<input type="hidden" name="sign_type" value="'+params.datas.signtype+'"/>');
			forms.push('<input type="hidden" name="service" value="'+params.datas.service+'"/>');
			forms.push('<input type="hidden" name="show_url" value="'+params.datas.merchanturl+'"/>');
			forms.push('<input type="hidden" name="product_code" value="'+params.datas.productcode+'"/>');
			forms.push('<input type="hidden" name="split_fund_info" value="'+params.datas.splitfundinfo+'"/>');
			//forms.push('<input type="hidden" name="ext_params" value="'+params.datas.extparams+'"/>');
			params.el.attr("method","get");
		}

		params.el.attr("action", params.datas.actionurl	);
	    params.el.html(forms.join(""));
	    return forms.length>0;
	}

	me.getPaymentName=function(paypartner){
		var result=paypartner;
		switch(paypartner){
			case "0":result="支付宝支付";break;
			case "1":result="微信支付";break;
			case "2":result="银联支付";break;
			case "3":result="招行支付";break;
			case "4":result="快钱支付";break;
			case "5":result="国际支付宝支付";break;
		}
		return result;
	}

	//清除结算数据
	me.removeClearingData=function(){
		me.removeSessionStorage("clearing_select_address");//收货地址
		me.removeSessionStorage("clearing_select_pay");//支付方式
		me.removeSessionStorage("clearing_select_lnvoice");//发票
		me.removeSessionStorage("clearing_select_coupon");//优惠券
		me.removeSessionStorage("clearing_select_redenvelopes");//红包
		me.removeSessionStorage("usedpointsign");//积分
		me.removeSessionStorage("select_address_backpage");//海淘选择收货地址后返回的url
		me.removeSessionStorage("buy_now_details_url");// 立即购买前的url
		me.removeSessionStorage("buy_now_uuid");// 立即购买的uuid
		//实名认证
		me.removeSessionStorage("certification_zheng");
		me.removeSessionStorage("certification_fan");
		me.removeSessionStorage("realnum");
		me.removeSessionStorage("realname");
		me.removeSessionStorage("add_address");
		me.removeSessionStorage("edit_address");
		//地址localStorage
		me.removelocalStorage("my_address");
	}

}).call(xigou, Zepto);

var USER_INFO = xigou.getLocalStorage('token');
var isScrolling = false;
var isScrollPage = [];
function useScroll(flgs,page,callback,isascending){
	if(flgs){
		window.onscroll=function(){
			var scrollTop = document.body.scrollTop || document.documentElement.scrollTop,
				windowHeight = document.documentElement.clientHeight,
				documentHeight = document.body.offsetHeight;
			if(windowHeight + scrollTop > documentHeight - 50 && !isScrolling){
				isScrolling = true;		
				page++;
				callback(page,isascending);
			}
		}
	}
	
}

/**
 * 跳转app原生登录
 */
function loginMessage(e){
	//清除本地数据
	xigou.removeSessionStorage('userinfo');
	xigou.removelocalStorage('token');
	//跳转原生登录
	if(e){
		xigou.alert({
			message:e,
			callback:function(){
				window.location.href = "logon.html";
			}
		});
	}
}
/**
 * 未登录跳转
 * @param 购物车或者个人中心
 */
function jumpLogin(type){
	if(USER_INFO){
		switch (type){
			case "shopcar":
				if(window.location.href.indexOf("haitao.html")>-1){
					xigou.setSessionStorage('refer','cart.html?tuntype=1');
					window.location.href="cart.html?tuntype=1";
				}else{
					xigou.setSessionStorage('refer','cart.html');
					window.location.href="cart.html";
				}
				break;
			default :
				xigou.setSessionStorage('refer','home.html');
				window.location.href="home.html";
				break;
		}
	}else{
		switch (type){
			case "shopcar":
				if(window.location.href.indexOf("haitao.html")>-1){
					xigou.setSessionStorage('refer','cart.html?tuntype=1');
					window.location.href="logon.html";
				}else{
					xigou.setSessionStorage('refer','cart.html');
					window.location.href="logon.html";
				}
				break;
			default :
				xigou.setSessionStorage('refer','home.html');
				window.location.href="logon.html";
				break;
		}
	}
}
/**
 * 埋点 m_tracking.js
 * **/




//-- Urchin On Demand Settings ONLY  //需求设置

//var _myuacct="";			// set up the Urchin Account
var _myuserv=1;			// service mode (0=local,1=remote,2=both)
//-- category
//var _mych="";
//var _mycate="";

//-- UTM User Settings 用户设置
var _myufsc=1;			// set client info flag (1=on|0=off) 设置客户端信息标志
var _myudn="auto";		// (auto|none|domain) set the domain name for cookies 设置Cookie域名
var _myuhash="on";		// (on|off) unique domain hash for cookies 唯一的cookie域名
var _myutimeout="1800";   	// set the inactive session timeout in seconds 设置会话超时时间

if( typeof tracking_collector == 'undefined' ) tracking_collector = 'http://tracking.xg.com';
//if( typeof tracking_collector == 'undefined' ) tracking_collector ='http://127.0.0.1:8080/ROOT';
var _myugifpath=tracking_collector+"/mlog.gif";	// set the web path to the __myutm.gif file
var _myutsp=",";			// transaction field separator  设置分隔符
var _myuflash=1;			// set flash version detect option (1=on|0=off) 设置是否刷新
var _myutitle=1;			// set the document title detect option (1=on|0=off) 是否开启文件名
var _myulink=0;			// enable linker functionality (1=on|0=off) 使用链接器功能
var _myuanchor=0;			// enable use of anchors for campaign (1=on|0=off) 允许使用活动锚点 (1=on|0=off)

//-- UTM Campaign Tracking Settings
var _myuctm=1;			// set campaign tracking module (1=on|0=off) 设置广告系列跟踪模块 (1=on|0=off)
var _myucto="15768000";		// set timeout in seconds (6 month default) 设置过期时间，以秒为单位（通常为六个月）
var _myuccn="utm_campaign";	// name
var _myucmd="utm_medium";		// medium (cpc|cpm|link|email|organic) 媒介
var _myucsr="utm_source";		// source 来源
var _myuctr="utm_term";		// term/keyword 术语/关键词
var _myucct="utm_content";	// content 内容
var _myucid="utm_id";		// id number
var _myucno="utm_nooverride";	// don't override 不重写

//-- Auto/Organic Sources and Keywords 自动/远程 源关键词
// - Add 22-28, George, 2007-4-29
var _myuOsr=new Array();
var _myuOkw=new Array();
_myuOsr[0]="google";	    _myuOkw[0]="q";
_myuOsr[1]="yahoo";	    _myuOkw[1]="p";
_myuOsr[2]="msn";		    _myuOkw[2]="q";
_myuOsr[3]="aol";		    _myuOkw[3]="query";
_myuOsr[4]="lycos";	    _myuOkw[4]="query";
_myuOsr[5]="ask";		    _myuOkw[5]="q";
_myuOsr[6]="altavista";	_myuOkw[6]="q";
_myuOsr[7]="search";	    _myuOkw[7]="q";
_myuOsr[8]="netscape";	_myuOkw[8]="query";
_myuOsr[9]="earthlink";	_myuOkw[9]="q";
_myuOsr[10]="cnn";	    _myuOkw[10]="query";
_myuOsr[11]="looksmart";	_myuOkw[11]="key";
_myuOsr[12]="about";	    _myuOkw[12]="terms";
_myuOsr[13]="excite";	    _myuOkw[13]="qkw";
_myuOsr[14]="mamma";	    _myuOkw[14]="query";
_myuOsr[15]="alltheweb";	_myuOkw[15]="q";
_myuOsr[16]="gigablast";	_myuOkw[16]="q";
_myuOsr[17]="voila";	    _myuOkw[17]="kw";
_myuOsr[18]="virgilio";	_myuOkw[18]="qs";
_myuOsr[19]="teoma";  	_myuOkw[19]="q";
_myuOsr[20]="baidu";      _myuOkw[20]="wd";
_myuOsr[21]="3721";       _myuOkw[21]="name";
_myuOsr[22]="baidu";      _myuOkw[22]="word";
_myuOsr[23]="qq";         _myuOkw[23]="w";
_myuOsr[24]="sogou";      _myuOkw[24]="query";
_myuOsr[25]="iask";       _myuOkw[25]="k";
_myuOsr[26]="zhongsou";   _myuOkw[26]="word";
_myuOsr[27]="alexa";      _myuOkw[27]="q";
_myuOsr[28]="163";        _myuOkw[28]="q";
_myuOsr[29]="360";        _myuOkw[29]="q";
_myuOsr[30]="soso";       _myuOkw[30]="w";
_myuOsr[31]="so";         _myuOkw[31]="q";
_myuOsr[32]="bing";       _myuOkw[32]="q";
_myuOsr[33]="youdao";     _myuOkw[33]="q";
_myuOsr[34]="sm.cn";     _myuOkw[34]="q";


//-- Auto/Organic Keywords to Ignore  忽略自动或系统的关键词
var _myuOno=new Array();
//_myuOno[0]="urchin";
//_myuOno[1]="urchin.com";
//_myuOno[2]="www.urchin.com";

//-- Referral domains to Ignore  忽略跳转域
var _myuRno=new Array();
//_myuRno[0]=".urchin.com";


//-- **** Don't modify below this point ***不要修改下面这一点
var _myuff,_myudh,_myudt,_myubl=0,_myudo="",_myuu,_myufns=0,_myuns=0,_myur="-",_myufno=0,_myust=0,_myubd=document,_myudl=_myubd.location,_myudlh="",_myutcp="/",_myuwv="1",logEvent="",trackerCode="";

var _myugifpath2=tracking_collector+"/mlog.gif"; //访问路径

// _myudl - document location
if (_myudl.hash)  //设置或返回从井号 (#) 开始的 URL（锚）。
	_myudlh=_myudl.href.substring(_myudl.href.indexOf('#'));// indexOf('#')从字符串搜索"#"的位置,默认从0开始，subString切割从'#'号到末尾
if (_myudl.protocol=="https:")
	_myugifpath2=tracking_collector+"/mlog.gif";
if (!_myutcp || _myutcp=="")
	_myutcp="/";


function myurchinTracker(logEvent,trackerCode,_href) { //测试整个流程
	if (_myudl.protocol=="file:") return; //location.protocol 可设置或返回当前 URL 的协议。 输出如：http:,file:
	//if (_myuff && (!page || page=="")) return;

	var a,b,c,v,z,k,x="",s="",f=0;
	var nx=" expires=Sun, 18 Jan 2038 00:00:00 GMT;"; //设置失效时间
	var dc=_myubd.cookie;  //document.cookie

	_myudh=_myuDomain(); //域名

	_rand = Math.random(); //随机数
	_myuu=Math.round(_rand*2147483647); //"utmn"
	_myudt=new Date(); //时间
	_myust=Math.round(_myudt.getTime()/1000);
	a=dc.indexOf("__myutma="+_myudh);//_myudh 域名, 返回"__myutma="+_myudh 第一次出现的位置
	b=dc.indexOf("__myutmb="+_myudh);
	c=dc.indexOf("__myutmc="+_myudh);

	if (_myudn && _myudn!="") {
		_myudo=" domain="+_myudn+";";
	}
	if (_myutimeout && _myutimeout!="") {
		x=new Date(_myudt.getTime()+(_myutimeout*1000));
		x=" expires="+x.toGMTString()+";";
	}

	//not enabled yet.
	if (_myulink) {
		if (_myuanchor && _myudlh && _myudlh!="") s=_myudlh+"&";
		s+=_myudl.search; //search:设置或返回从问号？开始的URL(?之后)
		if(s && s!="" && s.indexOf("__myutma=")>=0) {
			if (!(_myuIN(a=_myuGC(s,"__myutma=","&")))) a="-";
			if (!(_myuIN(b=_myuGC(s,"__myutmb=","&")))) b="-";
			if (!(_myuIN(c=_myuGC(s,"__myutmc=","&")))) c="-";
			v=_myuGC(s,"__myutmv=","&");
			z=_myuGC(s,"__myutmz=","&");
			k=_myuGC(s,"__myutmk=","&");
			if ((k*1) != ((_myuHash(a+b+c+z+v)*1)+(_myudh*1))) {_myubl=1;a="-";b="-";c="-";z="-";v="-";}

			if (a!="-" && b!="-" && c!="-")
				f=1;
			else if(a!="-")
				f=2;
		}
	}
	if(f==1) {
		_myubd.cookie="__myutma="+a+"; path="+_myutcp+";"+nx+_myudo;
		_myubd.cookie="__myutmb="+b+"; path="+_myutcp+";"+x+_myudo;
		_myubd.cookie="__myutmc="+c+"; path="+_myutcp+";"+_myudo;
	} else if (f==2) {
		a=_myuFixA(s,"&",_myust);
		_myubd.cookie="__myutma="+a+"; path="+_myutcp+";"+nx+_myudo;
		_myubd.cookie="__myutmb="+_myudh+"; path="+_myutcp+";"+x+_myudo;
		_myubd.cookie="__myutmc="+_myudh+"; path="+_myutcp+";"+_myudo;
		_myufns=1;
	} else if (a>=0 && b>=0 && c>=0) {
		_myubd.cookie="__myutmb="+_myudh+"; path="+_myutcp+";"+x+_myudo;
	} else {
		if (a>=0)
			a=_myuFixA(_myubd.cookie,";",_myust);
		else
			a=_myudh+"."+_myuu+"."+_myust+"."+_myust+"."+_myust+".1";
		_myubd.cookie="__myutma="+a+"; path="+_myutcp+";"+nx+_myudo;
		_myubd.cookie="__myutmb="+_myudh+"; path="+_myutcp+";"+x+_myudo;
		_myubd.cookie="__myutmc="+_myudh+"; path="+_myutcp+";"+_myudo;
		_myufns=1;
	}

	if (_myulink && v && v!="" && v!="-") {
		v=_myuUES(v);
		if (v.indexOf(";")==-1)
			_myubd.cookie="__myutmv="+v+"; path="+_myutcp+";"+nx+_myudo;
	}
	_myuInfo(logEvent,trackerCode,_href);
	_myufns=0;
	_myufno=0;
	_myuff=1;
}

//Info
function _myuInfo(logEvent,trackerCode,_href) {
	var url="",ref="",p="",s="",pg=_myudl.pathname+_myudl.search;  //pathname设置或返回当前 URL 的路径部分。search设置或返回从问号 (?) 开始的 URL（查询部分）。
	if (logEvent && logEvent!="") pg=_myuES(logEvent,1);

	_myur=_myubd.referrer;
//    if (!_myur || _myur=="") {
//        _myur="-";
//    } else {
//        p=_myur.indexOf(_myubd.domain);
//        if ((p>=0) && (p<=8)) { _myur="0"; }
//        if (_myur.indexOf("[")==0 && _myur.lastIndexOf("]")==(_myur.length-1)) { _myur="-"; }
//    }

	_href=_href.replace("&", "-");
	_myur = _myur.replace("&", "-");

	s+="q="+trackerCode+","+logEvent+","+_myuu+","; // 随机数 (第一个参数)
	if (_myudl.hostname && _myudl.hostname!="") s+=_myuES(_myudl.hostname); //_myuES 解码 获得hostname(第四个参数)
	if (_myufsc) {s+=_myuBInfo()}else{s+=",,,,,,,"};

	s+=","+getCookie()+","+parsePar()+","+getOrderNo();

//    if(_myuGCS()&&_myuGCS()!="") cookie = _myuGCS();

	//if (_myutitle && _myubd.title && _myubd.title!="") ref=_myuES(_myur);
	ref=_myuES(_myur);
	if(location.href) url=location.href;

	url =url.replace("&", "-");

	url=_myuES(url);
	_href=_myuES(_href);



	if (_myuserv==0 || _myuserv==2) {
		var i=new Image(1,1);
		//i.src=_myugifpath+"?"+"utmwv="+_myuwv+s;//old
		i.src=_myugifpath+"?"+s;
		i.onload=function() {_myuVoid();}
	}
	if (_myuserv==1 || _myuserv==2) {
		var i2=new Image(1,1);
		//i2.src=_myugifpath2+"?"+"utmwv="+_myuwv+s+"&utmac="+_myuacct+"&utmcc="+_myuGCS();
//        alert(s);
		i2.src=_myugifpath2+"?"+s+"&a="+ref+"&b="+url+"&c="+_href;
		//_myuGCS()获取cookie信息
		//alert("utmwv="+_myuwv+s+"&utmac="+_myuacct+"&utmcc="+_myuGCS());
		i2.onload=function() { _myuVoid(); } //_myuVoid(): return;
	}
	return;
}



function _myuVoid() { return; }

function _myuCInfo() {
	if (!_myucto || _myucto=="") { _myucto="15768000"; }

	var c="",t="-",t2="-",t3="-",o=0,cs=0,cn=0,i=0,z="-",s="";
	if (_myuanchor && _myudlh && _myudlh!="") s=_myudlh+"&";

	s+=_myudl.search;
	var x=new Date(_myudt.getTime()+(_myucto*1000));
	var dc=_myubd.cookie;
	x=" expires="+x.toGMTString()+";";
	if (_myulink && !_myubl) {
		z=_myuUES(_myuGC(s,"__myutmz=","&"));
		if (z!="-" && z.indexOf(";")==-1) {
			_myubd.cookie="__myutmz="+z+"; path="+_myutcp+";"+x+_myudo; return ""; }
		//_myudh域名,_myutcp = '/';x='超时时间',_myudo域名,_myust=Math.round(_myudt.getTime()/1000);
	}
	z=dc.indexOf("__myutmz="+_myudh);
	if (z>-1) { z=_myuGC(dc,"__myutmz="+_myudh,";"); }
	else { z="-"; }
	//alert(z);
	t=_myuGC(s,_myucid+"=","&");
	t2=_myuGC(s,_myucsr+"=","&");
	t3=_myuGC(s,"gclid=","&");
	if ((t!="-" && t!="") || (t2!="-" && t2!="") || (t3!="-" && t3!="")) {
		if (t!="-" && t!="") c+="utmcid="+_myuEC(t);
		if (t2!="-" && t2!="") { if (c != "") c+="|"; c+="utmcsr="+_myuEC(t2); }
		if (t3!="-" && t3!="") { if (c != "") c+="|"; c+="utmgclid="+_myuEC(t3); }
		t=_myuGC(s,_myuccn+"=","&");
		if (t!="-" && t!="") c+="|utmccn="+_myuEC(t);
		else c+="|utmccn=(not+set)";
		t=_myuGC(s,_myucmd+"=","&");
		if (t!="-" && t!="") c+="|utmcmd="+_myuEC(t);
		else  c+="|utmcmd=(not+set)";
		t=_myuGC(s,_myuctr+"=","&");
		if (t!="-" && t!="") c+="|utmctr="+_myuEC(t);
		else { t=_myuOrg(1); if (t!="-" && t!="") c+="|utmctr="+_myuEC(t); }
		t=_myuGC(s,_myucct+"=","&");
		if (t!="-" && t!="") c+="|utmcct="+_myuEC(t);
		t=_myuGC(s,_myucno+"=","&");
		if (t=="1") o=1;
		if (z!="-" && o==1) return "";
	}
	if (c=="-" || c=="") { c=_myuOrg(); if (z!="-" && _myufno==1)  return ""; }
	if (c=="-" || c=="") { if (_myufns==1)  c=_myuRef(); if (z!="-" && _myufno==1)  return ""; }
	if (c=="-" || c=="") {
		if (z=="-" && _myufns==1) { c="utmccn:(direct)&utmcsr:(direct)&utmcmd:(none)"; }// 将|换掉
		if (c=="-" || c=="") return "";
	}
	if (z!="-") {
		i=z.indexOf(".");
		if (i>-1) i=z.indexOf(".",i+1);
		if (i>-1) i=z.indexOf(".",i+1);
		if (i>-1) i=z.indexOf(".",i+1);
		t=z.substring(i+1,z.length);
		if (t.toLowerCase()==c.toLowerCase()) cs=1;
		t=z.substring(0,i);
		if ((i=t.lastIndexOf(".")) > -1) {
			t=t.substring(i+1,t.length);
			cn=(t*1);
		}
	}
	if (cs==0 || _myufns==1) {
		t=_myuGC(dc,"__myutma="+_myudh,";");
		if ((i=t.lastIndexOf(".")) > 9) {
			_myuns=t.substring(i+1,t.length);
			_myuns=(_myuns*1);
		}
		cn++;
		if (_myuns==0) _myuns=1;
		_myubd.cookie="__myutmz="+_myudh+"."+_myust+"."+_myuns+"."+cn+"."+c+"; path="+_myutcp+"; "+x+_myudo;
		//alert("__myutmz="+_myudh+"."+_myust+"."+_myuns+"."+c+"; path="+_myutcp+"; "+x+_myudo);
		//alert(_myubd.cookie);
	}
	if (cs==0 || _myufns==1) return "&utmcn=1";
	else return "&utmcr=1";

}


function _myuRef() { //获取ref的值
	if (_myur=="0" || _myur=="" || _myur=="-") return "";
	var i=0,h,k,n;
	if ((i=_myur.indexOf("://"))<0) return "";
	h=_myur.substring(i+3,_myur.length);
	if (h.indexOf("/") > -1) {
		k=h.substring(h.indexOf("/"),h.length);
		if (k.indexOf("?") > -1) k=k.substring(0,k.indexOf("?"));
		h=h.substring(0,h.indexOf("/"));
	}
	h=h.toLowerCase();
	n=h;
	if ((i=n.indexOf(":")) > -1) n=n.substring(0,i);
	for (var ii=0;ii<_myuRno.length;ii++) {
		if ((i=n.indexOf(_myuRno[ii].toLowerCase())) > -1 && n.length==(i+_myuRno[ii].length)) { _myufno=1; break; }
	}
	if (h.indexOf("www.")==0) h=h.substring(4,h.length);
	return "utmccn=(referral)|utmcsr="+_myuEC(h)+"|"+"utmcct="+_myuEC(k)+"|utmcmd=referral";//
}



function _myuOrg(t) {
	if (_myur=="0" || _myur=="" || _myur=="-") return "";
	var i=0,h,k,csr;
	if ((i=_myur.indexOf("://")) < 0) return "";
	h=_myur.substring(i+3,_myur.length);
	if (h.indexOf("/") > -1) {
		h=h.substring(0,h.indexOf("/"));
	}
	for (var ii=0;ii<_myuOsr.length;ii++) {
		if (h.toLowerCase().indexOf(_myuOsr[ii].toLowerCase()) > -1) {
			csr = _myuOsr[ii];
			if ((i=_myur.indexOf("?"+_myuOkw[ii]+"=")) > -1 || (i=_myur.indexOf("&"+_myuOkw[ii]+"=")) > -1) {
				k=_myur.substring(i+_myuOkw[ii].length+2,_myur.length);
				if ((i=k.indexOf("&")) > -1) k=k.substring(0,i);
				for (var yy=0;yy<_myuOno.length;yy++) {
					if (_myuOno[yy].toLowerCase()==k.toLowerCase()) { _myufno=1; break; }
				}
				if (t){
					return _myuEC(k);
				} else {
					if((i=csr.indexOf(".")) > -1){
						csr = csr.substring(0,i);
					}
					return "utmccn=(organic)|utmcsr="+_myuEC(csr)+"|"+"utmctr="+_myuEC(k)+"|utmcmd=organic";
				}
			}
		}
	}
	return "";
}

function _myuBInfo() {  //获取浏览器相关信息
	var sr="-",sc="-",ul="-",fl="-",je=1,os='-',browser='-',url='-',device='-';
	var n=navigator;
	if(n.platform) {os=n.platform;}
	if(n.userAgent) {browser=n.userAgent;
		browser=browser.replace(/,/g, "/");}
	if(getUserDevice()) {device=getUserDevice();}
	if (self.screen) {
		sr=screen.width+"x"+screen.height;
		sc=screen.colorDepth+"-bit";
	} else if (self.java) {
		var j=java.awt.Toolkit.getDefaultToolkit();
		var s=j.getScreenSize();
		sr=s.width+"x"+s.height;
	}
	if (n.language) { ul=n.language.toLowerCase(); }
	else if (n.browserLanguage) { ul=n.browserLanguage.toLowerCase(); }

	je=n.javaEnabled()?1:0;

	if (_myuflash)
		fl=_myuFlash();

	return ","+sr+","+os+","+sc+","+je+","+device+","+browser+","+fl; //第二个参数
}

function _myuFlash() { // 获得Flash版本
	var f="-",n=navigator;
	if (n.plugins && n.plugins.length) {
		for (var ii=0;ii<n.plugins.length;ii++) {
			if (n.plugins[ii].name.indexOf('Shockwave Flash')!=-1) {
				f=n.plugins[ii].description.split('Shockwave Flash ')[1];
				break;
			}
		}
	} else if (window.ActiveXObject) {
		for (var ii=10;ii>=2;ii--) {
			try {
				var fl=eval("new ActiveXObject('ShockwaveFlash.ShockwaveFlash."+ii+"');");
				if (fl) { f=ii + '.0'; break; }
			}
			catch(e) {}
		}
	}
	return f;
}


function _myuGCS() { //从cookie中获取相关数据
	var t,c="",dc=_myubd.cookie;
	if ((t=_myuGC(dc,"__myutma="+_myudh,";"))!="-") c+=_myuES("__myutma="+t+";+");//_myudh 域名
	if ((t=_myuGC(dc,"__myutmb="+_myudh,";"))!="-") c+=_myuES("__myutmb="+t+";+");
	if ((t=_myuGC(dc,"__myutmc="+_myudh,";"))!="-") c+=_myuES("__myutmc="+t+";+");
	if ((t=_myuGC(dc,"__myutmz="+_myudh,";"))!="-") c+=_myuES("__myutmz="+t+";+");
	if ((t=_myuGC(dc,"__myutmv="+_myudh,";"))!="-") c+=_myuES("__myutmv="+t+";");
	if (c.charAt(c.length-1)=="+") c=c.substring(0,c.length-1);
	return c;
}



function _myuGC(l,n,s) { //获取location中某个key=号后面的值， 分割符号cookie, name, separator
	if (!l || l=="" || !n || n=="" || !s || s=="") return "-";
	var i,i2,i3,c="-";
	i=l.indexOf(n);
	i3=n.indexOf("=")+1;
	if (i > -1) {
		i2=l.indexOf(s,i); if (i2 < 0) { i2=l.length; }//从l中搜索s，从第i个开始
		c=l.substring((i+i3),i2);
	}
	return c;
}



function _myuDomain() {
	if (!_myudn || _myudn=="" || _myudn=="none") { _myudn=""; return 1; }
	if (_myudn=="auto") {
		var d=_myubd.domain;
		if (d.substring(0,4)=="www.") {
			d=d.substring(4,d.length);//获取域名
		}
		_myudn=d;
	}
	if (_myuhash=="off") return 1;
	return _myuHash(_myudn);
}


function _myuHash(d) {
	if (!d || d=="") return 1;
	var h=0,g=0;
	for (var i=d.length-1;i>=0;i--) {
		var c=parseInt(d.charCodeAt(i));
		h=((h << 6) & 0xfffffff) + c + (c << 14);
		if ((g=h & 0xfe00000)!=0) h=(h ^ (g >> 21));
	}
	return h;
}


// - Calculate a for _myutma
function _myuFixA(c,s,t) {
	if (!c || c=="" || !s || s=="" || !t || t=="") return "-";
	var a=_myuGC(c,"__myutma="+_myudh,s);
	var lt=0,i=0;
	if ((i=a.lastIndexOf(".")) > 9) {
		_myuns=a.substring(i+1,a.length);
		_myuns=(_myuns*1)+1;
		a=a.substring(0,i);
		if ((i=a.lastIndexOf(".")) > 7) {
			lt=a.substring(i+1,a.length);
			a=a.substring(0,i);
		}
		if ((i=a.lastIndexOf(".")) > 5) {
			a=a.substring(0,i);
		}
		a+="."+lt+"."+t+"."+_myuns;
	}
	return a;
}

function _myuTrim(s) {
	if (!s || s=="") return "";
	//开头为空时，截取1到末尾
	while ((s.charAt(0)==' ') || (s.charAt(0)=='\n') || (s.charAt(0,1)=='\r')) s=s.substring(1,s.length);//第一个参数：字符串对象，第二个参数：开始的位置，第三个参数，显示长度，如果没有，则直至字符串尾
	//结尾为空时，从0开始。长度s.length-1
	while ((s.charAt(s.length-1)==' ') || (s.charAt(s.length-1)=='\n') || (s.charAt(s.length-1)=='\r')) s=s.substring(0,s.length-1);
	return s;
}


function _myuEC(s) {//遍历,返回字符串
	var n="";
	if (!s || s=="") return "";
	for (var i=0;i<s.length;i++) {if (s.charAt(i)==" ") n+="+"; else n+=s.charAt(i);}//str="adc"+"a"="adca
	return n;
}

function _myuIN(n) {
	if (!n) return false;
	for (var i=0;i<n.length;i++) {
		var c=n.charAt(i); //JavaScript中charAt函数的作用是返回指定索引位置处的字符。
		if ((c<"0" || c>"9") && (c!=".")) return false;
	}
	return true;
}


function _uuid(len, radix) {
	// Private array of chars to use
	var CHARS = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');

	var chars = CHARS, uuid = [], i;
	radix = radix || chars.length;

	if (len) {
		// Compact form
		for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
	} else {
		// rfc4122, version 4 form
		var r;

		// rfc4122 requires these characters
		uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
		uuid[14] = '4';

		// Fill in random data.  At i==19 set the high bits of clock sequence as
		// per rfc4122, sec. 4.1.5
		for (i = 0; i < 36; i++) {
			if (!uuid[i]) {
				r = 0 | Math.random()*16;
				uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
			}
		}
	}

	return uuid.join('');
};

addCookie();
//alert(getCookie());

function addCookie() {// 添加cookie
	var objName = "cookieId";
	var objValue = _uuid(10, 16);
	var objHours = 24;

	var str = objName + "=" + escape(objValue);
	if (objHours > 0) {// 为0时不设定过期时间，浏览器关闭时cookie自动消失
		var date = new Date();
		var ms = objHours * 3600 * 1000;
		date.setTime(date.getTime() + ms);
		str += "; path="+_myutcp+"; expires=" + date.toGMTString()+";"+_myudo;
	}
	//alert("添加cookie成功");
	if(!getCookie()){
		document.cookie = str;

	}
}

function getCookie() {// 获取指定名称的cookie的值
	var objName = "cookieId";
	var arrStr = document.cookie.split("; ");
	for (var i = 0; i < arrStr.length; i++) {
		var temp = arrStr[i].split("=");
		if (temp[0] == objName)
			return unescape(temp[1]);
	}
	return "";
}


function parsePar(){
	//{"topicId":"topicId11111"-"skuCode":"11111"}-{"topicId":"topicId11111"-"skuCode":"11111"}
	//参数使用逗号作为切割符，所有使用-代替逗号
	var paramStr = "";
	var items = $(".data-tracking input")
	if(items.size()!=0){
		var json="{";
		items.each(function(){
			if(json != "{") json+="-{";
			var topicId = $(this).attr("topicId");
			var skuCode = $(this).attr("skuCode");
			var quantity = $(this).attr("quantity");
			var salePrice = $(this).attr("salePrice");
			if(topicId==undefined) topicId="";
			if(skuCode==undefined) skuCode="";
			if(quantity==undefined) quantity="";
			if(salePrice==undefined) salePrice="";
			json+="\"topicId\":\""+topicId+"\"-";
			json+="\"skuCode\":\""+skuCode+"\"-";
			json+="\"quantity\":\""+quantity+"\"-";
			json+="\"salePrice\":\""+salePrice+"\"}";
		});
		paramStr = json;
	}
	return paramStr;
}

function getOrderNo(){
	var orderNo = $("a[order-no]").attr("order-no");
	if(orderNo==undefined) return "";
	return orderNo;
}

function _myuES(s,u) {
	if (typeof(encodeURIComponent) == 'function') {//encodeURIComponent编码，让其在计算机上可读。函数可把字符串作为 URI 组件进行编码。
		if (u) return encodeURI(s);
		else return encodeURIComponent(s);
	} else {
		return escape(s);
	}
}

function _myuUES(s) {
	//typeof 运算符返回一个用来表示表达式的数据类型的字符串。
	if (typeof(decodeURIComponent) == 'function') { //decodeURIComponent解码，用于解码URI
		return decodeURIComponent(s);//解码url
	} else {
		return unescape(s);//解码String对象，不可以解码URI
	}
}

function getUserDevice() { //获取访问设备信息
	var devices = {
		iphone: /^mozilla(.*)iphone(.*)mobile/,
		ipad: /^mozilla(.*)ipad/,
		android: /^mozilla(.*)linux(.*)android/,
		browser: /(^mozilla(.*)(msie|firefox|chrome|presto|safari))|(opera)/
	};

	var br = navigator.userAgent.toLowerCase();
	for (var key in devices) {
		if (br.match(devices[key])) {
			return key;
		}
	}
	return 'unknow';
};

/***
* 分类导航接口开关
**/
// (function($) {
// 	var me = this;
// 	me.activeClassNav = me.activeClassNav || {};
// 	me.activeClassNav.queryswitch = function(params) {
// 		var path = me.activeHost + me.activeUrl.index.queryswitch + '?' + params.p;
// 		ajaxGet(params, path);
// 	}
// 	var _queryswitch = me.getSessionStorage("queryswitch");
// 	if(_queryswitch == "1") {
// 		var _link = '<a href="classnav.html" class="footer_link footer_class" data-track="c_foot_classify">分类</a>';
// 		var _footer = $('a[class*=footer_spike]');
// 		_footer.replaceWith($(_link));
// 	}else {
// 		me.activeClassNav.queryswitch({
// 			p: 'name=frontcategory',
// 			callback: function(response, status) { //回调函数
// 				if (status == xigou.dictionary.success) {
// 					var json = response || null;
// 					if (null == json || json.length == 0) return false;
// 					if (json.resultInfo.code == 0) {
// 						var _val = json.data.switchs[0].value;
// 						if(_val == "1") {
// 							var _link = '<a href="classnav.html" class="footer_link footer_class" data-track="c_foot_classify">分类</a>';
// 							var _footer = $('a[class*=footer_spike]');
// 							_footer.replaceWith($(_link));
// 							me.setSessionStorage("queryswitch","1");
// 						}
// 					}
// 				}
// 			}
// 		});	
// 	}
	
// }).call(xigou, Zepto);

$(function(){
	// load
	var loadtrack = $('[data-loadtrack]').attr('data-loadtrack');
	if( loadtrack )
		myurchinTracker( 1, loadtrack,'');

	// click
	/*var el = $('[data-track]');
	el.on('click', function(e){
		var href = $(this).attr('href');
		if(href==undefined) href='';
		var dt = $(this).attr('data-track');
		if(dt==undefined) dt='';
		myurchinTracker(2,dt,href);
		var _tar = $(e.target);
		var _a = _tar.is('a') ? $(_tar) : $(_tar).parents('a');
		if( _a.is('a') ){
			if( _a.attr('href').match(/^#/) || _a.attr('target') == '_blank' ){
				return true;
			}
			e.preventDefault();
			setTimeout(function(){
				location.href = _a.attr('href');
			},200);
		}
	});*/


	$(document).on('click','[data-track]',function(e){
		var href = $(this).attr('href');
		if(href==undefined) href='';
		var dt = $(this).attr('data-track');
		if(dt==undefined) dt='';
		myurchinTracker(2,dt,href);
		var _tar = $(e.target);
		var _a = _tar.is('a') ? $(_tar) : $(_tar).parents('a');
		if( _a.is('a') ){
			if( _a.attr('href').match(/^#/) || _a.attr('target') == '_blank' ){
				return true;
			}
			e.preventDefault();
			setTimeout(function(){
				location.href = _a.attr('href');
			},200);
		}
	});
	
});








