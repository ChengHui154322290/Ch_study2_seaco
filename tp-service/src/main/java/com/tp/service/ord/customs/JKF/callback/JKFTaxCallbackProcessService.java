package com.tp.service.ord.customs.JKF.callback;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.customs.JKFConstant.JKFBusinessType;
import com.tp.common.vo.customs.JKFConstant.JKFFeedbackType;
import com.tp.common.vo.customs.JKFConstant.JKFResultError;
import com.tp.model.ord.PersonalgoodsDeclareInfo;
import com.tp.model.ord.PersonalgoodsTaxReceipt;
import com.tp.model.ord.JKF.JkfBaseDO;
import com.tp.model.ord.JKF.JkfCallbackResponse;
import com.tp.model.ord.JKF.JkfTaxIsNeedResult;
import com.tp.model.ord.JKF.JkfTaxIsNeedResult.JkfTaxIsNeedDto;
import com.tp.service.ord.IPersonalgoodsDeclareInfoService;
import com.tp.service.ord.IPersonalgoodsTaxReceiptService;
import com.tp.service.ord.customs.JKF.callback.IJKFClearanceCallbackProcessService;
import com.tp.util.StringUtil;

/**
 * 税单回执 
 */

@Service
public class JKFTaxCallbackProcessService implements IJKFClearanceCallbackProcessService{

	private static final Logger logger = LoggerFactory.getLogger(JKFTaxCallbackProcessService.class);
	
	@Autowired
	private IPersonalgoodsDeclareInfoService personalgoodsDeclareInfoService;
	
	@Autowired
	private IPersonalgoodsTaxReceiptService personalgoodsTaxReceiptService;
	
	@Override
	public boolean checkReceiptType(JKFFeedbackType type) {
		if(JKFFeedbackType.CUSTOMS_TAX_CALLBACK == type){
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public JkfCallbackResponse processCallback(JkfBaseDO receiptResult) {
		JkfTaxIsNeedResult taxResult = (JkfTaxIsNeedResult)receiptResult;
		JkfCallbackResponse response = validateResult(taxResult);
		if (response != null) return response; 
		
		String preEntryNo = taxResult.getBody().getJkfSign().getBusinessNo();
		PersonalgoodsDeclareInfo pgInfo = personalgoodsDeclareInfoService.queryPersonalgoodsDeclareInfoByPreEntryNo(preEntryNo);
		personalgoodsTaxReceiptService.insert(createTaxReceipt(taxResult, pgInfo));
		return new JkfCallbackResponse();
	}
	
	private PersonalgoodsTaxReceipt createTaxReceipt(JkfTaxIsNeedResult taxResult, PersonalgoodsDeclareInfo pgInfo){
		JkfTaxIsNeedDto dto = taxResult.getBody().getJkfTaxIsNeedDto();
		String personalgoodsNo = dto.getPersonalGoodsFormNo();
		String preEntryNo = taxResult.getBody().getJkfSign().getBusinessNo();
		PersonalgoodsTaxReceipt receipt = new PersonalgoodsTaxReceipt();
		receipt.setOrderCode(pgInfo.getOrderCode());
		receipt.setPersonalgoodsNo(personalgoodsNo);
		receipt.setExpressNo(preEntryNo);
		receipt.setIsTax(Integer.parseInt(dto.getIsNeed()));
		receipt.setTaxAmount(Double.valueOf(dto.getTaxAmount()));
		receipt.setCreateTime(new Date());
		return receipt;
	}
	
	private JkfCallbackResponse validateResult(JkfTaxIsNeedResult taxResult){
		String busineType = taxResult.getHead().getBusinessType();
		if (!JKFBusinessType.TAXISNEED.type.equals(busineType)) {
			logger.error("[TAX_CALLBACK][businessType={}]业务类型不正确", busineType);
			return new JkfCallbackResponse(JKFResultError.INVALID_BUSINESS_TYPE);
		}
		String businessNo = taxResult.getBody().getJkfSign().getBusinessNo();
		String personalgoodsNo = taxResult.getBody().getJkfTaxIsNeedDto().getPersonalGoodsFormNo();
		if (StringUtil.isEmpty(businessNo) || StringUtil.isEmpty(personalgoodsNo)) {
			logger.error("[TAX_CALLBACK]业务代码为空");
			return new JkfCallbackResponse(JKFResultError.INVALID_REQUEST_PARAM);
		}
		//业务处理
		PersonalgoodsDeclareInfo pgdecl = personalgoodsDeclareInfoService.queryPersonalgoodsDeclareInfoByPreEntryNo(businessNo);
		if (null == pgdecl) {
			logger.error("[TAX_CALLBACK][businessNO = {}][goodsFormNo={}]原申报清单不存在", businessNo, personalgoodsNo);
			return new JkfCallbackResponse();
		}
		return null;
	}

}
