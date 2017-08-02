<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/storage/js/inventory_list.js',
	  '/statics/backend/js/jqgrid/js/jquery.jqGrid.src.js',
	  '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	  '/statics/backend/js/dss/jqGridformatter.js'
] css=[
'/statics/select2/css/select2.css',
'/statics/select2/css/common.css',
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
	<form class="jqtransform" method="post" id="queryInventoryForm" action="${domain}/storage/inventory/manage/list.htm">
    <div class="box_border">
	    <div class="box_top">
	        <b class="pl15">仓储管理-&gt;库存管理</b> 
	    </div>
	    <!-- 搜索框 -->
	    <div>	      
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>   
                  	<td >SKU：</td>    
                  	<td>
                  		<!--<input type="text" id="sku" name="sku" class="input-text lh25" size="20" value='${sku}'>
                  		<select name="sku" id="sku" class="" style="width:225px; margin-left: 1px" >
							<option value="">--请选择sku--</option>
							<#list skuList as s>
								<option value="${s.sku}" <#if s.sku=query.sku> selected </#if> >${s.sku}</option>
							</#list>
						</select>
						-->
						<input type="text" data="1" name="spuName" value="${query.spuName}" id="spuName" style="width:220px;" class="input-text lh30 promotername ui-autocomplete-input" size="40" autocomplete="off">
						<input type="hidden" name="sku" value="${query.sku}" data-value="${query.spuName}_${query.sku}">
						
                  	</td> 
                  	<td >可销售库存：</td>    
                  	<td>
						<select id="chooseSection" name="chooseSection" class="select"> 
							<option value="">请选择</option> 
							<option value="max" <#if chooseSection == 'max'>selected</#if> ><=</option>   
							<option value="min" <#if chooseSection == 'min'>selected</#if>>>=</option>   
						</select>    
                  		<input type="text" id="usableInventory" name="usableInventory" class="input-text lh25" value='${usableInventory}'>
              		</td> 
                  	<td>商家：</td>
                  	<td>
                  		<select name="spId" id="supplierId" class="select2" style="width:225px; margin-left: 1px" >
							<option value="">--请选择商家--</option>
							<#list supplierList as s>
								<option value="${s.id}" <#if s.id=query.spId> selected </#if> >${s.name}</option>
							</#list>
						</select>
					</td>                  	
                     
                </tr>
                 <tr>  
                 	<td>达到预警线：</td>
                  	<td>
                  		<select class="select" name="warnFlag" id="warnFlag">
                  			<option value="" >请选择</option>
                  			<option value="1" <#if query.warnFlag==1> selected</#if>>是</option>
                  			<option value="2" <#if query.warnFlag==2> selected</#if>>否</option>
                  		</select>
					</td> 
                  	<td ><!--条形码：--></td>    
                  	<td>
                  		<!--<input type="text" id="barcode" name="barcode" class="input-text lh25" size="20" value='${barcode}'>
                  		<select id="barcode"  class="" style="width:225px; margin-left: 1px" >
							<option value="">--请选择条形码--</option>
							<#list skuList as s>
								<option value="${s.sku}" <#if s.sku=query.sku> selected </#if> >${s.barcode}</option>
							</#list>
						</select>
						-->
                  	</td> 
                  	<td ><!--商品标题：--></td>    
                  	<td>
                  		<!--<input type="text" id="mainTitle" name="mainTitle" class="input-text lh25" size="20" value='${mainTitle}'>
                  		<select id="mainTitle" class="" style="width:225px; margin-left: 1px" >
							<option value="">--请选择商品标题--</option>
							<#list skuList as s>
								<option value="${s.sku}" <#if s.sku=query.sku> selected </#if> >${s.spuName}</option>
							</#list>
						</select>
						-->
                  	</td> 
                </tr>
              </table>
    </div>
   <!-- 操作栏 -->
    <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;border-bottom: 1px solid #dadada;text-align:right;">
         <!-- 查询 -->
         <input class="btn btn82 btn_search" type="button" id="searthAtt" value="查询" >
         <!-- 新增 -->
         <input class="btn btn82 btn_add warehouseaddbtn" type="button" value="新增" name="button" onclick="toAdd()">
         <!-- 导入 -->
         <input class="btn btn82 btn_add warehouseaddbtn" type="button" value="导入" name="button" onclick="popTemplate()">
         <!-- 导出 -->
         <input class="btn btn82 btn_add warehouseaddbtn" type="button" value="导出" name="button" onclick="exportInventory()">
         <!-- 批量修改 -->
         <span id="popUpdateTemplate" style="cursor: default;display: inline-block;vertical-align: middle;border: 1px solid #dae0fa;height: 35px;box-sizing: border-box;line-height: 35px;padding: 0 10px;background: #f7f8f9;">
         	批量修改
     	 </span>
    
    
    </div>
    <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
					<th width="100">供应商</th>
					<th width="100">仓库</th>
					<th width="100">商品标题</th>
					<th width="100">sku</th>
					<th width="100">条形码</th>
					<th width="100">商品总库存数</th>
					<th width="100">商品预留库存数</th>
					<th width="100">商品可售库存数<!--（总库存数-预留库存数-冻结库存数）--></th>
					<th width="100">商品预警库存数</th>
					<th width="100">操作</th>
                </tr>
                <#if page.rows?? >
   						<#list page.rows as item>   
				            <tr class="tr" >
					              <td class="td_center">${item.spName!}</td>
					              <td class="td_center">${item.warehouseName!}</td>
					              <td class="td_center">${item.mainTitle!}</td>
					              <td class="td_center">${item.sku!}</td>
					              <td class="td_center">${item.barcode!}</td>
					              <td class="td_center">${item.realInventory!}</td>
					              <td class="td_center">${item.reserveInventory!}</td>
					              <td class="td_center">${item.inventory!}</td>
					              <td class="td_center">${item.warnInventory!}</td>
					              <td class="td_center">
					              	 <input type="button" value="编辑" class="ext_btn ext_btn_submit" onclick="toUpdate('${item.id!}')"/>
					              	 <input type="button" value="日志" class="ext_btn ext_btn_submit" onclick="showLog('${item.id!}')"/>
								  </td>
				             </tr>
	           		 </#list>
				</#if>
              </table>
              <@pager  pagination=page  formId="queryInventoryForm"  /> 
	     </div>
	</div>
	</div>
   </form>
   
   <script>
   $(function(){
   		$("#popUpdateTemplate").hover(
			function(event) { // 滑入
				$(this).css("background","#5ee393");
			},
			function(event) { // 滑出
				$(this).css("background","#f7f8f9");
			}
		);
		$("#popUpdateTemplate").bind("click",function(){
			popUpdateTemplate();
		});
   })
   
   
   // 导出
   function exportInventory(){
   		window.location.href = domain+ "/storage/inventory/manage/exportInventory.htm?"+$('#queryInventoryForm').serialize();
   }
   // 导入 -- 弹出框
   function popTemplate(){
	  	$.layer({
			type : 2,
			title : '上传模块',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '600px', 300 ],
			iframe : {
				src : domain+ "/storage/inventory/manage/popTemplate.htm"
			},
			end : function() {
				location.reload();
			}
		});
	  }
	// 批量修改 -- 弹出框
   function popUpdateTemplate(){
	  	$.layer({
			type : 2,
			title : '上传模块',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '600px', 300 ],
			iframe : {
				src : domain+ "/storage/inventory/manage/popUpdateTemplate.htm"
			},
			end : function() {
				location.reload();
			}
		});
	  }  
	  
	// 新增
	 function toAdd(){
	  	$.layer({
			type : 2,
			title : '新增库存记录',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '600px', 450 ],
			iframe : {
				src : domain+ "/storage/inventory/manage/toAdd.htm"
			},
			end : function() {
				location.reload();
			}
		});
	  }
	  // 更新
	  function toUpdate(inventoryId){
	  	if(inventoryId==null||inventoryId==""){
	  		layer.alert("数据有误",8);
	  	}
	  	$.layer({
			type : 2,
			title : '修改库存记录',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '650px', 550 ],
			iframe : {
				src : domain+ "/storage/inventory/manage/toUpdate.htm?inventoryId="+inventoryId
			},
			end : function() {
				location.reload();
			}
		});
