<#include "/common/common.ftl"/>
<@backend title="新增仓库预约单" 
js=[
	'/statics/supplier/js/common.js',
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	'/statics/supplier/js/web/warehouseorder_add.js']
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css']
>
<style type="text/css">
.pb15 {
	padding-bottom: 0px;
}
</style>
<div class="box">
	<div class="box_border">
		<div class="box_top">
			<b class="pl15">新增仓库预约单</b>
		</div>
		<div class="box_center" style="margin-bottom: 30px;">
			<!--by zhs  01132121 修改action -->
			<form id="warehouse_add_form" class="jqtransform" method="post" action="${domain}/supplier/warehouseOrderSave.htm">
			<input type="hidden" value="" id="supplierId" name="supplierId" />
			<input type="hidden" value="" id="warehouseId" name="warehouseId"/>
			<input type="hidden" value="" id="warehouseLinkmanId" name="warehouseLinkmanId" />
				<input type="hidden" value="" id="id" name ="id"/>
			<input type="hidden" value="" id="orderExpectDate" name="orderExpectDate" />
				<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
					<tbody>
						<tr>
							<td class="td_right" align="right">单据类型：</td>
							<td align="left" width="600">
								<div class="select_border">
									<div class="select_containers">
										<span class="fl">
											<select class="select" id="purchaseTypeId" name="purchaseType" style="width: 150px;">
												<option value="">全部</option>
												<#if PURCHARSE_TYPE_MAP?exists>
                                                    <#list PURCHARSE_TYPE_MAP?keys as key>
                                                       <option value="${key}">${PURCHARSE_TYPE_MAP[key]}</option>
                                                    </#list>
                                                </#if>
											</select>
											<input type="text" id="purchaseCode" name="purchaseCode" placeholder="单号" maxlength="60" class="_req input-text lh30" size="20">
											<input type="button" id="orderTypeConfirm" value="确定" class="ext_btn ext_btn_submit">
											<#-- <input type="button" id="orderTypeQuery" class="ext_btn" value="查询"> -->
										</span>
									</div>
								</div>

							</td>

							<td style="border-left: thin dashed blue;" valign="top" rowspan="7">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;预约日期： 
								<input type="text" id="bookingDate" name="bookingDate" datafmt="yyyy-MM-dd HH:00:00" class="_dateField input-text lh30" size="20">
							</td>
						</tr>

						<tr>
							<td class="td_right" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供应商：</td>
							<td align="left">
								<input type="text" name="supplierName" id="supplierName" value="" maxlength="200" class="_req input-text lh30" size="50" readonly="readonly">
							</td>
						</tr>

						<tr>
							<td class="td_right" align="right">日期：</td>
							<td align="left">
								<input type="text" name="purchaseDate" id="purchaseDate" value="" maxlength="200" class="_req input-text lh30" readonly="readonly" size="18">
							</td>
						</tr>

						<tr>
							<td class="td_right" align="right">仓库名称：</td>
							<td align="left">
								<input type="text" name="warehouseName" id="warehouseName" value="" maxlength="200" class="_req input-text lh30" size="50" readonly="readonly">
							</td>
						</tr>

						<tr>
							<td class="td_right" align="right">仓库地址：</td>
							<td align="left">
								<input type="text" name="warehouseAddr" id="warehouseAddr" value="" maxlength="200" class="_req input-text lh30" size="50" readonly="readonly">
							</td>
						</tr>

						<tr>
							<td class="td_right" align="right">仓库联系人：</td>
							<td align="left">
								<input type="text" name="warehouseLinkmanName" id="warehouseLinkmanName" value="" maxlength="200" class="_req input-text lh30" size="50" readonly="readonly">
							</td>
						</tr>

						<tr>
							<td class="td_right" align="right">联系电话：</td>
							<td align="left">
								<input type="text" name="warehouseLinkmanTel" id="warehouseLinkmanTel" value="" maxlength="200" class="_req input-text lh30" size="50" readonly="readonly">
							</td>
						</tr>

						<tr>
							<td colspan="2" align="center">
								<input type="button" id="order_add_save" value="保存" class="ext_btn ext_btn_submit">
								<input type="button" id= "order_add_saveandsubmit" value="提交" class="ext_btn ext_btn_submit">
								<input type="button" id="order_add_cancel" value="取消" id="" class="ext_btn ext_btn_submit">
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</div>

</@backend>