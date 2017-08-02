<#include "/common/common.ftl"/>
<@backend title="已销售数据" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/item/item-detailsales.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >
<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/item/itemDetailSales/list.htm">
   
  <div id="search_bar" class="mt10">
   
    	<div class="box"> 
    	
    	
    	    <div class="box_top"><b class="pl15">已囤数据</b>  
            </div>
	       <div class="box_center pt10 pb10">
                <tr>
                  <td >条形码：</td>
                  <td><input type="text" name="barcode" class="input-text lh25" size="20" value='${ado.barcode}'></td>
                  <td >PRDID：</td>
                  <td><input type="text" name="prdid" class="input-text lh25" size="20" value='${ado.prdid}'></td>
                </tr>
           </div>
        </div>  
	</div>
	  <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
               <div class="search_bar_btn" style="text-align:left;">  
	            	 <a href="javascript:void(0);"><input class="btn btn82 btn_res" id="reset" type="button" value="重置" name="button" /></a>
	                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" id="searthAtt" type="button" value="查询" name="button" /></a>
	      <!--           <a href="javascript:void(0);"><input class="ext_btn ext_btn_submit m10 exportButton"  type="button" value="导出" name="button" /></a>
	                 <a href="javascript:void(0);"><input class="ext_btn ext_btn_submit m10 exportButtonAll"  type="button" value="导出所有" name="button" /></a> -->
	                 <a href="javascript:void(0);"><input class="ext_btn ext_btn_submit m10  barcodecopy"  type="button" value="粘贴导入（按条码）" name="button" /></a>  
	                 <a href="javascript:void(0);"><input class="ext_btn ext_btn_submit m10  prdidcopy"  type="button" value="粘贴导入（按PRDID）" name="button" /></a>  
               </div>
      </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">
                    		<a href="javascript:void(0);" class="checkAll">全选</a>
                    		<a href="javascript:void(0);" class="disCheck">反选</a>
                      </th>
	                  <th width="100">条形码</th>
	                  <th width="100">PRDID</th>
	                  <th width="200">商品名称</th>       
                      <th width="100">历史基数</th>
	                  <th width="100">下单数</th>
	                  <th width="100">操作</th>
                </tr>
            <#list listSales.getRows() as zz>               
                <tr class="tr" >
		              <td class="td_center">
		              	<input type="checkbox" name="exportIds" value="${zz.id}" />  
		              </td>
		              <td class="td_center">${zz.barcode}</td>
		              <td class="td_center">${zz.prdid}</td>                     
		              <td class="td_center">${zz.mainTitle}</td>	
		              <td class="td_center">${zz.defaultSalesCount}</td>	
		              <td class="td_center">${zz.relSalesCount}</td>	
		              <td class="td_center">
		                <a href="javascript:void(0);" class="viewDetail"  viewid="${zz.id}">查看</a>
		               	<a href="javascript:void(0);" class="updateDetail" viewid="${zz.id}">修改</a>  
		              </td>	
	             </tr>
	            </#list>
              </table>
	     </div>
	     <@pager pagination=listSales  formId="queryAttForm"  />
	</div>
   </form>
</@backend>