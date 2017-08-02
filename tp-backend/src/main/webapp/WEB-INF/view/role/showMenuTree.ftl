<#include "/common/common.ftl"/> 
<div class="mt10" id="forms">
    <div class="box">
    	<label style="display:none" class="error"></label>
      <div class="box_border">
      	<div class="box_top">
			<input type="button" value="提交" class="btn btn82 btn_save2 btn_submit" name="button">
		</div>
        <div class="box_center">
        	<div id="sysMenu_tree">
			</div>
        </div>
       </div>
      </div>
</div>
<script src="${tree}/jquery-ui.custom.min.js" type="text/javascript"></script>
<script src="${tree}/jquery.dynatree.js" type="text/javascript"></script>
<script>
var selectKeys = "";

$(function(){
	initTree();
	
	$(".btn_pre").bind("click",function(){
		$('.tabtit').children().eq(0).click();
	})
	
	$(".btn_submit").bind("click",function(){
		selectKeys = "";
		$("#sysMenuIds").val(getSelectMenus());
		$("#roleMenuLimit").val(getRoleMenuLimits());
		$("#sysMenuLimitIds").val(getSelectLimits());
		
		if($("#name").length>50){
			$(".error").html("角色名称过长,最多只能有50个字符");
			return false;
		}
		
		if($("#roleDesc").length>100){
			$(".error").html("角色描述过长,最多只能有100个字符");
			return false;
		}
		
		$(this).attr("disabled","disabled");
		 $.ajax({
            url: "${base}/permission/role/save",
            data: $("#saveRoleForm").serialize(),
            type:'POST',
            dataType:'json',
            success : function(result) {
				if (result.success) {
					$('.tabclose').click();
					loadRoleList();
				} else {
					$(this).removeAttr("disabled");
						$(".error").html(result.msg.message);
						$(".error").show();
				}
			}
        });
	})
});


function initTree(){
	$("#sysMenu_tree").dynatree({
		clickFolderMode:3, //点开节点模式1,激活,2扩大,3激活,扩大
		isLazy:true,
		checkbox:true,
		selectMode: 3,
		expand:false,
		fx: { height: "toggle", duration: 200 },
		initAjax: {
			url: domain+"/permission/role/loadSysMenuTree",
			data:{"parentId":null}
		},
		onLazyRead: function(node){
			node.appendAjax({
				url: domain+"/permission/role/loadSysMenuTree",
				data:{"parentId":node.data.key}
			});
		},
		strings: {
			loading: "加载中...",
			loadError: "加载超时"
		},
		onPostInit: function(isReloading, isError) {
			initSelect();     
			this.reactivate();    
		}
	});
}


function initSelect(){
	var sKeys = $("#roleMenuLimit").val();
	if(isNull(sKeys))return;
	var arr = sKeys.split(",");
	$.each(arr,function(i,n){
		var tree = $("#sysMenu_tree").dynatree("getTree");
		var no = tree.getNodeByKey(n+"");
		if(null!=no){
			no.select(true);
		}
	});
}



function getSysMenuIds(s,node){
	if(isNull(s)) s = "";
	s += ","+node.data.key;
	if(!isNull(node.data.parent)){
		var tree = $("#sysMenu_tree").dynatree("getTree");
		var n = tree.getNodeByKey(node.data.parent+"");
		if(null!=n){
			getSysMenuIds(s,n);
		}
	}
	return s;
}


function getSelectMenus(){
	var tree = $("#sysMenu_tree").dynatree("getTree");
	var selRootNodes = tree.getSelectedNodes(false);
	$.each(selRootNodes, function(i,node){
		if(0 == node.data.type) {
			selectKeys = getParent(node.data.key);
			
			
			/*var tree = $("#sysMenu_tree").dynatree("getTree");
			var n = tree.getNodeByKey(node.data.parent+"");
			if(null != n){
				selectKeys += getSysMenuIds(selectKeys,n);
			}*/
			
		}
	});
	return !isNull(selectKeys)?selectKeys.substring(1):"";
}

function getRoleMenuLimits(){
	var tree = $("#sysMenu_tree").dynatree("getTree");
	var selectKeys1 = "";
	var selRootNodes = tree.getSelectedNodes(false);
	$.each(selRootNodes, function(i,node){
		if(0 == node.data.type) {
			selectKeys1 += ","+node.data.key;
			/*var tree = $("#sysMenu_tree").dynatree("getTree");
			var n = tree.getNodeByKey(node.data.parent+"");
			if(null != n){
				selectKeys += getSysMenuIds(selectKeys,n);
			}*/
			
		}
	});
	return !isNull(selectKeys1)?selectKeys1.substring(1):"";
}

function getParent(key){
	
	var tree = $("#sysMenu_tree").dynatree("getTree");
	var node = tree.getNodeByKey(key+"");
	if(null != node){
		selectKeys += ","+key;
		getParent(node.data.parent);
	}
	
	return selectKeys;
}

function getSelectLimits(){
	var tree = $("#sysMenu_tree").dynatree("getTree");
	var selectKeys = "";
	var selRootNodes = tree.getSelectedNodes(false);
	$.each(selRootNodes, function(i,node){
		if(1 == node.data.type) selectKeys += ","+node.data.key;
	});
	return !isNull(selectKeys)?selectKeys.substring(1):"";
}


function strLength(str){
    var byteLen=0,len=str.length;
    if(str){
        for(var i=0; i<len; i++){
            if(str.charCodeAt(i)>255) byteLen += 2;
            else byteLen++;
        }
        return byteLen;
    }else return 0;
}
</script>