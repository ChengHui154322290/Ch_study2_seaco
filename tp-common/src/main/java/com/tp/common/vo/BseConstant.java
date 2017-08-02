package com.tp.common.vo;


/**
 * 基础数据常量
 * @author szy
 *
 */
public final class BseConstant {

	/**地区类型为国家**/
	public  final static Integer DISTRICT_COUNTRY_TYPE = 2;
	/**
	 * 地址级别
	 * @author szy
	 *0-顶级 1-大洲 2-国家 3-政区 4-省 5-市 6-区 7-街道
	 */
	public enum DISTRICT_LEVEL{
		/**全球0*/
		GLOBAL(0,"全球"),	/**大洲1*/
		CONTINENT(1,"大洲"),/**国家2*/
		CONTRY(2,"国家"),	/**中国大区3*/
		REGION(3,"中国大区"),	/**省份4*/
		PROVINCE(4,"省份"),/**城市5*/
		CITY(5,"城市"),/**区县6*/
		AREA(6,"区县"),/**街道、乡镇7*/
		STREET(7,"街道、乡镇");
		
		public Integer code;
		public String cnName;
		
		DISTRICT_LEVEL(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			for(DISTRICT_LEVEL entry:DISTRICT_LEVEL.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
				}
			}
			return null;
		}
		
		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getCnName() {
			return cnName;
		}

		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	/**
	 * 
	 * @author szy
	 *
	 */
	public enum DictionaryCode {

		c1001("单位","c1001"),
		c1002("经营资质","c1002"),
		c1003("适用年龄","c1003");
		private  String name;
		
		private  String code;
		
		private DictionaryCode(String name, String code) {
	        this.name=name;
	        this.code=code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}
	
	/**
	 * 进口关税
	 * @author szy
	 *
	 */
	public enum TaxRateEnum {
		
		TARRIFRATE ("行邮税","tarrifrate"),
		
		PURRATE    ("进口增值税税率 ","purrate"),
		
		PRIMERATE    ("进项税率 ","primerate"),
		
		SALERATE   ("销项税率","salerate"),
		ADDEDVALUE("增值税率","addedValue"),
		EXCISE("消费税率","excise"),
		CUSTOMS("关税率","customs");
		
		private  String name;
		
		private  String type;
		
		private TaxRateEnum(String name, String type) {
	        this.name=name;
	        this.type=type;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}
		
		 public static String getName(String type) {
	         for (TaxRateEnum c : TaxRateEnum.values()) {
	             if (c.getType().equals(type)) {
	                 return c.name;
	             }
	         }
	         return null;
	     }
	    

	}
}
