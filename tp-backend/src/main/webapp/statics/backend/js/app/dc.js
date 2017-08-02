var dcDetail = domain+"/app/dcDetail.htm";

var addDetail = domain+"/app/addDetail.htm";

var dcOperate = domain+"/app/operate.htm";

var index = parent.layer.getFrameIndex(window.name);

var pageii ;

$(function(){
	$("#cancel").on("click",function () {
		parent.layer.close(index);
	})

	$(".enable-config").on("click",function () {
		var id = $(this).attr("data");
		$.post(dcOperate,{id:id,status:1},function (res) {
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
			title : '添加动态配置',
			shadeClose : true,
			maxmin : true,
			fix : true,
			area : [ '600px', 580 ],
			iframe : {
				src : dcDetail+"?id="+id
			}

		});
	});

	$(".disable-config").on("click",function () {
		var id = $(this).attr("data");
		$.post(dcOperate,{id:id,status:0},function (res) {
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
			title : '添加动态配置',
			shadeClose : true,
			maxmin : true,
			fix : true,
			area : [ '600px', 580 ],
			iframe : {
				src : dcDetail
			}

		});
	});


	$("#save").on("click",function () {
		var name = $("#name").val();
		if(name == null || name.trim()==""){
			layer.alert("请填写配置名称");
			return
		}

		var content = $("#content").val();
		if(content == null || content.trim()==""){
			layer.alert("请填写配置内容");
			return;
		}
		var id = $("#id").val();

		var  versionFrom = $("#versionFrom").val();
		var versionTo = $("#versionTo").val();
		if(versionFrom == null || versionFrom.trim()==""){
			layer.alert("请填写起始版本");
			return;
		}

		if(versionTo == null || versionTo.trim()==""){
			layer.alert("请填写终止版本");
			return;
		}

		$.post(addDetail,{name:name,content:content,id:id,versionFrom:versionFrom,versionTo:versionTo},function (res) {
			if(res.success){
				layer.alert("操作成功");
				parent.window.location.reload();
			}else {
				layer.alert(res.msg.message);
			}
		})



	})
});
