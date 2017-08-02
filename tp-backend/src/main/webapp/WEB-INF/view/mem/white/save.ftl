<#include "/common/common.ftl"/> 
<@backend title="编辑推送消息" js=[ 
	'/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/supplier/component/date/WdatePicker.js',
	'/statics/js/validation/jquery.validate.min.js',
	'/statics/backend/js/mem/whiteinfo.js'] 
	css=['/statics/backend/css/apppush.css','/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
<#if resultInfo.msg??>${resultInfo.msg.message}<#else>
<#assign whiteInfo = resultInfo.data>
<form method="post" action="${domain}/mem/white/save.htm" id="saveWhiteInfoForm">
<input type="hidden" name="id" value="${whiteInfo.id}"/>
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_center pt10 pb10">
				<div class="box_top">
					<b class="pl15">白名单管理</b>
				</div>
				<div class="box_center pt10 pb10">
					<table class="form_table" border="0" cellpadding="0" cellspacing="0">
						<tr><td style="width:120px;"></td><td></td><tr>
						<tr>
							<td><span style="color: red;">*</span>手机号：</td>
							<td>
											<div style="float:left">
												<input type="text" name="mobile" id="mobile" class="input-text lh25" size="45" maxlength = "25" value="${whiteInfo.mobile}" placeholder="手机号" <#if whiteInfo.mobile!=null>disabled</#if>>
											</div>
											<div id="titleTip" style="width:250px;float:left"></div>
											<input type="hidden" name="oldMobile" id="oldMobile" />
										</td>
						</tr>
						<tr>
							<td><span style="color: red;">*</span>级别：</td>
							<td>
								<span class="fl">
			                      <div class="select_border"> 
			                        <div class="select_containers "> 
			                        <select name="level" id="level" class="select" > 
				                        <option value="">请选择</option> 
				                       	<#list levelList as level>
				                       		<option value="${level.code}" <#if level.code==whiteInfo.level>selected</#if> > ${level.desc} </option>
				                       	</#list>
			                        </select> 
			                        </div> 
			                      </div> 
			                    </span>
                    		</td>
						</tr>
						<tr>
							<td>收货地址：</td>
							<td>
								<span class="fl">
			                      <div class="select_border"> 
			                        <div class="select_containers "> 
			                        <select name="addressId" id="consigneeAddressSelect" class="select" > 
			                            <#if whiteInfo.id != null>
			                                <#list addressList as address>
			                       				<option value="${address.id}" <#if address.id==whiteInfo.addressId>selected</#if> >${address.value}</option>
			                       	        </#list>
			                            </#if>
			                            <#if whiteInfo.id == null>
				                        		<option value="0">------------------------查询------------------------</option> 
				                        </#if>
			                        </select> 
			                        </div> 
			                      </div> 
			                    </span>
			                    <input type="hidden" name="consigneeAddress" id="consigneeAddress" value="${whiteInfo.consigneeAddress}"/>
                    		</td>
						</tr>
						<tr>
							<td>备注：</td>
							<td>
								<textarea rows="10" cols="80" name = "remark" style="overflow:hidden">${whiteInfo.remark}</textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div style="margin-top:30px;text-align:center;margin-top:10px;">
		<input type="button" value="保存" class="btn btn82 btn_add" id="saveWhiteInfoBtn" name="saveWhiteInfoBtn">
	</div>
</div>
</form>
</#if>
</@backend>
