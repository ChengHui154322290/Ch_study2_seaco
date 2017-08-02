package com.tp.service.dss;

import java.util.List;

import com.tp.model.dss.PromoterInfo;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 分销员信息接口
  */
public interface IPromoterInfoService extends IBaseService<PromoterInfo>{

	/**
	 * 根据类型/手机号/证件号查询是否有重复
	 * @param promoterInfo
	 * @return
	 */
	public Integer valiatePromoterInfo(PromoterInfo promoterInfo);
	/**
	 * 获取所有的所属人员
	 * @param promoterInfo
	 * @return
	 */
	public List<PromoterInfo> getAllChildPromoterInfo(PromoterInfo promoterInfo);
	
	Double getCurrentCommision(Long promoterid, String sku, Double price);

	Double getCurrentCommision2(PromoterInfo promoterinfo, Double price, Double commisionRate);

	public PromoterInfo insertScan(PromoterInfo obj) ;
	
	public String initInviteCode();
	
	public String queryShortNameByChannelCode(String channelCode);
}
