 <#include "/common/common.ftl"/>
 <!doctype html>
 <html lang="zh-CN">
 <head>
   <meta charset="UTF-8">
   <link rel="stylesheet" href="${domain}/statics/backend/css/common.css">
   <link rel="stylesheet" href="${domain}/statics/backend/css/main.css">
   <link rel="stylesheet" href="${domain}/statics/backend/js/jqgrid/css/ui.jqgrid.css">
   <link rel="stylesheet" href="${domain}/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css">
   <script src="${domain}/statics/backend/js/jquery.min.js"></script>
   <script src="${domain}/statics/backend/js/swfupload/swfupload.js"></script>
   <script src="${domain}/statics/backend/js/swfupload/js/fileprogress.js"></script>
   <script src="${domain}/statics/backend/js/swfupload/js/handlers.js"></script>
   <script src="${domain}/statics/backend/js/swfupload/js/swfupload.queue.js"></script>
   <script src="${domain}/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js"></script>
   <script src="${domain}/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js"></script>
   <script src="${domain}/statics/upload.js"></script>
   <script src="${domain}/statics/backend/js/item/item_file_upload.js"></script>
   <script>var domain = "${domain}";</script>
 </head>
 <body>
 
 
 		<div style="padding:20px;">
 			<input type="text" id="search_key" /> <input type="button" id="search" value="search"/>
 		</div>
 
		<table id="demoTable"></table>
		<div id="item-pictures">
 		</div>
		<div class="fieldset flash" style="display:none" id="fsUploadProgress">
			<span class="legend"></span>
		</div>
		<div>
			<span id="spanButtonPlaceHolder"></span>
		</div>
 </body>
 </html>