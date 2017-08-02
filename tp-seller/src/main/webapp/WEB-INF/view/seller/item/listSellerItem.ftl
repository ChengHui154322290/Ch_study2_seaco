<#include "/layout/inner_layout.ftl" />

<@sellContent title="首页" 
    js=[ '/static/scripts/item/listSellerItem.js']  
    css=[]>
    <script type="text/javascript" charset="utf-8">
    </script>
    <style>
    </style>
    
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">商品查询</h3>
	    </div>
    <div class="panel-body">
        <form class="form-inline" role="form" id="searchFormId">
	      <table width="100%" height="150px;">   
	      		<tr>
					<td>SPU：</td><td><input type="text" name="spu" /></td>
					<td>PRDID：</td><td><input type="text" name="prdid" /></td>
	      			<td>sku：</td><td><input type="text" name="sku" /></td>
	      			<td>条形码：</td><td><input type="text" name="barCode" /></td>
	      			<td>发布状态：</td>
	      			<td>
	      				<select name="status" >
	      					<option value="">全部</option>
	      					<option value="0">未上架</option>
	      					<option value="1">上架</option>
	      					<option value="2">作废</option>
	      				</select>
	      			</td>
	      		<tr>
	      		
	      	    <tr>
					<td>名称：</td><td><input type="text" name="itemName" /></td>
					<td>单位：</td><td><input type="text" name="unitName" /></td> 
	      			<td>品牌：</td><td><input type="text" name="brandName" /></td>
	      			<td>审核状态：</td>
	      			<td>
	      				<select name="auditStatus" >
	      					<option value="">全部</option>
	      					<option value="A">已上架</option>
	      					<option value="E">编辑中</option>
	      					<option value="S">审核中</option>
	      					<option value="R">已驳回</option>
	      					<option value="D">已作废</option>
	      				</select>
	      			</td>
	      		<tr>
	      		<tr>
                    <td colspan="6" align="center"> 
                      <!--   <button class="btn btn-default" id="resetButton">重置</button> -->
                        <button class="btn btn-primary btn-sm" id="searchForm">查询</button>
                    </td>
                </tr>               
	      </table>
       </form>      
       
       <div id="listContentShow">
       
       </div>
    </div><#-- panel-body -->
</div>
</@sellContent>
