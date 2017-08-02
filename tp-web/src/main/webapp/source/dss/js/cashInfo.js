var promoterType=xigou.getQueryString('type');
$(function() {
    // 获取用户信息
    var params = {
        token: xigou.getToken(),
        type:promoterType
    }

    xigou.promoterFunc.withdrawdetail({
        requestBody: params,
        callback: function(response, status) { //回调函数
            if (status == xigou.dictionary.success) {
                if (response.code == 0) {
                    $("#Amount").text(response.data.withdrawAmount);
                    // if(response.data.withdrawType == 2){
                    //     $("#zfb").show();
                    //     $("#bank").hide();
                    //     $("#alipayCode").text(response.data.withdrawBankAccount);
                    // }else{
                    //     $("#bankName").text(response.data.withdrawBank);
                    //     $("#bankCode").text(response.data.withdrawBankAccount);
                    // }

                    $("#withTime").text(response.data.withdrawTime);
                    $("#complete").on("click",function(){
                    	//绑定返回页面
                    	if(promoterType == 0 || promoterType == '0'){
                    		 window.location.href = "./dsscouponhome.html";
                    	}else if(promoterType == 1 || promoterType == '1'){
                    		 window.location.href = "./dsshome.html";
                    	}else if(promoterType == 2 || promoterType == '2'){
                    		 window.location.href = "./dssScancodeHome.html";
                    	}else{
                    		 window.location.href = "./home.html";
                    	}
                    })
                }
                else
                {
                    $.tips({
                        content:response.msg || "查询失败",
                        stayTime:2000,
                        type:"warn"
                    })
                }
            }
        }
    })
});

