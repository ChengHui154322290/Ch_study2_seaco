var SEARCH_TOPIC_RELATE = domain + "/topicRelate/search";
var index = parent.layer.getFrameIndex(window.name);

$(document).ready(function() {

	$("#search").on("click", function() {
		searchRelates(1);
	});
	
	$("#save").on("click", function() {
		var rowIndex = $("#rowIndex").val();
		if(null != rowIndex && "" != $.trim(rowIndex)){
			var row = parent.$("#topicRelationsList tr")[rowIndex];
			$(row).children().find("input[name='secondTopicId']").val($("input[name='relateTopicId']:checked")[0].value);
		}
		parent.layer.close(index);
	});

	$("#cancel").on("click", function() {
		parent.layer.close(index);
	});
	
	searchRelates(1);
});

function searchRelates(startPage){
	var number = $("#number").val();
	var name = $("#name").val();
	var topicId = $("#topicId").val();
	var pageSize = $("#pageSize").val();
	if (pageSize == null) {
		pageSize = "10";
	}
	$("#topicRelationSearchList").html("");
	var data = syncGet(SEARCH_TOPIC_RELATE, {
		topicId : topicId,
		number : number,
		name : name,
		startPage : startPage,
		pageSize : pageSize
	});
	$("#topicRelationSearchList").html(data);
	

	$("#prePage").on("click", function() {
		var pageNo = $("#currPage").text();
		if (parseInt(pageNo) < 2) {
			alert("已经是第一页了");
			return;
		}

		searchRelates(parseInt(pageNo) - 1);
	});

	$("#nextPage").on("click", function() {
		var pageNo = $("#currPage").text();
		var totalPage = $("#totalPage").text();
		if (parseInt(pageNo) == parseInt(totalPage)) {
			alert("已经是最后一页了");
			return;
		}
		searchRelates(parseInt(pageNo) + 1);
	});
	
	$("#pageSize").on("change", function() {
		searchRelates(1);
	});
}