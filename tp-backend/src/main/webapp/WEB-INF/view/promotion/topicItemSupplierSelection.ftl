<#include "/common/common.ftl"/> 
<@backend title="商品商家选择" 
		js=['/statics/backend/js/promotion/promotionItem_search.js',
			'/statics/backend/js/json2.js',
	        '/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/promotion/utils.js']
		css=[]>
		<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ">
			<tr>
				<td colspan="2">
					<#include "/promotion/subpages/topicItemSearchList.ftl">
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
</@backend>