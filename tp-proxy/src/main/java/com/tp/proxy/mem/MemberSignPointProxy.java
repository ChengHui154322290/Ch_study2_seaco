package com.tp.proxy.mem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mem.MemberSignPoint;
import com.tp.model.mmp.PointMember;
import com.tp.model.mmp.PointSignConfig;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.IMemberSignPointService;
import com.tp.service.mmp.IPointDetailService;
import com.tp.service.mmp.IPointMemberService;
import com.tp.service.mmp.IPointSignConfigService;
import com.tp.util.DateUtil;
/**
 * 会员签到获取积分日志表代理层
 * @author szy
 *
 */
@Service
public class MemberSignPointProxy extends BaseProxy<MemberSignPoint>{

	@Autowired
	private IMemberSignPointService memberSignPointService;
	@Autowired
	private IPointSignConfigService pointSignConfigService;
	@Autowired
	private IPointDetailService pointDetailService;
	
	@Override
	public IBaseService<MemberSignPoint> getService() {
		return memberSignPointService;
	}
	
	/**
	 * 获取会员签到页面数据
	 * 1.查询当前日期本月签到天数及所获取到的积分
	 * 2.查询签到积分配置，计算以后天数签到所获取的积分数
	 * @param memberId
	 * @return
	 */
	public ResultInfo<List<MemberSignPoint>> queryMemberSignPointListByMemberId(Long memberId){
		Date currentDate = new Date();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", memberId);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sign_date >='"+DateUtil.formatDate(currentDate, "yyyy-MM")+"'");
		List<MemberSignPoint> memberSignPointList = memberSignPointService.queryByParam(params);
		MemberSignPoint yesterdayMemberSignPoint = null;
		MemberSignPoint todayMemberSignPoint = null;
		if(CollectionUtils.isNotEmpty(memberSignPointList)){
			Collections.sort(memberSignPointList, new Comparator<MemberSignPoint>(){
				public int compare(MemberSignPoint o1, MemberSignPoint o2) {
					return o1.getSignDate().compareTo(o2.getSignDate());
				}
			});
			//比较最近日期是否是昨天或今天，如果是则连续
			String yesterdayString = DateUtil.formatDate(DateUtil.addDays(currentDate, -1));
			String todayString = DateUtil.formatDate(currentDate);
			for(MemberSignPoint t:memberSignPointList){
				t.setSigned(Boolean.TRUE);
				String crrentTimeStr = DateUtil.formatDate(t.getSignDate());
				 if(yesterdayString.equals(crrentTimeStr)){
					 yesterdayMemberSignPoint = t;
				 }else if(todayString.equals(crrentTimeStr)){
					 todayMemberSignPoint = t;
				 }
			}
		}else{
			memberSignPointList = new ArrayList<MemberSignPoint>();
		}
		
