package com.tp.shop.controller.dss;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.PageInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.exception.MobileException;
import com.tp.m.query.promoter.QueryPromoter;
import com.tp.m.util.JsonUtil;
import com.tp.model.dss.WithdrawDetailResponse;
import com.tp.shop.ao.dss.DSSWithdrawAO;
import com.tp.shop.ao.dss.PromoterAO;
import com.tp.shop.helper.AuthHelper;
import com.tp.shop.helper.RequestHelper;

@Controller
public class DSSWithdrawController {
	private static final Logger log = LoggerFactory.getLogger(DSSWithdrawController.class);
	
	@Autowired
	private DSSWithdrawAO dSSWithdrawAO;
	
	@Autowired
	private PromoterAO promoterAO;
	
	/**
	 * 查询提现明细
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/dss/getWithdrawDetail",method = RequestMethod.POST)
	@ResponseBody
	public String getWithdrawDetail(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPromoter promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
			if (log.isInfoEnabled()) {
	            log.info("[API接口 - 用户查询提现明细 入参] = {}", JsonUtil.convertObjToStr(promoter));
	        }
	        Long promoterId = promoterAO.authPromoter(promoter.getToken(), Integer.valueOf(promoter.getType()));
//			Long withdrawor = 4653l;
//			MResultVO<PageInfo<WithdrawDetailResponse>> result = dSSWithdrawAO.queryWithdrawDetail(withdrawor,1);
			MResultVO<PageInfo<WithdrawDetailResponse>> result = dSSWithdrawAO.queryWithdrawDetail(promoterId,Integer.parseInt(promoter.getCurpage()));
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 用户查询提现明细  MobileException] = {}", me.getMessage());
            log.error("[API接口 - 用户查询提现明细 返回值] = {}", JsonUtil.convertObjToStr(new MResultVO<>(me)));
            return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}catch(Exception e) {
            log.error(e.getMessage(), e.getMessage());
			return JsonUtil.convertObjToStr( new MResultVO<>( e.getMessage() ) );
		}   
	}
	
	
}
