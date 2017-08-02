<#include "/common/common.ftl"/> 
<@backend title="推送消息管理" 
 js=[ '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/app/pushinfo.js',
	'/statics/supplier/component/date/WdatePicker.js'] 
css=[] >
<form method="post" action="${domain}/app/push/list.htm" id="pushinfoListForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">推送消息列表</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="td_right" style="width:10%;"  align="left">推送日期：</td>
                        <td align="left" style="width:40%;" class="">
                                    <input type="text" name="startTime" class="input-text lh30 Wdate"
                               value='<#if pushInfo.startTime??>${pushInfo.startTime?string("yyyy-MM-dd")}</#if>'
                               onClick="WdatePicker({isShowClear:true,readOnly:true})">&nbsp;~&nbsp;
                               <input type="text" name="endTime" class="input-text lh30 Wdate"
                               value='<#if pushInfo.endTime??>${pushInfo.endTime?string("yyyy-MM-dd")}</#if>'
                               onClick="WdatePicker({isShowClear:true,readOnly:true})">
                        </td>
						<td>推送平台</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="platform" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list platformList as platform>
			                       		<option value="${platform.code}" <#if platform.code==pushInfo.platform>selected</#if> > ${platform.desc} </option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
                    	</td>
						<td>推送类型</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="pushType" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list pushTypeList as pushType>
			                       		<option value="${pushType.code}" <#if pushType.code==pushInfo.pushType>selected</#if> >${pushType.desc}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
						</td>
						<td>推送状态</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="pushStatus" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list pushStatusList as pushStatus>
			                       		<option value="${pushStatus.code}" <#if pushStatus.code==pushInfo.pushStatus>selected</#if> >${pushStatus.desc}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
						</td>
						<td>推送目标</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="pushTarget" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list pushTargetList as pushTarget>
			                       		<option value="${pushTarget.code}" <#if pushTarget.code==pushInfo.pushTarget>selected</#if> >${pushTarget.desc}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
						</td>
						<td>推送方式</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="pushWay" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list pushWayList as pushWay>
			                       		<option value="${pushWay.code}" <#if pushWay.code==pushInfo.pushWay>selected</#if> >${pushWay.desc}</option>
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
					<input type="button" value="新增" class="btn btn82 btn_add editPushInfoBtn" name="button" param="">
				</div>
			</div>
		</div>
	</div>
</div>
<div id="table" class="mt10">
	<div class="box span10 oh">
		<table  id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
			<tr style="text-align:center">
				<th width="12%">推送标题</th>
				<th width="5%">推送平台</th>
                <th width="5%">推送状态</th>
                <th width="5%">推送目标</th>
                <th width="5%">推送方式</th>
                <th width="5%">推送类型</th>
                <th width="10%">推送时间</th>
                <th width="20%">超链</th>
                <th width="5%">专场活动ID</th>
                <th width="8%">商品SKU</th>
                <th width="8%">操作</th>
			</tr>
			<#if page.rows?default([])?size !=0>     
				 <#list page.rows as pushInfo>
				<tr style="text-align:center">
					<td>${pushInfo.title}</td>
					<td>${pushInfo.platform}</td>
					<td>${pushInfo.pushStatusDesc}</td>
					<td>${pushInfo.pushTargetDesc}</td>
					<td>${pushInfo.pushWayDesc}</td>
					<td>${pushInfo.pushTypeDesc}</td>
					<td>${(pushInfo.sendDate?string("yyyy-MM-dd HH:mm:ss"))!}</td>
					<td>${pushInfo.link}</td>
					<td>${pushInfo.tid}</td>
					<td>${pushInfo.sku}</td>
					<td>
						<input type="button" value="编辑" class="ext_btn ext_btn_submit editPushInfoBtn" param="${pushInfo.id}">
						<#if pushInfo.pushStatus == 1 && pushInfo.pushWay == 1> 
							<input type="button" value="重新发送" class="ext_btn ext_btn_submit reSendMsgBtn" param="${pushInfo.id}">
						</#if>
					</td>
				</tr>
				</#list> 
			</#if>
		</table>
	</div>
</div>
<@pager  pagination=page  formId="pushinfoListForm"  /> 
</form>
</@backend>
