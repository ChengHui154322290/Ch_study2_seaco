/**
 *
 * @title   JavaScript Document
 * @authors Your Name (you@example.org)
 * @date    2015-02-14 13:41:54
 * @version Ver 1.0.0
 */
'use strict';
var min_date = new Date(Date.parse('2016/10/01'));
var type=xigou.getQueryString("type");
if(type == null){
    window.location.href="../logon.html";
}
$(function() {
    $('.order-ask').hide();
    var page = 1;
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status==xigou.dictionary.success){
                $('.orders-list').dropload({
                    scrollArea : window,
                    loadDownFn : function(me){
                        OrdersJuery(page,me);
                        me.resetload();
                        if(1 == page){
                            $(".dropload-refresh").hide();
                        }
                        page++;
                    }
                });
            }else{
                window.location.href="index.html";
            }
        }
    });


});

var allorder_max = "N";
function OrdersJuery(page, me){
    if(allorder_max == "Y"){
        me.disable("down",false);
        $('.ui-refresh-down>span:first-child').removeClass();
        return false;
    }
    var token = xigou.getToken();
    var createBeginTime = xigou.getSessionStorage('beginquerydate');
    var createEndTime = xigou.getSessionStorage('endquerydate');
    if(allorder_max != "Y"){
        var params = {
            'token' : token,
            'curpage' : page,
            'type' : type,
            'createBeginTime':createBeginTime,
            'createEndTime':createEndTime
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
                        setAllOrderHtmlView(response, page);
                        if(response.data.curpage >= response.data.totalpagecount){
                            me.lock();
                            $(".dropload-down").hide();
                        }
                        if(response.data.totalMoney&&response.data.totalMoney>=0){
                            $('.num_price').html(response.data.totalMoney);
                        }
                        if(response.data.totalcount&&response.data.totalcount>=0){
                            $('.num1').html(response.data.totalcount);
                        }

                    }else{
                        me.lock();
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
}
};


function setAllOrderHtmlView(json, page) {
    var o = json,
    length = o.data.list.length,   //订单总数量
    allpage = o.allpage,   //总页数
    count = o.count,       //当前页数量
    alllist = o.data.list,   // 全部订单
    flgs = false,
    html = [];

    //DOM
    for(var i=0; i<length; i++){
        html.push('<div class="orders-item" ordercode="' + alllist[i].ordercode + '">');
        html.push(' <div class="order_time_status">' + alllist[i].ordertime);
        if (!alllist[i].statusdesc) {
            alllist[i].statusdesc = " ";
        }
        html.push('     <span class="order_status">' + alllist[i].statusdesc + '</span></div>');
        for (var j = 0; j < alllist[i].lines.length; j++) {
            var item = alllist[i].lines[j];
            html.push('<div class="order_info" sku="' + item.sku + '" tid = ' + item.tid + '>');
            html.push(' <div class="order_image">');
            html.push('     <img src="' + item.imgurl + '">');
            html.push(' </div>');
            html.push(' <div class="oder_info2">');
            html.push('     <div class="order_name_price">');
            html.push('         <div class="order_price">¥' + item.price + '</div>');
            html.push('         <div class="order_name">' + item.name + '</div>');
            html.push('     </div>');
            html.push('     <div class="order_size_count">');
            html.push('         <div class="order_count">×' + item.count + '</div>');
            // html.push('         <br/><div class="bottom_income_value order_count">+' + item.commision + '</div>');
            if (item.specs.length > 0) {
                html.push('         <div>' + item.specs[0] + '</div>');
            }
            html.push('     </div>');
            html.push(' </div>');
            html.push('</div>');
        };
        html.push(' <div class="order_totle_count_price">');
        html.push('     <span class="float_right ">共 ' + alllist[i].linecount + ' 件 实付：<span class="order_lineprice">¥' + alllist[i].orderprice + '</span></span>');
        html.push(' </div>');

        // html.push('<div class="order_incomes">');
        // html.push('<div class="bottom_income">')
        // html.push('<span class="bottom_income_text">收入：</span>');
        // html.push('<span class="bottom_income_value">+' + alllist[i].commision + '</span>');
        // html.push('</div>');
        // html.push('</div>');
        html.push('</div>');
    };

    if(json.data.pagesize == "" || json.data.pagesize == 0 || json.data.pagesize == '0' || json.data.curpage == json.data.totalcount) {
        allorder_max = "Y";
    }

    if (page == 1 && html.length == 0) {
        $(".orders-list").empty();
        var htmlData = [];
        htmlData.push('<div class="order_empty"><div class="empty-tip">还木有订单哟~</div></div>');
        $('.orders-list').append(htmlData.join(''));
    } else {
        $('.orders-list').append(html.join(''));
    }
}
