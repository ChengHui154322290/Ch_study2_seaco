<#include "/common/common.ftl"/>
<@backend title="入区信息列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/wms/stockoutdecl.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >
	<!-- 查询条件开始 -->
<form method="post" action="${domain}/wms/stockoutdecl/doImportStockOutDecl.htm" id="stockOutDeclFileForm" name="excelImportForm" enctype="multipart/form-data" >
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">出库报检管理-导入报检明细</b></div>
            <div class="box_center pt10 pb10">
	       	<table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  	<td>货主编码：</td>    
                  	<td><input type="text" id="storer" name="storer" class="input-text lh25" size="20" value='${storer}'></td>
                  	<td>跨境平台系统订单号：</td>
                  	<td><input type="text" id="externalNo" name="externalNo" class="input-text lh25" size="20" value='${externalNo}'></td>
                  	<td>外部订单号：</td>
                  	<td><input type="text" id="externalNo2" name="externalNo2" class="input-text lh25" size="20" value='${externalNo2}'></td>
                  	<td>选择文件：</td>
                  	<td><input type="file" id="stockOutDeclFile" name="stockOutDeclFile" class="input-text lh25" size="20" value=''></td>
                  	<td><a href="${domain}/wms/stockoutdecl/downStockInTemplate.htm">[下载导入模板]</a></td>
                </tr>
            </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
              	 <a href="javascript:void(0);"><input id="doImportStockOutDeclBtn" class="btn btn82 btn_export" type="button" value="导入" name="button"  /></a>
              </div>
            </div>
			<#if '${message?if_exists}' != ''>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
				<font color="red">${message?if_exists}</font>
            </div>
			</#if>
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
            <#if data?default([])?size !=0>
            <#list data as declItemDo>
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