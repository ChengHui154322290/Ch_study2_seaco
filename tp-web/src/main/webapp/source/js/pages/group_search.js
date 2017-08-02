var CurPage = 1;
var bMaxPage = false;
var searchSortType = xigou.getQueryString('sort') || "";                    // 排序方式
var Key = xigou.getQueryString('key');      // 关键字
var categoryid = xigou.getQueryString('categoryid');
var brandid = xigou.getQueryString('brandid');
var fromPage = xigou.getQueryString('fromPage');
var Key = xigou.getQueryString('key');
var name = xigou.getQueryString('name');



$(function() {
    $(".dropload-refresh").hide();
    var dropload = $(".search_result_list").dropload({
        scrollArea: window,
        loadDownFn: function (me) {
            if (bMaxPage) {
                return;
            }
            me.resetload();
            Search(me, searchSortType);
            CurPage++;
        }
    
    });

        $(".search_names").text(Key);

    switch (searchSortType) {
        case 1:
            $('.div_search_sort_item').removeClass('current_sort');
            $('#sortBySales').addClass('current_sort');
            $('#sortBySales').children('u').addClass('down');
            $('#sortBySales').children('u').removeClass('up');
            break;
        case 2:
            $('.div_search_sort_item').removeClass('current_sort');
            $('#sortBySales').addClass('current_sort');
            $('#sortBySales').children('u').removeClass('down');
            $('#sortBySales').children('u').addClass('up');
            break;
        case 3:
            $('.div_search_sort_item').removeClass('current_sort');
            $('#sortByPrice').addClass('current_sort');
            break;
        case 5:
            $('.div_search_sort_item').removeClass('current_sort');
            $('#sortByNew').addClass('current_sort');
            break;
    }

    $('#input_search')[xigou.events.click](function () {
        $('.div_search_history').show();
    });

    window.onbeforeunload = function(){
        var url = [];
        if (searchSortType) {
            url.push('sort=' + searchSortType);
        }
        if (Key) {
            url.push('key=' + Key);
        }
        if (searchSortType) {
            url.push('sort=' + searchSortType);
        }
        if (categoryid) {
            url.push('categoryid=' + categoryid);
        }
        if (brandid) {
            url.push('brandid=' + brandid);
        }
        if (name) {
            url.push('name=' + name);
        }
        xigou.setSessionStorage('productdetails_backurl', 'group_search.html?' + url.join('&'));
    };
    onbeforeunload();
    $("form").submit(function(e){
        //e.preventDefault();
        return false;
    });

    $(".dropload-down").hide();
    InitSearchEdid(dropload);
    selectSortType(dropload);
    InitSearchHistory();
})

function InitSearchEdid(dropload) {
    if (Key && Key.length > 0) {
        $('#input_search')[0].value = Key;
        $('.div_clear_input').show();

        $('.div_clear_input')[xigou.events.click](function() {
            $('.div_search_history').show();
            $('#input_search_history')[0].value = ' ';
            $('#input_search_history').focus();
        })

        InitSearchHistory(dropload);
    }
    else {
      //  $('#id_input_search_box').hide();
        var name = xigou.getQueryString('name');
        $('#id_name').text(name);
        $('#id_name').show();
    }
}

function Search(me, sortType){
    var Params = 'curpage=' + CurPage;

    if (Key) {  // 关键字搜索
        Params = Params + '&key=' + Key;
    }
    else if (categoryid) {
        Params = Params + '&categoryid=' + categoryid;
    }
    else if (brandid) {
        Params = Params + '&brandid=' + brandid;
    }

    // 排列方式
    if (sortType) {
        Params = Params + '&sort=' + sortType;
    }
    Params = Params+'&salespattern=9';

    xigou.classify.search({
        p:Params,
        callback: function(response, status) {
            if (!status == xigou.dictionary.success) {
                $.tips({
                    content:'收索失败,请检测网络连接',
                    stayTime:2000,
                    type:"warn"
                });
                return;
            }
            else {
                InitPage(me, response);
            }
        }
    })
}

