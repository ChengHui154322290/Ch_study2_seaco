﻿var fromPage = xigou.getQueryString("fromPage");
$(function() {	
    xigou.classify.navigation({
        callback: function(response, status) {  // 回调函数
            if (status == xigou.dictionary.success) {
                if (response.code == 0) {
                    InitPage(response.data);
                }
                else {
                    $.tips({
                        content:response.msg||'请求导航失败,请检测网络连接!',
                        stayTime:2000,
                        type:"warn"
                    });
                }
            }
            else if (!response) {
                $.tips({
                    content:'请求导航失败,请检测网络连接!',
                    stayTime:2000,
                    type:"warn"
                });
            }
            else {
                $.tips({
                    content:response.msg||'请求导航失败,请检测网络连接!',
                    stayTime:2000,
                    type:"warn"
                });
            }
        }
    })
    
});

$(".sidebar_show").click(function(){
	$(".sidebar").show();
	$(".div_categories").show();
});
$(".sidebar_hide").click(function(){
	$(".sidebar").hide();
	$(".div_brands").show();
}); 

$(".list_left").click(function(){
	$(this).addClass("current");
	$(".list_right").removeClass("current1");
	$(".tab-content li:first-child").show().siblings().hide();
})
$(".list_right").click(function(){
	$(this).addClass("current1");
	$(".list_left").removeClass("current");
	$(".tab-content li:last-child").show().siblings().hide();
})

$(window).scroll(function(){
	var nub = $(".div_content").children().length/2;
	var sumHeight = 0;
	var sidebarHeight = 0;
	for(var i = 0; i< nub; i++){
		var scrolltop = document.documentElement.scrollTop || document.body.scrollTop;
		sumHeight +=($('#href'+i).height() + $('#href_content'+i).height());			
		if(scrolltop<sumHeight){			
			$(".link"+i).addClass("current_color").siblings().removeClass("current_color");
			$(".link"+i).children(".line").show();
			$(".link"+i).siblings().children(".line").hide();
			$(".link"+i).css({
	     		"font-size":"15px"
	     	}).siblings().css({
	     		"font-size":"13px"
	     	}) 
	     	
			return;
			
		}
	}	
})

$(".div_group").click(function(){
	$('.choose').show();
});
$(".li1").click(function(){
	$('.choose').hide();
	$(".div_group").html($(this).children("span").html());
	$(".div_group").attr('data','');

	$('.input_search').attr('placeholder','请输入相关产品或店铺名称');
});
$(".li2").click(function(){
	$('.choose').hide();
	$(".div_group").html($(this).children("span").html());
    $(".div_group").attr('data',9);
	$('.input_search').attr('placeholder','请输入您想要团购的商品');
});


function InitPage(data) {
    $('.ui-tab-content').show();
    $("form").submit(function(e){
        //e.preventDefault();
        return false;
    });
    Initcategories(data.categories);
    Initbrands(data.brands);
    sidebar(data.categories);
    InitSearchHistory();
}

