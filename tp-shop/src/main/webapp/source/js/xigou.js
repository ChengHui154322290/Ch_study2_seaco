/**
 *
 * @title   JavaScript Document
 * @authors xigou
 * @date    2015-12-27 13:35:12
 * @version Ver 1.0.0
 * 项目脚本主文件，每个页面都得引用。文件里有配置文件，公共参数，公共方法，公共组件
 */
var xigou = xigou || {};
/**字符串处理函数**/
 String.prototype.endWith=function(s){
	  if(s==null||s==""||this.length==0||s.length>this.length)
	     return false;
	  if(this.substring(this.length-s.length)==s)
	     return true;
	  else
	     return false;
	  return true;
	 };
 String.prototype.startWith=function(s){
  if(s==null||s==""||this.length==0||s.length>this.length)
   return false;
  if(this.substr(0,s.length)==s)
     return true;
  else
     return false;
  return true;
 };

 function isEmpty(parameters){
	 if(parameters == null || parameters == ""){
		 return true;
	 }else{
		 return false;
	 }
 }
 
 function isNotEmpty(parameters){
	 return !isEmpty(parameters);
 }


;
(function($) {
	var _this = this;

	//定义事件名称
	this.events = {
		click: function() {
			return "ontouchend" in document ? "tap" : "click";
		}(),
		change: "change"
	};

	//支付类型
	this.paymentType = {
		"0": "支付宝",
		"1": "微信支付",
		"2": "银联支付",
		"3": "招行支付",
		"4": "快钱支付"
	};

	//初始化定义常量对象
	this.dictionary = {
		success: 'success',
		error: 'error',
		fail: 'fail',
		chineseTips: {
			server: {
				value_is_null: '服务端未有数据返回'
			}
		},
		carCountDown: 30 * 60 * 1000 //购物车 去结算倒计时时间 30分钟
	};


	//默认图片
	this.default_img = "./resources/images/icon/icon-default.png";
	this.default_img2 = "./resources/images/icon/lazyload-610x266.png";
	this.default_img3 = "./resources/images/icon/lazyload-640x278.png";

	//Debug打印
	this.opendebugPrint = function() {
		return false;
	}();

	this.debugMsgList = []; //当body没有加载完成,临时存储日志
	this.debugPrint = function(msg) {
		if (_this.opendebugPrint) {
			if ($("#xigou_logs").size() < 1) {
				var html = '<div id="xigou_logs" style="display:none;top:10%;left:10%;width: 80%;height: 80%;position: fixed;border-radius: 5px;background-color: #ccc;z-index: 9999;">' + '<div class="content" style="padding:5px;overflow:scroll;height:90%"></div>' + '<div class="close" style="height:5%;text-align:center;padding:5px 0px 5px 0px;border-radius: 5px;border: 1px solid blue;color: white;background-color: #005bac;position: absolute;bottom:0px;width:99%;" onclick="$(this).parent().hide()">关闭</div>' + '</div>';
				$("body").append(html);
				_this.debugMsgList.push(msg);
			} else {
				if (_this.debugMsgList.length > 0) {
					$("#xigou_logs .content").append(_this.debugMsgList.join("<br/>"));
					_this.debugMsgList.length = 0;
				}
				$("#xigou_logs .content").append(msg + "<br/>");
			}
			if (window.console) {
				console.log(msg);
			}
		}
	};

	/**
	 *  console.log
	 */
	this.log = function(msg) {
		this.debugPrint(msg);
	};

	//是否使用静态数据
	this.locationData = function() {
		// 本地文件打开使用静态数据
		 // if (!window.location.host) {
		 // 	return true;
		 // }
		return false;
	}();
    this.activeHost = 'http://'+window.location.host+'/';
	this.xgHost = 'http://m.seagoor.com/';
	this.getOpenIdHost = this.xgHost;
	this.pageHost = this.activeHost;
	this.channelcode=window.location.host.split('\.')[0];
	this.activeUrl = {
		index: {
			getbanners: "index/getbanners.htm", //首页-广告位信息
			gettodaynew: "index/gettodaynew.htm", //首页-今日上新
			module:"index/module.htm",	// 首页-模块信息
		},
		user: {
			logon: "user/logon.htm", //用户-登录
			getcaptcha: "user/getcaptcha.htm", //用户-获取手机验证码
			pregeetest: "user/pregeetest.htm", //用户-极验预处理
			sendgeecode: "user/sendgeecode.htm", //用户-极验发送验证码
			supcount: "user/supcount.htm",
			regist: "user/regist.htm", //用户-注册
			modifypwd: "user/modifypwd.htm",
			logout: "user/logout.htm",
			unionlogon: "user/union/logon.htm",	// 联合登录
			bindunion:"user/safety/bindunion.htm"//绑定联合账户
		},
		dssuser: {
			regist: "dss/regist.htm",
			getdssinfo: "dss/getinfo.htm",
			shareshop: "/dss/shareShop.htm",
			shareScanPromoter: "/dss/shareScanPromoter.htm",
			getchannelinfo:"/dss/channel/getinfo.htm"
		},
		wx:{
			getopenid: "wx/oauth/getopenid.htm",
			config:"wx/oauth/config.htm",
			getcodeurl:"wx/oauth/getcodeurl.htm",
			getuserinfo: "wx/oauth/getuserinfo.htm"
		},
		file:{
			uploadimg: "file/uploadimg.htm",
		},
		cart:{
			add: "cart/add.htm",
			supcount: "cart/supcount.htm",
			load: "cart/load.htm",
			operation: "cart/operation.htm",
		},
		safety:{
			checkauth: "user/safety/checkauth.htm",
			auth: "user/safety/auth.htm",
		},
		coupon:{
			list: "coupon/list.htm",
			inreceive: "coupon/exchange.htm",
			redpacket:"coupon/receivemany.htm",
			offlinecouponcode: "coupon/offlinecouponcode.htm",
			queryordercouponcount:"coupon/queryordercouponcount.htm"
		},
		point:{
			list:'point/list.htm'
		},
		position: {
			getprovlist: "position/getprovlist.htm",
			getarealist: "position/getarealist.htm",
			getareatree: 'position/getareatree.htm'
		},
		address: {
			list: "address/list.htm",
			add: "address/add.htm",
			edit: "address/edit.htm",
			del: "address/del.htm",
		},
		topic: {
			detail: "topic/detail.htm",
			products: "topic/products.htm"
		},
		order: {
			list: "order/list.htm",
			submitinfo: "order/submitinfo.htm",
			submit: "order/submit.htm",
			confirm: "order/confirm.htm",
			calordel: "order/calordel.htm",
			detail: "order/detail.htm",
			buynow:"order/buynow.htm",
			logistic:"logistic/list.htm",
			mergesubmit:"order/mergesubmit.htm"
		},
		product: {
			detail: "product/detail.htm",
		},
		pay: {
			payorder: "pay/payorder.htm",
			payresult: "pay/payresult.htm",
		},
		aftersales: {
			list:"aftersales/list2detail.htm",
			companylist:"logistic/companylist.htm",
			apply: "aftersales/apply.htm",
			cancel: "aftersales/cancel.htm",
			submitlogistic: "aftersales/submitlogistic.htm",
		},
		classify: {
			navigation: "search/navigation.htm",
			search: "search.htm",
			condition: "search/condition.htm",
		},
		promoter: {
			account:"promoter/account.htm",
			billinfo:"promoter/billinfo.htm",
			dealers:"promoter/dealers.htm",
			orders:"promoter/orders.htm",
			withdraw:"promoter/withdraw.htm",
			login:"promoter/login.htm",
			items:"dss/items.htm",
			updatepromoter:"promoter/updatepromoter.htm",
			withdrawdetail:"promoter/withdrawdetail.htm",
			getopics:"promoter/getopics.htm",
			getopicitems:"promoter/getopicitems.htm",
			setshelves:"promoter/setshelves.htm",
			getitles:"promoter/getitles.htm",
			checkInvisteCode:"/promoter/getInviteCode.htm",
			saveInvistePromoter:"/promoter/saveInviteUserInfo.htm"
		},
		groupbuy: {
			detail: "groupbuy/detail.htm",
			launch: "groupbuy/launch.htm",
			join: "groupbuy/join.htm",
			my: "groupbuy/my.htm",
			list: "groupbuy/list.htm",
			listforindex:"groupbuy/listforindex.htm",
		},
		offlinegb:{
			banner:"offlinegb/banner.htm",
			shoplist:"offlinegb/shoplist.htm",
			hotsale: "offlinegb/hotsale.htm"
		},
		withdrawals:{
			getWithdrawDetail:"dss/getWithdrawDetail.htm"
		},
		hhblogin:{
			hhblogin:"hhbgroup/loginorregist.htm"
		}

	};


	//获取运行设备的视口高度和宽度
	this.win = {
		width: $(window).width(),
		height: $(window).height()
	};

	//获取设备信息
	this.device = function() {
		var navig = window.navigator;
		return {
			appCodeName: navig.appCodeName, //返回浏览器的代码名
			appMinorVersion: navig.appMinorVersion, //返回浏览器的次级版本
			appName: navig.appName, //返回浏览器的名称
			appVersion: navig.appVersion, //返回浏览器的平台和版本信息
			browserLanguage: navig.browserLanguage, //返回当前浏览器的语言
			cookieEnabled: navig.cookieEnabled, //返回指明浏览器中是否启用 cookie 的布尔值
			cpuClass: navig.cpuClass, //返回浏览器系统的 CPU 等级
			onLine: navig.onLine, //返回指明系统是否处于脱机模式的布尔值
			platform: navig.platform, //返回运行浏览器的操作系统平台
			systemLanguage: navig.systemLanguage, //返回 OS 使用的默认语言
			userAgent: navig.userAgent, //返回由客户机发送服务器的 user-agent 头部的值
			userLanguage: navig.userLanguage //返回 OS 的自然语言设置
		};
	}();

	//返回当前系统名称
	this.systemName = function() {
		var u = _this.device.userAgent,
			oem = ["IOS", "AM", "AP", "ATV", "WP", "WP8", "MACOSX"],
			version = "1.1.0";
		if (/Version/i.test(u)) {
			var index = u.indexOf('Version');
			if (index > -1) {
				version = u.slice(index + 8, index + 8 + 4);
			}
		}
		if (/iPhone|iPod|iPad/i.test(u)) {
			return {
				name: oem[0],
				version: version
			};
		} else if (/Android/i.test(u)) {
			return {
				name: oem[1],
				version: version
			};
		} else if (/Windows Phone 7.0/i.test(u)) {
			return {
				name: oem[4],
				version: version
			};
		} else if (/Windows Phone 8.0/i.test(u)) {
			return {
				name: oem[5],
				version: version
			};
		} else if (/MACOSX/i.test(u)) {
			return {
				name: oem[6],
				version: version
			};
		} else {
			return {
				name: 'NULL',
				version: version
			};
		}
	}();

	/**
	 * 得到地址栏参数
	 * @param names 参数名称
	 * @param urls 从指定的urls获取参数
	 * @returns string
	 */
	this.getQueryString = function(names, urls) {
		urls = urls || window.location.href;
		urls && urls.indexOf("?") > -1 ? urls = urls.substring(urls.indexOf("?") + 1) : "";
		var reg = new RegExp("(^|&)" + names + "=([^&]*)(&|$)", "i");
		var r = urls ? urls.match(reg) : window.location.search.substr(1).match(reg);
		if (r != null && r[2] != "") return decodeURI(r[2]);
		return null;
	};

    //修改url参数
    this.changeURLPar = function(destiny, par, par_value)
    {
        var pattern = par+'=([^&]*)';
        var replaceText = par+'='+par_value;
        if (destiny.match(pattern))
        {
            var tmp = '/\\'+par+'=[^&]*/';
            if("sku" == par)
            {
            	tmp = "/\sku=[^&]*/";
            }
            tmp = destiny.replace(eval(tmp), replaceText);
            return (tmp);
        }
        else
        {
            if (destiny.match('[\?]'))
            {
                return destiny+'&'+ replaceText;
            }
            else
            {
                return destiny+'?'+replaceText;
            }
        }
        return destiny+'\n'+par+'\n'+par_value;
    };
	
	//判断是否支持无痕Local\sessionStroage
	this.supportStroage = function() {
		var flag = true;
		try {
			if (window.localStorage) {
				window.localStorage["test"] = "test";
			} else {
				flag = false;
			}
		} catch (e) { //对于无痕模式下会出现异常
			flag = false;
		}
		return flag;
	};

	//setLocalStorage
	this.setLocalStorage = function(key, value, isJson) {
		_this.debugPrint("设置localStorage数据key=" + key + ",是否为json数据:" + (isJson ? "true" : "false"));
		if (!this.supportStroage()) {
			$.tips({
		                content:'暂不支持无痕浏览!',
		                stayTime:2000,
		                type:"warn"
		            })
			// xigou.alert("暂不支持无痕浏览!");
			return;
		}
		if (window.localStorage) {
			if (isJson) {
				value = JSON.stringify(value);
			}
			_this.debugPrint("设置localStorage数据key=" + key + ",value=" + value);
			try {
				window.localStorage[key] = value;
			} catch (e) {
				//处于无痕模式时，存放到cookie当中
				_this.debugPrint("当前浏览器处于无痕模式");
				$.tips({
		                content:'暂不支持无痕浏览!',
		                stayTime:2000,
		                type:"warn"
		            })
				// xigou.alert("不支持无痕浏览!");
			}

		} else {
			_this.debugPrint("当前浏览器不支持localStorage");
		}
	};

	//getLocalStorage
	this.getLocalStorage = function(key, isJson) {
		_this.debugPrint("获取localStorage数据key=" + key + ",是否为json数据:" + (isJson ? "true" : "false"));
		if (!this.supportStroage()) {
			$.tips({
		                content:'暂不支持无痕浏览!',
		                stayTime:2000,
		                type:"warn"
		            })
			// xigou.alert("暂不支持无痕浏览!");
			return;
		}
		if (window.localStorage) {
			var value = window.localStorage[key] || "";
			if (isJson && value) {
				value = JSON.parse(value);
			}
			_this.debugPrint("获取localStorage数据key=" + key + ",value=" + value);
			return value;
		} else {
			_this.debugPrint("当前浏览器不支持localStorage");
		}
	};

	//获取token
	this.getToken = function(){
		return _this.getLocalStorage('token');
	};

	//setSessionStorage
	this.setSessionStorage = function(key, value, isJson) {
		_this.debugPrint("设置sessionStorage数据key=" + key + ",是否为json数据:" + (isJson ? "true" : "false"));
		if (!this.supportStroage()) {
			$.tips({
		                content:'暂不支持无痕浏览!',
		                stayTime:2000,
		                type:"warn"
		            })
			// xigou.alert("暂不支持无痕浏览!");
			return;
		}
		if (window.sessionStorage) {
			if (isJson) {
				value = JSON.stringify(value);
			}
			_this.debugPrint("设置sessionStorage数据key=" + key + ",value=" + value);
			window.sessionStorage[key] = value;
		} else {
			_this.debugPrint("当前浏览器不支持sessionStorage");
		}
	};

	//getSessionStorage
	this.getSessionStorage = function(key, isJson) {
		_this.debugPrint("获取sessionStorage数据key=" + key + ",是否为json数据:" + (isJson ? "true" : "false"));
		if (!this.supportStroage()) {
			$.tips({
		                content:'暂不支持无痕浏览!',
		                stayTime:2000,
		                type:"warn"
		            })
			// xigou.alert("暂不支持无痕浏览!");
			return;
		}
		if (window.sessionStorage) {
			var value = window.sessionStorage[key] || "";
			if (isJson && value) {
				value = JSON.parse(value);
			}
			_this.debugPrint("获取sessionStorage数据key=" + key + ",value=" + value);
			return value;
		} else {
			_this.debugPrint("当前浏览器不支持sessionStorage");
		}
	};

	//removeSessionStorage
	this.removeSessionStorage = function(key) {
		_this.debugPrint("移除sessionStorage数据key=" + key);
		if (!this.supportStroage()) {
			$.tips({
		                content:'暂不支持无痕浏览!',
		                stayTime:2000,
		                type:"warn"
		            })
			// xigou.alert("暂不支持无痕浏览!");
			return;
		}
		if (window.sessionStorage) {
			window.sessionStorage.removeItem(key);
		} else {
			_this.debugPrint("当前浏览器不支持sessionStorage");
		}
	};

	//removelocalStorage
	this.removelocalStorage = function(key) {
		_this.debugPrint("移除localStorage数据key=" + key);
		if (!this.supportStroage()) {
			$.tips({
		                content:'暂不支持无痕浏览!',
		                stayTime:2000,
		                type:"warn"
		            })
			// xigou.alert("暂不支持无痕浏览!");
			return;
		}
		if (window.localStorage) {
			window.localStorage.removeItem(key);
		} else {
			_this.debugPrint("当前浏览器不支持localStorage");
		}
	};

	//getCookie
	this.getCookie = function(key) {
		_this.debugPrint("获取cookie值,当前域为" + document.domain + ",key为" + key);
		var arr, reg = new RegExp("(^| )" + key + "=([^;]*)(;|$)");
		if (arr = document.cookie.match(reg)) {
			_this.debugPrint("获取cookie值,\r\nkey:" + key + "\r\nvalue:" + arr[2]);
			return unescape(arr[2]);
		} else {
			_this.debugPrint("获取cookie值,不存在key为" + key + "的cookie");
			return null;
		}
	};

	//removeCookie
	this.removeCookie = function(p) {
		var dp = {
			domain: "",
			key: "",
			path: "/"
		}
		if (typeof p == 'object') {
			$.extend(dp, p);
		} else if (typeof p == 'string') {
			dp.key = p;
		}
		if (!dp.domain) {
			dp.domain = function() {
				if (/^\d/.test(document.domain)) {
					return document.domain;
				} else if (/^[a-z]/i.test(document.domain)) {
					return document.domain.substring(document.domain.indexOf("."));
				}
				return document.domain;
			}();
		}


		_this.debugPrint("移除cookie值,当前域为" + dp.domain + ",path为" + dp.path + ",key为" + dp.key);
		var exp = new Date("2000", "1", "1");
		var cval = this.getCookie(dp.key);
		if (cval != null) {
			_this.debugPrint("成功移除key为" + dp.key + "的cookie值");
			document.cookie = dp.key + "=" + cval + ";domain=" + dp.domain + ";path=" + dp.path + ";expires=" + exp.toGMTString();
		} else {
			_this.debugPrint("移除cookie值,不存在key为" + dp.key + "的cookie");
		}
	};

	//setCookie
	this.setCookie = function(cookie_name, cookie_value, isencode) {
		isencode = typeof isencode == 'undefined' ? true : isencode;
		if (isencode) {
			cookie_value = encodeURIComponent(cookie_value);
		}
		document.cookie = cookie_name + "=" + cookie_value + "; path=/;";
	};

	//获取默认区域和城市(如果数据库修改请修改此地方)
	this.defaultAddress = {
		area: {
			code: '250',
			name: '华东'
		},
		city: {
			code: '264',
			name: '上海市'
		}
	};
	if (!_this.getLocalStorage("address", true)) {
		_this.setLocalStorage("address", _this.defaultAddress, true);
	}

	//公共入参
	var type = 'wap';
	var ua = window.navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i) == 'micromessenger'){
		type = 'wx';
	}
	this.publicParams = {
		get: function(urls) {
			var params = urls.indexOf("?") > -1 ? '&' : '?';
			var arry = new Array();
			arry.push('oem=' + _this.systemName.name);
			arry.push('osversion=' + _this.systemName.version);
			arry.push('screenwidth=' + _this.win.width);
			arry.push('screenheight=' + _this.win.height);
			arry.push('apptype=' + type);
			arry.push('appversion=1.0.1');
			arry.push('nettype=unknown');
			arry.push('regcode=' + JSON.parse(_this.getLocalStorage("address")).area.code);
			arry.push('provcode=' + JSON.parse(_this.getLocalStorage("address")).city.code);
			arry.push('partner=xigou');
			params += arry.join('&');
			return params;
		},
		post: function() {
			return {
				oem: _this.systemName.name,
				osversion: _this.systemName.version,
				screenwidth: _this.win.width,
				screenheight: _this.win.height,
				apptype: type,
				appversion: '1.0.1',
				nettype: 'unknown',
				regcode: JSON.parse(_this.getLocalStorage("address")).area.code,
				provcode: JSON.parse(_this.getLocalStorage("address")).city.code,
				partner: 'xigou'
			};
		}
	};

	this._ajaxCount = 0; //发起ajax的次数(只为处理loading效果)
	this._ajaxCompleteCount = 0; //完成ajax的次数(只为处理loading效果)

	//post方法
	this.post = function(options) {
		var showLoading = true;
		if (typeof options.showLoading != 'undefined') {
			showLoading = options.showLoading;
		}
		_this.debugPrint("进入ajax的post方法请求  参数为：" + JSON.stringify(options));
//		alert("进入ajax的post方法请求  参数为：" + JSON.stringify(options));
		var data = options.requestBody;
		$.extend(data, _this.publicParams.post());
		
		$.ajax({
			type: 'post',
			url: options.url,
			dataType: "json",
			contentType: 'application/json',
			accepts: "application/json",
			data: JSON.stringify(data),
			timeout: 1000 * 60, //60秒超时
			beforeSend: function(XHR) {
				if (window.xigou) {
					if (showLoading) {
						_this._ajaxCount++;
						xigou.loading.open();
					}
				}
			},
			success: function(data) {
				if (window.xigou) {
					if (showLoading) {
						_this._ajaxCompleteCount++;
						if (_this._ajaxCount == _this._ajaxCompleteCount) {
							xigou.loading.close();
						}
					}
				}
//				alert("ajax的post方法返回数据为：" + JSON.stringify(data));
				//统一处理null值
				if (data) {
					data = JSON.stringify(data);
					data = JSON.parse(data.replace(/:null/g, ":\"\""));
				}

				_this.debugPrint("【" + options.url + "】完成ajax请求 进入success函数,返回值为：" + JSON.stringify(data) + "调用返回函数");
				$.isFunction(options.callback) && options.callback(data, _this.dictionary.success);
			},
			error: function(xhr, errorType, error) {
				if (window.xigou) {
					if (showLoading) {
						_this._ajaxCompleteCount++;
						if (_this._ajaxCount == _this._ajaxCompleteCount) {
							xigou.loading.close();
						}
					}
				}

				if (errorType == "abort") { //无网络
					$.tips({
		                content:'网络已断开',
		                stayTime:2000,
		                type:"warn"
		            })
//					xigou.alert("网络已断开");
				} else if (errorType == "timeout") { //超时
					$.tips({
		                content:'系统连接超时',
		                stayTime:2000,
		                type:"warn"
		            })
					// xigou.alert("系统连接超时");
				} else if (errorType == "error") { //服务器或者客户端错误
					switch (xhr.status) {
						case 403:
							$.tips({
				                content:'服务器禁止访问',
				                stayTime:2000,
				                type:"warn"
				            })
							// xigou.alert("服务器禁止访问");
							break;
						case 404:
							$.tips({
				                content:'未找到服务器',
				                stayTime:2000,
				                type:"warn"
				            })
							// xigou.alert("未找到服务器");
							break;
						case 500:
							$.tips({
				                content:'服务器未响应',
				                stayTime:2000,
				                type:"warn"
				            })
							// xigou.alert("服务器未响应");
							break;
						case 503:
							$.tips({
				                content:'服务器不可用',
				                stayTime:2000,
				                type:"warn"
				            })
							// xigou.alert("服务器不可用");
							break;
						case 504:
							$.tips({
				                content:'网关超时',
				                stayTime:2000,
				                type:"warn"
				            })
							// xigou.alert("网关超时");
							break;

					}
				} else {
					$.isFunction(dp.callback) && dp.callback({
						xhr: xhr,
						errorType: errorType,
						error: error
					}, _this.dictionary.fail);
				}

				_this.debugPrint("完成ajax请求 进入error函数");
				//_this.debugPrint("errorType:" + errorType + "/xhr:" + JSON.stringify(xhr) + "/error:" + error);
			}
		});
	};

	//get方法
	this.get = function(options) {
		var showLoading = true;
		if (typeof options.showLoading != 'undefined') {
			showLoading = options.showLoading;
		}
		_this.debugPrint("进入ajax的get方法请求  参数为：" + JSON.stringify(options));
		$.ajax({
			type: 'get',
			url: options.url + _this.publicParams.get(options.url),
			dataType: "json",
			contentType: 'application/json',
			accepts: "application/json",
			timeout: 1000 * 60, //60秒超时
			beforeSend: function(XHR) {
				if (window.xigou) {
					if (showLoading) {
						_this._ajaxCount++;
						xigou.loading.open();
					}
				}
			},
			success: function(data) {
				if (window.xigou) {
					if (showLoading) {
						_this._ajaxCompleteCount++;
						if (_this._ajaxCount == _this._ajaxCompleteCount) {
							xigou.loading.close();
						}
					}
				}

				//统一处理null值
				if (data) {
					data = JSON.stringify(data);
					data = JSON.parse(data.replace(/:null/g, ":\"\""));
				}

				_this.debugPrint("【" + options.url + "】完成ajax请求 进入success函数,返回值为：" + JSON.stringify(data) + "调用返回函数");
				$.isFunction(options.callback) && options.callback(data, _this.dictionary.success);
			},
			error: function(xhr, errorType, error) {
				if (window.xigou) {
					if (showLoading) {
						_this._ajaxCompleteCount++;
						if (_this._ajaxCount == _this._ajaxCompleteCount) {
							xigou.loading.close();
						}
					}
				}

				if (errorType == "abort") { //无网络
					$.tips({
		                content:'网络已断开',
		                stayTime:2000,
		                type:"warn"
		            })
					// xigou.alert("网络已断开");
				} else if (errorType == "timeout") { //超时
					$.tips({
				            content:'系统连接超时',
				            stayTime:2000,
				            type:"warn"
				        })
					// xigou.alert("系统连接超时");
				} else if (errorType == "error") { //服务器或者客户端错误
					switch (xhr.status) {
						case 403:
							$.tips({
				            	content:'服务器禁止访问',
				            	stayTime:2000,
				            	type:"warn"
				        	})
							// xigou.alert("服务器禁止访问");
							break;
						case 404:
							$.tips({
				            	content:'未找到服务器',
				            	stayTime:2000,
				            	type:"warn"
				        	})
							// xigou.alert("未找到服务器");
							break;
						case 500:
							$.tips({
				            	content:'服务器未响应',
				            	stayTime:2000,
				            	type:"warn"
				        	})
							// xigou.alert("服务器未响应");
							break;
						case 503:
							$.tips({
				            	content:'服务器不可用',
				            	stayTime:2000,
				            	type:"warn"
				        	})
							// xigou.alert("服务器不可用");
							break;
						case 504:
							$.tips({
				            	content:'网关超时',
				            	stayTime:2000,
				            	type:"warn"
				        	})
							// xigou.alert("网关超时");
							break;

					}
				} else {
					$.isFunction(dp.callback) && dp.callback({
						xhr: xhr,
						errorType: errorType,
						error: error
					}, _this.dictionary.fail);
				}

				_this.debugPrint("完成ajax请求 进入error函数");
				//_this.debugPrint("errorType:" + errorType + "/xhr:" + JSON.stringify(xhr) + "/error:" + error);
			}
		});
	};

	//替换指定位置的字符串
	this.strReplaceByLocation = function(str, begin, end, chars) {
		var fstStr = str.substring(0, begin);
		var scdStr = str.substring(begin, end);
		var lstStr = str.substring(end, str.length);
		//var matchExp = /w/g;
		return fstStr + chars + lstStr;
	};

	//添加href参数
	this.addParamToATag = function(o, key, value) {
		if (value && key) {
			var orUrl = o,
				joinChar = "?";
			if (typeof o == 'object') {
				orUrl = o.attr("href");
				if (/\?+/.test(orUrl)) {
					joinChar = "&";
				}
				o.attr("href", orUrl + joinChar + key + "=" + value);
			} else if (typeof o == 'string') {
				return orUrl + joinChar + key + "=" + value;
			}
			return true;
		}
		return false;
	};

	this.getDssUserInfo = function(params) {
		params.requestBody = params.requestBody || {};
		var path = this.activeHost + this.activeUrl.dssuser.getdssinfo;
		ajaxPost(params, path);
	}
	this.shareShop = function(params) {
		params.requestBody = params.requestBody || {};
		var path = this.activeHost + this.activeUrl.dssuser.shareshop;
		ajaxPost(params, path);
	}
	
	this.shareScanPromoter = function(params) {
		params.requestBody = params.requestBody || {};
		var path = this.activeHost + this.activeUrl.dssuser.shareScanPromoter;
		ajaxPost(params, path);
	}
	this.getChannelInfo = function(params) {
		params.requestBody = params.requestBody || {};
		var path = this.activeHost + this.activeUrl.dssuser.getchannelinfo;
		ajaxPost(params, path);
	}
	
	
	//邀请码入口注册
	this.registerInvister=function(params){
		params.requestBody = params.requestBody || {};
		var path = this.activeHost + this.activeUrl.promoter.saveInvistePromoter;
		ajaxPost(params, path);
	
	}
	
	//邀请码验证
	this.checkInvisteCode=function(params){
		params.requestBody = params.requestBody || {};
		var path = this.activeHost + this.activeUrl.promoter.checkInvisteCode;
		ajaxPost(params, path);
	
	}
	
	
	
	//获取用户信息
	this.getLoginUserInfo = function(p) {
		p = p || {};
		//p.callback(userinfo,status)
		//如果当前SessionStorage登录信息为空就取cookie的值
		if (_this.getSessionStorage("userinfo") == "") {
			var token = "";

			function getLoginInfo() {

				//如果cookie有存储TOKEN,请求服务端获取用户信息
				if (token != "") {
					//_this.log("<span style='color:red'>最终获取到token值，开始请求服务端</span>");
					//(function() {
					//	_this.log("<span style='color:red'>开始请求服务端获取用户信息</span>");
					//	if (_this.user.getLoginInfo) {
					//		_this.user.getLoginInfo({
					//			request: {
					//				loginName: "",
					//				accessToken: token
					//			},
					//			callback: function(response, status) {
					//				if (status == _this.dictionary.success) { //成功
					//					_this.log("<span style='color:red'>获取用户信息成功</span>");
					//					response.responseBody.user.accessToken = _this.getLocalStorage(_this.dictionary.ACCESS_TOKEN);
					//					_this.setSessionStorage("userinfo", response.responseBody.user, true);
					//					$.isFunction(p.callback) && p.callback(response.responseBody.user, _this.dictionary.success);
					//				} else {
					//					_this.log("<span style='color:red'>获取用户信息失败</span>");
					//					_this.log("<span style='color:red'>获取用户失败信息" + (response.errorMsg || "无") + "</span>");
					//					$.isFunction(p.callback) && p.callback(null, _this.dictionary.fail);
					//				}
					//			},
					//			ignoreError: p.ignoreError || null //忽略错误集合
					//		});
					//	} else {
					//		var _callee = arguments.callee;
					//		setTimeout(function() {
					//			_callee();
					//		}, 200);
					//	}
					//})();
					$.isFunction(p.callback) && p.callback("", _this.dictionary.success);
				} else {
					_this.log("<span style='color:red'>最终获未取到token值</span>");
					$.isFunction(p.callback) && p.callback(null, _this.dictionary.fail);
				}
			};

			_this.log("<span style='color:red'>SessionStorage-userinfo为空</span>");
			//获取cookie值
			if (_this.getLocalStorage("token")) {
				_this.log("<span style='color:red'>web-getCookie-ACCESS_TOKEN有值</span>");
				token = _this.getLocalStorage("token");
				//token = token.replace(/\"/g, ""); //服务器端写入cookies时加了双引号
				getLoginInfo();
			} else {
				$.isFunction(p.callback) && p.callback(null, _this.dictionary.fail);
			}
		} else {
			$.isFunction(p.callback) && p.callback(_this.getSessionStorage("userinfo", true), _this.dictionary.success);
		}
	};
    // 补0
    function  addtime(s){
        return s < 10 ? '0'+s : s ;
    }
    //解析时间-传入时间戳
	this.formatTimeDate = function(now) {
		if(typeof now != 'number'){
			try{
				now=parseInt(now);
			}catch(e){
				
			}
		}

		var time = new Date(now),
			year = time.getFullYear(),
			month = time.getMonth() + 1,
			date = time.getDate(),
			hour = time.getHours(),
			minute = time.getMinutes(),
			second = time.getSeconds();


		return year + "-" + addtime(month) + "-" + addtime(date) + "　" + addtime(hour) + ":" + addtime(minute) + ":" + addtime(second);
	};

	//上传图片  file标签onchange事件传入(this.files) 返回base64格式
	this.uploadImage = function(fileList, callback) {
		var file = fileList[0];
		var b = new FileReader;
		if ((/image/i.test(file.type)) && (typeof FileReader !== "undefined")) {
			b.readAsDataURL(file);
			b.onload = function(e) {
				callback(e.target.result);
			}
		} else {
			return callback("");
		}
	};

	this.compress = function(source_img_obj, quality, output_format){

		var mime_type = "image/jpeg";
		if(output_format!=undefined && output_format=="png"){
			mime_type = "image/png";
		}
		var cvs = document.createElement('canvas');
		//naturalWidth真实图片的宽度
		cvs.width = source_img_obj.naturalWidth;
		cvs.height = source_img_obj.naturalHeight;
		var ctx = cvs.getContext("2d").drawImage(source_img_obj, 0, 0);
		var newImageData = cvs.toDataURL(mime_type, quality/100);
		var result_image_obj = new Image();
		result_image_obj.src = newImageData;
		return result_image_obj;
	};

	this.compress2 = function(imageStr, quality, callbackfun){

		var mime_type = "image/png";
		var img = new Image();
		var cvs = document.createElement('canvas');
		img.src = imageStr;
		cvs.width = 682;
		cvs.height = 434;
		img.onload = function() {
			var width = img.naturalWidth;
			var height = img.naturalHeight;

			var ctx = cvs.getContext("2d").drawImage(img, 0, 0, width, height, 0, 0, 682, 434);
			var newImageData = cvs.toDataURL(mime_type, quality/100);
			var result_image_obj = new Image();
			result_image_obj.src = newImageData;
			result_image_obj.onload = function(){
				if (callbackfun && $.isFunction(callbackfun)) {
					callbackfun(result_image_obj);
				}
			}
		};
	};

	$(function() {
		/**
		 * 绑定长按页面title显示log日志事件
		 */
		$("div.title").off("longTap").longTap(function() {
			$("#xigou_logs").show();
		});
	});

	// 页面统计
	var _hmt = _hmt || [];
	(function() {
		var hm = document.createElement("script");
		hm.src = "//hm.baidu.com/hm.js?6613c6e61ea0ff167b10a66563346dca";
		var s = document.getElementsByTagName("script")[0];
		s.parentNode.insertBefore(hm, s);
	})();

	//返回上一页
	this.redirect = function(){
		var fromPage = document.referrer.match(/\/([^\/]+\.html)/);
		if(fromPage){
			location.href = fromPage[1];
		}else{
			window.history.back();
		}
	}
}).call(xigou, Zepto);

