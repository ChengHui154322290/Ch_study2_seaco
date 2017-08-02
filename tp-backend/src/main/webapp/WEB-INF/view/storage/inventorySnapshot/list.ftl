<#include "/common/common.ftl"/>
<@backend title="库存快照" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/storage/js/inventorySnapshot.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >

	 <form class="jqtransform" method="post" id="queryAttForm" action="${domain}/storage/inventory-snapshot/list.htm">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td >SKU:</td>
                  <td><input type="text" name="sku" class="input-text lh25" size="20" value='${ado.sku}'></td>
                  <td>创建日期</td>
                  <td colspan="3"> 
                  	<input type="text" name="createBeginTime" id="createBeginTime"  readonly value='${ado.createBeginTime}' class="input-text lh25" size="20">
                  	<span>到</span>
                  	<input type="text" name="createEndTime" id="createEndTime"    readonly  value='${ado.createEndTime}' class="input-text lh25" size="20">
                  </td>
                </tr>
              </table>
	   </div>
	  <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
               <div class="search_bar_btn" style="text-align:right;">
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" id="searthAtt" type="button" value="查询" name="button" /></a>
                 <a href="javascript:void(0);"><input class="btn btn82 btn_export exportButton"  type="button" value="导出" name="button" /></a>
               </div>
      </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">序号</th>
	                  <th width="100">SKU</th>
	                  <th width="100">区域</th>
	                  <th width="150">仓库</th>       
                      <th width="100">现货库存</th>
	                  <th width="100">占用库存</th>
	                  <th width="100">创建时间</th>
                </tr>
            <#list listSnap.rows as zz>               
                <tr class="tr" >
		              <td class="td_center">${zz.id}</td>
		              <td class="td_center">${zz.sku}</td>
		              <td class="td_center">${zz.address}</td>                     
		              <td class="td_center">${zz.wareHouseName}</td>	
		              <td class="td_center">${zz.inventory}</td>	
		              <td class="td_center">${zz.occupy}</td>	
		               <td class="td_center">${zz.snapTime?string("yyyy-MM-dd HH:mm:ss")}</td>	    
	             </tr>
	            </#list>
              </table>
	     </div>
	</div>
       <@pager  pagination=listSnap  formId="queryAttForm"  />  
   </form>
</@backend>