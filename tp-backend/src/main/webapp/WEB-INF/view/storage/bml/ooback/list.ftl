<#include "/common/common.ftl"/>
<@backend title="发货信息查询" js=[
] css=[] >


<div id="forms" class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">（标杆）发货明细查询</b></div>
            <div class="box_center">
              <form class="jqtransform" method="post" id="orderCodeSubmit" action="${domain}/storage/bml/ooback/list.htm">
               <table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0">
                 <tr>
                 <td class="td_right">订单号：</td>
                  <td class=""> 
                    <input type="text" name="orderCode" class="input-text lh30" size="40" value='${orderCode}'/>
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
                      <th width="100">订单号</th>
	                  <th width="100">运单号</th>
	                  <th width="100">发货时间</th>
	                  <th width="100">物流公司名称</th> 
	                  <th width="100">物流公司代码</th>             
	                  <th width="100">仓库编号</th>             
	                  <th width="100">订单包裹重量</th>             
	                  <th width="100">sku</th>             
	                  <th width="100">sku数量</th>             
                </tr>
                <tr class="tr" >
		              <td class="td_center">${shipResults.orderNo}</td>
		              <td class="td_center">${shipResults.shipNo}</td>
		              <td class="td_center">${shipResults.shipTime}</td>
		              <td class="td_center">${shipResults.carrierID}</td>
		              <td class="td_center">${shipResults.carrierName}</td>                     
		              <td class="td_center">${shipResults.bgNo}</td>                     
		              <td class="td_center">${shipResults.weight}</td>                     
		              <td class="td_center" colspan="2">
							<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
						<#list shipResults.send as item>
							<tr>
								<td>
								${item.skuCode}
								</td>
								<td>
								${item.skuNum}
								</td>
							</tr>							
						</#list>		              
							</table>
		              </td>                     
	             </tr>
              </table>
	     </div>
	</div>
               </form>
            </div>
          </div>
        </div>
     </div>

	 
    
   
   </form>


</@backend>