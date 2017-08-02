<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js','/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js','/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js','/statics/backend/js/item/item-search.js','/statics/backend/js/item/item-copy.js','/statics/backend/js/item/item-category.js'
  ,'/statics/select2/js/select2.js','/statics/select2/js/select2Util.js','/statics/select2/js/select2_locale_zh-CN.js','/statics/backend/js/item/item-select2.js'] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/select2/css/select2.css',
	'/statics/select2/css/common.css'] >
	<!--初始化列表查询-->
	<input type="hidden" id="largeIdHidden" value="${largeId}" />
	<input type="hidden" id="mediumIdHidden" value="${mediumId}" />
	<input type="hidden" id="smallIdHidden" value="${smallId}" />
	<input type="hidden" id="supplierIdHidden" value="${supplierId}" />
	<input type="hidden" id="domainURL" value="${domain}" />
	
    <form method="post" action="${domain}/item/list.htm" id="itemSearchForm">
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">商品列表页面</b>
            </div>
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
             	<tr>
				<td style="width:100px !important;" nowrap='nowrap' >商家：</td>  
				<td colspan = 6 class=""> 
				<span class="fl"> 
					<select
						name="supplierId" class="select2" style="width:350px; margin-left: 1px" >
							<option value=""  >--请选择分类--</option>
						<#list supplierList as s>
							<option value="${s.id}" <#if "${s.id}"="${supplierId}"> selected </#if> >${s.name}</option>
						</#list>
					</select>
				</span></td>
				</tr>
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
	                	<option value="2" <#if "${status}"=="2">selected</#if> >作废</option>
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
			           <td>备案编号:</td> 
			           <td>
			           		<input type="text" name="articleNumber" value="${query.articleNumber}" class="input-text lh25" size="20">
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
                  <input class="btn btn82 btn_search " type="button" value="预览" name="button"	onclick="previewSku()"  />
                 </a>               
                 <a href="javascript:void(0);"><input class="btn btn82 btn_search" onclick="itemFormSubmit('list')" type="button" value="查询" name="button" /></a>
                 <a href="add.htm">
                 	<input class="btn btn82 btn_add itemeditbtn" type="button" value="新增" name="button"
                 		onclick='window.open(this.parentNode.href,"_self");'  />
                 </a>
                 <!-- 
                 <input class="btn btn82 btn_add" type="button" value="复制" name="button" id="copyItemBtn"  />
                 -->
                 <a href="import.htm">
                  <input class="btn btn82 btn_export itemImportBtn" type="button" value="导入" name="button"
                 		onclick='window.open(this.parentNode.href,"_self");'  />
                 </a>
                 <a href="javascript:void(0);">
                  <input class="btn btn82 btn_export " type="button" value="导出" name="button"	onclick="itemFormSubmit('exportList')"  />
                 </a>
                 <a href="javascript:void(0);">
                  <input class="btn btn82 btn_add itemeditbtn" type="button" value="修改" name="button"	onclick="batchEdit()"  />
                 </a>
                
                 <a href="javascript:void(0);">
                  <input class="btn btn82 btn_export " style="font-size: 1px;" type="button" value="翻译导出" name="button"	onclick="itemDetailFormSubmit()"  />
                 </a>
                 <a href="importNew.htm">
                  <input class="btn btn82 btn_export " style="font-size: 1px;" type="button" value="翻译导入" name="button"	onclick='window.open(this.parentNode.href,"_self");'  />
                 </a>
              </div>
            </div>
          </div>
        </div>
    </div>
   <div id="table" class="mt10">
    	<div class="box span10 oh">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
            <tr>
           	   <th width="15"> </th>
           	   <th width="100">SPU名称</th>
           	   <th width="100">大类</th>
           	   <th width="100">中类</th>
           	   <th width="100">小类</th>
           	   <th width="100">spu</th>
           	   <th width="100">prdid</th>
           	   <th width="100">条形码</th>
               <th width="450">网站显示名称</th>
               <th width="150">类型</th>
               <th width="200">发布</th>
               <th width="200">创建日期</th>
               <th width="200">操作</th>
            </tr>
            <#if page.rows?default([])?size !=0>
            <#list page.rows as item>
             <tr class="tr">
               <td width="15"><input type="checkbox" name ="detailId"  class="detailIdCheckbox" value="${item.detailId}"/> </td>
               <td width="200">${item.spuName}</td>
               <td width="200">${item.largeName}</td>
               <td width="200">${item.mediumName}</td>
               <td width="200">${item.categoryName}</td>
               
               <td width="100">${item.spu}</td>
               <td width="100">${item.prdid}</td>
               <td width="100">${item.barcode}</td>
               <td width="500">${item.mainTitle}</td> 
               <td width="100"><#if item.wavesSign ==1>国内<#elseif item.wavesSign==2>海淘</#if></td> 
               <td width="100">${item.statusDesc}</td>
               <td width="200"><#if item.createTime??>${item.createTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
               <td width="200"><a  href="javascript:void(0);" onclick="show(${item.detailId});">[编辑]<a  href="javascript:void(0);" onclick="newPrd(${item.itemId});"> [PRD]</a>
               </td>
            </tr>
            </#list>
            </#if>
          </table>
    	</div>
</div>
   <@pager  pagination=page  formId="itemSearchForm"  /> 
</form>

	<script> 
			function show(id){
				var date = new Date();
				var tv={
					url:'/item/detail.htm?detailId='+id,
					linkid:'add_detail_'+date.getMilliseconds(),
					tabId:'add_detail_'+date.getMilliseconds(),
					text:'编辑sku信息'
				};
				parent.window.showTab(tv);
			}
			
			function newPrd(id){
			    /*
			    alert("亲，下个版本上线哦!") 
			    return ;
				*/
				var date = new Date();
				var tv={
					url:'/item/queryPrd.htm?itemId='+id,
					linkid:'query_prd_'+date.getMilliseconds(),
					tabId:'query_prd_'+date.getMilliseconds(),
					text:'新增prd'
				};
				parent.window.showTab(tv);
				
			}
	</script>
</@backend>