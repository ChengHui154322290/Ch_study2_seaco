<#include "/layout/inner_layout.ftl" />
<@sellContent title="工单管理" 
	js=[
		'/static/workorder/webUploader/js/webuploader.js',
		'/static/workorder/workorderUpload/refresh-upload.js',
		'/static/workorder/workorder.js'
	  ] 
	css=['/static/workorder/webUploader/css/webuploader.css',
	'/static/workorder/webUploader/css/style.css',
	'/static/workorder/webUploader/css/demo.css'
	] >
	
	<div class="mt10" id="forms">
        <div class="box">
          <div class="box_border">
            <#if errorMessage??><div class="box_top"><b class="pl15">${errorMessage}</b></div></#if>
			<form id="showLogForm" name="showLogForm" method="post"> 
			<#include "/seller/workorder/showinsertinfo.ftl"/>
				<table>
					<tbody>
						<#include "/seller/workorder/insertlog.ftl"/>
					</tbody>
					<tbody>
						<tr>
							<td>
							<input type="button" value="回复" onclick="replyWorkOrder(${workorderInfo.id},${workorderInfo.workorderNo})" class="ext_btn m10">
							<!--<input id="closeWorkorder" type="button" value="关闭" class="ext_btn m10">
							<input type="button" value="正常关闭" onclick="closeWorkorder('3')" class="ext_btn m10">
							<input type="button" value="强制关闭" onclick="closeWorkorder('4')" class="ext_btn m10">
							<input type="button" value="转移租" onclick="updateWorkorder(${workorderInfo.id})" class="ext_btn m10">
								<select class="select" name="groupId" id="groupId">
										<option value="0">请选择</option>
										<#list groupList as group>
										<option value="${group.code}">${group.cnName}</option>
										</#list>
								</select>
							<input type="button" value="申请补偿" onclick="location.href='list.htm'" class="ext_btn m10">-->
							</td>
						</tr>
					</tbody>
				</table>
			</form>
            <#include "/seller/workorder/log.ftl"/>
          </div>
        </div>
     </div>
</@sellContent>