(function($) {
    	var _this = this;

	//截取2位小数,不四舍五入
	this.subDecimal = function(num) {
		if (!isNaN(num)) {
			if (typeof num != 'number') {
				num = parseFloat(num);
			}
			num = Math.round(num * 100) / 100;
		}
		if (typeof(num) == 'undefined') {
			num = 0;
		}
		return num;
	}

	//折扣计算
	this.subDiscount = function(price, oldprice) {
		price = price == "" ? 0 : _this.subDecimal(price);
		oldprice = oldprice == "" ? 0 : _this.subDecimal(oldprice);
		//var result=(oldprice == price || oldprice == 0) ? "" : (parseInt((price * 100 / oldprice)) / 10);
		//取小数点后一位
		var result=(oldprice == price || oldprice == 0) ? "" : (Math.round(price * 100/ oldprice) / 10);
		//2015-3-21 0折不显示
		return (result==0 || result=="")?"":result+"折";
		//return (oldprice == price || oldprice == 0) ? "" : (parseInt((price * 100 / oldprice)) / 10 + "折");
	}
}).call(xigou, Zepto);

(function($){
	var _this = this;
	this.getwxOpenid = function(type, getNickName) {
		var url = '';
		if (type == 0) {
			url =  xigou.getOpenIdHost + 'getopenid.html?back_url=' + encodeURIComponent(location.href);
		}
		else {
			url =  xigou.getOpenIdHost + 'getopenid.html?back_url=' + encodeURIComponent(location.href);
		}
		if (getNickName) {
//			url += '&getNickName=1';
		}
		window.location.href = url;
	};

	// backurl 会跳链接
	// type 0 XXX/XXX.html用
	this.forceBindTel = function(p){
		if(!p || !$.isFunction(p.callback)) {
			return;
		}
		var backurl = p.backurl || window.location.href;
		var ua = window.navigator.userAgent.toLowerCase();
//		if(ua.match(/MicroMessenger/i) != 'micromessenger') {
		if(!isWeiXin()){
			// 不是微信
			p.callback(true);
		}
		else {
			var token = xigou.getToken();
			var openid = xigou.getLocalStorage("openid");
			// 微信还没自动登录
			if (!token || !openid) {
				p.callback(true);
			}
			var params = {
				'token': token,
				'uniontype': 1,
				'unionval': openid
			};
			xigou.activeUser.logon({
				requestBody: params,
				showLoading:false,
				callback: function(response, status) {
					var bNeed = false;
					do {
						if (status == xigou.dictionary.success) {
							if (response.code == '0' && response.data.tel) {
								bNeed = true;
								break;
							}
							else if (response.code == '-100') {
								// 用户失效 重新登录
								xigou.setSessionStorage("loginjump_url", backurl);
								if (p.type == 0) {
									window.location.href = '../logon.html';
								}
								else {
									window.location.href = 'logon.html';
								}
								return;
							}
						}
					} while (false);
					if (bNeed) {
						p.callback(true);
					}
					else {
						xigou.setSessionStorage('forceBindTel', backurl);
						if (p.type == 0) {
							window.location.href = '../forcebindtel.html';
						}
						else {
							window.location.href = 'forcebindtel.html';
						}
						p.callback(false);
					}
				}
			})
		}
	}

	this.wxreadyalert = function() {
		//alert('微信回调');
	}
}).call(xigou, Zepto)
//等待框
;
(function($) {
	var _this = this;
	var l = this.loading = {};

	l.open = function() {
		create();

		refresh();

		$(window).on('ortchange', function() {
			refresh();
		});
	};

	l.close = function() {
		if (document.getElementById("cpic_ui_mask")) {
			$('#cpic_ui_mask').hide().remove();
		}

		if (document.getElementById("cpic_ui_box")) {
			$('#cpic_ui_box').hide().remove();
		}
	};

	var create = function() {
		if ($("#cpic_ui_box").size() <= 0) {
			var h = new Array();
			h.push("<div id='cpic_ui_mask' style='height:" + _this.win.height + "px'></div>");
			h.push("<div id='cpic_ui_box' style='display:none'>");
			h.push("    <div class='cpic_ui_loading_img'>");
			h.push("    <div class='double-bounce1'></div>");
			h.push("    <div class='double-bounce2'></div>");
			h.push("</div>");
			h.push("</div>");
			h = $(h.join(""));
			$("body").append(h);
		}
	};

	var refresh = function() {
		var ret = {
			top: ((_this.win.height - 50) / 2) + "px",
			left: ((_this.win.width - 50) / 2) + "px",
			display: 'block'
		};

		$('#cpic_ui_mask').height(_this.win.height);
		$('#cpic_ui_box').css(ret);

	};
}).call(xigou, Zepto);

