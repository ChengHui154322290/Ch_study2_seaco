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
<form id="importLogForm" action="importNew.htm" method="post" >
	  <input type="hidden" name="logId" id="logId" value="${importLog.id}" />
	  <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top">
             <b class="pl15">商品资料导入</b>

            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
           	  
              <div class="search_bar_btn" style="text-align:right;">
               	 <select class="select" name="status">
                    	<option value="">-- 全部 --</option>
                    	<option value="0" <#if "${status}"=="0"> selected </#if>>导入成功</option>
                    	<option value="1" <#if "${status}"=="1"> selected </#if>>导入失败</option>
                 </select>
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search"  type="button" id="queryImportLogBtn"  value="查询" name="button" onclick="$('#importLogForm').attr('action','importNew.htm');$('#importLogForm').submit();" /></a>
                 <input  id="uploadFileBtnNew"  class="btn btn82 btn_export" type="button" value="上传" name="button"  />
                <!-- <input class="btn btn82 btn_export" type="button" value="导出"    id="exportFileBtn" /> -->
              </div>
              <div class="mt10" > 
    			  <span>总数量：${importLog.sumCount}</span><br/>
    			  <span>导入成功：${importLog.successCount}</span><br/>
    			  <span>导入失败：${importLog.failCount}  </span>
    			  <!--
    			  <span>状态：<#if importLog.status==1> 等待处理</#if> <#if importLog.status==2> 正在处理</#if> <#if importLog.status==4> 处理完成 </#if> <#if importLog.status==5>处理失败,请重新上传模板</#if> </span>
    			  -->
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
           	  <th width="60">原因</th>
           	  <th width="60">id</th>
           	  <th width="60">SKU编号</th>
           	  <th width="60">品牌名称</th>
           	  <th width="60">品牌故事</th>
           	  <th width="60">商品名称</th>
           	  <th width="300">商品详情描述</th>
           	  <th width="60">材质</th>
           	  <th width="60">特殊说明</th>
           	  
            </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as l>
             <tr>
               <td width="60">
               		<#if l.status == 0>
               			导入成功
               		</#if>
               		<#if l.status == 1>
               			导入失败
               		</#if>
               </td>
               <td width="60">${l.opMessage}</td>
               <td width="60">${l.detailId}</td>
               <td width="60">${l.sku}</td>
               <td width="60">${l.brandName}</td>
               <td width="60">${l.brandStory}</td>
               <td width="60">${l.itemTitle}</td>
               <td width="300">${l.itemDetailDecs}</td>
               <td width="60">${l.material}</td>
               <td width="60">${l.specialInstructions}</td>
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
