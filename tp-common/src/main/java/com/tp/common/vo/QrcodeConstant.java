package com.tp.common.vo;
/**
 * 二维码相关的常量
 * @author zhuss
 */
public interface QrcodeConstant {
	
	public static final String QRCODE = "qrscene_";//微信关注事件 自带参数

	public static final String QRCODE_CHANNEL = "qc_"; //渠道推广的二维码
	
	public static final String QRCODE_COUPON_CODE = "qcc_zhonglai"; //活动的优惠码的二维码
	
	public static final String QRCODE_PROMOTER_USER_CODE = "qpu_"; //扫描推广员
	
	public enum CHANNEL_SPECIAL{
		ZL("zhonglai");
		public String channle;
		
		private CHANNEL_SPECIAL(String channle){
			this.channle = channle;
		}
	}
	
	//二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
	public enum SCAN_TYPE{
		QR_SCENE("临时"),
		QR_LIMIT_SCENE("永久"),
		QR_LIMIT_STR_SCENE("永久的字符串参数值");
		public String desc;
		
		private SCAN_TYPE(String desc){
			this.desc = desc;
		}
	}
}