function InitPage(me, response) {
    if(fromPage == "index"){
        $(".goBack").parent("a").attr("href","group_buying.html");
    }
    if (!response) {
        $.tips({
            content:'收索失败,请检测网络连接',
            stayTime:2000,
            type:"warn"
        });
        return;
    }
    else if (response.code != 0) {
        $.tips({
            content:response.msg || '收索失败,请检测网络连接',
            stayTime:2000,
            type:"warn"
        });
        return;
    }

    // 最大页
    if (!response.data.curpage || !response.data.totalpagecount || response.data.curpage == response.data.totalpagecount) {
        bMaxPage = true;
        me.lock();
        $(".dropload-down").hide();
    }
    // 列表为空
    if (response.data.curpage == 1) {
        $('.ul_search_list').empty();
        if (!response.data.list || response.data.list.length == 0) {
            $('.div_search_list').addClass('div_search_list_none');
            bMaxPage = true;
            me.lock();
            $(".dropload-down").hide();
            return;
        }
        else {
            $('.div_search_list').removeClass('div_search_list_none');
        }
    }
    
    var htemlData = [];

    for (var index in response.data.list) {
        var itemData = response.data.list[index];
        var priceYuan = itemData.price.split('.')[0] || "00";
        var priceJiao = itemData.price.split('.')[1] || "00";
        htemlData.push('<div class="search_result_item">');
        htemlData.push('        <a href="group_detail.html?tid=' + itemData.tid + '&amp;sku=' + itemData.sku + '">');
        htemlData.push('   <span class="shop_logo"><img src="'+ itemData.imgurl+'"></span>');
        htemlData.push('        <div class="search_result_goods">');
        htemlData.push(' <div class="search_result_title" style="width: 200px; overflow: hidden">'+ itemData.name+'</div>');
        htemlData.push('      <div class="search_result_price">¥'+priceYuan +'.'+priceJiao+'<del class="old_price">¥'+itemData.oldprice+'</del></div>');
        htemlData.push('   <div class="search_result_shop_name">'+itemData.shopname+'<span class="sale">'+itemData.salescount+'</span></div>');
        htemlData.push('   </div>');
        htemlData.push('        </a>');
        htemlData.push('</div>');
    }
    $('.search_result_list').append(htemlData.join(" "));
}

function InitSearchHistory(dropload) {
    if(fromPage == "group_buying"){
        $('.div_search_history').show();
        $('#input_search_history').focus();
    }else{
        $('.div_search_history').hide();
    }
    var _history = xigou.getLocalStorage("searchHistory");
    var historyList = [];
    if (_history != "") {
        historyList = JSON.parse(_history);
    }

    if (historyList.length == 0) {
        $('.div_history_list').hide();
        $('.div_noHistory').show();
    }
    else {
        $('.div_history_list').show();
        $('.div_noHistory').hide();

        var htmlData = [];
        for (var i = 0; i < historyList.length; i++) {
           // var a = ' + historyList[i] + ';
            $("#id_ul_history").append('<li class="li_history_item"></li>');
            $("#id_ul_history li").eq(i).text( historyList[i]);
        }
       // $("#id_ul_history").append(htmlData.join(" "));

        $(".li_history_item")[xigou.events.click](function() {
            resetKeyAndSearch(this.innerHTML, historyList, dropload);
        })
    }

    $(".div_search_input_box").click(function(){
        $('.div_search_list').hide();
        $('.div_search_history').show();
        $('#input_search_history')[0].value = $('#input_search')[0].value;
        $('#input_search_history').focus();
        $('.div_clear_input_history').show();
    })

    $('.cancal_search')[xigou.events.click](function() {
        setTimeout(function(){
            /*$('#input_search_history').blur();
            $('#input_search_history')[0].value = "";
            $('.div_search_history').hide();
            $('.div_search_list').show();*/
        	window.location.href="group_buying.html"
        },150)

    })

    $('#input_search_history').keyup(function(e) {
        var a = $(this).val();
        if (a && a.length > 0) {
            $('.div_clear_input_history').show();
            if (13 == e.keyCode) {  // 开始搜索
                a = a.replace(/(^\s*)|(\s*$)/g, "");
                if (a.length > 0) {
                    resetKeyAndSearch(a, historyList, dropload);
                }
            }
        }
        else {
            $('.div_clear_input_history').hide();
        }
    });

    $('#input_search_history').keydown(function(e) {
        $('.div_clear_input_history').show();
    });

    $('.div_clear_input_history')[xigou.events.click](function() {
        $('#input_search_history')[0].value = "";
        $('.div_clear_input_history').hide();
    })
$('.div_clear_history').unbind( "ontouchend" in document ? "tap" : "click");
    $('.div_clear_history')[xigou.events.click](function() {
        var dia=$.dialog({
            title:'',
            content:"确认清空输入的搜索历史吗？ ",
            button:["取消","确认"]
        });

        dia.on("dialog:action",function(eDlg){
            if(1 == eDlg.index)
            {
                historyList = [];
                xigou.removelocalStorage("searchHistory");

                $('#id_ul_history').empty();
                $('.div_history_list').hide();
                $('.div_noHistory').show();
            }
        });
    })
}

function  resetKeyAndSearch(keyWords, historyList, dropload) {
    if (keyWords && keyWords.length > 0) {
        // 记录搜索记录 最多记录10条
        var index = historyList.indexOf(keyWords);
        if (-1 != index) {
            historyList.splice(index, 1);
        }
        historyList.unshift(keyWords);
        if (historyList.length > 10) {
            historyList.pop();
        }
        xigou.setLocalStorage("searchHistory",JSON.stringify(historyList));
        window.location.href="group_search.html?key=" + keyWords;

        //var htmlData = [];
        //for (var i = 0; i < historyList.length; i++) {
        //    htmlData.push('<li class="li_history_item">' + historyList[i] + '</li>');
        //}
        //$("#id_ul_history").empty();
        //$("#id_ul_history").append(htmlData.join(" "));
        //$(".li_history_item")[xigou.events.click](function() {
        //    resetKeyAndSearch(this.innerHTML, historyList, dropload);
        //})
        //
        ////$('#input_search_history')[0].value = "";
        //$('.div_clear_input_history').hide();
        //$('.div_history_list').show();
        //$('.div_noHistory').hide();
        //$('.div_search_list').show();
        //
        //Key = keyWords;
        //$('.div_search_history').hide();
        //$('#input_search')[0].value = keyWords;
        //$('.div_clear_input').show();
        //
        //$('.div_search_sort_item').removeClass('current_sort');
        //$('#sortByDefoule').addClass('current_sort');
        //searchSortType = '';
        //$('#input_search').focus();
        //
        //research(dropload);
    }
}

