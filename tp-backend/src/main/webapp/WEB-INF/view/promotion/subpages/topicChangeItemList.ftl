<!-- 商品信息 -->
<!-- 添加商品块 -->
<table id="topicItemDetails" class="input tabContent">
	<tr>
		<#if ("view" != "${mode}")>
			<td class="td_right">添加商品</td>
			<td colspan="5">
				<input type="button" class="ext_btn ext_btn_submit" value="新增商品" width="180" id="addTopicItem" /> 
					<input type="button" class="ext_btn ext_btn_submit" value="粘贴输入" width="180" id="pasteTopicItem"/>
			</td>
		<#else>
			<td class="td_right">商品信息:</td>
			<td colspan="5">
				<#if ("view" == "${mode}" && !(order)??)>
					<input type="button" class="ext_btn ext_btn_submit" id="lockAllItem" value="锁定全部商品" />
				</#if>
			</td>
		</#if>
	</tr>
	<tr>
		<td colspan="6">
			<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicItemsList">
				<tr align="center">
					<th style="display:none"></th>
					<th style="width:60px">排序</th>
					<th>供应商</th>
					<th style="width:160px">商品信息</th>
					<th>规格参数</th>
					<th>图片</th>
					<th style="width:80px">销售价</th>
					<th style="width:80px">限购数量</th>
					<th style="width:80px">限购总量</th>
					<th>仓库名称</th>
					<!--
					<th style="width:80px" title="可销售库存=总库存-预留库存-活动占用库存">可销售库存</th>
					-->
					<th>专场剩余库存</th>
					<th>标签</th>
					<th>购买方式</th>
					<th>备注</th>
					<#if ("view" != "${mode}" || ("" == "${order}" && topicDetail.topic.status == 3))>
						<th>操作</th>
					</#if>
				</tr>
				<#if ("view" != "${mode}")>
					<tr align="center" style="background-color: rgb(255, 255, 255);">
						<td colspan="5"></td>
						<td>
							<input type="text" class="input-text topicNumber" style="width:55px;" id="updPrice" />
							<input type="button" id="updPriceBt" value="T" width="30" />									
						</td>
						<td>
							<input type="text" class="input-text topicInteger" style="width:55px;" id="updAmount" />
							<input type="button" id="updAmountBt" value="T" width="30" />									
						</td>
						<td>
							<div id="updTotalAmountDiv">
								<input type="text" class="input-text topicInteger" style="width:55px;" id="updTotalAmount" />
								<input type="button" id="updTotalAmountBt" value="T" width="30" />
							</div>
						</td>
						<#if ("view" != "${mode}" || ("" == "${order}" && topicDetail.topic.status == 3))>
							<td colspan="5"></td>
						<#else>
							<td colspan="4"></td>
						</#if>
					</tr>
				</#if>
				<#if (topicDetail.topicItemDtoList??)>
					<#list topicDetail.topicItemDtoList as topicItem>
						<tr align="center" style="background-color: rgb(255, 255, 255);" class="topicItemData">
							<td style="display:none;">
								<input type="hidden" name="topicItemId" value="${topicItem.id!}" />
								<input type="hidden" name="topicItemItemId" value="${topicItem.itemId!}" />
								<input type="hidden" name="topicItemBarcode" value="${topicItem.barCode!}" />
								<input type="hidden" name="topicItemSku" value="${topicItem.sku!}" />
								<input type="hidden" name="topicItemSpu" value="${topicItem.spu!}" />
								<input type="hidden" name="topicItemName" value="${topicItem.name!}" />
								<input type="hidden" name="topicItemSupplierId" value="${topicItem.supplierId!}" />
								<input type="hidden" name="topicItemLocationId" value="${topicItem.stockLocationId!}" />
								<input type='hidden' name="pictureSize" value="${topicItem.pictureSize!}" />
								<input type='hidden' name="topicImage" value="${topicItem.topicImage!}" />
								<input type='hidden' name="salePrice" value="${topicItem.salePrice!}" />
								<input type='hidden' name="categoryId" value="${topicItem.categoryId!}" />
								<input type='hidden' name="stock" value="${topicItem.stock!}" />
								<input type='hidden' name="brandId" value="${topicItem.brandId!}" />
								<input type='hidden' name="inputSource" value="${topicItem.inputSource!}" />
								<input type='hidden' name="bondedArea" value="${topicItem.bondedArea!}" />
								<input type='hidden' name="whType" value="${topicItem.whType!}" />
								<input type='hidden' name="countryId" value="${topicItem.countryId!}" />
								<input type='hidden' name="countryName" value="${topicItem.countryName!}" />
								<input type='hidden' name="operStatus" value="${topicItem.operStatus!}" />
								<input type='hidden' name="lockStatus" value="${topicItem.lockStatus!}" />
								<input type='hidden' name="changeTopicItemId" value="${topicItem.changeTopicItemId!}" />
								<input type='hidden' name="topicChangeId" value="${topicItem.topicChangeId!}" />
								<input type='hidden' name="sourceLimitTotal" value="${topicItem.sourceLimitTotal!}" />
							</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="topicInteger" name="sortIndex" value="${topicItem.sortIndex!}" />
								<#else>
									<span>${topicItem.sortIndex!}</span>
								</#if>
							</td>
							<td><span>${topicItem.supplierName!}</span></td>
							<td><span>${topicItem.barCode!}</span><br/><span>${topicItem.sku!}</span><br/><span>${topicItem.name!}</span></td>
							<td><span>${topicItem.itemSpec!}</span></td>
							<td>
								<!-- 图片可以点击看完整图片 -->
								<div style="float:left;width:90%;">
									<img name="selectImage" style="width:90%" key="${topicItem.imageFullPath!}" src=""/>
								</div>
								<#if ("view" != "${mode}")>
									<div style="float:left;">
										<a name="removeImage" href="javascript:void(0);">X</a>
									</div>
								</#if>
							</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 topicNumber" name="topicPrice" value="${topicItem.topicPrice!}"/>
								<#else>
									<span>${topicItem.topicPrice!}</span>
								</#if>
							</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 topicInteger" name="limitAmount" value="${topicItem.limitAmount!}"/>
								<#else>
									<span>${topicItem.limitAmount!}</span>
								</#if>
							</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30" name="limitTotal" value="${topicItem.limitTotal!}" readonly/>
								<#else>
									<span>${topicItem.limitTotal!}</span>
								</#if>
							</td>
							<td><span>${topicItem.stockLocation!}</span></td>
							<!--
							<td><span>暂无</span></td>
							
							<td><span>${topicItem.stockAmout!}</span></td>
							-->
							<td><span>
								<!-- 预留 -->
								<#if (1 == topicDetail.topic.reserveInventoryFlag)>
									<#if topicItem.remainStock??>${topicItem.remainStock}</#if>
								<#else>
									
								</#if>
							</span></td>
							<td>
								<input name="hiddenItemTags" type="hidden" value='${topicItem.itemTags}'/>
							</td>
							<td>
								<#if ("view" != "${mode}")>
									<select class="select" style="width:80px;" name="purchaseMethod" id="purchaseMethod">
										<option value="1" <#if (topicItem.purchaseMethod == 1)>selected</#if>>普通</option>
										<option value="2" <#if (topicItem.purchaseMethod == 2)>selected</#if>>立即购买</option>
									</select>
								<#else>
									<span>
										<#if (topicItem.purchaseMethod == 1)>普通</#if>
										<#if (topicItem.purchaseMethod == 2)>立即购买</#if>
									</span>
								</#if>
							</td>
							<td><span>${topicItem.remark!}</span></td>
							<#if ("view" != "${mode}")>
								<td>
									<#if (1 == topicDetail.topic.reserveInventoryFlag)>
										<a href="javascript:void(0);" name="editItem">[编辑]</a> |
										<a href="javascript:void(0);" name="removeItem">[删除]</a>
									<#else>
										<a href="javascript:void(0);" name="removeItem">[删除]</a>
									</#if>
								</td>
							<#else>
								<#if (!(order)?? && topicDetail.topic.status == 3)>
									<td>
										<#if (0 == topicItem.lockStatus)>
											<a href="javascript:void(0);" name="lockItem">[锁定]</a>
										<#else>
											<a href="javascript:void(0);" name="lockItem">[解锁]</a>
										</#if>
									</td>
								</#if>
							</#if>
						</tr>
					</#list>
				</#if>
			</table>
		</td>
	</tr>
</table>
<script>
	$(function(){
		$("input[name='hiddenItemTags']").each(function(){
			var itemTags = $(this).val();
			//console.log(itemTags);
			if(itemTags){
				var jsonItemTags = JSON.parse(itemTags);
				for(var i=0;i<jsonItemTags.length; i++){
					$(this).parent().append("<div style='text-align:left;background-color: "+jsonItemTags[i].bgcolor+";color:"+jsonItemTags[i].fontcolor+"'>"+ jsonItemTags[i].tag +"</div><br>");
				}
			}
		});
	});
</script>