package com.tp.common.vo.supplier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.common.vo.supplier.entry.BankType;
import com.tp.common.vo.supplier.entry.CostType;
import com.tp.common.vo.supplier.entry.CurrencyType;
import com.tp.common.vo.supplier.entry.LinkType;
import com.tp.common.vo.supplier.entry.OrderAuditStatus;
import com.tp.common.vo.supplier.entry.OrderStatus;
import com.tp.common.vo.supplier.entry.PurcharseType;
import com.tp.common.vo.supplier.entry.RefundOrderAuditStatus;
import com.tp.common.vo.supplier.entry.SettlementRuleCount;
import com.tp.common.vo.supplier.entry.SettlementRuleDayType;
import com.tp.common.vo.supplier.entry.SettlementRuleType;
import com.tp.common.vo.supplier.entry.SupplierType;
import com.tp.common.vo.supplier.entry.SupplierUserStatus;

/**
 * 供应商/商家基本信息 常量
 *
 * @author szy
 */
public final class SupplierConstant {

    public static final int LENGTH_60 = 60;

    /** 消息页面（如添加供应商成功提示） */
    public static final String MESSAGE_PAGE = "supplier/supplier_message";

    /** 返回消息key */
    public static final String SUCCESS_KEY = "success";

    /** 返回消息key */
    public static final String MESSAGE_KEY = "message";

    /** 返回数据key */
    public static final String DATA_KEY = "data_key";

    /** 文件上传者的信息 */
    public static final String UPLOAD_CREATOR = "supplier_mode";

    /** 文件大小参数key */
    public static final String FILE_SIZE_KEY = "file_size_key";

    /** 文件参数key */
    public static final String UPLOADED_FILE_KEY = "uploaded_file_key";

    /** 文件参数 */
    public static final String UPLOADED_FILE_REAL_PATH = "uploaded_file_path_real";

    /** 商品批次号生成key */
    public static final String PRODUCT_BATCH_NO_KEY = "product_batch_no_key";

    /** 启用状态 */
    public static final Integer STATUS_ENABLE = 1;
    /** 禁用状态 */
    public static final Integer STATUS_DISABLE = 0;

    public static final Boolean STATUS_ENABLE_B = true;

    public static final Boolean STATUS_DISABLE_B = false;

    /**
     * 品牌授权图片
     */
    public static final String IMG_T_BRAND_LICEN = "brandLiscence";

    /**
     * 特殊资质证明
     */
    public static final String IMG_T_SPECIAL_PAPER = "specialPapers";

    /**
     * 文件最大值
     */
    public static final Long MAX_FILE_SIZE = 20194300L;
    /**
     * 图片最大值
     */
    public static final Long MAX_IMAGE_SIZE = 1194300L;

    /** key自增相关参数定义 start */
    // Sequence值缓存大小
    public static final Long POOL_SIZE = 10L;
    public static final Map<String, Long> NEXT_KEY = new HashMap<String, Long>();
    public static final Map<String, Long> MAX_KEY = new HashMap<String, Long>();
    public static final Long MAX_SIZE = 9999L;
    /** key自增相关参数定义 end */

    public static final Map<String, String> PURCHARSE_TYPE_LEVEL_MAP = new HashMap<String, String>();
    static {
        PURCHARSE_TYPE_LEVEL_MAP.put("1", "普通订单");
        PURCHARSE_TYPE_LEVEL_MAP.put("0", "紧急订单");
    }

    public static final Map<String, String> REFUND_PURCHARSE_TYPE_LEVEL_MAP = new HashMap<String, String>();
    static {
        REFUND_PURCHARSE_TYPE_LEVEL_MAP.put("1", "普通退单");
        REFUND_PURCHARSE_TYPE_LEVEL_MAP.put("0", "紧急退单");
    }

    /** 审核状态map */
    public static final Map<Integer, String> AUDIT_STATUS_MAP_ALL = new LinkedHashMap<Integer, String>();
    static {
        final AuditStatus[] values = AuditStatus.values();
        for (final AuditStatus sType : values) {
            AUDIT_STATUS_MAP_ALL.put(sType.getStatus(), sType.getName());
        }
    }

    /** 订单审核状态map */
    public static final Map<Integer, String> O_AUDIT_STATUS_MAP_ALL = new LinkedHashMap<Integer, String>();
    static {
        final OrderAuditStatus[] values = OrderAuditStatus.values();
        for (final OrderAuditStatus sType : values) {
            O_AUDIT_STATUS_MAP_ALL.put(sType.getStatus(), sType.getName());
        }
    }

