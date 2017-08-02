package com.tp.service.ord;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dao.ord.OrderChannelTrackDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.ord.remote.YiQiFaOrderDto;
import com.tp.model.ord.OrderChannelTrack;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.prd.ItemSku;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderChannelTrackService;
import com.tp.service.pay.IPaymentGatewayService;
import com.tp.service.prd.IItemSkuService;
import com.tp.util.HttpClientUtil;
import com.tp.util.StringUtil;

@Service
public class OrderChannelTrackService extends BaseService<OrderChannelTrack> implements IOrderChannelTrackService {

	private static final String ENCODE="UTF-8", EQUE = "=",AND="&";
	@Autowired
	private OrderChannelTrackDao orderChannelTrackDao;
	@Autowired
	private IItemSkuService itemSkuService;
	@Autowired
	private IPaymentGatewayService paymentGatewayService;
	@Value("#{meta['yiqifa.pushorder.url']}")
	private String yiqifaorderurl;
	
	
	@Override
	public BaseDao<OrderChannelTrack> getDao() {
		return orderChannelTrackDao;
	}
	
	public List<YiQiFaOrderDto> queryOrderListByYiQiFa(Map<String,Object> params){
		List<YiQiFaOrderDto> list = orderChannelTrackDao.queryOrderListByYiQiFa(params);
		if(CollectionUtils.isNotEmpty(list)){
			Set<String> skuCodeList =new HashSet<String>();
			for(YiQiFaOrderDto order:list){
				skuCodeList.add(order.getPn());
			}
			//暂时没有问题
			List<ItemSku> itemSkuList = itemSkuService.selectSkuListBySkuCode(new ArrayList<String>(skuCodeList));
			for(YiQiFaOrderDto order:list){
				for(ItemSku itemSku:itemSkuList){
					if(order.getPn().equals(itemSku.getSku())){
						order.setCt(ItemConstant.COMMISION_TYPE.getCode(itemSku.getCommisionType()));
					}
				}
			}
		}
		return list;
	}
	
