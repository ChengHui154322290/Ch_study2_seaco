package com.tp.backend.controller.pay;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.seagoorpay.SeagoorPayRefundStatus;
import com.tp.dto.seagoorpay.SeagoorPayStatus;
import com.tp.dto.seagoorpay.forbackend.SeagoorPayInfoQuery;
import com.tp.model.mem.MemberInfo;
import com.tp.model.pay.MerchantInfo;
import com.tp.model.pay.SeagoorPayInfo;
import com.tp.model.pay.SeagoorPayRefundInfo;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.proxy.pay.MerchantInfoProxy;
import com.tp.proxy.pay.SeagoorPayInfoProxy;
import com.tp.proxy.pay.SeagoorPayRefundInfoProxy;
import com.tp.util.DateUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by ldr on 2016/12/1.
 */

@Controller
@RequestMapping("/seagoorpay")
public class SeagoorPayInfoController extends AbstractBaseController {

    @Autowired
    private SeagoorPayInfoProxy seagoorPayInfoProxy;

    @Autowired
    private SeagoorPayRefundInfoProxy seagoorPayRefundInfoProxy;

    @Autowired
    private MerchantInfoProxy merchantInfoProxy;

    @Autowired
    private MemberInfoProxy memberInfoProxy;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/payinfo")
    public String payInfo(SeagoorPayInfoQuery query,Model model,String startTime,String endTime) throws Exception {
        List<MerchantInfo> list = getMerchantInfos();
        model.addAttribute("mlist",list);
        Map<String,Object>  param = new HashMap<>();
        if(query != null){

            ss(query);
            if(query.getMemberId() != null){
                param.put("memberId",query.getMemberId());
            }
            if(query.getStatus()!=null){
                param.put("status",query.getStatus());
            }
            if(StringUtils.isNotBlank(query.getMerchantId())){
                param.put("merchantId",query.getMerchantId());
            }
            if(StringUtils.isNotBlank(query.getPaymentCode())){
                param.put("paymentCode",query.getPaymentCode().trim());
            }
            if(StringUtils.isNotBlank(query.getMerPayCode())){
                param.put("merTradeCode",query.getMerPayCode().trim());
            }
            List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
            query.setStartTime(DateUtil.parse(startTime,DateUtil.NEW_FORMAT));
            query.setEndTime(DateUtil.parse(endTime,DateUtil.NEW_FORMAT));
            if(query.getStartTime()!=null){
                whereList.add(new DAOConstant.WHERE_ENTRY("create_time", DAOConstant.MYBATIS_SPECIAL_STRING.GT,query.getStartTime()));
            }
            if(query.getEndTime()!=null){
                whereList.add(new DAOConstant.WHERE_ENTRY("create_time", DAOConstant.MYBATIS_SPECIAL_STRING.LT,query.getEndTime()));
            }
            param.put(DAOConstant.MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
            param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(),"id desc");



        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(query.getPage() == null ? 1:query.getPage());
        pageInfo.setSize(query.getSize() == null ? 10: query.getSize());
        ResultInfo<PageInfo<SeagoorPayInfo>> result = seagoorPayInfoProxy.queryPageByParam(param,pageInfo);
        model.addAttribute("result",result);
        model.addAttribute("query",query);
        model.addAttribute("paysStatusValues", SeagoorPayStatus.values());
        return "seagoorpay/payInfo";

    }

    private List<MerchantInfo> getMerchantInfos() {
        ResultInfo<List<MerchantInfo>> resultInfo = merchantInfoProxy.queryByParam(new HashMap<>());
        return resultInfo.getData();
    }


    @RequestMapping("/refundinfo")
    public String refundInfo (SeagoorPayInfoQuery query,Model model,String startTime,String endTime){
        List<MerchantInfo> list = getMerchantInfos();
        model.addAttribute("mlist",list);
        Map<String,Object>  param = new HashMap<>();
        if(query != null){

            ss(query);
            if(query.getMemberId() != null){
                param.put("memberId",query.getMemberId());
            }
            if(query.getStatus()!=null){
                param.put("status",query.getStatus());
            }
            if(StringUtils.isNotBlank(query.getMerchantId())){
                param.put("merchantId",query.getMerchantId().trim());
            }
            if(StringUtils.isNotBlank(query.getPaymentCode())){
                param.put("paymentCode",query.getPaymentCode().trim());
            }
            if(StringUtils.isNotBlank(query.getMerPayCode())){
                param.put("merTradeCode",query.getMerPayCode().trim());
            }

            if(StringUtils.isNotBlank(query.getRefundCode())){
                param.put("refundCode",query.getRefundCode().trim());
            }

            if(StringUtils.isNotBlank(query.getMerRefundCode())){
                param.put("merRefundCode",query.getMerRefundCode().trim());
            }

            List<DAOConstant.WHERE_ENTRY> whereList = new ArrayList<DAOConstant.WHERE_ENTRY>();
            query.setStartTime(DateUtil.parse(startTime,DateUtil.NEW_FORMAT));
            query.setEndTime(DateUtil.parse(endTime,DateUtil.NEW_FORMAT));
            if(query.getStartTime()!=null){
                whereList.add(new DAOConstant.WHERE_ENTRY("create_time", DAOConstant.MYBATIS_SPECIAL_STRING.GT,query.getStartTime()));
            }
            if(query.getEndTime()!=null){
                whereList.add(new DAOConstant.WHERE_ENTRY("create_time", DAOConstant.MYBATIS_SPECIAL_STRING.LT,query.getEndTime()));
            }
            param.put(DAOConstant.MYBATIS_SPECIAL_STRING.WHERE.name(), whereList);
            param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(),"id desc");



        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(query.getPage() == null ? 1:query.getPage());
        pageInfo.setSize(query.getSize() == null ? 10: query.getSize());
        ResultInfo<PageInfo<SeagoorPayRefundInfo>> result = seagoorPayRefundInfoProxy.queryPageByParam(param,pageInfo);
        model.addAttribute("result",result);
        model.addAttribute("query",query);
        model.addAttribute("paysStatusValues", SeagoorPayRefundStatus.values());

        return  "seagoorpay/refundInfo";
    }

    private void ss(SeagoorPayInfoQuery query) {
        if(StringUtils.isNotBlank(query.getMemberMobile())){
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setMobile(query.getMemberMobile());
            ResultInfo<MemberInfo> memberInfoResultInfo = memberInfoProxy.queryUniqueByObject(memberInfo);
            if(memberInfoResultInfo.getData() !=null){
                query.setMemberId(memberInfoResultInfo.getData().getId());
            }
        }
    }


}
