<#include "/common/common.ftl"/>
<@backend title="出库报检列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/wms/stockoutdecl.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >
	<!-- 查询条件开始 -->
<form method="post" action="${domain}/wms/stockin/list.htm" id="stockInForm">	
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">出库报检数据详情</b></div>
          </div>
        </div>
    </div>
    <!-- 查询条件结束 -->
    
    <!-- 列表开始 -->
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" id="list_table">
                <tr>
           	   <th width="20">序号</th>
           	   <th width="60">商品编号</th>
           	   <th width="60">报检单号</th>
               <th width="80">载货单号</th>
                </tr>
            <#if declItemDoList?default([])?size !=0>
            <#list declItemDoList as declItemDo>
	             <tr>
	               <td width="20" align="center">${declItemDo_index+1}</td>
	               <td width="60">${declItemDo.sku?if_exists}</td>
	               <td width="60">${declItemDo.declNo?if_exists}</td>
	               <td width="80">${declItemDo.manifestNo?if_exists}</td>
	            </tr>
            </#list>
            </#if>
              </table>
	     </div>
	</div><!-- 列表结束 -->
</form>
</@backend>