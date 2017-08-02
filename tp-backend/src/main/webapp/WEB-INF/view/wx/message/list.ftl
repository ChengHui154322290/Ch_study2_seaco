<#include "/common/common.ftl"/> 
<@backend title="推送消息管理" 
 js=[ '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/wx/message.js'] 
css=[] >
<form method="post" action="${domain}/wx/message/list.htm" id="messageForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">消息列表</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>名称:</td>
                  		<td><input type="text" name="name" value="${messageInfo.name}"  class="input-text lh25" size="20"></td>
                  		<td></td>
						<td>消息场景</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="code" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list sceneList as scene>
			                       		<option value="${scene.code}" <#if scene.code==messageInfo.code>selected</#if> > ${scene.desc} </option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
                    	</td>
                    	<td>消息类型</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="type" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list typeList as type>
			                       		<option value="${type.code}" <#if type.code==messageInfo.type>selected</#if> > ${type.desc} </option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
                    	</td>
						<td>消息状态</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="status" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list statusList as status>
			                       		<option value="${status.code}" <#if status.code==messageInfo.status>selected</#if> >${status.desc}</option>
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
					<input type="button" value="新增" class="btn btn82 btn_add editMessageBtn" name="button" param="">
				</div>
			</div>
		</div>
	</div>
</div>
<div id="table" class="mt10">
	<div class="box span10 oh">
		<table  id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
			<tr style="text-align:center">
				<th width="10%">名称</th>
				<th width="10%">消息场景</th>
                <th width="10%">消息类型</th>
                <th width="20%">消息内容</th>
                <th width="10%">消息状态</th>
                <th width="10%">创建时间</th>
                <th width="30%">操作</th>
			</tr>
			<#if page.rows?default([])?size !=0>     
				 <#list page.rows as info>
				<tr style="text-align:center">
					<td>${info.name}</td>
					<td>${info.sceneDesc}</td>
					<td>${info.typeDesc}</td>
					<td title="${info.content}">${info.content}</td>
					<td>${info.statusDesc}</td>
					<td>${(info.createTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
					<td>
						<input type="button" value="编辑" class="ext_btn ext_btn_submit editMessageBtn" param="${info.id}">
						<input type="button" value="${(info.status == 1)?string('冻结','发布')}" class="ext_btn ext_btn_submit updatestatusbtn" msgId="${info.id}" msgStatus="${info.status}">
					</td>
				</tr>
				</#list> 
			</#if>
		</table>
	</div>
</div>
<@pager  pagination=page  formId="messageForm"  /> 
</form>
</@backend>
