/*
* @Author: d
* @Date:   2017-05-09 10:21:19
* @Last Modified by:   dongqian
* @Last Modified time: 2017-05-15 10:23:28
*/

'use strict';
$(function (){
    if(isWeiXin()){
        $('header').hide();
        $('title').html('推广统计')
    }
    var page =1;
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status == xigou.dictionary.success){
                loadDssAccount();
                OrdersJuery(page);
                queryChildPromoters(page)
            }else{
                window.location.href = "../logon.html";
            }
        }
    });
    $(".share_button").on("click",function(){
        if(null!=xigou.getLocalStorage("login_name") && ''!=xigou.getLocalStorage("login_name")){
            window.location.href="./dssShareStatistical.html?i="+xigou.getLocalStorage("login_name");
        }else{
          var shopMobile="";
          var userinfo =xigou.getSessionStorage("userinfo");
          var dssUserInfo = eval("(" + userinfo + ")");
          window.location.href="./dssShareStatistical.html?i="+dssUserInfo.telephone;
        }
    })
})
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
                         $(".cord").html("<img width='168px' style='margin:5px;' src='data:image/png;base64,"+json.data.img+"'/>" );
                         // $('.open_time').find('span').html(json.data.passtime)
                    }
                }
            }
        });
};
function OrdersJuery(page){
    var token = xigou.getToken();
        var params = {
            'token' : token,
            'curpage' : page,
            'type' : '2'
        };
        xigou.promoterFunc.queryOrders({
            requestBody : params,
            callback : function(response, status) { // 回调函数
            if (status == xigou.dictionary.success) {
                if (null == response) {
                    $.tips({
                        content : xigou.dictionary.chineseTips.server.value_is_null,
                        stayTime : 2000,
                        type : "warn"
                    });
                } else {
                    var code = response.code;
                    if(0 == code){
                        if(response.data.totalcount && response.data.totalcount > 0){
                            $('.totalcount_order').html(response.data.totalcount);
                        }
                    }else{
                        $(".dropload-down").hide();
                        $.tips({
                            content:"查询失败，请稍后重试！",
                            stayTime:2000,
                            type:"warn"
                         });
                        return;
                    }
                }
            }
        }
    });
};
function queryChildPromoters(page){
    var token = xigou.getToken();
    var params = {
            'token': token,
            'type': '2',
            'curpage':page,
            'pagesize':100//每次取回的数据条数
        };
    xigou.promoterFunc.queryChildPromoters({
        requestBody: params,
        callback: function(response, status) { //回调函数
            if (status == xigou.dictionary.success) {
                var json = response || null;
                if (null == json || json.length == 0)return false;
                if (json.code == 0) {
                    if(response.data.totalcount && response.data.totalcount > 0){
                        $('.totalcount_reg').html(response.data.totalcount);
                    }
                }else{
                    $(".dropload-down").hide();
                    $.tips({
                        content:"查询失败，请稍后重试！",
                        stayTime:2000,
                        type:"warn"
                    });
                    return;
                }
            }
         }
    });
}