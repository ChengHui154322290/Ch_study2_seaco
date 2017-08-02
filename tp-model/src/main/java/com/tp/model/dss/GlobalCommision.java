package com.tp.model.dss;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.multiply;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DssConstant;
import com.tp.model.BaseDO;
import com.tp.util.BigDecimalUtil;
/**
  * @author szy
  * 全局佣金设置表
  */
public class GlobalCommision extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1456900594219L;

	/**设置编码 数据类型int(3)*/
	@Id
	private Integer id;
	
	/**一级提佣比率 数据类型float(2,2)*/
	private Float firstCommisionRate;
	
	/**二级提佣比率 数据类型float(2,2)*/
	private Float secondCommisionRate;
	
	/**三级提佣比率 数据类型float(4,2)*/
	private Float threeCommisionRate;

	/**扫码提佣比率 数据类型float(4,2)*/
	private Float scanCommisionRate;

	/**创建者 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	public Double getCurrentCommisionRate(PromoterInfo promoterInfo,Double commisionRate){
		Float total = getTotalCommisionRate();
		if(total.isNaN() || total.equals(0f) || promoterInfo==null 
		 || !(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code.equals(promoterInfo.getPromoterType())
		 || DssConstant.PROMOTER_TYPE.COMPANY.code.equals(promoterInfo.getPromoterType()))
				){
			return Constant.ZERO;
		}
		if(promoterInfo.getParentPromoterId()==null || promoterInfo.getParentPromoterId()==0){
			return commisionRate;
		}
		if(threeCommisionRate==null){
			Float rate = divide(secondCommisionRate,total);
			return multiply(commisionRate, rate).doubleValue();
		}
		Float rate = divide(threeCommisionRate,total);
		return multiply(commisionRate, rate).doubleValue();
	}
	public Double getParentCommisionRate(PromoterInfo promoterInfo,Double commisionRate){
		Float total = getTotalCommisionRate();
		if(total.isNaN() || total.equals(0f) || promoterInfo==null 
		|| !(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code.equals(promoterInfo.getPromoterType())
		|| DssConstant.PROMOTER_TYPE.COMPANY.code.equals(promoterInfo.getPromoterType()))
		){
			return Constant.ZERO;
		}
		if(promoterInfo.getParentPromoterId()==null || promoterInfo.getParentPromoterId()==0){
			return Constant.ZERO;
		}
		if(threeCommisionRate==null){
			Float rate = divide(firstCommisionRate,total);
			return multiply(commisionRate, rate).doubleValue();
		}
		if(promoterInfo.getTopPromoterId()==null || promoterInfo.getTopPromoterId()==0){
			Float rate = divide(add(secondCommisionRate,firstCommisionRate).floatValue(),total);
			return multiply(commisionRate, rate).doubleValue();
		}
		Float rate = divide(secondCommisionRate,total);
		return multiply(commisionRate, rate).doubleValue();
	}

	public Double getTopCommisionRate(PromoterInfo promoterInfo,Double commisionRate){
		Float total = getTotalCommisionRate();
		if(threeCommisionRate==null || total.isNaN() || total.equals(0f) || promoterInfo==null 
		|| !(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code.equals(promoterInfo.getPromoterType())
		|| DssConstant.PROMOTER_TYPE.COMPANY.code.equals(promoterInfo.getPromoterType()))
		){
			return Constant.ZERO;
		}
		if(promoterInfo.getParentPromoterId()==null || promoterInfo.getParentPromoterId()==0 || promoterInfo.getTopPromoterId()==null || promoterInfo.getParentPromoterId()==0){
			return Constant.ZERO;
		}
		Float rate = divide(firstCommisionRate,total);
		return multiply(commisionRate, rate).doubleValue();
	}
	public Float getCurrentCommisionRate(PromoterInfo promoterInfo){
		Float total = getTotalCommisionRate();
		if(total.isNaN() || total.equals(0f) || promoterInfo==null 
		|| !(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code.equals(promoterInfo.getPromoterType())
		|| DssConstant.PROMOTER_TYPE.COMPANY.code.equals(promoterInfo.getPromoterType()))
		){
			return Constant.ZERO.floatValue();
		}
		if(promoterInfo.getParentPromoterId()==null || promoterInfo.getParentPromoterId()==0){
			return 1.0f;
		}
		if(threeCommisionRate==null){
			return divide(secondCommisionRate,total);
		}
		return divide(threeCommisionRate,total);
	}
	
	public Float getTotalCommisionRate(){
		return add(add(firstCommisionRate, secondCommisionRate),threeCommisionRate).floatValue();
	}

	private Float divide(Float a,Float b){
		return BigDecimalUtil.divide(a,b,6).floatValue();
	}
	public Integer getId(){
		return id;
	}
	public Float getFirstCommisionRate(){
		return firstCommisionRate;
	}
	public Float getSecondCommisionRate(){
		return secondCommisionRate;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setFirstCommisionRate(Float firstCommisionRate){
		this.firstCommisionRate=firstCommisionRate;
	}
	public void setSecondCommisionRate(Float secondCommisionRate){
		this.secondCommisionRate=secondCommisionRate;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public Float getThreeCommisionRate() {
		return threeCommisionRate;
	}
	public void setThreeCommisionRate(Float threeCommisionRate) {
		this.threeCommisionRate = threeCommisionRate;
	}
	public Float getScanCommisionRate() {
		return scanCommisionRate;
	}
	public void setScanCommisionRate(Float scanCommisionRate) {
		this.scanCommisionRate = scanCommisionRate;
	}	
}
