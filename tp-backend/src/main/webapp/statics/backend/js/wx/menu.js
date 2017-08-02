$(function(){
	var setting = {
			async: {
                enable: true,
                url:domain + '/wx/menu/convTree.htm'
            },
			view: {
				dblClickExpand: false
			},
			check: {
				enable: false
			},
			callback: {
				onRightClick: OnRightClick,
				onClick: onClick
			}
		};

		var zNodes =[
			{id:1, name:"小西锦囊",type:"click",key:"KF", open:true},
			{id:2, name:"右键操作 2", open:true,
				children:[
					   {id:21, name:"节点 2-1"},
					   {id:22, name:"节点 2-2"},
					   {id:23, name:"节点 2-3"},
					   {id:24, name:"节点 2-4"}
				]},
			{id:3, name:"右键操作 3", open:true,
				children:[
					   {id:31, name:"节点 3-1"},
					   {id:32, name:"节点 3-2"},
					   {id:33, name:"节点 3-3"},
					   {id:34, name:"节点 3-4"}
				]}
  	 	];

		function OnRightClick(event, treeId, treeNode) {
			if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
				zTree.cancelSelectedNode();
				showRMenu("root", event.clientX, event.clientY);
			} else if (treeNode && !treeNode.noR) {
				zTree.selectNode(treeNode);
				showRMenu("node", event.clientX, event.clientY);
			}
		}
		function showRMenu(type, x, y) {
			$("#rMenu ul").show();
			if (type=="root") {
				$("#m_add").show();
				$("#m_del").hide();
			} else {
				$("#m_del").show();
				$("#m_add").show();
			}
			rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

			$("body").bind("mousedown", onBodyMouseDown);
		}
		function hideRMenu() {
			if (rMenu) rMenu.css({"visibility": "hidden"});
			$("body").unbind("mousedown", onBodyMouseDown);
		}
		function onBodyMouseDown(event){
			if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
				rMenu.css({"visibility" : "hidden"});
			}
		}
		function onClick(event, treeId, treeNode, clickFlag) {
			$("#editMenuDiv").show();
			$("#name").val(null);
			$("#id").val(null); 
			$("#pid").val(null); 
			$("#parentName").val(null);
			$("#cKey").val(null);
			$("#vUrl").val(null);
			$("#type").val(null);
			var nodes = zTree.getSelectedNodes()[0];
			var parentNode = nodes.getParentNode();
			$("#name").val(nodes.name);
			$("#id").val(nodes.id); 
			$("#pid").val(nodes.pid); 
			$("#tr_type").show(); 
			$("#type option[value='"+nodes.type+"']").attr("selected", "selected");  
			if(parentNode == null){
				$("#tr_parentName").hide(); 
				$("#tr_url").hide(); 
				$("#tr_key").hide(); 
			}else{
				$("#parentName").val(parentNode.name);
				$("#tr_parentName").show();
			}
			if(nodes.vUrl){
				$("#vUrl").val(nodes.vUrl);
				$("#tr_url").show(); 
				$("#tr_key").hide(); 
			}
			if(nodes.cKey){
				$("#cKey").val(nodes.cKey);
				$("#tr_url").hide(); 
				$("#tr_key").show(); 
			}
		}
		
		$("#m_add").click(function(){
			hideRMenu();
			$("#editMenuDiv").show();
			$('#saveMenuForm')[0].reset();
			$("#id").val(null); 
			var nodes = zTree.getSelectedNodes();
			if (nodes && nodes.length >0) {
				var parentNode = nodes[0].getParentNode();
				if(parentNode == null){
					$("#pid").val(nodes[0].id); 
					$("#tr_parentName").show(); 
					$("#tr_name").show(); 
					$("#tr_type").show(); 
					$("#tr_url").hide(); 
					$("#tr_key").hide();
					$("#parentName").val(nodes[0].name);
				}else{
					layer.alert("自定义菜单只支持两级菜单");
				}
			} else {
				$("#pid").val(0); 
				$("#tr_parentName").hide(); 
				$("#tr_name").show(); 
				$("#tr_type").show(); 
				$("#tr_url").hide(); 
				$("#tr_key").hide();
			}
		});
		
		$("#m_del").click(function(){
			hideRMenu();
			var nodes = zTree.getSelectedNodes();
			if (nodes && nodes.length>0) {
				if (nodes[0].children && nodes[0].children.length > 0) {
					layer.confirm('您确认删除该菜单么,该菜单为父菜单,如果删除将连同子菜单一起删除?', function(){
						delMenu(nodes[0].id,nodes[0].pid);
						zTree.removeNode(nodes[0]);
					});
				} else {
					layer.confirm('您确认删除该菜单么?', function(){
						delMenu(nodes[0].id,nodes[0].pid);
						zTree.removeNode(nodes[0]);
					});
				}
			}
		});
		
		function delMenu(id,pid){
			$.ajax({
		        cache: true,
		        type: "GET",
		        url:domain + '/wx/menu/del.htm',
		        data:{id:id,pid:pid},
		        async: false,
		        error: function(request) {
		            alert("Connection error");
		        },
		        success: function(result) {
		        	if(result.success){
		        		pageii = layer.alert("删除成功", 1, function(){
		        			layer.close(pageii);
		        			$('#saveMenuForm')[0].reset();
		        			$("#editMenuDiv").hide();
		        			//zTree.reAsyncChildNodes(null, "refresh");
		                })
		        	}else{
		        		layer.alert(result.msg.message, 8);
		        	}
		        }
		    });
		}

		var zTree, rMenu;
		$(document).ready(function(){
			$("#editMenuDiv").hide();
			$.fn.zTree.init($("#menuTree"), setting);
			zTree = $.fn.zTree.getZTreeObj("menuTree");
			rMenu = $("#rMenu");
		});
		
		$('#type').change(function(){ 
			var p1=$(this).children('option:selected').val();
			if(p1 == 'view'){
				$("#tr_url").show(); 
				$("#tr_key").hide(); 
			}else if(p1 == 'click'){
				$("#tr_url").hide(); 
				$("#tr_key").show(); 
			}else {
				$("#tr_url").hide(); 
				$("#tr_key").hide();  
			}
		}); 
		
		var validate = $("#saveMenuForm").validate({
			debug: false, //调试模式取消submit的默认提交功能   
	        onkeyup: false,   
		    rules: {
		    	name:{required:true}
		    },
		    messages: {
		    	name: {required:'请输入菜单姓名'}
		    }
		});
		
		 $("#saveMenuBtn").click(function(){
			if(validate.form(this.form)){ //若验证通过，则调用保存/修改方法
				saveMenuInfo(this.form);
			}
		});
		 
		function saveMenuInfo(form){
			$.ajax({
		        cache: true,
		        type: "POST",
		        url:domain + '/wx/menu/save.htm',
		        data:$(form).serialize(),
		        async: false,
		        error: function(request) {
		            alert("Connection error");
		        },
		        success: function(result) {
		        	if(result.success){
		        		pageii = layer.alert("保存成功", 1, function(){
		        			layer.close(pageii);
		        			$('#saveMenuForm')[0].reset();
		        			$("#editMenuDiv").hide();
		        			zTree.reAsyncChildNodes(null, "refresh");
		                })
		        	}else{
		        		layer.alert(result.msg.message, 8);
		        	}
		        }
		    });
		}
		
		 $("#pushBtn").click(function(){
			layer.confirm('您确认发布该菜单么,微信公众号有次数限制,请谨慎发布?', function(){
				$.ajax({
			        cache: true,
			        type: "GET",
			        url:domain + '/wx/menu/push.htm',
			        async: false,
			        error: function(request) {
			            alert("Connection error");
			        },
			        success: function(result) {
			        	if(result.success){
			        		pageii = layer.alert("发布成功", 1, function(){
			        			layer.close(pageii);
			        			$('#saveMenuForm')[0].reset();
			        			$("#editMenuDiv").hide();
			        			zTree.reAsyncChildNodes(null, "refresh");
			                })
			        	}else{
			        		layer.alert(result.msg.message, 8);
			        	}
			        }
			    });	
			});
		});
});