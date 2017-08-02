<#include "/common/common.ftl"/> 
<@backend title="新增活动商品" 
			js=['/statics/backend/js/json2.js',
	        	'/statics/backend/js/layer/layer.min.js',
				'/statics/backend/js/promotion/utils.js',
				'/statics/backend/js/spectrum/spectrum.js',
				'/statics/backend/js/promotion/promotionItem_add.js']
			css=['/statics/backend/js/spectrum/spectrum.css']>
		<div style="margin-top:20px;" />
		<form id="addTopicItem" >
			<input type="hidden" id="topicId" name="topicId" value="${topicId}" />
			<input type="hidden" id="preSpu" name="preSpu" value="${spu}" />
			<input type="hidden" id="itemId" name="itemId" />
			<input type="hidden" id="itemSPU" name="spu" />
			<input type="hidden" id="itemSpec" name="itemSpec" />
			<input type="hidden" id="topicImage" name="topicImage" />
			<input type="hidden" id="imageFull" name="imageFull" />
			<input type="hidden" id="supplierId" name="supplierId" />
			<input type="hidden" id="preSupplierId" value="${supplierId}" />
			<input type="hidden" id="skuId" name="skuId" />
			<input type="hidden" id="name" name="name" />
			<input type="hidden" id="categoryId" name="categoryId" />
			<input type="hidden" id="stock" name="stock" />
			<input type="hidden" id="brandId" name="brandId" />
			<input type="hidden" id="bondedArea" name="bondedArea" />
			<input type="hidden" id="whType" name="whType" />
			<input type="hidden" id="countryId" name="countryId" />
			<input type="hidden" id="countryName" name="countryName" />
			<input type="hidden" id="parentBrandId" name="parentBrandId" value="${parentBrandId}" />
			<input type="hidden" id="itemTags" name="itemTags"/>
			<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15" style="width:100%;margin:0 auto;">
				<tr>
					<td class="td_right" width="50" align="right">序号:</div>
					<td>
						<input type="text" class="input-text lh30" id="sortIndex" name="sortIndex" value="${sortIndex!}" />
						<input type="hidden" id="itemId" />
					</td>
				</tr>
				<tr>
					<td class="td_right" width="50" align="right">条码:</td>
					<td>
						<input type="text" class="input-text lh30" id="itemBar" name="barCode" />
						<input type="button" class="btn btn82 btn_save2" id="itemBarConfirm" value="确定" />
						<input type="button" class="btn btn82 btn_search" id="itemBarSearch" value="查询" />
					</td>
				</tr>
				<tr>
					<td class="td_right" width="50" align="right">SKU:</td>
					<td>
						<input type="text" class="input-text lh30" id="itemSKU" name="sku" />
						<input type="button" class="btn btn82 btn_save2" id="itemSKUConfirm" value="确定" />
						<input type="button" class="btn btn82 btn_search" id="itemSKUSearch" value="查询" />
					</td>
				</tr>
				<tr>
					<td class="td_right" width="50" align="right">商家:</td>
					<td><span id="supplierName"></td>
				</tr>
				<tr>
					<td class="td_right" width="50" align="right">市场价:</td>
					<td>
						<span id="salePrice"></span>
					</td>
				</tr>
				<tr>
					<td class="td_right" width="50" align="right">销售价:</td>
					<td>
						<input type="text" class="input-text lh30 topicNumber"  id="topicPrice" />
					</td>
				</tr>
				<tr>
					<td class="td_right" width="50" align="right">限购数量:</td>
					<td>
						<input type="text" class="input-text lh30 topicInteger"  id="limitAmount" />
					</td>
				</tr>
				<!--
				<tr>
					<td class="td_right" width="50" align="right">限购总量:</td>
					<td>
						<input type="text" class="input-text lh30 topicInteger"  id="limitTotalAmount" />
					</td>
				</tr>
				-->
				<tr>
					<td class="td_right" width="50" align="right">图片大小:</td>
					<td>
						<select class="select" style="width:150px;" id="pictureSize">
							<option value="1" selected>1*1</option>
							<option value="2">1*2</option>
							<option value="4">1*4</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td_right" width="50" align="right">购买方式:</td>
					<td>
						<select class="select" style="width:150px;" id="purchaseMethod">
							<option value="1" selected>普通</option>
							<option value="2">立即购买</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td_right" width="50" align="right">仓库:</td>
					<td>
						<div id="whList">
							<select class="select" style="width:150px;" id="warehouse">
							</select>
						</div>
					</td>
				</tr>
				<!--
				<tr>
					<td class="td_right" width="50" align="right">可用库存:</td>
					<td><span id="warehouseAmount"></span></td>
				</tr>
				-->
				<tr>
					<td class="td_right" width="50" align="right top">标签:</td>
					<td>
						<table id="tagtable">
							<tr>
								<td><input class="input-text lh30" type="text" name="tags" style="color:#ffffff;background-color:#EE3454"/></td>
								<td><input type='text' name="color" value="#EE3454"/></td>
								<td>
									<input type="text" name="textColor" value="#ffffff"/>
									<span  name="addTag" title="" style="margin-left: 10px;cursor: pointer" class="add_coupon_icon">  </span>
									<span  name="removeTag" title="" style="margin-left: 10px;cursor: pointer" class="remove_coupon_icon">  </span>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr rowspan="3">
					<td class="td_right" width="50" align="right">备注:</td>
					<td>
						<textarea id="remark" name="remark" cols="40" rows="6"></textarea>
					</td>
				</tr>
				<tr>
					<td align="right">
						<div style="padding-right:10px">
							<input type="button" class="btn btn82 btn_save2" id="save" value="保存" />
						</div>
					</td>
					<td align="left">
						<div style="padding-left:10px">
							<input type="button" class="btn btn82 btn_del closebtn" id="cancel" value="取消" />
						</div>
					</td>
				</tr>
			</table>
		</form>
</@backend>