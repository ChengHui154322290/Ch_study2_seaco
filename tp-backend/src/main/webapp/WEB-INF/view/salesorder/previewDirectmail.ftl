<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/layer/layer.min.js',
    '/statics/backend/js/jquery.tools.js',
    '/statics/backend/js/form.js',
    '/statics/backend/js/item/item-update-status.js',
	'/statics/backend/js/salesorder/clearance.js'    
] 
    
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/select2/css/common.css'] >
	<form action="#" method="post" class="jqtransform"  id='directMailDeliveryForm'>
	<div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
          	<div class="box_top"><b class="pl15">直邮订单批量报关</b>
            </div>
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0"> 
              	<tr>
              		<td>航班号：</td>
              		<td><input type="text" name="voyageNo" id="voyageNo" value="${pgInfo.voyageNo}"  class="input-text lh25" size="40"></td>
              	</tr>  
              	<tr>
              		<td>总提运单号：</td>
              		<td><input type="text" name="billNo" id="billNo" value="${pgInfo.billNo}"  class="input-text lh25" size="40"></td>
              	</tr>
              	<tr>
              		<td>运输工具编号：</td>
              		<td><input type="text" name="trafNo" id="trafNo" value="${pgInfo.trafNo}"  class="input-text lh25" size="40"></td>
              	</tr>      
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;"> 
              	<a href="javascript:void(0);">
                  <input class="btn btn82 btn_search " type="button" value="提交" name="button"	onclick="dmBatchDelivery()"/>
                </a>             
              </div>
            </div>
          </div>
        </div>
    </div>     	     
	   <div id="table" class="mt10">
    	 <div class="box span10 oh">
	    	<table class="list_table"  border="0" cellpadding="0" cellspacing="0">
	     		<tr>
	     		  <th> </th>
                  <th>订单编号</th>
                  <th>申报海关</th>
                  <th>物流公司</th>
                  <th>进口类型</th>
                  <th>提运单号</th>
                  <th>航班号</th>
                  <th>运输工具编号</th>
                </tr>
                <#if pgInfoList??>
	                <#list pgInfoList as pi>
	                <tr>
	                	<td><input type="checkbox" class="pgOrderCodeCheckbox" value="${pi.orderCode}" <#if pi.importType==0>checked<#else>disabled</#if>/></td>
	                	<td>${pi.orderCode}</td>
	                	<td>${pi.declareCustoms}</td>
	                	<td>${pi.companyName}</td>
	                	<td><#if pi.importType==0>直邮<#else>保税</#if></td>
	                	<td>${pi.billNo}</td>
	                	<td>${pi.voyageNo}</td>
	                	<td>${pi.trafNo}</td>
	                </tr>
	                </#list>
				</#if>
         </table>
	     </form>            
    </div>
</@backend>