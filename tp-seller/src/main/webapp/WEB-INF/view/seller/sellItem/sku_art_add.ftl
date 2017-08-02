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
	'/static/select2/js/select2.js',
	'/static/select2/js/select2Util.js',
	'/static/select2/js/select2_locale_zh-CN.js',
	'/static/seller/js/item/item-select2.js',
	'/static/seller/js/item/item-sku-art-add.js']
	css=['/static/seller/css/style.css','/static/select2/css/select2.css',
	'/static/select2/css/common.css',
	'/static/themes/msgbox.css',
	'/static/seller/css/style.css',
	'/static/seller/css/main.css'] >
    <div> 
   <form action="addSkuArtNumber.htm" method="post" class="jqtransform"   id='addSkuArtNumberForm'  > 
   	 <input type="hidden" name="skuId" value="${skuId}" /> 
     <table class="form_table" border="0" cellpadding="0" cellspacing="0">
      <tr>
      	<td class="td_right requiredField">*商品备案号:</td>
		<td ><input type="text" class="input-text lh30" size="25"	name="articleNumber"  id="articleNumber" maxlength=60 onMouseOver="this.title=this.value" /></td>
		
      </tr>
      <tr>
     	 <td class="td_right requiredField">通关渠道:
		</td>
		 <td>
			<select name="bondedArea"  id="bondedArea" class="select" >
						<option value="" >请选择</option>
						<#list channels as t>
							<option value="${t.id}">${t.name}</option>  
						</#list>
			 </select> 
		</td>
      </tr>  
       <tr>
       		<td class="td_right requiredField">*HS编码:</td>
      		 <td ><input type="text" class="input-text lh30" size="100"	name="hsCode"  id="hsCode" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr>
       <tr>
     	 <td class="td_right requiredField">*第一单位:</td>
		 <td>
			<select name="itemFirstUnitCode"  id="itemFirstUnitCode" class="select2" style="width:150px;"></select> 
		</td>
      </tr> 
      <tr>
       		<td class="td_right requiredField">*第一单位对应数量:</td>
      		 <td ><input type="text" class="input-text lh30" size="100"	name="itemFirstUnitCount"  id="itemFirstUnitCount" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr>
      <tr>
     	 <td class="td_right">第二单位:</td>
		 <td>
			<select name="itemSecondUnitCode"  id="itemSecondUnitCode" class="select2" style="width:150px;"></select> 
		</td>
      </tr>
       <tr>
       		<td class="td_right requiredField">第二单位对应数量:</td>
      		 <td ><input type="text" class="input-text lh30" size="100"	name="itemSecondUnitCount"  id="itemSecondUnitCount" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr>     
      <tr>
       		<td class="td_right requiredField">备案价:</td>
      		<td ><input type="text" class="input-text lh30" size="100"	name="itemPrice"  id="itemPrice" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr>
      <tr>
       		<td class="td_right requiredField">国检备案号:</td>
      		<td ><input type="text" class="input-text lh30" size="100"	name="itemRecordNo"  id="itemRecordNo" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr>
       <tr>
       		<td class="td_right requiredField">商品报关名称:</td>
      		 <td ><input type="text" class="input-text lh30" size="100"	name="itemDeclareName"  id="itemDeclareName" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr> 
        <tr>
       		<td class="td_right">商品特征:</td>
      		 <td ><input type="text" class="input-text lh30" size="100"	name="itemFeature"  id="itemFeature" maxlength=100  onMouseOver="this.title=this.value" /></td>
       </tr> 
      	<td class="td_right"> <a href="#"><input type="button" name="button" value="保存" id="saveSkuArtNumberBtn" class="btn btn82 btn_search"></a></td>
     </table>
   
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                  <tr>
	                  <th width="100">通关渠道</th>
	                  <th width="150">商品备案号</th>
	                  <th width="100">HS编码</th> 
	                  <th width="100">备案第一单位</th>
	                  <th width="80">数量1</th>
	                  <th width="100">备案第二单位</th>
					  <th width="80">数量2</th>
	                  <th width="100">备案价格</th>
	                  <th width="100">国检备案号</th>	                  
	                  <th width="100">商品报关名称</th> 
	                  <th width="100">商品特征</th> 
                </tr>
            <#list skuArtList as s>               
                <tr class="tr" >
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
	             </tr>
	       	  </#list>    
              </table>
	     </div>
	    </form>
	</div>
</@sellContent>