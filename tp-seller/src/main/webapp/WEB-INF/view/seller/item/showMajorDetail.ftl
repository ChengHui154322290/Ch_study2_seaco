<#include "/layout/blank_layout.ftl" />

<@sellContent title="首页" 
    css=['/static/css/item-ul.css',
     '/static/css/item-detail.css']>
   	<script type="text/javascript" src="/static/scripts/item/editor/kindeditor-all.js"></script>     
   	<script type="text/javascript" src="/static/scripts/item/editorUtil.js"></script>     
   	<script type="text/javascript" src="/static/scripts/item/tab.js"></script>  
   				
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">商品查看</h3>
	    </div>
    <div class="panel-body">
        <form class="form-inline" role="form" id="searchFormId">
	  	    <table width="100%" height="200px;" class="table table-bordered">   
	  	    
	  	  	  <b class="pl15">商品SPU信息</b>
	      		<tr>
					<td width="20%">SPU：</td>
					<td width="80%">
							<input type="text"  value="${majorInfo.infoOpenDto.spu}" disabled/>
					</td>
	      		</tr>
	      	<tr>
	      		<td width="20%">商品类别：</td>
	      		<td width="80%">
	      			大类：<input type="text" value="${majorInfo.infoOpenDto.categoryOpenDto.bigCateName}" disabled />
	      			中类：<input type="text" value="${majorInfo.infoOpenDto.categoryOpenDto.middCateName}"  disabled />
	      			小类：<input type="text" value="${majorInfo.infoOpenDto.categoryOpenDto.smallCateName}"  disabled />
	      		</td>
	      	</tr>
	      	
	      	<tr>
	      		<td width="20%">商品品牌：</td>
	      		<td width="80%">
		      			<input type="text"  value="${majorInfo.infoOpenDto.brandName}" disabled /> 
		      		    单位:<input type="text"  value="${majorInfo.infoOpenDto.unitName}" disabled />
	      		</td>
	      	</tr>
	      	<tr>
	      		<td width="20%">SPU名称：</td>
	      		<td >
		      			<input type="text"  value="${majorInfo.infoOpenDto.spuName}" disabled />
	      		</td>
	      	</tr>
	      	<tr>
	      		<td width="20%">备注：</td>
	      		<td >
		      			 <input type="text"  value="${majorInfo.infoOpenDto.remark}"/>  
	      		</td>
	      	</tr>
	    </table>
	    
	    
	    
	     <table width="100%" height="200px;" class="table table-bordered">
	     	  <b class="pl15">商品信息</b>
			     <tbody><tr>
			     
			     			<td width="20%">prdid:</td>
							 <td >
									 <input type="text"  value="${majorInfo.detailOpenDto.prdid}" disabled />
							 </td>	
							  <td rowspan="5" align="center"  valign="middle"  >规格维度:</td>
							  <td rowspan="5" >
								 	<ul>
									 	<#if majorInfo.detailOpenDto.specGroupAndSpec?exists>
									 	
									 			<#list majorInfo.detailOpenDto.specGroupAndSpec?keys as myKey> 
													<li>
														${myKey} ：${majorInfo.detailOpenDto.specGroupAndSpec[myKey]}
													</li>	
											 	 </#list>
									      <#else>         
									      	<li>
								 	  			均码
								 	   		 </li>
					          			 </#if>
								 	</ul>
							  </td>
						</tr>
						<tr>
							 <td width="20%">商品名称:</td>
							 <td>
								   <input type="text"  value="${majorInfo.detailOpenDto.itemTitle}" disabled /> 
							 </td>
						</tr>
						<tr>
							 <td width="20%">网站显示名称:</td> 
							 <td>
								 <input type="text"  value="${majorInfo.detailOpenDto.mainTitle}" disabled  />
							 </td>
						</tr>
						<tr>
							<td width="20%">仓库名称:</td>
							<td>
								<input type="text" value="${majorInfo.detailOpenDto.storageTitle}" disabled  />
							</td>
						</tr>
						<tr>
							 <td width="20%">备注:</td>
							 <td>
							 	<input type="text" value="${majorInfo.detailOpenDto.remark}" disabled  />
							 </td>
						</tr>
						
				</tbody>
	     </table>
	     
	     <table width="100%" height="200px;" class="table table-bordered">
	        <b class="pl15">基本信息</b>
		        <tbody>
		        <tr>
					<td width="20%" rowspan="2">商品卖点(副标题):</td>
					<td rowspan="2">
						<textarea  cols="30" rows="3"  maxlength="100"  disabled >${majorInfo.detailOpenDto.subTitle}</textarea>
					</td>
					<td width="10%">市场价:</td>
					<td><input type="text" value="${majorInfo.detailOpenDto.basicPrice}" disabled /></td>
					<td width="10%" >生产厂家:</td>
					<td><input type="text" value="${majorInfo.detailOpenDto.manufacturer}" disabled /></td>
				</tr>
				
				
				<tr>
					<td width="20%">进项税率:</td>
					<td >
						<input type="text" value="${majorInfo.detailOpenDto.purRateName}" disabled />
					</td>
					
					<td width="20%">运费模板:</td>   	
					 <td class="">
					 	<input type="text" value="${majorInfo.detailOpenDto.freightTemplateName}" disabled />
					</td>	
				</tr>
				<tr>
					<td width="20%">商品类型:</td>
					<td class="">
						<input type="text" value="${majorInfo.detailOpenDto.itemTypeName}" disabled />
					</td>
					<td width="20%">体积:</td>
					<td class="">
					<input type="text"  value="${majorInfo.detailOpenDto.volumeLength}" disabled  size="5" style="line-height:normal;text-align:center;"  placeholder="长">
					<input type="text"  value="${majorInfo.detailOpenDto.volumeWidth}" disabled  size="5" style="line-height:normal;text-align:center;"  placeholder="宽">
					<input type="text"   value="${majorInfo.detailOpenDto.volumeHigh}" disabled  size="5" style="line-height:normal;text-align:center;"  placeholder="高">&nbsp;cm
					</td>
					
					
					<td width="20%">销项税率:</td>
					<td class="">
							<input type="text" value="${majorInfo.detailOpenDto.saleRateName}" disabled />
					</td>
				</tr>
				<tr>
					<td width="20%">规格:</td>
					<td><input type="text"  size="25" value="${majorInfo.detailOpenDto.specifications}" disabled    maxlength="60" ></td>
					<td width="20%">毛重:</td>
					<td><input type="text"  size="5"  value="${majorInfo.detailOpenDto.weight}" disabled    maxlength="9" >&nbsp;g</td>
					<td width="20%">箱规:</td>
					<td><input type="text"  value="${majorInfo.detailOpenDto.cartonSpec}" disabled size="25"   maxlength="60"></td>	
				</tr>
				
				<tr>
					<td width="20%">无理由退货天数:</td>
					<td><input type="text"   value="${majorInfo.detailOpenDto.returnDays}" disabled  size="5" maxlength="5"></td>
					<td width="20%">净重:</td>
					<td><input type="text" size="5"  value="${majorInfo.detailOpenDto.weightNet}" disabled  maxlength="9" >&nbsp;g</td>
					<td width="20%">条形码:</td>
					<td style="width:15%"><input type="text"  value="${majorInfo.detailOpenDto.barcode}" disabled  size="20"  maxlength="20"></td>
				</tr>
			</tbody>
	    </table>
	    
	       <table width="100%" height="50px;" class="table table-bordered">
	       
	       	    <tr>
	       			<td width="20%">是否海淘商品: </td>
	       			<td>
	       						<#if majorInfo.detailOpenDto.wavesSign ?? >
	       							<#if majorInfo.detailOpenDto.wavesSign == '2' >
	       								<input type="text" value="是" disabled />
	       							<#else> 
	       								<input type="text" value="否" disabled />
	       							</#if>
								</#if>
	       			</td>
	       			<td width="20%">是否有效期管理:</td>
	       			<td>
	       				<#if majorInfo.detailOpenDto.expSign ?? >
	       							<#if majorInfo.detailOpenDto.expSign == '1' >
	       								<input type="text" value="是" disabled />
	       								<input type="text" value="${majorInfo.detailOpenDto.expDays}" disabled />
	       							<#else> 
	       								<input type="text" value="否" disabled />
	       							</#if>
						</#if>
	       				
	       			</td>
	       			<td width="20%">适用年龄:</td>
	       			<td>
	       					<input type="text" value="${majorInfo.detailOpenDto.applyAgeName}" disabled />
	       			</td>
	       		</tr>
	       		<tr>
	       				<td width="20%">行邮税税率:</td>
	       				<td>
	       					<input type="text" value="${majorInfo.detailOpenDto.tarrifRateName}" disabled />
	       				</td>
	       				<td width="20%">产地:</td>
	       				<td>
	       					<input type="text" value="${majorInfo.detailOpenDto.countryName}" disabled />
	       				</td>
	       		</tr>
	       </table>
	       
	       
	      <table width="100%" height="100px;" class="table table-bordered">  
	         <b class="pl15">商品属性</b>
	         <tr style="defalut">   
	         	<td style="height:30px;">属性组</td>
	         </tr>
	         <#if majorInfo.detailOpenDto.baseAttr?exists>
                <#list majorInfo.detailOpenDto.baseAttr?keys as myKey> 
                   <tr style="height:50px;">   
                           <td style="height:30px;">${myKey}:</td> 
                           <td style="height:30px;">
	                           	<#list majorInfo.detailOpenDto.baseAttr[myKey] as attrValue> 
	                           			${attrValue} &nbsp; 
	                         	 </#list>	  
                           </td>     
                   </tr>
                </#list>
            </#if>
	    </table>
	      
	      <table width="100%" height="50px;" class="table table-bordered"> 
	         <b class="pl15">自定义属性</b> 
	      		<tr style="height:50px;">
					<th width="50%" style="height:30px;">自定义属性名</th>
					<th width="50%" style="height:30px;">自定义属性值</th> 
				</tr>
			    <#if majorInfo.detailOpenDto.customeAttr?exists>
			       <#list majorInfo.detailOpenDto.customeAttr?keys as myKey> 
			    	<tr style="height:50px;">
						<td style="height:30px;"> 
							${myKey}
						</td>  
						<td style="height:30px;">
							${majorInfo.detailOpenDto.customeAttr[myKey]}
						</td>
					</tr>
					  </#list>
				 </#if>
	      </table>
	       
	       
	         <table width="100%" height="50px;" class="table table-bordered">
	      		   <b class="pl15">图片与详情</b>
	      		   
						 <table class="input tabContent">
							<tr>
								<td>
								<div id="item-pictures">
								<#list majorInfo.listOfPictures as picture>
									<div class="item-picture">
										<img src="<@imageDownload code="${picture.picture}" width="200" />" />  
									</div>
								</#list>
						 		</div>
								</td>
							</tr>
			</table>
							      		   
	      	 <div class="box_center">
						<ul id="tab" class="tab" >
								<li>
									<input type="button" id="pcEditorTabBtn" value="PC模板 " />
								</li>
								<li>
									<input type="button" value="手机模板" id="mobileEditorTabBtn" />
								</li>
						</ul>
						<table class="input tabContent pctable"  style="width: 100%;height="300px;">
							<tr>
								<td>
								<textarea id="editor"  name="desc.description"  class="editor" style="width: 100%;">
										${majorInfo.descDetail}
								</textarea>
								</td>
							</tr>
						</table>
						<table  class="input tabContent mobiletable" style="width: 100%;height="300px;"  >  
							<tr>
								<td>
								<textarea id="mobileEditor" name="descMobile.description"   class="editor" style="width:100%;">  
										${majorInfo.descMobileDetail}
								</textarea>
								</td>
							</tr>
						</table>
				</div>
	       </table>
    </div><#-- panel-body -->
</div>
</form> 
</@sellContent>
