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
    '/static/seller/js/json2.js',
    '/static/seller/js/form.js',
    '/static/seller/js/item/item_copy.js'] 
	css=[
	'/static/seller/css/style.css' ] >

    <div >
	     <form action="" method="post" class="jqtransform"   id='selectSupplierForm'  > 
	     	<input type="hidden" id="detailId" value="${detailId}" />
            <div id="table" class="mt10">
             <div class="" style="margin-bottom:3px">
             	<span>只可以复制到</span><span class="requiredField">“0，无，无”</span><span>的prd</span>
             </div>
       		 <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
               	   <th width="15"><input type="checkbox" id="chkall" class="chkall"/> </th>
                   <th>PRDID</th>
                   <th>条形码</th>
                   <th>商品名称</th>
                   <th>图片（张）</th>
                   <th>PC商详</th>
                   <th>移动商详</th>
                </tr>
               <#if list?default([])?size !=0>       
                <#list list as l>
                 <tr class="supplierListTr">
                   <td width="15">
                   <#assign picSize=l.itemPicturesList?default([])?size>
                   <#assign descLength = l.itemDesc.description?trim?length>
                   <#assign descMobileLength = l.itemDescMobile.description?trim?length>
                   	 <#if picSize == 0 && descLength ==0 && descMobileLength==0>
                   	 	<input type="checkbox" name="detailId" value ="${l.itemDetail.id}"/>
                   	 <#else>
                   		 <input type="checkbox" name="detailId1" value ="${l.itemDetail.id}" disabled />
                   	 </#if>
                   	 
                    </td>
                    <td >${l.itemDetail.prdid}</td>
                    <td >${l.itemDetail.barcode }</td>
                    <td >${l.itemDetail.mainTitle}</td>
                    <td >${l.itemPicturesList?default([])?size}</td>
                    <td ><#if l.itemDesc.description?trim?length gt 0>有 <#else>无</#if></td>
                    <td ><#if l.itemDescMobile.description?trim?length gt 0>有 <#else>无</#if></td>
                 </tr>
                </#list>
                </#if>
                
              </table>
       	 </div>
       	 <div class="tc">
       	 	<input type="checkbox" id="chkall" class="chkall"/>全选
			<input type="button" id="copyPrdBtn" style="text-align:right;" value="复制" class="ext_btn ext_btn_submit m10"  <#if list?default([])?size =0>disabled</#if> />
	 	 </div>
	     </form>            
	   </div>	
    </div>
</@sellContent>