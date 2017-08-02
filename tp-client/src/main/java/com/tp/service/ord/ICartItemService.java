package com.tp.service.ord;

import java.util.List;

import com.tp.model.ord.CartItem;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 购物车商品信息表接口
  */
public interface ICartItemService extends IBaseService<CartItem>{

	/**
	 * 根据会员 ID查询购物车列表
	 * @param memberId
	 * @return
	 */
	public List<CartItem> queryListByMemberId(Long memberId,Long shopId);
	
	/**
	 * 获取购物车中商品总数
	 * @param memberId
	 * @return
	 */
	public Integer queryQuantityCountByMemberId(Long memberId,Long shopId);
	
	/**
	 * 修改选中状态
	 * @param cartItemList
	 * @param cartItem
	 * @return
	 */
	public Integer updateSelectedByList(List<CartItem> cartItemList,CartItem cartItem);
	
}
