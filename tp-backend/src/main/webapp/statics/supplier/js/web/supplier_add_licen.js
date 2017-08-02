jQuery(document).ready(function(){
	$('[imagenameattribute]').each(function(i){
		var imagenameattribute = $(this).attr('imagenameattribute');
		QiniuUpload(true,imagenameattribute,bucketname,bucketURL,"pickfiles_"+imagenameattribute,"container_"+imagenameattribute,"imguplod_"+imagenameattribute);
	});
    
	window.parent.popWaitDivMap.hideWaitDiv();
	jQuery("#sp_add_lien_submit").click(function(){
		var licenform = jQuery("#sp_licen_info_form");
		if(!validateInfo()){
			return;
		}
		$.ajax({
			url:licenform.attr('action'),
			data:licenform.serialize(),
			dataType:'json',
			type:'post',
			success:function(result){
				if(result.success){
					alertMsg('添加成功');
					disableAllBtn();
					window.location.href='/supplier/supplierList';
				}else{
					alertMsg(result.msg.message);
				}
			}
		});
	});
	
	jQuery("#sp_add_lien_submitAudit").click(function(){
		if(!validateInfo()){
			return;
		}
		jQuery("#needAuditId").val(1);
		var licenform = jQuery("#sp_licen_info_form");
		$.ajax({
			url:licenform.attr('action'),
			data:licenform.serialize(),
			dataType:'json',
			type:'post',
			success:function(result){
				if(result.success){
					alertMsg('添加成功');
					disableAllBtn();
					window.location.href='/supplier/supplierList';
				}else{
					alertMsg(result.msg.message);
				}
			}
		});
		
	});
	
	function disableAllBtn(){
		jQuery("#sp_add_lien_submit").attr("disabled",true);
		jQuery("#sp_add_lien_submitAudit").attr("disabled",true);
	}
});

