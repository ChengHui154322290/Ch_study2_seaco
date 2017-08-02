var CREATE_TOPIC = domain + "/topic/create";
var index = parent.layer.getFrameIndex(window.name);
$(document).ready(function() {
	$("#save").on("click", function() {
		var isSubmit = false;
		if (!validateForm()) {
			return;
		}

		if(!isSubmit){
			$.ajax({
				type : "post",
				url : CREATE_TOPIC,
				data : $("#addTopic").serialize(),
				success : function(data) {
					isSubmit = true;
					if (data.success) {
						parent.searchTopics(1);
						parent.layer.close(index);
					} else {
						layer.alert(data.msg.message);
					}
				},
				error : function(data) {
					layer.alert("新增失败!");
				}
			})
		}
	});
	$("#cancel").on("click", function() {
		parent.layer.close(index);
	});

});

function validateForm() {
	var type = $("#type").val();
	var name = $("#name").val();
	var valid = true;
	if (null == name || $.trim(name).length == 0) {
		validateInfo("必填", $("#name"), 2);
		valid = false;
	}
	if (null == type || parseInt(type) < 1 || parseInt(type) > 5) {
		validateInfo("不在有效范围内", $("#type"), 2);
		valid = false;
	}
	return valid;
}

function validateInfo(txt, control, time) {
	layer.tips(txt, control, {
		guide : 1,
		time : time,
		more : true
	});
}