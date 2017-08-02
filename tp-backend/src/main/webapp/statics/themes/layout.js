var northHeight,eastWidth;
var durationTime=100;
var eastStatus=true;//true表示当前状态为显示
var northStatus=true;//true表示当前状态为显示
var fullScreenStatus=false;//false表示当前状态为：不是全屏
var basePath = null;
$(document).ready(function () {
	basePath = domain;
	resizeContainer();
	initMenu();
	$(window).resize(function () {
		resizeContainer();
	});
	$("#north-seperate .icon-h").click(function(){
		if(northStatus===true){
			$(this).addClass("icon-bottom");
			northHeight=$("#north").height();
			$("#north").height(0);
			$("#north").hide();
			northStatus=false;
			resizeContainer();
		}else{
			$(this).removeClass("icon-bottom");
			$("#north").height(northHeight);
			$("#north").show();
			northStatus=true;
			resizeContainer();
		}
	});
	$("#east-seperate .icon-v").click(function(){
		if(eastStatus===true){
			$(this).addClass("icon-right");
			eastWidth=$("#east").width();
			$("#east").width(0);
			$("#east").hide();
			eastStatus=false;
			resizeContainer();	
		}else{
			$(this).removeClass("icon-right");
			$("#east").width(eastWidth);
			$("#east").show();
			eastStatus=true;
			resizeContainer();
		}
	});
	
		//全屏控制
	$("#full_screen_btn").click(function(){
		if(fullScreenStatus){
			$(this).attr('title','全屏');
			$("#east-seperate .icon-v").click();
			$("#north-seperate .icon-h").click();
			fullScreenStatus=false;
		}else{
			$(this).attr('title','退出全屏');
			$("#east-seperate .icon-v").click();
			$("#north-seperate .icon-h").click();
			fullScreenStatus=true;
		}
	});
	
	$(".east-menu h2").live('click',function(){
		
		if($(this).next("ul").is(":hidden")){
			$(this).next("ul").slideDown(400);
			$(this).children("a").css("background-position","15px 6px");
		}else{
			$(this).next("ul").slideUp(400);
			$(this).children("a").css("background-position","15px -33px");
		}
		
	});
	
	//tabs 分页控制
	$("#btn_tabs_prev").click(function(){
		//找到当前页
		var current;
		$("#menubar_tabs").find("a").each(function(i,n){
		
			var cls=$(this).attr("class");
			if(cls.indexOf("currenttab") != -1){
				current=$(this);
				return false;
			}
	    });
	   
	   	var prev=$(current).parent().prev();
	    //找上一页
	    var currentId=$(prev).children("a").attr("id");
	    if(currentId){
	    	//找到权限id
	    	//currentId=currentId.substring(6,currentId.length);
	    	//翻页
	    	changeContentTab(currentId);
	    }else{
	    	$.msgbox({msg:"已经是第一页了",icon:"warning"});
	    }
	
	});
	
	$("#btn_tabs_next").click(function(){
		//找到当前页
		var current;
		$("#menubar_tabs").find("a").each(function(){
			var cls=$(this).attr("class");
			if(cls.indexOf("currenttab") != -1){
				current=$(this);
				return false;
			}
	    });
	   var next=$(current).parent().next();
	    //找下一页
	    var currentId=$(next).children("a").attr("id");
	    if(currentId){
	    	//找到权限id
	    	//currentId=currentId.substring(6,currentId.length);
	    	//翻页
	    	changeContentTab(currentId);
	    }else{
	    	$.msgbox({msg:"已经是最后一页了",icon:"warning"});
	    }
	});
	
});

var menu;
var currTab = "tabli_0";
var manuMenuId="#nav_top";
//初始化系统菜单
function initMenu() {
	menu_url = domain+"/loadAdminRights?m="+Math.random(); 
	
	$.post(menu_url,null,function (data) {
		menu = $.parseJSON(data);
		initMainMenu();
	},"text");
}
			
//初始化主导航
function initMainMenu() {
	//$(manuMenuId).empty();
	$.each(menu, function (k, v) {
		var item = $("<li><a href=\"javascript:;\" title='"+v.text+"' id=\"tab_" + v.id + "\">" + v.text + "</a></li>");
		item.children("a").click(function () {
			var tabName = this.id.substr(4);
			switchTab(tabName);
			$("#nav_top").find("a").removeClass("select");
			$(this).addClass("select");
			
			//触发第一个选中
			$("#east").find("ul").children("li:eq(0)").children("a").click();
		});
		$(manuMenuId).append(item);
	});
	switchFirstTab();
}

function switchFirstTab(){
	for(var i=0;i<menu.length;i=i+1){
		currTab = menu[i].id;
		pickTab();
		initSubmenu();
		break;
	}
}
function switchTab(tabName) {
	currTab = tabName;
	pickTab();
	initSubmenu();
}
function pickTab() {
	var id = "#tab_" + currTab;
	$(manuMenuId).find("a").each(function () {
		$(this).removeClass("cur");
	});
	$(id).addClass("cur");
}

