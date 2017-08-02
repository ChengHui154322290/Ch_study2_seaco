package com.tp.service.ord;

import java.util.List;

import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.CartLineSimpleDTO;
import com.tp.model.ord.CartItem;

/**
 * Service
 * 
 * @author szy
 */
public interface ICartService {

	/**
	 * 根据会员ID查询购物车行列表
	 * 
	 * @param memberId
	 * @return
	 */
	List<CartLineSimpleDTO> selectCartLineDTOListByMemberId(Long memberId);
	/**
	 * 根据会员ID查询购物车行列表
	 * 
	 * @param memberId
	 * @return
	 */
	List<CartItem> queryListByMemberId(Long memberId);

	


	/**
	 * 保存购物车行列表
	 * 
	 * @param memberId
	 * @param lineList
	 */
	void insertCartLineSimpleDTOList(Long memberId, List<CartLineSimpleDTO> lineList);

	/**
	 * 更新redis selected状态
	 * 
	 * @param CartLineSimpleDTO
	 * @param selected
	 * @param memberId
	 */
	void updateCartLineSimpleDTOBySelected(CartLineSimpleDTO cartLineSimpleDTO, Boolean selected, Long memberId);

	/**
	 * 更新购物车行商品数量
	 * 
	 * @param CartLineSimpleDTO
	 * @param quantiy
	 * @param memberId
	 */
	void updateCartLineSimpleDTOByQuantity(CartLineSimpleDTO cartLineSimpleDTO, Integer quantity, Long memberId);

	/**
	 * 删除购物车行
	 * 
	 * @param CartLineSimpleDTO
	 * @param memberId
	 */
	void deleteCartLineSimpleDTO(CartLineSimpleDTO cartLineSimpleDTO, Long memberId);

	/**
	 * 下单成功后删除购物车中已结算商品
	 * 
	 * @param memberId
	 * @param settlementcartLineList
	 */
	void deleteCartLineSimpleDTOBySettlement(Long memberId, List<CartLineDTO> settlementcartLineList);

}
