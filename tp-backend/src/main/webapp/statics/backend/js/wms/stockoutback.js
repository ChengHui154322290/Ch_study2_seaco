/**
 * 
 */
$(function() {
	//列表状态下拉框设置选中
	var status=$("#statusSelected").val();
	$("#status").find("option[value='"+status+"']").attr("selected",true);
});

function addTab(id,text,tabUrl){
	var tv = {};
	tv.linkId = id+"_link";
	tv.tabId =  id;
	tv.url = tabUrl;
	tv.text = text;
	try{
		window.parent.showTab(tv);
	} catch(e){
	}
}
/*
 * 发货单回执明细
 */
function viewItem(id){
	addTab("view_stock_out_back_item"+id,"出库单回执管理-查看出库回执详情","/wms/stockoutBack/viewItem.htm?id="+id);
	return false;
}
