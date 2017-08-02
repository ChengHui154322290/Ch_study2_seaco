jQuery(function(){
	
	/*if(!needRefresh){
		window.location.href="/tologin?fm=re";
	}*/
	
	jQuery("#changeSecurite").click(function(){
		var codeUrl = "/securityCode";
		jQuery("#securiteCode").attr("src",codeUrl+"?da="+new Date().getTime());
	});
	
	jQuery("#loginBtn").click(function(){
		removeErrorMsg();
		/**
		 * 校验验证码
		 */
		jQuery.ajax({
			type: "post",
			url: "/checkSecuriteCode",
			data:{"code":jQuery("#checkCodeTxt").val()},
			dataType:"json",
			success: function(data){
				if(data.success && data.data){
					jQuery("#loginForm").submit();
				} else {
					addErrorMsg(1);
				}
			}
		});
	});
	
	/**
	 * 移除错误信息
	 */
	function removeErrorMsg(){
		jQuery("#checkCodeTxt").parent().removeClass("has-error");
		jQuery("#errorMsg").hide();
	}
	
	/**
	 * 添加错误信息
	 */
	function addErrorMsg(errorType){
		if(1 == errorType){
			jQuery("#checkCodeTxt").parent().addClass("has-error");
			jQuery("#errorMsg").show();
		}
	}
	
});