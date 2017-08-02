var detail = domain+"/app/switch/switchDetail.htm";

var addDetail = domain+"/app/switch/addSwitch.htm";

var operate = domain+"/app/switch/operate.htm";

var index = parent.layer.getFrameIndex(window.name);

var pageii ;

$(function(){
	$("#cancel").on("click",function () {
		parent.layer.close(index);
	})

	$(".enable-config").on("click",function () {
		var id = $(this).attr("data");
		$.post(operate,{id:id,status:1},function (res) {
			if(res.success){
				layer.alert("操作成功");
				window.location.reload();
			}else {
				layer.alert(res.msg.message);
			}
		});
	});

	$(".detail-config").on("click",function () {
		var id = $(this).attr("data");
		$.layer({
			type : 2,
			title : '添加开关信息',
			shadeClose : true,
			maxmin : true,
			fix : true,
			area : [ '600px', 580 ],
			iframe : {
				src : detail
			}

		});
	});

	$(".disable-config").on("click",function () {
		var id = $(this).attr("data");
		$.post(operate,{id:id,status:0},function (res) {
			if(res.success){
				layer.alert("操作成功");
				window.location.reload();
			}else {
				layer.alert(res.msg.message);
			}
		});
	})

	$(".del-config").on("click",function () {
		var id = $(this).attr("data");
		layer.confirm("确定删除?",function () {
			$.post(dcOperate,{id:id,status:2},function (res) {
				if(res.success){
					layer.alert("操作成功");
					window.location.reload();
				}else {
					layer.alert(res.msg.message);
				}
			});
		})

	})


	pageii=	$(".addButton").on("click", function() {
		$.layer({
			type : 2,
			title : '添加开关信息',
			shadeClose : true,
			maxmin : true,
			fix : true,
			area : [ '600px', 580 ],
			iframe : {
				src : detail
			}

		});
	});


	$("#save").on("click",function () {
		var name = $("#name").val();
		if(name == null || name.trim()==""){
			layer.alert("请填写开关名称");
			return
		}

		var code = $("#code").val();
		if(code == null || code.trim()==""){
			layer.alert("请填写开关CODE");
			return;
		}
		var status = $("#status").val();

		$.post(addDetail,{name:name,code:code,status:status},function (res) {
			if(res.success){
				layer.alert("操作成功");
				parent.window.location.reload();
			}else {
				layer.alert(res.msg.message);
			}
		})



	})
});
