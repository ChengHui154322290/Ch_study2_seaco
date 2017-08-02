<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/item/upload-file.js'
	] 
	css=[
	'/statics/backend/css/style.css' ] >
<!--
-->
<form id="importLogForm" action="import.htm" method="post" >
	  <input type="hidden" name="logId" id="logId" value="${importLog.id}" />
	  <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top">
             <b class="pl15">商品资料导入</b>

            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
           	  <div  style="text-align:right; float:left">
           	  	 选择模板 ：
           	  	 <select class="select" id="chooseTemplate">
           	  	 	<option value="">非海淘模板</option>
           	  	 	<option value="isWave">海淘模板</option>
           	  	 </select>
                 <input  id="uploadFileBtn"  class="btn btn82 btn_export" type="button" value="上传" name="button"  />
              </div>
              <div class="search_bar_btn" style="text-align:right;">
               	 <select class="select" name="status">
                    	<option value="0">-- 全部 --</option>
                    	<option value="1" <#if "${status}"=="1"> selected </#if>>导入成功</option>
                    	<option value="2" <#if "${status}"=="2"> selected </#if>>导入失败</option>
                 </select>
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search"  type="button" id="queryImportLogBtn"  value="查询" name="button" onclick="$('#importLogForm').attr('action','import.htm');$('#importLogForm').submit();" /></a>
                 <input class="btn btn82 btn_export" type="button" value="导出"    id="exportFileBtn" />
              </div>
              <div class="mt10" > 
    			  <span>总数量：${importLog.sumCount}</span>
    			  <span>导入成功：${importLog.successCount}</span>
    			  <span>导入失败：${importLog.failCount}  </span>
    			  <span>状态：<#if importLog.status==1> 等待处理</#if> <#if importLog.status==2> 正在处理</#if> <#if importLog.status==4> 处理完成 </#if> <#if importLog.status==5>处理失败,请重新上传模板</#if> </span>
    			  <#-->
    	 	 	  <span>导入未处理：${importLog.sumCount - importLog.failCount - importLog.successCount}  </span>
    	 	 	  </#-->
    	 	 </div>
            </div>
          </div>
        </div>
    </div>
   <div id="table" class="mt10">
    	<div class="box span10 oh">
    	<div style="overflow:auto;">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table list_table_no_lh" style="min-width:1300px;">
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
            <#if page.rows?default([])?size !=0>
            <#list page.rows as l>
             <tr>
               <td width="60">${l.status}</td>
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
               
               <!--
               
               <td width="60">${l.smallName}</td>
               <td width="60">${l.smallId}</td>
               <td width="60">${l.unitName}</td>
               <td width="60">${l.unitId}</td>
               -->
              
            </tr>
            </#list>
            <#else>
       	  		<tr style="text-align:center">
           	 	  <td width="100" colspan=13>无数据</td>
          	    </tr>
            </#if>
          </table>
         <div> 
    	</div>
    	<@pager  pagination=page  formId="importLogForm"  /> 
</form>
</@backend>
