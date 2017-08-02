package com.tp.common.vo.supplier.entry;

import org.aspectj.apache.bcel.generic.ReturnaddressType;

/**
 * {供应商类型} <br>
 * Create on : 2014年12月21日 下午2:11:18<br>
 *
 * @author szy
 * @version 0.0.1
 */
public enum SupplierType {

    PURCHASE("自营", "Purchase", "采购入仓类型的供应商"),

    SELL("代销", "sell", "入仓代发类型的供应商"),

    ASSOCIATE("代发", "Associate", "直接发货类型的商家"),

    MAIN("主供应商", "Main", "父供应商，可绑定不同类型的多个子供应商，多用于数据统计分析");

    private String name;

    private String value;

    private String explanation;

    private SupplierType(final String name, final String value, final String explanation) {
        this.name = name;
        this.value = value;
        this.explanation = explanation;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getExplanation() {
        return explanation;
    }
    
    /**
     * 是否是西客商城
     * 
     * @param supplierType
     * @return
     */
    public static Boolean isXg(String supplierType){
    	if(PURCHASE.getValue().equals(supplierType) || SELL.getValue().equals(supplierType)){
    		return true;
    	} else if(ASSOCIATE.getValue().equals(supplierType)) {
    		return false;
    	} else {
    		return null;
    	}
    }
        
    public static Boolean checkSupplierType(String supplierType){
    	if (supplierType.equals(PURCHASE.getValue())) {
			return true;
		}else if (supplierType.equals(SELL.getValue())) {
			return true;			
		}else if (supplierType.equals(ASSOCIATE.getValue())) {
			return true;
		}else if (supplierType.equals(MAIN.getValue())){
			return true;
		}    	    	
    	return false;
    }

    public static String getNameByValue(String value){
        if (value == null) return "";
        for(SupplierType type: SupplierType.values()){
            if(type.getValue().equals(value)) return type.getName();
        }
        return "";
    }
  
}
