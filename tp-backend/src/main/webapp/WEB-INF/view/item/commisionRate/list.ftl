<#include "/common/common.ftl"/> 
<@backend title="分销佣金比列管理" 
 js=[ '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/item/item_commision_rate.js',
	'/statics/supplier/component/date/WdatePicker.js'] 
css=[] >
<form method="post" action="${domain}/item/commisionRate/list.htm" id="commisionRateForm">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">商品列表</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>SPU名称:</td>
                  		<td><input type="text" name="spuName" value="${itemSku.spuName}"  class="input-text lh25" size="20"></td>
                  		<td>spu:</td>
                  		<td><input type="text" name="spu" value="${itemSku.spu}"  class="input-text lh25" size="20"></td>
                  		<td>商品名称:</td>
                  		<td>
                    		<input type="text" name="detailName" value="${itemSku.detailName}" class="input-text lh25" size="20">
                  		</td>
                  		<td>sku:</td>
                  		<td>
                   			<input type="text" name="sku" value="${itemSku.sku}" class="input-text lh25" size="20">
                 		</td>
                 		<td>比例区间:</td>
                  		<td>
                   			<input type="text" name="startRate" value="${itemSku.startRate}" class="input-text lh25" size="20"> 至
                   			<input type="text" name="endRate" value="${itemSku.endRate}" class="input-text lh25" size="20">
                 		</td>
						<td>发布状态</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="status" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list statusList as status>
			                       		<option value="${status.value}" <#if status.value==itemSku.status>selected</#if> >${status.key}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
						</td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: right;">
					<input type="submit" value="查询" class="btn btn82 btn_search querypagebtn" name="button">
				</div>
			</div>
		</div>
	</div>
</div>
<div id="table" class="mt10">
	<div class="box span10 oh">
		<table  id="CRZ0" width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
			<tr style="text-align:center">
				<th width="15%">SPU名称</th>
				<th width="10%">SPU</th>
                <th width="15%">商品名称</th>
                <th width="10%">SKU</th>
                <th width="20%">佣金比例(0~0.99)</th>
                <th width="10%">提佣类型</th>
                <th width="10%">发布状态</th>
                <th width="10%">操作</th>
			</tr>
			<#if page.rows?default([])?size !=0>     
				 <#list page.rows as itemSku>
				<tr style="text-align:center">
					<td>${itemSku.spuName}</td>
					<td>${itemSku.spu}</td>
					<td>${itemSku.detailName}</td>
					<td>${itemSku.sku}</td>
					<td value="${itemSku.id}">${itemSku.commisionRate}</td>
					<td><select name="commisionTypeSelect"><#list commisionTypeList as commisionType>
					<option value="${commisionType.type}" <#if commisionType.type==itemSku.commisionType>selected</#if> >${commisionType.cnName}</option>
					</#list></select></td>
					<td>${itemSku.statusDesc}</td>
					<td><input type="button" value="修改" class="ext_btn ext_btn_submit editCommisionRateBtn" isUpdate="0" skuId="${itemSku.id}" commisionRate="${itemSku.commisionRate}"></td>
				</tr>
				</#list> 
			</#if>
		</table>
	</div>
</div>
<@pager  pagination=page  formId="commisionRateForm"  /> 
</form>
</@backend>
