var pageii;
$(function(){
	$(":text[name='startTime']").datetimepicker();
	$(":text[name='endTime']").datetimepicker();
});

//查看
function view(type) {
	showTab(type + "_util_list_show", "工具", "/sys/util/" + type + '.htm');	
	return false;
}

function showTab(id, text, url) {
	var tv = {};
	tv.linkId = id+"_link";
	tv.tabId =  id;
	tv.url = url;
	tv.text = text;
	try{
		window.parent.showTab(tv);
	} catch(e){
	}
}