;
(function($) {
	this.toast = function(p,cssCls) {
		var h = $("<div class='one_toast "+cssCls+"' ><p>" + p + "</p></div>");
		$("body").append(h);

		var i = 0;
		var t = setInterval(function() {
			i = i + 0.1;
			if (i >= 1) {
				clearInterval(t);
			} else {
				h.css("opacity", i);
			}
		}, 80);

		setTimeout(function() {
			var j = 1;
			var ti = setInterval(function() {
				j = j - 0.1;
				if (j <= 0) {
					clearInterval(ti);
					h.remove();
				} else {
					h.css("opacity", j);
				}
			}, 80);
		}, 2000);

	}

}).call(xigou, Zepto);

//加载更多
;
(function($) {
	this.refresh = function(el, params) {
		el = el || $('.ui-refresh-down');

        //之前点击加载功能更换成滑动加载。
        el[xigou.events.click](function() {
			var item = $(this).find('span');
			if (!item.eq(0).hasClass('ui-loading')) {
				$(this).find('span').eq(0).addClass('ui-loading');
				$(this).find('span').eq(1).html('加载中...');
				$.isFunction(params.callback) && params.callback(true);
			}
        });
	};
}).call(xigou, Zepto);



/**加减控件
 * @param endval:截止值
 * @param callback:回调函数名返回当前值  function(val){}
 */
