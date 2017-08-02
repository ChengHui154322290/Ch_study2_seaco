<#include "/common/common.ftl"/>
<@backend title="入库明细查询" js=[
] css=[] >
	<div id="forms" class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">（标杆）入库明细查询</b></div>
            <div class="box_center">
              <form class="jqtransform" method="post" id="orderCodeSubmit" action="${domain}/storage/bml/inputorder/list.htm">
               <table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0">
                 <tr>
                 <td class="td_right">采购订单号：</td>
                  <td class=""> 
                    <input type="text" name="orderCode" class="input-text lh30" size="40" value='${orderCode}'/>
                    <input class="btn btn82 btn_search" type="submit"    id="searthAtt" value="查询"  >
                  </td>
                </tr>
                <#if errorMsg??>
                <tr>
                	<td colspan="4" style="color:red;" class="tc">${errorMsg}</td>
                </tr>
                </#if>
               </table>
  				 <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">SKU</th>
	                  <th width="100">入库类型</th>
	                  <th width="100">入库时间</th>
	                  <th width="100">入库预计数量</th> 
	                  <th width="100">入库数量</th>             
                </tr>
			<#if asNsResults??&&asNsResults?size gt 0>
	          <#list asNsResults as result>       
		          <#list result.details as item>    
	                <tr class="tr" >
			              <td class="td_center">${item.skuCode}</td>
			              <td class="td_center">${result.typeDesc}</td>
			              <td class="td_center">${item.receivedTime}</td>
			              <td class="td_center">${item.expectedQty}</td>
			              <td class="td_center">${item.receivedQty}</td>                     
		             </tr>
		           </#list>
	            </#list>
	         <#else>
	         	<tr class="tc">
	         		<td class="td_center" colspan="5">暂无</td>
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