$(function () {
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status != xigou.dictionary.success){
				xigou.setSessionStorage("loginjump_url", "home.html");
				window.location.href = "logon.html";
			}else{
				// 判定是否需要重新拉取用户微信信息
				/*if (isWeiXin()){
					if (!xigou.getLocalStorage('wxnickname') && !xigou.getLocalStorage('wxImage') && !xigou.getSessionStorage('getediconnickname')) {
						xigou.getwxOpenid(1, true);
					}
				}*/
				initUserInfo();
				dssLogin();
				/*if(isWeiXin()){
					$("#bindTel").show();
				}*/
			}
		}
	});
	/*window.onscroll = function() {
		var top = document.documentElement.scrollTop || document.body.scrollTop;
		if(top>10){
			$(".home-name").addClass("fixed")
		}else if(top<30){
			$(".home-name").removeClass("fixed")
		}
	}*/

});

function initUserInfo(){
	xigou.activeUser.initCount({
		requestBody:{
			'eshopPromoterId':xigou.getSessionStorage('promoterid'),
			token: xigou.getToken()
		},
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				if (null == response) {
					$.tips({
				        content:xigou.dictionary.chineseTips.server.value_is_null,
				        stayTime:2000,
				        type:"warn"
				    })
				}
				else if("0" ==response.code)
				{	
					//待付款的数量
					if(response.data.unpaycount > 0)
					{
						$(".home-unpay").html(response.data.unpaycount);
						$(".home-unpay").show();
					}
					else {
						$(".home-unpay").hide();
					}
					//是否使用第三方商城积分
					if(response.data.usedThirdShopPoint==true){
						$("#jinbi").show();
						$("#points").text(Math.round(response.data.points)+"个");
					}else{
						$("#xgb").show();
					}
					//待收货的数量
					if(response.data.unreceiptcount > 0)
					{
						$(".home-unrecv").html(response.data.unreceiptcount);
						$(".home-unrecv").show();
					}
					else {
						$(".home-unrecv").hide();
					}
					//待使用的数量
					if(response.data.unusecount > 0){
						$(".home-unuse").html(response.data.unusecount);
						$(".home-unuse").show();
					}else{
						$(".home-unuse").hide();
					}
					
					if (response.data.couponcount > 0) {
						$('.coupon_count').html(response.data.couponcount + '张');
					}
					var eshopName = '[' +  xigou.getSessionStorage("eshopName")  + ']';
					var name = eshopName + (xigou.getLocalStorage("show_name")|| xigou.getLocalStorage("login_name") || "会员");
					var nickName = xigou.getLocalStorage('wxnickname') || xigou.getLocalStorage("show_name")|| xigou.getLocalStorage("login_name") || "会员";
					if (name) {
						var oScript= document.createElement("script");
						oScript.type = "text/javascript";
						oScript.src='https://qiyukf.com/script/6e39dddabff63d902f55df3961c2793d.js?name=' + name + '&mobile=' + name;
						$('body')[0].appendChild(oScript);
					}
					if (nickName) {
						if (isWeiXin()){
							$("title")[0].innerHTML = nickName;
							$(".home-name").hide();
							$(".home-name").css("display","none");
							$(".div_icon div").css("top","17px");
							$(".home-bg").css("height","100px");
							$(".home-top").css("height","100px");
						}else{
							$(".home-name")[0].innerHTML = nickName;
						}
						
					}
					var imgIcon = xigou.getLocalStorage('wxImage');
					if (imgIcon) {
						$('div.div_icon_bg img').attr('onerror',"this.onerror=null;this.src='shop/"+xigou.channelcode+"_icon.png';xigou.removelocalStorage('wxImage');");
						$('div.div_icon_bg img').attr('src', imgIcon);
					}else if(xigou.channelcode){
						$('div.div_icon_bg img').attr('src', 'shop/'+xigou.channelcode+'_icon.png');
					}
				}
				else if("-100" ==response.code)
				{
					window.location.href = "logon.html";
				}
			//	dssLoginInit();
			}
		}
	});
};

