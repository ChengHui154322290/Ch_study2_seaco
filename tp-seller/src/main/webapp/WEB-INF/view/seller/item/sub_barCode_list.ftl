<table class="table table-bordered table-striped table-hover">
    <thead>
		  <tr>
	       	   <th align="center" width="20%">图片</th>
	       	   <th align="center" width="16%">条形码</th>
	       	   <th align="center" width="16%">SKU</th>
	       	   <th align="center" width="16%">名称</th>
	       	   <th align="center" width="16%">状态 </th>
	       	   <th align="center" width="16%">操作</th>
	        </tr>
    </thead>
    <tbody>
       	 <tr>
            <td><#if dataDto.mainPicture ?? ><img src="${dataDto.mainPicture}" /></#if></td> 
            <td align="center"  valign="middle">${dataDto.barCode}</td>
            <td align="center"  valign="middle">${dataDto.sku}</td>
            <td align="center"  valign="middle">${dataDto.name}</td>
            
            <#if dataDto.detailId ?? &&  dataDto.sku==null>   
            <td align="center"  valign="middle"><a href="javascript:void(0)" onclick="bindSku('${dataDto.detailId}')">绑定</a></td> 
            <td align="center"  valign="middle"> 
		            	<a href="javascript:void(0)"  onclick="showDetail('${(dataDto.detailId)!}')">查看明细</a>
		     </td>	
		    <#elseif dataDto.detailId  == null> 
		   		 <td align="center"  valign="middle"></td>  
	    		<td align="center"  valign="middle">
	            	<a href="javascript:void(0)" onclick="addNewSPU('${dataDto.barCode}')">新增商品</a>   
	            </td>
            <#else>
		        	<#if dataDto.status =='0' >   
		        	<td align="center"  valign="middle">未上架</td>  
		        	<#elseif dataDto.status =="1">
		        	<td align="center"  valign="middle">已上架</td> 
		        	<#elseif dataDto.status =="2" >
			        	<td align="center"  valign="middle">作废</td>  
		        	 </#if>   
		        	 <td align="center"  valign="middle">  
		            	<a href="javascript:void(0)" onclick="showDetail('${(dataDto.detailId)!}')">查看明细</a>
		            </td>
            </#if>
        </tr>
    </tbody>
</table>
