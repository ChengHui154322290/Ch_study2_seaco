<#include "/common/common.ftl"/>
<@backend title="" js=[
] css=[] >

	<div id="forms" class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">（标杆）库存查询</b></div>
            <div class="box_center">
              <form class="jqtransform" method="post" id="orderCodeSubmit" action="${domain}/storage/bml/inventory/list.htm">
               <table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0">
                 <tr>
                  <td class="td_right">SKU：</td>
                  <td class=""> 
                    <input type="text" id="sku" name="skuCode" class="input-text lh30" size="40" value='${skuCode}'/>
                     <input class="btn btn82 btn_search" type="submit"    id="searthAtt" value="查询"  >
                  </td>
                </tr>
                <#if errorMsg??>
                <tr>
                	<td class="tc" style="color:red" colspan="2"> ${errorMsg} </td>
                </tr>
                </#if>
               </table>
                <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">SKU</th>
	                  <th width="100">仓库批号</th>
	                  <th width="100">库存总数</th>
	                  <th width="100">库存分配数</th>
	                  <th width="100">库存冻结数</th> 
                </tr>
                <#if  stockResults?? >
   						<#list stockResults as result>   
				            <tr class="tr" >
					              <td class="td_center">${result.sku}</td>
					              <td class="td_center">${result.lotNumber}</td>
					              <td class="td_center">${result.qty}</td>
					              <td class="td_center">${result.qtyAllocated}</td> 
					              <td class="td_center">${result.qtyOnHold}</td> 
				             </tr>
	           		 </#list>
				<#else>
				     <tr class="tr" >
				     	<td class="tc" colspan="5">暂无</td>
				   	 </tr>
				   </#if>
              </table>
	     </div>
	</div>
               </form>
            </div>
          </div>
        </div>
     </div>
</@backend>