//  	    var tv = {
//	        url: domain+ "/storage/inventory/manage/toUpdate.htm?inventoryId="+inventoryId,
//	        tabId: "toUpdate"+inventoryId,
//	        text: "调整库存",
//	        doc: false
//	    };
//	    parent.window.showTab(tv);
	  }
	  function showLog(inventoryId){
	  	//alert(domain+ "/storage/inventory/manage/showLog.htm?inventoryId="+inventoryId)
	  	
	  	if(inventoryId==null||inventoryId==""){
	  		layer.alert("数据有误",8);
	  	}
	  	var tv = {
	        url: "/storage/inventory/manage/showLog.htm?inventoryId="+inventoryId,
	        tabId: "toShowLog"+inventoryId,
	        text: "日志查看",
	        doc: false
	    };
	    parent.window.showTab(tv);
	  }
	  
	  $(function(){
	  	/**
			$('#sku').select2({
				//placeholder: "请输入",
				//minimumInputLength: 1
			});
			$('#sku').on("change",function(){
				var sku = $(this).select2("val");
				$("#barcode").select2("val",sku);
				$("#mainTitle").select2("val",sku);
			})
			
			 $('#barcode').select2({
			});
			$('#barcode').on("change",function(){
				var sku = $(this).select2("val");
				$("#sku").select2("val",sku);
				$("#mainTitle").select2("val",sku);
			})
			 $('#mainTitle').select2({
			});
			$('#mainTitle').on("change",function(){
				var sku = $(this).select2("val");
				$("#sku").select2("val",sku);
				$("#barcode").select2("val",sku);
			})
		*/	
			// 模糊查询
			$( ".promotername" ).autocomplete({
				source: function (request, response) {
		            var term = request.term;
		            request.spuName = term;
		            var param = {
		            	spuName:term
		            }
		            $.post(domain+'/storage/inventory/manage/querySkuDetail.htm', param, response,'json');
		        },
				max:10,
				minLength: 2,
				select: function( event, ui ) {
					 $("input[name=sku]").val(ui.item.sku);
					 $("#spuName").val(ui.item.spuName);
					 $("input[name=sku]").attr("data-value",ui.item.spuName+'_'+ui.item.sku);
					 return false;
				}
			}).on('blur',function(){
				if($(this).val()=='' || $(this).val()==null){
					$(":hidden[name='sku']").val('');
				}
				var skuNew = $("#spuName").val()+"_"+$("input[name=sku]").val();
			 	var skuOld = $("input[name=sku]").attr("data-value");
			 	//console.log(skuNew+"==="+skuOld)
				if(skuNew!=skuOld){
					$(":hidden[name='sku']").val('');
				}
			});
			$(".promotername").data("autocomplete")._renderItem = function (ul, item) {
			    return $("<li></li>")
			        .data("item.autocomplete", item)
			        .append("<a>" + item.spuName + "</a>")
			        .appendTo(ul);
			};
	  })
   </script>
</@backend>