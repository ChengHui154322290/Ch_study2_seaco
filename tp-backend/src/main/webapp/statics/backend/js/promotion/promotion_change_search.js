var SEARCH_TOPIC = domain + "/topicChange/search?";
var ADD_TOPIC = domain + "/topicChange/add";
//var COPY_TOPIC = domain + "/topicChange/copy";
var APPROVE_TOPIC = domain + "/topicChange/approve";
var CANCEL_TOPIC = domain + "/topicChange/cancel";
var REFUSE_TOPIC = domain + "/topicChange/refuse";
var EDIT_TOPIC =   "/topicChange/{topicChangeId}/edit";
var VIEW_TOPIC =  "/topicChange/{topicChangeId}/detail";

$(document).ready(function() {
	$("#search").on("click", function() {
		searchTopics(1);
	});

	$("#reset").on("click", function() {
		$("#type").get(0).selectedIndex = 0;
		$("#status").get(0).selectedIndex = 0;
		$("#process").get(0).selectedIndex = 0;
		$("#salesPartten").get(0).selectedIndex = 0;
		$("#changeId").val("");
		$("#number").val("");
		$("#name").val("");
	});

	$("#add").on("click", function() {
		window.location.href = ADD_TOPIC;
	});
	searchTopics(1);

	$(".topicInteger").numeral();
});

function searchTopics(pageNo) {
	var changeId = $("#changeId").val();
	var type = $("#type").val();
	var number = $("#number").val();
	var name = $("#name").val();
	var status = $("#status").val();
	var process = $("#process").val();
	var salesPartten = $("#salesPartten").val();
	var perCount = $("select[name='perCount']").val();
	if (perCount == null) {
		perCount = "30";
	}
	$("#topic_list").html("");
	var data = syncPost(SEARCH_TOPIC + new Date().getTime(), {
		changeId : changeId,
		type : type,
		salesPartten : salesPartten,
		number : number,
		name : name,
		status : status,
		process : process,
		perCount : perCount,
		pageNo : pageNo
	});

	$("#topic_list").html(data);

	$("a[name='prePage']").off("click");
	$("a[name='prePage']").on("click", prePageBind);

	$("a[name='nextPage']").off("click");
	$("a[name='nextPage']").on("click", nextPageBind);

	$("a[name='approveTopicChange']").off("click");
	$("a[name='approveTopicChange']").on("click", approveTopicBind);
	
	$("a[name='cancelTopicChange']").off("click");
	$("a[name='cancelTopicChange']").on("click", cancelTopicBind);

	$("a[name='refuseTopicChange']").off("click");
	$("a[name='refuseTopicChange']").on("click", refuseTopicBind);
	
	$("a[name='editTopic']").off("click");
	$("a[name='editTopic']").on("click", editTopicBind);
	
	$("a[name='viewTopic']").off("click");
	$("a[name='viewTopic']").on("click", viewTopicBind);
	
	$("select[name='perCount']").off("change");
	$("select[name='perCount']").on("change", function() {
		if($("select[name='perCount']").val()!=$(this).val()){
			$("select[name='perCount']").val($(this).val());
		}
		searchTopics(1);
	});
}

function prePageBind() {
	var pageNo = $("#currPage").text();
	if (parseInt(pageNo) < 2) {
		alert("已经是第一页了");
		return;
	}

	searchTopics(parseInt(pageNo) - 1);
}

function nextPageBind() {
	var pageNo = $("#currPage").text();
	var totalPage = $("#totalPage").text();
	if (parseInt(pageNo) == totalPage) {
		alert("已经是最后一页了");
		return;
	}
	searchTopics(parseInt(pageNo) + 1);
}

function editTopicBind(){
	var topicId = $(this).attr("topicChangeId");
	if (null == topicId || "" == $.trim(topicId)
			|| "0" == $.trim(topicId)) {
		layer.alert("指定活动变更单无效!");
		return;
	}
	var url = EDIT_TOPIC.replaceAll("{topicChangeId}",topicId);
	show("editTopic"+topicId,url,"编辑活动变更单");
}

function viewTopicBind(){
	var topicId = $(this).attr("topicChangeId");
	if (null == topicId || "" == $.trim(topicId)
			|| "0" == $.trim(topicId)) {
		layer.alert("指定活动变更单无效!");
		return;
	}
	var url = VIEW_TOPIC.replaceAll("{topicChangeId}",topicId);
	show("viewTopic"+topicId,url,"查看活动变更单");
}

function approveTopicBind() {
	var topicChangeId = $(this).attr("topicChangeId");
	if (null == topicChangeId || "" == $.trim(topicChangeId)
			|| "0" == $.trim(topicChangeId)) {
		layer.alert("指定活动变更单无效!");
		return;
	}
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否批准指定活动变更单？',
	        btns: 2,                    
	        type: 4,
	        btn: ['确定','取消'],
	        yes: function(){
	        	var index = layer.load("请稍等...");
	        	var data = syncPost(APPROVE_TOPIC, {
	        		topicChangeId : topicChangeId
	        	})
	        	layer.close(index);
	        	if (null != data) {
	        		if (data.success) {
	        			layer.msg("审批通过", 1, 9);
	        			searchTopics(1);
	        			return;
	        		} else {
	        			layer.alert(data.msg.message);
	        			return;
	        		}
	        	} else {
	        		layer.alert("审批失败!");
        			return;
	        	}
	        }, no: function(){
	        }
	    }
	});
}

function cancelTopicBind() {
	var topicChangeId = $(this).attr("topicChangeId");
	if (null == topicChangeId || "" == $.trim(topicChangeId)
			|| "0" == $.trim(topicChangeId)) {
		layer.alert("指定活动变更单无效!");
		return;
	}
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否取消指定活动变更单？',
	        btns: 2,                    
	        type: 4,
	        btn: ['确定','取消'],
	        yes: function(){
	        	var index = layer.load("请稍等...");
	        	var data = syncPost(CANCEL_TOPIC, {
	        		topicChangeId : topicChangeId
	        	})
	        	layer.close(index);
	        	if (null != data) {
	        		if (data.success) {
	        			layer.msg("已取消", 1, 9);
	        			searchTopics(1);
	        			return;
	        		} else {
	        			layer.alert(data.data);
	        			return;
	        		}
	        	} else {
	        		layer.alert("取消失败!");
        			return;
	        	}
	        }, no: function(){
	        }
	    }
	});
}

function refuseTopicBind() {
	var topicChangeId = $(this).attr("topicChangeId");
	if (null == topicChangeId || "" == $.trim(topicChangeId)
			|| "0" == $.trim(topicChangeId)) {
		layer.alert("指定活动变更单无效!");
		return;
	}
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否驳回指定活动变更单？',
	        btns: 2,                    
	        type: 4,
	        btn: ['确定','取消'],
	        yes: function(){
	        	var index = layer.load("请稍等...");
	        	var data = syncPost(REFUSE_TOPIC, {
	        		topicChangeId : topicChangeId
	        	})
	        	layer.close(index);
	        	if (null != data) {
	        		if (data.success) {
	        			layer.msg("已驳回", 1, 9);
	        			searchTopics(1);
	        			return;
	        		} else {
	        			layer.alert(data.data);
	        			return;
	        		}

	        	} else {
	        		layer.alert("驳回失败!");
        			return;
	        	}
	        }, no: function(){
	        }
	    }
	});
}

function show(id,url,text){
	var date = new Date();
	var tv={
		url:url,
		tabId:id,
		text:text,
		doc:false
	};
	parent.window.showTab(tv);
}