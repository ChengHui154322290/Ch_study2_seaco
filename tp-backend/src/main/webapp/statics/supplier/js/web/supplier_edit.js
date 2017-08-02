jQuery(document).ready(function(){
	jQuery("#supplierEditSaveBtn").click(function(){
		jQuery("font._errorMsg").remove();
		/*if(!validateInfo()){
			return false;
		}*/
		
		if(!checkSupplierMulInputField()){
			return;
		}
		
		//校验商家平台
		if(!checkSupplierUser()){
			return;
		}
		
		//海淘供应商物流校验
		var isHaitao = jQuery("input[name='isHtSupplier']:checked").val();
		
		if(1 == parseInt(isHaitao)){
			checkHaitaoInfo(function(express){
				jQuery("#expressTemplateId").val(express.id);
				jQuery("#expressTemplateName").val(express.name);
				//校验品类
				// jQuery("#supplierEditForm").submit();
				$.ajax({
					url:$('#supplierEditForm').attr('action'),
					data:$('#supplierEditForm').serialize(),
					dataType:'json',
					type:'post',
					success:function(result){
						if(result.success){
						alertMsg('提交成功');
						window.location.href='/supplier/supplierList';
						}else{
							alertMsg(result.msg.message);
						}
					}
				});								
				jQuery("#supplierEditSaveBtn").attr("disabled",true);
			});
		} else {
			//校验品类
			//jQuery("#supplierEditForm").submit();
			$.ajax({
				url:$('#supplierEditForm').attr('action'),
				data:$('#supplierEditForm').serialize(),
				dataType:'json',
				type:'post',
				success:function(result){
					if(result.success){
					alertMsg('提交成功');
					window.location.href='/supplier/supplierList';
					}else{
						alertMsg(result.msg.message);
					}
				}
			});											
			jQuery("#supplierEditSaveBtn").attr("disabled",true);
			//jumpToPage("/supplier/toSupplierLicenAdd.htm");
		}
	});
	
	/**
	 * 校验商家平台
	 */
	function checkSupplierUser(){
		var supplierUserpwd = jQuery("#sp_user_password").val();
		if(!supplierUserpwd){
			alertMsg("请输入商家平台密码。");
			return false;
		}
//		var pa=/^(\w){6,17}$/;
		var pa=/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,.\/]).{8,16}$/;
		if('商家平台密码' != supplierUserpwd &&  !pa.test(supplierUserpwd)){
			alertMsg("密码必须由 8-16位字母、数字、特殊符号线组成。");
			return false;
		}
		return true;
	}
	
	jQuery("#supplierEditSubBtn").click(function(){
		if(!checkSupplierMulInputField()){
			return;
		}
		var isHaitao = jQuery("input[name='isHtSupplier']:checked").val();
		if(1 == parseInt(isHaitao)){
			checkHaitaoInfo(function(express){
				jQuery("#expressTemplateId").val(express.id);
				jQuery("#expressTemplateName").val(express.name);
				confirmBox('确认提交?',function(){
					jQuery("#statusValId2").val("submit");
					// by zhs 0302
					$.ajax({
						url:$('#supplierEditForm').attr('action'),
						data:$('#supplierEditForm').serialize(),
						dataType:'json',
						type:'post',
						success:function(result){
							if(result.success){
							alertMsg('提交成功');
							window.location.href='/supplier/supplierList';
							}else{
								alertMsg(result.msg.message);
							}
						}
					});
				});
			});
		} else {
			confirmBox('确认提交?',function(){
				jQuery("#statusValId2").val("submit");
				$.ajax({
					url:$('#supplierEditForm').attr('action'),
					data:$('#supplierEditForm').serialize(),
					dataType:'json',
					type:'post',
					success:function(result){
						if(result.success){
						alertMsg('提交成功');
						window.location.href='/supplier/supplierList';
						}else{
							alertMsg(result.msg.message);
						}
					}
				});
			});
		}
		
	});
	
	jQuery("#supplierAuditEditSubBtn").click(function(){
		if(!checkSupplierMulInputField()){
			return;
		}
		
		//校验商家平台
		if(!checkSupplierUser()){
			return;
		}
		
		var isHaitao = jQuery("input[name='isHtSupplier']:checked").val();
		if(1 == parseInt(isHaitao)){
			checkHaitaoInfo(function(express){
				jQuery("#expressTemplateId").val(express.id);
				jQuery("#expressTemplateName").val(express.name);
				confirmBox('确认提交?',function(){
					jQuery("#statusValId2").val("submit");
					// jQuery("#supplierEditForm").submit();
					$.ajax({
						url:$('#supplierEditForm').attr('action'),
						data:$('#supplierEditForm').serialize(),
						dataType:'json',
						type:'post',
						success:function(result){
							if(result.success){
							alertMsg('提交成功');
							window.location.href='/supplier/supplierList';
							}else{
								alertMsg(result.msg.message);
							}
						}
					});
				});
			});
		} else {
			confirmBox('确认提交?',function(){
				jQuery("#statusValId2").val("submit");
				// jQuery("#supplierEditForm").submit();
				$.ajax({
					url:$('#supplierEditForm').attr('action'),
					data:$('#supplierEditForm').serialize(),
					dataType:'json',
					type:'post',
					success:function(result){
						if(result.success){
						alertMsg('提交成功');
						window.location.href='/supplier/supplierList';
						}else{
							alertMsg(result.msg.message);
						}
					}
				});
			});
		}
	});
	
	/**
	 * 取消
	 */
	jQuery("#supplierEditCancelBtn").click(function(){
		confirmBox('确定取消？',function(){
			jQuery("#statusValId").val("cancel");
			jQuery("#supplierAuditForm").attr("action","/supplier/supplierAuditSave.htm");
			jQuery("#supplierAuditForm").submit();
		});
	});
	
	/**
	 * 终止
	 */
	jQuery("#supplierShowStopBtn").click(function(){
		confirmBox('<font color="red">您确认一定要终止该供应商合作，终止后无法恢复供应商信息！</font>',function(){
			jQuery("#supplierShowAuditForm").submit();
		});
	});
	
	/**
	 * 供应商审核
	 */
	jQuery("#supplierShowAuditBtn").click(function(){
		var spId = jQuery("#supplierIdHiddenId").val();
		ajaxRequest({
			method:'post',
			url:'/supplier/examination/toExaminationPage.htm',
			data:{"supplierId":spId},
			success:function(data){
				showPopDiv(5,data,{"title":"审核"});
			}
		})
	});
	
	/**
	 * 注册品牌事件
	 */
	var brandFieldObjs = jQuery("input[id^='desc_field_']");
	for(var i=0;i<brandFieldObjs.length;i++){
		var idStr = jQuery(brandFieldObjs[i]).attr("id");
		if(idStr){
			var index = idStr.toString().replace("desc_field_","");
			registeImgTextEvent(index);
		}
	}
	
	/**
	 * 供应取消审核
	 */
	jQuery("#supplierEdit_canceled").click(function(){
		if(!checkSupplierMulInputField()){
			return;
		}
		var isHaitao = jQuery("input[name='isHtSupplier']:checked").val();
		if(1 == parseInt(isHaitao)){
			checkHaitaoInfo(function(express){
				jQuery("#expressTemplateId").val(express.id);
				jQuery("#expressTemplateName").val(express.name);
				confirmBox('确认提交?',function(){
					jQuery("#statusValId2").val("submit");
					jQuery("#supplierEditForm").submit();
				});
			});
		} else {
			confirmBox('确认提交?',function(){
				jQuery("#statusValId2").val("submit");
				jQuery("#supplierEditForm").submit();
			});
		}
	});
	
});