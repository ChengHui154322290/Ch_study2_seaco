<#include "/common/common.ftl"/> 
<#include "/common/domain.ftl"/> 
<div style="width:100%;height:30px;">
	<div style="float:right;padding-right:10px;">
		每页：
		<#if ("50" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50" selected>50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
		<#elseif ("100" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50">50</option>
				<option value="100" selected>100</option>
				<option value="200">200</option>
			</select>
		<#elseif ("200" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200" selected>200</option>
			</select>
		<#else>
			<select name="perCount" class="select">
				<option value="30" selected>30</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
		</#if>
		<span id="currPage">${currPage!}</span>/<span id="totalPage">${totalPage!}</span>页
		记录总数：<span>${topicCount!}</span>
	</div>
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" name="nextPage">下一页</a>
	</div>
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" name="prePage">上一页</a>
	</div>
</div>

<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicList">
	<tbody>
	    <tr align="center">
			<th>变更单序号</th>
			<th>专场序号</th>
			<th>专场类型</th>
			<th>专场编号</th>
			<th>专场名称</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>变更单状态</th>
			<th>专场进度</th>
			<th>操作</th>
		</tr>
		<#if (topicChanges)??>
			<#list topicChanges as topicChange>
				 <tr align="center" class="tr" style="background-color: rgb(255, 255, 255);">
					<td>${topicChange.id!}</td>
					<td>${topicChange.changeTopicId!}</td>
					<td>
						<#if (1 == topicChange.type)>
							单品团
						<#elseif (2 == topicChange.type)>
							品牌团
						<#elseif (3 == topicChange.type)>
							主题团
						<#elseif (5 == topicChange.type)>
							商家店铺
						</#if>
					</td>
					<td>
						${topicChange.number!}
					</td>
					<td><a style="padding-right:5px;" name="viewTopic" topicChangeId="${topicChange.id}" href="javascript:void(0);">${topicChange.name!}</a></td>
					<td><#if (topicChange.startTime??)>${topicChange.startTime?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
					<td><#if (topicChange.endTime??)>${topicChange.endTime?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
					<td>
						<#if (1 == topicChange.status)>
							审核中
						<#elseif (2 == topicChange.status)>
							已取消
						<#elseif (3 == topicChange.status)>
							审核通过
						<#elseif (4 == topicChange.status)>
							已驳回
						<#elseif (5 == topicChange.status)>
							终止
						<#else>
							编辑中
						</#if>
					</td>
					<td>
						<#if (1 == topicChange.progress)>
							进行中
						<#elseif (2 == topicChange.progress)>
							已结束
						<#else>
							未开始
						</#if>
					</td>
					<td>
						<#if (topicChange.status == 0 || topicChange.status == 4)>
							<a style="padding-right:5px;" name="editTopic" topicChangeId="${topicChange.id}" href="javascript:void(0);">[编辑]</a>
						</#if>
						<#if (topicChange.status == 0 || topicChange.status == 4)>
							<a style="padding-right:5px;" name="cancelTopicChange" topicChangeId="${topicChange.id}" href="javascript:void(0);">[取消]</a>
						</#if>
						<#if (topicChange.status == 1)>
							<a style="padding-right:5px;" name="approveTopicChange" topicChangeId="${topicChange.id}" href="javascript:void(0);">[批准]</a>
						</#if>
						<a style="padding-right:5px;" name="viewTopic" topicChangeId="${topicChange.id}" href="javascript:void(0);">[详细]</a>
						<#if (topicChange.status == 1)>
							<a style="padding-right:5px;" name="refuseTopicChange" topicChangeId="${topicChange.id}" href="javascript:void(0);">[驳回]</a>
						</#if>
					</td>
				</tr>
			</#list>
		</#if>
	</tbody>
</table>

<div style="margin-top:10px;width:100%;height:30px;">
	<div style="float:right;padding-right:10px;">
		每页：
		<#if ("50" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50" selected>50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
		<#elseif ("100" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50">50</option>
				<option value="100" selected>100</option>
				<option value="200">200</option>
			</select>
		<#elseif ("200" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200" selected>200</option>
			</select>
		<#else>
			<select name="perCount" class="select">
				<option value="30" selected>30</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
		</#if>
		第<span id="currPage">${currPage!}</span>/<span id="totalPage">${totalPage!}</span>页
		记录总数：<span>${topicCount!}</span>
	</div>
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" name="nextPage">下一页</a>
	</div>
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" name="prePage">上一页</a>
	</div>
</div>