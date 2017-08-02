var pageii;
// 弹出 属性项编辑窗口.
$(function() {
	$('.editAtt').live('click', function() {
		var id = $(this).attr('param');

		pageii = $.layer({
			type : 2,
			title : '编辑属性项',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 500 ],
			iframe : {
				src : domain + '/basedata/attribute/edit.htm?id=' + id
			}
		});
	});
	
	$('.viewAtt').live('click', function() {
		var id = $(this).attr('param');

		pageii = $.layer({
			type : 2,
			title : '查看属性',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 500 ],
			iframe : {
				src : domain + '/basedata/attribute/viewAttr.htm?id=' + id
			}
		});
	});
	
	
	$('.attAddBtn').live('click', function() {
		pageii = $.layer({
			type : 2,
			title : '新增属性',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '700px', 500 ],
			iframe : {
				src : domain + '/basedata/attribute/add.htm'
			}
		});
	});

	// 取消按钮 关闭当前弹窗
	$('.closebtn').on('click', function() {
		parent.layer.close(parent.pageii);
	});

	// ---------------------------------------------------------------------------------------------------------------
	if ($("#tree").is("table")) {
		$('.categoryAdd').live(
				'click',
				function() {
					// var selectedCatId=getSelectedRow();

					var rowData = getSelectedRow();
					var selectedCatId = rowData['id'];
					var level = parseInt(rowData['level']) + 1; // jqGrid表中的level
																// 比实际 小1.

					// alert("id:"+selectedCatId+" level"+level);
					if (level > 0 && level < 3) {
						pageii = $.layer({
							type : 2,
							title : '新增类别',
							shadeClose : true,
							maxmin : true,
							fix : false,
							area : [ '500px', 500 ],
							iframe : {
								src : domain
										+ "/basedata/category/add.htm?catId="
										+ selectedCatId + "&level=" + level
							}
						});
					}
				});

		var colM = [
				{
					name : 'id',
					index : 'id',
					align : "center",
					hidden : true
				},
				{
					name : 'level',
					index : 'level',
					hidden : true,
					align : "center"
				},
				{
					name : 'name',
					index : 'name',
					formatter : function(cellValue, options, rowObject) {
						if (rowObject.level == 2) {
							var str = '';
							if ($("#listValue").is("input")) {
								var array = $("#listValue").val().split(",");
								for (var i = 0; i < array.length; i++) {
									if (rowObject.id == array[i]) {
										str = "<input name='ids' type='checkbox' checked='checked' value='"
												+ rowObject.id
												+ "'/>"
												+ cellValue + "";
										break;
									}
								}
								if (str == '') {
									var str = "<input name='ids' type='checkbox' value='"
											+ rowObject.id
											+ "'/>"
											+ cellValue
											+ "";
								}
							} else {
								var str = "<input name='ids' type='checkbox' value='"
										+ rowObject.id + "'/>" + cellValue + "";
							}

							return str;
						} else {
							return cellValue;
						}
					}
				}, {
					name : 'myLevelName',
					index : 'myLevelName',
					align : "center"
				}, ];

		var colN = [ 'ID', 'Level', '类别名称', '类别' ];

		$("#tree").jqGrid(
				{
					treeGrid : true,
					treeGridModel : 'adjacency',
					ExpandColumn : 'name',
					ExpandColClick : true,
					// url:'statics/data.json',
					url : domain + '/basedata/category/listCatJSON2.htm',
							
					datatype : 'json',
					height : 'auto',
					colNames : colN,
					loadonce:true,
					colModel : colM,
					pager : "false",
					viewrecords : true,
					// caption: 'none',
					jsonReader : {
						root : "rows",
						repeatitems : false
					},
					treeReader : {
						level_field : "level",
						parent_id_field : "parentId",
						leaf_field : "isLeaf"
					}
				});

	}
	// --------------------------------------------------------------------------------------------------------------
	
});

// 保存增加
function saveAddAtt(button) {
	var address = $(button).attr('param');

	$.ajax({
		cache : true,
		type : "POST",
		url : domain + address,
		data : $('#attributeAdd').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(result) {
			if(result.success){
	            layer.alert("新增成功", 1, function(){
	            	parent.window.location.reload();
	            	parent.layer.close(parent.pageii);
	            })
	        }else{
	        	layer.alert("新增失败", 8);		
	        }
		}
	});
}

// 保存更新
function saveAtt(button) {

	var address = $(button).attr('param');

	$.ajax({
		cache : true,
		type : "POST",
		url : domain + address,
		data : $('#attributeUpdate').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(result) {
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
// -----------------------------------------------------------------------------

function edit(id) {
	alert(id);
}

function editCatAtt(id) {

	$.layer({
		type : 2,
		title : '小类属性组',
		shadeClose : true,
		maxmin : true,
		fix : false,
		area : [ '1024px', 500 ],
		iframe : {
			src : domain
					+ '/basedata/categoryAttributeLink/list.htm?categoryId='
					+ id
		}
	});
}

function getSelectedRow() {
	var grid = $("#tree");
	var rowKey = grid.jqGrid('getGridParam', "selrow");
	var rowKey = grid.jqGrid('getRowData', rowKey);
	return rowKey;
}

function attrCateUpdate(button){
	var address = $(button).attr('param');

	$.ajax({
		cache : true,
		type : "POST",
		url : domain + address,
		data : $('#attrCateUpdateRelation').serialize(),
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if(data.success){
	            layer.alert("保存成功", 1, function(){
	        	parent.window.location.reload();
	        	parent.layer.close(parent.pageii);
	            })
	        	}else{
	        		layer.alert(data.msg.message, 8);		
	        	}
		}
	});
}
// -----------------------------------------------------------------------------
function dataReset(button){
	clearForm('attrForm');
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