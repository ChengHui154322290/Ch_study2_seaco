<#include "/common/common.ftl"/> 
<@backend title="推送消息管理" 
 js=[ '/statics/backend/js/layer/layer.min.js',
	'/statics/supplier/component/date/WdatePicker.js'] 
css=[] >
<form method="post" action="${domain}/mkt/channel/statistic.htm" id="statisticForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">渠道推广统计</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>渠道:</td>
                  		<td><input type="text" name="channel" value="${channelPromote.channel}"  class="input-text lh25" size="20"></td>
                  		<td></td>
                  		<td></td>
						<td>类型</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="type" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list typeList as type>
			                       		<option value="${type.code}" <#if type.code==channelPromote.type>selected</#if> >${type.desc}</option>
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
				</div>
			</div>
		</div>
	</div>
</div>
<div id="table" class="mt10">
	<div class="box span10 oh">
		<table  id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
			<tr style="text-align:center">
				<th width="10%">类型</th>
				<th width="15%">渠道</th>
				<th width="15%">已关注人数</th>
				<th width="15%">未关注人数</th>
                <th width="15%">来源</th>
			</tr>
			<#if page.rows?default([])?size !=0>     
				 <#list page.rows as channelPromote>
				<tr style="text-align:center">
					<td>${channelPromote.typeDesc}</td>
					<td>${channelPromote.channel}</td>
					<td>${channelPromote.followCount}</td>
					<td>${channelPromote.unFollowCount}</td>
					<td>${channelPromote.sourceDesc}</td>
				</tr>
				</#list> 
			</#if>
		</table>
	</div>
</div>
<@pager  pagination=page  formId="statisticForm"  /> 
</form>
</@backend>
