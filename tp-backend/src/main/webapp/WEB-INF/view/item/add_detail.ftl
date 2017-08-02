<#include "/common/common.ftl"/> 

<@backend title="" 
	js=[
	'/statics/backend/js/jquery.tools.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/editor/kindeditor-all.js',
	'/statics/backend/js/editorUtil.js', 
	'/statics/backend/js/tab.js',
	'/statics/backend/js/form.js',
	'/statics/backend/js/util.js',
	'/statics/js/jquery.cookie.js',
	'/statics/select2/js/select2.js',
	'/statics/select2/js/select2Util.js',
	'/statics/select2/js/select2_locale_zh-CN.js',
	'/statics/backend/js/item/item-select2.js',
	'/statics/qiniu/js/plupload/plupload.full.min.js',
    '/statics/qiniu/js/plupload/plupload.dev.js',
    '/statics/qiniu/js/plupload/moxie.js',
	'/statics/qiniu/js/plupload/moxie.js',
	'/statics/qiniu/src/qiniu.js',
	'/statics/qiniu/js/highlight/highlight.js',
	'/statics/qiniu/js/ui.js',
	'/statics/qiniu/xgUplod.js',
	'/statics/backend/js/item/item_picture_upload.js',
	'/statics/backend/js/item/item-detail.js',
	"/statics/backend/js/item/item_copy.js" ,
	'/statics/backend/js/item/item-category.js',
	'/statics/backend/js/item/item-attribute.js'
	] 
	css=[
	'/statics/backend/css/style.css','/statics/select2/css/select2.css',
	'/statics/select2/css/common.css' ] >
	<style type="text/css">
		.ke-dialog{
			top:15%;
		}
	</style>
