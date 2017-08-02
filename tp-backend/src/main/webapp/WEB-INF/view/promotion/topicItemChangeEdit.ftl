<#include "/common/common.ftl"/> 
<@backend title="编辑变更单中活动商品" 
		js=['/statics/backend/js/promotion/promotionItemChange_edit.js',
			'/statics/backend/js/json2.js',
	        '/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/promotion/utils.js']
		css=[]>
	<input type="hidden" id="topicItemChangeId" value="${topicItemChangeId!}"/>
	<#if (topicItemInfo??)>
		<input type="hidden" id="topicId" value="${topicItemInfo.topicId!}"/>
		<input type="hidden" id="topicItemId" value="${topicItemInfo.id!}"/>
		<input type="hidden" id="supplierId" value="${topicItemInfo.supplierId!}"/>
		<table cellspacing="0" cellpadding="0" border="0" width="100%" id="topicItemTable" class="list_table" style="margin-top:20px;">
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">商品编号:</td>
				<td style="text-align:left;">
					<span id="avaliableAmount">${inventory.sku!}</span>
				</td>
			</tr>
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">总库存:</td>
				<td style="text-align:left;">
					<span id="avaliableAmount">${inventory.inventory!}</span>
				</td>
			</tr>
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">预留库存:</td>
				<td style="text-align:left;">
					<span id="avaliableAmount">${inventory.reserveInventory!}</span>
				</td>
			</tr>
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">冻结库存:</td>
				<td style="text-align:left;">
					<span id="avaliableAmount">${inventory.occupy!}</span>
				</td>
			</tr>
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">预警库存:</td>
				<td style="text-align:left;">
					<span id="avaliableAmount">${inventory.warnInventory!}</span>
				</td>
			</tr>
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">可售库存:</td>
				<td style="text-align:left;">
					<span id="avaliableAmount">${inventory.inventory-inventory.occupy-inventory.reserveInventory!}</span>
				</td>
			</tr>
			<!--
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">有效库存:</td>
				<td style="float:left;margin-left:50px;">
					<span id="avaliableAmount">${topicItemInfo.stockAmount!}</span>
				</td>
			</tr>
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">已占用数量:</td>
				<td style="float:left;margin-left:50px;">
					<span id="saledAmount">${topicItemInfo.saledAmount!}</span>
				</td>
			</tr>
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">活动占用剩余库存:</td>
				<td style="float:left;margin-left:50px;">
					<span id="remainStock">${remainStock!}</span>
				</td>
			</tr>
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">当前限购总量:</td>
				<td style="float:left;margin-left:50px;">
					<span id="currentLimitAmount">${topicItemInfo.limitTotal!}</span>
				</td>
			</tr>
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">变更单限购总量:</td>
				<td style="float:left;margin-left:50px;">
					<span id="changeOrderLimitTotal">${changeOrderLimitTotal!}</span>
				</td>
			</tr>
			<tr style="height:30px;">
				<td class="td_right" width="50" align="right">变更限购总量:</td>
				<td style="float:left;margin-left:50px;">
					<input type="text" id="addLimitAmount" class="input-text lh30"/>
				</td>
			</tr>
			<tr style="height:30px;">
				<td align="right">
					<div style="padding-right:10px;margin-top:15px;">
						<input type="button" class="btn btn82 btn_save2" id="save" value="保存" />
					</div>
				</td>
				<td align="left">
					<div style="padding-left:10px;margin-top:15px;">
						<input type="button" class="btn btn82 btn_del closebtn" id="cancel" value="取消" />
					</div>
				</td>
			</tr>
			-->
		</table>
	<#else>
		获取库存信息失败<br/>
		<input type="button" class="btn btn82 btn_del closebtn" id="cancel" value="取消" />
	</#if>
</@backend>