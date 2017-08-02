package com.tp.service.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.cmt.NegativeCommentWordsDao;
import com.tp.model.cmt.NegativeCommentWords;
import com.tp.service.BaseService;
import com.tp.service.cmt.INegativeCommentWordsService;

@Service
public class NegativeCommentWordsService extends BaseService<NegativeCommentWords> implements INegativeCommentWordsService {

	@Autowired
	private NegativeCommentWordsDao negativeCommentWordsDao;
	
	@Override
	public BaseDao<NegativeCommentWords> getDao() {
		return negativeCommentWordsDao;
	}

}
