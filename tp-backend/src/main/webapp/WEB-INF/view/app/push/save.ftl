<#include "/common/common.ftl"/> 
<@backend title="编辑推送消息" js=[ 
	'/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/js/validation/jquery.validate.min.js',
	'/statics/backend/js/app/pushinfo.js'] 
	css=['/statics/backend/css/apppush.css',
	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css'] >
<#if resultInfo.msg??>${resultInfo.msg.message}<#else>
<#assign pushInfo = resultInfo.data>
<form method="post" action="${domain}/app/push/save.htm" id="savePushInfoForm">
<input type="hidden" name="id" value="${pushInfo.id}"/>
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<div class="box_top">
					<b class="pl15">APP客户端推送</b>
				</div>
				<div class="box_center pt10 pb10">
					<table class="form_table" border="0" cellpadding="0" cellspacing="0">
						<tr><td style="width:120px;"></td><td></td><tr>
						<tr>
							<td><span style="color: red;">*</span>推送标题：</td>
							<td>
											<div style="float:left">
												<input type="text" name="title" id="title" class="input-text lh25" size="45" maxlength = "100" value="${pushInfo.title}" placeholder="客户端推送标题" requeried>
											</div>
											<div id="titleTip" style="width:250px;float:left"></div>
										</td>
						</tr>
						<tr>
							<td>页面显示标题：</td>
							<td>
											<div style="float:left">
												<input type="text" name="pageTitle" id="title" class="input-text lh25" size="45" maxlength = "25" value="${pushInfo.pageTitle}" placeholder="页面显示标题" requeried>
											</div>
											<div id="pageTitleTip" style="width:250px;float:left"></div>
										</td>
						</tr>
						<tr>
							<td><span style="color: red;">*</span>推送类型：</td>
							<td>
								<span class="fl">
			                      <div class="select_border"> 
			                        <div class="select_containers "> 
			                        <select name="pushType" id="pushType" class="select" > 
				                        <option value="">请选择</option> 
				                       	<#list pushTypeList as pushType>
				                       		<option value="${pushType.code}" <#if pushType.code==pushInfo.pushType>selected</#if> >${pushType.desc}</option>
				                       	</#list>
			                        </select> 
			                        </div> 
			                      </div> 
			                    </span>
							</td>
						</tr>
						<tr>
							<td><span style="color: red;">*</span>推送平台：</td>
							<td>
								<span class="fl">
			                      <div class="select_border"> 
			                        <div class="select_containers "> 
			                        <select name="platform" id="platform" class="select" > 
				                        <option value="">请选择</option> 
				                       	<#list platformList as platform>
				                       		<option value="${platform.code}" <#if platform.code==pushInfo.platform>selected</#if> > ${platform.desc} </option>
				                       	</#list>
			                        </select> 
			                        </div> 
			                      </div> 
			                    </span>
                    		</td>
						</tr>
							<tr class = "link" id = "tr_link" <#if pushInfo.pushType == 0><#else> style="display:none"</#if>>
											<td style="width:116px;" ><span style="color: red">*</span>超链：</td>
											<td>
												<div style="float:left">
													<input type="text" name="link" id="link" class="input-text lh25" size="60" value="${pushInfo.link}" placeholder="超链URL地址" requeried>
												</div>
											</td>
							<tr>
							<tr class = "topic" id = "tr_topic" <#if pushInfo.pushType == 1 || pushInfo.pushType == 2 ><#else> style="display:none"</#if>>
											<td style="width:116px;"><span style="color: red">*</span>专场：</td>
											<td>
												<div style="float:left">
													<input type="text" name="tid" id="tid" class="input-text lh25" size="60" value="${pushInfo.tid}" placeholder="专场ID" requeried>
												</div>
											</td>
							<tr>
							<tr class = "sku" id = "tr_sku" <#if pushInfo.pushType == 2><#else> style="display:none"</#if>>
											<td style="width:116px;"><span style="color: red">*</span>商品：</td>
											<td>
												<div style="float:left">
													<input type="text" name="sku" id="sku" class="input-text lh25" size="60" value="${pushInfo.sku}" placeholder="商品SKU" requeried>
												</div>
											</td>
							<tr>
						<tr>
							<td><span style="color: red;">*</span>推送方式：</td>
							<td>
								<input id="pushStyle" type="radio" name="pushStyle" checked='checked' value="0"/>
								<label for="pushStyle" >按时间</label>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<div class="popover bottom push-style-1">
                					<div class="arrow"></div>
									<input id="sendtime1" type="radio" name="pushWay" value="1" <#if pushInfo.pushWay==1 || pushInfo.pushWay==null>checked='checked'</#if> />
									<label for="sendtime1" >即时</label>
									<input id="sendtime2" type="radio" name="pushWay" value="2" <#if pushInfo.pushWay==2>checked='checked'</#if> />
									<label for="sendtime2" >定时</label>
									<input type="text" name="sendDate" value="<#if pushInfo.sendDate??> ${(pushInfo.sendDate?string("yyyy-MM-dd HH:mm"))!}</#if>" <#if pushInfo.pushWay==2><#else>style="display:none;"</#if> class="input-text lh30 sendDate" size="20">
								</div>
							</td>
						</tr>
						<tr>
							<td><span style="color: red;">*</span>有效时长：</td>
							<td>
								<div style="float:left">
									<input type="text" id="activeTime" name="activeTime" class="input-text lh25" value="${pushInfo.activeTime!2}"/>小时, 该时间上线的用户均可收到通知。
								</div>
								<div id="offlineTimeTip" style="width:250px;float:left"></div>
							</td>
						</tr>
						
						<tr>
							<td><span style="color: red;">*</span>推送目标：</td>
							<td>
								<span class="fl">
			                      <div class="select_border"> 
			                        <div class="select_containers "> 
			                        <select name="pushTarget" id="pushTarget" class="select"> 
				                        <option value="">请选择</option> 
				                       	<#list pushTargetList as pushTarget>
				                       		<option value="${pushTarget.code}" <#if pushTarget.code==pushInfo.pushTarget>selected</#if> >${pushTarget.desc}</option>
				                       	</#list>
			                        </select> 
			                        </div> 
			                      </div> 
			                    </span>
							</td>
						</tr>
						<tr class = "clientId" id = "tr_clientId" <#if pushInfo.pushTarget == 1><#else> style="display:none"</#if>>
											<td style="width:116px;"><span style="color: red">*</span>ClientID：</td>
											<td>
												<div style="float:left">
													<input type="text" name="clientId" id="clientId" class="input-text lh25" size="60" value="${pushInfo.clientId}" placeholder="手机clientId" requeried>
												</div>
											</td>
							<tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div style="margin-top:30px;text-align:center;margin-top:10px;">
		<input type="button" value="发送" class="btn btn82 btn_add" id="sendMessageBtn" name="sendMessageBtn" <#if pushInfo.pushWay==1 || pushInfo.pushWay==null><#else> style="display:none"</#if>>
		<input type="button" value="保存" class="btn btn82 btn_add" id="savePushInfoBtn" name="savePushInfoBtn">
	</div>
</div>
</form>
</#if>
</@backend>
