<#include "/layout/inner_layout.ftl" />  

<@sellContent title="首页" 
 	 js=[
    	'/static/scripts/item/initalExistPrdInfo.js'
    	]  
    css=['/static/css/item-ul.css']>
    <style>
    </style>
    
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">新增PRDID</h3>
	    </div>
    <div class="panel-body">
        <form class="form-inline" role="form" id="searchFormId">
        
        
      	 <#if noData ??>
        	 	<table width="100%" height="200px;" class="table table-bordered">  
        	 		${noData}
        	 	</table>
        	 	<#else>
						<table width="100%" height="200px;" class="table table-bordered">   
					  	    
					  	  	  <b class="pl15">商品SPU信息</b>
					      		<tr>
									<td width="20%">SPU：</td>
									<td width="80%">
											<input type="text"  id="spu" value="${majorInfo.infoOpenDto.spu}" disabled/>
											<input type="hidden"  id="smallId" value="${majorInfo.infoOpenDto.categoryOpenDto.smallId}" disabled/>
									</td>
					      		</tr>
					      	<tr>
					      		<td width="20%">商品类别：</td>
					      		<td width="80%">
					      			大类：<input type="text" id="bigCateName" value="${majorInfo.infoOpenDto.categoryOpenDto.bigCateName}" disabled />
					      			中类：<input type="text" id="middCateName" value="${majorInfo.infoOpenDto.categoryOpenDto.middCateName}"  disabled />
					      			小类：<input type="text" id="smallCateName" value="${majorInfo.infoOpenDto.categoryOpenDto.smallCateName}"  disabled />
					      			<input id="smallId" type="hidden" value="${majorInfo.infoOpenDto.categoryOpenDto.smallId}"  />
					      		</td>
					      	</tr>
					      	
					      	<tr>
					      		<td width="20%">商品品牌：</td>
					      		<td width="80%">
						      			<input type="text" id="brandName"  value="${majorInfo.infoOpenDto.brandName}" disabled /> 
						      		    单位:<input type="text"  id="unitName" value="${majorInfo.infoOpenDto.unitName}" disabled />
					      		</td>
					      	</tr>
					      	<tr>
					      		<td width="20%">SPU名称：</td>
					      		<td >
						      			<input type="text"  id="mainTitle"  value="${majorInfo.infoOpenDto.spuName}" disabled />
					      		</td>
					      	</tr>
					      	<tr>
					      		<td width="20%">备注：</td>
					      		<td >
						      			 <input type="text" id="spuRemark"   value="${majorInfo.infoOpenDto.spuRemark}"  disabled  />
					      		</td>
					      	</tr>
					   </table>
					   
					   	
					    <table width="100%" height="100px;" class="table table-bordered">
							    	<tr>
							    			<td>新建规格信息：  
							    				<input type="button" class="btn btn-default" id="chooseSPec" onclick="addNewSpec();" value="规格选择" />
							    			</td>
							    	</tr>
					    </table>
					    
					    
					      <table  class="table table-bordered"> 
					    		 <b class="pl15">已经存在的商品对应规格信息</b>
					    		 
					    		<#if  prdExistInfo ??> 
					    		<thead>
					    			 <tr style="height:20px;">
						    				 <th align="center" width="20%" style="height:20px;">条形码</th>
						    				 <th align="center" width="20%" style="height:20px;">PRDID</th> 
						    				 <th align="center" width="20%" style="height:20px;">商品名称</th>
											 <#if prdExistInfo.specGroupInfo?exists>
										                <#list prdExistInfo.specGroupInfo?keys as myKey> 
										                 	<th align="center" class="existGroup" existGroup="${myKey}"  width="20%" style="height:20px;">${prdExistInfo.specGroupInfo[myKey]}</th>
										                </#list>
								           		 </#if>
						    		 	</tr>
						    		</thead>
						    		
						    		<tbody>
						    			<input type="hidden"  name="allSpec"  value='${allSpec}'/>
						    				<#if  prdExistInfo.listOfSimpleDetailOpenDto ??> 
						    						   <#list prdExistInfo.listOfSimpleDetailOpenDto as detailInfo> 
						    						      <tr class="existPrd">
													            <td>${(detailInfo.barcode)!}</td>
													            <td class="td_prd">${(detailInfo.prdid)!}</td>
													            <td>${(detailInfo.itemTitle)!}</td>
													           
													        </tr>
						    						   </#list>
						    				</#if>
						    	    </tbody>
					    		</#if>
					      </table>
        	  </#if>
        	  
        	  
        	      <table  id="skuListTable" class="table table-bordered"> 
        	      
        	      
        	      </table>  
        	       <#if  noSave ??> 
        	          	<div class="container" style="margin-left:400px;"> 
        	          	<font color="red">均码商品不能添加PRD</font>
        	          	</div>
        	       	<#else>
        	       		<div class="container" style="margin-left:400px;"> 
  							<div class="row">
        	 			 	<input type="button" class="btn btn-default" id="dataFormSave" value="保存">
        	 				</div>		 
        	 			</div>
        	     </#if>
        	 
        	 	 		 
       </form>      
    </div><#-- panel-body -->
</div>
<script type="text/javascript" >  
	
</script> 


</@sellContent>
