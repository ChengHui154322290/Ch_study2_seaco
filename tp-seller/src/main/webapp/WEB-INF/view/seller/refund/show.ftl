<#include "/layout/inner_layout.ftl" />

<@sellContent title="首页" 
    js=['/static/scripts/web/refundaudit.js',
        '/static/scripts/web/common/image_upload.js']
    css=[]>
<div class="panel-body">
<form class="jqtransform" id="rejectaudit">
	<input type="hidden" id="bucketURL" name="bucketURL" value="${bucketURL}">
	<table class="table table-bordered" style="width:900px;">
		<tr>
			<th width='125'>订单编号：</th>
			<td>${(rejectInfo.orderCode)!}</td>
			<th>订单类型：</th>
			<td>${(order.typeStr)!}</td>
			<th>订单状态:</th>
			<td>${(order.statusStr)!}</td>
		</tr>

		<tr>
			<th>退货单编号：</th>
			<td>${rejectInfo.rejectCode}</td>
			<th>申请时间：</th>
			<td>${rejectInfo.createTime?string('yy-MM-dd HH:mm:ss')}</td>
			<th>审核人：</th>
			<td>${(logList.operatorName)!}</td>
		</tr>
		<#if isHaitao?exists>
			<tr>
				<th>商品编号：</th>
				<td>${rejectItem.itemSkuCode}</td>
				<th>供应商SKU：</th>
				<td>${rejectItem.productCode}</td>
				<th>备案号：</th>
				<td>${rejectInfo.customCode}</td>
			</tr>
			<tr>
				<th>商品名称：</th>
				<td>${rejectItem.itemName}</td>
				<th>快递单号：</th>
				<td colspan="3">${(rejectInfo.expressNo)!} 
					<#if (rejectInfo.expressNo)??>
						<a href="javascript:void(0)" data-toggle="modal" data-target=".bs-example-modal-lg" onclick="showDelivery('${(rejectInfo.rejectCode)!}','${(rejectInfo.expressNo)!}')" >全程跟踪</a>
					</#if>
				</td>
			</tr>
				<th>退货数量：</th>
				<td>${rejectItem.itemRefundQuantity}</td>
				<th>退货金额：</th>
				<td colspan="3">${rejectInfo.refundAmount?string('0.00')}</td>
			</tr>
			<tr>	
		<#else>
			<tr>
				<th>商品编号：</th>
				<td>${rejectItem.itemSkuCode}</td>
				<th>供应商SKU：</th>
				<td>${rejectItem.productCode}</td>
				<th>商品名称：</th>
				<td>${rejectItem.itemName}</td>
			</tr>
			<tr>
				<th>快递单号：</th>
				<td>${(rejectInfo.expressNo)!} 
					<#if (rejectInfo.expressNo)??>
						<a href="javascript:void(0)" data-toggle="modal" data-target=".bs-example-modal-lg" onclick="showDelivery('${(rejectInfo.rejectCode)!}','${(rejectInfo.expressNo)!}')" >全程跟踪</a>
					</#if>
				</td>
				<th>退货数量：</th>
				<td>${rejectItem.itemRefundQuantity}</td>
				<th>退货金额：</th>
				<td>${rejectInfo.refundAmount?string('0.00')}</td>
			</tr>
		</#if>
		<tr>
			<th>问题描述：</th>
			<td colspan="5">${rejectInfo.buyerRemarks}</td>
		</tr>
		
		<tr>
			<th>用户上传图片：</th>
			<td colspan="5">
				<#if rejectInfo.buyerImgUrl??&&rejectInfo.buyerImgUrl?length gt 0>
					<#list  rejectInfo.buyerImgUrl?split(",") as url>
		                <#--<img src="<@imageDownload code='${url!}' width="80" />"/> -->
							<#if url?substring(0,4) == "http">
								<img src="${url}" width="250"/>
							<#else>
								<img src="${bucketURL}${url}" width="250"/>
							</#if>
					</#list>
				</#if>
			</td>
		</tr>
		
		<tr>
			<th>客服审核意见：</th>
			<td colspan="5">${(rejectInfo.remarks)!}</td>
		</tr>
		<#if auditshow??>
		<tr>
			<th>审核意见：</th>
			<td colspan="5">
				<textarea clos="50" rows="5" name="remark"   style="width: 600px; height: 93px;resize:none;" class="form-control"></textarea>
			</td>
		</tr>
		
		<#else>
		<tr>
              <th>审核意见：</th>
			  <td colspan="5">
                   ${(rejectInfo.sellerRemarks)!}
              </td>
        </tr>
 
        </#if>
        
    </table>
    <input type="hidden" name="rejectNo" value="${rejectInfo.rejectCode}">
	<input type="hidden" name="rejectId" value="${rejectInfo.rejectId}">
	<input type="hidden" name="rejectItemId" value="${rejectItem.rejectItemId}">
	<input type="hidden" name="success" >
	<input type="hidden" value="" name="auditType" id="auditTypeId" />
    <span id="hiddenFieldId">
	</span>
    </form>
    
    <table col="6"  class="table table-bordered" style="width:900px;">
	<#if auditshow??>
		<tr>
			<th>图片信息：</th>
			<td colspan="5">
				<form id="imageForm_auditImage" action="/uploadFile.htm" method="post" enctype="multipart/form-data">
                    <input type="file" id="auditImage_file" name="auditImage" showTag="auditImage" showType="3" maxLoad="5" class="_mulupload file" size="10">
                    <input type="hidden" value="${bucketname}" name="bucketname" />
                </form>
                <div class="row show-grid" id="imgSpan_auditImage">
                </div>
			</td>
		</tr>
		
		<tr>
			<th></th>
		    <td colspan="5">
				<span>为了帮助我们更好的解决问题，请您上传图片</span><br/>
				<span>最多可上传五张图片，每张图片大小不超过5M,支持bmp,gif,jpg,png,jpeg</span>
			</td>
		</tr>
		
		<tr>
			<td colspan="6" class="rejectbtnlist" align="center">
				<input type="button  btn-primary btn-sm" value="通过" class="btn btn82 btn_save2 submitreject" suc="true"  name="button"> 
                <input type="button  btn-primary btn-sm" value="不通过" class="btn btn82 btn_res submitreject" suc="false" name="button"> 
			</td>
		</tr>
	<#else>
		<tr>
		<th width='125'>商家上传图片：</th>
			<td colspan="5">
				<#if rejectInfo.sellerImgUrl??&&rejectInfo.sellerImgUrl?length gt 0>
					<#list  rejectInfo.sellerImgUrl?split(",") as url>
		                  <#-- <img src="<@imageDownload code='${url!}' width="80" />"/> -->
							<#if url?substring(0,4) == "http">
								<img src="${url}" width="250"/>
							<#else>
								<img src="${bucketURL}${url}" width="250"/>
							</#if>
					</#list>
				</#if>
			</td>
		</tr>
	</#if>
	</table>
</div>
</@sellContent>