;
(function($) {
	$.fn.cartshopopt = function(callback) {
		var quantity_decrease = $(this).find(".quantity_decrease");
		var quantity_increase = $(this).find(".quantity_increase");
		//减
		quantity_decrease[xigou.events.click](function() {
			_dynamic(this, -1);
		});
		//加
		quantity_increase[xigou.events.click](function() {
			_dynamic(this, 1);
		});
		var _dynamic = function(e, num) {
			var $parent = $(e).parent();
			var $quantity_txt = $parent.find(".quantity_txt");
			var intxt = parseInt($quantity_txt.html());
			intxt = intxt + num;
			var endval = parseInt($parent.attr("maxNum"));
			var bBack = false;
			if (intxt >= 1 && intxt <= endval) {
				bBack = true;
			}
			if (intxt >= endval) {
				intxt = endval;
				$(e).addClass("disabled");
			} else if (intxt <= 1) {
				intxt = 1;
				$(e).addClass("disabled");
			} else {
				$(e).removeClass("disabled");
			}

			if (endval != 1) {
				if (num > 0) { //加
					//减可用
					$(e).siblings(".quantity_decrease").removeClass("disabled");
				} else { //减
					//加可用
					$(e).siblings(".quantity_increase").removeClass("disabled");
				}
			}
			console.log(intxt);
			$quantity_txt.html(intxt);
			if (bBack) {
				callback(intxt, num, e);
			}
		}

	}
}).call(xigou, Zepto);

