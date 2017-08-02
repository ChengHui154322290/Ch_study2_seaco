/**
 * 页面的加载后需执行的js
 * 主要用于页面的初始化操作
 */
jQuery(function(){
	
//	$('.nav-tabs').on('click', 'i', function(){
//		var link = $(this).parent();
//		$( link.attr('href') ).remove();
//		link.remove();
//	});
//	.on('mouseenter', 'li' ,function(){
//		if( $(this).hasClass('active') ){
//			return;
//		}
//		$(this).find('i').show();
//	}).on('mouseleave', 'li', function(){
//		$(this).find('i').hide();
//	});
//	
    $('#accordion').on('click', 'a', function(e){
        e.preventDefault();
        var id = $(this).attr('data-id');
        var name = $(this).find("span").html();
        newTab(id,name,$(this).attr('href'));
        $(this).siblings("a").removeClass("active");
        $(this).addClass("active");
    });
    
});

/**
 * 刷新首页tab
 */
function refreshIndex(){
	$('#accordion').find("a").removeClass("active");
	refreshTab("index_page","/seller/index_content");
}

/**
 * 新增一个tab
 * 
 * 
 * @param id
 */
function newTab(id,name,url) {
	$('.nav-tabs').find("li").removeClass("active");
    var  selObj = $('.nav-tabs').find("li a[href='#"+id+"']");
    if(selObj.html()){
    	selObj.parent().addClass("active");
    } else {
    	$('.nav-tabs').append( $('<li role="presentation" class="active"><a href="#'+ id +'" role="tab" data-toggle="tab">'+ name +'<i class="glyphicon glyphicon-remove"><\/i><\/a><\/li>') );
    	reigsterTabChangeEvent($('.nav-tabs').find("a[href='#"+id+"']"),url);
    	$("i.glyphicon-remove").on('click',function(){
    		var id = $(this).parent().attr("href");
    		var div = id.toString().replace("#","");
    		 $("div[id='"+div+"']").remove();  
    		 $(this).parent().remove();
    		 var  selObj = $('.nav-tabs').find("li a");
    		 var tabSize=selObj.length;
    		 $(selObj[tabSize-1]).parent().addClass("active");
    		 $(selObj[tabSize-1]).click();
    	});
    	
    }
    refreshTab(id,url);
}

/*function loadContent(url,id){
	$.get(url, function(data) {
       $('#content').html( $('<div role="tabpanel" class="tab-pane active" id="'+ id +'">').html(data))
    });
}*/

/**
 * tab刷新
 */
function refreshTab(id,url){
	$.get(url, function(data) {
		if(data){
			var aStr = "<!--isLoginPage-->";
			if(data.toString().length>18){
				if(aStr == data.toString().substring(0,18)){
					window.location.href="/tologin?fm=re";
				} else {
					$('#content').html( $('<div role="tabpanel" class="tab-pane active" id="'+ id +'">').html(data));
				}
			}
	        
		}
    });
}

/**
 * 点击tab切换
 * 此代码修改需要注意事件不能循环调用
 */
function reigsterTabChangeEvent(a_obj,url){
	var id = $(a_obj).attr("href");
	var menuId = id.toString().replace("#","");
	$(a_obj).click(function(){
		if($("#accordion").find("a[data-id='"+menuId+"']").attr("href")){
			$("#accordion").find("a[data-id='"+menuId+"']").click();
		} else {
			refreshTab(id,url);
		}
	});
}

