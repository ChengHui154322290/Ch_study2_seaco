var pageii;
$(function(){	
	$('.reviewAdd').live('click',function(){
			pageii=$.layer({
	            type : 2,
	            title: '新增评论',
	            shadeClose: true,
	            maxmin: true,
	            fix : false,  
	            area: ['500px', 505],                   
	            iframe: {
	                src : domain+"/user/review/toAdd.htm"
	            } 
	        });
		 
	});
	
	// 进入批量导入评论界面
	$('.reviewImport').live('click',function(){
		pageii=$.layer({
            type : 2,
            title: '导入评论',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['1000px', 600],                   
            iframe: {
                src : domain+"/user/review/toImport.htm"
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
		        {name:'barcode', index:'barcode', align:"center"},
		        {name:'prd', index:'prd', align:"center"},
		        {name:'name', index:'name', align:"center"},
		        {name:'score', index:'score', align:"center"},
		        {name:'review', index:'review', align:"center"},
		        {name:'createTime', index:'createTime', align:"center"},
		        {name:'id2',index:'id2', align:"center",formatter:function(cellValue, options, rowObject){
					var	str="<a href='javascript:edit("+rowObject.id+")'>查看</a>";
					return str;
				}}
			];
		
		var colN = ['ID','条形码','PRD','商品名称', '分值','评论','评论日期','操作'];
		var uid = $("input[name='uid']").attr("data-v");
		var prd = $("input[name='prd']").attr("data-v");
		var barcode = $("input[name='barcode']").attr("data-v");
		var username = $("input[name='username']").attr("data-v");
		var itemName = $("input[name='itemName']").attr("data-v");
		var createBeginTime = $("input[name='createBeginTime']").attr("data-v");
		var createEndTime = $("input[name='createEndTime']").attr("data-v");
		
		var postData = {
        	uid: uid,
        	prd: prd,
        	barcode: barcode,
        	username: username,
        	itemName: itemName,
        	createBeginTime: createBeginTime,
        	createEndTime: createEndTime
        };
		$("#tree").jqGrid({
//			treeGrid: true,  
//		    treeGridModel: 'adjacency',  
//		    ExpandColumn: 'name',  
//		    ExpandColClick: false,  
		    url: domain+'/cmt/review/listJSON.htm?m='
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
        title: '查看评论明细',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['500px', 600],                     
        iframe: {
            src : domain+'/user/review/toShow.htm?id='+id
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
	        data:$('#reviewAdd').serialize(),
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
	        data:$('#reviewEdit').serialize(),
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