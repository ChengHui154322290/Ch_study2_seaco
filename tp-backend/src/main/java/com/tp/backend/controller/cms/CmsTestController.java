/**
 * 
 */
package com.tp.backend.controller.cms;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import com.tp.dto.cms.query.ParamSingleBusTemQuery;
import com.tp.proxy.cms.SingleTempleProxy;

/**
 * @version 2015年4月22日
 */
@Controller
@RequestMapping(value = "/cmstest")
public class CmsTestController {

	@Autowired
	private SingleTempleProxy singleTempleProxy;

	@RequestMapping(value = "/load")
	public String load(final ModelMap model, WebRequest request) {
		model.addAttribute("diff", 0);
		model.addAttribute("amount", 0);
		return "cms/test/test";
	}

	@RequestMapping(value = "/todayTopic")
	public String todayTopic(final ModelMap model,WebRequest request) throws Exception {
		Date startTime = new Date();
		
		ParamSingleBusTemQuery paramSingleBusTemQuery = new ParamSingleBusTemQuery();
		
		singleTempleProxy.singleIndexDiscountInfo(paramSingleBusTemQuery);
		
		Date endTime = new Date();
		long diff = endTime.getTime() - startTime.getTime();
		model.addAttribute("diff", diff);
//		model.addAttribute("amount", str);
		return "cms/test/test";
	}
}
