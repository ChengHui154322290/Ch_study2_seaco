package com.tp.world.ao.dss;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.FastConstant;
import com.tp.common.vo.PageInfo;
import com.tp.m.query.user.QueryUser;
import com.tp.model.dss.FastUserInfo;
import com.tp.proxy.dss.FastUserInfoProxy;

/**
 * 店铺员工管理
 * @author szy
 *
 */
@Service
public class ShopUserInfoAO {

	@Autowired
	private FastUserInfoProxy fastUserInfoProxy;
	
	public void list(QueryUser queryUser){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mobile",queryUser.getShopMobile());
		params.put("userType",FastConstant.USER_TYPE.MANAGER.code);
		params.put("enabled", Constant.ENABLED.YES);
		FastUserInfo fastUserInfo = fastUserInfoProxy.queryUniqueByParams(params).getData();
		if(fastUserInfo==null){
			return ;
		}
		params.remove("mobile");
		params.put("warehouseId", fastUserInfo.getWarehouseId());
		params.put("userType",FastConstant.USER_TYPE.EMPLOYEE.code);
		PageInfo<FastUserInfo> fastUserInfoList = fastUserInfoProxy.queryPageByParamNotEmpty(params,new PageInfo<FastUserInfo>(1,100)).getData();
		
	}
	
	public void save(FastUserInfo fastUserInfo){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mobile",fastUserInfo.parentUserMobile);
		params.put("userType",FastConstant.USER_TYPE.MANAGER.code);
		params.put("enabled", Constant.ENABLED.YES);
		FastUserInfo parentUserInfo = fastUserInfoProxy.queryUniqueByParams(params).getData();
		if(parentUserInfo==null){
			return ;
		}
		FastUserInfo oldFastUserInfo = fastUserInfoProxy.queryById(fastUserInfo.getFastUserId()).getData();
		if(oldFastUserInfo==null){
			return;
		}
		if(!parentUserInfo.getWarehouseId().equals(oldFastUserInfo.getFastUserId())){
			return;
		}
		
		FastUserInfo newFastUserInfo = new FastUserInfo();
		newFastUserInfo.setFastUserId(fastUserInfo.getFastUserId());
		newFastUserInfo.setUserName(fastUserInfo.getUserName());
		newFastUserInfo.setUserType(FastConstant.USER_TYPE.EMPLOYEE.code);
		newFastUserInfo.setEnabled(fastUserInfo.getEnabled());
		newFastUserInfo.setCreateTime(new Date());
		newFastUserInfo.setCreateUser(fastUserInfo.getUpdateUser());
		newFastUserInfo.setUpdateTime(new Date());
		newFastUserInfo.setCreateUser(fastUserInfo.getUpdateUser());
		if(newFastUserInfo.getFastUserId()!=null){
			fastUserInfoProxy.updateNotNullById(newFastUserInfo);
		}else{
			fastUserInfoProxy.insert(newFastUserInfo);
		}
		
	}
	
	public void delete(FastUserInfo fastUserInfo){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mobile",fastUserInfo.parentUserMobile);
		params.put("userType",FastConstant.USER_TYPE.MANAGER.code);
		params.put("enabled", Constant.ENABLED.YES);
		FastUserInfo parentUserInfo = fastUserInfoProxy.queryUniqueByParams(params).getData();
		if(parentUserInfo==null){
			return ;
		}
		FastUserInfo oldFastUserInfo = fastUserInfoProxy.queryById(fastUserInfo.getFastUserId()).getData();
		if(oldFastUserInfo==null){
			return;
		}
		if(!parentUserInfo.getWarehouseId().equals(oldFastUserInfo.getFastUserId())){
			return;
		}
		
		fastUserInfoProxy.deleteById(fastUserInfo.getFastUserId());
		
	}
}
