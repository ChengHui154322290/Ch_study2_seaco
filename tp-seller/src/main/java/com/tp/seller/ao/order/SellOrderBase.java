package com.tp.seller.ao.order;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;

import com.tp.common.util.ImageUtil;
import com.tp.common.vo.OrderConstant;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;
import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.seller.ao.base.SellerBaseAO;
import com.tp.seller.domain.SellerOrderDTO;
import com.tp.seller.domain.SellerOrderProductDTO;

/**
 * 订单的抽象方法
 * 
 * @author yfxie
 */
public abstract class SellOrderBase extends SellerBaseAO {

    public static SimpleDateFormat ORDER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    public static SimpleDateFormat ORDER_DATE_FORMAT_COLON = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 订单列表转换
     *
     * @param subOrderList
     * @return
     */
    public List<SellerOrderDTO> listConvert(final List<SubOrder4BackendDTO> subOrderList) {
        final List<SellerOrderDTO> retOrderList = new ArrayList<SellerOrderDTO>();
        if (null != subOrderList && subOrderList.size() > 0) {
            for (final SubOrder4BackendDTO order : subOrderList) {
                retOrderList.add(convertOrderInfo(order));
            }
        }
        return retOrderList;
    }

    /**
     * 订单转换
     *
     * @param retOrderList
     * @param order
     */
    public SellerOrderDTO convertOrderInfo(final SubOrder4BackendDTO order) {
        final SellerOrderDTO sellOrderDTO = new SellerOrderDTO();
        final SubOrder subOrder = order.getSubOrder();
        final OrderInfo salesOrder = order.getOrder();
        final OrderDelivery orderDelivery = order.getOrderDelivery();
        final OrderConsignee consignee = order.getOrderConsignee();
        final MemRealinfo memRealinfo = order.getMemRealinfo();

        if (null != order.getOrderReceipt()) {
            sellOrderDTO.setTitleStr(order.getReceiptDetail().getTitle());
        }
        if (null != salesOrder) {
            sellOrderDTO.setRemark(salesOrder.getRemark());
            sellOrderDTO.setSourceStr(salesOrder.getSourceStr());

        }
        if (null != subOrder) {
        	BeanUtils.copyProperties(subOrder, sellOrderDTO);
        	if(OrderConstant.ORDER_STATUS.PAYMENT.code<subOrder.getOrderStatus()
        	&& OrderConstant.OrderType.FAST.code.equals(subOrder.getType())){
				Long overTime = new Date().getTime()-subOrder.getPayTime().getTime();
				sellOrderDTO.setOverTime(0-(overTime/(1000*60)-2*60));
        	}
            sellOrderDTO.setTotalPay(subOrder.getPayTotal());
            sellOrderDTO.setDeliveryWay(subOrder.getSeaChannelStr());
            sellOrderDTO.setNickName(subOrder.getAccountName());
        }

        if (null != memRealinfo) {
            sellOrderDTO.setRealName(memRealinfo.getRealName());
            sellOrderDTO.setIdentityCode(memRealinfo.getIdentityCode());
            sellOrderDTO.setIdentifyFileFront(memRealinfo.getIdentityFrontImg());
            sellOrderDTO.setIdentifyFileBack(memRealinfo.getIdentityBackImg());
            sellOrderDTO.setIdentifyImageFront(ImageUtil.getCMSImgFullUrl(memRealinfo.getIdentityFrontImg()));
            sellOrderDTO.setIdenfifyImageBack(ImageUtil.getCMSImgFullUrl(memRealinfo.getIdentityBackImg()));
        }
        final StringBuffer sb = new StringBuffer();
        if (null != consignee) {
            sellOrderDTO.setMobile(consignee.getMobile());
            final String province = consignee.getProvinceName();
            final String city = consignee.getCityName();
            final String county = consignee.getCountyName();
            final String town = consignee.getTownName();
            final String address = consignee.getAddress();
            sb.append(province).append(" ");
            sb.append(city).append(" ");
            sb.append(county).append(" ");
            if (StringUtils.isNotBlank(town)) {
                sb.append(town).append(" ");
            }
            sb.append(address).append(" ");
            sellOrderDTO.setAddress(sb.toString());
            sellOrderDTO.setProvince(province);
            sellOrderDTO.setCity(city);
            sellOrderDTO.setCounty(county);
            sellOrderDTO.setPostCode(consignee.getPostcode());
            sellOrderDTO.setTelephone(consignee.getTelephone());
        }
        // copy商品list
        setProductList(order, sellOrderDTO);

        if (null != orderDelivery) {
            sellOrderDTO.setDeliveredTime(orderDelivery.getDeliveryTime());
            sellOrderDTO.setExpressCode(orderDelivery.getCompanyId());
            sellOrderDTO.setExpressName(orderDelivery.getCompanyName());
            sellOrderDTO.setPackageNo(orderDelivery.getPackageNo());
        }
        return sellOrderDTO;
    }

