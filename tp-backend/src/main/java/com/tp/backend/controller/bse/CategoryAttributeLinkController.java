package com.tp.backend.controller.bse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.proxy.bse.CategoryProxy;
import com.tp.result.bse.CategoryResult;


@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping(value="/basedata/categoryAttributeLink")
public class CategoryAttributeLinkController  extends AbstractBaseController{
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(CategoryAttributeLinkController.class);
	
	@Autowired
	private CategoryProxy categoryProxy;
	

	/**
	 * 跳转 到 categoryAttribute list页面. 
	 * @param model
	 * @param categoryId
	 */
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public void list(Long categoryId, Model model) {
		String ansNameStr=categoryProxy.getAncestorsNameStr(categoryId);
		CategoryResult attAndValues=categoryProxy.selectAttrsAndValuesByCatId(categoryId);
		
		model.addAttribute("ansNameStr",ansNameStr);
		model.addAttribute("attAndValues",attAndValues);

	}
}
