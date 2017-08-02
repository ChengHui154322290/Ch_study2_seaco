<#include "/common/common.ftl"/>
<@backend title="入区信息列表" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/form.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/wms/stockin.js'
] css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >
	<!-- 查询条件开始 -->
<form method="post" action="${domain}/wms/stockin/doImportStockIn.htm" id="stockInFileForm" name="excelImportForm" enctype="multipart/form-data" >
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_center pt10 pb10">
	       	<input type="hidden" name="stockasnId" id="stockasnId" value="${stockasnId}" />
	       	<table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  	<td>导入TOKEN：</td>
                  	<td><span id="uploadToken" name="uploadToken" size="20" >${uploadToken}</span>
                  	<input type="hidden" name="uploadToken" value="${uploadToken}" />
                  	</td>  
	            </tr>
	            <tr>	
                  	<td>选择文件：</td>
                  	<td><input type="file" id="stockInFile" name="stockInFile" class="input-text lh25" size="20" value=''>
                  		<input type="hidden" id="fileName" name="fileName" >
                  	</td>
                  	<td><a href="${domain}/wms/stockin/doExport.htm">[下载导入模板]</a></td>
                </tr>
            </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
              	 <a href="javascript:void(0);"><input id="doImportStockInBtn" class="btn btn82 btn_export" type="button" value="导入" name="button"  /></a>
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
	<@pager  pagination=page  formId="stockInForm" /> 
</form>
</@backend>