<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/storage/js/inventory_list.js',
	  '/statics/backend/js/jqgrid/js/jquery.jqGrid.src.js',
	  '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js'
] css=[
'/statics/select2/css/select2.css',
'/statics/select2/css/common.css',
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
	<form class="jqtransform" method="get" id="queryInventoryForm" action="${domain}/topic/batchAddItemList.htm">
    <div class="box_border" style="margin-bottom:-60px;">
	    <!-- 搜索框 -->
	    <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0" >
	       		<!-- 隐藏值 -->
	       		<input type="hidden" id="oldSkus" name="oldSkus" value="${oldSkus}">
	       		<input type="hidden" id="spu" name="spu" value="${params.spu}">
		   		<input type="hidden" id="supplierId" name="supplierId" value="${params.sp_id}" >
		   		<input type="hidden" id="brandId" name="brandId" value="${params.brandId}" >
		        <input type="hidden" id="topicId" name="topicId" value="${topicId}" >
		        <input type="hidden" id="sortIndex" name="sortIndex" value="${sortIndex}" >
                <tr>   
                  	<td >SKU：</td>    
                  	<td>
						<input type="text" data="1" name="sku" value="${sku}" id="sku" style="width:220px;" class="input-text lh30 promotername ui-autocomplete-input" size="40" autocomplete="off">
                  	</td> 
                  	<td>
                  		<input class="btn btn82 btn_search" type="submit" id="queryBtn" value="查询" >
                  		<input class="btn btn82 btn_search" type="button" id="chooseBtn" value="选择" >
                  	</td>
                </tr>
          </table>
	    </div>
	    <div id="table" class="mt10">
	        <div class="box span10 oh">
	              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
	                <tr>
	                	<th width="20">
	                	</th>
	                	<th width="100">商品标题</th>
	                	<th width="100">sku</th>
	                	<th width="100">条形码</th>
						<th width="100">供应商</th>
						<th width="100">仓库</th>
	                </tr>
	                <#if result.data.rows?? >
	   						<#list result.data.rows as item>  
					            <tr class="tr" >
						            <td width="20" style="text-align:center;">
				                		<input type="checkbox" />
				                		<div style="display:none;">
											<input type="hidden" vname="itemId" value="${item.itemId!}"/>
											<input type="hidden" vname="itemspu" value="${item.spu!}"/>
											<input type="hidden" vname="itemsku" value="${item.sku!}"/>
											<input type="hidden" vname="itemSpec" value="${item.itemSpec!}"/>
											<input type="hidden" vname="itemBarCode" value="${item.barCode!}"/>
											<input type="hidden" vname="topicImage" value="${item.topicImage!}"/>
											<input type="hidden" vname="imageFull" value="${item.imageFullPath!}"/>
											<input type="hidden" vname="itemSupplierId" value="${item.supplierId!}"/>
											<input type="hidden" vname="itemName" value="${item.name!}"/>
											<input type="hidden" vname="categoryId" value="${item.categoryId!}"/>
											<input type="hidden" vname="stock" value="${item.stock!}"/>
											<input type="hidden" vname="brandId" value="${item.brandId!}"/>
											<input type="hidden" vname="countryId" value="${item.countryId!}"/>
											<input type="hidden" vname="countryName" value="${item.countryName!}"/>
											<input type="hidden" vname="supplierName" value="${item.supplierName!}"/>
											<input type="hidden" vname="itemSalePrice" value="${item.salePrice!}"/>
											<input type="hidden" vname="itemTopicPrice" value="${item.topicPrice!}"/>
											
											<!-- 标签信息 -->
											<input type="hidden" vname="itemTags" value=""/>
											<!-- 仓库信息 -->
											<input type="hidden" vname="warehousesId" />
											<input type="hidden" vname="warehousesName" />
											<input type="hidden" vname="bondedArea" />
											<input type="hidden" vname="whType" />
				                		</div>
				                	</td>
									<td widtd="100">${item.name!}</td>
									<td widtd="100">${item.sku!}</td>
									<td widtd="100">${item.barCode!}</td>
									<td widtd="100">${item.supplierName!}</td>
									<td widtd="100">
										<#if item.warehouses?? >
											<select name="warehouseSelect" class="select">
	   											<#list item.warehouses as warehouses> 
	   												<option value="${warehouses.id!}" type="${warehouses.type!}" bondedArea="${warehouses.bondedArea!}" >${warehouses.name!}</option>	
	   											</#list>
											</select>
										</#if>
									</td>
					             </tr>
		           		 </#list>
					</#if>
	              </table>
	              <@pager  pagination=result.data  formId="queryInventoryForm"  /> 
		     </div>
		</div>
	</div>
   </form>
   <script>
   		$(function(){
   			
   			// 添加仓库信息
   			$("input[type=checkbox]").bind("change",function(){
   				var parentDom = $(this).parents("tr");
   				var warehouseSelect = parentDom.find("option:selected");
   				
   				parentDom.find("input[vname=warehousesId]").val(warehouseSelect.attr("value"));
   				parentDom.find("input[vname=warehousesName]").val(warehouseSelect.text());
   				parentDom.find("input[vname=bondedArea]").val(warehouseSelect.attr("bondedArea"));
   				parentDom.find("input[vname=whType]").val(warehouseSelect.attr("type"));
   				

   			});
   			
   			// 选择
   			$("#chooseBtn").bind("click",function(){
   				
   				var sortIndex = $("#sortIndex").val()*1; // 序号
   				var parentSpu = $("#spu").val()*1; // 
   				var parentsupplierId = $("#supplierId").val()*1; // 
   				var parentbrandId = $("#brandId").val()*1; // 
   				var parenttopicId = $("#topicId").val()*1; // 
   				
   				
   				$("input[type=checkbox]:checked").each(function(i,ele){
   					var parentDom = $(this).parent().find("div");
   					
   					// 序号++
   					sortIndex += 10;
   					
   					// 仓库信息
   					var warehousesId = parentDom.find("input[vname=warehousesId]").val();
   					var warehouseName = parentDom.find("input[vname=warehousesName]").val();
   					var bondedArea = parentDom.find("input[vname=bondedArea]").val();
   					var whType = parentDom.find("input[vname=whType]").val();
   					
   					// 图片
	   				var pictureUrl = parentDom.find("input[vname=topicImage]").val(); 
					var pictureFullUrl = parentDom.find("input[vname=imageFull]").val();
					if(null == pictureFullUrl || $.trim(pictureFullUrl).length == 0){
						pictureFullUrl = domain + "/statics/backend/images/wait_upload.png";
					}
					
					// 商品信息
					var itemId = parentDom.find("input[vname=itemId]").val();
   					var itemspu = parentDom.find("input[vname=itemspu]").val();
   					var itemsku = parentDom.find("input[vname=itemsku]").val();
   					var itemSpec = parentDom.find("input[vname=itemSpec]").val();
   					
   					var topicImage = parentDom.find("input[vname=topicImage]").val();
   					var imageFull = parentDom.find("input[vname=imageFull]").val();
   					var itemSupplierId = parentDom.find("input[vname=itemSupplierId]").val();
   					var itemName = parentDom.find("input[vname=itemName]").val();
   					
   					var categoryId = parentDom.find("input[vname=categoryId]").val();
   					var stock = parentDom.find("input[vname=stock]").val();
   					var brandId = parentDom.find("input[vname=brandId]").val();
   					var countryId = parentDom.find("input[vname=countryId]").val();
   					
   					var countryName = parentDom.find("input[vname=countryName]").val();
   					var itemBarCode = parentDom.find("input[vname=itemBarCode]").val();
   					var supplierName = parentDom.find("input[vname=supplierName]").val();
   					var itemSalePrice = parentDom.find("input[vname=itemSalePrice]").val(); // 吊牌价
   					var itemTopicPrice = parentDom.find("input[vname=itemTopicPrice]").val(); // 售价
   					
	
					var reserveInventoryFlag = parent.$("input[name='reserveInventoryFlag']:checked").val()||parent.$("input[name='reserveInventoryFlag']").val();
					var row = "<tr align='center' style='background-color: rgb(255, 255, 255);' class='tr topicItemData'>"
							+ "<td style='display:none;'>"
							+ "<input type='hidden' name='topicItemId' value='0' />"
							+ "<input type='hidden' name='topicItemItemId' value='" + itemId + "' />"
							+ "<input type='hidden' name='topicItemBarcode' value='" + itemBarCode + "' />"
							+ "<input type='hidden' name='topicItemSku' value='" + itemsku + "' />"
							+ "<input type='hidden' name='topicItemSpu' value='" + itemspu + "' />"
							+ "<input type='hidden' name='topicItemName' value='" + itemName.replace(/"/g,"&quot;").replace(/'/g,"&apos;") + "' />"
							+ "<input type='hidden' name='topicItemSupplierId' value='" + itemSupplierId + "' />"
							+ "<input type='hidden' name='topicItemLocationId' value='" + warehousesId + "' />"
							+ "<input type='hidden' name='topicImage' value='" + pictureUrl + "' />"
							+ "<input type='hidden' name='pictureSize' value='1' />"
							+ "<input type='hidden' name='salePrice' value='"+itemSalePrice+"' />"
							+ "<input type='hidden' name='categoryId' value='" + categoryId + "' />"
							+ "<input type='hidden' name='stock' value='' />"
							+ "<input type='hidden' name='brandId' value='" + brandId + "' />"
							+ "<input type='hidden' name='bondedArea' value='" + bondedArea + "' />"
							+ "<input type='hidden' name='whType' value='" + whType + "' />"
							+ "<input type='hidden' name='countryId' value='" + countryId + "' />"
							+ "<input type='hidden' name='countryName' value='" + countryName + "' />"
							+ "<input type='hidden' name='inputSource' value='0' />"
							+ "<input type='hidden' name='lockStatus' value='0' />"
							+ "<input type='hidden' name='itemTags' value=''/>"
							+ "</td>"
							+ "<td><input type='text' name='sortIndex' class='topicInteger' value='" + sortIndex + "' /></td>"
							+ "<td><span>" + supplierName + "</span></td>"
							+ "<td><span>" + itemBarCode + "</span><br/><span>" + itemsku
							+ "</span><br/><span id='name'>" + itemName + "</span></td>"
							+ "<td><span></span></td>"
							+ "<td>"
							+ "<div style='float:left;width:90%;'>"
							+ "<img name='selectImage' style='width:90%;' src='" + pictureFullUrl + "'/>"
							+ "</div>"
							+ "<div style='float:left;'>"
							+ "<a name='removeImage' href='javascript:void(0);'>X</a>"
							+ "</div>"
							+ "</td>"
							+ "<td>"
							+ "<input type='text' class='input-text lh30 topicNumber' name='topicPrice' value='"+itemTopicPrice+"'/>"
							+ "</td>"
							+ "<td>"
							+ "<input type='text' class='input-text lh30 topicInteger' name='limitAmount' value='0'/>"
							+ "</td>"
							+ "<td>"
							+ "<input type='text' class='input-text lh30 topicInteger' name='limitTotal' value='0' "+(reserveInventoryFlag==0?"readonly":"")+" />"
							+ "</td>"
							+ "<td><span>" + warehouseName + "</span></td>"
							+ "<td><span></span></td>"
							+ "<td><span></span></td>"
							+ "<td><select class='select' style='width:80px;' name='purchaseMethod' id='purchaseMethod'>"
							+ "<option value='1'>普通</option>"
							+ "<option value='2'>立即购买</option>"
							+ "</select></td>"
							+ "<td><span></span></td>"
							+ "<td>"
							+ "<a href='javascript:void(0);' name='removeItem'>删除</a>"
							+ "</td>" + "</tr>";
					parent.$("#topicItemsList").append(row);
   				});
   				parent.querySameSkuPrice();	
   				parent.$("#topicItemsList a[name=removeItem]").unbind();
   				parent.$("#topicItemsList a[name=removeItem]").bind("click",function(){
   					$(this).parent().parent().remove();
   				});
   				var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
         		parent.layer.close(index);
   			});
   		})
   
   
   </script>
</@backend>