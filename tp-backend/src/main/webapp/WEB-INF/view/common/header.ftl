<#include "/common/common.ftl"/>
<style>
.tab{position:relative; display:none; width:600px;min-height:350px;max-height:600px; border:1px solid #ccc;overflow-y:auto;overflow-x:visible; }
.tabmove{position:absolute; width:600px; height:30px; top:0; left:0;}
.tabtit{ display:block; height:30px; border-bottom:1px solid #ccc; background-color:#eee;}
.tabtit span{position:relative; float:left; width:120px; height:30px; line-height:30px; text-align:center; cursor:pointer;}
.tabtit span.tabnow{left:-1px; _top:1px; height:31px; border-left:1px solid #ccc; border-right:1px solid #ccc; background-color:#fff; z-index:10;}
.tab_main{padding:20px;  clear:both;background-color: #fff;}
.tabclose{position:absolute; right:10px; top:7px; cursor:pointer;}
</style>
<div id="north">
	<div id="logo"></div>
	<div class="cb"></div>
	<div id="north_right">
		<@shiro.guest>
				
		</@shiro.guest>
		<@shiro.user>
			<a href="#"><img class="user-icon" src="${base}/statics/backend/images/a.png" alt=""></a>
			&nbsp;&nbsp;当前用户：<@shiro.principal property="userName" />
			&nbsp;&nbsp;角色： <@shiro.principal property="roleName" />
			[<a class="updatePassword" href="javascript:void(0);">修改密码</a>]
			[<a class="updateUserInfo" href="javascript:void(0);">用户信息</a>]
			[
			<a href="${base}/logout">退出</a>]
		</@shiro.user>
		
	</div>
	<ul id="nav_top">
		<li><a href="javascript:void(0);"   class="select" onclick="showHome();return false;">首&nbsp;&nbsp;页</a></li>
	</ul>
</div>
<div id="north-seperate">
	<div class="icon-h icon-top">
		图标
	</div>
</div>
<div class="tab updatePassword-tab">
    <span class="tabmove"></span>
    <div class="tabtit">
        <span class="tabnow">修改密码</span>
        <span><label class="error updatePass-errorMessage"></label></span>
    </div>
    <ul class="tab_main">
        <li class="tab-updatePassword">
        
        </li>
    </ul>
    <span class="tabclose" title="关闭">X</span>
</div>

<div class="tab updateUserInfo-tab">
    <span class="tabmove"></span>
    <div class="tabtit">
        <span class="tabnow">用户信息</span>
        <span><label class="error updateUserInfo-errorMessage"></label></span>
    </div>
    <ul class="tab_main">
        <li class="tab-updateUserInfo">
        
        </li>
    </ul>
    <span class="tabclose" title="关闭">X</span>
</div>
<script>

function loadUpdatePassword(){
	$(".updatePass-errorMessage").html("");
	$.post(domain+"/permission/user/toUpdatePassword.htm",null,function(html){
		$(".tab-updatePassword").html(html);
	},"html");
}

function loadUpdateUserInfo(){
	$(".updateUserInfo-errorMessage").html("");
	$.post(domain+"/permission/user/toUpdateUserInfo.htm",null,function(html){
		$(".tab-updateUserInfo").html(html);
	},"html");
}

$(function(){
	$('.updatePassword').live('click',function(){
	
		loadUpdatePassword();
		
         tabi = $.layer({
		        type:1,
		        offset: ['50px', ''],
		        border: [0],
		        area: ['602px', 'auto'],
		        title: false,
		        shade : [0.7 , '#000' , true],
		        move: '.tabmove',
		        closeBtn: false,
		        page: {dom: '.updatePassword-tab'}
		    });    
        
	}); 
	
	$('.updateUserInfo').live('click',function(){
	
		loadUpdateUserInfo();
		
         tabi = $.layer({
		        type:1,
		        offset: ['50px', ''],
		        border: [0],
		        area: ['602px', 'auto'],
		        title: false,
		        shade : [0.7 , '#000' , true],
		        move: '.tabmove',
		        closeBtn: false,
		        page: {dom: '.updateUserInfo-tab'}
		    });    
        
	}); 
	
	bindClick();
	
});

function bindClick(){
//切换事件
	var btn = $('.tabnow');
	var main = $('.tab_main').children()
	var close = $('.tabclose');
	$(btn).on('click', function(){
	    var othis = $(this), index = othis.index();
	    othis.addClass('tabnow').siblings().removeClass('tabnow');
	    main.eq(index).show().siblings().hide();
	});
	
	
	//关闭层
	$(close).on('click', function(){
	    layer.close(tabi);
	});

};
</script>