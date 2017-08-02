var SEARCH_TOPIC_RELATE = domain + "/topicRelate/searchSingle";
var ADD_TOPIC_RELATE = domain + "/topicRelate/add";
var REMOVE_TOPIC_RELATE = domain + "/topicRelate/remove";
var CREATE_TOPIC_RELATE = domain + "/topicRelate/create";
var SELECTION_TOPIC_RELATE = domain + "/topicRelate/{topicId}/selection/{rowIndex}";
var relateRemoveList = new Array();
$(document).ready(
	function() {
		$("#addRelation").on("click", function() {
			var topicId = $("#topicId").val();
			var data = syncGet(ADD_TOPIC_RELATE, {});
			$("#topicRelationsList").append(data);
			addRalteButtonOperation();
		});
		$("input[name='removeRelation']").on(
				"click",
				function() {
					var topicId = $("#topicId").val();
					var relateTId = $(this).closest("tr").find(
							"span")[0].innerText;
					if (relateTId == null || relateTId == "") {
					} else {
						relateRemoveList.push(relateTId);
					}
					$(this).closest("tr").detach();
				});
});

function addRalteButtonOperation() {
	$("input[name='confirmRelation']").off("click");
	$("input[name='confirmRelation']").on("click", confirmRelateBind);

	$("input[name='searchRelation']").off("click");
	$("input[name='searchRelation']").on("click", searchRelateBind);

	$("input[name='removeRelation']").off("click");
	$("input[name='removeRelation']").on("click", removeRelateBind);
	
}

function searchRelateBind(){
	var topicId = $("#topicId").val();
	var rowIndex = $(this).closest("tr").index(); 
	var searchUrl = SELECTION_TOPIC_RELATE.replace(
			"{topicId}", topicId);
	searchUrl = searchUrl.replace("{rowIndex}", rowIndex);
	$.layer({
		type : 2,
		title : "查询专场活动",
		shadeClose : true,
		maxmin : true,
		fix : false,
		area : [ '800px', 600 ],
		iframe : {
			src : searchUrl
		},
		end : function(){
			var row = $("#topicRelationsList tr")[rowIndex];
			var relateTid = $(row).children().find("input[name='secondTopicId']").val();
			if(null == relateTid || "" == $.trim(relateTid)){
				return;
			}
			var data = syncGet(SEARCH_TOPIC_RELATE, {
				topicId : relateTid
			});
			if (data != null) {
				if (!data.success) {
					$(row).children().find("input[name='secondTopicId']").val("");
					layer.alert(data.data);
				} else {
					$(row).children().find("input[name='secondTopicId']").val(
							data.data.id);
					$(row).children().find("input[name='relateNumber']").val(
							data.data.number);
					$(row).children().find("input[name='relateName']").val(
							data.data.name);
				}
			}
		}
	});
}

function removeRelateBind(){
	var topicId = $("#topicId").val();
	var relateTId = $(this).closest("tr").find(
					"span")[0].innerText;
	if (relateTId == null || relateTId == "") {
	} else {
		relateRemoveList.push(relateTId);
	}
	$(this).closest("tr").detach();
}

function confirmRelateBind() {
	var topicId = $("#topicId").val();
	var relateTId = $(this).closest("tr").find(
			"input[name='secondTopicId']").val();
	if (topicId == relateTId) {
		$(this).closest("tr").find("input[name='secondTopicId']").val("");
		layer.alert("不能添加当前活动为关联活动");
		return;
	}
	var data = syncGet(SEARCH_TOPIC_RELATE, {
		topicId : relateTId
	});
	if (data == null || !data.success) {
		$(this).closest("tr").find("input[name='secondTopicId']").val("");
		layer.alert("关联活动不存在，或者该活动不是审核通过状态!")
	} else {
		$(this).closest("tr").find(
				"input[name='secondTopicId']").val(data.data.id);
		$(this).closest("tr")
				.find("input[name='relateNumber']").val(
						data.data.number);
		$(this).closest("tr").find("input[name='relateName']")
				.val(data.data.name);
	}
}
