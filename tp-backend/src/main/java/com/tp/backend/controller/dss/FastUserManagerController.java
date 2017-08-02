package com.tp.backend.controller.dss;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.FastConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.StorageConstant;
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.common.vo.Constant.ENABLED;
import com.tp.common.vo.FastConstant.SHOP_TYPE;
import com.tp.common.vo.FastConstant.USER_TYPE;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.FastUserInfo;
import com.tp.model.stg.Warehouse;
import com.tp.proxy.dss.FastUserInfoProxy;
import com.tp.proxy.stg.WarehouseProxy;

/**
 * 西客商城用户管理
 * @author szy
 *
 */
@Controller
@RequestMapping("/fast/user/")
public class FastUserManagerController extends AbstractBaseController {

	@Autowired
	private FastUserInfoProxy fastUserInfoProxy;
	@Autowired
	private WarehouseProxy warehouseProxy;
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public void userlist(Model model){
		model.addAttribute("warehouselist",queryWarehouse(null));
		model.addAttribute("usertypelist",USER_TYPE.values());
		model.addAttribute("shoptypelist",SHOP_TYPE.values());
	}
	@RequestMapping(value="list",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo<FastUserInfo> userlist(Model model,FastUserInfo fastUserInfo){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("warehouseId", fastUserInfo.getWarehouseId());
		params.put("userType", fastUserInfo.getUserType());
		params.put("shopType", fastUserInfo.getShopType());
		params.put("enabled", fastUserInfo.getEnabled());
		return fastUserInfoProxy.queryPageByParamNotEmpty(params, 
				new PageInfo<FastUserInfo>(fastUserInfo.getStartPage(),fastUserInfo.getPageSize())
				).getData();
	}
	
	@RequestMapping(value="save",method=RequestMethod.GET)
	public void save(Model model,Long fastUserId){
		model.addAttribute("warehouselist",queryWarehouse(null));
		model.addAttribute("usertypelist",USER_TYPE.values());
		model.addAttribute("shoptypelist",SHOP_TYPE.values());
		if(fastUserId!=null){
			model.addAttribute("fastUserInfo",fastUserInfoProxy.queryById(fastUserId).getData());
		}else{
			model.addAttribute("fastUserInfo",new FastUserInfo());
		}
	}
	@RequestMapping(value="save",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> save(Model model,FastUserInfo fastUserInfo){
		ResultInfo<Boolean> validateResult = validate(fastUserInfo);
		if(validateResult!=null){
			return validateResult;
		}
		fastUserInfo.setUserName(fastUserInfo.getMobile());
		fastUserInfo.setEnabled(ENABLED.YES);
		fastUserInfo.setUpdateUser(AUTHOR_TYPE.USER_OPERATER+getUserName());
		if(fastUserInfo.getFastUserId()!=null){
			return fastUserInfoProxy.updateNotNullById(fastUserInfo);
		}else{
			fastUserInfo.setCreateUser(fastUserInfo.getUpdateUser());
			return fastUserInfoProxy.insert(fastUserInfo);
		}
	}
	
	@RequestMapping(value="enableduser",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<Integer> enabledUser(Model model,Long fastUserId,Integer enabled){
		FastUserInfo fastUserInfo = new FastUserInfo();
		fastUserInfo.setFastUserId(fastUserId);
		fastUserInfo.setEnabled(enabled);
		return fastUserInfoProxy.updateNotNullById(fastUserInfo);
	}
	
	@RequestMapping(value="querywarehouse",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<List<Warehouse>> queryWarehouse(Model model,Integer shopType){
		return new ResultInfo<List<Warehouse>>(queryWarehouse(shopType));
	}
	
	public List<Warehouse> queryWarehouse(Integer type){
		Map<String,Object> params = new HashMap<String,Object>();
		if(type!=null){
			params.put("type", SHOP_TYPE.getWhType(type));
		}else{
			params.put("type", StorageConstant.StorageType.FAST.value);
		}
		return warehouseProxy.queryByParam(params).getData();
	}
	
	public ResultInfo<Boolean> validate(FastUserInfo fastUserInfo){
		if(StringUtils.isBlank(fastUserInfo.getMobile())){
			return new ResultInfo<Boolean>(new FailInfo("请输入手机号"));
		}
		if(StringUtils.isBlank(fastUserInfo.getUserName())){
			return new ResultInfo<Boolean>(new FailInfo("请输入用户姓名"));
		}
		if(fastUserInfo.getWarehouseId()==null){
			return new ResultInfo<Boolean>(new FailInfo("请选择关联店铺"));
		}
		if(fastUserInfo.getUserType()==null){
			return new ResultInfo<Boolean>(new FailInfo("请选择用户类型"));
		}
		if(fastUserInfo.getShopType()==null){
			return new ResultInfo<Boolean>(new FailInfo("请选择店铺类型"));
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mobile", fastUserInfo.getMobile());
		params.put("userType", fastUserInfo.getUserType());
		params.put("shopType", fastUserInfo.getShopType());
		FastUserInfo userInfo = fastUserInfoProxy.queryUniqueByParams(params).getData();
		if(userInfo!=null && !userInfo.getFastUserId().equals(fastUserInfo.getFastUserId())){
			return new ResultInfo<Boolean>(new FailInfo("手机号不能重复"));
		}
		if(FastConstant.USER_TYPE.MANAGER.code.equals(fastUserInfo.getUserType())){
			params.clear();
			params.put("warehouseId", fastUserInfo.getWarehouseId());
			params.put("userType", FastConstant.USER_TYPE.MANAGER.code);
			userInfo = fastUserInfoProxy.queryUniqueByParams(params).getData();
			if(userInfo!=null){
				if(fastUserInfo==null || !fastUserInfo.getFastUserId().equals(userInfo.getFastUserId())){
					return new ResultInfo<Boolean>(new FailInfo("每个店铺只有一个管理员"));
				}
			}
		}
		return null;
	}
}
