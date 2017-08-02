package com.tp.common.vo.supplier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tp.common.vo.supplier.entry.ContractPartyA;
import com.tp.common.vo.supplier.entry.ContractTemplate;
import com.tp.common.vo.supplier.entry.SalesChannels;

/**
 * 合同常量
 */
public final class ConstractConstant {

    /**
     * 甲方联系地址
     */
    public static final String PART_A_LINK_ADDRESS = "杭州市下城区石桥路272号";

    /**
     * 合同主体类型
     */
    public static final Map<String, String> CONTRACT_PARTY_A_MAP = new LinkedHashMap<String, String>();
    static {
        final ContractPartyA[] values = ContractPartyA.values();
        for (final ContractPartyA contractPartyA : values) {
            CONTRACT_PARTY_A_MAP.put(contractPartyA.getValue(), contractPartyA.getName());
        }
    }
    
    /**
     * 银行名称
     */
    public static final Map<String, String> BANK_NAME_PARTY_A_MAP = new LinkedHashMap<String, String>();
    static {
    	BANK_NAME_PARTY_A_MAP.put(ContractPartyA.HZSG.getValue(), "中国民生银行");
    }
    /**
     * 银行账户名称
     */
    public static final Map<String, String> BANK_ACCOUNT_NAME_PARTY_A_MAP = new LinkedHashMap<String, String>();
    static {
    	BANK_ACCOUNT_NAME_PARTY_A_MAP.put(ContractPartyA.HZSG.getValue(), ContractPartyA.HZSG.getName());
    }
    /**
     * 银行账户
     */
    public static final Map<String, String> BANK_ACCOUNT__PARTY_A_MAP = new LinkedHashMap<String, String>();
    static {
    	BANK_ACCOUNT__PARTY_A_MAP.put(ContractPartyA.HZSG.getValue(), "695636141");
    }
    

    /** 销售渠道 */
    public static final Map<String, String> SALES_WAY_MAP = new LinkedHashMap<String, String>();
    static {
        final SalesChannels[] salesWays = SalesChannels.values();
        for (final SalesChannels sw : salesWays) {
            SALES_WAY_MAP.put(sw.getValue() + "", sw.getName());
        }
    }

    /** 合同模板 */
    public static final Map<String, String> CONTRACT_TEMPLATE_MAP = new HashMap<String, String>();
    static {
        final ContractTemplate[] templates = ContractTemplate.values();
        for (final ContractTemplate template : templates) {
            CONTRACT_TEMPLATE_MAP.put(template.getKey(), template.getName());
        }
    }

    /** 合同大标题 */
    public static final Map<String, String> CONTRACT_TITLE_MAP = new HashMap<String, String>();
    static {
        final ContractTemplate[] templates = ContractTemplate.values();
        for (final ContractTemplate template : templates) {
            CONTRACT_TITLE_MAP.put(template.getKey(), template.getTitle());
        }
    }

    /** 合同大标题 */
    public static final Map<String, String> CONTRACT_TEMPLATE_FILE_MAP = new HashMap<String, String>();
    static {
        final ContractTemplate[] templates = ContractTemplate.values();
        for (final ContractTemplate template : templates) {
            CONTRACT_TEMPLATE_FILE_MAP.put(template.getKey(), template.getTemplateFile());
        }
    }

    private ConstractConstant() {
    }

}
