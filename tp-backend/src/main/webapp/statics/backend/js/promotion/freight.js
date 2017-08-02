var pageii;
$(function(){	
	$('.freightAdd').live('click',function(){
		
			pageii=$.layer({
	            type : 2,
	            title: '新增运费模板',
	            shadeClose: true,
	            maxmin: true,
	            fix : false,  
	            area: ['500px', 300],                   
	            iframe: {
	                src : domain+"/freight/toAdd.htm"
	            } 
	        });
		 
	});
	//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
	if ($("#tree").is("table")) {
		var colM = [ 
	            {name:'id', index:'id', align:"center", hidden:false},
		        {name:'name', index:'name', align:"center"},
		        {name:'calculateMode', index:'calculateMode', align:"center"},
		        {name:'freightType', index:'freightType', align:"center"},
		        {name:'detailInfo', index:'detailInfo', align:"center"},
		        {name:'createTime', index:'createTime', align:"center"},
		        {name:'id2',index:'id2', align:"center",formatter:function(cellValue, options, rowObject){
					var	str="<a href='javascript:edit("+rowObject.id+")'>修改</a>";
					return str;
				}}
			];
		
		var colN = ['ID','模板名称','计算方式','类型', '设置明细','创建时间','操作'];
		var freightType = $("select[name='freightType']").attr("data-v");
		var calculateMode = $("select[name=calculateMode]").attr("data-v");
		var templateName = $("input[name=templateName]").attr("data-v");
		var postData = {
        	freightType: freightType,
        	calculateMode: calculateMode,
        	templateName: templateName
        };
		$("#tree").jqGrid({
//			treeGrid: true,  
//		    treeGridModel: 'adjacency',  
//		    ExpandColumn: 'name',  
//		    ExpandColClick: false,  
		    url: domain+'/freight/listJSON.htm?m='
			+ Math.random(),
		    datatype: 'json',  
		    height:'auto',
		    colNames:colN,
			colModel:colM,
		    viewrecords: true,  
		    //   caption: 'none',  
		    jsonReader: {  
		        root: "rows",  
		        repeatitems: false  
		    },
		    treeReader : {  
	            level_field: "level",  
	            parent_id_field: "parentId",
	            leaf_field: "isLeaf"
	        },
	        pager:$('#gridpager'),
	    	rowNum:20,//每页显示记录数
//	        rowList:[5,10,15],//用于改变显示行数的下拉列表框的元素数组。
	    	rowList:[20,25,30],//用于改变显示行数的下拉列表框的元素数组。
	        postData: postData
		});
	}
    	
});
jQuery("#tree").setGridParam().trigger
function edit(id){
      pageii=$.layer({
        type : 2,
        title: '编辑运费模板',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['500px', 300],                     
        iframe: {
            src : domain+'/freight/toEdit.htm?id='+id
        } 
    });
}

function getSelectedRow() {
    var grid = $("#tree");
    var rowKey = grid.jqGrid('getGridParam',"selrow");
    var rowKey = grid.jqGrid('getRowData',rowKey);
    return rowKey;
}

//保存增加 
function saveAdd(button){
		var address= $(button).attr('param');
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:domain+address,
	        data:$('#freightTemplateAdd').serialize(),
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) {
	         	if(data.success){
	         		layer.alert("添加成功", 1, function(){
		            	parent.window.location.reload();
		            	parent.layer.close(parent.pageii);
		            });
	         	}else{
	            	layer.alert(data.msg.message, 8);
	            }
	        }
	    });
}

//保存增加 
function upd(button){
		var address= $(button).attr('param');
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:domain+address,
	        data:$('#freightTemplateEdit').serialize(),
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) {
	         	if(data.success){
	         		layer.alert("操作成功", 1, function(){
		            	parent.window.location.reload();
		            	parent.layer.close(parent.pageii);
		            });
	            }else{
	            	layer.alert(data.msg.message, 8);
	            }
	        }
	    });
}
