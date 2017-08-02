$(function(){
	
	var selectAll  = "<option value=''>--请选择分类--</option>";
	$('#largeIdSel').change(function(){
		var val = $(this).val();
		if(val==undefined||val==null||val.length==0){
			//清空中类
			$("#mediumIdSel").empty();
			$("#mediumIdSel").append(selectAll);
			$("#mediumIdSel").select2("val", '');
			//清空中类哈
			$("#smallIdSel").empty();
			$("#smallIdSel").append(selectAll);
			$("#smallIdSel").select2("val", '');
			
			return;
		}
		categoryChange(val, $("#mediumIdSel"));
		//清空小类哈
		$("#smallIdSel").empty();
		$("#smallIdSel").append(selectAll);
		var sval = $("#smallIdSel ").val();
		var mval = $("#mediumIdSel ").val();
	});
	
	$('#mediumIdSel').change(function(){
		var val = $(this).val();
		if(val==undefined||val==null||val.length==0){
			$("#smallIdSel").empty();
			$("#smallIdSel").append(selectAll);
			return;
		}
		categoryChange(val, $("#smallIdSel"));
	});
	
	function categoryChange(id,target){
		var url = domain+"/item/category-cld.htm";
		$.ajax({
			type : "get",
			url : url,
			data : "catId=" + id,
			async : false,
			success : function(data){
				if(data){
					target.html('');
					target.append(selectAll);
					$.each(data,function(i,n){
						var id = n.id;
						var name = n.name;
						//var code = n.code;
						var opt = $("<option />");
						opt.val(id);
						//name = name+"("+code+")";
						opt.html(name);
						target.append(opt);
					});
				}
			}
		});
	}
	
	//查询页面需要调用
	//selectCategory();
	
	//选中
	function selectCategory(){
		var _largeId = $("#largeIdSel").val();
		if(_largeId!=undefined && _largeId!=""){
			categoryChange(_largeId, $("#mediumIdSel"));
			var _mediumId = $("#mediumIdHidden").val();
			var _smallId = $("#smallIdHidden").val();
			if(_mediumId!=""){
				 $("#mediumIdSel option").each(function(){ 
					 var val = $(this).val();
					 if(val ==_mediumId){  
						 $(this).attr("selected",true); 
					 }
				 });  
				 categoryChange(_mediumId, $("#smallIdSel"));
				 if(_smallId!=""){
					$("#smallIdSel option").each(function(){  
						var val = $(this).val();
						 if(val ==_smallId){  
							 $(this).attr("selected",true); 
						 }
					 });  
				 }
			}
		}
	}
});