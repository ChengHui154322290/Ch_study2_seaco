<!-- 商品信息 -->
<!-- 关联活动 --> 
<table id="topicRelation" class="input tabContent">
	<tr>
		<td class="td_left">
			<h4>关联专场</h4>
		</td>
		<td colspan="5">
			<#if ("view" != "${mode}")>
				<input type="button" class="ext_btn ext_btn_submit" id="addRelation" value="新增" />
			</#if>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicRelationsList">
				<tr>
					<th style="display:none;"></th>
					<th>序号</td>
					<th>编号</th>
					<th>名称</th>
					<#if ("view" != "${mode}")>
						<th>操作</th>
					</#if>
				</tr>
				<#if (topicDetail.relateList??)>
					<#list topicDetail.relateList as relate>
						<tr align="center" style="background-color: rgb(255, 255, 255);">
							<td style="display:none;"><span>${relate.id!}</span></td>
							<td><span>${relate.secondTopicId!}</span></td>
							<td><span>${relate.secondTopicNumber!}</span></td>
							<td><span>${relate.secondTopicName!}</span></td>
							<#if ("view" != "${mode}")>
								<td>
									<input type="button" class="btn btn82 btn_del closebtn" name="removeRelation" value="移除" />
								</td>
							</#if>
						</tr>
					</#list>
				</#if>
			</table>
		</td>
	</tr>
</table>