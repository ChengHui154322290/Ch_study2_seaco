<#include "/common/common.ftl"/>
<@backend title="工单信息" 
	js=[
		'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
		'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
		'/statics/backend/js/textinput_common.js',
		'/statics/backend/js/layer/layer.min.js',
		'/statics/backend/js/customerservice/workorder.js',
		'/statics/backend/js/customerservice/reject.js'
	  ] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
	<div class="mt10" id="forms">
        <div class="box">
          <div class="box_border">
            <#if errorMessage??><div class="box_top"><b class="pl15">${errorMessage}</b></div></#if>
			<form action="" id="showLogForm" name="showLogForm" method="post"> 
			<#include "/customerservice/workorder/showinsertinfo.ftl"/>
				<table>
					<tbody>
						<#include "/customerservice/workorder/insertlog.ftl"/>
					</tbody>
					<tbody>
						<tr>
							<td colspan="2">
							<input type="button" value="回复" onclick="itemFormSubmit('insertLog')" class="ext_btn m10">
							<!--<input id="closeWorkorder" type="button" value="关闭" class="ext_btn m10">-->
							<input type="button" value="正常关闭" onclick="closeWorkorder('3')" class="ext_btn m10">
							<input type="button" value="强制关闭" onclick="closeWorkorder('4')" class="ext_btn m10">
							<input type="button" value="转移租" onclick="updateWorkorder(${workorderInfo.id})" class="ext_btn m10">
								<select class="select" name="groupId" id="groupId">
										<option value="0">请选择</option>
										<#list groupList as group>
										<option value="${group.code}">${group.cnName}</option>
										</#list>
								</select>
							<input type="button" value="申请补偿" onclick="location.href='/customerservice/offset/audit/apply.htm'" class="ext_btn m10">
							</td>
						</tr>
					</tbody>
				</table>
			</form>
            <#include "/customerservice/workorder/log.ftl"/>
          </div>
        </div>
     </div>
</@backend>