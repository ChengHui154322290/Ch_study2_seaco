package com.tp.proxy.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.cmt.ReviewImportList;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cmt.IReviewImportListService;
/**
 * 商品评论导入明细表代理层
 * @author szy
 *
 */
@Service
public class ReviewImportListProxy extends BaseProxy<ReviewImportList>{

	@Autowired
	private IReviewImportListService reviewImportListService;

	@Override
	public IBaseService<ReviewImportList> getService() {
		return reviewImportListService;
	}
}
