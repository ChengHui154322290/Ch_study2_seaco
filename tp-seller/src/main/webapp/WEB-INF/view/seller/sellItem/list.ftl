<#include "/layout/inner_layout.ftl" />
<@sellContent title="首页" 
    js=['/static/seller/js/item_list.js',
    '/static/seller/js/item/item-category.js',
    '/static/seller/js/item/item-search.js',
    '/static/seller/js/item/item_copy.js'
    ]
    css=[
    ]>
	<!--初始化列表查询-->
	<input type="hidden" id="largeIdHidden" value="${largeId}" />
	<input type="hidden" id="mediumIdHidden" value="${mediumId}" />
	<input type="hidden" id="smallIdHidden" value="${smallId}" />
	<input type="hidden" id="supplierIdHidden" value="${supplierId}" />
	<input type="hidden" id="domainURL" value="${domain}" />
	<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">商品列表页面</h3>
    </div>
    <div class="panel-body">
    <form method="post" action="${domain}/item/list.htm" id="itemSearchForm">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
             	<tr>
				<td  style="width:100px !important;" nowrap='nowrap'>商品类别：</td> 
				<td colspan = '8' class="">
				<span class="fl"> 
					<#if item.info.id?exists>
							<input type="hidden" id="spuId" name="id" value="${item.info.id}" />
					</#if>
					<#if item.info.largeId?exists>
							${item.info.largeName}->${item.info.mediumName}->${item.info.smallName}
					<#else>
					<select
						name="largeId" class="select2" id="largeIdSel" style="width:200px; margin-left: 1px" >
							<option value=""  >--请选择分类--</option>
						<#list categoryList as category>
							<option value="${category.id}" <#if "${category.id}"=="${query.largeId}">selected</#if> >${category.name}</option>
						</#list>
					</select> 
					<select name="mediumId" class="select2" id="mediumIdSel" style="width:200px; margin-left: 1px">
							<option value="">--请选择分类--</option>
							<#if midCategoryList??>
								<#list midCategoryList as midc>
									<option value="${midc.id}"  <#if "${midc.id}"=="${query.mediumId}">selected</#if> >${midc.name} </option>
								</#list>
							</#if>
					</select> 
					<select name="smallId" class="select2" id="smallIdSel" style="width:200px; margin-left: 1px">
							<option value="">--请选择分类--</option>
						<#if smlCategoryList??>
							<#list smlCategoryList as sml>
								<option value="${sml.id}"  <#if "${query.smallId}"=="${sml.id}"> selected</#if> >${sml.name} </option>
							</#list>
						</#if>							
					</select>
					</#if>
				</span></td>
				</tr>
                <tr>
                  <td>SPU名称:</td>
                  <td><input type="text" name="spuName" value="${query.spuName}"  class="input-text lh25" size="20"></td>
                  <td>spu:</td>
                  <td><input type="text" name="spu" value="${query.spu}"  class="input-text lh25" size="20"></td>
                  <td>网站显示名称:</td>
                  <td>
                    <input type="text" name="name" value="${query.name}" class="input-text lh25" size="20">
                  </td>
                 
                </tr>
                <tr>
                <td>sku:</td>
                  <td>
                   	<input type="text" name="sku" value="${query.sku}" class="input-text lh25" size="20">
                  </td>
                <td>条形码:</td>
                  <td>
                    <input type="text" name="barcode" value="${query.barcode}" class="input-text lh25" size="20">
                </td>
	            <td>发布状态:</td>
	            <td>
	                <select class="select2" name="status" style="width:100px; margin-left: 1px" >
	                	<option value="">-- 全部 --</option>
	                	<option value="0" <#if "${status}"=="0">selected</#if> >未上架</option>
	                	<option value="1" <#if "${status}"=="1">selected</#if> >已上架</option>
	                	<!--<option value="2" <#if "${status}"=="2">selected</#if> >作废</option>-->
	                </select>
	            </td>
                </tr>
                <tr>
                  <td>创建日期:</td>
                  <td colspan="3">
                  	<input type="text" name="createBeginTime" id="createBeginTime" value="<#if query.createBeginTime??>${query.createBeginTime?string("yyyy-MM-dd")}</#if>" class="input-text lh25" size="20">
                  	<span>到</span>
                  	<input type="text" name="createEndTime" id="createEndTime" value="<#if query.createEndTime??>${query.createEndTime?string("yyyy-MM-dd")}</#if>" class="input-text lh25" size="20">
                  </td>
                  <td>商品类型:</td>
                  <td>
                    <select class="select2" name="itemType" style="width:150px; margin-left: 1px">
                    	<option value="">-- 全部 --</option>
                    	<#list itemTypes as item>
                    		<option value="${item.value}" <#if query.itemType==item.value>selected="selected"</#if>>${item.key}</option>
                    	</#list>
                    </select>
                  </td>
                  <td>单位:</td>
                  <td>
                    <select class="select2" name="unitId" style="width:100px; margin-left: 1px">
                    	<option value="">-- 全部 --</option>
                    	<#list unitList as item>
                    		<option value="${item.id}" <#if query.unitId==item.id>selected="selected"</#if>>${item.name}</option>
                    	</#list>
                    </select>
                  </td>
                  <td>品牌:</td>
                  <td>
                    <span class="fl">
				   		<select class="select2"
							name="brandId" id="brandIdSel" style="width:200px; margin-left: 1px">
								<option value="">--请选择品牌--</option>
								<#list brandList as brand>
								<option value="${brand.id}" <#if "${brand.id}"=="${query.brandId}"> selected</#if> >${brand.name}</option>
							</#list>
					   </select>		
					</span>
                  </td>
                </tr>
                
                <tr>
                	  <td>商品类型:</td>
			            <td>
			                <select class="select2" name="wavesSign" style="width:100px; margin-left: 1px" >
			                	<option value="">-- 全部 --</option>
			                	<option value="1" <#if "${wavesSign}"=="1">selected</#if> >国内</option>
			                	<option value="2" <#if "${wavesSign}"=="2">selected</#if> >海淘</option>
			                </select> 
			            </td>
                </tr>
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <input type="checkbox" name ="id" id="chkall"/> 
              	 <select class="select2" id="itemStatus" style="width:100px; margin-left: 1px">
                    	<option value="1">批量上架</option>
                    	<option value="0">批量下架</option>
                 </select>
              	 <a href="javascript:void(0);">
                  <input class="btn btn-primary btn-sm" type="button" value="预览" name="button"	onclick="previewSku()"  />
                 </a>               
                 <a href="javascript:void(0);"><input class="btn btn-primary btn-sm" onclick="itemFormSubmit('list')" type="button" value="查询" name="button" /></a>
                 <a href="javascript:void(0);">
                 	<input class="btn btn-primary btn-sm"  id="addItem" type="button" value="新增" name="button"/>
                 </a>
                 <!-- 
                 <input class="btn btn-primary btn-sm" type="button" value="复制" name="button" id="copyItemBtn"  />
                 -->
                 <a >
                   <input class="btn btn-primary btn-sm" id="itemImport" type="button" value="导入" name="button"  />
                 </a>
                 <a href="javascript:void(0);">
                  <input class="btn btn-primary btn-sm" type="button" value="导出" name="button"	onclick="itemFormSubmit('exportList')"  />
                 </a>
                 <!--
                 <a href="javascript:void(0);">
                  <input class="btn btn-primary btn-sm" type="button" value="修改" name="button"	onclick="batchEdit()"  />
                 </a>
                 -->
              </div>
            </div>
          </div>
        </div>
    </div>
</form>
    <div id="contentShow">
        
    </div
 </div><#-- panel-body -->
</div>

</@sellContent>