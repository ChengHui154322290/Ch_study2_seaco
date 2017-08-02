<#include "/common/common.ftl"/>
<@backend title="出库明细查询" js=[
] css=[] >


	<div id="forms" class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">（标杆）出库明细查询</b></div>
            <div class="box_center">
              <form class="jqtransform" method="post" id="orderCodeSubmit" action="${domain}/storage/bml/ooorderdetail/list.htm">
               <table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0">
                 <tr>
                 <td class="td_right">订单号：</td>
                  <td class=""> 
                    <input type="text" id="sku" name="orderCode" class="input-text lh30" size="40" value='${orderCode}'/>
                  </td>
                  <td class="td_right">
                  	订单类型
                  </td>
                  <td>
                  	<div class="select_border"> 
                        <div class="select_containers "> 
		                    <select name="orderType" class="select">
		                    	<option value="CM" <#if orderType=='CM'>selected</#if>>
		                    		销售出库订单
		                    	</option>
		                    	<option value="TT" <#if orderType=='TT'>selected</#if>>
		                    		采购退货出库订单
		                    	</option>
		                    </select>
                    	</div>
                    </div>
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
			                	  <th width="100">序号</th>
			                      <th width="100">订单号</th>
				                  <th width="100">订单金额</th>
				                  <th width="100">承运人ID</th>
				                  <th width="100">SKU</th> 
				                  <th width="100">发货数量</th>             
				                  <th width="100">明细金额</th>                       
			                </tr>
			                <#if ordersResults??&&ordersResults?size gt 0>
			                <#list ordersResults as result>
			                <tr class="tr" >
			                	  <td class="td_center">${result_index+1}</td>
					              <td class="td_center">${result.custmorOrderNo2}</td>
					              <td class="td_center">${result.orderPrice}</td>
					              <td class="td_center">${result.carrierID}</td>
					              <td class="td_center" colspan="3">
					              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
					              	<#list result.list as detail>
					              		<tr>
					              			<td class="td_center">${detail.skuCode}</td>
					              			<td class="td_center">${detail.qtyShipped}</td>
					              			<td class="td_center">${detail.price}</td>
					              		</tr>
					              	</#list>
					              </table>
					              </td>                     
				             </tr>
				             </#list>
				             <#else>
				             	<tr class="tr" >
					              <td class="td_center" colspan="6">暂无</td>
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