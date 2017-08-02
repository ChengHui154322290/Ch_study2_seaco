package com.tp.proxy.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.cmt.NegativeCommentWords;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cmt.INegativeCommentWordsService;
/**
 * 评论差评词代理层
 * @author szy
 *
 */
@Service
public class NegativeCommentWordsProxy extends BaseProxy<NegativeCommentWords>{

	@Autowired
	private INegativeCommentWordsService negativeCommentWordsService;

	@Override
	public IBaseService<NegativeCommentWords> getService() {
		return negativeCommentWordsService;
	}
}
