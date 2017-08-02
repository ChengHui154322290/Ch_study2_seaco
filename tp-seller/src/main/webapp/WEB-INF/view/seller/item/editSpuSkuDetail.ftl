<#include "/layout/blank_layout.ftl" />

<@sellContent title="首页" 
	js=[
	
		'/static/scripts/item/editor/kindeditor-all.js',
		'/static/scripts/item/editorUtil.js', 
		'/static/scripts/item/item-detail.js',
		"/static/scripts/swfupload/swfupload.js",
		"/static/scripts/swfupload/js/fileprogress.js",
		"/static/scripts/swfupload/js/handlers.js",
		"/static/scripts/swfupload/js/swfupload.queue.js",
		"/static/scripts/item/item_file_upload.js"  
				]
    css=['/static/css/item-ul.css',
   	 	 '/static/css/item-detail.css'
    	]> 
   	<style>  
    	.requiredField {
			    color: red;
		}  
    </style>
 <form class="form-inline" role="form" id="inputForm"  method="post" enctype="multipart/form-data">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">商品查看</h3>
	    </div>
    <div class="panel-body">
     
       		 <input type="hidden" value="${sessionId}" id="sessionId" />
       		 <input type="hidden" value="${skuId}" id="skuId"  name="skuId"/>  
	        <input type="hidden" name="skuListJson"  id="skuListJson"/>
			<input type="hidden" name="attrListJson"  id="attrListJson"/>
			<input type="hidden" name="attrItemJson"  id="attrItemJson"/>
			<input type="hidden" name="auditType"  value="E" />
			
	       		 
	  	    <table width="100%" height="200px;" class="table table-bordered">   
	  	    
	  	  	  <b class="pl15">商品SPU信息</b>
	      		<tr>
					<td width="20%">SPU：</td>
					<td width="80%">
							 <#if  item.info.spu ??>
									<input type="text"  value="${item.info.spu}" disabled/>
							 <#else>		
									 (<font color="red">审核成功后自动生成</font>)
							 </#if>			
					</td>
	      		</tr>
	      	<tr>
	      		<td width="20%">商品类别：</td>
	      		<td width="80%">
	      			大类：<input type="text" value="${item.info.largeName}" disabled />
	      			中类：<input type="text" value="${item.info.mediumName}"  disabled />
	      			小类：<input type="text" value="${item.info.smallName}"  disabled />  
	      		</td>
	      	</tr>
	      	
	      	<tr>
	      		<td width="20%">商品品牌：</td>
	      		<td width="80%">
		      			<input type="text"  value="${item.info.brandName}" disabled /> 
		      		    单位:<input type="text"  value="${item.info.unitName}" disabled />
	      		</td>
	      	</tr>
	      	<tr>
	      		<td width="20%">SPU名称：</td>
	      		<td >
		      			<input type="text"  value="${item.info.mainTitle}" disabled />
	      		</td>
	      	</tr>
	      	<tr>
	      		<td width="20%">备注：</td>
	      		<td >
		      			 <input type="text" value="${item.info.remark}" disabled />
	      		</td>
	      	</tr>
	    </table>
	    
	    
	    
	     <table width="100%" height="200px;" class="table table-bordered">
	     	  <b class="pl15">商品信息</b>
			     <tbody><tr>
			     
			     			<td width="20%">prdid:</td>
							 <td >
								 (<font color="red">审核成功后自动生成</font>)
							 </td>	
							  <td rowspan="5" align="center"  valign="middle"  >规格维度:</td>
							  <td rowspan="5" >
											<ul>
											 	  <#if detailSpecList?default([])?size !=0>
											 	  <#list detailSpecList as t>
											 	  	<li>
											 	  		<#if "${t.specGroupId}" =="-1">
											 	  			均码  
											 	  		<#else>
											 	  		${t.specGroupName}:${t.specName}
											 	  		</#if>
											 	  		
											 	  	</li>
											 	  </#list>
											 	  <#else>
											 	    <li>均码  </li>
											 	  </#if>
										 	</ul>
							  			</td>
							</tr>
						<tr>
							 <td width="20%"><span class="requiredField">*</span>商品名称:</td>  
							 <td>
								   <input type="text"  value="${detail.mainTitle}"  name="detail.itemTitle"   id="itemTitle"  maxlength=60 /> 
							 </td>
						</tr>
						<tr>
							 <td width="20%"><span class="requiredField">*</span>网站显示名称:</td> 
							 <td>
								 <input type="text" value="${detail.mainTitle}"     value="${detail.mainTitle}" id="mainTitle" maxlength=60 	name="detail.mainTitle"    />
							 </td>
						</tr>
						<tr>
							<td width="20%"><span class="requiredField">*</span>仓库名称:</td>
							<td>
								<input type="text" value="${detail.storageTitle}"  name="detail.storageTitle"  id="storageTitle" maxlength=60  />
							</td>
						</tr>
						<tr>
							 <td width="20%">备注:</td>
							 <td>
							 	<input type="text" name="detail.remark" value="${majorInfo.detailOpenDto.remark}" value="${detail.remark}" id="detailRemark" maxlength=60  />
							 </td>
						</tr>
						
				</tbody>
	     </table>
	     
	     <table width="100%" height="200px;" class="table table-bordered">
	        <b class="pl15">基本信息</b>
		        <tbody>
		        <tr>
					<td width="20%" rowspan="2"><span class="requiredField">*</span>商品卖点(副标题):</td>
					<td rowspan="2">
						<textarea    name="detail.subTitle" cols="30" rows="3" id="subTitle" maxlength=100 >${detail.subTitle}</textarea>
					</td>
					
					<td width="10%"><span class="requiredField">*</span>市场价:</td>
					<td><input type="text" name="detail.basicPrice" value="${detail.basicPrice}" id="basicPrice"  maxlength=9  /></td>
					<td width="10%" >生产厂家:</td>
					<td><input type="text" name="detail.manufacturer" value ="${detail.manufacturer}" maxlength=100	  /></td>
				</tr>
	
				<tr>
					<td width="20%"><span class="requiredField">*</span>进项税率:</td>
					<td >
						 <select name="detail.purRate" class="select">
								<#list purRateList as t>
									<option value="${t.id}">${t.rate}%</option>
								</#list>
						</select>
					</td>
					
				 <td width="20%"><span class="requiredField">*</span>运费模板:</td>   	
				 <td class="">
						<select	name="detail.freightTemplateId" class="select">
							<#list freightTemplateList as t>
								<option value="${t.id}" <#if "${detail.freightTemplateId}"=="${t.id}"> selected</#if> >${t.name}</option>
							</#list>
						</select>  
					</td>
				</tr>
				<tr>
					<td width="20%"><span class="requiredField">*</span>商品类型:</td>
					<td class="">
					  	<select	name="detail.itemType" class="select">
							<option value="1" <#if "${detail.itemType}"=="1"> selected</#if>>正常商品</option>
							<option value="2" <#if "${detail.itemType}"=="2"> selected</#if>>服务商品</option>
							<option value="3" <#if "${detail.itemType}"=="3"> selected</#if>>二手商品</option>
							<option value="4" <#if "${detail.itemType}"=="4"> selected</#if>>报废商品</option>
						</select>
					</td>
					<td width="20%">体积:</td>
					<td class="">
							<input type="text" class="input-text lh30" size="5" style="line-height:normal;text-align:center;" name="detail.volumeLength" value="${detail.volumeLength}" id="volumeLength" maxlength=9 placeholder="长"  />
							<input type="text" class="input-text lh30" size="5"	style="line-height:normal;text-align:center;" name="detail.volumeWidth"  value="${detail.volumeWidth}"  id="volumeWidth" maxlength=9 placeholder="宽"  />
							<input type="text" class="input-text lh30" size="5"	style="line-height:normal;text-align:center;" name="detail.volumeHigh"  value="${detail.volumeHigh}" id="volumeHigh" maxlength=9 placeholder="高"  />&nbsp;cm
					</td>
					
					<td width="20%"><span class="requiredField">*</span>销项税率:</td>
					<td class="">
							<select	name="detail.saleRate" class="select">
								<#list saleRateList as t>
									<option value="${t.id}"   <#if "${detail.saleRate}"=="${t.id}"> selected</#if> >${t.rate}%</option>
								</#list>
							</select>
					</td>
				</tr>
				<tr>
					<td width="20%">规格:</td>
					<td><input type="text"  size="25" size="25"	name="detail.specifications" value="${detail.specifications}" id="specifications" maxlength=60  ></td>
					<td width="20%"><span class="requiredField">*</span>毛重:</td>
					<td><input type="text"  size="5" value="${detail.weight}" id="weight" maxlength=9 name="detail.weight"  >&nbsp;g</td>
					<td width="20%">箱规:</td>
					<td><input type="text"  size="25"	name="detail.cartonSpec"  value="${detail.cartonSpec}" id="cartonSpec" maxlength=60 ></td>	
				</tr>
				
				<tr>
					<td width="20%"><span class="requiredField">*</span>无理由退货天数:</td>
					<td><input type="text"  size="5"	name="detail.returnDays" value="${detail.returnDays}"  id="returnDays" maxlength="5" ></td>
					<td width="20%">净重:</td>
					<td><input type="text"  size="5" value="${detail.weightNet}" id="weightNet" maxlength=9 name="detail.weightNet"  >&nbsp;g</td>
					<td width="20%"><span class="requiredField">*</span>条形码:</td> 
					<td style="width:15%"><input type="text"  size="20"	name="detail.barcode"  value="${detail.barcode}" id="barcode" maxlength=20  readonly="readonly" ></td> 
				</tr>
			</tbody>
	    </table>
	    
	       <table width="100%" height="50px;" class="table table-bordered">
	       
	       	    <tr>
	       			<td width="20%"><span class="requiredField">*</span>是否海淘商品: </td>
	       			<td>
	       					<select name="detail.wavesSign" class="select" id="wavesSignId">
					  			<option value="1" <#if "${detail.wavesSign}"=="1"> selected</#if> >否</option>
								<option value="2" <#if "${detail.wavesSign}"=="2"> selected</#if> >是</option>
							</select>
	       			</td>
	       			<td width="20%"><span class="requiredField">*</span>是否有效期管理:</td>
	       			<td>
	       					<select name="detail.expSign"  id="selectExpSign" class="select">
					   			<option value="2" <#if "${detail.expSign}"=="2"> selected</#if> >否</option>
								<option value="1" <#if "${detail.expSign}"=="1"> selected</#if> >是</option>
							</select>
	       			</td>
	       			
	       			<td class="show_expdays" nowrap='nowrap'>  
	       				<input type="text" class="input-text lh30 ml20" size="5" value="${detail.expDays}" name="detail.expDays" id="inputExpDays" maxlength="5" />有效期月数
	       			</td>
	       			
	       			<td width="20%"><span class="requiredField">*</span>适用年龄:</td>
	       			<td>
	       				<select name="detail.applyAgeId"  id="applyAgeId" class="select2" style="width:150px;"> 
								<option value="" >请选择</option>
								<#list applyAgeList as t>
									<option value="${t.id}" <#if "${detail.applyAgeId}"=="${t.id}"> selected</#if> >${t.name}</option>
								</#list>
						</select>
	       			</td>
	       		</tr>
	       		<tr class="show_haitao">
	       				<td width="20%"><span class="requiredField">*</span>行邮税税率:</td>
	       				<td>
	       						<select	name="detail.tarrifRate" id="tarrifRate" class="select2"  style="width:300px;"> 
							  		<option value="">--请选择行邮税税率--</option>
									<#list tarrifList as t>
										<option value="${t.id}" <#if "${detail.tarrifRate}"=="${t.id}"> selected</#if> >${t.code}-${t.rate}%-${t.remark}</option>
									</#list>
								</select>
	       				</td>
	       				<td width="20%"><span class="requiredField">*</span>产地:</td>
	       				<td>
	       				
	       					<select name="detail.countryId"  id="countryId" class="select2"  style="width:200px; margin-left: 1px" > 
								<option value="">--请选择产地--</option>
								<#list listCountry as country>
									<option value="${country.id}"   <#if "${detail.countryId}"=="${country.id}"> selected</#if>   >${country.name}</option>
								</#list>
							</select>
	       				</td>
	       		</tr>
	       </table>
	       
	       
	      
	      <table width="100%" height="200px;" class="table table-bordered">
        		<div class="box_top">
				<b class="pl15">商品属性</b>
				<div class="box_center" >
						<div class="tl" style=" border: 1px solid #d3dbde" >
							<div style="float:left; " >
								<b class="pl15">属性组 ：</b>
							</div>
							
							<div style="float:left;" id="attributeId">
								<#list listAttributeResult as attributeResult>
									<div class="attrItem" style="margin-bottom:5px;"> 
										<input type="hidden" name="attributeId" value="${attributeResult.attribute.id}" /> 
										
										<span>${attributeResult.attribute.name}<#if attributeResult.attribute.isRequired==true><span class="requiredField">(<font color="red">必选</font>)</span></#if>:</span> 
										<#if attributeResult.attribute.allowMultiSelect >
											<!--多选 -->
											<#list attributeResult.attributeValues as l>
													<input class="attributeValueClass"  type="checkbox" isRequired="${attributeResult.attribute.isRequired}" name="attributeValueId" value=${l.id} <#if "${l.isSelect}"=="1"> checked</#if>  >${l.name}
											</#list>
										<#else>
											<!--单选 -->
											<select name="attributeValueId" class="select" isRequired="${attributeResult.attribute.isRequired}">
												<#if attributeResult.attribute.isRequired==true>
												<#list attributeResult.attributeValues as l>
													<option value=${l.id}  <#if "${l.isSelect}"=="1"> selected</#if> >${l.name}</option>		
												</#list>	
												<#else>
												<option value=""  >请选择</option>	
												<#list attributeResult.attributeValues as l>
													<option value=${l.id}  <#if "${l.isSelect}"=="1"> selected</#if> >${l.name}</option>		
												</#list>	
												</#if>	
											</select>
										</#if>
									 </div>	
								</#list>
							</div>
							<div style="clear:both;"></div>
						</div>
						
						<div style="margin-top:30px;"> 
							<input type="button" id="addAttributeBtn"  value="添加属性" class="btn btn-default" > 
						</div>
						<table id="detailAttrList"  class="table table-bordered">
							<tr>
							<th width="60">自定义属性名</th>
							<th width="200">自定义属性值</th>
							<th width="60">操作 </th>
							</tr>
							<#list attrList  as l>
							<tr  class="attrList">
								<td class="td_left"  >
									<input type="text" class="input-text lh30"  name="attrKey" value="${l.attrKey}"  maxlength="20"/>
								</td>
								<td class="td_left">
								 <input type="text" class="input-text lh30"  name="attrValue" value="${l.attrValue}"  maxlength="200" />
								</td>
								<td class="td_left">
									<input type="button" id="" value="-" class="ext_btn ext_btn_submit m10 deleteAttrBtn">
								</td>
							</tr>
							</#list>
						</table>	
					</div>
	      </table>
	       
   		
	     <table width="100%" height="50px;" class="table table-bordered">
	  		   <b class="pl15">图片与详情</b>
					 <tr>
						<td>
							<div>
								<span id="spanButtonPlaceHolder"></span><span style="margin-left:20px;">可以上传 jpg;jpeg;png;bmp类型图片,图片长宽为800像素,最大1 MB 第一张默认为主图</span>
							</div>
							<div id="item-pictures">
							<#list picList as picture>
								<div class="item-picture">
									<img src="<@imageDownload code="${picture}" width="200" />" />
									<span class="remove-btn" title="删除">删除</span>
									<span class="pre-btn" title="前移">前移</span>
									<span class="next-btn" title="后移">后移</span>
									<input name="picList" value="${picture}"  type="hidden"/>           
								</div>
							</#list> 
					 		</div>
						<div class="fieldset flash" style="display:none" id="fsUploadProgress">
							<span class="legend"></span>
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
										${desc.description}
								</textarea>
								</td>
							</tr>
						</table>
						<table  class="input tabContent mobiletable" style="width: 100%;height="300px;"  >  
							<tr>
								<td>
								<textarea id="mobileEditor" name="descMobile.description"   class="editor" style="width:100%;">  
										${descMobile.description}
								</textarea>
								</td>
							</tr>
						</table>
				</div>
				
					
	         </table>
	         
	         			
			<table class="table table-bordered">	   
					  <b class="pl15">SKU信息</b>
					  <tr>
					  		<td><span class="requiredField">*</span>市场价:</td> 
					  		<td>
					  			<input type="text" name="detail.skuPrice" value="${skuPrice}" id="skuPrice"  maxlength=9></input> 
					  		</td>
					  </tr>
	        </table>
	        
	        <br>
	         
	         	 <#if  editFlag ??>
	         	 		<div style="margin-top;50px;"> 
		         			<input type="button" class="btn btn-default" value="保存" id="saveSpu" style="margin-left:300px;" />&nbsp;
							<input type="button" class="btn btn-default" value="提交审核" id="submitNewSpu"/>   
	         			</div>
	         	</#if>
    </div><#-- panel-body -->
</div>

</form>      
</@sellContent>
   