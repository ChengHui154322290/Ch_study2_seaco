<#include "/commons/base/page.ftl" />
<label class="control-label">总数：${(page.records)!0}</label>
<table class="table table-bordered table-striped table-hover">
    <thead>
		  <tr>
	       	   <th align="center" width="20%">SPU</th>
	       	   <th align="center" width="16%">PRDID</th>
	       	   <th align="center"  width="16%">SKU</th>
	       	   <th align="center" width="16%">条形码</th>
	       	   <th align="center" width="16%">名称</th>
	       	   <th align="center"  width="16%">操作</th>
	        </tr>
    </thead>
    <tbody>
      <#if (page.rows)?default([])?size !=0>       
        <#list page.rows as sub>
        <tr>
            <td>${(sub.spu)!}</td>
            <td>${(sub.prdid)!}</td>
            <td>${(sub.sku)!}</td>
            <td>${(sub.barcode)!}</td>
            <td>${(sub.mainTitle)!}</td>
            <td>
            		<#if sub.mainMark??>
            			<a href="javascript:void(0)" onclick="showMainSkuInfo('${(sub.skuId)!}')">查看明细</a>
            			<a href="javascript:void(0)" onclick="showMainAuditJournal('${(sub.sku)!}')">日志</a>   
            		<#else>
            			<a href="javascript:void(0)" onclick="showSkuInfo('${(sub.skuId)!}')">查看明细</a>
            			<a href="javascript:void(0)" onclick="showAuditJournal('${(sub.skuId)!}')">日志</a> 
            		</#if>
            		
            </td>
        </tr>
        </#list>
        </#if>
    </tbody>
</table>
<#if page?exists>
<@p page=page.page totalpage=page.total />
</#if>