//类型对应值
;
(function($) {
	//退货类型
	this.getreturnGoodsType = function(key) {
		var vals = {
			"0": "30天无理由",
			"1": "商品质量问题",
			"2": "商品错发或漏发",
			"3": "收到商品破损",
			/*"4": "拍错了",
			"5": "不想要了",
			"6": "已重新下单",*/
			"7": "拍错，已重新下单",
			"8": "价格变动"
		};
		return vals[key];
	};
	//售后状态
	this.getCustomerServiceState = function(key) {
		var vals = {
			"0": "<span style='color:#FB0C7C;'>待售后审核</span>",
			"1": "<span style='color:#0DD6BD;'>退货中</span>",
			"2": "<span style='color:#0DD6BD;'>退款中</span>",
			"3": "<span style=''>已退款</span>",
			"4": "<span style='color:#F60A0A;'>审核不通过</span>",
			"5": "<span style=''>取消退货</span>"
		};
		return key == "" ? "" : vals[key];
	};
}).call(xigou, Zepto);

$(function() {
	//设置refer,登录返回
	if(!window.location.pathname.endWith("login.html") && !window.location.pathname.endWith("logon.html"))
	{
		xigou.setSessionStorage("refer", window.document.referrer, false);
	}
});

/**
 *Base64解密
 *for孕育登陆时采用方法
**/

