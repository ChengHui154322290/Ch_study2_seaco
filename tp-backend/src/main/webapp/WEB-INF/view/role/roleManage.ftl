<#include "/common/common.ftl"/> 
<#include "/common/common-js.ftl"/>
<style>
/* tab模式 */
.tab{position:relative; display:none; width:600px;min-height:350px;max-height:600px; border:1px solid #ccc;overflow-y:auto;overflow-x:visible; }
.tabmove{position:absolute; width:600px; height:30px; top:0; left:0;}
.tabtit{ display:block; height:30px; border-bottom:1px solid #ccc; background-color:#eee;}
.tabtit span{position:relative; float:left; width:120px; height:30px; line-height:30px; text-align:center; cursor:pointer;}
.tabtit span.tabnow{left:-1px; _top:1px; height:31px; border-left:1px solid #ccc; border-right:1px solid #ccc; background-color:#fff; z-index:10;}
.tab_main{padding:20px;  clear:both;background-color: #fff;}
.tab_main .tab-setRoleInfo{display:none;}
.tab_main .tab-setMenu{display:none;}
.tab_main .tab-setLimit{display:none;}
.tab_main li.tab-setRoleInfo{display:block;}
.tabclose{position:absolute; right:10px; top:7px; cursor:pointer;}

body {
	overflow:scroll;
}

</style>

<link type="text/css" rel="stylesheet" href="${validation}/jquery.validate.min.css"  />
<script src="${tree}/jquery.cookie.js" type="text/javascript"></script>
<script src="${contextMenu}/jquery.contextMenu.js" type="text/javascript"></script>
<link href="${contextMenu}/jquery.contextMenu.css" rel="stylesheet" type="text/css">
<link href="${tree}/skin-vista/ui.dynatree.css" rel="stylesheet" type="text/css">

<div class="container">
	<div class="box_border">
		<div class="box_top"><b class="pl15">角色列表&nbsp;</b><a class="editRole_btn ext_btn" href="javascript:void(0);"><span class="add"></span>新增角色</a></div>
		<div class="roleList">
		</div>
	</div>
	
	<div class="tab">
	    <span class="tabmove"></span>
	    <div class="tabtit">
	        <span class="tabnow">角色详细信息</span>
	        <span>设置权限</span>
	    </div>
	    <ul class="tab_main">
	        <li class="tab-setRoleInfo">
	        
	        </li>
	        <li class="tab-setMenu">
	        
	        </li>
	    </ul>
	    <span class="tabclose" title="关闭">X</span>
	</div>
</div>

<script>
function loadRoleList(){
	var pageNo = $("#pageNo").val();
	var pageSize = $("#pageSize").val();
	$.post("${base}/permission/role/roleList",{pageNo:pageNo,pageSize:pageSize},function(html){
		$(".roleList").html(html);
	},"html");
}

function loadEditRoleManage(roleId){
	$.post(domain+"/permission/role/editRoleManage.htm",{roleId:roleId},function(html){
		$(".tab-setRoleInfo").html(html);
	},"html");
}

function loadRoleTree(roleId){
	$.post(domain+"/permission/role/showMenuTree",{roleId:roleId},function(html){
		$(".tab-setMenu").html(html);
	},"html");
}

$(function(){
	loadRoleList();
	$('.editRole_btn').live('click',function(){
	
		var roleId = $(this).attr("data-id");
		loadEditRoleManage(roleId);
		loadRoleTree(roleId);
		
		$('.tabtit').children().eq(0).click();
		 /* pageii=$.layer({
            type : 2,
            title: '角色->编辑',
            shadeClose: false,
            maxmin: true,
            fix : true,  
            area: ['800px', '400px'],                     
            iframe: {
                src : domain+'/permission/role/editRoleManage.htm'
            }
        });*/
         tabi = $.layer({
		        type:1,
		        offset: ['50px', ''],
		        border: [0],
		        area: ['602px', 'auto'],
		        title: false,
		        shade : [0.7 , '#000' , true],
		        move: '.tabmove',
		        closeBtn: false,
		        page: {dom: '.tab'}
		    });    
        
	}); 
	
	bindClick();
	
});

function bindClick(){
//切换事件
	var btn = $('.tabtit').children();
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
