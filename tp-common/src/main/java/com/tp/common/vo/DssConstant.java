package com.tp.common.vo;

import java.util.ArrayList;
import java.util.List;

import com.tp.common.vo.Constant.TF;

/**
 * 促销常量类
 * @author szy
 *
 */
public final class DssConstant {

	public enum VO_STATUS{
		
		UN_PASS(0,"未开通"),								/**0, 未开通*/		
		EN_PASS_UN_IDEN(1,"已开通未认证"),		/**1, 已开通未认证"*/
		IN_PASS_IDEN(2,"开通店铺已认证"),   		/**2 开通店铺已认证*/
		FORBIDDEN(3,"禁用");  							/**3 店铺被禁用*/
		
		public Integer code;
		public String cnName;
		VO_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(PROMOTER_STATUS entry:PROMOTER_STATUS.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	/**
	 * 促销人员状态
	 * @author szy
	 *
	 */
	public enum PROMOTER_STATUS{
		/**0,"未开通"*/
		UN_PASS(0,"未开通"),/**1,"已开通"*/
		EN_PASS(1,"已开通"),/**2,"禁用"*/
		IN_PASS(2,"禁用");
		public Integer code;
		public String cnName;
		PROMOTER_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(PROMOTER_STATUS entry:PROMOTER_STATUS.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	/**
	 * 证件类型
	 * @author szy
	 *
	 */
	public enum CARD_TYPE{
		/**0,"未开通"*/
		IDENTITY_CARD(1,"身份证");
		public Integer code;
		public String cnName;
		CARD_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(CARD_TYPE entry:CARD_TYPE.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	
	/**
	 * 促销人员类型
	 * @author szy
	 *
	 */
	public enum PROMOTER_TYPE{
		COUPON(0,"卡券推广"),
		DISTRIBUTE(1,"店铺分销"),
		SCANATTENTION(2,"扫码推广"),
		COMPANY(3,"公司店铺");
		public Integer code;
		public String cnName;
		PROMOTER_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(PROMOTER_TYPE entry:PROMOTER_TYPE.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	/**
	 * 后台配置(backend生成)推广人员类型
	 * @author szy
	 *
	 */
	public enum PROMOTER_BACKEND_TYPE{
		COUPON(0,"卡券推广"),
		SCANATTENTION(2,"扫码推广");
		public Integer code;
		public String cnName;
		PROMOTER_BACKEND_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(PROMOTER_BACKEND_TYPE entry:PROMOTER_BACKEND_TYPE.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}

	/**
	 * 提现状态
	 * @author szy
	 *
	 */
	public enum WITHDRAW_STATUS{
		APPLY(1,"申请"),
		AUDITING(2,"审核中"),
		PASS(3,"审核通过"),
		UNPASS(4,"审核未通过"),
		PAYED(5,"财务打款成功"),
		UNPAY(6,"财务打款失败");
		public Integer code;
		public String cnName;
		WITHDRAW_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(WITHDRAW_STATUS entry:WITHDRAW_STATUS.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		public static List<Integer> canApplyStatus(){
			List<Integer> list = new ArrayList<Integer>();
			list.add(UNPASS.code);
			list.add(PAYED.code);
			list.add(UNPAY.code);
			return list;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	/**
	 * 提现状态
	 * @author szy
	 *
	 */
	public enum WITHDRAW_LOG_TYPE{
		APPLY(1,"申请"),
		AUDIT(2,"审核"),
		PAYMENT(3,"打款");
		public Integer code;
		public String cnName;
		WITHDRAW_LOG_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(WITHDRAW_LOG_TYPE entry:WITHDRAW_LOG_TYPE.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		
		public static WITHDRAW_LOG_TYPE getType(Integer status){
			if(WITHDRAW_STATUS.APPLY.code.equals(status) || WITHDRAW_STATUS.AUDITING.code.equals(status)){
				return APPLY;
			}
			if(WITHDRAW_STATUS.PASS.code.equals(status) || WITHDRAW_STATUS.UNPASS.code.equals(status)){
				return AUDIT;
			}else{
				return PAYMENT;
			}
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	/**
	 * 账务类型
	 * @author szy
	 *
	 */
	public enum ACCOUNT_TYPE{
		INCOMING(1,"进账"),
		OUTCOMING(2,"出帐");
		public Integer code;
		public String cnName;
		ACCOUNT_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(ACCOUNT_TYPE entry:ACCOUNT_TYPE.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}

	/**
	 * 帐目类型
	 * @author szy
	 *
	 */
	public enum BUSSINESS_TYPE{
		ORDER(1,"订单入账"),
		REFERRAL_FEES(2,"新人提成"),
		REFUND(3,"退款出账"),
		WITHDRAW(4,"提现出账");
		public Integer code;
		public String cnName;
		BUSSINESS_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(BUSSINESS_TYPE entry:BUSSINESS_TYPE.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	
	/**
	 * 佣金汇总状态
	 * @author szy
	 *
	 */
	public enum COLLECT_STATUS{
		NO(TF.NO,"未汇总"),
		YES(TF.YES,"已汇总");
		public Integer code;
		public String cnName;
		COLLECT_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(COLLECT_STATUS entry:COLLECT_STATUS.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	
	/**
	 * 佣金汇总状态
	 * @author szy
	 *
	 */
	public enum INDIRECT_TYPE{
		NO(TF.NO,"直接提佣"),
		YES(TF.YES,"下级提佣"),
		GRANDSON(2,"下下级提佣");
		public Integer code;
		public String cnName;
		INDIRECT_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(INDIRECT_TYPE entry:INDIRECT_TYPE.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	public enum GETINFO_PRIORITY {
		// token
		TOKEN(0),
		// shop
		SHOP(1);
		
		public int code;
		
		private GETINFO_PRIORITY(int code) {
			this.code = code;
		}
		
		public static GETINFO_PRIORITY getByCode(int code){
			for(GETINFO_PRIORITY c : GETINFO_PRIORITY.values()){
				if(code == c.code) return c;
			}
			return null;
		}
	}
	
	public enum GETINFO_SOURCE {
		// token
		TOKEN(0),
		// shop
		SHOP(1),		
		// related shop
		RELATEDSHOP(2);
		
		
		public int code;
		
		private GETINFO_SOURCE(int code) {
			this.code = code;
		}
		
		public static GETINFO_SOURCE getByCode(int code){
			for(GETINFO_SOURCE c : GETINFO_SOURCE.values()){
				if(code == c.code) return c;
			}
			return null;
		}
	}

	public enum PROMOTERTOPIC_STATUS {
		// 下架
		OFFSHELF(0),
		// 上架
		ONSHELF(1);
		
		public int code;
		
		private PROMOTERTOPIC_STATUS(int code) {
			this.code = code;
		}
		
		public static PROMOTERTOPIC_STATUS getByCode(int code){
			for(PROMOTERTOPIC_STATUS c : PROMOTERTOPIC_STATUS.values()){
				if(code == c.code) return c;
			}
			return null;
		}
	}
	
	public enum PROMOTERTOPIC_TYPE {
		// 专题
		TOPIC(0),
		// 专题商品
		TOPICITEM(1);
		
		public int code;
		
		private PROMOTERTOPIC_TYPE(int code) {
			this.code = code;
		}
		
		public static PROMOTERTOPIC_TYPE getByCode(int code){
			for(PROMOTERTOPIC_TYPE c : PROMOTERTOPIC_TYPE.values()){
				if(code == c.code) return c;
			}
			return null;
		}
	}	

	
	public enum PROMOTERTOPIC_CALLMODE {
		// 调用getopics
		CALLMODE_TOPIC(0),
		// 调用getopicItems
		CALLMODE_TOPICITEM(1);
		
		public int code;
		
		private PROMOTERTOPIC_CALLMODE(int code) {
			this.code = code;
		}
		
		public static PROMOTERTOPIC_CALLMODE getByCode(int code){
			for(PROMOTERTOPIC_CALLMODE c : PROMOTERTOPIC_CALLMODE.values()){
				if(code == c.code) return c;
			}
			return null;
		}
	}	
	
	
		/**
		 * 支付类型
		 * @author szy
		 *
		 */
	public enum PAYMENT_MODE{
		/**1-银行（借记卡）*/
		UNPAY(1,"银行（借记卡）"),
		/**2-支付宝）*/
		ALIPAY(2,"支付宝"),
		POINT(3,"积分");
	
		public Integer code;
		public String cnName;
		PAYMENT_MODE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(PAYMENT_MODE entry:PAYMENT_MODE.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}	
	
	/**
	 * 分销员层级
	 * @author szy
	 *
	 */
	public enum PROMOTER_LEVEL{
		SMALLEST(1,"小"),
		NORMAL(5,"中"),
		LARGEST(10,"大");
		public Integer code;
		public String cnName;
		PROMOTER_LEVEL(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(Integer code){
			for(PROMOTER_LEVEL entry:PROMOTER_LEVEL.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return "";
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	/**
	 * 邀请码使用状态
	 * @author szy
	 *
	 */
	public enum PROMOTER_SCAN_USE{
		/**0,"未使用"*/
		UNUSE("0","未使用"),/**1,"已使用"*/
		USED("1","已使用");
		public String code;
		public String cnName;
		PROMOTER_SCAN_USE(String code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		public static String getCnName(String code){
			for(PROMOTER_SCAN_USE entry:PROMOTER_SCAN_USE.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return "";
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getCnName() {
			return cnName;
		}
		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
}

