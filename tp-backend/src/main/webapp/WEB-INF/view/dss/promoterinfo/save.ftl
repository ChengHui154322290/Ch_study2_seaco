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
<form method="post" action="${domain}/dss/promoterinfo/save.htm" id="savepromoterinfoForm">
<input type="hidden" name="promoterId" value="${promoterInfo.promoterId}"/>
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>姓名</td>
						<td><input id="promoterName" type="text" name="promoterName" value="${promoterInfo.promoterName}" class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>店铺昵称</td>
						<td>${promoterInfo.nickName}</td>
					</tr>
					<tr>
						<td>密码</td>
						<td><input id="passWord" type="password" name="passWord"  value="<#if promoterInfo.passWord??>Xggj2016!@#</#if>"  class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>确认密码</td>
						<td><input id="confirmPassWord" type="password" name="confirmPassWord"  value="<#if promoterInfo.passWord??>Xggj2016!@#</#if>" class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>性别</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select id="gender" name="gender" class="select"> 
			                       	<#list genderList as gender>
			                       		<option value="${gender.code}" <#if gender.code==promoterInfo.gender>selected</#if> >${gender.cnName}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
		                 </td>
					</tr>
					<tr>
						<td>出生日期</td>
						<td><input id="birthday" type="text" name="birthday" value="<#if promoterInfo.birthday??>${(promoterInfo.birthday)?string("yyyy-MM-dd")}</#if>" class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>手机号</td>
						<td><input id="mobile" type="text" name="mobile" value="${promoterInfo.mobile}" class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>QQ</td>
						<td><input id="qq" type="text" name="qq" value="${promoterInfo.qq}" class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>微信</td>
						<td><input id="weixin" type="text" name="weixin" value="${promoterInfo.weixin}" class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>证件类型</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select id="credentialType" name="credentialType" class="select"> 
			                       	<#list credentialTypeList as credentialType>
			                       		<option value="${credentialType.code}" <#if credentialType.code==promoterInfo.credentialType>selected</#if> >${credentialType.cnName}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
						</td>
					</tr>
					<tr>
						<td>证件号码</td>
						<td><input id="credentialCode" type="text" name="credentialCode" value="${promoterInfo.credentialCode}" class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>开户银行</td>
						<td><input id="bankName" type="text" name="bankName" value="${promoterInfo.bankName}" class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>银行卡号码</td>
						<td><input id="bankAccount" type="text" name="bankAccount" value="${promoterInfo.bankAccount}" class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>支付宝帐户</td>
						<td><input id="alipay" type="text" name="alipay" value="${promoterInfo.alipay}" class="input-text lh25" size="20"></td>
					</tr>
					<tr>
						<td>推广类型</td>
						<td>
							<#if promoterInfo.promoterId??>
								<#list promoteTypeList as pt>
								<#if pt.code==promoterInfo.promoterType>${pt.cnName}</#if>
								</#list>
							<#else>													
								<span class="fl">
		                    	  <div class="select_border"> 
		                    	    <div class="select_containers "> 
		                    	    <select id="promoterType" name="promoterType" class="select"> 
			            	           	<#list promoteTypeList as pt>
			        	               		<option value="${pt.code}" <#if pt.code==promoterInfo.promoterType>selected</#if> >${pt.cnName}</option>
			    	                   	</#list>
		    	                    </select> 
			                        </div> 
			                      </div> 
			                    </span>		                    
   							</#if>
						</td>
					</tr>
					<tr>
						<td>拉新佣金</td>
						<td><input id="referralUnit" type="text" name="referralUnit"  value="${promoterInfo.referralUnit}" class="input-text lh25" size="20">元</td>
					</tr>
					<tr>
						<td>返佣比例</td>
						<td><input id="commisionRate" type="text" name="commisionRate" onblur=IsNum()  value="${promoterInfo.commisionRate}" class="input-text lh25" size="20">%</td>
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
