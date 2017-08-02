var detail = domain+"/app/skin/skinDetail.htm";

var uploadPic = domain+"/app/skin/uploadPic/";

var saveSkin = domain+"/app/skin/save.htm";

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

	$(".image-style").on("click",function () {
		picOperate(this);
	});
	$(".image-style-A").on("click",function () {
		picOperate(this)
	});

$(".edit-class").on("click",function () {
	var id = $(this).attr("data");
	$.layer({
		type : 2,
		title : '添加皮肤',
		shadeClose : true,
		maxmin : true,
		fix : true,
		area : [ '600px', 600 ],
		iframe : {
			src : detail+"?id="+id
		}

	});
})



	pageii=	$(".addButton").on("click", function() {
		$.layer({
			type : 2,
			title : '添加皮肤',
			shadeClose : true,
			maxmin : true,
			fix : true,
			area : [ '600px', 600 ],
			iframe : {
				src : detail
			}

		});
	});


	$("#save").on("click",function () {
		var name = $("#name").val();
		var id = $("#id").val();
		if(name == null || name.trim()==""){
			layer.alert("请填写皮肤名称");
			return
		}
		var startTime = $("#startTime").val();
		if(startTime == null || startTime.trim()==""){
			layer.alert("填写时间区间A");
			return;
		}
		var endTime = $("#endTime").val();
		if(endTime == null || endTime.trim()==""){
			layer.alert("填写时间区间B");
			return;
		}


		var iconA = $("#iconA").val();
		if(iconA == null || iconA.trim()==""){
			layer.alert("上传ICONA");
			return;
		}

		var iconB = $("#iconB").val();
		if(iconB == null || iconB.trim()==""){
			layer.alert("上传ICONB");
			return;
		}

		var iconC = $("#iconC").val();
		if(iconC == null || iconC.trim()==""){
			layer.alert("上传iconC");
			return;
		}

		var iconD = $("#iconD").val();
		if(iconD == null || iconD.trim()==""){
			layer.alert("上传iconD");
			return;
		}


		var iconASelected = $("#iconASelected").val();
		if(iconASelected == null || iconASelected.trim()==""){
			layer.alert("上传ICONASELECTED");
			return;
		}

		var iconBSelected = $("#iconBSelected").val();
		if(iconBSelected == null || iconBSelected.trim()==""){
			layer.alert("上传ICONBSELECTED");
			return;
		}

		var iconCSelected = $("#iconCSelected").val();
		if(iconCSelected == null || iconCSelected.trim()==""){
			layer.alert("上传ICONCSELECTED");
			return;
		}

		var iconDSelected = $("#iconDSelected").val();
		if(iconDSelected == null || iconDSelected.trim()==""){
			layer.alert("上传iconDSelected");
			return;
		}

		var tapBar = $("#tapBar").val();
		if(tapBar == null || tapBar.trim()==""){
			layer.alert("上传TAPBAR");
			return;
		}

		var selectedColor = $("#selected-color").attr("data-color");
		if(selectedColor == null || selectedColor.trim()==""){
			layer.alert("请选择[选中颜色]");
			return;
		}


		var unSelectedColor = $("#un-selected-color").attr("data-color");
		if(unSelectedColor == null || unSelectedColor.trim()==""){
			layer.alert("请选择[未选中颜色]");
			return;
		}

		var status = $("#status").val();

		$.post(saveSkin,{ id: id, name:name,startTime: startTime,endTime:endTime,status:status,iconA:iconA,iconB:iconB,iconC:iconC,iconD:iconD,iconASelected:iconASelected,iconBSelected:iconBSelected,iconCSelected:iconCSelected,iconDSelected:iconDSelected,tapBar:tapBar,selectedColor:selectedColor,unSelectedColor:unSelectedColor},function (res) {
			if(res.success){
				layer.alert("操作成功");
				parent.window.location.reload();
			}else {
				layer.alert(res.msg.message);
			}
		})



	});

	addColorEvent($("input[id='selected-color']"),$("input[id='selected-color']").attr("data-color"));
	addColorEvent($("input[id='un-selected-color']"),$("input[id='un-selected-color']").attr("data-color"));

});

