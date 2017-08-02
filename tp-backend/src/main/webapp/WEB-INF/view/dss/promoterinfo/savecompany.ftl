<#include "/common/common.ftl"/> 
<@backend title="促销人员管理" 
 js=[ 
    '/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
    '/statics/backend/js/layer/layer.min.js',
 	'/statics/js/validation/jquery.validate.min.js',
    '/statics/js/validation/jquery.validate.method.min.js',
	'/statics/backend/js/dss/promoterinfo.js'] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css','/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css'] >
<#if resultInfo.msg??>${resultInfo.msg.message}<#else>
<#assign promoterInfo = resultInfo.data>
<form method="post" action="${domain}/dss/promoterinfo/save.htm" id="savepromotercompanyForm">
<input type="hidden" name="promoterId" value="${promoterInfo.promoterId}"/>
<input type="hidden" name="promoterType" value="3"/>
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>公司全称</td>
						<td><input id="promoterName" type="text" name="promoterName" value="${promoterInfo.promoterName}" class="input-text lh25" size="40"></td>
					</tr>
					<tr>
						<td>商城名称</td>
						<td><input id="nickName" type="text" name="nickName" value="${promoterInfo.nickName}" class="input-text lh25" size="40" maxlength="10" placeholder="商城显示名称及发送短信公司名"></td>
					</tr>
					<tr>
						<td>密码</td>
						<td><input id="passWord" type="password" name="passWord"  value="<#if promoterInfo.passWord??>Xggj2016!@#</#if>"  class="input-text lh25" size="40"></td>
					</tr>
					<tr>
						<td>确认密码</td>
						<td><input id="confirmPassWord" type="password" name="confirmPassWord"  value="<#if promoterInfo.passWord??>Xggj2016!@#</#if>" class="input-text lh25" size="40"></td>
					</tr>
					<tr>
						<td>联系手机</td>
						<td><input id="mobile" type="text" name="mobile" value="${promoterInfo.mobile}" class="input-text lh25" size="40" placeholder="营销管理登录绑定手机"></td>
					</tr>
					<tr>
						<td>开户银行</td>
						<td><input id="bankName" type="text" name="bankName" value="${promoterInfo.bankName}" class="input-text lh25" size="40"></td>
					</tr>
					<tr>
						<td>银行卡号码</td>
						<td><input id="bankAccount" type="text" name="bankAccount" value="${promoterInfo.bankAccount}" class="input-text lh25"  size="40"></td>
					</tr>
					
					<tr>
						<td>渠道代码</td>
						<td><#if promoterInfo.promoterId!=null>${promoterInfo.channelCode}<#else> <input id="channelCode" type="text" name="channelCode" value="${promoterInfo.channelCode}" class="input-text lh25"  size="40" placeholder="用于域名的创建"></#if></td>
					</tr>
					<tr>
						<td>分享标题</td>
						<td><input id="shareTitle" type="text" name="shareTitle" value="${promoterInfo.shareTitle}" class="input-text lh25" size="40"></td>
					</tr>
					<tr>
						<td>分享内容</td>
						<td><textarea id="shareContent" name="shareContent" class="textarea">${promoterInfo.shareContent}</textarea></td>
					</tr>
					<tr>
						<td>是否开通分销</td>
						<td>
							<select id="companyDssType" name="companyDssType" class="select" style="width:100px;">
								<option value="">请选择</option>
								<option value="0" <#if promoterInfo.companyDssType == 0>selected="selected"</#if>>否</option>
								<option value="1" <#if promoterInfo.companyDssType == 1>selected="selected"</#if>>是</option>
							</select>
						</td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: center;">
					<input type="submit" value="编辑" class="btn btn82 btn_add savepromoterinfobtn" name="button" id="savepromoterinfobtn">
					<#if promoterInfo.promoterId??><input type="submit" value="重置密码" class="btn btn82 btn_add refreshpasswordbtn" name="button" id="refreshpasswordbtn"></#if>
				</div>
			</div>
		</div>
	</div>
</div>
</form>
</#if>
</@backend>
