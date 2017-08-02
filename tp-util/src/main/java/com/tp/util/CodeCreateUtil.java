package com.tp.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 代码生成器
 * @author szy
 *
 */
public final class CodeCreateUtil {
	private final static String DATE_FORMATE = "yyMMdd";
	private final static int INDEX_NO_LENGTH = 8;
	private final static int INIT_VALUE = 10000000;
	private static final Long INIT = System.currentTimeMillis();
	private static Integer RANDOM_IP = Math.abs((int)System.currentTimeMillis()%9);
	private final static Map<Integer,AtomicLong> INIT_MAP = new HashMap<Integer,AtomicLong>();
	
	static {
		initRandomIp();
		for(BILL_TYPE entry:BILL_TYPE.values()){
			INIT_MAP.put(entry.code, new AtomicLong(INIT));
		}
	}
	
	/**
	 * 生成订单编码
	 * @return long
	 */
	public static Long initOrderCode(){
		return createCode(BILL_TYPE.ORDER.code,Long.valueOf(DateUtil.formatDate(new Date(), DATE_FORMATE)),INDEX_NO_LENGTH,INIT_VALUE);
	}
	
	/**
	 * 生成子订单编码
	 * @return long
	 */
	public static Long initSubOrderCode(){
		return createCode(BILL_TYPE.SUB_ORDER.code,Long.valueOf(DateUtil.formatDate(new Date(), DATE_FORMATE)),INDEX_NO_LENGTH,INIT_VALUE);
	}
	
	/**
	 * 生成退贷，拒收编号
	 * @return
	 */
	public static Long initRejectCode(){
		return createCode(BILL_TYPE.REJECT.code,Long.valueOf(DateUtil.formatDate(new Date(), DATE_FORMATE)),INDEX_NO_LENGTH,INIT_VALUE);
	}
	/**
	 * 生成退贷，拒收编号
	 * @return
	 */
	public static Long initOffsetCode(){
		return createCode(BILL_TYPE.OFFSET.code,Long.valueOf(DateUtil.formatDate(new Date(), DATE_FORMATE)),INDEX_NO_LENGTH,INIT_VALUE);
	}
	/**
	 * 生成取消单号
	 * @return
	 */
	public static Long initCancelCode(){
		return createCode(BILL_TYPE.CANCEL.code,Long.valueOf(DateUtil.formatDate(new Date(), DATE_FORMATE)),INDEX_NO_LENGTH,INIT_VALUE);
	}
	
	/**
	 * 生成退款单号
	 * @return
	 */
	public static Long initRefundCode(){
		return createCode(BILL_TYPE.REFUND.code,Long.valueOf(DateUtil.formatDate(new Date(), DATE_FORMATE)),INDEX_NO_LENGTH,INIT_VALUE);
	}
	
	/**
	 * 生成支付流水号
	 * @return
	 */
	public static String initPaymentSerial(){	
		return createCodeStr(BILL_TYPE.PAYMENT.code,Long.valueOf(DateUtil.formatDate(new Date(), DATE_FORMATE)),12,INIT_VALUE*10000);
	}
	
	public static String initAttributeValue(){
		return createCodeStr(BILL_TYPE.ATTRIBUTE_VALUE.code,0L,INDEX_NO_LENGTH,INIT_VALUE);
	}
	
	public static String initCodeValue(){
		return createCodeStr(BILL_TYPE.OTHER.code,0L,INDEX_NO_LENGTH,INIT_VALUE);
	}
	public static String initForbiddenWordsCode(){
		return createCodeStr(BILL_TYPE.FORBIDDEN_WORDS.code,0L,INDEX_NO_LENGTH,INIT_VALUE);
	}
	
	private static Long createCode(Integer billTypeCode,Long middle,Integer size,Integer step){
		return Long.parseLong(createCodeStr(billTypeCode,middle,size,step));
	}
	private static String createCodeStr(Integer billTypeCode,Long middle,Integer size,Integer step){
		StringBuffer initStr = new StringBuffer();
		initStr.append(billTypeCode).append(middle).append(RANDOM_IP);
		if(size>0){
			initStr.append(String.format("%1$0"+size+"d", Math.abs(INIT_MAP.get(billTypeCode).getAndIncrement()%step)));
		}
		return initStr.toString().trim();
	}
	
	/**
	 * 随机数IP定位
	 */
	private static void initRandomIp(){
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			NetworkInterface networkInterface;
			Enumeration<InetAddress> inetAddresses;
			InetAddress inetAddress;
			String sip;
			while (networkInterfaces.hasMoreElements()) {
				networkInterface = networkInterfaces.nextElement();
				inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					inetAddress = inetAddresses.nextElement();
					if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
						sip = inetAddress.getHostAddress();
						if(null!=sip){
							RANDOM_IP = (int)(Long.valueOf(sip.replaceAll("\\.", ""))%9);
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * 单据类型
	 * @author szy
	 *
	 */
	public enum BILL_TYPE{
		/**订单 -1*/
		ORDER(1),/**子订单 -2*/
		SUB_ORDER(2),/**退货，拒收-3*/
		REJECT(3),/**取消单 -4*/
		CANCEL(4),/**退款-5*/
		REFUND(5),/**补偿-6*/
		OFFSET(6),/**支付流水号-10*/
		PAYMENT(10),
		OTHER(1),
		ATTRIBUTE_VALUE(11),
		FORBIDDEN_WORDS(12);
		public Integer code;
		
		BILL_TYPE(Integer code){
			this.code = code; 
		}
	}
	
	public static void main(String[] args){
		for(int i=0;i<10;i++){
			System.out.println(initAttributeValue());
		}
	}
}