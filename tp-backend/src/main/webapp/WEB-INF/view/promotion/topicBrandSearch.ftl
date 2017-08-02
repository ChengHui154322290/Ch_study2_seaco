<#include "/common/common.ftl"/> 
<@backend title="商品品牌查询" 
		js=['/statics/backend/js/promotion/promotionBrand_search.js',
			'/statics/backend/js/json2.js',
	        '/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/promotion/utils.js']
		css=[]>
	<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
		<tr>
			<td class="td_right" width="50" align="right">英文名:</td>
			<td width="50" align="left">
				<input type="text" class="input-text lh30" id="nameEn" name="nameEn" value="" />
			</td>
			<td class="td_right" width="50" align="right">中文名:</td>
			<td width="50" align="left">
				<input type="text" class="input-text lh30" id="name" name="name" value="" />
			</td>
		</tr>
		<tr>
			<td class="td_right" colspan="4" style="text-align:center;">
				<input type="button" id="searchBrand" class="btn btn82 btn_search" value="查询"/>
			</td>
		</tr>
		<tr>
			<td colspan="5">
				<div id="BrandList"/>
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