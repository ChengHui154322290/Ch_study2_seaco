<#if (topicItem)??>
	<li style="display:none;float:left;border:1;"><span id="id">${topicItem.id!}</span></li>
	<li style="width:30px;float:left;border:1;"><span id="sortIndex">${topicItem.sortIndex!}</span></li>
	<li style="width:120px;float:left;border:1;"><span id="supplierName">${topicItem.supplierName!}</span></li>
	<li style="width:120px;float:left;border:1;"><span id="sku">${topicItem.sku!}</span><br/><span id="name">${topicItem.name!}</span></li>
	<li style="width:120px;float:left;border:1;"><span id="itemColor">${topicItem.itemColor!}</span><br/><span id="itemSize">${topicItem.itemSize!}</span></li>
	<!-- 图片可以点击看完整图片 -->
	<li style="width:120px;float:left;border:1;"><img src="${topicItem.topicImage!}"/></li>
	<li style="width:120px;float:left;border:1;"><input type="text" name="topicPrice" value="${topicItem.topicPrice!}"/></li>
	<li style="width:90px;float:left;border:1;"><input type="text" name="limitAmount" value="${topicItem.limitAmount!}"/></li>
	<li style="width:90px;float:left;border:1;"><input type="text" name="limitTotal" value="${topicItem.limitTotal!}"/></li>
	<li style="width:30px;float:left;border:1;"><span id="saledAmount">${topicItem.saledAmount!}</span></li>
	<li style="width:30px;float:left;border:1;">
		<select id="warehouse">
			<option value="wh1" selected>仓库1</option>
			<option value="wh2">仓库2</option>
		</select>
	</li>
	<li style="width:90px;float:left;border:1;"><span id="stockAmount">${topicItem.stockAmount!}</span></li>
	<li style="width:90px;float:left;border:1;"><span id="remark">${topicItem.remark!}</span></li>
	<li style="width:180px;float:left;border:1;">
		<a id="removeItem">移除</a>
	</li>
</#if>