<#include "/commons/base/page.ftl" />
<div class="widget-header header-color-blue">
	<h5 class="bigger lighter">
	<i class="icon-table"></i>
		总数：${(page.records)!0}
	</h5>												
</div>
<table class="table table-striped table-bordered table-hover">
    <thead>
         <tr>
              <th  width="60">状态</th>
           	  <th width="300">原因</th>
           	  <th width="60">SPU编号</th>
           	  <th width="60">PRDID编号</th>
           	  <th width="60">SKU编号</th>
           	  
           	  <th width="60">商家名称</th>
           	  <th width="60">*商家ID</th>
           	  <th width="100">*条形码（商品码）</th>
           	  <th width="60">*SKU名称</th>
           	  <th width="60">大类名称</th>
           	  <th width="60">*大类ID</th>
           	  <th width="60">中类名称</th>
           	  <th width="60">*中类ID</th>
            </tr>
    </thead>
    <tbody>
        <#if (page.rows)?default([])?size !=0>       
        <#list page.rows as l>
        <tr class="tr">
               <tr>
               <td width="60">
                   <#if l.status = 1> 
                                                                     成功
                   <#else>
                                                                 失败
                   </#if> </td>
               <td width="300">${l.opMessage}</td>
               <td width="60">${l.spuCode}</td>
               <td width="60">${l.prdid}</td>
               <td width="60">${l.skuCode}</td>
               <td width="60">${l.spName}</td>
               <td width="60">${l.spId}</td>
               <td width="60">${l.barcode}</td>
               <td width="60">${l.mainTitle}</td>
               <td width="60">${l.largeName}</td>
               <td width="60">${l.largeId}</td>
               <td width="60">${l.mediumName}</td>
               <td width="60">${l.mediumId}</td>
            </tr>
        </#list>
        </#if>
    </tbody>
</table>
<#if page?exists>
<@p page=page.page totalpage=page.total />
</#if>
