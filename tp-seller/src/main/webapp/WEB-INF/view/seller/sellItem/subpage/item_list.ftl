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
           	   <th width="100">SPU名称</th>
           	   <th width="100">大类</th>
           	   <th width="100">中类</th>
           	   <th width="100">小类</th>
           	   <th width="100">spu</th>
           	   <th width="100">prdid</th>
           	   <th width="100">条形码</th>
               <th width="450">网站显示名称</th>
               <th width="150">类型</th>
               <th width="200">发布</th>
               <th width="200"><i class="icon-time bigger-110 hidden-480"></i>创建日期</th>
               <th width="200">操作</th>
        </tr>
    </thead>
    <tbody>
        <#if (page.rows)?default([])?size !=0>       
        <#list page.rows as item>
        <tr class="tr">
               <td width="15"><input type="checkbox" name ="detailId"  class="detailIdCheckbox" value="${item.detailId}"/> </td>
               <td width="200">${item.spuName}</td>
               <td width="200">${item.largeName}</td>
               <td width="200">${item.mediumName}</td>
               <td width="200">${item.categoryName}</td>
               
               <td width="100">${item.spu}</td>
               <td width="100">${item.prdid}</td>
               <td width="100">${item.barcode}</td>
               <td width="500">${item.mainTitle}</td> 
               <td width="100"><#if item.wavesSign ==1>国内<#elseif item.wavesSign==2>海淘</#if></td> 
               <td width="100">${item.statusDesc}</td>
               <td width="200"><#if item.createTime??>${item.createTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
               <td width="200"><a  href="javascript:void(0);" onclick="show(${item.detailId});">[编辑]<a  href="javascript:void(0);" onclick="newPrd(${item.itemId});"> [PRD]</a>
               </td>
            </tr>
        </#list>
        </#if>
    </tbody>
</table>
<#if page?exists>
<@p page=page.page totalpage=page.total />
</#if>
