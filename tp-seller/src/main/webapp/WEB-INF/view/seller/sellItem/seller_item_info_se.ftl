<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/static/backend/js/jquery.tools.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/editor/kindeditor-all.js',
	'/statics/backend/js/editorUtil.js', 
	'/statics/backend/js/tab.js',
	'/statics/backend/js/form.js',
	'/statics/backend/js/util.js',
	'/statics/js/jquery.cookie.js',
	"/statics/backend/js/swfupload/js/fileprogress.js",
	"/statics/backend/js/swfupload/js/handlers.js",
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/item/item-seller.js'
	] 
	css=[
	'/static/backend/css/style.css',
	'/static/select2/css/common.css',
	'/static/css/main.css' ] >
	<style type="text/css">
		.ke-dialog{
			top:15%;
		}
	</style>
<input type="hidden" value="${sessionId}" id="sessionId" />
<input type="hidden" value="${parentFrameId}" id="parentFrameId" /> 
<form id="inputForm" action="saveDetail.htm" method="post" enctype="multipart/form-data">	

<div class="box_border">
	<div class="box_top">
		<b class="pl15">商品SPU信息</b>
	</div>
	<!-- 基础信息  -->
	<div class="box_center">
		<table class="input commContent">
			<tr>
				<td class="td_left" style="width:15%">
					SPU：
				</td>
				<td class="" colspan=1>
				    ${plantFormItemDetail.infoSellerDto.spu!"系统自动生成"}
				</td>
			</tr>
			<tr>
				<td class="td_left"><span class="requiredField">*</span>商品类别：</td>
				<td class="td_left">大类:${plantFormItemDetail.infoSellerDto.categorySellerDto.bigCateName}
                                   ->中类:${plantFormItemDetail.infoSellerDto.categorySellerDto.middCateName}
								    ->小类:${plantFormItemDetail.infoSellerDto.categorySellerDto.smallCateName}</td>
			</tr>
			<tr>
				<td class="td_left"><span class="requiredField">*</span>商品品牌：</td>
				<td class="">
					<span class="fl">
				   		${plantFormItemDetail.infoSellerDto.brandName}
					</span>												
				</td>
			</tr>
			<tr>
				<td class="td_left"><span class="requiredField">*</span>单位：</td>
				<td class="">
					<span class="fl">
				   		${plantFormItemDetail.infoSellerDto.unitName}
					</span>													
				</td>
			</tr>
			<tr>
				<td class="td_left"><span class="requiredField">*</span>SPU名称：</td>
				<td class="">
				    <span class="fl">
				   		${plantFormItemDetail.infoSellerDto.spuName}
					</span>	
				</td>
			</tr>
			<tr>
				<td class="td_left">备注：</td>
				<td class=""><span class="fl">
				   		${plantFormItemDetail.infoSellerDto.remark}
			    </span>	</td>
			</tr>
		</table>
	</div>
</div>


<div class="box_border">
	<div class="box_top">
		<b class="pl15">商品信息</b>
	</div>
	
	<div class="box_center">
		<table class="input tabContent">
				<tr><td class="td_right">prdid:</td>
					 <td >
						<span class="fl">
				   		   ${plantFormItemDetail.detailSellerDto.prdid!'系统自动生成'}
					    </span>	
					 </td>	
					  <td rowspan=5  class="td_right"><span class="requiredField">*</span>规格维度:</td>
					  <td  rowspan=5  style="width:40%">
					 	<ul>		 	
					 	  <#if  plantFormItemDetail.detailSellerDto.specGroupAndSpec??&& plantFormItemDetail.detailSellerDto.specGroupAndSpec?size gt 0>
                                 <#list  plantFormItemDetail.detailSellerDto.specGroupAndSpec?keys as key>
                                    ${key}:${plantFormItemDetail.detailSellerDto.specGroupAndSpec[key]} </br>
                                </#list>
					 	  <#else>
					 	    <li>均码  </li>
					 	  </#if>
					 	</ul>
					  </td>
					
				</tr>
				<tr>
					 <td class="td_right"><span class="requiredField">*</span>商品名称:</td>
					 <td>
					    <span class="fl">
				   		   ${plantFormItemDetail.detailSellerDto.itemTitle}
					    </span>	
					 </td>
				</tr>
				<tr>
					 <td class="td_right"><span class="requiredField">*</span>网站显示名称:</td> 
					<td>
					    <span class="fl">
				   		   ${plantFormItemDetail.detailSellerDto.mainTitle}
					    </span>	
					 </td>
				</tr>
				<tr>
					<td class="td_right"><span class="requiredField">*</span>仓库名称:</td>
					<td>
					    <span class="fl">
				   		   ${plantFormItemDetail.detailSellerDto.storageTitle}
					    </span>	
					 </td>
				</tr>
				<tr>
					 <td class="td_right">备注:</td>
					<td>
					    <span class="fl">
				   		   ${plantFormItemDetail.detailSellerDto.remark}
					    </span>	
					 </td>
				</tr>

		</table>		
	</div>
</div>	
	