    /** 退货单审核状态map */
    public static final Map<Integer, String> REFUND_O_AUDIT_STATUS_MAP_ALL = new LinkedHashMap<Integer, String>();
    static {
        final RefundOrderAuditStatus[] values = RefundOrderAuditStatus.values();
        for (final RefundOrderAuditStatus sType : values) {
            REFUND_O_AUDIT_STATUS_MAP_ALL.put(sType.getStatus(), sType.getName());
        }
    }

    /** 审核状态map 显示 */
    public static final Map<String, String> AUDIT_STATUS_MAP_SELECT_ALL = new LinkedHashMap<String, String>();
    static {

        AUDIT_STATUS_MAP_SELECT_ALL.put(AuditStatus.WAIT_UPLOAD_FILE.getStatus().toString(), AuditStatus.WAIT_UPLOAD_FILE.getName());

        AUDIT_STATUS_MAP_SELECT_ALL.put(AuditStatus.EDITING.getStatus().toString(), AuditStatus.EDITING.getName());

        AUDIT_STATUS_MAP_SELECT_ALL.put(AuditStatus.EXAMING.getStatus().toString(), AuditStatus.EXAMING.getName());

        AUDIT_STATUS_MAP_SELECT_ALL.put(AuditStatus.CANCELED.getStatus().toString(), AuditStatus.CANCELED.getName());

        AUDIT_STATUS_MAP_SELECT_ALL.put(AuditStatus.THROUGH.getStatus().toString(), AuditStatus.THROUGH.getName());

        AUDIT_STATUS_MAP_SELECT_ALL.put(AuditStatus.REFUSED.getStatus().toString(), AuditStatus.REFUSED.getName());

        AUDIT_STATUS_MAP_SELECT_ALL.put(AuditStatus.STOPED.getStatus().toString(), AuditStatus.STOPED.getName());
    }

    /** 审核状态map 下拉框 */
    public static final Map<String, String> AUDIT_STATUS_MAP_SELECT = new LinkedHashMap<String, String>();
    static {

        AUDIT_STATUS_MAP_SELECT.put(AuditStatus.EDITING.getStatus().toString(), AuditStatus.EDITING.getName());

        AUDIT_STATUS_MAP_SELECT.put(AuditStatus.EXAMING.getStatus().toString(), AuditStatus.EXAMING.getName());

        AUDIT_STATUS_MAP_SELECT.put(AuditStatus.CANCELED.getStatus().toString(), AuditStatus.CANCELED.getName());

        AUDIT_STATUS_MAP_SELECT.put(AuditStatus.THROUGH.getStatus().toString(), AuditStatus.THROUGH.getName());

        AUDIT_STATUS_MAP_SELECT.put(AuditStatus.REFUSED.getStatus().toString(), AuditStatus.REFUSED.getName());

        AUDIT_STATUS_MAP_SELECT.put(AuditStatus.STOPED.getStatus().toString(), AuditStatus.STOPED.getName());
    }

    /** 订单审核状态map 下拉框 */
    public static final Map<String, String> O_AUDIT_STATUS_MAP_SELECT = new LinkedHashMap<String, String>();
    static {

        O_AUDIT_STATUS_MAP_SELECT.put(OrderAuditStatus.EDITING.getStatus().toString(), OrderAuditStatus.EDITING.getName());

        O_AUDIT_STATUS_MAP_SELECT.put(OrderAuditStatus.EXAMING.getStatus().toString(), OrderAuditStatus.EXAMING.getName());

        O_AUDIT_STATUS_MAP_SELECT.put(OrderAuditStatus.CANCELED.getStatus().toString(), OrderAuditStatus.CANCELED.getName());

        O_AUDIT_STATUS_MAP_SELECT.put(OrderAuditStatus.THROUGH.getStatus().toString(), OrderAuditStatus.THROUGH.getName());

        O_AUDIT_STATUS_MAP_SELECT.put(OrderAuditStatus.REFUSED.getStatus().toString(), OrderAuditStatus.REFUSED.getName());

        O_AUDIT_STATUS_MAP_SELECT.put(OrderAuditStatus.PURCHARSE_FINISHED.getStatus().toString(), OrderAuditStatus.PURCHARSE_FINISHED.getName());
    }

