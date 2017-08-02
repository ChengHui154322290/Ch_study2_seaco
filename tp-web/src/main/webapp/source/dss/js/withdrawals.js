var promoterType=xigou.getQueryString('type');
$(function() {
	//分销类型
    // 获取用户信息
    var params = {
        token: xigou.getToken(),
        type:promoterType
    }
    //绑定返回页面
//	if(promoterType == 0 || promoterType == '0'){
//		$(".goBack").attr("href", "dsscouponhome.html");
//	}else if(promoterType == 1 || promoterType == '1'){
//		$(".goBack").attr("href", "dssScancodeHome.html");
//	}else if(promoterType == 2 || promoterType == '2'){
//		$(".goBack").attr("href", "dsshome.html");
//	}else{
//		$(".goBack").attr("href", "../home.html");
//	}
    if(isWeiXin()){
        $('.ui-header').hide();
        $("title").html("提现");
        $(".withdrawals-info").css({
            "margin-top":"0"
        })
    }
    xigou.promoterFunc.loadDssAccount({
        requestBody: params,
        callback: function(response, status) { //回调函数
            if (status == xigou.dictionary.success) {
                var json = response || null;
                if (null == json )return false;
                if (json.code == 0) {
                    InitPage(json.data);
                }
            }
        }
    })
    // selectWay();

});

function InitPage(data) {
    // 初始化页面信息
    // if (data.name) {    // 持卡人
    //     $('#input-telnum')[0].value = data.name;
    //     $('#input-telnum').attr('disabled','disabled');
    // }
    // if (data.credentialcode) {  // 身份证号
    //     $('#input-credential-code-num')[0].value = data.credentialcode;
    //     $('#input-credential-code-num').attr('disabled','disabled');
    // }
    // if (data.bank) {    // 到账银行
    //     $('#input-bank')[0].value = data.bank;
    //     $('#input-bank').attr('disabled','disabled');
    // }
    // if (data.bankaccount) { // 银行卡号
    //     $('#input-bank-no')[0].value = data.bankaccount;
    //     $('#input-bank-no').attr('disabled','disabled');
    // }
    if (data.surplusamount) {
        $('.item-block-usable div.label').html('可用提现金额¥' + data.surplusamount);
    }
    // if (data.alipay) {
    //     $('#input-alipay')[0].value = data.alipay;
    //     $('#input-alipay').attr('disabled','disabled');
    // }
    //var withdrawalsParam = xigou.getSessionStorage('withdrawalsParam', true);
    //if (withdrawalsParam) {
    //    InitWithdrawals(withdrawalsParam);
    //}

    //if(data.surplusamount<=0){
    //    return;
    //}else{
        // withdrawals();
    //}
    $('.btn_withdrawals').click(function () {
        var curWithdraw = $('#input-withdrawals-amount')[0].value;
        if(data.surplusamount >= 100 && curWithdraw >=100){
            withdrawals(curWithdraw);
        }else{
            $.tips({
                content: "可用提现金额或提现金额不足100元",
                stayTime:2000,
                type:"warn"
            })
        };
    })
}

//function InitWithdrawals(withdrawalsParam) {
    // if (withdrawalsParam.payway == 'alipay') {
    //     $('.bank').hide();
    //     $('.alipay').show();
    //     $('#input-way').html('支付宝');
    // }
    // else if (withdrawalsParam.payway == 'unionPay') {
    //     $('.alipay').hide();
    //     $('.bank').show();
    //     $('#input-way').html('银行卡（储蓄卡）');
    // }
    // $("#input-telnum").val(withdrawalsParam.name);
    // $("#input-credential-code-num").val(withdrawalsParam.credentialcode);
    // $("#input-way").attr('type', withdrawalsParam.payway);
    // $("#input-alipay").val(withdrawalsParam.alipayno);
    // $("#input-bank").val(withdrawalsParam.bank);
    // $("#input-bank-no").val(withdrawalsParam.bankNo);
    // $("#input-withdrawals-amount").val(withdrawalsParam.withdrawalsAmount);
//}

function withdrawals(curWithdraw) {
    // $('.btn_withdrawals')[xigou.events.click](function(){
        // var params = {
        //     token: xigou.getToken(),
        //     type: promoterType,//提现账户类型
        //     promoterName:$('#input-telnum')[0].value,
        //     credentialType:'1',
        //     credentialCode:$('#input-credential-code-num')[0].value,
        //     curWithdraw:$('#input-withdrawals-amount')[0].value
        // };
        var params = {
            'token': xigou.getToken(),
            'curWithdraw':curWithdraw,
            'type': promoterType//提现账户类型
        };

        // if ($('#input-way').attr('data-type') == 'alipay') {
        //     // 支付宝
        //     params.bankAccount = $('#input-alipay')[0].value;
        //     params.withdrawType = '2';
        // }
        // else {
        //     params.bankName = $('#input-bank')[0].value;
        //     params.bankAccount = $('#input-bank-no')[0].value;
        //     params.withdrawType = '1';
        // }

        xigou.promoterFunc.dsswithdraw({
            requestBody: params,
            callback: function(response, status) { //回调函数
                if (status == xigou.dictionary.success) {
                    if (response.code == 0) {
                        $.tips({
                            content:"提现请求成功,等待客服审核" || response.msg,
                            stayTime:2000,
                            type:"warn"
                        })
                        window.location.href = "cashInfo.html?type="+promoterType;
                    }
                    else
                    {
                        $.tips({
                            content:response.msg || "提现请求失败",
                            stayTime:2000,
                            type:"warn"
                        })
                    }
                }
            }
        });
    // })
}

// function selectWay(){
    //$('.arrow')[xigou.events.click](function() {
    //    var withdrawalsParam = {
    //        name: $("#input-telnum").val(),
    //        credentialcode: $("#input-credential-code-num").val(),
    //        payway:$("#input-way").attr('type'),
    //        alipayno:$("#input-alipay").val(),
    //        bank:$("#input-bank").val(),
    //        bankNo:$("#input-bank-no").val(),
    //        withdrawalsAmount: $('#input-withdrawals-amount').val()
    //    }
    //    xigou.setSessionStorage('withdrawalsParam', withdrawalsParam, true);
    //    setTimeout(window.location.href = 'withdrawalsway.html', 250);
    //})
// }