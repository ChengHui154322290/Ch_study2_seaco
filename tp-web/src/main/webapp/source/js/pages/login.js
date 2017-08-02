// JavaScript Document
$(function() {
	login();
	initValiData();
	if(isWeiXin()){
    	$(".ui-header").hide();
    	$(".input_info_list").css({
    		"margin-top":"0"
    	})
    	$("title").html("登录");
    }
});

var backurl = xigou.getQueryString("backurl");
var buy_now_details_url = xigou.getSessionStorage('buy_now_details_url');
var details_url = xigou.getSessionStorage('details_url');
var fanli = xigou.getQueryString("fanli");
var _try = xigou.getQueryString("try");
var game = xigou.getQueryString("game");
var topicid = xigou.getQueryString("topicid");
var mtoapp = xigou.getQueryString("mtoapp");
if(fanli == "1") {
	$(".registr").attr("href","registr.html?fanli=1");
}

if(null != backurl){
	if(buy_now_details_url){
		$('.back').attr('href',"javascript:void(0)");
		$('.back')[xigou.events.click](function(){
			xigou.removeSessionStorage('buy_now_details_url');
			window.location.href=decodeURIComponent(buy_now_details_url);
		})
	}
	if(details_url){
		$('.back').attr('href',"javascript:void(0)");
		$('.back')[xigou.events.click](function(){
			xigou.removeSessionStorage('details_url');
			window.location.href=decodeURIComponent(details_url);
		})
	}
	else{
		$('.back').attr("href",decodeURIComponent(backurl));
	}
}
//登录
function login() {
	//获取页面高度，设置body高度，解决输入法弹出把logo推上来问题
	var bh=$("body").height(),
		wh=$(window).height();
		$(".logo").css("top",(Math.max(bh,wh))*0.92-42+"px");

	if (xigou.getLocalStorage("login_name")) {
		$("input.name").val(xigou.getLocalStorage("login_name"));
	}
	$('#loginBtnID')[xigou.events.click](function() {
        $("#loginBtnID").focus();
        if (!xigou.valiformdata.check(null, valiDataCallBack)) {
			return;
		}

		var params = {
			'token': '',
			// 'uuid':uuid,
			'loginname': $("input.name").val(),
			'pwd': $("input.pwd").val(),
		};

		if (isWeiXin()) {
			var openid = xigou.getLocalStorage("openid");
			if (openid) {
				params.uniontype = 1;
				params.unionval = openid;
			}
		}
		var dp = {
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					if (null == response) {
						$.tips({
							content:xigou.dictionary.chineseTips.server.value_is_null,
							stayTime:2000,
							type:"warn"
						});
					} else {
						var stauts = response.code;
						switch (stauts) {
							case "0":
								if (response.data.promoterinfo && response.data.promoterinfo != "{}") {
									var promoterinfo = 	JSON.parse(response.data.promoterinfo);
									promoterinfo.token = response.data.token;
									xigou.setLocalStorage("dssUser", promoterinfo, true);
								}

								var loginjump_url = xigou.getSessionStorage("loginjump_url");

								var back = {
									'token': response.data.token,
									'telephone': response.data.tel,
									'name': response.data.name
								};
								//实名认证信息清除
								xigou.removeSessionStorage('realname');
								xigou.removeSessionStorage('realnum');
								xigou.setSessionStorage("userinfo", back, true);
								xigou.setLocalStorage("login_name", $("input.name").val());
								xigou.setLocalStorage("show_name", response.data.tel || response.data.name);
								xigou.setLocalStorage("token", response.data.token);

								//提交数据的单独处理(添加购物车）
								if(xigou.getSessionStorage("refer_data")){
									params = xigou.getSessionStorage("refer_data",true);
									params.token = response.data.token;

									xigou.activeUser.addshopping({
										requestBody: params,
										callback: function(response, status) { //回调函数
											xigou.removeSessionStorage("refer_data");
											if (status == xigou.dictionary.success) {
											} else {
												$.tips({
													content:'添加购物车失败！',
													stayTime:2000,
													type:"warn"
												});
											}
											window.location.href = "cart.html";
										}
									});
									//立即购买
								}else if(xigou.getSessionStorage("buy_now_refer_data")){
									params = xigou.getSessionStorage("buy_now_refer_data",true);
									params.token = response.data.token;
									xigou.activeUser.buynow({
										requestBody: params,
										callback: function(response, status) { //回调函数
											var tuntype = xigou.getSessionStorage("buy_now_tun_type");
											xigou.removeSessionStorage("buy_now_refer_data");
											xigou.removeSessionStorage("buy_now_tun_type");
											if (status == xigou.dictionary.success) {
												var json = response || null;
												if (null == json || json.length == 0) return false;
													xigou.setSessionStorage("buy_now_uuid", json.data.uuid);
													window.location.href="settle.html";
											} else {
												//xigou.alert('请求失败，详见' + response);
												window.location.href=xigou.getSessionStorage("buy_now_details_url");
											}
										}
									});
								}
								else if(loginjump_url){
									xigou.setSessionStorage("loginjump_url","");
									window.location.href = decodeURIComponent(loginjump_url);
								}
								else {
									if(xigou.getSessionStorage("refer",false).indexOf('login.html')>-1 || xigou.getSessionStorage("refer",false).indexOf('logon.html')>-1){
										window.location.href = "home.html";
									}else{
										if(xigou.getSessionStorage("refer",false).indexOf("/wxhb")>-1||xigou.getSessionStorage("refer",false).indexOf("/wxhb.html")>-1){
											window.location.href = "home.html";
											return;
										}
										window.location.href = "index.html";
									}
								}

								break;
							case "-99":
								var back = {
									'token': response.data.token,
									'telephone': response.data.tel,
									'name': response.data.name
								};
								xigou.setSessionStorage("userinfo", back, true);
								xigou.setLocalStorage("login_name", $("input#name").val());
								xigou.setLocalStorage("token", response.token);
								window.location.href = "binding.html";
								break;
							default:
								$('input#name').unbind("change");

								$.tips({
							        content:response.msg||"登录失败",
							        stayTime:2000,
							        type:"warn"
							    });

								// xigou.toast(response.msg||"登录失败");
								// if(GeeTest){
								// 	GeeTest[0].refresh();
								// }

								break;
						}
					}
				} else {
					$.tips({
						content:'请求失败，详见' + response,
						stayTime:2000,
						type:"warn"
					});
				}
			}
		};
		xigou.activeUser.logon(dp);
	});
	$('#registerBtnClass')[xigou.events.click](function(){
		 window.location.href="register.html"; 
	});
};