//初始化子导航
function initSubmenu() {
	var m = 0;
	for(var i=0;i<menu.length;i=i+1){
		if(currTab == menu[i].id){
			m = menu[i];
		}
	}	
	$("#east").empty();	
	$.each(m.children, function (k, v) {		
		var wElement=$("<div class=\"east-menu\"><h2><a href=\"javascript:void(0);\">" + v.text + "</a></h2><ul style=\"display: none;\" class=\"bcw\"></ul></div>");	
		$.each(v.children, function (tk, tv) {			
			var liElement=$("<li class=\"bcw\"></li>");	
			var aElement=$("<a href=\"javascript:;\" title='"+tv.text+"' id=\"nav_rights_"+tv.id+"\" >"+tv.text+"</a>");			
			$(aElement).click(function(){
				tv.linkId = $(this).attr("id");
				tv.tabId =  "tabli_"+tv.id;
				showTab(tv);
			});
			$(liElement).append(aElement);
			$(wElement).find("ul").append(liElement);
		});
		$("#east").append(wElement);
	});
}
//打开指定URL的页面
function showMyTab(url){
	for(var i=0;i<menu.length;i++){
		var m=menu[i];
		for(var x=0;x<m.children.length;x++){
			var mc=m.children[x];
			for(var y=0;y<mc.children.length;y++){
				var tmc=mc.children[y];
				if(tmc!=null&&tmc.url==url){
					tmc.tabId = "tabli_"+tmc.id;
					showTab(tmc);
					return;
				}
			}
		}
	}
}
//切换选项卡
function changeContentTab(tabId){
	//取消所有tab的选中状态
	$("#menubar_tabs").find("a").removeClass("currenttab");
	$("#menubar_tabs").find("a span").addClass("b_gray");
	
	$("#"+tabId).addClass("currenttab");
	//隐藏所有的div
	$("#west iframe").hide();
	$("#mainIframe_"+tabId).show();
	
	var cur=$("#"+tabId).parent();
    if($(cur).html()){
   		var ot=$(cur).attr("offsetTop");
		$("#menubar_tabs").attr("scrollTop",ot);
   	}
}

//关闭指定选项卡
function closeTab(tabId,event){
	//判断要关闭的tab是否是当前tab
	var tab = $("#"+tabId);
	var cls = tab.attr("class");
	if(cls == "currenttab"){
		//存在前一个,高亮它
		//存在后一个,高亮它
		var p=tab.parent().next();
		if(!p || p.length == 0){
			p = tab.parent().prev();	//存在后一个
		}
		
		if(p && p.length != 0){
			$(p).children("a").addClass("currenttab");
			
		//	$(p).find("span").removeClass("b_gray");
			var preId = $(p).children("a").attr("id");
			$("#mainIframe_"+preId).show();
			
			var ot=$(p).attr("offsetTop");
			$("#menubar_tabs").attr("scrollTop",ot);
		}
	}
	tab.parent().remove();
	$("#mainIframe_"+tabId).remove();
	if (event && event.stopPropagation){
      	event.stopPropagation();
    }else{
       	window.event.cancelBubble=true;
    }
}

function showHome(){
	//显示首页旁边的一级菜单中的二级菜单内容
	$("#nav_top li:eq(1)").children("a").click();
	
	//隐藏所有的iframe
	$("#west iframe").hide();
	//取消所有tab的选中状态
	$("#menubar_tabs").find("a").removeClass("currenttab");
	
	//显示
	$("#tabli_0").addClass("currenttab");
	$("#tabli_0").find("span").removeClass("b_gray");
	$("#mainIframe_0").show();
	
	//定位到指定tab
	var ot=$("#tabli_0").parent().attr("offsetTop");
	$("#menubar_tabs").attr("scrollTop",ot);
	$("#nav_top").find("a").removeClass("select");
	$("#nav_top").find("a").first().addClass("select");
	currTab = "tabli_0";
}

//追加iframe
function appendIframe(element,iframe,url){
	$(element).append(iframe);
}
//显示iframe
function showIframe(iframeId,url){
	$("#"+iframeId).show();
	if(url){
		document.getElementById(iframeId).src = url; 
	}
}
//选中A标签
function selectLink(linkId){
	$("#east").find("ul").children("li").children("a").removeClass("select");
	$("#"+linkId).addClass("select");
}

