<#include "/common/common.ftl"/>
<@backend title="新增优惠券" js=[	
    '/statics/backend/js/layer/layer.min.js',
    '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',  
    '/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
     '/statics/backend/js/promotion/utils.js',
    '/statics/backend/js/editor/kindeditor-all.js',
	'/statics/backend/js/editorUtil.js', 
     '/statics/qiniu/js/plupload/plupload.full.min.js',
    '/statics/qiniu/js/plupload/plupload.dev.js',
    '/statics/qiniu/js/plupload/moxie.js',
	'/statics/qiniu/js/plupload/moxie.js',
	'/statics/qiniu/src/qiniu.js',
	'/statics/qiniu/js/highlight/highlight.js',
	'/statics/qiniu/js/ui.js',
	'/statics/qiniu/xgUplod.js',
	'/statics/backend/js/coupon/coupon_picture_upload.js',
    '/statics/backend/js/editorUtil.js',
	'/statics/backend/js/coupon/coupon_add.js']   
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
	] >
	 <div id="container" >
	  <input type="hidden" id="bucketname" name="bucketname" value="${bucketname}">
	 <input type="hidden" id="bucketURL" name="bucketURL" value="${bucketURL}">
     <table class="form_table pt15 pb15" border="0" cellpadding="0" cellspacing="0" width="1000px;">
         <tbody>
         		<tr>
	                  <td class="td_left">批次号：</td>
	                  <td >  <span style="margin-left:20px;margin-top:20px">自动生成</span>
									  <span id="">
	                   		   		  		<span style="margin-left:380px;">状态：</span>
	                   		   		  		<input  type="text" size="40"  class="input-text" style="width:80px;text-align: center;" name=""  value="编辑中" disabled/>
									  </span>
					</td>
               </tr>
               
               <tr>
                  <td style="width:120px;vertical-align: top;" align="left">*优惠券类型：</td>  
                   <td style="border: 1px dashed #BBBBBB;">        
                   		   		  <span style="margin-left:20px;margin-top:20px">优惠券面值：</span>
                   		   		  <input id="show_face_input" type="text" size="40"  class="input-text" style="width:80px;" name=""  value="" disabled/>
								   <span id="show_choose_over_value">
                   		   		  		<span style="margin-left:80px;">优惠券需满金额：</span>
                   		   		  		<input id="show_over_input" type="text" size="40"  class="input-text" style="width:80px;" name=""  value="" disabled/>
								  </span>
								  </br>
								  </br>
								  <span style="margin-left:20px;margin-top:50px">发行量：</span>
								  <input type="text" size="40" class="input-text lh30 couponCount" style="width:70px;margin-left:80px;" name="name"  maxlength="18"/>&nbsp;&nbsp;张（-1 表示不限量）
								  </br>
								  </br>
                   </td>
                </tr>
                <tr  id="type_choose" style="margin=top:20px;">   
              		<td align="left">*优惠券名称：</td>  
              		<td class="" style="margin-top:20px; "  >    
	                    <input name="couponType" type="radio" value="0"/>&nbsp;&nbsp;满减券
	                    <input id="over_type" type="text" size="40"  class="input-text" style="width:150px;margin-left:30px;" name="name"  value="输入满减券名称"/><span class="over_type_span">满 </span> 
						  <input id="over_type_input_over" type="text" size="40"  class="input-text" style="width:80px;display:inline-block;" name=""  value=""/>
						  <span class="over_type_span">元减</span> 
						  <input id="over_type_input_face" type="text" size="40"  class="input-text" style="width:80px;display:inline-block;" name=""  value=""/>
						   <span class="over_type_span">元 </span> 
						    <span class="over_type_remark">&nbsp;&nbsp;备注</span> 
						    <input  type="text" size="40"  class="input-text remark1" style="width:80px;margin-left:10px;" name="name"  maxlength="30"/>
	                    </br>
	                    </br>
	                     <input name="couponType" type="radio"  value="1">&nbsp;&nbsp;现金券 
	                     <input id="cash_type" type="text" size="40" class="input-text" style="width:150px;margin-left:30px;" name="name"  value="输入现金券名称"/>  
	                       <span class="cash_type_span">面值</span>
						  <input id="cash_type_input_face" type="text" size="40"  class="input-text" style="width:80px;display:inline-block;" name=""  value=""/>
						  <span class="exchangeXgMoney">&nbsp;&nbsp;是否允许兑换西客币</span>    
						  <select class="select exchangeXgMoney" name="exchangeXgMoney" id="exchangeXgMoney" style="width:100px;" >
                            <option value="0">否</option>
                            <option value="1">是</option>
                           </select>
						  
						  <span class="cash_type_remark">&nbsp;&nbsp;备注</span>    
						  <input  type="text" size="40"  class="input-text remark2" style="width:80px;margin-left:10px;" name="name"  maxlength="30"/> 
                	 </td>
                </tr>
                                <tr>
                 <td class="td_left" >优惠券图片<br><p style="color:red">(尺寸: 150*150)</p></td>
                  <td class=""> 
                    <input type="hidden" name="couponImagePath" id="couponImagePath"  size="40"  value="${couponDto.couponImagePath}" >
                    <img  id="imgShowCouponImagePath" width="100"  height="100" <#if couponDto.couponImagePath != "">  src="${bucketURL}/${couponDto.couponImagePath}" <#else> src=""  </#if>  />
                     <a class="ext_btn ext_btn_submit m10" id="couponImagePickfiles"   href="#" imagenameattribute="picList">
		                <i class="glyphicon glyphicon-plus"></i>
		                <span class="legend">添加图片</span>
                     </a>
                  </td>
                </tr>
                <tr>
                  <td class="td_left">*优惠券发放时间：</td>
                  <td class="">
 					<input type="text" id="coupon_release_stime" size="20" class="input-text lh20" name="name" readonly />&nbsp;&nbsp;到&nbsp;&nbsp;
 					<input type="text" id="coupon_release_etime" size="20" class="input-text lh20" name="name" readonly /> 
                  </td>
                 </tr>
                 
                 <tr>
                  <td class="td_left">*优惠券使用时间</td>
	                  <td class="">
	 					<input name="coupon_use_type" type="radio"  value="0"/>&nbsp;&nbsp;时间段有效&nbsp;&nbsp;
	 					<input name="coupon_use_type" type="radio" value="1"/>&nbsp;&nbsp;领取日起有效
	 					
		 				<input id="coupon_use_stime" type="text" size="20" class="input-text lh20" name="name" readonly /><span class="coupon_use_time">&nbsp;&nbsp;到&nbsp;&nbsp;</span>
	 					<input id="coupon_use_etime" type="text" size="20" class="input-text lh20" name="name" readonly /> 
	 					
	 					<input id="use_date_interval" type="text" size="20" class="input-text lh10" style="width:30px;" name="name" maxlength="18" /><span class="use_date_interval">日内<span style="color:red;margin-left:10px;"><strong>*</strong>领取有效天数必须大于等于1,等于1代表发放当天有效</span></span>
	 					
	                  </td>
                 </tr>
                <tr>
                  <td class="td_left">*适用范围：</td> 
                  <td class="">
                 	<input name="use_range" type="checkbox" value="0" id="all_platform"> 全部
                    <input name="use_range" type="checkbox" value="1" id="platform_self" > 自营+代销
                    <input name="use_range" type="checkbox" value="2" id="platform_union"> 联营
                  </td>
                </tr>
                <tr>
                  <td class="td_left">*适用平台：</td>
                  <td class="">
					  <#list  platformEnum as pf>
                          <li style="float:left;padding-right:20px;">
                              <label><input type="checkbox" name="platformCodes" id="" value="${pf.code}" readonly
								  <#list topicDetail.platformCodes as code >
									  <#if code = pf.code>
                                            checked
									  </#if>
								  </#list>

                              /> ${pf.name()} </label>
                          </li>
					  </#list>
                  </td>
                </tr> 
                <tr>
                  <td class="td_left">*适用海淘：</td>
                  <td class="">
                    <input name="hitao_sign" type="checkbox" value="0" id="both_hitao"> 全网
                    <input name="hitao_sign" type="checkbox" value="1" id="nohitao"> 非海淘
                    <input name="hitao_sign" type="checkbox" value="2" id="justhitao"> 海淘
                  </td> 
                </tr> 
                <tr>
                  <td class="td_left">*发券主体：</td>
                  <td class="">
                    <input name="source_type" class="source_type" type="radio" value="1" id="source_type_1"  checked="checked"> 西客商城
                    <input name="source_type" class="source_type"  type="radio" value="2" id="source_type_2"> 商户 
                    <span id="source_info" style="display:none;">
                    	<input type="text" name="source_id" id="source_id"  class="input-text source_id" style="width:50px;margin-left:30px;" value="商户ID">
                    	<input type="text" name="source_name" id="source_name" class="input-text source_name" style="width:150px;margin-left:30px;" value="商户名称">
                      	&nbsp;&nbsp;
						<input type="button" class="btn btn82 btn_save2 confirmSource" id="" value="确定" />
						<input type="button" class="btn btn82 btn_search searchSource" id="" value="查询" />
                    </span>
                  </td> 
                </tr>

                <tr>
                    <td class="td_left">*是否可领取：</td>
                    <td class="">
                        <select class="select offerType" name="offerType" id="offerType" style="width:100px;" >
                            <option value="1">可发放</option>
                            <option value="0">可发放可领取</option>
                            <option value="2">可领取</option>
                        </select>
                        <span id="showReceiveSpan" style="display:none">
                                                                      是否在领券专区显示     <input name="isShowReceive" type="radio"  value="2"/>&nbsp;&nbsp;是&nbsp;&nbsp;
	 					<input name="isShowReceive" type="radio" value="1" checked="checked"/>&nbsp;&nbsp;否
                        </span>
                    </td>
                </tr>

                <tr>
                    <td class="td_left">*仅支持扫码领取：</td>
                    <td class="">
                        <select class="select just_scan" name="just_scan" style="width:100px;" >
                            <option value="1">不限制</option>
                            <option value="2">仅支持扫码</option>
                        </select>
                    </td>
                </tr>
				<tr title="实体卡券请选择不激活,监制完成后再激活">
                    <td class="td_left">*是否立即激活：</td>
                    <td class="">
                        <input name="activeStatus" type="radio"  value="0"/>&nbsp;&nbsp;不激活&nbsp;&nbsp;
	 					<input name="activeStatus" type="radio" value="1" checked="checked"/>&nbsp;&nbsp;激活
                    </td>
                </tr>
                 <tr id="exist_allways">
	                  <td style="width:120px;vertical-align: top;" align="left">优惠商品范围：</td> 
	                  <td style="border: 1px dashed #BBBBBB;">    
                  				 <div id="all_items"><input type="checkbox" id="all_items_checkbox" value="0" />全品类 </div>     
                  				 <br/>  
                   		   		  <span id="coupon_range_span">    
                   		   		  <span id="category_span_1">
                   		   		  <input name="useScope" type="radio"  value="4" checked="checked"/>&nbsp;&nbsp;类别&nbsp;&nbsp;<br>
	                   		   		  	<span name="category_span_2">
		                   		   		    <span  name="add_category" title="增加" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
										   <span  name="remove_category" title="删除" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="remove_coupon_icon">  </span>
		                   		   		  <span style="margin-left:20px;margin-top:20px">类别：</span>
		                   		   		  <span style="margin-left:20px;margin-left:20px">大类</span>
		                   		   		  <select class="select largeIdSel" name="" style="margin-left:10px;width:100px;" >  
		   		   		  						<option value="">--请选择分类--</option>
												<#list categoryList as category>
													<option value="${category.id}">${category.name}</option>
												</#list>
										   </select>
		                   		   		   <span style="margin-left:10px;">中类：</span>
		                   		   		   <select class="select mediumIdSel" name="" style="width:100px;" >  
		                   		   		   			<option value="">--请选择分类--</option>
										   </select>
									  	   <span style="margin-left:10px;">小类：</span> 
		                   		   		   <select class="select smallIdSel" name="" style="width:100px;" >  
		                   		   		   			<option value="">--请选择分类--</option>
										   </select>
										  </br>
										  </br>
								  	</span>
								  </span>
								  <span id="brand_span_1">
								  <span name="brand_span_2">
								   <input name="useScope" type="radio"  value="3" checked="checked"/>&nbsp;&nbsp;品牌&nbsp;&nbsp;<br>
								  	 <span id="add_brand"  title="增加" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
									 <span id="remove_brand" name="remove_brand" title="删除" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="remove_coupon_icon">  </span>
								 	 <span style="margin-left:20px;margin-top:50px">品牌：</span>
								 	 <input type="text" class="input-text lh30 brandId" style="width:50px;margin-left: 20px;"  id="default_brand_id"/>
								  	 <input type="text" size="40" class="input-text lh30 brandName" style="width:120px;margin-left:20px;" name="name" />
									  &nbsp;&nbsp;
									  <input type="button" class="btn btn82 btn_save2 confirmBrand" id="" value="确定" />
									 <input type="button" class="btn btn82 btn_search searchBrand" id="" value="查询" />
								  	</br>
								  	</br>
								  </span>
								  </span>
								   <span id="sku_span_1">
								   <span  name="sku_span_2">
								     <input name="useScope" type="radio"  value="1" checked="checked"/>&nbsp;&nbsp;商品&nbsp;&nbsp;<br>
									    <span id="add_sku" name="add_sku" title="增加" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
									    <span  name="remove_sku" title="删除" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="remove_coupon_icon">  </span>
									   <span style="margin-left:20px;margin-top:50px">商品：</span>
									   <select class="select itemLevelSelect" name="item_sku"  style="width:50px;margin-left:20px;">  
												<option value="0">sku</option> 
									    </select>
								   		<input type="text" size="40" class="input-text lh30" style="width:120px;margin-left:20px;" maxlength="20" name="typeCode" />
										  </br>
										  </br>
								   </span>
								   </span>
								   <span id="topic_span_1">
								   <span  name="topic_span_2">
								   <input name="useScope" type="radio" value="2" />&nbsp;&nbsp;专场&nbsp;&nbsp;<br>
	 				 	                <span id="topicSelect" >专场(输入专场id) <input  type="text"   class="input-text lh30 brandId" style="width:200px;" id="topicId"  name="topicId" />(专场设置优惠券不支持选择不包含商品)</span>
								   </span>
								   </span>
								   
								   <!-- copy 模板-->
								   <span id="category_span_copy" style="display:none;">
	                   		   		  	<span  name="category_span_2">
	                   		   		  	 		<span  name="add_category" title="增加" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
										   		<span name="remove_category" title="删除" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="remove_coupon_icon">  </span>
			                   		   		   <span style="margin-left:20px;margin-top:20px">类别：</span>
			                   		   		   <span style="margin-left:20px;margin-left:20px">大类</span>
			                   		   		   <select class="select largeIdSel" name="" style="margin-left:10px;width:100px;" >  
			   		   		  						<option value="">--请选择分类--</option>
													<#list categoryList as category>
														<option value="${category.id}">${category.name}</option>
													</#list>
											   </select>
			                   		   		   <span style="margin-left:10px;">中类：</span>
			                   		   		   <select class="select mediumIdSel" name="" style="width:100px;" >  
			                   		   		   			<option value="">--请选择分类--</option>
											   </select>
										  	   <span style="margin-left:10px;">小类：</span> 
			                   		   		   <select class="select smallIdSel" name="" style="width:100px;" >  
			                   		   		   			<option value="">--请选择分类--</option>
											   </select>
										  </br>
										  </br>
									  	</span>
									  </span>
									  <span id="brand_span_copy" style="display:none;">
									  <span  name="brand_span_2">
									  	 <span id="add_brand"   title="增加"  style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
										 <span id="remove_brand" name="remove_brand" title="删除"  style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="remove_coupon_icon">  </span>
									 	 <span style="margin-left:20px;margin-top:50px">品牌：</span>
									 	 <input type="text" class="input-text lh30 brandId" style="width:50px; margin-left: 20px;"  />
									  	 <input type="text" size="40" class="input-text lh30 brandName" style="width:120px;margin-left:20px;" name="name" />
										  &nbsp;&nbsp;
										  <input type="button" class="btn btn82 btn_save2 confirmBrand" value="确定" />
										 <input type="button" class="btn btn82 btn_search searchBrand"  value="查询" />
									  	</br>
									  	</br>
									  </span>
									  </span>
									  <span id="sku_span_copy" style="display:none;">
									   <span name="sku_span_2">
										    <span id="add_sku" name="add_sku" title="增加"  style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
										    <span  name="remove_sku"  title="删除" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="remove_coupon_icon">  </span>
										   <span style="margin-left:20px;margin-top:50px">商品：</span>
										   <select class="select itemLevelSelect" name="item_sku"  style="width:50px;margin-left:20px;">  
													<option value="0">sku</option> 
										    </select>
									   		<input type="text" size="40" class="input-text lh30" style="width:120px;margin-left:20px;" maxlength="20" name="typeCode" />
											  </br>
											  </br>
									   </span>
									   </span>
								   </span>
								  <!--end  -->
                  	 </td>
              	  </tr>
              	  
              	  <!-- 不包含-->
              	  <tr id="exist_allways_no">
	                  <td style="width:120px;vertical-align: top;" align="left">不包含：</td> 
	                  <td style="border: 1px dashed #BBBBBB;">    
                  				 <br/>  
                   		   		  <span id="coupon_range_span_no">    
                   		   		  <span id="category_span_1_no">
	                   		   		  	<span name="category_span_2_no">
		                   		   		    <span  name="add_category_no" title="增加" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
										   <span   name="remove_category_no" title="删除" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="remove_coupon_icon">  </span>
		                   		   		  <span style="margin-left:20px;margin-top:20px">类别：</span>
		                   		   		  <span style="margin-left:20px;margin-left:20px">大类</span>
		                   		   		  <select class="select largeIdSel" name="" style="margin-left:10px;width:100px;" >  
		   		   		  						<option value="">--请选择分类--</option>
												<#list categoryList as category>
													<option value="${category.id}">${category.name}</option>
												</#list>
										   </select>
		                   		   		   <span style="margin-left:10px;">中类：</span>
		                   		   		   <select class="select mediumIdSel" name="" style="width:100px;" >  
		                   		   		   			<option value="">--请选择分类--</option>
										   </select>
									  	   <span style="margin-left:10px;">小类：</span> 
		                   		   		   <select class="select smallIdSel" name="" style="width:100px;" >  
		                   		   		   			<option value="">--请选择分类--</option>
										   </select>
										  </br>
										  </br>
								  	</span>
								  </span>
								  <span id="brand_span_1_no">
								  <span name="brand_span_2_no">
								  	 <span id="add_brand_no"  title="增加" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
									 <span id="remove_brand_no" name="remove_brand_no" title="删除" style="margin-left: 10px;margin-right: 10px;cursor: pointer"  class="remove_coupon_icon">  </span>
								 	 <span style="margin-left:20px;margin-top:50px">品牌：</span>
								 	 <input type="text" class="input-text lh30 brandId" style="width:50px;margin-left: 20px;"  id="default_brand_id"/>
								  	 <input type="text" size="40" class="input-text lh30 brandName" style="width:120px;margin-left:20px;" name="name" />
									  &nbsp;&nbsp;
									  <input type="button" class="btn btn82 btn_save2 confirmBrand" id="" value="确定" />
									 <input type="button" class="btn btn82 btn_search searchBrand" id="" value="查询" />
								  	</br>
								  	</br>
								  </span>
								  </span>
								   <span id="sku_span_1_no">
								   <span  name="sku_span_2_no">
									    <span id="add_sku_no" name="add_sku_no" title="增加" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
									    <span  name="remove_sku_no" title="删除" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="remove_coupon_icon">  </span>
									   <span style="margin-left:20px;margin-top:50px">商品：</span>
									   <select class="select itemLevelSelect" name="item_sku"  style="width:50px;margin-left:20px;">  
												<option value="0">sku</option> 
									    </select>
								   		<input type="text" size="40" class="input-text lh30" style="width:120px;margin-left:20px;" maxlength="20" name="typeCode" />
										  </br>
										  </br>
								   </span>
								   </span>
								   <!-- copy 模板-->
								   <span id="category_span_copy_no" style="display:none;">
	                   		   		  	<span  name="category_span_2_no">
	                   		   		  	 		<span  name="add_category_no" title="增加" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
										   		<span name="remove_category_no" title="删除" style="margin-left: 10px;margin-right: 10px;cursor: pointer"  class="remove_coupon_icon">  </span>
			                   		   		   <span style="margin-left:20px;margin-top:20px">类别：</span>
			                   		   		   <span style="margin-left:20px;margin-left:20px">大类</span>
			                   		   		   <select class="select largeIdSel" name="" style="margin-left:10px;width:100px;" >  
			   		   		  						<option value="">--请选择分类--</option>
													<#list categoryList as category>
														<option value="${category.id}">${category.name}</option>
													</#list>
											   </select>
			                   		   		   <span style="margin-left:10px;">中类：</span>
			                   		   		   <select class="select mediumIdSel" name="" style="width:100px;" >  
			                   		   		   			<option value="">--请选择分类--</option>
											   </select>
										  	   <span style="margin-left:10px;">小类：</span> 
			                   		   		   <select class="select smallIdSel" name="" style="width:100px;" >  
			                   		   		   			<option value="">--请选择分类--</option>
											   </select>
										  </br>
										  </br>
									  	</span>
									  </span>
									  <span id="brand_span_copy_no" style="display:none;">
									  <span  name="brand_span_2_no">
									  	 <span id="add_brand"   title="增加"  style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
										 <span id="remove_brand_no" name="remove_brand_no" title="删除"  style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="remove_coupon_icon">  </span>
									 	 <span style="margin-left:20px;margin-top:50px">品牌：</span>
									 	 <input type="text" class="input-text lh30 brandId" style="width:50px; margin-left: 20px;"  />
									  	 <input type="text" size="40" class="input-text lh30 brandName" style="width:120px;margin-left:20px;" name="name" />
										  &nbsp;&nbsp;
										  <input type="button" class="btn btn82 btn_save2 confirmBrand" value="确定" />
										 <input type="button" class="btn btn82 btn_search searchBrand"  value="查询" />
									  	</br>
									  	</br>
									  </span>
									  </span>
									  <span id="sku_span_copy_no" style="display:none;">
									   <span name="sku_span_2_no">
										    <span id="add_sku_no" name="add_sku_no" title="增加"  style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="add_coupon_icon">  </span>|
										    <span  name="remove_sku_no"  title="删除" style="margin-left: 10px;margin-right: 10px;cursor: pointer" class="remove_coupon_icon">  </span>
										   <span style="margin-left:20px;margin-top:50px">商品：</span>
										   <select class="select itemLevelSelect" name="item_sku"  style="width:50px;margin-left:20px;">  
													<option value="0">sku</option> 
										    </select>
									   		<input type="text" size="40" class="input-text lh30" style="width:120px;margin-left:20px;" maxlength="20" name="typeCode" />
											  </br>
											  </br>
									   </span>
									   </span>
								   </span>
								  <!--end  -->
                  	 </td>
              	  </tr>
              	  
                 <tr >
                   <td class="td_right">&nbsp;</td>
                   <td class="">
                   <br/>
                   <br/>
                     <input name="button" class="btn btn82 btn_save2 datasubmit" value="保存" type="button" id='datasubmit' value="保存" param='/coupon/saveCoupon.htm' status='0'> 
                     <input name="button" class="btn btn82 btn_res datasubmit" value="提交" type="button" id="reset" param='/coupon/saveCoupon.htm' status='1'> 
                     <input name="button" class="ext_btn" value="返回" type="button" id="cancel_page">   
                   </td>
                 </tr>
               </tbody>
              </table>
             </div>
</@backend>