    /** 退货单审核状态map 下拉框 */
    public static final Map<String, String> REFUND_O_AUDIT_STATUS_MAP_SELECT = new LinkedHashMap<String, String>();
    static {

        REFUND_O_AUDIT_STATUS_MAP_SELECT.put(RefundOrderAuditStatus.EDITING.getStatus().toString(), RefundOrderAuditStatus.EDITING.getName());

        REFUND_O_AUDIT_STATUS_MAP_SELECT.put(RefundOrderAuditStatus.EXAMING.getStatus().toString(), RefundOrderAuditStatus.EXAMING.getName());

        REFUND_O_AUDIT_STATUS_MAP_SELECT.put(RefundOrderAuditStatus.CANCELED.getStatus().toString(), RefundOrderAuditStatus.CANCELED.getName());

        REFUND_O_AUDIT_STATUS_MAP_SELECT.put(RefundOrderAuditStatus.THROUGH.getStatus().toString(), RefundOrderAuditStatus.THROUGH.getName());

        REFUND_O_AUDIT_STATUS_MAP_SELECT.put(RefundOrderAuditStatus.REFUSED.getStatus().toString(), RefundOrderAuditStatus.REFUSED.getName());

        REFUND_O_AUDIT_STATUS_MAP_SELECT.put(RefundOrderAuditStatus.PURCHARSE_FINISHED.getStatus().toString(), RefundOrderAuditStatus.PURCHARSE_FINISHED.getName());
    }

    /** 供应商类型map */
    public static final Map<String, String> SUPPLIER_TYPES = new LinkedHashMap<String, String>();
    static {
        final SupplierType[] values = SupplierType.values();
        for (final SupplierType sType : values) {
            SUPPLIER_TYPES.put(sType.getValue(), sType.getName());
        }
    }
    /** 供应商类型说明map */
    public static final Map<String, String> SUPPLIERTYPE_EXPLANATIONS = new LinkedHashMap<String, String>();
    static {
        final SupplierType[] values = SupplierType.values();
        for (final SupplierType sType : values) {
            SUPPLIERTYPE_EXPLANATIONS.put(sType.getValue(), sType.getExplanation());
        }
    }
    /** 商家平台状态 */
    public static final Map<String, String> SUPPLIERUSERSTATUS_MAP = new LinkedHashMap<String, String>();
    static {
        final SupplierUserStatus[] values = SupplierUserStatus.values();
        for (final SupplierUserStatus sUserStatus : values) {
            SUPPLIERUSERSTATUS_MAP.put(sUserStatus.getStatus().toString(), sUserStatus.getName());
        }
    }

    /** 结算规则类型 */
    public static final Map<String, String> SETTLEMENTRULETYPE_MAP = new LinkedHashMap<String, String>();
    static {
        final SettlementRuleType[] values = SettlementRuleType.values();
        for (final SettlementRuleType sType : values) {
            SETTLEMENTRULETYPE_MAP.put(sType.getValue(), sType.getName());
        }
    }

    /** 每月结算次数 */
    public static final Map<String, String> SETTLEMENTRULECOUNT_MAP = new LinkedHashMap<String, String>();
    static {
        final SettlementRuleCount[] values = SettlementRuleCount.values();
        for (final SettlementRuleCount sCount : values) {
            SETTLEMENTRULECOUNT_MAP.put(sCount.getValue(), sCount.getName());
        }
    }

    /** 结算规则的天的类型 */
    public static final Map<String, String> SETTLEMENTRULEDAYTYPE_MAP = new LinkedHashMap<String, String>();
    static {
        final SettlementRuleDayType[] values = SettlementRuleDayType.values();
        for (final SettlementRuleDayType sType : values) {
            SETTLEMENTRULEDAYTYPE_MAP.put(sType.getValue(), sType.getName());
        }
    }

    /** 银行类型map */
    public static final Map<String, String> SUPPLIER_BANK_TYPES = new LinkedHashMap<String, String>();
    static {
        final BankType[] values = BankType.values();
        for (final BankType sType : values) {
            SUPPLIER_BANK_TYPES.put(sType.getValue(), sType.getName());
        }
    }

    /** 币种map */
    public static final Map<String, String> SUPPLIER_CURRENCY_TYPES = new LinkedHashMap<String, String>();
    static {
        final CurrencyType[] values = CurrencyType.values();
        for (final CurrencyType sType : values) {
            SUPPLIER_CURRENCY_TYPES.put(sType.getValue(), sType.getName());
        }
    }