// 重新设置排序方式
function selectSortType(dropload) {
    $('.div_search_sort_item')[xigou.events.click](function(){
        var id = this.getAttribute("id");
        if (id == "sortByPrice") {  // 按价格两种排列方式
            if ($(this).hasClass('current_sort')) {
                if ($(this).children('u').hasClass('up')){
                    $(this).children('u').removeClass('up');
                    $(this).children('u').addClass('down');
                    searchSortType = 1;
                }
                else {
                    $(this).children('u').removeClass('down');
                    $(this).children('u').addClass('up');
                    searchSortType = 2;
                }
            }
            else {
                $('.div_search_sort_item').removeClass('current_sort');
                $(this).addClass('current_sort');
                $(this).children('u').removeClass('down');
                $(this).children('u').addClass('up');
                searchSortType = 2;
            }
        }
        else {
            if ($(this).hasClass('current_sort')) {
                return;
            }

            $('.div_search_sort_item').removeClass('current_sort');
            $(this).addClass('current_sort');
            var id = this.getAttribute("id");
            if (id == 'sortByDefoule') {
                searchSortType = '';
            }
            else if (id == 'sortBySales') {
                searchSortType = 3;
            }
            else if (id == 'sortByNew') {
                searchSortType = 5;
            }
        }
        research(dropload);
    })
}

function research(dropload) {
    CurPage = 1;
    bMaxPage = false;
    $('.ul_search_list').empty();
    dropload.unlock();
    dropload.resetload();
    Search(dropload, searchSortType);
    CurPage++;
}



// function InitSearchHistory() {
//
//
//     if(fromPage == "group_buying"){
//         $('.div_search_history').show();
//         $('#input_search_history').focus();
//     }else{
//         $('.div_search_history').hide();
//     }
//     var _history = xigou.getLocalStorage("searchHistory");
//     var historyList = [];
//     if (_history != "") {
//         historyList = JSON.parse(_history);
//     }
//
//     if (historyList.length == 0) {
//         $('.div_history_list').hide();
//         $('.div_noHistory').show();
//     }
//     else {
//         $('.div_history_list').show();
//         $('.div_noHistory').hide();
//
//         //var htmlData = [];
//         for (var i = 0; i < historyList.length; i++) {
//             $("#id_ul_history").append('<li class="li_history_item"></li>');
//             $("#id_ul_history li").eq(i).text( historyList[i]);
//         }
//         //$("#id_ul_history").append(htmlData.join(" "));
//
//         $(".li_history_item")[xigou.events.click](function() {
//             Search(this.innerHTML, historyList);
//         })
//     }
//
//     $(".div_search")[xigou.events.click](function(){
//         $('.ui-tab-content').hide();
//         $('#input_search')[0].value = "";
//         $('.div_search_history').show();
//
//         $('#input_search').focus().select();
//     })
//
//     $('.cancal_search')[xigou.events.click](function() {
//         $('.ui-tab-content').show();
//         $('#input_search')[0].value = "";
//         $('.div_search_history').hide();
//         $('#input_search').blur();
//     })
//
//     $('#input_search').keyup(function(e) {
//         var a = $(this).val();
//         if (a && a.length > 0) {
//             $('.div_clear_input').show();
//             if (13 == e.keyCode) {  // 开始搜索
//                 a = a.replace(/(^\s*)|(\s*$)/g, "");
//                 if (a.length > 0) {
//                     $('.ui-tab-content').show();
//                     Search(a, historyList);
//                 }
//             }
//         }
//         else {
//             $('.div_clear_input').hide();
//         }
//     });
//
//     $('.div_clear_input')[xigou.events.click](function() {
//         $('.div_clear_input').hide();
//         $('#input_search')[0].value = "";
//     })
//
//     $('.div_clear_history')[xigou.events.click](function() {
//         var dia=$.dialog({
//             title:'',
//             content:"确认清空输入的搜索历史吗？ ",
//             button:["确认","取消"]
//         });
//
//         dia.on("dialog:action",function(eDlg){
//             if(0 == eDlg.index)
//             {
//                 $('#id_ul_history').empty();
//                 $('.div_history_list').hide();
//                 $('.div_noHistory').show();
//             }
//         });
//
//         historyList = [];
//         xigou.removelocalStorage("searchHistory");
//
//
//     })
// }
