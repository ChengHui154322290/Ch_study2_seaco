package com.tp.backend.controller.dss;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.GlobalCommision;
import com.tp.proxy.dss.GlobalCommisionProxy;

/**
 * 分销佣金比率设置
 * @author szy
 *
 */
@Controller
@RequestMapping("/dss/globalcommision/")
public class GlobalCommisionManageController extends AbstractBaseController {

	@Autowired
	private GlobalCommisionProxy globalCommisionProxy;
	
	/**
	 * 获取全局佣金比率
	 * @param model
	 */
	@RequestMapping("index")
	public void index(Model model){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " id desc");
		model.addAttribute("resultInfo",globalCommisionProxy.queryLastGlobalCommision());
		model.addAttribute("commisionList", globalCommisionProxy.queryByParam(params).getData());
	}
	
	/**
	 * 设置新的全局比率
	 * @param model
	 * @param globalCommision
	 * @return
	 */
	@RequestMapping("insert")
	@ResponseBody
	public ResultInfo<GlobalCommision> insert(Model model,GlobalCommision globalCommision){
		globalCommision.setCreateUser(super.getUserName()+AUTHOR_TYPE.USER_OPERATER);
		return globalCommisionProxy.insert(globalCommision);
	}
}
