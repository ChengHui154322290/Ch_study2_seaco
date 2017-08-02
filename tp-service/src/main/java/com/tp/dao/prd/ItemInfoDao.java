package com.tp.dao.prd;

import com.tp.common.dao.BaseDao;
import com.tp.model.prd.ItemInfo;

import java.util.List;

public interface ItemInfoDao extends BaseDao<ItemInfo> {

	String selectDetailIdDesc(Long detailId);

	String selectDetailIdMobileDesc(Long detailId);

	List<ItemInfo> queryByItemIds(List<Long> list);
}
