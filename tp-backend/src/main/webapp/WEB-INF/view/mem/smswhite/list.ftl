<#include "/common/common.ftl"/> 
<@backend title="短信白名单" 
 js=['/statics/themes/layout.js',
 	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.js', 
 	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/mem/smswhite.js'
	] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >

<form method="post" action="${domain}/mem/smswhite/list.htm" id="smsWhiteListForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">短信白名单列表</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>白名单开关状态</td>
						<td>
							<input type="button" value="<#if status==0>开启<#else>关闭</#if>" class="ext_btn updatesmsWhiteStatus" status="${status}">
						</td>	
					</tr>
					<tr>
						<td>手机号</td>
						<td><input type="text" id = "mobile" name="mobile" class="input-text lh25" size="15" value="${mobile}"></td>
					</tr>					
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: right;">
					<input type="submit" value="查询" class="btn btn82 btn_search" name="button" click="$('#smsWhiteListForm').submit();">
					<input type="button" value="新增" class="btn btn82 btn_add addSmsWhite" name="button" param="">
					<input type="button" value="限制" class="btn btn82 btn_add limitCondition" name="button" param="">
					<input type="button" value="统计" class="btn btn82 btn_add" name="button" param="" onclick="statistics()">
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
				<th>手机号</th>
				<th>操作</th>
			</tr>
			<#if page.rows??> 
				<#list page.rows as smsmobile>
				<tr>
					<td>${smsmobile}</td>
					<td>
						<input type="button" value="删除" class="ext_btn ext_btn_submit deleteSmsWhite" param="${smsmobile}">					
					</td>
				</tr>
				</#list> 
			</#if>
		</table>
	</div>
</div>
<@pager  pagination=page  formId="smsWhiteListForm"  /> 
</#if>
</form>
	<script> 
			function statistics(){
				var tv={
					url:'/mem/smswhite/smsStatistics.htm',
					text:'短信统计'
				};
				parent.window.showTab(tv);
			}
	</script>
</@backend>
