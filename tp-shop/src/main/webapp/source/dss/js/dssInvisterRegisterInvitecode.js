$(function () {
 $('.div-register,#achieve')[xigou.events.click](function () {
		var inviteCode=$("#input-inviste-code").val() 
		if(inviteCode==""){
			$.tips({
				content : "邀请码不能为空！",
				stayTime : 2000,
				type : "warn"
			});
		}
		var params = {
			'inviteCode': inviteCode
		};
		xigou.checkInvisteCode({
			requestBody: params,
			callback: function(response, status) {
				if (status == xigou.dictionary.success && response && response.data ) {	// 邀请码有效
					window.location.href = "./dssInviisteRegister.html?inviteCode="+inviteCode;
				}
				else {	// 跳转到分销员注册页面
					$.tips({
						content : "邀请码无效！",
						stayTime : 2000,
						type : "warn"
					});
				}
			}
		})
	 })
});