(function(global) {
	'use strict';
	// existing version for noConflict()
	var _Base64 = global.Base64;
	var version = "2.1.5";
	// if node.js, we use Buffer
	var buffer;
	if (typeof module !== 'undefined' && module.exports) {
		buffer = require('buffer').Buffer;
	}
	// constants
	var b64chars
		= 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';
	var b64tab = function(bin) {
		var t = {};
		for (var i = 0, l = bin.length; i < l; i++) t[bin.charAt(i)] = i;
		return t;
	}(b64chars);
	var fromCharCode = String.fromCharCode;
	// encoder stuff
	var cb_utob = function(c) {
		if (c.length < 2) {
			var cc = c.charCodeAt(0);
			return cc < 0x80 ? c
				: cc < 0x800 ? (fromCharCode(0xc0 | (cc >>> 6))
			+ fromCharCode(0x80 | (cc & 0x3f)))
				: (fromCharCode(0xe0 | ((cc >>> 12) & 0x0f))
			+ fromCharCode(0x80 | ((cc >>>  6) & 0x3f))
			+ fromCharCode(0x80 | ( cc         & 0x3f)));
		} else {
			var cc = 0x10000
				+ (c.charCodeAt(0) - 0xD800) * 0x400
				+ (c.charCodeAt(1) - 0xDC00);
			return (fromCharCode(0xf0 | ((cc >>> 18) & 0x07))
			+ fromCharCode(0x80 | ((cc >>> 12) & 0x3f))
			+ fromCharCode(0x80 | ((cc >>>  6) & 0x3f))
			+ fromCharCode(0x80 | ( cc         & 0x3f)));
		}
	};
	var re_utob = /[\uD800-\uDBFF][\uDC00-\uDFFFF]|[^\x00-\x7F]/g;
	var utob = function(u) {
		return u.replace(re_utob, cb_utob);
	};
	var cb_encode = function(ccc) {
		var padlen = [0, 2, 1][ccc.length % 3],
			ord = ccc.charCodeAt(0) << 16
				| ((ccc.length > 1 ? ccc.charCodeAt(1) : 0) << 8)
				| ((ccc.length > 2 ? ccc.charCodeAt(2) : 0)),
			chars = [
				b64chars.charAt( ord >>> 18),
				b64chars.charAt((ord >>> 12) & 63),
				padlen >= 2 ? '=' : b64chars.charAt((ord >>> 6) & 63),
				padlen >= 1 ? '=' : b64chars.charAt(ord & 63)
			];
		return chars.join('');
	};
	var btoa = global.btoa ? function(b) {
		return global.btoa(b);
	} : function(b) {
		return b.replace(/[\s\S]{1,3}/g, cb_encode);
	};
	var _encode = buffer
			? function (u) { return (new buffer(u)).toString('base64') }
			: function (u) { return btoa(utob(u)) }
		;
	var encode = function(u, urisafe) {
		return !urisafe
			? _encode(u)
			: _encode(u).replace(/[+\/]/g, function(m0) {
			return m0 == '+' ? '-' : '_';
		}).replace(/=/g, '');
	};
	var encodeURI = function(u) { return encode(u, true) };
	// decoder stuff
	var re_btou = new RegExp([
		'[\xC0-\xDF][\x80-\xBF]',
		'[\xE0-\xEF][\x80-\xBF]{2}',
		'[\xF0-\xF7][\x80-\xBF]{3}'
	].join('|'), 'g');
	var cb_btou = function(cccc) {
		switch(cccc.length) {
			case 4:
				var cp = ((0x07 & cccc.charCodeAt(0)) << 18)
						|    ((0x3f & cccc.charCodeAt(1)) << 12)
						|    ((0x3f & cccc.charCodeAt(2)) <<  6)
						|     (0x3f & cccc.charCodeAt(3)),
					offset = cp - 0x10000;
				return (fromCharCode((offset  >>> 10) + 0xD800)
				+ fromCharCode((offset & 0x3FF) + 0xDC00));
			case 3:
				return fromCharCode(
					((0x0f & cccc.charCodeAt(0)) << 12)
					| ((0x3f & cccc.charCodeAt(1)) << 6)
					|  (0x3f & cccc.charCodeAt(2))
				);
			default:
				return  fromCharCode(
					((0x1f & cccc.charCodeAt(0)) << 6)
					|  (0x3f & cccc.charCodeAt(1))
				);
		}
	};
	var btou = function(b) {
		return b.replace(re_btou, cb_btou);
	};
	var cb_decode = function(cccc) {
		var len = cccc.length,
			padlen = len % 4,
			n = (len > 0 ? b64tab[cccc.charAt(0)] << 18 : 0)
				| (len > 1 ? b64tab[cccc.charAt(1)] << 12 : 0)
				| (len > 2 ? b64tab[cccc.charAt(2)] <<  6 : 0)
				| (len > 3 ? b64tab[cccc.charAt(3)]       : 0),
			chars = [
				fromCharCode( n >>> 16),
				fromCharCode((n >>>  8) & 0xff),
				fromCharCode( n         & 0xff)
			];
		chars.length -= [0, 0, 2, 1][padlen];
		return chars.join('');
	};
	var atob = global.atob ? function(a) {
		return global.atob(a);
	} : function(a){
		return a.replace(/[\s\S]{1,4}/g, cb_decode);
	};
	var _decode = buffer
		? function(a) { return (new buffer(a, 'base64')).toString() }
		: function(a) { return btou(atob(a)) };
	var decode = function(a){
		return _decode(
			a.replace(/[-_]/g, function(m0) { return m0 == '-' ? '+' : '/' })
				.replace(/[^A-Za-z0-9\+\/]/g, '')
		);
	};
	var noConflict = function() {
		var Base64 = global.Base64;
		global.Base64 = _Base64;
		return Base64;
	};
	// export Base64
	global.Base64 = {
		VERSION: version,
		atob: atob,
		btoa: btoa,
		fromBase64: decode,
		toBase64: encode,
		utob: utob,
		encode: encode,
		encodeURI: encodeURI,
		btou: btou,
		decode: decode,
		noConflict: noConflict
	};
	// if ES5 is available, make Base64.extendString() available
	if (typeof Object.defineProperty === 'function') {
		var noEnum = function(v){
			return {value:v,enumerable:false,writable:true,configurable:true};
		};
		global.Base64.extendString = function () {
			Object.defineProperty(
				String.prototype, 'fromBase64', noEnum(function () {
					return decode(this)
				}));
			Object.defineProperty(
				String.prototype, 'toBase64', noEnum(function (urisafe) {
					return encode(this, urisafe)
				}));
			Object.defineProperty(
				String.prototype, 'toBase64URI', noEnum(function () {
					return encode(this, true)
				}));
		};
	}
	// that's it!
})(this);

