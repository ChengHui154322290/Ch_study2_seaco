$(function () {
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status == xigou.dictionary.success){
				loadDssAccount();
			}else{
				window.location.href = "../logon.html";
			}
		}
	});	
	$("#withdrawingBtn")[xigou.events.click](function(){
		window.location.href='cashInfo.html?type=2';
	});
});


function loadDssAccount(){
	var token = xigou.getToken();
	var params = {
			'token': token,
			'type': '2'
		};
		xigou.promoterFunc.loadDssAccount({
			requestBody: params,
			callback: function(response, status) { //回调函数
				if (status == xigou.dictionary.success) {
					var json = response || null;
					if (null == json || json.length == 0)return false;
					if (json.code == 0) {
						InitCouponAccountPage(json);
					}
				}
			}
		});		
};

function InitCouponAccountPage(json) {		
	var status = json.data.status;
	var withdrawStatus = json.data.withdrawstatus;
	if(null == status || status > 3){ //数据异常
		return;
	}
	//状态栏和提现按钮显示状态
	statusDivShow(status);
	withdrawButtionShow(status);
	if(2 == status){ //已开通
		if(null != withdrawStatus && 1 == withdrawStatus){	 //提现中
			$("#canWithdrawBtn").hide();			
			$("#withdrawingBtn").show();
			$("#withdrawingAmount").text(json.data.withdrawingamount);
		}		
	}
	
	$("#userName").text(json.data.name);
	$("#mobile").text(json.data.mobile);
	$("#fuserName").text(json.data.name);
	$("#fmobile").text(json.data.mobile);
	
	//总收入
	$("#accumulatedAmount").text(json.data.totalfees);
	//已提现
	$("#withdrawedAmount").text(json.data.withdrawedfees);	
	//未提现
	$("#surplusAmount").text(json.data.surplusamount);
	//订单返现
	$("#orderAmount").text(json.data.orderamount);
	//扫码总数
	$("#scancount").text(json.data.scancount);
	////已使用个数
	//$("#usedCount").text(json.data.cpusedcount);
	////已兑换
	//$("#exchangedCount").text(json.data.cpexchangedcount);
	////未兑换
	//$("#unexchangeCount").text(json.data.cpunexchangecount);
	//身份证号
	$("#credentialcode").val(json.data.credentialcode);
	////银行名称
	//$("#bankName").val(json.data.bank);
	////银行账号
	//$("#bankAccount").val(json.data.bankaccount);
	//提现金额
	$("#curWithdraw").val(json.data.surplusamount);
	//支付宝账号
	$("#Alipay").val(json.data.alipay);
	
	//账户详情
	accountDetail();
	//账单详情
	billinfo();
	//订单详情
	orderinfo();
	//提现请求
	withdraw();
};

function accountDetail(){
	$('.main-info').click(function(e){
		window.location.href='scancodeDetail.html?type=2';
	});
}

function billinfo(){
	$("#accountFlowQuery")[xigou.events.click](function(){
		window.location.href='billinfo.html?type=2';
	});
}

function orderinfo(){
	$("#orderQuery")[xigou.events.click](function(){
		window.location.href='dssorders.html?type=2';
	});
}

function withdraw(){
	$("#canWithdrawBtn")[xigou.events.click](function(){
		window.location.href='withdrawals.html?type=2';
//		var surplusAmount=$("#surplusAmount").html();
//		if(null == surplusAmount){
//			return;
//		}
//		var amount = parseFloat(surplusAmount);
//		if(amount < 0.001){
//			$.tips({
//				content:"余额不足",
//                stayTime:2000,
//                type:"warn"
//             });
//			return;
//		}
//		var dia=$.dialog({
//            title:'',
//            content:"您确认提现¥"+surplusAmount+"吗?",
//            button:["确认","取消"]
//        });
//		
//        dia.on("dialog:action",function(e){
//        	if(e.index == 0)
//        	{
//        		var dssPromoterId = xigou.getLocalStorage("dss_dealer_promoter_id");
//        		if(null == dssPromoterId){
//        			window.location.href = "../logon.html";
//        		}
//        		var token = xigou.getToken();
//				var params = {
//					token:token,
//					type: 2,
//					promoterName:$("#userName").text(),
//					credentialType:"1",
//					credentialCode:$("#credentialcode").val(),
//					//bankName:$("#bankName").val(),
//					//bankAccount:$("#bankAccount").val(),
//					bankAccount:$("#Alipay").val(),
//					curWithdraw:$("#curWithdraw").val()
//				};
//
//				xigou.promoterFunc.dsswithdraw({
//					requestBody: params,
//					callback: function(response, status) { //回调函数
//						if (status == xigou.dictionary.success) {
//							if (response.code == 0) {
//								$.tips({
//				                    content:"提现请求成功,等待客服审核" || response.msg,
//				                    stayTime:2000,
//				                    type:"warn"
//				                 })
//								window.location.href = "dssScancodeHome.html";
//							}
//							else
//							{
//								$.tips({
//				                    content:response.msg || "提现请求失败",
//				                    stayTime:2000,
//				                    type:"warn"
//				                 })
//							}
//						}
//					}
//				});
//        	}
//        });  
	});
}

function statusDivShow(status){
	if(0 == status){
		$("#normalStatusDiv").hide();
		$("#unpayStatusDiv").show();
		$("#forbiddenStatusDiv").hide();
	}else if(1 == status){
		$("#normalStatusDiv").show();
		$("#unpayStatusDiv").hide();
		$("#forbiddenStatusDiv").hide();
		$("#normalStatusDiv .wrz").show();
	}else if(2 == status){
		$("#normalStatusDiv").show();
		$("#unpayStatusDiv").hide();
		$("#forbiddenStatusDiv").hide();
		$("#normalStatusDiv .yrz").show();
	}else{
		$("#normalStatusDiv").hide();
		$("#unpayStatusDiv").hide();
		$("#forbiddenStatusDiv").show();
	}
}

function withdrawButtionShow(status){
	if(2 == status){ //已开通
		$("#withdrawDisableBtn").hide();
		$("#canWithdrawBtn").show();			
		$("#withdrawingBtn").hide();
	}else{				//被禁用
		$("#withdrawingBtn").hide();
		$("#withdrawDisableBtn").show();
		$("#canWithdrawBtn").hide();
	}
}