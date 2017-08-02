<#include "/common/common.ftl"/>
<@backend title="编辑专场活动"
		  js=['/statics/backend/js/jquery.min.js',
		  	'/statics/backend/js/jquery.tools.js',
			'/statics/backend/js/jquery.form.2.2.7.js',
	        '/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/json2.js',
			'/statics/backend/js/promotion/utils.js',
			'/statics/backend/js/editor/kindeditor-all.js',
			'/statics/backend/js/promotion/editorUtils.js',
			'/statics/backend/js/promotion/promotion_submit.js',
			'/statics/backend/js/promotion/promotionShop_search.js',
			'/statics/backend/js/promotion/promotion_edit.js',
			'/statics/backend/js/promotion/promotionCoupon_edit.js',

			'/statics/backend/js/promotion/promotionRelation_edit.js']
		css=['/statics/backend/js/dateTime2/css/jquery-ui-1.8.17.custom.css',
			 '/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
			 '/statics/backend/css/style.css']>
		<style>
			 .ke-dialog-default{
			 	position:fixed;margin:auto;left:0 !important; right:0 !important; top:0px !important; bottom:0 !important;
			 }
		</style>
		<form action="${domain + "/topic/save"}" method="POST" ID="submitTopic">
            <input type="hidden" id="page-type-info" value="topic">
            <input type="hidden" id="bucketname" name="bucketname" value="${bucketname}">
            <input type="hidden" id="bucketURL" name="bucketURL" value="${bucketURL}">
