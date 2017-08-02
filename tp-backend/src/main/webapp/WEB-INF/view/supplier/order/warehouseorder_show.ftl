<#include "/common/common.ftl"/>
<@backend title="预约单查看" 
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
			<b class="pl15">预约单查看</b>
		</div>
		<div class="box_center" style="margin-bottom: 30px;">
			<!--by zhs bug 01132108 修改action -->
			<form id="warehouse_show_form" class="jqtransform" method="post" action="${domain}/supplier/warehouseOrderSub.htm">
				<input type="hidden" value="${(warehouseOrderVO.id)!}" name="spId" />
				<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
					<tbody>
						<tr>
							<td class="td_right" align="right">单据类型：</td>
							<td align="left" width="600">
								<div class="select_border">
									<div class="select_containers">
										<span class="fl">
											<select class="select" name="purchaseType" style="width: 150px;" readOnly="readOnly">
												<#if PURCHARSE_TYPE_MAP?exists>
                                                    <#list PURCHARSE_TYPE_MAP?keys as key>
                                                       <#if warehouseOrderVO.purchaseType == key>
                                                       <option value="${key}" selected="selected">${PURCHARSE_TYPE_MAP[key]}</option>
                                                       <#else>
                                                        <#--<option value="${key}">${ORDER_TYPE_MAP[key]}</option>-->
                                                       </#if>
                                                    </#list>
                                                </#if>
											</select>
											<input type="text" id="orderCode" name="orderCode" value="${(warehouseOrderVO.purchaseCode)!}" maxlength="60" class="_req input-text lh30" size="20" readonly="readonly">
											<#-- <input type="button" id="orderTypeConfirm" value="确定" class="ext_btn ext_btn_submit"> -->
											<#-- <input type="button" id="orderTypeQuery" class="ext_btn" value="查询"> -->
										</span>
									</div>
								</div>

							</td>

							<td style="border-left: thin dashed blue;" valign="top" rowspan="7">&nbsp;&nbsp;预约日期： 
								<input type="text" id="bookingDate" name="bookingDate" value="${(warehouseOrderVO.bookingDate?string("yyyy-MM-dd HH:mm:ss"))!}" class="_req input-text lh30" size="20" readonly="readonly">
							</td>
						</tr>

						<tr>
							<td class="td_right" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;供应商：</td>
							<td align="left">
								<input type="text" name="supplierName" id="supplierName" value="${(warehouseOrderVO.supplierName)!}" maxlength="200" class="_req input-text lh30" size="50" readonly="readonly">
							</td>
							<td class="td_right"></td>
							<td class="td_right"></td>
						</tr>

						<tr>
							<td class="td_right" align="right">日期：</td>
							<td align="left">
								<input type="text" name="purchaseDate" id="orderDate" value="${(warehouseOrderVO.purchaseDate?string("yyyy-MM-dd HH:mm:ss"))!}" maxlength="200" class="_req input-text lh30" size="18" readonly="readonly">
							</td>
							<td class="td_right"></td>
							<td class="td_right"></td>
						</tr>

						<tr>
							<td class="td_right" align="right">仓库名称：</td>
							<td align="left">
								<input type="text" name="warehouseName" id="warehouseName" value="${(warehouseOrderVO.warehouseName)!}" maxlength="200" class="_req input-text lh30" size="50" readonly="readonly">
							</td>
							<td class="td_right"></td>
							<td class="td_right"></td>
						</tr>

						<tr>
							<td class="td_right" align="right">仓库地址：</td>
							<td align="left">
								<input type="text" name="warehouseAddr" id="warehouseAddr" value="${(warehouseOrderVO.warehouseAddr)!}" maxlength="200" class="_req input-text lh30" size="50" readonly="readonly">
							</td>
							<td class="td_right"></td>
							<td class="td_right"></td>
						</tr>

						<tr>
							<td class="td_right" align="right">仓库联系人：</td>
							<td align="left">
								<input type="text" name="warehouseLinkmanName" id="warehouseLinkmanName" value="${(warehouseOrderVO.warehouseLinkmanName)!}" maxlength="200" class="_req input-text lh30" size="50" readonly="readonly">
							</td>
							<td class="td_right"></td>
							<td class="td_right"></td>
						</tr>

						<tr>
							<td class="td_right" align="right">联系电话：</td>
							<td align="left">
								<input type="text" name="warehouseLinkmanTel" id="warehouseLinkmanTel" value="${(warehouseOrderVO.warehouseLinkmanTel)!}" maxlength="200" class="_req input-text lh30" size="50" readonly="readonly">
							</td>
							<td class="td_right"></td>
							<td class="td_right"></td>
						</tr>

						<tr>
							<td colspan="2" align="center">
								<input type="button" id="warehoueSubmit" value="提交" class="ext_btn ext_btn_submit">
								<#--<input type="button" id="order_add_cancel" value="取消" id="" class="ext_btn ext_btn_submit">-->
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</div>

</@backend>