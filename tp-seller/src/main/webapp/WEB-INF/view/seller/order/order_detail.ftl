<#include "/layout/inner_layout.ftl" />

<@sellContent title="首页" 
    js=[]
    css=[]>

<div class="panel panel-default">
  <div class="panel-heading">
    <span class="panel-title">显示订单信息</span>
    <#if orderInfo.type==7 && orderInfo.orderStatus gte 3 && orderInfo.orderStatus lt 6>
    <span class="fastorderoverspan" style="color:blue;">　
	    <#if orderInfo.overTime gt 0 >还剩<#else>超时</#if>
	    <span class="fastorderovertime">${orderInfo.overTime}</span>分　
	    <#if orderInfo.orderStatus==3><input class="btn btn-primary fastreceivingorderbtn" type="button" value="接单"  data_code="${orderInfo.orderCode}"/></#if>
		<#if orderInfo.orderStatus==4><input class="btn btn-primary fastdeliveryorderbtn" type="button" value="配送" data_code="${orderInfo.orderCode}"/></#if>
    </span>
    </#if>
  </div>
  <div class="panel-body">
	<table class="table table-striped table-bordered table-hover">
		<tr style="background-color:#f5f5f5;">
			<th colspan="6">订单基本信息</th>
		</tr>
		<tr>
			<th width="10%">订单编号：</th>
			<td>${(orderInfo.orderCode)!} 
				<a href="javascript:void(0)" data_code="${orderInfo.orderCode}" id="ordertrack">全程跟踪</a>
				<a href="javascript:void(0);" data_code="${orderInfo.orderCode}" id="order-log">订单日志</a>
			</td>
			<th width="10%">订单状态：</th>
			<td width="15%">${(orderInfo.statusStr)!}</td>
			<th width="10%">订单类型：</th>
			<td width="15%">${(orderInfo.typeStr)!}</td>
		</tr>
		
		<tr>
			<th>下单时间：</th>
			<td>${(orderInfo.createTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
			<th>支付时间：</th>
			<td>${(orderInfo.payTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
			<th>发货时间：</th>
			<td>${(orderInfo.deliveredTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
		</tr>
		<tr>
			<th>完成时间：</th>
			<td>${(orderInfo.doneTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
			<th>快递公司：</th>
			<td>${(orderInfo.expressName)!}</td>
			<th>快递编号：</th>
			<td>${(orderInfo.packageNo)!}</td>
		</tr>
		<tr>
			<th>支付方式：</th>
			<td>${(orderInfo.payTypeStr)!}</td>
			<th>支付单号：</th>
			<td colspan="3">${(orderInfo.payCode)!}</td>
		</tr>
		<tr>
			<th>备注：</th>
			<td colspan="5">${(orderInfo.remark)!}</td>
		</tr>
	</table>
		
	<table class="table table-striped table-bordered table-hover">
		<tr  style="background-color:#f5f5f5;">
			<th colspan="6" align="left">收货人信息</th>
		</tr>
		<tr>
			<th width="10%">收货人姓名：</th>
			<td>${(orderInfo.consigneeName)!}</td>
			<th width="10%">邮编：</th>
			<td width="15%">${(orderInfo.postCode)!}</td>
			<th width="10%">手机：</th>
			<td width="15%">${(orderInfo.mobile)!}</td>
		</tr>
		
		<tr>
			<th>收货地址：</th>
			<td>${(orderInfo.address)!}</td>
			<th>身份证号：</th>
			<td>${(orderInfo.identityCode)!}</td>
			<th>真实姓名：</th>
			<td>${(orderInfo.realName)!}</td>
		</tr>
		<tr>
			<th>下载身份证：</th>
			<td colspan="5">
			    <#if (orderInfo.identifyFileFront)?exists && orderInfo.identifyFileFront != null>
			        <img width="220" height="150" src="${orderInfo.identifyImageFront}" /><a href="javascript:void(0)" onclick="downloadFile('${orderInfo.identifyFileFront}')">下载</a>
			    </#if>
			    <#if (orderInfo.idenfifyImageBack)?exists && orderInfo.idenfifyImageBack != null>
			        <img  width="220" height="150" src="${orderInfo.idenfifyImageBack}" /><a href="javascript:void(0)" onclick="downloadFile('${orderInfo.identifyFileBack}')">下载</a>
			    </#if>
			</td>
		</tr>
	</table>
	
	<table class="table table-striped table-bordered table-hover">
		<tr style="background-color:#f5f5f5;">
			<th colspan="6" align="left">财务信息</th>
		</tr>
		<tr>
			<th width="10%">商品总金额：</th>
			<td>${(orderInfo.itemTotal)?string('0.00')}</td>
			<th width="10%">实际支付金额：</th>
			<td width="15%">${(orderInfo.total)?string('0.00')}</td>
			<th width="10%">优惠总金额：</th>
			<td width="15%">${(orderInfo.discount)!}</td>
		</tr>
		<tr>
			<th>运费：</th>
			<td>${(orderInfo.freight)?string('0.00')}</td>
			<th>发票抬头：</th>
			<td colspan="3">${(orderInfo.titleStr)!}</td>
		</tr>
	</table>
	
	<table class="table table-striped table-bordered table-hover">
		<tr style="background-color:#f5f5f5;">
			<th colspan="13">商品信息</th>
		</tr>
		<tr style="background: none repeat scroll 0% 0% rgb(245, 245, 245);" align="center">
			<td>商品编号</td>
			<td>商品名称</td>
			<td>供应商SKU</td>
			<#--<td>商品条形码</td>-->
			<td>品牌名称</td>
			<td>备案号（海关）</td>
			<td>西客商城价</td>
			<td>优惠金额</td>
			<td>实付价</td>
			<#--<td>商品数量</td>-->
			<td>实付小计</td>
		</tr>
		<#if (orderInfo.productList)?exists>      
		<#list orderInfo.productList as product>
		<tr align="center">
			<td>${(product.productCode)!}</td>
			<td>${(product.productName)!}</td>
			<td>${(product.barCode)!}</td>
			<#--<td>${(product.barCode)!}</td>-->
			<td>${(product.brandName)!}</td>
			<td>${(product.productCodeHaitao)!}</td>
			<td>${(product.price)!}</td>
			<td>${(product.discount)!}</td>
			<td>${(product.realPrice)!}</td>
			<#--<td>${(product.quantity)!}</td>-->
			<td>${(product.total)!}</td>
		</tr>
		</#list>
		</#if>
	</table>
    <div><#-- panel-body -->
</div>
	<div class="fastuserinfodiv" style="display:none;">
		<#if fastUserInfoList??>
			<select class="select fastuserinfo">
				<#list fastUserInfoList as fastUserInfo>
					<option value="联系信息:${fastUserInfo.userName},${fastUserInfo.mobile}" fastuserid="${fastUserInfo.fastUserId}">${fastUserInfo.userName}-${fastUserInfo.userTypeCn}</option>
				</#list>
			</select>
		</#if>
	</div>
</@sellContent>