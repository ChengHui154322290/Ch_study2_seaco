<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>跳转快钱支付平台</title>
</head>
<body>
	<form name="kqPay" action="${payInfo.actionurl}" method="post">
			<input type="hidden" name="inputCharset" value="${payInfo.inputcharset}" />
			<input type="hidden" name="pageUrl" value="${payInfo.pageurl}" />
			<input type="hidden" name="bgUrl" value="${payInfo.bgurl}" />
			<input type="hidden" name="version" value="${payInfo.version}" />
			<input type="hidden" name="language" value="${payInfo.language}" />
			<input type="hidden" name="signType" value="${payInfo.signtype}" />
			<input type="hidden" name="signMsg" value="${payInfo.signmsg}" />
			<input type="hidden" name="merchantAcctId" value="${payInfo.merchantacctid}" />
			<input type="hidden" name="mobileGateway" value="${payInfo.mobilegateway}" />
			<input type="hidden" name="payerName" value="${payInfo.payername}" />
			<input type="hidden" name="payerIdType" value="${payInfo.payeridtype}" />
			<input type="hidden" name="payerId" value="${payInfo.payerid}" />
			<input type="hidden" name="orderId" value="${payInfo.orderid}" />
			<input type="hidden" name="orderAmount" value="${payInfo.orderamount}" />
			<input type="hidden" name="orderTime" value="${payInfo.ordertime}" />
			<input type="hidden" name="productName" value="${payInfo.productname}" />
			<input type="hidden" name="productNum" value="${payInfo.productnum}" />
			<input type="hidden" name="productId" value="${payInfo.productid}" />
			<input type="hidden" name="productDesc" value="${payInfo.productdesc}" />
			<input type="hidden" name="ext1" value="${payInfo.ext1}" />
			<input type="hidden" name="ext2" value="${payInfo.ext2}" />
			<input type="hidden" name="payType" value="${payInfo.paytype}" />
			<input type="hidden" name="bankId" value="${payInfo.bankid}" />
			<input type="hidden" name="redoFlag" value="${payInfo.redoflag}" />
			<input type="hidden" name="pid" value="${payInfo.pid}" />
		</form>
		<script type="text/javascript">document.all.kqPay.submit();</script> 
</body>
</html>