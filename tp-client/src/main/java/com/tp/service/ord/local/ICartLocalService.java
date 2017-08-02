package com.tp.service.ord.local;

import java.util.List;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.CartLineSimpleDTO;
import com.tp.result.mmp.TopicItemInfoResult;


/**
 * 购物车服务
 * 
 * @author szy
 * @version 0.0.1
 */
public interface ICartLocalService {
	


	/**
	 * 查询会员购物车
	 * 
	 * @param memberId
	 * @param cartType
	 * @return CartDTO
	 */
	CartDTO findMemberCart(Long memberId, int cartType);
	
	/**
	 * 查询会员购物车商品总数量
	 * 
	 * @param memberId
	 * @return
	 */
	 int getCartQuantity(Long memberId);
	 
	/**
	 * 根据购物车类型查询会员购物车商品总数量
	 * 
	 * @param memberId
	 * @param cartType
	 * @return
	 */
	 int getCartQuantityByCartType(Long memberId,int cartType);
	
	/**
	 * 加入购物车
	 * 
	 * @param cartline
	 * @param memberId
	 * @param ip
	 * @param platformId
	 * @return
	 */
//	 ResultInfo<CartLineSimpleDTO> addToCart(CartLineDTO cartline, Long memberId, String ip, int platformType);
	
	/**
	 * 加载购物车
	 * 
	 * @param memberId
	 * @param cartType
	 * @return List<CartLineDTO>
	 */
	CartDTO loadCart(Long memberId,int cartType);
	
	/**
	 * 加载最新单品团
	 * 
	 * @return List<TopicItemInfoResult>
	 */
	List<TopicItemInfoResult> loadLastestSingleTopic();
	
	/**
	 * 更新购物车selected状态
	 * 
	 * @param skuCode
	 * @param memberId
	 * @return
	 */
	void selectCart(String skuCode, Long topicId, Boolean selected, Long memberId);
	
	/**
	 * 全部更新购物车selected状态
	 * 
	 * @param selectedAll
	 * @param cartType
	 * @param memberId
	 * @return
	 */
	void selectAllCart(Boolean selectedAll, int cartType, Long memberId);
	
	/**
	 * 更新购物车行商品数量
	 * 
	 * @param skuCode
	 * @param quantity
	 * @param memberId
	 * @return
	 */
	void updateCart(CartLineDTO cartLine, Long memberId, int platformType);
	
	/**
	 * 删除购物车行
	 * 
	 * @param skuCode
	 * @param topicId
	 * @param memberId
	 * @return
	 */
	void deleteCart(String skuCode, Long topicId, Long memberId);
	
	/**
	 * 删除全部购物车行
	 * 
	 * @param cartType
	 * @param memberId
	 * @return
	 */
	void deleteAllCart(int cartType, Long memberId);
	
	/**
	 * 结算check
	 * 
	 * @param memberId
	 * @param ip
	 * @param platform
	 * @param cartLineList
	 * @return
	 */
//	ResultInfo<Boolean> checkCart(Long memberId, String ip, Integer platform, List<CartLineDTO> cartLineList);
	
	/**
	 * 加载有效购物车
	 * 
	 * @param memberId
	 * @param cartType
	 * @return List<CartLineDTO>
	 */
//	CartDTO getValidateCart(Long memberId, int cartType);
	
	/**
	 * 购物车tab显示控制
	 * 
	 * @param memberId
	 * @return
	 */
	public String showCartTab(Long memberId);

//	
//
//	/**
//	 * 查询会员购物车
//	 * 
//	 * @param memberId
//	 * @param cartType
//	 * @return CartDTO
//	 */
//	CartDTO findMemberCart(Long memberId, int cartType);
//	
//	/**
//	 * 查询会员购物车商品总数量
//	 * 
//	 * @param memberId
//	 * @return
//	 */
//	 int getCartQuantity(Long memberId);
//	 
//	/**
//	 * 根据购物车类型查询会员购物车商品总数量
//	 * 
//	 * @param memberId
//	 * @param cartType
//	 * @return
//	 */
//	 int getCartQuantityByCartType(Long memberId,int cartType);
//	
//	/**
//	 * 加入购物车
//	 * 
//	 * @param cartline
//	 * @param memberId
//	 * @param ip
//	 * @param platformId
//	 * @return
//	 */
//	 ResultInfo<CartLineSimpleDTO> addToCart(CartLineDTO cartline, Long memberId, String ip, int platformType);
//	
//	/**
//	 * 加载购物车
//	 * 
//	 * @param memberId
//	 * @param cartType
//	 * @return List<CartLineDTO>
//	 */
//	CartDTO loadCart(Long memberId,int cartType);
//	
//	/**
//	 * 加载最新单品团
//	 * 
//	 * @return List<TopicItemInfoResult>
//	 */
//	List<TopicItemInfoResult> loadLastestSingleTopic();
//	
//	/**
//	 * 更新购物车selected状态
//	 * 
//	 * @param skuCode
//	 * @param memberId
//	 * @return
//	 */
//	void selectCart(String skuCode, Long topicId, Boolean selected, Long memberId);
//	
//	/**
//	 * 全部更新购物车selected状态
//	 * 
//	 * @param selectedAll
//	 * @param cartType
//	 * @param memberId
//	 * @return
//	 */
//	void selectAllCart(Boolean selectedAll, int cartType, Long memberId);
//	
//	/**
//	 * 更新购物车行商品数量
//	 * 
//	 * @param skuCode
//	 * @param quantity
//	 * @param memberId
//	 * @return
//	 */
//	void updateCart(CartLineDTO cartLine, Long memberId, int platformType);
//	
//	/**
//	 * 删除购物车行
//	 * 
//	 * @param skuCode
//	 * @param topicId
//	 * @param memberId
//	 * @return
//	 */
//	void deleteCart(String skuCode, Long topicId, Long memberId);
//	
//	/**
//	 * 删除全部购物车行
//	 * 
//	 * @param cartType
//	 * @param memberId
//	 * @return
//	 */
//	void deleteAllCart(int cartType, Long memberId);
//	
//	/**
//	 * 结算check
//	 * 
//	 * @param memberId
//	 * @param ip
//	 * @param platform
//	 * @param cartLineList
//	 * @return
//	 */
//	ResultInfo<Boolean> checkCart(Long memberId, String ip, Integer platform, List<CartLineDTO> cartLineList);
//	
	/**
	 * 加载有效购物车
	 * 
	 * @param memberId
	 * @param cartType
	 * @return List<CartLineDTO>
	 */
	CartDTO getValidateCart(Long memberId, int cartType);
//	
//	/**
//	 * 购物车tab显示控制
//	 * 
//	 * @param memberId
//	 * @return
//	 */
//	public String showCartTab(Long memberId);
}