function valiDataCallBack(message, id, mname) {
	$.tips({
		content:message,
		stayTime:2000,
		type:"warn"
	});

	$("#" + id).focus();
	/*
    xigou.alert({
    	message:message,
    	callback:function(){
    		$("#"+id).focus();
    	}
    });*/
};

function initValiData() {
	var _form = {
		methods: {
			"name": {
				required: "required",
				emailormobile:"emailormobile"
			},

		},
		errors: {
			"name": {
				required: "请输入用户账号",
				emailormobile: "请填写正确的手机号或者邮箱地址"
				
			},
		}
	};
	//账号消除
    $('#name').focus(function(e) {
        var a = $(this).val();
        if (a && a.length > 0) {
            $('.div_clear_input1').show();
        }
        else {
            $('.div_clear_input1').hide();
        }
    });
	$('#name').blur(function(e) {
		$('.div_clear_input1').hide();
	});
    $('.div_clear_input1')[xigou.events.click](function() {
        $('.div_clear_input1').hide();
        $('#name')[0].value = "";
    })
    
    
    //密码消除
    $('#pwd').focus(function(e) {
        var a = $(this).val();
        if (a && a.length > 0) {
            $('.div_clear_input2').show();
        }
        else {
            $('.div_clear_input2').hide();
        }
    });
	$('#pwd').blur(function(e) {
		$('.div_clear_input2').hide();
	});
    $('.div_clear_input1')[xigou.events.click](function() {
        $('.div_clear_input2').hide();
        $('#pwd')[0].value = "";
    })
	return xigou.valiformdata.initValiData(_form);
};

function getopenid(){
	var _code = xigou.getSessionStorage("weixin_code");
	xigou.activeUser.openid({
		requestBody: {code:_code},
		callback: function(response, status) {
			xigou.setLocalStorage("openid",response.data.openid);
			window.location.href = window.location.href;
		} 
	});
}

