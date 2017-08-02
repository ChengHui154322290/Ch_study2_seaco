package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class StatisticPay extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557815L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**payType 支付方式 
“web”；通过网页页面支付；“plugin”; 应用内嵌插件支付 
“ios-appstore”:通过苹果appstore购买
 “ios-ylb”:ios虚拟币
“ylb”:通过娱乐币购买
 数据类型varchar(45)*/
	private String type;
	
	/**payPartner 支付的渠道
“alipay”:阿里支付宝；
“tenpay”:腾讯财付通支付；
“ios-appstore”:通过苹果appstore购买
 “ios-ylb”:ios虚拟币
“ylb”:通过娱乐币购买
 数据类型varchar(45)*/
	private String partner;
	
	/**payResult“0”:表示支付成功“-1”:用户中止了支付（生成了支付订单，但没启动支付）；其他值表示支付出现问题；投递各支付方式或者渠道预定义的支付错误值，投递格式:”payPartner-错误值” 如，支付宝支付错误40000,”系统异常” 数据类型varchar(100)*/
	private String result;
	
	/**payItem 支付的业务类型 “西客商城”“海”等
 数据类型varchar(45)*/
	private String item;
	
	/**payDetail 支付业务详细描述	购买业务的详细描述： “鞋子”“奶瓶”等
 数据类型varchar(255)*/
	private String detail;
	
	/**payMoney 支付的金额 支付项目对应的金额数目 数据类型double*/
	private Double amount;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**公共参数表 id 数据类型bigint(20)*/
	private Long staBaseId;
	
	
	public Long getId(){
		return id;
	}
	public String getType(){
		return type;
	}
	public String getPartner(){
		return partner;
	}
	public String getResult(){
		return result;
	}
	public String getItem(){
		return item;
	}
	public String getDetail(){
		return detail;
	}
	public Double getAmount(){
		return amount;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getStaBaseId(){
		return staBaseId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setPartner(String partner){
		this.partner=partner;
	}
	public void setResult(String result){
		this.result=result;
	}
	public void setItem(String item){
		this.item=item;
	}
	public void setDetail(String detail){
		this.detail=detail;
	}
	public void setAmount(Double amount){
		this.amount=amount;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setStaBaseId(Long staBaseId){
		this.staBaseId=staBaseId;
	}
}
