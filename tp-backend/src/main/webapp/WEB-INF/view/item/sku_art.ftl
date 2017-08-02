<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/form.js',
	'/statics/backend/js/item/item-sku-art-add.js']
	css=[] >
    <div> 
    <div>
            <!--location.href='selectSupplier.htm' -->
			<input type="button" id="inputFormSaveBtn" style="text-align:right;" value="新建" 
				 onclick="location.href='toAddSkuArtNumber.htm?skuId=${skuId}'" class="ext_btn ext_btn_submit m10">
	</div>
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">序号</th>
	                  <th width="100">通关渠道</th>
	                  <th width="150">商品备案号（货号）</th>
	                  <th width="50">HS编码</th> 
	                  <th width="100">HS第一单位</th>
	                  <th width="50">数量1</th>
	                  <th width="100">HS第二单位</th>
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
                      <td class="td_center">
                      		<a skuId="${s.skuId}" id="${s.id}" href="javascript:void(0);" class="editSkuArtBtn">[编辑]</a>
                      		<a skuId="${s.skuId}" id="${s.id}" href="javascript:void(0);" class="deleteSkuArtBtn">[删除]</a>
                      </td>
	             </tr>
	         </#list>
              </table>
	     </div>
	</div>
</@backend>