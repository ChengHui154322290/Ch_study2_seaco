var daleiHtml="";
function queryByCategoryAllByParentId(){
	$.ajax({
		url:domain+'/basedata/category/'+'queryByCategoryAllByParentId',
		dataType:'json',
		type:'get',
		async : false,
		success:function(result){
			if(result.success){
				var data = result.data;
				$.each(data,function(i,category){
					daleiHtml+='<option value="'+category.id+'">'+category.name+'<option>';
				});
			}
		}
	})
}