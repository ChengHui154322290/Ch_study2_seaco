//function closePopTab(){
//	closeCurrentTab();
//}

function updatePassword(){
		var loginName = jQuery("#loginName").val();
		var oldpassword = jQuery("#oldpassword").val();
		var password = jQuery("#password").val();
		var confirmPassword = jQuery("#confirmPassword").val();
		if(!password){
			alertMsg('密码不能为空。');
			return;
		}
		if(!oldpassword){
			alertMsg('原始密码不能为空。');
			return;
		}
		
		if (!checkPassword(password)) {
			return;
		}
		
		if(confirmPassword != password){
			alertMsg('新密码和确认密码不一致。');
			return;
		}
		ajaxRequest({
			method:'post',
			url:'/seller/pop_updatepassword.htm',
			data:{"loginName":loginName,"password":password,"oldpassword":oldpassword},
			success:function(data){
				if(data.success && 'false' != data.success){
					alertMsg('密码修改成功。');
				} else {
					alertMsg('密码修改失败。');
					return;
				}
			}
		})
}

function checkPassword(password){
	var pa=/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,.\/]).{8,16}$/;
	if( !pa.test(password)){
		alertMsg("新密码必须由 8-16位字母、数字、特殊符号线组成。");
		return false;
	}
	return true;
}


/**
 * 用户信息tab
 */
function showUserInfo(userName){
	showPopDiv("/seller/pop_userinfo",{"userName":userName});
}