<div class="box_border">
	<div class="box_top">
		<b class="pl15">基本信息</b>
	</div>
	
	<div class="box_center">
		<table class="input tabContent">
			<tr>
				<td class="td_right" rowspan="2"><span class="requiredField">*</span>商品卖点(副标题):</td>
				<td rowspan="2">
					 <span class="fl">
					   		   ${plantFormItemDetail.detailSellerDto.subTitle}
				      </span>	
				<td class="td_right">市场价:</td>
				<td>
				      <span class="fl">
					   		   ${plantFormItemDetail.detailSellerDto.basicPrice}
				      </span>
			    </td>
				<td class="td_right">生产厂家:</td>
				<td>
				      <span class="fl">
					   		   ${plantFormItemDetail.detailSellerDto.manufacturer}
				      </span>
			    </td>
			</tr>
			<tr>
				<td class="td_right"><span class="requiredField">*</span>运费模板:</td>
				<td class="">
				  <span class="fl">	
					 ${plantFormItemDetail.detailSellerDto.freightTemplateName}
				  </span>
				</td>
				<td class="td_right"><span class="requiredField">*</span>进项税率:</td>
				<td class=""><span class="fl">
				     ${plantFormItemDetail.detailSellerDto.purRateName}%
				</span></td>
			</tr>
			<tr>
				<td class="td_right"><span class="requiredField">*</span>商品类型:</td>
				<td class="">
				  <span class="fl">
				  	 ${plantFormItemDetail.detailSellerDto.itemTypeName}
				  </span>
				</td>
				<td class="td_right">体积:</td>
				<td class="">
				<#if plantFormItemDetail.detailSellerDto.volumeLength??>
			     	长:${plantFormItemDetail.detailSellerDto.volumeLength}
				</#if>
				<#if plantFormItemDetail.detailSellerDto.volumeWidth??>
			     	宽:${plantFormItemDetail.detailSellerDto.volumeWidth}
				</#if>
				<#if plantFormItemDetail.detailSellerDto.volumeHigh??>
			     	高:${plantFormItemDetail.detailSellerDto.volumeHigh}
				</#if>
				</td>
				<td class="td_right"><span class="requiredField">*</span>销项税率:</td>
				<td class="">
				  <span class="fl">
				  	 ${plantFormItemDetail.detailSellerDto.saleRateName}%
				  </span>
				</td>
			</tr>
			
			<tr>
				<td class="td_right">规格:</td>
				<td >
				<span class="fl">
				  	 ${plantFormItemDetail.detailSellerDto.specifications}
				  </span>
				</td>
				
				<td class="td_right"><span class="requiredField">*</span>毛重:</td>
				<td>
				  <span class="fl">
				  	 ${plantFormItemDetail.detailSellerDto.weight}g
				  </span>
				</td> 
				<td class="td_right">箱规:</td>	
				    <td>
					 <span class="fl">
					  	 ${plantFormItemDetail.detailSellerDto.cartonSpec}
					  </span>
					 </td>
			</tr>
			<tr>
				<td class="td_right"><span class="requiredField">*</span>无理由退货天数:</td>
				<td>
				    <span class="fl">
					  	 ${plantFormItemDetail.detailSellerDto.returnDays}天
					</span>
			    </td>
				<td class="td_right">净重:</td>
				<td>
                     <span class="fl">
                     <#if plantFormItemDetail.detailSellerDto.weightNet??>
                     	 ${plantFormItemDetail.detailSellerDto.weightNet}g
                     </#if>
					  </span>
			    </td>		  
				<td class="td_right"><span class="requiredField">*</span>条形码:</td>
				<td style="width:15%">
				   <span class="fl">
				     ${plantFormItemDetail.detailSellerDto.barcode}
				   </span>
				</td>
			</tr>
		</table>
		</div>
	</div>
	<div class="box_border">
		<table>
			<tr>
				<td>
				  <span class="fl" style="width:200px;">
				  <span class="requiredField ml20">*</span>是否海淘商品:
					  			 <#if "${plantFormItemDetail.detailSellerDto.wavesSign}"=="1">否</#if>
								 <#if "${plantFormItemDetail.detailSellerDto.wavesSign}"=="2">是</#if>
				  </span>
				</td>
				<td >
				  <span class="fl">
				  <span class="requiredField ml20">*</span>是否有效期管理:
					 <#if "${plantFormItemDetail.detailSellerDto.expSign}"=="2">是 <#else>否</#if>
				  </span>	
				</td>
				 <#if "${plantFormItemDetail.detailSellerDto.expSign}"=="2">
				 <td>
				  <span class="fl" style="width:200px;">
				            有效期月数:	${plantFormItemDetail.detailSellerDto.expDays}
				  </span>
				</td>
				 </#if>
				<td  nowrap='nowrap'>
				  <span class="fl" >
				  <span class="requiredField ml20">*</span>
				    适用年龄:${plantFormItemDetail.detailSellerDto.applyAgeName}
				  </span>	
				</td>
			</tr>
					
		 <#if "${plantFormItemDetail.detailSellerDto.wavesSign}"=="2">
			<tr> 
				<td class="tarriRateClass" style="padding-left:20px;" nowrap='nowrap'>
					  <span class="fl" >
					  <span class="requiredField">*</span>行邮税税率:
					  	${plantFormItemDetail.detailSellerDto.tarrifRateName}
					  </span>
					</td>
			
				<td  class="triggerCountryClass"  nowrap='nowrap'>
				  <span class="fl">
				  <span class="requiredField ml20">*</span>产地:
					 	${plantFormItemDetail.detailSellerDto.countryName}
				  </span>	
				</td>
			</tr>
		</#if>	
		</table>
	</div>
	
	
	<div class="box_border">
		<div class="box_top">
			<b class="pl15">商品属性</b>
		</div>
		<div class="box_center" >
			<div class="tl" style=" border: 1px solid #d3dbde" >
				<div style="float:left; " >
					<b class="pl15">属性组 ：</b>
				</div>
				 
				<div style="float:left;" id="attributeId">
		             <#if  plantFormItemDetail.detailSellerDto.baseAttr??&& plantFormItemDetail.detailSellerDto.baseAttr?size gt 0>
                         <#list  plantFormItemDetail.detailSellerDto.baseAttr?keys as key>
                            <div>${key}:<#list plantFormItemDetail.detailSellerDto.baseAttr[key] as att>&nbsp;&nbsp; ${att} &nbsp;&nbsp;</#list></div>
                        </#list> 	 
				     </#if>
				</div>
				<div style="clear:both;"></div>
			</div>
			
			<table id="detailAttrList" class="input tabContent">
				<tr>
				<th width="60">自定义属性名</th>
				<th width="200">自定义属性值</th>
				</tr>
				<#if  plantFormItemDetail.detailSellerDto.customeAttr??&& plantFormItemDetail.detailSellerDto.customeAttr?size gt 0>
				       <#list  plantFormItemDetail.detailSellerDto.customeAttr?keys as key>
                                     <tr  class="attrList">
										<td class="td_left"  >
										  <span class="fl">
					  	                    ${key}
					                      </span>
										</td>
										<td class="td_left">
										   <span class="fl">
					  	                     ${plantFormItemDetail.detailSellerDto.customeAttr[key]}
					                       </span>
										</td>
									</tr>
                                </#list>
				  </#if>
			  </table>	
			</div>
         </div>
	
	

	<div class="box_border">
		<div class="box_top">
			<b class="pl15">
			图片与详情
			</b>
			
		</div>
		
	<table class="input tabContent">
	<tr>
		<td>
		
		<div id="item-pictures">
		<#list plantFormItemDetail.listOfPictures as picDetail>
			<div class="item-picture">
				<img src="<@itemImageDownload code="${picDetail.picture}" width="200" />" />
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
						<input type="button" value="PC模板 " />
					</li>
					<li>
						<input type="button" value="手机模板" id="mobileEditorTabBtn" />
					</li>
		</ul>
		<table class="input tabContent"">
				<tr>
					<td>
						<textarea id="editor" name="desc.description" class="editor" style="width: 100%;">
							${plantFormItemDetail.descDetail}
						</textarea>
					</td>
				</tr>
		</table>
		<table  class="input tabContent">
				<tr>
					<td>
						<textarea id="mobileEditor" name="descMobile.description" class="editor" style="width:100%;">
							${plantFormItemDetail.descMobileDetail}
						</textarea>
					</td>
				</tr>
		</table>
     </div>		
		
	</div>
	
	<div class="box_border">
		<div class="box_center">
		<table class="input tabContent" id="suppListTable">
			<tr>
				<th width="60">SKU编号</th>
				<th width="200">商家名称</th>
				<th width="100">商家商品码（条形码）</th>
				<th width="60">市场价</th>		
				<th width="60">类型</th>
			</tr>
			<tr class="skuList">
			<td width="60" style='color:red' align="center">        
				     系统自动生成
			</td>
			<td width="200" align="center">
		      ${supplierName}
			</td>
			<td width="100" align="center">
		      ${plantFormItemDetail.detailSellerDto.barcode}
			</td>
			<td width="60" align="center">
		     ${plantFormItemDetail.detailSellerDto.basicPrice}
			</td>
			<td width="60" align="center">
			   <#if "${plantFormItemDetail.detailSellerDto.wavesSign}"=="1">非海淘</#if>
			   <#if "${plantFormItemDetail.detailSellerDto.wavesSign}"=="2">海淘</#if>
			</td>
			</tr>			
		</table>
	 </div>
	 
	 <div class="tc">
		<input type="button" id="auditBtn" style="text-align:right;" value="审核" class="ext_btn ext_btn_submit m10">
		<input type="hidden" value="${sellerSkuId}" name="sellerSkuId"  id="sellerSkuId"/>	
		<input type="hidden" id="formToken" name="formToken" value="${formToken}">
		<input type="button" value="返回" onclick="location.href='seller_skuInfo_list.htm'"	class="ext_btn m10">
	 </div>
</div>
</form>
</@backend>