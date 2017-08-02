package com.tp.dao.prd;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.dto.prd.DetailMainInfoDto;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.SkuDto;
import com.tp.model.prd.ItemDetail;
import com.tp.query.prd.DetailQuery;

public interface ItemDetailDao extends BaseDao<ItemDetail> {

	Integer updatePrdInfoByItemId(ItemDetail itemDetail);

	List<SkuDto> selectSkuDetailInfo(List<Long> skuIds);

	List<ItemDetail> selectItemDetailsByBarcodeAndName(ItemDetail itemDetail);

	List<ItemDetail> selectItemDetailsByPrdList(List<String> prdList);

	List<ItemResultDto> queryPageByQuery(DetailQuery query);

	Integer queryCountByQuery(DetailQuery query);

	/**
	 * @param itemId
	 * @return
	 */
	List<DetailMainInfoDto> selectListDetailIdsByItemId(Long itemId);

}
