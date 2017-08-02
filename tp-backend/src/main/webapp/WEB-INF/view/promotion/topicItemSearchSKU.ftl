<#include "/common/common.ftl"/> 
<@backend title="活动商品查询(SKU)" 
		js=['/statics/backend/js/json2.js',
	        '/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/promotion/utils.js',
			'/statics/backend/js/promotion/promotionItem_search.js']
		css=[]>
	
		<input type="hidden" class="input-text lh30" class="input-text lh30" id="parentBrandId" value="${parentBrandId!}" />
		<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
			<tr>
				<td class="td_right" width="50" align="right">SKU:</td>
				<td width="50" align="left">
					<input type="text" class="input-text lh30"  id="sku" value="${sku!}" />
				</td>
				<td><input type="button" class="btn btn82 btn_search" id="skuSearch" value="查询" /></td>
			</tr>
			<tr>
				<td colspan="3">
					<div id="topicItemSearchList">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right">
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
</@backend>