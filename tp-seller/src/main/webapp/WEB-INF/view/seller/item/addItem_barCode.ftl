<#include "/layout/inner_layout.ftl" />

<@sellContent title="首页" 
    js=[ '/static/scripts/item/list_barCode.js']  
    css=[]>
    <script type="text/javascript" charset="utf-8">
    </script>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">商家-商品新增</h3>
    </div>
    <div class="panel-body">
        <form class="form-inline" role="form" id="searchFormId">
	      <table width="100%" height="100px;">   
	        <tr style="height:100px;">
	           <td style="width:250px;padding-left:100px;">商品码（条形码）：</td>
	           <td>
	           		<input type="text" name="barCode" class="form-control" style="width:200px;"  value="${barCode}">
	           		<input type="button" class="btn btn-primary btn-sm" name="searchButton" value="确定" id="searchForm">  
	           </td> 
	        </tr>
	      </table>
       </form>      
       <div id="listBarCodeContentShow">
       
       </div>
    </div><#-- panel-body -->
</div>
</@sellContent>
