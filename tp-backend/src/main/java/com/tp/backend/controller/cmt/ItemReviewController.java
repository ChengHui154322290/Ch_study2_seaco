package com.tp.backend.controller.cmt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.CmtConstant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.cmt.ItemReview;
import com.tp.model.mem.MemberInfo;
import com.tp.proxy.cmt.ItemReviewProxy;
import com.tp.proxy.cmt.ItemReviewValidationProxy;


@Controller
@RequestMapping("/comment/review/")
public class ItemReviewController extends AbstractBaseController{
	
	@Autowired
	private ItemReviewProxy itemReviewProxy;
	
	@Autowired
	private ItemReviewValidationProxy itemReviewValidationProxy;
	
	@RequestMapping("/getMemberInfo")
	@ResponseBody
	public ResultInfo<Map<String,String>> getMemberInfo(Model model,String loginName)
			throws Exception {
		MemberInfo memberInfo = itemReviewProxy.getmemberInfoByLoginName(loginName);
		if(null != memberInfo){
			Map<String, String> map = new HashMap<>();
			map.put("uid", memberInfo.getId()+"");
			map.put("memNickName", memberInfo.getNickName());
			map.put("loginName", memberInfo.getMobile());
			return new ResultInfo<Map<String, String>>(map);
		}
		return new ResultInfo<Map<String, String>>(new FailInfo("查询失败"));
	}
	
	@RequestMapping(value="/list")
	public void review(Model model, ItemReview review, Integer page, Integer size){
		PageInfo<ItemReview> pageInfo = itemReviewProxy.getItemReview(review, new PageInfo<ItemReview>(page, size));
		model.addAttribute("page", pageInfo);
		model.addAttribute("review", review);
	}
	
	@RequestMapping(value="/add")
	public String add(Model model) {
		return "comment/review/add";
	}
	
	@RequestMapping(value="/addreview")
	@ResponseBody
	public ResultInfo<Boolean> addReview(ItemReview review) {
		ResultInfo<Boolean> validationResult = itemReviewValidationProxy.itemReviewValidation(review, 2); 
		if (Boolean.FALSE == validationResult.isSuccess()) {
			return validationResult;
		}
		ResultInfo<MemberInfo> validationUserInfoResult = 
				itemReviewValidationProxy.validationUserInfo(review.getUid(), null, review.getUserName());
		if (Boolean.FALSE == validationUserInfoResult.isSuccess()) {
			return new ResultInfo<Boolean>(new FailInfo(validationUserInfoResult.getMsg().getMessage()));
		}
		review.setUid(validationUserInfoResult.getData().getId());
		review.setUserName(validationUserInfoResult.getData().getNickName());
		review.setCreateTime(new Date());
		review.setIsAnonymous(CmtConstant.ItemReviewConstant.ISANONYMOUS.NOT_ANONYMOUS);
		review.setIsCheck(CmtConstant.ItemReviewConstant.ISCHECK.CHECKED);
		review.setIsDelete(CmtConstant.ItemReviewConstant.ISDELETE.UNDELETE);
		review.setStatus(CmtConstant.ItemReviewConstant.STATUS.ZERO);
		review.setModifyTime(new Date());
		review.setIsHide(CmtConstant.ItemReviewConstant.HIDE.SHOW);
		review.setIsTop(CmtConstant.ItemReviewConstant.TOP.UNLIMITED);
		review.setMark(5);
		
		ResultInfo<ItemReview> saveResult = itemReviewProxy.insert(review);
		if (Boolean.FALSE == saveResult.isSuccess()) {
			return new ResultInfo<Boolean>(new FailInfo(saveResult.getMsg().getMessage()));
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
	   
	@RequestMapping("/detail")
	public String detail(Long id, Model model){
		model.addAttribute("review", itemReviewProxy.getItemReview(id));
		return "/comment/review/detail";
	}
	
	@RequestMapping(value="/update")
	@ResponseBody
	public ResultInfo<Boolean> update(ItemReview review){
		if (null == review.getId()) {
			return new ResultInfo<Boolean>(new FailInfo("评论ID不存在"));
		}
		ResultInfo<Boolean> contentValidationResult = 
				itemReviewValidationProxy.itemReviewContentValidation(review.getContent());
		if(Boolean.FALSE == contentValidationResult.isSuccess()){
			return contentValidationResult;
		}
		ItemReview itemReview = new ItemReview();
		itemReview.setId(review.getId());
		itemReview.setIsHide(review.getIsHide());
		itemReview.setMark(review.getMark());
		itemReview.setIsTop(review.getIsTop());
		itemReview.setContent(review.getContent());
		itemReviewProxy.updateNotNullById(itemReview);
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
}
