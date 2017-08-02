var title = '';
var desc = '';
var lineLink = '';
var imgUrl = '';
var showLoading = true;
var dssUserMobile = '';
var type = 1;
$(function(){
    if (isWeiXin()) {
        InitWeixin();
    }
    var querytype = xigou.getQueryString('type');
    if(querytype){
    	type=querytype;
    }
    var curPage = 1;
    var loadItemMax = 'N';
    var itemIndex = 0;
    var showState = ["未上架","已上架"];
    var from=xigou.getQueryString("from");
    var fromTopicid =xigou.getSessionStorage("fromTopicid");
    var scroll;
    var clickFlag = 0;
    xigou.removeSessionStorage("speciallist");
    xigou.removeSessionStorage("historypageshop");
    xigou.removeSessionStorage("speciallist2");
    xigou.removeSessionStorage("historypageshop2");
    sgList();//绑定事件
    showItemPicDetail();

    //获取主题团分类
    xigou.promoterFunc.getopics({
        requestBody :{
            token:xigou.getToken(),
            type:type,
            topictype:3
        },
        showLoading : showLoading,
        callback:function (response, status) {
            if(status == xigou.dictionary.success){
                if(response.code == 0){
                    var topicName ="";
                    var data = response.data;
                    var item = '<li class = "lists"><ul class = "all-content"></ul></li>';
                    for(var i =0;i<data.length;i++){
                        topicName  +='<li data-topicid='+data[i].topicid+'>'+data[i].name+'</li>';
                        $(".lists").eq(0).after(item);
                    }
                    $("#all").after(topicName);
                    scrolla = new fz.Scroll(".ui-scroller-a", {
                        scrollX: true
                    });
                    if(from == "brandList"){
                        scrolla._translate(scrolla.maxScrollX,0);
                        $("#pGroup").trigger("click");
                    }
                }else{
                    $.tips({
                        content:response.msg || "获取列表失败",
                        stayTime:2000,
                        type:"warn"
                    });
                }
            }
        }
    })

    //绑定列表获取事件
    function sgList(){
        $("#ClassifiedTitle li").live("click",function(){

            var $this = $(this);
            itemIndex = $this.index();
            $this.addClass('current').siblings('li').removeClass('current');
            $('.lists').eq(itemIndex).show().siblings('.lists').hide();
            if(!$(this).hasClass("zt")){
                if($(this).attr("data-loadend") == "Y"){
                    return;
                }
                var id = $(this).attr("data-topicid");
                curPage = $(this).attr("curPage") || 1;
                //初始化dropload变量
                loadItemMax = 'N';
                //重置下拉刷新
                dropload.unlock();
                dropload.noData(false);
                dropload.resetload();
            }
        })
        //品牌团
        $("#pGroup").on("click",function(){
            var _this = $(this);
            pGroupActive();
        });
    }
    //加载产品列表
    function LoadItemInfo(id,curpage,me){
        if(loadItemMax == 'Y'){
            $('.ui-refresh-down>span:first-child').removeClass();
            return;
        }

        xigou.promoterFunc.getopicitems({
            requestBody :{
                token:xigou.getToken(),
                type:type,
                topicid:id || "",
                topictype:3,
                curpage:curpage
            },
            showLoading : showLoading,
            async : false,
            callback:function (response, status) {
                if(status == xigou.dictionary.success){
                    if(response.code == 0){
                        if (response.data.curpage==response.data.totalpagecount ) {
                            $("#ClassifiedTitle li").eq(itemIndex).attr("data-loadend","Y") ;
                            me.lock();
                            me.noData();
                            $(".dropload-down").hide();
                        }
                        var topicItem = "";

                        var dataList =  response.data.list;
                        for(var i = 0; i <dataList.length; i++){
                            topicItem +=   '<li ng-repeat="x in topicItem">'
                                +'<a class="item_img" tid="' + dataList[i].topicid + '" sku="' + dataList[i].sku + '" onclick="showItemDetail(this,'+dataList[i].commission+')"><img src ='+dataList[i].imgurl+' /></a>'
                                +'<p class = "dsc">'+dataList[i].name+'</p>'
                                +'<p class="product_price">'
                                +'¥<span class="product_price_yuan">'+dataList[i].topicprice+'</span>&nbsp;<span class="product_old_price">¥'+dataList[i].saleprice+'</span>'
                                +'</p>'
                                +'<p class = "c2" style = "color:#999;font-size:14px;">返佣：<span class = "c1" style = "color:#ee3e54;" >'+dataList[i].commission+'</span></p>'
                                +'<p class = "fl s_state" style = "line-height: 23px;margin-top: 7px;"><i class = "icon'+dataList[i].onshelves+' fl show_state"  sku = "'+dataList[i].sku+'" topicid = "'+dataList[i].topicid+'" onshelves = "'+dataList[i].onshelves+'"></i><span>'+showState[dataList[i].onshelves]+'</span></p>'
                                +'</li>';
                        }
                        if(topicItem==''){
                        	topicItem ='<li ng-repeat="x in topicItem"></li>';
                        }
                        $('.tab-content li.lists').eq(itemIndex).find(".all-content").append(topicItem);
                        me.resetload();
                        bindChangeClick();
                        curPage++;
                        $(".ui-tab-nav li").eq(itemIndex).attr("curPage",curPage) ;//加载的当前页数保存起来
                        //if(curPage >1){
                        //    $(".cartnull").hide();
                        //}

                    }else {
                        $.tips({
                            content:response.msg || "获取列表失败",
                            stayTime:2000,
                            type:"warn"
                        });
                    }
                }
            }
        })
    };
    //切换上架状态事件
    function bindChangeClick(){
        $(".show_state,.show_brand_state").on("click",function(){
            //防止点击短时间多次点击
            var c_clickFlag = new Date().getTime();
            if((c_clickFlag - clickFlag) > 2000){
                clickFlag = c_clickFlag;
            }else{
                return;
            }
            var _this = $(this),
                btnFlag = $(this).hasClass("show_state") == true?1:0,//专题或者品牌
                shelftype = btnFlag?1:0,//上下架类型 0：专题 1：商品
                onshelf_s = _this.attr("onshelves"),//上下架状态 0：下架 1：上架
                onshelf = eval(onshelf_s)?0:1,
                sku = _this.attr("sku"),
                topicid = _this.attr("topicid");

            xigou.promoterFunc.setshelves({
                requestBody :{
                    token:xigou.getToken(),
                    type:type,
                    topicid:topicid,
                    shelftype:shelftype,
                    onshelf:onshelf,
                    sku:sku,
                    topictype:btnFlag ? 3 : 2 //专题类型(2:品牌团 3:主题团)
                },
                callback:function (response, status) {
                    if(status == xigou.dictionary.success){
                        if(response.code == 0){
                            $.tips({
                                content:response.msg,
                                stayTime:2000,
                                type:"warn"
                            });
                            _this.attr("onshelves",response.data.onshelves);
                            //切换上架状态
                            if(btnFlag){
                                //主题团
                                _this.siblings('span').text(showState[response.data.onshelves]);
                                _this.removeClass('icon'+onshelf_s).addClass('icon'+onshelf);
                            }else{
                                //品牌团
                                _this.removeClass('rg-status'+onshelf_s).addClass('rg-status'+onshelf);
                            }
                        }else{
                            $.tips({
                                content:response.msg || "操作失败",
                                stayTime:2000,
                                type:"warn"
                            });
                        }
                    }
                }
            })

        });
    }
    var dropload = $("body").dropload({
        scrollArea : window,
        loadDownFn:function(me){
            var id = $("li.current").attr("data-topicid") || "";
            LoadItemInfo(id,curPage,me);
        }
    });
    function pGroupActive(){
        dropload.lock();
        dropload.noData();
        $(".dropload-down").hide();
        xigou.promoterFunc.getopics({
            requestBody :{
                token:xigou.getToken(),
                type:type,
                topictype:2
            },
            showLoading : showLoading,
            callback:function (response, status) {
                if(status == xigou.dictionary.success){
                    if(response.code == 0){
                        var data = response.data;
                        var singleList ="";
                        for(var i = 0;i<data.length;i++){
                            singleList += '<div class ="zt-item">'
                                + '<a class = "zt_img" href = "brandList.html?topicid='+data[i].topicid+'&type='+type+'"><img src='+data[i].image+' /></a>'
                                + '<div class = "show_brand_state rg-status'+data[i].onshelves+'" topicid ="'+data[i].topicid+'" onshelves = "'+data[i].onshelves+'"></div>'
                                + '</div>';
                        }
                        $("#b .zt-content").append(singleList);
                        //记录锚点
                        if(from == "brandList"){
                            var fromT = $('a[href$="'+fromTopicid+'"]')[0];
                            if (fromT) {
                                setTimeout(function(){
                                    xigou.removeSessionStorage('fromTopicid');
                                    fromTopicid = "";
                                    fromT.scrollIntoView(false);
                                }, 500);
                            }
                        }

                        bindChangeClick();
                        if(singleList.length >0){
                            $(".cartnull").hide();
                        }
                    }else{
                        $.tips({
                            content:response.msg || "获取列表失败",
                            stayTime:2000,
                            type:"warn"
                        });
                    }
                }
            }
        })
    }
    //计算字符串宽度
    function Compute(v) {
        var d = document.getElementById('dvCompute');
        d.innerHTML = v;
        return d.offsetWidth;
    }
});

