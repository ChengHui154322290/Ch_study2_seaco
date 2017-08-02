package com.tp.service.mmp;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.formatToPrice;
import static com.tp.util.BigDecimalUtil.multiply;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.util.mmp.MathUtils;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.dao.mmp.CouponDao;
import com.tp.dao.mmp.CouponRangeDao;
import com.tp.dao.mmp.CouponUserDao;
import com.tp.dao.mmp.FreightTemplateDao;
import com.tp.dao.mmp.TopicDao;
import com.tp.dao.mmp.TopicInfoDao;
import com.tp.dao.mmp.TopicItemDao;
import com.tp.dto.mmp.CartCouponDTO;
import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.mmp.enums.CouponType;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.SeaOrderItemDTO;
import com.tp.dto.ord.SeaOrderItemWithSupplierDTO;
import com.tp.dto.ord.SeaOrderItemWithWarehouseDTO;
import com.tp.exception.ServiceException;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponRange;
import com.tp.model.mmp.CouponUser;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicItem;
import com.tp.service.mmp.IPriceService;
import com.tp.util.BigDecimalUtil;

@Service
public class PriceService implements IPriceService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static Double FIRST_FREE_AMOUNT = 5.00D;
    
    private final static Double CUSTOMS_RATE_LIMIT=2000.00D;
    private final static Float RATE_DISCOUNT = 0.7f;
    //@Autowired
    //private TopicItemDao topicItemDao;

    @Autowired
    private CouponUserDao couponUserDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private FreightTemplateDao freightTemplateDao;

    @Autowired
    private CouponRangeDao couponRangeDao;

    @Autowired
    private TopicInfoDao topicInfoDao;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private TopicItemDao topicItemDao;


    @Override
    public CartDTO cartTotalPrice(CartDTO cartDTO) throws Exception {
        try {
            if (cartDTO != null) {
                BigDecimal totalPrice = BigDecimal.ZERO;
                List<CartLineDTO> lineList = cartDTO.getLineList();
                for (CartLineDTO cartLine : lineList) {
                    String sku = cartLine.getSkuCode();
                    long topicId = cartLine.getTopicId();
                    int quantity = cartLine.getQuantity();

                    TopicItem promotionItemDO = new TopicItem();
                    promotionItemDO.setSku(sku);
                    promotionItemDO.setTopicId(topicId);
                    List<TopicItem> pitemList = topicItemDao.queryByObject(promotionItemDO);
                    if (!pitemList.isEmpty() && pitemList.size() > 0) {
                        TopicItem pitemDO = pitemList.get(0);
                        Double topicPrice = pitemDO.getTopicPrice();
                        totalPrice = totalPrice.add(new BigDecimal(topicPrice.toString())
                                .multiply(new BigDecimal(quantity)));
                    }
                }// end for
                cartDTO.setRealSum(totalPrice.doubleValue());
                return cartDTO;
            } else {// end if
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public SeaOrderItemDTO hitaoOrderTotalPrice(SeaOrderItemDTO orderDTO, List<Long> couponUserIdList) throws Exception {
        return hitaoOrderTotalPriceWithCoupon(orderDTO, couponUserIdList).getSeaOrderItemDTO();
    }

    @Override
    public CartCouponDTO hitaoOrderTotalPriceWithCoupon(SeaOrderItemDTO orderDTO, List<Long> couponUserIdList) throws Exception {
        CartCouponDTO cartCouponDTO = new CartCouponDTO();
        Long memberId = orderDTO.getMemberId();
        AssertUtil.notNull(memberId, "用户Id为空");
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal orignalTotalPrice = BigDecimal.ZERO;
        BigDecimal remainMoneyPack = BigDecimal.ZERO;
        int totalQuantity = 0;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalFreightFee = BigDecimal.ZERO;
        List<CartLineDTO> allCartLineList = new ArrayList<CartLineDTO>();
        List<SeaOrderItemWithSupplierDTO> supplierList = orderDTO.getSeaOrderItemWithSupplierList();
        if (supplierList != null && supplierList.size() > 0) {
            for (SeaOrderItemWithSupplierDTO supplierDTO : supplierList) {
                BigDecimal supplierTotalPrice = BigDecimal.ZERO;
                BigDecimal supplierTotalTax = BigDecimal.ZERO;
                BigDecimal supplierTotalFreightFee = BigDecimal.ZERO;
                Long supplierId = supplierDTO.getSupplierId();
                Long supplierFrightId = supplierDTO.getFreightTempleteId();
                FreightTemplate supplierFright = freightTemplateDao.queryById(supplierFrightId);
                if (supplierFright == null)
                    throw new Exception("运费模板为空");
                Float postage = supplierFright.getPostage();//运费额
                Float freePostage = supplierFright.getFreePostage();//满额免运费
                int frightType = supplierFright.getFreightType();

                List<SeaOrderItemWithWarehouseDTO> warehouseDTOList = supplierDTO.getSeaOrderItemWithWarehouseList();
                if (warehouseDTOList != null && warehouseDTOList.size() > 0) {//按照仓库分
                    for (SeaOrderItemWithWarehouseDTO warehouseDTO : warehouseDTOList) {
                        Long warehouseId = warehouseDTO.getWarehouseId();
                        Integer storageType = warehouseDTO.getStorageType();
                        String seaChannelCode = warehouseDTO.getSeaChannelCode();

                        List<CartLineDTO> cartLineList = warehouseDTO.getCartLineList();
                        BigDecimal warehouseTotalPrice = BigDecimal.ZERO;
                        BigDecimal warehouseTotalTax = BigDecimal.ZERO;
                        BigDecimal warehouseTotalFrightFee = BigDecimal.ZERO; //TODO 计算运费
                        int itemTotalQuantity = 0;
                        int warehouesTotalCount = 0;
                        if (cartLineList != null && cartLineList.size() > 0) {
                            for (CartLineDTO cartLine : cartLineList) {
                            	if (cartLine.getWavesSign()==2 && storageType != OrderConstant.OrderType.DOMESTIC.getCode().intValue()) { //国内直发 ，免税费，但是要显示，所以不加入总价
                                    if (!ClearanceChannelsEnum.HWZY.name().equals(seaChannelCode)) {//海外直邮，免税费，但是要显示，也不加入总价
                                    	Double taxRate = (new BigDecimal(cartLine.getAddedValueRate()).add(new BigDecimal(cartLine.getExciseRate()))
		                			             .divide(new BigDecimal(100).subtract(new BigDecimal(cartLine.getExciseRate())),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100))).doubleValue();
                                    	if(formatToPrice(multiply(cartLine.getSalePrice(),cartLine.getQuantity())).doubleValue()>=CUSTOMS_RATE_LIMIT){
                                    		cartLine.setTarrifRate(add(taxRate,cartLine.getCustomsRate()).doubleValue());
                                    	}else{
                                    		cartLine.setTarrifRate(multiply(taxRate,RATE_DISCOUNT).doubleValue());
                                    	}
                                    }
                                }
                                String sku = cartLine.getSkuCode();
                                long topicId = cartLine.getTopicId();
                                int quantity = cartLine.getQuantity();

                                Topic topic = topicDao.queryById(topicId);
                                TopicItem promotionItemDO = new TopicItem();
                                promotionItemDO.setSku(sku);
                                promotionItemDO.setTopicId(topicId);
                                promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
                                List<TopicItem> pitemList = topicItemDao.queryByObject(promotionItemDO);
                                if (pitemList != null && pitemList.size() > 0) {
                                    TopicItem pitemDO = pitemList.get(0);
                                    TopicItemInfoDTO result = new TopicItemInfoDTO();
                                    result.setStockAmount(pitemDO.getStockAmount());
                                    result.setLimitAmount(pitemDO.getLimitAmount());
                                    result.setTopicPrice(pitemDO.getTopicPrice());
                                    result.setTopicImage(pitemDO.getTopicImage());

                                    result.setTopicId(topicId);
                                    result.setTopicStatus(topic.getStatus());// 专题专题
                                    result.setLastingType(topic.getLastingType());// 专题持续类型
                                    result.setStartTime(topic.getStartTime());
                                    result.setEndTime(topic.getEndTime());
                                    //设置活动是否预占库存:0否1是（5.4）
                                    result.setTopicInventoryFlag(topic.getReserveInventoryFlag());
                                    
                                    cartLine.setTopicItemInfo(result);
                                    cartLine.setWarehouseId(warehouseId);
                                    if (cartLine.getSelected()) {
                                        Double topicPrice = pitemDO.getTopicPrice();
                                        Double orignalPrice = cartLine.getListPrice();
                                        // 入参非空判断
                                        BigDecimal linePrice = new BigDecimal(topicPrice.toString()).multiply(new BigDecimal(quantity));
                                        BigDecimal orgPrice = new BigDecimal(orignalPrice.toString());
                                        BigDecimal lineOrignalPrice = orgPrice.multiply(new BigDecimal(quantity));
                                        BigDecimal lineTax = linePrice.multiply(new BigDecimal(cartLine.getTarrifRate()));
                                        // 传入税费都是整数，需要除以100 四舍五入
                                        lineTax = lineTax.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                                        if (logger.isDebugEnabled()) {
                                            logger.info("[hitaoOrderTotalPriceWithCoupon]haitao tax fee sku:" + pitemDO.getSku() + "..rate:" + cartLine.getTarrifRate() + "..fee:" + lineTax);
                                        }
                                        cartLine.setSubTotal(linePrice.doubleValue());
                                        cartLine.setTaxfFee(lineTax.doubleValue());
                                        warehouseTotalPrice = warehouseTotalPrice.add(linePrice);
                                        warehouseTotalTax = warehouseTotalTax.add(lineTax);
                                        totalPrice = totalPrice.add(linePrice);
                                        orignalTotalPrice = orignalTotalPrice.add(lineOrignalPrice);
                                        totalQuantity += quantity;
                                        itemTotalQuantity += quantity;
                                        warehouesTotalCount += 1;
                                        supplierTotalPrice = supplierTotalPrice.add(linePrice);
                                        allCartLineList.add(cartLine);
                                    }
                                }
                            }// end for  cartLineList
                        }
                        //如果仓库的总税费不大于50 ，税费记为0
                        double actualUsedTaxfee = 0;
                       /** if (warehouseTotalTax.doubleValue() <= 50) {
                            for (CartLineDTO cartLine : cartLineList) {
                                cartLine.setTaxfFee(0d);
                            }
                            //warehouseTotalTax = BigDecimal.ZERO;
                        } else {*/
                        	//将税费算入总税费中
                            logger.error("storageType:" + storageType + " ...............seaChannelCode :  " + seaChannelCode);
                            if (storageType != OrderConstant.OrderType.DOMESTIC.getCode().intValue()) { //国内直发 ，免税费，但是要显示，所以不加入总价
                                logger.debug("非国内直发");
                                if (!ClearanceChannelsEnum.HWZY.name().equals(seaChannelCode)) {//海外直邮，免税费，但是要显示，也不加入总价
                                    logger.debug("非海外直邮");
                                    supplierTotalTax = supplierTotalTax.add(warehouseTotalTax);
                                    totalTax = totalTax.add(warehouseTotalTax);
                                    actualUsedTaxfee = warehouseTotalTax.doubleValue();
                                }
                            }
                       // }
//						warehouseDTO.setItemTotalQuantity(warehouesTotalCount);
                        warehouseDTO.setTotalPrice(warehouseTotalPrice.doubleValue());
                        warehouseDTO.setTotalTaxfee(warehouseTotalTax.doubleValue());
                        warehouseDTO.setItemTotalQuantity(itemTotalQuantity);
                        warehouseDTO.setActualUsedTaxfee(actualUsedTaxfee);
                        logger.debug("actualUsedTaxfee...." + actualUsedTaxfee);

                    }//end  for warehouseDTOList
                    //计算供应商信息
                    //运费是否满足免费额
                    if ((freePostage != null && freePostage == 0) || (postage != null && postage == 0)) {//包邮
                        for (SeaOrderItemWithWarehouseDTO warehouseDTO : warehouseDTOList) {
                            warehouseDTO.setTotalFreight(0d);
                            double warehousePayPrice = new BigDecimal(warehouseDTO.getTotalPrice())
                                    .add(new BigDecimal(warehouseDTO.getActualUsedTaxfee()))
                                    .add(new BigDecimal(warehouseDTO.getTotalFreight()))
                                    .doubleValue();
                            warehouseDTO.setTotalPayPrice(warehousePayPrice);
                        }
                    } else {
                        if (postage != null && postage != 0) {
                            if (supplierTotalPrice.doubleValue() >= freePostage) {//满足包邮
                                for (SeaOrderItemWithWarehouseDTO warehouseDTO : warehouseDTOList) {
                                    warehouseDTO.setTotalFreight(0d);
                                    double warehousePayPrice = new BigDecimal(warehouseDTO.getTotalPrice())
                                            .add(new BigDecimal(warehouseDTO.getActualUsedTaxfee()))
                                            .add(new BigDecimal(warehouseDTO.getTotalFreight()))
                                            .doubleValue();
                                    warehouseDTO.setTotalPayPrice(warehousePayPrice);
                                }
                            } else {
                                //不满足包邮，按比例分配运费到仓库
                                if (warehouseDTOList.size() > 1) {
                                    BigDecimal tempFreight = BigDecimal.ZERO;
                                    for (SeaOrderItemWithWarehouseDTO warehouseDTO : warehouseDTOList) {
                                        BigDecimal warehouseFreight = BigDecimal.ZERO;
                                        BigDecimal warehousePrice = new BigDecimal(warehouseDTO.getTotalPrice());
                                        warehouseFreight = MathUtils.formatToPrice(MathUtils.multiply(MathUtils.divide(warehousePrice, supplierTotalPrice), postage));
                                        warehouseDTO.setTotalFreight(warehouseFreight.doubleValue());
                                        tempFreight = tempFreight.add(warehouseFreight);
                                        double warehousePayPrice = new BigDecimal(warehouseDTO.getTotalPrice())
                                                .add(new BigDecimal(warehouseDTO.getActualUsedTaxfee()))
                                                .add(new BigDecimal(warehouseDTO.getTotalFreight()))
                                                .doubleValue();
                                        warehouseDTO.setTotalPayPrice(warehousePayPrice);
                                    }
                                    BigDecimal remainFreight = BigDecimal.ZERO;
                                    remainFreight = new BigDecimal(postage).subtract(tempFreight);
                                    SeaOrderItemWithWarehouseDTO firstWarehouseDTO = warehouseDTOList.get(0);
                                    firstWarehouseDTO.setTotalFreight(new BigDecimal(firstWarehouseDTO.getTotalFreight()).add(remainFreight).doubleValue());
                                    firstWarehouseDTO.setTotalPayPrice(new BigDecimal(firstWarehouseDTO.getTotalPrice())
                                            .add(new BigDecimal(firstWarehouseDTO.getActualUsedTaxfee()))
                                            .add(new BigDecimal(firstWarehouseDTO.getTotalFreight())).doubleValue());
                                    //.add(remainFreight).doubleValue());
                                } else {
                                    SeaOrderItemWithWarehouseDTO firstWarehouseDTO = warehouseDTOList.get(0);
                                    firstWarehouseDTO.setTotalFreight(postage.doubleValue());
                                    BigDecimal paytotal = new BigDecimal(firstWarehouseDTO.getTotalPrice())
                                            .add(new BigDecimal(firstWarehouseDTO.getActualUsedTaxfee()))
                                            .add(new BigDecimal(postage));
                                    firstWarehouseDTO.setTotalPayPrice(paytotal.doubleValue());
                                }
                                //加到总运费中
                                totalFreightFee = totalFreightFee.add(new BigDecimal(postage));
                            }
                        } else {
                            //包邮
                            for (SeaOrderItemWithWarehouseDTO warehouseDTO : warehouseDTOList) {
                                warehouseDTO.setTotalFreight(0d);
                                double warehousePayPrice = new BigDecimal(warehouseDTO.getTotalPrice())
                                        .add(new BigDecimal(warehouseDTO.getActualUsedTaxfee()))
                                        .add(new BigDecimal(warehouseDTO.getTotalFreight()))
                                        .doubleValue();
                                warehouseDTO.setTotalPayPrice(warehousePayPrice);
                            }
                        }
                    } //运费处理结束
                }// end

            }//end  for supplierList
            
            orderDTO.setTotalFreight(totalFreightFee.doubleValue());
            orderDTO.setTotalPrice(totalPrice.doubleValue());
            orderDTO.setRealSum(totalPrice.doubleValue());
            orderDTO.setPayPrice(totalPrice.add(totalFreightFee).add(totalTax).doubleValue());
            orderDTO.setTotalTaxfee(totalTax.doubleValue());
            if (logger.isDebugEnabled()) {
                logger.info("[hitaoOrderTotalPriceWithCoupon]haitao tax fee :" + totalTax.doubleValue());
            }
            orderDTO.setTotalOrignalSum(orignalTotalPrice.doubleValue());
            BigDecimal totalDiscount = orignalTotalPrice.subtract(totalPrice);
            orderDTO.setTotalDiscount(totalDiscount.doubleValue());
            orderDTO.setQuantity(totalQuantity);
        }

        //优惠券的使用
        BigDecimal price = totalPrice;
        // 税费加入红包计算
        BigDecimal remainFee = totalTax;
        BigDecimal remainFreight = totalFreightFee;
        Map<Long, List<String>> cuidSkuMap = new HashMap<Long, List<String>>();
        List<OrderCouponDTO> couponDtoList = new ArrayList<OrderCouponDTO>();
        if (couponUserIdList != null && couponUserIdList.size() > 0) {
            List<Long> resCouponIdList = new ArrayList<Long>();
            Set<Long> couponUserIdSet = new HashSet<Long>(couponUserIdList);
            for (Long cuid : couponUserIdSet) {
                CouponUser couponUser = couponUserDao.queryById(cuid);
                if (couponUser != null) {
                    if (!couponUser.getToUserId().equals(memberId)) {
                        throw new ServiceException("优惠券信息错误");
                    }

                    long couponId = couponUser.getBatchId();
                    Coupon coupon = couponDao.queryById(couponId);
                    if (coupon != null) {
                        if (coupon.getCouponType() == CouponType.NO_CONDITION.ordinal())
                            resCouponIdList.add(cuid);
                        else
                            resCouponIdList.add(0, cuid);
                    }
                }
            }
            /*if (logger.isDebugEnabled())
                logger.info("coupon list ------------" + resCouponIdList.get(0));*/
            int totalCoupon = 0;
            for (Long cuid : resCouponIdList) {
                List<String> skuList = new ArrayList<String>();
                CouponUser couponUser = couponUserDao.queryById(cuid);
                if (couponUser != null) {
                    long couponId = couponUser.getBatchId();
                    Coupon coupon = couponDao.queryById(couponId);
                    if (coupon != null) {
                        int status = couponUser.getStatus();
                        if (status == CouponUserStatus.NORMAL.ordinal()) {
                            checkCouponStatus(couponUser, coupon);    //再一次计算时间，验证临界点优惠券有效期到来的问题
                            CouponRange rangeDO = new CouponRange();
                            rangeDO.setCouponId(couponId);
                            rangeDO.setRangeType(0);
                            List<CouponRange> rangeList = new ArrayList<CouponRange>();
                            List<CouponRange> rangeListTemp = couponRangeDao.queryByObject(rangeDO);
                            if (rangeList != null && rangeList.size() > 0) {
                                for (CouponRange range : rangeListTemp) {
                                    if (range.getRangeType() == 0) {
                                        rangeList.add(range);
                                    }
                                }
                            }
                            if (rangeList != null && rangeList.size() > 0) {
                                List<CartLineDTO> cartLineListCanUse = new ArrayList<>();
                                for (CartLineDTO cartLine : allCartLineList) {
                                    Boolean canUse = checkCartLineCanUse(cartLine, rangeList);
                                    if (canUse) {
                                        //条件符合，看总价是否符合，（如果单个行总价不满足，则某几行总和满足）
                                        cartLineListCanUse.add(cartLine);
                                    }
                                }
                                price = processCoupon(cuidSkuMap, price, cuid,
                                        skuList, couponUser, coupon, cartLineListCanUse);
                            } else {//没有限制条件
                                price = processCoupon(cuidSkuMap, price, cuid,
                                        skuList, couponUser, coupon, allCartLineList);
                            }
                            //优惠券的详细信息,拆单的时候需要用到
                            try {
                                OrderCouponDTO couponDto = new OrderCouponDTO();
                                couponDto.setCouponUserId(cuid);
                                couponDto.setCouponName(coupon.getCouponName());
                                couponDto.setCouponType(coupon.getCouponType());
                                couponDto.setFaceValue(coupon.getFaceValue());
                                couponDto.setNeedOverMon(coupon.getNeedOverMon());
                                couponDto.setUseRange(coupon.getUseRange());
                                couponDto.setCouponRangeList(rangeList);
                                couponDto.setSourceId(coupon.getSourceId());
                                couponDto.setSourceName(coupon.getSourceName());
                                couponDto.setSourceType(coupon.getSourceType());
                                couponDto.setPromoterId(couponUser.getPromoterId());
                                couponDtoList.add(couponDto);
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                                throw e;
                            }
                            totalCoupon+= coupon.getFaceValue();
                        } else {
                            if (status == CouponUserStatus.INVALID.ordinal()) {
                                logger.error("[hitaoOrderTotalPriceWithCoupon]coupon is invalid...");
                                throw new Exception("优惠券无效");
                            } else if (status == CouponUserStatus.OVERDUE.ordinal()) {
                                logger.error("[hitaoOrderTotalPriceWithCoupon]coupon is overdue...");
                                throw new Exception("优惠券过期");
                            } else if (status == CouponUserStatus.USED.ordinal()) {
                                logger.error("[hitaoOrderTotalPriceWithCoupon]coupon is used...");
                                throw new Exception("优惠券已被使用");
                            }
                        }
                    } else {
                        logger.error("[hitaoOrderTotalPriceWithCoupon]coupon not exist...");
                        throw new Exception("优惠券不存在！");
                    }
                } else {
                    logger.error("[hitaoOrderTotalPriceWithCoupon]coupon not exist...");
                    throw new Exception("优惠券不存在！");
                }
            }
            orderDTO.setTotalCoupon(totalCoupon);
            if (logger.isDebugEnabled())
                logger.error("price ..............." + price.doubleValue());
        }

        if(orderDTO.getFirstMinus() && price.doubleValue()>0){
        	CouponUser couponUser = new CouponUser();
        	couponUser.setCouponType(CouponType.FIRST_MINUS.ordinal());
        	Coupon coupon = new Coupon();
        	coupon.setFaceValue(FIRST_FREE_AMOUNT.intValue());
        	List<String> skuList = new ArrayList<String>();
        	price = processCoupon(cuidSkuMap, price, -1000l,
                    skuList, couponUser, coupon, allCartLineList);
        	OrderCouponDTO couponDto = new OrderCouponDTO();
            couponDto.setCouponUserId(-1000l);
            couponDto.setCouponName("首单立减5元");
            couponDto.setCouponType(CouponType.FIRST_MINUS.ordinal());
            couponDto.setFaceValue(FIRST_FREE_AMOUNT.intValue());
            couponDto.setNeedOverMon(0);
            couponDto.setUseRange("APP首单直减");
            couponDto.setCouponRangeList(null);
            couponDto.setSourceId(null);
            couponDto.setSourceName("首单立减5元");
            couponDto.setSourceType(1);
            couponDtoList.add(couponDto);
        }
        if (price.doubleValue() < 0) {
            remainMoneyPack = BigDecimal.ZERO.subtract(price);
            price = BigDecimal.ZERO;
            if (logger.isDebugEnabled())
                logger.error("红包金额大于商品综合： +=====" + remainMoneyPack.doubleValue());
        }
        orderDTO.setRealSum(price.doubleValue());
        BigDecimal remainPay = price.add(totalFreightFee).add(totalTax);
        if (remainPay.doubleValue() > remainMoneyPack.doubleValue()) {
            remainPay = remainPay.subtract(remainMoneyPack);
        } else {
            remainPay = BigDecimal.ZERO;
        }
        orderDTO.setPayPrice(remainPay.doubleValue());
        
        cartCouponDTO.setCouponDtoList(couponDtoList);
        cartCouponDTO.setCuidSkuMap(cuidSkuMap);
        cartCouponDTO.setSeaOrderItemDTO(orderDTO);
        return cartCouponDTO;
    }

    //验证优惠券是否还可用
    private void checkCouponStatus(CouponUser couponUser, Coupon coupon)
            throws Exception {
        Date now = new Date();
        Integer useType = coupon.getCouponUseType();
        if (Integer.valueOf(useType) == 0) {// 固定区段有效
            if (coupon.getCouponUseEtime() != null) {
                if (now.after(coupon
                        .getCouponUseEtime())) {
                    couponUser.setStatus(CouponUserStatus.OVERDUE.ordinal());
                    couponUserDao.updateNotNullById(couponUser);
                    throw new Exception("优惠券过期");
                }
            }
        } else {// 领取几日有效
            Integer receiveDay = coupon.getUseReceiveDay();
            Date receiveTime = couponUser.getCreateTime();
            if (receiveDay != null && receiveTime != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(receiveTime);
                calendar.add(Calendar.DAY_OF_MONTH, receiveDay);
                Date newTime = calendar.getTime();
                if (now.after(newTime)) {
                    couponUser.setStatus(CouponUserStatus.OVERDUE.ordinal());
                    couponUserDao.updateNotNullById(couponUser);
                    throw new Exception("优惠券过期");
                }
            }
        }
    }

    @Override
    public CartDTO orderTotalPrice(CartDTO cartDTO, List<Long> couponUserIdList, Long memberId) throws Exception {
        CartCouponDTO ccdto = orderTotalPriceWithCoupon(cartDTO, couponUserIdList, memberId);
        cartDTO = ccdto.getCartDTO();
        return cartDTO;
    }

    /**
     * 设置购物车的总价等属性
     *
     * @param cartDTO
     * @throws Exception
     */
    public void validateCartPrice(CartDTO cartDTO) throws Exception {
        if (cartDTO != null) {
            BigDecimal totalPrice = BigDecimal.ZERO;
            BigDecimal orignalTotalPrice = BigDecimal.ZERO;
            int totaluantity = 0;
            List<CartLineDTO> lineList = cartDTO.getLineList();
            if (lineList != null && lineList.size() > 0) {
                for (CartLineDTO cartLine : lineList) {
                    String sku = cartLine.getSkuCode();
                    long topicId = cartLine.getTopicId();
                    int quantity = cartLine.getQuantity();

                    Topic topic = topicDao.queryById(topicId);
                    TopicItem promotionItemDO = new TopicItem();
                    promotionItemDO.setSku(sku);
                    promotionItemDO.setTopicId(topicId);
                    promotionItemDO.setDeletion(DeletionStatus.NORMAL.ordinal());
                    List<TopicItem> pitemList = topicItemDao.queryByObject(promotionItemDO);
                    if (pitemList != null && pitemList.size() > 0) {
                        TopicItem pitemDO = pitemList.get(0);
                        TopicItemInfoDTO result = new TopicItemInfoDTO();
                        result.setStockAmount(pitemDO.getStockAmount());
                        result.setLimitAmount(pitemDO.getLimitAmount());
                        result.setTopicPrice(pitemDO.getTopicPrice());
                        result.setTopicImage(pitemDO.getTopicImage());

                        result.setTopicId(topicId);
                        result.setTopicStatus(topic.getStatus());// 专题专题
                        result.setLastingType(topic.getLastingType());// 专题持续类型
                        result.setStartTime(topic.getStartTime());
                        result.setEndTime(topic.getEndTime());
                        //设置活动是否预占库存:0否1是（5.4）
                        result.setTopicInventoryFlag(topic.getReserveInventoryFlag());
                        
                        cartLine.setTopicItemInfo(result);
                        cartLine.setWarehouseId(pitemDO.getStockLocationId());
                        if (cartLine.getSelected()) {
                            Double topicPrice = pitemDO.getTopicPrice();
                            totalPrice = totalPrice.add(new BigDecimal(topicPrice.toString())
                                    .multiply(new BigDecimal(quantity)));

                            cartLine.setSubTotal(new BigDecimal(topicPrice.toString()).multiply(new BigDecimal(quantity)).doubleValue());
                            Double orignalPrice = cartLine.getListPrice();
                            orignalTotalPrice = orignalTotalPrice.add(new BigDecimal(orignalPrice.toString())
                                    .multiply(new BigDecimal(quantity)));
                            totaluantity += quantity;
                        }
                    }
                }// end for
                cartDTO.setRealSum(totalPrice.doubleValue());
                cartDTO.setOriginalSum(orignalTotalPrice.doubleValue());
                cartDTO.setTopicRealSum(totalPrice.doubleValue());
                cartDTO.setQuantity(totaluantity);
            } else {
                logger.error("[validateCartPrice]empty in cart...");
                throw new Exception("购物车没有商品！");
            }
        }
    }

    @Override
    public CartCouponDTO orderTotalPriceWithCoupon(CartDTO cartDTO, List<Long> couponUserIdList, Long memberId) throws Exception {
        CartCouponDTO ccDTO = new CartCouponDTO();
        Map<Long, List<String>> cuidSkuMap = new HashMap<Long, List<String>>();
        List<OrderCouponDTO> couponDtoList = new ArrayList<OrderCouponDTO>();
        validateCartPrice(cartDTO);//设置总价等属性
        BigDecimal price = new BigDecimal(cartDTO.getRealSum().toString());
        BigDecimal remainMoneyPack = BigDecimal.ZERO;
        Double firstMinusAmount = 0d;
        if (couponUserIdList != null && couponUserIdList.size() > 0) {
            Set<Long> couponUserIdSet = new HashSet<Long>(couponUserIdList);
            List<Long> resCouponIdList = new ArrayList<Long>();
            for (Long cuid : couponUserIdSet) {
                CouponUser couponUser = couponUserDao.queryById(cuid);
                if (couponUser != null) {
                    long couponId = couponUser.getBatchId();
                    Coupon coupon = couponDao.queryById(couponId);
                    if (coupon != null) {
                        if (coupon.getCouponType() == CouponType.NO_CONDITION.ordinal())
                            resCouponIdList.add(cuid);
                        else
                            resCouponIdList.add(0, cuid);
                    }
                }
            }
            if (logger.isDebugEnabled())
                logger.error("coupon list ------------" + resCouponIdList.get(0));
            for (Long cuid : resCouponIdList) {
                List<String> skuList = new ArrayList<String>();
                CouponUser couponUser = couponUserDao.queryById(cuid);
                if (couponUser != null) {
                    int status = couponUser.getStatus();
                    if (status == CouponUserStatus.NORMAL.ordinal()) {
                        long couponId = couponUser.getBatchId();
                        Coupon coupon = couponDao.queryById(couponId);
                        if (coupon != null) {
                            CouponRange rangeDO = new CouponRange();
                            rangeDO.setCouponId(couponId);
                            rangeDO.setRangeType(0);
                            List<CouponRange> rangeList = new ArrayList<CouponRange>();
                            List<CouponRange> rangeListTemp = couponRangeDao.queryByObject(rangeDO);
                            if (rangeList != null && rangeList.size() > 0) {
                                for (CouponRange range : rangeListTemp) {
                                    if (range.getRangeType() == 0) {
                                        rangeList.add(range);
                                    }
                                }
                            }
                            if (rangeList != null && rangeList.size() > 0) {
                                List<CartLineDTO> cartLineList = cartDTO.getLineList();
                                List<CartLineDTO> cartLineListCanUse = new ArrayList<>();
                                for (CartLineDTO cartLine : cartLineList) {
                                    Boolean canUse = checkCartLineCanUse(cartLine, rangeList);
                                    if (canUse) {
                                        //条件符合，看总价是否符合，（如果单个行总价不满足，则某几行总和满足）
                                        cartLineListCanUse.add(cartLine);
                                    }
                                }
                                price = processCoupon(cuidSkuMap, price, cuid,
                                        skuList, couponUser, coupon, cartLineListCanUse);
                            } else {//没有限制条件
                                List<CartLineDTO> cartLineList = cartDTO.getLineList();
                                price = processCoupon(cuidSkuMap, price, cuid,
                                        skuList, couponUser, coupon, cartLineList);
                            }
                            //优惠券的详细信息,拆单的时候需要用到
                            try {
                                OrderCouponDTO couponDto = new OrderCouponDTO();
                                couponDto.setCouponUserId(cuid);
                                couponDto.setCouponName(coupon.getCouponName());
                                couponDto.setCouponType(coupon.getCouponType());
                                couponDto.setFaceValue(coupon.getFaceValue());
                                couponDto.setNeedOverMon(coupon.getNeedOverMon());
                                couponDto.setUseRange(coupon.getUseRange());
                                couponDto.setCouponRangeList(rangeList);
                                couponDto.setSourceId(coupon.getSourceId());
                                couponDto.setSourceName(coupon.getSourceName());
                                couponDto.setSourceType(coupon.getSourceType());
                                couponDto.setPromoterId(couponUser.getPromoterId());
                                couponDtoList.add(couponDto);
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                                throw e;
                            }
                        } else {
                            logger.error("[orderTotalPriceWithCoupon]coupon not exist....");
                            throw new Exception("优惠券不存在！");
                        }
                    } else {
                        if (status == CouponUserStatus.INVALID.ordinal()) {
                            logger.error("[orderTotalPriceWithCoupon]coupon is invalid....");
                            throw new Exception("优惠券无效");
                        } else if (status == CouponUserStatus.OVERDUE.ordinal()) {
                            logger.error("[orderTotalPriceWithCoupon]coupon is overdue....");
                            throw new Exception("优惠券过期");
                        } else if (status == CouponUserStatus.USED.ordinal()) {
                            logger.error("[orderTotalPriceWithCoupon]coupon is used....");
                            throw new Exception("优惠券已被使用");
                        }
                    }
                }
            }
            if(cartDTO.firstMinus && price.doubleValue()>0){
            	price = BigDecimalUtil.divide(price, FIRST_FREE_AMOUNT);
            	if(price.doubleValue()<0){
                	firstMinusAmount = BigDecimalUtil.divide(0d, price).doubleValue();
            	}
            	OrderCouponDTO couponDto = new OrderCouponDTO();
                couponDto.setCouponUserId(-1000l);
                couponDto.setCouponName("首单立减5元");
                couponDto.setCouponType(CouponType.FIRST_MINUS.ordinal());
                couponDto.setFaceValue(FIRST_FREE_AMOUNT.intValue());
                couponDto.setNeedOverMon(0);
                couponDto.setUseRange("");
                couponDto.setCouponRangeList(null);
                couponDto.setSourceId(null);
                couponDto.setSourceName("首单立减5元");
                couponDto.setSourceType(cartDTO.getSourceType());
                couponDtoList.add(couponDto);
            }
            if (price.doubleValue() < 0) {//说明红包的金额大于商品总价，导致总价出现负数
                cartDTO.setRealSum(0D);
                remainMoneyPack = BigDecimal.ZERO.subtract(price);
                logger.debug("红包金额大于商品综合： +=====" + remainMoneyPack.doubleValue());
            } else
                cartDTO.setRealSum(price.doubleValue());
        }
        //计算运费
        BigDecimal totalPrice = new BigDecimal(cartDTO.getTopicRealSum().toString());
        Double freightFee = 0D;
        //是否有全场满包邮
        FreightTemplate ftdo = new FreightTemplate();
        ftdo.setCalculateMode(0);//全场包邮
        ftdo.setFreightType(0);//国内
        ftdo.setIsDelete(0);
        long count = freightTemplateDao.queryByObjectCount(ftdo);
        if (count == 0) {//没有全场包邮活动
            freightFee = compareCartLine(cartDTO, freightFee);
        } else {//有全场满包邮活动
            //freightFee = 0D;
            List<FreightTemplate> flist = freightTemplateDao.queryByObject(ftdo);
            FreightTemplate freightTemplate = flist.get(0);
            Double postage = freightTemplate.getPostage().doubleValue();
            Double freePostage = freightTemplate.getFreePostage().doubleValue();

            int res = totalPrice.compareTo(new BigDecimal(freePostage)); // -1 小于   0 等于    1 大于
            if (res == -1) {//不符合包邮条件
                //freightFee = postage;
                //比较单个商品的运费
                freightFee = compareCartLine(cartDTO, postage);
            } else {
                freightFee = 0D;
            }
        }

        if (remainMoneyPack.compareTo(BigDecimal.ZERO) > 0) {
            if (remainMoneyPack.compareTo(new BigDecimal(freightFee)) >= 0) {
                freightFee = 0D;
            } else {
                freightFee = new BigDecimal(freightFee).subtract(remainMoneyPack).doubleValue();
            }
        }
        if(cartDTO.firstMinus && freightFee>0 && firstMinusAmount>0){
        	freightFee = BigDecimalUtil.divide(freightFee, firstMinusAmount).doubleValue();
        }
        if(freightFee<0){
        	freightFee =0d;
        }
        cartDTO.setRealFee(freightFee);
        ccDTO.setCartDTO(cartDTO);
        ccDTO.setCuidSkuMap(cuidSkuMap);
        ccDTO.setCouponDtoList(couponDtoList);
        return ccDTO;
    }


    private BigDecimal processCoupon(Map<Long, List<String>> cuidSkuMap,
                                     BigDecimal price, Long cuid, List<String> skuList,
                                     CouponUser couponUser, Coupon coupon,
                                     List<CartLineDTO> cartLineList) throws Exception {
        int couponType = couponUser.getCouponType();
        if (couponType == CouponType.HAS_CONDITION.ordinal()) {//优惠券
            int need = coupon.getNeedOverMon();
            for (CartLineDTO cartLine : cartLineList) {
                BigDecimal totalPrice = BigDecimal.ZERO;
                Double topicPrice = cartLine.getTopicItemInfo().getTopicPrice();
                int quantity = cartLine.getQuantity();
                totalPrice = totalPrice.add(new BigDecimal(topicPrice.toString())
                        .multiply(new BigDecimal(quantity)));
                //cartLine.setSubTotal(topicPrice);//行总价
                //cartLine.setSubTotal(new BigDecimal(topicPrice.toString()).multiply(new BigDecimal(quantity)).doubleValue());
                int res = totalPrice.compareTo(new BigDecimal(need)); // -1 小于   0 等于    1 大于
                if (res != -1) {
                    skuList.add(cartLine.getSkuCode());
                } else {
                    //单个商品行不符合使用条件，应该是某总和满足条件
                    skuList.add(cartLine.getSkuCode());
                }
            }
            if (skuList.size() != 0) {
                price = price.subtract(new BigDecimal(coupon.getFaceValue()));
                cuidSkuMap.put(cuid, skuList);
                if (price.doubleValue() < 0)
                    price = new BigDecimal(0);//保证优惠券使用后，不出现负数，跟红包区分
            } else {
                logger.error("[processCoupon]coupon is not in rule ....");
                //不符合使用条件
                throw new Exception("优惠券不合使用规则！");
            }

        } else {//红包
            price = price.subtract(new BigDecimal(coupon.getFaceValue()));//可能出现负数，表示红包大于总商品价格
            for (CartLineDTO cartLine : cartLineList) {
                skuList.add(cartLine.getSkuCode());
            }
            cuidSkuMap.put(cuid, skuList);
        }
        return price;
    }

    private Boolean checkCartLineCanUse(CartLineDTO cartLine, List<CouponRange> rangeList) {
        Set<Long> brandIdSet = new HashSet<Long>();
        Set<Long> cidSetF = new HashSet<Long>();
        Set<Long> cidSetS = new HashSet<Long>();
        Set<Long> cidSetT = new HashSet<Long>();
        Set<String> skuSet = new HashSet<String>();
        for (CouponRange range : rangeList) {
            Long bid = range.getBrandId();
            Long cidF = range.getCategoryId();
            Long cidS = range.getCategoryMiddleId();
            Long cidT = range.getCategorySmallId();
            String sku = range.getCode();
            if (bid != null && bid != 0l) {
                brandIdSet.add(bid);
            }
            if (cidF != null && cidF != 0l) {
                cidSetF.add(cidF);
            }
            if (cidS != null && cidS != 0l) {
                cidSetS.add(cidS);
            }
            if (cidT != null && cidT != 0l) {
                cidSetT.add(cidT);
            }
            if (sku != null && !"".equals(sku)) {
                skuSet.add(sku);
            }
        }
        String sku = cartLine.getSkuCode();
        Long brandId = cartLine.getBrandId();
        Long largeId = cartLine.getLargeId();
        Long middleId = cartLine.getMediumId();
        Long smallId = cartLine.getSmallId();
        if (skuSet != null && skuSet.contains(sku))
            return true;
        if (cidSetT != null && cidSetT.contains(smallId))
            return true;
        if (cidSetS != null && cidSetS.contains(middleId))
            return true;
        if (cidSetF != null && cidSetF.contains(largeId))
            return true;
        if (brandIdSet != null && brandIdSet.contains(brandId))
            return true;
        return false;

    }


    //比较单个商品的运费，取最近运费
    private Double compareCartLine(CartDTO cartDTO, Double freightFee) throws Exception {
        Double resFee = 0D;
        List<CartLineDTO> lineList = cartDTO.getLineList();
        if (lineList != null) {
            //for(CartLineDTO cartLine : lineList){
            for (int i = 0; i <= lineList.size() - 1; i++) {
                CartLineDTO cartLine = lineList.get(i);
                Long ftId = cartLine.getFreightTemplateId().longValue();
                if (ftId != null) {
                    FreightTemplate ftDO = freightTemplateDao.queryById((long) ftId);
                    if (ftDO == null) {
                        logger.error("[compareCartLine]Freight is not exist ....id:" + ftId);
                        throw new Exception("运费模板不存在！");
                    }
                    if (i == 0 && freightFee == 0D)
                        freightFee = ftDO.getPostage().doubleValue();
                    if (freightFee == 0)
                        return freightFee;
                    else {
                        if (freightFee > ftDO.getPostage().doubleValue())
                            freightFee = ftDO.getPostage().doubleValue();
                    }
                }
            }//end for
        } else {//end if
            freightFee = 0D; //没有
        }
        resFee = freightFee;
        return resFee;
    }

    //比较单个商品的运费，取最近运费
    private Double compareCartLineOld(CartDTO cartDTO, Double freightFee) throws Exception {
        Double resFee = 0D;
        List<CartLineDTO> lineList = cartDTO.getLineList();
        if (lineList != null) {
            //for(CartLineDTO cartLine : lineList){
            for (int i = 0; i <= lineList.size() - 1; i++) {
                CartLineDTO cartLine = lineList.get(i);
                TopicItemInfoDTO itemInfo = cartLine.getTopicItemInfo();
                double topicPrice = itemInfo.getTopicPrice();
                int quantity = cartLine.getQuantity();
                BigDecimal totalPrice = BigDecimal.ZERO;
                totalPrice = totalPrice.add(new BigDecimal(topicPrice).multiply(new BigDecimal(quantity)));
                Long ftId = cartLine.getFreightTemplateId().longValue();
                if (ftId != null) {
                    FreightTemplate ftDO = freightTemplateDao.queryById((long) ftId);
                    if (i == 0 && freightFee == 0D)
                        freightFee = ftDO.getPostage().doubleValue();
                    Float freePostage = ftDO.getFreePostage();
                    if (freePostage != null) {//有包邮活动
                        int res = totalPrice.compareTo(new BigDecimal(freePostage)); // -1 小于   0 等于    1 大于
                        if (res == -1) {//不符合包邮条件
                            //Float afterPostage = ftDO.getAftPostage();
                            Double postage = ftDO.getPostage().doubleValue();
                            if (postage != null) {
                                if (postage < freightFee) {
                                    freightFee = postage;
                                }
                            }
                        } else {//符合包邮条件
                            freightFee = 0D;
                            break;
                        }

                    } else {//没有包邮活动
                        Double postage = ftDO.getPostage().doubleValue();
                        if (postage != null) {
                            if (postage < freightFee) {
                                freightFee = postage;
                            }
                        }
                    }
                }

            }//end for
        } else {//end if
            freightFee = 0D; //没有
        }
        resFee = freightFee;
        return resFee;
    }
}
