package com.tp.proxy.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.cmt.ReviewImg;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cmt.IReviewImgService;
/**
 * 评论图片信息代理层
 * @author szy
 *
 */
@Service
public class ReviewImgProxy extends BaseProxy<ReviewImg>{

	@Autowired
	private IReviewImgService reviewImgService;

	@Override
	public IBaseService<ReviewImg> getService() {
		return reviewImgService;
	}
}
