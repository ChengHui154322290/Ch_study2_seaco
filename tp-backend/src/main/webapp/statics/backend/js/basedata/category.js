var pageii;
$(function(){	
	$('.categoryAdd').live('click',function(){
		//var selectedCatId=getSelectedRow();
		
		var rowData=getSelectedRow();
		var selectedCatId=rowData['id'];
		var level=parseInt(rowData['level'])+1; //jqGrid表中的level 比实际 小1.
		var code=parseInt(rowData['code']);
		//alert("id:"+selectedCatId+" level"+level);
		if(level>0 && level<3){
			pageii=$.layer({
	            type : 2,
	            title: '新增类别',
	            shadeClose: true,
	            maxmin: true,
	            fix : false,  
	            area: ['500px', 500],                   
	            iframe: {
	                src : domain+"/basedata/category/add.htm?catId="+selectedCatId +"&level="+level+"&code="+code
	            } 
	        });
		 } else if(level!=3){
			pageii=$.layer({
	            type : 2,
	            title: '新增大类别',
	            shadeClose: true,
	            maxmin: true,
	            fix : false,  
	            area: ['500px', 300],                   
	            iframe: {
	                src : domain+"/basedata/category/add.htm?level="+0
	            } 
	        });
		}
	});
	//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
	if ($("#tree").is("table")) {
		var colM = [ 
	            {name:'id',index:'id', align:"center", hidden:true},
	            {name:'level',index:'level',hidden:true, align:"center"},
		        {name:'name',index:'name',formatter:function(cellValue, options, rowObject){
		        	return rowObject.name+"("+rowObject.id+")";
		        }},
	            {name:'myLevelName',index:'myLevelName',align:"center"},
				{name:'code',index:'code',align:"center"},
				{name:'remark',index:'remark',align:"center",},
				{name:'status',index:'status', align:"center",formatter:function(cellValue, options, rowObject){
					if(cellValue==true){
						return " 有效";
					}else{
						return "无效";
					}
				}},
				
				{name:'id2',index:'id2',formatter:function(cellValue, options, rowObject){
					var str= "[<a href='javascript:edit("+cellValue+","+rowObject.level+")'>编辑</a>]&nbsp;"
					if(rowObject.level==2){
						str=str+"[<a href='javascript:editCatAtt("+cellValue+")'>属性组</a>]&nbsp;"
					}
					if(rowObject.level==0){
						str=str+"[<a href='javascript:editCatCerts("+cellValue+")'>资质绑定</a>]&nbsp;"
					}		
				//	str=str+"[<a href='javascript:'>日志</a>]&nbsp;"
					return str;
				}}
			];
		
		var colN = ['ID','Level','类别名称','类别', '类别编号','备注','状态','操作'];
		
		var lastSel;
		$("#tree").jqGrid({
			treeGrid: true,  
		    treeGridModel: 'adjacency',  
		    ExpandColumn: 'name',  
		    ExpandColClick: true,  
		    //url:'statics/data.json',
		    url: domain+'/basedata/category/listCatJSON2.htm?m=' + Math.random(),
		    datatype: 'json',  
			loadonce:true,
		    height:'300',
		    width:"1000",
		    colNames:colN,
			colModel:colM,
		    pager: "false",
		   // multiselect: true,
		    scrollrows:true,//使得选中的行显示到可视区域
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
	        onSelectRow: function(id){
			      if(id && id!==lastSel){
			         $('#tree').restoreRow(lastSel);
			         lastSel=id;
			      } else{
			    	  $("#tree").resetSelection(); 
			    	  lastSel=null;
			      }
			   }
		   
		});
		//------------------------------------------------	
		$("#searthAtt").click(function(){
			var codeSearch = $("#code").val();
			var nameSearch = $("#name").val();
			var levelSearch= $("#level").val();
			var allIDs = $("#tree").getDataIDs();
			for(var i=0;i<allIDs.length;i++){
				var id = allIDs[i];
				var rowData = $("#tree").getRowData(id);
				var name = rowData.name.trim();
				var level = rowData.level;	
				var code = rowData.code.trim();			
					if(name.indexOf(nameSearch)!=-1 && code.indexOf(codeSearch)!=-1 &&(level==levelSearch) ){
						Expand(rowData.id);
						$("#tree").setSelection(rowData.id,function(obj){alert(obj);});
						//return;
					}	
			}
			
		});	
	}
});
function edit(id){
      pageii=$.layer({
        type : 2,
        title: '编辑种类组',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['1024px', 500],                     
        iframe: {
            src : domain+'/basedata/category/edit.htm?id='+id
        } 
    });
}

