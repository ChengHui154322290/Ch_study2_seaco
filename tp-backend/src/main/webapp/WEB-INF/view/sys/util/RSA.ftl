<#include "/common/common.ftl"/> 
<@backend title="" js=[ '/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
	'/statics/backend/js/sys/util/util.js'] 
	css=[
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css'] >

<form action="${domain}/sys/util/rsa.htm" method="post" id="RSAUtilForm" class="RSAUtilForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">RSA加解密工具,加密用私钥,解密用公钥</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>数据：</td>
						<td><textarea style="height:150px;width:680px" class="input-text lh30" name="content" id="content">${content}</textarea></td>
					</tr>
					<tr>
						<td>KEY:</td>
						<td><input type="text" name="key" id="key" value="${key}"  class="input-text lh25" size="50"></input></td>
					</tr>
					<tr>
						<td>签名:</td>
						<td><input type="text" name="sign" id="key" value="${sign}"  class="input-text lh25" size="50"></input></td>
					</tr>
					<tr>
						<td><input type="button" value="签名" id="RSASign" class="btn btn82 btn_save2"/></td>
						<td><input type="button" value="验签" id="RSAVerify" class="btn btn82 btn_save2"/></td>
					</tr>
					<tr>
						<td>结果：</td>
						<td><textarea style="height:150px;width:680px" class="input-text lh30" name="result" id="result">${result}</textarea></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
</form>
</@backend>