// 显示商品详情
function showItemDetail(item,commision){
    var $this = $(item);
    var sku = $this.attr('sku');
    var tid = $this.attr('tid');
    var shopMobileVal  = null;
    var dssUserInfo = xigou.getSessionStorage("dssUser");
    if(dssUserInfo && dssUserInfo != "{}"){
        var userInfo =JSON.parse(dssUserInfo);
        shopMobileVal = userInfo ?(userInfo.mobile && userInfo.token ?userInfo.mobile :null):null;
    }
    xigou.activeProduct.details({
        p: 'sku=' + sku + '&tid=' + tid + '&shopmobile=' + shopMobileVal,
        showLoading : showLoading,
        callback:function(response, status) {
            var json = response || null;
            if (status == xigou.dictionary.success && response && response.code == '0') {
                $('body').css('overflow', 'hidden');
                $('.div_show_detail').removeClass('down').addClass('up');
                $('.detail-content').hide();
                $('.item_detail').show();
                var imageurl = json.data.imglist;
                var price = isNaNDefaultInt(json.data.price); //优惠后价格
                var oldprice = isNaNDefaultInt(json.data.oldprice); //原价
                var discount = xigou.subDiscount(price, oldprice); //折扣
                if(json.data.name == "undefined"){
                    json.data.name = "";
                }
                var name = json.data.name; //商品名称
                if(json.data.feature == "undefined"){
                    json.data.feature = "";
                }
                var feature = json.data.feature; //特色
                var specs = json.data.specs; //规格（包括颜色，尺寸等），返回数组
                var skulist = json.data.skus; //规格对应sku数值
                var count = json.data.stock; //库存数量

                var restrictcount = json.data.limitcount;//限购数量;
                var status = json.data.status;//商品状态
                var countryimagepath = json.data.countryimg;//国家图片;
//                var commision = json.data.commision; //返佣
                SetPrice(price);
                $('#old_price').text('¥' + oldprice);

                //商品图片
                slider_show(imageurl);
                if (json.data.salescountdesc) {
                    $(".sale-count")[0].innerHTML = json.data.salescountdesc;
                }

                $('.detail-content').html(json.data.detail);
                // 商品名称  标题 等等
                $(".dec-title")[0].innerHTML = name;
                $(".dec-info")[0].innerHTML = feature;
                $("#contry-img").attr("src",json.data.countryimg);
                if (json.data.channel) {
                    $(".warehouse")[0].innerHTML = json.data.channel;
                }
                if (json.data.countryname) {
                    $(".country-name")[0].innerHTML = json.data.countryname;
                }
                if(commision){
                    $("#fy_price").show().find("span").html('¥&nbsp;'+commision);
                }

                // 促销
                if (!json.data.tags || json.data.tags.length == 0) {
                    $('.div_promotion').hide();
                }
                else {
                    $('.div_promotion').show();
                    var htmlData = [];
                    for (var i = 0; i < json.data.tags.length; i++) {
                        var tagItem = json.data.tags[i];
                        if (tagItem && tagItem.tag) {
                            var fontcolor = tagItem.fontcolor || '#ffffff';
                            var bgcolor = tagItem.bgcolor || '#f57584';
                            var tag = tagItem.tag || " ";
                        }
                        htmlData.push('<span style="color: ' + fontcolor + ';background-color: ' + bgcolor + '">' + tag + '</span>');
                    }
                    $('.div_promotion_desc').empty();
                    $('.div_promotion_desc').append(htmlData.join(' '));
                }
                setShareItem(json.data);
            }
        }
    });

    function slider_show(imageurl) {
        if (imageurl == null || imageurl.length == 0) return false;
        var html = [];
        html.push('<div class="ui-slider item-img-list">');
        html.push('<ul class="ui-slider-content">');
        for (var i = 0; i < imageurl.length; i++) {
            html.push('<li>');
            html.push('	<a href="javascript:void(0);"><img src="' + imageurl[i] + '"/></a>');
            html.push('</li>');
        };
        html.push('</ul>');
        html.push('</div>');
        $('#sliderlist').empty().html(html.join(''));
        var slider = new fz.Scroll('.item-img-list', {
            role: 'slider',
            indicator: true,
            autoplay: true,
            interval: 3000,
            defaultIndex:1
        });
    }

    // 设置价格
    function SetPrice(itemPrice){
        if(typeof(itemPrice) == undefined || itemPrice == 0)
        {
            return;
        }

        var Yan = "00", Fen = "00";
        var CHARS = itemPrice.split('.');
        if (CHARS.length > 0) {
            Yan = CHARS[0];
            if (CHARS.length > 1) {
                Fen = CHARS[1];
            }
        }

        var innerHTML = '¥<span class="iem-price2">' + Yan + '</span>.' + Fen;
        $('#iem-price')[0].innerHTML = innerHTML;
    }
}

