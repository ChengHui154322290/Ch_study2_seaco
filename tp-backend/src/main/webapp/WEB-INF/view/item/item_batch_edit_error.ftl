<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/item/item_batch_edit.js'
	] 
	css=[
	'/statics/backend/css/style.css' ] >

 <div style="width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
 <div class="box_center">
 	<div class="mt10">
 		<span>批量修改结果( </span>
    	<span>导入成功：${successCnt}</span>    
    	<span>导入失败：${failCnt})</span>
    </div>
	<#if message??>	
        <div><span class="fl">${message}</span></div>
    <#else>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
     	<tr>
           <th width="50">SKU</th>
           <th width="120">失败原因</th>
        </tr>
        <#list errorMap?keys as key>
		    <tr>
	            <td>${key}</td>
	            <td>${errorMap[key]}</td>
	        </tr>
		</#list>
    </table>
    </#if>
</div>
</div>
</@backend>
