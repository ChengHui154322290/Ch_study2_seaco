package com.tp.world.convert;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.PaymentConstant.GATEWAY_TYPE;
import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.appdata.AliAppPayData;
import com.tp.dto.pay.appdata.AliInternationalMergeAppPayData;
import com.tp.dto.pay.appdata.AliInternationalMergeAppSdkPayData;
import com.tp.dto.pay.postdata.AliPayPostData;
import com.tp.dto.pay.postdata.WeixinPayPostData;
import com.tp.enums.common.PlatformEnum;
import com.tp.m.base.BaseQuery;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.pay.QueryPay;
import com.tp.m.util.DateUtil;
import com.tp.m.util.NumberUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.groupbuy.OrderRedeemItemVo;
import com.tp.m.vo.pay.APPGJZFBPayVO;
import com.tp.m.vo.pay.APPWXPayVO;
import com.tp.m.vo.pay.APPZFBPayVO;
import com.tp.m.vo.pay.BasePayVO;
import com.tp.m.vo.pay.PayResultVO;
import com.tp.m.vo.pay.PaywayVO;
import com.tp.m.vo.pay.WAPGJZFBPayVO;
import com.tp.m.vo.pay.WAPWXPayVO;
import com.tp.m.vo.pay.WAPZFBPayVO;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.query.pay.AppPaymentCallDto;
import com.tp.util.Base64Util;
import com.tp.world.helper.VersionHelper;

/**
 * 支付封装类
 * @author zhuss
 * @2016年1月7日 下午5:31:28
 */
public class PayConvert {
	
	/**
	 * 临时处理支付方式列表
	 * @return
	 */
	public static List<PaywayVO> counvertPayways(BaseQuery base){
		boolean handleOldVersion = VersionHelper.before120Version(base);
		List<PaywayVO> list = new ArrayList<>();
		if(handleOldVersion)list.add(new PaywayVO(GATEWAY_TYPE.ALIPAY.code, GATEWAY_TYPE.ALIPAY.desc));
		else list.add(new PaywayVO(GATEWAY_TYPE.MEGER_ALIPAY.code, GATEWAY_TYPE.MEGER_ALIPAY.desc));
		list.add(new PaywayVO(GATEWAY_TYPE.WEIXIN.code, GATEWAY_TYPE.WEIXIN.desc));
		//list.add(new PaywayVO(GATEWAY_TYPE.WEIXIN.code, GATEWAY_TYPE.WEIXIN_EXTERNAL.desc));
		return list;
	}
	