<input type="hidden" value="${sessionId}" id="sessionId" />
<form id="inputForm" action="saveDetail.htm" method="post" enctype="multipart/form-data">	
<input type="hidden" id="bucketname" name="bucketname" value="${bucketname}">
<input type="hidden" id="bucketURL" name="bucketURL" value="${bucketURL}">
<#include "/item/subpages/add_spu.ftl">
<div class="box_border">
	<div class="box_top">
		<b class="pl15">商品信息</b>
	</div>
	
	<div class="box_center">
		<table class="input tabContent">
				<tr><td class="td_right">prdid:</td>
					 <td >
						<#if detail.prdid?exists>
								${detail.prdid}
						<#else>
						</#if> 
					 </td>	
					  <td rowspan=5  class="td_right"><span class="requiredField">*</span>规格维度:</td>
					  <td  rowspan=5  style="width:40%">
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
					 <td class="td_right"><span class="requiredField">*</span>商品名称:</td>
					 <td  ><input type="text" class="input-text lh30" size="60"	name="itemDetail.itemTitle" 
					  value="<#if "${((detail.itemTitle))?xhtml}" =="" >${((detail.mainTitle))?xhtml}<#else>${((detail.itemTitle))?xhtml}</#if>" id="itemTitle" maxlength=60  onMouseOver="this.title=this.value" /></td>
				</tr>
				<tr>
					 <td class="td_right"><span class="requiredField">*</span>网站显示名称:</td> 
					 <td  ><input type="text" class="input-text lh30" size="60"	name="itemDetail.mainTitle"  value="${((detail.mainTitle))?xhtml}" id="mainTitle" maxlength=60   onMouseOver="this.title=this.value" /></td>
				</tr>
				<tr>
					<td class="td_right"><span class="requiredField">*</span>仓库名称:</td>
					<td   ><input type="text" class="input-text lh30" size="60"	name="itemDetail.storageTitle"  
					value="<#if "${((detail.itemTitle))?xhtml}" =="" >${((detail.mainTitle))?xhtml}<#else>${((detail.storageTitle))?xhtml}</#if>" id="storageTitle" maxlength=60 onMouseOver="this.title=this.value"	/></td>
				</tr>
				<tr>
					 <td class="td_right">备注:</td>
					 <td ><input type="text" class="input-text lh30" size="60"	name="itemDetail.remark"  value="${((detail.remark))?xhtml}" id="detailRemark" maxlength=60 onMouseOver="this.title=this.value" /></td>
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
					<textarea name="itemDetail.subTitle" cols="30" rows="3" id="subTitle" maxlength=100 onMouseOver="this.title=this.value" >${((detail.subTitle))?xhtml}</textarea></td>
				<td class="td_right"><span class="requiredField">*</span>市场价:</td>
				<td><input type="text" name="itemDetail.basicPrice" value="${detail.basicPrice}" id="basicPrice"  maxlength=9
					class="input-text lh30" size="10"></td>
				<td class="td_right">生产厂家:</td>
				<td><input type="text" name="itemDetail.manufacturer" value ="${((detail.manufacturer))?xhtml}" maxlength=100	class="input-text lh30" size="25" onMouseOver="this.title=this.value" /></td>
			</tr>
			<tr>
				<td class="td_right"><span class="requiredField">*</span>运费模板:</td>
				<td class="">
				  <span class="fl">
					<select	name="itemDetail.freightTemplateId" class="select">
						<#list freightTemplateList as t>
							<option value="${t.id}" <#if "${detail.freightTemplateId}"=="${t.id}"> selected</#if> >${t.name}</option>
						</#list>
					</select>
				  </span>
				</td>
				<td class="td_right"><span class="requiredField">*</span>进项税率:</td>
				<td class=""><span class="fl">
				   <select name="itemDetail.purRate" class="select">
							<#list purRateList as t>
								<option value="${t.id}">${t.rate}%</option>
							</#list>
					</select>
				</span></td>
				<!--
				<td class="td_right"><span class="requiredField">*</span>是否进口商品：</td>
				<td>
					  <select name="itemDetail.importedSign" class="select">
					 			<option value="1"  <#if "${detail.unit}"=="1"> selected</#if>>否</option>
								<option value="2"  <#if "${detail.unit}"=="2"> selected</#if>  >是</option>
						</select>
				</td>-->
			</tr>
			<tr>
				<td class="td_right"><span class="requiredField">*</span>商品类型:</td>
				<td class="">
				  <span class="fl">
				  	<select	name="itemDetail.itemType" class="select">
						<option value="1" <#if "${detail.itemType}"=="1"> selected</#if>>正常商品</option>
						<option value="2" <#if "${detail.itemType}"=="2"> selected</#if>>服务商品</option>
						<option value="3" <#if "${detail.itemType}"=="3"> selected</#if>>二手商品</option>
						<option value="4" <#if "${detail.itemType}"=="4"> selected</#if>>报废商品</option>
					</select>
				  </span>
				</td>
				<td class="td_right">体积:</td>
				<td class="">
				<input type="text" class="input-text lh30" size="5" style="line-height:normal;text-align:center;" name="itemDetail.volumeLength" value="${detail.volumeLength}" id="volumeLength" maxlength=9 placeholder="长"  />
				<input type="text" class="input-text lh30" size="5"	style="line-height:normal;text-align:center;" name="itemDetail.volumeWidth"  value="${detail.volumeWidth}"  id="volumeWidth" maxlength=9 placeholder="宽"  />
				<input type="text" class="input-text lh30" size="5"	style="line-height:normal;text-align:center;" name="itemDetail.volumeHigh"  value="${detail.volumeHigh}" id="volumeHigh" maxlength=9 placeholder="高"  />&nbsp;cm
				</td>
				<td class="td_right"><span class="requiredField">*</span>销项税率:</td>
				<td class="">
				  <span class="fl">
				  	<select	name="itemDetail.saleRate" class="select">
						<#list saleRateList as t>
							<option value="${t.id}"   <#if "${detail.saleRate}"=="${t.id}"> selected</#if> >${t.rate}%</option>
						</#list>
					</select>
				  </span>
				</td>
			</tr>
			<tr>
				<td class="td_right">规格:</td>
				<td ><input type="text" class="input-text lh30" size="25"	name="itemDetail.specifications" value="${((detail.specifications))?xhtml}" id="specifications" maxlength=60 onMouseOver="this.title=this.value" /></td>
				<td class="td_right"><span class="requiredField">*</span>毛重:</td>
				<td><input type="text" class="input-text lh30" size="5" value="${detail.weight}" id="weight" maxlength=9 name="itemDetail.weight" />&nbsp;g</td>
				<td class="td_right">箱规:</td>
				<td ><input type="text" class="input-text lh30" size="25"	name="itemDetail.cartonSpec"  value="${((detail.cartonSpec))?xhtml}" id="cartonSpec" maxlength=60 onMouseOver="this.title=this.value" /></td>	
			</tr>
			<tr>
				<td class="td_right"><span class="requiredField">*</span>无理由退货天数:</td>
				<td><input type="text" class="input-text lh30" size="5"	name="itemDetail.returnDays" value="${detail.returnDays}"  id="returnDays" maxlength="5" /></td>
				<td class="td_right"><span class="requiredField">*</span>净重:</td>
				<td><input type="text" class="input-text lh30" size="5" value="${detail.weightNet}" id="weightNet" maxlength=9 name="itemDetail.weightNet" />&nbsp;g</td>
				<td class="td_right"><span class="requiredField">*</span>条形码:</td>
				<td style="width:15%"><input type="text" class="input-text lh30" size="20"	name="itemDetail.barcode"  value="${detail.barcode}" id="barcode" maxlength=20  /></td>
			</tr>
			<tr>
				<td class="td_right"><span class="requiredField">*</span>商品净数量(计量单位对应的数量,如三罐装奶粉,则填写3):</td>
				<td><input type="text" class="input-text lh30" size="5"	name="itemDetail.unitQuantity" value="${detail.unitQuantity}"  id="unitQuantity" maxlength="5" /></td>
				<td class="td_right"><span class="requiredField">*</span>商品独立包装数量(不考虑计量单位):</td>
				<td><input type="text" class="input-text lh30" size="5"	name="itemDetail.wrapQuantity" value="${detail.wrapQuantity}"  id="wrapQuantity" maxlength="5" /></td>				
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
					<select name="itemDetail.wavesSign" class="select" id="wavesSignId">
					  			 <option value="1" <#if "${detail.wavesSign}"=="1"> selected</#if> >否</option> 
								<option value="2" <#if "${detail.wavesSign}"=="2"> selected</#if> >是</option> 
					</select>
				  </span>
				</td>
				<td >
				  <span class="fl">
				  <span class="requiredField ml20">*</span>是否有效期管理:
					<select name="itemDetail.expSign"  id="selectExpSign" class="select">
					   			<option value="2" <#if "${detail.expSign}"=="2"> selected</#if> >否</option>
								<option value="1" <#if "${detail.expSign}"=="1"> selected</#if> >是</option>
					</select>
				  </span>	
				</td>
				<td class="expSignClass" nowrap='nowrap'><input type="text" class="input-text lh30 ml20" size="5" value="${detail.expDays}" name="itemDetail.expDays" id="inputExpDays" maxlength="5" />有效期月数</td>
				<td  nowrap='nowrap'>
				  <span class="fl">
				  <span class="requiredField ml20">*</span>适用年龄: 
					<select name="itemDetail.applyAgeId"  id="applyAgeId" class="select2" style="width:150px;"> 
								<option value="" >请选择</option>
								<#list applyAgeList as t>
									<option value="${t.id}" <#if "${detail.applyAgeId}"=="${t.id}"> selected</#if> >${t.name}</option>
								</#list>
					</select>
				  </span>	
				</td>
				<td  class="triggerCountryClass"  nowrap='nowrap'>
				  <span class="fl">
				  <span class="requiredField ml20">*</span>产地:
					<select name="itemDetail.countryId"  id="countryId" class="select2"  style="width:200px; margin-left: 1px" > 
								<option value="">--请选择产地--</option>
								<#list listCountry as country>
									<option value="${country.id}"   <#if "${detail.countryId}"=="${country.id}"> selected</#if>   >${country.name}</option>
								</#list>
					</select>
				  </span>	
				</td>
			</tr>
			
			<tr> 
				<td class="tarriRateClass" style="padding-left:20px;" nowrap='nowrap'>
					  <span class="fl" >
					  <span class="requiredField">*</span>行邮税税率:
					  	<select	name="itemDetail.tarrifRate" id="tarrifRate" class="select2"  style="width:250px;"> 
					  		<option value="">--请选择行邮税税率--</option>
							<#list tarrifList as t>
								<option value="${t.id}" <#if "${detail.tarrifRate}"=="${t.id}"> selected</#if> >${t.code}-${t.rate}%-${t.remark}</option>
							</#list>
						</select>
					  </span>
					</td>
				<td class="customsListClass" style="padding-left:20px;" nowrap='nowrap' colspan="2">
				  <span class="fl" >
				  <span class="requiredField">*</span>关税税率:
				  	<select	name="itemDetail.customsRate" id="customsRate" class="select2"  style="width:200px;"> 
				  		<option value="">--请选择关税税率--</option>
						<#list customsList as t>
							<option value="${t.id}" <#if "${detail.customsRate}"=="${t.id}"> selected</#if> >${t.code}-${t.rate}%-${t.remark}</option>
						</#list>
					</select>
				  </span>
				</td>
				<td class="exciseRateClass" style="padding-left:20px;" nowrap='nowrap'>
				  <span class="fl" >
				  <span class="requiredField">*</span>消费税税率:
				  	<select	name="itemDetail.exciseRate" id="exciseRate" class="select2"  style="width:200px;"> 
				  		<option value="">--请选择消费税税率--</option>
						<#list exciseList as t>
							<option value="${t.id}" <#if "${detail.exciseRate}"=="${t.id}"> selected</#if> >${t.code}-${t.rate}%-${t.remark}</option>
						</#list>
					</select>
				  </span>
				</td>
				<td class="addedValueRateClass" style="padding-left:20px;" nowrap='nowrap'>
				  <span class="fl" >
				  <span class="requiredField">*</span>增值税税率:
				  	<select	name="itemDetail.addedValueRate" id="addedValueRate" class="select2"  style="width:200px;"> 
				  		<option value="">--请选择增值税税率--</option>
						<#list addedValueList as t>
							<option value="${t.id}" <#if "${detail.addedValueRate}"=="${t.id}"> selected</#if> >${t.code}-${t.rate}%-${t.remark}</option>
						</#list>
					</select>
				  </span>
				</td>
			</tr>
		</table>
	</div>
	
	<#include "/item/subpages/add_attribute.ftl">
	<#include "/item/subpages/add_dummy_attribute.ftl">
	<div class="box_border">
		<div class="box_top">
			<b class="pl15">
			图片与详情
			</b>
			
		</div>
		<#include "/item/subpages/add_pictures.ftl">
		<#include "/item/subpages/add_description.ftl">
		<input type="button" id="detailPicPreview"   value="详情图片预览" class="ext_btn ext_btn_submit m10" " />
		<input type="button" id="copyPicAndDetail"   value="复制" class="ext_btn ext_btn_submit m10" " />
		<span>复制图片、详情到不同的Prd下面</span>		
	</div>
	
	
	<div class="box_border">
		<div class="box_top">
			<b class="pl15">供应商信息</b>
		</div>
		<div class="box_center">
		<div class="">
			<!-- 如果prdid已经关联了自营（西客商城）的商家，就不能再选择了 -->
			<input type="button" id="selectSupplierbtn"  value="选择供应商" class="ext_btn ext_btn_submit m10" status="${hasXgSeller}">
		</div>
		<table class="input tabContent" id="suppListTable">
			<tr>
				<th width="60">序号</th>
				<th width="60">SKU编号</th>
				<th width="200">商家名称</th>
				<th width="100">商家商品码（条形码）</th>
				<th width="60">市场价</th>
				<th width="60">上下架</th>
				<th width="60">类型</th>
				<th width="100" style="text-align:left"><input type="checkbox" id="chooseItemStatus">批量上架</th>
			</tr>
			<#list detailSkuList as l >
			<tr class="skuList">
			<td width="60">
			<input type="text" class="input-text lh30 ml20" size="5" value="${l.sort}" maxlength=4 name="sort" />
			</td>
			<td width="60">${l.sku}</td>
				<input type="hidden" name="skuId" value="${l.id}" />
				<input type="hidden" name="spId" value="${l.spId}" />
				<td width="200">${l.spName}</td>
				<td width="200">${l.barcode}</td>
				<td width="60"><input type="text" class="input-text lh30 ml20" size="5" value="${l.basicPrice}" maxlength=9 name="skuBasicPrice" /></td>
				<td width="60">
					<#if "${l.status}"=="0"> 未上架</#if>
					<#if "${l.status}"=="1"> 已上架</#if>
				    <#if "${l.status}"=="2"> 作废</#if>
				</td>
				<td width="60">
					<#if "${l.saleType}"=="0">
						西客商城
					</#if>
					<#if "${l.saleType}"=="1">
						商家
					</#if>
					
				</td>
				<!--操作类型 自营的，可以添加供应商-->
			    <td width="100">
			    	<input type="hidden" name="status" value="${l.status}">
			    	<#if "${l.status}"=="2">
			    		已作废
			    	<#else>
				    	<input type="checkbox" name="statusCheckbox" <#if "${l.status}"=="1"> checked </#if> >上架
				    	<a id="${l.id}" href="javascript:void(0);" class="cancelSkuBtn">[作废]</a>
				   		<#if l.saleType==0 >
							<a id="${l.id}" href="javascript:void(0);" class="querySkuSupplier">[供应商]</a>
						<#else>
						</#if>
				   		<#if detail.wavesSign==2 > 
							<a id="${l.id}" href="javascript:void(0);" class="querySkuArtNumber">[备案信息]</a>
						</#if>
					</#if>  
				</td>
			</tr>	
			</#list>
			
		</table>
	 </div>
	 
	 <div class="tc" style="    padding-bottom: 100px">
		<input type="button" id="inputFormSaveBtn" style="text-align:right;" value="保存" class="ext_btn ext_btn_submit m10">
		<input type="hidden" value="${detailId}" name="itemDetail.id"  id="detailId"/>
		<input type="hidden" name="skuListJson"  id="skuListJson"/>
		<input type="hidden" name="attrListJson"  id="attrListJson"/>
		<input type="hidden" name="attrItemJson"  id="attrItemJson"/>
		<input type="hidden" name="dummyAttrListJson"  id="dummyAttrListJson"/>


		<input type="hidden" id="formToken" name="formToken" value="${formToken}">
		<input type="button" value="返回" onclick="location.href='list.htm'"	class="ext_btn m10">
	 </div>
</div>
</form>
</@backend>