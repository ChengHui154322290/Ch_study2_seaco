package com.tp.backend.controller.bse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.ClearanceChannels;
import com.tp.proxy.bse.ClearanceChannelsProxy;

@Controller
@RequestMapping("/basedata/clearanceChannels")
public class ClearanceChannelsController extends AbstractBaseController {

	 private static final Logger LOG = LoggerFactory.getLogger(ClearanceChannelsController.class);


	@Autowired
	private ClearanceChannelsProxy clearanceChannelsProxy;

	/**
	 * 通关渠道列表
	 * @param model
	 * @param clearanceChannels
	 */
	@RequestMapping(value = "/list")
	public void list(Model model, ClearanceChannels clearanceChannels) throws Exception {
		if (null==clearanceChannels ) {
			clearanceChannels = new ClearanceChannels();
		}
		ResultInfo<List<ClearanceChannels>> listOfClearanceChannels = clearanceChannelsProxy.queryByObject(clearanceChannels);
		model.addAttribute("listOfClearanceChannels", listOfClearanceChannels.getData());
	}

	/**
	 *   通关渠道编辑
	 * @param model
	 * @param 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/edit")
	public void edit(@RequestParam(value = "id") Long id, Model model) throws Exception {
		if (null==id ) {
			throw new Exception("id为空,异常");
		}
		ResultInfo<ClearanceChannels> clearanceChannelResultInfo = clearanceChannelsProxy.queryById(id);
		model.addAttribute("clearanceChannel", clearanceChannelResultInfo.getData());
	}

	@RequestMapping(value = "/add")
	public void addColor(Model model) {
	}

	/**
	 * 税率增加
	 * @param
	 */
	@RequestMapping(value = "/addClearanceChannelSubmit", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> addClearanceChannelSubmit(ClearanceChannels clearanceChannels) throws Exception {
		if (null==clearanceChannels) {
			LOG.info("数据不能为空");
			throw new Exception("数据不能为空");
		}
		return clearanceChannelsProxy.addClearanceChannel(clearanceChannels);
	}

	/**
	 * 更新 通关渠道
	 * @param
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> updateClearanceChannel(ClearanceChannels clearanceChannels) throws Exception {
		if (null==clearanceChannels ) {
			LOG.info("数据不能为空");
			throw new Exception("数据不能为空");
		}
		
		return clearanceChannelsProxy.updateClearanceChannel(clearanceChannels, false);

	}
}
