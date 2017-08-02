/**
 * 
 */
package com.tp.dto.ord;

import java.util.List;
import java.util.Map;


/**
 * 购物车DTO
 * 
 * @author szy
 * @version 0.0.1
 * 
 */
public class CartDTO implements BaseDTO {

	private static final long serialVersionUID = 3326347195492828071L;
	
	/** 购物车行列表 */
	private List<CartLineDTO> lineList;
	/** 有效sku列表 */
	private List<String> validateSkuList;
	/** 购物车map 海淘使用 */
	private Map<String, List<CartLineDTO>> seaMap;
	/** 供应商map 海淘使用 */
	private Map<String, String> supplierMap;
	/** 应付金额 */
	private Double originalSum;
	/** 活动未使用优惠券应付金额 */
	private Double topicRealSum;
	/** 实付金额 */
	private Double realSum;
	/** 应付运费 */
	private Double originalFee;
	/** 实付运费 */
	private Double realFee;
	/** 总数量 */
	private Integer quantity;
	/** 购物车商品总数量 */
	private Integer quantityAll;
	/** 购物车总金额 */
	private Double realSumAll;
	/** 预计进口税 */
	private Double taxSumAll;
	/** 购物车标志 */
	private int cartType;
	
	private Long memberId;
	
	private String ip;
	
	private String token;
	
	/**下单平台*/
	private Integer sourceType;
	
	public Boolean firstMinus = Boolean.FALSE;
	
	public Integer getSourceType() {
		return sourceType;
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	public List<CartLineDTO> getLineList() {
		return lineList;
	}
	public void setLineList(List<CartLineDTO> lineList) {
		this.lineList = lineList;
	}
	public Double getOriginalSum() {
		return originalSum;
	}
	public void setOriginalSum(Double originalSum) {
		this.originalSum = originalSum;
	}
	public Double getRealSum() {
		return realSum;
	}
	public void setRealSum(Double realSum) {
		this.realSum = realSum;
	}
	public Double getOriginalFee() {
		return originalFee;
	}
	public void setOriginalFee(Double originalFee) {
		this.originalFee = originalFee;
	}
	public Double getRealFee() {
		return realFee;
	}
	public void setRealFee(Double realFee) {
		this.realFee = realFee;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getTopicRealSum() {
		return topicRealSum;
	}
	public void setTopicRealSum(Double topicRealSum) {
		this.topicRealSum = topicRealSum;
	}
	public Integer getQuantityAll() {
		return quantityAll;
	}
	public void setQuantityAll(Integer quantityAll) {
		this.quantityAll = quantityAll;
	}
	public Double getRealSumAll() {
		return realSumAll;
	}
	public void setRealSumAll(Double realSumAll) {
		this.realSumAll = realSumAll;
	}
	public List<String> getValidateSkuList() {
		return validateSkuList;
	}
	public void setValidateSkuList(List<String> validateSkuList) {
		this.validateSkuList = validateSkuList;
	}
	public Map<String, List<CartLineDTO>> getSeaMap() {
		return seaMap;
	}
	public void setSeaMap(Map<String, List<CartLineDTO>> seaMap) {
		this.seaMap = seaMap;
	}
	public Double getTaxSumAll() {
		return taxSumAll;
	}
	public void setTaxSumAll(Double taxSumAll) {
		this.taxSumAll = taxSumAll;
	}
	public int getCartType() {
		return cartType;
	}
	public void setCartType(int cartType) {
		this.cartType = cartType;
	}
	public Map<String, String> getSupplierMap() {
		return supplierMap;
	}
	public void setSupplierMap(Map<String, String> supplierMap) {
		this.supplierMap = supplierMap;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