    /**
     * 设置商品信息
     *
     * @param order
     * @param sellOrderDTO
     */
    public void setProductList(final SubOrder4BackendDTO order, final SellerOrderDTO sellOrderDTO) {
        final List<OrderItem> orderLineList = order.getOrderItemList();
        if (null != orderLineList) {
            final List<SellerOrderProductDTO> sellOrderList = new ArrayList<SellerOrderProductDTO>(
                orderLineList.size());
            for (int i = 0; i < orderLineList.size(); i++) {
                final SellerOrderProductDTO sellerOrderProductDTO = new SellerOrderProductDTO();
                final OrderItem orderLineDTO = orderLineList.get(i);
                sellerOrderProductDTO.setProductName(orderLineDTO.getSpuName());
                sellerOrderProductDTO.setProductCode(orderLineDTO.getSkuCode());
                sellerOrderProductDTO.setBarCode(orderLineDTO.getBarCode());
                sellerOrderProductDTO.setQuantity(orderLineDTO.getQuantity());
                sellerOrderProductDTO.setPrice(orderLineDTO.getPrice());
                sellerOrderProductDTO.setTotal(orderLineDTO.getSubTotal());
                sellerOrderProductDTO.setUnit(orderLineDTO.getUnit());
                sellerOrderProductDTO.setTopicId(orderLineDTO.getTopicId());
                sellerOrderProductDTO.setProductCodeHaitao(orderLineDTO.getProductCode());
                sellerOrderProductDTO.setDiscount(orderLineDTO.getDiscount());
                sellerOrderProductDTO.setRealPrice(orderLineDTO.getRealPrice());
                sellerOrderProductDTO.setBrandName(orderLineDTO.getBrandName());
                sellerOrderProductDTO.setWeight(orderLineDTO.getWeight());
                sellOrderList.add(sellerOrderProductDTO);
            }
            sellOrderDTO.setProductList(sellOrderList);
        }
    }

    /**
     * 将Excel文件内容转换为List对象
     * 
     * @param fis excel文件
     * @return List<List> list存放形式的内容
     * @throws IOException
     */
    public List<List<String>> excelFileConvertToList(final FileInputStream fis) throws Exception {
        final Workbook wb = WorkbookFactory.create(fis);

        final Sheet sheet = wb.getSheetAt(0);

        final List<List<String>> rows = new ArrayList<List<String>>();
        for (final Row row : sheet) {
            final List<String> cells = new ArrayList<String>();
            for (final Cell cell : row) {
                String str = null;
                switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    str = cell.getRichStringCellValue().getString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        str = DateFormatUtils.format(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
                    } else {
                        final DecimalFormat df = new DecimalFormat("#");// 转换成整型
                        str = String.valueOf(df.format(cell.getNumericCellValue()));
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    str = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    try {
                        str = String.valueOf(cell.getNumericCellValue());
                    } catch (final IllegalStateException e) {
                        str = String.valueOf(cell.getRichStringCellValue());
                    }
                    break;
                default:
                    str = "";
                }
                cells.add(str);
            }
            rows.add(cells);
        }
        return rows;
    }

