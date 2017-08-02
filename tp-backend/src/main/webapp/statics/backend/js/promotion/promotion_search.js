var SEARCH_TOPIC = domain + "/topic/search?";
var LOAD_TOPIC = domain + "/topic/load";
var ADD_TOPIC = domain + "/topic/add";
var COPY_TOPIC = domain + "/topic/copy";
var APPROVE_TOPIC = domain + "/topic/approve";
var CANCEL_TOPIC = domain + "/topic/cancel";
var TERMIN_TOPIC = domain + "/topic/terminate";
var REFUSE_TOPIC = domain + "/topic/refuse";
var EDIT_TOPIC =  "/topic/{topicId}/edit";
var VIEW_TOPIC = "/topic/{topicId}/detail";

$(document).ready(function() {

	$("#search").on("click", function() {
		searchTopics(1);
	});

	$("#reset").on("click", function() {
		$("#type").get(0).selectedIndex = 0;
		$("#status").get(0).selectedIndex = 0;
		$("#process").get(0).selectedIndex = 0;
		$("#salesPartten").get(0).selectedIndex = 0;
		$("#id").val("");
		$("#number").val("");
		$("#name").val("");
	});




	$("#add").on("click", function() {
		$.layer({
			type : 2,
			title : '新增专场活动',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '600px', 400 ],
			iframe : {
				src : ADD_TOPIC
			},
			end : function() {
				searchTopics(1);
			}
		});
	});
	searchTopics(1);
	$(".topicInteger").numeral();
});


function searchTopics(pageNo) {
	var id = $("#id").val();
	var type = $("#type").val();
	var number = $("#number").val();
	var name = $("#name").val();
	var status = $("#status").val();
	var process = $("#process").val();
	var salesPartten = $("#salesPartten").val();
	var perCount = $("select[name='perCount']").val();
	var sort = $("#sort_val").val();
	if (perCount == null) {
		perCount = "30";
	}
	$("#topic_list").html("");
	var data = syncPost(SEARCH_TOPIC + new Date().getTime(), {
		id : id,
		type : type,
		salesPartten : salesPartten,
		number : number,
		name : name,
		status : status,
		process : process,
		perCount : perCount,
		pageNo : pageNo,
		sort: sort
	});

	$("#topic_list").html(data);

	$("a[name='prePage']").off("click");
	$("a[name='prePage']").on("click", prePageBind);

	$("a[name='nextPage']").off("click");
	$("a[name='nextPage']").on("click", nextPageBind);

	$("a[name='copyTopic']").off("click");
	$("a[name='copyTopic']").on("click", copyTopicBind);

	$("a[name='approveTopic']").off("click");
	$("a[name='approveTopic']").on("click", approveTopicBind);

	$("a[name='showLog']").off("click");
	$("a[name='showLog']").on("click", showLog);
	
	$("a[name='cancelTopic']").off("click");
	$("a[name='cancelTopic']").on("click", cancelTopicBind);

	$("a[name='terminateTopic']").off("click");
	$("a[name='terminateTopic']").on("click", terminTopicBind);
	
	$("a[name='refuseTopic']").off("click");
	$("a[name='refuseTopic']").on("click", refuseTopicBind);
	
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
	var topicId = $(this).attr("topicId");
	if (null == topicId || "" == $.trim(topicId)
			|| "0" == $.trim(topicId)) {
		layer.alert("指定专场活动无效!");
		return;
	}
	var url = EDIT_TOPIC.replaceAll("{topicId}",topicId);
	show("editTopic"+topicId,url,"编辑活动信息");
}

function viewTopicBind(){
	var topicId = $(this).attr("topicId");
	if (null == topicId || "" == $.trim(topicId)
			|| "0" == $.trim(topicId)) {
		layer.alert("指定专场活动无效!");
		return;
	}
	var url = VIEW_TOPIC.replaceAll("{topicId}",topicId);
	show("viewTopic"+topicId,url,"查看活动详情");
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
function showLog() {
	var topicId = $(this).attr("topicId");
	$.layer({
		type : 2,
		title : "operate logs",
		shadeClose : true,
		maxmin : true,
		fix : false,
		area : [ '900px', 650 ],
		iframe : {
			src : '/mmp/topicOperateLog.htm?topicId='+topicId
		},
		end : function(){

		}
	});
}

function copyTopicBind() {
	var topicId = $(this).attr("topicId");
	if (null == topicId || "" == $.trim(topicId)
			|| "0" == $.trim(topicId)) {
		layer.alert("指定专场活动无效!");
		return;
	}
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否复制指定活动？',
	        btns: 2,                    
	        type: 4,
	        btn: ['确定','取消'],
	        yes: function(){
	        	var data = syncPost(COPY_TOPIC, {
	        		topicId : topicId
	        	})
	        	if (null != data) {
	        		if (data.success) {
	        			layer.msg("复制专场活动成功!", 1, 9);
	        			searchTopics(1);
	        			return;
	        		} else {
	        			layer.alert(data.msg.message);
	        			return;
	        		}
	        	} else {
	        		layer.alert("复制专场活动失败!");
        			return;
	        	}
	        }, no: function(){
	        }
	    }
	});
}