    /** 联系人类型map */
    public static final Map<String, String> SUPPLIER_LINK_TYPES = new LinkedHashMap<String, String>();
    static {
        final LinkType[] values = LinkType.values();
        for (final LinkType sType : values) {
            SUPPLIER_LINK_TYPES.put(sType.getValue(), sType.getName());
        }
    }

    /**
     * 先前的状态
     */
    public static final Map<Integer, Set<Integer>> PREVIOUS_AUDIT_STATUS = new LinkedHashMap<Integer, Set<Integer>>();
    static {
        final AuditStatus[] values = AuditStatus.values();
        for (final AuditStatus sType : values) {
            PREVIOUS_AUDIT_STATUS.put(sType.getStatus(), sType.getPreStatus());
        }
    }

    /**
     * 订单审核先前的状态
     */
    public static final Map<Integer, Set<Integer>> O_PREVIOUS_AUDIT_STATUS = new LinkedHashMap<Integer, Set<Integer>>();
    static {
        final OrderAuditStatus[] values = OrderAuditStatus.values();
        for (final OrderAuditStatus sType : values) {
            O_PREVIOUS_AUDIT_STATUS.put(sType.getStatus(), sType.getPreStatus());
        }
    }

    /**
     * 审核结果map
     */
    public static final Map<Integer, String> AUDIT_RESULT = new LinkedHashMap<Integer, String>();
    static {
        final AuditStatus[] values = AuditStatus.values();
        for (final AuditStatus sType : values) {
            AUDIT_RESULT.put(sType.getStatus(), sType.getResult());
        }
    }

    /**
     * 订单审核结果map
     */
    public static final Map<Integer, String> O_AUDIT_RESULT = new LinkedHashMap<Integer, String>();
    static {
        final OrderAuditStatus[] values = OrderAuditStatus.values();
        for (final OrderAuditStatus sType : values) {
            O_AUDIT_RESULT.put(sType.getStatus(), sType.getResult());
        }
    }

    /**
     * 退货单审核结果map
     */
    public static final Map<Integer, String> REFUND_O_AUDIT_RESULT = new LinkedHashMap<Integer, String>();
    static {
        final RefundOrderAuditStatus[] values = RefundOrderAuditStatus.values();
        for (final RefundOrderAuditStatus sType : values) {
            REFUND_O_AUDIT_RESULT.put(sType.getStatus(), sType.getResult());
        }
    }

    public static final Map<String, String> PURCHARSE_TYPE_MAP = new LinkedHashMap<String, String>();
    static {
        final PurcharseType[] values = PurcharseType.values();
        for (final PurcharseType orderType : values) {
            PURCHARSE_TYPE_MAP.put(orderType.getValue(), orderType.getName());
        }
    }

    public static final Map<String, String> PURCHARSE_STATUS_MAP = new LinkedHashMap<String, String>();
    static {
        final OrderStatus[] values = OrderStatus.values();
        for (final OrderStatus orderType : values) {
            PURCHARSE_STATUS_MAP.put(orderType.getStatus().toString(), orderType.getName());
        }
    }

    /**
     * 费用类型
     */
    public static final Map<String, String> COST_TYPE_MAP = new LinkedHashMap<String, String>();
    static {
        final CostType[] values = CostType.values();
        for (final CostType costType : values) {
            COST_TYPE_MAP.put(costType.getValue(), costType.getName());
        }
    }

    /** 供应商列表的tabId */
    public static final String SP_SUPPLIERLIST_TAB_ID = "mainIframe_tabli_22";
    /** 合同列表的tabId */
    public static final String SP_CONTRACTLIST_TAB_ID = "mainIframe_tabli_23";
    /** 报价单列表的tabId */
    public static final String SP_QUATATIONLIST_TAB_ID = "mainIframe_tabli_24";
    /** 采购单列表的tabId */
    public static final String SP_PURCHASEPURCHARSELIST_TAB_ID = "mainIframe_tabli_25";
    /** 采购退货单列表的tabId */
    public static final String SP_PURCHASEPURCHARSEBACKLIST_TAB_ID = "mainIframe_tabli_26";
    /** 代销单列表的tabId */
    public static final String SP_SELLPURCHARSELIST_TAB_ID = "mainIframe_tabli_27";
    /** 代销退货单列表的tabId */
    public static final String SP_SELLPURCHARSEBACKLIST_TAB_ID = "mainIframe_tabli_28";
    /** 仓库预约单列表的tabId */
    public static final String SP_WAREHOUSEPURCHARSELIST_TAB_ID = "mainIframe_tabli_29";

    private SupplierConstant() {
    }

}
