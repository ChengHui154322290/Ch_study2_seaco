var pageii;
$(function(){
	//取消按钮
	$('#closeBtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
	//确认
	$('#confirmImportBtn').on('click',function(){
		parent.$('#logId').val($(this).attr("logId"));
		parent.newTab("item_import","商品导入","/seller/item/import.htm");
		parent.layer.close(parent.pageii);
	});
	
	$('#downTemplateBtn').on('click',function(){
		var isWave = parent.$('#chooseTemplate').val();
		if(isWave!=""){
			$(this).attr('href','downExcelTemplate.htm?waveSign='+isWave);
		}else{
			$(this).attr('href','downExcelTemplate.htm');
		}
		
	});
	

	/**
	 * 上传附件
	 */
	$('.dataFormSave').on('click',function(){
		if(check()){
			var isWave = parent.$('#chooseTemplate').val();
			if(isWave!=""){
				 $('#inputForm').attr('action',"uploadSkuExcel.htm?waveSign="+isWave);
			}else{
				 $('#inputForm').attr('action',"uploadSkuExcel.htm");
			}
			
			$('#inputForm').submit();	
		}
	}); 
	
	
	/**
	 * 导出excel
	 */
	$('#exportFileBtn').on('click',function(){
		if($("#logId").val() == "") {
			alert("请上传数据后，再导出");
			return ;
		}
		$('#importLogForm').attr('action','/seller/item/export.htm');
		$('#importLogForm').submit();
		$('#importLogForm').attr('action','/seller/item/import.htm');
		
	}); 
	
}); 	

function check(){
	var fileName = $("#skuExcel").val();
	if($.trim(fileName)==''){
		alert("请选择导入模板");
	    return false;
	}
	if(fileName.lastIndexOf(".")!=-1){
		var fileType = (fileName.substring(fileName.lastIndexOf(".")+1,fileName.length)).toLowerCase();
	    var suppotFile = new Array();
	    suppotFile[0] = "xls";
	    suppotFile[1] = "xlsx";
	    for(var i =0;i<suppotFile.length;i++){
	       if(suppotFile[i]==fileType){
	    	   return true;
	       }else{
	    	   continue;
	       }
	    }
	    alert("不支持文件类型"+fileType);
		return false;
	 }
}