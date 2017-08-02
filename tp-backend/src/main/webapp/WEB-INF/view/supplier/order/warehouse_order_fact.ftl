<#include "/common/common.ftl"/> 
<#include "/supplier/common/page.ftl" />
<@backend title="仓库预约单" 
    js=['/statics/supplier/js/common.js',
    '/statics/backend/js/layer/layer.min.js',
        '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
        '/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
        	'/statics/supplier/js/validator.js',
        '/statics/supplier/js/web/warehouseorder_list.js'
       ] 
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >

            	<!--by zhs 01132104 修改action -->
                <form id="warehouseorder_list_form" class="jqtransform" action="${domain}/supplier/warehouseorderList.htm" method="post">

            <div class="box_center  pt10 pb10">
                <table width="100%" cellspacing="0" cellpadding="0" border="0"
                    class="list_table CRZ" id="CRZ0">
                    <tbody>
                        <tr align="center">
                            <th width="50%">商品名称</th>
                        	<th width="20%">SKU</th>
                            <th width="15%">采购数量</th>
                            <th width="15%">实际入库数量</th>
                        </tr>
                        
				<#if result.data?default([])?size !=0>
                    <#list result.data as d>
                    <tr align="center">
                        <th width="50%">${d.itemName}</th>
                        <th width="20%">${d.sku}</th>
                        <th width="15%">${d.planAmount}</th>
                        <th width="15%">${d.factAmount}</th>
                    </tr>
                        </#list>
                   </#if>
                    </tbody>
                </table>               
            </div>
         </form>

</@backend>