    /**
     * 获取日志类型字符串
     * 
     * @return
     */
    public String getOrderDateStr(final Date date) {
        if (null != date) {
            return ORDER_DATE_FORMAT_COLON.format(date);
        } else {
            return "";
        }
    }

    /**
     * 生成每列对应的setter方法 此方法传入的header参数必须在DeliveryOrder里面是存在的（考虑效率问题 ，此方法不会校验，上层方法必须校验）
     * 
     * @return
     * @throws Exception
     */
    // private Map<Integer,Method> genGetMethodMap(String[] heads) throws Exception {
    // Map<Integer,Method> retMethodMap = new HashMap<Integer,Method>();
    // if(null == heads || heads.length==0){
    // throw new Exception("头部信息为空");
    // }
    // Map<String,Integer> indexMap = genHeaderIndexMap(Arrays.asList(heads));
    // SellerOrderDTO order = new SellerOrderDTO();
    // Map<String,Method> fieldCodeMethodMap = new HashMap<String,Method>();
    // fieldCodeMethodMap.put(DeliveryOrder.ORDER_CODE.getKeyName(), SellerOrderDTO.class.getMethod("getSubOrderCode",
    // SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.ORDER_DATE.getKeyName(), SellerOrderDTO.class.getMethod("getOrderDate",
    // SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.PAY_TIME_STR.getKeyName(), SellerOrderDTO.class.getMethod("getPayTimeStr",
    // SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.PRODUCT_NAME.getKeyName(),
    // SellerOrderDTO.class.getMethod("getSubOrderCode", SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.PRODUCT_CODE.getKeyName(),
    // SellerOrderDTO.class.getMethod("getSubOrderCode", SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.QUANTITY.getKeyName(), SellerOrderDTO.class.getMethod("getSubOrderCode",
    // SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.PRICE.getKeyName(), SellerOrderDTO.class.getMethod("getSubOrderCode",
    // SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.TOTAL_AMOUNT_STR.getKeyName(),
    // SellerOrderDTO.class.getMethod("getSubOrderCode", SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.CONIGNEE_NAME.getKeyName(),
    // SellerOrderDTO.class.getMethod("getSubOrderCode", SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.MOBILE.getKeyName(), SellerOrderDTO.class.getMethod("getSubOrderCode",
    // SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.ADRESS.getKeyName(), SellerOrderDTO.class.getMethod("getSubOrderCode",
    // SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.EXPRESS_NAME.getKeyName(),
    // SellerOrderDTO.class.getMethod("getSubOrderCode", SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.EXPRESS_CODE.getKeyName(),
    // SellerOrderDTO.class.getMethod("getSubOrderCode", SellerOrderDTO.class));
    // fieldCodeMethodMap.put(DeliveryOrder.PACKAGE_NO.getKeyName(), SellerOrderDTO.class.getMethod("getSubOrderCode",
    // SellerOrderDTO.class));
    // for(int i=0;i<heads.length;i++) {
    // retMethodMap.put(new Integer(i), fieldCodeMethodMap.get(heads[i]));
    // }
    // return retMethodMap;
    // }

    /**
     * 生成头部索引和标题对应关系
     * 
     * @param heads
     * @return
     */
    // private Map<Integer, String> genIndexHeaderMap(String[] heads) {
    // Map<Integer,String> indexMap = new HashMap<Integer,String>();
    // if(null != heads && heads.length>0){
    // for(int i=0;i<heads.length;i++){
    // String title = heads[i];
    // if(null != title){
    // indexMap.put(new Integer(i), title.trim());
    // }
    // }
    // }
    // return indexMap;
    // }

}
