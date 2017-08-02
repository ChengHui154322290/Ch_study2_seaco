package com.tp.seller.util;

import java.util.HashMap;
import java.util.Map;

import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.common.vo.StorageConstant.StorageType;


/**
 * 商家平台外部常量类定义
 *
 * @author yfxie
 */
public final class SellerOutConstant {

    /**
     * 订单状态的map
     */
    public static final Map<String, String> ORDER_STATUS_MAP = new HashMap<String, String>();

    /**
     * 订单类型的map
     */
    public static final Map<String, String> ORDER_TYPE_MAP = new HashMap<String, String>();

    /**
     * 发货方式的map
     */
    public static final Map<String, String> DELIVERY_WAY_MAP = new HashMap<String, String>();

    /**
     * 导入CSV分隔符
     */
    public static final char SEPARATOR = ',';
    /**
     * 导出订单文件名
     */
    public static final String ORDER_FILER_NAME = "西客商城商家待发货订单列表";
    public static final String ORDER_LIST_FILE_NAME = "西客商城商家订单查询列表";
    /**
     * 导出默认快递文件名
     */
    public static final String ORDER_EXPRESS_FILE_NAME = "默认快递列表";

    private SellerOutConstant() {

    }

    static {
        // final ORDER_STATUS[] orderStatus = OrderConstant.ORDER_STATUS.values();
        final ORDER_STATUS[] orderStatus = OrderConstant.ORDER_STATUS.getCurrentStatus();
        for (final ORDER_STATUS os : orderStatus) {
            ORDER_STATUS_MAP.put(os.getCode().toString(), os.getCnName());
        }
    }

    static {
        final OrderType[] orderTypes = OrderConstant.OrderType.values();
        for (final OrderType ot : orderTypes) {
            ORDER_TYPE_MAP.put(ot.code.toString(), ot.cnName.toString());
        }
    }

    static {
        final StorageType[] storageTypes = StorageType.values();
        for (final StorageType sc : storageTypes) {
            DELIVERY_WAY_MAP.put(sc.getValue().toString(), sc.getName());
        }
    }

}
