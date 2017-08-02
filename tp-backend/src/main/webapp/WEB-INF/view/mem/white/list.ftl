<#include "/common/common.ftl"/> 
<@backend title="推送消息管理" 
 js=[ '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/mem/whiteinfo.js',
	'/statics/supplier/component/date/WdatePicker.js'] 
css=[] >
<form method="post" action="${domain}/mem/white/list.htm" id="whiteinfoListForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">白名单列表</b>
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
			                       		<option value="${status.code}" <#if status.code==whiteInfo.status>selected</#if> >${status.desc}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
						</td>
						<td>级别</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="level" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list levelList as level>
			                       		<option value="${level.code}" <#if level.code==whiteInfo.level>selected</#if> >${level.desc}</option>
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
					<input type="button" value="新增" class="btn btn82 btn_add editWhiteInfoBtn" name="button" param="">
				</div>
			</div>
		</div>
	</div>
</div>
<div id="table" class="mt10">
	<div class="box span10 oh">
		<table  id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
			<tr style="text-align:center">
				<th width="10%">手机号</th>
				<th width="5%">级别</th>
                <th width="10%">真实姓名</th>
                <th width="15%">收货地址</th>
                <th width="15%">证件号</th>
                <th width="10%">备注</th>
                <th width="10%">创建时间</th>
                <th width="10%">状态</th>
                <th width="10%">操作</th>
			</tr>
			<#if page.rows?default([])?size !=0>     
				 <#list page.rows as whiteInfo>
				<tr style="text-align:center">
					<td>${whiteInfo.mobile}</td>
					<td>${whiteInfo.levelDesc}</td>
					<td>${whiteInfo.trueName}</td>
					<td title="${whiteInfo.consigneeAddress}">${whiteInfo.consigneeAddress}</td>
					<td>${whiteInfo.certificateValue}</td>
					<td title="${whiteInfo.remark}">${whiteInfo.remark}</td>
					<td>${(whiteInfo.createTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
					<td>${whiteInfo.statusDesc}</td>
					<td>
						<input type="button" value="编辑" class="ext_btn ext_btn_submit editWhiteInfoBtn" param="${whiteInfo.id}">
						<input type="button" value="${(whiteInfo.status == 1)?string('冻结','解冻')}" class="ext_btn ext_btn_submit updatestatusbtn" whiteId="${whiteInfo.id}" whiteStatus="${whiteInfo.status}">
					</td>
				</tr>
				</#list> 
			</#if>
		</table>
	</div>
</div>
<@pager  pagination=page  formId="whiteinfoListForm"  /> 
</form>
</@backend>
