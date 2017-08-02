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
        	<div class="box_top"><b class="pl15">入库单信息</b></div>
  		</div>
    </div>
        
	<div id="table" class="mt10">
        <div class="box span10 oh">
			<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0" style=" overflow:outo"> 
				<tbody>
					<tr>
						<td class="td_right" style="width: 20%">采购单号：</td>
						<td class="" style="width: 30%">
							${stockasn.orderCode}
						</td>
						<td class="td_right" style="width: 20%">合同号：</td>
						<td class="" style="width: 30%">
							${stockasn.contractCode}
						</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 20%">供应商编码：</td>
						<td class="" style="width: 30%">
							${stockasn.supplierCode}
						</td>
						<td class="td_right" style="width: 20%">供应商名称：</td>
						<td class="" style="width: 30%">
							${stockasn.supplierName}
						</td>
					</tr>
					<tr>
						<td class="td_right" style="width: 20%">仓库编号：</td>
						<td class="" style="width: 30%">
							${stockasn.warehouseCode}
						</td>
						<td class="td_right" style="width: 20%">仓库名称：</td>
						<td class="" style="width: 30%">${stockasn.warehouseName}</td>
					</tr>
					<tr>	
						<td class="td_right" style="width: 20%">创建时间：</td>
						<td class="" style="width: 30%">${stockasn.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
					</tr>
					
				</tbody>
			</table>
		</div>
	</div>
	
	<!-- 查询条件开始 -->
<form method="post" action="${domain}/wms/stockin/viewItem.htm" id="stockInDetailForm">	
	<input type="hidden" name="stockasnId" value="${stockasnDetail.stockasnId}">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">入库数据详情</b></div>
          </div>
        </div>
    </div>
    <!-- 查询条件结束 -->
    
    <!-- 列表开始 -->
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" id="list_table">
                <tr>
           	   <th width="20">序号</th>
           	   <th width="50">商品sku</th>
               <th width="50">商品条形码</th>
               <th width="50">申报数量</th>
               <th width="50">商品实际价格</th>
               <th width="50">销售价格</th>
               <th width="50">创建时间</th>
               <th width="50">备注</th>
                </tr>
            <#if page.rows ?default([])?size !=0>
            <#list page.rows as asnItem>
	             <tr>
	               <td width="20" align="center">${asnItem_index+1}</td>
	               <td width="50" align="center">${asnItem.itemSku?if_exists}</td>
	               <td width="50" align="center">${asnItem.itemBarcode?if_exists}</td>
	               <td width="50" align="center">${asnItem.quantity?if_exists}</td>
	               <td width="50" align="center">${asnItem.actualPrice?if_exists}</td>
	               <td width="50" align="center">${asnItem.price?if_exists}</td>
	               <td width="50" align="center">${asnItem.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	               <td width="50" align="center">${asnItem.remark?if_exists}</td>
	            </tr>
            </#list>
            </#if>
              </table>
	     </div>
	</div><!-- 列表结束 -->
	<@pager  pagination=page  formId="stockInDetailForm" /> 
</form>
</@backend>