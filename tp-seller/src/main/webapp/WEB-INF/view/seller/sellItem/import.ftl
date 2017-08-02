<#include "/layout/inner_layout.ftl" />

<@sellContent title="" 
	js=[
	'/static/seller/js/item/item_import_list.js',
	'/static/seller/js/item/upload-file.js'
	] 
	css=[
      ] >
<!--
-->
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">商品导入列表页面</h3>
    </div>
    <div class="panel-body">
<form id="importLogForm"  action="${domain}/seller/item/import.htm"  method="post" >
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
                <a> <input  id="uploadFileBtn"  class="btn btn-primary btn-sm" type="button" value="上传" name="button"  /></a>
              </div>
              <div class="search_bar_btn" style="text-align:right;">
               	 <select class="select" name="status">
                    	<option value="0">-- 全部 --</option>
                    	<option value="1" <#if "${status}"=="1"> selected </#if>>导入成功</option>
                    	<option value="2" <#if "${status}"=="2"> selected </#if>>导入失败</option>
                 </select>
                 <a><input class="btn btn-primary btn-sm"  type="button" id="queryImportLogBtn"  value="查询" name="button"  /></a>
                <a> <input class="btn btn-primary btn-sm" type="button" value="导出"    id="exportFileBtn" /></a>
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
</form>
    <div id="contentShow">
        
    </div
</@sellContent>
