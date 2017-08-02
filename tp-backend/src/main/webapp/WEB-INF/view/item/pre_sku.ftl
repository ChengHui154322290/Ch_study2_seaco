<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/layer/layer.min.js',
    '/statics/backend/js/jquery.tools.js',
    '/statics/backend/js/form.js',
    '/statics/backend/js/item/item-update-status.js'] 
    
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/select2/css/common.css'] >

    <div >
    	 <input type="checkbox" name ="skuId"  id="checkAll" checked />
   		 <input type="button" id="batchUpdateStatus" style="text-align:right;" value=" <#if "${status}"=="0">批量下架</#if> <#if "${status}"=="1">批量上架</#if>" class="ext_btn ext_btn_submit m10">
	     <span id = "tipMsgId"></span>
	     <form action="#" method="post" class="jqtransform"   id='updateSkuListStatus'  > 
	     <table class="list_table"  border="0" cellpadding="0" cellspacing="0">
	     		<tr>
	     		  <td></td>
                  <td>商品名称</td>
                  <td>barcode</td>
                  <td>sku</td>
                  <td>状态</td>
                  <td>更新结果</td>
                </tr>
                <#list skuList as item>
                <tr>
                  <td>
                      <#if "${status}"=="0"> 
                      	<#if "${item.status}"=="0"><input type="checkbox"  value="${item.id}" disabled /></#if>
                      	<#if "${item.status}"=="1"><input type="checkbox" class="skuIdCheckbox" value="${item.id}" checked /></#if>
                      	<#if "${item.status}"=="2"><input type="checkbox" class="skuIdCheckbox" value="${item.id}" checked /></#if>
                      </#if> 
                      <#if "${status}"=="1"> 
                      	<#if "${item.status}"=="0"><input type="checkbox" class="skuIdCheckbox" value="${item.id}" checked /></#if>
                      	<#if "${item.status}"=="1"><input type="checkbox"  value="${item.id}" disabled /></#if>
                      	<#if "${item.status}"=="2"><input type="checkbox" class="skuIdCheckbox" value="${item.id}" checked /></#if>
                      </#if> 
                  </td>
                  <td>${item.detailName}</td>
                  <td>${item.barcode}</td>
                  <td>${item.sku}</td>
                  <td>
                    <#if "${item.status}"=="0">未上架</#if>
                    <#if "${item.status}"=="1">已上架</#if>
                    <#if "${item.status}"=="2">作废</#if>
                  </td>
                   <td id="skuId_${item.id}"></td>
                </tr>
                </#list>
         </table>
	     </form>            
    </div>
</@backend>