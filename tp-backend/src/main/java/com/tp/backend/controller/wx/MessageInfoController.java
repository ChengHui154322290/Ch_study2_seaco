package com.tp.backend.controller.wx;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.wx.MessageConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.AppServiceException;
import com.tp.model.wx.MessageInfo;
import com.tp.proxy.wx.MenuInfoProxy;
import com.tp.proxy.wx.MessageInfoProxy;
import com.tp.util.BeanUtil;
/**
 * 微信消息控制器
 * @author zhuss
 */
@Controller
@RequestMapping("/wx/message/")
public class MessageInfoController {

	private  Logger logger = LoggerFactory.getLogger(MessageInfoController.class);
	
	@Autowired
	private MessageInfoProxy messageInfoProxy;
	
	@Autowired
	private MenuInfoProxy menuInfoProxy;
	
	/**
	 * 消息列表
	 * @param model
	 * @param promoterInfo
	 */
	@RequestMapping("list")
	public void list(Model model,MessageInfo messageInfo){
		model.addAttribute("messageInfo", messageInfo);
		model.addAttribute("sceneList", MessageConstant.SCENE.values());
		model.addAttribute("typeList", MessageConstant.TYPE.values());
		model.addAttribute("statusList", MessageConstant.STATUS.values());
		Map<String, Object> params = BeanUtil.beanMap(messageInfo);
		if( params.containsKey("name")){
        	params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " name like '%"+params.get("name")+"%'");  
    		params.remove("name");
    	}
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
		PageInfo<MessageInfo> resutlInfo =messageInfoProxy.queryPageByParam(params,new PageInfo<MessageInfo>(messageInfo.getStartPage(),messageInfo.getPageSize())).getData();
		model.addAttribute("page", resutlInfo);
	}
	
	/**
	 * 跳转消息编辑页
	 * @param model
	 * @param id
	 */
	@RequestMapping(value="save",method=RequestMethod.GET)
	public void save(Model model,Long id){
		ResultInfo<MessageInfo> messageInfo = null;
		if(null==id){
			messageInfo = new ResultInfo<MessageInfo>(new MessageInfo());
		}else{
			messageInfo = messageInfoProxy.queryById(id);
		}
		model.addAttribute("resultInfo", messageInfo);
		model.addAttribute("sceneList", MessageConstant.SCENE.values());
		model.addAttribute("typeList", MessageConstant.TYPE.values());
		model.addAttribute("statusList", MessageConstant.STATUS.values());
		model.addAttribute("menuList",menuInfoProxy.getKVMenu());
	}
	
	/**
	 * 保存消息记录
	 * @param model
	 * @param promoterInfo
	 * @return
	 */
	@RequestMapping(value="save",method=RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> save(MessageInfo messageInfo){
		try{
			return messageInfoProxy.saveMessage(messageInfo);
		}catch(AppServiceException ase){
			logger.error("[微信 - 保存发送消息 AppServiceException] ={}",ase.getMessage()); 
			return new ResultInfo<>(new FailInfo(ase.getMessage()));
		}
	}
	
	/**
	 * 修改状态
	 * @return
	 */
	@RequestMapping("uptstatus")
	@ResponseBody
	public ResultInfo<Integer> updateStatus(Model model,Integer id,Integer status){
		if(null == id || null == status) return new ResultInfo<>(new FailInfo("参数异常,必选字段为空"));
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setId(id);
		messageInfo.setStatus(status);
		messageInfo.setModifyTime(new Date());
		return messageInfoProxy.updateStatus(messageInfo);
	}
}
