package com.tp.backend.controller.bse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.Attribute;
import com.tp.model.bse.AttributeValue;
import com.tp.proxy.bse.AttributeProxy;
import com.tp.proxy.bse.AttributeValueProxy;
import com.tp.proxy.bse.CategoryProxy;

/**
 * @author admin
 * @version 0.0.1
 */
@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping("/basedata/attributeValue")
public class AttributeValueController extends AbstractBaseController {
	 private static final Logger LOGGER = LoggerFactory.getLogger(AttributeValueController.class);
    
	@Autowired
	private AttributeValueProxy attributeValueProxy;
	@Autowired
	private CategoryProxy categoryProxy;
	@Autowired
	private AttributeProxy attributeProxy;
	
	@RequestMapping(value = "/edit")
	public void edit(Long attId,Long cateId, Model model){
		
		AttributeValue attValue=new AttributeValue();
		attValue.setAttributeId(attId);
		//自动生成的编号.
		ResultInfo<List<AttributeValue>> listOfAttributeValue = attributeValueProxy.queryByObject(attValue);
		String nameStr = categoryProxy.getAncestorsNameStr(cateId);
		String attributeName = attributeProxy.selectById(attId).getName();
		model.addAttribute("attValue",attValue);
		model.addAttribute("nameStr",nameStr);
		model.addAttribute("listOfAttributeValue", listOfAttributeValue.getData());
		model.addAttribute("attributeName", attributeName);
		model.addAttribute("attId", attId);
	}
	
	
	@RequestMapping("/saveAttrValue")
	@ResponseBody
	public ResultInfo<?> saveAttrValue(AttributeValue attributeValue){
		if(null==attributeValue){
			LOGGER.info("数据不能为空");
			return new ResultInfo<>(new FailInfo("数据不能为空"));
		}
		return attributeValueProxy.saveAttrValue(attributeValue);
		
	}
	
	
	@RequestMapping("/addAttrValue")
	public void add(@RequestParam(value = "id") Long id,Model model){
		 Attribute attribute = attributeProxy.selectById(id);
		 model.addAttribute("attribute", attribute);
	}
	
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	@ResponseBody
	public  ResultInfo<?> atttrValueEdit(Integer[] status,Long [] ids){
		if(null==ids || ids.length==0){
			return new ResultInfo<>(new FailInfo("没有需要更新的属性项"));
		}
		attributeValueProxy.atttrValueEditSubmit(ids,status);
    	return new ResultInfo<>(Boolean.TRUE);
	}
}
