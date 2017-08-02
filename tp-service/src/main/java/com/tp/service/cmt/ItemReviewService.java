package com.tp.service.cmt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.CmtConstant;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.constant.common.ExceptionConstant;
import com.tp.dao.cmt.ItemReviewDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.ForbiddenWords;
import com.tp.model.cmt.ItemReview;
import com.tp.service.BaseService;
import com.tp.service.bse.IForbiddenWordsService;
import com.tp.service.cmt.IItemReviewService;
import com.tp.util.StringUtil;

@Service
public class ItemReviewService extends BaseService<ItemReview> implements IItemReviewService {

	@Autowired
	private ItemReviewDao itemReviewDao;
	
	@Override
	public BaseDao<ItemReview> getDao() {
		return itemReviewDao;
	}

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IForbiddenWordsService forbiddenWordsService;

	@Override
	public PageInfo<ItemReview> findItemReviews_app(ItemReview query, int startPage, int pageSize){
		if (null == query
				|| (null == query.getId() && null == query.getOrderCode() && StringUtil.isNullOrEmpty(query.getPid())
							&& null == query.getSkuCode()))
			return new PageInfo<ItemReview>();
		if (null == query.getIsCheck())
			query.setIsCheck(CmtConstant.ItemReviewConstant.ISCHECK.CHECKED);
		if (null == query.getStatus())
			query.setStatus(CmtConstant.ItemReviewConstant.STATUS.ZERO);
		if (null == query.getIsDelete())
			query.setIsDelete(CmtConstant.ItemReviewConstant.ISDELETE.UNDELETE);
		
		query.setIsHide(CmtConstant.ItemReviewConstant.HIDE.SHOW);
		if (startPage > 0)
			query.setStartPage(startPage);
		if (pageSize > 0)
			query.setPageSize(pageSize);

		return queryPageListByItemReview(query, false);
	}


	public PageInfo<ItemReview> queryPageListByItemReview(ItemReview query, boolean isForBackend) {
		if(null == query) return new PageInfo<ItemReview>();
		Map<String, Object> params = getValuesByObject(query);
		if(isForBackend) {
			params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "id desc");
		} else {
			params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "is_top desc ,create_time desc");
		}		
		return queryPageByParam(params, new PageInfo<ItemReview>(query.getStartPage(), query.getPageSize()));
	}
	
	public PageInfo<ItemReview> queryPageListByItemReviewWithPrdListForBackend(ItemReview query){
		if(null == query) return new PageInfo<ItemReview>();
		Map<String, Object> params = getValuesByObject(query);
		if (!query.getPrds().isEmpty()) {
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "pid in (" + StringUtil.join(query.getPrds(), Constant.SPLIT_SIGN.COMMA) + ")");
		}
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "id desc");
		return queryPageByParam(params, new PageInfo<ItemReview>(query.getStartPage(), query.getPageSize())); 
	}
	

	@Override
	public ResultInfo<ItemReview> save(ItemReview itemReview) {
			if (null == itemReview.getPid())
				return new ResultInfo<ItemReview>(new FailInfo(ExceptionConstant.ErrorCode.ITEM_REVIEW_PID_IS_NULL.code));
			if (StringUtil.isNullOrEmpty(itemReview.getContent()))
				return new ResultInfo<ItemReview>(new FailInfo(ExceptionConstant.ErrorCode.ITEM_REVIEW_CONTENT_IS_NULL.code));
			if (StringUtil.isNullOrEmpty(itemReview.getOrderCode()))
				return new ResultInfo<ItemReview>(new FailInfo(ExceptionConstant.ErrorCode.ITEM_REVIEW_ORDER_CODE_IS_NULL.code));
			if (StringUtil.isNullOrEmpty(itemReview.getUid()))
				return new ResultInfo<ItemReview>(new FailInfo(ExceptionConstant.ErrorCode.ITEM_REVIEW_USER_ID_IS_NULL.code));
			if (null == itemReview.getIsAnonymous())
				itemReview.setIsAnonymous(CmtConstant.ItemReviewConstant.ISANONYMOUS.NOT_ANONYMOUS);
			
			Map<String, Object> params = new HashMap<>();
			params.put("skuCode", itemReview.getSkuCode());
			params.put("orderCode", itemReview.getOrderCode());
			params.put("pid", itemReview.getPid());
			params.put("uid", itemReview.getUid());
			params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "create_time desc");
			List<ItemReview> itemReviewObjs  = queryByParamNotEmpty(params);
			
			if(null!=itemReviewObjs&&!itemReviewObjs.isEmpty()) 
				return new ResultInfo<ItemReview>(new FailInfo(ExceptionConstant.ErrorCode.ITEM_REVIEW_HAS_REVIEW.code));
			
			ForbiddenWords forbiddenWordsObj = new ForbiddenWords();
			forbiddenWordsObj.setStatus(1);
			
			List<ForbiddenWords> forbiddenWords = forbiddenWordsService.queryByObject(forbiddenWordsObj);
			
			for (ForbiddenWords wordE : forbiddenWords) {
				if(itemReview.getContent().contains(wordE.getWords())) 
					return new ResultInfo<ItemReview>(
							new FailInfo("\""+wordE.getWords()+"\"&nbsp;&nbsp;"+ExceptionConstant.ErrorCode.THIS_WORDS_IS_FORBIDDEN_WORDS.value));
			}
			
			
			itemReview.setIsAnonymous(CmtConstant.ItemReviewConstant.ISANONYMOUS.NOT_ANONYMOUS);
			itemReview.setIsCheck(CmtConstant.ItemReviewConstant.ISCHECK.CHECKED);
			itemReview.setIsDelete(CmtConstant.ItemReviewConstant.ISDELETE.UNDELETE);
			itemReview.setStatus(CmtConstant.ItemReviewConstant.STATUS.ZERO);
			itemReview.setModifyTime(new Date());
			itemReview.setIsHide(CmtConstant.ItemReviewConstant.HIDE.SHOW);
			itemReview.setIsTop(CmtConstant.ItemReviewConstant.TOP.UNLIMITED);
			itemReview.setMark(5);
			
			
			if(null == itemReview.getCreateTime())
				itemReview.setCreateTime(new Date());				
			logger.debug("[begin]user Comment:"+itemReview.toString());
			itemReview = this.insert(itemReview);
			logger.debug("[end]user Comment:"+itemReview.getId());
			return new ResultInfo<ItemReview>(itemReview);
	}
	
	@Override
	public List<ItemReview> findTopItemReview(ItemReview query){
			Integer count = query.getCount();
			if (null == count) count = 1;
			Map<String, Object> params = getValuesByObject(query);
			if (null != query.getStatus()) {
				params.put("status", query.getStatus());
			}
			params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "create_time desc");
			params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), count);
			return queryByParamNotEmpty(params);
	}
	
	
	@Override
	public ItemReview findTopOneReview(String prdId){
		Map<String, Object> params = new HashMap<>();
		params.put("pid", prdId);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "create_time desc");
		return queryUniqueByParams(params);
	}

	@Override
	public List<ItemReview> selectByOrderCodes(List<String> orderCodes){
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "order_code in(" + StringUtil.join(orderCodes, Constant.SPLIT_SIGN.COMMA) + ")");
		return queryByParamNotEmpty(params);
	}
}
