package com.tp.proxy.cmt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.ForbiddenWords;
import com.tp.model.cmt.ItemReview;
import com.tp.model.mem.MemberInfo;
import com.tp.service.bse.IForbiddenWordsService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.util.StringUtil;


@Service
public class ItemReviewValidationProxy {
	 
	@Autowired
	private IForbiddenWordsService forbiddenWordsService;
	
	@Autowired
	private IMemberInfoService memberInfoService;
	
	/** 默认是正常用户评论 */
	public ResultInfo<Boolean> itemReviewValidation(ItemReview itemReview) {
		return itemReviewValidation(itemReview, 1);
	}
	/** type = 1正常用户评论 2后台添加或者修改 */
	public ResultInfo<Boolean> itemReviewValidation(ItemReview itemReview, Integer type) { 
		if(StringUtil.isNullOrEmpty(itemReview.getSpu())) 
			return new ResultInfo<Boolean>(new FailInfo("SPU为空", 910));
		if (null == itemReview.getUid() && StringUtil.isNullOrEmpty(itemReview.getUserName())) {
			return new ResultInfo<Boolean>(new FailInfo("用户信息为空", 910));
		}
		if(1 == type && StringUtil.isNullOrEmpty(itemReview.getOrderCode()))
			return new ResultInfo<Boolean>(new FailInfo("订单号为空", 910));
		if(1 == type && !StringUtils.isNumeric(itemReview.getOrderCode()))
			return new ResultInfo<Boolean>(new FailInfo("订单号包含非数值型数据", 910));
		if(null == itemReview.getMark())
			return new ResultInfo<Boolean>(new FailInfo("分值为空", 910));
		ResultInfo<Boolean> contentValidationResult = itemReviewContentValidation(itemReview.getContent());
		if (Boolean.FALSE == contentValidationResult.isSuccess()) {
			return contentValidationResult;
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
	
	/** 校验会员信息,确认是否唯一*/
	public ResultInfo<MemberInfo> validationUserInfo(Long uid, String loginName, String userName){
		if (uid == null && StringUtil.isNullOrEmpty(loginName) && StringUtil.isNullOrEmpty(userName)) {
			return new ResultInfo<MemberInfo>(new FailInfo("用户信息为空"));
		}
		MemberInfo memberInfo = new MemberInfo();
		memberInfo.setId(uid);
		memberInfo.setMobile(loginName);
		memberInfo.setNickName(userName);
		List<MemberInfo> memInfos = memberInfoService.queryByObject(memberInfo);
		if (CollectionUtils.isEmpty(memInfos)) {
			return new ResultInfo<MemberInfo>(new FailInfo("用户不存在"));
		}
		if (memInfos.size() > 1) {
			return new ResultInfo<MemberInfo>(new FailInfo("用户信息不唯一"));
		}
		return new ResultInfo<MemberInfo>(memInfos.get(0));
	}
	
	public String filterItemReviewContent(String content, String replace){
		Pattern forbiddenPattern = getForbiddenWordsPattern();
		return forbiddenPattern.matcher(content).replaceAll(replace);
	}
	
	public String filterItemReviewContent(Pattern pattern, String content, String replace){
		return pattern.matcher(content).replaceAll(replace);
	}
	
	public ResultInfo<Boolean> itemReviewContentValidation(String content){
		if(null == content)
			return new ResultInfo<Boolean>(new FailInfo("评论内容为空", 910));
		if(content.length() < 10)
			return new ResultInfo<Boolean>(new FailInfo("评论至少10个字", 910));
		if(content.length() > 5000)
			return new ResultInfo<Boolean>(new FailInfo("评论至多5000个字", 910));
		if(content.contains("script"))
			return new ResultInfo<Boolean>(new FailInfo("评论包含非法字符", 910));
		if (contentValidation(content)) {
			return new ResultInfo<Boolean>(new FailInfo("包含违禁词"));
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
	
	public boolean contentValidation(String content){
		Pattern forbiddenPattern = getForbiddenWordsPattern();
		return forbiddenPattern.matcher(content).matches();
	}
	
	public boolean contentValidation(Pattern pattern, String content){
		return pattern.matcher(content).matches();
	}
	
	public Pattern getForbiddenWordsPattern(){
		Map<String, Object> params = new HashMap<>();
    	params.clear();
    	params.put("status", 1);
    	List<ForbiddenWords> forbiddenWords = forbiddenWordsService.queryByParam(params);
    	StringBuffer sBuffer = new StringBuffer();
    	for (ForbiddenWords forbiddenWords2 : forbiddenWords) {
			sBuffer.append(forbiddenWords2.getWords()).append("|");
		}
    	sBuffer.deleteCharAt(sBuffer.length() - 1);
    	return Pattern.compile(new String(sBuffer.toString().getBytes()));
	}
}
