var type=xigou.getQueryString("type");
if(type == null)
{
    window.location.href="../home.html";
}

var onelevel_max = "N";

$(function() {
    var page = 1;
    if(type == 0 || type == '0'){
        $(".goBack").attr("href", "dealers.html?type=0");
    }else if(type == 1 || type == '1'){
        $(".goBack").attr("href", "dealers.html?type=1");
    }else{
        $(".goBack").attr("href", "../home.html");
    }

    var name = xigou.getQueryString('name') || '全球购用户';
    $('title').html(name + '的分销');

    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status==xigou.dictionary.success){
                $('.info-list').dropload({
                    scrollArea : window,
                    loadDownFn : function(me){
                        levelonePartner(page,me);
                        me.resetload();
                        if(1 == page)
                        {
                            $(".dropload-refresh").hide();
                        }
                        page++;
                    }
                });
            }else{
                window.location.href="../home.html";
            }
        }
    });
});

//function showDetail() {
//    $('.div_show_detail')[xigou.events.click](function(){
//        var $this = $(this);
//        if ($this.hasClass('up')) {
//            $this.removeClass('up').addClass('down');
//            var detailInfo = $this.parents('.info-item').find('.detail_info');
//            detailInfo.hide();
//        }
//        else {
//            $this.removeClass('down').addClass('up');
//            var detailInfo = $this.parents('.info-item').find('.detail_info');
//            detailInfo.show();
//        }
//    })
//}

function levelonePartner(page, me){
    if(onelevel_max == "Y"){
        me.disable("down",false);
        $('.ui-refresh-down>span:first-child').removeClass();
        return false;
    }
    if(onelevel_max != "Y"){
        var token = xigou.getToken();
        var params = {
            'type' : type,
            'token':token,
            'level':2,
            'sonpromoterid': xigou.getQueryString('promoterid'),
            'curpage' : page
        };

        xigou.promoterFunc.queryDealers({
            requestBody : params,
            callback : function(response, status) {
                if (status == xigou.dictionary.success) {
                    if (null == response) {
                        $.tips({
                            content : xigou.dictionary.chineseTips.server.value_is_null,
                            stayTime : 2000,
                            type : "warn"
                        });
                    }
                    else {
                        var code = response.code;
                        if(0 == code){
                            if (page == 1 && response.data.list.length == 0) {
                                $('.info-list').html('<div class="empty-box"><div class="empty-tip">还木有分销员哟</div></div>');
                                me.lock();
                                $(".dropload-down").hide();
                                onelevel_max = 'Y';
                                return;
                            }

                            $.each(response.data.list, function(index, value){
                                $('.info-item-list').append(createItemInfoHtml(value).join(''));
                            });

                            if(response.data.curpage >= response.data.totalpagecount){
                                me.lock();
                                $(".dropload-down").hide();
                                onelevel_max = 'Y';
                            }
                        }else{
                            me.lock();
                            $(".dropload-down").hide();
                            onelevel_max = 'Y';
                        }
                    }
                }
            }
        });
    }
}

function createItemInfoHtml(info) {
    var htmlData = [];
    htmlData.push('<div class="info-item">');
    htmlData.push(' <div class="user-info">');
    htmlData.push('     <div class="div_level_2"></div>');
    htmlData.push('     <div class="div_name_tel">');
    var name = info.nickname || info.name || '全球购用户';
    htmlData.push('         <div class="div_name">' + name + '</div>');
    htmlData.push('         <div class="div_tel">联系电话：' + info.mobile + '</div>');
    htmlData.push('     </div>');
    htmlData.push(' </div>');
    htmlData.push(' <div class="div_dealers_info">');
    htmlData.push('     <div>');
    htmlData.push('         <div>3</div>');
    htmlData.push('			<div>¥' + info.orderamount + '</div>');
    htmlData.push('			<div>¥' + info.totalfees + '</div>');
    htmlData.push('     </div>');
    htmlData.push('		<div>');
    htmlData.push('			<div>分销级别</div>');
    htmlData.push('			<div>订单金额</div>');
    htmlData.push('			<div>收人金额</div>');
    htmlData.push('		</div>');
    htmlData.push(' </div>');
    htmlData.push('</div>');
    return htmlData;
}