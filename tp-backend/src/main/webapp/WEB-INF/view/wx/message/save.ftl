<#include "/common/common.ftl"/> 
<@backend title="编辑推送消息" js=[ 
	'/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/js/validation/jquery.validate.min.js',
	'/statics/backend/js/wx/message.js'] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
<#if resultInfo.msg??>${resultInfo.msg.message}<#else>
<#assign msg = resultInfo.data>
<form method="post" action="${domain}/wx/message/save.htm" id="saveMsgForm">
<input type="hidden" name="id" value="${msg.id}"/>
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<div class="box_top">
					<b class="pl15">微信回复消息</b>
				</div>
				<div class="box_center pt10 pb10">
					<table class="form_table" border="0" cellpadding="0" cellspacing="0">
						<tr><td style="width:120px;"></td><td></td><tr>
						<tr>
							<td><span style="color: red;">*</span>名称：</td>
							<td>
											<div style="float:left">
												<input type="text" name="name" id="name" class="input-text lh25" size="45" maxlength = "25" value="${msg.name}" placeholder="" requeried>
											</div>
											<div id="titleTip" style="width:250px;float:left"></div>
										</td>
						</tr>
						<tr>
							<td><span style="color: red;">*</span>消息场景：</td>
							<td>
								<span class="fl">
			                      <div class="select_border"> 
			                        <div class="select_containers "> 
				                       <select name="code" id="sceneSelect" class="select"> 
				                        <option value="">请选择</option> 
				                       	<#list sceneList as scene>
				                       		<option value="${scene.code}" <#if scene.code==msg.code>selected</#if> > ${scene.desc} </option>
				                       	</#list>
			                        </select> 
			                        </div> 
			                      </div> 
			                    </span>
							</td>
						</tr>
						<tr id = "tr_key" <#if msg.code == 'click'><#else> style="display:none"</#if>>
							<td><span style="color: red;">*</span>菜单：</td>
							<td>
								<span class="fl">
			                      <div class="select_border"> 
			                        <div class="select_containers "> 
				                       <select name="cKey" id="cKey" class="select"> 
				                        <option value="">请选择</option> 
				                       	<#list menuList as menu>
				                       		<option value="${menu.key}" <#if menu.key==msg.cKey>selected</#if> > ${menu.value} </option>
				                       	</#list>
			                        </select> 
			                        </div> 
			                      </div> 
			                    </span>
							</td>
						<tr>
						<tr>
							<td><span style="color: red;">*</span>消息类型</td>
							<td>
								<span class="fl">
			                      <div class="select_border"> 
			                        <div class="select_containers "> 
			                        <select name="type" id="typeSelect" class="select" > 
				                         <option value="">请选择</option> 
				                       	 <#list typeList as type>
				                       		<option value="${type.code}" <#if type.code==msg.type>selected</#if> > ${type.desc} </option>
				                       	 </#list>
			                        </select> 
			                        </div> 
			                      </div> 
			                    </span>
                    		</td>
						</tr>
						<tr>
							<td>内容：</td>
							<td>
								<textarea rows="10" cols="80" name = "content" style="overflow:hidden">${msg.content}</textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div style="margin-top:30px;text-align:center;margin-top:10px;">
		<input type="button" value="保存" class="btn btn82 btn_add" id="saveMsgBtn" name="saveMsgBtn">
	</div>
</div>
</form>
</#if>
</@backend>
