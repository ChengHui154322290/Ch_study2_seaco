/*
* @Author: dongqian
* @Date:   2017-05-09 17:16:39
* @Last Modified by:   dongqian
* @Last Modified time: 2017-05-11 11:21:45
*/

'use strict';
var type=xigou.getQueryString("type");
if(type == null)
{
    window.location.href="../logon.html";
}

$(function() {
    var page = 1;
    // if(type == 0 || type == '0'){
    //     $(".goBack").attr("href", "dsscouponhome.html");
    // }else if(type == 1 || type == '1'){
    //     $(".goBack").attr("href", "dsshome.html");
    // }else{
    //     $(".goBack").attr("href", "../home.html");
    // }
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status==xigou.dictionary.success){
                $('.orders-finish').dropload({
                    scrollArea : window,
                    loadDownFn : function(me){
                        finishOrders(page,me);
                        me.resetload();
                        if(1 == page){
                            $(".dropload-refresh").hide();
                        }
                        page++;
                    }
                });
                var nDefaulePage = xigou.getQueryString("defaule");
                if (nDefaulePage)
                {
                    setdefaulesel(nDefaulePage);
                }
            }else{
                window.location.href="../logon.html";
            }
        }
    });
});

var finishorder_max = "N";

function finishOrders(page, me){

    if(finishorder_max == "Y"){
        me.disable("down",false);
        $('.ui-refresh-down>span:first-child').removeClass();
        return false;
    }
    if(finishorder_max != "Y"){
        var token = xigou.getToken();
        var createBeginTime = xigou.getSessionStorage('beginquerydate');
        var createEndTime = xigou.getSessionStorage('endquerydate');
        var params = {
            'token' : token,
            'orderstatus':6,
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
                        setFinishOrderHtmlView(response, page);
                        if(response.data.curpage >= response.data.totalpagecount){
                            me.lock();
                            $(".dropload-down").hide();
                        }
                    }else{
                        me.lock();
                        $(".dropload-down").hide();
                    }
                }
            }
        }
    });
}
};


function setFinishOrderHtmlView(json, page) {
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
        finishorder_max = "Y";
    }

    if (page == 1 && html.length == 0) {
        $(".orders-finish").empty();
        var htmlData = [];
        htmlData.push('<div class="order_empty"><div class="empty-tip">还木有订单哟~</div></div>');
        $('.orders-finish').append(htmlData.join(''));
    } else {
        $('.orders-finish').append(html.join(''));
    }
}

//切换当前选中List
function setdefaulesel(npage)
{
    var scroller = $('.ui-tab-content')[0];
    var nav = $('.ui-tab-nav')[0];

    $(scroller.children[0]).removeClass('current');
    $(nav.children[0]).removeClass('current');
    $(scroller.children[npage]).addClass('current');
    $(nav.children[npage]).addClass('current');
    var width = -1 * scroller.clientWidth * npage / 3;
    scroller.style.transform = "translate(" + width + "px, 0px)";
    $(scroller.children)[0].height = "0px";
}