// 隐藏商品详情
function hideItemDetail() {
    $('.item_detail').hide();
    $('body').css('overflow', 'auto');
    setShareItem();
}

// 显示/隐藏图文详情
function showItemPicDetail() {
    var $this = $('.div_show_detail');
    var detail = $('.detail-content');
    $('.div_show_detail')[xigou.events.click](function(){
        if ($this.hasClass('up')) {
            $this.removeClass('up').addClass('down');
            detail.show();
        } else {
            $this.removeClass('down').addClass('up');
            detail.hide();
        }
    })
}

function setShareItem(itemData) {
    if (!dssUserMobile) {
        var dssUser = xigou.getSessionStorage("dssUser");
        if (dssUser && dssUser != "{}") {
            dssUser = JSON.parse(dssUser);
            if (dssUser.token && dssUser.token == xigou.getToken()) {
                dssUserMobile = dssUser.mobile || dssUser.shopmobile;
            }
        }
    }
    if (!itemData) {
        title = '全球购商城｜是电商，也是shopping mall';
        desc = '全国首家跨境生活综合体，线下商场与线上商城，同步更新，同享优惠。现在注册，立享150元优惠券，3月更多活动，精彩不断。';
        lineLink = xigou.activeHost + 'shop.html';
        if (dssUserMobile) {
            lineLink = lineLink + "?shop=" + dssUserMobile;
        }
        imgUrl = xigou.activeHost + 'img/bg_style1.png';
    }
    else {
        title = itemData.name;
        desc = itemData.feature;
        lineLink = xigou.activeHost + 'item.htm?tid=' + itemData.tid + '&sku=' + itemData.sku;
        if (itemData.imglist && itemData.imglist.length > 0) {
            imgUrl = itemData.imglist[0];
        }
        else {
            imgUrl = xigou.activeHost + 'img/bg_style1.png';
        }
        if (dssUserMobile) {
            lineLink = lineLink + "&shop=" + dssUserMobile;
        }
    }

    wx.onMenuShareAppMessage({
        title: title,
        desc: desc,
        link: lineLink,
        imgUrl: imgUrl,
        trigger: function (res) {
            // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
        },
        success: function (res) {
        },
        cancel: function (res) {
        },
        fail: function (res) {
        }
    });

    wx.onMenuShareTimeline({
        title: title,
        link: lineLink,
        imgUrl: imgUrl,
        trigger: function (res) {
            // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
        },
        success: function (res) {
        },
        cancel: function (res) {
        },
        fail: function (res) {
        }
    });

    wx.onMenuShareQQ({
        title: title,
        desc: desc,
        link: lineLink,
        imgUrl: imgUrl,
        trigger: function (res) {
        },
        complete: function (res) {
        },
        success: function (res) {
        },
        cancel: function (res) {
        },
        fail: function (res) {
        }
    });

    wx.onMenuShareWeibo({
        title: title,
        desc: desc,
        link: lineLink,
        imgUrl: imgUrl,
        trigger: function (res) {
        },
        complete: function (res) {
        },
        success: function (res) {
        },
        cancel: function (res) {
        },
        fail: function (res) {
        }
    });

    wx.onMenuShareQZone({
        title: title,
        desc: desc,
        link: lineLink,
        imgUrl: imgUrl,
        trigger: function (res) {
        },
        complete: function (res) {
        },
        success: function (res) {
        },
        cancel: function (res) {
        },
        fail: function (res) {
        }
    });
}

function InitWeixin() {
    showLoading = false;
    xigou.loading.open();
    var pa = [];
    var url = location.href.split('#')[0].replace(/&+/g, "%26");
    pa.push('url=' + url);

    xigou.activeUser.config({
        p : pa.join('&'),
        showLoading: false,
        callback: function(response, status) { //回调函数
            if (status != xigou.dictionary.success) {
                return;
            } else if (!response || 0 != response.code) {
                return;
            }
            var data = response.data;
            wx.config({
                appId: data.appid,
                timestamp: data.timestamp,
                nonceStr: data.nonceStr,
                signature: data.signature,
                jsApiList: [
                    'checkJsApi',
                    'onMenuShareTimeline',
                    'onMenuShareAppMessage',
                    'onMenuShareQQ',
                    'onMenuShareWeibo',
                    'onMenuShareQZone',
					'scanQRCode',
                ]
            });
        }
    })
}

wx.ready(function() {
    // 1 判断当前版本是否支持指定 JS 接口，支持批量判断
    wx.checkJsApi({
        jsApiList: [
            'getNetworkType',
            'previewImage'
        ],
        success: function (res) {
        }
    });

    setShareItem();

    xigou.loading.close();
    xigou.wxreadyalert();
});