		Integer days = 0;
		params.clear();
		params.put("used", Constant.ENABLED.YES);
		if(todayMemberSignPoint!=null){
			days = todayMemberSignPoint.getDays();
		}else if(yesterdayMemberSignPoint!=null){
			days = yesterdayMemberSignPoint.getDays();
		}
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sequence_day >"+days);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " sequence_day asc");
		params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), 60);
		List<PointSignConfig> pointSignConfigList = pointSignConfigService.queryByParam(params);
		if(CollectionUtils.isNotEmpty(pointSignConfigList)){
			Collections.sort(pointSignConfigList, new Comparator<PointSignConfig>(){
				public int compare(PointSignConfig o1, PointSignConfig o2) {
					return o1.getSequenceDay().compareTo(o2.getSequenceDay());
				}
			});
		}else{
			pointSignConfigList = new ArrayList<PointSignConfig>();
			params.clear();
			params.put("used", Constant.ENABLED.YES);
			params.put(MYBATIS_SPECIAL_STRING.LIMIT.name(), 1);
			params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " sequence_day desc");
			PointSignConfig pointSignConfig = pointSignConfigService.queryUniqueByParams(params);
			Integer point = 0;
			if(pointSignConfig!=null){
				point = pointSignConfig.getPoint();
			}
			for(int i=0;i<60;i++){
				PointSignConfig pointSign = new PointSignConfig();
				pointSign.setSequenceDay(i+days);
				pointSign.setPoint(point);
			}
		}
		if(todayMemberSignPoint!=null){
			for(int i=0;i<pointSignConfigList.size();i++){
				PointSignConfig pointSignConfig = pointSignConfigList.get(i);
				MemberSignPoint memberSignPoint = new MemberSignPoint();
				memberSignPoint.setDays(pointSignConfig.getSequenceDay());
				memberSignPoint.setPoint(pointSignConfig.getPoint());
				memberSignPoint.setSignDate(DateUtil.addDays(currentDate, i+1));
				memberSignPoint.setSigned(Boolean.FALSE);
				memberSignPointList.add(memberSignPoint);
			}
		}else{
			for(int i=0;i<pointSignConfigList.size();i++){
				PointSignConfig pointSignConfig = pointSignConfigList.get(i);
				MemberSignPoint memberSignPoint = new MemberSignPoint();
				memberSignPoint.setDays(pointSignConfig.getSequenceDay());
				memberSignPoint.setPoint(pointSignConfig.getPoint());
				memberSignPoint.setSignDate(DateUtil.addDays(currentDate, i));
				memberSignPoint.setSigned(Boolean.FALSE);
				if(i==0){
					memberSignPoint.setEnabled(Boolean.TRUE);
				}
				memberSignPointList.add(memberSignPoint);
			}
		}
		return new ResultInfo<>(memberSignPointList);
	}

	/**
	 * 会员签到
	 * 1.获取今日是否签到
	 * 2.获取最近签到日期
	 * 3.计算签到所获取到积分
	 * 4.插入签到数据
	 * @param memberId
	 * @return
	 */
	public ResultInfo<MemberSignPoint> insertMemberSignPoint(Long memberId,String memberName){
		Date currentDate = new Date();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", memberId);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sign_date >='"+DateUtil.formatDate(currentDate)+"' and sign_date<='"+DateUtil.formatDate(DateUtil.addDays(currentDate, 1))+"'");
		Integer count = memberSignPointService.queryByParamCount(params);
		if(count>0){
			return new ResultInfo<MemberSignPoint>(new FailInfo("本日已签到"));
		}
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sign_date <='"+DateUtil.formatDate(currentDate)+"' and sign_date>='"+DateUtil.formatDate(DateUtil.addDays(currentDate, -1))+"'");
		MemberSignPoint memberSignPoint = memberSignPointService.queryUniqueByParams(params);
		Integer beginDay = 1;
		Integer point = 0;
		if(memberSignPoint!=null){
			beginDay = beginDay+memberSignPoint.getDays();
			point = memberSignPoint.getPoint();
		}
		params.clear();
		params.put("sequenceDay", beginDay);
		params.put("used", Constant.ENABLED.YES);
		PointSignConfig pointSignConfig = pointSignConfigService.queryUniqueByParams(params);
		if(pointSignConfig!=null){
			point = pointSignConfig.getPoint();
		}
		memberSignPoint = new MemberSignPoint();
		memberSignPoint.setCreateTime(currentDate);
		memberSignPoint.setCreateUser(Constant.AUTHOR_TYPE.MEMBER+memberName);
		memberSignPoint.setMemberId(memberId);
		memberSignPoint.setPoint(point);
		memberSignPoint.setDays(beginDay);
		memberSignPoint.setSignDate(currentDate);
		try{
			memberSignPoint = memberSignPointService.insert(memberSignPoint);
			pointDetailService.addPointByMemberSign(memberId, memberName, point, currentDate);
			return new ResultInfo<MemberSignPoint>(memberSignPoint);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,memberSignPoint);
			return new ResultInfo<>(failInfo);
		}
	}
	/**
	 * 会员签到日历页面
	 * 1.按月在日历中展示已签到日期和积分
	 * 2.如果当天没签到，则插入当天签到数据（计算当天签到应得积分）
	 */
	public ResultInfo<List<MemberSignPoint>> showAndSignPoint(Long memberId,String memberName){
		Integer point = 0;
		Date currentDate = new Date();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", memberId);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sign_date >='"+DateUtil.formatDate(currentDate, "yyyy-MM-dd")+"'");
		List<MemberSignPoint> memberSignPointList = memberSignPointService.queryByParam(params);//查询当天签到情况
		if(CollectionUtils.isEmpty(memberSignPointList)){//当天还没签到
			//计算当日签到应得积分（查询前一天签到数据，如果前一天没有签到数据，则当日为第一天签到，获得5积分；
			//如果前一天有签到数据，则当日为连续签到，若前一天签到获得积分不为10，则当天积分为昨天积分加1，否则为10）
			MemberSignPoint todaySignPoint = new MemberSignPoint();
			params.clear();
			params.put("memberId", memberId);
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sign_date <'"+DateUtil.formatDate(currentDate)+"' and sign_date >='"+DateUtil.formatDate(DateUtil.addDays(currentDate, -1))+"'");//前一天的日期
			List<MemberSignPoint> signPointList = memberSignPointService.queryByParam(params);//查询前一天签到情况
			if(CollectionUtils.isNotEmpty(signPointList)){//连续签到
				MemberSignPoint yesTodaySignPoint = signPointList.get(0);
				
				if(yesTodaySignPoint.getDays() >= 6){//最大
					params.clear();
					params.put("sequenceDay", 6);
					params.put("used", 1);
					List<PointSignConfig> pointSignConfigs = pointSignConfigService.queryByParam(params);
					todaySignPoint.setPoint(pointSignConfigs.get(0).getPoint());
					point = pointSignConfigs.get(0).getPoint();
				}else{
					params.clear();
					params.put("sequenceDay", yesTodaySignPoint.getDays()+1);
					params.put("used", 1);
					List<PointSignConfig> pointSignConfigs = pointSignConfigService.queryByParam(params);
					todaySignPoint.setPoint(pointSignConfigs.get(0).getPoint());
					point = pointSignConfigs.get(0).getPoint();
				}
				todaySignPoint.setDays(yesTodaySignPoint.getDays()+1);
			}else{//不连续签到
				todaySignPoint.setDays(1);
				params.clear();
				params.put("sequenceDay", 1);
				params.put("used", 1);
				List<PointSignConfig> pointSignConfigs = pointSignConfigService.queryByParam(params);
				todaySignPoint.setPoint(pointSignConfigs.get(0).getPoint());//
				point = pointSignConfigs.get(0).getPoint();
			}
			todaySignPoint.setSignDate(currentDate);
			todaySignPoint.setMemberId(memberId);
			todaySignPoint.setCreateTime(currentDate);
			todaySignPoint.setCreateUser(memberName);
			memberSignPointService.changeMemberSignPoint(todaySignPoint, memberId, memberName, point);
			/*memberSignPointService.insert(todaySignPoint);
			params.clear();
			params.put("memberId", memberId);
			List<PointMember> pointList = pointMemberService.queryByParam(params);
			PointMember pointMember = new PointMember();
			if(CollectionUtils.isEmpty(pointList)){//会员积分记录为空，新增
				pointMember.setMemberId(memberId);
				pointMember.setAccumulatePoint(Long.valueOf(point));
				pointMember.setTotalPoint(point);
				pointMember.setCreateTime(currentDate);
				pointMember.setCreateUser(memberName);
				pointMember.setUpdateTime(currentDate);
				pointMember.setUpdateUser(memberName);
				pointMemberService.insert(pointMember);
			}else{//会员积分记录不为空，修改
				pointMember.setAccumulatePoint(Long.sum(Long.valueOf(point), pointList.get(0).getAccumulatePoint()));
				pointMember.setTotalPoint(point+Integer.parseInt(String.valueOf(pointList.get(0).getAccumulatePoint())));
				pointMember.setUpdateTime(currentDate);
				pointMember.setUpdateUser(memberName);
				pointMemberService.insert(pointMember);
			}*/
			
		}
		params.clear();
		params.put("memberId", memberId);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sign_date >='"+DateUtil.formatDate(currentDate, "yyyy-MM")+"'and sign_date <'"+DateUtil.formatDate(DateUtil.addMonths(currentDate, 1),"yyyy-MM")+"'");//当前月份
		List<MemberSignPoint> signPointList = memberSignPointService.queryByParam(params);//当前月的签到情况
