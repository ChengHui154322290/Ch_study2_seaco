package com.tp.proxy.pay;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.common.vo.ord.OrderCodeType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.seagoorpay.SeagoorPayRefundStatus;
import com.tp.model.mmp.PointDetail;
import com.tp.model.pay.SeagoorPayRefundInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IPointDetailService;
import com.tp.service.ord.IOrderCodeGeneratorService;
import com.tp.service.pay.ISeagoorPayRefundInfoService;
import com.tp.util.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 代理层
 *
 * @author szy
 */
@Service
public class SeagoorPayRefundInfoProxy extends BaseProxy<SeagoorPayRefundInfo> {

    @Autowired
    private ISeagoorPayRefundInfoService seagoorPayRefundInfoService;

    @Autowired
    private IPointDetailService pointDetailService;

    @Autowired
    private IOrderCodeGeneratorService orderCodeGeneratorService;


    @Override
    public IBaseService<SeagoorPayRefundInfo> getService() {
        return seagoorPayRefundInfoService;
    }

    public ResultInfo<SeagoorPayRefundInfo> refund(SeagoorPayRefundInfo info) {
        ResultInfo<SeagoorPayRefundInfo> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {

                Date cur = new Date();
                info.setCreateTime(cur);
                info.setUpdateTime(cur);
                info.setRefundCode(orderCodeGeneratorService.generate(OrderCodeType.SEAGOOR_PAY_REFUND).toString());
                info.setStatus(SeagoorPayRefundStatus.PROCESSING.getCode());
                SeagoorPayRefundInfo seagoorPayRefundInfo = seagoorPayRefundInfoService.insert(info);

                PointDetail pointDetailQuery = new PointDetail();
                pointDetailQuery.setBizType(PointConstant.BIZ_TYPE.SEAGOOR_PAY.code);
                pointDetailQuery.setBizId(seagoorPayRefundInfo.getPaymentCode());
                pointDetailQuery.setMemberId(seagoorPayRefundInfo.getMemberId());
                pointDetailQuery.setPointType(PointConstant.OPERATE_TYPE.MINUS.type);
                System.out.println(JsonUtil.convertObjToStr(pointDetailQuery));
                PointDetail pointDetail = pointDetailService.queryUniqueByObject(pointDetailQuery);
                pointDetail.setPoint(seagoorPayRefundInfo.getRefundFee());
                pointDetail.setRelateBizType(PointConstant.BIZ_TYPE.SEAGOOR_PAY.code);
                pointDetail.setBizType(PointConstant.BIZ_TYPE.SEAGOOR_PAY_REFUND.code);
                pointDetail.setTitle(PointConstant.BIZ_TYPE.SEAGOOR_PAY_REFUND.title);
                System.out.println("refund detail:" + JsonUtil.convertObjToStr(pointDetail));
                ResultInfo<Integer> pointResult = pointDetailService.updatePointByRefund(pointDetail);
                logger.info("SEAGOOR_PAY_REFUND_POINT_RESULT="+JsonUtil.convertObjToStr(pointResult));
                if (!pointResult.isSuccess()) {
                    seagoorPayRefundInfo.setRemark(pointResult.getMsg().getMessage());
                    seagoorPayRefundInfo.setStatus(SeagoorPayRefundStatus.FAIL.getCode());
                    seagoorPayRefundInfoService.updateById(seagoorPayRefundInfo);
                    result.setMsg(pointResult.getMsg());
                } else if (pointResult.getData() == null || pointResult.getData() == 0) {
                    seagoorPayRefundInfo.setRemark("更新0条计分记录,退款失败");
                    seagoorPayRefundInfo.setStatus(SeagoorPayRefundStatus.FAIL.getCode());
                    seagoorPayRefundInfoService.updateById(seagoorPayRefundInfo);
                    result.setMsg(new FailInfo("退款失败", 5320));
                } else {
                    seagoorPayRefundInfo.setStatus(SeagoorPayRefundStatus.SUCCESS.getCode());
                    seagoorPayRefundInfoService.updateById(seagoorPayRefundInfo);
                    result.setData(seagoorPayRefundInfo);
                }


            }
        });
        return result;
    }

    public ResultInfo<PageInfo<SeagoorPayRefundInfo>> queryRefundList(Map<String, Object> param) {
        ResultInfo<PageInfo<SeagoorPayRefundInfo>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                result.setData(seagoorPayRefundInfoService.queryByParamsForPage(param));

            }
        });
        return result;
    }

}
