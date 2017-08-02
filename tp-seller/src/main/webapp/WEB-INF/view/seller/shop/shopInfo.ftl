<#include "/layout/inner_layout.ftl" />
<@sellContent title="" js=[
	'/static/seller/js/editor/kindeditor-all.js',
	'/static/seller/js/jquery.tools.js',
	'/static/seller/js/layer/layer.min.js',
	'/static/seller/editorUtil.js', 
	'/static/seller/js/tab.js',
	'/static/seller/js/form.js',
	'/static/seller/js/util.js',
	'/static/seller/js/jquery.cookie.js',
	'/static/select2/js/select2.js',
	'/static/select2/js/select2Util.js',
	'/static/select2/js/select2_locale_zh-CN.js',
	'/static/seller/js/item/item-select2.js',
	'/static/qiniu/js/plupload/plupload.full.min.js',
    '/static/qiniu/js/plupload/plupload.dev.js',
    '/static/qiniu/js/plupload/moxie.js',
	'/static/qiniu/js/plupload/moxie.js',
	'/static/qiniu/src/qiniu.js',
	'/static/qiniu/js/highlight/highlight.js',
	'/static/qiniu/js/ui.js',
	'/static/qiniu/xgUplod.js',
	'/static/seller/js/shop/picture_upload.js',
	'/static/seller/js/shop/mobile_picture_upload.js',
	'/static/seller/js/shop/sellerShop.js',
	'/static/seller/js/shop/editorUtils.js',
	'/static/seller/js/shop/shop_picture_upload.js'
	] 
	css=[
	'/static/seller/css/style.css','/static/select2/css/select2.css',
	'/static/select2/css/common.css',
	'/static/themes/msgbox.css',
	'/static/seller/css/style.css',
	'/static/seller/css/main.css',
	'/static/seller/css/common.css'
	]  >
	 <div class="box_border">
       <div class="box_top"><b class="pl15">供应商店铺信息</b></div>
       <div class="box_center">
		<form id="inputForm" action="save.htm" method="post" enctype="multipart/form-data">
			<input type="hidden" id="bucketname" name="bucketname" value="${bucketname}">
			<input type="hidden" id="id" name="id" value="${supplierShop.id}">
			<input type="hidden" id="bucketURL" name="bucketURL" value="${bucketURL}">
			<input type="hidden" id="bucketURL" name="bucketURL" value="${bucketURL}">
			<input type="hidden" id="supplierId" name="supplierId" value="${supplierId}">
			<!-- 基础信息  -->
		<div id="container" >
		<table class="input commContent"  >
				<tr>
                  <td class="td_right"><strong style="color:red;">*</strong>店铺名称：</td>
                  <td class="" colspan=5 > 
                    <input type="text" name="shopName" id="shopName" class="form-control" maxlength="12"  value="${supplierShop.shopName}" >
                  </td>
                 
                </tr>
                
                <tr>
	                   <td class="td_right">店铺地址</td>
	                   <td class="" > 
	                      <input type="text" name="shopAddress" class="form-control" size="40" maxlength="24" value="${supplierShop.shopAddress}" >
	                   </td>
	                   
	                   <td class="td_right">经度</td>
	                   <td class=""> 
	                   	 <input type="text" name="longitude" class="form-control" size="40" value="${supplierShop.longitude}" >
	                   </td>
	                 
	                   <td class="td_right">纬度</td>
	                   <td class=""> 
	                     <input type="text" name="latitude" class="form-control" size="40" value="${supplierShop.latitude}" >
	                    </td>
                </tr>
                 <tr>
                 
	                  <td class="td_right">店铺电话</td>
	                  <td class=""> 
	                      <input type="text" name="shopTel" class="form-control" size="40" value="${supplierShop.shopTel}" >
	                  </td>
	                  <td class="td_right">营业时间</td>
	                  <td class=""> 
	                     <input type="text" name="businessTime"  value="${supplierShop.businessTime}" class="form-control"  type="text" >
	                  </td>
	                  
	                   <td class="td_right"></td>
	                  <td class=""> 
	                  </td>
                 
                </tr>
                
                <tr>
                  <td class="td_right" >logo图片<br><p style="color:red">(尺寸: 100*100)</p></td>
                  <td class=""> 
                    <input type="hidden" name="logoPath" id="logoPath"  size="40"  value="${supplierShop.logoPath}">
                     <img  id="imglogoPath" width="120"  height="120" <#if (supplierShop.logoPath != null && supplierShop.logoPath!="") >  src="${bucketURL}/${supplierShop.logoPath}"  <#else> src=""  </#if> />
                     <a class="ext_btn ext_btn_submit m10" id="pickfiles"   href="#" imagenameattribute="picList">
		                <i class="glyphicon glyphicon-plus"></i>
		                <span class="legend">添加图片</span>
                     </a>
                  </td>
                  
                  
                  <td class="td_right" >店铺图片<br><p style="color:red">(尺寸: 750*400)</p></td>
                  <td class=""> 
                    <input type="hidden" name="mobileImage" id="mobileImage"  size="40"  value="${supplierShop.mobileImage}" >
                    <img  id="imgMobileImage" width="100"  height="100" <#if supplierShop.mobileImage != "">  src="${bucketURL}/${supplierShop.mobileImage}" <#else> src=""  </#if>  />
                     <a class="ext_btn ext_btn_submit m10" id="mobilePickfiles"   href="#" imagenameattribute="picList">
		                <i class="glyphicon glyphicon-plus"></i>
		                <span class="legend">添加图片</span>
                     </a>
                  </td>
                 
                 
                    <td class="td_right" >店铺top图片<br><p style="color:red">(尺寸: 710*146)</p></td>
                  <td class=""> 
                    <input type="hidden" name="shopImagePath" id="shopImagePath"  size="40"  value="${supplierShop.shopImagePath}" >
                    <img  id="imgShopMobileImage" width="100"  height="100" <#if supplierShop.shopImagePath != "">  src="${bucketURL}/${supplierShop.shopImagePath}" <#else> src=""  </#if>  />
                     <a class="ext_btn ext_btn_submit m10" id="shopImagePickfiles"   href="#" imagenameattribute="picList">
		                <i class="glyphicon glyphicon-plus"></i>
		                <span class="legend">添加图片</span>
                     </a>
                  </td>
                </tr>
                <tr>
		                  <td class="td_right">搜搜标签1</td>
		                  <td class=""> 
		                    <input type="text" name="searchTitle1" class="form-control" size="40" value="${supplierShop.searchTitle1}"  >
		                  </td>
		                 
		                  <td class="td_right">搜搜标签2</td>
		                  <td class=""> 
		                    <input type="text" name="searchTitle2" class="form-control" size="40" value="${supplierShop.searchTitle2}" >
		                  </td>
		                  
		                   <td class="td_right">搜搜标签3</td>
		                  <td class=""> 
		                    <input type="text" name="searchTitle3" class="form-control" size="40" value="${supplierShop.searchTitle3}"  >
		                  </td>
                  
                 
                </tr>
                
                 <tr>
		                  <td class="td_right">搜搜标签4</td>
		                  <td class=""> 
		                    <input type="text" name="searchTitle4" class="form-control" size="40" value="${supplierShop.searchTitle4}" >
		                  </td>
		                 
		                  <td class="td_right"></td>
		                  <td class=""> 
		                    
		                  </td>
		                  
		                   <td class="td_right"></td>
		                  <td class=""> 
		                     
		                  </td>

                 
                </tr>
                
                 
                
                 <tr style="height:500px">
		<td class="td_right">专场介绍</td>
		<td colspan="5" >
			<table cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicImages" style="width:100%,height:500px" >
				<tbody>
					<tr>
						<td style="width:80%">
							<textarea class="editor" id="phoneContentEditor" name="introMobile" style="width: 100%;height:500px;">${supplierShop.introMobile!}</textarea>
						</td>

						<td style="width:80px;text-align:center;">
							<input type="button" id="previewPhoneContent" class="ext_btn ext_btn_submit m10" value="浏览"/>
						</td>
					</tr>
				</tbody>
			</table>
		</td>
		</table>
		
		</div>
		<div class="tc">
				<input type="button" value="保存" id= "dataFormSave" class="ext_btn ext_btn_submit m10" />
		</div>
	</form>
		</div>
	</div>
	</@sellContent>