//		result.setData(memberSignPointList);
//		return result;
		return new ResultInfo<>(signPointList);
	}

	/**
	 * 会员签到页面
	 */
	public ResultInfo<List<MemberSignPoint>> showSignPointData(Long memberId){
		Date currentDate = new Date();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("memberId", memberId);
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sign_date >='"+DateUtil.formatDate(currentDate)+"'");
		List<MemberSignPoint> memberSignPointList = memberSignPointService.queryByParam(params);//查询当天签到情况
		if(CollectionUtils.isNotEmpty(memberSignPointList)){//当天已签到
			return new ResultInfo<>(memberSignPointList);
		}else{//当天未签到
			params.clear();
			params.put("memberId", memberId);
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sign_date <'"+DateUtil.formatDate(currentDate)+"' and sign_date >='"+DateUtil.formatDate(DateUtil.addDays(currentDate, -1))+"'");
			List<MemberSignPoint> yestodayMemberSignPointList = memberSignPointService.queryByParam(params);//查询前一天签到情况
			List<MemberSignPoint> memberSignPoints = new ArrayList<MemberSignPoint>();
			if(CollectionUtils.isNotEmpty(yestodayMemberSignPointList)){//前一天已签到
				
				MemberSignPoint memberSignPoint = new MemberSignPoint();
				memberSignPoint.setDays(yestodayMemberSignPointList.get(0).getDays()+1);
				params.clear();
				if(yestodayMemberSignPointList.get(0).getDays() >= 6){
					params.put("sequenceDay", 6);
				}else{
					params.put("sequenceDay", yestodayMemberSignPointList.get(0).getDays()+1);
				}
				params.put("used", 1);
				List<PointSignConfig> pointSignConfigs = pointSignConfigService.queryByParam(params);
				
				memberSignPoint.setPoint(pointSignConfigs.get(0).getPoint());
				memberSignPoint.setSigned(Boolean.FALSE);
				memberSignPoint.setEnabled(Boolean.TRUE);
				memberSignPoints.add(memberSignPoint);
				return new ResultInfo<>(memberSignPoints);
			}else{//前一天未签到
				MemberSignPoint memberSignPoint = new MemberSignPoint();
				memberSignPoint.setDays(1);
				params.clear();
				params.put("sequenceDay", 1);
				params.put("used", 1);
				List<PointSignConfig> pointSignConfigs = pointSignConfigService.queryByParam(params);
				
				memberSignPoint.setPoint(pointSignConfigs.get(0).getPoint());
				memberSignPoint.setSigned(Boolean.FALSE);
				memberSignPoint.setEnabled(Boolean.TRUE);
				memberSignPoints.add(memberSignPoint);
				return new ResultInfo<>(memberSignPoints);
			}
		}
	}
}
