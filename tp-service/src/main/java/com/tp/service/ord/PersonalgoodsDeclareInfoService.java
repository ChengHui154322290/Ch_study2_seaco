package com.tp.service.ord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.wms.WmsConstant.ExpressCompany;
import com.tp.dao.ord.PersonalgoodsDeclareInfoDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.SubOrder;
import com.tp.service.BaseService;
import com.tp.service.ord.IPersonalgoodsDeclareInfoService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.JKF.IJKFDeclarePersonalGoodsLocalService;
import com.tp.util.StringUtil;

@Service
public class PersonalgoodsDeclareInfoService extends BaseService<PersonalgoodsDeclareInfo> implements IPersonalgoodsDeclareInfoService {

	@Autowired
	private PersonalgoodsDeclareInfoDao personalgoodsDeclareInfoDao;
	
	private static final Logger log = LoggerFactory.getLogger(PersonalgoodsDeclareInfoService.class);

	@Autowired
	private IJKFDeclarePersonalGoodsLocalService jKFDeclarePersonalGoodsLocalService;
	
	@Autowired
	private ISubOrderService subOrderService;
	
	@Override
	public BaseDao<PersonalgoodsDeclareInfo> getDao() {
		return personalgoodsDeclareInfoDao;
	}

    /**
     *	个人物品申报海关 
     */
    public List<PersonalgoodsDeclareInfo> queryUndeclaredPersonalGoods(Map<String, Object> map){
    	return personalgoodsDeclareInfoDao.queryUndeclaredPersonalGoods(map);
    }
    
	@Override
	public PersonalgoodsDeclareInfo queryUniquePersonalGoodsDeclByOrderCode(String orderCode) {
		if (StringUtils.isEmpty(orderCode)) {
			return null;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", orderCode);
		List<PersonalgoodsDeclareInfo> personalgoodsDeclareInfos = getDao().queryByParamNotEmpty(params);
		if (CollectionUtils.isNotEmpty(personalgoodsDeclareInfos)) {
			return personalgoodsDeclareInfos.get(0);
		}
		return null;
	}
	
	/** 生成个人物品申报单 */
	@Override
	public ResultInfo<PersonalgoodsDeclareInfo> createPersonalgoodsDeclareInfo(SubOrder subOrder,
			ExpressCompany expressCompany, Long channelId, String waybillNo){
		if (ClearanceChannelsEnum.HANGZHOU.id.equals(channelId)) {
			return new ResultInfo<>(
					jKFDeclarePersonalGoodsLocalService.createPersonalgoodsDeclareInfo(subOrder, expressCompany, waybillNo));
		}else{
			log.error("订单{}的保税区类型暂不支持：{}", subOrder.getOrderCode(), channelId);
			return new ResultInfo<>(new FailInfo("非杭州保税区订单"));
		}
	}

	@Transactional
	@Override
	public ResultInfo<Boolean> updateDirectmailPersonalgoodsDeclareInfos(List<PersonalgoodsDeclareInfo> updateInfos) {
		if(CollectionUtils.isEmpty(updateInfos)) return new ResultInfo<>(new FailInfo("清单列表为空"));
		List<Long> orderCodes = new ArrayList<>();
		updateInfos.forEach(new Consumer<PersonalgoodsDeclareInfo>() {
			public void accept(PersonalgoodsDeclareInfo t) {
				if (t.getOrderCode() != null) orderCodes.add(t.getOrderCode());
			}
		});
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "order_code in(" + StringUtil.join(orderCodes, SPLIT_SIGN.COMMA) + ")");
		List<SubOrder> subOrderList = subOrderService.queryByParam(params);
		List<PersonalgoodsDeclareInfo> originalInfos = queryByParam(params);
		
		FailInfo failInfo = validateUpdateDirectmailPersonalgoodsInfos(updateInfos, subOrderList, originalInfos);
		if(failInfo != null){
			return new ResultInfo<>(failInfo);
		}
		personalgoodsDeclareInfoDao.updateDirectmailPersonalgoodsDeclareInfos(updateInfos);
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	
	private FailInfo validateUpdateDirectmailPersonalgoodsInfos(List<PersonalgoodsDeclareInfo> updateInfos, 
			List<SubOrder> subOrders, List<PersonalgoodsDeclareInfo> originalInfos){
		
		for(PersonalgoodsDeclareInfo updateInfo : updateInfos){
			Long orderCode = updateInfo.getOrderCode();
			PersonalgoodsDeclareInfo originalInfo = null;
			for(PersonalgoodsDeclareInfo info : originalInfos){
				if(info.getOrderCode().equals(orderCode)){
					originalInfo = info;
				}
			}
			if(originalInfo == null) {
				return new FailInfo("订单"+orderCode+"对应清关单不存在,直邮订单请支付一小时后导入报关数据");
			}
			if (originalInfo.getImportType() == null || originalInfo.getImportType() != 0){
				return new FailInfo("订单"+orderCode+"不是直邮订单");
			}
			SubOrder subOrder = null;
			for(SubOrder sub : subOrders){
				if(sub.getOrderCode().equals(orderCode)){
					subOrder = sub;
				}
			}
			if(subOrder == null) return new FailInfo("订单"+orderCode+"对应子单信息不存在");
			if(!subOrder.getClearanceStatus().equals(ClearanceStatus.NEW.code)){
				return new FailInfo("订单"+orderCode+"已推送海关,不允许修改");
			}
		}
		return null;
	}

	@Override
	public PersonalgoodsDeclareInfo queryPersonalgoodsDeclareInfoByPreEntryNo(String preEntryNo) {
		if(StringUtil.isEmpty(preEntryNo)) return null;
		Map<String, Object> params = new HashMap<>();
		params.put("preEntryNo", preEntryNo);
		return queryUniqueByParams(params);
	}
}