function resizeContainer() {
	var wheight = $(window).height();
	var northH =  $("#north").height();
	//最后的6是分割线的高度
	var h = wheight - northH - 6;
	$("#main").height(h);
	var wwidth = $(window).width();
	var eastW = $("#east").width();
	if(eastW<0){
		eastW = 0;
	}
	//最后的5是分割线的高度
	var w;
	//IE6乃万恶之源
	if($.browser.msie && parseInt($.browser.version) <= 6){
		w = wwidth - eastW - 16;
	}else{
		w = wwidth - eastW- 6;
	}
	
	$("#west").width(w);
}

function closeCurrentWin(){
	var iframe = $("#west").find("iframe:visible");
	var iframeId = iframe.attr("id");
	var tabId = iframeId.substring(11);	//mainIframe_tabli_specialapproval_1
	closeTab(tabId);
}

/**
 * tv={url:'',tabId:'',linkId:'',text:''}
 * @param tv
 */
function showTab(tv){
	if(tv.url==null||tv.url.length==0){
		return;
	}
	var linkId = tv.linkId;
	var tabId = tv.tabId;
	var doc = tv.doc;
	if(!doc){
		doc = document;
		$("#east",doc).find("a").removeClass("select");
		$(this).addClass("select");
	}
	//隐藏所有的iframe
	$("#west iframe",doc).hide();
	//取消所有tab的选中状态
	$("#menubar_tabs",doc).find("a").removeClass("currenttab");
	$("#menubar_tabs",doc).find("span").addClass("b_gray");
	
	//查找是否存在此tab
	var exsTab = $("#"+tabId,doc);
	if(exsTab && exsTab.length > 0){
		exsTab.addClass("currenttab");
		exsTab.find("span").removeClass("b_gray");
		var iframeId = "mainIframe_"+tabId;
		if(tv.refresh){
			top.window.showIframe(iframeId,tv.url);	
		}else{
			top.window.showIframe(iframeId);
		}
		
	}else{
		//增加tab
		var linked = "<h3><a id=\""+tabId+"\" class=\"currenttab\" href=\"javascript:void(0);\" title=\""+tv.text+"\" onclick=\"changeContentTab('"+tabId+"','"+tabId+"');\">"+tv.text+"<span onclick=\"closeTab('"+tabId+"',event);\" class='b_gray' title='关闭'></span></a></h3>";
		$("#menubar_tabs",doc).append(linked);
		var iframeUrl = domain+tv.url;
		if(doc != document){
			var ifr = "<iframe  id=\"mainIframe_"+tabId+"\" class=\"mainIframe\" frameborder=\"0\" name=\"mainIframe_"+tabId+"\" src=\""+iframeUrl+"\"></iframe>";
			top.window.appendIframe("#west",ifr);
		}else{
			var ifr = $("<iframe  id=\"mainIframe_"+tabId+"\" class=\"mainIframe\" frameborder=\"0\" name=\"mainIframe_"+tabId+"\" ></iframe>");
			$("#west",doc).append(ifr);	
			
			$(ifr,doc).attr('src',iframeUrl);
			
		}
		
		
		//tabs定位到最后一个
		var last=$("#menubar_tabs",doc).find("h3").last();
   		var ot=$(last,doc).attr("offsetTop");
		$("#menubar_tabs",doc).attr("scrollTop",ot);
	}
	
	if(doc == document){
		resizeContainer();	
	}
	
	$("#"+tabId,doc).addClass("currenttab");
	
	
	//定位到指定tab
	var ot=$("#"+tabId,doc).parent().attr("offsetTop");
	$("#menubar_tabs",doc).attr("scrollTop",ot);
	$("#menubar_tabs",doc).find("span").addClass("b_gray");
	
	if(linkId){
		top.window.selectLink(linkId);
	}
}



/**
 * 消息提示
 */
//msg:消息提示文字
//icon:提示小图标,可选值 success, error ,warning, info
//time:持续时间,毫秒
(function($) {
	var $msgbox = function(options) {
		var defaults = {
			msg : '操作失败',
			icon : 'clear',
			time : '2000',
			callBack : null
		};
		var settings = jQuery.extend(defaults, options);
		var tipiconclass = "gtl_ico_" + settings.icon;
		$('#ts_Msgbox').remove();
		var box = "<div class=\"ts_msgbox_layer_wrap\" id=\"ts_Msgbox\" style=\"display:none\"><span class=\"ts_msgbox_layer\" style=\"z-index: 10000;\" id=\"mode_tips_v2\"><span class=\""
				+ tipiconclass
				+ "\"></span>"
				+ settings.msg
				+ "<span class=\"gtl_end\"></span></span></div>";
		$("body").append(box);
		$('#ts_Msgbox').fadeIn();
		window.setTimeout(function() {
					$('#ts_Msgbox').fadeOut(function() {
						if (settings.callBack != null
								&& typeof settings.callBack == 'function') {
							settings.callBack();
						}
					});
				}, settings.time);
	}
	$.msgbox = function(options) {
		return new $msgbox(options);
	}
	return $.msgbox;
})(jQuery);
