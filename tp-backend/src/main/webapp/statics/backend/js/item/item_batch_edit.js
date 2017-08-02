var pageii;
$(function(){
	
	//取消按钮
	$('#closeBtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
	$('#downEditTemplateBtn').live('click',function(){
		$(this).attr('href','downEditExcelTemplate.htm');
	});
	
	/**
	 * 上传附件
	 */
	$('.eidtDataFormSave').live('click',function(){
		if(check()){
			$('#batchEditForm').attr('action',"uploadEditExcel.htm");			
			$('#batchEditForm').submit();	
		}
	}); 
	
});

function check(){
	var fileName = $("#editExcel").val();
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