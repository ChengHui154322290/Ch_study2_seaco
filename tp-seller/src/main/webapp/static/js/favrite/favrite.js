Fav={
		addFavrite:function(menuId){
			var url = domain+"/favrite/addFavrite.htm";
			$.post(url,{'menuId':menuId},function(data){
				if(data.result==1){
					$.msgbox({msg:data.message,icon:"success"});
				}else{
					$.msgbox({msg:data.message,icon:"error"});
				}
			});
		},
		removeFavrite:function(menuId,callBack){
			var url = domain+"/favrite/removeFavrite.htm";
			$.post(url,{'menuId':menuId},function(data){
				if(data.result==1){
					$.msgbox({msg:data.message,icon:"success"});
					if(callBack&&typeof callBack == 'function'){
						callBack();
					}
				}else{
					$.msgbox({msg:data.message,icon:"error"});
				}
			});
		}
}

$(function(){
	refreshFavStatus();
	$('.menuCheckbox').change(function(){
		var menuId = $(this).attr('menuId');
		if(menuId == undefined){
			return;
		}
		var checked = $(this).attr('checked');
		if(checked){
			Fav.addFavrite(menuId);
		}else{
			Fav.removeFavrite(menuId);
		}
	});
});
function refreshFavStatus(){
	$('.menuCheckbox').each(function(){
		var menuId = $(this).attr("menuId");
		var url = domain+"/favrite/checkedFavrite.htm";
		var $this = $(this);
		$.post(url,{'menuId':menuId},function(data){
			if(data.result==1){
				if(data.errorCode=='404'){
					$this.attr('checked',false);
				}else{
					$this.attr('checked',true);
				}
			}else{
				$this.attr('checked',false);
			}
		});
	});
}
/**
 * 打开一个新的标签
 * @param id
 * 		menuId
 * @param name
 * 		标签名称
 * @param url
 * 		url
 */
function openNewOperTab(id,name,url){
	var tv={url:url,tabId:'favrite-tab-'+id,text:name};
	window.parent.showTab(tv);
}
