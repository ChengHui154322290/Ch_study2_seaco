package com.tp.service.ord;

import java.util.List;

import com.tp.model.ord.OrderRedeemItem;
import com.tp.query.ord.RedeemItemQuery;
import com.tp.result.ord.OrderRedeemItemStatistics;
import com.tp.service.IBaseService;
/**
  * @author zhouguofeng 
  * 商家线下购买兑换码信息接口
  */
public interface IOrderRedeemItemService extends IBaseService<OrderRedeemItem>{
	/**
	 * 
	 * cancleRedeemInfo:(退单，变更兑换码状态). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param orderCode  
	 * @sinceJDK 1.8
	 */
	public void cancleRedeemInfo(long orderCode);
	/**
	 * 
	 * cancleOverDueRedeemInfo:(过期取消兑换码). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param orderCode  
	 * @sinceJDK 1.8
	 */
	public void cancleOverDueRedeemInfo(long orderCode);
    /**
     * 
     * getOrderRedeemItemByObject:(根据订单ID 查询兑换码信息). <br/>  
     * TODO(这里描述这个方法适用条件 – 可选).<br/>   
     *  
     * @author zhouguofeng  
     * @param orderID
     * @return  
     * @sinceJDK 1.8
     */
	public List<OrderRedeemItem>  getOrderRedeemItemByObject(Long  orderCode);
	/**
	 * 
	 * generateAndSaveRedeemInfo:(根据订单编号获取未使用的金额). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param orderCode Double
	 * @return  
	 * @sinceJDK 1.8
	 */
	public  Double   getUnUseAmountByOrderCode(long orderCode);
	/**
	 * 
	 * generateAndSaveRedeemInfo:(生成兑换码逻辑). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param orderId
	 * @return  
	 * @sinceJDK 1.8
	 */
	public  String   generateAndSaveRedeemInfo(long orderCode);
	
	/**
	 * 
	 * getOverdueOrderRedeemItem:(获取未使用过期的兑换码). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @return  
	 * @sinceJDK 1.8
	 */
	public List<OrderRedeemItem> getOverdueOrderRedeemItem();
	
	/**
	 * 根据参数统计收入
	 * @param redeemItemQuery
	 * @return
	 */
	OrderRedeemItemStatistics queryStatisticsByQuery(RedeemItemQuery redeemItemQuery);
	
	/**
	 * 
	 * getUsedAmountByOrderCode:(已使用的兑换码). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param orderCode
	 * @return  
	 * @sinceJDK 1.8
	 */
	public Double getUsedAmountByOrderCode(long orderCode);
	
	
	/**
	 * 
	 * getUnusedRedeemCodeRateByOrderCode:(获取未使用的比例). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param orderCode
	 * @return  
	 * @sinceJDK 1.8
	 */
	public  Double  getUnusedRateByOrderCode(long orderCode);

}
