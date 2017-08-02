<#include "/common/common.ftl"/> 

<@backend title="报价单导入" 
	js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/supplier/js/web/quotation_upload_file.js'
	] 
	css=[
	'/statics/backend/css/style.css' ] >
<!--
-->
<form id="quotationImportForm" action= "${domain}/supplier/quotationImport.htm" method="post" >
	  <input type="hidden" name="logId" id="logId" value="${importLog.id}" />
	  <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top">
             <b class="pl15">报价单导入</b>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
				<div  style="text-align:right; float:left">
					<input  id="uploadFileBtn"  class="btn btn82 btn_export" type="button" value="上传" name="button"  />

				</div>
				<div  style="text-align:right; float:left">
					<input type="button" name="button" id="quotationImportRefresh" class="btn btn82 btn_search" value="刷新">
				</div>


              <div class="mt10" > 
    			  <span>总数量：${sumcount}</span>
    			  <span>导入成功：${successcount}</span>
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
              <th width="60">报价单索引</th>
              <th width="300">错误信息</th>
           	  <th width="60">报价单名称</th>           	  
              <th width="60">商家ID</th>
           	  <th width="60">合同ID</th>           	  
           	  <th width="60">合同类型</th>	  
            </tr>
            <#if fail_quotationinfo?default([])?size !=0>
            <#list fail_quotationinfo as l>
             <tr>
               <td width="60">${l.quotationInfoIndex}</td>
               <td width="300">${l.excelOpmessage}</td>
               <td width="60">${l.quotationName}</td>
               <td width="60">${l.supplierId}</td>                             
               <td width="60">${l.contractId}</td>                             
               <td width="60">${l.contractTypeName}</td>                             
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
