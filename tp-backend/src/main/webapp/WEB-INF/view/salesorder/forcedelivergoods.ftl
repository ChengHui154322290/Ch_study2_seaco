<#include "/common/common.ftl"/> 
<@backend title="" js=[ '/statics/backend/js/dateTime2/js/jquery-ui.min.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
	'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
	'/statics/backend/js/salesorder/forceDeliverGoods.js'] 
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css','/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css'] >

<form action="${domain}/salesorder/forcedelivergoods.htm" method="post" class="forcedelivergoodsform">
<div id="search_bar" class="mt10">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">强制发货 (注：<span style="color: red">每次最多只能发50条，格式:订单编号</span><span style="color: blue">空格</span><span style="color: red">快递名称</span><span style="color: blue">空格</span><span style="color: red">快递编号</span><span style="color: blue">空格</span><span style="color: red">运单号 　为一条</span>)</span></b>
			</div>
			<div class="box_top">
				<b class="pl15"><span style="color: red">只能在发货正常发货失败后，没有其它方案选择时使用，副作用很大(强制发货只更新订单状态，不会取消库存等其它操作)，每次操作，都会记录操作人及操作时间，请谨慎使用</span></b>
			</div>
			<div class="box_top">
				<b class="pl15"><span style="color: red">请确认发货信息的正确性后才进行发货,可复制excel中订单号，快递名称，快递编号，运单号这四列数据，不要复制列标题</span></b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>发货信息</td>
						<td><textarea style="height:150px;width:680px" class="input-text lh30 deliverygoodlist"></textarea><span style="color: blue" title="1115061200152110　中通速递	zhongtong	363668235537">例</span></td>
						<td><div class="deliverygooddiv"></div></td>
					</tr>
					<tr>
						<td> 　</td>
						<td><input type="button" value="发货" class="btn btn82 btn_save2 deliverygoodbtn"/></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<div id="table" class="mt10">
	<div class="box span10 oh">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
		<#if results??>
		<tr>
			<th width="10%">子订单编号 </th>
			<th width="5%">错误编码</th>
			<th>信息描述</th>	
			<th width="10%">当前状态</th>
		</tr>
		<#if results.orderOperatorErrorList??> 
		<#list results.orderOperatorErrorList as orderOperatorError>
		<tr>
			<td>${orderOperatorError.subOrderCode}</td>
			<td>${orderOperatorError.errorCode}</td>
			<td>${orderOperatorError.errorMessage}</td>
			<td>${orderOperatorError.orderStatus}</td>
		</tr>
		</#list> 
		</#if>
		<#elseif resultData??>
			<tr><td><#if resultData.errorCode==1>传入参数为空<#elseif resultData.errorCode==2>传入参数为空<#elseif resultData.errorCode==3>要发货条数大于100条<#else>处理成功</#if></td></tr>
		</#if>
	</table>
</div>	
</div>
</form>
</@backend>