function Initbrands(data) {
    var htmlData = [];

    for (var i = 0; i < data.length; i++) {
        
        if( i == 0 ){
        	htmlData.push('<div class="div_brands_name" style="padding:0;"><img src="' + data[i].imgurl + '"/></div>');
    	}else{
    		htmlData.push('<div class="div_brands_name"><img src="' + data[i].imgurl + '"/></div>');
    	}
        
        htmlData.push('<div class="div_brands_child">');

        if (!data[i].child) {
            continue;
        }

        for (var j = 0; j < data[i].child.length; j++) {        	
            htmlData.push('<div class="div_brands_child_item_box">');
            var name = data[i].child[j].name || " ";
            htmlData.push(' <div class="div_brands_child_item" brandid="' + data[i].child[j].brandid + '" name="' + name + '">');
            if(data[i].child[j].imgurl == null || data[i].child[j].imgurl == undefined || data[i].child[j].imgurl == ""){
            	htmlData.push('     <img src="modifiable/img/loading_bg.png" />');
            }else{
            	htmlData.push('     <img src="' + data[i].child[j].imgurl + '">');
            } 
            htmlData.push(' </div>');
            htmlData.push('<div class="div_brands_child_item_name">' + data[i].child[j].name + '</div>');
            htmlData.push('</div>');            
        }
        
        var s = data[i].child.length/4;
        var s = s.toString()
        var arr = s.split('.');
        if(arr[1] == 5){
        	htmlData.push('<div class="div_brands_child_item_box">');
            htmlData.push(' <div class="div_brands_child_item_forbidden">');
            htmlData.push('     <img src="img/classify/ggct.png">');
            htmlData.push(' </div>');
            htmlData.push('<div class="div_brands_child_item_name">正品保证</div>');
            htmlData.push('</div>'); 
            htmlData.push('<div class="div_brands_child_item_box">');
            htmlData.push(' <div class="div_brands_child_item_forbidden">');
            htmlData.push('     <img src="img/classify/free_delivery.png">');
            htmlData.push(' </div>');
            htmlData.push('<div class="div_brands_child_item_name">全场包邮</div>');
            htmlData.push('</div>');
        }else if(arr[1] == 75){
        	htmlData.push('<div class="div_brands_child_item_box">');
            htmlData.push(' <div class="div_brands_child_item_forbidden">');
            htmlData.push('     <img src="img/classify/ggct.png">');
            htmlData.push(' </div>');
            htmlData.push('<div class="div_brands_child_item_name">正品保证</div>');
            htmlData.push('</div>');
        }else if(arr[1] == 25){
        	htmlData.push('<div class="div_brands_child_item_box">');
            htmlData.push(' <div class="div_brands_child_item_forbidden">');
            htmlData.push('     <img src="img/classify/ggct.png">');
            htmlData.push(' </div>');
            htmlData.push('<div class="div_brands_child_item_name">正品保证</div>');
            htmlData.push('</div>');
            htmlData.push('<div class="div_brands_child_item_box">');
            htmlData.push(' <div class="div_brands_child_item_forbidden">');
            htmlData.push('     <img src="img/classify/free_delivery.png">');
            htmlData.push(' </div>');
            htmlData.push('<div class="div_brands_child_item_name">全场包邮</div>');
            htmlData.push('</div>');
            htmlData.push('<div class="div_brands_child_item_box">');
            htmlData.push(' <div class="div_brands_child_item_forbidden">');
            htmlData.push('     <img src="img/classify/free_sale.png">');
            htmlData.push(' </div>');
            htmlData.push('<div class="div_brands_child_item_name">无忧售后</div>');
            htmlData.push('</div>');
        }
        htmlData.push('</div>');
    }

    $('.div_brands').append(htmlData.join(' '));


    var brandsItems = $('.div_brands_child_item_box');
    if (brandsItems && brandsItems.length > 0) {
        var itemWidth = brandsItems[0].offsetWidth || 60 ;
        $('.div_brands_child_item_box').css(
            "height",itemWidth + 38 
        );
    }

    $('.div_brands_child_item').click(function() {
        window.location.href='search.html?brandid=' + $(this).attr("brandid") + '&name=' + $(this).attr("name");
    })
}

function sidebar(data){
	var htmlData = [];
	htmlData.push(' <div class="sidebar">');
	for (var i = 0; i < data.length; i++) {
        if (!data[i].name) {
            data[i].name = ' ';
        }
        if(i==0){
        	htmlData.push(' <a style="font-size:15px;" class="link'+i+' current_color link" onclick=linkClick(' + i + ');>'+ data[i].name + '<div class="line" style="display:block;"></div></a>');
        }else{
        	htmlData.push(' <a class="link'+i+' link"  onclick=linkClick(' + i + '); >'+ data[i].name + '<div class="line"></div></a>');
        }
       
        
	}
	htmlData.push('</div>');
	$('body').append(htmlData.join(" "));
	$(".link").click(function(){
		$(this).addClass("current_color").siblings().removeClass("current_color");
 		$(this).children(".line").show();
     	$(this).siblings().children(".line").hide();
     	$(this).css({
     		"font-size":"15px"
     	}).siblings().css({
     		"font-size":"13px"
     	})    		
   });
}

function linkClick(idx){
	var height = getsidebarHeight(idx);
	$('body').scrollTop(height);
	
}

