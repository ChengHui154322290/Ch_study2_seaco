package com.tp.service.pay.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import com.tp.dto.pay.postdata.AliPayRefundPostData;
import com.tp.result.pay.TradeStatusResult;
import com.tp.util.AlipayUtil;
import com.tp.util.MD5;
import com.tp.util.RSA;

/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 *详细：构造支付宝各接口表单HTML文本，获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-13
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipaySubmit {

	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	public static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

	private static final String _INPUT_CHARSET_UTF8 = "utf-8";
	/**
	 * 生成签名结果
	 * 
	 * @param sPara
	 *            要签名的数组
	 * @return 签名结果字符串
	 */
	public static String buildRequestMysign(Map<String, String> sPara, String signType, String signKey) {
		String prestr = AlipayCore.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		if ("MD5".equals(signType)){
			return MD5.sign(prestr, signKey, sPara.get("_input_charset"));	
		}
		return MD5.sign(prestr, signKey, sPara.get("_input_charset"));
	}
	@Deprecated
	public static String buildRequestMysign(Map<String, String> sPara) {
		String prestr = AlipayCore.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String mysign = "";
		// if(sPara.get("sign_type").equals("MD5") ) {
		mysign = MD5.sign(prestr, "wy5w10pnojk0ehjqatg4tgomjk582tka", sPara.get("_input_charset"));
		// }
		return mysign;
	}
	/**
	 * 生成要请求给支付宝的参数数组
	 * 
	 * @param sParaTemp
	 *            请求前的参数数组
	 * @return 要请求的参数数组
	 * @deprecated 
	 */
	@Deprecated
	private static Map<String, String> buildRequestPara(
			Map<String, String> sParaTemp) {
		// 除去数组中的空值和签名参数
		Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
		// 生成签名结果
		String mysign = buildRequestMysign(sPara);

		// 签名结果与签名方式加入请求提交参数组中
		sPara.put("sign", mysign);
		sPara.put("sign_type", "MD5");

		return sPara;
	}

	private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp, String signType, String signKey) {
		// 除去数组中的空值和签名参数
		Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
		// 生成签名结果
		String mysign = buildRequestMysign(sPara, signType, signKey);
		// 签名结果与签名方式加入请求提交参数组中
		sPara.put("sign", mysign);
		sPara.put("sign_type", signType);
		return sPara;
	}
	/**
	 * 建立请求，以表单HTML形式构造（默认）
	 * 
	 * @param sParaTemp
	 *            请求参数数组
	 * @param strMethod
	 *            提交方式。两个值可选：post、get
	 * @param strButtonName
	 *            确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	@Deprecated
	public static String buildRequest(Map<String, String> sParaTemp,
			String strMethod, String strButtonName) {
		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp);
		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<html><body><form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\""
				+ ALIPAY_GATEWAY_NEW
				+ "_input_charset="
				+ sPara.get("_input_charset")
				+ "\" method=\""
				+ strMethod
				+ "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);

			sbHtml.append("<input type=\"hidden\" name=\"" + name
					+ "\" value=\"" + value + "\"/>");
		}

		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName
				+ "\" style=\"display:none;\"></form></body></html>");
		sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

		return sbHtml.toString();
	}

	/**
	 * 建立请求，以表单HTML形式构造，带文件上传功能
	 * 
	 * @param sParaTemp
	 *            请求参数数组
	 * @param strMethod
	 *            提交方式。两个值可选：post、get
	 * @param strButtonName
	 *            确认按钮显示文字
	 * @param strParaFileName
	 *            文件上传的参数名
	 * @return 提交表单HTML文本
	 */
	@Deprecated
	public static String buildRequest(Map<String, String> sParaTemp,
			String strMethod, String strButtonName, String strParaFileName) {
		// 待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp);
		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\"  enctype=\"multipart/form-data\" action=\""
				+ ALIPAY_GATEWAY_NEW
				+ "_input_charset="
				+ sPara.get("_input_charset")
				+ "\" method=\""
				+ strMethod
				+ "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);

			sbHtml.append("<input type=\"hidden\" name=\"" + name
					+ "\" value=\"" + value + "\"/>");
		}

		sbHtml.append("<input type=\"file\" name=\"" + strParaFileName
				+ "\" />");

		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName
				+ "\" style=\"display:none;\"></form>");

		return sbHtml.toString();
	}

	public static TradeStatusResult singleTradeQuery(Properties paymentConfig, String outTradeNo, boolean isInternational) throws Exception {

		Map<String, String> sParam = new HashMap<String, String>();
		sParam.put("service", "single_trade_query");
		sParam.put("partner", paymentConfig.getProperty("ALIPAY_PARTNER"));
		if(isInternational)
			sParam.put("partner", paymentConfig.getProperty("ALIPAY_MERGEALIPAY_PARTNER"));
		sParam.put("_input_charset", paymentConfig.getProperty("ALIPAY_INPUT_CHARSET"));
		
		sParam.put("out_trade_no", outTradeNo);
		String xml = null;
		if(isInternational){
			sParam.put("sign_type", "MD5");
			sParam.put("sign", AlipayUtil.buildRequestMysign(sParam, paymentConfig.getProperty("ALIPAY_MERGEALIPAY_KEY")));
			xml = buildInternaltionalRequest(sParam, "gb2312");
		}
		else{
			sParam = buildRequestPara(sParam, "MD5", paymentConfig.getProperty("ALIPAY_KEY"));
			xml = buildRequest("", "", sParam, "gb2312");
		}
		
		TradeStatusResult result = new TradeStatusResult();
		Pattern p = Pattern.compile(".*?<is_success>(.*?)</is_success>.*?");
		Matcher m = p.matcher(xml);
		if (m.find()) {
			if ("T".equals(m.group(1))) {
				p = Pattern.compile(".*?<trade_no>(.*?)</trade_no>.*?");
				m = p.matcher(xml);
				if (m.find()) {
					result.setTradeNo(m.group(1));
					p = Pattern
							.compile("<trade_status>(TRADE_SUCCESS|TRADE_FINISHED)</trade_status>");
					m = p.matcher(xml);
					if (m.find()) {
						result.setSuccess(true);
					} else {
						result.setSuccess(false);
						p = Pattern
								.compile("<trade_status>(.*?)</trade_status>");
						m = p.matcher(xml);
						if (m.find())
							result.setErrorMsg(m.group(1));
					}
				} else {
					result.setSuccess(false);
					result.setErrorMsg("返回结果异常");
				}

			} else {
				result.setSuccess(false);
				p = Pattern.compile(".*?<error>(.*?)</error>.*?");
				m = p.matcher(xml);
				if (m.find())
					result.setErrorMsg(m.group(1));
			}
		}
		return result;
	}
	
	public static TradeStatusResult singleRefundQuery(Properties paymentConfig, String outTradeNo, boolean isInternational) throws Exception {

		Map<String, String> sParam = new HashMap<String, String>();
		sParam.put("service", "single_trade_query");
		sParam.put("partner", paymentConfig.getProperty("ALIPAY_PARTNER"));
		if(isInternational)
			sParam.put("partner", paymentConfig.getProperty("ALIPAY_INTERNATIONAL_PARTNER"));
		sParam.put("_input_charset", paymentConfig.getProperty("ALIPAY_INPUT_CHARSET"));
		
		sParam.put("out_trade_no", outTradeNo);
		String xml = null;
		if(isInternational){
			sParam.put("sign_type", "MD5");
			sParam.put("sign", AlipayUtil.buildRequestMysign(sParam, paymentConfig.getProperty("ALIPAY_INTERNATIONAL_KEY")));
			xml = buildInternaltionalRequest(sParam, "gb2312");
		}
		else{
			sParam = buildRequestPara(sParam, "MD5", paymentConfig.getProperty("ALIPAY_KEY"));
			xml = buildRequest("", "", sParam, "gb2312");
		}
		System.out.println(xml);
		TradeStatusResult result = new TradeStatusResult();
		Pattern p = Pattern.compile(".*?<is_success>(.*?)</is_success>.*?");
		Matcher m = p.matcher(xml);
		if (m.find()) {
			if ("T".equals(m.group(1))) {
				p = Pattern.compile(".*?<trade_no>(.*?)</trade_no>.*?");
				m = p.matcher(xml);
				if (m.find()) {
					result.setTradeNo(m.group(1));
					p = Pattern.compile("<refund_status>(REFUND_SUCCESS|REFUND_FINISHED)</refund_status>");
					m = p.matcher(xml);
					if (m.find()) {
						result.setSuccess(true);
					} else {
						result.setSuccess(false);
						p = Pattern
								.compile("<trade_status>(.*?)</trade_status>");
						m = p.matcher(xml);
						if (m.find())
							result.setErrorMsg(m.group(1));
					}
				} else {
					result.setSuccess(false);
					result.setErrorMsg("返回结果异常");
				}

			} else {
				result.setSuccess(false);
				p = Pattern.compile(".*?<error>(.*?)</error>.*?");
				m = p.matcher(xml);
				if (m.find())
					result.setErrorMsg(m.group(1));
			}
		}
		return result;
	}
	public static String refund(AliPayRefundPostData refundData) throws Exception {
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "refund_fastpay_by_platform_nopwd");
        sParaTemp.put("partner", refundData.getPartner());
        sParaTemp.put("_input_charset", refundData.getInputCharset());
		sParaTemp.put("notify_url", refundData.getNotifyUrl());
		sParaTemp.put("batch_no", refundData.getBatchNo());
		sParaTemp.put("refund_date", refundData.getRefundDate());
		sParaTemp.put("batch_num", refundData.getBatchNum());
		sParaTemp.put("detail_data", refundData.getDetailData());
		
		//建立请求
		sParaTemp = buildRequestPara(sParaTemp, "MD5", refundData.getKey());
		String sHtmlText = AlipaySubmit.buildRequest("", "", sParaTemp, "gb2312");
		return sHtmlText;
	}
	public static void main(String[] args) throws Exception {
		String tradeNos[] = new String[]{
				"42016051375"};
		System.out.println(tradeNos.length);
		for(int i=0; i<tradeNos.length; i++){
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "single_trade_query");
			sParaTemp.put("partner", "2088121396013774");
//			sParaTemp.put("partner", "2088221414569800");
	        sParaTemp.put("_input_charset", "UTF-8");
			sParaTemp.put("out_trade_no", tradeNos[i]);
//			sParaTemp.put("sign", AlipayUtil.buildRequestMysign(sParaTemp, "yg03g11t3mezvmrdlbh4eqioz8flagoc"));
//			合并国际支付宝
//			sParaTemp.put("sign", AlipayUtil.buildRequestMysign(sParaTemp, "smkgmguaj39w3w3bm1nhizlem1spfjkm"));
			
			try {
//				String xml = buildInternaltionalRequest(sParaTemp,"utf-8");
				String xml = buildRequest("", "", buildRequestPara(sParaTemp, "MD5", "wy5w10pnojk0ehjqatg4tgomjk582tka"), "gb2312");
				System.out.println(xml);
				TradeStatusResult result = new TradeStatusResult();
				Pattern p = Pattern.compile(".*?<is_success>(.*?)</is_success>.*?");
				Matcher m = p.matcher(xml);
				if(m.find()){
					if ("T".equals(m.group(1))) {
						p = Pattern.compile(".*?<trade_no>(.*?)</trade_no>.*?");
						m = p.matcher(xml);
						if (m.find()) {
							result.setTradeNo(m.group(1));
							p = Pattern.compile("<trade_status>(TRADE_SUCCESS|TRADE_FINISHED)</trade_status>");
							m = p.matcher(xml);
							if (m.find()) {
								result.setSuccess(true);
								p = Pattern.compile("<gmt_payment>(.*?)</gmt_payment>");
								m = p.matcher(xml);
								System.out.print(i+"  ");
								System.out.print(sParaTemp.get("out_trade_no")+"  ");
								if (m.find()) {
									System.out.print(m.group(1)+"  ");
								}
								p = Pattern.compile("<total_fee>(.*?)</total_fee>");
								m = p.matcher(xml);
								if (m.find()) {
									System.out.println(m.group(1));
								}
							} else {
								System.out.print(sParaTemp.get("out_trade_no")+"  ");
								result.setSuccess(false);
								p = Pattern.compile("<trade_status>(.*?)</trade_status>");
								m = p.matcher(xml);
								if (m.find())
									result.setErrorMsg(m.group(1));
							}
						} else {
							result.setSuccess(false);
							result.setErrorMsg("返回结果异常");
						}

					} else {
						result.setSuccess(false);
						p = Pattern.compile(".*?<error>(.*?)</error>.*?");
						m = p.matcher(xml);
						if (m.find())
							result.setErrorMsg(m.group(1));
					}
				}
//				System.out.println(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	  
	}
	/**
	 * 建立请求，以模拟远程HTTP的POST请求方式构造并获取支付宝的处理结果
	 * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值 如：buildRequest("",
	 * "",sParaTemp)
	 * 
	 * @param strParaFileName
	 *            文件类型的参数名
	 * @param strFilePath
	 *            文件路径
	 * @param sParaTemp
	 *            请求参数数组
	 * @return 支付宝处理结果
	 * @throws Exception
	 */
	public static String buildRequest(String strParaFileName, String strFilePath, Map<String, String> sPara, String charset) throws Exception {
		// 待请求参数数组
		/*Map<String, String> sPara = buildRequestPara(sParaTemp);*/

		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset("utf-8");

		request.setParameters(generatNameValuePair(sPara));
		request.setUrl(ALIPAY_GATEWAY_NEW + "_input_charset=" + sPara.get("_input_charset"));

		HttpResponse response = httpProtocolHandler.execute(request, strParaFileName, strFilePath);
		if (response == null) {
			return null;
		}

		String strResult = new String(response.getByteResult(), charset);

		return strResult;
	}

	public static String buildInternaltionalRequest(Map<String, String> sParaTemp, String charset) throws Exception{
		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset("utf-8");

		request.setParameters(generatNameValuePair(sParaTemp));
		request.setUrl(ALIPAY_GATEWAY_NEW + "_input_charset=" + sParaTemp.get("_input_charset"));

		HttpResponse response = httpProtocolHandler.execute(request, "", "");
		if (response == null) {
			return null;
		}
		Header[] headers = response.getResponseHeaders();
		String strResult = new String(response.getByteResult(), charset);

		return strResult;
	}
	/**
	 * MAP类型数组转换成NameValuePair类型
	 * 
	 * @param properties
	 *            MAP类型数组
	 * @return NameValuePair类型数组
	 */
	private static NameValuePair[] generatNameValuePair(
			Map<String, String> properties) {
		NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			nameValuePair[i++] = new NameValuePair(entry.getKey(),
					entry.getValue());
		}

		return nameValuePair;
	}

	/**
	 * 生成签名结果
	 * 
	 * @param sPara
	 *            要签名的数组
	 * @return 签名结果字符串
	 */
	public static String BuildRequestMysignAdapter(Map<String, String> sPara, String privateKey) {
		String prestr = AlipayCore.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

		String mysign = "";
		/*
		 * if(AlipayConfig.sign_type.equals("MD5") ) { mysign = MD5.sign(prestr,
		 * AlipayConfig.key, AlipayConfig.input_charset); }
		 * if(AlipayConfig.sign_type.equals("0001") ){ mysign = RSA.sign(prestr,
		 * AlipayConfig.private_key, AlipayConfig.input_charset); }
		 */

		mysign = RSA.sign(prestr, privateKey, "UTF-8");
		return mysign;
	}

	/**
	 * 生成要请求给支付宝的参数数组
	 * 
	 * @param sParaTemp
	 *            请求前的参数数组
	 * @param privateKey RSA 签名需要的私钥
	 * @return 要请求的参数数组
	 */
	public static Map<String, String> BuildRequestParaAdapter(Map<String, String> sParaTemp, String privateKey) {
		// 除去数组中的空值和签名参数
		Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
		// 生成签名结果
		String mysign = BuildRequestMysignAdapter(sPara, privateKey);

		// 签名结果与签名方式加入请求提交参数组中
		sPara.put("sign", mysign);
		if (!sPara.get("service").equals("alipay.wap.trade.create.direct")
				&& !sPara.get("service").equals(
						"alipay.wap.auth.authAndExecute")) {
			sPara.put("sign_type", "RSA");
		}

		return sPara;
	}

	/**
	 * 建立请求，以模拟远程HTTP的POST请求方式构造并获取支付宝的处理结果
	 * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值 如：buildRequest("",
	 * "",sParaTemp)
	 * 
	 * @paramALIPAY_GATEWAY_NEW 支付宝网关地址
	 * @param strParaFileName
	 *            文件类型的参数名
	 * @param strFilePath
	 *            文件路径
	 * @param sParaTemp
	 *            请求参数数组
	 * @param privateKey RSA签名需要的私钥
	 * @return 支付宝处理结果
	 * @throws Exception
	 */
	public static String buildRequest(String ALIPAY_GATEWAY_NEW, String strParaFileName, String strFilePath, Map<String, String> sParaTemp, String privateKey) throws Exception {
		// 待请求参数数组
		Map<String, String> sPara = BuildRequestParaAdapter(sParaTemp, privateKey);

		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler
				.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset(_INPUT_CHARSET_UTF8);

		request.setParameters(generatNameValuePair(sPara));
		request.setUrl(ALIPAY_GATEWAY_NEW + "_input_charset="
				+ _INPUT_CHARSET_UTF8);

		HttpResponse response = httpProtocolHandler.execute(request,
				strParaFileName, strFilePath);
		if (response == null) {
			return null;
		}
		String strResult = response.getStringResult(_INPUT_CHARSET_UTF8);

		return strResult;
	}

	/**
	 * 建立请求，以表单HTML形式构造（默认）
	 * 
	 * @paramALIPAY_GATEWAY_NEW 支付宝网关地址
	 * @param sParaTemp
	 *            请求参数数组
	 * @param strMethod
	 *            提交方式。两个值可选：post、get
	 * @param strButtonName
	 *            确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	public static String buildRequest(String ALIPAY_GATEWAY_NEW, Map<String, String> sParaTemp, String strMethod, String strButtonName, String privateKey) {
		// 待请求参数数组
		Map<String, String> sPara = BuildRequestParaAdapter(sParaTemp, privateKey);
		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\""
				+ ALIPAY_GATEWAY_NEW
				+ "_input_charset="
				+ "utf-8"
				+ "\" method=\"" + strMethod + "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);

			sbHtml.append("<input type=\"hidden\" name=\"" + name
					+ "\" value=\"" + value + "\"/>");
		}

		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName
				+ "\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

		return sbHtml.toString();
	}

	/**
	 * 解析远程模拟提交后返回的信息，获得token
	 * 
	 * @param text
	 *            要解析的字符串
	 * @return 解析结果
	 * @throws Exception
	 */
	public static String getRequestToken(String text, String privateKey) throws Exception {
		String request_token = "";
		// 以“&”字符切割字符串
		String[] strSplitText = text.split("&");
		// 把切割后的字符串数组变成变量与数值组合的字典数组
		Map<String, String> paraText = new HashMap<String, String>();
		for (int i = 0; i < strSplitText.length; i++) {

			// 获得第一个=字符的位置
			int nPos = strSplitText[i].indexOf("=");
			// 获得字符串长度
			int nLen = strSplitText[i].length();
			// 获得变量名
			String strKey = strSplitText[i].substring(0, nPos);
			// 获得数值
			String strValue = strSplitText[i].substring(nPos + 1, nLen);
			// 放入MAP类中
			paraText.put(strKey, strValue);
		}

		if (paraText.get("res_data") != null) {
			String res_data = paraText.get("res_data");
			// 解析加密部分字符串（RSA与MD5区别仅此一句）
			/*
			 * if(AlipayConfig.sign_type.equals("0001")) { res_data =
			 * RSA.decrypt(res_data, AlipayConfig.private_key,
			 * AlipayConfig.input_charset); }
			 */

			res_data = RSA.decrypt(res_data, privateKey, _INPUT_CHARSET_UTF8);

			// token从res_data中解析出来（也就是说res_data中已经包含token的内容）
			Document document = DocumentHelper.parseText(res_data);
			request_token = document.selectSingleNode(
					"//direct_trade_create_res/request_token").getText();
		}
		return request_token;
	}

	/**
	 * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
	 * 
	 * @return 时间戳字符串
	 * @throws IOException
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	/*
	 * public static String query_timestamp() throws MalformedURLException,
	 * DocumentException, IOException {
	 * 
	 * //构造访问query_timestamp接口的URL串 String strUrl = ALIPAY_GATEWAY_NEW +
	 * "service=query_timestamp&partner=" + AlipayConfig.partner +
	 * "&_input_charset" +AlipayConfig.input_charset; StringBuffer result = new
	 * StringBuffer();
	 * 
	 * SAXReader reader = new SAXReader(); Document doc = reader.read(new
	 * URL(strUrl).openStream());
	 * 
	 * List<Node> nodeList = doc.selectNodes("//alipay/*");
	 * 
	 * for (Node node : nodeList) { // 截取部分不需要解析的信息 if
	 * (node.getName().equals("is_success") && node.getText().equals("T")) { //
	 * 判断是否有成功标示 List<Node> nodeList1 =
	 * doc.selectNodes("//response/timestamp/*"); for (Node node1 : nodeList1) {
	 * result.append(node1.getText()); } } }
	 * 
	 * return result.toString(); }
	 */
}