function approveTopicBind() {
	var topicId = $(this).attr("topicId");
	if (null == topicId || "" == $.trim(topicId)
			|| "0" == $.trim(topicId)) {
		layer.alert("指定专场活动无效!");
		return;
	}
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否批准指定活动？',
	        btns: 2,                    
	        type: 4,
	        btn: ['确定','取消'],
	        yes: function(){
	        	var data = syncPost(APPROVE_TOPIC, {
	        		topicId : topicId
	        	})
	        	if (null != data) {
	        		if (data.success) {
	        			layer.msg("专场活动审批通过!", 1, 9);
	        			searchTopics(1);
	        			return;
	        		} else {
	        			layer.alert(data.msg.message);
	        			return;
	        		}
	        	} else {
	        		layer.alert("专场活动审批失败!");
        			return;
	        	}
	        }, no: function(){
	        }
	    }
	});
}

function cancelTopicBind() {
	var topicId = $(this).attr("topicId");
	if (null == topicId || "" == $.trim(topicId)
			|| "0" == $.trim(topicId)) {
		layer.alert("指定专场活动无效!");
		return;
	}
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否取消指定活动？',
	        btns: 2,                    
	        type: 4,
	        btn: ['确定','取消'],
	        yes: function(){
	        	var data = syncPost(CANCEL_TOPIC, {
	        		topicId : topicId
	        	})
	        	if (null != data) {
	        		if (data.success) {
	        			layer.msg("取消专场活动成功!", 1, 9);
	        			searchTopics(1);
	        			return;
	        		} else if(data.msg != null) {
	        			layer.alert(data.msg.message);
	        			return;
	        		} else {
	        			document.write(data);
	        		}
	        	} else {
	        		layer.alert("取消专场活动失败!");
        			return;
	        	}
	        }, no: function(){
	        }
	    }
	});
}

function terminTopicBind() {
	var topicId = $(this).attr("topicId");
	if (null == topicId || "" == $.trim(topicId)
			|| "0" == $.trim(topicId)) {
		layer.alert("指定专场活动无效!");
		return;
	}
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否终止指定活动？',
	        btns: 2,                    
	        type: 4,
	        btn: ['确定','取消'],
	        yes: function(){
	        	var data = syncPost(TERMIN_TOPIC, {
	        		topicId : topicId
	        	})
	        	if (null != data) {
	        		if (data.success) {
	        			layer.msg("终止专场活动成功!", 1, 9);
	        			searchTopics(1);
	        			return;
	        		} else if(data.msg != null) {
	        			layer.alert(data.msg.message);
	        			return;
	        		} else {
	        			document.write(data);
	        		}
	        	} else {
	        		layer.alert("终止专场活动失败!");
        			return;
	        	}
	        }, no: function(){
	        }
	    }
	});
}

function refuseTopicBind() {
	var topicId = $(this).attr("topicId");
	if (null == topicId || "" == $.trim(topicId)
			|| "0" == $.trim(topicId)) {
		layer.alert("指定专场活动无效!");
		return;
	}
	$.layer({
	    shade: [0],
	    area: ['auto','auto'],
	    dialog: {
	        msg: '是否驳回指定活动？',
	        btns: 2,                    
	        type: 4,
	        btn: ['确定','取消'],
	        yes: function(){
	        	var data = syncPost(REFUSE_TOPIC, {
	        		topicId : topicId
	        	})
	        	if (null != data) {
	        		if (data.success) {
	        			layer.msg("活动已驳回!", 1, 9);
	        			searchTopics(1);
	        			return;
	        		} else {
	        			layer.alert(data.msg.message);
	        			return;
	        		}

	        	} else {
	        		layer.alert("驳回专场活动失败!");
        			return;
	        	}
	        }, no: function(){
	        }
	    }
	});
}