<#include "/common/common.ftl"/> 
<#include "/common/common-js.ftl"/>
<style>
/* tab模式 */
.tab{position:relative; display:none; width:800px;min-height:350px;max-height:700px; border:1px solid #ccc;overflow-y:auto;overflow-x:visible; }
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
</style>
<div class="container">
	<div class="box_border">
		<div class="box_top"><b class="pl15">用户列表&nbsp;</b><a class="editUser_btn ext_btn" href="javascript:void(0);"><span class="add"></span>新增用户</a></div>
		<div>
			<form id="userListForm" method="post">
				
			</form>
		</div>
	</div>
	<div class="tab">
	    <span class="tabmove"></span>
	    <div class="tabtit">
	        <span class="tabnow">编辑用户</span>
	    </div>
	    <ul class="tab_main">
	        <li class="tab-editUser">
	        
	        </li>
	    </ul>
	    <span class="tabclose" title="关闭">X</span>
	</div>
</div>
<script>
function loadUserList(){
	 $.ajax({
        url: "${base}/permission/user/userList",
        data: $("#userListForm").serialize(),
        type:'POST',
        dataType:'html',
        success : function(html) {
			$("#userListForm").html(html);
		}
    });
}


function loadEditUserManage(userId){
	$.post(domain+"/permission/user/editUserManage.htm",{userId:userId},function(html){
		$(".tab-editUser").html(html);
	},"html");
}

$(function(){
	loadUserList();
	$('.editUser_btn').live('click',function(){
		var userId = $(this).attr("data-id");
		loadEditUserManage(userId);
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
	
	//关闭层
	$('.tabclose').bind('click', function(){
	    layer.close(tabi);
	});
	
});

</script>
