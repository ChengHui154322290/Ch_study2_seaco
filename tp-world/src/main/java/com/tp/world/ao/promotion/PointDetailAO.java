package com.tp.world.ao.promotion;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.user.QueryUser;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.point.PointDetailVO;
import com.tp.m.vo.point.PointMemberVO;
import com.tp.model.mmp.PointDetail;
import com.tp.model.mmp.PointMember;
import com.tp.proxy.mmp.PointDetailProxy;
import com.tp.proxy.mmp.PointMemberProxy;
import com.tp.world.convert.PointConvert;

/**
 * 积分管理
 * @author szy
 *
 */
@Service
public class PointDetailAO {

	@Autowired
	private PointDetailProxy pointDetailProxy;
	@Autowired
	private PointMemberProxy pointMemberProxy;
	
	public MResultVO<PointMemberVO> queryPointDetailByMemberId(QueryUser queryUser) {
		ResultInfo<PointMember> result = pointMemberProxy.queryPointMemberByMemberId(queryUser.getUserid());
		if(result.isSuccess()){
			PointMember pointMember = result.getData();
			PointMemberVO pointMemberVO = new PointMemberVO();
			if(null==pointMember){
				pointMemberVO.setCount("0");
				return new MResultVO<PointMemberVO>(MResultInfo.SUCCESS,pointMemberVO);
			}
			pointMemberVO.setCount(Integer.toString(pointMember.getTotalPoint()));
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("memberId", queryUser.getUserid());
			params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
			if(StringUtil.isBlank(queryUser.getCurpage())){
				queryUser.setCurpage("1");
			}
			ResultInfo<PageInfo<PointDetail>> pointDetailPageResult = pointDetailProxy.queryPageByParam(params, new PageInfo<PointDetail>(Integer.valueOf(queryUser.getCurpage()),10));
			if(pointDetailPageResult.isSuccess()){
				if(pointDetailPageResult.getData()==null || CollectionUtils.isEmpty(pointDetailPageResult.getData().getRows())){
					pointMemberVO.setCount("0");
					return new MResultVO<PointMemberVO>(MResultInfo.SUCCESS,pointMemberVO);
				}
				
				Page<PointDetailVO> pointDetailPage = PointConvert.convertPointDetailPage(pointDetailPageResult.getData());
				pointMemberVO.setPointDetailPage(pointDetailPage);
				
				return new MResultVO<PointMemberVO>(MResultInfo.SUCCESS,pointMemberVO);
			}
		}
		return new MResultVO<>(result.getMsg().getMessage());
	}

}
