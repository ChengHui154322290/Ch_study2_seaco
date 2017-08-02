package com.tp.proxy.cmt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.cmt.BeViewpoint;
import com.tp.model.mem.MemberInfo;
import com.tp.model.prd.ItemDetail;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cmt.IBeViewpointService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.prd.IItemDetailService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;
/**
 * 西客观点代理层
 * @author szy
 *
 */
@Service
public class BeViewpointProxy extends BaseProxy<BeViewpoint>{

	@Autowired
	private IBeViewpointService beViewpointService;

	@Autowired
	private IItemDetailService itemDetailService;
	
	@Autowired
	private IMemberInfoService memberInfoService;
	
	@Autowired
	private ItemReviewValidationProxy itemReviewValidationProxy;
	
	@Override
	public IBaseService<BeViewpoint> getService() {
		return beViewpointService;
	}
		
	public BeViewpoint  getViewPointById (Long id){
		BeViewpoint viewPoint = beViewpointService.queryById(id);
		
		MemberInfo memberInfo = memberInfoService.queryById(viewPoint.getUserId());
		if(null != memberInfo){
			viewPoint.setMemNickName(memberInfo.getNickName());
			viewPoint.setMemLoginName(memberInfo.getMobile());
		}
		return viewPoint ;
	}
	
	/** backend接口 */
	public ResultInfo<Boolean> save(UserInfo userInfo, BeViewpoint beViewpoint){
		ResultInfo<Boolean> validResult  = validViewpoint(beViewpoint);
		if(Boolean.FALSE == validResult.isSuccess()){
			return validResult;
		}
		if(null == beViewpoint.getId()){
			beViewpoint.setCreateUserId(userInfo.getId());
			beViewpoint.setCreateTime(new Date());
			beViewpoint.setModifyUserId(userInfo.getId());
			beViewpoint.setModifyTime(new Date());
			if(null==beViewpoint.getHideSign()){
				beViewpoint.setHideSign(0);
			}if(null==beViewpoint.getStickSign()){
				beViewpoint.setStickSign(1);
			}
			beViewpointService.insert(beViewpoint);
		}else{
			beViewpoint.setModifyUserId(userInfo.getId());
			beViewpoint.setModifyTime(new Date());
			beViewpointService.updateNotNullById(beViewpoint);
		}
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}
	
	/** backend接口 */
	public PageInfo<BeViewpoint> listViewpoints(BeViewpoint beViewPoint, PageInfo<BeViewpoint> pageInfo) {
		List<Long> memberIds = new ArrayList<Long>();
		Map<String, Object> params = new HashMap<>();
		if(null != beViewPoint.getMemLoginName() || null != beViewPoint.getMemNickName()){
			MemberInfo memberInfo = new MemberInfo();
			if(StringUtil.isNotBlank(beViewPoint.getMemLoginName()))
				memberInfo.setMobile(beViewPoint.getMemLoginName());
			if(StringUtil.isNotBlank(beViewPoint.getMemNickName()))
				memberInfo.setNickName(beViewPoint.getMemNickName());
			List<MemberInfo> memberInfos = memberInfoService.queryByObject(memberInfo);
			for (MemberInfo mi : memberInfos) {
				memberIds.add(mi.getId());
			}
			beViewPoint.setMemNickName(null);
			beViewPoint.setMemLoginName(null);
		}
		
		params = BeanUtil.beanMap(beViewPoint);
		params.remove("createStartDate");
		params.remove("createEndDate");
		List<WHERE_ENTRY> whEntries = new ArrayList<>();
		if (null != beViewPoint.getCreateStartDate()) {
			whEntries.add(new WHERE_ENTRY("viewpoint_time", MYBATIS_SPECIAL_STRING.GT, beViewPoint.getCreateStartDate()));
		}
		if (null != beViewPoint.getCreateEndDate()) {
			whEntries.add(new WHERE_ENTRY("viewpoint_time", MYBATIS_SPECIAL_STRING.LT, beViewPoint.getCreateEndDate()));
		}
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
		if (CollectionUtils.isNotEmpty(memberIds)) {
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "user_id in(" + StringUtils.join(memberIds, Constant.SPLIT_SIGN.COMMA) + ")");	
		}
		return beViewpointService.queryPageByParam(params, pageInfo);
	}
	
	/** backend接口 
	 * */
	public ResultInfo<Boolean> validViewpoint(BeViewpoint beViewPoint) {
		if(null == beViewPoint){
			return new ResultInfo<Boolean>(new FailInfo("数据为空 "));
		}
		// step1 校验 spu
		if(StringUtil.isNullOrEmpty(beViewPoint.getSort())) {
			return new ResultInfo<Boolean>(new FailInfo("序号不能为空 "));
		}
		if (StringUtil.isNullOrEmpty(beViewPoint.getBarcode())) {
			return new ResultInfo<Boolean>(new FailInfo("条码不能为空 "));
		}
		if (StringUtil.isNullOrEmpty(beViewPoint.getContent())) {
			return new ResultInfo<Boolean>(new FailInfo("评论内容不能为空 "));
		} 
		if (StringUtil.isNullOrEmpty(beViewPoint.getSpu())) {
			return new ResultInfo<Boolean>(new FailInfo("SPU不能为空"));
		}
		if (StringUtil.isNullOrEmpty(beViewPoint.getUserId())) {
			return new ResultInfo<Boolean>(new FailInfo("用户不能为空"));
		}
		if (StringUtil.isNullOrEmpty(beViewPoint.getViewpointTime())) {
			return new ResultInfo<Boolean>(new FailInfo("评论时间不能为空"));
		}
		
		if (itemReviewValidationProxy.contentValidation(beViewPoint.getContent())) {
			return new ResultInfo<Boolean>(new FailInfo("包含违禁词"));
		}
		
		return new ResultInfo<Boolean>(Boolean.TRUE);
	}

	public ItemDetail getPidByBarcode(String barcode){
		if(StringUtils.isBlank(barcode)){
			return null;
		}
		ItemDetail detail = new ItemDetail();
		detail.setBarcode(barcode);
		return itemDetailService.queryUniqueByObject(detail);
	}

	public MemberInfo getmemberInfoByLoginName(String loginName){
		//获取用户信息
		MemberInfo memberInfo = null;
		if(StringUtils.isBlank(loginName)){
			return null;
		}
		memberInfo = new MemberInfo();
		memberInfo.setMobile(loginName);
		List<MemberInfo> memberInfos = memberInfoService.queryByObject(memberInfo);
		if(CollectionUtils.isNotEmpty(memberInfos)){
			return memberInfos.get(0);
		}
		return null;
	}
}
