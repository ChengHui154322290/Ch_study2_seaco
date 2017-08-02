package com.tp.world.ao.GroupCoupon;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.FastConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.model.dss.FastUserInfo;
import com.tp.proxy.dss.FastUserInfoProxy;

/**
 * 店铺人员管理
 * @author szy
 *
 */
@Service
public class FastUserInfoAO {
	
	private static final Logger log = LoggerFactory.getLogger(FastUserInfoAO.class);
	
	@Autowired
	private FastUserInfoProxy fastUserInfoProxy;
	
	public MResultVO<List<FastUserInfo>> queryUserInfoListByManagerMobile(String mobile){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("enabled", Constant.ENABLED.YES);
		params.put("userType",FastConstant.USER_TYPE.MANAGER.code);
		params.put("shopType",FastConstant.SHOP_TYPE.GROUP_COUPON.code);
		params.put("mobile", mobile);
		FastUserInfo manager = fastUserInfoProxy.queryUniqueByParams(params).getData();
		if(manager==null){
			return new MResultVO<>("您无权查询店员");
		}
		params.put("userType",FastConstant.USER_TYPE.EMPLOYEE.code);
		params.put("warehouseId", manager.getWarehouseId());
		params.remove("mobile");
		try{
			List<FastUserInfo> userInfoList = fastUserInfoProxy.queryByParam(params).getData();
			return new MResultVO<>(MResultInfo.SUCCESS,userInfoList);
		}catch(Exception ex){
			log.error("[API接口 - 获取店员列表 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	public MResultVO<FastUserInfo> insertUserInfo(final FastUserInfo fastUserInfo){
		ResultInfo<FastUserInfo> validate = validate(fastUserInfo);
		if(validate!=null && !validate.success){
			return new MResultVO<>(validate.getMsg().getMessage());
		}
		FastUserInfo userInfo = new FastUserInfo();
		userInfo.setCreateTime(new Date());
		userInfo.setCreateUser(fastUserInfo.getUpdateUser());
		userInfo.setEnabled(Constant.ENABLED.YES);
		userInfo.setMobile(fastUserInfo.getMobile());
		userInfo.setUpdateTime(new Date());
		userInfo.setShopType(FastConstant.SHOP_TYPE.GROUP_COUPON.code);
		userInfo.setUpdateUser(fastUserInfo.getUpdateUser());
		userInfo.setUserName(fastUserInfo.getMobile());
		userInfo.setUserType(FastConstant.USER_TYPE.EMPLOYEE.code);
		userInfo.setWarehouseId(fastUserInfo.getWarehouseId());
		try{
			ResultInfo<FastUserInfo> result = fastUserInfoProxy.insert(userInfo);
			if(result.success){
				return new MResultVO<>(MResultInfo.SUCCESS,result.getData());
			}
			return new MResultVO<>(result.getMsg().getMessage());
		}catch(Exception ex){
			log.error("[API接口 - 新增店员 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	public MResultVO<Integer> deleteUserInfo(final FastUserInfo fastUserInfo){
		Long warehouseId = fastUserInfoProxy.queryFastUserWarehouseIdbyMobile(fastUserInfo.parentUserMobile,fastUserInfo.getShopType());
		if(warehouseId==null){
			return new MResultVO<>("您没有权限删除店员");
		}
		FastUserInfo employee = fastUserInfoProxy.queryById(fastUserInfo.getFastUserId()).getData();
		if(employee==null){
			return new MResultVO<>("该店员不存在");
		}
		if(!employee.getWarehouseId().equals(warehouseId)){
			return new MResultVO<>("您没有权限删除该店员");
		}
		try{
			ResultInfo<Integer> result = fastUserInfoProxy.deleteById(fastUserInfo.getFastUserId());
 			if(result.success){
				return new MResultVO<>(MResultInfo.SUCCESS,result.getData());
			}
 			return new MResultVO<>(result.getMsg().getMessage());
		}catch(Exception ex){
			log.error("[API接口 - 删除店员 Exception] = {}",ex);
			return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
		}
	}
	
	public ResultInfo<FastUserInfo> validate(FastUserInfo fastUserInfo){
		if(StringUtils.isBlank(fastUserInfo.getMobile())){
			return new ResultInfo<FastUserInfo>(new FailInfo("请输入手机号",110031));
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mobile", fastUserInfo.parentUserMobile);
		params.put("enabled", Constant.ENABLED.YES);
		params.put("userType",FastConstant.USER_TYPE.MANAGER.code);
		params.put("shopType",FastConstant.SHOP_TYPE.GROUP_COUPON.code);
		FastUserInfo manager = fastUserInfoProxy.queryUniqueByParams(params).getData();
		if(manager==null){
			return new ResultInfo<>(new FailInfo("您没有权限添加店员",110033));
		}
		if(fastUserInfo.getMobile().equals(fastUserInfo.parentUserMobile)){
			return new ResultInfo<>(new FailInfo("手机号不能与管理员手机重复",130324));
		}
		params.put("mobile", fastUserInfo.getMobile());
		params.put("userType",FastConstant.USER_TYPE.EMPLOYEE.code);
		params.remove("enabled");
		params.put("warehouseId", manager.getWarehouseId());
		Integer count = fastUserInfoProxy.queryByParamCount(params).getData();
		if(count!=null && count>0){
			return new ResultInfo<>(new FailInfo("店员已存在，如查询不到可能是已禁用",130324));
		}
		fastUserInfo.setWarehouseId(manager.getWarehouseId());
		return new ResultInfo<>(fastUserInfo);
	}
}
