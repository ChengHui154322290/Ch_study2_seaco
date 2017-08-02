<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ">
	<tbody>	
		<tr>
			<th>选择</th>
			<th>编号</th>
			<th>名称</th>
		</tr>
		<#if (topics??)>
			<#list topics as topic>
				<tr align="center" class="tr" style="background-color: rgb(255, 255, 255);">
					<td><input type="radio" name="relateTopicId" value="${topic.id!}" /></td>
					<td>${topic.number!}</td>
					<td>${topic.name!}</td>
				</tr>
			</#list>
		</#if>
	</tbody>
</table>
<div style="width:100%;height:30px;">
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" id="nextPage">下一页</a>
	</div>
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" id="prePage">上一页</a>
	</div>
	<div style="float:right;padding-right:10px;">
		总：<span>${topicCount!}</span>
		每页：
		<#if ("20" == "${pageSize!}")>
			<select id="pageSize">
				<option value="10">10</option>
				<option value="20" selected>20</option>
			</select>
		<#else>
			<select id="pageSize">
				<option value="10" selected>10</option>
				<option value="20">20</option>
			</select>
		</#if>
		<span id="currPage">${currPage!}</span>/<span id="totalPage">${totalPage!}</span>页
	</div>
</div>