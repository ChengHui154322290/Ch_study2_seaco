<#include "/common/common.ftl"/>
<#include "/coupon/common/page.ftl" />
<@backend title="库存快照" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/storage/js/inventory_adjust_list.js'  
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css' 
] >

<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/storage/inventoryAdjust/list.htm">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr  style="text-align:center;"> 
                  <td>SKU:</td>      
                  <td><input type="text" name="sku" class="input-text lh25" size="14" value='${ado.sku}'></td>   
                  <td >创建日期：</td>  
                  <td colspan="3">
                  	<input type="text" name="startDate" id="startDate" value='${ado.startDate}' class="input-text lh25" size="15" readonly />
                  	<span>到：</span>
                  	<input type="text" name="endDate"   id="endDate"  value='${ado.endDate}' class="input-text lh25" size="15" readonly />
                  </td> 
                </tr>
              </table>
	   </div>
	  <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
               <div class="search_bar_btn" style="text-align:right;">
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" id="searthAtt" type="button" value="查询" name="button" /></a>
                 <a href="javascript:void(0);"><input class="btn btn82 btn_export exportButton" id="searthAtt" type="button" value="导出" name="button" /></a>
                 <a href="javascript:void(0);"><input class="btn btn82 btn_add logAddButton" id="searthAtt" type="button" value="盘点" name="button" /></a>
              </div>
      </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">序号</th>
	                  <th width="100">SKU</th>
	                  <th width="100">仓库</th>
	                  <th width="100">类型</th>
	                  <th width="100">盘点数量</th>
	                  <th width="150">盘点时间</th>       
                      <th width="100">备注</th>
                </tr>
              <#list listVoLogs as log>               
                <tr class="tr" >
		              <td class="td_center">${log.id} </td>
		              <td class="td_center">${log.sku}</td>
		              <td class="td_center">${log.warehouseName}</td>
		              <td class="td_center">
		              <#if log.action == 1>
		              		盘盈
		              <#elseif log.action == 2> 
		              		盘亏
		              <#elseif log.action == 3> 
		              		初始化
		              </#if>
		              </td>
		              <td class="td_center">${log.quantity}</td>
		              <td class="td_center"><#if log.createDate??>${log.createDate?string("yyyy-MM-dd HH:mm:ss")}</#if></td>     
 					  <td class="td_center">${log.remark}</td>                   
	             </tr>
	            </#list>
	            
	               <#if  onData ?? >
				     <tr class="tr" >
				     	${onData}
				   	 </tr>
				   </#if> 
              </table>
	     </div>
	</div> 
     <@pager  pagination=listLogs  formId="queryAttForm"  />  
   </form>
</@backend>