function dssLogin(){
	var token = xigou.getToken();
	var params = {
		'token': token
	};
	xigou.promoterFunc.dssLogin({
		requestBody: params,
		callback: function(response, status) {
			if (status == xigou.dictionary.success) {
				var homeTopHeight = $(".home-top").height();
				if (response != null && response.code == 0) {
					var json = response;
					
					if(response.data.isshopadmin == 1){
						addIndexManage();
					}else{
						//是否开通分销
						var dssType = xigou.getSessionStorage("companyDssType");
						if(dssType == 1){ //该站点开通店铺
							setDssInfo(response);
						}	
					}
					var dssUser = xigou.getSessionStorage("dssUser");
					var nickname="";
					var shopmobile="" ;
					if(dssUser!=""){
						 nickname = JSON.parse(dssUser).nickname;
						 shopmobile = JSON.parse(dssUser).shopmobile;
					}
					if(xigou.getSessionStorage("userinfo")){
						var userinfo = xigou.getSessionStorage("userinfo");
						var telephone = JSON.parse(userinfo).telephone;
						if (isWeiXin()){
							if(shopmobile == telephone){
								var homename = nickname || $("title")[0].innerHTML;
								$("title")[0].innerHTML = homename;
							}else{
								var homename = telephone || $("title")[0].innerHTML;
								$("title")[0].innerHTML = homename;
							}
						}else{
							if(shopmobile == telephone){
								var homename = nickname || $(".home-name")[0].innerHTML;
								$(".home-name")[0].innerHTML = homename;
							}else{
								var homename = telephone || $(".home-name")[0].innerHTML;
								$(".home-name")[0].innerHTML = homename;
							}
						}
					}else{
						if (isWeiXin()){
							var homename = telephone || $("title")[0].innerHTML;
							$("title")[0].innerHTML = homename;
						}else{
							var homename = telephone || $(".home-name")[0].innerHTML;
							$(".home-name")[0].innerHTML = homename;
						}
					}
					xigou.setLocalStorage("show_name", homename);
				}
			}
//			bindState();
		}
	});
}

function bindState(){
	var params = {
		'token': xigou.getToken()
	};
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
					if(stauts == 0) {
							if(!response.data.tel){
								$("#bdState").show();
							}else{
								$("#bindTel a").attr("href","bindtel.html?mobile="+response.data.tel);
							}
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
}

function addIndexManage(){
	var htmlData=[];
	htmlData.push('<li class="hotcommodityshop">');
	htmlData.push('<a href="dss/hotcommodity.html?type=3">');
	htmlData.push('<span class="icon-lists hotitem">我的首页管理</span>');
	htmlData.push('<span class="ui-list-text">&nbsp;</span>');
	htmlData.push('</a></li>');
	$('.coupontab').before(htmlData.join(''));
}

function setDssInfo(response) {
	var json = response;
	var isShop = (null != json.data.isshopdss && 1 == json.data.isshopdss);
	var isCoupon = (null != json.data.iscoupondss && 1 == json.data.iscoupondss);
	var isScanCode = (null != json.data.isscandss && 1 == json.data.isscandss);
//	var channelCode = xigou.getSessionStorage('userinfo');
	var channelCode = xigou.channelcode;
	var htmlData = "", N = 0;
	if (isShop) {
		if(channelCode.toLowerCase()!='hhb'){
			htmlData = '<div class="myDss"><i></i><div>我的店铺</div></div>';
			N++;
		}
	}

	$(".div_dss_info ").append(htmlData);
	if (N > 1) {
		$(".div_dss_info").removeClass("show_arr");
		$(".div_dss_info > div > div").eq(0).addClass("bdr");
		if (N > 2) {
			$(".div_dss_info > div >div").eq(1).addClass("bdr");
		}
	}
	$('.myDss')[xigou.events.click](function () {
		setTimeout(function () {
			window.location.href = 'dss/dsshome.html';
		}, 250);
	});
}
function dssLoginInit(){
	var token = xigou.getToken();
	var params = {
		'token': token
	};
	xigou.promoterFunc.dssLogin({
		requestBody: params,
		callback: function(response, status) {
			if (status == xigou.dictionary.success) {
				var homeTopHeight = $(".home-top").height();
				if (response != null && response.code == 0) {
					setDssInfo(response);

				}
				else{
					//$('.home-bg').css('margin-top', homeTopHeight);
				}
			}
			bindState();
		}
	});
}
