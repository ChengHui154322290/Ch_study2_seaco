<#include "/common/common.ftl"/> 
<@backend title="短信发送限制" 
			js=['/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/mem/smswhite.js']
			css=[]>
		<div style="margin-top:20px;" />
		<div class="box_top">
				<b class="pl15">短信发送限制</b>
		</div>
		<form id="smsLimitForm" >
			<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15" style="width:100%;margin:0 auto;">
				<tr>
					<td class="td_right" width="50" align="right">*单日总量限制:</div>
					<td>
						<input type="text" class="input-text lh30" id="dailyLimitCount" name="dailyLimitCount" value="${limitInfo.dailyLimitCount}">-1表示不限制
					</td>
				</tr>
				<tr>
					<td class="td_right" width="50" align="right">*单手机号每日总量限制:</div>
					<td>
						<input type="text" class="input-text lh30" id="singleDailyLimitCount" name="singleDailyLimitCount" value="${limitInfo.singleDailyLimitCount}">-1表示不限制
					</td>
				</tr>
				<tr>
					<td align="left">
						<div style="padding-right:10px">
							<input type="button" class="btn btn82 btn_add saveLimitData" id="save" value="保存" />
						</div>
					</td>
					<td align="left">
						<div style="padding-left:10px">
							<input type="button" class="btn btn82 btn_del closeBtn" id="cancel" value="取消" />
						</div>
					</td>
				</tr>
			</table>
		</form>
</@backend>