package com.tp.service.ord;

import java.util.List;
import java.util.Map;

import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.SubOrder;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 个人物品申报数据表接口
  */
public interface IPersonalgoodsDeclareInfoService extends IBaseService<PersonalgoodsDeclareInfo>{
	/**
     *	查询未申报运单
     *  @param map
     *  @return 
     */
	List<PersonalgoodsDeclareInfo> queryUndeclaredPersonalGoods(Map<String, Object> map);
	
	/**
	 * 根据订单号查询申报单
	 * @param orderCode
	 * @return 
	 */
	PersonalgoodsDeclareInfo queryUniquePersonalGoodsDeclByOrderCode(String orderCode);
	
	/**
	 * 生成预录入号码 
	 */
	ResultInfo<PersonalgoodsDeclareInfo> createPersonalgoodsDeclareInfo(SubOrder subOrder,
			ExpressCompany expressCompany, Long channelId, String waybillNo);
	
	/**
	 * 更新 
	 */
	ResultInfo<Boolean> updateDirectmailPersonalgoodsDeclareInfos(List<PersonalgoodsDeclareInfo> infos);
	
	PersonalgoodsDeclareInfo queryPersonalgoodsDeclareInfoByPreEntryNo(String preEntryNo);
}
