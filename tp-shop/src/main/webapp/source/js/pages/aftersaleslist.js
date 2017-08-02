var cur_list_page = 1;
var list_max = "N";

$(function(){
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status==xigou.dictionary.success){
                initList()
            }else{
                xigou.setSessionStorage("loginjump_url", "cart.html");
                window.location.href = "logon.html";
            }
        }
    });
});

function initList() {
    $('.content').dropload({
        scrollArea : window,
        loadDownFn : function(me){
            me.resetload();
            requestsalelist(cur_list_page, me);
            cur_list_page++;
        }
    });
}

function requestsalelist(page, me) {
    if(list_max == "Y"){
        isScrolling = false;
        $('.dropload-down').hide();
        $('.ui-refresh-down>span:first-child').removeClass();
        return;
    }

    var params = {
        'token': xigou.getToken(),
        'curpage': page,
    };

    xigou.aftersales.list({
        requestBody: params,
        callback:function(response, status) { // 回调函数
            isScrolling = false;
            if (status == xigou.dictionary.success) {
                if (response.data.curpage == 1) {
                    if (!response.data.list || response.data.list.length == 0) {
                        $('.after_sales_list').addClass('after_sales_list_none');
                    }
                    else {
                        $('.after_sales_list').removeClass('after_sales_list_none');
                    }
                }
                if (!response.data || !response.data.list || !response.data.curpage || !response.data.totalpagecount) {    // 请求数据异常
                    list_max = "Y";
                    me.lock();
                    $(".dropload-down").hide();
                    return;
                }
                else  if (response.data.list.length == 0) { // 请求列表为空
                    list_max = "Y";
                    me.lock();
                    $(".dropload-down").hide();
                    return;
                }
                else if (response.data.curpage == response.data.totalpagecount) {   // 最后一页
                    list_max = "Y";
                    me.lock();
                    $(".dropload-down").hide();
                }

                var htmlData = [];
                for (var i = 0; i < response.data.list.length; i++) {
                    var item = response.data.list[i];
                    var linepriceYan = item.itemprice.split('.')[0] || '00';
                    var linepriceFen = item.itemprice.split('.')[1] || '00';
                    var totalriceYan = item.returnprice.split('.')[0] || '00';
                    var totalpriceFen = item.returnprice.split('.')[1] || '00';

                    htmlData.push('<li class="after_sales_item">');
                    htmlData.push('<a href="returndetail.html?asid=' + item.asid + '">');
                    htmlData.push(' <div class="after_sales_info">');
                    if (item.status == 2) { // 审核不通过
                        htmlData.push('     <span class="color_red">' + item.statusdesc + '</span>');
                    }
                    else {
                        htmlData.push('     <span>' + item.statusdesc + '</span>');
                    }
                    htmlData.push('     <div>售后单号：' + item.ascode + '</div>');
                    htmlData.push(' </div>');
                    htmlData.push(' <div class="item_info">');
                    htmlData.push('     <div class="div_item_img">');
                    htmlData.push('         <img src="' + item.itemimg + '" alt="" width="75" height="75">');
                    htmlData.push('     </div>');
                    htmlData.push('     <div class="item_price_info">');
                    htmlData.push('         <span class="lineprice">¥<span>' + linepriceYan + '</span>.' + linepriceFen + '</span>');
                    htmlData.push('         <div class="item_name">' + item.itemname + '</div>');
                    htmlData.push('         <span class="lineprice">×' + item.buycount + '</span>');
                    htmlData.push('         <div class="item_format"> </div>');
                    htmlData.push('     </div>');
                    htmlData.push(' </div>');
                    htmlData.push(' <div class="total_price">');
                    htmlData.push('     <div class="total_price_info">');
                    htmlData.push('         共' + item.buycount + '件 退款金额：');
                    htmlData.push('         <span class="price_fen">¥<span class="price_yuan">'+ totalriceYan + '.</span>' + totalpriceFen + '</span>');
                    htmlData.push('     </div>');
                    htmlData.push(' </div>');
                    htmlData.push('</div>');
                    htmlData.push('</a>');
                    htmlData.push('</li>');
                }
                $(".after_sales_list").append(htmlData.join(" "));

                // 记录列表
                var dataStr = JSON.stringify(response.data.list);
                if (page == 1) {
                    xigou.setLocalStorage("aftersaleslist", dataStr);
                }
                else {
                    var _speciallist = xigou.getLocalStorage("aftersaleslist");
                    var _oldData = JSON.parse(_speciallist);
                    for(var m = 0,len = response.data.list.length;m < len ;m++) {
                        _oldData.push(response.data.list[m]);
                    }
                    xigou.setLocalStorage("aftersaleslist",JSON.stringify(_oldData));
                }
            }
        }
    });
}