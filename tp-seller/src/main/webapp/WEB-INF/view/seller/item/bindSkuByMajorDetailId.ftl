<#include "/layout/inner_layout.ftl" />

<@sellContent title="首页" 
    js=[
    	'/static/scripts/item/sellerToBindSku.js'
    	]  
    css=[]>
    <script type="text/javascript" charset="utf-8">
    </script>
    <style>
    </style>
    
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">商品绑定</h3>
	    </div>
    <div class="panel-body">   
        <input type="hidden" id="majorDetailId" name="majorDetailId" value="${majorDetailId}"/>  
	  	   	 <table width="200px" height="200px;" class="table table-bordered">   
	  	   	    <tr>
		  	   	 	<td>
		  	   	 		* 市场价：
		  	   	 	</td>
	  	   	 	<td>
	  	   	 		<input id="price" class = "default" name="price" />
	  	   	 	</td>
	  	   	 	</tr>
	  	   	 	<tr>
                    <td  colspan="2" align="center">     
                        <button class="btn btn-default" id="submitSkuBindButtion">绑定</button>
                    </td>
                </tr>
	         </table>
    </div><#-- panel-body -->
</div>
</@sellContent>
