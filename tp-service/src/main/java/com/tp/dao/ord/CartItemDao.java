package com.tp.dao.ord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.CartItem;

public interface CartItemDao extends BaseDao<CartItem> {

	public Integer queryQuantityCountByMemberId(@Param("memberId")Long memberId,@Param("shopId")Long shopId);
	
	public Integer updateSelectedByList(@Param("cartItemList")List<CartItem> cartItemList,@Param("selected")Integer selected,@Param("memberId")Long memberId,@Param("skuCode")String skuCode,@Param("topicId")Long topicId,@Param("shopId") Long shopId);
}
