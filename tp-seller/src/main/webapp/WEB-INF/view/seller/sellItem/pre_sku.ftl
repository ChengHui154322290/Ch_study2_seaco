<#include "/layout/inner_layout.ftl" />
<script type="text/javascript" charset="utf-8" src="/static/scripts/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/common/jquery.form.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/component/jqueryui/js/jquery-ui-1.9.2.custom.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/common/bootstrap.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/web/main.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/component/date/WdatePicker.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/web/common/common.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/web/common/validator.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/layer/layer.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/layer/extend/layer.ext.js"></script>

<script type="text/javascript" charset="utf-8" src="/static/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/typeahead-bs2.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.ui.touch-punch.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.slimscroll.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.easy-pie-chart.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.sparkline.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/flot/jquery.flot.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/flot/jquery.flot.pie.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/flot/jquery.flot.resize.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/ace-elements.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/ace.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/ace-extra.min.js"></script>
<@sellContent title="" js=[	
    '/static/seller/js/layer/layer.min.js',
    '/static/seller/js/jquery.tools.js',
    '/static/seller/js/form.js',
    '/static/seller/js/item/item-update-status.js'] 
    
	css=['/static/seller/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/static/select2/css/common.css','/static/css/main.css'] >

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
                      	<#if "${item.status}"=="1"><input type="checkbox" class="skuIdCheckbox" name='skuIdCheckbox' value="${item.id}" checked /></#if>
                      	<#if "${item.status}"=="2"><input type="checkbox" class="skuIdCheckbox" name='skuIdCheckbox'  value="${item.id}" checked /></#if>
                      </#if> 
                      <#if "${status}"=="1"> 
                      	<#if "${item.status}"=="0"><input type="checkbox" class="skuIdCheckbox" name='skuIdCheckbox'  value="${item.id}" checked /></#if>
                      	<#if "${item.status}"=="1"><input type="checkbox"  value="${item.id}" disabled /></#if>
                      	<#if "${item.status}"=="2"><input type="checkbox" class="skuIdCheckbox" name='skuIdCheckbox'  value="${item.id}" checked /></#if>
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
</@sellContent>
