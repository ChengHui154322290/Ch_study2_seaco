package com.tp.backend.controller.mmp.coupon;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.util.mmp.DataUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.TF;
import com.tp.common.vo.mmp.CouponConstant;
import com.tp.common.vo.mmp.ExchangeCodeConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.model.mmp.ExchangeCouponChannel;
import com.tp.model.mmp.ExchangeCouponChannelCode;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.mmp.ExchangeCouponChannelCodeProxy;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

@Controller
@RequestMapping("/topicCoupon")
public class ExchangeCouponCodeController extends AbstractBaseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExchangeCouponChannelCodeProxy exchangeCouponChannelCodeProxy;


    @InitBinder
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
        binder.registerCustomEditor(Date.class, dateEditor);
    }

    /**
     * 静态活动列表管理的展示页面
     *
     * @param
     * @return 页面
     * @throws Exception
     */
    @RequestMapping(value = {"/listStaticActivity"}, method = RequestMethod.GET)
    public String listStaticActivity(Model model, String activityInfo) {
        ExchangeCouponChannel query = new ExchangeCouponChannel();
        if (activityInfo != null) {
            JSONObject jSONObject = new JSONObject();
            jSONObject = (JSONObject) JSONValue.parse(activityInfo);
            if (!DataUtil.isNvl(jSONObject.get("actNameBak"))) {
                query.setActName(jSONObject.get("actNameBak").toString());
            }
        }

        PageInfo<ExchangeCouponChannel> cmsActListDO = new PageInfo<ExchangeCouponChannel>();
        try {
            cmsActListDO = exchangeCouponChannelCodeProxy.queryExchangeCodeList(query);
        } catch (Exception e) {
            logger.error("兑换码活动列表查询报错", e);
            e.printStackTrace();
        }
        if (cmsActListDO != null) {
            model.addAttribute("pageList", cmsActListDO);
            model.addAttribute("query", query);
        }

        return "coupon/exchangeCodeList";
    }

    /**
     * 静态活动列表管理的编辑页面
     *
     * @param
     * @return 页面
     * @throws Exception
     */
    @RequestMapping(value = {"/addexchangecode"}, method = RequestMethod.GET)
    public String addStaticActivity(Model model, String activityInfo) {
        return "coupon/exchangeCodeAdd";
    }

    /**
     * 生成兑换码
     *
     * @param
     * @return 主键
     * @throws Exception
     */
    @RequestMapping(value = {"/addExchangeCode.htm"}, method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addExchangeCode(Model model, HttpServletRequest request, HttpServletResponse response) {
        if (DataUtil.isNvl(request.getParameter("actId"))) {
            return new ResultInfo(new FailInfo("请先提交"));
        }

        String actId = request.getParameter("actId");
        String couponId = request.getParameter("couponId");
        String num = request.getParameter("currentnum");

        ExchangeCouponChannelCode mode = new ExchangeCouponChannelCode();
        mode.setActId(Long.parseLong(actId));
        mode.setCouponId(Long.parseLong(couponId));
        mode.setStatus(0);

        UserInfo user = getUserInfo();
        if (null != user) {
            mode.setCreateUser(user.getUserName());
        } else {
            mode.setCreateUser("");
        }
        mode.setCreateTime(new Date());

        ResultInfo resultInfo = exchangeCouponChannelCodeProxy.insertExcode(mode, Integer.parseInt(num));

        return resultInfo;
    }

    /**
     * 导出
     *
     * @param request
     * @param response
     * @throws InvalidFormatException
     * @throws IOException
     */
    @RequestMapping("/export")
    public void exportExcel(HttpServletRequest request,
                            HttpServletResponse response)
            throws InvalidFormatException, IOException, ParseException {
        Map<String, Object> params = new HashMap<>();
        String actId = request.getParameter("actId");
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");
        String status = request.getParameter("status");
        Date startTime = null;
        Date endTime = null;
        if (StringUtils.isNotEmpty(startTimeStr)) {
            startTime = DateUtil.parseDate(startTimeStr, DateUtil.NEW_FORMAT);
        }
        if (StringUtils.isNotEmpty(endTimeStr)) {
            endTime = DateUtil.parseDate(endTimeStr, DateUtil.NEW_FORMAT);
        }
        params.put("actiId", actId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("status", status);
        List<ExchangeCouponChannelCode> list = exchangeCouponChannelCodeProxy.getExchangeCodeByTimeAndStatus(params);
        request.setCharacterEncoding("UTF-8");
        exchangeCouponChannelCodeProxy.export(request, response, "", list);
    }

    /**
     * 兑换码活动列表查询
     *
     * @param
     * @return 页面
     * @throws Exception
     */
    @RequestMapping(value = {"/queryExchangeCodeList"}, method = RequestMethod.POST)
    public String queryExchangeCodeList(Model model,
                                        ExchangeCouponChannel query) {
        if (query == null) {
            query = new ExchangeCouponChannel();
        }

        PageInfo<ExchangeCouponChannel> cmsActListDO = new PageInfo<ExchangeCouponChannel>();
        try {
            cmsActListDO = exchangeCouponChannelCodeProxy.queryExchangeCodeList(query);
        } catch (Exception e) {
            logger.error("兑换码活动列表查询报错", e);
            e.printStackTrace();
        }
        if (cmsActListDO != null) {
            model.addAttribute("pageList", cmsActListDO);
            model.addAttribute("query", query);
        }
        return "coupon/exchangeCodeList";
    }
    
    /**
     * 兑换码活动列表查询
     *
     * @param
     * @return 页面
     * @throws Exception
     */
    @RequestMapping(value = {"/listExchangeCodeDetail"})
    public String listExchangeCodeDetail(Model model,ExchangeCouponChannelCode query,Integer decode) {
        if(TF.YES.equals(decode) && StringUtil.isNotBlank(query.getPromoterName())){
        	try {
        		query.setPromoterName(new String(query.getPromoterName().getBytes("ISO8859-1"),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
        }
        model.addAttribute("pageList", exchangeCouponChannelCodeProxy.queryExchangeCouponByParam(query).getData());
        model.addAttribute("query", query);
        model.addAttribute("useStatusList", CouponUserStatus.values());
        return "coupon/listExchangeCodeDetail";
    }
    
    /**
     * 绑定卡券到推广员
     * @param model
     * @param query
     * @param refreshBind
     * @return
     */
    @RequestMapping(value = {"/updatepromoteridbind"})
    @ResponseBody
    public ResultInfo<Integer> updatePromoterIdBind(Model model,ExchangeCouponChannelCode query,Integer refreshBind){
    	if(query.getCouponId()==null){
    		return new ResultInfo<>(new FailInfo("请选择卡券批次"));
    	}
    	if(query.getPromoterId()==null){
    		return new ResultInfo<>(new FailInfo("请选择要绑定的卡券推广员"));
    	}
    	if(CollectionUtils.isEmpty(query.getCouponCodeIdList()) && query.getBeginCodeSeq()==null && query.getEndCodeSeq()==null){
    		return new ResultInfo<>(new FailInfo("请选择要绑定批次的序列号区间或卡券"));
    	}
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("beginCodeSeq", query.getBeginCodeSeq());
    	params.put("endCodeSeq", query.getEndCodeSeq());
    	params.put("couponId", query.getCouponId());
    	params.put("bindUser", Constant.AUTHOR_TYPE.USER_OPERATER+super.getUserName());
    	if(!CollectionUtils.isEmpty(query.getCodeSeqList())){
    		params.put("codeSeqList", query.getCodeSeqList());
    	}
    	if(!CollectionUtils.isEmpty(query.getCouponCodeIdList())){
    		params.put("couponCodeIdList", query.getCouponCodeIdList());
    	}
    	params.put("promoterId", query.getPromoterId());
    	if(null!=refreshBind && TF.YES.equals(refreshBind)){
    		params.put("refreshBind", refreshBind);
    	}
    	return exchangeCouponChannelCodeProxy.updatePromoterIdBind(params);
    }
    /**
     * 绑定卡券到推广员
     * @param model
     * @param query
     * @param refreshBind
     * @return
     */
    @RequestMapping(value = {"/disabledpromoterid"})
    @ResponseBody
    public ResultInfo<Integer> disabledpromoterid(Model model,ExchangeCouponChannelCode query,Integer enabled){
    	if(query.getCouponId()==null){
    		return new ResultInfo<>(new FailInfo("请选择卡券批次"));
    	}
    	if(CollectionUtils.isEmpty(query.getCouponCodeIdList()) && query.getBeginCodeSeq()==null && query.getEndCodeSeq()==null){
    		return new ResultInfo<>(new FailInfo("请选择要封存批次的序列号区间或卡券"));
    	}
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("beginCodeSeq", query.getBeginCodeSeq());
    	params.put("endCodeSeq", query.getEndCodeSeq());
    	params.put("couponId", query.getCouponId());
    	params.put("bindUser", Constant.AUTHOR_TYPE.USER_OPERATER+super.getUserName());
    	if(!CollectionUtils.isEmpty(query.getCodeSeqList())){
    		params.put("codeSeqList", query.getCodeSeqList());
    	}
    	if(!CollectionUtils.isEmpty(query.getCouponCodeIdList())){
    		params.put("couponCodeIdList", query.getCouponCodeIdList());
    	}
    	if(!TF.YES.equals(enabled)){
    		params.put("status", ExchangeCodeConstant.STATUS_SEALED);
    	}else{
    		params.put("status", ExchangeCodeConstant.STATUS_EXCHANGE);
    	}
    	return exchangeCouponChannelCodeProxy.disabledPromoterId(params);
    }
    /**
     * 兑换码活动的终止操作
     *
     * @param
     * @return 主键
     * @throws Exception
     */
    @RequestMapping(value = {"/stopActExchangeCode.htm"}, method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo stopActExchangeCode(Model model, String params) {
        try {

            if (params != null) {
                JSONObject jSONObject = new JSONObject();
                jSONObject = (JSONObject) JSONValue.parse(params);
                Long id = Long.parseLong(jSONObject.get("id").toString());
                int counts = exchangeCouponChannelCodeProxy.stopActExchangeCode(id);
                return new ResultInfo(counts);
            }
        } catch (Exception e) {
            logger.error("兑换码活动的终止操作出错", e);
            return new ResultInfo(new FailInfo(e.getMessage()));
        }
        return new ResultInfo();
    }

    /**
     * 兑换码活动的创建和修改页面
     *
     * @param consigneeAddressDO
     * @return 页面
     * @throws Exception
     * @throws NumberFormatException
     */
    @RequestMapping(value = {"/editActExchangeCode.htm"}, method = RequestMethod.POST)
    public String editActExchangeCode(Model model, String params) {
        if (params != null) {
            JSONObject jSONObject = new JSONObject();
            Object obj = JSONValue.parse(params);
            jSONObject = (JSONObject) obj;

            if (!DataUtil.isNvl(jSONObject.get("id"))) {
                ExchangeCouponChannel sd = new ExchangeCouponChannel();
                try {
                    sd = exchangeCouponChannelCodeProxy.getActExchangeCodeById(Long.parseLong(jSONObject.get("id").toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("兑换码活动的创建和修改页面的查询出错", e);
                }
                model.addAttribute("detail", sd);
            }


            if (!DataUtil.isNvl(jSONObject.get("actNameBak"))) {
                String actName = jSONObject.get("actNameBak").toString();
                model.addAttribute("actNameBak", actName);
            }
        }
        return "coupon/exchangeCodeAdd";
    }

    /**
     * 保存兑换码数据
     *
     * @param
     * @return 主键
     * @throws Exception
     */
    @RequestMapping(value = {"/saveActExchangeCode.htm"}, method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveActExchangeCode(Model model,
                                          HttpServletRequest request, HttpServletResponse response) {
        String actId = request.getParameter("actId");
        String actName = request.getParameter("actName");
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");
        String channel = request.getParameter("channel");
        String type = request.getParameter("type");
        String num = request.getParameter("num");
        String useNum = request.getParameter("useNum");
        UserInfo user = getUserInfo();
        Date cur = new Date();
        ExchangeCouponChannel mode = new ExchangeCouponChannel();
        mode.setCreateUser(user.getUserName());
        mode.setCreateTime(cur);
        mode.setUpdateUser(user.getUserName());
        mode.setUpdateTime(cur);
        mode.setActName(actName);
        if (!DataUtil.isNvl(channel)) {
            mode.setChannel(Integer.parseInt(channel));
        }
        if (!DataUtil.isNvl(type)) {
            mode.setType(Integer.parseInt(type));
        }

        try {
            if (!DataUtil.isNvl(startdate)) {
                mode.setStartDate(DateUtil.parse(startdate, DateUtil.NEW_FORMAT));
            }
            if (!DataUtil.isNvl(enddate)) {
                mode.setEndDate(DateUtil.parseDate(enddate, DateUtil.NEW_FORMAT));
            }
        } catch (ParseException e1) {
            logger.error("转换时间出错", e1);
            return new ResultInfo(new FailInfo("转换时间出错"));
        }

        if (!DataUtil.isNvl(num)) {
            mode.setNum(Integer.parseInt(num));
        }
        mode.setStatus(0);

        try {
            if (DataUtil.isNvl(actId)) {
                if (!DataUtil.isNvl(useNum)) {//只有新增的时候需要设置
                    mode.setUseNum(Integer.parseInt(useNum));
                }
                exchangeCouponChannelCodeProxy.insertExcode(mode);
            } else {
                mode.setId(Long.parseLong(actId));
                exchangeCouponChannelCodeProxy.updateExcode(mode);
            }
        } catch (Exception e) {
            logger.error("生成兑换码出错", e);
            return new ResultInfo(new FailInfo("生成兑换码出错"));
        }

        return new ResultInfo();
    }

    @RequestMapping("/exChangeCodeSel")
    public String exChangeCodeSel(String actiId, Model model) {
        model.addAttribute("actiId", actiId);
        return "coupon/exchangeCodeSel";
    }

    @RequestMapping("exchangeCodeDetail")
    public String exchangeCodeDetail(Long actId,Model model){
        List<Map<String, String>>  result = exchangeCouponChannelCodeProxy.queryExchangeCountDetails(actId);
        model.addAttribute("details",result);

        return "coupon/listExchageCodeDetail";

    }
    
    @RequestMapping("cancleCodeDetail")
    public String cancleCodeDetail(ExchangeCouponChannelCode query,Model model){
    	Map<String,Object> params = new HashMap<String,Object>();
    	if(!CollectionUtils.isEmpty(query.getCouponCodeIdList())){
    		params.put("couponCodeIdList", query.getCouponCodeIdList());
    		List<ExchangeCouponChannelCode>  cancleExchangeList  =exchangeCouponChannelCodeProxy.queryExchangeCouponByParams(params);
    	   String cancleExchangeCodes="";
    	   String cancleExchangeIds="";
    		for(int i=0,len=cancleExchangeList.size();i<len;i++){
    			if(i==0){
    				cancleExchangeCodes=cancleExchangeList.get(i).getExchangeCode();
    				cancleExchangeIds=cancleExchangeList.get(i).getId().toString();
    			}else{
    				cancleExchangeCodes=cancleExchangeCodes+","+cancleExchangeList.get(i).getExchangeCode();
    				cancleExchangeIds=cancleExchangeIds+","+cancleExchangeList.get(i).getId();
    			}
    	    }
    		model.addAttribute("cancleExchangeCodes",cancleExchangeCodes);
    		model.addAttribute("cancleExchangeIds",cancleExchangeIds);
    	}
    	model.addAttribute("couponCodeIdList",query.getCouponCodeIdList());
        return "coupon/cancleExchangeCode";

    }
    
    
    /**
     * 作废优化券
     * @param model
     * @param query
     * @param refreshBind
     * @return
     */
    @RequestMapping(value = {"/cancleCouponPromoterid"})
    @ResponseBody
    public ResultInfo<Integer> cancleCouponPromoterid(Model model,ExchangeCouponChannelCode query,Integer enabled){
    	Map<String,Object> params = new HashMap<String,Object>();
    	if(!CollectionUtils.isEmpty(query.getCouponCodeIdList())){
    		params.put("couponCodeIdList", query.getCouponCodeIdList());
    	}
       params.put("cancelReason", query.getCancelReason());//作废原因
       params.put("status", ExchangeCodeConstant.STATUS_CANCLE);//作废
    	return exchangeCouponChannelCodeProxy.cancledPromoterId(params);
    }
}
