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
	'/static/seller/js/form.js',
	'/static/seller/js/item/item-sku-art-add.js']
	css=[
	'/static/seller/css/style.css','/static/select2/css/select2.css',
	'/static/select2/css/common.css',
	'/static/themes/msgbox.css',
	'/static/seller/css/style.css',
	'/static/seller/css/main.css',
	'/seller/css/main.css'
	] >
    <div> 
    <div>
            <!--location.href='selectSupplier.htm' -->
			<input type="button" id="inputFormSaveBtn" style="text-align:right;" value="新建" 
				 onclick="location.href='/seller/item/toAddSkuArtNumber.htm?skuId=${skuId}'" class="ext_btn ext_btn_submit m10">
	</div>
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">序号</th>
	                  <th width="100">通关渠道</th>
	                  <th width="150">商品备案号（货号）</th>
	                  <th width="50">HS编码</th> 
	                  <th width="100">备案第一单位</th>
	                  <th width="50">数量1</th>
	                  <th width="100">备案第二单位</th>
	                  <th width="100">数量2</th>
	                  <th width="80">备案价格</th>
	                  <th width="100">国检备案号</th>
	                  <th width="100">商品报关名称</th> 
	                  <th width="80">商品特征</th> 
	                  <th width="50">操作</th>  
                </tr>
            <#list skuArtList as s>               
                <tr class="tr" >
                	  <input type="hidden" name="skuArtId" value="${s.id}" />
                	  <td class="td_center">${s.id}</td>
		              <td class="td_center">
		              	 <#list channels as val>  
				  	 			<#if  val.id == s.bondedArea>
							 		 ${val.name}
							   </#if>  
						</#list>
		              </td>
                      <td class="td_center">${s.articleNumber}</td>
                      <td class="td_center">${s.hsCode}</td>
                      <td class="td_center">${s.itemFirstUnit}</td>
                      <td class="td_center">${s.itemFirstUnitCount}</td>
                      <td class="td_center">${s.itemSecondUnit}</td>
                      <td class="td_center">${s.itemSecondUnitCount}</td>
                      <td class="td_center">${s.itemPrice}</td>
                      <td class="td_center">${s.itemRecordNo}</td>
                      <td class="td_center">${s.itemDeclareName}</td>
                      <td class="td_center">${s.itemFeature}</td>
                      <td class="td_center"><a skuId="${s.skuId}" id="${s.id}" href="javascript:void(0);" class="deleteSkuArtBtn">[删除]</a></td>
	             </tr>
	         </#list>
              </table>
	     </div>
	</div>
</@sellContent>