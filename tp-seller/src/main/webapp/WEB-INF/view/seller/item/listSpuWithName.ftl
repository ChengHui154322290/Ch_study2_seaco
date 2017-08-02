<table class="table table-bordered table-striped table-hover">
    <thead>
		  <tr>
	       	   <th align="center" width="50%">商品名称</th>
	       	   <th align="center" width="30%">SPU</th>
	       	   <th align="center"  width="20%">操作</th>
	        </tr>
    </thead>
    <tbody> 
      <#if spuList ??>       
        <#list spuList as spu>
        <tr>
            <td>${(spu.spuName)!}</td>
            <td>${(spu.spu)!}</td>
            <td>
            	<a href="javascript:void(0)" onclick="showSpuInfo('${(spu.spu)!}')">选择</a>  
            </td>
        </tr>
        </#list>
        </#if>
    </tbody>
</table>
