package com.tp.service.cmt;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.cmt.ItemReview;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 商品评论信息表接口
  */
public interface IItemReviewService extends IBaseService<ItemReview>{
	
	/**
	 * 
	 * <pre>
	 *  app段,查询商品评论列表
	 * </pre>
	 *
	 * @param query
	 * @return
	 * @throws UserServiceException
	 */
	PageInfo<ItemReview> findItemReviews_app(ItemReview query, int startPage, int pageSize);


	/**
	 * 
	 * <pre>
	 * 发布评论方法
	 * </pre>
	 *
	 * @param itemReivew 必填项 (orderCode,uid,pid,content)
	 * @return
	 * @throws UserServiceException
	 */
	ResultInfo<ItemReview> save(ItemReview itemReivew);


	/**
	 * 
	 * <pre>
	 *  根据 商品id 得到该商品 最新的一条评论
	 * </pre>
	 *
	 * @param prdId
	 * @return
	 */
	ItemReview findTopOneReview(String prdId);


	/**
	 * 
	 * <pre>
	 *  根据条件查询,可以设置query中的count来查询 前多少条记录
	 * </pre>
	 *
	 * @param query
	 * @return
	 * @throws UserServiceException
	 */
	List<ItemReview> findTopItemReview(ItemReview query);
	
	PageInfo<ItemReview> queryPageListByItemReview(ItemReview query, boolean isForBackend);
	
	PageInfo<ItemReview> queryPageListByItemReviewWithPrdListForBackend(ItemReview query);

	List<ItemReview> selectByOrderCodes(List<String> orderCodes);
}
