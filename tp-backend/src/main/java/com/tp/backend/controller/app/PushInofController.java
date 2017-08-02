package com.tp.backend.controller.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.app.PushConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.AppServiceException;
import com.tp.model.app.PushInfo;
import com.tp.proxy.app.PushInfoProxy;
import com.tp.util.BeanUtil;

/**
 * APP推送消息
 * @author zhuss
 * @2016年2月24日 下午5:33:17
 */
@Controller
@RequestMapping("/app/push/")
public class PushInofController extends AbstractBaseController {
	
	private  Logger logger = LoggerFactory.getLogger(PushInofController.class);
	
	@Autowired
	private PushInfoProxy pushInfoProxy;
	
	/**
	 * 推送消息列表
	 * @param model
	 * @param promoterInfo
	 */
	@RequestMapping("list")
	public void list(Model model,PushInfo pushInfo,Integer size,Integer page){
		if(page!=null)pushInfo.setStartPage(page);
		if(size!=null)pushInfo.setPageSize(size);
		model.addAttribute("pushInfo", pushInfo);
		model.addAttribute("size", size);
		model.addAttribute("page", page);
		model.addAttribute("pushStatusList", PushConstant.PUSH_STATUS.values());
		model.addAttribute("platformList", PushConstant.PUSH_PLATFORM.values());
		model.addAttribute("pushTargetList", PushConstant.PUSH_TARGET.values());
		model.addAttribute("pushTypeList", PushConstant.PUSH_TYPE.values());
		model.addAttribute("pushWayList", PushConstant.PUSH_WAY.values());
		Map<String, Object> param = BeanUtil.beanMap(pushInfo);
		if(MapUtils.isNotEmpty(param))pushInfo.remove(param);
		List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
		if(pushInfo.getStartTime()!=null){
			whereList.add(new DAOConstant.WHERE_ENTRY("send_date",MYBATIS_SPECIAL_STRING.GT,pushInfo.getStartTime()));
		}
		if(pushInfo.getEndTime()!=null){
			whereList.add(new DAOConstant.WHERE_ENTRY("send_date",MYBATIS_SPECIAL_STRING.LT,pushInfo.getEndTime()));
		}
		param.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name()," push_status asc,create_date desc");
		param.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
		PageInfo<PushInfo> resutlInfo =pushInfoProxy.queryPageByParam(param,new PageInfo<PushInfo>(pushInfo.getStartPage(),pushInfo.getPageSize())).getData();
		model.addAttribute("page", resutlInfo);
	}
	
	/**
	 * 跳转推送消息编辑页
	 * @param model
	 * @param id
	 */
	@RequestMapping(value="save",method=RequestMethod.GET)
	public void save(Model model,Long id){
		ResultInfo<PushInfo> pushInfo = null;
		if(null==id){
			pushInfo = new ResultInfo<PushInfo>(new PushInfo());
		}else{
			pushInfo = pushInfoProxy.queryById(id);
		}
		model.addAttribute("resultInfo", pushInfo);
		model.addAttribute("platformList", PushConstant.PUSH_PLATFORM.values());
		model.addAttribute("pushTypeList", PushConstant.PUSH_TYPE.values());
		model.addAttribute("pushTargetList", PushConstant.PUSH_TARGET.values());
	}
	
	/**
	 * 保存推送消息记录
	 * @param model
	 * @param promoterInfo
	 * @return
	 */
	@RequestMapping(value="save",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> save(PushInfo pushInfo){
		try{
			if(pushInfo.getId()==null){
				pushInfo.validate(pushInfo);
				pushInfo.setCreateDate(new Date());
				pushInfo.setCreateUser(super.getUserName());
				pushInfo.setPushStatus(PushConstant.PUSH_STATUS.NO.getCode());
				if(null == pushInfo.getActiveTime())pushInfo.setActiveTime(2);
				pushInfo.setIsDel(0);
				pushInfo.setNetType(0);
				return pushInfoProxy.insert(pushInfo);
			}else{
				pushInfo.setModifyDate(new Date());
				pushInfo.setModifyUser(super.getUserName());
				return pushInfoProxy.updateNotNullById(pushInfo);
			}
		}catch(AppServiceException ase){
			logger.error("[APP推送 - 保存发送消息 AppServiceException] ={}",ase.getMessage()); 
			return new ResultInfo<>(new FailInfo(ase.getMessage()));
		}
	}
	
	 /**
	  * 发送推送消息
	  * @param pushInfo
	  * @return
	  */
	@RequestMapping(value = "/send")
	@ResponseBody
	public ResultInfo<String> send(PushInfo pushInfo){
		try{
			if(pushInfo.getId()==null){
				pushInfo.validate(pushInfo);
				pushInfo.setCreateDate(new Date());
				pushInfo.setCreateUser(super.getUserName());
				pushInfo.setPushStatus(PushConstant.PUSH_STATUS.NO.getCode());
				pushInfo.setNetType(0);
				if(null == pushInfo.getActiveTime())pushInfo.setActiveTime(2);
				pushInfo.setIsDel(0);
			}else{
				pushInfo.setModifyDate(new Date());
				pushInfo.setModifyUser(super.getUserName());
			}
			return pushInfoProxy.sendMessage(pushInfo);
		}catch(AppServiceException ase){
			logger.error("[APP推送 - 发送消息 AppServiceException] ={}",ase.getMessage()); 
			return new ResultInfo<>(new FailInfo(ase.getMessage()));
		}
	}
	
	public void initSubBinder(ServletRequestDataBinder binder){ 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