function editCatAtt(id){	
	pageii=$.layer({
        type : 2,
        title: '小类属性组编辑',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['800px', 600],                     
        iframe: {
            src : domain+'/basedata/categoryAttributeLink/list.htm?categoryId='+id
        } 
    });
}

function editCatCerts(id){	
	pageii=$.layer({
		type : 2,
		title: '经营资质',
		shadeClose: true,
		maxmin: true,
		fix : false,  
		area: ['800px', 600],                     
		iframe: {
			src : domain+"/basedata/category/addCertLinked.htm?cateId="+id
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
function saveAddCat(button){
		var address= $(button).attr('param');
		
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:domain+address,
	        data:$('#catAdd').serialize(),
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(result) {
	        	if(result.success){
		            layer.alert("保存成功", 1, function(){
		        	parent.window.location.reload();
		        	parent.layer.close(parent.pageii);
	            })
	        	}else{
	        		layer.alert("保存失败", 8);		
	        	}
	        }
	    });
}


function cateUpdate(button){
	var address= $(button).attr('param');
	$.ajax({
        cache: true,
        type: "POST",
        url:domain+address,
        data:$('#catEdit').serialize(),
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(result) {    	
        	if(result.success){
	            layer.alert("保存成功", 1, function(){
	        	parent.window.location.reload();
	        	parent.layer.close(parent.pageii);
            })
        	}else{
        		layer.alert("保存失败", 8);		
        	}
        }
    });
}

function addCataSpecGroupLinked(button) {
	var id= $(button).attr('param');
	pageii=$.layer({
        type : 2,
        title: '新增小类和规格组关系',
        shadeClose: true,
        maxmin: true,
        fix : false,  
        area: ['600px', 400],                   
        iframe: {
            src : domain+"/basedata/category/addCataSpecGroupLinked.htm?cateId="+id
        } 
    });
}

function updateCataSpecGroupLinked(button){
	var address= $(button).attr('param');
	$.ajax({
        cache: true,
        type: "POST",
        url:domain+address,
        data:$('#updateCataSpecGroup').serialize(),
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(result) {    	
        	if(result.success){
	            layer.alert("保存成功", 1, function(){
	        	parent.window.location.reload();
	        	parent.layer.close(parent.pageii);
            })
        	}else{
        		layer.alert("保存失败", 8);		
        	}
        }
    });
}

function updateCateCertLinked(button){
	var address= $(button).attr('param');
	$.ajax({
        cache: true,
        type: "POST",
        url:domain+address,
        data:$('#updateCateCertFrom').serialize(),
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(result) {    	
        	if(result.success){
	            layer.alert("保存成功", 1, function(){
	        	parent.window.location.reload();
	        	parent.layer.close(parent.pageii);
            })
        	}else{
        		layer.alert("保存失败", 8);		
        	}
        }
    });
}

function dataReset(button){
	clearForm('queryForm');
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
function Expand(rowid) {
	   var ssrecord = jQuery("#tree").getRowData(rowid);
	   var rows = $("#tree").jqGrid('getNodeAncestors', ssrecord);
	   for (var i = 0; i < rows.length; i++){
	        var childRows = $("#tree").jqGrid('getNodeChildren', rows[i]);
	        $("#tree").jqGrid('expandNode', rows[i]);    
	        $("#tree").jqGrid('expandRow', rows[i]);
	    }
	}
