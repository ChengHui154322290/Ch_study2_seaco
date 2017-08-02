package com.tp.m.ao.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.util.ptm.EncryptionUtil;
import com.tp.common.vo.OrderConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.m.query.order.YiQiFaQueryOrder;
import com.tp.m.util.StringUtil;
import com.tp.proxy.ord.OrderChannelTrackProxy;
import com.tp.util.DateUtil;

@Service
public class YiQiFaOrderAO {

	private static final Logger logger = LoggerFactory.getLogger(YiQiFaOrderAO.class);
	@Autowired
	private OrderChannelTrackProxy orderChannelProxy;
	@Value("#{meta['yiqifa.queryip']}")
	private String yiqifaQueryIp;
	@Value("#{meta['yiqifa.token']}")
	private String token;
	
	public String queryOrderListByChannelYiQiFaParams(YiQiFaQueryOrder queryOrder){
		String d = queryOrder.getD();
		String ud = queryOrder.getUd();
		String os = queryOrder.getOs();
		String encryptionSource = queryOrder.getCid()+
				                  (d!=null?d:"")+
				                  (ud!=null?ud:"")+
				                  (os!=null?os:"") + 
				                  token;
		String encryptionSign = EncryptionUtil.encrptMD5(EncryptionUtil.encrptMD5(encryptionSource)+token);
		if(!encryptionSign.equals(queryOrder.getSign())){
			return "签名不正确";
		}
		if(StringUtil.isNotBlank(yiqifaQueryIp) && !Arrays.asList(yiqifaQueryIp.split(",")).contains(queryOrder.getRequestIp())){
			return "请求IP地址非法";	
		}
		if(StringUtil.isBlank(queryOrder.getCid())){
			return "没有标识";
		}
		if(StringUtil.isBlank(queryOrder.getD()) && StringUtil.isBlank(queryOrder.getUd())){
			return "没有查询时间段";
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("clientCode", queryOrder.getCid());
		if(StringUtil.isNotBlank(queryOrder.getD())){
			Date createDate = DateUtil.parse(queryOrder.getD(), "yyyyMMdd");
			if(createDate==null){
				return "下单时间格式不正确，正确格式：yyyyMMdd";
			}
			params.put("beginCreateTime", createDate);
			params.put("endCreateTime", DateUtil.addDays(createDate, 1));
		}
		if(StringUtil.isNotBlank(queryOrder.getUd())){
			Date updateDate = DateUtil.parse(queryOrder.getUd(), "yyyyMMdd");
			if(updateDate==null){
				return "订单更新时间格式不正确，正确格式：yyyyMMdd";
			}
			params.put("beginUpdateTime", updateDate);
			params.put("endUpdateTime", DateUtil.addDays(updateDate, 1));
		}
		
		if(StringUtil.isNotBlank(queryOrder.getOs())){
			params.put("orderStatus", OrderConstant.ORDER_STATUS.getCode(queryOrder.getOs()));
		}
		
		try{
			ResultInfo<String> result = orderChannelProxy.queryOrderListByChannelYiQiFaParams(params);
			if(result.success){
				return result.data;
			}else{
				return result.getMsg().getMessage();
			}
		}catch(Exception e){
			ExceptionUtils.println(new FailInfo("亿起发查询订单列表出错"), logger, queryOrder,e.getMessage());
			return "亿起发查询订单列表出错";
		}
	}
}