function getsidebarHeight(idx){
	var sumHeight = 0;
	for(var i = 0; i< idx; i++){
		var cssHeight = 2*parseInt($('#href'+i).css('margin-top').replace('px', '')) + 
						parseInt($('#href'+i).css('margin-bottom').replace('px', '')) +
						parseInt($('#href_content'+i).css('margin-top').replace('px', '')) +
						parseInt($('#href_content'+i).css('margin-bottom').replace('px', ''));
		sumHeight +=($('#href'+i).height() + $('#href_content'+i).height() + cssHeight);
	}
	return sumHeight;
}

function Initcategories(data) {	
    var MaxWidth = document.body.clientWidth - 30;
    var MaxCount = Math.floor(MaxWidth/13) * 2;
    var TmpLength = 0;
    var htmlData = [];
    htmlData.push('<div class="div_content">');
    for (var i = 0; i < data.length; i++) {
        if (!data[i].name) {
            data[i].name = ' ';
        }
        htmlData.push('<div class="div_img" id="href'+i+'" categoryid="' + data[i].categoryid + '" + name="' + data[i].name + '">');
        htmlData.push(' <div class="div_child_name" >'+ data[i].name + '</div>');
        htmlData.push('</div>');

        if (!data[i].child) {
            continue;
        }

        var NoArr = [];
        var child_len = data[i].child.length;
        var b = 0;
        while (b< child_len){
        	NoArr.push(b);
        	b = b+3;
        }
        
        NoArr.push(data[i].child.length);

        htmlData.push('<div class="div_child" id=href_content' + i +'>')
        for (var j = 0; j < NoArr.length - 1; j++) {
            htmlData.push('<div class="div_child_item">')
            for (var k = NoArr[j]; k < NoArr[j+1] - 1; k++) {
                if (data[i].child[k].ishighlight == 1) {
                	if(data[i].child[k].imgurl == null || data[i].child[k].imgurl == undefined || data[i].child[k].imgurl == ""){
                		htmlData.push('<div class="div_child_item_name highlight" categoryid="' + data[i].child[k].categoryid + ' " name="'+ data[i].child[k].name +'"><div class="item_img"><img src="modifiable/img/loading_bg.png"/></div><div class="item_name">' + data[i].child[k].name + '</div></div>');
                	}else{
                		htmlData.push('<div class="div_child_item_name highlight" categoryid="' + data[i].child[k].categoryid + ' " name="'+ data[i].child[k].name +'"><div class="item_img"><img src="'+ data[i].child[k].imgurl +'"/></div><div class="item_name">' + data[i].child[k].name + '</div></div>');
                	}                 
                }
                else {
                	if(data[i].child[k].imgurl == null || data[i].child[k].imgurl == undefined || data[i].child[k].imgurl == ""){
                		htmlData.push('<div class="div_child_item_name" categoryid="' + data[i].child[k].categoryid + ' " name="'+ data[i].child[k].name +'"><div class="item_img"><img src="modifiable/img/loading_bg.png"/></div><div class="item_name">' + data[i].child[k].name + '</div></div>');
                	}else{
                		htmlData.push('<div class="div_child_item_name" categoryid="' + data[i].child[k].categoryid + ' " name="'+ data[i].child[k].name +'"><div class="item_img"><img src="'+ data[i].child[k].imgurl +'" /></div><div class="item_name">' + data[i].child[k].name + '</div></div>');
                	}                    
                }
                htmlData.push('<div class="div_child_item_split"></div>');
            }
            if (data[i].child[NoArr[j+1] - 1].ishighlight == 1) {
            	if(data[i].child[k].imgurl == null || data[i].child[k].imgurl == undefined || data[i].child[k].imgurl == ""){
            		htmlData.push('<div class="div_child_item_name highlight" categoryid="' + data[i].child[NoArr[j+1] - 1].categoryid + ' " name="'+ data[i].child[NoArr[j+1] - 1].name +'"><div class="item_img"><img src="modifiable/img/loading_bg.png"/></div><div class="item_name">' + data[i].child[NoArr[j+1] - 1].name + '</div></div>');
            	}else{
            		htmlData.push('<div class="div_child_item_name highlight" categoryid="' + data[i].child[NoArr[j+1] - 1].categoryid + ' " name="'+ data[i].child[NoArr[j+1] - 1].name +'"><div class="item_img"><img src="'+ data[i].child[k].imgurl +'" /></div><div class="item_name">' + data[i].child[NoArr[j+1] - 1].name + '</div></div>');
            	}                
            }
            else {
            	if(data[i].child[k].imgurl == null || data[i].child[k].imgurl == undefined || data[i].child[k].imgurl == ""){
            		htmlData.push('<div class="div_child_item_name" categoryid="' + data[i].child[NoArr[j+1] - 1].categoryid + ' " name="'+ data[i].child[NoArr[j+1] - 1].name +'"><div class="item_img"><img src="modifiable/img/loading_bg.png"/></div><div class="item_name">' + data[i].child[NoArr[j+1] - 1].name + '</div></div>');
            	}else{
            		htmlData.push('<div class="div_child_item_name" categoryid="' + data[i].child[NoArr[j+1] - 1].categoryid + ' " name="'+ data[i].child[NoArr[j+1] - 1].name +'"><div class="item_img"><img src="'+ data[i].child[k].imgurl +'" /></div><div class="item_name">' + data[i].child[NoArr[j+1] - 1].name + '</div></div>');
            	} 
            }
            htmlData.push('</div>');
        }
        htmlData.push('</div>');
        
    }
    htmlData.push('</div>');
    
    $('.div_categories').append(htmlData.join(" "));

    $('.div_img').click(function() {
        window.location.href='search.html?categoryid=' + $(this).attr("categoryid") + '&name=' + $(this).attr("name");
    })

    $('.div_child_item_name').click(function() {
        window.location.href='search.html?categoryid=' + $(this).attr("categoryid") + '&name=' + $(this).attr("name");
    })
}

