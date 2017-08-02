package com.tp.dao.prd;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.dto.prd.ItemDetailSalesCountDto;
import com.tp.model.prd.ItemDetailSalesCount;

public interface ItemDetailSalesCountDao extends BaseDao<ItemDetailSalesCount> {

	/**
	 * @param detailIds
	 * @return
	 */
	Integer getDetailSalesCountByDetailIds(List<Long> detailIds);

	/**
	 * @param detailId
	 * @return
	 */
	int checkDetailIdExist(Long detailId);

	/**
	 * @param updateRealCount
	 */
	void updateRealSalesCount(ItemDetailSalesCount updateRealCount);

	/**
	 * @param updateDO
	 */
	void updateDefaultCountByDetailId(ItemDetailSalesCount updateDO);

	/**
	 * @param dto
	 * @return
	 */
	int updateDetailSalesCountDefaultCount(ItemDetailSalesCountDto dto);

}