<script>
$(function(){
	$("input[name='reserveInventoryFlag']").bind("click",function(){
		var flag = $(this).val();
		$("input[name=limitTotal]").each(function(){
			//var flag = $(this).attr("readonly");
			if(flag=="1"){ // 预留
				$(this).attr("readonly",false);
			}else{
				$(this).attr("readonly",true);
			}
		})
	})
	$("#salesPartten").bind("change",function(){
		if($(this).val()=="6"){
			// 秒杀  更改预留状态
			$("input[name='reserveInventoryFlag']").each(function(){
				if($(this).val()==1){ // 预留
					$(this).attr("checked",true);
				}
				if($(this).val()==0){ // 不预留
					$(this).attr("checked",false);
				}
			})
			$("input[name=limitTotal]").each(function(){
				$(this).attr("readonly",false);
			})
		}
		
	})
});
</script>
			<div class="box_border">
				<div class="box_top">
					<b class="pl15">编辑专场活动</b>
				</div>
				<div class="box_center">
					<#include "/promotion/subpages/topicVariable.ftl"/>
					<table id="topicTable" class="input tabContent">
						<tr>
							<td class="td_left" colspan="6">
								<h4>基本信息</h4>
							</td>
						</tr>
						<tr>
							<td class="td_right">
								活动序号
							</td>
							<td colspan="5">
								${topicDetail.topic.id!}
							</td>
						</tr>
						<!-- 基本信息块 -->
						<tr>
							<td class="td_right"><strong style="color:red;">*</strong>专场类型:</td>
							<td>
								<#if ("view" != "${mode}")>
									<#if (2 == topicDetail.topic.type)>
										<select name="type" class="select" style="width:150px;" id="type">
											<option value="1">单品团</option>
											<option value="2" selected>品牌团</option>
											<option value="3">主题团</option>
											<option value="5">商家店铺</option>
										</select>
									<#elseif (3 == topicDetail.topic.type)>
										<select name="type" class="select" style="width:150px;" id="type">
											<option value="1">单品团</option>
											<option value="2">品牌团</option>
											<option value="3" selected>主题团</option>
                                            <option value="5">商家店铺</option>
										</select>
									<#elseif (5 == topicDetail.topic.type)>
                                        <select name="type" class="select" style="width:150px;" id="type">
                                            <option value="1">单品团</option>
                                            <option value="2">品牌团</option>
                                            <option value="3" >主题团</option>
                                            <option value="5" selected>商家店铺</option>
                                        </select>
									<#else>
										<select name="type" class="select" style="width:150px;" id="type">
											<option value="1" selected>单品团</option>
											<option value="2">品牌团</option>
											<option value="3">主题团</option>
                                            <option value="5">商家店铺</option>
										</select>
									</#if>
								<#else>
									<#if (2 == topicDetail.topic.type)>
										品牌团
									<#elseif (3 == topicDetail.topic.type)>
										主题团
									<#elseif (1 == topicDetail.topic.type)>
										单品团
									<#elseif (5 == topicDetail.topic.type)>
                                        商家店铺
									</#if>
								</#if>
							</td>
							<td class="td_right">专场时间:</td>
							<td>
								<#if ("view" != "${mode}")>
									<#if (1 == topicDetail.topic.lastingType)>
										<input type="radio" name="topic.lastingType" value="0"/>长期在线
										<input type="radio" name="topic.lastingType" value="1" checked="checked" />固定期限
									<#else>
										<input type="radio" name="topic.lastingType" value="0" checked="checked"/>长期在线
										<input type="radio" name="topic.lastingType" value="1" />固定期限
									</#if>
								<#else>
									<#if (1 == topicDetail.topic.lastingType)>
										固定期限
									<#else>
										长期在线
									</#if>
								</#if>
							</td>
							<td class="td_right">状态:</td>
							<td>
								<#if (1 == topicDetail.topic.status)>
									审核中
								<#elseif (2 == topicDetail.topic.status)>
									已取消
								<#elseif (3 == topicDetail.topic.status)>
									审核通过
								<#elseif (4 == topicDetail.topic.status)>
									已驳回
								<#elseif (5 == topicDetail.topic.status)>
									终止
								<#else>
									编辑中
								</#if>
							</td>
						</tr>
						</tr>
							<td class="td_right"><strong style="color:red;">*</strong>专场编号:</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 topicInteger" name="topic.number" id="number" value="${topicDetail.topic.number!}" />
								<#else>
									<span>${topicDetail.topic.number!}</span>
								</#if>
							</td>
							<td class="td_right"><strong style="color:red;">*</strong>开始时间:</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 dateInput" name="startTime" readonly="true" id="startTime" datafmt="yyyy-MM-dd HH:mm:ss" value="${topicDetail.startTime!}" />
								<#else>
									<span>${topicDetail.startTime!}</span>
								</#if>
							</td>
							<td class="td_right">支持商户提报:</td>
							<td>
								<#if ("view" != "${mode}")>
									<#if (0 == topicDetail.topic.isSupportSupplier)>
										<input type="radio" name="topic.isSupportSupplier" value="0" checked="checked" />支持
										<input type="radio" name="topic.isSupportSupplier" value="1" />不支持
									<#else>
										<input type="radio" name="topic.isSupportSupplier" value="0" />支持
										<input type="radio" name="topic.isSupportSupplier" value="1" checked="checked" />不支持
									</#if>
								<#else>
									<#if (0 == topicDetail.topic.isSupportSupplier)>
										支持
									<#else>
										不支持
									</#if>
								</#if>
							</td>
						</tr>
						<tr>
							<td class="td_right"><strong style="color:red;">*</strong>专场名称:</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30" name="topic.name" id="name" value="${topicDetail.topic.name!}" />
								<#else>
									<span>${topicDetail.topic.name!}</span>
								</#if>
							</td>
							<td class="td_right"><strong style="color:red;">*</strong>结束时间:</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30 dateInput" name="endTime" readonly="true" datafmt="yyyy-MM-dd HH:mm:ss" id="endTime" value="${topicDetail.endTime!}" />
								<#else>
									<span>${topicDetail.endTime!}</span>
								</#if>
							</td>
							<td class="td_right">折扣:</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30" name="topic.discount" id="discount" value="${topicDetail.topic.discount!}" />
								<#else>
									<span>${topicDetail.topic.discount!}</span>
								</#if>
							</td>

						</tr>
						<tr>
							<td class="td_right">活动卖点:</td>
							<td>
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30" style="width:100%;" name="topic.topicPoint" id="topicPoint" value="${topicDetail.topic.topicPoint!}" />
								<#else>
									<span>${topicDetail.topic.topicPoint!}</span>
								</#if>
							</td>
							<td class="td_right"><label id="b_or_s">品牌:</label></td>
							<td>
								<#if ("view" != "${mode}")>

                                    <span id="supplier-info" <#if (5 == topicDetail.topic.type)> style="display: none" </#if>>
                                    <input type="text" class="input-text lh30" style="width:50px;" name="topic.supplierId" id="supplierId" placeholder="商家Id" value="${topicDetail.topic.supplierId!}" />
									<input type="text" class="input-text lh30" style="width:200px;" name="topic.supplierName" id="supplierName" placeholder="商家名称" value="${topicDetail.topic.supplierName!}" />
                                    </span>
									<span id="brand-info" <#if (5 != topicDetail.topic.type)> style="display: none" </#if>>
									<input type="text" class="input-text lh30" style="width:50px;" name="topic.brandId" id="brandId" placeholder="品牌Id" value="${topicDetail.topic.brandId!}" />
									<input type="text" class="input-text lh30" style="width:200px;" name="topic.brandName" id="brandName" placeholder="品牌名称" value="${topicDetail.topic.brandName!}" />
                                    </span>

									<input type="button" class="btn btn82 btn_save2" id="confirmBrand" value="确定" />
									<input type="button" class="btn btn82 btn_search" id="searchBrand" value="查询" />
								<#else>
                                    <#if (5 == topicDetail.topic.type) >
                                        <span>${topicDetail.topic.supplierName!}</span>
                                    <#else>
                                        <span>${topicDetail.topic.brandName!}</span>
                                    </#if>
								</#if>
							</td>
							<td class="td_right">销售类型:</td>
								<td>
									<#if ("view" != "${mode}")>
										<select class="select" id="salesPartten"
		                                    style="width: 130px;">
						                        <option value="1" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='1'>selected='selected'</#if>>不限</option>
						                        <option value="2" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='2'>selected='selected'</#if>>旗舰店</option>
						                        <option value="3" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='3'>selected='selected'</#if>>商城</option>
						                        <option value="4" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='4'>selected='selected'</#if>>海淘</option>
						                        <option value="5" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='5'>selected='selected'</#if>>闪购</option>
						                        <option value="6" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='6'>selected='selected'</#if>>秒杀</option>
						                        <option value="8" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='8'>selected='selected'</#if>>分销</option>
						                        <option value="9" <#if topicDetail.topic.salesPartten??&&topicDetail.topic.salesPartten?string=='9'>selected='selected'</#if>>线下团购</option>
		                                </select>
									<#else>
										<#if (1 == topicDetail.topic.salesPartten)>
											不限
										<#elseif (2 == topicDetail.topic.salesPartten)>
											旗舰店
										<#elseif (3 == topicDetail.topic.salesPartten)>
											商城
										<#elseif (4 == topicDetail.topic.salesPartten)>
											海淘
										<#elseif (5 == topicDetail.topic.salesPartten)>
											闪购
										<#elseif (6 == topicDetail.topic.salesPartten)>
											秒杀
										<#elseif (8 == topicDetail.topic.salesPartten)>
                                            分销
										<#elseif (9 == topicDetail.topic.salesPartten)>
                                            线下团购
										</#if>
									</#if>
								</td>
						</tr>
						<tr>
							<td class="td_right">备注:</td>
							<td colspan="1">
								<#if ("view" != "${mode}")>
									<input type="text" class="input-text lh30" style="width:100%;" name="topic.remark" id="remark" value="${topicDetail.topic.remark!}" />
								<#else>
									<span>${topicDetail.topic.remark!}</span>
								</#if>
							</td>
							<td class="td_right">是否可使用西狗币</td>
							<td colspan="3">
							<#if ("view" != "${mode}")>
									<#if (1 == topicDetail.topic.canUseXgMoney)>
										<input type="radio" name="topic.canUseXgMoney" value="1" checked="checked" />允许
										<input type="radio" name="topic.canUseXgMoney" value="0" />不允许
									<#else>
										<input type="radio" name="topic.canUseXgMoney" value="1" />允许
										<input type="radio" name="topic.canUseXgMoney" value="0" checked="checked" />不允许
									</#if>
								<#else>
									<#if (1 == topicDetail.topic.canUseXgMoney)>
										允许
									<#else>
										不允许
									</#if>
								</#if>
							</td>
						</tr>
						<tr>
							<td class="td_right">是否预留库存</td>
							<td colspan="5">
							<#if ("view" != "${mode}")>
									<input type="radio" name="reserveInventoryFlag" value="1" <#if ("1" == "${topicDetail.topic.reserveInventoryFlag!}")>checked</#if> />预留<span style="color:#9fa2a2;">（专场占用库存）</span>
									<input type="radio" name="reserveInventoryFlag" value="0" <#if ("0" == "${topicDetail.topic.reserveInventoryFlag!}")>checked</#if> />不预留
								<#else>
									<#if (1 == topicDetail.topic.reserveInventoryFlag)>
										预留<span style="color:#9fa2a2;">（专场占用库存）</span>
									<#else>
										不预留
									</#if>
								</#if>
							</td>
						</tr>
					</table>
					<#include "/promotion/subpages/topicPolicy.ftl"/>
					<#include "/promotion/subpages/topicArea.ftl"/>
					<#include "/promotion/subpages/topicItemList.ftl"/>
					<#include "/promotion/subpages/topicPlatform.ftl"/>
					<#include "/promotion/subpages/topicDesc.ftl"/>
					<#include "/promotion/subpages/topicCouponList.ftl"/>
					<#include "/promotion/subpages/topicRelateList.ftl"/>
					<#include "/promotion/subpages/topicAuditInfo.ftl"/>
					<table id="topicAuditInfo" class="input tabContent">
						<tr>
							<td align="center">
								<#if ("view" != "${mode}")>
									<input type="button" class="ext_btn ext_btn_submit" id="saveTopic" value="保存" />
									<input type="button" class="ext_btn ext_btn_submit" id="submTopic" value="提交" />
								</#if>
								<#if ("view" == "${mode}" && !(order)??)>
									<input type="button" class="ext_btn ext_btn_submit" id="genChangeOrder" value="生成变更单" />
								</#if>
								<input type="button" class="ext_btn ext_btn_submit" id="cancel" value="取消" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
</@backend>