<form id="updatepasswordform" action="" method="post" >
<dl class="dl-horizontal" style="margin-right:110px;">
    <dt >用户名：</dt><dd><input type="text" class="form-control" value="${(supplieruserDo.loginName)!}" readOnly="readOnly" name="loginName" id="loginName"/></dd><br/>
    <dt style="display:none;"></dt><dd><input  class="form-control" value=""  type="password" style="display:none;"/></dd>
    <dt >原始密码：</dt><dd><input type="password" class="form-control" id="oldpassword" value="" /></dd><br/>
    <dt >新密码：</dt><dd><input type="password" class="form-control" id="password" name="password" /></dd><br/>
    <dt >确认密码：</dt><dd><input type="password" class="form-control" id="confirmPassword" name="confirmPassword" /></dd><br/>
    <dt></dt>        
    <dd>
    	<input type="button" class="btn btn-info btn-sm"  onclick="updatePassword();" value="保存">&nbsp;&nbsp;&nbsp;&nbsp;
    	<input type="button" class="btn btn-warning btn-sm" value="取消" onclick="hideOldPopPage()">
    </dd>
</dl>
</form>