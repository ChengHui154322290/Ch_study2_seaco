/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.m.controller.order;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.tp.dto.ord.kuaidi100.ExpressInfo;
import com.tp.dto.ord.kuaidi100.ExpressResult;
import com.tp.m.ao.order.Kuaidi100CallbackAO;
/**
 * <pre>
 * 快递100回调controller
 * </pre>
 * @time 2015-2-3 上午11:20:06
 */
@Controller
@RequestMapping(value = "/kuaidi100")
public class Kuaidi100CallbackController {

	private static final Logger log = LoggerFactory.getLogger(Kuaidi100CallbackController.class);

	@Autowired
	private Kuaidi100CallbackAO kuaidi100CallbackAO;

	@RequestMapping(value = "/gainExpress/{code}", method = {RequestMethod.POST, RequestMethod.GET})
	public void gainExpressInfo(@PathVariable String code, HttpServletRequest request, HttpServletResponse response) {
		ExpressResult result = null;
		try {
			response.setCharacterEncoding("UTF-8");
			if (StringUtils.isNotEmpty(code)) {
				/** param="{'status':'polling','billstatus':'sending','message':'正在派件','lastResult':{'message':'ok','nu':'100216625719','ischeck':'0','condition':'H100','com':'yuantong','status':'200','state':'5','data':[{'time':'2015-02-04 07:37:06','ftime':'2015-02-04 07:37:06','context':'上海市杨浦区五角场公司 派件中 操作员：派件人: 张俊粉'},{'time':'2015-02-04 07:04:37','ftime':'2015-02-04 07:04:37','context':'上海市杨浦区五角场公司 已收入 操作员：谭发娥'},{'time':'2015-02-03 21:23:52','ftime':'2015-02-03 21:23:52','context':'上海转运中心公司 已发出 操作员：黄东赛'},{'time':'2015-02-03 21:17:24','ftime':'2015-02-03 21:17:24','context':'上海转运中心公司 已拆包 操作员：卜茫茫'},{'time':'2015-02-02 21:41:55','ftime':'2015-02-02 21:41:55','context':'河南省郑州市十八里河公司 已打包 操作员：赵扎根'},{'time':'2015-02-02 20:33:57','ftime':'2015-02-02 20:33:57','context':'河南省郑州市十八里河公司 已收件 操作员：取件人: 聂高然'}]}}"; */
				String param = request.getParameter("param");
				log.info("[回调接口 - 快递100推送运单记录信息] = {}", param);
				if (StringUtils.isNotBlank(param)) {
					ExpressInfo expressInfo = JSONObject.parseObject(param, ExpressInfo.class);
					result = kuaidi100CallbackAO.saveExpressInfo(code, expressInfo);
				} else {
					result = new ExpressResult(false, "500", "处理失败");
				}
			}else {
				result = new ExpressResult(false, "500", "请求链接错误");
			}
		} catch (Exception e) {
			log.error("[回调接口 - 处理获取快递100平台推送的运单数据异常] = {}", e.getMessage());
			result = new ExpressResult(false, "500", "处理异常" + e.getMessage());
		}
		/** 保存失败，服务端等30分钟会重复推送。 */
		try {
			response.getWriter().print(JSONObject.toJSONString(result));
		} catch (IOException e1) {
			log.error("[回调接口 -返回快递100平台推送结果处理信息时IO异常] = {}", e1.getMessage());
		}
	}
}
