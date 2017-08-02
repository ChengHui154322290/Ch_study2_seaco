<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="达人审核管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/base/masterUserApprove.js'] 
    css=['/statics/backend/css/style.css']>
    
	<form id="masterUserApprove" method="post">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">审核详情</b>
			</div>
			<div class="box_center">
				<table id="topicTable" class="input tabContent">
					<tr>
						<td></td>
						<td>
							<input type="hidden" id="status" name="cmmExamineStatus" value="${masterUser.cmmExamineStatus}" />
							<input type="hidden" id="id" name="id" value="${masterUser.id}" />
							<input type="hidden" id="cmmActivityId" name="cmmActivityId" value="${masterUser.cmmActivityId}" />
						</td>
					</tr>
					<tr>
						<td>手机号：</td>
						<td>${masterUser.cmmMobile}</td>
					</tr>
					<tr>
						<td>用户名：</td>
						<td>${masterUser.cmmUserName}</td>
					</tr>
					<tr>
						<td>申请宣言：</td>
						<td><textarea readonly="true">${masterUser.cmmApplyDeclaration}</textarea></td>
					</tr>
					<tr>
						<td>申请时间：</td>
						<td>${masterUser.cmmApplyDate?string("yyyy-MM-dd HH:mm:ss")}</td>
					</tr>
					<#if ('view' != view)>
						<tr>
							<td>审核意见：</td>
							<td><textarea style="height:100px" name="cmmExamineSuggest">${masterUser.cmmExamineSuggest}</textarea></td>
						</tr>
						<tr>
							<td></td>
							<td>
								<input type="button" class="ext_btn ext_btn_submit" width="120px" id="approve" value="审批通过" />
								<input type="button" class="ext_btn ext_btn_submit" width="120px" id="refuse" value="审批不通过" />
								<input type="button" class="ext_btn ext_btn_submit" width="120px" id="cancel" value="返回" />
							</td>
						</tr>
					<#else>
						<tr>
							<td>审核状态：</td>
							<td>
								<#if (2 == masterUser.cmmExamineStatus)>
									审核通过
								<#elseif (3 == masterUser.cmmExamineStatus)>
									审核不通过
								<#else></#if>
							</td>
						</tr>
						<tr>
							<td>审核意见：</td>
							<td><textarea style="height:100px" readonly="true">${masterUser.cmmExamineSuggest}</textarea></td>
						</tr>
					</#if>
				</table>
			</div>
		</div>
	</form>
</@backend>