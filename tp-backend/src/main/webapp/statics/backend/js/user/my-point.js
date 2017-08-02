var pageii;
$(function(){	
	$('.pointAdd').live('click',function(){
			pageii=$.layer({
	            type : 2,
	            title: '完善个人信息送积分',
	            shadeClose: true,
	            maxmin: true,
	            fix : false,  
	            area: ['500px', 505],                   
	            iframe: {
	                src : domain+"/user/point/toAddMy.htm"
	            } 
	        });
		 
	});
	
	
	// 时间控件初始化
	$( "#createBeginTime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	        $( "#createEndTime" ).datepicker( "option", "minDate", selectedDate );
	    }
	});
	$( "#createEndTime" ).datepicker({
		dateFormat:'yy-mm-dd',
		onClose: function( selectedDate ) {
	        $( "#createBeginTime" ).datepicker( "option", "maxDate", selectedDate );
	    }
	});
	
	//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
	if ($("#tree").is("table")) {
		var colM = [ 
	            {name:'id', index:'id', width:"35px", align:"center", hidden:false},
		        {name:'point', index:'point', align:"center"},
		        {name:'platform', index:'platform', align:"center"},
		        {name:'deadLineFlag', index:'deadLineFlag', align:"center"},
		        {name:'createBeginTime', index:'createBeginTime', align:"center"},
		        {name:'createEndTime', index:'createEndTime', align:"center"},
		        {
		        	name:'id2',
		        	index:'id2', 
		        	align:"center",
		        	formatter:function(cellValue, options, rowObject){
		        		var	str="<a href='javascript:edit("+rowObject.id+")'>查看</a> ｜ <a href='javascript:del("+rowObject.id+")'>删除</a>";
		        		return str;
		        	}
		        }
			];
		
		var colN = ['ID','积分数','平台','有无期限', '起始日期','结束日期','操作'];
		var point = $("input[name='point']").attr("data-v");
		var platform = $("input[name='platform']").attr("data-v");
		var isExpiry = $("input[name='isExpiry']").attr("data-v");
		var createBeginTime = $("input[name='createBeginTime']").attr("data-v");
		var createEndTime = $("input[name='createEndTime']").attr("data-v");

		var postData = {
			point: point,
			isExpiry: isExpiry,
        	createBeginTime: createBeginTime,
        	createEndTime: createEndTime
        };
		$("#tree").jqGrid({
//			treeGrid: true,  
//		    treeGridModel: 'adjacency',  
//		    ExpandColumn: 'name',  
//		    ExpandColClick: false,  
		    url: domain+'/user/point/my/listJSON.htm?m='
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
        title: '注册送积分',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['500px', 600],                     
        iframe: {
            src : domain+'/user/point/toShow.htm?id='+id
        } 
    });                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
}

function del(id){
	layer.confirm('您确认删除么?', function(){
		$.ajax({
	        cache: true,
	        type: "get",
	        url:domain+'/user/point/delete.htm?id='+id,
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) {
	        	if(data.resultInfo.type=="1"){
	                layer.alert(data.resultInfo.message, 1, function(){
	            	window.location.reload();
	            	layer.close(parent.pageii);
	                })
	            	}else{
	            		layer.alert(data.resultInfo.message, 8);		
	            	}
	        }
	    });
		
		
	 },function(){
	 	
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
	        data:$('#pointAdd').serialize(),
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) {
	         	if(data.resultInfo.type=="1"){
	                layer.alert(data.resultInfo.message, 1, function(){
	            	parent.window.location.reload();
	            	parent.layer.close(parent.pageii);
	                })
	            	}else{
	            		layer.alert(data.resultInfo.message, 8);		
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
	        data:$('#pointEdit').serialize(),
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) {
	         	if(data.resultInfo.type=="1"){
	                layer.alert(data.resultInfo.message, 1, function(){
	            	parent.window.location.reload();
	            	parent.layer.close(parent.pageii);
	                })
	            	}else{
	            		layer.alert(data.resultInfo.message, 8);		
	            	}
	        }
	    });
}

//-------------------------------------重置按钮--如下----------------------------------------

function dataReset(button){
	clearForm('reviewForm');
}

function clearForm(id) {
	var formObj = document.getElementById(id);
	if (formObj == undefined) {
		return;
	}
	for (var i = 0; i < formObj.elements.length; i++) {
		if (formObj.elements[i].type == "text") {
			formObj.elements[i].value = "";
		} else if (formObj.elements[i].type == "password") {
			formObj.elements[i].value = "";
		} else if (formObj.elements[i].type == "radio") {
			formObj.elements[i].checked = false;
		} else if (formObj.elements[i].type == "checkbox") {
			formObj.elements[i].checked = false;
		} else if (formObj.elements[i].type == "select-one") {
			formObj.elements[i].options[0].selected = true;
		} else if (formObj.elements[i].type == "select-multiple") {
			for (var j = 0; j < formObj.elements[i].options.length; j++) {
				formObj.elements[i].options[j].selected = false;
			}
		} else if (formObj.elements[i].type == "file") {
			var file = formObj.elements[i];
			if (file.outerHTML) {
				file.outerHTML = file.outerHTML;
			} else {
				file.value = ""; // FF(包括3.5)
			}
		} else if (formObj.elements[i].type == "textarea") {
			formObj.elements[i].value = "";
		}
	}
}