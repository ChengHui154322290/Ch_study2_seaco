package com.tp.proxy.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.cmt.ReviewImportLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cmt.IReviewImportLogService;
/**
 * 商品评论导入日志表代理层
 * @author szy
 *
 */
@Service
public class ReviewImportLogProxy extends BaseProxy<ReviewImportLog>{

	@Autowired
	private IReviewImportLogService reviewImportLogService;

	@Override
	public IBaseService<ReviewImportLog> getService() {
		return reviewImportLogService;
	}
}
