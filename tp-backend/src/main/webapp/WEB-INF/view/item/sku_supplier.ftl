<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/form.js',
	'/statics/backend/js/item/item-supplier-add.js']
	css=[] >
    <div> 
    <div>
            <!--location.href='selectSupplier.htm' -->
			<input type="button" id="inputFormSaveBtn" style="text-align:right;" value="新建"
				 onclick="location.href='selectSupplier.htm?addNewSupplierFlag=1&skuId=${skuId}'" class="ext_btn ext_btn_submit m10">
	</div>
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">ID</th>
	                  <th width="100">skuId</th>
	                  <th width="150">供应商名称</th>
	                  <th width="100">创建时间</th> 
	                  <th width="100">操作</th> 
                </tr>
            <#list skuSupplierList as s>               
                <tr class="tr" >
                	  <input type="hidden" name="supplierId" value="${s.supplierId}" />
		              <td class="td_center">${s.id}</td>
                      <td class="td_center">${s.skuId}</td>
                      <td class="td_center">${s.supplierName}</td>
                      <td class="td_center">${s.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                      <td class="td_center"><a skuId="${s.skuId}" id="${s.id}" href="javascript:void(0);" class="deleteSkuSupplierBtn">[删除]</a></td>
	             </tr>
	         </#list>
              </table>
	     </div>
	</div>
</@backend>