	public void pushOrderByChannelYiQiFaParentOrderCode(Long parentOrderCode){
		List<String> paramStringList;
		try {
			paramStringList = queryOrderListByChannelYiQiFaParentOrderCode(parentOrderCode);
			if(CollectionUtils.isNotEmpty(paramStringList)){
				for(String urlParam:paramStringList){
					String result = HttpClientUtil.getData(yiqifaorderurl+"?"+urlParam, "UTF-8");
					logger.info("[{}]订单实时推送亿起发返回结果:{},参数:{}",parentOrderCode,result,urlParam);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| UnsupportedEncodingException e) {
			ExceptionUtils.println(new FailInfo("推送到亿起发出错"+e.getMessage()), logger, parentOrderCode,e);
		} catch (Exception e) {
			ExceptionUtils.println(new FailInfo("推送到亿起发出错 EXCEPTION"), logger, parentOrderCode,e.getMessage());
		}
		
	}
	
	public List<String> queryOrderListByChannelYiQiFaParentOrderCode(Long parentOrderCode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, UnsupportedEncodingException{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderCode", parentOrderCode);
		List<YiQiFaOrderDto> list = queryOrderListByChannelYiQiFa(params);
		List<String> orderStringList = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(list)){
			for(YiQiFaOrderDto order:list){
				order.setPna(encode(order.getPna()));
				order.setCt(encode(order.getCt()));
				order.setTa(encode(order.getTa()));
				order.setSd(encode(order.getSd()));
				order.setOs(encode(order.getOs()));
				order.setPs(encode(order.getPs()));
				order.setPw(encode(order.getPw()));
				order.setFac(encode(order.getFac()));
				order.setPn(encode(order.getPn()));
				order.setPp(encode(order.getPp()));
				orderStringList.add(order.getUrlParam());
			}
		}
    	return orderStringList;
    }
	
	public String queryOrderListByChannelYiQiFaParams(Map<String,Object> params){
		List<YiQiFaOrderDto> list = queryOrderListByChannelYiQiFa(params);
		StringBuffer orderListString = new StringBuffer();
		if(CollectionUtils.isNotEmpty(list)){
			for(YiQiFaOrderDto order:list){
				orderListString.append(order.toString()+"\n");
			}
		}
    	return orderListString.toString();
    }
	
	public List<YiQiFaOrderDto> queryOrderListByChannelYiQiFa(Map<String,Object> params){
    	params.put("channelCode", "0118");
    	List<YiQiFaOrderDto> yiQiFaOrderDto = queryOrderListByYiQiFa(params);
    	yiQiFaOrderDto = initYiQiFaOrderDtoBySubOrder(yiQiFaOrderDto);
    	return yiQiFaOrderDto;
    }
    	
    private List<YiQiFaOrderDto> initYiQiFaOrderDtoBySubOrder(final List<YiQiFaOrderDto> yiQiFaOrderDtoList){
    	Map<String,List<YiQiFaOrderDto>> orderMap = new HashMap<String,List<YiQiFaOrderDto>>();
    	if(CollectionUtils.isNotEmpty(yiQiFaOrderDtoList)){
    		List<PaymentGateway> gatewayList = paymentGatewayService.queryByParam(new HashMap<String,Object>());
    		yiQiFaOrderDtoList.forEach(new Consumer<YiQiFaOrderDto>(){
				public void accept(YiQiFaOrderDto t) {
					t.setFac(t.getFac().replaceAll(",", "|"));
					if(t.getFac().endsWith("|")){
						t.setFac(t.getFac().substring(0,t.getFac().length()-1));
					}
					if(t.getFac().startsWith("|")){
						t.setFac(t.getFac().replaceFirst("\\|", ""));
					}
					t.setPna(t.getPna().replaceAll("\\|", "｜"));
					List<YiQiFaOrderDto> list = orderMap.get(t.getOn());
					if(list==null){
						list = new ArrayList<YiQiFaOrderDto>();
					}
					list.add(t);
					orderMap.put(t.getOn(), list);
				}
    		});
    		List<YiQiFaOrderDto> encodeList = new ArrayList<YiQiFaOrderDto>();
    		orderMap.forEach(new BiConsumer<String,List<YiQiFaOrderDto>>(){
				public void accept(String t, List<YiQiFaOrderDto> u) {
					YiQiFaOrderDto firstOrder = u.get(0);
					if(u.size()>1){
						String SPACEBAR = Constant.SPLIT_SIGN.SPACEBAR;
						for(int i=1;i<u.size();i++){
							YiQiFaOrderDto order = u.get(i);
							firstOrder.setPn(firstOrder.getPn()+SPACEBAR+order.getPn());
							firstOrder.setPna(firstOrder.getPna()+SPACEBAR+order.getPna());
							firstOrder.setCt(firstOrder.getCt()+SPACEBAR+order.getCt());
							firstOrder.setTa(firstOrder.getTa()+SPACEBAR+order.getTa());
							firstOrder.setPp(firstOrder.getPp()+SPACEBAR+order.getPp());
							u.remove(i);
							i--;
						}
					}
					firstOrder.setPs(PaymentConstant.PAYMENT_STATUS.NO_PAY.cnName);
					if(Integer.valueOf(firstOrder.getOs())>OrderConstant.ORDER_STATUS.PAYMENT.code){
						firstOrder.setPs(PaymentConstant.PAYMENT_STATUS.PAYED.cnName);
						for(PaymentGateway gateway:gatewayList){
							if(gateway.getGatewayId().toString().equals(firstOrder.getPw())){
								firstOrder.setPw(gateway.getGatewayName());
							}
						}
					}
					firstOrder.setOs(OrderConstant.ORDER_STATUS.getCnName(Integer.valueOf(firstOrder.getOs())));
					if(StringUtil.isBlank(firstOrder.getFav())){
						firstOrder.setFav("0.00");
					}
					encodeList.add(firstOrder);
				}
    		});
    		return encodeList;
    	}
    	return yiQiFaOrderDtoList;
    }
    private String encode(String str){
    	if(str==null)str="";
    	try {
			return URLEncoder.encode(str, ENCODE);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
    }
}
