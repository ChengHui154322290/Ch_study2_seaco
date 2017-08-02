package com.tp.proxy.prd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.DetailDto;
import com.tp.dto.prd.ItemDto;
import com.tp.exception.ItemServiceException;
import com.tp.model.bse.Category;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemInfo;
import com.tp.proxy.bse.CategoryProxy;
import com.tp.service.prd.IItemManageService;

/**
 * 
 * <pre>
 * 商品PRD管理的AO层操作
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@Service
public class ItemPrdProxy {
	
	@Autowired
	private CategoryProxy categoryProxy; 
	@Autowired
	private ItemValidateProxy itemValidateProxy;
	@Autowired
	private IItemManageService itemManageService;
	private final static Logger LOGGER = LoggerFactory.getLogger(ItemPrdProxy.class);
	
	/**
	 * 获取prd列表
	 * @param detailId
	 * @return
	 */
	public List<DetailDto> queryPrd(Long detailId){
		LOGGER.debug("查询prd列表： detailId：{}",detailId);
		return null;
	}
	
	/**
	 * 
	 * <pre>
	 * 保存spu和部分prdid信息
	 * </pre>
	 *
	 * @param item
	 * @return
	 * @throws ItemServiceException
	 */
	public ResultInfo<Long> saveItemPrds(String info,String details,String userName){
		//校验逻辑
		JSONArray aryJson=JSONArray.fromObject(details);
		LOGGER.info("item_info: "+info);
		LOGGER.info("item_detail: "+details);
		ItemInfo infoDO = (ItemInfo) JSONObject.toBean(JSONObject.fromObject(info), ItemInfo.class);
		ItemDetail[] detailList = (ItemDetail[]) JSONArray.toArray(JSONArray.fromObject(aryJson),ItemDetail.class);
		
		ItemDto item = new ItemDto();
		Category category  = categoryProxy.queryById(infoDO.getSmallId()).getData();
		if(null!=category){
			infoDO.setSmallCode(category.getCode());//设置小类的编码关联spu的编码
		}
		item.setItemInfo(infoDO);
		item.setItemDetailList(Arrays.asList(detailList));
		ResultInfo<Boolean> resultInfo  = itemValidateProxy.validItemPrd(infoDO, detailList,2);
		if(!resultInfo.success){
			return new ResultInfo<Long>(resultInfo.msg);
		}
		try{
			Long itemId = itemManageService.saveItemPrds(initItem(item,userName),userName);
			return new ResultInfo<Long>(itemId);
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
			return new ResultInfo<>(new FailInfo("保存出错"));
		}
	}
	
	/**
	 * 
	 * <pre>
	 *  初始化商品 
	 * </pre>
	 *
	 * @param item
	 * @return
	 */
	private ItemDto initItem(ItemDto item,String userName) {
		ItemInfo info = null;
		info = item.getItemInfo();
		info.setCreateTime(new Date());
		info.setUpdateTime(new Date());
		// 登陆用户中取值
		info.setCreateUser(userName);
		info.setUpdateUser(userName);
		item.setItemInfo(info);
		List<ItemDetail> detailList = item.getItemDetailList();
		List<ItemDetail> detailListTmp = new ArrayList<ItemDetail>();
		if (CollectionUtils.isNotEmpty(detailList)) {
			for (ItemDetail detail : detailList) {
				detail.setCreateTime(new Date());
				detail.setUpdateTime(new Date());
				detail.setCreateUser(userName);
				detail.setUpdateUser(userName);
				detailListTmp.add(detail);
			}
		}
		
		if(null!=detailList){
			item.setItemDetailList(detailListTmp);
		}
		return item;
	}	
}
