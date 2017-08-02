<#include "/common/common.ftl"/> 
<@backend title="推送消息管理" 
 js=[ '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/app/versioninfo.js',
	'/statics/supplier/component/date/WdatePicker.js'] 
css=[] >
<form method="post" action="${domain}/app/version/list.htm" id="versioninfoListForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">版本消息列表</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>状态</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="status" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list statusList as status>
			                       		<option value="${status.code}" <#if status.code==versionInfo.status>selected</#if> >${status.desc}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
						</td>
						<td>平台</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="platform" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list platformList as platform>
			                       		<option value="${platform.code}" <#if platform.code==versionInfo.platform>selected</#if> >${platform.desc}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
						</td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: right;">
					<input type="submit" value="查询" class="btn btn82 btn_search querypagebtn" name="button">
					<input type="button" value="新增" class="btn btn82 btn_add editVersionInfoBtn" name="button" param="">
				</div>
			</div>
		</div>
	</div>
</div>
<div id="table" class="mt10">
	<div class="box span10 oh">
		<table  id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
			<tr style="text-align:center">
				<th width="10%">版本号</th>
				<th width="10%">版本平台</th>
                <th width="5%">是否最新</th>
                <th width="25%" >更新简介</th>
                <th width="10%">发布时间</th>
                <th width="10%">下载地址</th>
                <th width="10%">版本状态</th>
                <th width="10%">创建时间</th>
                <th width="15%">操作</th>
			</tr>
			<#if page.rows?default([])?size !=0>     
				 <#list page.rows as versionInfo>
				<tr style="text-align:center">
					<td>${versionInfo.version}</td>
					<td>${versionInfo.platformDesc}</td>
					<td>${(versionInfo.isNew == 1)?string('是','否')}</td>
					<td title="${versionInfo.remark}">${versionInfo.remark}</td>
					<td>${(versionInfo.pushTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
					<td title="${versionInfo.downUrl}">${versionInfo.downUrl}</td>
					<td>${versionInfo.statusDesc}</td>
					<td>${(versionInfo.createTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
					<td>
						<input type="button" value="编辑" class="ext_btn ext_btn_submit editVersionInfoBtn" param="${versionInfo.id}">
						<input type="button" value="${(versionInfo.status == 1)?string('冻结','发布')}" class="ext_btn ext_btn_submit updatestatusbtn" versionId="${versionInfo.id}" versionStatus="${versionInfo.status}">
					</td>
				</tr>
				</#list> 
			</#if>
		</table>
	</div>
</div>
<@pager  pagination=page  formId="versioninfoListForm"  /> 
</form>
</@backend>
