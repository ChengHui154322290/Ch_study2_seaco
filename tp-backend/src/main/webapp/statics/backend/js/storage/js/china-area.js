/***
 * 配送区域树
 */
var ztreeId = "areaTree";
$(function(){
	var __a = areaData1.concat(areaData2);
	var __b = areaData3.concat(areaData4);
	var _areaData =__a.concat(__b);
	var areaTree = null;// 物料树
	var url="getAreas.htm"; 
	loadTree(_areaData,true);
	//加载树
	function loadTree(nodes,flag){
		//初始化tree
		var setting = {
			check: {
				enable: true
			},
			data: {
				simpleData: {
					enable: true
				}
			}
		};
		
		if(_areaData!=undefined){
			areaTree = $.fn.zTree.init($("#areaTree"),setting,_areaData);
			var ids = $("#selectAreaIds").val();
			treeExpandNode(3);//展开第三级
			if(ids!=""){
				setTreeChecked(ids,ztreeId);
			}
			return ;
		}
		
		if(_areaData==undefined){
			//物料树后台Ajax路径
			$.ajax({
				type : 'POST',
				url : url,
				async: flag, 
				dataType : "json",
				success : function(result) {
					areaTree = $.fn.zTree.init($("#areaTree"),setting, result);
					var ids = $("#selectAreaIds").val();
					treeExpandNode(3);//展开第三级
					if(ids!=""){
						setTreeChecked(ids,ztreeId);
					}
				}
			});
		}
		
		
	};
	
	/**
	 * 树点击事件
	 */
	function zTreeOnClick(e,treeId,treeNode){
		areaTree = $.fn.zTree.getZTreeObj(ztreeId);
		areaTree.selectNode(treeNode,false);
		if(treeNode){
			var type = treeNode.type; //级别
			if(type==6){
				var streetUrl = "getStreets.htm?id=" + treeNode.id;
				$.ajax({
					type : 'POST',
					url : streetUrl,
					async: false, 
					dataType : "json",
					success : function(result) {
						var treeObj = $.fn.zTree.getZTreeObj(ztreeId);
						var nodes =result;
						if (nodes.length>0) {
							treeObj.reAsyncChildNodes(result, "refresh");
						}
					}
				});
			}
		}
		
	}
	
	/**
	 * 指定节点的树展开
	 */
	function treeExpandNode(length){
		//遍历层级的节点，展开
		var nodes = areaTree.getNodesByParam("type",length, null); 
		for( var i = 0 ; i < nodes.length;i++){
			areaTree.expandNode(nodes[i], true, false, true);
		}
	}
});

/**
 * 
 */
function  getSelectAreaIds() {
	$("#selectAreaIds").val("");
	var returnobj = new Array(); 
	var treeObj = $.fn.zTree.getZTreeObj(ztreeId);
	var nodes = treeObj.getCheckedNodes(true);
	
	if (returnobj ==null){
		returnobj = "" ;
	}else{
		for(var i = 0 ; i < nodes.length ; i++){	
			var type = nodes[i].type; //层级
			var selectState = nodes[i].check_Child_State ; //状态 2-全选 1半选
			if(type == 2){//全国
				if(selectState==2){
					returnobj.push(nodes[i].id);
					$("#selectAreaIds").val(nodes[i].id);
					return ;
				}
			}else if(type == 3){ //华北区
				if(selectState==2){
					returnobj.push(nodes[i].id);
					var ids = [] ;
					ids = getChildren(ids,nodes[i]);
					i=i+ids.length-1;
				}
				
			}else if(type == 4){ //省
				if(selectState==2){
					returnobj.push(nodes[i].id);
					var ids = [] ;
					ids = getChildren(ids,nodes[i]);
					i=i+ids.length-1;
				}
			}else if(type == 5){ //市
				if(selectState==2){
					returnobj.push(nodes[i].id);
					var ids = [] ;
					ids = getChildren(ids,nodes[i]);
					i=i+ids.length-1;
				}else if(selectState==-1){
					returnobj.push(nodes[i].id);
				}
				
			}else if(type == 6){//县
				if(selectState==2){
					returnobj.push(nodes[i].id);
					var ids = [] ;
					ids = getChildren(ids,nodes[i]);
					i=i+ids.length-1;
				}else if(selectState==-1){
					returnobj.push(nodes[i].id);
				}
				
			}else if(type == 7){//街道
				returnobj.push(nodes[i].id);
			}
		} 
	}
	var ids = returnobj.join(",");
	$("#selectAreaIds").val(ids);
}


function getChildren(ids,treeNode){
	ids.push(treeNode.id);
	if (treeNode.isParent){
		for(var obj in treeNode.children){
			getChildren(ids,treeNode.children[obj]);
		}
	}
	return ids;
}


/*
*默认选中
*/
function setTreeChecked(id,divid){		
	if(id&&id==""){
		return ;
	}
	//如果id为空，自动返回
	if(id.indexOf(',') == -1){
		var treeObj = $.fn.zTree.getZTreeObj(divid);
		treeObj.checkNode(treeObj.getNodeByParam("id", id, null), true, true);
		return ;
	}
	var cids = id.split(",");	
	for(var j = 0; j < cids.length; j++){
			var treeObj = $.fn.zTree.getZTreeObj(divid);
			treeObj.checkNode(treeObj.getNodeByParam("id", cids[j], null), true, true);
	}
}



