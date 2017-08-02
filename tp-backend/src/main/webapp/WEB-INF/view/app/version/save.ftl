<#include "/common/common.ftl"/> 
<@backend title="编辑推送消息" js=[ 
	'/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/supplier/component/date/WdatePicker.js',
	'/statics/js/validation/jquery.validate.min.js',
	'/statics/backend/js/app/versioninfo.js'] 
	css=['/statics/backend/css/apppush.css','/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
<#if resultInfo.msg??>${resultInfo.msg.message}<#else>
<#assign versionInfo = resultInfo.data>
<form method="post" action="${domain}/app/version/save.htm" id="saveVersionInfoForm">
<input type="hidden" name="id" value="${versionInfo.id}"/>
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<div class="box_top">
					<b class="pl15">APP版本管理</b>
				</div>
				<div class="box_center pt10 pb10">
					<table class="form_table" border="0" cellpadding="0" cellspacing="0">
						<tr><td style="width:120px;"></td><td></td><tr>
						<tr>
							<td><span style="color: red;">*</span>版本号：</td>
							<td>
											<div style="float:left">
												<input type="text" name="version" id="version" class="input-text lh25" size="45" maxlength = "25" value="${versionInfo.version}" placeholder="版本号" requeried>
											</div>
											<div id="titleTip" style="width:250px;float:left"></div>
										</td>
						</tr>
						<tr>
							<td><span style="color: red;">*</span>平台：</td>
							<td>
								<span class="fl">
			                      <div class="select_border"> 
			                        <div class="select_containers "> 
			                        <select name="platform" id="platform" class="select" > 
				                        <option value="">请选择</option> 
				                       	<#list platformList as platform>
				                       		<option value="${platform.code}" <#if platform.code==versionInfo.platform>selected</#if> > ${platform.desc} </option>
				                       	</#list>
			                        </select> 
			                        </div> 
			                      </div> 
			                    </span>
                    		</td>
						</tr>
						<tr>
						<td><span style="color: red;">*</span>是否最新：</td>
						<td>
						<input id="radioNew1" type="radio" name="isNew" value="1" <#if versionInfo.isNew==1>checked='checked'</#if> />是
						<input id="radioNew2" type="radio" name="isNew" value="0" <#if versionInfo.isNew==0 || versionInfo.isNew==null>checked='checked'</#if> />否
							</td>
						</tr>
						<tr>
							<td>下载地址：</td>
						<td>
									<div style="float:left">
												<input type="text" name="downUrl" id="downUrl" class="input-text lh25" size="45" maxlength = "100" value="${versionInfo.downUrl}" placeholder="下载地址" requeried>
											</div>
											<div id="titleTip" style="width:250px;float:left"></div>
							</td>
						</tr>
						<tr>
							<td>更新内容：</td>
							<td>
								<textarea rows="10" cols="80" name = "remark" style="overflow:hidden">${versionInfo.remark}</textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div style="margin-top:30px;text-align:center;margin-top:10px;">
		<input type="button" value="保存" class="btn btn82 btn_add" id="saveVersionInfoBtn" name="saveVersionInfoBtn">
	</div>
</div>
</form>
</#if>
</@backend>
