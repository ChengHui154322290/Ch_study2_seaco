package com.tp.m.ao.GroupCoupon;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.FastConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.common.vo.PaymentConstant.REDEEM_CODE_STATUS;
import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.base.OptionVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.order.QueryRedeemItem;
import com.tp.m.util.DateUtil;
import com.tp.model.mmp.TopicItem;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.proxy.dss.FastUserInfoProxy;
import com.tp.proxy.mmp.TopicItemProxy;
import com.tp.proxy.ord.OrderRedeemItemProxy;
import com.tp.query.ord.RedeemItemQuery;
import com.tp.result.ord.OrderRedeemItemStatistics;
import com.tp.util.Base64Util;

@Service
public class OrderRedeemItemAO {

	@Autowired
	private OrderRedeemItemProxy orderRedeemItemProxy;
	@Autowired
	private FastUserInfoProxy fastUserInfoProxy;
	@Autowired
	private TopicItemProxy topicItemProxy;
	
	public MResultVO<PageInfo<OrderRedeemItem>> queryListByParam(QueryRedeemItem redeemItemQuery){
		Long warehouseId = fastUserInfoProxy.queryFastUserWarehouseIdbyMobile(redeemItemQuery.getMobile(),FastConstant.SHOP_TYPE.GROUP_COUPON.code);
		if(null==warehouseId){
			return new MResultVO<>("您没有权限进行查询兑换券");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("skuCode", redeemItemQuery.getSkuCode());
		params.put("redeemCodeState", redeemItemQuery.getRedeemCodeState());
		List<WHERE_ENTRY> whereList = new ArrayList<WHERE_ENTRY>();
		if(redeemItemQuery.getBeginDate()!=null){
			whereList.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.GT, redeemItemQuery.getBeginDate()));
		}
		if(redeemItemQuery.getEndDate()!=null){
			whereList.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.LT, DateUtil.addDay2Date(1, redeemItemQuery.getEndDate())));
		}
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(),whereList);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " redeem_code_state asc,update_time desc");
		if(StringUtils.isNotBlank(redeemItemQuery.getRedeemCode())){
			params.clear();
			try {
				String redeemCode=Base64Util.encrypt(redeemItemQuery.getRedeemCode().getBytes("UTF-8")).replaceAll("\r|\n", "");
				params.put("redeemCode", redeemCode);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();  
			}
		}
		params.put("warehouseId", warehouseId);
		try{
			ResultInfo<PageInfo<OrderRedeemItem>> result = orderRedeemItemProxy.queryPageByParamNotEmpty(params, new PageInfo<OrderRedeemItem>(Integer.valueOf(redeemItemQuery.getCurpage()),10));
			if(result.success){
				if(CollectionUtils.isNotEmpty(result.data.getRows())){
					result.data.getRows().forEach(new Consumer<OrderRedeemItem>(){
						public void accept(OrderRedeemItem t) {
							if(PaymentConstant.REDEEM_CODE_STATUS.USED.code.equals(t.getRedeemCodeState())){
								try {
									byte[] redeemCodeBase64=Base64Util.decrypt(t.getRedeemCode());//将base64转化为明码
									String 	redeemCode = new String(redeemCodeBase64, "UTF-8");
									t.setRedeemCode(redeemCode);
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();  
								}
							}else{
								t.setRedeemCode("******");
							}
						}
					});
				}
				return new MResultVO<>(MResultInfo.SUCCESS,result.getData());
			}
			return new MResultVO<>(result.getMsg().getMessage());
		}catch(Throwable exception){
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	public MResultVO<OrderRedeemItemStatistics> queryStatisticsByQuery(QueryRedeemItem redeemItem){
		RedeemItemQuery redeemItemQuery = new RedeemItemQuery();
		BeanUtils.copyProperties(redeemItem, redeemItemQuery);
		Long warehouseId = fastUserInfoProxy.queryFastUserWarehouseIdbyMobile(redeemItemQuery.getMobile(),FastConstant.SHOP_TYPE.GROUP_COUPON.code);
		if(null==warehouseId){
			return new MResultVO<>("您没有权限查询兑换券");
		}
		redeemItemQuery.setWarehouseId(warehouseId);
		if(StringUtils.isNotBlank(redeemItemQuery.getRedeemCode())){
			try {
				String redeemCode=Base64Util.encrypt(redeemItemQuery.getRedeemCode().getBytes("UTF-8")).replaceAll("\r|\n", "");
				redeemItemQuery.setRedeemCode(redeemCode);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();  
			}
		}
		if(redeemItemQuery.getEndDate()!=null){
			redeemItemQuery.setEndDate(DateUtil.addDay2Date(1, redeemItemQuery.getEndDate()));
		}
		try{
			ResultInfo<OrderRedeemItemStatistics> result = orderRedeemItemProxy.queryStatisticsByQuery(redeemItemQuery);
			if(result.success){
				return new MResultVO<>(MResultInfo.SUCCESS,result.getData());
			}
			return new MResultVO<>(result.getMsg().getMessage());
		}catch(Throwable exception){
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}

	public MResultVO<List<OptionVO>> queryBySkuListByShop(String tel) {
		Long warehouseId = fastUserInfoProxy.queryFastUserWarehouseIdbyMobile(tel,FastConstant.SHOP_TYPE.GROUP_COUPON.code);
		if(null==warehouseId){
			return new MResultVO<>("您没有权限进行查询");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("deletion", Constant.DISABLED.NO);
		params.put("stockLocationId", warehouseId);
		params.put("whType",StorageConstant.StorageType.BUY_COUPONS.value);
		try{
			ResultInfo<List<TopicItem>> result = topicItemProxy.queryByParam(params);
			if(result.success){
				return new MResultVO<>(MResultInfo.SUCCESS,convartSkuList(result.getData()));
			}
			return new MResultVO<>(result.getMsg().getMessage());
		}catch(Throwable exception){
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	public MResultVO<List<OptionVO>> convartRedeemCodeStateList(){
		List<OptionVO> list = new ArrayList<OptionVO>();
		for(REDEEM_CODE_STATUS entry:PaymentConstant.REDEEM_CODE_STATUS.values()){
			OptionVO option = new OptionVO();
			option.setId(entry.code.toString());
			option.setName(entry.cnName);
			list.add(option);
		}
		return new MResultVO<>(MResultInfo.SUCCESS,list);
	}
	
	private List<OptionVO> convartSkuList(List<TopicItem> topicItemList){
		final Map<String,String> map= new HashMap<String,String>();
		final List<OptionVO> list = new ArrayList<OptionVO>();
		if(CollectionUtils.isNotEmpty(topicItemList)){
			topicItemList.forEach(new Consumer<TopicItem>(){
				public void accept(TopicItem t) {
					map.put(t.getSku(), t.getName());
				}
			});
			map.forEach(new BiConsumer<String, String>() {
				@Override
				public void accept(String t, String u) {
					OptionVO option = new OptionVO();
					option.setId(t);
					option.setName(u);
					list.add(option);
				}
			});
		}
		return list;
	}
	
}