if (this['Meteor']) {
	Base64 = global.Base64; // for normal export in Meteor.js
}

 var wx_param = {
    "appId" : "", //公众号名称，由商户传入
    "timeStamp" : "", //时间戳
    "nonceStr" : "", //随机串
    "package" : "",//扩展包
    "signType" : "", //微信签名方式:1.sha1
    "paySign" : "" //微信签名
};

function onBridgeReady(wx_param, pid, ordercode){
	alert(ordercode);
	if (typeof(WeixinJSBridge) == "undefined")
	{
		$.tips({
	        content:"请在微信里面使用",
	        stayTime:2000,
	        type:"warn"
	    })
	}
	else
	{
		WeixinJSBridge.invoke(
	       'getBrandWCPayRequest', wx_param,function(res){ 
	    	   //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
	           if(res.err_msg == "get_brand_wcpay_request:ok" ) {
	           	// window.location.href = "payresult.html?pid="+payid+"&ordercode="+ordercode;
	        	   window.location.href = wx_param.returnurl+"&ordercode="+ordercode;
	           }
	           //用户支付途中取消 
	           if(res.err_msg == "get_brand_wcpay_request:cancel" ) {
	        	   window.location.href = "orders.html";
	           }
	           //用户支付失败
	           if(res.err_msg == "get_brand_wcpay_request:fail" ) {
	        	   window.location.href = "orders.html";
	           }
	       }
	   ); 
	}
}

function onDssBridgeReady(wx_param, pid, ordercode){

	if (typeof(WeixinJSBridge) == "undefined")
	{
		$.tips({
	        content:"请在微信里面使用",
	        stayTime:2000,
	        type:"warn"
	    })
	}
	else
	{
		WeixinJSBridge.invoke(
	       'getBrandWCPayRequest', wx_param,function(res){  
	    	   //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
	           if(res.err_msg == "get_brand_wcpay_request:ok" ) {
	           	// window.location.href = "payresult.html?pid="+payid+"&ordercode="+ordercode;
	        	   window.location.href = wx_param.returnurl;
	           }
	           //用户支付途中取消 
	           if(res.err_msg == "get_brand_wcpay_request:cancel" ) {
	        	   window.location.href = "dsshome.html";
	           }
	           //用户支付失败
	           if(res.err_msg == "get_brand_wcpay_request:fail" ) {
	        	   window.location.href = "dsshome.html";
	           }
	       }
	   ); 
	}
}