var ua = window.navigator.userAgent.toLowerCase(); 
if(ua.match(/MicroMessenger/i) == 'micromessenger') 
{
	var openid = xigou.getLocalStorage("openid");
	if(!openid) {
		xigou.getwxOpenid(1, true);
	}
	else {
		// 微信自动联合登录
		var params = {
			uniontype: '1',
			unionval: openid,
//	        signature:'1a0708f6e3fdc5ebb25c89a4e0765fdc', //wx1.0.1
	        signature:'af288db869b27f21cc1440385d63d4d5', //wx1.5.0
			curtime: (new Date()).valueOf().toString()
		};

		var dssUser = xigou.getLocalStorage("dssUser");
		if (dssUser && dssUser != "{}") {
			dssUser = JSON.parse(dssUser);
			if (dssUser && dssUser.mobile) {
				params.shopMobile = dssUser.mobile;
			}
		}

		xigou.activeUser.unionlogon({
			requestBody: params,
			callback: function(response, status) { // 回调函数
				if (status == xigou.dictionary.success) {
					if (null == response) {
						return;
					} else {
						var stauts = response.code;
						switch (stauts) {
							case "0":
								if (response.data.promoterinfo && response.data.promoterinfo != "{}") {
									var promoterinfo = 	JSON.parse(response.data.promoterinfo);
									promoterinfo.token = response.data.token;
									xigou.setLocalStorage("dssUser", promoterinfo, true);
								}

								var loginjump_url = xigou.getSessionStorage("loginjump_url");

								var back = {
									'token': response.data.token,
									'telephone': response.data.tel,
									'name': response.data.name
								};
								//实名认证信息清除
								xigou.removeSessionStorage('realname');
								xigou.removeSessionStorage('realnum');
								xigou.setSessionStorage("userinfo", back, true);
								xigou.setLocalStorage("show_name", response.data.tel || response.data.name);
								xigou.setLocalStorage("token", response.data.token);

								//提交数据的单独处理(添加购物车）
								if(xigou.getSessionStorage("refer_data")){
									params = xigou.getSessionStorage("refer_data",true);
									params.token = response.data.token;

									xigou.activeUser.addshopping({
										requestBody: params,
										callback: function(response, status) { //回调函数
											xigou.removeSessionStorage("refer_data");
											if (status == xigou.dictionary.success) {
											} else {
												$.tips({
													content:'添加购物车失败！',
													stayTime:2000,
													type:"warn"
												});
											}
											setTimeout(function(){
												window.location.href = "cart.html";
											}, 250);
										}
									});
									//立即购买
								}else if(xigou.getSessionStorage("buy_now_refer_data")){
									params = xigou.getSessionStorage("buy_now_refer_data",true);
									params.token = response.data.token;
									xigou.activeUser.buynow({
										requestBody: params,
										callback: function(response, status) { //回调函数
											var tuntype = xigou.getSessionStorage("buy_now_tun_type");
											xigou.removeSessionStorage("buy_now_refer_data");
											xigou.removeSessionStorage("buy_now_tun_type");
											if (status == xigou.dictionary.success) {
												var json = response || null;
												if (null == json || json.length == 0) return false;
												xigou.setSessionStorage("buy_now_uuid", json.data.uuid);
												setTimeout(function(){
													window.location.href="settle.html";
												}, 250);
											} else {
												//xigou.alert('请求失败，详见' + response);
												setTimeout(function(){
													window.location.href=xigou.getSessionStorage("buy_now_details_url");
												}, 250);
											}
										}
									});
								}
								else if(loginjump_url){
									xigou.setSessionStorage("loginjump_url","");
									setTimeout(function(){
										window.location.href = decodeURIComponent(loginjump_url);
									}, 250);
								}
								else {
									if(xigou.getSessionStorage("refer",false).indexOf('login.html')>-1 || xigou.getSessionStorage("refer",false).indexOf('logon.html')>-1){
										setTimeout(function(){
											window.location.href = "home.html";
										}, 250);
									}else{
										if(xigou.getSessionStorage("refer",false).indexOf("/wxhb")>-1||xigou.getSessionStorage("refer",false).indexOf("/wxhb.html")>-1){
											setTimeout(function(){
												window.location.href = "home.html";
											}, 250);
										}
										else {
											setTimeout(function(){
												window.location.href = "index.html";
											}, 250);
										}
									}
								}

								break;
							default:
								return;
						}
					}
				}
			}
		});
	}
}