var pageii;
$(function(){
	
	/**
	 * 导入结算文件
	 */
	$('#importSettleFile').unbind().bind('click',function(){
		  pageii=$.layer({
            type : 2,
            title: '上传文件',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['600px','300px', 300],                     
            iframe: {
                src : domain+'/cms/uploadMasterSettleFile.htm'
            } 
        });
	}); 
	
	//取消按钮
	$('#closeBtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
	//确认
	$('#confirmImportBtn').on('click',function(){
		//parent.$('#logId').val($(this).attr("logId"));
		parent.$('#mastListQuery').click();
		parent.layer.close(parent.pageii);
	});
	
	
	
	/**
	 * 上传附件
	 */
	$('#dataFormSave').live('click',function(){
		if(check()){
			$('#inputForm').submit();	
			/*$("#dataFormSave").attr("action",domain +"/cms/uploadMasterOrderExcel.htm"); 
			$('#dataFormSave').ajaxSubmit(function(data){
				alert(data);
		    });*/
		}
	}); 
	
	/**
	 * 导出excel
	 */
	$('#exportSettle').unbind().bind('click',function(){
		$('#contract_list_form').attr('action','export.htm');
		$('#contract_list_form').submit();
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