	/**
	 * 封装支付方式列表
	 * @param ways
	 * @return
	 */
	public static List<PaywayVO> convertPaywayList(List<PaymentGateway> ways,BaseQuery base){
		boolean handleOldVersion = VersionHelper.before120Version(base);
		List<PaywayVO> list = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(ways)){
			for(PaymentGateway way : ways){
				if(StringUtil.equals(way.getGatewayCode(), GATEWAY_TYPE.MEGER_ALIPAY.code)){
					if(handleOldVersion){
						way.setGatewayCode(GATEWAY_TYPE.ALIPAY.code);
						way.setGatewayName(GATEWAY_TYPE.ALIPAY.desc);
					}
				}
				list.add(new PaywayVO(way.getGatewayCode(),way.getGatewayName()));
			}
		}
		return list;
	}
	
	/**
	 * 检验订单是否符合支付条件
	 */
	public static void checkPayOrder(PaymentInfo info,Long uid){
		if(null == info)throw new MobileException(MResultInfo.PAYINFO_NULL);
		//检查Token中用户ID与订单创建的用户ID是否一致
		if(!StringUtil.equals(info.getCreateUser(), uid))throw new MobileException(MResultInfo.PAYINFO_USER_NOT_MATCH);
		if(StringUtil.equals(info.getCanceled(),1))throw new MobileException(MResultInfo.PAYINFO_ORDER_CANCEL);
		//if(StringUtil.equals(info.getStatus(), PaymentConstant.PAYMENT_STATUS.PAYING.code))throw new MobileException(MResultInfo.PAYINFO_PAYING);
		if(StringUtil.equals(info.getStatus(), PaymentConstant.PAYMENT_STATUS.PAYED.code))throw new MobileException(MResultInfo.PAYINFO_PAYED);
		/**if(PaymentConstant.BIZ_TYPE.DSS.code.equals(info.getBizType())){
			throw new MobileException(MResultInfo.ORDER_LIST_EMPTY);
		}*/
	}
	
	/**
	 * 封装立即支付入参对象
	 * @param pay
	 * @return
	 */
	public static AppPaymentCallDto convertPayQuery(QueryPay pay){
		AppPaymentCallDto apc = new AppPaymentCallDto();
		apc.setGateway(pay.getPayway());
		apc.setSdk(pay.isSdk());
		apc.setPaymentId(StringUtil.getLongByStr(pay.getPayid()));
		apc.setUserId(pay.getUserid().toString());
		//微信浏览器的微信支付
		if((StringUtil.equals(pay.getPayway(), PaymentConstant.GATEWAY_TYPE.WEIXIN.code)|| StringUtil.equals(pay.getPayway(), GATEWAY_TYPE.WEIXIN_EXTERNAL.code))&&!pay.isSdk()){
			Map<String,Object> params = new HashMap<>();
			params.put("openid", pay.getOpenid());
			apc.setParams(params);
		}
		//第三方商城支付
		if (StringUtil.isNotBlank(pay.getChannelcode())) {
			Map<String, Object> params =  (apc.getParams() == null) ? (new HashMap<String, Object>()) : apc.getParams();
			params.put("channelCode", pay.getChannelcode());
			apc.setParams(params);
		}

		return apc;
	}
	
	/**
	 * 封装WAP的支付信息
	 * @return
	 */
	public static BasePayVO convertWapPayInfo(AppPayData payData,PaymentInfo info){
		BasePayVO payinfo  = new BasePayVO();
		convertBasePay(info,payinfo);
		if(StringUtil.equals(info.getAmount(), NumberUtil.DOUBLE_ZERO)){//0元订单
			return payinfo;
		}
		if (StringUtil.equals(info.getGatewayCode(), PaymentConstant.GATEWAY_TYPE.ALIPAY.code)) {// 支付宝支付
			AliPayPostData alip = (AliPayPostData) payData;
			WAPZFBPayVO wapzfb = convertWAPZFB(alip);
			convertBasePay(info,wapzfb);
			return wapzfb;
		}else if(StringUtil.equals(info.getGatewayCode(), PaymentConstant.GATEWAY_TYPE.MEGER_ALIPAY.code)){//国际支付宝 - 合并支付
			AliInternationalMergeAppPayData wapergeAlip = (AliInternationalMergeAppPayData) payData;
			WAPGJZFBPayVO wapgjzfb = convertWAPGJZFB(wapergeAlip);
			convertBasePay(info,wapgjzfb);
			return wapgjzfb;
		}else if(StringUtil.equals(info.getGatewayCode(), PaymentConstant.GATEWAY_TYPE.WEIXIN.code) ||StringUtil.equals(info.getGatewayCode(), GATEWAY_TYPE.WEIXIN_EXTERNAL.code) ){//微信支付
			WeixinPayPostData wxpostdata = (WeixinPayPostData) payData;
			WAPWXPayVO wapwx = convertWAPWX(wxpostdata);
			convertBasePay(info,wapwx);
			return wapwx;
		} 
		return payinfo;
	}
	
	/**
	 * 封装父类参数
	 * @return
	 */
    public static void convertBasePay(PaymentInfo info,BasePayVO payinfo){
    	payinfo.setPrice(StringUtil.getStrByObj(info.getAmount()));
    	payinfo.setOrdercode(StringUtil.getStrByObj(info.getBizCode()));
		payinfo.setPayid(StringUtil.getStrByObj(info.getPaymentId()));
	}
	/**
	 * 封装WAP支付宝支付信息
	 * @return
	 */
	public static WAPZFBPayVO convertWAPZFB(AliPayPostData alip){
		WAPZFBPayVO to = new WAPZFBPayVO();
		if(null != alip){
			to.setActionurl(alip.getWap_action_url());
			to.setInputcharset(alip.getInputCharset());
			to.setNotifyurl(alip.getNotifyUrl());
			to.setOuttradeno(alip.getOutTradeNo());
			to.setPartner(alip.getPartner());
			to.setPaymenttype(alip.getPaymentType());
			to.setReturnurl(alip.getReturnUrl());
			to.setSellerid(alip.getSellerEmail());
			to.setService(alip.getService());
			to.setShowurl(alip.getShowUrl());
			to.setSubject(alip.getSubject());
			to.setTotalfee(alip.getTotalFee());
			to.setSign(alip.getSign());
			to.setSigntype(alip.getSignType());
		}
		return to;
	}
	
	/**
	 * 封装WAP国际支付宝支付信息
	 * @return
	 */
	public static WAPGJZFBPayVO convertWAPGJZFB(AliInternationalMergeAppPayData alip){
		WAPGJZFBPayVO to = new WAPGJZFBPayVO();
		if(null != alip){
			to.setActionurl(alip.getActionUrl());
			to.setBody(alip.getBody());
			to.setCurrency(alip.getCurrency());
			to.setInputcharset(alip.getInputCharset());
			to.setMerchanturl(alip.getMerchantUrl());
			to.setNotifyurl(alip.getNotifyUrl());
			to.setOuttradeno(alip.getOutTradeNo());
			to.setPartner(alip.getPartner());
			to.setProductcode(alip.getProductCode());
			to.setReturnurl(alip.getReturnUrl());
			to.setRmbfee(alip.getRmbFee());
			to.setSigntype(alip.getSignType());
			to.setService(alip.getService());
			to.setSplitfundinfo(alip.getSplit_fund_info());
			to.setSign(alip.getSignature());
			to.setSubject(alip.getSubject());
		}
		return to;
	}
	
	/**
	 * 封装WAP微信支付信息
	 * @return
	 */
	public static WAPWXPayVO convertWAPWX(WeixinPayPostData wxpostdata){
		WAPWXPayVO wx = new WAPWXPayVO();
		if(null != wxpostdata){
			wx.setAppId(wxpostdata.getAppid());
			wx.set_package("prepay_id="+wxpostdata.getPrepayId());
			wx.setNonceStr(wxpostdata.getNonce_str());
			wx.setTimeStamp(StringUtil.getStrByObj(DateUtil.convertSecondByStr(wxpostdata.getTime_start(), DateUtil.WX_PAY_TIME_FORMAT)));
			wx.setPaySign(wxpostdata.getSign());
			wx.setSignType("MD5");
			wx.setReturnurl(wxpostdata.getReturnUrl());
		}
		return wx;
	}
	
	/**
	 * 封装APP的支付信息
	 * @return
	 */
	public static BasePayVO convertAppPayInfo(AppPayData payData,PaymentInfo info,String platform){
		BasePayVO payinfo  = new BasePayVO();
		convertBasePay(info,payinfo);
		if(StringUtil.equals(info.getAmount(), NumberUtil.DOUBLE_ZERO)){//0元订单
			return payinfo;
		}
		if (StringUtil.equals(info.getGatewayCode(), PaymentConstant.GATEWAY_TYPE.ALIPAY.code)) {// 支付宝支付
			AliAppPayData ali = (AliAppPayData) payData;
			APPZFBPayVO appzfb = convertAPPZFB(ali);
			convertBasePay(info,appzfb);
			return appzfb;
		}else if(StringUtil.equals(info.getGatewayCode(), PaymentConstant.GATEWAY_TYPE.MEGER_ALIPAY.code)){//国际支付宝 - 合并支付
			AliInternationalMergeAppSdkPayData  ergeAlip = (AliInternationalMergeAppSdkPayData) payData;
			APPGJZFBPayVO appgjzfb = convertAPPGJZFB(ergeAlip);
			convertBasePay(info,appgjzfb);
			return appgjzfb;
		}else if(StringUtil.equals(info.getGatewayCode(), PaymentConstant.GATEWAY_TYPE.WEIXIN.code) || StringUtil.equals(info.getGatewayCode(), GATEWAY_TYPE.WEIXIN_EXTERNAL.code)){//微信支付 & 微信境外支付
			WeixinPayPostData wxpostdata = (WeixinPayPostData) payData;
			APPWXPayVO appwx = convertAPPWX(wxpostdata,platform);
			convertBasePay(info,appwx);
			return appwx;
		} 
		return payinfo;
	}
	
	/**
	 * 封装APP支付宝支付信息
	 * @return
	 */
	public static APPZFBPayVO convertAPPZFB(AliAppPayData ali){
		APPZFBPayVO to = new APPZFBPayVO();
		if(null != ali){
			String orderInfo = ali.getOrderInfo();
			String signedInfo = ali.getSignedString();
			to.setOrderinfo(orderInfo);
			to.setSignedinfo(signedInfo);
		}
		return to;
	}
	
	/**
	 * 封装APP国际支付宝支付信息
	 * @return
	 */
	public static APPGJZFBPayVO convertAPPGJZFB(AliInternationalMergeAppSdkPayData ali){
		APPGJZFBPayVO to = new APPGJZFBPayVO();
		if(null != ali){
			String orderInfo = ali.getInfoData();
			String signedInfo = ali.getSignature();
			to.setOrderinfo(orderInfo);
			to.setSignedinfo(signedInfo);
		}
		return to;
	}
	
	/**
	 * 封装APP微信支付信息
	 * @return
	 */
	public static APPWXPayVO convertAPPWX(WeixinPayPostData wxpostdata,String platform){
		APPWXPayVO to = new APPWXPayVO();
		if(null != wxpostdata){
			to.setAppid(wxpostdata.getAppid());
			to.setPartnerid(wxpostdata.getMch_id());
			to.setPrepayid(wxpostdata.getPrepayId());
			if(StringUtil.equals(platform, PlatformEnum.IOS.name()))to.setSignedinfo(wxpostdata.getIosSign());
			if(StringUtil.equals(platform, PlatformEnum.ANDROID.name()))to.setSignedinfo(wxpostdata.getAndroidSign());
			to.setNoncestr(wxpostdata.getNonce_str());
			to.setTimestart(StringUtil.getStrByObj(DateUtil.convertTimestampByStr(wxpostdata.getTime_start(), DateUtil.WX_PAY_TIME_FORMAT)));
		}
		return to;
	}
	
	/**
	 * 封装支付结果
	 * 支付状态:0未支付 1支付中 2已支付 3支付失败
	 * @param info
	 * @return
	 */
	public static PayResultVO convertPayResult(PaymentInfo info,List<OrderRedeemItem> orderRedeemItemList){
		PayResultVO vo = new PayResultVO();
		List<OrderRedeemItemVo> orderRedeemItemVoList=new ArrayList<OrderRedeemItemVo>();
		for(OrderRedeemItem orderRedeemItem:orderRedeemItemList){
			byte[] redeemCodeBase64=Base64Util.decrypt(orderRedeemItem.getRedeemCode());//将base64转化为明码
			try {
				  String 	redeemCode = new String(redeemCodeBase64, "UTF-8");
				  orderRedeemItem.setRedeemCode(redeemCode);//设置兑换码明码
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();  
				}
			OrderRedeemItemVo  orderRedeemItemVo=new OrderRedeemItemVo();
			BeanUtils.copyProperties(orderRedeemItem, orderRedeemItemVo);//对象属性拷贝
			orderRedeemItemVoList.add(orderRedeemItemVo);
		}
		
		if(null != info){
			vo.setStatus(StringUtil.getStrByObj(info.getStatus()));
			vo.setStatusdesc(PaymentConstant.PAYMENT_STATUS.getCnName(info.getStatus()));
			vo.setOrderRedeemItemList(orderRedeemItemVoList);
		}
		return vo;
	}
}
