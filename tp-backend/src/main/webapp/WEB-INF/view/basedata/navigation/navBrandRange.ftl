<#include "/common/common.ftl"/>

<@backend title=""
js=[  '/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js',
'/statics/backend/js/basedata/bse_navigation_brand_range.js'
]
css=[
'/statics/backend/css/style.css',
'/statics/select2/css/select2.css',
'/statics/select2/css/common.css'
] >

<div class="box_border">

    <!-- 基础信息  -->
    <div class="box_center">
        <input type="hidden" id="largeIdHidden" value="${item.itemInfo.largeId}"/>
        <input type="hidden" id="mediumIdHidden" value="${item.itemInfo.mediumId}"/>
        <input type="hidden" id="smallIdHidden" value="${item.itemInfo.smallId}"/>
        <input type="hidden" id="categoryId" value="${categoryId}"/>
        <input type="hidden" id="subTableId" value="${subTableId}"/>
        <input type="hidden" id="type" value="${type}"/>
        <input type="hidden" id="baseSort" value="${baseSort}">
        <input type="hidden" id="rangeList" value="${rangeList}">

        <input type="hidden" id="defBrands" value="${brands}"/>
        <input type="hidden" id="defCategories" value="${categories}"/>
        <table class="input commContent">
            <!--初始化列表查询-->

            <tr id="exist_allways">
                <br/>
                   		   		   <span id="coupon_range_span">
                   		   		   <span id="category_span_1" <#if type==2 > style="display: none" </#if>>
                   		   		  	<span id="category_span_0" name="category_span_2">
                   		   		  	 <span name="add_category" title="增加"
                                           style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                           class="add_coupon_icon">  </span>|
									 <span name="remove_category" title="删除"
                                           style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                           class="remove_coupon_icon">  </span>
	                   		   		  <span style="margin-left:20px;margin-top:20px">类别：</span>
	                   		   		  <span style="margin-left:20px;margin-left:20px">大类</span>
	                   		   		  <select class="select largeIdSel" name="" style="margin-left:10px;width:120px;">
                                          <option value="">--请选择分类--</option>
                                          <#list categoryList as category>
                                              <option value="${category.id}">${category.name}</option>
                                          </#list>
                                      </select>
	                   		   		   <span style="margin-left:10px;">中类：</span>
	                   		   		   <select class="select mediumIdSel" name="" style="width:120px;">
                                           <option value="">--请选择分类--</option>
                                       </select>
								  	   <span style="margin-left:10px;">小类：</span>
	                   		   		   <select class="select smallIdSel" name="" style="width:120px;">
                                           <option value="">--请选择分类--</option>
                                       </select>
                                        </br>
                                        </br>
								  	</span>
								  </span>
								  <span id="brand_span_1">
								  <span id="brand_span_2" name="brand_span_2">
								     <span id="add_brand" name="add_brand" title="增加"
                                           style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                           class="add_coupon_icon">  </span>|
									 <span id="remove_brand" name="remove_brand" title="删除"
                                           style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                           class="remove_coupon_icon">  </span>
								 	 <span style="margin-left:20px;margin-top:50px">品牌：</span>
                                      <select class="select2" style="width: 160px" name="brand">
                                          <option value >--请选择--</option>
                                          <#list brandList as brand>
                                              <option value="${brand.id}">${brand.name}</option>
                                          </#list>
                                      </select>排序:<input type="text" maxlength="10" name="sort" class="input-text lh25"  style="width: 80px; text-align: right" name="sort">

                                      </br>
                                      </br>
								  </span>
								  </span>
								   <span id="sku_span_1" style="display: none">
								   <span id="sku_span_2" name="sku_span_2">
								        <span id="add_sku" name="add_sku" title="增加"
                                              style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                              class="add_coupon_icon">  </span>|
									    <span name="remove_sku" title="删除"
                                              style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                              class="remove_coupon_icon">  </span>
									   <span style="margin-left:20px;margin-top:50px">商品：</span>
									   <select class="select itemLevelSelect" name="item_sku"
                                               style="width:50px;margin-left:20px;">
                                           <option value="0">sku</option>
                                       </select>
								   		<input type="text" size="40" class="input-text lh30"
                                               style="width:120px;margin-left:20px;" maxlength="20" name="typeCode"
                                               id="default_sku"/>
                                       </br>
                                       </br>
								   </span>
								   </span>
                                       <!-- copy 模板-->
								   <span id="category_span_copy" style="display:none;">
                   		   		  	<span id="category_span_0" name="category_span_2">
                   		   		  	    <span name="add_category" title="增加"
                                              style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                              class="add_coupon_icon">  </span>|
										<span name="remove_category" title="删除"
                                              style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                              class="remove_coupon_icon">  </span>
	                   		   		   <span style="margin-left:20px;margin-top:20px">类别：</span>
	                   		   		   <span style="margin-left:20px;margin-left:20px">大类</span>
	                   		   		   <select class="select largeIdSel" name="" style="margin-left:10px;width:120px;">
                                           <option value="">--请选择分类--</option>
                                           <#list categoryList as category>
                                               <option value="${category.id}">${category.name}</option>
                                           </#list>
                                       </select>
	                   		   		   <span style="margin-left:10px;">中类：</span>
	                   		   		   <select class="select mediumIdSel" name="" style="width:120px;">
                                           <option value="">--请选择分类--</option>
                                       </select>
								  	   <span style="margin-left:10px;">小类：</span>
	                   		   		   <select class="select smallIdSel" name="" style="width:120px;">
                                           <option value="">--请选择分类--</option>
                                       </select>
                                        </br>
                                        </br>
								  	</span>
								  </span>
								  <span id="brand_span_copy" style="display:none;">
								  <span id="brand_span_2" name="brand_span_2">
								   <span id="add_brand" name="add_brand" title="增加"
                                         style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                         class="add_coupon_icon">  </span>|
									 <span id="remove_brand" name="remove_brand" title="删除"
                                           style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                           class="remove_coupon_icon">  </span>
								 	 <span style="margin-left:20px;margin-top:50px">品牌：</span>
								 	 <select class="select2copy" style="width: 160px" name="brand">
                                         <option value >--请选择--</option>
                                         <#list brandList as brand>
                                             <option value="${brand.id}">${brand.name}</option>
                                         </#list>
                                     </select> 排序:<input type="text" maxlength="10" name="sort" class="input-text lh25" style="width: 80px; text-align: right" name="sort">
                                      </br>
                                      </br>
								  </span>
								  </span>
								  <span id="sku_span_copy" style="display:none;">
								   <span id="sku_span_2" name="sku_span_2">
								       <span id="add_sku" name="add_sku" title="增加"
                                             style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                             class="add_coupon_icon">  </span>|
									    <span name="remove_sku" title="删除"
                                              style="margin-left: 10px;margin-right: 10px;cursor: pointer"
                                              class="remove_coupon_icon">  </span>
									   <span style="margin-left:20px;margin-top:50px">商品：</span>
									   <select class="select itemLevelSelect" name="item_sku"
                                               style="width:50px;margin-left:20px;">
                                           <option value="0">sku</option>
                                       </select>
								   		<input type="text" size="40" class="input-text lh30"
                                               style="width:120px;margin-left:20px;" maxlength="20" name="typeCode"/>
                                       </br>
                                       </br>
								   </span>
								   </span>
								   </span>

                <input class="btn btn82 btn_save2" type="button" id='submit_nav_range' value="保存"/>

                <input class="btn btn82 btn_nochecked closebtn" type="button" value="取消" id="buttoncancel"/ >

            </tr>

        </table>
    </div>
</div>
</@backend>