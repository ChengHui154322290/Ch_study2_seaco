<#include "/common/common.ftl"/> 
<@backend title="短信白名单" 
 js=['/statics/themes/layout.js',
 	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.js', 
 	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/mem/smswhite.js'
	] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >

<form method="post" action="${domain}/mem/smswhite/smsStatistics.htm" id="smsStatisticsForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">短信发送统计</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="td_right" width="50" align="right">日期：</div>
						<td>
							<input type="text" class="input-text lh30" id="statisticsTime" name="statisticsTime" value=<#if statisticsTime??>"${statisticsTime?string("yyyy-mm-dd")}"<#else>""</#if> onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})">
						</td>	
					</tr>					
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: right;">
					<input type="submit" class="btn btn82 btn_search" id="search" click="$('#smsStatisticsForm').submit();" value="查询" />
				</div>
			</div>
		</div>
	</div>
</div>
<#if resultInfo.msg??>${resultInfo.msg.message}<#else>
<#assign page = resultInfo.data>
<div id="table" class="mt10">
	<div class="box span10 oh">
		<table  id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
			<tr>
				<th>日期</th>
				<th>短信数</th>
			</tr>
			<#if page.rows??> 
				<#list page.rows as dto>
					<tr>
						<td>${dto.dayTime}</td>
						<td>${dto.dailyAmount}</td>
					</tr>
				</#list> 
			</#if>
		</table>
	</div>
</div>
<@pager  pagination=page  formId="smsStatisticsForm"  /> 
</#if>
</form>
</@backend>
