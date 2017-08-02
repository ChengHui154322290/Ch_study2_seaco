package com.tp.common.vo.mem;

import java.io.Serializable;
import java.util.Random;

public class SmsUtil implements Serializable {

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 8529155858955740858L;

	private final static Integer RANDOM_MIN = 100000;
	
	private final static Integer RANDOM_MAX = 999999;

	/**
	 * 
	 * <pre>
	 * 得到注册时的手机验证内容
	 * </pre>
	 *
	 * @param randomNumber
	 *            验证码
	 * @return
	 */
	public static String getRegSmsContent(Integer randomNumber) {
		
		String smsContent = "【西客商城】您正在进行注册操作，验证码为："+randomNumber+"，15分钟内有效，请妥善保管。";
		return smsContent;
	}
	
	public static String getRegDSSSmsContent(Integer randomNumber) {
		
		String smsContent = "【西客商城】您正在进行注册分销员操作，验证码为："+randomNumber+"，15分钟内有效，请妥善保管。";
		return smsContent;
	}
	public static String getUnionBindmblContent(Integer randomNumber) {
		
		String smsContent = "【西客商城】您正在进绑定手机号操作，验证码为："+randomNumber+"，15分钟内有效，请妥善保管。";
		return smsContent;
	}

	public static String getResetPwdSmsContent(Integer randomNumber) {
		String smsContent = "【西客商城】您正在进行找回密码操作，验证码为："+randomNumber+"，15分钟内有效，请妥善保管。";
		return smsContent;
	}
	
	public static String getApplyReturnGodsSmsContent(String orderCode) {
		String smsContent = "【西客商城】您的订单"+orderCode+"已申请退货,请在24小时内将退货寄回,具体退货地址请登录账号查看!";
		return smsContent;
	}
	
	public static String getBindPhoneSmsContent() {
		String smsContent = "【西客商城】手机号绑定成功！";
		return smsContent;
	}
	
	@Deprecated
	public static String getFavoritePromotionSmsContent(String promotionName) {
		String smsContent = "【西客商城】您关注的[" + promotionName + "]还有15分钟即将开售。";
		return smsContent;
	}
	
	
	public static String getFavoritePromotionSmsContent1(String promotionName) throws Exception {
		String smsContent = "【西客商城】您关注的[" + promotionName + "]即将开售，快去看看吧~m.51seaco.com ";
		return smsContent;
	}
	
	
	
	/**
	 * 
	 * <pre>
	 * 得到注册时的手机验证内容
	 * </pre>
	 *
	 * @param randomNumber
	 *            验证码
	 * @return
	 */
	public static String getQuickRegSmsContent(Integer randomNumber) {
		String smsContent = "【西客商城】验证码:" + randomNumber
				+ ",该验证码用于快捷注册后的登录密码,下次可以通过手机号登录哦,亲(请勿向任何人提供您收到的短信验证码)";
		return smsContent;
	}
	
	/**
	 * 
	 * <pre>
	 * 绑定手机短信内容
	 * </pre>
	 *
	 * @param randomNumber
	 *            验证码
	 * @return
	 */
	public static String getBindMobileSmsContent(Integer randomNumber) {
		String smsContent = "【西客商城】您正在进行绑定手机操作，验证码为："+randomNumber+"，15分钟内有效，请妥善保管。";
		return smsContent;
	}
	
	/**
	 * 领取优惠券的手机短信验证
	 * @param randomNumber: 验证码
	 * @return
	 */
	public static String getReceiveCouponSmsContent(Integer randomNumber) {
		String smsContent = "【西客商城】您正在进行领取优惠券操作，验证码为："+randomNumber+"，15分钟内有效，请妥善保管。";
		return smsContent;
	}
	
	/**
	 * 
	 * <pre>
	 *  获得6位数的验证码
	 * </pre>
	 *
	 * @return
	 */
	public static Integer getRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(RANDOM_MAX)%(RANDOM_MAX-RANDOM_MIN+1) + RANDOM_MIN;
		return randomNumber;
	}
}
