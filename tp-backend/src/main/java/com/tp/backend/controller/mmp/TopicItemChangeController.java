package com.tp.backend.controller.mmp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mmp.TopicItem;
import com.tp.model.mmp.TopicItemChange;
import com.tp.model.stg.Inventory;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.mmp.TopicItemChangeProxy;
import com.tp.proxy.mmp.TopicItemProxy;
import com.tp.proxy.stg.InventoryProxy;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping(value = "/topicItemChange")
public class TopicItemChangeController extends AbstractBaseController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TopicItemChangeProxy itemChangeProxy;

	@Autowired
	private TopicItemProxy topicItemProxy;
	
	
    @Autowired
    private InventoryProxy inventoryProxy;

	/**
	 * 跳转至增加当前库存界面
	 * 
	 * @param topicId
	 * @param topicItemId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/{topicId}/editItem/{topicItemId}/{topicItemChangeId}", method = RequestMethod.GET)
	public String editTopicItemLoad(@PathVariable Long topicId,
			@PathVariable Long topicItemId,
			@PathVariable Long topicItemChangeId, ModelMap model,
			HttpServletRequest request) {
		TopicItem itemDO = topicItemProxy.getTopicItemByItemId(topicItemId);
		ResultInfo<TopicItem> result = topicItemProxy.getTopicItemCurrentStock(itemDO);
		TopicItemChange changeDo = itemChangeProxy.getTopicItemChangeDOById(topicItemChangeId);
		model.addAttribute("topicItemInfo", result.getData());
		model.addAttribute("topicItemChangeId", topicItemChangeId);
		model.addAttribute("changeOrderLimitTotal", changeDo.getLimitTotal());
		model.addAttribute("remainStock",topicItemProxy.getTopicItemRemainStock(result.getData()));
//		return "promotion/topicItemChangeEdit";
		
		
		// 通过sku+仓库id 查询库存记录
		Long stockLocationId = changeDo.getStockLocationId();
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("sku", itemDO.getSku());
        params.put("warehouse_id", stockLocationId);
		ResultInfo<List<Inventory>> queryByParam = inventoryProxy.queryByParam(params );
		List<Inventory> data = queryByParam.getData();
		if(data!=null&&data.size()==1){
			Inventory inventory = data.get(0);
			model.addAttribute("inventory", inventory);
		}else {
			model.addAttribute("inventory", new Inventory());
		}
		return "promotion/topicItemChangeEdit2";
	}

	/**
	 * 增加单个锁定库存商品的库存
	 * 
	 * @param amount
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/editItem", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo editTopicItem(
			@RequestParam("topicItemChangeId") Long topicItemChangeId,
			@RequestParam("amount") int amount, ModelMap model,
			HttpServletRequest request) {

			if (null == topicItemChangeId || 0 == topicItemChangeId) {
				log.error("topic item change order info error.... id:"+ topicItemChangeId);
				return new ResultInfo( "选定专场活动商品无效!");
			}
			UserInfo user = getUserInfo();
			if (amount > 0) {
				return itemChangeProxy.requestAddStock(
						topicItemChangeId, amount, user.getId(),
						user.getUserName());
			} else if (amount < 0) {
				return itemChangeProxy.requestBackStock(
						topicItemChangeId, amount, user.getId(),
						user.getUserName());
			}
			return new ResultInfo();
	}

	@RequestMapping(value = "/getFilterInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo<Map<String,String>> getFilterInfo(@RequestParam("topicId") Long topicId,
			HttpServletRequest request) {

		Map<String, String> filterInfo = itemChangeProxy.getFilterInfo(topicId);
		if (filterInfo == null || filterInfo.size() == 0) {
			return null;
		} else {
			return new ResultInfo<>( filterInfo);
		}

	}
}
