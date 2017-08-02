<#include "/common/common.ftl"/>
<@backend title="订单状态查询" js=[] 
css=[] >

    <div id="forms" class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">（标杆）订单状态查询</b></div>
            <div class="box_center">
              <form class="jqtransform" method="post" id="orderCodeSubmit" action="${domain}/storage/bml/ooorder-status/search.htm">
               <table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0">
                 <tr>
                  <td class="td_right">订单号：</td>
                  <td class=""> 
                    <input type="text" name="orderCode" class="input-text lh30" size="40" value="${orderCode}"/>
                    <div class="select_border"> 
                        <div class="select_containers "> 
                    <select name="orderType" class="select">
                    	<option value="CM" <#if orderType=='CM'>selected</#if>>
                    		销售出库订单
                    	</option>
                    	<option value="TT" <#if orderType=='TT'>selected</#if>>
                    		采购退货出库订单
                    	</option>
                    	<option value="FG" <#if orderType=='FG'>selected</#if>>
                    		采购入库预约单
                    	</option>
                    </select>
                    </div>
                    </div>
                     <input class="btn btn82 btn_search" type="submit"    id="searthAtt" value="查询"  >
                  </td>
                </tr>
                 <tr>
                 	 <td class="td_right">订单号：</td>
                     <td>${orderCode!("请输入订单号进行查询")}
                 </tr>
                 <tr>
                     <td class="td_right">订单状态：</td>
                     <td>
                     	<#if resultMessage??&&resultMessage.desc??>${resultMessage.desc}</#if>
                     	<#if resultMessage??&&resultMessage.message??>${resultMessage.message}</#if>
                     </td>       
                 </tr>
               </table>
               </form>
            </div>
          </div>
        </div>
     </div>
</@backend>