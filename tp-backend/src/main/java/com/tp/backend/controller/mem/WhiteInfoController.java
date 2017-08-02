package com.tp.backend.controller.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.mem.WhiteConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.ConsigneeAddressKVDTO;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.WhiteInfo;
import com.tp.proxy.mem.WhiteInfoProxy;
import com.tp.util.BeanUtil;

/**
 * 白名单管理
 * @author zhuss
 * @2016年4月06日 下午1:23:44
 */
@Controller
@RequestMapping("/mem/white/")
public class WhiteInfoController extends AbstractBaseController {
	
	private  Logger logger = LoggerFactory.getLogger(WhiteInfoController.class);
	
	@Autowired
	private WhiteInfoProxy whilteInfoProxy;
	
	
	/**
	 * 白名单信息列表
	 * @param model
	 * @param promoterInfo
	 */
	@RequestMapping("list")
	public void list(Model model,WhiteInfo whiteInfo){
		model.addAttribute("whiteInfo", whiteInfo);
		model.addAttribute("statusList", WhiteConstant.STATUS.values());
		model.addAttribute("levelList", WhiteConstant.LEVEL.values());
		Map<String, Object> param = BeanUtil.beanMap(whiteInfo);
		List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
		param.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		param.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "create_time desc");
		PageInfo<WhiteInfo> resultInfo =whilteInfoProxy.queryPageByParam(param,new PageInfo<WhiteInfo>(whiteInfo.getStartPage(),whiteInfo.getPageSize())).getData();
		model.addAttribute("page", resultInfo);
	}
	
	/**
	 * 跳转编辑页
	 * @param model
	 * @param id
	 */
	@RequestMapping(value="save",method=RequestMethod.GET)
	public void save(Model model,Long id){
		ResultInfo<WhiteInfo> whiteInfo = null;
		if(null==id){
			whiteInfo = new ResultInfo<WhiteInfo>(new WhiteInfo());
		}else{
			whiteInfo = whilteInfoProxy.queryById(id);
			String mobile = whiteInfo.getData().getMobile();
			ResultInfo<List<ConsigneeAddressKVDTO>> addressList = whilteInfoProxy.addresslist(mobile);
			model.addAttribute("addressList", addressList.getData());
		}
		model.addAttribute("resultInfo", whiteInfo);
		model.addAttribute("levelList", WhiteConstant.LEVEL.values());
	}
	
	/**
	 * 保存白名单记录
	 * @param model
	 * @param promoterInfo
	 * @return
	 */
	@RequestMapping(value="save",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> save(Model model,WhiteInfo whiteInfo){
		try{
			whiteInfo.setModifyTime(new Date());
			whiteInfo.setModifyUser(super.getUserName());
			if(null != whiteInfo.getAddressId() && whiteInfo.getAddressId() < 1){
				whiteInfo.setConsigneeAddress("");
			}
			return whilteInfoProxy.save(whiteInfo);
		}catch(UserServiceException ase){
			logger.error("[白名单管理 - 保存白名单用户 AppServiceException] ={}",ase.getMessage()); 
			return new ResultInfo<>(new FailInfo(ase.getMessage()));
		}catch(Exception e){
			logger.error("[白名单管理 - 保存白名单用户 AppServiceException] ={}",e); 
			return new ResultInfo<>(new FailInfo("保存失败"));
		}
	}
	
	/**
	 * 修改状态
	 * @return
	 */
	@RequestMapping("uptstatus")
	@ResponseBody
	public ResultInfo<Integer> updateVersionStatus(Model model,Long id,Integer status){
		if(null == id || null == status) return new ResultInfo<>(new FailInfo("参数异常,必选字段为空"));
		WhiteInfo whiteInfo = new WhiteInfo();
		whiteInfo.setId(id);
		whiteInfo.setStatus(status);
		whiteInfo.setModifyTime(new Date());
		whiteInfo.setModifyUser(super.getUserName());
		return whilteInfoProxy.updateNotNullById(whiteInfo);
	}
	
	/**
	 * 白名单信息列表
	 * @param model
	 * @param promoterInfo
	 */
	@RequestMapping(value = "addresslist",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<List<ConsigneeAddressKVDTO>> addresslist(String mobile){
		return whilteInfoProxy.addresslist(mobile);
	}
}
