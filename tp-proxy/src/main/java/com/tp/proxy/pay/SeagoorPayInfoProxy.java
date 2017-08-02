package com.tp.proxy.pay;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.common.vo.ord.OrderCodeType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.seagoorpay.SeagoorPayStatus;
import com.tp.model.mmp.PointDetail;
import com.tp.model.pay.SeagoorPayInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IPointDetailService;
import com.tp.service.ord.IOrderCodeGeneratorService;
import com.tp.service.pay.ISeagoorPayInfoService;
import com.tp.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 代理层
 * @author szy
 *
 */
@Service
public class SeagoorPayInfoProxy extends BaseProxy<SeagoorPayInfo>{

	@Autowired
	private ISeagoorPayInfoService seagoorPayInfoService;

	@Autowired
	private IPointDetailService pointDetailService;

	@Autowired
	private IOrderCodeGeneratorService orderCodeGeneratorService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public IBaseService<SeagoorPayInfo> getService() {
		return seagoorPayInfoService;
	}

	public ResultInfo<SeagoorPayInfo> pay(SeagoorPayInfo info){
		ResultInfo<SeagoorPayInfo> result = new ResultInfo<>();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				Date cur = new Date();
				info.setCreateTime(cur);
				info.setUpdateTime(cur);
				info.setStatus(SeagoorPayStatus.NOT_PAY.getCode());
				Long code = orderCodeGeneratorService.generate(OrderCodeType.SEAGOOR_PAY);
				info.setPaymentCode(code.toString());

				SeagoorPayInfo seagoorPayInfo =seagoorPayInfoService.insert(info);

				PointDetail pointDetail = new PointDetail();
				pointDetail.setBizId(code.toString());
				pointDetail.setBizType(PointConstant.BIZ_TYPE.SEAGOOR_PAY.code);
				pointDetail.setCreateUser(info.getMemberId().toString());
				pointDetail.setPoint(info.getTotalFee());
				pointDetail.setMemberId(info.getMemberId());
				pointDetail.setTitle(PointConstant.BIZ_TYPE.SEAGOOR_PAY.title);
				pointDetail.setPointType(PointConstant.OPERATE_TYPE.MINUS.type);
				Integer i = pointDetailService.updatePointByMemberUsed(pointDetail);
				if(i == 0){
					logger.error("SEAGOOR_PAY_ERROR_BALANCE_NOT_ENOUGH.PARAM={}",JsonUtil.convertObjToStr(info));
					result.setMsg(new FailInfo("用户余额不足",5310));
					return ;
				}
				seagoorPayInfo.setStatus(SeagoorPayStatus.SUCCESS.getCode());

				seagoorPayInfoService.updateById(seagoorPayInfo);
				result.setData(seagoorPayInfo);
			}
		});

		return result;
	}


	public ResultInfo<PageInfo<SeagoorPayInfo>> queryByparamForPage(Map<String,Object> params){
		ResultInfo<PageInfo<SeagoorPayInfo>> result = new ResultInfo<>();
		this.execute(result, new Callback() {
			@Override
			public void process() throws Exception {
				result.setData(seagoorPayInfoService.queryByParamForPage(params));
			}
		});
		return result;
	}

}
