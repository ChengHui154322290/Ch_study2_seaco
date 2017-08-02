$(document).ready(function (){
  var colM = [ 
			{name:'name',index:'name'},
			{name:'codeNum',index:'codeNum',align:"center"},
			{name:'showAdd',index:'showAdd',align:"center",hidden:true},
			{name:'level',index:'level', width:180,align:"center"},
   			{name:'id',index:'id',hidden:true, align:"center"}
		];
	var colN = ['类别名称','类别编号','是否显示添加按钮','操作',''];
	$("#demoTable").jqGrid({ 
		treeGrid: true,  
	    treeGridModel: 'adjacency',  
	    ExpandColumn: 'name',  
	    ExpandColClick: true,  
	    url: 'category.htm?m='+Math.random(), 
	    datatype: 'json',  
	    height:'300',
	    colNames:colN,
		colModel:colM,
	    loadonce:true,
	    viewrecords: true,  
	    scrollrows:true,//使得选中的行显示到可视区域
	    gridview:true,
	    //   caption: 'none',  
	    jsonReader: {  
	        root: "rows",  
	        repeatitems: false  
	    },
	    treeReader : {  
            level_field: "level",  
            parent_id_field: "parent"
        }
	});

	$("#search_key").keyup(function(){
		var key = $("#search_key").val();
		if(key.length==0){
			return;
		}
		var allIDs = $("#demoTable").getDataIDs();
		for(var i=0;i<allIDs.length;i++){
			var id = allIDs[i];
			var rowData = $("#demoTable").getRowData(id);
			var name = rowData.name;
			if(name.indexOf(key)!=-1){
				$("#demoTable").setSelection(rowData.id,function(obj){alert(obj);});
				return;
			}
		}
		
	});

});