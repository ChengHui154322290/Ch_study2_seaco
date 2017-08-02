package com.tp.proxy.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.cmt.ReviewTagsStatis;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cmt.IReviewTagsStatisService;
/**
 * 评论标签统计信息表代理层
 * @author szy
 *
 */
@Service
public class ReviewTagsStatisProxy extends BaseProxy<ReviewTagsStatis>{

	@Autowired
	private IReviewTagsStatisService reviewTagsStatisService;

	@Override
	public IBaseService<ReviewTagsStatis> getService() {
		return reviewTagsStatisService;
	}
}
