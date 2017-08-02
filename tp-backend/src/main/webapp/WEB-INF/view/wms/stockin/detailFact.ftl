<#include "/common/common.ftl"/>
<@backend title="入库明细列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/wms/js/stockin.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >

	<div class="box">
    	<div class="box_border">
        	<div class="box_top"><b class="pl15">入库单回执信息</b></div>
  		</div>
    </div>
        
	<div id="table" class="mt10">
        <div class="box span10 oh">
			<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0" style=" overflow:outo"> 
				<tbody>
					<tr>
						<td class="td_right" style="width: 20%">入库订单号：</td>
						<td class="" style="width: 30%">
							${stockasnFact.stockasnId}
						</td>
						<td class="td_right" style="width: 20%">采购单号：</td>
						<td class="" style="width: 30%">
							${stockasnFact.orderCode}
						</td>
					<tr>
					</tr>
						<td class="td_right" style="width: 20%">仓库编号：</td>
						<td class="" style="width: 30%">
							${stockasnFact.warehouseCode}
						</td>
						<td class="td_right" style="width: 20%">货主：</td>
						<td class="" style="width: 30%">
							${stockasnFact.goodsOwner}
						</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 20%">审核人：</td>
						<td class="" style="width: 30%">
							${stockasnFact.auditor}
						</td>
						<td class="td_right" style="width: 20%">审核时间：</td>
						<td class="" style="width: 30%">
						<#if '${stockasnFact.auditTime?if_exists}' >
							${stockasnFact.auditTime?string("yyyy-MM-dd HH:mm:ss")}
						</#if>
						</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 20%">电商编码：</td>
						<td class="" style="width: 30%">
							${stockasnFact.providerCode}
						</td>
						<td class="td_right" style="width: 20%">备注：</td>
						<td class="" style="width: 30%">
							${stockasnFact.remark}
						</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 20%">创建时间：</td>
						<td class="" style="width: 30%">${stockasnFact.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
					</tr>
					
				</tbody>
			</table>
		</div>
	</div>
	
	<!-- 查询条件开始 -->
<form method="post" action="${domain}/wms/stockin/detailFact.htm" id="stockInDetailFactForm">	
	<input type="hidden" name="stockasnFactId" value="${stockasnDetailFact.stockasnFactId}">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">入库单回执数据详情</b></div>
          </div>
        </div>
    </div>
    <!-- 查询条件结束 -->
    
    <!-- 列表开始 -->
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" id="list_table">
                <tr>
           	   <th width="5%">序号</th>
           	   <th width="9%">我方sku</th>
               <th width="9%">第三方sku</th>
               <th width="9%">实际入库数量</th>
               <th width="9%">创建时间</th>
                </tr>
            <#if page.rows ?default([])?size !=0>
            <#list page.rows as detailFact>
	             <tr>
	               <td width="5%" align="center">${detailFact_index+1}</td>
	               <td width="9%" align="center">${detailFact.sku?if_exists}</td>
	               <td width="9%" align="center">${detailFact.skuTp?if_exists}</td>
	               <td width="9%" align="center">${detailFact.quantity?if_exists}</td>
	               <td width="9%" align="center">${detailFact.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	            </tr>
            </#list>
            </#if>
              </table>
	     </div>
	</div><!-- 列表结束 -->
	<@pager  pagination=page  formId="stockInDetailFactForm" /> 
</form>
</@backend>