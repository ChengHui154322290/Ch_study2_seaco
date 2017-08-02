package com.tp.world.ao.comment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.comment.QueryComment;

/**
 * 评论业务层
 * @author zhuss
 * @2016年1月4日 下午6:15:58
 */
@Service
public class CommentAO {
	private static final Logger log = LoggerFactory.getLogger(CommentAO.class);

	/**
	 * 提交评论
	 * @param comment
	 * @return
	 */
	public MResultVO<MResultInfo> submit(QueryComment comment){
		try{
			return new MResultVO<>(MResultInfo.COMMENT_SUCCESS);
		}catch(Exception ex){
			log.error("[API接口 - 提交评论 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 评论列表
	 * @param comment
	 * @return
	 */
	public MResultVO<MResultInfo> getCommentList(QueryComment comment) {
		try {
			return new MResultVO<>(MResultInfo.SUCCESS);
		} catch (Exception e) {
			log.error("[API接口 - 评论列表  Exception] = {}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
}
