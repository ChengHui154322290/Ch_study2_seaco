package com.tp.world.controller.comment;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.m.base.BaseQuery;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.comment.QueryComment;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.world.ao.comment.CommentAO;
import com.tp.world.helper.AuthHelper;
import com.tp.world.helper.RequestHelper;

/**
 * 点评控制器
 * @author zhuss
 * @2016年1月4日 下午5:13:42
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

	private static final Logger log = LoggerFactory.getLogger(CommentController.class);
	
	@Autowired
	private CommentAO commentAO;
	
	@Autowired
	private AuthHelper authHelper;
	
	/**
	 * 提交点评
	 * @param comment
	 * @return
	 */
	@RequestMapping(value="/submit",method = RequestMethod.POST)
	@ResponseBody
	public String submit(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryComment comment = (QueryComment) JsonUtil.getObjectByJsonStr(jsonStr, QueryComment.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 提交点评 入参] = {}",JsonUtil.convertObjToStr(comment));
			}
			AssertUtil.notBlank(comment.getSku(), MResultInfo.ITEM_SKU_NULL);
			AssertUtil.notBlank(comment.getOrdercode(), MResultInfo.ORDER_CODE_NULL);
			AssertUtil.notBlank(comment.getSerscore(), MResultInfo.COMMENT_SERSCORE_NULL);
			AssertUtil.notBlank(comment.getItemscore(), MResultInfo.COMMENT_ITEMSCORE_NULL);
			TokenCacheTO usr = authHelper.authToken(comment.getToken());
			comment.setUserid(usr.getUid());
			MResultVO<MResultInfo> result = commentAO.submit(comment);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 提交点评 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 提交点评  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 提交点评 返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 评论列表
	 * @param userTO
	 * @return
	 */
	@RequestMapping(value="/list",method = RequestMethod.GET)
	@ResponseBody
	public String getCommentList(BaseQuery baseQuery,HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryComment comment = (QueryComment) JsonUtil.getObjectByJsonStr(jsonStr, QueryComment.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 评论列表 入参] = {}",JsonUtil.convertObjToStr(comment));
			}
			MResultVO<MResultInfo> result = commentAO.getCommentList(comment);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 评论列表 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 评论列表  MobileException] = {}",me.getMessage());
			return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.ACCOUNT_TIMEOUT));
		}
	}
}
