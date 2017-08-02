<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/storage/js/inputorder.js'
] css=[] >
   <span>标杆库存查询</span>
	 <form class="jqtransform" method="post" id="queryAttForm" action="${domain}/storage/benchmarkInventory/list.htm">
    <div >
	   <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>SKU:</td>
                  <td><input type="text" name="sku" class="input-text lh25" size="20" value='${sku}'></td>
                </tr>
              </table>
	   </div>
	  <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <input class="btn btn82 btn_search" type="submit"   id="searthAtt" value="查询" >
              </div>
        </div>
    </div>
    
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                      <th width="100">SKU</th>
	                  <th width="100">仓库批号</th>
	                  <th width="100">库存总数</th>
	                  <th width="100">库存冻结数</th> 
                </tr>
                <tr class="tr" >
		              <td class="td_center">${sku}</td>
		              <td class="td_center">${lotNumber}</td>
		              <td class="td_center">${qty}</td>
		              <td class="td_center">${qtyOnHold}</td>
	             </tr>
              </table>
	     </div>
	</div>
   </form>
   </@backend>