<#include "/common/common.ftl"/> 
<@backend title="新增工单" js=[
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/tab.js',
'/statics/backend/js/textinput_common.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/customerservice/workorder.js'
]
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css']
>

<div class="mt10" id="forms">
	<div class="box">
		<div class="box_border">
			<div class="box_center">
				<form class="workorderinfoinsertform" id="workorderinfoinsertform">
					<table width="100%" cellspacing="0" cellpadding="0" border="0"
						class="form_table pt15 pb15">
						<tbody>
							<tr>
								<td class="td_right">订单编号：</td>
								<td><input type="text" value=""
									name="orderCode" id="orderCode" class="input-text lh30" size="20" maxlength="20"
									onmouseover="this.title=this.value" title=""> <input
									type="button" onclick="view($('#orderCode').val())" value="查询" class="ext_btn ext_btn_submit m10" /></td>
							</tr>
							<tr>
								<td class="td_right">优先级：</td>
								<td><select class="select" name="priorityLevel">
										<#list priorityLevelList as priorityLevel>
										<option value="${priorityLevel.code}">${priorityLevel.cnName}</option>
										</#list>
								</select></td>
							</tr>
							<tr>
								<td class="td_right">业务类型：</td>
								<td><select class="select" name="bizType" id="bizType" onchange="selectReason(this.value)"> 
								<option value="0">请选择业务类型</option>
										<#list bizTypeList as bizType>
										<option value="${bizType.code}">${bizType.cnName}</option>
										</#list>
								</select></td>
							</tr>
							<tr>
								<td class="td_right">原因：</td>
								<td><select class="select" id="reason" name="reason"> 
								 <#list reasonList as reason>
										<option value="${reason.cnName}">${reason.cnName}</option>
								 </#list>
								</select></td>
							</tr>
							<tr>
								<td class="td_right">组别：</td>
								<td><select class="select" name="groupId">
										<#list groupList as group>
										<option value="${group.code}">${group.cnName}</option>
										</#list>
								</select></td>
							</tr>
							<tr>
								<td class="td_right">商家处理方式：</td>
								<td><select class="select" name="supplierMethodId">
										<#list disposeList as dispose>
										<option value="${dispose.code}">${dispose.cnName}</option>
										</#list>
								</select></td>
							</tr>
							
								 <#include "/customerservice/workorder/insertlog.ftl"> 
								 
							<tr>
								<td style="text-align: right;"><input type="button" id="datasubmit"
									style="text-align: right;" value="保存" onclick="workorderSaveSubmit()"
									class="ext_btn ext_btn_submit m10"></td>
								<td><input type="button" value="返回"
									onclick="location.href='list.htm'" class="ext_btn m10"></td>
							<tr>
						</tbody>

					</table>
				</form>
			</div>

		</div>
	</div>
</div>
</@backend>
