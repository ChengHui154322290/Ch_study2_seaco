package com.tp.seller.constant;

/**
 * <pre>
 * 商品相关的一些参数
 * </pre>
 *
 * @author qunxi.shao
 * @version $Id: ItemConstant.java, v 0.1 2015年1月3日 上午10:48:17 qunxi.shao Exp $
 */
public final class ItemConstant {

    /** 小类编码的长度 */
    public static final int SMALL_CODE_LENGTH = 6;

    /** SPU编码的长度 */
    public static final int SPU_CODE_LENGTH = 10;

    /** prdid编码的长度 */
    public static final int PRDID_CODE_LENGTH = 12;

    /** SKU编码的长度 */
    public static final int SKU_CODE_LENGTH = 14;

    /** 生产spu，prdid，sku编码 初始化的值1 */
    public static final int CODE_INIT_VALUE = 1;

    /** 基础数据是否有效 :0-失效 ,1-有效,2-全部 */
    public static final int INVALID_DATAS = 0;
    public static final int EFFECTIVE_DATAS = 1;
    public static final int ALL_DATAS = 2;

    /** 运费模板，1为单品团 */
    public static final int SINGLE_FREIGHTTEMPLATE_STATUS = 1;

    /** PRDID 有西客商城妈妈商家 **/
    public static final int HAS_SEAGOOR_SELLER = 1;

    /** 规格均码处理 */
    public static final Long FREE_SIZE_ID = -1L;
    public static final String FREE_SIZE_NAME = "*";

    /** prdid 显示是否上架 */
    public static final String ON_SALES_STR = "已上架";
    public static final String OFF_SALES_STR = "未上架";

    /** 商品属性来源 ，0-来自基础数据里面的设定，1-商品部门自定义 */
    public static final int ATTR_FROM_BASEDATA = 0;
    public static final int ATTR_CUSTOM = 1;
    public static final int ATTR_SELECTED = 1;

    /** 默认分割符合 */
    public static final String DEFAULT_SEPARATOR = ",";
    public static final String DEFAULT_JOIN = "-";

    /*** 自营西客商城妈妈商家 */
    public static final long SUPPLIER_ID = 0;
    public static final String SUPPLIER_NAME = "西客商城";

    /** 分类的level */
    public static final int CATEGORY_LARGE_LEVEL = 1;
    public static final int CATEGORY_MEDIUM_LEVEL = 2;
    public static final int CATEGORY_SMALL_LEVEL = 3;

    /** 商品服务消息队列名称 **/
    public static final String ITEM_INFO_PUB_MSG = "item_info_queue_topic";
    public static final String ITEM_DETAIL_PUB_MSG = "item_detail_queue_topic";
    public static final String ITEM_SKU_PUB_MSG = "item_sku_queue_topic";
    public static final String ITEM_PROMOTION_PUB_MSG = "item_promotion_sku_queue_p2p";

    /*** 海淘商品标示符 **/
    public static final int HAI_TAO = 2;

    private ItemConstant() {
    }
}
