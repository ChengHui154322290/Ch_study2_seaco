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
             <th width="15"> </th>
           	    <th width="15"><input type="checkbox" id="chkall" /> </th>
                   <th>供应商编号</th>
                   <th>供应商名称</th>
                   <th>类型</th>
        </tr>
    </thead>
    <tbody>
        <#if (page.rows)?default([])?size !=0>       
        <#list page.rows as item>
        <tr class="tr">
           <td width="15">
             <input type="checkbox" name="spId" value ="${sl.id}"/>
             <input type="hidden" name="supplierType" value='${sl.supplierType}' />
             <input type="hidden" name="supplierName" value='${sl.name}' />
             <input type="hidden" name="supplierTypeName" value='${supplierTypes[sl.supplierType]}' />
            </td>
            <td >${sl.id}</td>
            <td >${sl.name}</td>
            <td >${supplierTypes[sl.supplierType]}</td>
        </tr>
        </#list>
        </#if>
    </tbody>
</table>
<#if page?exists>
<@p page=page.page totalpage=page.total />
</#if>
