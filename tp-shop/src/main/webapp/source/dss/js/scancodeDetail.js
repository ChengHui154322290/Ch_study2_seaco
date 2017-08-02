var userMobile;
var type=xigou.getQueryString("type");
if(type == null)
{
	window.location.href="../logon.html";
}

$(function () {
	if(type == 2 || type == '2'){
		$(".goBack").attr("href", "dssScancodeHome.html");
	}else if(type == 1 || type == '1'){
		$(".goBack").attr("href", "dsshome.html");
	}else{
		$(".goBack").attr("href", "../home.html");
	}
	$(".share_scan_button").on("click",function(){
	  var shopMobile="";
	  var userinfo =xigou.getSessionStorage("userinfo");
	  var dssUserInfo = eval("(" + userinfo + ")");
	  window.location.href="./dssScanShareShop.html?i="+dssUserInfo.telephone;
	})
	
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status == xigou.dictionary.success){
				loadDssAccount();
			}else{
				window.location.href = "../logon.html";
			}
		}
	});	
});

function loadDssAccount(){
	var token = xigou.getToken();
	var params = {
		'type' : type,
		'token' : token
	};
	xigou.promoterFunc.loadDssAccount({
		requestBody : params,
		callback : function(response, status) { // 回调函数
			if (status == xigou.dictionary.success) {
				var json = response || null;
				if (null == json || json.length == 0)
					return false;
				if (json.code == 0) {
					InitAccountDetailPage(json);
				}
			}
		}
	});	
};

function InitAccountDetailPage(json){		
	//开通时间
	setValue('#passTime', json.data.passtime);
	setValue('#nickName', json.data.nickname);
	setValue('#weixin', json.data.weixin);
	setValue('#qq', json.data.qq);
	setValue('#mobile', json.data.mobile);
	setValue('#email', json.data.email);
	setValue('#name', json.data.name);
	setValue('#credential', json.data.credential);
	setValue('#credentialCode', json.data.credentialcode);
	setValue('#bank', json.data.bank);
	setValue('#bankAccount', json.data.bankaccount);
	setValue('#alipay', json.data.alipay);
	$(".div_dsslogo").html("<img width='180' style='margin:15px 10px 10px 10px;' src='data:image/png;base64,"+json.data.img+"'/>" );
	//	$(".div_dsslogo").html("<img width='180' style='margin:15px 10px 10px 10px;' src='"+json.data.img+"'/>");
}

function setValue(id, value){
	value = value || ' ';
	$(id).text(value);
}