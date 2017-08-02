$(function(){
	$('#storageType').change(function(){
		var type = $(this).val();
		if(type=="1"||type=="2"||type=="3"){
			$("#bondedArea").val("0");
			$(".bondedAreaTr").hide();
		}else{
			bondedAreaChange(type, $("#bondedArea"));
			$(".bondedAreaTr").show();
		}
	});
	
	function bondedAreaChange(id,target){
		var url = domain+"/storage/warehouse/bondedarea-list.htm";
		$.ajax({
			type : "get",
			url : url,
			data : "typeId=" + id,
			async : false,
			success : function(data){
				if(data){
					target.html('');
					target.append("<option value='' >请选择</option>");
					$.each(data,function(i,n){
						var bid = n.id;
						var name = n.name;
						var opt = $("<option />");
						opt.val(bid);
						opt.html(name);
						if(id == "5" && name == "海外直邮") //海外直购默认选择海外直邮
							opt.attr("selected",true);
						target.append(opt);
					});
				}
			}
		});
	}
});