function picOperate( tar) {
	var mode = $(tar).attr("id");
	var url = uploadPic+mode;
	$.layer({
		type : 2,
		title : '上传图片',
		shadeClose : true,
		maxmin : true,
		fix : false,
		area: [400, 400],
		iframe : {
			src : url
		}
	});
}


function addColorEvent(selector, initColor) {
	var color = initColor || "#ffffff";
	selector.spectrum({
		color: color,
		showInput: true,
		className: "full-spectrum",
		showInitial: true,
		showPalette: true,
		showSelectionPalette: true,
		maxSelectionSize: 5,
		preferredFormat: "hex",
		localStorageKey: "spectrum.demo",
		move: function (color) {
		//	$(selector).attr("data-color",color.toHexString());
		},
		show: function () {

		},
		beforeShow: function () {

		},
		hide: function (color) {
			$(selector).attr("data-color",color.toHexString());

			// if($(this).attr("name") == 'color')
			//
			// 	$(this).closest("tr").find("[name='tags']").css("background-color", color.toHexString());
			// else {
			// 	$(this).closest("tr").find("[name='tags']").css("color", color.toHexString());
			// }
			// $(this).val(color.toHexString());
		},
		change: function(color) {
			//$(selector).attr("data-color",color.toHexString());
		},
		palette: [
			["rgb(0, 0, 0)", "rgb(67, 67, 67)", "rgb(102, 102, 102)",
				"rgb(204, 204, 204)", "rgb(217, 217, 217)","rgb(255, 255, 255)"],
			["rgb(152, 0, 0)", "rgb(255, 0, 0)", "rgb(255, 153, 0)", "rgb(255, 255, 0)", "rgb(0, 255, 0)",
				"rgb(0, 255, 255)", "rgb(74, 134, 232)", "rgb(0, 0, 255)", "rgb(153, 0, 255)", "rgb(255, 0, 255)"],
			["rgb(230, 184, 175)", "rgb(244, 204, 204)", "rgb(252, 229, 205)", "rgb(255, 242, 204)", "rgb(217, 234, 211)",
				"rgb(208, 224, 227)", "rgb(201, 218, 248)", "rgb(207, 226, 243)", "rgb(217, 210, 233)", "rgb(234, 209, 220)",
				"rgb(221, 126, 107)", "rgb(234, 153, 153)", "rgb(249, 203, 156)", "rgb(255, 229, 153)", "rgb(182, 215, 168)",
				"rgb(162, 196, 201)", "rgb(164, 194, 244)", "rgb(159, 197, 232)", "rgb(180, 167, 214)", "rgb(213, 166, 189)",
				"rgb(204, 65, 37)", "rgb(224, 102, 102)", "rgb(246, 178, 107)", "rgb(255, 217, 102)", "rgb(147, 196, 125)",
				"rgb(118, 165, 175)", "rgb(109, 158, 235)", "rgb(111, 168, 220)", "rgb(142, 124, 195)", "rgb(194, 123, 160)",
				"rgb(166, 28, 0)", "rgb(204, 0, 0)", "rgb(230, 145, 56)", "rgb(241, 194, 50)", "rgb(106, 168, 79)",
				"rgb(69, 129, 142)", "rgb(60, 120, 216)", "rgb(61, 133, 198)", "rgb(103, 78, 167)", "rgb(166, 77, 121)",
				"rgb(91, 15, 0)", "rgb(102, 0, 0)", "rgb(120, 63, 4)", "rgb(127, 96, 0)", "rgb(39, 78, 19)",
				"rgb(12, 52, 61)", "rgb(28, 69, 135)", "rgb(7, 55, 99)", "rgb(32, 18, 77)", "rgb(76, 17, 48)"]
		]
	});
}