// 获取字符串长度 中文字计2 英文字符计1
function GetStrLen(str) {
    var len = 0;
    for (var i = 0; i < str.length; i++) {
        var c = str.charCodeAt(i);
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {
            len++;
        }
        else {
            len += 2;
        }
    }

    return len;
}

function InitSearchHistory() {


    if(fromPage == "index"){
        $('.div_search_history').show();
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

        //var htmlData = [];
        for (var i = 0; i < historyList.length; i++) {
            $("#id_ul_history").append('<li class="li_history_item"></li>');
            $("#id_ul_history li").eq(i).text( historyList[i]);
        }
        //$("#id_ul_history").append(htmlData.join(" "));

        $(".li_history_item")[xigou.events.click](function() {
            Search(this.innerHTML, historyList);
        })
    }

    $(".div_search")[xigou.events.click](function(){
        $('.ui-tab-content').hide();
        $('#input_search')[0].value = "";
        $('.div_search_history').show();
        $(".content").hide();
        $('#input_search').focus().select();
    })

    $('.cancal_search')[xigou.events.click](function() {
        $('.ui-tab-content').show();
        $('#input_search')[0].value = "";
        $('.div_search_history').hide();
        $('#input_search').blur();
        $(".content").show();
    })

    $('#input_search').keyup(function(e) {
        var a = $(this).val();
        if (a && a.length > 0) {
            $('.div_clear_input').show();
            if (13 == e.keyCode) {  // 开始搜索
                a = a.replace(/(^\s*)|(\s*$)/g, "");
                if (a.length > 0) {
                    $('.ui-tab-content').show();
                    Search(a, historyList);
                }
            }
        }
        else {
            $('.div_clear_input').hide();
        }
    });

    $('.div_clear_input')[xigou.events.click](function() {
        $('.div_clear_input').hide();
        $('#input_search')[0].value = "";
    })

    $('.div_clear_history')[xigou.events.click](function() {
        var dia=$.dialog({
            title:'',
            content:"确认清空输入的搜索历史吗？ ",
            button:["取消","确认"]
        });

        dia.on("dialog:action",function(eDlg){
            if(1 == eDlg.index)
            {
                $('#id_ul_history').empty();
                $('.div_history_list').hide();
                $('.div_noHistory').show();
            }
        });

        historyList = [];
        xigou.removelocalStorage("searchHistory");


    })
}

function Search(keyWords, historyList) {
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
    $('.div_search_history').hide();
     var sp = $(".div_group").attr('data');

    if(fromPage == "index"){
    		window.location.href="search.html?key=" + keyWords + "&fromPage=index"+"&sp="+sp;      
    }else{
        window.location.href="search.html?key=" + keyWords+"&sp="+sp;
    }

}